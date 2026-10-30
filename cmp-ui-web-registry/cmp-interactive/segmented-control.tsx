"use client";

import * as React from "react";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";
import { cn } from "cn";

export type SegmentedControlOption = {
  value: string;
  label: React.ReactNode;
  disabled?: boolean;
};
export type SegmentedControlProps = Omit<
  React.ComponentProps<typeof ToggleGroup>,
  | "children"
  | "ref"
  | "type"
  | "value"
  | "defaultValue"
  | "onValueChange"
  | "disabled"
  | "className"
  | "size"
> & {
  ref?: React.Ref<HTMLDivElement>;
  options: SegmentedControlOption[];
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  size?: "small" | "medium" | "large";
  fullWidth?: boolean;
  disabled?: boolean;
  className?: string;
};

function SegmentedControl({
  options,
  value,
  defaultValue,
  onValueChange,
  size = "medium",
  fullWidth = false,
  disabled = false,
  className,
  ref,
  ...groupProps
}: SegmentedControlProps) {
  return (
    <ToggleGroup
      {...groupProps}
      ref={ref}
      type="single"
      value={value}
      defaultValue={defaultValue}
      onValueChange={(next) => {
        if (next) onValueChange?.(next);
      }}
      disabled={disabled}
      className={cn(
        "min-h-[var(--ien-state-minimum-touch-target)] gap-ien-xxs rounded-ien-full border border-ien-border bg-ien-surface-weak p-ien-xxs",
        fullWidth && "w-full [&>*]:flex-1",
        className,
      )}
    >
      {options.map((option) => (
        <ToggleGroupItem
          key={option.value}
          value={option.value}
          disabled={option.disabled}
          className={cn(
            "min-h-9 rounded-ien-full px-ien-md text-ien-label2 text-ien-text-secondary transition-colors duration-[var(--ien-motion-fast)] data-[state=on]:bg-ien-surface data-[state=on]:text-ien-brand data-[state=on]:shadow-[0_var(--ien-elevation-raised)_calc(var(--ien-elevation-raised)*2)_rgb(0_0_0/0.12)]",
            size === "small" && "min-h-8 px-ien-sm",
            size === "large" &&
              "min-h-[var(--ien-state-minimum-touch-target)] px-ien-lg",
          )}
        >
          {option.label}
        </ToggleGroupItem>
      ))}
    </ToggleGroup>
  );
}

export { SegmentedControl, SegmentedControl as IenSegmentedControl };
