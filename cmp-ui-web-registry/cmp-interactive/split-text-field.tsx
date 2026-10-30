"use client";

import * as React from "react";
import { Input } from "@/components/ui/input";
import { cn } from "cn";
import { normalizeSplitTextFieldChange } from "./split-text-field-logic";

export type SplitTextFieldProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "role" | "aria-label" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  length?: number;
  type?: "text" | "numeric";
  disabled?: boolean;
  label?: string;
  className?: string;
};

function SplitTextField({
  value,
  defaultValue = "",
  onValueChange,
  length = 6,
  type = "numeric",
  disabled = false,
  label = "인증 코드",
  className,
  ref,
  ...rootProps
}: SplitTextFieldProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const current = (value ?? internalValue).slice(0, length);
  const refs = React.useRef<Array<HTMLInputElement | null>>([]);
  const update = (next: string, focusIndex?: number) => {
    const filtered = type === "numeric" ? next.replace(/\D/g, "") : next;
    const bounded = filtered.slice(0, length);
    if (value === undefined) setInternalValue(bounded);
    onValueChange?.(bounded);
    if (focusIndex !== undefined)
      refs.current[Math.max(0, Math.min(focusIndex, length - 1))]?.focus();
  };
  return (
    <div
      {...rootProps}
      ref={ref}
      role="group"
      aria-label={label}
      className={cn("flex gap-ien-xs", className)}
    >
      {Array.from({ length }, (_, index) => (
        <Input
          key={index}
          ref={(node) => {
            refs.current[index] = node;
          }}
          type="text"
          inputMode={type === "numeric" ? "numeric" : "text"}
          autoComplete={index === 0 ? "one-time-code" : "off"}
          value={current[index] ?? ""}
          disabled={disabled}
          aria-label={`${label}, ${index + 1}번째 문자`}
          onChange={(event) => {
            const change = normalizeSplitTextFieldChange({
              current,
              input: event.currentTarget.value,
              index,
              length,
              type,
            });
            update(change.value, change.focusIndex);
          }}
          onKeyDown={(event) => {
            if (event.key === "Backspace" && !current[index] && index > 0) {
              event.preventDefault();
              update(
                current.slice(0, index - 1) + current.slice(index),
                index - 1,
              );
            }
            if (event.key === "ArrowLeft" && index > 0) {
              event.preventDefault();
              refs.current[index - 1]?.focus();
            }
            if (event.key === "ArrowRight" && index < length - 1) {
              event.preventDefault();
              refs.current[index + 1]?.focus();
            }
          }}
          onPaste={(event) => {
            event.preventDefault();
            const pasted = event.clipboardData.getData("text");
            const change = normalizeSplitTextFieldChange({
              current,
              input: pasted,
              index,
              length,
              type,
              replaceWholeValue: true,
            });
            update(change.value, change.focusIndex);
          }}
          className="size-[var(--ien-state-minimum-touch-target)] rounded-ien-default text-center font-mono text-ien-title2"
        />
      ))}
    </div>
  );
}

export { SplitTextField, SplitTextField as IenSplitTextField };
