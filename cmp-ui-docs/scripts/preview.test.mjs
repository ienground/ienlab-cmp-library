import test from "node:test";
import assert from "node:assert/strict";
import { createServer } from "vite";
import React from "react";
import { renderToString } from "react-dom/server";
import { collectRegistry, createWebCatalog } from "./registry.mjs";
import { catalog } from "../web/catalog.js";
import { fileURLToPath } from "node:url";

const repository = fileURLToPath(new URL("../../", import.meta.url));
test(
  "모든 컴포넌트 프리뷰를 브라우저 전역 없이 서버 렌더링한다",
  { timeout: 120000 },
  async (context) => {
    const server = await createServer({
      server: { middlewareMode: true, hmr: false },
      appType: "custom",
    });
    try {
      const items = createWebCatalog(
        await collectRegistry(`${repository}/registry.json`, repository),
        catalog,
        repository,
      );
      for (const item of items) {
        await context.test(item.id, async () => {
          const module = await server.ssrLoadModule(
            `${repository}/${item.preview || item.source}`,
          );
          const Preview = item.preview ? module.default : module.Button;
          assert.ok(Preview, `${item.id}: 프리뷰 export 누락`);
          if (item.id === "card") {
            const { Card } = await server.ssrLoadModule(
              `${repository}/${item.source}`,
            );
            assert.equal(
              Card({}).props.onClick,
              undefined,
              "정적 서버 카드에 이벤트 핸들러를 생성하면 안 됩니다.",
            );
            assert.equal(Card({}).props.onKeyDown, undefined);
          }
          const html = renderToString(
            React.createElement(
              Preview,
              item.preview ? undefined : { children: "버튼" },
            ),
          );
          assert.ok(html.length > 0, `${item.id}: 빈 프리뷰`);
        });
      }
    } finally {
      await server.close();
    }
  },
);
