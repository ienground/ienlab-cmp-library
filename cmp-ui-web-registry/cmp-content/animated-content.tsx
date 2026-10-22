"use client";

import * as React from "react";
import { cn } from "@/lib/ien-utils";

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
  const animationRef = React.useRef<Animation | null>(null);
  const setContentRef = React.useCallback(
    (element: HTMLDivElement | null) => {
      contentRef.current = element;
      if (typeof forwardedRef === "function") return forwardedRef(element);
      if (forwardedRef) forwardedRef.current = element;
    },
    [forwardedRef],
  );
  React.useLayoutEffect(() => {
    const element = contentRef.current;
    const previous = animationRef.current;
    const interrupted =
      previous?.playState === "running" || previous?.playState === "paused";
    const current = element ? getComputedStyle(element) : null;
    const opacity = current?.opacity;
    const transform = current?.transform;
    previous?.cancel();
    animationRef.current = null;
    if (
      !element ||
      window.matchMedia("(prefers-reduced-motion: reduce)").matches
    )
      return;
    const base = getComputedStyle(element);
    const baseTransform = base.transform === "none" ? "" : base.transform;
    const fromTransform =
      transition === "fade-up"
        ? "translateY(var(--ien-spacing-xs))"
        : transition === "fade-down"
          ? "translateY(calc(var(--ien-spacing-xs) * -1))"
          : transition === "fade-scale"
            ? "scale(var(--ien-button-pressed-scale))"
            : "";
    const easing =
      getComputedStyle(element)
        .getPropertyValue("--ien-motion-standard-easing")
        .trim() || "ease";
    const animation = element.animate(
      [
        {
          opacity: interrupted ? opacity : 0,
          transform: interrupted
            ? transform
            : `${baseTransform} ${fromTransform}`.trim() || "none",
        },
        { opacity: base.opacity, transform: base.transform },
      ],
      { duration: Math.max(0, duration ?? motionDurationMs(element)), easing },
    );
    animationRef.current = animation;
    animation.onfinish = () => {
      if (animationRef.current === animation) animationRef.current = null;
    };
  }, [targetState, transition, duration]);
  React.useLayoutEffect(() => () => animationRef.current?.cancel(), []);
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
