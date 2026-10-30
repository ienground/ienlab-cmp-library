"use client";

/*
 * MIT License
 *
 * Copyright (c) 2023 shadcn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import * as React from "react";
import { CheckIcon, MinusIcon } from "lucide-react";
import { cn } from "cn";
import { Checkbox as CheckboxPrimitive } from "radix-ui";

type CheckboxProps = React.ComponentProps<typeof CheckboxPrimitive.Root> & {
  shape?: "square" | "circle" | "dot" | "line";
  tone?: "neutral" | "brand" | "success" | "warning" | "danger" | "info";
};
function Checkbox({
  className,
  shape = "square",
  tone = "brand",
  ...props
}: CheckboxProps) {
  const toneClasses = {
    neutral:
      "data-[state=checked]:bg-ien-surface-variant data-[state=checked]:text-ien-text-primary",
    brand:
      "data-[state=checked]:bg-ien-brand data-[state=checked]:text-ien-on-brand",
    success:
      "data-[state=checked]:bg-ien-success data-[state=checked]:text-ien-on-success",
    warning:
      "data-[state=checked]:bg-ien-warning data-[state=checked]:text-ien-on-warning",
    danger:
      "data-[state=checked]:bg-ien-danger data-[state=checked]:text-ien-on-danger",
    info: "data-[state=checked]:bg-ien-info data-[state=checked]:text-ien-on-info",
  }[tone];
  const rounded =
    shape === "circle" || shape === "dot"
      ? "rounded-ien-full"
      : shape === "line"
        ? "rounded-none border-0 bg-transparent data-[state=checked]:bg-transparent data-[state=checked]:text-ien-brand"
        : "rounded-ien-xs";
  return (
    <CheckboxPrimitive.Root
      data-slot="checkbox"
      data-shape={shape}
      data-tone={tone}
      className={cn(
        "peer relative flex size-[var(--ien-icon-sm)] shrink-0 items-center justify-center border border-ien-border bg-ien-surface transition-colors outline-none focus-visible:border-ien-brand focus-visible:ring-2 focus-visible:ring-ien-brand/50 disabled:cursor-not-allowed disabled:opacity-[var(--ien-state-disabled-alpha)] aria-invalid:border-ien-danger data-[state=checked]:border-transparent",
        rounded,
        toneClasses,
        shape === "dot" && "data-[state=checked]:text-ien-brand",
        className,
      )}
      {...props}
    >
      <CheckboxPrimitive.Indicator
        data-slot="checkbox-indicator"
        className="grid place-content-center text-current [&>svg]:size-3.5"
      >
        {shape === "dot" ? (
          <span
            aria-hidden="true"
            className="size-full rounded-ien-full bg-current"
          />
        ) : props.checked === "indeterminate" ||
          props.defaultChecked === "indeterminate" ? (
          <MinusIcon aria-hidden="true" />
        ) : (
          <CheckIcon aria-hidden="true" />
        )}
      </CheckboxPrimitive.Indicator>
    </CheckboxPrimitive.Root>
  );
}
type IenCheckboxProps = Omit<
  CheckboxProps,
  "shape" | "checked" | "defaultChecked" | "onCheckedChange" | "disabled"
> & {
  checked?: boolean;
  defaultChecked?: boolean;
  onCheckedChange?: (checked: boolean) => void;
  enabled?: boolean;
  disabled?: boolean;
  size?: number | string;
  label?: string;
  wrapperClassName?: string;
};

function IenCheckboxVariant({
  shape,
  defaultSize,
  size: requestedSize,
  label,
  enabled = true,
  disabled = false,
  checked,
  defaultChecked,
  onCheckedChange,
  className,
  wrapperClassName,
  id,
  "aria-label": ariaLabel,
  ...props
}: IenCheckboxProps & {
  shape: "circle" | "dot" | "line";
  defaultSize: number;
}) {
  const generatedId = React.useId();
  const controlId = id ?? generatedId;
  const size = requestedSize ?? props.style?.width ?? defaultSize;
  const sizeValue = typeof size === "number" ? `${size}px` : size;
  return (
    <label
      htmlFor={controlId}
      className={cn(
        "inline-flex min-h-[var(--ien-state-minimum-touch-target)] min-w-[var(--ien-state-minimum-touch-target)] items-center gap-ien-xs",
        !enabled || disabled ? "cursor-not-allowed" : "cursor-pointer",
        wrapperClassName,
      )}
    >
      <Checkbox
        {...props}
        id={controlId}
        shape={shape}
        checked={checked}
        defaultChecked={defaultChecked}
        disabled={!enabled || disabled}
        aria-label={label ? undefined : ariaLabel}
        onCheckedChange={(next) => onCheckedChange?.(next === true)}
        className={cn(
          shape === "circle" &&
            "rounded-ien-full border-2 data-[state=checked]:bg-ien-brand data-[state=checked]:text-ien-on-brand",
          shape === "dot" &&
            "rounded-ien-full border-2 data-[state=checked]:bg-ien-brand data-[state=checked]:text-ien-brand",
          shape === "line" &&
            "rounded-ien-none border-0 bg-transparent text-ien-text-secondary data-[state=checked]:bg-transparent data-[state=checked]:text-ien-brand",
          enabled &&
            !disabled &&
            "active:scale-[var(--ien-button-pressed-scale)]",
          (!enabled || disabled) && "opacity-[var(--ien-state-disabled-alpha)]",
          className,
        )}
        style={{ ...props.style, width: sizeValue, height: sizeValue }}
      />
      {label && (
        <span
          className={cn(
            "text-ien-body2",
            (!enabled || disabled) && "text-ien-text-disabled",
          )}
        >
          {label}
        </span>
      )}
    </label>
  );
}

function IenCircleCheckbox(props: IenCheckboxProps) {
  return <IenCheckboxVariant {...props} shape="circle" defaultSize={24} />;
}
function IenDotCheckbox(props: IenCheckboxProps) {
  return <IenCheckboxVariant {...props} shape="dot" defaultSize={8} />;
}
function IenLineCheckbox(props: IenCheckboxProps) {
  return <IenCheckboxVariant {...props} shape="line" defaultSize={24} />;
}
export type { CheckboxProps, IenCheckboxProps };
export {
  Checkbox,
  Checkbox as IenCheckbox,
  IenCircleCheckbox,
  IenDotCheckbox,
  IenLineCheckbox,
};
