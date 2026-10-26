"use client"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"

import * as React from "react"
import { cn } from "cn"
import { RadioGroup as RadioGroupPrimitive } from "radix-ui"

const RadioDesignContext = React.createContext({ size: 24, enabled: true })

function RadioGroup({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  size = 24,
  enabled = true,
  disabled,
  ...props
}: React.ComponentProps<typeof RadioGroupPrimitive.Root> & IenToneProps & { size?: number; enabled?: boolean }) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <IenThemeContext.Provider value={ienTheme}>
      <RadioDesignContext.Provider value={{ size, enabled: enabled && !disabled }}>
        <RadioGroupPrimitive.Root
          data-slot="radio-group"
          data-ien=""
          data-ien-tone={ienTheme.tone}
          data-ien-gradient={ienTheme.useGradient}
          disabled={disabled || !enabled}
          className={cn("grid gap-(--ien-spacing-xxs) w-full", className)}
          {...props}
        />
      </RadioDesignContext.Provider>
    </IenThemeContext.Provider>
  )
}

function RadioGroupItem({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  size,
  enabled = true,
  disabled,
  label,
  style,
  onPointerDown,
  ...props
}: React.ComponentProps<typeof RadioGroupPrimitive.Item> & IenToneProps & { size?: number; enabled?: boolean; label?: string }) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const design = React.useContext(RadioDesignContext)
  const isDisabled = disabled || !enabled || !design.enabled
  const shake = React.useRef<Animation | null>(null)
  React.useEffect(() => () => shake.current?.cancel(), [])
  return (
    <RadioGroupPrimitive.Item
      data-slot="radio-group-item"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      disabled={isDisabled}
      onPointerDown={(event) => {
        onPointerDown?.(event)
        if (event.defaultPrevented || !isDisabled || window.matchMedia("(prefers-reduced-motion: reduce)").matches) return
        shake.current?.cancel()
        shake.current = event.currentTarget.animate([
          { transform: "translateX(0)", offset: 0 },
          { transform: "translateX(-6px)", offset: 0.15 },
          { transform: "translateX(6px)", offset: 0.35 },
          { transform: "translateX(-3.6px)", offset: 0.55 },
          { transform: "translateX(3.6px)", offset: 0.75 },
          { transform: "translateX(0)", offset: 1 },
        ], { duration: 200 })
      }}
      className={cn(
        "group/radio-group-item peer relative inline-flex min-h-(--ien-state-minimum-touch-target) min-w-(--ien-state-minimum-touch-target) w-fit shrink-0 items-center justify-center gap-(--ien-spacing-xs) border-0 bg-transparent bg-none p-0 text-(--ien-text-primary) outline-none focus-visible:ring-3 focus-visible:ring-ring/50 aria-invalid:ring-3 aria-invalid:ring-destructive/40 disabled:cursor-not-allowed disabled:text-(--ien-text-disabled)",
        className,
      )}
      style={{ "--ien-radio-size": `${size ?? design.size}px`, ...style } as React.CSSProperties}
      {...props}
    >
      <span
        data-slot="radio-group-circle"
        aria-hidden="true"
        className="relative flex size-(--ien-radio-size) shrink-0 items-center justify-center rounded-full border-(length:--ien-stroke-medium) border-(--ien-border-strong) [corner-shape:round] [transition:border-color_200ms_cubic-bezier(0.4,0,0.2,1),scale_var(--ien-button-press-duration)_var(--ien-button-press-easing)] group-data-[state=checked]/radio-group-item:border-(--ien-control-color) group-[:enabled:active]/radio-group-item:scale-95 group-disabled/radio-group-item:opacity-(--ien-state-disabled-alpha) motion-reduce:transition-none"
      >
        <RadioGroupPrimitive.Indicator
          forceMount
          data-slot="radio-group-indicator"
          data-ien=""
          data-ien-tone={ienTheme.tone}
          data-ien-gradient={ienTheme.useGradient}
          className="absolute size-[calc(var(--ien-radio-size)/2)] scale-0 rounded-full bg-(--ien-control-color) opacity-0 [corner-shape:round] transition-[scale,opacity] duration-(--ien-button-press-duration) ease-(--ien-button-press-easing) data-[state=checked]:scale-100 data-[state=checked]:opacity-100 motion-reduce:transition-none"
          style={{ backgroundImage: ienTheme.useGradient === false ? "none" : "var(--ien-gradient, linear-gradient(135deg, color-mix(in srgb, var(--ien-control-color), var(--ien-control-on) 18%), var(--ien-control-color), color-mix(in srgb, var(--ien-control-color), var(--ien-control-on) 36%)))" }}
        />
      </span>
      {label != null && <span>{label}</span>}
    </RadioGroupPrimitive.Item>
  )
}

export { RadioGroup, RadioGroupItem }
