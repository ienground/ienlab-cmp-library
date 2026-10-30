"use client"

import * as React from "react"
import { cn } from "cn"
import { Button } from "@/components/ui/button"
import type { IenTone } from "@/lib/ien-theme"
import { useIenLanguage } from "@/lib/ien-theme-context"

export type SwipeBoxAction = {
  id: string
  label: string
  icon?: React.ReactNode
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

function SwipeBox({ startActions = [], endActions = [], open, defaultOpen = null, onOpenChange, disabled = false, actionWidth = 60, children, className, onKeyDown, ...props }: SwipeBoxProps) {
  const ko = useIenLanguage("ko") === "ko"
  const [localOpen, setLocalOpen] = React.useState(defaultOpen)
  const [dragOffset, setDragOffset] = React.useState<number | null>(null)
  const gesture = React.useRef<{ id: number; x: number; y: number; start: number; maxStart: number; maxEnd: number; direction: number; locked: boolean } | null>(null)
  const id = React.useId()
  const startToggle = React.useRef<HTMLButtonElement>(null)
  const endToggle = React.useRef<HTMLButtonElement>(null)
  const width = Number.isFinite(actionWidth) ? Math.max(1, actionWidth) : 60
  const side = disabled ? null : open === undefined ? localOpen : open
  const resolvedSide = side === "start" && startActions.length ? "start" : side === "end" && endActions.length ? "end" : null
  const startWidth = startActions.length ? startActions.length * width + 8 : 0
  const endWidth = endActions.length ? endActions.length * width + 8 : 0
  const offset = resolvedSide === "start" ? startWidth : resolvedSide === "end" ? -endWidth : 0
  const currentOffset = disabled ? 0 : dragOffset ?? offset
  const swiping = currentOffset !== 0
  const itemSize = Math.max(0, width - 8)
  const change = (next: "start" | "end" | null) => { setLocalOpen(next); onOpenChange?.(next) }
  const actions = (items: SwipeBoxAction[], position: "start" | "end") => {
    if (!items.length) return null
    const revealedWidth = currentOffset * (position === "start" ? 1 : -1) > 0 ? Math.abs(currentOffset) : 0
    const normalWidth = items.length * width + 8
    const expansion = Math.max(0, revealedWidth - normalWidth)
    return <div id={`${id}-${position}`} data-slot="swipe-box-actions" aria-hidden={resolvedSide !== position} inert={resolvedSide !== position} className="absolute inset-y-0 overflow-hidden" style={{ width: revealedWidth, [position === "start" ? "insetInlineStart" : "insetInlineEnd"]: 0 }}>
      <div className="absolute inset-y-0 flex items-center px-ien-xxs" style={{ width: normalWidth + expansion, [position === "start" ? "insetInlineStart" : "insetInlineEnd"]: 0 }}>
        {items.map((action, index) => {
          const primary = position === "start" ? index === 0 : index === items.length - 1
          const revealOrder = position === "start" ? index : items.length - 1 - index
          const reveal = Math.max(0, Math.min(1, (revealedWidth - 8 - width * revealOrder) / width))
          const labelVisible = primary && expansion > 24
          return <div key={action.id} className="flex shrink-0 items-center justify-center" style={{ width: width + (primary ? expansion : 0), paddingBlock: 10 }}>
            <Button tone={action.tone} shape="capsule" animateSize={false} aria-label={action.label} title={action.label} disabled={disabled || action.disabled} className={cn("gap-0 overflow-hidden px-ien-sm text-[length:var(--ien-font-size-label2)] [&_svg]:size-(--ien-icon-sm)", position === "start" ? "origin-left rtl:origin-right" : "origin-right rtl:origin-left")} style={{ transitionProperty: "background-color,color,box-shadow", width: itemSize + (primary ? expansion : 0), height: itemSize, opacity: reveal, transform: `scale(${reveal})` }} onClick={() => { (position === "start" ? startToggle : endToggle).current?.focus(); change(null); action.onClick() }}>
              {action.icon ? <>
                <span aria-hidden="true" className="shrink-0">{action.icon}</span>
                <span aria-hidden="true" data-slot="swipe-box-action-label" className="grid transition-[grid-template-columns,opacity,margin] duration-(--ien-motion-fast) ease-(--ien-motion-standard-easing) motion-reduce:transition-none" style={{ gridTemplateColumns: labelVisible ? "1fr" : "0fr", opacity: labelVisible ? 1 : 0, marginInlineStart: labelVisible ? "var(--ien-spacing-xxs)" : 0 }}><span className="overflow-hidden">{action.label}</span></span>
              </> : action.label}
            </Button>
          </div>
        })}
      </div>
    </div>
  }
  return <div {...props} data-slot="swipe-box" aria-disabled={disabled || undefined} className={cn("relative isolate", className)} onKeyDown={(event) => { onKeyDown?.(event); if (!event.defaultPrevented && event.key === "Escape" && resolvedSide) { (resolvedSide === "start" ? startToggle : endToggle).current?.focus(); change(null); setDragOffset(null); gesture.current = null; event.stopPropagation() } }}>
    {actions(startActions, "start")}{actions(endActions, "end")}
    <div data-slot="swipe-box-content" className={cn("relative flex touch-pan-y items-center transition-[transform,border-radius,background-color] duration-(--ien-motion-fast) ease-(--ien-motion-standard-easing) motion-reduce:transition-none", swiping && "rounded-ien-lg bg-(--ien-surface-variant)")} style={{ minHeight: startActions.length || endActions.length ? itemSize + 20 : undefined, transform: `translateX(calc(${currentOffset}px * var(--ien-swipe-direction, 1)))`, transitionDuration: dragOffset == null ? undefined : "0ms" }}
      ref={(element) => { if (element) element.style.setProperty("--ien-swipe-direction", getComputedStyle(element).direction === "rtl" ? "-1" : "1") }}
      onPointerDown={(event) => {
        if (disabled || !event.isPrimary || event.button !== 0 || (event.target instanceof Element && event.target.closest("button,a,input,textarea,select,[contenteditable=true]"))) return
        gesture.current = { id: event.pointerId, x: event.clientX, y: event.clientY, start: offset, maxStart: startActions.length ? Math.max(startWidth, event.currentTarget.clientWidth) : 0, maxEnd: endActions.length ? Math.max(endWidth, event.currentTarget.clientWidth) : 0, direction: getComputedStyle(event.currentTarget).direction === "rtl" ? -1 : 1, locked: false }
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
        setDragOffset(Math.max(-current.maxEnd, Math.min(current.maxStart, current.start + dx * current.direction)))
      }}
      onPointerUp={(event) => {
        const current = gesture.current
        if (!current || current.id !== event.pointerId) return
        if (current.locked) {
          const next = Math.max(-endWidth, Math.min(startWidth, current.start + (event.clientX - current.x) * current.direction))
          change(next > width / 2 ? "start" : next < -width / 2 ? "end" : null)
        }
        gesture.current = null
        setDragOffset(null)
      }}
      onPointerCancel={() => { gesture.current = null; setDragOffset(null) }}
      onLostPointerCapture={() => { gesture.current = null; setDragOffset(null) }}>
      <div className="min-w-0 flex-1">{children}</div>
      <div className="absolute end-ien-xs flex gap-ien-xxxs">
        {startActions.length > 0 && <Button ref={startToggle} variant="ghost" size="sm" className="sr-only focus:not-sr-only focus:relative focus:bg-(--ien-surface)" disabled={disabled} aria-controls={`${id}-start`} aria-expanded={resolvedSide === "start"} onClick={() => change(resolvedSide === "start" ? null : "start")}>{ko ? "앞쪽 작업" : "Start actions"}</Button>}
        {endActions.length > 0 && <Button ref={endToggle} variant="ghost" size="sm" className="sr-only focus:not-sr-only focus:relative focus:bg-(--ien-surface)" disabled={disabled} aria-controls={`${id}-end`} aria-expanded={resolvedSide === "end"} onClick={() => change(resolvedSide === "end" ? null : "end")}>{ko ? "작업" : "Actions"}</Button>}
      </div>
    </div>
  </div>
}

export { SwipeBox }
