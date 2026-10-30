import type { ComponentProps } from "react"
import { Slot } from "radix-ui"
import { cva, type VariantProps } from "class-variance-authority"
import { cn } from "cn"

const typographyVariants = cva("font-sans", {
  variants: {
    variant: {
      display: "text-ien-display",
      title1: "text-ien-title1",
      title2: "text-ien-title2",
      title3: "text-ien-title3",
      body1: "text-ien-body1",
      body2: "text-ien-body2",
      label1: "text-ien-label1",
      label2: "text-ien-label2",
      caption: "text-ien-caption",
    },
  },
  defaultVariants: { variant: "body1" },
})

function Typography({
  variant = "body1",
  asChild = false,
  className,
  ...props
}: ComponentProps<"p"> & VariantProps<typeof typographyVariants> & {
  asChild?: boolean
}) {
  const Comp = asChild ? Slot.Root : "p"
  return (
    <Comp
      data-slot="typography"
      data-ien-typography={variant}
      className={cn(typographyVariants({ variant }), className)}
      {...props}
    />
  )
}

export { Typography, typographyVariants }
