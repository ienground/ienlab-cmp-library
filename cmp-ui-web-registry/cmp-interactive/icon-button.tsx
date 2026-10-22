"use client";

import * as React from "react";
import { Button, type ButtonProps } from "@/components/ui/button";
import { IenToggleButton, type IenToggleButtonProps } from "@/components/ui/toggle";
import { cn } from "@/lib/ien-utils";

type IconButtonSize = "small" | "medium" | "regular" | "large";
const sizes = {
  small: "size-9 p-0 [&_svg]:size-[18px] [--ien-button-loading-size:18px]",
  medium: "size-11 p-0 [&_svg]:size-6 [--ien-button-loading-size:24px]",
  regular: "size-11 p-0 [&_svg]:size-6 [--ien-button-loading-size:24px]",
  large: "size-13 p-0 [&_svg]:size-7 [--ien-button-loading-size:28px]",
};
export type IconToggleButtonProps = Omit<IenToggleButtonProps, "size"> & {
  label?: string;
  contentDescription?: string;
  size?: IconButtonSize;
};
function IconToggleButton({ label, contentDescription, size = "large", className, ...props }: IconToggleButtonProps) {
  return <IenToggleButton {...props} size={size === "regular" ? "medium" : size} aria-label={label ?? contentDescription ?? props["aria-label"]} contentPadding={0} className={cn("active:scale-95", sizes[size], className)} />;
}
export type IconButtonProps = Omit<ButtonProps, "size"> & {
  label?: string;
  contentDescription?: string;
  pressed?: boolean;
  defaultPressed?: boolean;
  onPressedChange?: (pressed: boolean) => void;
  checked?: boolean;
  defaultChecked?: boolean;
  onCheckedChange?: (checked: boolean) => void;
  shapes?: IenToggleButtonProps["shapes"];
  variants?: IenToggleButtonProps["variants"];
  toggleColors?: IenToggleButtonProps["colors"];
  size?: IconButtonSize;
};
function IconButton({ label, contentDescription, size = "large", pressed, defaultPressed, onPressedChange, checked, defaultChecked, onCheckedChange, shapes, variants, toggleColors, className, ...props }: IconButtonProps) {
  if (checked !== undefined || defaultChecked !== undefined || onCheckedChange || pressed !== undefined || defaultPressed !== undefined || onPressedChange) {
    const { colors: _colors, asChild: _asChild, content, tone, variant, useGradient, ...toggleProps } = props;
    return <IconToggleButton {...toggleProps} tone={tone ?? undefined} variant={variant ?? undefined} useGradient={useGradient ?? undefined} label={label ?? contentDescription} size={size} checked={checked ?? pressed} defaultChecked={defaultChecked ?? defaultPressed} onCheckedChange={onCheckedChange ?? onPressedChange} shapes={shapes} variants={variants} colors={toggleColors} className={className}>{content ?? props.children}</IconToggleButton>;
  }
  return <Button {...props} type={props.type ?? "button"} size={size === "regular" ? "medium" : size} aria-label={label ?? contentDescription ?? props["aria-label"]} contentPadding={0} className={cn("active:scale-95", sizes[size], className)} />;
}
export { IconButton, IconButton as IenIconButton, IconToggleButton, IconToggleButton as IenIconToggleButton };
