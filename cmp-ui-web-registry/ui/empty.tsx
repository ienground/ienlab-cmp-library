"use client"

import type { IenToneProps } from "@/lib/ien-theme"
import { useIenTheme } from "@/lib/ien-theme-context"
import { cva, type VariantProps } from "class-variance-authority"
import { cn } from "cn"

function Empty({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  const theme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <div
      data-slot="empty"
      data-ien=""
      data-ien-tone={theme.tone}
      data-ien-gradient={theme.useGradient}
      className={cn(
        "gap-2 rounded-xl border-dashed flex w-full min-w-0 flex-1 flex-col items-center justify-center text-center text-balance",
        className
      )}
      {...props}
    />
  )
}

function EmptyHeader({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="empty-header"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "gap-2 flex max-w-sm flex-col items-center",
        className
      )}
      {...props}
    />
  )
}

const emptyMediaVariants = cva(
  "mb-2 flex shrink-0 items-center justify-center [&_svg]:pointer-events-none [&_svg]:shrink-0",
  {
    variants: {
      variant: {
        default: "bg-transparent",
        icon: "bg-(--ien-control-weak) text-(--ien-control-color) flex size-16 shrink-0 items-center justify-center rounded-2xl [&_svg:not([class*='size-'])]:size-8",
      },
    },
    defaultVariants: {
      variant: "default",
    },
  }
)

function EmptyMedia({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  variant = "default",
  ...props
}: React.ComponentProps<"div"> &
  VariantProps<typeof emptyMediaVariants> &
  IenToneProps) {
  return (
    <div
      data-slot="empty-media"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      data-variant={variant}
      className={cn(emptyMediaVariants({ variant, className }))}
      {...props}
    />
  )
}

function EmptyTitle({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="empty-title"
      data-ien-typography="title2"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn("text-[length:var(--ien-font-size-title2)] leading-(--ien-line-height-title2) font-bold", className)}
      {...props}
    />
  )
}

function EmptyDescription({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"p"> & IenToneProps) {
  return (
    <div
      data-slot="empty-description"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "text-[length:var(--ien-font-size-body2)] leading-(--ien-line-height-body2) text-muted-foreground [&>a]:underline [&>a]:underline-offset-4 [&>a:hover]:text-primary",
        className
      )}
      {...props}
    />
  )
}

function EmptyContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="empty-content"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "gap-2.5 text-sm flex w-full max-w-sm min-w-0 flex-col items-center text-balance",
        className
      )}
      {...props}
    />
  )
}

export {
  Empty,
  EmptyHeader,
  EmptyTitle,
  EmptyDescription,
  EmptyContent,
  EmptyMedia,
}
