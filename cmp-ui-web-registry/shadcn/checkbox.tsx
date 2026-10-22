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
import { cn } from "@/lib/ien-utils";
import { Checkbox as CheckboxPrimitive } from "radix-ui";
import { buttonVariants } from "@/components/ui/button";

type CheckboxShape = "square" | "circle" | "dot" | "line";
type CheckboxTone = "neutral" | "brand" | "success" | "warning" | "danger" | "info";
type CheckboxProps = React.ComponentProps<typeof CheckboxPrimitive.Root> & {
  shape?: CheckboxShape;
  tone?: CheckboxTone;
};

function Checkbox({
  className,
  shape = "square",
  tone = "brand",
  style,
  ...props
}: CheckboxProps) {
  const fill = buttonVariants({
    variant: "fill",
    tone,
    size: "icon-xs",
    useGradient: true,
  });
  return (
    <CheckboxPrimitive.Root
      data-slot="checkbox"
      data-shape={shape}
      data-tone={tone}
      className={cn(
        "peer relative flex size-[var(--ien-icon-sm)] shrink-0 items-center justify-center overflow-hidden border border-ien-border-strong bg-ien-surface transition-[scale,border-color] duration-[var(--ien-motion-fast)] outline-none focus-visible:border-ien-brand focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 disabled:cursor-not-allowed disabled:opacity-[var(--ien-state-disabled-alpha)] aria-invalid:border-ien-danger data-[state=checked]:border-[var(--ien-checkbox-tone)] active:scale-95 motion-reduce:transition-none",
        shape === "circle" || shape === "dot"
          ? "rounded-ien-full"
          : shape === "line"
            ? "rounded-none border-0 bg-transparent text-ien-border-strong transition-colors duration-200 data-[state=checked]:border-0 data-[state=checked]:bg-transparent data-[state=checked]:text-[var(--ien-checkbox-tone)]"
            : "rounded-ien-xs",
        className,
      )}
      style={{
        "--ien-checkbox-tone": `var(--ien-${tone})`,
        ...style,
      } as React.CSSProperties}
      {...props}
    >
      {shape === "line" ? (
        <CheckIcon aria-hidden="true" className="size-full" />
      ) : (
        <CheckboxPrimitive.Indicator
          data-slot="checkbox-indicator"
          className={cn(
            fill,
            "absolute inset-0 flex size-full items-center justify-center rounded-[inherit]",
          )}
        >
          {shape === "dot" ? null : props.checked === "indeterminate" ||
            props.defaultChecked === "indeterminate" ? (
            <MinusIcon aria-hidden="true" className="relative size-3.5" />
          ) : (
            <CheckIcon
              aria-hidden="true"
              className={cn("relative size-3.5", shape === "circle" && "size-[65%]")}
            />
          )}
        </CheckboxPrimitive.Indicator>
      )}
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
  style,
  "aria-label": ariaLabel,
  ...props
}: IenCheckboxProps & {
  shape: Exclude<CheckboxShape, "square">;
  defaultSize: number;
}) {
  const generatedId = React.useId();
  const controlId = id ?? generatedId;
  const labelId = `${controlId}-label`;
  const wrapperRef = React.useRef<HTMLLabelElement>(null);
  const [internalChecked, setInternalChecked] = React.useState(
    defaultChecked ?? false,
  );
  const isControlled = checked !== undefined;
  const isChecked = checked ?? internalChecked;
  const isInteractive = enabled && !disabled;
  const size = requestedSize ?? style?.width ?? defaultSize;
  const sizeValue = typeof size === "number" ? `${size}px` : size;

  const handleCheckedChange = (next: boolean | "indeterminate") => {
    if (!isInteractive) {
      shakeElement(wrapperRef.current);
      return;
    }
    const nextChecked = next === true;
    if (!isControlled) setInternalChecked(nextChecked);
    onCheckedChange?.(nextChecked);
  };

  return (
    <label
      ref={wrapperRef}
      htmlFor={controlId}
      className={cn(
        "inline-flex min-h-[var(--ien-state-minimum-touch-target)] min-w-[var(--ien-state-minimum-touch-target)] items-center gap-ien-xs",
        !isInteractive && "cursor-not-allowed",
        wrapperClassName,
      )}
    >
      <Checkbox
        {...props}
        id={controlId}
        shape={shape}
        checked={isChecked}
        aria-checked={isChecked}
        aria-disabled={!isInteractive}
        tabIndex={isInteractive ? props.tabIndex : -1}
        aria-labelledby={label ? labelId : undefined}
        aria-label={label ? undefined : ariaLabel}
        onCheckedChange={handleCheckedChange}
        className={cn(
          shape === "circle" && "rounded-ien-full border-2",
          shape === "dot" && "rounded-ien-full border-2",
          (shape === "circle" || shape === "dot") && "bg-transparent",
          shape === "line" && "rounded-none border-0 bg-transparent",
          !isInteractive && "opacity-[var(--ien-state-disabled-alpha)]",
          className,
        )}
        style={{ ...style, width: sizeValue, height: sizeValue }}
      />
      {label && (
        <span
          id={labelId}
          className={cn(
            "text-ien-body2",
            !isInteractive && "text-ien-text-disabled",
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
