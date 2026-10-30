"use client";

import * as React from "react";
import { Checkbox } from "@/components/ui/checkbox";
import { cn } from "cn";

export type AgreementItem = {
  id: string;
  label: React.ReactNode;
  required?: boolean;
  description?: React.ReactNode;
  href?: string;
};
export type AgreementProps = Omit<
  React.ComponentProps<"fieldset">,
  "children" | "disabled" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLFieldSetElement>;
  items: AgreementItem[];
  value?: string[];
  defaultValue?: string[];
  onValueChange?: (value: string[]) => void;
  onOpenItem?: (item: AgreementItem) => void;
  disabled?: boolean;
  className?: string;
};

function Agreement({
  items,
  value,
  defaultValue = [],
  onValueChange,
  onOpenItem,
  disabled = false,
  className,
  ref,
  ...rootProps
}: AgreementProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const idPrefix = React.useId().replaceAll(":", "");
  const selected = value ?? internalValue;
  const update = (id: string, checked: boolean) => {
    const next = checked
      ? [...new Set([...selected, id])]
      : selected.filter((item) => item !== id);
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
  };
  const allChecked =
    items.length > 0 && items.every((item) => selected.includes(item.id));
  const toggleAll = (checked: boolean) => {
    const next = checked ? items.map((item) => item.id) : [];
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
  };
  return (
    <fieldset
      {...rootProps}
      ref={ref}
      disabled={disabled}
      className={cn(
        "grid gap-ien-xs rounded-ien-default border border-ien-border bg-ien-surface p-ien-md text-ien-text-primary disabled:opacity-50",
        className,
      )}
    >
      <legend className="sr-only">약관 동의</legend>
      <label className="flex min-h-[var(--ien-state-minimum-touch-target)] cursor-pointer items-center gap-ien-sm border-b border-ien-border pb-ien-xs text-ien-label1">
        <Checkbox
          checked={allChecked}
          disabled={disabled}
          onCheckedChange={(checked) => toggleAll(checked === true)}
          aria-label="모든 약관에 동의"
        />
        <span>전체 동의</span>
      </label>
      {items.map((item) => {
        const checkboxId = idPrefix + "-agreement-" + item.id;
        const labelId = idPrefix + "-label-" + item.id;
        return (
          <div
            key={item.id}
            className="flex min-h-[var(--ien-state-minimum-touch-target)] items-start gap-ien-sm py-[var(--ien-control-gap)]"
          >
            <Checkbox
              id={checkboxId}
              disabled={disabled}
              checked={selected.includes(item.id)}
              onCheckedChange={(checked) => update(item.id, checked === true)}
              aria-labelledby={labelId}
              className="mt-ien-xxxs"
            />
            <div className="min-w-0 flex-1 text-ien-body2">
              <label
                id={labelId}
                htmlFor={checkboxId}
                className="cursor-pointer"
              >
                <span
                  className={
                    item.required ? "text-ien-danger" : "text-ien-text-tertiary"
                  }
                >
                  [{item.required ? "필수" : "선택"}]{" "}
                </span>
                {item.label}
              </label>
              {item.description && (
                <p className="mt-ien-xxs text-ien-caption text-ien-text-secondary">
                  {item.description}
                </p>
              )}
            </div>
            {onOpenItem && (
              <button
                type="button"
                disabled={disabled}
                onClick={() => onOpenItem(item)}
                className="min-h-9 px-ien-xs text-ien-caption text-ien-text-secondary underline underline-offset-2 disabled:cursor-not-allowed"
              >
                보기
              </button>
            )}
          </div>
        );
      })}
    </fieldset>
  );
}

export { Agreement, Agreement as IenAgreement };
