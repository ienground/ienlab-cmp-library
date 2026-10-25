"use client"

import { useEffect, useMemo, useRef, type ComponentProps, type CSSProperties } from "react"
import { indeterminateFrame, initialIndeterminateFrame } from "@kyralabs/m3e-motion"
import { loadingIndicatorShapes } from "@kyralabs/m3e-shapes"
import { cn } from "cn"
import type { IenTone } from "@/lib/ien-theme"

// CMP가 사용하는 Compose Material3 LoadingIndicator.kt의 시간과 스프링 설정입니다.
const loadingTiming = {
  morphIntervalMillis: 650,
  rotationDurationMillis: 4666,
  spring: { dampingRatio: 0.6, stiffness: 200 },
  visibilityThreshold: 0.1,
}

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
  const pathRef = useRef<SVGPathElement>(null)
  const rotationRef = useRef<SVGGElement>(null)
  const shapes = useMemo(() => loadingIndicatorShapes(null, true, 38 / 48), [])

  useEffect(() => {
    const path = pathRef.current
    const rotation = rotationRef.current
    if (!path || !rotation) return
    const reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)")
    let animationFrame = 0
    let startedAt = performance.now()
    const draw = (elapsed: number) => {
      const frame = reducedMotion.matches
        ? initialIndeterminateFrame
        : indeterminateFrame(elapsed, shapes.morphs.length, loadingTiming)
      path.setAttribute("d", shapes.pathData(frame.morphIndex, frame.morphProgress, 100))
      rotation.setAttribute("transform", `rotate(${frame.rotation} 50 50)`)
    }
    const tick = (now: number) => {
      draw(now - startedAt)
      animationFrame = requestAnimationFrame(tick)
    }
    const start = () => {
      cancelAnimationFrame(animationFrame)
      startedAt = performance.now()
      draw(0)
      if (!reducedMotion.matches) animationFrame = requestAnimationFrame(tick)
    }
    start()
    reducedMotion.addEventListener("change", start)
    return () => {
      cancelAnimationFrame(animationFrame)
      reducedMotion.removeEventListener("change", start)
    }
  }, [shapes])

  return (
    <span
      {...props}
      role="progressbar"
      aria-label={ariaLabel}
      data-slot="loading-indicator"
      data-ien=""
      data-ien-tone={tone}
      className={cn("inline-flex size-[var(--ien-indicator-size)] shrink-0 overflow-hidden align-middle", className)}
      style={{ "--ien-indicator-size": `${size}px`, color: color ?? "var(--ien-control-color)", ...style } as CSSProperties}
    >
      <svg viewBox="0 0 100 100" className="block size-full" aria-hidden="true" focusable="false">
        <g ref={rotationRef} transform="rotate(90 50 50)">
          <path ref={pathRef} d={shapes.pathData(0, 0, 100)} fill="currentColor" />
        </g>
      </svg>
    </span>
  )
}

export { LoadingIndicator }
export type { LoadingIndicatorProps }
