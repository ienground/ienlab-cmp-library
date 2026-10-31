"use client"

import * as React from "react"
import { Dialog, DialogClose, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { useIenLanguage } from "@/lib/ien-theme-context"
import type { IenTone } from "@/lib/ien-theme"

export type AlertDialogProps = React.ComponentProps<typeof Dialog> & {
  title: React.ReactNode
  message?: React.ReactNode
  icon?: React.ReactNode
  confirmText?: string
  onConfirm?: () => void
  confirmDisabled?: boolean
  tone?: IenTone
  actions?: React.ReactNode
  children?: React.ReactNode
  buttonLayout?: "horizontal" | "vertical"
}

function AlertDialog({ title, message, icon, confirmText, onConfirm, confirmDisabled, tone, actions, children, buttonLayout = "horizontal", onOpenChange, ...props }: AlertDialogProps) {
  const language = useIenLanguage("ko")
  const contentRef = React.useRef<HTMLDivElement>(null)
  return <Dialog {...props} onOpenChange={onOpenChange}>
    <DialogContent ref={contentRef} role="alertdialog" showCloseButton={false}
      onPointerDownOutside={(event) => event.preventDefault()}
      onOpenAutoFocus={(event) => {
        const cancel = contentRef.current?.querySelector<HTMLButtonElement>("[data-dialog-cancel]:not(:disabled)")
        if (cancel) { event.preventDefault(); cancel.focus() }
      }}>
      <DialogHeader>{icon != null && <div aria-hidden="true" className="text-(--ien-brand)">{icon}</div>}<DialogTitle>{title}</DialogTitle><DialogDescription className={message == null ? "sr-only" : undefined}>{message ?? title}</DialogDescription></DialogHeader>
      {children}
      {actions !== null && <DialogFooter className={buttonLayout === "vertical" ? "flex-col [&>button]:w-full" : undefined}>
        {actions !== undefined ? actions : <DialogClose asChild><Button tone={tone} disabled={confirmDisabled} onClick={onConfirm}>{confirmText ?? (language === "ko" ? "확인" : "OK")}</Button></DialogClose>}
      </DialogFooter>}
    </DialogContent>
  </Dialog>
}

export type ConfirmDialogProps = AlertDialogProps & {
  cancelText?: string
  onCancel?: () => void
  cancelDisabled?: boolean
}

function ConfirmDialog({ cancelText, onCancel, cancelDisabled, confirmText, onConfirm, confirmDisabled, tone, onOpenChange, ...props }: ConfirmDialogProps) {
  const language = useIenLanguage("ko")
  return <AlertDialog {...props} onOpenChange={onOpenChange} actions={<>
    <DialogClose asChild><Button data-dialog-cancel="" variant="weak" tone="neutral" disabled={cancelDisabled} onClick={onCancel}>{cancelText ?? (language === "ko" ? "취소" : "Cancel")}</Button></DialogClose>
    <DialogClose asChild><Button tone={tone} disabled={confirmDisabled} onClick={onConfirm}>{confirmText ?? (language === "ko" ? "확인" : "Confirm")}</Button></DialogClose>
  </>} />
}

export { AlertDialog, ConfirmDialog }
