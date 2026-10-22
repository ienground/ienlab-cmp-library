"use client";

import * as React from "react";
import { cn } from "@/lib/ien-utils";

export interface IenAnimatedItemsProps<T> extends Omit<
  React.ComponentPropsWithRef<"div">,
  "children"
> {
  items: readonly T[];
  itemKey: (item: T) => React.Key;
  renderItem: (item: T) => React.ReactNode;
  gap?: number | string;
  align?: "start" | "center" | "end" | "stretch";
  verticalArrangement?: React.CSSProperties["justifyContent"];
  horizontalAlignment?: React.CSSProperties["alignItems"];
  horizontalArrangement?: React.CSSProperties["justifyContent"];
  verticalAlignment?: React.CSSProperties["alignItems"];
}

function motionDurationMs(element: Element): number {
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

type ItemPosition = {
  left: number;
  top: number;
  width: number;
  height: number;
};
type ItemSnapshot = ItemPosition & { opacity: string };
type AnimatedEntry<T> = {
  item: T;
  key: React.Key;
  visible: boolean;
  exitPosition?: ItemPosition;
};

function AnimatedItems<T>({
  items,
  itemKey,
  renderItem,
  direction,
  gap = 0,
  align = "stretch",
  verticalArrangement,
  horizontalAlignment,
  horizontalArrangement,
  verticalAlignment,
  className,
  style,
  ref: forwardedRef,
  ...props
}: IenAnimatedItemsProps<T> & { direction: "column" | "row" }) {
  const [entries, setEntries] = React.useState<AnimatedEntry<T>[]>(() =>
    items.map((item) => ({ item, key: itemKey(item), visible: true })),
  );
  const rootElement = React.useRef<HTMLDivElement>(null);
  const elements = React.useRef(new Map<React.Key, HTMLDivElement>());
  const snapshots = React.useRef(new Map<React.Key, ItemSnapshot>());
  const animations = React.useRef(new Map<React.Key, Animation>());
  const rootAnimation = React.useRef<Animation | null>(null);
  const previousSize = React.useRef<{ width: number; height: number } | null>(
    null,
  );
  const pending = React.useRef(false);
  const setRootRef = React.useCallback(
    (element: HTMLDivElement | null) => {
      rootElement.current = element;
      if (typeof forwardedRef === "function") return forwardedRef(element);
      if (forwardedRef) forwardedRef.current = element;
    },
    [forwardedRef],
  );

  React.useLayoutEffect(() => {
    const root = rootElement.current;
    if (!root) return;
    const next: AnimatedEntry<T>[] = items.map((item) => ({
      item,
      key: itemKey(item),
      visible: true,
    }));
    const visibleEntries = entries.filter((entry) => entry.visible);
    if (
      next.length === visibleEntries.length &&
      next.every(
        (entry, index) =>
          entry.key === visibleEntries[index]?.key &&
          Object.is(entry.item, visibleEntries[index]?.item),
      )
    )
      return;

    const rootRect = root.getBoundingClientRect();
    const captured = new Map<React.Key, ItemSnapshot>();
    for (const [key, element] of elements.current) {
      const rect = element.getBoundingClientRect();
      captured.set(key, {
        left: rect.left - rootRect.left - root.clientLeft + root.scrollLeft,
        top: rect.top - rootRect.top - root.clientTop + root.scrollTop,
        width: rect.width,
        height: rect.height,
        opacity: getComputedStyle(element).opacity,
      });
    }
    for (const animation of animations.current.values()) animation.cancel();
    animations.current.clear();
    rootAnimation.current?.cancel();
    snapshots.current = captured;
    previousSize.current = { width: rootRect.width, height: rootRect.height };
    pending.current = true;
    const keys = new Set(next.map((entry) => entry.key));
    const reducedMotion = window.matchMedia(
      "(prefers-reduced-motion: reduce)",
    ).matches;
    setEntries([
      ...next,
      ...(reducedMotion
        ? []
        : entries
            .filter((entry) => !keys.has(entry.key))
            .map((entry) => ({
              ...entry,
              visible: false,
              exitPosition: captured.get(entry.key) ?? entry.exitPosition,
            }))),
    ]);
  }, [items, itemKey, entries]);

  React.useLayoutEffect(() => {
    const root = rootElement.current;
    if (!root || !pending.current) return;
    pending.current = false;
    const duration = motionDurationMs(root);
    const reducedMotion = window.matchMedia(
      "(prefers-reduced-motion: reduce)",
    ).matches;
    if (reducedMotion || duration <= 0) {
      setEntries((current) => current.filter((entry) => entry.visible));
      return;
    }
    const computed = getComputedStyle(root);
    const easing =
      computed.getPropertyValue("--ien-motion-standard-easing").trim() ||
      "ease";
    const offset =
      computed.getPropertyValue("--ien-spacing-xs").trim() || "0px";
    const rootRect = root.getBoundingClientRect();
    const frames = entries.flatMap((entry) => {
      const element = elements.current.get(entry.key);
      if (!element) return [];
      const rect = element.getBoundingClientRect();
      const previous = snapshots.current.get(entry.key);
      const dx = previous
        ? previous.left -
          (rect.left - rootRect.left - root.clientLeft + root.scrollLeft)
        : 0;
      const dy = previous
        ? previous.top -
          (rect.top - rootRect.top - root.clientTop + root.scrollTop)
        : 0;
      return [{ entry, element, previous, dx, dy }];
    });
    for (const { entry, element, previous, dx, dy } of frames) {
      if (entry.visible && previous && !dx && !dy && previous.opacity === "1")
        continue;
      const animation = element.animate(
        [
          {
            opacity: previous?.opacity ?? 0,
            transform: previous
              ? `translate(${dx}px, ${dy}px)`
              : `translateY(${offset})`,
          },
          {
            opacity: entry.visible ? 1 : 0,
            transform: entry.visible
              ? "translate(0, 0)"
              : `translateY(calc(${offset} * -1))`,
          },
        ],
        { duration, easing, fill: "both" },
      );
      animations.current.set(entry.key, animation);
      animation.onfinish = () => {
        if (animations.current.get(entry.key) !== animation) return;
        animations.current.delete(entry.key);
        if (!entry.visible) {
          setEntries((current) =>
            current.filter((item) => item.key !== entry.key || item.visible),
          );
        } else {
          animation.cancel();
        }
      };
    }
    const previous = previousSize.current;
    const dimension = direction === "column" ? "height" : "width";
    if (previous && Math.abs(previous[dimension] - rootRect[dimension]) > 0.5) {
      const contentSize =
        computed.boxSizing === "border-box"
          ? 0
          : direction === "column"
            ? Number.parseFloat(computed.paddingTop) +
              Number.parseFloat(computed.paddingBottom) +
              root.clientTop * 2
            : Number.parseFloat(computed.paddingLeft) +
              Number.parseFloat(computed.paddingRight) +
              root.clientLeft * 2;
      rootAnimation.current = root.animate(
        [
          {
            [dimension]: `${Math.max(0, previous[dimension] - contentSize)}px`,
          },
          {
            [dimension]: `${Math.max(0, rootRect[dimension] - contentSize)}px`,
          },
        ],
        { duration, easing },
      );
    }
  }, [entries, direction]);

  React.useLayoutEffect(
    () => () => {
      for (const animation of animations.current.values()) animation.cancel();
      rootAnimation.current?.cancel();
    },
    [],
  );

  return (
    <div
      ref={setRootRef}
      {...props}
      className={cn(
        "relative flex",
        direction === "column" ? "flex-col" : "flex-row",
        className,
      )}
      style={{ gap, alignItems: (direction === "column" ? horizontalAlignment : verticalAlignment) ?? align, justifyContent: direction === "column" ? verticalArrangement : horizontalArrangement, ...style }}
    >
      {entries.map(({ item, key, visible, exitPosition }) => (
        <div
          key={key}
          ref={(element) => {
            if (element) elements.current.set(key, element);
            else elements.current.delete(key);
          }}
          className="min-w-0"
          style={
            visible
              ? undefined
              : { position: "absolute", pointerEvents: "none", ...exitPosition }
          }
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
