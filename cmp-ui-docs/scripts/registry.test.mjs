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

const repository = fileURLToPath(new URL("../../", import.meta.url));
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
  const components = items.filter((item) => item.meta?.category === "REUI");
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
