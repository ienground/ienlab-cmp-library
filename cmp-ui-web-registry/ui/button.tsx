"use client"

import { ProgressIndicator } from "@/components/ui/progress-indicator"
import { IenContinuousCorners } from "@/lib/ien-theme-context"
import { resolveIenTone, type IenToneProps } from "@/lib/ien-theme"
import * as React from "react"
import { cva, type VariantProps } from "class-variance-authority"
import { cn } from "cn"
import { Slot } from "radix-ui"

const buttonVariants = cva(
  "focus-visible:border-ring focus-visible:ring-ring/50 aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive dark:aria-invalid:border-destructive/50 rounded-lg border bg-clip-border text-sm font-medium focus-visible:ring-3 aria-invalid:ring-3 [&_svg:not([class*='size-'])]:size-4 group/button inline-flex shrink-0 items-center justify-center whitespace-nowrap transition-all outline-none select-none disabled:pointer-events-none disabled:opacity-(--ien-state-disabled-alpha) [&_svg]:pointer-events-none [&_svg]:shrink-0",
  {
    variants: {
      variant: {
        fill: "ien-fill",
        weak: "ien-weak",
        line: "ien-outline",
        ghost: "ien-clear",
      },
      size: {
        default: "h-8 gap-1.5 px-2.5 has-data-[icon=inline-end]:pr-2 has-data-[icon=inline-start]:pl-2",
        xs: "h-6 gap-1 rounded-[min(var(--radius-md),10px)] px-2 text-xs in-data-[slot=button-group]:rounded-lg has-data-[icon=inline-end]:pr-1.5 has-data-[icon=inline-start]:pl-1.5 [&_svg:not([class*='size-'])]:size-3",
        sm: "h-7 gap-1 rounded-[min(var(--radius-md),12px)] px-2.5 text-[0.8rem] in-data-[slot=button-group]:rounded-lg has-data-[icon=inline-end]:pr-1.5 has-data-[icon=inline-start]:pl-1.5 [&_svg:not([class*='size-'])]:size-3.5",
        lg: "h-9 gap-1.5 px-2.5 has-data-[icon=inline-end]:pr-2 has-data-[icon=inline-start]:pl-2",
        icon: "size-8",
        "icon-xs": "size-6 rounded-[min(var(--radius-md),10px)] in-data-[slot=button-group]:rounded-lg [&_svg:not([class*='size-'])]:size-3",
        "icon-sm": "size-7 rounded-[min(var(--radius-md),12px)] in-data-[slot=button-group]:rounded-lg",
        "icon-lg": "size-9",
      },
    },
    defaultVariants: {
      variant: "fill",
      size: "default",
    },
  },
)

function Button({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  variant = "fill",
  size = "default",
  asChild = false,
  loading = false,
  disabled,
  children,
  onClickCapture,
  onKeyDownCapture,
  ...props
}: React.ComponentProps<"button"> &
  VariantProps<typeof buttonVariants> & {
    asChild?: boolean
    /** 콘텐츠를 로더로 전환하고 색상을 유지한 채 입력을 막습니다. */
    loading?: boolean
  } & IenToneProps) {
  const Comp = asChild ? Slot.Root : "button"
  const blocked = disabled || loading
  const spinnerRef = React.useRef<HTMLSpanElement>(null)
  const [showSpinner, setShowSpinner] = React.useState(loading)
  React.useEffect(() => {
    if (loading) {
      setShowSpinner(true)
      return
    }
    if (!showSpinner || !spinnerRef.current) return
    const duration = Number.parseFloat(getComputedStyle(spinnerRef.current).transitionDuration) * 1000
    const timeout = window.setTimeout(() => setShowSpinner(false), duration || 0)
    return () => window.clearTimeout(timeout)
  }, [loading, showSpinner])
  const transitionClassName = "transition-[opacity,transform] duration-(--ien-motion-fast) ease-(--ien-motion-standard-easing) motion-reduce:transition-none"
  const content = (value: React.ReactNode) => (
    <span
      data-slot="button-content"
      className={cn("inline-flex items-center justify-center gap-[inherit]", transitionClassName, loading && "scale-80 opacity-0")}
    >
      {value}
    </span>
  )
  const child = asChild ? React.Children.only(children) : null
  const slottedContent = React.isValidElement<{ children?: React.ReactNode }>(child)
    ? React.cloneElement(child, {}, content(child.props.children))
    : children

  return (
    <>
      <IenContinuousCorners />
      <Comp
        {...props}
        data-slot="button"
        data-ien-typography="label1"
        data-ien=""
        data-ien-tone={resolveIenTone(ienTone, variant)}
        data-ien-gradient={ienUseGradient}
        data-variant={variant}
        data-size={size}
        data-loading={loading || undefined}
        aria-busy={loading || props["aria-busy"]}
        aria-disabled={blocked || props["aria-disabled"]}
        disabled={blocked}
        className={cn(buttonVariants({ variant, size, className: cn("relative", className) }))}
        onClickCapture={(event) => {
          if (blocked) {
            event.preventDefault()
            event.stopPropagation()
            return
          }
          onClickCapture?.(event)
        }}
        onKeyDownCapture={(event) => {
          if (blocked && (event.key === "Enter" || event.key === " ")) {
            event.preventDefault()
            event.stopPropagation()
            return
          }
          onKeyDownCapture?.(event)
        }}
      >
        {asChild ? <Slot.Slottable>{slottedContent}</Slot.Slottable> : content(children)}
        <span
          ref={spinnerRef}
          data-slot="button-spinner"
          aria-hidden="true"
          className={cn("pointer-events-none absolute inset-0 inline-flex items-center justify-center", transitionClassName, !loading && "scale-80 opacity-0")}
        >
          {(loading || showSpinner) && <ProgressIndicator size={20} strokeWidth={2} color="currentColor" trackColor="transparent" />}
        </span>
      </Comp>
    </>
  )
}

export { Button, buttonVariants }
