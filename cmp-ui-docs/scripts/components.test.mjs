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
    await context.test("웹 확장 컴포넌트는 구분선과 안내 영역, 스와이프 대체 조작을 제공한다", async () => {
      const { Border } = await load("ui/separator.tsx");
      assert.match(render(Border, { variant: "padding24" }), /margin-inline:var\(--ien-spacing-xl\)/);
      assert.match(render(Border, { variant: "height", height: 24 }), /height:24px/);
      const { BottomInfo } = await load("ui/bottom-info.tsx");
      assert.match(render(BottomInfo, { children: "안내" }), /linear-gradient/);
      assert.doesNotMatch(render(BottomInfo, { gradient: "none" }), /linear-gradient/);
      const { SwipeBox } = await load("ui/swipe-box.tsx");
      const props = { children: "메일", endActions: [{ id: "delete", label: "삭제", onClick() {} }] };
      assert.match(render(SwipeBox, props), /aria-expanded="false"/);
      assert.match(render(SwipeBox, props), /inert=""/);
      assert.match(render(SwipeBox, { ...props, open: "end" }), /aria-expanded="true"/);
    });
    await context.test("드래그가 크기를 제어하는 버튼은 자동 크기 애니메이션을 실행하지 않는다", async () => {
      const { useIenAnimatedSize } = await load("lib/ien-animated-size.ts");
      let bind;
      const forwardedRef = { current: null };
      function Probe() {
        bind = useIenAnimatedSize(forwardedRef, false);
        return null;
      }
      render(Probe, {});
      const element = {};
      // 브라우저 크기 측정과 ResizeObserver 없이 ref 연결만 수행해야 합니다.
      const cleanup = bind(element);
      assert.equal(forwardedRef.current, element);
      cleanup();
      assert.equal(forwardedRef.current, null);
      const { SwipeBox } = await load("ui/swipe-box.tsx");
      const html = render(SwipeBox, { open: "end", endActions: [{ id: "delete", label: "삭제", onClick() {} }] });
      assert.match(html, /transition-property:background-color,color,box-shadow/);
    });
    await context.test("다이얼로그와 목적별 프리셋은 닫힌 상태에서 본문을 노출하지 않는다", async () => {
      const { Dialog, DialogContent, DialogTitle } = await load("ui/dialog.tsx");
      assert.doesNotMatch(render(Dialog, { open: false, children: React.createElement(DialogContent, {}, React.createElement(DialogTitle, {}, "비공개 제목")) }), /비공개 제목/);
      const { AlertDialog, ConfirmDialog } = await load("ui/alert-dialog.tsx");
      for (const Component of [AlertDialog, ConfirmDialog]) assert.doesNotMatch(render(Component, { open: false, title: "확인 제목", onConfirm() {} }), /확인 제목/);
      const presets = await load("ui/dialog-presets.tsx");
      for (const name of ["SaveAlertDialog", "DeleteAlertDialog", "UpdateAlertDialog", "NetworkAlertDialog", "ProgressDialog", "DatePickerDialog", "TimePickerDialog", "TextFieldDialog"]) {
        assert.equal(typeof presets[name], "function", name);
        assert.doesNotMatch(render(presets[name], { open: false, title: "확인 제목", onConfirm() {}, onSave() {}, onUnsave() {} }), /확인 제목/);
      }
    });
    await context.test("분할 버튼은 독립된 두 버튼의 간격·모서리·펼침 상태와 로딩 차단을 유지한다", async () => {
      const { SplitButton } = await load("ui/split-button.tsx");
      const html = render(SplitButton, { children: "저장", menu: null });
      assert.match(html, /data-slot="split-button"/);
      assert.match(html, /gap-ien-xxxs/);
      assert.equal((html.match(/data-slot="button"/g) ?? []).length, 2);
      assert.doesNotMatch(html, /pointer-events-none absolute inset-y-1\/4/);
      assert.match(html, /height:52px/);
      assert.match(html, /border-start-end-radius:var\(--ien-radius-sm\)/);
      assert.match(render(SplitButton, { children: "저장", menu: null, size: "sm" }), /height:36px/);
      assert.match(render(SplitButton, { children: "저장", menu: null, size: "md" }), /height:44px/);
      const expanded = render(SplitButton, { children: "저장", menu: null, open: true });
      assert.match(expanded, /border-start-start-radius:26px/);
      assert.match(expanded, /rotate-180/);
      const loading = render(SplitButton, { children: "저장", menu: null, loading: true });
      assert.equal((loading.match(/ disabled=""/g) ?? []).length, 2);
    });
    await context.test("토스트 코너가 CMP 2.1.4의 실제 베지어·원호 좌표와 일치한다", async () => {
      const { createIenContinuousRoundedRectanglePath } = await load("lib/ien-continuous-corners.ts");
      // capsule-desktop 2.1.4의 G2Continuity에서 추출한 우측 상단 코너 좌표입니다.
      const cases = [
        { size: [300, 54, 24], expected: [265.7340282, 0, 276.71325820418605, 0, 284.5163585341644, 0, 291.16387788696846, 5.4001515796055095, 23.591653265817648, 23.591653265817648, 0, 0, 1, 294.5998484203945, 8.836122113031559, 300, 15.4836414658356, 300, 22.880329945291262, 300, 26.427333438503886] },
        { size: [300, 76, 24], expected: [263.3120376, 0, 274.1803206885805, 0, 279.7833896520364, 0, 284.806159403151, 1.8281386829783557, 22.36292019111724, 22.36292019111724, 0, 0, 1, 298.17186131702164, 15.193840596849014, 300, 20.21661034796356, 300, 25.819679311419506, 300, 36.6879624] },
        { size: [40, 40, 20], expected: [20, 0, 21.005050633883346, 0, 28.284271247461902, 0, 34.14213562373095, 5.857864376269049, 20, 20, 0, 0, 1, 34.14213562373095, 5.857864376269051, 40, 11.7157287525381, 40, 18.994949366116654, 40, 20] },
      ];
      for (const { size: [width, height, radius], expected } of cases) {
        const path = createIenContinuousRoundedRectanglePath(width, height, [radius, radius, radius, radius]);
        const corner = path.match(/[MLCAZ][^MLCAZ]*/g).slice(0, 4).join(" ");
        const actual = corner.match(/-?\d*\.?\d+(?:e[+-]?\d+)?/gi).map(Number);
        assert.equal(actual.length, expected.length);
        for (let i = 0; i < expected.length; i++) assert.ok(Math.abs(actual[i] - expected[i]) < 1e-9, `${width}×${height}: 좌표 ${i}`);
        assert.ok(path.endsWith("Z"));
      }
    });
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
