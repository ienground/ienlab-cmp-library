import { mkdir, readFile, writeFile } from "node:fs/promises";
import path from "node:path";
import { tmpdir } from "node:os";
import { fileURLToPath } from "node:url";
import { parse } from "@babel/parser";

const revision = process.argv[2] ?? "067aa0dc7f8bbfec1cd76fc5ad31f842be5f569a";
if (!/^[a-f0-9]{40}$/.test(revision)) throw new Error("REUI 커밋 SHA를 지정하세요.");
const style = "radix-nova";
const upstream = `https://raw.githubusercontent.com/keenthemes/reui/${revision}/`;
const repository = fileURLToPath(new URL("../../", import.meta.url));
const output = path.join(repository, "cmp-ui-web-registry");
try {
  const current = JSON.parse(await readFile(path.join(output, "registry.json"), "utf8"));
  if (current.items.some((item) => item.meta?.ienDesign)) {
    throw new Error("CMP 디자인이 적용된 소스입니다. REUI 갱신은 별도 경로에서 가져온 뒤 변경 사항을 병합하세요.");
  }
} catch (error) {
  if (error.code !== "ENOENT") throw error;
}
const cache = path.join(tmpdir(), `ienlab-reui-${revision}`);
await mkdir(cache, { recursive: true });

async function download(file) {
  const cached = path.join(cache, file);
  try { return await readFile(cached, "utf8"); } catch (error) {
    if (error.code !== "ENOENT") throw error;
  }
  const response = await fetch(upstream + file);
  if (!response.ok) throw new Error(`REUI 소스 다운로드 실패: ${file} (${response.status})`);
  const text = await response.text();
  await mkdir(path.dirname(cached), { recursive: true });
  await writeFile(cached, text);
  return text;
}

async function parallel(values, work) {
  let next = 0;
  await Promise.all(Array.from({ length: Math.min(8, values.length) }, async () => {
    while (next < values.length) await work(values[next++]);
  }));
}

function literal(node) {
  if (node.type === "ArrayExpression") return node.elements.map(literal);
  if (node.type === "ObjectExpression") return Object.fromEntries(node.properties.map((property) => {
    if (property.type !== "ObjectProperty" || property.computed) throw new Error("지원하지 않는 REUI 레지스트리 선언입니다.");
    return [property.key.name ?? property.key.value, literal(property.value)];
  }));
  if (["StringLiteral", "NumericLiteral", "BooleanLiteral"].includes(node.type)) return node.value;
  if (node.type === "TemplateLiteral" && node.expressions.length === 0) return node.quasis[0].value.cooked;
  if (node.type === "NullLiteral") return null;
  throw new Error(`지원하지 않는 레지스트리 값: ${node.type}`);
}

function registryArray(source, name) {
  const ast = parse(source, { sourceType: "module", plugins: ["typescript"] });
  for (const statement of ast.program.body) {
    const declaration = statement.declaration ?? statement;
    const variable = declaration.declarations?.find((item) => item.id.name === name);
    if (variable) return literal(variable.init);
  }
  throw new Error(`REUI ${name} 레지스트리가 없습니다.`);
}

const index = JSON.parse(await download(`public/r/styles/${style}/registry.json`));
const originals = index.items.filter((item) => !item.name.startsWith("c-"));
const foundation = [
  ...registryArray(await download("registry/bases/radix/ui/_registry.ts"), "ui"),
  ...registryArray(await download("registry/bases/radix/hooks/_registry.ts"), "hooks"),
];
const foundationByName = new Map(foundation.map((item) => [item.name, item]));
const styleSource = await download("registry/styles/style-nova.css");
const styles = new Map(Array.from(styleSource.matchAll(/\.(cn-[\w-]+)\s*\{([^{}]*)\}/g), ([, name, block]) => [
  name, Array.from(block.matchAll(/@apply\s+([^;]+);/g), (match) => match[1]).join(" "),
]));
styles.set("cn-font-heading", "font-sans");

const modules = new Map();
const roots = new Set();
const sourceFiles = new Map();
function localTarget(target) {
  if (target.startsWith("components/reui/")) return target.slice("components/".length);
  if (target.startsWith("components/ui/")) return target.slice("components/".length);
  if (target.startsWith("hooks/") || target.startsWith("lib/")) return target;
  throw new Error(`지원하지 않는 REUI 설치 경로: ${target}`);
}
function installTarget(file) {
  if (file.startsWith("reui/")) return `@components/${file}`;
  if (file.startsWith("ui/")) return `@ui/${file.slice(3)}`;
  if (file.startsWith("hooks/")) return `@hooks/${file.slice(6)}`;
  return `@lib/${file.slice(4)}`;
}
function addFiles(item, files, isFoundation = false) {
  for (const file of files) {
    const local = isFoundation ? file.path : localTarget(file.target);
    let content = file.content;
    if (isFoundation) {
      content = content.replace(/\bcn-[\w-]+\b/g, (name) => {
        return styles.get(name) ?? "";
      });
      content = content.replace(/\bdata-(open|closed):/g, (_, state) => `data-[state=${state}]:`)
        .replace(/\bdata-(horizontal|vertical):/g, (_, value) => `data-[orientation=${value}]:`)
        .replace(/\bdata-checked:/g, "data-[state=checked]:")
        .replace(/\bdata-unchecked:/g, "data-[state=unchecked]:");
    }
    content = adaptTypes(content).replaceAll("@/app/(create)/components/icon-placeholder", "@/lib/reui-icons")
      .replace(/@\/registry\/bases\/radix\/ui\//g, "@/components/ui/")
      .replace(/@\/registry\/bases\/radix\/hooks\//g, "@/hooks/");
    sourceFiles.set(local, content);
    file.local = local;
  }
  modules.set(item.name, { ...item, files });
}

function adaptTypes(content) {
  return content.replace(/import\s*\{[^}]+\}\s*from\s*["'][^"']+["']/g, (declaration) => declaration.replace(/(?<!type\s)\b(RefObject|ComponentProps|ColumnDef|PaginationState|SortingState)\b/g, "type $1"));
}

await parallel(originals, async (item) => {
  const built = JSON.parse(await download(`public/r/styles/${style}/${item.name}.json`));
  addFiles(built, built.files);
  for (const file of built.files) {
    if (file.local.startsWith("reui/")) {
      const name = file.local.slice(5).split("/")[0].replace(/\.tsx?$/, "");
      roots.add(name);
    }
  }
});

async function addFoundation(name) {
  if (modules.has(name)) return;
  const item = foundationByName.get(name);
  if (!item) throw new Error(`REUI 기본 의존성이 없습니다: ${name}`);
  const files = await Promise.all(item.files.map(async (file) => ({
    ...file, content: await download(`registry/bases/radix/${file.path}`),
  })));
  addFiles(item, files, true);
}

let pending = [];
do {
  pending = [...new Set([...modules.values()].flatMap((item) => item.registryDependencies ?? [])
    .filter((name) => !name.startsWith("@") && !modules.has(name)))];
  await parallel(pending, addFoundation);
} while (pending.length > 0);

const previews = new Map();
async function addPreview(name) {
  if (previews.has(name)) return;
  const example = index.items.find((item) => item.name === `c-${name}-1`);
  if (!example) return;
  const built = JSON.parse(await download(`public/r/styles/${style}/${example.name}.json`));
  const files = built.files.map((file) => ({ ...file, local: `previews/${name}.tsx` }));
  if (files.length !== 1) throw new Error(`프리뷰 파일 구성을 확인하세요: ${name}`);
  let content = adaptTypes(files[0].content).replaceAll("@/app/(create)/components/icon-placeholder", "@/lib/reui-icons");
  const links = [...content.matchAll(/import\s+(\w+)\s+from\s+["']next\/link["'];?/g)];
  for (const [declaration, component] of links) content = content.replace(declaration, "").replace(new RegExp(`(<\\/?)${component}(?=[\\s>])`, "g"), "$1a");
  const ast = parse(content, { sourceType: "module", plugins: ["typescript", "jsx"] });
  if (!ast.program.body.some((node) => node.type === "ExportDefaultDeclaration")) content += "\nexport default Pattern;\n";
  sourceFiles.set(files[0].local, content);
  previews.set(name, { ...built, source: content, file: files[0].local });
  for (const dependency of built.registryDependencies ?? []) {
    if (!dependency.startsWith("@") && !modules.has(dependency)) await addFoundation(dependency);
  }
}
await parallel([...roots, ...[...modules.keys()].filter((name) => foundationByName.has(name) && !name.startsWith("use-"))], addPreview);

// 프리뷰에서 추가한 기본 컴포넌트의 의존성도 모두 포함한다.
do {
  pending = [...new Set([...modules.values()].flatMap((item) => item.registryDependencies ?? [])
    .filter((name) => !name.startsWith("@") && !modules.has(name)))];
  await parallel(pending, addFoundation);
} while (pending.length > 0);
await parallel([...modules.keys()].filter((name) => foundationByName.has(name) && !name.startsWith("use-")), addPreview);

const { sourceApi } = await import("./registry.mjs");
const moduleNames = new Map();
function owner(file) {
  if (file.startsWith("reui/")) return file.slice(5).split("/")[0].replace(/\.tsx?$/, "");
  return path.basename(file).replace(/\.tsx?$/, "");
}
for (const [file] of sourceFiles) {
  if (file.startsWith("previews/")) continue;
  const alias = file.startsWith("ui/") || file.startsWith("reui/") ? `@/components/${file}` : `@/${file}`;
  moduleNames.set(alias.replace(/\.tsx?$/, ""), owner(file));
}

const tabler = await import("@tabler/icons-react");
const icons = new Set(["IconSquareRounded"]);
function walk(node) {
  if (!node || typeof node !== "object") return;
  if (node.type === "JSXAttribute" && node.name?.name === "tabler" && node.value?.type === "StringLiteral" && node.value.value in tabler) icons.add(node.value.value);
  for (const value of Object.values(node)) {
    if (Array.isArray(value)) value.forEach(walk);
    else if (value && typeof value === "object") walk(value);
  }
}
for (const content of sourceFiles.values()) walk(parse(content, { sourceType: "module", plugins: ["typescript", "jsx"] }));
const names = [...icons].sort();
sourceFiles.set("lib/reui-icons.tsx", `import { ${names.join(", ")} } from "@tabler/icons-react";\nimport type { ComponentProps } from "react";\n\nconst icons = { ${names.join(", ")} };\ntype IconProps = ComponentProps<typeof IconSquareRounded> & { lucide?: string; tabler?: string; hugeicons?: string; phosphor?: string; remixicon?: string };\nexport function IconPlaceholder({ tabler, lucide: _lucide, hugeicons: _hugeicons, phosphor: _phosphor, remixicon: _remixicon, ...props }: IconProps) {\n  const Icon = tabler && Object.hasOwn(icons, tabler) ? icons[tabler as keyof typeof icons] : IconSquareRounded;\n  return <Icon {...props} />;\n}\n`);
moduleNames.set("@/lib/reui-icons", "reui-icons");

const items = new Map();
for (const module of modules.values()) {
  for (const file of module.files) {
    const name = owner(file.local);
    const item = items.get(name) ?? {
      name, type: module.type, title: module.name === name ? module.title : undefined,
      description: `REUI ${name} 컴포넌트입니다.`, dependencies: [], registryDependencies: [], files: [],
      meta: { category: file.local.startsWith("reui/") ? "REUI" : "기본 컴포넌트", cmpIds: [], upstream: { repository: "https://github.com/keenthemes/reui", revision, style }, exports: [] },
    };
    if (!item.files.some((entry) => entry.path === file.local)) item.files.push({ path: file.local, type: file.type, target: installTarget(file.local) });
    if (module.cssVars) item.cssVars = { ...item.cssVars, ...module.cssVars };
    if (module.css) item.css = { ...item.css, ...module.css };
    if (module.name === name && module.title) item.title = module.title;
    item.dependencies.push(...(module.dependencies ?? []).map((name) => {
      const versionAt = name.indexOf("@", name.startsWith("@") ? 1 : 0);
      return versionAt === -1 ? name : name.slice(0, versionAt);
    }));
    items.set(name, item);
  }
}
items.set("reui-icons", { name: "reui-icons", type: "registry:lib", dependencies: ["@tabler/icons-react"], files: [{ path: "lib/reui-icons.tsx", type: "registry:lib", target: "@lib/reui-icons.tsx" }] });

const upstreamPackage = JSON.parse(await download("package.json"));
const packageFile = path.join(repository, "cmp-ui-docs/package.json");
const packageJson = JSON.parse(await readFile(packageFile, "utf8"));
const packages = new Map();
for (const item of items.values()) {
  item.files.sort((a, b) => Number(path.basename(b.path).replace(/\.tsx?$/, "") === item.name) - Number(path.basename(a.path).replace(/\.tsx?$/, "") === item.name) || a.path.localeCompare(b.path));
  const dependencies = new Set(item.dependencies ?? []);
  const internal = new Set();
  for (const file of item.files) {
    const api = sourceApi(sourceFiles.get(file.path), file.path);
    if (/\bprocess\.env\b/.test(sourceFiles.get(file.path))) dependencies.add("@types/node");
    if (item.meta) item.meta.exports.push(...api.names);
    for (const name of api.imports) {
      if (name.startsWith(".")) continue;
      if (name.startsWith("@/")) {
        const target = moduleNames.get(name);
        if (!target) throw new Error(`설치할 수 없는 내부 import: ${name} (${file.path})`);
        if (target !== item.name) internal.add(`@ienlab/${target}`);
      } else {
        const dependency = name.startsWith("@") ? name.split("/").slice(0, 2).join("/") : name.split("/")[0];
        if (!["react", "react-dom"].includes(dependency)) dependencies.add(dependency);
      }
    }
  }
  item.dependencies = [...dependencies].sort().map((name) => `${name}@${packageJson.dependencies[name] ?? packageJson.devDependencies[name] ?? upstreamPackage.dependencies[name]}`);
  item.registryDependencies = [...internal].sort();
  for (const dependency of dependencies) {
    const version = packageJson.dependencies[dependency] ?? packageJson.devDependencies[dependency] ?? upstreamPackage.dependencies[dependency];
    if (!version || version.startsWith("workspace:")) throw new Error(`REUI 의존성 버전을 확인하세요: ${dependency}`);
    packages.set(dependency, version);
  }
  if (item.meta) {
    item.title = item.name.split("-").map((part) => part[0].toUpperCase() + part.slice(1)).join(" ");
    item.meta.exports = [...new Set(item.meta.exports)];
    const preview = previews.get(item.name);
    if (preview) { item.meta.preview = preview.file; item.meta.example = preview.source; }
  }
  if (item.cssVars?.light) {
    const colors = ["destructive-foreground", "success", "success-foreground", "info", "info-foreground", "warning", "warning-foreground", "invert", "invert-foreground"];
    item.cssVars.theme = { ...item.cssVars.theme, ...Object.fromEntries(colors.filter((name) => name in item.cssVars.light).map((name) => [`color-${name}`, `var(--${name})`])) };
  }
}
for (const preview of previews.values()) {
  for (const name of sourceApi(preview.source, preview.file).imports) {
    if (name.startsWith("@/")) {
      if (!moduleNames.has(name)) throw new Error(`프리뷰 내부 import가 없습니다: ${name} (${preview.file})`);
    } else if (!name.startsWith(".") && !["react", "react-dom"].includes(name)) {
      const dependency = name.startsWith("@") ? name.split("/").slice(0, 2).join("/") : name.split("/")[0];
      const version = upstreamPackage.dependencies[dependency];
      if (!version || version.startsWith("workspace:")) throw new Error(`프리뷰 의존성을 확인하세요: ${dependency}`);
      packages.set(dependency, version);
    }
  }
}

await mkdir(output, { recursive: true });
for (const [file, content] of sourceFiles) {
  const target = path.join(output, file);
  await mkdir(path.dirname(target), { recursive: true });
  await writeFile(target, content);
}
const sorted = [...items.values()].sort((a, b) => a.name.localeCompare(b.name));
await writeFile(path.join(output, "registry.json"), JSON.stringify({ $schema: "https://ui.shadcn.com/schema/registry.json", name: "ienlab", homepage: "https://cmp.ien.zone", items: sorted }, null, 2) + "\n");
await writeFile(path.join(output, "LICENSE.md"), (await download("LICENSE.md")).replace("Copyright (c) 2025 Keenthemes Inc", "Copyright (c) 2025 Keenthemes Inc\nCopyright (c) 2023 shadcn"));
packageJson.dependencies = Object.fromEntries(Object.entries({ ...Object.fromEntries([...packages].filter(([name]) => !packageJson.devDependencies[name])), ...packageJson.dependencies }).sort(([a], [b]) => a.localeCompare(b)));
await writeFile(packageFile, JSON.stringify(packageJson, null, 2) + "\n");
console.log(`REUI ${roots.size}개 기본 컴포넌트와 의존성 ${items.size}개 항목, 프리뷰 ${previews.size}개를 가져왔습니다. (${revision})`);
