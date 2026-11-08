import test from "node:test";
import assert from "node:assert/strict";
import { fileURLToPath } from "node:url";
import { chromium, expect } from "@playwright/test";
import { createServer } from "vite";

const fixture = fileURLToPath(new URL("./fixtures/component-interactions.tsx", import.meta.url));

test("웹 컴포넌트의 브라우저 상호작용", { timeout: 120000 }, async (context) => {
  const server = await createServer({
    cacheDir: fileURLToPath(new URL("../node_modules/.vite-browser-tests", import.meta.url)),
    server: { host: "127.0.0.1", port: 0, hmr: false, watch: null },
    plugins: [{ name: "component-interactions", configureServer(vite) {
      vite.middlewares.use((request, response, next) => {
        if (!request.url?.startsWith("/__component-test")) return next();
        response.setHeader("Content-Type", "text/html");
        response.end(`<html lang="ko"><body><div id="root" style="max-width:600px;padding:24px"></div><script type="module" src="/@vite/client"></script><script type="module" src="/@fs/${fixture}"></script></body></html>`);
      });
    } }],
  });
  let browser;
  try {
    await server.listen();
    browser = await chromium.launch({ headless: true, ...(process.env.PLAYWRIGHT_CHANNEL ? { channel: process.env.PLAYWRIGHT_CHANNEL } : {}) });
    const address = server.httpServer.address();
    const page = await browser.newPage({ viewport: { width: 1024, height: 900 }, reducedMotion: "reduce" });
    page.setDefaultTimeout(10000);
    const open = async (kind) => {
      await page.goto(`http://127.0.0.1:${address.port}/__component-test?fixture=${kind}`);
      await page.getByRole(["actions", "confirm", "fields", "fields-cancel"].includes(kind) ? "button" : "listbox").first().waitFor();
    };
    const drag = async (list, distance) => {
      const box = await list.boundingBox();
      const x = box.x + box.width / 2, y = box.y + box.height / 2;
      await page.mouse.move(x, y);
      await page.mouse.down();
      await page.mouse.move(x, y - distance, { steps: 10 });
      await page.mouse.up();
    };
    await context.test("휠은 마우스 드래그로 선택하며 드래그 뒤 클릭이 선택을 덮어쓰지 않는다", async () => {
      await open("wheel");
      await drag(page.getByRole("listbox"), 44);
      await expect(page.locator("output")).toHaveText("2");
      await expect(page.getByRole("option", { name: "항목 2", exact: true })).toHaveAttribute("aria-selected", "true");
    });
    await context.test("비활성 행을 건너뛰고 외부 값과 키보드 선택을 동기화한다", async () => {
      await open("wheel");
      const list = page.getByRole("listbox");
      await list.press("ArrowDown");
      await list.press("ArrowDown");
      await expect(page.locator("output")).toHaveText("4");
      await page.getByRole("button", { name: "외부 값 변경" }).click();
      await expect.poll(() => list.evaluate((element) => element.scrollTop)).toBe(264);
      await list.press("Home");
      await expect(page.locator("output")).toHaveText("0");
    });
    await context.test("긴 휠의 마지막 항목까지 가상화와 선택을 유지한다", async () => {
      await open("long");
      const list = page.getByRole("listbox");
      for (const [key, label] of [["End", "항목 999"], ["Home", "항목 0"], ["End", "항목 999"]]) {
        await list.press(key);
        await expect(page.getByRole("option", { name: label, exact: true })).toHaveAttribute("aria-selected", "true");
      }
      assert.ok(await page.getByRole("option").count() <= 21);
    });
    await context.test("읽기 전용·비활성 휠은 드래그나 키 입력으로 바뀌지 않는다", async () => {
      for (const kind of ["readonly", "disabled"]) {
        await open(kind);
        const list = page.getByRole("listbox");
        await drag(list, 88);
        await list.press("ArrowDown");
        await expect(page.locator("output")).toHaveText("1");
        await expect.poll(() => list.evaluate((element) => element.scrollTop)).toBe(44);
      }
    });
    await context.test("연도 변경은 윤년 말일을 보정한다", async () => {
      await open("date");
      await page.getByRole("listbox", { name: "년", exact: true }).press("ArrowUp");
      await expect(page.locator("output")).toHaveText("2023-02-28");
    });
    await context.test("시간·기간 선택기도 같은 드래그 동작을 사용한다", async () => {
      await open("time");
      await drag(page.locator('[data-slot="time-picker-column-list"]').first(), 44);
      await expect(page.locator("output")).toHaveText("15:30");
      await open("duration");
      await drag(page.getByRole("listbox", { name: "분", exact: true }), 44);
      await expect(page.locator("output")).toHaveText("3721");
    });
    await context.test("마우스 휠·행 클릭도 드래그 추가 후 유지된다", async () => {
      await open("wheel");
      const list = page.getByRole("listbox");
      await list.hover();
      await page.mouse.wheel(0, 44);
      await expect(page.locator("output")).toHaveText("2");
      await page.getByRole("option", { name: "항목 1", exact: true }).click();
      await expect(page.locator("output")).toHaveText("1");
    });
    await context.test("터치 스와이프는 브라우저의 기본 스크롤로 선택한다", async () => {
      const mobile = await browser.newPage({ viewport: { width: 390, height: 844 }, hasTouch: true, isMobile: true });
      try {
        await mobile.goto(`http://127.0.0.1:${address.port}/__component-test?fixture=wheel`);
        const list = mobile.getByRole("listbox");
        await list.waitFor();
        const box = await list.boundingBox();
        const session = await mobile.context().newCDPSession(mobile);
        const x = box.x + box.width / 2, y = box.y + box.height * 0.7;
        await session.send("Input.dispatchTouchEvent", { type: "touchStart", touchPoints: [{ x, y }] });
        for (let step = 1; step <= 8; step++) {
          await session.send("Input.dispatchTouchEvent", { type: "touchMove", touchPoints: [{ x, y: y - step * 10 }] });
          await new Promise((resolve) => setTimeout(resolve, 16));
        }
        await session.send("Input.dispatchTouchEvent", { type: "touchEnd", touchPoints: [] });
        await expect(mobile.locator("output")).not.toHaveText("1");
        await expect(mobile.locator("output")).not.toHaveText("3");
      } finally {
        await mobile.close();
      }
    });
    await context.test("확인 전 날짜 변경은 취소할 수 있고 확인한 값만 반영한다", async () => {
      await open("confirm");
      await page.getByRole("button", { name: "날짜", exact: true }).click();
      await page.getByRole("listbox", { name: "년", exact: true }).press("ArrowUp");
      await expect(page.locator("output")).toHaveText("2024-02-29");
      await page.keyboard.press("Escape");
      await page.getByRole("listbox").first().waitFor({ state: "hidden" });
      await page.getByRole("button", { name: "날짜", exact: true }).click();
      await expect(page.getByRole("option", { name: "2024년", exact: true })).toHaveAttribute("aria-selected", "true");
      await page.getByRole("listbox", { name: "년", exact: true }).press("ArrowUp");
      await page.getByRole("button", { name: "확인", exact: true }).click();
      await expect(page.locator("output")).toHaveText("2023-02-28");
    });
    await context.test("폼 초기화는 분할 입력과 약관 선택을 기본값으로 복원한다", async () => {
      await open("fields");
      await page.getByRole("textbox", { name: "텍스트", exact: true }).fill("수정");
      await page.getByRole("textbox", { name: "앞자리", exact: true }).fill("99");
      await page.getByRole("checkbox", { name: "마케팅" }).check();
      await page.getByRole("button", { name: "폼 초기화" }).click();
      await expect(page.getByRole("textbox", { name: "텍스트", exact: true })).toHaveValue("초기");
      await expect(page.getByRole("textbox", { name: "앞자리", exact: true })).toHaveValue("12");
      await expect(page.getByRole("checkbox", { name: "이용약관" })).toBeChecked();
      await expect(page.getByRole("checkbox", { name: "마케팅" })).not.toBeChecked();
    });
    await context.test("초기화를 취소한 폼은 입력과 약관 선택을 유지한다", async () => {
      await open("fields-cancel");
      await page.getByRole("textbox", { name: "텍스트", exact: true }).fill("수정값");
      await page.getByRole("textbox", { name: "앞자리", exact: true }).fill("99");
      await page.getByRole("checkbox", { name: "마케팅" }).check();
      await page.getByRole("button", { name: "폼 초기화" }).click();
      await expect(page.locator('[data-slot="input-counter"]')).toHaveText("3/10");
      await expect(page.getByRole("textbox", { name: "앞자리", exact: true })).toHaveValue("99");
      await expect(page.getByRole("checkbox", { name: "이용약관" })).toBeChecked();
      await expect(page.getByRole("checkbox", { name: "마케팅" })).toBeChecked();
    });
    await context.test("탐색·스와이프 작업 버튼은 상위 폼을 제출하지 않는다", async () => {
      await open("actions");
      await page.getByRole("button", { name: "다음 항목" }).click();
      await expect(page.getByRole("status", { name: "폼 제출" })).toHaveText("0");
      await page.getByRole("button", { name: "보관", exact: true }).click();
      await expect(page.getByRole("status", { name: "보관 횟수" })).toHaveText("1");
      await expect(page.getByRole("status", { name: "폼 제출" })).toHaveText("0");
    });
  } finally {
    await browser?.close();
    await server.close();
  }
});
