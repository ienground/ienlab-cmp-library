import type { IenToneProps } from "@/lib/ien-theme"
import { cva, type VariantProps } from "class-variance-authority"
import { cn } from "cn"
import { Slot } from "radix-ui"

import { Separator } from "@/components/ui/separator"

const buttonGroupVariants = cva(
  "has-[>[data-slot=button-group]]:gap-2 has-[select[aria-hidden=true]:last-child]:[&>[data-slot=select-trigger]:last-of-type]:rounded-r-lg group/button-group flex w-fit items-stretch *:focus-visible:relative *:focus-visible:z-10 [&>[data-slot=select-trigger]:not([class*='w-'])]:w-fit [&>input]:flex-1",
  {
    variants: {
      orientation: {
        horizontal:
          "[&>[data-slot]:not(:has(~[data-slot]))]:rounded-r-lg! [&>*:not(:first-child)]:rounded-l-none [&>:not([data-slot=button-group]):not([data-slot=button-group-separator])+*:not([data-slot=button-group]):not([data-slot=button-group-separator])]:-ml-px [&>*:not(:last-child)]:rounded-r-none",
        vertical:
          "[&>[data-slot]:not(:has(~[data-slot]))]:rounded-b-lg! flex-col [&>*:not(:first-child)]:rounded-t-none [&>:not([data-slot=button-group]):not([data-slot=button-group-separator])+*:not([data-slot=button-group]):not([data-slot=button-group-separator])]:-mt-px [&>*:not(:last-child)]:rounded-b-none",
      },
    },
    defaultVariants: {
      orientation: "horizontal",
    },
  }
)

function ButtonGroup({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  orientation = "horizontal",
  ...props
}: React.ComponentProps<"div"> &
  VariantProps<typeof buttonGroupVariants> &
  IenToneProps) {
  return (
    <div
      role="group"
      data-slot="button-group"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      data-orientation={orientation}
      className={cn(buttonGroupVariants({ orientation }), className)}
      {...props}
    />
  )
}

function ButtonGroupText({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  asChild = false,
  ...props
}: React.ComponentProps<"div"> & {
  asChild?: boolean
} & IenToneProps) {
  const Comp = asChild ? Slot.Root : "div"

  return (
    <Comp
      className={cn(
        "bg-muted gap-2 rounded-lg border px-2.5 text-sm font-medium [&_svg:not([class*='size-'])]:size-4 flex items-center [&_svg]:pointer-events-none",
        className
      )}
      {...props}
    />
  )
}

function ButtonGroupSeparator({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  orientation = "vertical",
  ...props
}: React.ComponentProps<typeof Separator> & IenToneProps) {
  return (
    <Separator
      data-slot="button-group-separator"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      orientation={orientation}
      className={cn(
        "bg-input relative self-stretch data-[orientation=horizontal]:mx-px data-[orientation=horizontal]:w-auto data-[orientation=vertical]:my-px data-[orientation=vertical]:h-auto",
        className
      )}
      {...props}
    />
  )
}

export {
  ButtonGroup,
  ButtonGroupSeparator,
  ButtonGroupText,
  buttonGroupVariants,
}
