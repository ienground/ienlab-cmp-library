"use client";

import * as React from "react";
import { cn } from "cn";
import { Button } from "@/components/ui/button";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet";

export type BottomSheetProps = Omit<
  React.ComponentProps<typeof SheetContent>,
  | "children"
  | "side"
  | "showCloseButton"
  | "onEscapeKeyDown"
  | "onInteractOutside"
  | "ref"
  | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open?: boolean;
  defaultOpen?: boolean;
  onOpenChange?: (open: boolean) => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  children: React.ReactNode;
  className?: string;
  dismissible?: boolean;
};

function BottomSheet({
  open,
  defaultOpen,
  onOpenChange,
  title,
  description,
  children,
  className,
  dismissible = true,
  ref,
  ...contentProps
}: BottomSheetProps) {
  return (
    <Sheet open={open} defaultOpen={defaultOpen} onOpenChange={onOpenChange}>
      <SheetContent
        {...contentProps}
        ref={ref}
        side="bottom"
        showCloseButton={dismissible}
        onEscapeKeyDown={(event) => {
          if (!dismissible) event.preventDefault();
        }}
        onInteractOutside={(event) => {
          if (!dismissible) event.preventDefault();
        }}
        className={cn(
          "mx-auto max-h-[90dvh] w-full max-w-2xl rounded-t-ien-xl border-ien-border bg-ien-surface text-ien-text-primary",
          className,
        )}
      >
        <div
          aria-hidden
          className="mx-auto mt-ien-xs h-1 w-10 rounded-ien-full bg-ien-border-strong"
        />
        {(title || description) && (
          <SheetHeader className="px-ien-lg pb-ien-none">
            {title && (
              <SheetTitle className="text-ien-title3 text-ien-text-primary">
                {title}
              </SheetTitle>
            )}
            {description && (
              <SheetDescription className="text-ien-body2 text-ien-text-secondary">
                {description}
              </SheetDescription>
            )}
          </SheetHeader>
        )}
        <div className="min-h-0 overflow-y-auto px-ien-lg pb-[max(var(--ien-spacing-lg),env(safe-area-inset-bottom))]">
          {children}
        </div>
      </SheetContent>
    </Sheet>
  );
}

export type BottomSheetOption<T extends string = string> = {
  value: T;
  label: React.ReactNode;
  description?: React.ReactNode;
  disabled?: boolean;
};
export type BottomSheetSelectProps<T extends string = string> = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  value?: T;
  defaultValue?: T;
  onValueChange?: (value: T) => void;
  options: BottomSheetOption<T>[];
  title?: React.ReactNode;
  description?: React.ReactNode;
  placeholder?: string;
  disabled?: boolean;
  className?: string;
};

function BottomSheetSelect<T extends string>({
  value,
  defaultValue,
  onValueChange,
  options,
  title = "선택",
  description,
  placeholder = "항목 선택",
  disabled = false,
  className,
  ref,
  ...rootProps
}: BottomSheetSelectProps<T>) {
  const [internalValue, setInternalValue] = React.useState<T | undefined>(
    defaultValue,
  );
  const [open, setOpen] = React.useState(false);
  const idPrefix = React.useId().replaceAll(":", "");
  const selected = value ?? internalValue;
  const selectedOption = options.find((option) => option.value === selected);
  const choose = (next: T) => {
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
    setOpen(false);
  };
  return (
    <div {...rootProps} ref={ref} className={cn("grid gap-ien-xs", className)}>
      <Button
        type="button"
        variant="line"
        tone="neutral"
        disabled={disabled}
        aria-haspopup="dialog"
        aria-expanded={open}
        onClick={() => setOpen(true)}
      >
        {selectedOption?.label ?? placeholder}
      </Button>
      <BottomSheet
        open={open}
        onOpenChange={setOpen}
        title={title}
        description={description}
      >
        <RadioGroup
          value={selected}
          onValueChange={choose}
          aria-label={typeof title === "string" ? title : "선택 항목"}
          className="grid gap-ien-xs py-ien-sm"
        >
          {options.map((option, index) => {
            const optionId = idPrefix + "-option-" + index;
            const optionLabelId = optionId + "-label";
            const optionDescriptionId = optionId + "-description";
            return (
              <label
                key={option.value}
                htmlFor={optionId}
                className={cn(
                  "flex min-h-12 cursor-pointer items-start gap-ien-sm rounded-ien-default border border-ien-border p-ien-sm text-ien-body2 transition-colors duration-[var(--ien-motion-fast)] has-[:disabled]:cursor-not-allowed has-[:disabled]:opacity-50",
                  option.value === selected
                    ? "border-ien-brand bg-ien-brand-weak text-ien-on-brand-weak"
                    : "bg-ien-surface text-ien-text-primary",
                  option.disabled && "cursor-not-allowed opacity-50",
                )}
              >
                <RadioGroupItem
                  id={optionId}
                  value={option.value}
                  disabled={disabled || option.disabled}
                  aria-labelledby={optionLabelId}
                  aria-describedby={
                    option.description ? optionDescriptionId : undefined
                  }
                  className="mt-ien-xxxs"
                />
                <span className="grid gap-[var(--ien-spacing-xxxs)]">
                  <span id={optionLabelId}>{option.label}</span>
                  {option.description && (
                    <span
                      id={optionDescriptionId}
                      className="text-ien-caption text-ien-text-secondary"
                    >
                      {option.description}
                    </span>
                  )}
                </span>
              </label>
            );
          })}
        </RadioGroup>
      </BottomSheet>
    </div>
  );
}

export {
  BottomSheet,
  BottomSheet as IenBottomSheet,
  BottomSheetSelect,
  BottomSheetSelect as IenBottomSheetSelect,
};
