import { mkdir, readFile, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { catalog } from "../web/catalog.js";

const projectDirectory = fileURLToPath(new URL("../", import.meta.url));
const repositoryDirectory = path.resolve(projectDirectory, "..");
const outputFile = path.join(projectDirectory, "web/generated-api-docs.json");

function findDeclaration(source, name) {
  const declarationPattern = new RegExp(
    "\\b(?:fun\\s+(?:<[^>]+>\\s*)?(?:[\\w?.<>]+\\.)?|(?:(?:data|sealed|enum|value)\\s+)?(?:class|interface|object)\\s+)" +
      name +
      "\\b",
  );
  return declarationPattern.exec(source);
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

function extractApi(source, name, fallbackDescription) {
  const declaration = findDeclaration(source, name);
  if (!declaration) {
    return {
      name,
      signature: "",
      description: fallbackDescription,
      parameters: [],
    };
  }

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
  apiDocs[component.id] = component.api.map((name) =>
    extractApi(source, name, component.description),
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
