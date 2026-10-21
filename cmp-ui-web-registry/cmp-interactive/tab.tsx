"use client";

import * as React from "react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { cn } from "cn";

export type TabItem = {
  value: string;
  label: React.ReactNode;
  content: React.ReactNode;
  disabled?: boolean;
};
export type TabProps = Omit<
  React.ComponentProps<typeof Tabs>,
  | "children"
  | "ref"
  | "value"
  | "defaultValue"
  | "onValueChange"
  | "orientation"
  | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  items: TabItem[];
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  orientation?: "horizontal" | "vertical";
  className?: string;
};

function Tab({
  items,
  value,
  defaultValue,
  onValueChange,
  orientation = "horizontal",
  className,
  ref,
  ...tabsProps
}: TabProps) {
  const first = items[0]?.value;
  return (
    <Tabs
      {...tabsProps}
      ref={ref}
      value={value}
      defaultValue={defaultValue ?? first}
      onValueChange={onValueChange}
      orientation={orientation}
      className={cn(orientation === "vertical" && "flex-row", className)}
    >
      <TabsList
        variant="line"
        className={cn(
          "w-full justify-start rounded-none border-b border-ien-border bg-transparent p-ien-none",
          orientation === "vertical" && "w-auto border-r border-b-0",
        )}
      >
        {items.map((item) => (
          <TabsTrigger
            key={item.value}
            value={item.value}
            disabled={item.disabled}
            className="min-h-[var(--ien-state-minimum-touch-target)] rounded-none px-ien-md text-ien-label2 data-[state=active]:text-ien-brand"
          >
            {item.label}
          </TabsTrigger>
        ))}
      </TabsList>
      {items.map((item) => (
        <TabsContent key={item.value} value={item.value} className="pt-ien-md">
          {item.content}
        </TabsContent>
      ))}
    </Tabs>
  );
}

export { Tab, Tab as IenTab };
