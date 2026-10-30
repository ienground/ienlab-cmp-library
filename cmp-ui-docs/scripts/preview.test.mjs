import test from "node:test";
import assert from "node:assert/strict";
import { createServer } from "vite";
import React from "react";
import { renderToString } from "react-dom/server";
import { collectRegistry, createWebCatalog } from "./registry.mjs";
import { catalog } from "../web/catalog.js";
import { fileURLToPath } from "node:url";

const repository = fileURLToPath(new URL("../../", import.meta.url));

test("분할 문자 입력에서 자동 완성 값을 여러 칸에 반영한다", async () => {
  const server = await createServer({
    server: { middlewareMode: true, hmr: false },
    appType: "custom",
  });
  try {
    const { normalizeSplitTextFieldChange } = await server.ssrLoadModule(
      `${repository}/cmp-ui-web-registry/cmp-interactive/split-text-field-logic.ts`,
    );
    assert.deepEqual(
      normalizeSplitTextFieldChange({
        current: "",
        input: "123456",
        index: 0,
        length: 6,
        type: "numeric",
      }),
      { value: "123456", focusIndex: 5 },
    );
    assert.deepEqual(
      normalizeSplitTextFieldChange({
        current: "123456",
        input: "9",
        index: 2,
        length: 6,
        type: "numeric",
      }),
      { value: "129456", focusIndex: 3 },
    );
    assert.deepEqual(
      normalizeSplitTextFieldChange({
        current: "123456",
        input: "12x34",
        index: 0,
        length: 6,
        type: "numeric",
      }),
      { value: "1234", focusIndex: 4 },
    );
    assert.deepEqual(
      normalizeSplitTextFieldChange({
        current: "123456",
        input: "789",
        index: 2,
        length: 6,
        type: "numeric",
        replaceWholeValue: true,
      }),
      { value: "789", focusIndex: 5 },
    );
    assert.deepEqual(
      normalizeSplitTextFieldChange({
        current: "123456",
        input: "",
        index: 3,
        length: 6,
        type: "numeric",
      }),
      { value: "12356", focusIndex: 3 },
    );
  } finally {
    await server.close();
  }
});

test("숫자 스피너는 소수 step을 정확히 더하고 경계에 맞춘다", async () => {
  const server = await createServer({
    server: { middlewareMode: true, hmr: false },
    appType: "custom",
  });
  try {
    const { stepNumericValue } = await server.ssrLoadModule(
      `${repository}/cmp-ui-web-registry/cmp-interactive/numeric-spinner-logic.ts`,
    );
    assert.equal(stepNumericValue(0.2, 0.1, 1), 0.3);
    assert.equal(stepNumericValue(0.3, 0.1, 1), 0.4);
    assert.equal(stepNumericValue(-0.3, 0.1, 1), -0.2);
    assert.equal(stepNumericValue(0.2, 0.1, 1, 0, 0.3), 0.3);
    assert.equal(stepNumericValue(0.1, 0.1, -1, 0), 0);
  } finally {
    await server.close();
  }
});

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
