import { access, mkdir, readFile, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { catalog } from "../web/catalog.js";

const projectDirectory = fileURLToPath(new URL("../", import.meta.url));
const repositoryDirectory = path.resolve(projectDirectory, "..");
const outputFile = path.join(projectDirectory, "web/generated-api-docs.json");

function findDeclarations(source, name) {
  const declarationPattern = new RegExp(
    "^[ \\t]*(?:fun\\s+(?:<[^>]+>\\s*)?(?:[\\w?.<>]+\\.)?|(?:(?:data|sealed|enum|value)\\s+)?(?:class|interface|object)\\s+)" +
      name +
      "\\b",
    "gm",
  );
  return Array.from(source.matchAll(declarationPattern));
}

function readKDoc(source, declarationIndex) {
  const beforeDeclaration = source.slice(0, declarationIndex).trimEnd();
  const commentEnd = beforeDeclaration.lastIndexOf("*/");
  if (commentEnd === -1) return null;

  const commentStart = beforeDeclaration.lastIndexOf("/**", commentEnd);
  if (commentStart === -1) return null;

  const annotationLines = source
    .slice(commentEnd + 2, declarationIndex)
    .split(/\r?\n/)
    .every((line) => line.trim() === "" || line.trim().startsWith("@"));
  if (!annotationLines) return null;

  return beforeDeclaration
    .slice(commentStart + 3, commentEnd)
    .split(/\r?\n/)
    .map((line) => line.replace(/^\s*\* ?/, "").trim())
    .join("\n")
    .trim();
}

function parseKDoc(text) {
  if (!text) return { description: "", parameters: [] };

  const description = [];
  const parameters = [];
  let currentParameter = null;

  for (const line of text.split("\n")) {
    const parameterMatch = line.match(/^@param\s+(\S+)\s*(.*)$/);
    if (parameterMatch) {
      currentParameter = {
        name: parameterMatch[1],
        description: parameterMatch[2].trim(),
      };
      parameters.push(currentParameter);
      continue;
    }

    if (line.startsWith("@")) {
      currentParameter = null;
      continue;
    }

    if (currentParameter) {
      currentParameter.description +=
        (currentParameter.description ? " " : "") + line.trim();
    } else if (line.trim()) {
      description.push(line.trim());
    }
  }

  return {
    description: description.join("\n"),
    parameters: parameters.filter((parameter) => parameter.description),
  };
}

function findSignatureEnd(source, openParenthesis) {
  let depth = 0;
  let quote = null;
  let escaped = false;

  for (let index = openParenthesis; index < source.length; index += 1) {
    const character = source[index];
    const nextCharacter = source[index + 1];

    if (quote) {
      if (escaped) {
        escaped = false;
      } else if (character === "\\") {
        escaped = true;
      } else if (character === quote) {
        quote = null;
      }
      continue;
    }

    if (character === '"' || character === "'") {
      quote = character;
    } else if (character === "/" && nextCharacter === "/") {
      index = source.indexOf("\n", index);
      if (index === -1) return source.length - 1;
    } else if (character === "(") {
      depth += 1;
    } else if (character === ")") {
      depth -= 1;
      if (depth === 0) return index;
    }
  }

  return source.length - 1;
}

function extractApi(source, name, declaration, fallbackDescription) {
  const declarationIndex = declaration.index;
  const openParenthesis = source.indexOf("(", declarationIndex + declaration[0].length);
  const declarationLineEnd = source.indexOf("\n", declarationIndex);
  const hasParameters =
    openParenthesis !== -1 &&
    (declarationLineEnd === -1 || openParenthesis < declarationLineEnd);
  const signatureEnd = hasParameters
    ? findSignatureEnd(source, openParenthesis)
    : declarationLineEnd === -1
      ? source.length
      : declarationLineEnd;
  const signature = source
    .slice(declarationIndex, signatureEnd + (hasParameters ? 1 : 0))
    .replace(/\s+/g, " ")
    .trim();
  const kdoc = parseKDoc(readKDoc(source, declarationIndex));

  return {
    name,
    signature,
    description: kdoc.description || fallbackDescription,
    parameters: kdoc.parameters,
  };
}

function extractApis(source, name, fallbackDescription) {
  const declarations = findDeclarations(source, name);
  if (declarations.length === 0) {
    return [
      {
        name,
        signature: "",
        description: fallbackDescription,
        parameters: [],
      },
    ];
  }

  return declarations.map((declaration) =>
    extractApi(source, name, declaration, fallbackDescription),
  );
}

const apiDocs = {};

for (const component of catalog) {
  if (component.module !== "cmp-ui") {
    apiDocs[component.id] = component.api.map((name) => ({
      name,
      signature: "",
      description: component.description,
      parameters: [],
    }));
    continue;
  }

  const source = await readFile(
    path.join(repositoryDirectory, component.source),
    "utf8",
  );
  apiDocs[component.id] = component.api.flatMap((name) =>
    extractApis(source, name, component.description),
  );
}

await mkdir(path.dirname(outputFile), { recursive: true });
await writeFile(outputFile, JSON.stringify(apiDocs, null, 2) + "\n");

const colorSource = await readFile(
  path.join(repositoryDirectory, "cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/foundation/IenColorScheme.kt"),
  "utf8",
);
const colorConstructor = colorSource.match(/data class IenColorScheme\(([\s\S]*?)\n\)/);
if (!colorConstructor) throw new Error("IenColorScheme 선언을 찾을 수 없습니다.");
const colorRoles = Array.from(colorConstructor[1].matchAll(/val (\w+): Color/g), (match) => match[1]);
const descriptions = Object.fromEntries(
  Array.from(colorSource.matchAll(/@property (\w+) ([^\n]+)/g), (match) => [match[1], match[2]]),
);
function extractColorTokens(mode) {
  const block = colorSource.match(new RegExp(`internal object Ien${mode}ColorTokens \\{([\\s\\S]*?)\\n\\}`));
  if (!block) throw new Error(`Ien${mode}ColorTokens를 찾을 수 없습니다.`);
  const colors = Object.fromEntries(
    Array.from(block[1].matchAll(/val (\w+) = Color\(0x([\dA-Fa-f]{8})\)/g), (match) => {
      const argb = match[2].toUpperCase();
      return [match[1], "#" + argb.slice(2) + (argb.startsWith("FF") ? "" : argb.slice(0, 2))];
    }),
  );
  if (Object.keys(colors).length !== colorRoles.length || colorRoles.some((role) => !colors[role])) {
    throw new Error(`Ien${mode}ColorTokens 필드가 IenColorScheme과 다릅니다.`);
  }
  return colors;
}
await writeFile(
  path.join(projectDirectory, "web/generated-color-scheme.json"),
  JSON.stringify({ light: extractColorTokens("Light"), dark: extractColorTokens("Dark"), descriptions }, null, 2) + "\n",
);

const designScreenSource = await readFile(
  path.join(
    repositoryDirectory,
    "example/composeApp/src/commonMain/kotlin/zone/ien/utils/example/ui/screens/designsystem/DesignSystemScreen.kt",
  ),
  "utf8",
);
const designLines = designScreenSource.split(/\r?\n/);
const mapping = {};
const mappingRegex = /"([a-z0-9-]+)"(?:,\s*"([a-z0-9-]+)")?\s*->\s*([A-Za-z0-9]+)\(/g;
let mappingMatch;
while ((mappingMatch = mappingRegex.exec(designScreenSource)) !== null) {
  mapping[mappingMatch[1]] = mappingMatch[3];
  if (mappingMatch[2]) mapping[mappingMatch[2]] = mappingMatch[3];
}
const sampleLines = {};
for (const [id, fnName] of Object.entries(mapping)) {
  const lineIndex = designLines.findIndex((line) =>
    new RegExp("\\bfun\\s+" + fnName + "\\b").test(line),
  );
  sampleLines[id] = lineIndex !== -1 ? lineIndex + 1 : 1;
}
await writeFile(
  path.join(projectDirectory, "web/generated-sample-lines.js"),
  `// Auto-generated by scripts/generate-api-docs.mjs. Do not edit.\nexport const sampleLines = ${JSON.stringify(sampleLines, null, 2)};\n`,
);

const themeSource = await readFile(
  path.join(repositoryDirectory, "cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/foundation/IenTheme.kt"),
  "utf8",
);
const webTokens = JSON.parse(await readFile(
  path.join(repositoryDirectory, "cmp-ui-web-registry/tokens.json"), "utf8",
));
const kebabCase = (name) => name.replace(/[A-Z]/g, (letter) => `-${letter.toLowerCase()}`);
const rem = (pixels) => `${pixels / 16}rem`;

function defaultTokenBlock(className) {
  const block = themeSource.match(new RegExp(`data class ${className}\\(([\\s\\S]*?)\\n\\)`))?.[1];
  if (!block) throw new Error(`${className} 선언을 찾을 수 없습니다.`);
  return block;
}

function dimensionTokens(className) {
  const block = defaultTokenBlock(className);
  const fields = Array.from(block.matchAll(/val (\w+): Dp = ([\d.]+)\.dp/g));
  if (fields.length === 0 || fields.length !== Array.from(block.matchAll(/val \w+:/g)).length) {
    throw new Error(`${className}의 모든 기본값을 읽지 못했습니다.`);
  }
  return Object.fromEntries(fields.map((match) => [match[1], Number(match[2])]));
}

const typographyBlock = themeSource.match(/private fun defaultIenTypography\(\) = IenTypography\(([\s\S]*?)\n\)/)?.[1];
if (!typographyBlock) throw new Error("기본 IenTypography를 찾을 수 없습니다.");
const fontWeights = { Normal: 400, SemiBold: 600, Bold: 700 };
const typography = Object.fromEntries(Array.from(
  typographyBlock.matchAll(/(\w+) = TextStyle\(fontSize = ([\d.]+)\.sp, lineHeight = ([\d.]+)\.sp, fontWeight = FontWeight\.(\w+)\)/g),
  (match) => {
    if (!fontWeights[match[4]]) throw new Error(`지원하지 않는 글꼴 굵기입니다: ${match[4]}`);
    return [match[1], { fontSize: Number(match[2]), lineHeight: Number(match[3]), fontWeight: fontWeights[match[4]] }];
  },
));
const typographyFields = Array.from(defaultTokenBlock("IenTypography").matchAll(/val (\w+): TextStyle/g), (match) => match[1]);
if (typographyFields.length !== Object.keys(typography).length || typographyFields.some((name) => !typography[name])) {
  throw new Error("IenTypography 필드와 추출한 기본 스타일이 다릅니다.");
}
const webTypography = webTokens.typography;
if (!webTypography || typographyFields.some((name) => !webTypography[name]) || Object.keys(webTypography).length !== typographyFields.length) {
  throw new Error("웹 타이포그래피 단계가 IenTypography와 다릅니다.");
}
const motionBlock = defaultTokenBlock("IenMotion");
const motion = Object.fromEntries(Array.from(motionBlock.matchAll(/val (\w+Millis): Int = (\d+)/g), (match) => [match[1], Number(match[2])]));
const easing = motionBlock.match(/CubicBezierEasing\(([^)]+)\)/)?.[1];
if (Object.keys(motion).length !== 4 || !easing) throw new Error("IenMotion 기본값을 읽지 못했습니다.");
motion.standardEasing = easing.split(",").map((value) => Number(value.trim().replace(/f$/, "")));
if (motion.standardEasing.length !== 4 || motion.standardEasing.some((value) => !Number.isFinite(value))) {
  throw new Error("IenMotion 감속 곡선이 올바르지 않습니다.");
}
const stateBlock = defaultTokenBlock("IenStateTokens");
const state = Object.fromEntries(Array.from(stateBlock.matchAll(/val (\w+): (?:Float|Dp) = ([\d.]+)(?:f|\.dp)/g), (match) => [match[1], Number(match[2])]));
if (Object.keys(state).length !== Array.from(stateBlock.matchAll(/val \w+:/g)).length) {
  throw new Error("IenStateTokens 기본값을 읽지 못했습니다.");
}
const designTokens = {
  spacing: dimensionTokens("IenSpacing"),
  radius: dimensionTokens("IenRadius"),
  stroke: dimensionTokens("IenStroke"),
  elevation: dimensionTokens("IenElevation"),
  icon: dimensionTokens("IenIconSize"),
  typography,
  motion,
  state,
  web: webTokens,
};
const variables = {};
const tailwindTheme = {};
for (const group of ["spacing", "radius", "stroke", "elevation", "icon"]) {
  for (const [name, value] of Object.entries(designTokens[group])) {
    const variable = `ien-${group}-${kebabCase(name)}`;
    variables[variable] = rem(value);
    if (group === "spacing" || group === "radius") {
      tailwindTheme[`${group}-ien-${name}`] = `var(--${variable})`;
    }
  }
}
for (const [name, style] of Object.entries(webTypography)) {
  if (![style.fontSize, style.lineHeight, style.fontWeight].every((value) => Number.isFinite(value) && value > 0)) {
    throw new Error(`웹 타이포그래피 ${name} 값이 올바르지 않습니다.`);
  }
  variables[`ien-font-size-${name}`] = rem(style.fontSize);
  variables[`ien-line-height-${name}`] = rem(style.lineHeight);
  variables[`ien-font-weight-${name}`] = String(style.fontWeight);
  tailwindTheme[`text-ien-${name}`] = `var(--ien-font-size-${name})`;
  tailwindTheme[`text-ien-${name}--line-height`] = `var(--ien-line-height-${name})`;
  tailwindTheme[`text-ien-${name}--font-weight`] = `var(--ien-font-weight-${name})`;
}
for (const [name, value] of Object.entries(motion)) {
  variables[`ien-motion-${kebabCase(name.replace(/Millis$/, ""))}`] = name === "standardEasing"
    ? `cubic-bezier(${value.join(", ")})` : `${value}ms`;
}
for (const [name, value] of Object.entries(state)) {
  variables[`ien-state-${kebabCase(name)}`] = name === "minimumTouchTarget" ? rem(value) : String(value);
}
for (const group of ["font", "control", "button"]) {
  for (const [name, value] of Object.entries(webTokens[group])) {
    if (group === "button" && name === "press-easing") continue;
    variables[`ien-${group}-${name}`] = value;
  }
}
const spring = webTokens.button["press-easing"];
const pressDuration = Number.parseFloat(webTokens.button["press-duration"]);
if (!/^\d+(?:\.\d+)?ms$/.test(webTokens.button["press-duration"]) || spring.dampingRatio !== 1 || spring.stiffness <= 0 || spring.sampleCount < 2 || !Number.isFinite(pressDuration)) {
  throw new Error("버튼 스프링 토큰이 올바르지 않습니다.");
}
variables["ien-button-press-easing"] = `linear(${Array.from({ length: spring.sampleCount }, (_, index) => {
  if (index === spring.sampleCount - 1) return 1;
  const time = (index / (spring.sampleCount - 1)) * pressDuration / 1000;
  const frequency = Math.sqrt(spring.stiffness);
  return (1 - (1 + frequency * time) * Math.exp(-frequency * time)).toFixed(6);
}).join(", ")})`;
const springDuration = Number.parseFloat(webTokens.motion["spring-duration"]);
for (const [name, value] of Object.entries(webTokens.motion)) {
  if (typeof value === "string") {
    variables[`ien-motion-${name}`] = value;
    continue;
  }
  if (value.dampingRatio <= 0 || value.dampingRatio >= 1 || value.stiffness <= 0 || value.sampleCount < 2 || !Number.isFinite(springDuration)) {
    throw new Error(`웹 스프링 ${name} 설정이 올바르지 않습니다.`);
  }
  const frequency = Math.sqrt(value.stiffness);
  const damped = frequency * Math.sqrt(1 - value.dampingRatio ** 2);
  variables[`ien-motion-${name}`] = `linear(${Array.from({ length: value.sampleCount }, (_, index) => {
    if (index === value.sampleCount - 1) return 1;
    const time = index / (value.sampleCount - 1) * springDuration / 1000;
    const response = 1 - Math.exp(-value.dampingRatio * frequency * time) * (Math.cos(damped * time) + value.dampingRatio * frequency / damped * Math.sin(damped * time));
    return response.toFixed(6);
  }).join(", ")})`;
}
const semanticAliases = {
  "ien-neutral": "var(--ien-text-primary)",
  "ien-on-neutral": "var(--ien-surface-raised)",
  "ien-neutral-weak": "var(--ien-surface-weak)",
};
function colorVariables(mode) {
  return {
    ...Object.fromEntries(Object.entries(extractColorTokens(mode)).map(([name, value]) => [`ien-${kebabCase(name)}`, value])),
    ...semanticAliases,
    ...Object.fromEntries(Object.entries(webTokens.docs.syntax).filter(([name]) => name.startsWith(`${mode.toLowerCase()}-`)).map(([name, value]) => [`ien-syntax-${name.slice(mode.length + 1)}`, value])),
  };
}
for (const name of Object.keys(webTokens.docs.syntax).filter((name) => name.startsWith("light-"))) {
  tailwindTheme[`color-ien-syntax-${name.slice(6)}`] = `var(--ien-syntax-${name.slice(6)})`;
}
for (const name of colorRoles) tailwindTheme[`color-ien-${kebabCase(name)}`] = `var(--ien-${kebabCase(name)})`;
for (const name of Object.keys(webTokens.font)) tailwindTheme[`font-ien-${name}`] = `var(--ien-font-${name})`;
const tokenCssVars = { theme: tailwindTheme, light: { ...variables, ...colorVariables("Light") }, dark: colorVariables("Dark") };
// 소비 프로젝트에도 문서 프리뷰와 동일한 CMP 토큰을 설치합니다.
const registryManifestPath = path.join(repositoryDirectory, "cmp-ui-web-registry/registry.json");
const registryManifest = JSON.parse(await readFile(registryManifestPath, "utf8"));
const ienThemeItem = registryManifest.items.find((item) => item.name === "ien-theme");
if (!ienThemeItem) throw new Error("IEN 공통 테마 항목이 없습니다.");
const themeAliases = {
  background: "surface", foreground: "text-primary", card: "surface-raised", "card-foreground": "text-primary",
  popover: "surface-raised", "popover-foreground": "text-primary", primary: "brand", "primary-foreground": "on-brand",
  secondary: "surface-weak", "secondary-foreground": "text-primary", muted: "surface-weak", "muted-foreground": "text-secondary",
  accent: "brand-weak", "accent-foreground": "brand", destructive: "danger", "destructive-foreground": "on-danger",
  success: "success", "success-foreground": "on-success", warning: "warning", "warning-foreground": "on-warning",
  info: "info", "info-foreground": "on-info", invert: "text-primary", "invert-foreground": "surface",
  border: "border", input: "border", ring: "brand",
};
const aliases = Object.fromEntries(Object.entries(themeAliases).map(([name, role]) => [name, `var(--ien-${role})`]));
ienThemeItem.cssVars = {
  theme: {
    ...tailwindTheme,
    ...Object.fromEntries(Object.keys(themeAliases).map((name) => [`color-${name}`, `var(--${name})`])),
    ...Object.fromEntries(["sm", "md", "lg", "xl", "2xl", "3xl", "4xl"].map((name) => [`radius-${name}`, `var(--ien-control-radius-${name})`])),
    "font-sans": "var(--ien-font-sans)",
  },
  light: { ...tokenCssVars.light, ...aliases },
  dark: { ...tokenCssVars.dark, ...aliases },
};
ienThemeItem.css = webTokens.components;
// REUI 원본의 보조 팔레트가 CMP의 의미적 색상을 덮어쓰지 않게 합니다.
for (const item of registryManifest.items) {
  if (item.name === "ien-theme") continue;
  if (item.meta?.preview) {
    item.meta.example = await readFile(path.join(repositoryDirectory, "cmp-ui-web-registry", item.meta.preview), "utf8");
  }
  for (const mode of ["light", "dark"]) {
    for (const name of Object.keys(item.cssVars?.[mode] ?? {})) {
      if (name in aliases) item.cssVars[mode][name] = aliases[name];
    }
  }
}
await writeFile(registryManifestPath, JSON.stringify(registryManifest, null, 2) + "\n");
await writeFile(path.join(projectDirectory, "web/generated-design-tokens.json"), JSON.stringify(designTokens, null, 2) + "\n");
function declarations(tokens) {
  return Object.entries(tokens).map(([name, value]) => `  --${name}: ${value};`).join("\n");
}
const docsVariables = {};
for (const group of ["layout", "font", "font-size", "font-weight", "line-height", "spacing", "radius", "motion", "syntax", "palette", "meta"]) {
  for (const [name, value] of Object.entries(webTokens.docs[group])) docsVariables[`docs-${group}-${name.replaceAll(".", "-")}`] = value;
}
const docsColorVariables = (mode) => Object.fromEntries(Object.entries(webTokens.docs.theme[mode]).map(([name, value]) => [`docs-color-${name}`, value]));
await writeFile(
  path.join(projectDirectory, "web/generated-design-tokens.css"),
  `/* CMP IenTheme와 웹 tokens.json에서 자동 생성합니다. */\n:root {\n${declarations({ ...tokenCssVars.light, ...docsVariables, ...docsColorVariables("light") })}\n}\n\n.dark {\n${declarations({ ...tokenCssVars.dark, ...docsColorVariables("dark") })}\n}\n\n@theme inline {\n${declarations(tailwindTheme)}\n}\n`,
);

const { collectRegistry, validateRegistry, createWebCatalog, registryModuleAliases, sourceApi } = await import("./registry.mjs");
const allItems = await collectRegistry(path.join(repositoryDirectory, "registry.json"), repositoryDirectory);
validateRegistry(allItems);
const registryVariables = { theme: {}, light: {}, dark: {} };
for (const item of allItems) {
  for (const mode of Object.keys(registryVariables)) Object.assign(registryVariables[mode], item.cssVars?.[mode]);
}
const tokenCssFile = path.join(projectDirectory, "web/generated-design-tokens.css");
await writeFile(tokenCssFile, (await readFile(tokenCssFile, "utf8")) + `\n:root {\n${declarations(registryVariables.light)}\n}\n.dark {\n${declarations(registryVariables.dark)}\n}\n@theme inline {\n${declarations(registryVariables.theme)}\n}\n`);
await writeFile(path.join(projectDirectory, "web/generated-web-catalog.json"), JSON.stringify(createWebCatalog(allItems, catalog, repositoryDirectory), null, 2) + "\n");
const modulePaths = Object.fromEntries(Object.entries(registryModuleAliases(allItems)).map(([name, source]) => [name, [path.relative(projectDirectory, source)]]));
const packageImports = new Set();
for (const item of allItems) {
  const sources = item.files.map((file) => file.content);
  if (item.meta?.preview) sources.push(await readFile(path.resolve(path.dirname(item.manifestPath), item.meta.preview), "utf8"));
  for (const source of sources) {
    for (const name of sourceApi(source, item.name).imports) if (!name.startsWith(".") && !name.startsWith("@/")) packageImports.add(name);
    for (const match of source.matchAll(/import\(["']([^"']+)["']\)/g)) if (!match[1].startsWith(".")) packageImports.add(match[1]);
  }
}
const packagePaths = {};
function typeExport(value) {
  if (typeof value === "string") return value;
  if (!value || typeof value !== "object") return undefined;
  return typeExport(value.types) ?? typeExport(value.import) ?? typeExport(value.default) ?? typeExport(value.require);
}
for (const name of packageImports) {
  const packageName = name.startsWith("@") ? name.split("/").slice(0, 2).join("/") : name.split("/")[0];
  const packageDirectory = path.join(projectDirectory, "node_modules", packageName);
  const metadata = JSON.parse(await readFile(path.join(packageDirectory, "package.json"), "utf8"));
  const subpath = name === packageName ? "." : `.${name.slice(packageName.length)}`;
  let entry = subpath === "." ? metadata.types ?? metadata.typings ?? typeExport(metadata.exports?.["."] ?? metadata.exports) ?? metadata.main : typeExport(metadata.exports?.[subpath]);
  if (!entry) for (const [pattern, value] of Object.entries(metadata.exports ?? {})) {
    if (!pattern.includes("*")) continue;
    const [prefix, suffix] = pattern.split("*");
    if (subpath.startsWith(prefix) && subpath.endsWith(suffix)) {
      entry = typeExport(value)?.replaceAll("*", subpath.slice(prefix.length, suffix.length ? -suffix.length : undefined));
      break;
    }
  }
  if (!entry) throw new Error(`레지스트리 패키지의 타입 경로가 없습니다: ${name}`);
  const declaration = entry.replace(/\.mjs$/, ".d.mts").replace(/\.cjs$/, ".d.cts").replace(/(?<!\.d)\.js$/, ".d.ts");
  if (declaration !== entry) {
    try { await access(path.resolve(packageDirectory, declaration)); entry = declaration; }
    catch (error) { if (error.code !== "ENOENT") throw error; }
  }
  packagePaths[name] = [`./${path.relative(projectDirectory, path.resolve(packageDirectory, entry))}`];
}
await writeFile(path.join(projectDirectory, "tsconfig.registry.json"), JSON.stringify({ compilerOptions: { paths: {
  ...modulePaths,
  ...packagePaths,
  "@/*": ["./web/*"],
  "cn/config": ["./node_modules/cn/dist/config.d.ts"],
  "react": ["./node_modules/@types/react"],
  "react/*": ["./node_modules/@types/react/*"],
  "react-dom": ["./node_modules/@types/react-dom"],
  "react-dom/*": ["./node_modules/@types/react-dom/*"],
  "*": ["./node_modules/*"],
} } }, null, 2) + "\n");
function registryCss(rules) {
  return Object.entries(rules).map(([name, value]) => value && typeof value === "object"
    ? `${name} {\n${registryCss(value)}\n}`
    : `  ${name}: ${value};`).join("\n");
}
const componentCss = allItems.filter((item) => item.css).map((item) => registryCss(item.css)).join("\n");
if (componentCss) {
  const cssFile = path.join(projectDirectory, "web/generated-design-tokens.css");
  await writeFile(cssFile, (await readFile(cssFile, "utf8")) + "\n" + componentCss + "\n");
}
const registrySources = {};
for (const item of allItems) {
  for (const file of item.files) {
    registrySources[path.relative(repositoryDirectory, file.absolutePath).split(path.sep).join("/")] = file.content;
  }
  if (item.meta?.preview) {
    const file = path.resolve(path.dirname(item.manifestPath), item.meta.preview);
    registrySources[path.relative(repositoryDirectory, file).split(path.sep).join("/")] = await readFile(file, "utf8");
  }
}
await writeFile(path.join(projectDirectory, "web/generated-web-sources.json"), JSON.stringify(registrySources) + "\n");
