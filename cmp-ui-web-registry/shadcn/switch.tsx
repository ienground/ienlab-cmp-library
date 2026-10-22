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
import { Switch as SwitchPrimitive } from "radix-ui";

type SwitchTone = "neutral" | "brand" | "success" | "warning" | "danger" | "info";
type SwitchSize = "sm" | "default";
type SwitchTrackContent = {
  on?: React.ReactNode;
  off?: React.ReactNode;
};
type SwitchProps = Omit<
  React.ComponentProps<typeof SwitchPrimitive.Root>,
  "checked" | "defaultChecked" | "onCheckedChange" | "disabled"
> & {
  checked?: boolean;
  defaultChecked?: boolean;
  onCheckedChange?: (checked: boolean) => void;
  enabled?: boolean;
  disabled?: boolean;
  size?: SwitchSize;
  tone?: SwitchTone;
  thumbContent?: React.ReactNode;
  trackContent?: SwitchTrackContent;
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

function Switch({
  className,
  size = "default",
  tone = "brand",
  checked,
  defaultChecked,
  onCheckedChange,
  enabled = true,
  disabled = false,
  thumbContent,
  trackContent,
  onPointerDownCapture,
  onKeyDownCapture,
  style,
  ...props
}: SwitchProps) {
  const switchRef = React.useRef<HTMLButtonElement>(null);
  const [internalChecked, setInternalChecked] = React.useState(
    defaultChecked ?? false,
  );
  const isControlled = checked !== undefined;
  const isChecked = checked ?? internalChecked;
  const isInteractive = enabled && !disabled;
  const onContent = trackContent?.on;
  const offContent = trackContent?.off;
  const large = size === "default";

  const handleCheckedChange = (next: boolean) => {
    if (!isInteractive) {
      shakeElement(switchRef.current);
      return;
    }
    if (!isControlled) setInternalChecked(next);
    onCheckedChange?.(next);
  };

  return (
    <SwitchPrimitive.Root
      {...props}
      ref={switchRef}
      data-slot="switch"
      data-size={size}
      data-tone={tone}
      data-enabled={isInteractive}
      checked={isChecked}
      aria-checked={isChecked}
      aria-disabled={!isInteractive}
      disabled={false}
      tabIndex={isInteractive ? props.tabIndex : -1}
      onCheckedChange={handleCheckedChange}
      onPointerDownCapture={(event) => {
        onPointerDownCapture?.(event);
        if (!isInteractive && !event.defaultPrevented) {
          event.preventDefault();
          event.stopPropagation();
          shakeElement(switchRef.current);
        }
      }}
      onKeyDownCapture={(event) => {
        onKeyDownCapture?.(event);
        if (!isInteractive && !event.defaultPrevented) {
          event.preventDefault();
          event.stopPropagation();
          shakeElement(switchRef.current);
        }
      }}
      style={{ "--ien-switch-tone": `var(--ien-${tone})`, ...style } as React.CSSProperties}
      className={cn(
        "group/switch relative inline-flex shrink-0 items-center rounded-ien-full border-0 bg-transparent p-0 outline-none focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 aria-disabled:cursor-not-allowed",
        "data-[size=default]:h-8 data-[size=default]:w-[52px] data-[size=sm]:h-6 data-[size=sm]:w-10",
        !isInteractive && "cursor-not-allowed",
        className,
      )}
    >
      <span
        aria-hidden="true"
        className="pointer-events-none absolute inset-0 rounded-[inherit] bg-ien-border-strong transition-[background-color,opacity] duration-[var(--ien-motion-fast)] group-data-[state=checked]/switch:bg-[var(--ien-switch-tone)] group-data-[enabled=false]/switch:opacity-[var(--ien-state-disabled-alpha)] motion-reduce:transition-none"
      />
      {(onContent != null || offContent != null) && (
        <span
          aria-hidden="true"
          className={cn(
            "pointer-events-none absolute left-0 top-1/2 h-4 w-[52px] -translate-y-1/2 overflow-hidden text-[9px] leading-none text-current",
            !large && "h-3 w-10",
          )}
        >
          <span
            className="absolute inset-y-0 left-0 w-[52px] transition-transform duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none"
            style={{
              width: large ? 52 : 40,
              transform: `translateX(${isChecked ? 0 : large ? -20 : -15}px)`,
            }}
          >
            {onContent != null && (
              <span className="absolute inset-y-0 left-1 flex w-4 items-center justify-center">
                {onContent}
              </span>
            )}
            {offContent != null && (
              <span
                className="absolute inset-y-0 flex w-4 items-center justify-center"
                style={{ left: large ? 52 : 40 }}
              >
                {offContent}
              </span>
            )}
          </span>
        </span>
      )}
      <SwitchPrimitive.Thumb
        data-slot="switch-thumb"
        className={cn(
          "pointer-events-none absolute left-1 top-1/2 block -translate-y-1/2 rounded-ien-full bg-ien-surface shadow-sm transition-[width,transform] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
          large
            ? isChecked
              ? "h-6 w-6 translate-x-5"
              : "size-4 translate-x-0"
            : isChecked
              ? "h-[18px] w-[18px] translate-x-[15px]"
              : "size-3 translate-x-0",
        )}
      >
        {thumbContent}
      </SwitchPrimitive.Thumb>
    </SwitchPrimitive.Root>
  );
}

type IenSwitchProps = Omit<SwitchProps, "trackContent"> & {
  trackContent?: React.ReactNode;
  onTrackContent?: React.ReactNode;
  offTrackContent?: React.ReactNode;
};

function IenSwitch({
  trackContent,
  onTrackContent,
  offTrackContent,
  ...props
}: IenSwitchProps) {
  return (
    <Switch
      {...props}
      trackContent={{
        on: onTrackContent ?? trackContent,
        off: offTrackContent ?? trackContent,
      }}
    />
  );
}

export type { SwitchTone, SwitchProps, IenSwitchProps };
export { Switch, IenSwitch };
