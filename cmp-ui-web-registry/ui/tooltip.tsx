"use client"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"

import * as React from "react"
import { cn } from "cn"
import { Tooltip as TooltipPrimitive } from "radix-ui"

function TooltipProvider({
  delayDuration = 0,
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Provider>) {
  return (
    <TooltipPrimitive.Provider
      data-slot="tooltip-provider"
      delayDuration={delayDuration}
      {...props}
    />
  )
}

function Tooltip({
  tone: ienTone,
  useGradient: ienUseGradient,
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Root> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <IenThemeContext.Provider value={ienTheme}>
      <TooltipPrimitive.Root data-slot="tooltip" {...props} />
    </IenThemeContext.Provider>
  )
}

function TooltipTrigger({
  tone,
  useGradient,
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Trigger> & IenToneProps) {
  const ienTheme = useIenTheme({ tone, useGradient })
  return (
    <TooltipPrimitive.Trigger
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-slot="tooltip-trigger"
      data-ien-typography="label1"
      {...props}
    />
  )
}

function TooltipContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  sideOffset = 6,
  width,
  fitContentWidth = false,
  messageAlign = "left",
  style,
  children,
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Content> & IenToneProps & {
  width?: number
  fitContentWidth?: boolean
  messageAlign?: "left" | "center" | "right"
}) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const isNeutral = !ienTheme.tone || ienTheme.tone === "neutral"
  return (
    <TooltipPrimitive.Portal>
      <TooltipPrimitive.Content
        data-slot="tooltip-content"
        data-ien-typography="label2"
        data-ien=""
        data-ien-tone={ienTheme.tone ?? "neutral"}
        data-ien-gradient={ienTheme.useGradient}
        sideOffset={sideOffset}
        className={cn(
          "z-50 inline-flex items-center gap-1.5 rounded-[12px] px-(--ien-spacing-md) py-2.5 text-[length:var(--ien-font-size-label2)] leading-(--ien-line-height-label2) font-bold whitespace-normal break-words max-w-(--radix-tooltip-content-available-width) origin-(--radix-tooltip-content-transform-origin) shadow-[0_8px_16px_#001d3a26]",
          "data-[state=open]:animate-in data-[state=open]:fade-in-0 data-[state=open]:zoom-in-[0.98] data-[state=delayed-open]:animate-in data-[state=delayed-open]:fade-in-0 data-[state=delayed-open]:zoom-in-[0.98] data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=closed]:zoom-out-[0.98] data-[side=bottom]:slide-in-from-top-1 data-[side=left]:slide-in-from-right-1 data-[side=right]:slide-in-from-left-1 data-[side=top]:slide-in-from-bottom-1 motion-reduce:animate-none",
          isNeutral
            ? "bg-(--ien-surface-raised) text-(--ien-text-primary)"
            : "bg-(--ien-control-color) text-(--ien-control-on)",
          messageAlign === "center" ? "text-center justify-center" : messageAlign === "right" ? "text-right justify-end" : "text-left justify-start",
          fitContentWidth ? "w-fit" : "w-70",
          className,
        )}
        style={{ width, ...style }}
        {...props}
      >
        {children}
        <TooltipPrimitive.Arrow
          width={12}
          height={6}
          className={isNeutral ? "fill-(--ien-surface-raised)" : "fill-(--ien-control-color)"}
        />
      </TooltipPrimitive.Content>
    </TooltipPrimitive.Portal>
  )
}

export { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger }
