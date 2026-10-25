import { Skeleton, skeletonListRowTextsTypes, type SkeletonElement, type SkeletonBackground, type SkeletonPlay, type SkeletonRepeat } from "@/components/ui/skeleton";
import { CodeExample } from "./RegistryCodeExample";
import { Typography } from "@/components/ui/typography";
import { useState } from "react";
import type { CSSProperties } from "react";
import { Badge } from "@/components/reui/badge";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { ChevronDown, ChevronUp, Copy, Plus, RotateCcw, Trash2 } from "lucide-react";
import { DropdownMenu } from "radix-ui";
import { highlightKotlin } from "./ColorSchemeBuilder";
import { copyTextToClipboard } from "./clipboard";
import designTokens from "./generated-design-tokens.json";

type PresetKind = "Title" | "Subtitle" | "List" | "ListWithIcon" | "Card";
type ElementKind = "Block" | "Row" | "Column" | "Box" | "Spacer" | PresetKind | "ListRowTexts";
const listRowTextsTypes = skeletonListRowTextsTypes;
type ListRowTextsType = typeof listRowTextsTypes[number];
type BoxAlignment = "Center" | "TopStart" | "BottomEnd";
type WidthValue =
  | { mode: "fill" }
  | { mode: "fraction"; fraction: number }
  | { mode: "dp"; value: number }
  | { mode: "radius"; token: RadiusToken; multiplier: number };
type SpacingToken = keyof typeof designTokens.spacing;
type SpacingValue =
  | { unit: "dp"; value: number }
  | { unit: "token"; token: SpacingToken }
  | RadiusHeight;

type TypographyToken = keyof typeof designTokens.typography;

const radiusTokenValues = designTokens.radius;
type RadiusToken = keyof typeof radiusTokenValues;
type RadiusHeight = { unit: "radius"; token: RadiusToken; multiplier: number };
type HeightValue = number | RadiusHeight | { unit: "typography"; token: TypographyToken };

interface BlockNode {
  id: string;
  type: "Block";
  width: WidthValue;
  height: HeightValue;
  shape: "Rounded" | "Circle";
}

interface SpacerNode {
  id: string;
  type: "Spacer";
  width: SpacingValue;
  height: SpacingValue;
}

interface ContainerNodeBase {
  id: string;
  width: WidthValue;
  height: HeightValue | null;
  spacing: SpacingValue;
  children: SkeletonNode[];
}

interface RowNode extends ContainerNodeBase {
  type: "Row";
}

interface ColumnNode extends ContainerNodeBase {
  type: "Column";
}

interface BoxNode extends ContainerNodeBase {
  type: "Box";
  alignment: BoxAlignment;
}

type ContainerNode = RowNode | ColumnNode | BoxNode;
interface PresetNode {
  id: string;
  type: PresetKind;
}

interface ListRowTextsNode {
  id: string;
  type: "ListRowTexts";
  textType: ListRowTextsType;
}

type SkeletonNode = BlockNode | SpacerNode | ContainerNode | PresetNode | ListRowTextsNode;
type PreviewLayout = "Root" | ElementKind;

const elementLabels: Record<ElementKind, string> = {
  Block: "블록",
  Row: "행",
  Column: "열",
  Box: "겹치기",
  Spacer: "간격",
  Title: "제목 (Title)",
  Subtitle: "부제목 (Subtitle)",
  List: "리스트 (List)",
  ListWithIcon: "아이콘 리스트 (ListWithIcon)",
  Card: "카드 (Card)",
  ListRowTexts: "리스트 행 텍스트 (ListRowTexts)",
};
const elementKinds: ElementKind[] = [
  "Block", "Row", "Column", "Box", "Spacer",
  "Title", "Subtitle", "List", "ListWithIcon", "Card", "ListRowTexts",
];
const spacingTokenValues = designTokens.spacing;
const spacingTokens = Object.keys(spacingTokenValues) as SpacingToken[];
const typographyLineHeights = Object.fromEntries(
  Object.entries(designTokens.typography).map(([name, style]) => [name, style.lineHeight]),
) as Record<TypographyToken, number>;

let nextNodeId = 0;

function createNodeId(): string {
  nextNodeId += 1;
  return `skeleton-element-${nextNodeId}`;
}

function createNode(type: ElementKind): SkeletonNode {
  const id = createNodeId();
  switch (type) {
    case "Title":
    case "Subtitle":
    case "List":
    case "ListWithIcon":
    case "Card":
      return { id, type };
    case "ListRowTexts":
      return { id, type, textType: "OneRowTypeA" };
    case "Block":
      return {
        id,
        type,
        width: { mode: "fill" },
        height: radiusHeightFor(radiusTokenValues.default),
        shape: "Rounded",
      };
    case "Spacer":
      return {
        id,
        type,
        width: { unit: "dp", value: 0 },
        height: { unit: "dp", value: 12 },
      };
    case "Row":
    case "Column":
      return {
        id,
        type,
        width: { mode: "fill" },
        height: null,
        spacing: { unit: "token", token: "xs" },
        children: [],
      };
    case "Box":
      return {
        id,
        type,
        width: { mode: "dp", value: 180 },
        height: 112,
        spacing: { unit: "token", token: "none" },
        alignment: "Center",
        children: [
          {
            id: createNodeId(),
            type: "Block",
            width: { mode: "fill" },
            height: 112,
            shape: "Rounded",
          },
        ],
      };
  }
}

function createDefaultNodes(): SkeletonNode[] {
  return [];
}

function updateNodeTree(
  nodes: SkeletonNode[],
  targetId: string,
  update: (node: SkeletonNode) => SkeletonNode,
): SkeletonNode[] {
  return nodes.map((node) => {
    if (node.id === targetId) return update(node);
    if (isContainer(node)) {
      return {
        ...node,
        children: updateNodeTree(node.children, targetId, update),
      };
    }
    return node;
  });
}

function removeNodeTree(nodes: SkeletonNode[], targetId: string): SkeletonNode[] {
  return nodes
    .filter((node) => node.id !== targetId)
    .map((node) =>
      isContainer(node)
        ? { ...node, children: removeNodeTree(node.children, targetId) }
        : node,
    );
}

function moveNodeTree(
  nodes: SkeletonNode[],
  targetId: string,
  offset: -1 | 1,
): SkeletonNode[] {
  const currentIndex = nodes.findIndex((node) => node.id === targetId);
  if (currentIndex !== -1) {
    const targetIndex = Math.max(0, Math.min(nodes.length - 1, currentIndex + offset));
    if (targetIndex === currentIndex) return nodes;
    const next = [...nodes];
    const [moved] = next.splice(currentIndex, 1);
    if (!moved) return nodes;
    next.splice(targetIndex, 0, moved);
    return next;
  }
  return nodes.map((node) =>
    isContainer(node)
      ? { ...node, children: moveNodeTree(node.children, targetId, offset) }
      : node,
  );
}

function isContainer(node: SkeletonNode): node is ContainerNode {
  return node.type === "Row" || node.type === "Column" || node.type === "Box";
}

function numberValue(value: string): number | null {
  if (value.trim() === "") return null;
  const parsed = Number(value);
  if (!Number.isFinite(parsed)) return null;
  return Math.max(0, Math.min(640, Math.round(parsed)));
}

function widthForMode(current: WidthValue, mode: string): WidthValue {
  if (mode === "radius") {
    if (current.mode === "radius") return current;
    const radius = radiusHeightFor(current.mode === "dp" ? current.value : 40);
    return { mode: "radius", token: radius.token, multiplier: radius.multiplier };
  }
  if (mode === "fraction") {
    return current.mode === "fraction"
      ? current
      : { mode: "fraction", fraction: 0.5 };
  }
  if (mode === "dp") {
    return current.mode === "dp"
      ? current
      : { mode: "dp", value: current.mode === "radius" ? heightPixels({ ...current, unit: "radius" }) : 40 };
  }
  return { mode: "fill" };
}

function spacingPixels(spacing: SpacingValue): number {
  if (spacing.unit === "radius") return heightPixels(spacing);
  return spacing.unit === "dp"
    ? spacing.value
    : spacingTokenValues[spacing.token];
}

function spacingExpression(spacing: SpacingValue): string {
  if (spacing.unit === "radius") return heightExpression(spacing);
  return spacing.unit === "dp"
    ? `${spacing.value}.dp`
    : `IenTheme.spacing.${spacing.token}`;
}

function fillMaxWidthExpression(fraction: number): string {
  return fraction >= 1 ? "fillMaxWidth()" : `fillMaxWidth(${fraction}f)`;
}

function spacingTokenFor(value: SpacingValue): SpacingToken {
  if (value.unit === "token") return value.token;
  return spacingTokens.reduce((closest, token) =>
    Math.abs(spacingTokenValues[token] - spacingPixels(value)) <
    Math.abs(spacingTokenValues[closest] - spacingPixels(value))
      ? token
      : closest,
  );
}

function blockShapeValue(value: string): BlockNode["shape"] {
  return value === "Circle" ? "Circle" : "Rounded";
}

function boxAlignmentValue(value: string): BoxAlignment {
  if (value === "TopStart" || value === "BottomEnd") return value;
  return "Center";
}

function heightPixels(height: HeightValue): number {
  return typeof height === "number"
    ? height
    : height.unit === "typography"
      ? typographyLineHeights[height.token]
      : radiusTokenValues[height.token] * height.multiplier;
}

function heightExpression(height: HeightValue): string {
  if (typeof height === "number") return `${height}.dp`;
  if (height.unit === "typography") {
    return `with(LocalDensity.current) { IenTheme.typography.${height.token}.lineHeight.toDp() }`;
  }
  return `IenTheme.radius.${height.token} * ${height.multiplier}`;
}

function radiusHeightFor(pixels: number): RadiusHeight {
  return {
    unit: "radius",
    token: "default",
    multiplier: Math.max(1, Math.round(pixels / radiusTokenValues.default)),
  };
}

function elementModifier(node: ContainerNode): string | null {
  const modifiers: string[] = [];
  switch (node.width.mode) {
    case "dp":
      modifiers.push(`width(${node.width.value}.dp)`);
      break;
    case "radius":
      modifiers.push(`width(${heightExpression({ ...node.width, unit: "radius" })})`);
      break;
    case "fraction":
      modifiers.push(fillMaxWidthExpression(node.width.fraction));
      break;
    case "fill":
      modifiers.push("fillMaxWidth()");
      break;
  }
  if (node.height !== null) modifiers.push(`height(${heightExpression(node.height)})`);
  return modifiers.length > 0 ? `Modifier.${modifiers.join(".")}` : null;
}

function generateNodeCode(node: SkeletonNode, indentLevel: number): string {
  const indent = "    ".repeat(indentLevel);
  if (node.type === "Block") {
    const parameters = [
      typeof node.height === "object" && node.height.unit === "typography"
        ? `typography = IenTheme.typography.${node.height.token}`
        : `height = ${heightExpression(node.height)}`,
    ];
    if (node.width.mode === "dp") {
      parameters.push(`width = ${node.width.value}.dp`);
    } else if (node.width.mode === "radius") {
      parameters.push(`width = ${heightExpression({ ...node.width, unit: "radius" })}`);
    }
    if (node.shape === "Circle") parameters.push("shape = CircleShape");
    if (node.width.mode === "fraction") {
      parameters.push(`modifier = Modifier.${fillMaxWidthExpression(node.width.fraction)}`);
    }
    return `IenSkeletonElement.Block(\n${parameters
      .map((parameter) => `${indent}    ${parameter},`)
      .join("\n")}\n${indent})`;
  }
  if (node.type === "Spacer") {
    const parameters = [`height = ${spacingExpression(node.height)}`];
    if (spacingPixels(node.width) > 0 || node.width.unit === "token") {
      parameters.push(`width = ${spacingExpression(node.width)}`);
    }
    return `IenSkeletonElement.Spacer(${parameters.join(", ")})`;
  }

  if (!isContainer(node)) {
    return node.type === "ListRowTexts"
      ? `IenSkeletonElement.ListRowTexts(type = IenListRowTextsType.${node.textType})`
      : `IenSkeletonElement.${node.type}`;
  }

  const parameters: string[] = [];
  const modifier = elementModifier(node);
  if (node.type === "Row") {
    parameters.push(
      `horizontalArrangement = Arrangement.spacedBy(${spacingExpression(node.spacing)})`,
    );
    parameters.push("verticalAlignment = Alignment.CenterVertically");
  } else if (node.type === "Column") {
    parameters.push(
      `verticalArrangement = Arrangement.spacedBy(${spacingExpression(node.spacing)})`,
    );
  } else {
    parameters.push(`contentAlignment = Alignment.${node.alignment}`);
  }

  if (node.children.length === 0) {
    parameters.push("elements = emptyList()");
  } else {
    const childIndent = "    ".repeat(indentLevel + 2);
    parameters.push(
      `elements = listOf(\n${node.children
        .map((child) => `${childIndent}${generateNodeCode(child, indentLevel + 2)}`)
        .join(",\n")}\n${indent}    )`,
    );
  }
  if (modifier) parameters.push(`modifier = ${modifier}`);

  return `IenSkeletonElement.${node.type}(\n${parameters
    .map((parameter) => `${indent}    ${parameter},`)
    .join("\n")}\n${indent})`;
}

function generateKotlinCode(nodes: SkeletonNode[]): string {
  const elements = nodes
    .map((node) => `        ${generateNodeCode(node, 2)}`)
    .join(",\n");
  const imports = [
    "import androidx.compose.foundation.layout.Arrangement",
    "import androidx.compose.foundation.layout.fillMaxWidth",
    "import androidx.compose.foundation.layout.height",
    "import androidx.compose.foundation.layout.width",
    "import androidx.compose.foundation.shape.CircleShape",
    "import androidx.compose.ui.Alignment",
    "import androidx.compose.ui.Modifier",
    "import androidx.compose.ui.unit.dp",
    "import zone.ien.utils.ui.feedback.IenSkeleton",
    "import zone.ien.utils.ui.feedback.IenSkeletonElement",
    "import zone.ien.utils.ui.feedback.IenSkeletonRepeat",
  ];
  if (containsListRowTexts(nodes)) {
    imports.push("import zone.ien.utils.ui.list.IenListRowTextsType");
  }
  if (containsThemeToken(nodes)) {
    imports.push("import zone.ien.utils.ui.foundation.IenTheme");
  }
  if (containsTypographyHeight(nodes)) {
    imports.push("import androidx.compose.ui.platform.LocalDensity");
  }
  return [
    ...imports,
    "",
    "IenSkeleton(",
    "    custom = listOf(",
    elements,
    "    ),",
    "    repeatLastItemCount = IenSkeletonRepeat.Count(1),",
    "    modifier = Modifier.fillMaxWidth(),",
    ")",
  ].join("\n");
}

function containsListRowTexts(nodes: SkeletonNode[]): boolean {
  return nodes.some((node) => node.type === "ListRowTexts" ||
    (isContainer(node) && containsListRowTexts(node.children)));
}

function containsThemeToken(nodes: SkeletonNode[]): boolean {
  return nodes.some((node) => {
    if (node.type === "Spacer") {
      return node.width.unit !== "dp" || node.height.unit !== "dp";
    }
    if (node.type !== "Block" && !isContainer(node)) return false;
    if (node.width.mode === "radius") return true;
    if (node.height !== null && typeof node.height !== "number") return true;
    if (isContainer(node)) {
      const usesToken =
        (node.type === "Row" || node.type === "Column") &&
        node.spacing.unit === "token";
      return usesToken || containsThemeToken(node.children);
    }
    return false;
  });
}

function containsTypographyHeight(nodes: SkeletonNode[]): boolean {
  return nodes.some((node) => {
    if (node.type === "Spacer") return false;
    if (!isContainer(node)) return false;
    if (typeof node.height === "object" && node.height?.unit === "typography") {
      return true;
    }
    return isContainer(node) && containsTypographyHeight(node.children);
  });
}

function previewStyle(
  node: BlockNode | ContainerNode,
  parentLayout: PreviewLayout,
): CSSProperties {
  const style: CSSProperties = {};
  switch (node.width.mode) {
    case "dp":
      style.width = `${node.width.value}px`;
      break;
    case "radius":
      style.width = `${heightPixels({ ...node.width, unit: "radius" })}px`;
      break;
    case "fraction":
      style.width = `${node.width.fraction * 100}%`;
      break;
    case "fill":
      if (parentLayout === "Row") style.flex = "1 1 0%";
      else style.width = "100%";
      break;
  }
  if (node.height !== null) style.height = `${heightPixels(node.height)}px`;
  return style;
}

function SkeletonPreviewNode({
  node,
  parentLayout,
  index,
}: {
  node: SkeletonNode;
  parentLayout: PreviewLayout;
  index: number;
}) {
  if (node.type === "Spacer") {
    return (
      <div
        aria-hidden="true"
        className="skeleton-preview-spacer"
        style={{
          width: `${spacingPixels(node.width)}px`,
          height: `${spacingPixels(node.height)}px`,
        }}
      />
    );
  }
  if (node.type === "Block") {
    return (
      <div
        aria-hidden="true"
        className={`skeleton-preview-block${node.shape === "Circle" ? " circle" : ""}`}
        style={previewStyle(node, parentLayout)}
      />
    );
  }

  if (!isContainer(node)) {
    const block = (width: string, height: number, circle = false) => (
      <div
        aria-hidden="true"
        className={`skeleton-preview-block${circle ? " circle" : ""}`}
        style={{ width, height, flexShrink: 0 }}
      />
    );
    if (node.type === "Title") return block("42%", 24);
    if (node.type === "Subtitle") return block("34%", 16);
    if (node.type === "Card") {
      return <div aria-hidden="true" className="skeleton-preview-block" style={{ width: "100%", height: 132, borderRadius: radiusTokenValues.default, flexShrink: 0 }} />;
    }
    const widthStyle: CSSProperties = parentLayout === "Row"
      ? { flex: "1 1 0%", minWidth: 0 }
      : { width: "100%" };
    if (node.type === "List") {
      return (
        <div style={{ ...widthStyle, height: 48, display: "flex", alignItems: "center" }}>
          {block(`${[92, 78, 86][index % 3]}%`, 18)}
        </div>
      );
    }
    if (node.type === "ListWithIcon") {
      return (
        <div className="skeleton-preview-node row" style={{ ...widthStyle, height: 56, gap: 16 }}>
          {block("40px", 40, true)}
          <div className="skeleton-preview-node column" style={{ flex: "1 1 0%", gap: 8 }}>
            {block(`${[70, 56, 64][index % 3]}%`, 18)}
            {block(`${[38, 44, 32][index % 3]}%`, 14)}
          </div>
        </div>
      );
    }
    if (node.type !== "ListRowTexts") return null;
    const type = node.textType;
    const rightAligned = type.startsWith("Right");
    const rowCount = type.startsWith("Three") ? 3 : type.includes("Two") ? 2 : 1;
    const topTypography: "title2" | "label1" | "body1" = type === "TwoRowTypeG" ? "title2"
      : ["OneRowTypeB", "OneRowTypeC", "TwoRowTypeD", "TwoRowTypeE", "ThreeRowTypeC", "ThreeRowTypeD"].includes(type)
        ? "label1" : "body1";
    const bottomTypography = type === "TwoRowTypeF" || type === "ThreeRowTypeF" ? "label2" : "caption";
    const fontSizes = { title2: 20, body1: 16, body2: 15, label1: 14, label2: 13, caption: 12 };
    const textRow = (width: string, typography: keyof typeof fontSizes) => (
      <div style={{ width: "100%", height: typographyLineHeights[typography], display: "flex", alignItems: "center", justifyContent: rightAligned ? "flex-end" : "flex-start" }}>
        {block(width, fontSizes[typography])}
      </div>
    );
    return (
      <div className="skeleton-preview-node column" style={{ ...widthStyle, gap: 2 }}>
        {textRow("70%", topTypography)}
        {rowCount === 3 && textRow("55%", "body2")}
        {rowCount >= 2 && textRow("40%", bottomTypography)}
      </div>
    );
  }

  const style = previewStyle(node, parentLayout);
  if (node.type === "Row" || node.type === "Column") {
    style.gap = `${spacingPixels(node.spacing)}px`;
  }

  return (
    <div
      className={`skeleton-preview-node ${node.type.toLowerCase()}`}
      style={style}
      data-alignment={node.type === "Box" ? node.alignment : undefined}
    >
      {node.children.map((child, childIndex) => (
        <SkeletonPreviewNode key={child.id} node={child} parentLayout={node.type} index={index + childIndex + 1} />
      ))}
    </div>
  );
}

function NumberField({
  label,
  value,
  onChange,
  blankMeansFill = false,
  min = 0,
  max = 640,
}: {
  label: string;
  value: number | null;
  onChange: (value: number | null) => void;
  blankMeansFill?: boolean;
  min?: number;
  max?: number;
}) {
  return (
    <label className="skeleton-number-field">
      <span>{label}</span>
      <Input
        aria-label={label}
        max={max}
        min={min}
        placeholder={blankMeansFill ? "채움" : "0"}
        type="number"
        value={value ?? ""}
        onChange={(event) => onChange(numberValue(event.currentTarget.value))}
      />
    </label>
  );
}

function WidthEditor({
  value,
  onChange,
  allowRadius = false,
}: {
  value: WidthValue;
  onChange: (value: WidthValue) => void;
  allowRadius?: boolean;
}) {
  return (
    <div className="skeleton-dimension-row">
      <label className="skeleton-select-field">
        <span>너비 방식</span>
        <select
          aria-label="너비 크기"
          value={value.mode}
          onChange={(event) =>
            onChange(widthForMode(value, event.currentTarget.value))
          }
        >
          <option value="fill">부모 너비 채우기</option>
          <option value="fraction">부모 너비 비율</option>
          <option value="dp">고정 dp</option>
          {allowRadius && (
            <option value="radius">IenTheme.radius × 정수배</option>
          )}
        </select>
      </label>
      {value.mode === "fraction" && (
        <NumberField
          label="너비 비율 (%)"
          max={100}
          min={1}
          value={Math.round(value.fraction * 100)}
          onChange={(percentage) =>
            onChange({
              mode: "fraction",
              fraction: Math.max(0.01, Math.min(1, (percentage ?? 50) / 100)),
            })
          }
        />
      )}
      {value.mode === "dp" && (
        <NumberField
          label="너비 (dp)"
          value={value.value}
          onChange={(width) =>
            onChange({ mode: "dp", value: width ?? 0 })
          }
        />
      )}
      {value.mode === "radius" && (
        <RadiusDimensionFields
          label="너비"
          value={value}
          onChange={(radius) => onChange({ ...value, ...radius })}
        />
      )}
    </div>
  );
}

function RadiusDimensionFields({ label, value, onChange }: {
  label: string;
  value: Pick<RadiusHeight, "token" | "multiplier">;
  onChange: (value: Pick<RadiusHeight, "token" | "multiplier">) => void;
}) {
  return (
    <>
      <label className="skeleton-select-field">
        <span>{label} 토큰</span>
        <select
          aria-label={`${label} radius 토큰`}
          value={value.token}
          onChange={(event) => {
            const token = event.currentTarget.value;
            if (Object.hasOwn(radiusTokenValues, token)) {
              onChange({ ...value, token: token as RadiusToken });
            }
          }}
        >
          {Object.entries(radiusTokenValues).map(([token, pixels]) => (
            <option key={token} value={token}>
              {token} ({pixels}dp)
            </option>
          ))}
        </select>
      </label>
      <NumberField
        label={`${label} 정수배`}
        min={1}
        value={value.multiplier}
        onChange={(multiplier) =>
          onChange({ ...value, multiplier: Math.max(1, multiplier ?? 1) })
        }
      />
    </>
  );
}

function HeightEditor({
  value,
  onChange,
  allowAuto = false,
  allowTypography = false,
}: {
  value: HeightValue | null;
  onChange: (value: HeightValue | null) => void;
  allowAuto?: boolean;
  allowTypography?: boolean;
}) {
  const mode = value === null
    ? "auto"
    : typeof value === "number"
      ? "dp"
      : value.unit;
  return (
    <div className="skeleton-dimension-row">
      <label className="skeleton-select-field">
        <span>높이 방식</span>
        <select
          aria-label="높이 크기"
          value={mode}
          onChange={(event) => {
            const nextMode = event.currentTarget.value;
            const pixels = value === null ? 24 : heightPixels(value);
            if (nextMode === "auto") onChange(null);
            else if (nextMode === "dp") onChange(pixels);
            else if (nextMode === "radius") onChange(radiusHeightFor(pixels));
            else onChange({ unit: "typography", token: "body1" });
          }}
        >
          {allowAuto && <option value="auto">콘텐츠에 맞춤</option>}
          <option value="dp">직접 지정 (dp)</option>
          <option value="radius">IenTheme.radius × 정수배</option>
          {allowTypography && <option value="typography">IenTheme.typography</option>}
        </select>
      </label>
      {typeof value === "number" && (
        <NumberField
          label="높이 (dp)"
          value={value}
          onChange={(height) => onChange(height ?? 0)}
        />
      )}
      {value !== null && typeof value !== "number" && (
        value.unit === "typography" ? (
          <label className="skeleton-select-field">
            <span>타이포그래피</span>
            <select
              aria-label="높이 타이포그래피"
              value={value.token}
              onChange={(event) => onChange({
                unit: "typography",
                token: event.currentTarget.value as TypographyToken,
              })}
            >
              {Object.keys(typographyLineHeights).map((token) => (
                <option key={token} value={token}>IenTheme.typography.{token}</option>
              ))}
            </select>
          </label>
        ) : (
          <RadiusDimensionFields
            label="높이"
            value={value}
            onChange={(radius) => onChange({ ...value, ...radius })}
          />
        )
      )}
    </div>
  );
}

function SpacingEditor({
  label,
  value,
  onChange,
  allowRadius = false,
}: {
  label: string;
  value: SpacingValue;
  onChange: (value: SpacingValue) => void;
  allowRadius?: boolean;
}) {
  return (
    <div className="skeleton-dimension-row">
      <label className="skeleton-select-field">
        <span>{label} 기준</span>
        <select
          aria-label={`${label} 단위`}
          value={value.unit}
          onChange={(event) =>
            onChange(
              event.currentTarget.value === "radius"
                ? radiusHeightFor(spacingPixels(value))
                : event.currentTarget.value === "token"
                ? { unit: "token", token: spacingTokenFor(value) }
                : { unit: "dp", value: spacingPixels(value) },
            )
          }
        >
          <option value="dp">직접 지정 (dp)</option>
          <option value="token">IenTheme.spacing</option>
          {allowRadius && (
            <option value="radius">IenTheme.radius × 정수배</option>
          )}
        </select>
      </label>
      {value.unit === "dp" ? (
        <NumberField
          label={`${label} (dp)`}
          value={value.value}
          onChange={(spacing) =>
            onChange({ unit: "dp", value: spacing ?? 0 })
          }
        />
      ) : value.unit === "radius" ? (
        <RadiusDimensionFields
          label="높이"
          value={value}
          onChange={(radius) => onChange({ ...value, ...radius })}
        />
      ) : (
        <label className="skeleton-select-field">
          <span>{label} 토큰</span>
          <select
            aria-label={`${label} 테마 토큰`}
            value={value.token}
            onChange={(event) =>
              onChange({
                unit: "token",
                token: spacingTokens.includes(
                  event.currentTarget.value as SpacingToken,
                )
                  ? (event.currentTarget.value as SpacingToken)
                  : "xs",
              })
            }
          >
            {spacingTokens.map((token) => (
              <option key={token} value={token}>
                IenTheme.spacing.{token}
              </option>
            ))}
          </select>
        </label>
      )}
    </div>
  );
}

function AddElementMenu({
  label,
  onAdd,
}: {
  label: string;
  onAdd: (type: ElementKind) => void;
}) {
  const [open, setOpen] = useState(false);
  return (
    <DropdownMenu.Root open={open} onOpenChange={setOpen}>
      <DropdownMenu.Trigger asChild>
        <Button
          aria-expanded={open}
          className="skeleton-add-trigger"
          size="sm"
          type="button"
          variant="line"
        >
          <Plus aria-hidden="true" /> {label}
        </Button>
      </DropdownMenu.Trigger>
      <DropdownMenu.Portal>
        <DropdownMenu.Content
          align="end"
          className="skeleton-add-options"
          sideOffset={5}
        >
          {elementKinds.map((type) => (
            <DropdownMenu.Item
              className="skeleton-add-option"
              key={type}
              onSelect={() => onAdd(type)}
            >
              {elementLabels[type]}
            </DropdownMenu.Item>
          ))}
        </DropdownMenu.Content>
      </DropdownMenu.Portal>
    </DropdownMenu.Root>
  );
}

function SkeletonNodeEditor({
  node,
  siblingIndex,
  siblingCount,
  onUpdate,
  onRemove,
  onAddChild,
  onMove,
}: {
  node: SkeletonNode;
  siblingIndex: number;
  siblingCount: number;
  onUpdate: (id: string, update: (node: SkeletonNode) => SkeletonNode) => void;
  onRemove: (id: string) => void;
  onAddChild: (id: string, type: ElementKind) => void;
  onMove: (id: string, offset: -1 | 1) => void;
}) {
  const patch = (update: (node: SkeletonNode) => SkeletonNode) =>
    onUpdate(node.id, update);
  return (
    <div className={`skeleton-node-editor ${isContainer(node) ? "container" : ""}`}>
      <div className="skeleton-node-heading">
        <Badge variant="weak">{elementLabels[node.type]}</Badge>
        {isContainer(node) && (
          <AddElementMenu label="자식 추가" onAdd={(type) => onAddChild(node.id, type)} />
        )}
        <Button
          aria-label={`${elementLabels[node.type]} 위로 이동`}
          className="skeleton-move-button"
          disabled={siblingIndex === 0}
          onClick={() => onMove(node.id, -1)}
          size="icon-xs"
          type="button"
          variant="ghost"
        >
          <ChevronUp aria-hidden="true" />
        </Button>
        <Button
          aria-label={`${elementLabels[node.type]} 아래로 이동`}
          className="skeleton-move-button"
          disabled={siblingIndex === siblingCount - 1}
          onClick={() => onMove(node.id, 1)}
          size="icon-xs"
          type="button"
          variant="ghost"
        >
          <ChevronDown aria-hidden="true" />
        </Button>
        <Button
          aria-label={`${elementLabels[node.type]} 요소 삭제`}
          className="skeleton-remove-button"
          onClick={() => onRemove(node.id)}
          size="icon-xs"
          type="button"
          variant="ghost"
        >
          <Trash2 aria-hidden="true" />
        </Button>
      </div>
      <div className="skeleton-node-fields">
        {node.type === "ListRowTexts" && (
          <label className="skeleton-select-field">
            <span>텍스트 타입</span>
            <select
              aria-label="리스트 행 텍스트 타입"
              value={node.textType}
              onChange={(event) => {
                const textType = listRowTextsTypes.find((type) => type === event.currentTarget.value);
                if (textType) patch((current) => current.type === "ListRowTexts" ? { ...current, textType } : current);
              }}
            >
              {listRowTextsTypes.map((type) => (
                <option key={type} value={type}>{type}</option>
              ))}
            </select>
          </label>
        )}
        {node.type === "Block" && (
          <>
            <WidthEditor
              allowRadius
              value={node.width}
              onChange={(width) =>
                patch((current) =>
                  current.type === "Block" ? { ...current, width } : current,
                )
              }
            />
            <HeightEditor
              allowTypography
              value={node.height}
              onChange={(height) =>
                patch((current) =>
                  current.type === "Block"
                    ? { ...current, height: height ?? 0 }
                    : current,
                )
              }
            />
            <label className="skeleton-select-field">
              <span>모양</span>
              <select
                aria-label="블록 모양"
                value={node.shape}
                onChange={(event) => {
                  const shape = blockShapeValue(event.currentTarget.value);
                  patch((current) =>
                    current.type === "Block" ? { ...current, shape } : current,
                  );
                }}
              >
                <option value="Rounded">둥근 사각형</option>
                <option value="Circle">원형</option>
              </select>
            </label>
          </>
        )}
        {isContainer(node) && (
          <>
            <WidthEditor
              value={node.width}
              onChange={(width) =>
                patch((current) =>
                  isContainer(current) ? { ...current, width } : current,
                )
              }
            />
            <HeightEditor
              allowAuto
              value={node.height}
              onChange={(height) =>
                patch((current) =>
                  isContainer(current) ? { ...current, height } : current,
                )
              }
            />
            {(node.type === "Row" || node.type === "Column") && (
              <SpacingEditor
                label="간격"
                value={node.spacing}
                onChange={(spacing) =>
                  patch((current) =>
                    current.type === "Row" || current.type === "Column"
                      ? { ...current, spacing: spacing ?? 0 }
                      : current,
                  )
                }
              />
            )}
            {node.type === "Box" && (
              <label className="skeleton-select-field">
                <span>정렬</span>
                <select
                  aria-label="겹친 요소 정렬"
                  value={node.alignment}
                  onChange={(event) => {
                    const alignment = boxAlignmentValue(event.currentTarget.value);
                    patch((current) =>
                      current.type === "Box" ? { ...current, alignment } : current,
                    );
                  }}
                >
                  <option value="Center">가운데</option>
                  <option value="TopStart">왼쪽 위</option>
                  <option value="BottomEnd">오른쪽 아래</option>
                </select>
              </label>
            )}
          </>
        )}
        {node.type === "Spacer" && (
          <>
            <SpacingEditor
              label="가로 간격"
              value={node.width}
              onChange={(width) =>
                patch((current) =>
                  current.type === "Spacer"
                    ? { ...current, width: width ?? 0 }
                    : current,
                )
              }
            />
            <SpacingEditor
              allowRadius
              label="세로 간격"
              value={node.height}
              onChange={(height) =>
                patch((current) =>
                  current.type === "Spacer"
                    ? { ...current, height: height ?? 0 }
                    : current,
                )
              }
            />
          </>
        )}
      </div>
      {isContainer(node) && node.children.length > 0 && (
        <div className="skeleton-node-children">
          {node.children.map((child, siblingIndex) => (
            <SkeletonNodeEditor
              key={child.id}
              node={child}
              siblingIndex={siblingIndex}
              siblingCount={node.children.length}
              onUpdate={onUpdate}
              onRemove={onRemove}
              onAddChild={onAddChild}
              onMove={onMove}
            />
          ))}
        </div>
      )}
    </div>
  );
}

function webDimension(value: SpacingValue | HeightValue): number | string {
  if (typeof value === "number") return value;
  if (value.unit === "radius") return `calc(var(--ien-radius-${value.token}) * ${value.multiplier})`;
  if (value.unit === "token") return `var(--ien-spacing-${value.token})`;
  if (value.unit === "typography") return typographyLineHeights[value.token];
  return value.value;
}
function webElements(nodes: SkeletonNode[], parent: PreviewLayout = "Root"): SkeletonElement[] {
  return nodes.map((node): SkeletonElement => {
    if (node.type === "Spacer") return { type: "Spacer", width: webDimension(node.width), height: webDimension(node.height) };
    if (node.type === "ListRowTexts") return { type: "ListRowTexts", textType: node.textType };
    if (node.type !== "Block" && !isContainer(node)) return { type: node.type };
    const width = node.width.mode === "fill" ? "100%" : node.width.mode === "fraction" ? `${node.width.fraction * 100}%`
      : node.width.mode === "radius" ? webDimension({ ...node.width, unit: "radius" }) : node.width.value;
    const layout = { width, ...(parent === "Row" && node.width.mode === "fill" ? { weight: 1 } : {}) };
    if (node.type === "Block") return { type: "Block", ...layout, shape: node.shape,
      ...(typeof node.height !== "number" && node.height.unit === "typography"
        ? { typography: node.height.token } : { height: webDimension(node.height) }) };
    return { type: node.type, ...layout, ...(node.height !== null ? { height: webDimension(node.height) } : {}),
      spacing: webDimension(node.spacing), ...(node.type === "Box" ? { alignment: node.alignment } : {}),
      elements: webElements(node.children, node.type) };
  });
}

export function SkeletonComposer({ language = "kotlin" }: { language?: "kotlin" | "tsx" }) {
  const [nodes, setNodes] = useState(() => language === "tsx" ? [createNode("ListWithIcon")] : createDefaultNodes());
  const [background, setBackground] = useState<SkeletonBackground>("Grey");
  const [play, setPlay] = useState<SkeletonPlay>("Show");
  const [repeat, setRepeat] = useState<SkeletonRepeat>({ type: "Count", value: 1 });
  const [radius, setRadius] = useState(radiusTokenValues.sm);
  const elements = webElements(nodes);
  const [copiedCode, setCopiedCode] = useState("");
  const [copySucceeded, setCopySucceeded] = useState(false);
  const [copiedWithoutImports, setCopiedWithoutImports] = useState(false);
  const code = language === "tsx"
    ? `import { Skeleton } from "@/components/ui/skeleton"\n\nexport function Pattern() {\n  return (\n    <Skeleton\n      background="${background}"\n      play="${play}"\n      radius={${radius}}\n      repeatLastItemCount={${JSON.stringify(repeat)}}\n      custom={${JSON.stringify(elements, null, 2)}}\n    />\n  )\n}\n`
    : generateKotlinCode(nodes);
  const codeWithoutImports = code
    .split("\n")
    .filter((line) => !line.startsWith("import "))
    .join("\n")
    .trim();
  const copyMessage = copiedWithoutImports
    ? copySucceeded
      ? "import 없이 Kotlin 코드를 복사했습니다."
      : "복사에 실패했습니다."
    : copiedCode === code
      ? copySucceeded
        ? "Kotlin 코드를 복사했습니다."
        : "복사에 실패했습니다."
      : "";

  function updateNode(
    id: string,
    update: (node: SkeletonNode) => SkeletonNode,
  ) {
    setNodes((current) => updateNodeTree(current, id, update));
  }

  function addRootNode(type: ElementKind) {
    const newNode = createNode(type);
    setNodes((current) => [...current, newNode]);
  }

  function addChildNode(id: string, type: ElementKind) {
    const child = createNode(type);
    updateNode(id, (node) =>
      isContainer(node) ? { ...node, children: [...node.children, child] } : node,
    );
  }

  async function copyCode() {
    const copied = await copyTextToClipboard(code);
    setCopiedCode(code);
    setCopiedWithoutImports(false);
    setCopySucceeded(copied);
  }

  async function copyCodeWithoutImports() {
    const copied = await copyTextToClipboard(codeWithoutImports);
    setCopiedCode(code);
    setCopiedWithoutImports(true);
    setCopySucceeded(copied);
  }

  return (
    <section className="skeleton-composer" aria-labelledby="skeleton-composer-title">
      <div className="skeleton-composer-heading">
        <div>
          <p className="panel-kicker">SKELETON COMPOSER</p>
          <Typography variant="title2" asChild><h3 id="skeleton-composer-title">스켈레톤 조합기</h3></Typography>
          <p>{language === "tsx" ? "기본 요소와 리스트 행 텍스트를 조합하고 실제 웹 Skeleton과 TSX 코드를 확인하세요." : "기본 요소와 리스트 행 텍스트를 조합하고 Kotlin 코드를 복사하세요."}</p>
        </div>
        <Badge variant="line">{language === "tsx" ? "SkeletonElement" : "IenSkeletonElement"}</Badge>
      </div>

      <div className="skeleton-composer-grid">
        <Card className="skeleton-composer-card">
          <div className="skeleton-composer-card-heading">
            <div>
              <Typography variant="title3" asChild><h4>조합 미리보기</h4></Typography>
              <p>분홍색 가이드라인은 부모 너비(100%)를 나타냅니다.</p>
              {language === "tsx" && <div className="mt-3 grid gap-3">
                <label>배경 <select className="playground-select" value={background} onChange={(event) => setBackground(event.target.value as SkeletonBackground)}>
                  {["White", "Grey", "GreyOpacity100"].map((value) => <option key={value}>{value}</option>)}
                </select></label>
                <label>재생 상태 <select className="playground-select" value={play} onChange={(event) => setPlay(event.target.value as SkeletonPlay)}>
                  <option>Show</option><option>Hide</option>
                </select></label>
                <label>반복 방식 <select className="playground-select" value={repeat.type} onChange={(event) => setRepeat(event.target.value === "Infinite" ? { type: "Infinite" } : { type: "Count", value: 1 })}>
                  <option>Count</option><option>Infinite</option>
                </select></label>
                {repeat.type === "Count" && <label>마지막 항목 반복 <Input type="number" min={1} step={1} value={repeat.value} onChange={(event) => setRepeat({ type: "Count", value: Number(event.target.value) })} /></label>}
                <label>모서리 반경 <Input type="number" min={0} value={radius} onChange={(event) => setRadius(Number(event.target.value))} /></label>
              </div>}
            </div>
          </div>
          <div className="skeleton-preview-stage" role="img" aria-label="현재 스켈레톤 조합">
            <div className="skeleton-preview-root">
              {language === "tsx" ? <Skeleton custom={elements} background={background} play={play} radius={radius} repeatLastItemCount={repeat} />
                : nodes.map((node, index) => <SkeletonPreviewNode key={node.id} node={node} parentLayout="Root" index={index} />)}
            </div>
          </div>
        </Card>

        <Card className="skeleton-composer-card">
          <div className="skeleton-composer-card-heading">
            <div>
              <Typography variant="title3" asChild><h4>요소 구조</h4></Typography>
              <p>각 요소 안에 다른 요소를 넣어 레이아웃을 구성합니다.</p>
            </div>
            <div className="skeleton-composer-actions">
              <Button
                onClick={() => setNodes(createDefaultNodes())}
                size="sm"
                type="button"
                variant="ghost"
              >
                <RotateCcw aria-hidden="true" /> 기본 구성 복원
              </Button>
              <AddElementMenu label="항목 추가" onAdd={addRootNode} />
            </div>
          </div>
          <div className="skeleton-composer-tree">
            {nodes.length === 0 ? (
              <p className="skeleton-composer-empty">아직 요소가 없습니다.</p>
            ) : (
              nodes.map((node, siblingIndex) => (
                <SkeletonNodeEditor
                  key={node.id}
                  node={node}
                  siblingIndex={siblingIndex}
                  siblingCount={nodes.length}
                  onUpdate={updateNode}
                  onRemove={(id) => setNodes((current) => removeNodeTree(current, id))}
                  onAddChild={addChildNode}
                  onMove={(id, offset) => setNodes((current) => moveNodeTree(current, id, offset))}
                />
              ))
            )}
          </div>
        </Card>
      </div>

      {language === "tsx" ? <CodeExample label="React · TSX 사용 예시" code={code} /> : <Card className="skeleton-composer-code">
        <div className="skeleton-composer-code-heading">
          <div>
            <Typography variant="title3" asChild><h4>Kotlin 코드</h4></Typography>
            <p>현재 조합과 같은 구조를 사용하는 코드입니다.</p>
          </div>
          <div className="skeleton-composer-actions">
            <Button onClick={copyCode} size="sm" type="button" variant="line">
              <Copy aria-hidden="true" />
              코드 복사
            </Button>
            <Button onClick={copyCodeWithoutImports} size="sm" type="button" variant="line">
              <Copy aria-hidden="true" />
              import 없이 복사
            </Button>
          </div>
        </div>
        <pre className="api-signature skeleton-composer-code-block" tabIndex={0}>
          <code>{highlightKotlin(code)}</code>
        </pre>
        <p className="skeleton-copy-message" aria-live="polite">{copyMessage}</p>
      </Card>}
    </section>
  );
}
