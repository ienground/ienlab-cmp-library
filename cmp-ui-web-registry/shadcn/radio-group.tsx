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
import { cn } from "@/lib/ien-utils";
import { RadioGroup as RadioGroupPrimitive } from "radix-ui";
import { buttonVariants } from "@/components/ui/button";

type RadioTone = "neutral" | "brand" | "success" | "warning" | "danger" | "info";
type RadioSize = number | string;
type RadioGroupContextValue = {
  enabled: boolean;
  size: RadioSize;
  tone: RadioTone;
};
const RadioGroupContext = React.createContext<RadioGroupContextValue>({
  enabled: true,
  size: 24,
  tone: "brand",
});

type RadioGroupProps = Omit<
  React.ComponentProps<typeof RadioGroupPrimitive.Root>,
  "disabled"
> & {
  enabled?: boolean;
  disabled?: boolean;
  size?: RadioSize;
  tone?: RadioTone;
};

function RadioGroup({
  className,
  enabled = true,
  disabled = false,
  size = 24,
  tone = "brand",
  children,
  ...props
}: RadioGroupProps) {
  const isEnabled = enabled && !disabled;
  return (
    <RadioGroupContext.Provider value={{ enabled: isEnabled, size, tone }}>
      <RadioGroupPrimitive.Root
        data-slot="radio-group"
        data-enabled={isEnabled}
        aria-disabled={!isEnabled}
        className={cn("grid w-full gap-ien-xxs", className)}
        {...props}
      >
        {children}
      </RadioGroupPrimitive.Root>
    </RadioGroupContext.Provider>
  );
}

type RadioGroupItemProps = Omit<
  React.ComponentProps<typeof RadioGroupPrimitive.Item>,
  "disabled"
> & {
  enabled?: boolean;
  disabled?: boolean;
  size?: RadioSize;
  tone?: RadioTone;
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

function RadioGroupItem({
  className,
  enabled,
  disabled = false,
  size,
  tone,
  onPointerDownCapture,
  onKeyDownCapture,
  style,
  ...props
}: RadioGroupItemProps) {
  const group = React.useContext(RadioGroupContext);
  const itemRef = React.useRef<HTMLButtonElement>(null);
  const isEnabled = (enabled ?? true) && group.enabled && !disabled;
  const resolvedSize = size ?? group.size;
  const resolvedTone = tone ?? group.tone;
  const sizeValue = typeof resolvedSize === "number" ? `${resolvedSize}px` : resolvedSize;
  const fill = buttonVariants({
    variant: "fill",
    tone: resolvedTone,
    size: "icon-xs",
    useGradient: true,
  });
  const toneVariables = buttonVariants({
    variant: "ghost",
    tone: resolvedTone,
    size: "icon-xs",
    useGradient: false,
  });

  return (
    <RadioGroupPrimitive.Item
      {...props}
      ref={itemRef}
      data-slot="radio-group-item"
      data-tone={resolvedTone}
      aria-disabled={!isEnabled}
      disabled={false}
      tabIndex={isEnabled ? props.tabIndex : -1}
      onPointerDownCapture={(event) => {
        onPointerDownCapture?.(event);
        if (!isEnabled && !event.defaultPrevented) {
          event.preventDefault();
          event.stopPropagation();
          shakeElement(itemRef.current);
        }
      }}
      onKeyDownCapture={(event) => {
        onKeyDownCapture?.(event);
        if (!isEnabled && !event.defaultPrevented) {
          event.preventDefault();
          event.stopPropagation();
          shakeElement(itemRef.current);
        }
      }}
      style={{ ...style, width: sizeValue, height: sizeValue }}
      className={cn(
        "group/radio-group-item peer relative flex shrink-0 items-center justify-center rounded-ien-full border-2 border-ien-border-strong bg-transparent outline-none transition-[scale,border-color] duration-[var(--ien-motion-fast)] focus-visible:border-ien-brand focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 data-[state=checked]:border-[var(--ien-button-tone)] active:scale-95 motion-reduce:transition-none",
        toneVariables,
        "border-ien-border-strong bg-transparent active:scale-95",
        !isEnabled && "cursor-not-allowed opacity-[var(--ien-state-disabled-alpha)]",
        className,
      )}
    >
      <span
        aria-hidden="true"
        className={cn(
          fill,
          "size-1/2 rounded-ien-full scale-0 opacity-0 transition-[scale,opacity] duration-200 ease-[var(--ien-motion-standard-easing)] group-data-[state=checked]/radio-group-item:scale-100 group-data-[state=checked]/radio-group-item:opacity-100",
        )}
      />
    </RadioGroupPrimitive.Item>
  );
}

export type { RadioGroupProps, RadioGroupItemProps };
export { RadioGroup, RadioGroupItem };
