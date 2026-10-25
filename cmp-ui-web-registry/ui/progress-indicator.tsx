"use client"

import { useRef, type ComponentProps, type CSSProperties } from "react"
import { Progress as MaterialProgress } from "material/react"
import { cn } from "cn"
import type { IenTone } from "@/lib/ien-theme"
import { useMaterialIndicatorTheme } from "@/lib/ien-material-indicator"

type ProgressIndicatorProps = Omit<ComponentProps<"span">, "children" | "color"> & {
  tone?: IenTone
  /** 원형 또는 선형 진행 표시입니다. */
  variant?: "circular" | "linear"
  /** Expressive 물결 모양을 사용합니다. */
  shape?: "flat" | "wavy"
  /** 0~1 진행률입니다. 생략하면 불확정 애니메이션을 표시합니다. */
  progress?: number
  /** 원형 표시 크기(px)입니다. */
  size?: number
  /** 선 두께(px)입니다. */
  strokeWidth?: number
  color?: string
  trackColor?: string
}

function ProgressIndicator({
  variant = "circular", shape = "flat", progress, size = shape === "wavy" ? 48 : 40,
  strokeWidth = 4, color, trackColor, tone = "brand",
  className, style, "aria-label": ariaLabel = "불러오는 중", ...props
}: ProgressIndicatorProps) {
  const ref = useRef<HTMLSpanElement>(null)
  useMaterialIndicatorTheme(ref)
  return (
    <span
      {...props}
      ref={ref}
      data-slot="progress-indicator"
      data-ien=""
      data-ien-tone={tone}
      className={cn("inline-flex shrink-0 align-middle", variant === "linear" ? "w-full" : "size-[var(--ien-indicator-size)]", className)}
      style={{
        "--ien-indicator-size": `${size}px`,
        "--mtrl-sys-color-primary": color ?? "var(--ien-control-color)",
        "--mtrl-sys-color-secondary-container": trackColor ?? "var(--ien-control-weak)",
        "--mtrl-sys-color-primary-container": trackColor ?? "var(--ien-control-weak)",
        ...style,
      } as CSSProperties}
    >
      <MaterialProgress
        key={`${variant}-${size}`}
        variant={variant}
        shape={shape}
        value={progress === undefined ? 0 : Math.max(0, Math.min(1, progress)) * 100}
        indeterminate={progress === undefined}
        thickness={String(strokeWidth)}
        size={size}
        aria-label={ariaLabel}
        className={cn("w-full", variant === "circular" && "h-full [&::part(progress)]:h-full [&::part(progress)]:w-full [&::part(canvas)]:h-full! [&::part(canvas)]:w-full!")}
      />
    </span>
  )
}

export { ProgressIndicator }
export type { ProgressIndicatorProps }
