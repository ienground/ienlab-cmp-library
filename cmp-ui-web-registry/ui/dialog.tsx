"use client"

import * as React from "react"
import { Dialog as DialogPrimitive } from "radix-ui"
import { IconX } from "@tabler/icons-react"
import { cn } from "cn"
import { Button } from "@/components/ui/button"
import { useIenLanguage } from "@/lib/ien-theme-context"

function Dialog(props: React.ComponentProps<typeof DialogPrimitive.Root>) {
  return <DialogPrimitive.Root {...props} />
}
function DialogTrigger(props: React.ComponentProps<typeof DialogPrimitive.Trigger>) {
  return <DialogPrimitive.Trigger data-slot="dialog-trigger" {...props} />
}
function DialogClose(props: React.ComponentProps<typeof DialogPrimitive.Close>) {
  return <DialogPrimitive.Close data-slot="dialog-close" {...props} />
}

export type DialogContentProps = React.ComponentProps<typeof DialogPrimitive.Content> & {
  showCloseButton?: boolean
  dismissible?: boolean
}

function DialogContent({ children, className, showCloseButton = true, dismissible = true, onEscapeKeyDown, onPointerDownOutside, onInteractOutside, onOpenAutoFocus, onCloseAutoFocus, ...props }: DialogContentProps) {
  const language = useIenLanguage("ko")
  const returnFocus = React.useRef<HTMLElement | null>(null)
  return <DialogPrimitive.Portal>
    <DialogPrimitive.Overlay data-slot="dialog-overlay" className="fixed inset-0 z-50 bg-(--ien-overlay) data-[state=open]:animate-in data-[state=open]:fade-in-0 data-[state=closed]:animate-out data-[state=closed]:fade-out-0 motion-reduce:animate-none" />
    <DialogPrimitive.Content {...props} data-slot="dialog-content"
      onOpenAutoFocus={(event) => { returnFocus.current = document.activeElement instanceof HTMLElement ? document.activeElement : null; onOpenAutoFocus?.(event) }}
      onCloseAutoFocus={(event) => {
        onCloseAutoFocus?.(event)
        if (!event.defaultPrevented && returnFocus.current?.isConnected) { event.preventDefault(); returnFocus.current.focus() }
      }}
      onEscapeKeyDown={(event) => { onEscapeKeyDown?.(event); if (!dismissible) event.preventDefault() }}
      onPointerDownOutside={(event) => { onPointerDownOutside?.(event); if (!dismissible) event.preventDefault() }}
      onInteractOutside={(event) => { onInteractOutside?.(event); if (!dismissible) event.preventDefault() }}
      className={cn("fixed left-1/2 top-1/2 z-50 flex max-h-[calc(100dvh-2rem)] w-[calc(100%-2rem)] max-w-md -translate-x-1/2 -translate-y-1/2 flex-col gap-ien-lg overflow-y-auto rounded-ien-xl border border-(--ien-border) bg-(--ien-surface) p-ien-xl text-(--ien-text-primary) shadow-lg outline-none data-[state=open]:animate-in data-[state=open]:fade-in-0 data-[state=open]:zoom-in-95 data-[state=closed]:animate-out data-[state=closed]:fade-out-0 motion-reduce:animate-none", className)}>
      {children}
      {showCloseButton && dismissible && <DialogClose asChild><Button variant="ghost" size="icon-sm" className="absolute end-3 top-3" aria-label={language === "ko" ? "닫기" : "Close"}><IconX /></Button></DialogClose>}
    </DialogPrimitive.Content>
  </DialogPrimitive.Portal>
}
function DialogHeader({ className, ...props }: React.ComponentProps<"div">) {
  return <div {...props} data-slot="dialog-header" className={cn("flex flex-col gap-ien-sm", className)} />
}
function DialogFooter({ className, ...props }: React.ComponentProps<"div">) {
  return <div {...props} data-slot="dialog-footer" className={cn("flex flex-wrap justify-end gap-ien-xs", className)} />
}
function DialogTitle({ className, ...props }: React.ComponentProps<typeof DialogPrimitive.Title>) {
  return <DialogPrimitive.Title {...props} data-slot="dialog-title" className={cn("pe-ien-xl text-[length:var(--ien-font-size-title2)] leading-(--ien-line-height-title2) font-semibold", className)} />
}
function DialogDescription({ className, ...props }: React.ComponentProps<typeof DialogPrimitive.Description>) {
  return <DialogPrimitive.Description {...props} data-slot="dialog-description" className={cn("text-[length:var(--ien-font-size-body2)] leading-(--ien-line-height-body2) text-(--ien-text-secondary)", className)} />
}

export { Dialog, DialogTrigger, DialogClose, DialogContent, DialogHeader, DialogFooter, DialogTitle, DialogDescription }
