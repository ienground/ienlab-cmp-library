"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { cn } from "@/lib/ien-utils";

export type NumericSpinnerSize = "tiny" | "small" | "medium" | "large";

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
  number?: number;
  defaultNumber?: number;
  onValueChange?: (value: number) => void;
  onNumberChange?: (value: number) => void;
  min?: number;
  max?: number;
  minNumber?: number;
  maxNumber?: number;
  step?: number;
  numberFormatter?: (value: number) => string;
  size?: NumericSpinnerSize;
  enabled?: boolean;
  disabled?: boolean;
  disable?: boolean;
  label?: string;
  decreaseAriaLabel?: string;
  increaseAriaLabel?: string;
  className?: string;
};

const sizeStyles: Record<
  NumericSpinnerSize,
  { height: string; minWidth: string; actionWidth: string; numberWidth: string; numberHeight: string; numberText: string; actionText: string }
> = {
  tiny: {
    height: "32px",
    minWidth: "92px",
    actionWidth: "30px",
    numberWidth: "40px",
    numberHeight: "28px",
    numberText: "text-ien-label2",
    actionText: "text-ien-label1",
  },
  small: {
    height: "36px",
    minWidth: "104px",
    actionWidth: "34px",
    numberWidth: "46px",
    numberHeight: "32px",
    numberText: "text-ien-label1",
    actionText: "text-ien-title3",
  },
  medium: {
    height: "44px",
    minWidth: "120px",
    actionWidth: "40px",
    numberWidth: "56px",
    numberHeight: "38px",
    numberText: "text-ien-title3",
    actionText: "text-ien-title3",
  },
  large: {
    height: "52px",
    minWidth: "140px",
    actionWidth: "48px",
    numberWidth: "68px",
    numberHeight: "46px",
    numberText: "text-ien-title2",
    actionText: "text-ien-title2",
  },
};

function shakeElement(element: HTMLElement | null) {
  element?.animate(
    [
      { translate: "0 0" },
      { translate: "-6px 0" },
      { translate: "6px 0" },
      { translate: "-3.6px 0" },
      { translate: "3.6px 0" },
      { translate: "0 0" },
    ],
    { duration: 200 },
  );
}

function NumericSpinner({
  ref,
  rootRef,
  inputProps,
  value,
  number,
  defaultValue,
  defaultNumber,
  onValueChange,
  onNumberChange,
  min,
  minNumber,
  max,
  maxNumber,
  step = 1,
  numberFormatter = (current) => String(current),
  size = "medium",
  enabled = true,
  disabled = false,
  disable = false,
  label = "숫자",
  decreaseAriaLabel = `${label} 감소`,
  increaseAriaLabel = `${label} 증가`,
  className,
  ...divProps
}: NumericSpinnerProps) {
  const lower = min ?? minNumber ?? 0;
  const upper = max ?? maxNumber ?? 999;
  const resolvedMin = Math.min(lower, upper);
  const resolvedMax = Math.max(lower, upper);
  const resolvedStep = Number.isFinite(step) && step > 0 ? step : 1;
  const clamp = (candidate: number) =>
    Math.min(resolvedMax, Math.max(resolvedMin, candidate));
  const requestedInitial =
    value ?? number ?? defaultNumber ?? defaultValue ?? resolvedMin;
  const initialValue = clamp(
    Number.isFinite(requestedInitial) ? requestedInitial : resolvedMin,
  );
  const [internalValue, setInternalValue] = React.useState(initialValue);
  const controlled = value !== undefined || number !== undefined;
  const requestedCurrent = value ?? number ?? internalValue;
  const current = clamp(
    Number.isFinite(requestedCurrent) ? requestedCurrent : resolvedMin,
  );
  const [inputValue, setInputValue] = React.useState(String(current));
  const [dragOffset, setDragOffset] = React.useState(0);
  const editing = React.useRef(false);
  const dragStart = React.useRef<number | null>(null);
  const inputElement = React.useRef<HTMLInputElement>(null);
  const rootElement = React.useRef<HTMLDivElement>(null);
  const isInteractive = enabled && !disabled && !disable;
  const canDecrease = isInteractive && current > resolvedMin;
  const canIncrease = isInteractive && current < resolvedMax;
  const dimensions = sizeStyles[size];

  React.useImperativeHandle(ref, () => inputElement.current!, []);
  React.useImperativeHandle(rootRef, () => rootElement.current!, []);

  React.useEffect(() => {
    if (!editing.current) setInputValue(String(current));
  }, [current]);

  const emitValue = (candidate: number) => {
    if (!Number.isFinite(candidate)) return current;
    const next = clamp(candidate);
    if (!controlled) setInternalValue(next);
    onValueChange?.(next);
    onNumberChange?.(next);
    return next;
  };

  const update = (candidate: number) => {
    if (!isInteractive) {
      shakeElement(rootElement.current);
      return;
    }
    setInputValue(String(emitValue(candidate)));
  };

  const commitDraft = () => {
    const parsed = Number(inputValue);
    if (inputValue.trim() !== "" && Number.isFinite(parsed)) {
      setInputValue(String(emitValue(parsed)));
    } else {
      setInputValue(String(current));
    }
    editing.current = false;
  };

  return (
    <div
      {...divProps}
      ref={(node) => {
        rootElement.current = node;
      }}
      data-slot="numeric-spinner"
      data-size={size}
      data-enabled={isInteractive}
      aria-disabled={!isInteractive || undefined}
      onPointerDownCapture={(event) => {
        divProps.onPointerDownCapture?.(event);
        if (!isInteractive && !event.defaultPrevented) {
          shakeElement(rootElement.current);
        }
      }}
      className={cn(
        "inline-flex h-[var(--ien-spinner-height)] min-w-[var(--ien-spinner-min-width)] items-center justify-around gap-ien-none rounded-ien-default bg-ien-surface-variant px-ien-xxs transition-[scale,background-color] duration-[var(--ien-motion-fast)] active:scale-[0.97] motion-reduce:transition-none",
        !isInteractive && "cursor-not-allowed",
        className,
      )}
      style={{
        "--ien-spinner-height": dimensions.height,
        "--ien-spinner-min-width": dimensions.minWidth,
        "--ien-spinner-action-width": dimensions.actionWidth,
        "--ien-spinner-number-width": dimensions.numberWidth,
        "--ien-spinner-number-height": dimensions.numberHeight,
        ...divProps.style,
      } as React.CSSProperties}
    >
      <Button
        type="button"
        variant="ghost"
        tone="neutral"
        size="icon-sm"
        aria-label={decreaseAriaLabel}
        aria-disabled={!canDecrease}
        tabIndex={canDecrease ? undefined : -1}
        className={cn(
          "h-full w-[var(--ien-spinner-action-width)] min-w-0 rounded-none px-0 text-ien-text-secondary",
          dimensions.actionText,
          !canDecrease && "text-ien-text-disabled",
        )}
        onClick={() => {
          if (canDecrease) update(current - resolvedStep);
          else if (isInteractive) shakeElement(rootElement.current);
        }}
      >
        −
      </Button>
      <div
        className="relative z-10 flex h-[var(--ien-spinner-number-height)] w-[var(--ien-spinner-number-width)] shrink-0 items-center justify-center overflow-hidden rounded-ien-default bg-ien-surface text-ien-text-primary shadow-[0_0_var(--ien-spinner-elevation)_rgb(0_0_0_/_0.16)] transition-transform duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)]"
        style={{
          "--ien-spinner-elevation": size === "tiny" ? "3px" : size === "small" ? "var(--ien-elevation-raised)" : size === "medium" ? "5px" : "6px",
          transform: `translateX(${dragOffset}px)`,
        } as React.CSSProperties}
        onPointerDown={(event) => {
          if (!isInteractive) return;
          dragStart.current = event.clientX;
          event.currentTarget.setPointerCapture(event.pointerId);
        }}
        onPointerMove={(event) => {
          if (dragStart.current === null) return;
          const limit = Number.parseFloat(dimensions.actionWidth);
          setDragOffset(Math.max(-limit, Math.min(limit, event.clientX - dragStart.current)));
        }}
        onPointerUp={(event) => {
          if (dragStart.current === null) return;
          const distance = event.clientX - dragStart.current;
          if (Math.abs(distance) >= 24) {
            const nextCanChange = distance < 0 ? canDecrease : canIncrease;
            if (nextCanChange) update(current + (distance < 0 ? -resolvedStep : resolvedStep));
            else if (isInteractive) shakeElement(rootElement.current);
          }
          dragStart.current = null;
          setDragOffset(0);
        }}
        onPointerCancel={() => {
          dragStart.current = null;
          setDragOffset(0);
        }}
      >
        <Input
          {...inputProps}
          ref={(node) => {
            inputElement.current = node;
          }}
          type="text"
          inputMode="decimal"
          role="spinbutton"
          aria-label={label}
          aria-valuenow={current}
          aria-valuemin={Number.isFinite(resolvedMin) ? resolvedMin : undefined}
          aria-valuemax={Number.isFinite(resolvedMax) ? resolvedMax : undefined}
          value={editing.current ? inputValue : numberFormatter(current)}
          disabled={!isInteractive}
          onFocus={() => {
            editing.current = true;
            setInputValue(String(current));
          }}
          onChange={(event) => {
            const next = event.currentTarget.value;
            setInputValue(next);
            if (next.trim() !== "" && Number.isFinite(Number(next))) {
              emitValue(Number(next));
            }
          }}
          onBlur={commitDraft}
          className={cn(
            "h-full w-full border-0 bg-transparent px-0 text-center shadow-none focus-visible:ring-0",
            dimensions.numberText,
            !isInteractive && "text-ien-text-disabled",
          )}
        />
      </div>
      <Button
        type="button"
        variant="ghost"
        tone="neutral"
        size="icon-sm"
        aria-label={increaseAriaLabel}
        aria-disabled={!canIncrease}
        tabIndex={canIncrease ? undefined : -1}
        className={cn(
          "h-full w-[var(--ien-spinner-action-width)] min-w-0 rounded-none px-0 text-ien-text-secondary",
          dimensions.actionText,
          !canIncrease && "text-ien-text-disabled",
        )}
        onClick={() => {
          if (canIncrease) update(current + resolvedStep);
          else if (isInteractive) shakeElement(rootElement.current);
        }}
      >
        +
      </Button>
    </div>
  );
}

export { NumericSpinner, NumericSpinner as IenNumericSpinner };
