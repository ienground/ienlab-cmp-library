"use client"

import type { IenToneProps } from "@/lib/ien-theme"
import * as React from "react"
import { cn } from "cn"
import { Field, FieldDescription, FieldError, FieldLabel } from "@/components/ui/field"

export type TextareaLengthLimit =
  | { mode: "none" }
  | { mode: "block" | "error"; maxLength: number }

export interface TextareaState {
  enabled?: boolean
  readOnly?: boolean
  status?:
    | { type: "normal" }
    | { type: "error"; message: string }
    | { type: "success"; message?: string }
}

export type TextareaProps = React.ComponentProps<"textarea"> & IenToneProps & {
  variant?: "boxed" | "underline" | "plain"
  label?: string
  supportingText?: string
  lengthLimit?: TextareaLengthLimit
  minLines?: number
  maxLines?: number
  state?: TextareaState
  onValueChange?: (value: string) => void
}

function Textarea({
  variant = "boxed",
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  label,
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
  const generatedId = React.useId()
  const inputId = id ?? generatedId
  const inputRef = React.useRef<HTMLTextAreaElement | null>(null)
  const [uncontrolledValue, setUncontrolledValue] = React.useState(
    () => String(defaultValue ?? "")
  )
  const currentValue = value === undefined ? uncontrolledValue : String(value)
  const limit = lengthLimit && lengthLimit.mode !== "none"
    ? Math.max(0, Math.trunc(lengthLimit.maxLength))
    : undefined
  const exceeded = limit !== undefined && currentValue.length > limit
  const invalid = state?.status?.type === "error" ||
    (lengthLimit?.mode === "error" && exceeded) ||
    (ariaInvalid !== undefined && ariaInvalid !== false && ariaInvalid !== "false")
  const message = state?.status?.type === "error" || state?.status?.type === "success"
    ? state.status.message ?? supportingText
    : supportingText
  const minimumLines = Math.max(1, Math.trunc(minLines ?? rows ?? 4))
  const maximumLines = Math.max(minimumLines, Math.trunc(maxLines))
  const descriptionId = message ? `${inputId}-description` : undefined
  const counterId = limit !== undefined ? `${inputId}-counter` : undefined
  const descriptionIds = [describedBy, descriptionId, counterId].filter(Boolean).join(" ") || undefined

  React.useEffect(() => {
    const input = inputRef.current
    const form = input?.form
    if (!input || !form || value !== undefined) return
    const handleReset = (event: Event) => {
      queueMicrotask(() => {
        if (!event.defaultPrevented) setUncontrolledValue(input.value)
      })
    }
    form.addEventListener("reset", handleReset)
    return () => form.removeEventListener("reset", handleReset)
  }, [value])

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
      maxLength={lengthLimit?.mode === "error" ? undefined : lengthLimit?.mode === "block" ? limit : maxLength}
      aria-invalid={invalid ? true : ariaInvalid}
      aria-describedby={descriptionIds}
      onChange={(event) => {
        const nextValue = event.currentTarget.value
        if (lengthLimit?.mode === "block" && limit !== undefined &&
          nextValue.length > limit && nextValue.length >= currentValue.length) {
          event.currentTarget.value = currentValue
          return
        }
        setUncontrolledValue(nextValue)
        onChange?.(event)
        onValueChange?.(nextValue)
      }}
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

  if (!label && !message && limit === undefined) return textarea

  return (
    <Field data-invalid={invalid || undefined} data-disabled={disabled || state?.enabled === false || undefined} className="gap-1.5">
      {label && (
        <FieldLabel htmlFor={inputId}>
          {label}
          {required && <span aria-hidden="true" className="text-destructive">*</span>}
        </FieldLabel>
      )}
      {textarea}
      {(message || limit !== undefined) && (
        <div className="flex items-start justify-between gap-2">
          {message && (invalid
            ? <FieldError id={descriptionId}>{message}</FieldError>
            : <FieldDescription id={descriptionId} className={state?.status?.type === "success" ? "text-(--ien-success)" : undefined}>{message}</FieldDescription>)}
          {limit !== undefined && (
            <span id={counterId} data-slot="textarea-counter" className={cn("ms-auto shrink-0 text-xs tabular-nums text-muted-foreground", exceeded && "text-destructive")}>
              {currentValue.length}/{limit}
            </span>
          )}
        </div>
      )}
    </Field>
  )
}

export { Textarea }
