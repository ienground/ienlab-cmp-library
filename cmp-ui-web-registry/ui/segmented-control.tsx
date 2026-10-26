"use client"

import * as React from "react"
import { RadioGroup } from "radix-ui"
import { cn } from "cn"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenContinuousCorners, useIenTheme } from "@/lib/ien-theme-context"
import { useDisabledMenuShake } from "@/lib/ien-menu"

export type SegmentedControlSize = "small" | "large"
export type SegmentedControlAlignment = "fixed" | "fluid"

export type SegmentedControlItem = {
  value: string
  label: React.ReactNode
  enabled?: boolean
  size?: SegmentedControlSize
}

export type SegmentedControlProps = Omit<React.ComponentProps<typeof RadioGroup.Root>, "children" | "defaultValue" | "value" | "onValueChange" | "orientation" | "asChild"> & IenToneProps & {
  items: SegmentedControlItem[]
  value?: string
  defaultValue?: string
  onValueChange?: (value: string) => void
  size?: SegmentedControlSize
  alignment?: SegmentedControlAlignment
  enabled?: boolean
}

function SegmentedControl({
  items, value, defaultValue, onValueChange, size = "small", alignment = "fixed",
  enabled = true, disabled = false, tone, useGradient, className, style,
  ref, onPointerDownCapture, ...props
}: SegmentedControlProps) {
  const theme = useIenTheme({ tone, useGradient })
  const [localValue, setLocalValue] = React.useState(() => defaultValue ?? items.find((item) => item.enabled !== false)?.value ?? items[0]?.value)
  const selectedValue = value ?? localValue
  const blocked = disabled || !enabled
  const handlePointerDownCapture = useDisabledMenuShake(onPointerDownCapture, "[data-slot=segmented-control-item]")
  const trackRef = React.useRef<HTMLDivElement>(null)
  const [bounds, setBounds] = React.useState<{ left: number; width: number } | null>(null)

  React.useLayoutEffect(() => {
    const track = trackRef.current
    if (!track) return
    const update = () => {
      const selected = Array.from(track.querySelectorAll<HTMLButtonElement>("[data-slot=segmented-control-item]"))
        .find((item) => item.dataset.state === "checked")
      const next = selected ? { left: selected.offsetLeft, width: selected.offsetWidth } : null
      setBounds((previous) => previous?.left === next?.left && previous?.width === next?.width ? previous : next)
    }
    update()
    const observer = new ResizeObserver(update)
    observer.observe(track)
    for (const item of track.querySelectorAll("[data-slot=segmented-control-item]")) observer.observe(item)
    return () => observer.disconnect()
  }, [selectedValue, items, size, alignment])

  React.useEffect(() => {
    if (alignment !== "fluid") return
    const track = trackRef.current
    const selected = track?.querySelector<HTMLElement>('[data-state="checked"]')
    const viewport = track?.parentElement
    if (!track || !selected || !viewport) return
    const left = selected.offsetLeft
    const right = left + selected.offsetWidth
    const padding = parseFloat(getComputedStyle(track).paddingLeft)
    if (left < viewport.scrollLeft + padding) viewport.scrollLeft = Math.max(0, left - padding)
    else if (right > viewport.scrollLeft + viewport.clientWidth - padding) viewport.scrollLeft = right - viewport.clientWidth + padding
  }, [selectedValue, alignment, bounds?.left, bounds?.width])

  return (
    <>
      <IenContinuousCorners />
      <RadioGroup.Root
        {...props}
        ref={ref}
        orientation="horizontal"
        value={selectedValue ?? ""}
        onValueChange={(next) => {
          if (value === undefined) setLocalValue(next)
          onValueChange?.(next)
        }}
        disabled={blocked}
        onPointerDownCapture={handlePointerDownCapture}
        data-slot="segmented-control"
        data-size={size}
        data-alignment={alignment}
        data-ien=""
        data-ien-shape="capsule"
        data-ien-tone={theme.tone}
        data-ien-gradient={theme.useGradient}
        className={cn(
          "max-w-full rounded-full bg-(--ien-surface-variant) bg-clip-border data-disabled:opacity-(--ien-state-disabled-alpha)",
          alignment === "fixed" ? "w-full overflow-hidden" : "w-fit overflow-x-auto overflow-y-hidden [scrollbar-width:none] [&::-webkit-scrollbar]:hidden",
          className,
        )}
        style={style}
      >
        <div ref={trackRef} className={cn(
          "relative isolate flex items-stretch gap-(--ien-spacing-xxs) overflow-clip p-(--ien-spacing-xxs)",
          size === "small" ? "h-10" : "h-12",
          alignment === "fixed" ? "w-full" : "w-max min-w-full",
        )}>
          {bounds && <span
            aria-hidden="true"
            data-slot="segmented-control-indicator"
            data-ien-shape="capsule"
            className="pointer-events-none absolute inset-y-(--ien-spacing-xxs) left-0 -z-10 rounded-full bg-(--ien-surface-raised) bg-clip-border transition-[transform,width] duration-(--ien-motion-spring-duration) ease-(--ien-motion-spring-medium-bouncy) motion-reduce:transition-none"
            style={{ width: bounds.width, transform: `translateX(${bounds.left}px)` }}
          />}
          {items.map((item) => {
            const itemSize = item.size ?? size
            return <RadioGroup.Item
              key={item.value}
              value={item.value}
              disabled={blocked || item.enabled === false}
              data-slot="segmented-control-item"
              data-ien-shape="capsule"
              data-ien-typography={itemSize === "small" ? "label2" : "label1"}
              className={cn(
                "flex min-w-0 shrink-0 items-center justify-center gap-(--ien-spacing-xs) whitespace-nowrap rounded-full bg-clip-border font-semibold text-(--ien-text-secondary) outline-none transition-[color,transform] data-[state=checked]:text-(--ien-text-primary) disabled:text-(--ien-text-disabled) enabled:active:scale-95 focus-visible:ring-2 focus-visible:ring-(--ien-brand) focus-visible:ring-inset motion-reduce:transition-none [&_svg]:size-(--ien-icon-sm) [&_svg]:shrink-0",
                alignment === "fixed" ? "flex-1 basis-0" : itemSize === "small" ? "min-w-16" : "min-w-20",
                itemSize === "small" ? "px-(--ien-spacing-sm) text-[length:var(--ien-font-size-label2)] leading-(--ien-line-height-label2)" : "px-(--ien-spacing-md) text-[length:var(--ien-font-size-label1)] leading-(--ien-line-height-label1)",
              )}
            >{item.label}</RadioGroup.Item>
          })}
        </div>
      </RadioGroup.Root>
    </>
  )
}

export { SegmentedControl }
