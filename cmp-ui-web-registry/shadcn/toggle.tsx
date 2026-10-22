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
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/ien-utils";
import { Toggle as TogglePrimitive } from "radix-ui";
import { buttonVariants, type IenContentPadding } from "@/components/ui/button";

type IenVariant = "fill" | "weak" | "line" | "ghost";
export type ToggleVariant = "default" | "outline" | IenVariant;
export type ToggleSize = "sm" | "default" | "lg" | "small" | "medium" | "large";
export type SemanticTone = "neutral" | "brand" | "success" | "warning" | "danger" | "info";

const toggleVariants = cva(
  "group/toggle inline-flex items-center justify-center gap-ien-xxs rounded-[var(--ien-control-radius-base)] text-ien-body2 font-[var(--ien-font-weight-label1)] whitespace-nowrap transition-[scale,border-radius] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] outline-none hover:bg-ien-surface-variant hover:text-ien-text-primary focus-visible:border-ien-brand focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 disabled:pointer-events-none disabled:opacity-[var(--ien-state-disabled-alpha)] aria-disabled:cursor-not-allowed aria-disabled:opacity-[var(--ien-state-disabled-alpha)] aria-invalid:border-ien-danger aria-pressed:bg-ien-surface-variant data-[state=on]:bg-ien-surface-variant [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4 motion-reduce:transition-none",
  {
    variants: {
      variant: {
        default: "bg-transparent",
        outline:
          "border border-ien-border-strong bg-transparent hover:bg-ien-surface-variant",
        fill: "",
        weak: "",
        line: "",
        ghost: "",
      },
      size: {
        default: "h-8 min-w-8 px-ien-xs",
        sm: "h-7 min-w-7 rounded-[min(var(--ien-control-radius-md),12px)] px-ien-xs text-[0.8rem] [&_svg:not([class*='size-'])]:size-3.5",
        lg: "h-9 min-w-9 px-ien-sm",
        small: "h-9 min-w-9 px-ien-sm",
        medium: "h-11 min-w-11 px-ien-md",
        large: "h-[52px] min-w-[52px] px-ien-lg",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  },
);

export type ToggleShapes = {
  checked?: React.CSSProperties["borderRadius"];
  unchecked?: React.CSSProperties["borderRadius"];
};
export type ToggleVariants = {
  checked?: IenVariant;
  unchecked?: IenVariant;
};
export type ToggleColors = {
  checkedContainer?: React.CSSProperties["backgroundColor"];
  checkedContent?: React.CSSProperties["color"];
  checkedBorder?: React.CSSProperties["borderColor"];
  checkedBackgroundBrush?: React.CSSProperties["backgroundImage"] | null;
  uncheckedContainer?: React.CSSProperties["backgroundColor"];
  uncheckedContent?: React.CSSProperties["color"];
  uncheckedBorder?: React.CSSProperties["borderColor"];
  uncheckedBackgroundBrush?: React.CSSProperties["backgroundImage"] | null;
  disabledContainer?: React.CSSProperties["backgroundColor"];
  disabledContent?: React.CSSProperties["color"];
  disabledBorder?: React.CSSProperties["borderColor"];
};
export type ToggleState = {
  enabled?: boolean;
  loading?: boolean;
};

type ToggleProps = Omit<
  React.ComponentProps<typeof TogglePrimitive.Root>,
  "pressed" | "defaultPressed" | "onPressedChange" | "disabled"
> &
  Partial<VariantProps<typeof toggleVariants>> & {
    pressed?: boolean;
    defaultPressed?: boolean;
    onPressedChange?: (pressed: boolean) => void;
    checked?: boolean;
    defaultChecked?: boolean;
    onCheckedChange?: (checked: boolean) => void;
    enabled?: boolean;
    disabled?: boolean;
    loading?: boolean;
    state?: ToggleState;
    tone?: SemanticTone;
    checkedTone?: SemanticTone;
    uncheckedTone?: SemanticTone;
    checkedVariant?: IenVariant;
    uncheckedVariant?: IenVariant;
    variants?: ToggleVariants;
    useGradient?: boolean;
    shapes?: ToggleShapes;
    colors?: Partial<ToggleColors>;
    contentPadding?: IenContentPadding;
    display?: "inline" | "block" | "full";
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

function Toggle({
  className,
  variant = "default",
  size = "default",
  tone = "brand",
  checkedTone,
  uncheckedTone,
  checkedVariant,
  uncheckedVariant,
  variants,
  useGradient = true,
  shapes,
  colors,
  contentPadding,
  display = "inline",
  pressed,
  defaultPressed,
  onPressedChange,
  checked,
  defaultChecked,
  onCheckedChange,
  enabled,
  disabled = false,
  loading,
  state,
  children,
  onPointerDownCapture,
  onKeyDownCapture,
  style,
  ...props
}: ToggleProps) {
  const toggleRef = React.useRef<HTMLButtonElement>(null);
  const [internalPressed, setInternalPressed] = React.useState(
    checked ?? pressed ?? defaultChecked ?? defaultPressed ?? false,
  );
  const isControlled =
    checked !== undefined || pressed !== undefined;
  const isPressed = checked ?? pressed ?? internalPressed;
  const isEnabled = (enabled ?? true) && state?.enabled !== false && !disabled;
  const isLoading = loading === true || state?.loading === true;
  const canInteract = isEnabled && !isLoading;
  const isSemanticSize = size === "small" || size === "medium" || size === "large";
  const isIenVariant =
    variant === "fill" ||
    variant === "weak" ||
    variant === "line" ||
    variant === "ghost" ||
    variants !== undefined ||
    checkedVariant !== undefined ||
    uncheckedVariant !== undefined ||
    checkedTone !== undefined ||
    uncheckedTone !== undefined ||
    tone !== "brand" ||
    state !== undefined ||
    enabled !== undefined ||
    isSemanticSize ||
    disabled ||
    loading !== undefined ||
    shapes !== undefined ||
    colors !== undefined ||
    contentPadding !== undefined ||
    display !== "inline" ||
    useGradient !== true;
  const explicitIenVariant: IenVariant | undefined =
    variant === "outline"
      ? "line"
      : variant === "fill" || variant === "weak" || variant === "line" || variant === "ghost"
        ? variant
        : undefined;
  const resolvedVariant = isPressed
    ? variants?.checked ?? checkedVariant ?? (isIenVariant ? explicitIenVariant ?? "fill" : "fill")
    : variants?.unchecked ?? uncheckedVariant ?? (isIenVariant ? explicitIenVariant ?? "weak" : "weak");
  const resolvedTone = isPressed
    ? checkedTone ?? tone
    : uncheckedTone ?? tone;
  const legacyVariant = variant === "outline" ? "outline" : "default";
  const legacySize = size === "sm" || size === "lg" ? size : "default";
  const classVariants = isIenVariant
    ? buttonVariants({
        variant: resolvedVariant,
        tone: resolvedTone,
        useGradient,
      })
    : toggleVariants({ variant: legacyVariant, size: legacySize });
  const radius = display === "full" ? 0 : isPressed ? shapes?.checked : shapes?.unchecked;
  const colorState = isPressed ? "checked" : "unchecked";
  const customStyle = {
    ...style,
    ...(isIenVariant && isSemanticSize
      ? {
          fontSize:
            size === "small"
              ? "var(--ien-font-size-label2)"
              : size === "medium"
                ? "var(--ien-font-size-label1)"
                : "var(--ien-font-size-body1)",
          lineHeight:
            size === "small"
              ? "var(--ien-line-height-label2)"
              : size === "medium"
                ? "var(--ien-line-height-label1)"
                : "var(--ien-line-height-body1)",
        }
      : {}),
    ...(contentPadding !== undefined
      ? typeof contentPadding === "object"
        ? contentPadding
        : { padding: contentPadding }
      : {}),
    ...(radius !== undefined ? { borderRadius: radius } : {}),
    ...(!isEnabled && (isIenVariant || colors)
      ? {
          backgroundColor: colors?.disabledContainer ?? "var(--ien-surface-weak)",
          color: colors?.disabledContent ?? "var(--ien-text-disabled)",
          borderColor:
            colors?.disabledBorder ??
            "color-mix(in srgb, var(--ien-border) 48%, transparent)",
          backgroundImage: "none",
        }
      : {
          ...(colors?.[`${colorState}Container`] !== undefined
            ? { backgroundColor: colors[`${colorState}Container`] }
            : {}),
          ...(colors?.[`${colorState}Content`] !== undefined
            ? { color: colors[`${colorState}Content`] }
            : {}),
          ...(colors?.[`${colorState}Border`] !== undefined
            ? { borderColor: colors[`${colorState}Border`] }
            : {}),
          ...(colors?.[`${colorState}BackgroundBrush`] !== undefined
            ? {
                backgroundImage:
                  colors[`${colorState}BackgroundBrush`] ?? "none",
              }
            : {}),
        }),
  } satisfies React.CSSProperties;

  const handlePressedChange = (next: boolean) => {
    if (!canInteract) {
      shakeElement(toggleRef.current);
      return;
    }
    if (!isControlled) setInternalPressed(next);
    onPressedChange?.(next);
    onCheckedChange?.(next);
  };

  return (
    <TogglePrimitive.Root
      {...props}
      ref={toggleRef}
      data-slot="toggle"
      data-variant={resolvedVariant}
      data-tone={resolvedTone}
      data-size={size}
      data-enabled={isEnabled}
      data-loading={isLoading}
      aria-busy={isLoading || undefined}
      pressed={isPressed}
      aria-pressed={isPressed}
      aria-disabled={!canInteract}
      disabled={false}
      tabIndex={canInteract ? props.tabIndex : -1}
      onPressedChange={handlePressedChange}
      onPointerDownCapture={(event) => {
        onPointerDownCapture?.(event);
        if (!canInteract && !event.defaultPrevented) {
          event.preventDefault();
          event.stopPropagation();
          shakeElement(toggleRef.current);
        }
      }}
      onKeyDownCapture={(event) => {
        onKeyDownCapture?.(event);
        if (!canInteract && !event.defaultPrevented) {
          event.preventDefault();
          event.stopPropagation();
          shakeElement(toggleRef.current);
        }
      }}
      style={customStyle}
      className={cn(
        "group/toggle inline-flex items-center justify-center gap-ien-xxs text-ien-body2 font-[var(--ien-font-weight-label1)] whitespace-nowrap outline-none transition-[scale,border-radius,background-color,color,border-color] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] focus-visible:border-ien-brand focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 aria-invalid:border-ien-danger aria-disabled:cursor-not-allowed active:scale-[var(--ien-button-pressed-scale)] motion-reduce:transition-none [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
        display === "block" && "flex w-full",
        display === "full" && "flex w-full rounded-none",
        !isEnabled && isIenVariant && "data-[enabled=false]:bg-none",
        classVariants,
        isIenVariant &&
          (size === "small"
            ? "h-9 min-w-9 px-ien-sm"
            : size === "medium"
              ? "h-11 min-w-11 px-ien-md"
              : size === "large"
                ? "h-[52px] min-w-[52px] px-ien-lg"
                : size === "sm"
                  ? "h-7 min-w-7 px-ien-xs"
                  : size === "lg"
                    ? "h-9 min-w-9 px-ien-sm"
                    : "h-8 min-w-8 px-ien-xs"),
        className,
      )}
    >
      <span className="inline-grid min-w-0 place-items-center">
        <span
          className="col-start-1 row-start-1 inline-flex items-center justify-center gap-[inherit] transition-[opacity,scale] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none"
          style={{ opacity: isLoading ? 0 : 1, scale: isLoading ? 0.8 : 1 }}
          aria-hidden={isLoading || undefined}
          inert={isLoading || undefined}
        >
          {children}
        </span>
        <span
          role="status"
          aria-label={isLoading ? "불러오는 중" : undefined}
          aria-hidden={!isLoading}
          className="pointer-events-none col-start-1 row-start-1 inline-flex transition-[opacity,scale] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none"
          style={{ opacity: isLoading ? 1 : 0, scale: isLoading ? 1 : 0.8 }}
        >
          <span className="size-[var(--ien-icon-md)] animate-spin rounded-full border-2 border-current border-r-transparent motion-reduce:animate-none" />
        </span>
      </span>
    </TogglePrimitive.Root>
  );
}

type IenToggleButtonProps = ToggleProps;

function IenToggleButton({
  checkedVariant = "fill",
  uncheckedVariant = "weak",
  variants,
  size = "large",
  tone = "brand",
  ...props
}: IenToggleButtonProps) {
  return (
    <Toggle
      {...props}
      checkedVariant={checkedVariant}
      uncheckedVariant={uncheckedVariant}
      variants={variants}
      size={size}
      tone={tone}
    />
  );
}

export type {
  ToggleProps,
  IenToggleButtonProps,
};
export { Toggle, IenToggleButton, toggleVariants };
