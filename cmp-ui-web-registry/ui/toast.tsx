"use client"

import * as React from "react"
import { IconCheck, IconX } from "@tabler/icons-react"
import { cn } from "cn"
import { Toaster as Sonner, toast } from "sonner"
import { IenContinuousCorners } from "@/lib/ien-theme-context"

/** 화면 루트에 한 번 배치합니다. toast의 action으로 Snackbar 작업을 제공합니다. */
function Toaster({ style, toastOptions, icons, ...props }: React.ComponentProps<typeof Sonner>) {
  const classNames = toastOptions?.classNames
  const actionClassName = "m-0! h-auto! shrink-0 rounded-(--ien-radius-sm)! bg-transparent! px-(--ien-spacing-sm)! py-(--ien-spacing-xs)! text-[length:var(--ien-font-size-label1)]! leading-(--ien-line-height-label1)! font-bold! text-[#3182f6]! focus-visible:ring-2! focus-visible:ring-[#3182f6]!"

  return (
    <>
      <IenContinuousCorners />
      <Sonner
        position="bottom-center"
        offset="var(--ien-spacing-lg)"
        mobileOffset={{
          top: "var(--ien-spacing-lg)",
          right: "var(--ien-spacing-lg)",
          bottom: "calc(var(--ien-spacing-lg) + env(safe-area-inset-bottom, 0px))",
          left: "var(--ien-spacing-lg)",
        }}
        {...props}
        icons={{
          success: <IconCheck aria-hidden="true" />,
          error: <IconX aria-hidden="true" />,
          info: <IconCheck aria-hidden="true" />,
          warning: <IconCheck aria-hidden="true" />,
          ...icons,
        }}
        style={{
          "--normal-bg": "#191f28",
          "--normal-text": "#fff",
          "--normal-border": "transparent",
          "--border-radius": "var(--ien-radius-xl)",
          width: "min(420px, calc(100vw - var(--mobile-offset-left) - var(--mobile-offset-right)))",
          ...style,
        } as React.CSSProperties}
        toastOptions={{
          ...toastOptions,
          classNames: {
            ...classNames,
            toast: cn("rounded-(--ien-radius-xl) [--ien-control-corner-profile:cmp] focus-visible:outline-2! focus-visible:outline-offset-2! focus-visible:outline-[#3182f6]! motion-reduce:transition-none!", classNames?.toast),
            title: cn("font-normal! leading-(--ien-line-height-body2)!", classNames?.title),
            description: cn("text-white/70! leading-(--ien-line-height-body2)!", classNames?.description),
            icon: cn("m-0! size-(--ien-icon-md)! justify-center! rounded-full [--ien-control-corner-shape:round] bg-[color-mix(in_srgb,var(--ien-toast-icon,transparent)_15%,transparent)] text-(--ien-toast-icon) [&_svg]:m-0! [&_svg]:size-[70%]!", classNames?.icon),
            success: cn("[--ien-toast-icon:var(--ien-success)]", classNames?.success),
            error: cn("[--ien-toast-icon:var(--ien-danger)]", classNames?.error),
            info: cn("[--ien-toast-icon:var(--ien-info)]", classNames?.info),
            warning: cn("[--ien-toast-icon:var(--ien-warning)]", classNames?.warning),
            actionButton: cn(actionClassName, classNames?.actionButton),
            cancelButton: cn(actionClassName, "text-white/70!", classNames?.cancelButton),
            closeButton: cn("bg-[#191f28]! text-white! border-0!", classNames?.closeButton),
          },
          style: {
            background: "#191f28",
            color: "#fff",
            border: 0,
            borderRadius: "var(--ien-radius-xl)",
            boxShadow: "0 3px 6px rgb(0 0 0 / 15%), 0 6px 12px rgb(0 0 0 / 10%)",
            width: "fit-content",
            maxWidth: "100%",
            left: 0,
            right: 0,
            marginInline: "auto",
            padding: "var(--ien-spacing-md) var(--ien-spacing-lg)",
            gap: "var(--ien-spacing-md)",
            fontFamily: "var(--ien-font-sans)",
            fontSize: "var(--ien-font-size-body2)",
            lineHeight: "var(--ien-line-height-body2)",
            transitionDuration: "var(--ien-motion-normal), var(--ien-motion-fast), var(--ien-motion-normal), var(--ien-motion-fast)",
            transitionTimingFunction: "var(--ien-motion-standard-easing)",
            ...toastOptions?.style,
          },
        }}
      />
    </>
  )
}

/** 재시도·실행 취소 등 단일 작업을 포함한 알림을 표시합니다. */
function snackbar(message: string, action: { label: string; onClick: () => void }, options?: Parameters<typeof toast>[1]) {
  return toast(message, { ...options, action })
}

export { Toaster, toast, snackbar }
