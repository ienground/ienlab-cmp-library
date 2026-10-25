"use client"

import type { IenToneProps } from "@/lib/ien-theme"
import * as React from "react"
import { cn } from "cn"
import { TextFieldFrame, useTextField, type TextFieldOptions, type TextFieldLengthLimit, type TextFieldState } from "@/lib/ien-text-field"

export type TextareaLengthLimit = TextFieldLengthLimit
export type TextareaState = TextFieldState

export type TextareaProps = React.ComponentProps<"textarea"> & IenToneProps & TextFieldOptions & {
  variant?: "boxed" | "underline" | "plain"
  minLines?: number
  maxLines?: number
}

function Textarea({
  variant = "boxed",
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  label,
  labelOption = "appear",
  help,
  hasError,
  supportingText,
  lengthLimit,
  minLines,
  maxLines = 8,
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
  rows,
  style,
  id,
  ref,
  "aria-describedby": describedBy,
  "aria-invalid": ariaInvalid,
  ...props
}: TextareaProps) {
  const field = useTextField({ value, defaultValue, lengthLimit, state, hasError, help, supportingText, id, "aria-describedby": describedBy, "aria-invalid": ariaInvalid })
  const { inputId, inputRef, currentValue, invalid, descriptionIds } = field
  const minimumLines = Math.max(1, Math.trunc(minLines ?? rows ?? 4))
  const maximumLines = Math.max(minimumLines, Math.trunc(maxLines))
  const textarea = (
    <textarea
      ref={(node) => {
        inputRef.current = node
        if (typeof ref === "function") return ref(node)
        if (ref) ref.current = node
      }}
      id={inputId}
      value={value}
      defaultValue={defaultValue}
      required={required}
      disabled={disabled || state?.enabled === false}
      readOnly={readOnly || state?.readOnly}
      rows={rows ?? minimumLines}
      maxLength={lengthLimit && lengthLimit.mode !== "none" ? undefined : maxLength}
      aria-invalid={invalid ? true : ariaInvalid}
      aria-describedby={descriptionIds}
      onChange={(event) => {
        const nextValue = event.currentTarget.value
        if (!field.accept(nextValue)) {
          event.currentTarget.value = currentValue
          return
        }
        onChange?.(event)
        onValueChange?.(nextValue)
      }}
      onFocus={(event) => { field.setFocused(true); onFocus?.(event) }}
      onBlur={(event) => { field.setFocused(false); onBlur?.(event) }}
      style={{
        minHeight: `calc(${minimumLines}lh + 1rem + 2px)`,
        maxHeight: `calc(${maximumLines}lh + 1rem + 2px)`,
        ...style,
      }}
      data-slot="textarea"
      data-variant={variant}
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      {...props}
      className={cn(
        "border-input dark:bg-input/30 focus-visible:border-ring focus-visible:ring-ring/50 aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive dark:aria-invalid:border-destructive/50 disabled:bg-input/50 dark:disabled:bg-input/80 rounded-lg border bg-transparent px-2.5 py-2 text-base transition-colors focus-visible:ring-3 aria-invalid:ring-3 md:text-sm flex field-sizing-content resize-none overflow-y-auto leading-5 w-full outline-none placeholder:text-muted-foreground disabled:cursor-not-allowed disabled:opacity-(--ien-state-disabled-alpha)",
        variant === "underline"
          ? "rounded-none border-x-0 border-t-0 bg-transparent px-0 dark:bg-transparent"
          : variant === "plain"
            ? "border-transparent bg-transparent shadow-none focus-visible:border-transparent dark:bg-transparent"
            : "",
        !invalid && state?.status?.type === "success" && "border-(--ien-success) focus-visible:border-(--ien-success)",
        className,
      )}
    />
  )

  return (
    <TextFieldFrame field={field} label={label} labelOption={labelOption} required={required} disabled={disabled} state={state} slot="textarea">
      {textarea}
    </TextFieldFrame>
  )
}

export { Textarea }
