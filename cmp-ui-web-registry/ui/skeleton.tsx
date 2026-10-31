"use client"

import * as React from "react"
import { cn } from "cn"
import type { IenToneProps } from "@/lib/ien-theme"
import { useIenLanguage, useIenTheme } from "@/lib/ien-theme-context"

export const skeletonListRowTextsTypes = [
  "OneRowTypeA", "OneRowTypeB", "OneRowTypeC",
  "RightOneRowTypeA", "RightOneRowTypeB", "RightOneRowTypeC", "RightOneRowTypeD", "RightOneRowTypeE",
  "TwoRowTypeA", "TwoRowTypeB", "TwoRowTypeC", "TwoRowTypeD", "TwoRowTypeE", "TwoRowTypeF", "TwoRowTypeG",
  "RightTwoRowTypeA", "RightTwoRowTypeB", "RightTwoRowTypeC", "RightTwoRowTypeD", "RightTwoRowTypeE",
  "ThreeRowTypeA", "ThreeRowTypeB", "ThreeRowTypeC", "ThreeRowTypeD", "ThreeRowTypeE", "ThreeRowTypeF",
] as const
export type SkeletonListRowTextsType = typeof skeletonListRowTextsTypes[number]
export type SkeletonPattern = "TopList" | "TopListWithIcon" | "AmountTopList" | "AmountTopListWithIcon" | "SubtitleList" | "SubtitleListWithIcon" | "ListOnly" | "ListWithIconOnly" | "CardOnly" | "ListRowTexts"
export type SkeletonBackground = "White" | "Grey" | "GreyOpacity100"
export type SkeletonPlay = "Show" | "Hide"
export type SkeletonRepeat = { type: "Count"; value: number } | { type: "Infinite" }
export type SkeletonTypography = "display" | "title1" | "title2" | "title3" | "body1" | "body2" | "label1" | "label2" | "caption"
type Dimension = number | string

type ElementLayout = {
  width?: Dimension
  height?: Dimension
  weight?: number
  className?: string
  style?: React.CSSProperties
}
export type SkeletonElement =
  | { type: "Title" | "Subtitle" | "List" | "ListWithIcon" | "Card" }
  | { type: "ListRowTexts"; textType: SkeletonListRowTextsType }
  | ({ type: "Block"; shape?: "Rounded" | "Circle" } & ElementLayout &
      ({ height: Dimension; typography?: SkeletonTypography } | { height?: undefined; typography: SkeletonTypography }))
  | ({ type: "Row" | "Column" | "Box"; elements: SkeletonElement[]; spacing?: Dimension; alignment?: "TopStart" | "Center" | "BottomEnd"; justify?: "start" | "center" | "end" | "space-between"; align?: "start" | "center" | "end" | "stretch" } & ElementLayout)
  | { type: "Spacer"; height: Dimension; width?: Dimension }

// IenTheme.typography에 대응하는 블록 높이와 행 높이입니다.
const textMetrics: Record<SkeletonTypography, readonly [number, number]> = {
  display: [30, 38], title1: [24, 32], title2: [20, 28], title3: [18, 26],
  body1: [16, 24], body2: [15, 22], label1: [14, 20], label2: [13, 18], caption: [12, 16],
}
const groupedMotion = React.createContext(false)
const backgroundColors: Record<SkeletonBackground, string> = {
  White: "var(--ien-surface-raised)",
  Grey: "var(--ien-surface-variant)",
  GreyOpacity100: "color-mix(in srgb, var(--ien-text-primary) 5%, transparent)",
}
function useSkeletonMotion(ref: React.RefObject<HTMLDivElement | null>, animate: boolean) {
  React.useLayoutEffect(() => {
    if (!animate) return
    for (const animation of ref.current?.getAnimations() ?? []) {
      if (animation instanceof CSSAnimation && animation.animationName === "ien-skeleton-wave") animation.startTime = 0
    }
  }, [ref, animate])
}
function motionDelay(index: number) {
  return `${((index * 0.78 / (Math.PI * 2)) % 1) * 1200 - 1200}ms`
}

export type SkeletonMotionGroupProps = React.ComponentProps<"div"> & { animationIndex?: number }
function SkeletonMotionGroup({ animationIndex = 0, children, className, style, ref: forwardedRef, ...props }: SkeletonMotionGroupProps) {
  const parentGrouped = React.useContext(groupedMotion)
  const ref = React.useRef<HTMLDivElement>(null)
  useSkeletonMotion(ref, !parentGrouped)
  return (
    <div {...props} ref={(node) => {
      ref.current = node
      if (typeof forwardedRef === "function") return forwardedRef(node)
      if (forwardedRef) forwardedRef.current = node
    }} data-slot="skeleton-motion-group" data-skeleton-motion={parentGrouped ? undefined : ""}
      className={cn("min-w-0", className)} style={{ "--ien-skeleton-delay": motionDelay(animationIndex), ...style } as React.CSSProperties}>
      <groupedMotion.Provider value>{children}</groupedMotion.Provider>
    </div>
  )
}

type BlockProps = React.ComponentProps<"div"> & { animationIndex: number; animate?: boolean; shape?: "Rounded" | "Circle" }
function SkeletonBlock({ animationIndex, animate = true, shape, className, style, ...props }: BlockProps) {
  const parentGrouped = React.useContext(groupedMotion)
  const animated = animate && !parentGrouped
  const ref = React.useRef<HTMLDivElement>(null)
  useSkeletonMotion(ref, animated)
  return <div {...props} ref={ref} data-slot="skeleton-block" data-ien="" data-skeleton-motion={animated ? "" : undefined}
    className={cn("min-w-0 shrink-0 transition-none", className)}
    style={{ backgroundColor: "var(--ien-skeleton-color)", borderRadius: shape === "Circle" ? "50%" : "var(--ien-skeleton-radius)",
      ...(shape === "Circle" ? { cornerShape: "round" } : {}),
      "--ien-skeleton-delay": motionDelay(animationIndex), ...style } as React.CSSProperties} />
}

function ElementView({ element, index }: { element: SkeletonElement; index: number }) {
  const block = (width: Dimension, height: Dimension, animate = true, shape?: "Circle") =>
    <SkeletonBlock animationIndex={index} animate={animate} shape={shape} style={{ width, height }} />
  switch (element.type) {
    case "Title": return block("42%", 24)
    case "Subtitle": return block("34%", 16)
    case "Card": return <SkeletonBlock animationIndex={index} style={{ width: "100%", height: 132, borderRadius: "var(--ien-radius-default)" }} />
    case "List": return <div className="flex h-12 w-full items-center">{block(`${[92, 78, 86][index % 3]}%`, 18)}</div>
    case "ListWithIcon": return (
      <SkeletonMotionGroup animationIndex={index} className="flex h-14 w-full items-center gap-ien-md">
        {block(40, 40, false, "Circle")}
        <div className="flex min-w-0 flex-1 flex-col gap-ien-xs">
          {block(`${[70, 56, 64][index % 3]}%`, 18, false)}
          {block(`${[38, 44, 32][index % 3]}%`, 14, false)}
        </div>
      </SkeletonMotionGroup>
    )
    case "ListRowTexts": {
      const type = element.textType
      const right = type.startsWith("Right")
      const rows = type.startsWith("Three") ? 3 : type.includes("Two") ? 2 : 1
      const top: SkeletonTypography = type === "TwoRowTypeG" ? "title2"
        : ["OneRowTypeB", "OneRowTypeC", "TwoRowTypeD", "TwoRowTypeE", "ThreeRowTypeC", "ThreeRowTypeD"].includes(type) ? "label1" : "body1"
      const bottom: SkeletonTypography = type === "TwoRowTypeF" || type === "ThreeRowTypeF" ? "label2" : "caption"
      const text = (fraction: string, typography: SkeletonTypography) => <div className={cn("flex w-full items-center", right && "justify-end")} style={{ height: textMetrics[typography][1] }}>{block(fraction, textMetrics[typography][0], false)}</div>
      return <SkeletonMotionGroup animationIndex={index} className="flex w-full flex-col gap-ien-xxxs" data-text-type={type}>
        {text("70%", top)}{rows === 3 && text("55%", "body2")}{rows >= 2 && text("40%", bottom)}
      </SkeletonMotionGroup>
    }
    case "Block": return <SkeletonBlock animationIndex={index} shape={element.shape} className={element.className}
      style={{ width: element.width ?? "100%", height: element.height ?? textMetrics[element.typography ?? "body2"][1], flex: element.weight ? `${element.weight} 1 0%` : undefined, ...element.style }} />
    case "Spacer": return <div aria-hidden="true" style={{ width: element.width ?? 0, height: element.height, flexShrink: 0 }} />
    default: {
      const box = element.type === "Box"
      const align = element.align === "start" ? "flex-start" : element.align === "end" ? "flex-end" : element.align
      const justify = element.justify === "start" ? "flex-start" : element.justify === "end" ? "flex-end" : element.justify
      const placement = element.alignment === "Center" ? "center" : element.alignment === "BottomEnd" ? "end" : "start"
      return <div className={cn("min-w-0", element.className)} style={{ display: box ? "grid" : "flex", flexDirection: element.type === "Column" ? "column" : "row",
        width: element.width ?? "100%", height: element.height, gap: box ? undefined : element.spacing ?? 0,
        alignItems: align ?? "flex-start", justifyContent: justify ?? "flex-start", placeItems: box ? placement : undefined,
        flex: element.weight ? `${element.weight} 1 0%` : undefined, ...element.style }}>
        {element.elements.map((child, childIndex) => <div key={childIndex} className="contents" style={box ? { display: "grid", gridArea: "1 / 1", width: "100%", placeItems: placement } : undefined}>
          <ElementView element={child} index={index + childIndex + 1} />
        </div>)}
      </div>
    }
  }
}
function patternElements(pattern: SkeletonPattern, textType: SkeletonListRowTextsType): SkeletonElement[] {
  if (pattern === "ListRowTexts") return [{ type: "ListRowTexts", textType }]
  if (pattern === "CardOnly") return [{ type: "Card" }]
  const list: SkeletonElement = { type: pattern.includes("WithIcon") ? "ListWithIcon" : "List" }
  if (pattern === "ListOnly" || pattern === "ListWithIconOnly") return [list]
  const spacer: SkeletonElement = { type: "Spacer", height: pattern.startsWith("Amount") ? "var(--ien-spacing-xs)" : "var(--ien-spacing-xxs)" }
  if (pattern.startsWith("Amount")) return [{ type: "Title" }, { type: "Subtitle" }, spacer, list]
  return [{ type: pattern.startsWith("Subtitle") ? "Subtitle" : "Title" }, spacer, list]
}

export type SkeletonProps = React.ComponentProps<"div"> & IenToneProps & {
  height?: Dimension
  width?: Dimension
  radius?: Dimension
  pattern?: SkeletonPattern
  listRowTextsType?: SkeletonListRowTextsType
  custom?: SkeletonElement[]
  repeatLastItemCount?: SkeletonRepeat
  play?: SkeletonPlay
  background?: SkeletonBackground
}
function Skeleton({ height, width, radius = "var(--ien-radius-sm)", pattern, listRowTextsType = "OneRowTypeA", custom,
  repeatLastItemCount = { type: "Count", value: 3 }, play = "Show", background = "Grey", tone, useGradient,
  className, style, children, "aria-label": ariaLabelProp, ...props }: SkeletonProps) {
  const language = useIenLanguage("ko")
  const ariaLabel = ariaLabelProp ?? (language === "ko" ? "불러오는 중" : "Loading")
  useIenTheme({ tone, useGradient })
  if (play === "Hide") return null
  // 기존 className으로 크기를 정하던 단일 블록 호출을 유지합니다.
  const single = height != null || (pattern === undefined && custom === undefined && className !== undefined)
  const elements = custom ?? patternElements(pattern ?? "TopList", listRowTextsType)
  const count = repeatLastItemCount.type === "Infinite" ? 30 : Math.max(1, Math.trunc(repeatLastItemCount.value) || 1)
  const repeated = elements.length ? [...elements.slice(0, -1), ...Array.from({ length: count }, () => elements[elements.length - 1])] : []
  return <div {...props} data-slot="skeleton" data-ien="" data-ien-tone={tone} data-ien-gradient="false" role="status" aria-label={ariaLabel} aria-live="polite" aria-busy="true"
    className={cn(single ? "rounded-(--ien-skeleton-radius)" : "flex w-full flex-col gap-ien-sm", className)}
    style={{ width, "--ien-skeleton-radius": typeof radius === "number" ? `${radius}px` : radius, "--ien-skeleton-color": backgroundColors[background], ...style } as React.CSSProperties}>
    {single ? <SkeletonBlock animationIndex={0} className="size-full" style={{ height: height ?? "100%", borderRadius: "inherit", cornerShape: "inherit" } as React.CSSProperties}>{children}</SkeletonBlock>
      : repeated.map((element, index) => <ElementView key={index} element={element} index={index} />)}
  </div>
}

export { Skeleton, SkeletonMotionGroup }
