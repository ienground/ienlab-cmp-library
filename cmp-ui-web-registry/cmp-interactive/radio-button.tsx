"use client";

import * as React from "react";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { cn } from "@/lib/ien-utils";

export type RadioButtonOption = {
  value: string;
  label: React.ReactNode;
  description?: React.ReactNode;
  enabled?: boolean;
  disabled?: boolean;
};

export type RadioButtonProps = Omit<
  React.ComponentProps<typeof RadioGroup>,
  | "children"
  | "ref"
  | "value"
  | "defaultValue"
  | "onValueChange"
  | "name"
  | "orientation"
  | "disabled"
  | "enabled"
  | "size"
  | "tone"
  | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  options: RadioButtonOption[];
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  name?: string;
  orientation?: "horizontal" | "vertical";
  className?: string;
  enabled?: boolean;
  disabled?: boolean;
  size?: number | string;
  tone?: "neutral" | "brand" | "success" | "warning" | "danger" | "info";
};

function RadioButton({
  options,
  value,
  defaultValue,
  onValueChange,
  name,
  orientation = "vertical",
  className,
  enabled = true,
  disabled = false,
  size = 24,
  tone = "brand",
  ref,
  ...groupProps
}: RadioButtonProps) {
  const groupId = React.useId().replaceAll(":", "");
  const isEnabled = enabled && !disabled;
  return (
    <RadioGroup
      {...groupProps}
      ref={ref}
      value={value}
      defaultValue={defaultValue}
      onValueChange={onValueChange}
      name={name}
      orientation={orientation}
      enabled={isEnabled}
      size={size}
      tone={tone}
      className={cn(
        orientation === "horizontal"
          ? "flex flex-wrap gap-ien-xxs"
          : "grid gap-ien-xxs",
        className,
      )}
    >
      {options.map((option) => {
        const id = `${groupId}-${option.value}`;
        const optionEnabled = option.enabled !== false && option.disabled !== true;
        const itemEnabled = isEnabled && optionEnabled;
        return (
          <label
            key={option.value}
            htmlFor={id}
            className={cn(
              "flex min-h-[var(--ien-state-minimum-touch-target)] w-full cursor-pointer items-center gap-ien-xs text-ien-body2 text-ien-text-primary",
              !itemEnabled && "cursor-not-allowed",
            )}
          >
            <RadioGroupItem
              id={id}
              value={option.value}
              enabled={optionEnabled}
            />
            <span
              className={cn(
                "grid gap-[var(--ien-spacing-xxxs)]",
                !itemEnabled && "text-ien-text-disabled",
              )}
            >
              <span>{option.label}</span>
              {option.description && (
                <span
                  className={cn(
                    "text-ien-caption text-ien-text-secondary",
                    !itemEnabled && "text-ien-text-disabled",
                  )}
                >
                  {option.description}
                </span>
              )}
            </span>
          </label>
        );
      })}
    </RadioGroup>
  );
}

export { RadioButton, RadioButton as IenRadioButton };
