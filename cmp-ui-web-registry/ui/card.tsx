import type { IenToneProps } from "@/lib/ien-theme"
import * as React from "react"
import { cn } from "cn"

function Card({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  size = "default",
  variant = "filled",
  toneVariant = "solid",
  ...props
}: React.ComponentProps<"div"> & {
  size?: "default" | "sm"
  variant?: "filled" | "outlined"
  toneVariant?: "solid" | "weak"
} & IenToneProps) {
  return (
    <div
      data-slot="card"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      data-variant={variant}
      data-tone-variant={toneVariant}
      data-size={size}
      className={cn(
        "ring-(--ien-card-border) gap-(--card-spacing) overflow-hidden rounded-xl py-(--card-spacing) text-sm ring-1 [--card-spacing:--spacing(4)] has-data-[slot=card-footer]:pb-0 has-[>img:first-child]:pt-0 data-[size=sm]:[--card-spacing:--spacing(3)] data-[size=sm]:has-data-[slot=card-footer]:pb-0 *:[img:first-child]:rounded-t-xl *:[img:last-child]:rounded-b-xl group/card flex flex-col",
        variant === "filled" ? "ien-card-filled" : "ien-card-outlined",
        className,
      )}
      {...props}
    />
  )
}

function CardHeader({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-header"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "gap-1 rounded-t-xl px-(--card-spacing) [.border-b]:pb-(--card-spacing) group/card-header @container/card-header grid auto-rows-min items-start has-data-[slot=card-action]:grid-cols-[1fr_auto] has-data-[slot=card-description]:grid-rows-[auto_auto]",
        className
      )}
      {...props}
    />
  )
}

function CardTitle({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-title"
      data-ien-typography="title2"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn("text-base leading-snug font-medium group-data-[size=sm]/card:text-sm font-sans", className)}
      {...props}
    />
  )
}

function CardDescription({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-description"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn("text-inherit text-sm opacity-75", className)}
      {...props}
    />
  )
}

function CardAction({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-action"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        " col-start-2 row-span-2 row-start-1 self-start justify-self-end",
        className
      )}
      {...props}
    />
  )
}

function CardContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-content"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn("px-(--card-spacing)", className)}
      {...props}
    />
  )
}

function CardFooter({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="card-footer"
      data-ien-typography="caption"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "bg-transparent rounded-b-xl border-t border-current/15 p-(--card-spacing) flex items-center",
        className,
      )}
      {...props}
    />
  )
}

export {
  Card,
  CardHeader,
  CardFooter,
  CardTitle,
  CardAction,
  CardDescription,
  CardContent,
}
