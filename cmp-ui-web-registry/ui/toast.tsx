"use client"

import * as React from "react"
import { Toaster as Sonner, toast } from "sonner"

/** 화면 루트에 한 번 배치합니다. toast의 action으로 Snackbar 작업을 제공합니다. */
function Toaster({ style, toastOptions, ...props }: React.ComponentProps<typeof Sonner>) {
  return <Sonner position="bottom-center" closeButton {...props} style={{
    "--normal-bg": "var(--ien-surface-raised)", "--normal-text": "var(--ien-text-primary)", "--normal-border": "var(--ien-border)",
    "--success-bg": "var(--ien-success-weak)", "--success-text": "var(--ien-on-success-weak)", "--success-border": "var(--ien-success-weak)",
    "--error-bg": "var(--ien-danger-weak)", "--error-text": "var(--ien-on-danger-weak)", "--error-border": "var(--ien-danger-weak)",
    "--info-bg": "var(--ien-info-weak)", "--info-text": "var(--ien-on-info-weak)", "--info-border": "var(--ien-info-weak)",
    "--warning-bg": "var(--ien-warning-weak)", "--warning-text": "var(--ien-on-warning-weak)", "--warning-border": "var(--ien-warning-weak)",
    "--border-radius": "var(--ien-radius-default)", ...style,
  } as React.CSSProperties} toastOptions={{ ...toastOptions, style: { fontFamily: "var(--ien-font-sans)", fontSize: "var(--ien-font-size-body2)", lineHeight: "var(--ien-line-height-body2)", ...toastOptions?.style } }} />
}

/** 재시도·실행 취소 등 단일 작업을 포함한 알림을 표시합니다. */
function snackbar(message: string, action: { label: string; onClick: () => void }, options?: Parameters<typeof toast>[1]) {
  return toast(message, { ...options, action })
}

export { Toaster, toast, snackbar }
