"use client"

import * as React from "react"
import { cn } from "cn"
import type { IenToneProps } from "@/lib/ien-theme"
import { useIenLanguage, useIenTheme } from "@/lib/ien-theme-context"
import { PullToRefreshGesture } from "@/lib/ien-pull-to-refresh"

export interface PullToRefreshState {
  distance: number
  progress: number
  ready: boolean
  isRefreshing: boolean
}

export type PullToRefreshProps = React.ComponentProps<"div"> & IenToneProps & {
  /** 실제 데이터 갱신 상태입니다. 요청 시작 시 true, 완료·실패 시 false로 변경합니다. */
  isRefreshing: boolean
  /** 실제 요청과 오류 처리는 호출자가 담당합니다. */
  onRefresh: () => void
  enabled?: boolean
  /** 갱신을 요청하는 표시 거리(px)입니다. 기본값은 56입니다. */
  threshold?: number
  indicator?: (state: PullToRefreshState) => React.ReactNode
}

export type PullToRefreshIndicatorProps = React.ComponentProps<"span"> & IenToneProps & PullToRefreshState

function PullToRefreshIndicator({
  distance, progress, ready, isRefreshing, tone, useGradient, className, style, ...props
}: PullToRefreshIndicatorProps) {
  const theme = useIenTheme({ tone, useGradient })
  const fraction = Math.max(0, Math.min(1, progress))
  return (
    <span
      {...props}
      aria-hidden="true"
      data-slot="pull-to-refresh-indicator"
      data-ien=""
      data-ien-tone={theme.tone}
      data-ien-shape="capsule"
      data-ready={ready || undefined}
      data-refreshing={isRefreshing || undefined}
      className={cn(
        "pointer-events-none absolute left-1/2 z-10 flex h-9 -translate-x-1/2 items-center justify-center overflow-hidden rounded-full border border-(--ien-border) bg-(--ien-surface) bg-clip-border text-(--ien-control-color) shadow-[0_4px_16px_color-mix(in_srgb,var(--ien-text-primary)_8%,transparent)] transition-[width,background-color,border-color,opacity,top] duration-(--ien-motion-normal) ease-(--ien-motion-standard-easing) data-ready:border-(--ien-control-weak) data-ready:bg-(--ien-control-weak) motion-reduce:transition-none",
        className,
      )}
      style={{ width: 36 + 36 * fraction, top: isRefreshing ? 8 : Math.min(8, distance - 28), opacity: distance > 0 || isRefreshing ? 1 : 0, ...style }}
    >
      <svg width="72" height="36" viewBox="0 0 72 36" className="shrink-0" aria-hidden="true">
        {[0, 1, 2].map((index) => (
          <g key={index} transform={`translate(${36 + (index - 1) * 13 * fraction} 18)`}>
            <circle
              r={index === 1 ? 4 : 3.5}
              fill="currentColor"
              opacity={index === 1 || isRefreshing ? 1 : fraction}
              className={isRefreshing ? "ien-pull-to-refresh-dot" : undefined}
              style={{ animationDelay: `${index * 150}ms` }}
            />
          </g>
        ))}
      </svg>
    </span>
  )
}

/** 높이가 제한된 영역에 터치·마우스 당김, 휠 오버스크롤과 Alt+R 갱신을 제공합니다. */
function PullToRefresh({
  isRefreshing, onRefresh, enabled = true, threshold = 56, indicator,
  tone, useGradient, className, style, children, onKeyDown,
  "aria-label": ariaLabel, "aria-describedby": describedBy, ...props
}: PullToRefreshProps) {
  const theme = useIenTheme({ tone, useGradient })
  const language = useIenLanguage("ko")
  const labels = language === "ko"
    ? { region: "새로고침 가능한 목록", hint: "목록 맨 위에서 아래로 당기거나 위로 더 스크롤해 새로고침합니다. 이 영역에 포커스한 뒤 Alt+R로도 갱신할 수 있습니다.", pulling: "아래로 당겨 새로고침", ready: "놓으면 새로고침", refreshing: "목록을 새로고침하는 중" }
    : { region: "Refreshable list", hint: "At the top, pull down or scroll further up to refresh. You can also focus this region and press Alt+R.", pulling: "Pull down to refresh", ready: "Release to refresh", refreshing: "Refreshing the list" }
  const hintId = React.useId()
  const viewportRef = React.useRef<HTMLDivElement>(null)
  const [distance, setDistance] = React.useState(0)
  const [dragging, setDragging] = React.useState(false)
  const gesture = React.useMemo(() => new PullToRefreshGesture(threshold), [threshold])
  const requestLocked = React.useRef(false)
  const suppressClick = React.useRef(false)
  const requestRefresh = React.useCallback(() => {
    if (!enabled || isRefreshing || requestLocked.current) return
    requestLocked.current = true
    try {
      onRefresh()
    } catch (error) {
      requestLocked.current = false
      throw error
    }
  }, [enabled, isRefreshing, onRefresh])

  React.useEffect(() => {
    if (!isRefreshing) requestLocked.current = false
  }, [isRefreshing])

  React.useEffect(() => {
    const viewport = viewportRef.current
    if (!viewport) return
    let pointerId: number | null = null
    let touchId: number | null = null
    let wheelTimer: ReturnType<typeof setTimeout> | undefined
    const cancel = () => {
      clearTimeout(wheelTimer)
      wheelTimer = undefined
      gesture.cancel()
      setDistance(0)
      setDragging(false)
      touchId = null
      if (pointerId !== null && viewport.hasPointerCapture(pointerId)) viewport.releasePointerCapture(pointerId)
      pointerId = null
    }
    const canStart = (target: EventTarget | null) => {
      if (!enabled || isRefreshing || requestLocked.current || viewport.scrollTop > 0) return false
      if (!(target instanceof Element)) return false
      if (target.closest('button, a, input, textarea, select, [contenteditable]:not([contenteditable="false"]), [role="slider"], [data-pull-to-refresh-ignore], [data-slot="pull-to-refresh"] [data-slot="pull-to-refresh"]')) return false
      for (let node: Element | null = target; node && node !== viewport; node = node.parentElement) {
        const css = getComputedStyle(node)
        if (node.scrollTop > 0 && /(auto|scroll)/.test(css.overflowY)) return false
        if (node.scrollWidth > node.clientWidth && /(auto|scroll)/.test(css.overflowX)) return false
      }
      return true
    }
    const move = (x: number, y: number, event: Event) => {
      if (viewport.scrollTop > 0) { cancel(); return }
      const next = gesture.move(x, y)
      if (!gesture.dragging) return
      if (!event.cancelable) { cancel(); return }
      event.preventDefault()
      setDragging(true)
      setDistance(next)
    }
    const finish = (blockClick = true) => {
      suppressClick.current = blockClick && gesture.dragging
      const shouldRefresh = gesture.finish()
      cancel()
      if (shouldRefresh) requestRefresh()
    }
    const touchStart = (event: TouchEvent) => {
      suppressClick.current = false
      cancel()
      if (event.touches.length !== 1) return
      const touch = event.touches[0]
      touchId = touch.identifier
      gesture.start(touch.clientX, touch.clientY, canStart(event.target))
    }
    const touchMove = (event: TouchEvent) => {
      if (event.touches.length !== 1) { cancel(); return }
      const touch = event.touches[0]
      if (touch.identifier === touchId) move(touch.clientX, touch.clientY, event)
    }
    const touchEnd = (event: TouchEvent) => {
      if (Array.from(event.changedTouches).some((touch) => touch.identifier === touchId)) finish()
    }
    const pointerDown = (event: PointerEvent) => {
      if (event.pointerType !== "mouse" || event.button !== 0) return
      suppressClick.current = false
      cancel()
      pointerId = event.pointerId
      gesture.start(event.clientX, event.clientY, canStart(event.target))
    }
    const pointerMove = (event: PointerEvent) => {
      if (event.pointerId !== pointerId) return
      move(event.clientX, event.clientY, event)
      if (gesture.dragging && !viewport.hasPointerCapture(event.pointerId)) viewport.setPointerCapture(event.pointerId)
    }
    const pointerUp = (event: PointerEvent) => { if (event.pointerId === pointerId) finish() }
    const pointerCancel = (event: PointerEvent) => { if (event.pointerId === pointerId) cancel() }
    const pointerLeave = () => { if (!gesture.dragging) cancel() }
    const click = (event: MouseEvent) => {
      if (!suppressClick.current) return
      suppressClick.current = false
      event.preventDefault()
      event.stopPropagation()
    }
    const scroll = () => { if (viewport.scrollTop > 0) cancel() }
    const wheel = (event: WheelEvent) => {
      if (pointerId !== null || touchId !== null) return
      if (event.ctrlKey || event.shiftKey || Math.abs(event.deltaX) >= Math.abs(event.deltaY)) { cancel(); return }
      if (!canStart(event.target) || !event.cancelable) { cancel(); return }
      if (!gesture.dragging && event.deltaY >= 0) return
      event.preventDefault()
      const unit = event.deltaMode === 1 ? 16 : event.deltaMode === 2 ? viewport.clientHeight : 1
      const next = gesture.wheel(event.deltaY * unit)
      setDistance(next)
      setDragging(gesture.dragging)
      clearTimeout(wheelTimer)
      wheelTimer = setTimeout(() => finish(false), 180)
    }
    viewport.addEventListener("touchstart", touchStart, { passive: true })
    viewport.addEventListener("touchmove", touchMove, { passive: false })
    viewport.addEventListener("touchend", touchEnd)
    viewport.addEventListener("touchcancel", cancel)
    viewport.addEventListener("pointerdown", pointerDown)
    viewport.addEventListener("pointermove", pointerMove)
    viewport.addEventListener("pointerup", pointerUp)
    viewport.addEventListener("pointercancel", pointerCancel)
    viewport.addEventListener("lostpointercapture", pointerCancel)
    viewport.addEventListener("pointerleave", pointerLeave)
    viewport.addEventListener("scroll", scroll, { passive: true })
    viewport.addEventListener("click", click, true)
    viewport.addEventListener("wheel", wheel, { passive: false })
    cancel()
    return () => {
      clearTimeout(wheelTimer)
      viewport.removeEventListener("touchstart", touchStart)
      viewport.removeEventListener("touchmove", touchMove)
      viewport.removeEventListener("touchend", touchEnd)
      viewport.removeEventListener("touchcancel", cancel)
      viewport.removeEventListener("pointerdown", pointerDown)
      viewport.removeEventListener("pointermove", pointerMove)
      viewport.removeEventListener("pointerup", pointerUp)
      viewport.removeEventListener("pointercancel", pointerCancel)
      viewport.removeEventListener("lostpointercapture", pointerCancel)
      viewport.removeEventListener("pointerleave", pointerLeave)
      viewport.removeEventListener("scroll", scroll)
      viewport.removeEventListener("click", click, true)
      viewport.removeEventListener("wheel", wheel)
      if (pointerId !== null && viewport.hasPointerCapture(pointerId)) viewport.releasePointerCapture(pointerId)
    }
  }, [enabled, isRefreshing, gesture, requestRefresh])

  const offset = isRefreshing ? threshold : enabled ? distance : 0
  const state: PullToRefreshState = { distance: offset, progress: Math.min(1, offset / threshold), ready: offset >= threshold, isRefreshing }
  const status = isRefreshing ? labels.refreshing : state.ready ? labels.ready : offset > 0 ? labels.pulling : ""
  return (
    <div
      {...props}
      role={props.role ?? "region"}
      tabIndex={props.tabIndex ?? 0}
      aria-label={ariaLabel ?? labels.region}
      aria-describedby={[describedBy, enabled ? hintId : undefined].filter(Boolean).join(" ") || undefined}
      aria-keyshortcuts={enabled ? "Alt+r" : undefined}
      aria-busy={isRefreshing}
      data-slot="pull-to-refresh"
      data-state={isRefreshing ? "refreshing" : state.ready ? "ready" : offset > 0 ? "pulling" : "idle"}
      data-disabled={!enabled || undefined}
      data-ien=""
      data-ien-tone={theme.tone}
      className={cn("relative isolate flex min-h-0 flex-col overflow-hidden outline-none focus-visible:ring-3 focus-visible:ring-ring/50", className)}
      style={style}
      onKeyDown={(event) => {
        onKeyDown?.(event)
        if (event.defaultPrevented || event.repeat || event.target !== event.currentTarget) return
        if (event.altKey && !event.ctrlKey && !event.metaKey && event.key.toLowerCase() === "r") {
          event.preventDefault()
          requestRefresh()
        }
      }}
    >
      {indicator ? indicator(state) : <PullToRefreshIndicator {...state} tone={theme.tone} useGradient={theme.useGradient} />}
      <div ref={viewportRef} data-slot="pull-to-refresh-viewport" className={cn("min-h-0 flex-1 overflow-y-auto", enabled && "overscroll-y-contain", dragging && "select-none")}>
        <div data-slot="pull-to-refresh-content" className={cn("min-h-full", !dragging && "transition-transform duration-(--ien-motion-normal) ease-(--ien-motion-standard-easing) motion-reduce:transition-none")} style={{ transform: `translateY(${offset}px)` }}>
          {children}
        </div>
      </div>
      <span id={hintId} className="sr-only">{enabled ? labels.hint : ""}</span>
      <span role="status" aria-live="polite" aria-atomic="true" className="sr-only">{status}</span>
    </div>
  )
}

export { PullToRefresh, PullToRefreshIndicator }
