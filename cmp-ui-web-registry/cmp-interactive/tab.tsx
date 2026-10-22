"use client";

import * as React from "react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { cn } from "@/lib/ien-utils";

export type TabItem = {
  value: string;
  label: React.ReactNode;
  content: React.ReactNode;
  enabled?: boolean;
  disabled?: boolean;
  key?: React.Key;
  redBean?: boolean;
  ariaLabel?: string;
  icon?: React.ReactNode;
  selectedIcon?: React.ReactNode;
  badge?: number;
};

export type TabProps = Omit<
  React.ComponentProps<typeof Tabs>,
  | "children"
  | "ref"
  | "value"
  | "defaultValue"
  | "onValueChange"
  | "onChange"
  | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  items: TabItem[];
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  selectedIndex?: number;
  onSelectedIndexChange?: (index: number) => void;
  size?: "small" | "large";
  fluid?: boolean;
  itemGap?: number | string;
  ariaLabel?: string;
  onChange?: (index: number, key: React.Key | undefined) => void;
  className?: string;
};

function Tab({
  items,
  value,
  defaultValue,
  onValueChange,
  selectedIndex,
  onSelectedIndexChange,
  size = "large",
  fluid = false,
  itemGap,
  ariaLabel = "탭",
  onChange,
  orientation = "horizontal",
  className,
  ref,
  ...tabsProps
}: TabProps) {
  const firstEnabledValue =
    items.find((item) => item.enabled !== false && !item.disabled)?.value ??
    items[0]?.value;
  const initialValue = defaultValue ?? firstEnabledValue;
  const [localValue, setLocalValue] = React.useState<string | undefined>(initialValue);
  const controlled = selectedIndex !== undefined || value !== undefined;
  const selectedValue =
    selectedIndex !== undefined
      ? items[selectedIndex]?.value
      : value !== undefined
        ? value
        : localValue;
  const activeIndex = items.findIndex((item) => item.value === selectedValue);
  const tabHeight = size === "small" ? "h-10" : "h-12";
  const fluidHorizontal = fluid && orientation === "horizontal";
  const listRef = React.useRef<HTMLDivElement>(null);

  React.useEffect(() => {
    if (!controlled) setLocalValue(initialValue);
  }, [controlled, initialValue]);

  const handleValueChange = (nextValue: string) => {
    const nextIndex = items.findIndex((item) => item.value === nextValue);
    if (nextIndex < 0) return;
    const item = items[nextIndex];
    if (!item || item.disabled || item.enabled === false) return;
    if (!controlled) setLocalValue(nextValue);
    onValueChange?.(nextValue);
    onSelectedIndexChange?.(nextIndex);
    onChange?.(nextIndex, item.key);
  };

  React.useEffect(() => {
    if (!fluidHorizontal || activeIndex < 0) return;
    const list = listRef.current;
    const activeTab = list?.querySelector<HTMLElement>(
      '[role="tab"][data-state="active"]',
    );
    if (!list || !activeTab) return;
    const listBounds = list.getBoundingClientRect();
    const tabBounds = activeTab.getBoundingClientRect();
    const centeredLeft =
      list.scrollLeft +
      tabBounds.left -
      listBounds.left +
      tabBounds.width / 2 -
      list.clientWidth / 2;
    const left = Math.max(0, Math.min(list.scrollWidth - list.clientWidth, centeredLeft));
    const reduceMotion =
      typeof window.matchMedia === "function" &&
      window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    list.scrollTo({ left, behavior: reduceMotion ? "auto" : "smooth" });
  }, [activeIndex, fluidHorizontal, items.length]);

  return (
    <Tabs
      {...tabsProps}
      ref={ref}
      value={selectedValue}
      onValueChange={handleValueChange}
      orientation={orientation}
      className={cn(orientation === "vertical" && "flex-row", className)}
    >
      <TabsList
        variant="line"
        ref={listRef}
        aria-label={ariaLabel}
        className={cn(
          "h-auto rounded-none bg-transparent p-0",
          fluidHorizontal
            ? "w-max max-w-full overflow-x-auto"
            : orientation === "horizontal"
              ? "w-full"
              : "w-auto shrink-0 flex-col",
        )}
        style={{
          gap:
            itemGap ??
            (fluidHorizontal ? "var(--ien-spacing-xl)" : "var(--ien-spacing-none)"),
        }}
      >
        {items.map((item, index) => {
          const isActive = index === activeIndex;
          const badge = item.badge && item.badge !== 0 ? item.badge : item.redBean ? -1 : 0;
          const textLabel =
            typeof item.label === "string" || typeof item.label === "number"
              ? String(item.label)
              : undefined;
          const badgeLabel =
            badge > 0
              ? `, 알림 ${badge > 99 ? "99+" : badge}`
              : badge < 0
                ? ", 새 알림 있음"
                : "";
          const icon = isActive ? item.selectedIcon ?? item.icon : item.icon;

          return (
            <TabsTrigger
              key={item.key ?? item.value}
              value={item.value}
              disabled={item.disabled || item.enabled === false}
              aria-label={item.ariaLabel ?? (textLabel ? `${textLabel}${badgeLabel}` : undefined)}
              className={cn(
                "justify-center rounded-none px-0 text-ien-text-secondary transition-[color,scale] duration-[var(--ien-motion-fast)] active:scale-[0.94] data-[state=active]:text-ien-text-primary",
                tabHeight,
                size === "small" ? "text-ien-label2" : "text-ien-label1",
                fluidHorizontal
                  ? size === "small"
                    ? "min-w-12 flex-none px-ien-xs"
                    : "min-w-14 flex-none px-ien-xs"
                  : orientation === "vertical"
                    ? "w-full flex-none justify-start"
                    : "flex-1",
              )}
            >
              {icon && (
                <span aria-hidden="true" className="inline-flex shrink-0 items-center">
                  {icon}
                </span>
              )}
              <span className="relative inline-flex min-w-0 items-center justify-center gap-ien-xs">
                {item.label}
                {badge > 0 && (
                  <span
                    aria-hidden="true"
                    className="inline-flex min-h-5 items-center rounded-ien-full bg-ien-danger px-ien-xs py-ien-xxxs text-ien-caption text-ien-on-danger"
                  >
                    {badge > 99 ? "99+" : badge}
                  </span>
                )}
                {badge < 0 && (
                  <span
                    aria-hidden="true"
                    className="absolute -top-0.5 -right-1 size-[5px] rounded-full bg-ien-danger"
                  />
                )}
              </span>
            </TabsTrigger>
          );
        })}
      </TabsList>
      {items.map((item) => (
        <TabsContent
          key={item.key ?? item.value}
          value={item.value}
          className={orientation === "vertical" ? "pl-ien-md" : "pt-ien-md"}
        >
          {item.content}
        </TabsContent>
      ))}
    </Tabs>
  );
}

export { Tab, Tab as IenTab };
