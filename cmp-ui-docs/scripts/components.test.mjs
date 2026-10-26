import test from "node:test";
import assert from "node:assert/strict";
import React from "react";
import { renderToStaticMarkup } from "react-dom/server";
import { createServer } from "vite";
import { fileURLToPath } from "node:url";

const repository = fileURLToPath(new URL("../../", import.meta.url));
test("CMP 대응 컴포넌트의 공개 계약", async (context) => {
  const server = await createServer({ cacheDir: fileURLToPath(new URL("../node_modules/.vite-component-tests", import.meta.url)), server: { middlewareMode: true, hmr: false }, appType: "custom" });
  const load = (file) => server.ssrLoadModule(`${repository}/cmp-ui-web-registry/${file}`);
  const render = (Component, props) => renderToStaticMarkup(React.createElement(Component, props));
  try {
    const { Input } = await load("ui/input.tsx");
    await context.test("비밀번호 표시 전환과 지우기는 입력 상태에 따라 노출한다", () => {
      const password = render(Input, { type: "password", defaultValue: "secret" });
      assert.match(password, /aria-label="비밀번호 표시"/);
      assert.match(password, /type="password"/);
      assert.match(render(Input, { clearable: true, defaultValue: "내용" }), /aria-label="입력 지우기"/);
      for (const state of [{ readOnly: true }, { disabled: true }, { state: { enabled: false } }]) {
        assert.doesNotMatch(render(Input, { clearable: true, defaultValue: "내용", ...state }), /aria-label="입력 지우기"/);
      }
    });
    await context.test("포맷된 표시는 원본 값과 구분하고 사용자 텍스트를 이스케이프한다", async () => {
      assert.match(render(Input, { value: "123456", format: { transform: (value) => value.slice(0, 3) + "-" + value.slice(3) } }), /value="123-456"/);
      const { HighlightText } = await load("ui/text-content.tsx");
      const html = render(HighlightText, { text: "a+b <script>", highlights: ["a+b", "<script>", ""] });
      assert.equal((html.match(/<mark/g) ?? []).length, 2);
      assert.doesNotMatch(html, /<script>/);
      assert.match(html, /&lt;script&gt;/);
    });
    await context.test("약관의 부분 선택·필수·비활성 상태를 표현한다", async () => {
      const { Agreement } = await load("ui/agreement.tsx");
      const html = render(Agreement, { items: [{ value: "terms", label: "약관", required: true }, { value: "marketing", label: "마케팅" }, { value: "blocked", label: "잠김", disabled: true }], defaultValue: ["terms"] });
      assert.match(html, /aria-checked="mixed"/);
      assert.match(html, /\[필수\]/);
      assert.match(html, /disabled/);
      assert.doesNotMatch(render(Agreement, { items: [] }), /aria-checked="true"/);
    });
    await context.test("다중 슬라이더와 날짜·기간 폼 값의 구조를 유지한다", async () => {
      const { RangeSlider } = await load("ui/slider.tsx");
      assert.equal((render(RangeSlider, { defaultValue: [20, 80], label: "범위" }).match(/role="slider"/g) ?? []).length, 2);
      const { DurationPicker } = await load("reui/duration-picker.tsx");
      const html = render(DurationPicker, { value: 3661, name: "duration", form: "settings", required: true });
      assert.match(html, /name="duration"/);
      assert.match(html, /value="3661"/);
      assert.match(html, /form="settings"/);
      assert.match(html, /required/);
    });
    await context.test("단계 오류는 완료 상태보다 우선하며 분할 입력은 잘못된 길이를 거부한다", async () => {
      const { Stepper, StepperItem, StepperIndicator } = await load("reui/stepper.tsx");
      const html = renderToStaticMarkup(React.createElement(Stepper, { value: 2 }, React.createElement(StepperItem, { step: 1, error: true, completed: true }, React.createElement(StepperIndicator))));
      assert.match(html, /data-state="error"/);
      assert.match(html, /aria-label="오류"/);
      const { SplitInput } = await load("ui/split-input.tsx");
      assert.throws(() => render(SplitInput, { lengths: [0], labels: ["입력"] }), RangeError);
      assert.throws(() => render(SplitInput, { lengths: [3], labels: [] }), RangeError);
      const { WheelPicker } = await load("ui/wheel-picker.tsx");
      assert.throws(() => render(WheelPicker, { options: [], label: "선택", visibleRows: 0 }), RangeError);
    });
  } finally {
    await server.close();
  }
});
