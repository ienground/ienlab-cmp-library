import test from "node:test";
import assert from "node:assert/strict";
import { mkdtemp, writeFile, mkdir, rm, readFile } from "node:fs/promises";
import { tmpdir } from "node:os";
import path from "node:path";
import { fileURLToPath } from "node:url";
import {
  collectRegistry,
  validateRegistry,
  createWebCatalog,
  sourceApi,
} from "./registry.mjs";
import { catalog } from "../web/catalog.js";
import { resolveIenTone } from "../../cmp-ui-web-registry/lib/ien-theme.ts";
import { createPlaygroundCatalog, instrumentPlayground } from "./playground.mjs";

const repository = fileURLToPath(new URL("../../", import.meta.url));

test("단독 Button이 연속 코너를 초기화하고 ghost variant는 테두리를 표시하지 않는다", async () => {
  const source = await readFile(path.join(repository, "cmp-ui-web-registry/ui/button.tsx"), "utf8");
  assert.match(source, /<IenContinuousCorners \/>/);
  assert.match(source, /bg-clip-border/);
  assert.doesNotMatch(source, /bg-clip-padding/);
  const context = await readFile(path.join(repository, "cmp-ui-web-registry/lib/ien-theme-context.tsx"), "utf8");
  assert.match(context, /export function IenContinuousCorners\(\) \{\s*useEffect\(\(\) => observeIenContinuousCorners\(\), \[\]\)/);
  const tokens = JSON.parse(await readFile(path.join(repository, "cmp-ui-web-registry/tokens.json"), "utf8"));
  assert.equal(tokens.components["@layer components"][".ien-clear"]["border-color"], "transparent");
  const items = await collectRegistry(path.join(repository, "registry.json"), repository);
  const theme = items.find((item) => item.name === "ien-theme");
  assert.ok(theme.dependencies.includes("@lisse/core@0.7.3"));
  assert.ok(theme.files.some((file) => file.path.endsWith("ien-continuous-corners.ts")));
});

test("모든 웹 컴포넌트의 Playground와 실제 variant·상속 속성을 추출한다", async () => {
  const items = await collectRegistry(path.join(repository, "registry.json"), repository);
  const previews = Object.fromEntries(await Promise.all(items.filter((item) => item.meta?.preview).map(async (item) => [item.name, await readFile(path.resolve(path.dirname(item.manifestPath), item.meta.preview), "utf8")])));
  const definitions = createPlaygroundCatalog(items, previews);
  assert.equal(Object.keys(definitions).length, 52);
  const button = definitions.button.targets[0];
  assert.equal(button.name, "Button");
  assert.deepEqual(button.props.find((prop) => prop.name === "tone").options, ["neutral", "brand", "success", "warning", "danger", "info"]);
  assert.deepEqual(button.props.find((prop) => prop.name === "variant").options, ["fill", "weak", "line", "ghost"]);
  assert.equal(button.props.find((prop) => prop.name === "disabled").kind, "boolean");
  assert.equal(button.props.find((prop) => prop.name === "useGradient").defaultValue, true);
  assert.equal(definitions["number-field"].targets[0].props.find((prop) => prop.name === "min").kind, "number");
  assert.equal(definitions.cascader.targets[0].props.find((prop) => prop.name === "multiple").kind, "boolean");
  assert.equal(definitions["data-grid"].targets[0].props.find((prop) => prop.name === "table").kind, "reference");
  for (const definition of Object.values(definitions)) {
    assert.ok(definition.targets.length);
    assert.ok(definition.edits.every((edit) => definition.targets.some((target) => target.key === edit.key)), "코드 조정 대상이 누락되었습니다.");
    for (const target of definition.targets) for (const prop of target.props) {
      if (prop.kind === "select") assert.ok(prop.options.length, `${target.name}.${prop.name}`);
    }
  }
});

test("문서 프리뷰의 실제 JSX 컴포넌트만 Playground에 연결한다", () => {
  const source = 'import { Button as Action, buttonVariants, type ButtonProps } from "@/components/ui/button"; export default function Example(){return <Action>실행</Action>}';
  const result = instrumentPlayground(source, "/web/playground.tsx");
  assert.match(result, /Button as __playground_Action/);
  assert.match(result, /const Action = __playgroundComponent/);
  assert.doesNotMatch(result, /__playground_buttonVariants|__playground_ButtonProps/);
  assert.deepEqual(sourceApi(result, "playground.tsx").names, []);
});

test("기존 variant를 CMP tone으로 해석하고 명시한 tone을 우선한다", () => {
  assert.equal(resolveIenTone(undefined, "destructive"), "danger");
  assert.equal(resolveIenTone(undefined, "success-light"), "success");
  assert.equal(resolveIenTone(undefined, "warning-outline"), "warning");
  assert.equal(resolveIenTone(undefined, "secondary"), "neutral");
  assert.equal(resolveIenTone("info", "destructive"), "info");
  assert.equal(resolveIenTone(undefined, "default"), undefined);
});

test("모든 UI 설치에 CMP 테마와 라이트·다크 색상, 그라데이션을 포함한다", async () => {
  const items = await collectRegistry(path.join(repository, "registry.json"), repository);
  const theme = items.find((item) => item.name === "ien-theme");
  assert.ok(theme);
  for (const item of items.filter((item) => item.type === "registry:ui")) {
    assert.ok(item.registryDependencies.includes("@ienlab/ien-theme"), item.name);
  }
  for (const tone of ["neutral", "brand", "success", "warning", "danger", "info"]) {
    assert.ok(theme.cssVars.light[`ien-${tone}`], tone);
    assert.ok(theme.cssVars.dark[`ien-${tone}`], tone);
    assert.ok(theme.css["@layer components"][`:where([data-ien-tone="${tone}"])`], tone);
  }
  assert.match(theme.css["@layer components"][".ien-fill"]["background-image"], /18%.*36%/);
  assert.equal(theme.css["@layer components"][':where([data-ien-gradient="false"])']["--ien-gradient"], "none");
  for (const [name, rule] of Object.entries(theme.css["@layer components"])) {
    if (name.includes("data-ien-typography")) {
      assert.equal(rule["font-size"], undefined, name);
      assert.equal(rule["line-height"], undefined, name);
    }
  }
});
async function fixture(run) {
  const directory = await mkdtemp(path.join(tmpdir(), "ien-registry-test-"));
  try {
    await run(directory);
  } finally {
    await rm(directory, { recursive: true, force: true });
  }
}

test("레지스트리 include를 재귀적으로 읽고 원본 기준 파일 경로를 보존한다", async () => {
  await fixture(async (directory) => {
    await mkdir(path.join(directory, "parts"));
    await writeFile(
      path.join(directory, "registry.json"),
      JSON.stringify({ include: ["parts/items.json"] }),
    );
    await writeFile(
      path.join(directory, "parts/items.json"),
      JSON.stringify({
        items: [
          {
            name: "example",
            type: "registry:ui",
            files: [
              {
                path: "example.tsx",
                target: "@ui/example.tsx",
                type: "registry:ui",
              },
            ],
          },
        ],
      }),
    );
    await writeFile(
      path.join(directory, "parts/example.tsx"),
      "export function Example() { return null }",
    );
    const items = await collectRegistry(
      path.join(directory, "registry.json"),
      directory,
    );
    assert.equal(items.length, 1);
    assert.equal(
      items[0].files[0].absolutePath,
      path.join(directory, "parts/example.tsx"),
    );
    assert.equal(
      items[0].manifestPath,
      path.join(directory, "parts/items.json"),
    );
  });
});

test("중복 이름, 누락된 내부 의존성, 레지스트리 순환 참조를 거부한다", async () => {
  assert.throws(() => validateRegistry([{ name: "a" }, { name: "a" }]), /중복/);
  assert.throws(
    () =>
      validateRegistry([
        { name: "a", registryDependencies: ["@ienlab/missing"] },
      ]),
    /missing/,
  );
  assert.throws(
    () =>
      validateRegistry([
        { name: "a", registryDependencies: ["@ienlab/b"] },
        { name: "b", registryDependencies: ["@ienlab/a"] },
      ]),
    /순환/,
  );
  await fixture(async (directory) => {
    await writeFile(
      path.join(directory, "registry.json"),
      JSON.stringify({ include: ["registry.json"] }),
    );
    await assert.rejects(
      collectRegistry(path.join(directory, "registry.json"), directory),
      /순환/,
    );
  });
});

test("포함 파일이 저장소 밖으로 벗어나지 않게 한다", async () => {
  await fixture(async (directory) => {
    await writeFile(
      path.join(directory, "registry.json"),
      JSON.stringify({ include: ["../private.json"] }),
    );
    await assert.rejects(
      collectRegistry(path.join(directory, "registry.json"), directory),
      /저장소/,
    );
  });
});

test("REUI 기본 컴포넌트와 필수 의존성을 하나의 레지스트리로 제공한다", async () => {
  const items = await collectRegistry(path.join(repository, "registry.json"), repository);
  validateRegistry(items);
  const expected = "alert autocomplete badge cascader code-block color-picker data-grid date-selector emoji-picker event-calendar file-type filters frame gantt icon-stack icon-tile kanban number-field phone-input rating scrollspy signature-pad sortable stepper time-picker timeline tree".split(" ");
  const components = items.filter((item) =>
    item.meta?.upstream?.repository === "https://github.com/keenthemes/reui" &&
    item.files.some((file) => file.path.startsWith("reui/")),
  );
  assert.deepEqual(components.map((item) => item.name).sort(), expected.sort());
  assert.ok(items.some((item) => item.name === "button"));
  assert.ok(items.some((item) => item.name === "use-mobile"));
  assert.ok(items.every((item) => item.name !== "tokens"));
  for (const item of components) {
    assert.match(item.meta.upstream.revision, /^[a-f0-9]{40}$/);
    assert.equal(item.meta.upstream.style, "radix-nova");
    assert.ok(item.meta.exports.every((name) => typeof name === "string"));
  }
});

test("모든 공개 웹 컴포넌트에 REUI 프리뷰와 올바른 설치 경로를 연결한다", async () => {
  const items = await collectRegistry(path.join(repository, "registry.json"), repository);
  const web = createWebCatalog(items, catalog, repository);
  assert.equal(web.length, items.filter((item) => item.type === "registry:ui").length);
  for (const item of web) {
    assert.ok(item.preview, `${item.id}: 프리뷰 누락`);
    assert.ok(item.example, `${item.id}: 사용 예시 누락`);
    assert.ok(item.api.length > 0, `${item.id}: 공개 API 누락`);
    assert.equal(item.cmpId, "");
    assert.equal(item.installCommand, `npx shadcn@latest add @ienlab/${item.id}`);
    assert.match(await readFile(path.join(repository, item.preview), "utf8"), /export default/);
  }
  assert.equal(web.find((item) => item.id === "alert").packageName, "@/components/reui/alert");
  assert.equal(web.find((item) => item.id === "button").packageName, "@/components/ui/button");
});

test("설치되는 소스가 내부 파일과 외부 패키지 의존성을 빠짐없이 선언한다", async () => {
  const items = await collectRegistry(path.join(repository, "registry.json"), repository);
  const { registryModuleAliases } = await import("./registry.mjs");
  const aliases = registryModuleAliases(items);
  const owners = new Map(items.flatMap((item) => item.files.map((file) => [file.absolutePath, item.name])));
  const problems = [];
  for (const item of items) for (const file of item.files) {
    assert.doesNotMatch(file.content, /@\/app\/|@\/registry\/|@\/lib\/ien-utils|@ienlab\/tokens/, file.path);
    assert.doesNotMatch(file.path, /^(?:cmp-content|cmp-interactive|shadcn|registry)\//);
    for (const name of sourceApi(file.content, file.absolutePath).imports) {
      if (name.startsWith(".")) continue;
      if (name.startsWith("@/")) {
        const owner = owners.get(aliases[name]);
        if (!owner) problems.push(`${item.name}: ${name} 설치 대상 없음`);
        else if (owner !== item.name && !item.registryDependencies?.includes(`@ienlab/${owner}`)) problems.push(`${item.name}: @ienlab/${owner} 의존성 누락`);
      } else {
        const dependency = name.startsWith("@") ? name.split("/").slice(0, 2).join("/") : name.split("/")[0];
        if (!["react", "react-dom"].includes(dependency) && !item.dependencies?.some((entry) => entry === dependency || entry.startsWith(`${dependency}@`))) problems.push(`${item.name}: ${dependency} 패키지 누락`);
      }
    }
  }
  assert.deepEqual(problems, []);
  const calendar = items.find((item) => item.name === "calendar");
  assert.ok(calendar.dependencies.some((entry) => entry.startsWith("react-day-picker@^9.")));
});
