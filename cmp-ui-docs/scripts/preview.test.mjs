import test from "node:test";
import assert from "node:assert/strict";
import { createServer } from "vite";
import React from "react";
import { renderToString } from "react-dom/server";
import { collectRegistry, createWebCatalog } from "./registry.mjs";
import { catalog } from "../web/catalog.js";
import { fileURLToPath } from "node:url";

const repository = fileURLToPath(new URL("../../", import.meta.url));
test("모든 REUI 공식 프리뷰를 브라우저 전역 없이 서버 렌더링한다", { timeout: 120000 }, async (context) => {
  const server = await createServer({ server: { middlewareMode: true, hmr: false }, appType: "custom" });
  try {
    const items = createWebCatalog(await collectRegistry(`${repository}/registry.json`, repository), catalog, repository);
    for (const item of items) {
      await context.test(item.id, async () => {
        assert.ok(item.preview, `${item.id}: 프리뷰 경로 누락`);
        const module = await server.ssrLoadModule(`${repository}/${item.preview}`);
        assert.ok(module.default, `${item.id}: 프리뷰 export 누락`);
        const html = renderToString(React.createElement(module.default));
        assert.ok(html.length > 0, `${item.id}: 빈 프리뷰`);
      });
    }
  } finally {
    await server.close();
  }
});
