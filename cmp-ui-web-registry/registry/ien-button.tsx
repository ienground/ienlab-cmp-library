import * as React from "react"
import { cva, type VariantProps } from "class-variance-authority"
import { cn } from "cn"
import { Slot } from "radix-ui"

const buttonVariants = cva(
  "group/button inline-flex shrink-0 items-center justify-center rounded-[var(--ien-button-radius)] border-[length:var(--ien-stroke-thin)] border-transparent bg-clip-padding text-[length:var(--ien-button-font-size-default)] leading-[var(--ien-button-line-height-default)] font-[var(--ien-button-font-weight)] whitespace-nowrap transition-[scale] duration-[var(--ien-button-press-duration)] ease-[var(--ien-button-press-easing)] motion-reduce:transition-none motion-reduce:active:scale-100 outline-none select-none focus-visible:border-ring focus-visible:ring-[length:var(--ien-button-focus-ring-width)] focus-visible:ring-[color:color-mix(in_oklab,var(--ring)_var(--ien-button-focus-ring-alpha),transparent)] active:scale-[var(--ien-button-pressed-scale)] disabled:bg-none disabled:pointer-events-none disabled:opacity-[var(--ien-button-disabled-alpha)] aria-invalid:border-destructive aria-invalid:ring-3 aria-invalid:ring-destructive/20 dark:aria-invalid:border-destructive/50 dark:aria-invalid:ring-destructive/40 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-default)]",
  {
    variants: {
      variant: {
        fill:
          "border-[var(--ien-button-tone)] bg-[var(--ien-button-tone)] text-[var(--ien-button-on-tone)] active:inset-shadow-[0_0_0_100vmax_var(--ien-button-pressed-overlay-fill)]",
        weak:
          "border-[var(--ien-button-tone-weak)] bg-[var(--ien-button-tone-weak)] text-[var(--ien-button-tone)] active:inset-shadow-[0_0_0_100vmax_var(--ien-button-pressed-overlay-fill)]",
        line:
          "border-[var(--ien-button-tone)] bg-transparent text-[var(--ien-button-tone)] active:inset-shadow-[0_0_0_100vmax_var(--ien-button-pressed-overlay-clear)]",
        ghost:
          "bg-transparent text-[var(--ien-button-tone)] active:inset-shadow-[0_0_0_100vmax_var(--ien-button-pressed-overlay-clear)]",
      },
      tone: {
        neutral:
          "[--ien-button-tone:var(--ien-neutral)] [--ien-button-on-tone:var(--ien-on-neutral)] [--ien-button-tone-weak:var(--ien-neutral-weak)]",
        brand:
          "[--ien-button-tone:var(--ien-brand)] [--ien-button-on-tone:var(--ien-on-brand)] [--ien-button-tone-weak:var(--ien-brand-weak)]",
        success:
          "[--ien-button-tone:var(--ien-success)] [--ien-button-on-tone:var(--ien-on-success)] [--ien-button-tone-weak:var(--ien-success-weak)]",
        warning:
          "[--ien-button-tone:var(--ien-warning)] [--ien-button-on-tone:var(--ien-on-warning)] [--ien-button-tone-weak:var(--ien-warning-weak)]",
        danger:
          "[--ien-button-tone:var(--ien-danger)] [--ien-button-on-tone:var(--ien-on-danger)] [--ien-button-tone-weak:var(--ien-danger-weak)]",
        info:
          "[--ien-button-tone:var(--ien-info)] [--ien-button-on-tone:var(--ien-on-info)] [--ien-button-tone-weak:var(--ien-info-weak)]",
      },
      useGradient: {
        true: "",
        false: "",
      },
      size: {
        default:
          "h-[var(--ien-button-height-default)] gap-[var(--ien-button-gap-default)] px-[var(--ien-button-padding-default)] has-data-[icon=inline-end]:pr-[var(--ien-button-icon-padding-default)] has-data-[icon=inline-start]:pl-[var(--ien-button-icon-padding-default)]",
        xs:
          "h-[var(--ien-button-height-xs)] gap-[var(--ien-button-gap-xs)] px-[var(--ien-button-padding-xs)] text-[length:var(--ien-button-font-size-xs)] leading-[var(--ien-button-line-height-xs)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-xs)] in-data-[slot=button-group]:rounded-[var(--ien-button-radius-group)] has-data-[icon=inline-end]:pr-[var(--ien-button-icon-padding-xs)] has-data-[icon=inline-start]:pl-[var(--ien-button-icon-padding-xs)]",
        sm:
          "h-[var(--ien-button-height-sm)] gap-[var(--ien-button-gap-sm)] px-[var(--ien-button-padding-sm)] text-[length:var(--ien-button-font-size-sm)] leading-[var(--ien-button-line-height-sm)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-sm)] in-data-[slot=button-group]:rounded-[var(--ien-button-radius-group)] has-data-[icon=inline-end]:pr-[var(--ien-button-icon-padding-sm)] has-data-[icon=inline-start]:pl-[var(--ien-button-icon-padding-sm)]",
        lg:
          "h-[var(--ien-button-height-lg)] gap-[var(--ien-button-gap-lg)] px-[var(--ien-button-padding-lg)] text-[length:var(--ien-button-font-size-lg)] leading-[var(--ien-button-line-height-lg)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-lg)] has-data-[icon=inline-end]:pr-[var(--ien-button-icon-padding-lg)] has-data-[icon=inline-start]:pl-[var(--ien-button-icon-padding-lg)]",
        "icon":
          "size-[var(--ien-button-height-default)]",
        "icon-xs":
          "size-[var(--ien-button-height-xs)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-xs)] in-data-[slot=button-group]:rounded-[var(--ien-button-radius-group)]",
        "icon-sm":
          "size-[var(--ien-button-height-sm)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-sm)] in-data-[slot=button-group]:rounded-[var(--ien-button-radius-group)]",
        "icon-lg":
          "size-[var(--ien-button-height-lg)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-lg)]",
      },
    },
    compoundVariants: [
      {
        variant: "fill",
        useGradient: true,
        className:
          "bg-[image:linear-gradient(to_bottom_right_in_srgb,color-mix(in_oklab,var(--ien-button-tone),var(--ien-button-on-tone)_var(--ien-button-gradient-fill-start)),var(--ien-button-tone),color-mix(in_oklab,var(--ien-button-tone),var(--ien-button-on-tone)_var(--ien-button-gradient-fill-end)))]",
      },
      {
        variant: "weak",
        useGradient: true,
        className:
          "bg-[image:linear-gradient(to_bottom_right_in_srgb,color-mix(in_oklab,var(--ien-button-tone-weak),var(--ien-button-tone)_var(--ien-button-gradient-weak-start)),var(--ien-button-tone-weak),var(--ien-button-tone-weak))]",
      },
    ],
    defaultVariants: {
      variant: "fill",
      tone: "brand",
      size: "default",
      useGradient: true,
    },
  }
)

export type ButtonProps = React.ComponentProps<"button"> &
  VariantProps<typeof buttonVariants> & {
    asChild?: boolean
  }

export type IenButtonProps = ButtonProps
export type IenButtonVariant = NonNullable<ButtonProps["variant"]>
export type IenSemanticTone = NonNullable<ButtonProps["tone"]>
export type IenButtonSize = NonNullable<ButtonProps["size"]>

function Button({
  className,
  variant = "fill",
  tone = "brand",
  size = "default",
  useGradient = true,
  asChild = false,
  ...props
}: ButtonProps) {
  const Comp = asChild ? Slot.Root : "button"

  return (
    <Comp
      data-slot="button"
      data-variant={variant}
      data-tone={tone}
      data-size={size}
      data-gradient={useGradient}
      className={cn(buttonVariants({ variant, tone, size, useGradient, className }))}
      {...props}
    />
  )
}

export { Button, Button as IenButton, buttonVariants }
