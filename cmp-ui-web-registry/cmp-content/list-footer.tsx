"use client";

import * as React from "react";
import { ChevronRight } from "lucide-react";
import { cn } from "@/lib/ien-utils";

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
    indent?: number | string;
    className?: string;
  }) => (
    <div
      aria-hidden
      className={cn(
        "h-[var(--ien-stroke-hairline)] bg-ien-border/40",
        className,
      )}
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
  shape?: React.CSSProperties["borderRadius"];
}

export function IenListFooter({
  children,
  border = "full",
  textColor = "var(--ien-brand)",
  iconColor = "var(--ien-brand)",
  shape,
  icon,
  hairline,
  shadow,
  className,
  onPointerDown,
  onPointerUp,
  onPointerCancel,
  disabled,
  style,
  ...props
}: IenListFooterProps) {
  const [pressed, setPressed] = React.useState(false);
  const interactive = props.onClick != null && !disabled;
  const line = hairline ??
    (border === "none" ? null : (
      <IenListFooterDefaults.Hairline
        indent={border === "indented" ? "var(--ien-spacing-md)" : 0}
      />
    ));
  return (
    <div className="w-full">
      {line}
      <button
        type="button"
        disabled={disabled || !interactive}
        className={cn(
          "relative flex min-h-12 w-full items-center justify-center gap-ien-xxs overflow-hidden rounded-ien-none px-ien-md py-ien-sm text-ien-body2 transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand disabled:cursor-default disabled:opacity-70",
          interactive &&
            "hover:bg-ien-surface-weak active:bg-ien-surface-variant",
          className,
        )}
        style={{ ...style, color: textColor, borderRadius: shape }}
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
