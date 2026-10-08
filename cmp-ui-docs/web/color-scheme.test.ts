import assert from "node:assert/strict";
import { readFile } from "node:fs/promises";
import test from "node:test";
import {
  colorRoles,
  colorToArgb,
  composeColorQuery,
  contrastRatio,
  defaultColorSchemes,
  defaultColorSeeds,
  generateColorSchemes,
  generateKotlinTheme,
  isHexColor,
  semanticGroups,
} from "./color-scheme.ts";

test("생성기의 필드와 기본 색상이 실제 Kotlin ColorScheme 선언과 일치한다", async () => {
  const source = await readFile(
    new URL(
      "../../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/foundation/IenColorScheme.kt",
      import.meta.url,
    ),
    "utf8",
  );
  const constructor = source.match(
    /data class IenColorScheme\(([\s\S]*?)\n\)/,
  )?.[1];
  assert.ok(constructor);
  const required = Array.from(
    constructor.matchAll(/val (\w+): Color/g),
    (match) => match[1],
  );
  assert.deepEqual(colorRoles, required);
  for (const [mode, tokenName] of [
    ["light", "Light"],
    ["dark", "Dark"],
  ] as const) {
    const block = source.match(
      new RegExp(
        `internal object Ien${tokenName}ColorTokens \\{([\\s\\S]*?)\\n\\}`,
      ),
    )?.[1];
    assert.ok(block);
    for (const role of colorRoles) {
      assert.equal(
        colorToArgb(defaultColorSchemes()[mode][role]),
        block.match(
          new RegExp(`val ${role} = Color\\(0x([\\dA-Fa-f]{8})\\)`),
        )?.[1],
      );
    }
  }
});

test("자동 생성한 모든 상태 색상의 전경과 배경 대비가 4.5 이상이다", () => {
  for (const color of [
    "#000000",
    "#FFFFFF",
    "#FFFF00",
    "#3182F6",
    "#FF0088",
    "#808080",
  ]) {
    const seeds = defaultColorSeeds();
    for (const group of semanticGroups) seeds[group.base] = color;
    const schemes = generateColorSchemes(seeds);
    for (const mode of ["light", "dark"] as const) {
      for (const group of semanticGroups) {
        assert.ok(
          contrastRatio(schemes[mode][group.on], schemes[mode][group.base]) >=
            4.5,
        );
        assert.ok(
          contrastRatio(
            schemes[mode][group.onWeak],
            schemes[mode][group.weak],
          ) >= 4.5,
        );
      }
    }
  }
});

test("알파값과 Kotlin ARGB 코드 및 Compose 쿼리가 보존된다", () => {
  assert.equal(colorToArgb("#12345680"), "80123456");
  assert.equal(colorToArgb("#abcdef"), "FFABCDEF");
  const schemes = defaultColorSchemes();
  const code = generateKotlinTheme(schemes);
  for (const role of colorRoles) {
    assert.equal(code.match(new RegExp(`    ${role} = Color`, "g"))?.length, 2);
  }
  assert.ok(code.includes("overlay = Color(0x99000000)"));
  assert.ok(code.includes("lightColors = AppLightColors"));
  assert.equal(
    new URLSearchParams(composeColorQuery(schemes.dark)).get("color.overlay"),
    "B3000000",
  );
  assert.equal(isHexColor("#xyzxyz"), false);
  assert.equal(isHexColor("#123"), false);
  assert.throws(() => colorToArgb("#nothex"));
});

test("앱 이름으로 유효한 Kotlin 테마 식별자를 생성한다", () => {
  const code = generateKotlinTheme(defaultColorSchemes(), "Love Hero");

  assert.ok(
    code.includes(
      "internal val LoveHeroLightColors = DefaultColors.lightColors.copy(",
    ),
  );
  assert.ok(
    code.includes(
      "internal val LoveHeroDarkColors = DefaultColors.darkColors.copy(",
    ),
  );
  assert.ok(code.includes("fun LoveHeroTheme("));
});

test("아이콘 스타일이 생성한 Kotlin 테마에 적용된다", () => {
  const styles = [
    ["tabler", "IconStyle.Tabler"],
    ["material-filled", "IconStyle.Material.Filled"],
    ["material-rounded", "IconStyle.Material.Rounded"],
    ["material-sharp", "IconStyle.Material.Sharp"],
  ] as const;
  for (const [style, kotlinStyle] of styles) {
    const code = generateKotlinTheme(defaultColorSchemes(), "App", style);
    assert.ok(code.includes(`LocalIconStyle provides ${kotlinStyle}`));
    assert.ok(code.includes("CompositionLocalProvider"));
  }
});
