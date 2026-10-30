"use client";

import * as React from "react";
import { cn } from "cn";

export interface IenAnimatedContentProps<T> extends Omit<
  React.ComponentPropsWithRef<"div">,
  "children"
> {
  targetState: T;
  children: React.ReactNode | ((state: T) => React.ReactNode);
  transition?: "fade" | "fade-up" | "fade-down" | "fade-scale";
  duration?: number;
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

export function IenAnimatedContent<T>({
  targetState,
  children,
  transition = "fade",
  duration,
  className,
  style,
  ref: forwardedRef,
  ...props
}: IenAnimatedContentProps<T>) {
  const content =
    typeof children === "function" ? children(targetState) : children;
  const contentRef = React.useRef<HTMLDivElement>(null);
  const setContentRef = React.useCallback(
    (element: HTMLDivElement | null) => {
      contentRef.current = element;
      if (typeof forwardedRef === "function") return forwardedRef(element);
      if (forwardedRef) forwardedRef.current = element;
    },
    [forwardedRef],
  );
  React.useEffect(() => {
    const element = contentRef.current;
    if (
      !element ||
      window.matchMedia("(prefers-reduced-motion: reduce)").matches
    )
      return;
    const fromTransform =
      transition === "fade-up"
        ? "translateY(var(--ien-spacing-xs))"
        : transition === "fade-down"
          ? "translateY(calc(var(--ien-spacing-xs) * -1))"
          : transition === "fade-scale"
            ? "scale(var(--ien-button-pressed-scale))"
            : "none";
    const easing =
      getComputedStyle(element)
        .getPropertyValue("--ien-motion-standard-easing")
        .trim() || "ease";
    const animation = element.animate(
      [
        { opacity: 0, transform: fromTransform },
        { opacity: 1, transform: "none" },
      ],
      { duration: duration ?? motionDurationMs(element), easing },
    );
    return () => animation.cancel();
  }, [targetState, transition, duration]);
  return (
    <div
      ref={setContentRef}
      className={cn("motion-reduce:animate-none", className)}
      style={style}
      {...props}
    >
      {content}
    </div>
  );
}

export { IenAnimatedContent as AnimatedContent };
