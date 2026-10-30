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
import { cn } from "cn";
import { Switch as SwitchPrimitive } from "radix-ui";
type SwitchTone =
  "neutral" | "brand" | "success" | "warning" | "danger" | "info";
type SwitchProps = React.ComponentProps<typeof SwitchPrimitive.Root> & {
  size?: "sm" | "default";
  tone?: SwitchTone;
  thumbContent?: React.ReactNode;
  trackContent?: { on?: React.ReactNode; off?: React.ReactNode };
};
function Switch({
  className,
  size = "default",
  tone = "brand",
  onCheckedChange,
  thumbContent,
  trackContent,
  ...props
}: SwitchProps) {
  const toneClass = {
    neutral: "data-[state=checked]:bg-ien-surface-variant",
    brand: "data-[state=checked]:bg-ien-brand",
    success: "data-[state=checked]:bg-ien-success",
    warning: "data-[state=checked]:bg-ien-warning",
    danger: "data-[state=checked]:bg-ien-danger",
    info: "data-[state=checked]:bg-ien-info",
  }[tone];

  return (
    <SwitchPrimitive.Root
      data-slot="switch"
      data-size={size}
      data-tone={tone}
      onCheckedChange={onCheckedChange}
      className={cn(
        "peer group/switch relative inline-flex shrink-0 items-center rounded-ien-full border border-transparent transition-colors outline-none focus-visible:ring-2 focus-visible:ring-ien-brand/50 disabled:cursor-not-allowed disabled:opacity-[var(--ien-state-disabled-alpha)] data-[size=default]:h-[1.15rem] data-[size=default]:w-8 data-[size=sm]:h-3.5 data-[size=sm]:w-6 data-[state=unchecked]:bg-ien-surface-variant",
        toneClass,
        className,
      )}
      {...props}
    >
      <span
        aria-hidden="true"
        className="pointer-events-none absolute inset-0 flex items-center justify-between px-[3px] text-[9px] text-current"
      >
        {trackContent?.on}
        <span />
        {trackContent?.off}
      </span>
      <SwitchPrimitive.Thumb
        data-slot="switch-thumb"
        className="pointer-events-none relative z-10 block size-[var(--ien-icon-xs)] rounded-ien-full bg-ien-surface-raised shadow-sm transition-transform group-data-[size=default]/switch:data-[state=checked]:translate-x-[calc(100%+2px)] group-data-[size=sm]/switch:data-[state=checked]:translate-x-[calc(100%-1px)]"
      >
        {thumbContent}
      </SwitchPrimitive.Thumb>
    </SwitchPrimitive.Root>
  );
}

type IenSwitchProps = Omit<SwitchProps, "disabled" | "trackContent"> & {
  enabled?: boolean;
  trackContent?: React.ReactNode;
  onTrackContent?: React.ReactNode;
  offTrackContent?: React.ReactNode;
};

function IenSwitch({
  enabled = true,
  trackContent,
  onTrackContent,
  offTrackContent,
  className,
  ...props
}: IenSwitchProps) {
  const onContent = onTrackContent ?? trackContent;
  const offContent = offTrackContent ?? trackContent;

  return (
    <Switch
      {...props}
      disabled={!enabled}
      trackContent={{ on: onContent, off: offContent }}
      className={cn("data-[state=unchecked]:bg-ien-border-strong", className)}
    />
  );
}
export type { SwitchTone, SwitchProps, IenSwitchProps };
export { Switch, IenSwitch };
