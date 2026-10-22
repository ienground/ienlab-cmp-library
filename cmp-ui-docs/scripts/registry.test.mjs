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
const officialComponents =
  `accordion alert alert-dialog aspect-ratio attachment avatar badge breadcrumb bubble button button-group calendar card carousel chart checkbox code-block collapsible combobox command context-menu data-table date-picker dialog direction drawer dropdown-menu empty field hover-card input input-group input-otp item kbd label marker menubar message message-scroller native-select navigation-menu pagination popover progress questionnaire radio-group resizable scroll-area select separator sheet sidebar skeleton slider spinner switch table tabs textarea toast toggle toggle-group tooltip trace typography form sonner`.split(
    " ",
  );
const consolidatedComponents = {
  input: "text-field",
  textarea: "text-area",
  tabs: "tab",
  "radio-group": "radio-button",
  progress: "progress-bar",
  separator: "border",
  sheet: "bottom-sheet",
  drawer: "bottom-sheet",
  toast: "snackbar",
  sonner: "snackbar",
  spinner: "loader",
};

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

test("shadcn 기능과 CMP 문서의 모든 컴포넌트가 대표 항목으로 제공된다", async () => {
  const items = await collectRegistry(
    path.join(repository, "registry.json"),
    repository,
  );
  validateRegistry(items);
  const names = new Set(items.map((item) => item.name));
  assert.deepEqual(
    officialComponents.filter(
      (name) => !names.has(consolidatedComponents[name] ?? name),
    ),
    [],
  );
  const covered = new Set(
    items.flatMap(
      (item) => item.meta?.cmpIds ?? (item.name === "button" ? ["button"] : []),
    ),
  );
  assert.deepEqual(
    catalog.filter((item) => !covered.has(item.id)).map((item) => item.id),
    [],
  );
  for (const item of items.filter(
    (item) => item.type === "registry:ui" && item.name !== "button",
  )) {
    assert.ok(item.meta?.example, `${item.name} 사용 예시가 없습니다.`);
    assert.ok(item.meta?.preview, `${item.name} 실제 프리뷰가 없습니다.`);
    assert.ok(
      (
        await readFile(
          path.resolve(path.dirname(item.manifestPath), item.meta.preview),
          "utf8",
        )
      ).includes("export default"),
      `${item.name} 기본 프리뷰가 없습니다.`,
    );
  }
});

test("기능이 겹치는 설치 항목은 CMP 이름 아래로 통합한다", async () => {
  const items = await collectRegistry(
    path.join(repository, "registry.json"),
    repository,
  );
  const names = new Set(items.map((item) => item.name));
  for (const [duplicate, canonical] of Object.entries(consolidatedComponents)) {
    assert.equal(
      names.has(duplicate),
      false,
      `${duplicate}: 중복 등록이 남아 있습니다.`,
    );
    assert.equal(
      names.has(canonical),
      true,
      `${canonical}: 대표 항목이 없습니다.`,
    );
  }
  const snackbar = items.find((item) => item.name === "snackbar");
  assert.deepEqual(snackbar.meta.cmpIds, ["snackbar", "toast"]);
  assert.ok(snackbar.files.some((file) => file.target === "@ui/toast.tsx"));
  assert.ok(items.every((item) => !item.dependencies?.includes("sonner")));
});

test("웹 컴포넌트 이름은 CMP 이름과 일치하고 한글 제목을 사용하지 않는다", async () => {
  const items = await collectRegistry(
    path.join(repository, "registry.json"),
    repository,
  );
  const web = createWebCatalog(items, catalog, repository);
  for (const item of web) {
    assert.doesNotMatch(item.name, /[가-힣]/u, item.id);
    const cmp = catalog.find((component) => component.id === item.cmpId);
    if (cmp) {
      assert.equal(item.name, cmp.name, item.id);
      assert.equal(
        items.find((entry) => entry.name === item.id).title,
        cmp.name,
        item.id,
      );
    }
  }
  const bottomBar = items.find((item) => item.name === "bottom-bar");
  const renamed = createWebCatalog(
    [{ ...bottomBar, title: "하단 바" }],
    catalog,
    repository,
  );
  assert.equal(renamed[0].name, "BottomBar");
});

test("문서 카탈로그가 실제 소스·CMP 비교·설치 명령을 제공한다", async () => {
  const items = await collectRegistry(
    path.join(repository, "registry.json"),
    repository,
  );
  const web = createWebCatalog(items, catalog, repository);
  const button = web.find((item) => item.id === "button");
  assert.ok(button);
  assert.equal(button.installCommand, "npx shadcn@latest add @ienlab/button");
  assert.equal(button.cmpId, "button");
  assert.match(button.source, /ien-button\.tsx$/);
  assert.ok(web.every((item) => item.api.length > 0));
});

test("웹 타이포그래피는 CMP 단계 이름과 웹 글자 크기를 사용한다", async () => {
  const tokens = JSON.parse(await readFile(path.join(repository, "cmp-ui-web-registry/tokens.json"), "utf8"));
  assert.deepEqual(tokens.typography.body2, { fontSize: 14, lineHeight: 20, fontWeight: 400 });
  assert.deepEqual(tokens.typography.title1, { fontSize: 30, lineHeight: 36, fontWeight: 600 });
  assert.equal(tokens.button.radius, "var(--ien-radius-default)");
  assert.equal(tokens.control["radius-base"], "var(--ien-radius-default)");
});

test("설치되는 모든 소스가 외부 패키지와 내부 컴포넌트 의존성을 선언한다", async () => {
  const items = await collectRegistry(
    path.join(repository, "registry.json"),
    repository,
  );
  const { registryModuleAliases } = await import("./registry.mjs");
  const aliases = registryModuleAliases(items);
  const owners = new Map(
    items.flatMap((item) =>
      item.files.map((file) => [file.absolutePath, item.name]),
    ),
  );
  const problems = [];
  for (const item of items)
    for (const file of item.files) {
      for (const name of sourceApi(file.content, file.absolutePath).imports) {
        if (name.startsWith(".")) continue;
        if (name.startsWith("@/")) {
          const owner = owners.get(aliases[name]);
          if (!owner) problems.push(`${item.name}: ${name} 설치 대상 없음`);
          else if (
            owner !== item.name &&
            !item.registryDependencies?.includes(`@ienlab/${owner}`)
          )
            problems.push(`${item.name}: @ienlab/${owner} 의존성 누락`);
        } else {
          const dependency = name.startsWith("@")
            ? name.split("/").slice(0, 2).join("/")
            : name.split("/")[0];
          if (
            !["react", "react-dom"].includes(dependency) &&
            !item.dependencies?.some(
              (entry) =>
                entry === dependency || entry.startsWith(`${dependency}@`),
            )
          )
            problems.push(`${item.name}: ${dependency} 패키지 누락`);
        }
      }
    }
  assert.deepEqual(problems, []);
});
