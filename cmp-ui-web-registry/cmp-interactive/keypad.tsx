"use client";

import * as React from "react";
import { Delete, RotateCcw } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "cn";

export type KeypadProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "className" | "onKeyDown"
> & {
  ref?: React.Ref<HTMLDivElement>;
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  onSubmit?: (value: string) => void;
  maxLength?: number;
  masked?: boolean;
  disabled?: boolean;
  className?: string;
  onKeyDown?: React.KeyboardEventHandler<HTMLDivElement>;
};

function Keypad({
  value,
  defaultValue = "",
  onValueChange,
  onSubmit,
  maxLength = 16,
  masked = true,
  disabled = false,
  className,
  ref,
  onKeyDown,
  tabIndex,
  ...rootProps
}: KeypadProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const current = value ?? internalValue;
  const update = (next: string) => {
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
  };
  const append = (digit: string) => {
    if (!disabled && current.length < maxLength) update(current + digit);
  };
  const keys = [
    "1",
    "2",
    "3",
    "4",
    "5",
    "6",
    "7",
    "8",
    "9",
    "",
    "0",
    "backspace",
  ];
  return (
    <div
      {...rootProps}
      ref={ref}
      data-slot="keypad"
      tabIndex={tabIndex ?? (disabled ? -1 : 0)}
      onKeyDown={(event) => {
        onKeyDown?.(event);
        if (event.defaultPrevented) return;
        if (disabled) return;
        if (/^[0-9]$/.test(event.key)) {
          event.preventDefault();
          append(event.key);
        } else if (event.key === "Backspace" && current) {
          event.preventDefault();
          update(current.slice(0, -1));
        }
      }}
      className={cn(
        "grid max-w-xs gap-ien-sm outline-none focus-visible:ring-2 focus-visible:ring-ien-brand",
        className,
      )}
    >
      <output
        aria-label="입력한 숫자"
        aria-live="polite"
        className="min-h-12 rounded-ien-default border border-ien-border bg-ien-surface px-ien-md py-ien-sm text-center font-mono text-ien-title2 tracking-[0.35em] text-ien-text-primary"
      >
        {masked ? "•".repeat(current.length) : current || " "}
      </output>
      <div
        className="grid grid-cols-3 gap-ien-xs"
        role="group"
        aria-label="숫자 키패드"
      >
        {keys.map((key, index) =>
          key === "" ? (
            <span key={`blank-${index}`} aria-hidden="true" />
          ) : key === "backspace" ? (
            <Button
              key={key}
              type="button"
              variant="weak"
              tone="neutral"
              disabled={disabled || !current}
              onClick={() => update(current.slice(0, -1))}
              aria-label="마지막 숫자 지우기"
              className="h-12"
            >
              <Delete aria-hidden="true" />
            </Button>
          ) : (
            <Button
              key={key}
              type="button"
              variant="weak"
              tone="neutral"
              disabled={disabled || current.length >= maxLength}
              onClick={() => append(key)}
              className="h-12 text-ien-title3"
            >
              {key}
            </Button>
          ),
        )}
      </div>
      <div className="flex gap-ien-xs">
        <Button
          type="button"
          variant="line"
          tone="neutral"
          disabled={disabled || !current}
          onClick={() => update("")}
          className="flex-1"
        >
          <RotateCcw aria-hidden="true" /> 지우기
        </Button>
        {onSubmit && (
          <Button
            type="button"
            disabled={disabled || !current}
            onClick={() => onSubmit(current)}
            className="flex-1"
          >
            확인
          </Button>
        )}
      </div>
    </div>
  );
}

export { Keypad, Keypad as IenKeypad };
