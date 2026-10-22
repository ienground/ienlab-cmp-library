"use client";

import * as React from "react";
import { Input } from "@/components/ui/input";
import { type IenTextFieldState } from "@/components/ui/text-field";
import { cn } from "@/lib/ien-utils";

export type SplitTextFieldProps = Omit<React.ComponentProps<"div">, "children" | "ref" | "role" | "aria-label" | "className"> & {
  ref?: React.Ref<HTMLDivElement>;
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  length?: number;
  type?: "text" | "numeric";
  disabled?: boolean;
  readOnly?: boolean;
  state?: IenTextFieldState;
  placeholderChar?: string;
  mask?: boolean;
  label?: string;
  className?: string;
};
function SplitTextField({ value, defaultValue = "", onValueChange, length = 6, type = "numeric", disabled = false, readOnly = false, state, placeholderChar = "•", mask = false, label = "인증 코드", className, ref, ...rootProps }: SplitTextFieldProps) {
  const count = Math.max(1, Math.trunc(Number.isFinite(length) ? length : 6));
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const [focused, setFocused] = React.useState(false);
  const [selection, setSelection] = React.useState(0);
  const inputRef = React.useRef<HTMLInputElement>(null);
  const id = React.useId();
  const isDisabled = disabled || state?.enabled === false;
  const isReadOnly = readOnly || state?.readOnly === true;
  const current = (value ?? internalValue).slice(0, count);
  const error = state?.status?.type === "error" ? state.status.message : undefined;
  return <div {...rootProps} ref={ref} className={cn("grid gap-ien-xxs", className)}>
    <div className="relative flex gap-ien-xs">
      <Input ref={inputRef} type={mask ? "password" : "text"} inputMode={type === "numeric" ? "numeric" : "text"} autoComplete="one-time-code" value={current} maxLength={count} disabled={isDisabled} readOnly={isReadOnly} aria-label={label} aria-invalid={error !== undefined || undefined} aria-describedby={error !== undefined ? id : undefined} className="absolute inset-0 z-10 h-full w-full cursor-text opacity-0" onFocus={() => setFocused(true)} onBlur={() => setFocused(false)} onSelect={(event) => setSelection(event.currentTarget.selectionStart ?? current.length)} onChange={(event) => {
        if (isDisabled || isReadOnly) return;
        const next = (type === "numeric" ? event.currentTarget.value.replace(/\D/g, "") : event.currentTarget.value).slice(0, count);
        if (value === undefined) setInternalValue(next);
        onValueChange?.(next);
      }} onClick={(event) => {
        const box = event.currentTarget.getBoundingClientRect();
        const index = Math.min(current.length, Math.max(0, Math.floor((event.clientX - box.left) / (box.width / count))));
        event.currentTarget.setSelectionRange(index, index);
        setSelection(index);
      }} />
      {Array.from({ length: count }, (_, index) => <span aria-hidden="true" key={index} className={cn("flex h-13 min-w-0 flex-1 items-center justify-center rounded-ien-default border bg-ien-surface px-ien-sm text-ien-title3 transition-colors duration-[var(--ien-motion-fast)] motion-reduce:transition-none", error !== undefined ? "border-ien-danger" : current[index] || focused && index === selection ? "border-ien-brand" : "border-ien-border", current[index] && !isDisabled ? "text-ien-text-primary" : "text-ien-text-disabled", isDisabled && "bg-ien-surface-weak", focused && index === Math.min(selection, count - 1) && "ring-2 ring-ien-brand/20")}>{current[index] ? mask ? placeholderChar : current[index] : placeholderChar}</span>)}
    </div>
    {error !== undefined && <p id={id} className="text-ien-caption text-ien-danger">{error}</p>}
  </div>;
}
export { SplitTextField, SplitTextField as IenSplitTextField };
