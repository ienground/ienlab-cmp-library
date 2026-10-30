"use client"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"

import * as React from "react"
import { cn } from "cn"
import { Separator as SeparatorPrimitive } from "radix-ui"

function Separator({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  orientation = "horizontal",
  decorative = true,
  ...props
}: React.ComponentProps<typeof SeparatorPrimitive.Root> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <IenThemeContext.Provider value={ienTheme}>
      <SeparatorPrimitive.Root
        data-slot="separator"
        data-ien=""
        data-ien-tone={ienTheme.tone}
        data-ien-gradient={ienTheme.useGradient}
        decorative={decorative}
        orientation={orientation}
        className={cn(
        "shrink-0 bg-border data-[orientation=horizontal]:h-px data-[orientation=horizontal]:w-full data-[orientation=vertical]:w-px data-[orientation=vertical]:self-stretch",
        className
      )}
        {...props}
      />
    </IenThemeContext.Provider>
  )
}

export type BorderProps = React.ComponentProps<typeof Separator> & {
  variant?: "full" | "padding24" | "height"
  height?: number
  thickness?: number
  color?: string
}

function Border({ variant = "full", height = 16, thickness = 1, color, style, ...props }: BorderProps) {
  return <Separator {...props} orientation="horizontal" data-slot="border"
    style={{
      height: variant === "height" ? height : thickness,
      ...(variant === "padding24" ? { width: "auto", marginInline: "var(--ien-spacing-xl)" } : {}),
      backgroundColor: color ?? (variant === "height" ? "var(--ien-surface-weak)" : "var(--ien-border)"),
      ...style,
    }} />
}

export { Separator, Border }
