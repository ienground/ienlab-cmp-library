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
import { ToggleGroup as ToggleGroupPrimitive } from "radix-ui";
import { buttonVariants, type IenContentPadding } from "@/components/ui/button";
import type { ToggleSize, ToggleVariant } from "@/components/ui/toggle";

type ToggleGroupTone = "neutral" | "brand" | "success" | "warning" | "danger" | "info";
type ToggleGroupStyle = {
  tone?: ToggleGroupTone;
  checkedVariant?: "fill" | "weak" | "line" | "ghost";
  uncheckedVariant?: "fill" | "weak" | "line" | "ghost";
  useGradient?: boolean;
  enabled?: boolean;
  spacing?: number;
  orientation?: "horizontal" | "vertical";
};

type ToggleGroupContextValue = ToggleGroupStyle & {
  variant: ToggleVariant;
  size: ToggleSize;
  enabled: boolean;
};
const ToggleGroupContext = React.createContext<ToggleGroupContextValue>({
  variant: "default",
  tone: "brand",
  size: "default",
  checkedVariant: "fill",
  uncheckedVariant: "weak",
  useGradient: true,
  enabled: true,
  spacing: 2,
  orientation: "horizontal",
});

type ToggleGroupProps = React.ComponentProps<
  typeof ToggleGroupPrimitive.Root
> &
  ToggleGroupStyle & {
    variant?: ToggleVariant;
    size?: ToggleSize;
  };

function ToggleGroup({
  className,
  variant = "default",
  size = "default",
  tone = "brand",
  checkedVariant = "fill",
  uncheckedVariant = "weak",
  useGradient = true,
  enabled = true,
  spacing = 2,
  orientation = "horizontal",
  children,
  ...props
}: ToggleGroupProps) {
  const isEnabled = enabled && props.disabled !== true;
  return (
    <ToggleGroupContext.Provider
      value={{
        variant,
        size,
        tone,
        checkedVariant,
        uncheckedVariant,
        useGradient,
        enabled: isEnabled,
        spacing,
        orientation,
      }}
    >
      <ToggleGroupPrimitive.Root
        {...props}
        data-slot="toggle-group"
        data-variant={variant}
        data-size={size}
        data-spacing={spacing}
        data-orientation={orientation}
        orientation={orientation}
        style={{ "--gap": spacing } as React.CSSProperties}
        aria-disabled={!isEnabled}
        className={cn(
          "group/toggle-group flex w-fit flex-row items-center gap-[--spacing(var(--gap))] rounded-[var(--ien-control-radius-base)] data-[size=sm]:rounded-[min(var(--ien-control-radius-md),10px)] data-[orientation=vertical]:flex-col data-[orientation=vertical]:items-stretch aria-disabled:opacity-[var(--ien-state-disabled-alpha)]",
          className,
        )}
      >
        {children}
      </ToggleGroupPrimitive.Root>
    </ToggleGroupContext.Provider>
  );
}

type ToggleGroupItemProps = React.ComponentProps<
  typeof ToggleGroupPrimitive.Item
> & {
  variant?: ToggleVariant;
  size?: ToggleSize;
  tone?: ToggleGroupTone;
  enabled?: boolean;
  loading?: boolean;
  checkedVariant?: "fill" | "weak" | "line" | "ghost";
  uncheckedVariant?: "fill" | "weak" | "line" | "ghost";
  useGradient?: boolean;
  contentPadding?: IenContentPadding;
  shape?: React.CSSProperties["borderRadius"];
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

function ToggleGroupItem({
  className,
  children,
  variant,
  size,
  tone,
  enabled = true,
  disabled = false,
  loading = false,
  checkedVariant,
  uncheckedVariant,
  useGradient,
  contentPadding,
  shape,
  onPointerDownCapture,
  onKeyDownCapture,
  style,
  ...props
}: ToggleGroupItemProps) {
  const group = React.useContext(ToggleGroupContext);
  const itemRef = React.useRef<HTMLButtonElement>(null);
  const isEnabled = enabled && group.enabled && !disabled && !loading;
  const resolvedTone = tone ?? group.tone ?? "brand";
  const resolvedVariant = variant ?? group.variant;
  const resolvedSize = size ?? group.size;
  const selectedVariant =
    checkedVariant ??
    group.checkedVariant ??
    (resolvedVariant === "fill" || resolvedVariant === "weak" || resolvedVariant === "line" || resolvedVariant === "ghost"
      ? resolvedVariant
      : resolvedVariant === "outline"
        ? "line"
        : "fill");
  const unselectedVariant =
    uncheckedVariant ??
    (variant === "fill" || variant === "weak" || variant === "line" || variant === "ghost"
      ? variant
      : group.uncheckedVariant ?? "weak");
  const gradient = useGradient ?? group.useGradient ?? true;
  const fill = buttonVariants({
    variant: selectedVariant,
    tone: resolvedTone,
    useGradient: gradient,
  });
  const weak = buttonVariants({
    variant: unselectedVariant,
    tone: resolvedTone,
    useGradient: gradient,
  });
  const sizeClass = {
    sm: "h-7 min-w-7 px-ien-xs",
    default: "h-8 min-w-8 px-ien-xs",
    lg: "h-9 min-w-9 px-ien-sm",
    small: "h-9 min-w-9 px-ien-sm",
    medium: "h-11 min-w-11 px-ien-md",
    large: "h-[52px] min-w-[52px] px-ien-lg",
  }[resolvedSize];

  return (
    <ToggleGroupPrimitive.Item
      {...props}
      ref={itemRef}
      data-slot="toggle-group-item"
      data-variant={resolvedVariant}
      data-size={resolvedSize}
      data-spacing={group.spacing}
      data-tone={resolvedTone}
      data-loading={loading}
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
      style={{
        ...style,
        ...(contentPadding !== undefined
          ? typeof contentPadding === "object"
            ? contentPadding
            : { padding: contentPadding }
          : {}),
        ...(shape !== undefined ? { borderRadius: shape } : {}),
      }}
      className={cn(
        "group/toggle-group-item relative inline-flex shrink-0 items-center justify-center gap-ien-xxs overflow-hidden whitespace-nowrap border border-transparent text-ien-body2 font-[var(--ien-font-weight-label1)] outline-none transition-[scale,border-radius] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] focus:z-10 focus-visible:z-10 focus-visible:border-ien-brand focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 data-[state=on]:text-[var(--ien-button-on-tone)] data-[state=off]:text-[var(--ien-button-tone)] active:scale-[var(--ien-button-pressed-scale)] aria-disabled:cursor-not-allowed aria-invalid:border-ien-danger motion-reduce:transition-none [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
        "group-data-[spacing=0]/toggle-group:rounded-ien-none group-data-[spacing=0]/toggle-group:px-ien-xs group-data-[orientation=horizontal]/toggle-group:data-[spacing=0]:first:rounded-l-[var(--ien-control-radius-base)] group-data-[orientation=vertical]/toggle-group:data-[spacing=0]:first:rounded-t-[var(--ien-control-radius-base)] group-data-[orientation=horizontal]/toggle-group:data-[spacing=0]:last:rounded-r-[var(--ien-control-radius-base)] group-data-[orientation=vertical]/toggle-group:data-[spacing=0]:last:rounded-b-[var(--ien-control-radius-base)] group-data-[orientation=horizontal]/toggle-group:data-[spacing=0]:data-[variant=outline]:border-l-0 group-data-[orientation=vertical]/toggle-group:data-[spacing=0]:data-[variant=outline]:border-t-0 group-data-[orientation=horizontal]/toggle-group:data-[spacing=0]:data-[variant=outline]:first:border-l group-data-[orientation=vertical]/toggle-group:data-[spacing=0]:data-[variant=outline]:first:border-t",
        fill,
        "data-[state=off]:border-[var(--ien-button-tone-weak)] data-[state=off]:bg-[var(--ien-button-tone-weak)] data-[state=off]:bg-[image:linear-gradient(to_bottom_right_in_srgb,color-mix(in_oklab,var(--ien-button-tone-weak),var(--ien-button-tone)_var(--ien-button-gradient-weak-start)),var(--ien-button-tone-weak),var(--ien-button-tone-weak))]",
        !gradient && "data-[state=off]:bg-none",
        resolvedVariant === "outline" &&
          "data-[state=on]:border-[var(--ien-button-tone)] data-[state=on]:bg-transparent data-[state=on]:text-[var(--ien-button-tone)]",
        resolvedVariant === "default" && "data-[state=off]:border-transparent",
        sizeClass,
        !isEnabled && "opacity-[var(--ien-state-disabled-alpha)]",
        className,
      )}
    >
      {loading ? (
        <span
          aria-hidden="true"
          className="size-[var(--ien-icon-sm)] animate-spin rounded-full border-2 border-current border-r-transparent"
        />
      ) : (
        <>
          <span
            aria-hidden="true"
            className={cn(
              weak,
              "pointer-events-none absolute inset-0 size-full rounded-[inherit] opacity-0 transition-opacity duration-[var(--ien-motion-fast)] group-data-[state=off]/toggle-group-item:opacity-100",
            )}
          />
          <span className="relative z-10 inline-flex items-center gap-ien-xxs">
            {children}
          </span>
        </>
      )}
    </ToggleGroupPrimitive.Item>
  );
}

export type { ToggleGroupProps, ToggleGroupItemProps };
export { ToggleGroup, ToggleGroupItem };
