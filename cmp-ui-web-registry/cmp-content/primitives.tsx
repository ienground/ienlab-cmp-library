"use client";

import * as React from "react";
import { cn } from "@/lib/ien-utils";

export interface IenSurfaceProps extends React.ComponentPropsWithRef<"div"> {
  color?: string;
  contentColor?: string;
  shape?: "none" | "small" | "default" | "large" | "full";
  borderColor?: string;
  borderWidth?: number | string;
  tonalElevation?: number;
  backgroundBrush?: string;
}

export function IenSurface({
  color = "var(--ien-surface)",
  contentColor = "var(--ien-text-primary)",
  shape = "default",
  borderColor,
  borderWidth = "var(--ien-stroke-thin)",
  tonalElevation = 0,
  backgroundBrush,
  className,
  style,
  ...props
}: IenSurfaceProps) {
  const radius =
    shape === "none"
      ? "0"
      : shape === "small"
        ? "var(--ien-radius-sm)"
        : shape === "large"
          ? "var(--ien-radius-lg)"
          : shape === "full"
            ? "var(--ien-radius-full)"
            : "var(--ien-radius-default)";
  return (
    <div
      className={cn("text-ien-body2", className)}
      style={{
        color: contentColor,
        background: backgroundBrush ?? color,
        border: borderColor ? `${borderWidth} solid ${borderColor}` : undefined,
        borderRadius: radius,
        boxShadow: tonalElevation
          ? `0 ${tonalElevation / 2}px ${tonalElevation}px rgb(0 0 0 / 12%)`
          : undefined,
        ...style,
      }}
      {...props}
    />
  );
}

export interface IenProvideTextStyleProps extends React.ComponentPropsWithRef<"div"> {
  color?: string;
  textClassName?: string;
  style?: React.CSSProperties;
}

export function IenProvideTextStyle({
  color = "currentColor",
  textClassName = "text-ien-body2",
  className,
  style,
  ...props
}: IenProvideTextStyleProps) {
  return (
    <div
      className={cn(textClassName, className)}
      style={{ color, ...style }}
      {...props}
    />
  );
}

export interface IenIconProps extends React.ComponentPropsWithRef<"span"> {
  icon: React.ReactNode;
  contentDescription?: string;
  size?: number | string;
  tint?: string;
}

export function IenIcon({
  icon,
  contentDescription,
  size = "var(--ien-icon-md)",
  tint = "currentColor",
  className,
  style,
  ...props
}: IenIconProps) {
  return (
    <span
      role={contentDescription ? "img" : undefined}
      aria-label={contentDescription}
      aria-hidden={contentDescription ? undefined : true}
      className={cn(
        "inline-grid shrink-0 place-items-center [&>svg]:size-full",
        className,
      )}
      style={{ width: size, height: size, color: tint, ...style }}
      {...props}
    >
      {icon}
    </span>
  );
}

export interface IenDividerProps extends React.ComponentPropsWithRef<"div"> {
  color?: string;
  thickness?: number | string;
  orientation?: "horizontal" | "vertical";
}

function Divider({
  color = "var(--ien-border)",
  thickness = "var(--ien-stroke-thin)",
  orientation = "horizontal",
  className,
  ...props
}: IenDividerProps) {
  const horizontal = orientation === "horizontal";
  return (
    <div
      role="separator"
      aria-orientation={orientation}
      className={cn(horizontal ? "w-full" : "h-full", className)}
      style={{
        height: horizontal ? thickness : "100%",
        width: horizontal ? "100%" : thickness,
        backgroundColor: color,
      }}
      {...props}
    />
  );
}

export const IenDivider = Object.assign(Divider, {
  Horizontal: (props: Omit<IenDividerProps, "orientation">) => (
    <Divider {...props} orientation="horizontal" />
  ),
  Vertical: (props: Omit<IenDividerProps, "orientation">) => (
    <Divider {...props} orientation="vertical" />
  ),
});

export interface IenBorderBoxProps extends React.ComponentPropsWithRef<"div"> {
  shape?: "none" | "small" | "default" | "large" | "full";
  color?: string;
  width?: number | string;
  padding?: number | string;
}

export function IenBorderBox({
  shape = "default",
  color = "var(--ien-border)",
  width = "var(--ien-stroke-thin)",
  padding = "var(--ien-spacing-md)",
  className,
  style,
  ...props
}: IenBorderBoxProps) {
  const radius =
    shape === "none"
      ? "var(--ien-radius-none)"
      : shape === "small"
        ? "var(--ien-radius-sm)"
        : shape === "large"
          ? "var(--ien-radius-lg)"
          : shape === "full"
            ? "var(--ien-radius-full)"
            : "var(--ien-radius-default)";
  return (
    <div
      className={cn("text-ien-text-primary", className)}
      style={{
        border: `${width} solid ${color}`,
        borderRadius: radius,
        padding,
        ...style,
      }}
      {...props}
    />
  );
}

export interface IenClickableProps extends Omit<
  React.ComponentPropsWithRef<"div">,
  "onClick"
> {
  onClick: () => void;
  enabled?: boolean;
  role?: "button" | "link" | "checkbox" | "radio" | "switch";
  checked?: boolean;
  pressed?: boolean;
}

export function IenClickable({
  onClick,
  enabled = true,
  role = "button",
  checked,
  pressed,
  className,
  onKeyDown,
  ...props
}: IenClickableProps) {
  const handleKeyDown = (event: React.KeyboardEvent<HTMLDivElement>) => {
    onKeyDown?.(event);
    if (!enabled || event.defaultPrevented) return;
    const activates =
      role === "link"
        ? event.key === "Enter"
        : event.key === "Enter" || event.key === " ";
    if (activates) {
      event.preventDefault();
      onClick();
    }
  };
  return (
    <div
      role={role}
      aria-checked={
        role === "checkbox" || role === "radio" || role === "switch"
          ? checked
          : undefined
      }
      aria-pressed={role === "button" ? pressed : undefined}
      aria-disabled={!enabled || undefined}
      tabIndex={enabled ? 0 : undefined}
      className={cn(
        "inline-flex min-h-[var(--ien-state-minimum-touch-target)] min-w-[var(--ien-state-minimum-touch-target)] items-center justify-center",
        enabled
          ? "cursor-pointer"
          : "cursor-default opacity-[var(--ien-state-disabled-alpha)]",
        className,
      )}
      onClick={enabled ? onClick : undefined}
      onKeyDown={handleKeyDown}
      {...props}
    />
  );
}

export interface IenLoaderPrimitiveProps extends React.ComponentPropsWithRef<"span"> {
  color?: string;
  strokeWidth?: number;
  size?: number | string;
  label?: string;
}

export function IenLoaderPrimitive({
  color = "currentColor",
  strokeWidth = 3,
  size = "var(--ien-icon-md)",
  label = "불러오는 중",
  className,
  style,
  ...props
}: IenLoaderPrimitiveProps) {
  return (
    <span
      aria-label={label}
      role="status"
      className={cn(
        "inline-block animate-spin rounded-full border-r-transparent motion-reduce:animate-none",
        className,
      )}
      style={{
        width: size,
        height: size,
        borderWidth: strokeWidth,
        borderColor: color,
        borderRightColor: "transparent",
        ...style,
      }}
      {...props}
    />
  );
}

export function IenAsteriskTextWrapper({
  children,
  className,
  ...props
}: React.ComponentPropsWithRef<"span">) {
  return (
    <span
      className={cn("inline-flex items-center gap-ien-xxs", className)}
      {...props}
    >
      {children}
      <span aria-label="필수" className="text-ien-danger">
        *
      </span>
    </span>
  );
}

export {
  IenSurface as Surface,
  IenProvideTextStyle as ProvideTextStyle,
  IenIcon as Icon,
  IenBorderBox as BorderBox,
  IenClickable as Clickable,
  IenLoaderPrimitive as LoaderPrimitive,
  IenAsteriskTextWrapper as AsteriskTextWrapper,
};
