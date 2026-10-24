"use client"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"

import type { ReactNode } from "react"
import { createContext, useContext, useId } from "react"
import { NumberField as NumberFieldPrimitive } from "@base-ui/react/number-field"
import type { VariantProps } from "class-variance-authority"
import { cva } from "class-variance-authority"

import { cn } from "cn"
import { Label } from "@/components/ui/label"
import { IconPlaceholder } from "@/lib/reui-icons"

const NumberFieldContext = createContext<{
  fieldId: string
  size: NonNullable<VariantProps<typeof numberFieldGroupVariants>["size"]>
} | null>(null)

const numberFieldGroupVariants = cva(
  "relative flex w-fit items-center rounded-lg bg-muted px-(--ien-spacing-xxs) text-foreground data-disabled:pointer-events-none data-disabled:opacity-(--ien-state-disabled-alpha)",
  {
    variants: {
      size: {
        tiny: "h-8 min-w-23 text-xs",
        small: "h-9 min-w-26 text-sm",
        medium: "h-11 min-w-30 text-base",
        large: "h-13 min-w-35 text-lg",
        sm: "h-9 min-w-26 text-sm",
        default: "h-11 min-w-30 text-base",
        lg: "h-13 min-w-35 text-lg",
      },
    },
    defaultVariants: {
      size: "medium",
    },
  },
)

const numberFieldButtonVariants = cva(
  "relative flex h-full shrink-0 cursor-pointer items-center justify-center rounded-lg text-muted-foreground transition-colors hover:bg-background/50 hover:text-foreground focus-visible:outline-none focus-visible:ring-3 focus-visible:ring-ring/50 data-disabled:pointer-events-none data-disabled:text-(--ien-text-disabled) pointer-coarse:after:absolute pointer-coarse:after:size-full pointer-coarse:after:min-h-11 pointer-coarse:after:min-w-11",
  {
    variants: {
      size: {
        tiny: "w-7.5 [&_svg]:size-3.5",
        small: "w-8.5 [&_svg]:size-4",
        medium: "w-10 [&_svg]:size-4",
        large: "w-12 [&_svg]:size-5",
        sm: "w-8.5 [&_svg]:size-4",
        default: "w-10 [&_svg]:size-4",
        lg: "w-12 [&_svg]:size-5",
      },
    },
    defaultVariants: {
      size: "medium",
    },
  }
)

const numberFieldInputVariants = cva(
  "relative z-10 min-w-0 rounded-lg border border-transparent bg-background px-2 text-center font-medium text-foreground tabular-nums shadow-md outline-none transition-colors cursor-text select-text placeholder:text-muted-foreground focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50 aria-invalid:border-destructive aria-invalid:ring-3 aria-invalid:ring-destructive/20 disabled:cursor-not-allowed",
  {
    variants: {
      size: {
        tiny: "h-7 w-10 text-xs",
        small: "h-8 w-11.5 text-sm",
        medium: "h-9.5 w-14 text-base",
        large: "h-11.5 w-17 text-lg",
        sm: "h-8 w-11.5 text-sm",
        default: "h-9.5 w-14 text-base",
        lg: "h-11.5 w-17 text-lg",
      },
    },
    defaultVariants: {
      size: "medium",
    },
  }
)

function NumberField({
  tone: ienTone,
  useGradient: ienUseGradient,
  id,
  className,
  size = "medium",
  ...props
}: NumberFieldPrimitive.Root.Props &
  VariantProps<typeof numberFieldGroupVariants> &
  IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const generatedId = useId()
  const fieldId = id ?? generatedId
  const sizeValue = size ?? "medium"

  return (
    <IenThemeContext.Provider value={ienTheme}>
      <NumberFieldContext.Provider value={{ fieldId, size: sizeValue }}>
        <NumberFieldPrimitive.Root
          className={cn("flex w-full flex-col items-start gap-2", className)}
          data-size={sizeValue}
          data-slot="number-field"
          data-ien=""
          data-ien-tone={ienTheme.tone}
          data-ien-gradient={ienTheme.useGradient}
          id={fieldId}
          {...props}
        />
      </NumberFieldContext.Provider>
    </IenThemeContext.Provider>
  )
}

function NumberFieldGroup({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  size: sizeProp,
  ...props
}: NumberFieldPrimitive.Group.Props &
  Partial<VariantProps<typeof numberFieldGroupVariants>> &
  IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const context = useContext(NumberFieldContext)
  if (!context) {
    throw new Error(
      "NumberFieldGroup must be used within a NumberField component."
    )
  }
  const size = sizeProp ?? context.size

  return (
    <NumberFieldPrimitive.Group
      className={cn(numberFieldGroupVariants({ size }), className)}
      data-slot="number-field-group"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    />
  )
}

function NumberFieldDecrement({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  size: sizeProp,
  children,
  ...props
}: NumberFieldPrimitive.Decrement.Props &
  Partial<VariantProps<typeof numberFieldButtonVariants>> & {
    children?: React.ReactNode
  } & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const context = useContext(NumberFieldContext)
  if (!context) {
    throw new Error(
      "NumberFieldDecrement must be used within a NumberField component."
    )
  }
  const size = sizeProp ?? context.size

  return (
    <NumberFieldPrimitive.Decrement
      className={cn(
        numberFieldButtonVariants({ size }),
        "rounded-s-lg",
        className
      )}
      data-slot="number-field-decrement"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    >
      {children ?? (
        <IconPlaceholder
          lucide="MinusIcon"
          tabler="IconMinus"
          hugeicons="MinusSignIcon"
          phosphor="MinusIcon"
          remixicon="RiSubtractLine"
        />
      )}
    </NumberFieldPrimitive.Decrement>
  )
}

function NumberFieldIncrement({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  size: sizeProp,
  children,
  ...props
}: NumberFieldPrimitive.Increment.Props &
  Partial<VariantProps<typeof numberFieldButtonVariants>> & {
    children?: ReactNode
  } & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const context = useContext(NumberFieldContext)
  if (!context) {
    throw new Error(
      "NumberFieldIncrement must be used within a NumberField component."
    )
  }
  const size = sizeProp ?? context.size

  return (
    <NumberFieldPrimitive.Increment
      className={cn(
        numberFieldButtonVariants({ size }),
        "rounded-e-lg",
        className
      )}
      data-slot="number-field-increment"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    >
      {children ?? (
        <IconPlaceholder
          lucide="PlusIcon"
          tabler="IconPlus"
          hugeicons="PlusSignIcon"
          phosphor="PlusIcon"
          remixicon="RiAddLine"
        />
      )}
    </NumberFieldPrimitive.Increment>
  )
}

function NumberFieldInput({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  size: sizeProp,
  ...props
}: NumberFieldPrimitive.Input.Props &
  Partial<VariantProps<typeof numberFieldInputVariants>> &
  IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const context = useContext(NumberFieldContext)
  if (!context) {
    throw new Error(
      "NumberFieldInput must be used within a NumberField component."
    )
  }
  const size = sizeProp ?? context.size

  return (
    <NumberFieldPrimitive.Input
      className={cn(numberFieldInputVariants({ size }), className)}
      data-slot="number-field-input"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    />
  )
}

function NumberFieldScrubArea({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  label,
  ...props
}: React.ComponentProps<typeof Label> & {
  label: string
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const context = useContext(NumberFieldContext)
  if (!context) {
    throw new Error(
      "NumberFieldScrubArea must be used within a NumberField component for accessibility."
    )
  }

  return (
    <Label
      className={cn("cursor-default", className)}
      data-slot="number-field-scrub-area"
      tone={ienTheme.tone ?? "neutral"}
      useGradient={ienTheme.useGradient}
      htmlFor={context.fieldId}
      {...props}
    >
      {label}
    </Label>
  )
}

export {
  NumberField,
  NumberFieldScrubArea,
  NumberFieldDecrement,
  NumberFieldIncrement,
  NumberFieldGroup,
  NumberFieldInput,
}
