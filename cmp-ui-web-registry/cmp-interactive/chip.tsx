"use client";

import * as React from "react";
import { X } from "lucide-react";
import { cn } from "cn";

export type ChipProps = Omit<
  React.ComponentProps<"span">,
  "children" | "ref" | "className" | "onClick"
> & {
  ref?: React.Ref<HTMLSpanElement>;
  children: React.ReactNode;
  kind?: "assist" | "filter" | "input" | "suggestion";
  selected?: boolean;
  defaultSelected?: boolean;
  onSelectedChange?: (selected: boolean) => void;
  onClick?: () => void;
  onRemove?: () => void;
  disabled?: boolean;
  elevated?: boolean;
  className?: string;
};

function Chip({
  children,
  kind = "assist",
  selected,
  defaultSelected = false,
  onSelectedChange,
  onClick,
  onRemove,
  disabled = false,
  elevated = false,
  className,
  ref,
  ...rootProps
}: ChipProps) {
  const [internalSelected, setInternalSelected] =
    React.useState(defaultSelected);
  const active = selected ?? internalSelected;
  const toggle = () => {
    if (disabled) return;
    const next = !active;
    if (selected === undefined) setInternalSelected(next);
    onSelectedChange?.(next);
    onClick?.();
  };
  const actionable = kind === "assist" || kind === "suggestion";
  return (
    <span
      {...rootProps}
      ref={ref}
      className={cn(
        "inline-flex min-h-8 items-center gap-[var(--ien-control-gap)] rounded-ien-full border px-ien-sm text-ien-label2 transition-colors",
        active
          ? "border-ien-brand bg-ien-brand-weak text-ien-on-brand-weak"
          : "border-ien-border bg-ien-surface text-ien-text-primary",
        elevated &&
          "shadow-[0_var(--ien-elevation-raised)_calc(var(--ien-elevation-raised)*2)_rgb(0_0_0/0.12)]",
        disabled && "cursor-not-allowed opacity-50",
        className,
      )}
      data-kind={kind}
    >
      {kind === "filter" ? (
        <button
          type="button"
          aria-pressed={active}
          disabled={disabled}
          onClick={toggle}
          className="min-h-8 disabled:cursor-not-allowed"
        >
          {children}
        </button>
      ) : actionable ? (
        <button
          type="button"
          disabled={disabled}
          onClick={onClick}
          className="min-h-8 disabled:cursor-not-allowed"
        >
          {children}
        </button>
      ) : (
        <span>{children}</span>
      )}
      {kind === "input" && onRemove && (
        <button
          type="button"
          aria-label="항목 제거"
          disabled={disabled}
          onClick={onRemove}
          className="grid size-5 place-items-center rounded-ien-full hover:bg-black/10 focus-visible:outline-2 focus-visible:outline-ien-brand disabled:cursor-not-allowed"
        >
          <X aria-hidden="true" className="size-3.5" />
        </button>
      )}
    </span>
  );
}

export { Chip, Chip as IenChip };
