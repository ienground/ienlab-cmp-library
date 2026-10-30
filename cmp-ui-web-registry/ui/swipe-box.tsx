"use client"

import * as React from "react"
import { cn } from "cn"
import { Button } from "@/components/ui/button"
import type { IenTone } from "@/lib/ien-theme"
import { useIenLanguage } from "@/lib/ien-theme-context"

export type SwipeBoxAction = {
  id: string
  label: string
  onClick: () => void
  tone?: IenTone
  disabled?: boolean
}
export type SwipeBoxProps = Omit<React.ComponentProps<"div">, "onChange"> & {
  startActions?: SwipeBoxAction[]
  endActions?: SwipeBoxAction[]
  open?: "start" | "end" | null
  defaultOpen?: "start" | "end" | null
  onOpenChange?: (side: "start" | "end" | null) => void
  disabled?: boolean
  actionWidth?: number
}

function SwipeBox({ startActions = [], endActions = [], open, defaultOpen = null, onOpenChange, disabled = false, actionWidth = 72, children, className, onKeyDown, ...props }: SwipeBoxProps) {
  const ko = useIenLanguage("ko") === "ko"
  const [localOpen, setLocalOpen] = React.useState(defaultOpen)
  const [dragOffset, setDragOffset] = React.useState<number | null>(null)
  const gesture = React.useRef<{ id: number; x: number; y: number; start: number; direction: number; locked: boolean } | null>(null)
  const id = React.useId()
  const startToggle = React.useRef<HTMLButtonElement>(null)
  const endToggle = React.useRef<HTMLButtonElement>(null)
  const width = Number.isFinite(actionWidth) ? Math.max(1, actionWidth) : 72
  const side = disabled ? null : open === undefined ? localOpen : open
  const resolvedSide = side === "start" && startActions.length ? "start" : side === "end" && endActions.length ? "end" : null
  const offset = resolvedSide === "start" ? startActions.length * width : resolvedSide === "end" ? -endActions.length * width : 0
  const change = (next: "start" | "end" | null) => { setLocalOpen(next); onOpenChange?.(next) }
  const actions = (items: SwipeBoxAction[], position: "start" | "end") => items.length > 0 && <div id={`${id}-${position}`} aria-hidden={resolvedSide !== position} inert={resolvedSide !== position} className="absolute inset-y-0 flex" style={{ [position === "start" ? "insetInlineStart" : "insetInlineEnd"]: 0 }}>
    {items.map((action) => <Button key={action.id} tone={action.tone} disabled={disabled || action.disabled} className="h-full rounded-none px-ien-xs" style={{ width }} onClick={() => { (position === "start" ? startToggle : endToggle).current?.focus(); change(null); action.onClick() }}>{action.label}</Button>)}
  </div>
  return <div {...props} data-slot="swipe-box" aria-disabled={disabled || undefined} className={cn("relative isolate overflow-hidden rounded-ien-md", className)} onKeyDown={(event) => { onKeyDown?.(event); if (!event.defaultPrevented && event.key === "Escape" && resolvedSide) { (resolvedSide === "start" ? startToggle : endToggle).current?.focus(); change(null); setDragOffset(null); gesture.current = null; event.stopPropagation() } }}>
    {actions(startActions, "start")}{actions(endActions, "end")}
    <div data-slot="swipe-box-content" className="relative flex touch-pan-y items-center bg-(--ien-surface) transition-transform duration-(--ien-motion-fast) motion-reduce:transition-none" style={{ transform: `translateX(calc(${disabled ? 0 : dragOffset ?? offset}px * var(--ien-swipe-direction, 1)))`, transitionDuration: dragOffset == null ? undefined : "0ms" }}
      ref={(element) => { if (element) element.style.setProperty("--ien-swipe-direction", getComputedStyle(element).direction === "rtl" ? "-1" : "1") }}
      onPointerDown={(event) => {
        if (disabled || !event.isPrimary || event.button !== 0 || (event.target instanceof Element && event.target.closest("button,a,input,textarea,select,[contenteditable=true]"))) return
        gesture.current = { id: event.pointerId, x: event.clientX, y: event.clientY, start: offset, direction: getComputedStyle(event.currentTarget).direction === "rtl" ? -1 : 1, locked: false }
      }}
      onPointerMove={(event) => {
        const current = gesture.current
        if (disabled || !current || current.id !== event.pointerId) return
        const dx = event.clientX - current.x, dy = event.clientY - current.y
        if (!current.locked) {
          if (Math.max(Math.abs(dx), Math.abs(dy)) < 8) return
          if (Math.abs(dy) >= Math.abs(dx)) { gesture.current = null; return }
          current.locked = true
          event.currentTarget.setPointerCapture(event.pointerId)
        }
        event.preventDefault()
        setDragOffset(Math.max(-endActions.length * width, Math.min(startActions.length * width, current.start + dx * current.direction)))
      }}
      onPointerUp={(event) => {
        const current = gesture.current
        if (!current || current.id !== event.pointerId) return
        if (current.locked) {
          const next = Math.max(-endActions.length * width, Math.min(startActions.length * width, current.start + (event.clientX - current.x) * current.direction))
          change(next > width / 2 ? "start" : next < -width / 2 ? "end" : null)
        }
        gesture.current = null
        setDragOffset(null)
      }}
      onPointerCancel={() => { gesture.current = null; setDragOffset(null) }}
      onLostPointerCapture={() => { gesture.current = null; setDragOffset(null) }}>
      <div className="min-w-0 flex-1">{children}</div>
      <div className="flex shrink-0 gap-ien-xxxs pe-ien-xs">
        {startActions.length > 0 && <Button ref={startToggle} variant="ghost" size="sm" disabled={disabled} aria-controls={`${id}-start`} aria-expanded={resolvedSide === "start"} onClick={() => change(resolvedSide === "start" ? null : "start")}>{ko ? "앞쪽 작업" : "Start actions"}</Button>}
        {endActions.length > 0 && <Button ref={endToggle} variant="ghost" size="sm" disabled={disabled} aria-controls={`${id}-end`} aria-expanded={resolvedSide === "end"} onClick={() => change(resolvedSide === "end" ? null : "end")}>{ko ? "작업" : "Actions"}</Button>}
      </div>
    </div>
  </div>
}

export { SwipeBox }
