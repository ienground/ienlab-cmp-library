"use client"

import * as React from "react"
import { IconChevronDown } from "@tabler/icons-react"
import { Button } from "@/components/ui/button"
import { cn } from "cn"
import { DropdownMenu, DropdownMenuContent, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { useIenLanguage } from "@/lib/ien-theme-context"

export type SplitButtonProps = React.ComponentProps<typeof Button> & {
  menu: React.ReactNode
  menuLabel?: string
  open?: boolean
  onOpenChange?: (open: boolean) => void
}

function SplitButton({ menu, menuLabel, open, onOpenChange, tone, variant, size = "lg", display = "inline", disabled, loading, useGradient, style, ...props }: SplitButtonProps) {
  const language = useIenLanguage("ko")
  const [localOpen, setLocalOpen] = React.useState(false)
  const expanded = open ?? localOpen
  const small = size === "sm" || size === "icon-sm"
  const large = size === "lg" || size === "icon-lg"
  const extraSmall = size === "xs" || size === "icon-xs"
  const height = extraSmall ? 24 : small ? 36 : large ? 52 : 44
  const outerRadius = display === "full" ? 0 : height / 2
  const innerRadius = large ? "var(--ien-radius-sm)" : "var(--ien-radius-xs)"
  const buttonStyle: React.CSSProperties = {
    height,
    fontSize: `var(--ien-font-size-${extraSmall ? "caption" : small ? "label2" : large ? "body1" : "label1"})`,
    lineHeight: `var(--ien-line-height-${extraSmall ? "caption" : small ? "label2" : large ? "body1" : "label1"})`,
  }
  return <div role="group" data-slot="split-button" className={cn("inline-flex items-stretch gap-ien-xxxs [--ien-control-corner-profile:cmp]", display !== "inline" && "flex w-full")}>
    <Button {...props} tone={tone} variant={variant} size={size} disabled={disabled} loading={loading} useGradient={useGradient}
      style={{ ...buttonStyle, flex: display !== "inline" ? 1 : undefined, minWidth: 0, paddingInline: `var(--ien-spacing-${large ? "lg" : small || extraSmall ? "sm" : "md"})`, borderStartStartRadius: outerRadius, borderEndStartRadius: outerRadius, borderStartEndRadius: innerRadius, borderEndEndRadius: innerRadius, ...style }} />
    <DropdownMenu open={expanded} onOpenChange={(next) => { setLocalOpen(next); onOpenChange?.(next) }}>
      <DropdownMenuTrigger asChild><Button type="button" tone={tone} variant={variant} size={size} useGradient={useGradient}
        style={{ ...buttonStyle, width: height, padding: 0, transitionProperty: "border-radius, background-color, color, box-shadow, transform", borderStartStartRadius: expanded ? outerRadius : innerRadius, borderEndStartRadius: expanded ? outerRadius : innerRadius, borderStartEndRadius: outerRadius, borderEndEndRadius: outerRadius }}
        disabled={disabled || loading} aria-label={menuLabel ?? (language === "ko" ? "추가 작업" : "More actions")}><IconChevronDown className={cn("size-6 transition-transform duration-(--ien-motion-fast) motion-reduce:transition-none", expanded && "rotate-180")} /></Button></DropdownMenuTrigger>
      <DropdownMenuContent align="end">{menu}</DropdownMenuContent>
    </DropdownMenu>
  </div>
}

export { SplitButton }
