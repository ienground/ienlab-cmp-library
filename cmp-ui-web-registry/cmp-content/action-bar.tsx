"use client";

import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type IenActionBarSide = "top" | "bottom";
export const IenActionBarDefaults = { maxWidth: 640 } as const;

export interface IenActionBarProps extends React.ComponentPropsWithRef<"div"> {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  side?: IenActionBarSide;
  align?: "start" | "center" | "end";
  sideOffset?: number | string;
  alignOffset?: number | string;
  contentPadding?: React.CSSProperties["padding"];
  containerColor?: string;
  contentColor?: string;
  maxWidth?: number | string;
  selection?: React.ReactNode;
  close?: React.ReactNode | false;
  children: React.ReactNode;
}

export function IenActionBar({
  open,
  onOpenChange,
  side = "bottom",
  align = "center",
  sideOffset = "var(--ien-spacing-md)",
  maxWidth = IenActionBarDefaults.maxWidth,
  alignOffset = 0,
  contentPadding,
  containerColor,
  contentColor,
  selection,
  close,
  children,
  className,
  style,
  ...props
}: IenActionBarProps) {
  const id = React.useId();

  React.useEffect(() => {
    if (!open) return;
    const onKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") onOpenChange(false);
    };
    window.addEventListener("keydown", onKeyDown);
    return () => window.removeEventListener("keydown", onKeyDown);
  }, [open, onOpenChange]);

  return (
    <div
      className="pointer-events-none absolute inset-0 z-20"
      aria-hidden={!open}
      inert={!open}
    >
      <div
        className={cn(
          "pointer-events-auto absolute left-1/2 flex w-[min(calc(100%-2rem),var(--ien-action-bar-max))] -translate-x-1/2 items-center gap-ien-sm rounded-ien-xl border border-ien-border bg-ien-surface-raised p-ien-xs text-ien-text-primary shadow-[0_var(--ien-elevation-floating)_calc(var(--ien-elevation-floating)*2)_rgb(0_0_0/0.12)] transition-[opacity,translate,visibility] duration-[var(--ien-motion-normal)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
          side === "top"
            ? "top-[var(--ien-action-bar-offset)]"
            : "bottom-[var(--ien-action-bar-offset)]",
          align === "start" && "left-4 translate-x-0",
          align === "end" && "left-auto right-4 translate-x-0",
          open
            ? "translate-y-0 opacity-100"
            : side === "top"
              ? "-translate-y-3 opacity-0"
              : "translate-y-3 opacity-0",
          !open && "invisible pointer-events-none",
          className,
        )}
        style={
          {
            "--ien-action-bar-max":
              typeof maxWidth === "number" ? `${maxWidth}px` : maxWidth,
            "--ien-action-bar-offset":
              typeof sideOffset === "number" ? `${sideOffset}px` : sideOffset,
            marginInlineStart: align === "end" ? undefined : alignOffset,
            marginInlineEnd: align === "end" ? (typeof alignOffset === "number" ? -alignOffset : `calc(-1 * ${alignOffset})`) : undefined,
            padding: contentPadding,
            backgroundColor: containerColor,
            color: contentColor,
            transitionDelay: open ? "0s" : "0s, 0s, var(--ien-motion-normal)",
            ...style,
          } as React.CSSProperties
        }
        role="toolbar"
        aria-label="선택 항목 작업"
        aria-hidden={!open}
        aria-describedby={selection ? id : undefined}
        {...props}
      >
        {selection != null && (
          <div
            id={id}
            className="shrink-0 border-r border-ien-border pr-3 text-ien-label1"
          >
            {selection}
          </div>
        )}
        <div className="flex min-w-0 flex-1 items-center gap-2 overflow-x-auto">
          {children}
        </div>
        {close !== false && (
          <button
            type="button"
            className="grid size-9 shrink-0 place-items-center rounded-ien-full text-ien-text-secondary transition-colors hover:bg-ien-surface-weak focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand"
            aria-label="닫기"
            onClick={() => onOpenChange(false)}
          >
            {close ?? <span aria-hidden>×</span>}
          </button>
        )}
      </div>
    </div>
  );
}

export { IenActionBar as ActionBar };
