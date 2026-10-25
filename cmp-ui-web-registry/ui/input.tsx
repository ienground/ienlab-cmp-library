"use client"

import type { IenToneProps } from "@/lib/ien-theme"
import * as React from "react"
import { cn } from "cn"
import { TextFieldFrame, useTextField, type TextFieldOptions } from "@/lib/ien-text-field"

export type InputProps = React.ComponentProps<"input"> & IenToneProps & TextFieldOptions & {
  variant?: "boxed" | "underline" | "plain"
}

function Input({
  variant = "boxed",
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  type,
  label,
  labelOption = "appear",
  help,
  supportingText,
  hasError,
  lengthLimit,
  state,
  onValueChange,
  value,
  defaultValue,
  maxLength,
  onChange,
  onFocus,
  onBlur,
  required,
  disabled,
  readOnly,
  id,
  ref,
  "aria-describedby": describedBy,
  "aria-invalid": ariaInvalid,
  ...props
}: InputProps) {
  const field = useTextField({ value, defaultValue, lengthLimit, state, hasError, help, supportingText, id, "aria-describedby": describedBy, "aria-invalid": ariaInvalid })
  const input = (
    <input
      type={type}
      ref={(node) => {
        field.inputRef.current = node
        if (typeof ref === "function") return ref(node)
        if (ref) ref.current = node
      }}
      id={field.inputId}
      value={value}
      defaultValue={defaultValue}
      required={required}
      disabled={disabled || state?.enabled === false}
      readOnly={readOnly || state?.readOnly}
      maxLength={lengthLimit && lengthLimit.mode !== "none" ? undefined : maxLength}
      aria-invalid={field.invalid ? true : ariaInvalid}
      aria-describedby={field.descriptionIds}
      onFocus={(event) => { field.setFocused(true); onFocus?.(event) }}
      onBlur={(event) => { field.setFocused(false); onBlur?.(event) }}
      onChange={(event) => {
        const nextValue = event.currentTarget.value
        if (!field.accept(nextValue)) {
          event.currentTarget.value = field.currentValue
          return
        }
        onChange?.(event)
        onValueChange?.(nextValue)
      }}
      data-slot="input"
      data-variant={variant}
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "dark:bg-input/30 border-input focus-visible:border-ring focus-visible:ring-ring/50 aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive dark:aria-invalid:border-destructive/50 disabled:bg-input/50 dark:disabled:bg-input/80 h-8 rounded-lg border bg-transparent px-2.5 py-1 text-base transition-colors file:h-6 file:text-sm file:font-medium focus-visible:ring-3 aria-invalid:ring-3 md:text-sm w-full min-w-0 outline-none file:inline-flex file:border-0 file:bg-transparent file:text-foreground placeholder:text-muted-foreground disabled:pointer-events-none disabled:cursor-not-allowed disabled:opacity-(--ien-state-disabled-alpha)",
        variant === "underline"
          ? "rounded-none border-x-0 border-t-0 bg-transparent px-0 dark:bg-transparent"
          : variant === "plain"
            ? "border-transparent bg-transparent shadow-none focus-visible:border-transparent dark:bg-transparent"
            : "",
        !field.invalid && state?.status?.type === "success" && "border-(--ien-success) focus-visible:border-(--ien-success)",
        className,
      )}
      {...props}
    />
  )
  return (
    <TextFieldFrame field={field} label={label} labelOption={labelOption} required={required} disabled={disabled} state={state} slot="input">
      {input}
    </TextFieldFrame>
  )
}

export { Input }
