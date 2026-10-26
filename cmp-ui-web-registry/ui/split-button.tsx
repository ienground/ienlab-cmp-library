"use client"

import * as React from "react"
import { IconChevronDown } from "@tabler/icons-react"
import { Button } from "@/components/ui/button"
import { ButtonGroup } from "@/components/ui/button-group"
import { DropdownMenu, DropdownMenuContent, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { useIenLanguage } from "@/lib/ien-theme-context"

export type SplitButtonProps = React.ComponentProps<typeof Button> & {
  menu: React.ReactNode
  menuLabel?: string
  open?: boolean
  onOpenChange?: (open: boolean) => void
}

function SplitButton({ menu, menuLabel, open, onOpenChange, tone, variant, size, disabled, loading, ...props }: SplitButtonProps) {
  const language = useIenLanguage("ko")
  return <ButtonGroup>
    <Button {...props} tone={tone} variant={variant} size={size} disabled={disabled} loading={loading} />
    <DropdownMenu open={open} onOpenChange={onOpenChange}>
      <DropdownMenuTrigger asChild><Button type="button" tone={tone} variant={variant} size={size} className="px-3" disabled={disabled || loading} aria-label={menuLabel ?? (language === "ko" ? "추가 작업" : "More actions")}><IconChevronDown /></Button></DropdownMenuTrigger>
      <DropdownMenuContent align="end">{menu}</DropdownMenuContent>
    </DropdownMenu>
  </ButtonGroup>
}

export { SplitButton }
