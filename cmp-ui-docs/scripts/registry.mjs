import { readFile } from "node:fs/promises";
import path from "node:path";
import { parse } from "@babel/parser";

function assertInside(file, repository) {
  const relative = path.relative(repository, file);
  if (
    relative === ".." ||
    relative.startsWith(`..${path.sep}`) ||
    path.isAbsolute(relative)
  ) {
    throw new Error(`저장소 밖의 레지스트리 경로입니다: ${file}`);
  }
}

export async function collectRegistry(
  manifestPath,
  repository,
  ancestors = [],
) {
  const absolute = path.resolve(manifestPath);
  assertInside(absolute, repository);
  if (ancestors.includes(absolute))
    throw new Error(`레지스트리 include 순환 참조: ${absolute}`);
  const manifest = JSON.parse(await readFile(absolute, "utf8"));
  const directory = path.dirname(absolute);
  const items = [];
  for (const item of manifest.items ?? []) {
    const files = [];
    for (const file of item.files ?? []) {
      const absolutePath = path.resolve(directory, file.path);
      assertInside(absolutePath, repository);
      files.push({
        ...file,
        absolutePath,
        content: await readFile(absolutePath, "utf8"),
      });
    }
    items.push({ ...item, files, manifestPath: absolute });
  }
  for (const included of manifest.include ?? []) {
    items.push(
      ...(await collectRegistry(path.resolve(directory, included), repository, [
        ...ancestors,
        absolute,
      ])),
    );
  }
  return items;
}

export function validateRegistry(items) {
  const byName = new Map();
  for (const item of items) {
    if (byName.has(item.name))
      throw new Error(`중복 레지스트리 이름: ${item.name}`);
    byName.set(item.name, item);
  }
  const visited = new Set();
  function visit(name, parents) {
    if (parents.includes(name))
      throw new Error(
        `레지스트리 의존성 순환 참조: ${[...parents, name].join(" → ")}`,
      );
    if (visited.has(name)) return;
    const item = byName.get(name);
    for (const dependency of item.registryDependencies ?? []) {
      if (!dependency.startsWith("@ienlab/")) continue;
      const dependencyName = dependency.slice("@ienlab/".length);
      if (!byName.has(dependencyName))
        throw new Error(
          `${name}: 내부 의존성 ${dependencyName} 구현이 없습니다.`,
        );
      visit(dependencyName, [...parents, name]);
    }
    visited.add(name);
  }
  for (const item of items) visit(item.name, []);
}

export function registryModuleAliases(items) {
  const aliases = {};
  const owners = {};
  const prefixes = {
    "@ui/": "@/components/ui/",
    "@components/": "@/components/",
    "@hooks/": "@/hooks/",
    "@lib/": "@/lib/",
  };
  for (const item of items) {
    for (const file of item.files) {
      if (!file.target) continue;
      for (const [placeholder, prefix] of Object.entries(prefixes)) {
        if (!file.target.startsWith(placeholder)) continue;
        const target =
          prefix +
          file.target.slice(placeholder.length).replace(/\.(?:tsx?|jsx?)$/, "");
        if (aliases[target] && aliases[target] !== file.absolutePath) {
          throw new Error(
            `설치 대상 중복: ${target} (${owners[target]}, ${item.name})`,
          );
        }
        aliases[target] = file.absolutePath;
        owners[target] = item.name;
      }
    }
  }
  return aliases;
}

export function sourceApi(source, filename) {
  let file;
  try {
    file = parse(source, {
      sourceType: "module",
      plugins: ["typescript", "jsx"],
    });
  } catch (error) {
    throw new Error(`${filename}: ${error.message}`, { cause: error });
  }
  const entries = [];
  const names = new Set();
  const text = (node) => (node ? source.slice(node.start, node.end) : "");
  for (const statement of file.program.body) {
    const exported = statement.type === "ExportNamedDeclaration";
    if (exported)
      for (const specifier of statement.specifiers)
        names.add(specifier.exported.name ?? specifier.exported.value);
    const declaration = exported ? statement.declaration : statement;
    if (!declaration) continue;
    if (declaration.type === "FunctionDeclaration" && declaration.id) {
      if (exported) names.add(declaration.id.name);
      entries.push({
        name: declaration.id.name,
        signature: `${declaration.id.name}(${declaration.params.map(text).join(", ")})`,
        kind: "component",
        props: [],
      });
    }
    if (
      ["TSInterfaceDeclaration", "TSTypeAliasDeclaration"].includes(
        declaration.type,
      ) &&
      exported
    ) {
      names.add(declaration.id.name);
      const members =
        declaration.body?.body ?? declaration.typeAnnotation?.members ?? [];
      const props = members
        .filter((member) => member.type === "TSPropertySignature")
        .map((member) => ({
          name: text(member.key),
          type: text(member.typeAnnotation?.typeAnnotation) || "unknown",
          optional: Boolean(member.optional),
        }));
      entries.push({
        name: declaration.id.name,
        signature: text(declaration),
        kind: "type",
        props,
      });
    }
    if (declaration.type === "VariableDeclaration") {
      for (const variable of declaration.declarations) {
        const name = variable.id.name;
        if (!name) continue;
        if (exported) names.add(name);
        entries.push({
          name,
          signature: `${name}${variable.id.typeAnnotation ? text(variable.id.typeAnnotation) : ""}`,
          kind: "value",
          props: [],
        });
      }
    }
  }
  return {
    names: [...names],
    entries: entries.filter((entry) => names.has(entry.name)),
    client: file.program.directives.some(
      (directive) => directive.value.value === "use client",
    ),
  };
}

export function createWebCatalog(items, cmpCatalog, repository) {
  return items
    .filter((item) =>
      ["registry:ui", "registry:block", "registry:component"].includes(
        item.type,
      ),
    )
    .map((item) => {
      const meta = item.meta ?? {};
      const cmpIds = meta.cmpIds ?? (item.name === "button" ? ["button"] : []);
      const cmp = cmpCatalog.find((component) => cmpIds.includes(component.id));
      const mainFile = item.files.find((file) => /\.tsx?$/.test(file.path));
      if (!mainFile)
        throw new Error(`${item.name}: 공개 컴포넌트 소스가 없습니다.`);
      const info = sourceApi(mainFile.content, mainFile.absolutePath);
      const source = path
        .relative(repository, mainFile.absolutePath)
        .split(path.sep)
        .join("/");
      const preview = meta.preview
        ? path
            .relative(
              repository,
              path.resolve(path.dirname(item.manifestPath), meta.preview),
            )
            .split(path.sep)
            .join("/")
        : "";
      return {
        id: item.name,
        name:
          item.title ??
          cmp?.name ??
          item.name
            .split("-")
            .map((word) => word[0].toUpperCase() + word.slice(1))
            .join(" "),
        category: meta.category ?? cmp?.category ?? "웹 컴포넌트",
        description: item.description ?? cmp?.description ?? "",
        api: info.names.length ? info.names : (meta.exports ?? []),
        module: "React",
        packageName: `@/components/ui/${item.name}`,
        source,
        sample: source,
        cmpId: cmp?.id ?? "",
        cmpIds,
        preview,
        example:
          meta.example ??
          (item.name === "button"
            ? 'import { Button } from "@/components/ui/button"\n\n<Button variant="fill" tone="brand">저장</Button>'
            : ""),
        installCommand: `npx shadcn@latest add @ienlab/${item.name}`,
        dependencies: item.registryDependencies ?? [],
        packages: item.dependencies ?? [],
        client: info.client,
        limitations: [
          ...(Array.isArray(meta.limitations)
            ? meta.limitations
            : typeof meta.limitations === "string"
              ? [meta.limitations]
              : []),
          ...(meta.constraints ?? []),
        ],
        apiEntries: info.entries,
        manifest: {
          ...item,
          files: item.files.map(({ absolutePath, content, ...file }) => file),
          manifestPath: undefined,
        },
      };
    })
    .sort((left, right) => left.name.localeCompare(right.name));
}
