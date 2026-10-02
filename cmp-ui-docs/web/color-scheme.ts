import defaults from "./generated-color-scheme.json" with { type: "json" };

export type ColorRole = keyof typeof defaults.light;
export type ColorMode = "light" | "dark";
export type ColorScheme = Record<ColorRole, string>;
export type ColorSchemes = Record<ColorMode, ColorScheme>;
export type ColorSeeds = Record<
  "brand" | "success" | "warning" | "danger" | "info",
  string
>;

export const colorRoles = Object.keys(defaults.light) as ColorRole[];
export const colorDescriptions = defaults.descriptions;
export const semanticGroups = [
  {
    base: "brand",
    on: "onBrand",
    weak: "brandWeak",
    onWeak: "onBrandWeak",
    label: "브랜드",
  },
  {
    base: "success",
    on: "onSuccess",
    weak: "successWeak",
    onWeak: "onSuccessWeak",
    label: "성공",
  },
  {
    base: "warning",
    on: "onWarning",
    weak: "warningWeak",
    onWeak: "onWarningWeak",
    label: "경고",
  },
  {
    base: "danger",
    on: "onDanger",
    weak: "dangerWeak",
    onWeak: "onDangerWeak",
    label: "오류",
  },
  {
    base: "info",
    on: "onInfo",
    weak: "infoWeak",
    onWeak: "onInfoWeak",
    label: "정보",
  },
] as const;

export function defaultColorSchemes(): ColorSchemes {
  return { light: { ...defaults.light }, dark: { ...defaults.dark } };
}

export function defaultColorSeeds(): ColorSeeds {
  return Object.fromEntries(
    semanticGroups.map(({ base }) => [base, defaults.light[base]]),
  ) as ColorSeeds;
}

export function isHexColor(value: string, allowAlpha = true): boolean {
  return (
    allowAlpha ? /^#[\dA-Fa-f]{6}(?:[\dA-Fa-f]{2})?$/ : /^#[\dA-Fa-f]{6}$/
  ).test(value);
}

function rgb(value: string): number[] {
  if (!isHexColor(value))
    throw new Error("색상은 #RRGGBB 또는 #RRGGBBAA 형식이어야 합니다.");
  return [1, 3, 5].map((index) => parseInt(value.slice(index, index + 2), 16));
}

function mix(color: string, target: string, amount: number): string {
  const from = rgb(color);
  const to = rgb(target);
  return (
    "#" +
    from
      .map((value, index) =>
        Math.round(value + (to[index] - value) * amount)
          .toString(16)
          .padStart(2, "0"),
      )
      .join("")
      .toUpperCase()
  );
}

export function contrastRatio(foreground: string, background: string): number {
  const backdrop = rgb(background);
  const alpha =
    foreground.length === 9 ? parseInt(foreground.slice(7), 16) / 255 : 1;
  const text = rgb(foreground).map(
    (value, index) => value * alpha + backdrop[index] * (1 - alpha),
  );
  function luminance(channels: number[]): number {
    const linear = channels.map((value) => {
      const normalized = value / 255;
      return normalized <= 0.04045
        ? normalized / 12.92
        : ((normalized + 0.055) / 1.055) ** 2.4;
    });
    return linear[0] * 0.2126 + linear[1] * 0.7152 + linear[2] * 0.0722;
  }
  const textLuminance = luminance(text);
  const backdropLuminance = luminance(backdrop);
  return (
    (Math.max(textLuminance, backdropLuminance) + 0.05) /
    (Math.min(textLuminance, backdropLuminance) + 0.05)
  );
}

function onColor(background: string): string {
  return contrastRatio("#FFFFFF", background) >=
    contrastRatio("#000000", background)
    ? "#FFFFFF"
    : "#000000";
}

function readableAccent(color: string, background: string): string {
  const target = onColor(background);
  for (let step = 0; step <= 100; step++) {
    const candidate = mix(color, target, step / 100);
    if (contrastRatio(candidate, background) >= 4.5) return candidate;
  }
  return target;
}

export function generateColorSchemes(seeds: ColorSeeds): ColorSchemes {
  const schemes = defaultColorSchemes();
  for (const group of semanticGroups) {
    const seed = seeds[group.base];
    if (!isHexColor(seed, false))
      throw new Error("기준 색상은 #RRGGBB 형식이어야 합니다.");
    for (const mode of ["light", "dark"] as const) {
      const scheme = schemes[mode];
      const base =
        mode === "dark" ? mix(seed, "#FFFFFF", 0.25) : seed.toUpperCase();
      const weak = mix(base, scheme.background, mode === "dark" ? 0.8 : 0.92);
      scheme[group.base] = base;
      scheme[group.on] = onColor(base);
      scheme[group.weak] = weak;
      scheme[group.onWeak] = readableAccent(base, weak);
    }
  }
  return schemes;
}

export function colorToArgb(color: string): string {
  if (!isHexColor(color)) throw new Error("올바른 HEX 색상을 입력해 주세요.");
  return (
    (color.length === 9 ? color.slice(7) : "FF") + color.slice(1, 7)
  ).toUpperCase();
}

export function composeColorQuery(scheme: ColorScheme): string {
  return new URLSearchParams(
    colorRoles.map((role) => [`color.${role}`, colorToArgb(scheme[role])]),
  ).toString();
}

export function generateKotlinTheme(schemes: ColorSchemes): string {
  function declaration(mode: ColorMode, name: string): string {
    return `val ${name} = IenColorScheme(\n${colorRoles.map((role) => `    ${role} = Color(0x${colorToArgb(schemes[mode][role])}),`).join("\n")}\n)`;
  }
  return `import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import zone.ien.utils.ui.foundation.IenColorScheme
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.defaultIenTokens

${declaration("light", "AppLightColors")}

${declaration("dark", "AppDarkColors")}

val AppTokens = defaultIenTokens().copy(
    lightColors = AppLightColors,
    darkColors = AppDarkColors,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    IenTheme(tokens = AppTokens, darkTheme = darkTheme, content = content)
}
`;
}
