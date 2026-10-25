"use client"

import type { IenToneProps } from "@/lib/ien-theme"
import { useIenTheme } from "@/lib/ien-theme-context"
import * as React from "react"
import { cn } from "cn"

export type CardColor =
  | "background" | "surface" | "surface-raised" | "surface-weak" | "surface-variant"
  | "text-primary" | "text-secondary" | "text-tertiary" | "text-disabled"
  | "border" | "border-strong" | "overlay"
  | "brand" | "on-brand" | "brand-weak" | "on-brand-weak"
  | "success" | "on-success" | "success-weak" | "on-success-weak"
  | "warning" | "on-warning" | "warning-weak" | "on-warning-weak"
  | "danger" | "on-danger" | "danger-weak" | "on-danger-weak"
  | "info" | "on-info" | "info-weak" | "on-info-weak"

export interface CardColors {
  container?: CardColor
  content?: CardColor
  border?: CardColor | "none"
  containerBrush?: "default" | "none"
}

export interface CardShape {
  radius?: "none" | "xs" | "sm" | "default" | "md" | "lg" | "xl" | "full"
}

export type CardSpacing = "none" | "xxxs" | "xxs" | "xs" | "sm" | "md" | "lg" | "xl" | "xxl" | "xxxl"
export type CardPadding = CardSpacing | { top?: CardSpacing; bottom?: CardSpacing; start?: CardSpacing; end?: CardSpacing }
export type CardElevation = "none" | "raised" | "floating" | "overlay"

export type CardProps = React.ComponentProps<"div"> & IenToneProps & {
  size?: "default" | "sm"
  variant?: "filled" | "outlined"
  toneVariant?: "solid" | "weak"
  shape?: CardShape
  contentPadding?: CardPadding
  colors?: CardColors
  tonalElevation?: CardElevation
}

function Card({
  tone: ienTone = "neutral",
  useGradient: ienUseGradient,
  className,
  size = "default",
  variant = "filled",
  toneVariant = "solid",
  shape,
  contentPadding,
  colors,
  tonalElevation,
  style,
  onClick,
  onKeyDown,
  onKeyUp,
  role,
  tabIndex,
  ...props
}: CardProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const tone = ienTheme.tone ?? "neutral"
  const neutral = tone === "neutral"
  const solidContainer = neutral ? "var(--ien-surface-raised)" : `var(--ien-${tone})`
  const weakContainer = neutral ? "var(--ien-surface-weak)" : `var(--ien-${tone}-weak)`
  const baseContainer = toneVariant === "solid" ? solidContainer : weakContainer
  const baseContent = neutral ? "var(--ien-text-primary)"
    : toneVariant === "solid" ? `var(--ien-on-${tone})` : solidContainer
  const container = colors?.container ? `var(--ien-${colors.container})` : (variant === "filled" ? baseContainer : "var(--ien-surface)")
  const content = colors?.content ? `var(--ien-${colors.content})` : (variant === "filled" ? baseContent : "var(--ien-text-primary)")
  const border = colors?.border === "none" ? "transparent" : colors?.border ? `var(--ien-${colors.border})`
    : variant === "filled" ? baseContainer
    : neutral ? (toneVariant === "weak" ? "var(--ien-border)" : "var(--ien-border-strong)")
    : baseContainer
  const elevation = tonalElevation ?? (variant === "filled" ? "raised" : "none")
  const start = neutral ? (toneVariant === "solid" ? 2 : 1) : toneVariant === "solid" ? 18 : 6
  const end = neutral ? (toneVariant === "solid" ? 4 : 2) : toneVariant === "solid" ? 36 : 10
  const brush = colors?.containerBrush === "none" ? "none"
    : variant === "filled" && ienTheme.useGradient !== false
      ? `var(--ien-gradient, linear-gradient(135deg, color-mix(in srgb, ${container}, ${content} ${start}%), ${container}, color-mix(in srgb, ${container}, ${content} ${end}%)))`
      : "none"
  // CMP Surface와 같이 기본 surface 색상에만 tonal elevation을 합성합니다.
  const surface = brush === "none" && container === "var(--ien-surface)" && elevation !== "none"
    ? `color-mix(in srgb, var(--ien-brand) var(--ien-elevation-${elevation}-tint), ${container})`
    : container
  const padding = contentPadding ?? (size === "sm" ? "sm" : "md")
  const edges = typeof padding === "string"
    ? { top: padding, bottom: padding, start: padding, end: padding }
    : { top: padding.top ?? "none", bottom: padding.bottom ?? "none", start: padding.start ?? "none", end: padding.end ?? "none" }

  return (
    <div
      data-slot="card"
      data-ien=""
      data-ien-tone={tone}
      data-ien-gradient={ienTheme.useGradient}
      data-variant={variant}
      data-tone-variant={toneVariant}
      data-size={size}
      data-clickable={onClick ? "" : undefined}
      data-tonal-elevation={elevation}
      role={role ?? (onClick ? "button" : undefined)}
      tabIndex={tabIndex ?? (onClick ? 0 : undefined)}
      onClick={onClick}
      onKeyDown={(event) => {
        onKeyDown?.(event)
        if (!onClick || event.defaultPrevented || event.target !== event.currentTarget || event.repeat) return
        if (event.key === " ") event.preventDefault()
        if (event.key === "Enter") { event.preventDefault(); event.currentTarget.click() }
      }}
      onKeyUp={(event) => {
        onKeyUp?.(event)
        if (onClick && !event.defaultPrevented && event.target === event.currentTarget && event.key === " ") {
          event.preventDefault()
          event.currentTarget.click()
        }
      }}
      className={cn(
        "group/card flex flex-col gap-(--card-spacing) overflow-hidden rounded-(--ien-card-radius) border-solid border-(length:--ien-stroke-thin) border-(--ien-card-border) bg-(--ien-card-container) bg-clip-border [background-image:var(--ien-card-brush)] pt-(--card-padding-top) pb-(--card-padding-bottom) ps-(--card-padding-start) pe-(--card-padding-end) text-(--ien-card-content) has-[>[data-slot=card-footer]]:pb-0 has-[>img:first-child]:pt-0 [&>img]:-ms-(--card-padding-start) [&>img]:-me-(--card-padding-end) [&>img]:max-w-none [--card-spacing:var(--ien-spacing-md)] data-[size=sm]:[--card-spacing:var(--ien-spacing-sm)]",
        variant === "filled" ? "ien-card-filled" : "ien-card-outlined",
        onClick && "cursor-pointer outline-none transition-[filter] duration-150 hover:brightness-[0.98] active:brightness-95 focus-visible:ring-3 focus-visible:ring-ring/50 motion-reduce:transition-none",
        className,
      )}
      style={{
        "--ien-card-container": surface,
        "--ien-card-content": content,
        "--ien-card-border": border,
        "--ien-card-brush": brush,
        "--ien-card-radius": `var(--ien-radius-${shape?.radius ?? "lg"})`,
        "--ien-control-corner-shape": "squircle",
        "--card-padding-top": `var(--ien-spacing-${edges.top})`,
        "--card-padding-bottom": `var(--ien-spacing-${edges.bottom})`,
        "--card-padding-start": `var(--ien-spacing-${edges.start})`,
        "--card-padding-end": `var(--ien-spacing-${edges.end})`,
        ...style,
      } as React.CSSProperties}
      {...props}
    />
  )
}

function CardHeader({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-header"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "gap-1 -ms-(--card-padding-start) -me-(--card-padding-end) ps-(--card-padding-start) pe-(--card-padding-end) [.border-b]:pb-(--card-spacing) group/card-header @container/card-header grid auto-rows-min items-start has-data-[slot=card-action]:grid-cols-[1fr_auto] has-data-[slot=card-description]:grid-rows-[auto_auto]",
        className
      )}
      {...props}
    />
  )
}

function CardTitle({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-title"
      data-ien-typography="title2"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn("text-base leading-snug font-medium group-data-[size=sm]/card:text-sm font-sans", className)}
      {...props}
    />
  )
}

function CardDescription({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-description"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn("text-inherit text-sm opacity-75", className)}
      {...props}
    />
  )
}

function CardAction({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-action"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        " col-start-2 row-span-2 row-start-1 self-start justify-self-end",
        className
      )}
      {...props}
    />
  )
}

function CardContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-content"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn("-ms-(--card-padding-start) -me-(--card-padding-end) ps-(--card-padding-start) pe-(--card-padding-end)", className)}
      {...props}
    />
  )
}

function CardFooter({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-footer"
      data-ien-typography="caption"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "-ms-(--card-padding-start) -me-(--card-padding-end) ps-(--card-padding-start) pe-(--card-padding-end) pt-(--card-spacing) pb-(--card-padding-bottom) flex items-center",
        className,
      )}
      {...props}
    />
  )
}

export {
  Card,
  CardHeader,
  CardFooter,
  CardTitle,
  CardAction,
  CardDescription,
  CardContent,
}
