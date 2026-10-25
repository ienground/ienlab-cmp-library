"use client"

import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"
import * as React from "react"
import { cn } from "cn"
import { Switch as SwitchPrimitive } from "radix-ui"

export type SwitchProps = React.ComponentProps<typeof SwitchPrimitive.Root> & IenToneProps & {
  enabled?: boolean
  thumbContent?: React.ReactNode
  trackContent?: React.ReactNode
  onTrackContent?: React.ReactNode
  offTrackContent?: React.ReactNode
  /** 기존 웹 호출부와의 호환을 위한 크기 옵션입니다. CMP 기본 규격은 default입니다. */
  size?: "sm" | "default"
}

function Switch({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  size = "default",
  enabled = true,
  disabled,
  checked,
  defaultChecked = false,
  onCheckedChange,
  thumbContent,
  trackContent,
  onTrackContent,
  offTrackContent,
  onPointerDown,
  style,
  children,
  ref,
  ...props
}: SwitchProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const [localChecked, setLocalChecked] = React.useState(defaultChecked)
  const currentChecked = checked ?? localChecked
  const isDisabled = disabled || !enabled
  const rootRef = React.useRef<HTMLButtonElement | null>(null)
  const shake = React.useRef<Animation | null>(null)
  const resolvedOn = onTrackContent ?? trackContent
  const resolvedOff = offTrackContent ?? trackContent
  const hasThumbContent = thumbContent !== null && thumbContent !== undefined && thumbContent !== false
  const trackColor = currentChecked ? "var(--ien-brand)" : "var(--ien-border-strong)"

  React.useEffect(() => () => shake.current?.cancel(), [])

  return (
    <IenThemeContext.Provider value={ienTheme}>
      <SwitchPrimitive.Root
        ref={(node) => {
          rootRef.current = node
          if (typeof ref === "function") return ref(node)
          if (ref) ref.current = node
        }}
        data-slot="switch"
        data-ien=""
        data-ien-tone={ienTheme.tone}
        data-ien-gradient="false"
        data-size={size}
        checked={currentChecked}
        disabled={isDisabled}
        onCheckedChange={(next) => {
          if (isDisabled) return
          setLocalChecked(next)
          onCheckedChange?.(next)
        }}
        onPointerDown={(event) => {
          onPointerDown?.(event)
          if (event.defaultPrevented || !isDisabled || window.matchMedia("(prefers-reduced-motion: reduce)").matches) return
          shake.current?.cancel()
          shake.current = rootRef.current?.animate([
            { transform: "translateX(0)", offset: 0 },
            { transform: "translateX(-6px)", offset: 0.15 },
            { transform: "translateX(6px)", offset: 0.35 },
            { transform: "translateX(-3.6px)", offset: 0.55 },
            { transform: "translateX(3.6px)", offset: 0.75 },
            { transform: "translateX(0)", offset: 1 },
          ], { duration: 200 }) ?? null
        }}
        className={cn(
          "peer group/switch relative inline-flex shrink-0 items-center rounded-full border-0 bg-none outline-none [--ien-control-corner-shape:round] [corner-shape:round] transition-none focus-visible:ring-3 focus-visible:ring-ring/50 aria-invalid:ring-3 aria-invalid:ring-destructive/40 disabled:cursor-not-allowed after:absolute after:inset-x-0 after:-inset-y-2 after:content-[''] motion-reduce:transition-none",
          "[--switch-width:52px] [--switch-height:32px] [--switch-selected-thumb:24px] [--switch-pressed-thumb:28px] [--switch-thumb:16px] [--switch-icon:16px] [--switch-on-content:4px] [--switch-off-content:52px] [--switch-content-travel:20px] [--switch-start:calc((var(--switch-height)-var(--switch-thumb))/2)] h-(--switch-height) w-(--switch-width)",
          "data-[state=checked]:[--switch-thumb:var(--switch-selected-thumb)] data-[state=checked]:[--switch-start:calc(var(--switch-width)-var(--switch-thumb)-(var(--switch-height)-var(--switch-thumb))/2)]",
          size === "sm" && "[--switch-width:24px] [--switch-height:14px] [--switch-selected-thumb:10px] [--switch-pressed-thumb:12px] [--switch-thumb:7px] [--switch-icon:7px] [--switch-on-content:2px] [--switch-off-content:24px] [--switch-content-travel:9px]",
          hasThumbContent && "[--switch-thumb:var(--switch-selected-thumb)]",
          !isDisabled && "active:[--switch-thumb:var(--switch-pressed-thumb)] data-[state=checked]:active:[--switch-thumb:var(--switch-pressed-thumb)]",
          className,
        )}
        style={{
          backgroundColor: isDisabled
            ? `color-mix(in srgb, ${trackColor} calc(var(--ien-state-disabled-alpha) * 100%), transparent)`
            : trackColor,
          ...style,
        }}
        {...props}
      >
        {(resolvedOn != null || resolvedOff != null) && (
          <span aria-hidden="true" data-slot="switch-track-content" className="pointer-events-none absolute inset-x-0 top-1/2 h-(--switch-icon) -translate-y-1/2 overflow-hidden text-(--ien-surface)">
            <span className="absolute inset-0 transition-transform duration-(--ien-motion-switch-thumb-duration) ease-(--ien-motion-switch-thumb) group-data-[state=unchecked]/switch:-translate-x-(--switch-content-travel) rtl:group-data-[state=unchecked]/switch:translate-x-(--switch-content-travel) group-active/switch:duration-0 motion-reduce:transition-none">
              <span className="absolute start-(--switch-on-content) inline-flex size-(--switch-icon) items-center justify-center text-xs [&_svg]:size-(--switch-icon)">{resolvedOn}</span>
              <span className="absolute start-(--switch-off-content) inline-flex size-(--switch-icon) items-center justify-center text-xs [&_svg]:size-(--switch-icon)">{resolvedOff}</span>
            </span>
          </span>
        )}
        <SwitchPrimitive.Thumb
          data-slot="switch-thumb"
          className="pointer-events-none absolute top-1/2 z-10 inline-flex size-(--switch-thumb) -translate-y-1/2 items-center justify-center rounded-full bg-(--ien-surface) text-(--ien-brand) [corner-shape:round] [inset-inline-start:var(--switch-start)] transition-[inset-inline-start,width,height] duration-(--ien-motion-switch-thumb-duration) ease-(--ien-motion-switch-thumb) group-active/switch:duration-0 motion-reduce:transition-none [&_svg]:size-(--switch-icon)"
        >
          {thumbContent}
        </SwitchPrimitive.Thumb>
        {children}
      </SwitchPrimitive.Root>
    </IenThemeContext.Provider>
  )
}

export { Switch }
