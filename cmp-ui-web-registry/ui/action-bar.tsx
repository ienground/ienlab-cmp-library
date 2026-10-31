"use client"

import * as React from "react"
import { Toolbar } from "radix-ui"
import { IconX } from "@tabler/icons-react"
import { Button } from "@/components/ui/button"
import { cn } from "cn"
import { useIenLanguage } from "@/lib/ien-theme-context"

export type ActionBarProps = React.ComponentProps<typeof Toolbar.Root> & { open: boolean; onOpenChange: (open: boolean) => void; selection?: React.ReactNode }
function ActionBar({ open, onOpenChange, selection, children, className, ...props }: ActionBarProps) {
  const language = useIenLanguage("ko")
  if (!open) return null
  return <Toolbar.Root {...props} data-slot="action-bar" aria-label={props["aria-label"] ?? (language === "ko" ? "선택 항목 작업" : "Selection actions")} className={cn("flex w-fit max-w-full flex-wrap items-center gap-2 rounded-3xl border border-(--ien-border) bg-(--ien-surface-raised) p-2 shadow-lg", className)}>
    {selection && <span className="px-2 text-[length:var(--ien-font-size-label1)]">{selection}</span>}{children}
    <Toolbar.Button asChild><Button type="button" variant="ghost" tone="neutral" size="icon-sm" onClick={() => onOpenChange(false)} aria-label={language === "ko" ? "닫기" : "Close"}><IconX /></Button></Toolbar.Button>
  </Toolbar.Root>
}
function ActionBarButton(props: React.ComponentProps<typeof Button>) {
  return <Toolbar.Button asChild><Button {...props} /></Toolbar.Button>
}
export { ActionBar, ActionBarButton }
