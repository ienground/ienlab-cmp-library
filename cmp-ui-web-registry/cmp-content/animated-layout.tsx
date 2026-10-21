"use client";

import * as React from "react";
import { cn } from "cn";

export interface IenAnimatedItemsProps<T> extends Omit<
  React.ComponentPropsWithRef<"div">,
  "children"
> {
  items: readonly T[];
  itemKey: (item: T) => React.Key;
  renderItem: (item: T) => React.ReactNode;
  gap?: number | string;
  align?: "start" | "center" | "end" | "stretch";
}

function motionDurationMs(element: Element | null): number {
  if (!element) return 0;
  const duration = getComputedStyle(element)
    .getPropertyValue("--ien-motion-normal")
    .trim();
  const value = Number.parseFloat(duration);
  if (!Number.isFinite(value)) return 0;
  return duration.endsWith("ms")
    ? value
    : duration.endsWith("s")
      ? value * 1000
      : 0;
}

type AnimatedEntry<T> = { item: T; key: React.Key; visible: boolean };

function AnimatedItems<T>({
  items,
  itemKey,
  renderItem,
  direction,
  gap = 0,
  align = "stretch",
  className,
  style,
  ref: forwardedRef,
  ...props
}: IenAnimatedItemsProps<T> & { direction: "column" | "row" }) {
  const [entries, setEntries] = React.useState<AnimatedEntry<T>[]>(() =>
    items.map((item) => ({ item, key: itemKey(item), visible: true })),
  );
  const entriesRef = React.useRef(entries);
  const rootElement = React.useRef<HTMLDivElement>(null);
  const setRootRef = React.useCallback(
    (element: HTMLDivElement | null) => {
      rootElement.current = element;
      if (typeof forwardedRef === "function") return forwardedRef(element);
      if (forwardedRef) forwardedRef.current = element;
    },
    [forwardedRef],
  );
  const elements = React.useRef(new Map<React.Key, HTMLDivElement>());
  const previousRects = React.useRef(new Map<React.Key, DOMRect>());
  entriesRef.current = entries;

  React.useEffect(() => {
    const next: AnimatedEntry<T>[] = items.map((item) => ({
      item,
      key: itemKey(item),
      visible: true,
    }));
    const keys = new Set(next.map((entry) => entry.key));
    const old = entriesRef.current;
    const visibleEntries = old.filter((entry) => entry.visible);
    const unchanged =
      next.length === visibleEntries.length &&
      next.every(
        (entry, index) =>
          entry.key === visibleEntries[index]?.key &&
          Object.is(entry.item, visibleEntries[index]?.item),
      );
    const hasExitingEntries = old.some((entry) => !entry.visible);
    if (unchanged && !hasExitingEntries) return;
    if (!unchanged) {
      setEntries([
        ...next,
        ...old
          .filter((entry) => !keys.has(entry.key))
          .map((entry) => ({ ...entry, visible: false })),
      ]);
    }
    const timer = window.setTimeout(() => {
      setEntries((current) => current.filter((entry) => entry.visible));
    }, motionDurationMs(rootElement.current));
    return () => window.clearTimeout(timer);
  }, [items, itemKey]);

  React.useEffect(() => {
    const reducedMotion = window.matchMedia(
      "(prefers-reduced-motion: reduce)",
    ).matches;
    const duration = motionDurationMs(rootElement.current);
    const easing = rootElement.current
      ? getComputedStyle(rootElement.current)
          .getPropertyValue("--ien-motion-standard-easing")
          .trim() || "ease"
      : "ease";
    const enterOffset = rootElement.current
      ? getComputedStyle(rootElement.current)
          .getPropertyValue("--ien-spacing-xs")
          .trim() || "0px"
      : "0px";
    const nextRects = new Map<React.Key, DOMRect>();
    for (const [key, element] of elements.current) {
      const rect = element.getBoundingClientRect();
      const previous = previousRects.current.get(key);
      if (!reducedMotion) {
        if (previous) {
          const dx = previous.left - rect.left;
          const dy = previous.top - rect.top;
          if (dx || dy)
            element.animate(
              [
                { transform: `translate(${dx}px, ${dy}px)` },
                { transform: "translate(0, 0)" },
              ],
              { duration, easing },
            );
        } else {
          element.animate(
            [
              { opacity: 0, transform: `translateY(${enterOffset})` },
              { opacity: 1, transform: "translateY(0)" },
            ],
            { duration, easing },
          );
        }
      }
      nextRects.set(key, rect);
    }
    previousRects.current = nextRects;
  });

  return (
    <div
      ref={setRootRef}
      {...props}
      className={cn(
        "flex motion-reduce:[&>*]:transition-none",
        direction === "column" ? "flex-col" : "flex-row",
        className,
      )}
      style={{ gap, alignItems: align, ...style }}
    >
      {entries.map(({ item, key, visible }) => (
        <div
          key={key}
          ref={(element) => {
            if (element) elements.current.set(key, element);
            else elements.current.delete(key);
          }}
          className={cn(
            "min-w-0 transition-[opacity,transform] duration-[var(--ien-motion-normal)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
            visible
              ? "translate-y-0 opacity-100"
              : "pointer-events-none -translate-y-1 opacity-0",
          )}
          aria-hidden={!visible}
          inert={!visible}
        >
          {renderItem(item)}
        </div>
      ))}
    </div>
  );
}

export function IenAnimatedColumn<T>(props: IenAnimatedItemsProps<T>) {
  return <AnimatedItems {...props} direction="column" />;
}

export function IenAnimatedRow<T>(props: IenAnimatedItemsProps<T>) {
  return <AnimatedItems {...props} direction="row" />;
}

export { IenAnimatedColumn as AnimatedColumn, IenAnimatedRow as AnimatedRow };
