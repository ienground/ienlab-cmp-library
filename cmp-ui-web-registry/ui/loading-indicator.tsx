"use client"

import { useEffect, useRef, useState, type ComponentProps, type CSSProperties } from "react"
import { M3LoadingIndicator } from "@alerix/m3-loading-indicator/react"
import { cn } from "cn"
import type { IenTone } from "@/lib/ien-theme"
import { useMaterialIndicatorTheme } from "@/lib/ien-material-indicator"

type LoadingIndicatorProps = Omit<ComponentProps<"span">, "children" | "color"> & {
  tone?: IenTone
  /** 로딩 표시 크기(px)입니다. */
  size?: number
  color?: string
}

function LoadingIndicator({
  size = 48, color, tone = "brand",
  className, style, "aria-label": ariaLabel = "불러오는 중", ...props
}: LoadingIndicatorProps) {
  const ref = useRef<HTMLSpanElement>(null)
  const [resolvedColor, setResolvedColor] = useState("transparent")
  useMaterialIndicatorTheme(ref, setResolvedColor)
  const [reducedMotion, setReducedMotion] = useState(false)
  useEffect(() => {
    const query = window.matchMedia("(prefers-reduced-motion: reduce)")
    const sync = () => setReducedMotion(query.matches)
    sync()
    query.addEventListener("change", sync)
    return () => query.removeEventListener("change", sync)
  }, [])
  return (
    <span
      {...props}
      ref={ref}
      role="progressbar"
      aria-label={ariaLabel}
      data-slot="loading-indicator"
      data-ien=""
      data-ien-tone={tone}
      className={cn("inline-flex size-[var(--ien-indicator-size)] shrink-0 align-middle", className)}
      style={{ "--ien-indicator-size": `${size}px`, color: color ?? "var(--ien-control-color)", ...style } as CSSProperties}
    >
      <M3LoadingIndicator
        size={size}
        color={resolvedColor}
        paused={reducedMotion}
        aria-hidden="true"
        style={{ width: "100%", height: "100%" }}
      />
    </span>
  )
}

export { LoadingIndicator }
export type { LoadingIndicatorProps }
