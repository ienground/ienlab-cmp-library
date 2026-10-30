"use client";

import * as React from "react";
import { ChevronRight } from "lucide-react";
import { cn } from "cn";

export type IenListFooterBorder = "full" | "indented" | "none";

export const IenListFooterDefaults = {
  Text: ({
    text,
    className,
    ...props
  }: React.ComponentPropsWithRef<"span"> & { text: string }) => (
    <span
      className={cn("text-ien-body2 font-medium text-ien-brand", className)}
      {...props}
    >
      {text}
    </span>
  ),
  Hairline: ({
    indent = 0,
    className,
  }: {
    indent?: number;
    className?: string;
  }) => (
    <div
      aria-hidden
      className={cn("h-px bg-ien-border/50", className)}
      style={{ marginLeft: indent }}
    />
  ),
  Shadow: ({ className }: { className?: string }) => (
    <span
      aria-hidden
      className={cn(
        "absolute inset-0 rounded-[inherit] bg-ien-brand/5",
        className,
      )}
    />
  ),
};

export interface IenListFooterProps extends Omit<
  React.ComponentPropsWithRef<"button">,
  "children"
> {
  children: React.ReactNode;
  border?: IenListFooterBorder;
  textColor?: string;
  iconColor?: string;
  icon?: React.ReactNode;
  hairline?: React.ReactNode;
  shadow?: React.ReactNode;
  asChild?: boolean;
}

export function IenListFooter({
  children,
  border = "full",
  textColor,
  iconColor,
  icon,
  hairline,
  shadow,
  className,
  onPointerDown,
  onPointerUp,
  onPointerCancel,
  disabled,
  ...props
}: IenListFooterProps) {
  const [pressed, setPressed] = React.useState(false);
  const interactive = props.onClick != null && !disabled;
  const line =
    hairline ??
    (border === "none" ? null : (
      <IenListFooterDefaults.Hairline indent={border === "indented" ? 16 : 0} />
    ));
  return (
    <div className="w-full">
      {line}
      <button
        type="button"
        disabled={disabled || !interactive}
        className={cn(
          "relative flex min-h-12 w-full items-center justify-center gap-1 overflow-hidden rounded-ien-none px-ien-md py-ien-sm text-ien-body2 transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand disabled:cursor-default disabled:opacity-70",
          interactive &&
            "hover:bg-ien-surface-weak active:bg-ien-surface-variant",
          className,
        )}
        style={{ color: textColor }}
        onPointerDown={(event) => {
          setPressed(true);
          onPointerDown?.(event);
        }}
        onPointerUp={(event) => {
          setPressed(false);
          onPointerUp?.(event);
        }}
        onPointerCancel={(event) => {
          setPressed(false);
          onPointerCancel?.(event);
        }}
        onBlur={() => setPressed(false)}
        {...props}
      >
        {pressed && interactive && (shadow ?? <IenListFooterDefaults.Shadow />)}
        <span className="relative z-[1]">{children}</span>
        {icon != null && (
          <span className="relative z-[1]" style={{ color: iconColor }}>
            {icon}
          </span>
        )}
      </button>
    </div>
  );
}

export { IenListFooter as ListFooter };
