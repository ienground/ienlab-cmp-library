import test from "node:test";
import assert from "node:assert/strict";
import { createServer } from "vite";
import React from "react";
import { renderToString } from "react-dom/server";
import { collectRegistry, createWebCatalog, sourceApi } from "./registry.mjs";
import { catalog } from "../web/catalog.js";
import { fileURLToPath } from "node:url";
import { readFile } from "node:fs/promises";

const repository = fileURLToPath(new URL("../../", import.meta.url));
test("모든 REUI 공식 프리뷰를 브라우저 전역 없이 서버 렌더링한다", { timeout: 120000 }, async (context) => {
  const server = await createServer({ server: { middlewareMode: true, hmr: false }, appType: "custom" });
  try {
    const items = createWebCatalog(await collectRegistry(`${repository}/registry.json`, repository), catalog, repository);
    const generated = JSON.parse(await readFile(`${repository}/cmp-ui-docs/web/generated-web-catalog.json`, "utf8"));
    const { WebPlayground, playgroundExample } = await server.ssrLoadModule(`${repository}/cmp-ui-docs/web/web-playground.tsx`);
    for (const item of items) {
      await context.test(item.id, async () => {
        assert.ok(item.preview, `${item.id}: 프리뷰 경로 누락`);
        const module = await server.ssrLoadModule(`${repository}/${item.preview}`);
        assert.ok(module.default, `${item.id}: 프리뷰 export 누락`);
        const html = renderToString(React.createElement(module.default));
        assert.ok(html.length > 0, `${item.id}: 빈 프리뷰`);
        const definition = generated.find((entry) => entry.id === item.id).playground;
        const playground = renderToString(React.createElement(WebPlayground, { definition, source: definition.example, Preview: module.default, renderPreview: (preview) => preview }));
        assert.match(playground, /속성 조정/);
        assert.doesNotMatch(playground, /프리뷰를 불러오지 못했습니다/);
        if (item.id === "button") {
          const previewHtml = playground.match(/aria-label="Playground 미리보기"([\s\S]*?)<\/section>/)?.[1] ?? "";
          assert.equal((previewHtml.match(/data-slot="button"/g) ?? []).length, 1);
          const example = playgroundExample(definition.example, definition, { [definition.targets[0].key]: { tone: "danger", variant: "weak", children: "변경한 버튼" } });
          assert.match(example, /"tone": "danger"/);
          assert.match(example, /변경한 버튼/);
          assert.doesNotMatch(example, />버튼</);
          sourceApi(example, "playground-example.tsx");
        }
        if (item.id === "number-field") {
          const example = playgroundExample(definition.example, definition, { [definition.targets[0].key]: { value: 42 } });
          assert.match(example, /"value": 42/);
          assert.doesNotMatch(example, /defaultValue=/);
          sourceApi(example, "number-field-playground.tsx");
        }
      });
    }
  } finally {
    await server.close();
  }
});
