import { resolveIenTone, type IenToneProps } from "@/lib/ien-theme"
import { cva, type VariantProps } from "class-variance-authority"

import { cn } from "cn"

const alertVariants = cva(
  [
    "group/ien-alert relative w-full text-sm border has-[>svg]:grid-cols-[calc(var(--spacing)*3)_1fr] grid-cols-[0_1fr] grid gap-y-0.5 items-center [&>svg:not([class*=size-])]:size-4",
    "has-[>[data-slot=alert-title]+[data-slot=alert-description]]:[&_[data-slot=alert-action]]:sm:row-end-3",
    "has-[>[data-slot=alert-title]+[data-slot=alert-description]]:items-start",
    "has-[>[data-slot=alert-title]+[data-slot=alert-description]]:[&_svg]:translate-y-0.5",
    "rounded-lg",
    "px-3",
    "py-2.5",
    "has-[>svg]:gap-x-2.5",
  ],
  {
    variants: {
      variant: {
        fill: "ien-fill",
        weak: "ien-weak",
        outline: "ien-outline",
        clear: "ien-clear",
        default: "ien-weak",
        destructive: "ien-weak",
        info: "ien-weak",
        success: "ien-weak",
        warning: "ien-weak",
        invert: "ien-fill",
      },
    },
    defaultVariants: {
      variant: "default",
    },
  },
)

function Alert({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  variant,
  ...props
}: React.ComponentProps<"div"> &
  VariantProps<typeof alertVariants> &
  IenToneProps) {
  return (
    <div
      data-slot="alert"
      data-ien=""
      data-ien-tone={resolveIenTone(ienTone, variant) ?? "neutral"}
      data-ien-gradient={ienUseGradient}
      data-variant={variant ?? "default"}
      role="alert"
      className={cn(alertVariants({ variant }), className)}
      {...props}
    />
  )
}

function AlertTitle({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="alert-title"
      data-ien-typography="title3"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "col-start-2 line-clamp-1 min-h-4 font-medium tracking-tight",
        className
      )}
      {...props}
    />
  )
}

function AlertDescription({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="alert-description"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "text-muted-foreground group-data-[variant=fill]/ien-alert:text-inherit group-data-[variant=invert]/ien-alert:text-inherit col-start-2 grid justify-items-start gap-1 text-sm [&_p]:leading-relaxed",
        className,
      )}
      {...props}
    />
  )
}

function AlertAction({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="alert-action"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "flex gap-1.5 max-sm:col-start-2 max-sm:mt-2 max-sm:justify-start sm:col-start-3 sm:row-start-1 sm:justify-end sm:self-center",
        className
      )}
      {...props}
    />
  )
}

export { Alert, AlertTitle, AlertDescription, AlertAction }
