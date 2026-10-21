"use client";

import * as React from "react";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { cn } from "cn";

export type RadioButtonOption = {
  value: string;
  label: React.ReactNode;
  description?: React.ReactNode;
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
  disabled?: boolean;
};

function RadioButton({
  options,
  value,
  defaultValue,
  onValueChange,
  name,
  orientation = "vertical",
  className,
  disabled = false,
  ref,
  ...groupProps
}: RadioButtonProps) {
  const groupId = React.useId().replaceAll(":", "");
  return (
    <RadioGroup
      {...groupProps}
      ref={ref}
      value={value}
      defaultValue={defaultValue}
      onValueChange={onValueChange}
      name={name}
      disabled={disabled}
      className={cn(
        orientation === "horizontal"
          ? "flex flex-wrap gap-ien-sm"
          : "grid gap-ien-sm",
        className,
      )}
    >
      {options.map((option) => {
        const id = groupId + "-" + option.value;
        return (
          <label
            key={option.value}
            htmlFor={id}
            className={cn(
              "flex min-h-[var(--ien-state-minimum-touch-target)] cursor-pointer items-start gap-ien-sm rounded-ien-default border border-ien-border bg-ien-surface p-ien-md text-ien-text-primary transition-colors duration-[var(--ien-motion-fast)] has-[:checked]:border-ien-brand has-[:checked]:bg-ien-brand-weak has-[:disabled]:cursor-not-allowed has-[:disabled]:opacity-50",
              option.disabled && "cursor-not-allowed opacity-50",
            )}
          >
            <RadioGroupItem
              id={id}
              value={option.value}
              disabled={option.disabled}
              className="mt-ien-xxxs"
            />
            <span className="grid gap-[var(--ien-spacing-xxxs)] text-ien-body2">
              <span>{option.label}</span>
              {option.description && (
                <span className="text-ien-caption text-ien-text-secondary">
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
