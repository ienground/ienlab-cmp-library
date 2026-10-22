"use client";

import * as React from "react";
import { ChevronDown } from "lucide-react";
import { Checkbox } from "@/components/ui/checkbox";
import { cn } from "@/lib/ien-utils";

export type AgreementVariant =
  | "xLarge"
  | "large"
  | "medium"
  | "mediumTitle"
  | "small"
  | "smallLast";
export type AgreementCheckboxVariant = "checkbox" | "dot" | "hidden";
export type AgreementCheckboxMotion = "strong" | "weak";

export type AgreementItem = {
  id: string;
  label?: React.ReactNode;
  title?: React.ReactNode;
  checked?: boolean;
  required?: boolean;
  showOptional?: boolean;
  description?: React.ReactNode;
  enabled?: boolean;
  indent?: boolean;
  checkboxVariant?: AgreementCheckboxVariant;
  checkboxMotion?: AgreementCheckboxMotion;
  transitionDelay?: number;
  href?: string;
  left?: React.ReactNode;
  middle?: React.ReactNode;
  right?: React.ReactNode;
  collapsible?: boolean;
  collapsed?: boolean;
  defaultCollapsed?: boolean;
  onCollapsedChange?: (collapsed: boolean) => void;
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
  onItemCheckedChange?: (id: string, checked: boolean) => void;
  onAllCheckedChange?: (checked: boolean) => void;
  onOpenItem?: (item: AgreementItem) => void;
  title?: React.ReactNode;
  variant?: AgreementVariant;
  itemVariant?: AgreementVariant;
  enabled?: boolean;
  disabled?: boolean;
  showGradient?: boolean;
  collapsible?: boolean;
  collapsed?: boolean;
  defaultCollapsed?: boolean;
  onCollapsedChange?: (collapsed: boolean) => void;
  leftSlot?: React.ReactNode;
  middleSlot?: React.ReactNode;
  rightSlot?: React.ReactNode;
  className?: string;
};

const rowStyles: Record<AgreementVariant, { height: string; py: string; text: string; bold?: boolean }> = {
  xLarge: { height: "min-h-16", py: "py-[14px]", text: "text-ien-title3", bold: true },
  large: { height: "min-h-14", py: "py-[10px]", text: "text-ien-body1" },
  medium: { height: "min-h-12", py: "py-2", text: "text-ien-body2" },
  mediumTitle: { height: "min-h-12", py: "py-2", text: "text-ien-body1", bold: true },
  small: { height: "min-h-10", py: "py-1.5", text: "text-ien-body2" },
  smallLast: { height: "min-h-9", py: "py-1", text: "text-ien-body2" },
};

export type AgreementRowProps = {
  variant?: AgreementVariant;
  indent?: number;
  enabled?: boolean;
  left?: React.ReactNode;
  middle?: React.ReactNode;
  right?: React.ReactNode;
  onClick?: React.MouseEventHandler<HTMLDivElement>;
  onPressEnd?: React.MouseEventHandler<HTMLDivElement>;
  className?: string;
};

function AgreementRow({
  variant = "large",
  indent = 0,
  enabled = true,
  left,
  middle,
  right,
  onClick,
  onPressEnd,
  className,
}: AgreementRowProps) {
  const styles = rowStyles[variant];
  const handleClick = onPressEnd ?? onClick;
  return (
    <div
      role={handleClick ? "button" : undefined}
      tabIndex={handleClick && enabled ? 0 : undefined}
      aria-disabled={handleClick && !enabled || undefined}
      onClick={enabled ? handleClick : undefined}
      onKeyDown={
        enabled && handleClick
          ? (event) => {
              if (event.key === "Enter" || event.key === " ") {
                event.preventDefault();
                event.currentTarget.click();
              }
            }
          : undefined
      }
      className={cn(
        "relative flex w-full items-center gap-ien-xs px-ien-md transition-[opacity,background-color] duration-[var(--ien-motion-fast)]",
        styles.height,
        styles.py,
        handleClick && enabled && "cursor-pointer active:opacity-[0.72]",
        handleClick && !enabled && "cursor-not-allowed",
        className,
      )}
      style={{ paddingLeft: `calc(var(--ien-spacing-md) + ${Math.max(0, indent) * 24}px)` }}
    >
      {left != null && (
        <span
          className="inline-flex w-6 shrink-0 items-center justify-center"
          onClick={(event) => event.stopPropagation()}
        >
          {left}
        </span>
      )}
      <span
        className={cn(
          "flex min-w-0 flex-1 items-center gap-ien-xs",
          styles.text,
          styles.bold && "font-semibold",
          !enabled && "text-ien-text-disabled",
        )}
      >
        {middle}
      </span>
      {right != null && (
        <span
          className="inline-flex shrink-0 items-center gap-ien-xs"
          onClick={(event) => event.stopPropagation()}
        >
          {right}
        </span>
      )}
    </div>
  );
}

function Agreement({
  items,
  value,
  defaultValue = [],
  onValueChange,
  onItemCheckedChange,
  onAllCheckedChange,
  onOpenItem,
  title = "전체 동의",
  variant = "large",
  itemVariant = "small",
  enabled = true,
  disabled = false,
  showGradient = true,
  collapsible = false,
  collapsed,
  defaultCollapsed = false,
  onCollapsedChange,
  leftSlot,
  middleSlot,
  rightSlot,
  className,
  ref,
  ...rootProps
}: AgreementProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const [internalCollapsed, setInternalCollapsed] = React.useState(defaultCollapsed);
  const [internalItemCollapsed, setInternalItemCollapsed] = React.useState<Record<string, boolean>>({});
  const idPrefix = React.useId().replaceAll(":", "");
  const isEnabled = enabled && !disabled;
  const itemStateIsControlled = value !== undefined || items.some((item) => item.checked !== undefined);
  const selected = value ?? (
    items.some((item) => item.checked !== undefined)
      ? items.filter((item) => item.checked).map((item) => item.id)
      : internalValue
  );
  const isCollapsed = collapsed ?? internalCollapsed;
  const allChecked = items.length > 0 && items.every((item) => selected.includes(item.id));
  const emitValue = (next: string[]) => {
    const unique = [...new Set(next)];
    if (!itemStateIsControlled) setInternalValue(unique);
    onValueChange?.(unique);
  };

  const updateItem = (item: AgreementItem, checked: boolean) => {
    if (!isEnabled || item.enabled === false) return;
    const next = checked
      ? [...selected, item.id]
      : selected.filter((id) => id !== item.id);
    emitValue(next);
    onItemCheckedChange?.(item.id, checked);
  };

  const toggleAll = (checked: boolean) => {
    if (!isEnabled) return;
    onAllCheckedChange?.(checked);
    const next = checked
      ? items.filter((item) => item.enabled !== false).map((item) => item.id)
      : selected.filter((id) => items.some((item) => item.id === id && item.enabled === false));
    emitValue(next);
    for (const item of items) {
      if (item.enabled === false) continue;
      onItemCheckedChange?.(item.id, checked);
    }
  };

  const toggleCollapsed = () => {
    const next = !isCollapsed;
    if (collapsed === undefined) setInternalCollapsed(next);
    onCollapsedChange?.(next);
  };

  return (
    <fieldset
      {...rootProps}
      ref={ref}
      disabled={!isEnabled}
      className={cn(
        "grid gap-ien-xs rounded-ien-default border border-ien-border bg-ien-surface p-ien-md text-ien-text-primary",
        className,
      )}
    >
      <legend className="sr-only">약관 동의</legend>
      <AgreementRow
        variant={variant}
        enabled={isEnabled}
        left={
          leftSlot ?? (
            <Checkbox
              checked={allChecked}
              disabled={!isEnabled}
              onCheckedChange={(checked) => toggleAll(checked === true)}
              aria-label="모든 약관에 동의"
            />
          )
        }
        middle={middleSlot ?? title}
        right={
          <>
            {rightSlot}
            {collapsible && (
              <button
                type="button"
                disabled={!isEnabled}
                aria-label={isCollapsed ? "약관 펼치기" : "약관 접기"}
                aria-expanded={!isCollapsed}
                onClick={(event) => {
                  event.stopPropagation();
                  toggleCollapsed();
                }}
                className="grid size-8 place-items-center rounded-ien-full text-ien-text-disabled outline-none focus-visible:ring-2 focus-visible:ring-ien-brand disabled:cursor-not-allowed"
              >
                <ChevronDown
                  aria-hidden="true"
                  className={cn("size-4 transition-transform duration-[var(--ien-motion-fast)]", !isCollapsed && "rotate-180")}
                />
              </button>
            )}
          </>
        }
        onPressEnd={collapsible ? toggleCollapsed : undefined}
        className="border-b border-ien-border pb-ien-xs"
      />
      {!isCollapsed && items.map((item) => {
        const itemEnabled = isEnabled && item.enabled !== false;
        const itemChecked = selected.includes(item.id);
        const itemCollapsed = item.collapsed ?? internalItemCollapsed[item.id] ?? item.defaultCollapsed ?? false;
        const checkboxId = `${idPrefix}-agreement-${item.id}`;
        const labelId = `${idPrefix}-label-${item.id}`;
        const displayLabel = item.title ?? item.label;
        const toggleItemCollapsed = () => {
          const next = !itemCollapsed;
          if (item.collapsed === undefined) {
            setInternalItemCollapsed((previous) => ({ ...previous, [item.id]: next }));
          }
          item.onCollapsedChange?.(next);
        };
        const checkbox = item.left ?? (
          item.checkboxVariant === "hidden" ? (
            <span aria-hidden="true" className="size-6" />
          ) : (
            <Checkbox
              id={checkboxId}
              shape={item.checkboxVariant === "dot" || item.indent ? "dot" : "circle"}
              checked={itemChecked}
              disabled={!itemEnabled}
              onCheckedChange={(checked) => updateItem(item, checked === true)}
              aria-labelledby={labelId}
              className={cn(
                "size-6 transition-transform motion-reduce:transition-none",
                item.checkboxMotion === "strong" ? "duration-300 ease-out" : "duration-[180ms]",
                "data-[state=checked]:scale-100 data-[state=unchecked]:scale-[0.86]",
              )}
              style={{ transitionDelay: `${Math.max(0, (item.transitionDelay ?? 0) + 0.1)}s` }}
            />
          )
        );
        const necessity = item.required
          ? <span className="shrink-0 text-ien-caption font-semibold text-ien-brand">[필수]</span>
          : item.showOptional
            ? <span className="shrink-0 text-ien-caption font-semibold text-ien-text-secondary">[선택]</span>
            : null;
        const defaultMiddle = (
          <span className="grid min-w-0 gap-ien-xxs">
            <span className="flex min-w-0 items-center gap-ien-xs">
              {necessity}
              <label
                id={labelId}
                htmlFor={checkboxId}
                className="min-w-0 flex-1 cursor-pointer"
                onClick={(event) => event.stopPropagation()}
              >
                {displayLabel}
              </label>
            </span>
            {item.description != null && !itemCollapsed && (
              <span className="text-ien-caption text-ien-text-secondary">{item.description}</span>
            )}
          </span>
        );
        const arrow = item.collapsible && (
          <button
            type="button"
            disabled={!itemEnabled}
            aria-label={itemCollapsed ? "약관 상세 펼치기" : "약관 상세 접기"}
            aria-expanded={!itemCollapsed}
            onClick={(event) => {
              event.stopPropagation();
              toggleItemCollapsed();
            }}
            className="grid size-8 place-items-center rounded-ien-full text-ien-text-disabled outline-none focus-visible:ring-2 focus-visible:ring-ien-brand disabled:cursor-not-allowed"
          >
            <ChevronDown aria-hidden="true" className={cn("size-4 transition-transform duration-[var(--ien-motion-fast)]", !itemCollapsed && "rotate-180")} />
          </button>
        );
        const openButton = item.href ? (
          <a
            href={item.href}
            onClick={(event) => event.stopPropagation()}
            className="min-h-8 px-ien-xs text-ien-caption text-ien-text-secondary underline underline-offset-2"
          >
            보기
          </a>
        ) : onOpenItem ? (
          <button
            type="button"
            disabled={!itemEnabled}
            onClick={(event) => {
              event.stopPropagation();
              onOpenItem(item);
            }}
            className="min-h-8 px-ien-xs text-ien-caption text-ien-text-secondary underline underline-offset-2 disabled:cursor-not-allowed"
          >
            보기
          </button>
        ) : null;

        return (
          <div key={item.id} className="relative">
            {showGradient && item.indent && (
              <span
                aria-hidden="true"
                className="absolute inset-y-0 left-[18px] z-0 w-[2px] rounded-full"
                style={{ backgroundImage: "linear-gradient(to bottom, transparent, var(--ien-border-strong), transparent)" }}
              />
            )}
            <AgreementRow
              variant={item.indent ? "small" : itemVariant}
              indent={item.indent ? 1 : 0}
              enabled={itemEnabled}
              left={checkbox}
              middle={item.middle ?? defaultMiddle}
              right={item.right ?? <>{arrow}{openButton}</>}
              onPressEnd={itemEnabled ? () => updateItem(item, !itemChecked) : undefined}
              className={cn(item.indent && "bg-ien-surface-variant/30")}
            />
          </div>
        );
      })}
    </fieldset>
  );
}

export { Agreement, Agreement as IenAgreement, AgreementRow, AgreementRow as IenAgreementRow };
