"use client";

import * as React from "react";
import { cn } from "cn";

export type SwipeBoxAction = {
  label: string;
  onClick: () => void;
  tone?: "neutral" | "danger";
  disabled?: boolean;
};
export type SwipeBoxProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  children: React.ReactNode;
  startAction?: SwipeBoxAction;
  endAction?: SwipeBoxAction;
  threshold?: number;
  className?: string;
};

function SwipeBox({
  children,
  startAction,
  endAction,
  threshold = 72,
  className,
  ref,
  ...rootProps
}: SwipeBoxProps) {
  const [offset, setOffset] = React.useState(0);
  const origin = React.useRef<{ x: number; y: number } | null>(null);
  const startWidth = 88;
  const clamp = (x: number) =>
    Math.max(
      endAction ? -startWidth : 0,
      Math.min(startAction ? startWidth : 0, x),
    );
  return (
    <div
      {...rootProps}
      ref={ref}
      data-slot="swipe-box"
      className={cn(
        "relative overflow-hidden rounded-ien-default border border-ien-border bg-ien-surface",
        className,
      )}
    >
      {startAction && (
        <button
          type="button"
          tabIndex={offset > 0 ? 0 : -1}
          aria-hidden={offset <= 0}
          onClick={startAction.onClick}
          disabled={startAction.disabled}
          className={cn(
            "absolute inset-y-0 left-0 w-[88px] px-ien-sm text-ien-label2 text-ien-on-brand",
            startAction.tone === "danger" ? "bg-ien-danger" : "bg-ien-brand",
          )}
        >
          {startAction.label}
        </button>
      )}
      {endAction && (
        <button
          type="button"
          tabIndex={offset < 0 ? 0 : -1}
          aria-hidden={offset >= 0}
          onClick={endAction.onClick}
          disabled={endAction.disabled}
          className={cn(
            "absolute inset-y-0 right-0 w-[88px] px-ien-sm text-ien-label2 text-ien-on-brand",
            endAction.tone === "danger" ? "bg-ien-danger" : "bg-ien-brand",
          )}
        >
          {endAction.label}
        </button>
      )}
      <div
        className="relative z-10 touch-pan-y bg-ien-surface"
        style={{
          transform: `translateX(${offset}px)`,
          transition: origin.current
            ? "none"
            : "transform var(--ien-motion-fast) var(--ien-motion-standard-easing)",
        }}
        onPointerDown={(event) => {
          origin.current = { x: event.clientX - offset, y: event.clientY };
        }}
        onPointerMove={(event) => {
          if (!origin.current) return;
          const dx = event.clientX - origin.current.x;
          if (Math.abs(event.clientY - origin.current.y) < Math.abs(dx))
            setOffset(clamp(dx));
        }}
        onPointerUp={() => {
          if (!origin.current) return;
          origin.current = null;
          setOffset(
            Math.abs(offset) >= threshold ? Math.sign(offset) * startWidth : 0,
          );
        }}
        onPointerCancel={() => {
          origin.current = null;
          setOffset(0);
        }}
        onKeyDown={(event) => {
          if (event.key === "Escape") setOffset(0);
          if (event.key === "ArrowLeft" && endAction) setOffset(-startWidth);
          if (event.key === "ArrowRight" && startAction) setOffset(startWidth);
        }}
        tabIndex={0}
        aria-label="스와이프하여 보조 동작 표시"
      >
        {children}
      </div>
    </div>
  );
}

export { SwipeBox, SwipeBox as IenSwipeBox };
