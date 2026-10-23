import { resolveIenTone, type IenToneProps } from "@/lib/ien-theme"
import { cva, type VariantProps } from "class-variance-authority"
import { Slot } from "radix-ui"

import { cn } from "cn"

const badgeVariants = cva(
  [
    "relative inline-flex shrink-0 items-center justify-center w-fit border font-medium whitespace-nowrap outline-none transition-shadow",
    "focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-1 focus-visible:ring-offset-background disabled:pointer-events-none disabled:opacity-(--ien-state-disabled-alpha)",
    "[&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*=size-])]:size-3",
  ],
  {
    variants: {
      variant: {
        fill: "ien-fill",
        weak: "ien-weak",
        clear: "ien-clear",
        default: "ien-fill",
        outline: "ien-outline",
        secondary: "ien-fill",
        info: "ien-fill",
        success: "ien-fill",
        warning: "ien-fill",
        destructive: "ien-fill",
        focus: "ien-fill",
        invert: "ien-fill",
        "primary-light": "ien-weak",
        "warning-light": "ien-weak",
        "success-light": "ien-weak",
        "info-light": "ien-weak",
        "destructive-light": "ien-weak",
        "invert-light": "ien-weak",
        "focus-light": "ien-weak",
        "primary-outline": "ien-outline",
        "warning-outline": "ien-outline",
        "success-outline": "ien-outline",
        "info-outline": "ien-outline",
        "destructive-outline": "ien-outline",
        "invert-outline": "ien-outline",
        "focus-outline": "ien-outline",
      },
      size: {
        xs: "px-1 py-0.25 text-[0.6rem] leading-none h-4 min-w-4 gap-1",
        sm: "px-1 py-0.25 text-[0.625rem] leading-none h-4.5 min-w-4.5 gap-1",
        default: "px-1.25 py-0.5 text-xs h-5 min-w-5 gap-1",
        lg: "px-1.5 py-0.5 text-xs h-5.5 min-w-5.5 gap-1",
        xl: "px-2 py-0.75 text-sm h-6 min-w-6 gap-1.5",
      },
      /** `default`: active style radius. `full`: pill radius. */
      radius: {
        default:
          "rounded-sm",
        full: "rounded-full",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
      radius: "default",
    },
  },
)

interface BadgeProps
  extends React.ComponentProps<"span">,
    VariantProps<typeof badgeVariants> {
  tone?: IenToneProps["tone"]
  useGradient?: boolean

  asChild?: boolean
}

function Badge({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  variant,
  size,
  radius,
  asChild = false,
  ...props
}: BadgeProps & IenToneProps) {
  const Comp = asChild ? Slot.Root : "span"

  return (
    <Comp
      data-slot="badge"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={resolveIenTone(ienTone, variant)}
      data-ien-gradient={ienUseGradient}
      data-variant={variant ?? "default"}
      className={cn(badgeVariants({ variant, size, radius, className }))}
      {...props}
    />
  )
}

export { Badge, badgeVariants, type BadgeProps }
