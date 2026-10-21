"use client";

import * as React from "react";
import { Minus, Plus } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { cn } from "cn";

export type NumericSpinnerProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref"
> & {
  ref?: React.Ref<HTMLInputElement>;
  rootRef?: React.Ref<HTMLDivElement>;
  inputProps?: Omit<
    React.ComponentProps<"input">,
    | "type"
    | "value"
    | "defaultValue"
    | "onChange"
    | "onBlur"
    | "onFocus"
    | "disabled"
    | "min"
    | "max"
    | "step"
    | "aria-label"
    | "className"
    | "ref"
  >;
  value?: number;
  defaultValue?: number;
  onValueChange?: (value: number) => void;
  min?: number;
  max?: number;
  step?: number;
  disabled?: boolean;
  label?: string;
  className?: string;
};

function NumericSpinner({
  ref,
  rootRef,
  inputProps,
  value,
  defaultValue = 0,
  onValueChange,
  min = -Infinity,
  max = Infinity,
  step = 1,
  disabled = false,
  label = "숫자",
  className,
  ...divProps
}: NumericSpinnerProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const current = value ?? internalValue;
  const [inputValue, setInputValue] = React.useState(String(current));
  const editing = React.useRef(false);

  React.useEffect(() => {
    if (!editing.current) setInputValue(String(current));
  }, [current]);

  const update = (next: number) => {
    if (!Number.isFinite(next)) return;
    const bounded = Math.min(max, Math.max(min, next));
    if (value === undefined) {
      setInternalValue(bounded);
      setInputValue(String(bounded));
    } else {
      setInputValue(String(value));
    }
    onValueChange?.(bounded);
  };
  return (
    <div
      {...divProps}
      ref={rootRef}
      data-slot="numeric-spinner"
      className={cn(
        "inline-flex items-center gap-ien-xxs rounded-ien-default border border-ien-border bg-ien-surface p-ien-xxs",
        className,
      )}
    >
      <Button
        type="button"
        variant="ghost"
        tone="neutral"
        size="icon-sm"
        aria-label={`${label} 감소`}
        disabled={disabled || current <= min}
        onClick={() => update(current - step)}
      >
        <Minus aria-hidden="true" />
      </Button>
      <Input
        {...inputProps}
        type="number"
        ref={ref}
        aria-label={label}
        value={inputValue}
        min={Number.isFinite(min) ? min : undefined}
        max={Number.isFinite(max) ? max : undefined}
        step={step}
        disabled={disabled}
        onFocus={() => {
          editing.current = true;
        }}
        onChange={(event) => {
          const next = event.currentTarget.value;
          setInputValue(next);
          if (next !== "" && Number.isFinite(Number(next))) {
            const bounded = Math.min(max, Math.max(min, Number(next)));
            if (value === undefined) setInternalValue(bounded);
            onValueChange?.(bounded);
          }
        }}
        onBlur={() => {
          editing.current = false;
          const parsed = Number(inputValue);
          if (inputValue !== "" && Number.isFinite(parsed)) update(parsed);
          else setInputValue(String(current));
        }}
        className="h-9 w-20 border-0 bg-transparent text-center shadow-none focus-visible:ring-0"
      />
      <Button
        type="button"
        variant="ghost"
        tone="neutral"
        size="icon-sm"
        aria-label={`${label} 증가`}
        disabled={disabled || current >= max}
        onClick={() => update(current + step)}
      >
        <Plus aria-hidden="true" />
      </Button>
    </div>
  );
}

export { NumericSpinner, NumericSpinner as IenNumericSpinner };
