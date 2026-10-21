"use client";

import * as React from "react";
import { Button, type ButtonProps } from "@/components/ui/button";
import { cn } from "cn";

export type IconButtonProps = Omit<ButtonProps, "size"> & {
  label: string;
  pressed?: boolean;
  defaultPressed?: boolean;
  onPressedChange?: (pressed: boolean) => void;
  size?: "small" | "regular" | "large";
};

function IconButton({
  label,
  pressed,
  defaultPressed,
  onPressedChange,
  size = "regular",
  className,
  onClick,
  children,
  ...props
}: IconButtonProps) {
  const [internalPressed, setInternalPressed] = React.useState(
    defaultPressed ?? false,
  );
  const toggle = (event: React.MouseEvent<HTMLButtonElement>) => {
    onClick?.(event);
    if (event.defaultPrevented || (props.disabled ?? false)) return;
    const next = !(pressed ?? internalPressed);
    if (pressed === undefined) setInternalPressed(next);
    onPressedChange?.(next);
  };
  return (
    <Button
      {...props}
      type={props.type ?? "button"}
      size={
        size === "small" ? "icon-sm" : size === "large" ? "icon-lg" : "icon"
      }
      aria-label={label}
      aria-pressed={
        pressed === undefined &&
        defaultPressed === undefined &&
        !onPressedChange
          ? undefined
          : (pressed ?? internalPressed)
      }
      onClick={toggle}
      className={cn("rounded-ien-full", className)}
    >
      {children}
    </Button>
  );
}

export { IconButton, IconButton as IenIconButton };
