"use client";

/*
 * MIT License
 *
 * Copyright (c) 2023 shadcn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/ien-utils";
import { Tabs as TabsPrimitive } from "radix-ui";

function Tabs({
  className,
  orientation = "horizontal",
  ...props
}: React.ComponentProps<typeof TabsPrimitive.Root>) {
  return (
    <TabsPrimitive.Root
      data-slot="tabs"
      data-orientation={orientation}
      orientation={orientation}
      className={cn(
        "group/tabs flex gap-ien-xs data-[orientation=horizontal]:flex-col",
        className,
      )}
      {...props}
    />
  );
}

const tabsListVariants = cva(
  "group/tabs-list inline-flex w-fit items-center justify-center rounded-[var(--ien-control-radius-base)] p-[3px] text-ien-text-secondary group-data-[orientation=horizontal]/tabs:h-8 group-data-[orientation=vertical]/tabs:h-fit group-data-[orientation=vertical]/tabs:flex-col data-[variant=line]:rounded-none",
  {
    variants: {
      variant: {
        default: "bg-ien-surface-variant",
        line: "gap-ien-xxs bg-transparent",
      },
    },
    defaultVariants: {
      variant: "default",
    },
  },
);

function TabsList({
  className,
  variant = "default",
  ref,
  children,
  ...props
}: React.ComponentProps<typeof TabsPrimitive.List> &
  VariantProps<typeof tabsListVariants>) {
  const listRef = React.useRef<HTMLDivElement>(null);
  const [indicator, setIndicator] = React.useState<{
    x: number;
    y: number;
    width: number;
    height: number;
    vertical: boolean;
  } | null>(null);

  React.useLayoutEffect(() => {
    if (variant !== "line") {
      setIndicator(null);
      return;
    }

    const list = listRef.current;
    if (!list) return;
    const updateIndicator = () => {
      const activeTab = list.querySelector<HTMLElement>(
        '[role="tab"][data-state="active"]',
      );
      if (!activeTab) {
        setIndicator(null);
        return;
      }
      const listBounds = list.getBoundingClientRect();
      const tabBounds = activeTab.getBoundingClientRect();
      const vertical = list.dataset.orientation === "vertical";
      const width = Math.max(20, tabBounds.width - 24);
      const height = Math.max(20, tabBounds.height - 24);
      const bounds = {
        x:
          tabBounds.left - listBounds.left + list.scrollLeft +
          (tabBounds.width - width) / 2,
        y:
          tabBounds.top - listBounds.top + list.scrollTop +
          (tabBounds.height - height) / 2,
        width,
        height,
        vertical,
      };
      setIndicator((previous) =>
        previous &&
        previous.x === bounds.x &&
        previous.y === bounds.y &&
        previous.width === bounds.width &&
        previous.height === bounds.height &&
        previous.vertical === bounds.vertical
          ? previous
          : bounds,
      );
    };

    updateIndicator();
    const resizeObserver = new ResizeObserver(updateIndicator);
    resizeObserver.observe(list);
    list.querySelectorAll<HTMLElement>('[role="tab"]').forEach((tab) =>
      resizeObserver.observe(tab),
    );
    const mutationObserver = new MutationObserver(updateIndicator);
    mutationObserver.observe(list, {
      attributes: true,
      attributeFilter: ["data-state", "data-orientation"],
      childList: true,
      subtree: true,
    });
    list.addEventListener("scroll", updateIndicator);
    window.addEventListener("resize", updateIndicator);

    return () => {
      resizeObserver.disconnect();
      mutationObserver.disconnect();
      list.removeEventListener("scroll", updateIndicator);
      window.removeEventListener("resize", updateIndicator);
    };
  }, [variant]);
  React.useImperativeHandle(ref, () => listRef.current!, [ref]);

  return (
    <TabsPrimitive.List
      ref={listRef}
      data-slot="tabs-list"
      data-variant={variant}
      className={cn(
        tabsListVariants({ variant }),
        variant === "line" && "relative h-auto p-0",
        className,
      )}
      {...props}
    >
      {children}
      {variant === "line" && indicator && (
        <span
          aria-hidden="true"
          className={cn(
            "pointer-events-none absolute z-10 bg-ien-text-primary transition-[transform,width,height] duration-[var(--ien-motion-spring-duration)] [transition-timing-function:var(--ien-motion-spring-medium-bouncy)] motion-reduce:transition-none",
            indicator.vertical
              ? "top-0 right-0 w-0.5 rounded-full"
              : "bottom-0 left-0 h-0.5 rounded-full",
          )}
          style={{
            width: indicator.vertical ? undefined : indicator.width,
            height: indicator.vertical ? indicator.height : undefined,
            transform: indicator.vertical
              ? `translateY(${indicator.y}px)`
              : `translateX(${indicator.x}px)`,
          }}
        />
      )}
    </TabsPrimitive.List>
  );
}

function TabsTrigger({
  className,
  ...props
}: React.ComponentProps<typeof TabsPrimitive.Trigger>) {
  return (
    <TabsPrimitive.Trigger
      data-slot="tabs-trigger"
      className={cn(
        "relative inline-flex h-[calc(100%-1px)] flex-1 items-center justify-center gap-[var(--ien-control-gap)] rounded-ien-sm border-ien-border border-transparent px-[var(--ien-control-gap)] py-ien-xxxs text-ien-body2 font-[var(--ien-font-weight-label1)] whitespace-nowrap text-ien-text-primary/60 transition-colors duration-[var(--ien-motion-fast)] group-data-[orientation=vertical]/tabs:w-full group-data-[orientation=vertical]/tabs:justify-start hover:text-ien-text-primary focus-visible:border-ien-brand focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 focus-visible:outline-1 focus-visible:outline-ien-brand disabled:pointer-events-none disabled:opacity-[var(--ien-state-disabled-alpha)] has-data-[icon=inline-end]:pr-ien-xxs has-data-[icon=inline-start]:pl-ien-xxs group-data-[variant=default]/tabs-list:data-[state=active]:shadow-[0_var(--ien-elevation-raised)_calc(var(--ien-elevation-raised)*2)_rgb(0_0_0/0.12)] group-data-[variant=line]/tabs-list:data-[state=active]:shadow-none [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
        "group-data-[variant=line]/tabs-list:bg-transparent group-data-[variant=line]/tabs-list:data-[state=active]:bg-transparent dark:group-data-[variant=line]/tabs-list:data-[state=active]:border-transparent dark:group-data-[variant=line]/tabs-list:data-[state=active]:bg-transparent",
        "data-[state=active]:bg-ien-background data-[state=active]:text-ien-text-primary dark:data-[state=active]:border-ien-surface-variant dark:data-[state=active]:bg-ien-surface-variant/30 dark:data-[state=active]:text-ien-text-primary",
        className,
      )}
      {...props}
    />
  );
}

function TabsContent({
  className,
  ...props
}: React.ComponentProps<typeof TabsPrimitive.Content>) {
  return (
    <TabsPrimitive.Content
      data-slot="tabs-content"
      className={cn("flex-1 text-ien-body2 outline-none", className)}
      {...props}
    />
  );
}

export { Tabs, TabsList, TabsTrigger, TabsContent, tabsListVariants };
