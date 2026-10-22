"use client";

import * as React from "react";
import { Button, type ButtonProps } from "@/components/ui/button";
import { cn } from "@/lib/ien-utils";

export type SwipeBoxState = {
  enabled?: boolean;
  loading?: boolean;
};

export type SwipeBoxAction = {
  key?: React.Key;
  label?: React.ReactNode;
  icon?: React.ReactNode;
  onClick: () => void;
  variant?: "fill" | "weak" | "line" | "ghost";
  tone?: "neutral" | "brand" | "success" | "warning" | "danger" | "info";
  colors?: ButtonProps["colors"];
  state?: SwipeBoxState;
  shape?: React.CSSProperties["borderRadius"];
  restoreOnClick?: boolean;
  onClickLabel?: string;
  weight?: number;
  showLabelOnExpansion?: boolean;
  disabled?: boolean;
};
export type SwipeBoxProps = Omit<
  React.ComponentProps<"div">,
  | "children"
  | "ref"
  | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  children: React.ReactNode;
  startActions?: SwipeBoxAction[];
  endActions?: SwipeBoxAction[];
  startAction?: SwipeBoxAction;
  endAction?: SwipeBoxAction;
  threshold?: number;
  itemWidth?: number;
  height?: number | string;
  startToEndFullSwipeEnabled?: boolean;
  endToStartFullSwipeEnabled?: boolean;
  expansionHapticEnabled?: boolean;
  openDirection?: "start" | "end" | null;
  onOpenDirectionChange?: (direction: "start" | "end" | null) => void;
  className?: string;
};

function SwipeBox({
  children,
  startActions,
  endActions,
  startAction,
  endAction,
  threshold,
  itemWidth = 60,
  height,
  startToEndFullSwipeEnabled = true,
  endToStartFullSwipeEnabled = true,
  expansionHapticEnabled = true,
  openDirection,
  onOpenDirectionChange,
  className,
  ref,
  style,
  onKeyDown,
  onPointerDown,
  onPointerMove,
  onPointerUp,
  onPointerCancel,
  ...rootProps
}: SwipeBoxProps) {
  const [offset, setOffset] = React.useState(0);
  const [parentWidth, setParentWidth] = React.useState(0);
  const [internalOpenDirection, setInternalOpenDirection] = React.useState<
    "start" | "end" | null
  >(null);
  const origin = React.useRef<{
    x: number;
    y: number;
    offset: number;
    pointerId: number;
    dragging: boolean;
  } | null>(null);
  const offsetRef = React.useRef(0);
  const rootRef = React.useRef<HTMLDivElement>(null);
  const controlled = openDirection !== undefined;
  const currentOpenDirection = controlled ? openDirection : internalOpenDirection;
  const resolvedStartActions = startActions ?? (startAction ? [startAction] : []);
  const resolvedEndActions = endActions ?? (endAction ? [endAction] : []);
  const actionWidth = (actions: SwipeBoxAction[]) =>
    actions.reduce((total, action) => total + itemWidth * (action.weight ?? 1), 0) +
    (actions.length > 0 ? 8 : 0);
  const startRevealWidth = actionWidth(resolvedStartActions);
  const endRevealWidth = actionWidth(resolvedEndActions);
  const updateOffset = (nextOffset: number) => {
    offsetRef.current = nextOffset;
    setOffset(nextOffset);
  };
  const setDirection = (direction: "start" | "end" | null) => {
    if (!controlled) setInternalOpenDirection(direction);
    onOpenDirectionChange?.(direction);
    updateOffset(
      direction === "start"
        ? startRevealWidth
        : direction === "end"
          ? -endRevealWidth
          : 0,
    );
  };
  const fullSwipeThreshold = (count: number) =>
    parentWidth > 0 ? parentWidth * (count >= 2 ? 0.85 : 0.5) : Infinity;
  const snapThreshold = (revealWidth: number) => threshold ?? revealWidth / 2;

  React.useImperativeHandle(ref, () => rootRef.current!, []);
  React.useLayoutEffect(() => {
    const root = rootRef.current;
    if (!root) return;
    const resizeObserver = new ResizeObserver(() => setParentWidth(root.clientWidth));
    resizeObserver.observe(root);
    setParentWidth(root.clientWidth);
    return () => resizeObserver.disconnect();
  }, []);
  React.useLayoutEffect(() => {
    if (currentOpenDirection === "start") updateOffset(startRevealWidth);
    else if (currentOpenDirection === "end") updateOffset(-endRevealWidth);
    else updateOffset(0);
    // Action arrays are described by their total width; a render-time array identity change
    // must not interrupt a pointer gesture.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentOpenDirection, startRevealWidth, endRevealWidth]);

  const triggerHapticIfNeeded = (nextOffset: number) => {
    if (!expansionHapticEnabled) return;
    const reachedStart = nextOffset > startRevealWidth;
    const reachedEnd = -nextOffset > endRevealWidth;
    const wasReached =
      offsetRef.current > startRevealWidth || offsetRef.current < -endRevealWidth;
    if (
      (reachedStart || reachedEnd) &&
      !wasReached &&
      typeof navigator !== "undefined" &&
      "vibrate" in navigator
    ) {
      navigator.vibrate(8);
    }
  };

  const revealAction = (action: SwipeBoxAction) => {
    if (action.disabled || action.state?.enabled === false || action.state?.loading) {
      return false;
    }
    action.onClick();
    if (action.restoreOnClick !== false) setDirection(null);
    return true;
  };

  const renderActions = (
    actions: SwipeBoxAction[],
    direction: "start" | "end",
    revealWidth: number,
    visible: boolean,
  ) => {
    if (!actions.length) return null;
    const leadingIndex = direction === "start" ? 0 : actions.length - 1;
    const fullSwipeLimit = fullSwipeThreshold(actions.length);
    const labelsVisible =
      visible &&
      (Math.abs(offset) > revealWidth + 24 || Math.abs(offset) >= fullSwipeLimit);
    const fullSwipeEnabled =
      direction === "start"
        ? startToEndFullSwipeEnabled
        : endToStartFullSwipeEnabled;
    const fullSwipe =
      visible && fullSwipeEnabled && Math.abs(offset) >= fullSwipeLimit;
    const fullSwipeIndex = direction === "start" ? 0 : actions.length - 1;
    const otherActionWidth = actions.reduce(
      (total, action, index) =>
        index === fullSwipeIndex
          ? total
          : total + itemWidth * (action.weight ?? 1),
      0,
    );
    const expandedActionWidth = Math.max(0, Math.abs(offset) - 8 - otherActionWidth);
    return (
      <div
        aria-hidden={!visible}
        className={cn(
          "absolute inset-y-0 overflow-hidden",
          direction === "start" ? "left-0" : "right-0",
        )}
        style={{
          width: Math.abs(offset),
          minHeight: height ?? 72,
        }}
      >
        <div
          className={cn(
            "absolute inset-y-0 flex items-center px-1",
            direction === "start" ? "left-0 justify-start" : "right-0 justify-end",
          )}
          style={{
            width: fullSwipe ? Math.abs(offset) : revealWidth,
            minHeight: height ?? 72,
          }}
        >
          {actions.map((action, index) => {
            const directionProgress = Math.max(
              0,
              (direction === "start" ? offset : -offset) - 8,
            );
            const revealOrder =
              direction === "start" ? index : actions.length - 1 - index;
            const scale = Math.max(
              0,
              Math.min(1, (directionProgress - revealOrder * itemWidth) / itemWidth),
            );
            const isFullSwipeAction = index === fullSwipeIndex;
            const actionCellWidth =
              fullSwipe && isFullSwipeAction
                ? expandedActionWidth
                : itemWidth * (action.weight ?? 1);
            const actionState = {
              enabled: action.state?.enabled ?? !action.disabled,
              loading: action.state?.loading ?? false,
            };
            const labelVisible =
              index === leadingIndex &&
              labelsVisible &&
              action.showLabelOnExpansion !== false;
            const accessibleLabel =
              action.onClickLabel ??
              (typeof action.label === "string" ? action.label : "스와이프 동작");

            return (
              <div
                key={action.key ?? `${direction}-${index}`}
                className="flex h-full shrink-0 items-center justify-center px-1 py-[10px]"
                style={{ width: actionCellWidth }}
              >
                <Button
                  type="button"
                  variant={action.variant ?? "fill"}
                  tone={action.tone ?? "brand"}
                  size="icon-lg"
                  colors={action.colors}
                  state={actionState}
                  disabled={action.disabled || !actionState.enabled}
                  aria-label={accessibleLabel}
                  tabIndex={visible ? 0 : -1}
                  onClick={() => revealAction(action)}
                  className={cn(
                    "h-[52px] shrink-0 rounded-full transition-[transform,opacity] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
                    fullSwipe && isFullSwipeAction ? "w-full" : "w-[52px]",
                  )}
                  contentPadding={{ paddingInline: "var(--ien-spacing-sm)" }}
                  shape={action.shape ?? "9999px"}
                  style={{
                    transform: `scale(${scale})`,
                    transformOrigin: direction === "start" ? "left center" : "right center",
                    opacity: fullSwipe && !isFullSwipeAction ? 0.35 : scale,
                    transition: origin.current?.dragging ? "none" : undefined,
                  }}
                >
                  {action.icon && <span aria-hidden="true">{action.icon}</span>}
                  {labelVisible && <span className="text-ien-label2">{action.label}</span>}
                </Button>
              </div>
            );
          })}
        </div>
      </div>
    );
  };

  return (
    <div
      {...rootProps}
      ref={rootRef}
      data-slot="swipe-box"
      role="group"
      aria-label={rootProps["aria-label"] ?? "스와이프 동작"}
      className={cn(
        "relative overflow-hidden",
        className,
      )}
      style={style}
      onPointerDown={(event) => {
        onPointerDown?.(event);
        if (event.defaultPrevented || !event.isPrimary || event.button !== 0) return;
        if (
          !(event.target instanceof Element) ||
          !event.target.closest('[data-swipe-box-content="true"]')
        ) {
          return;
        }
        origin.current = {
          x: event.clientX,
          y: event.clientY,
          offset: offsetRef.current,
          pointerId: event.pointerId,
          dragging: false,
        };
      }}
      onPointerMove={(event) => {
        onPointerMove?.(event);
        const start = origin.current;
        if (!start || start.pointerId !== event.pointerId) return;
        const dx = event.clientX - start.x;
        const dy = event.clientY - start.y;
        if (
          !start.dragging &&
          Math.abs(dy) >= 8 &&
          Math.abs(dy) > Math.abs(dx)
        ) {
          origin.current = null;
          if (event.currentTarget.hasPointerCapture(event.pointerId)) {
            event.currentTarget.releasePointerCapture(event.pointerId);
          }
          return;
        }
        if (
          !start.dragging &&
          (Math.abs(dx) < 8 || Math.abs(dx) <= Math.abs(dy))
        ) {
          return;
        }
        start.dragging = true;
        if (!event.currentTarget.hasPointerCapture(event.pointerId)) {
          event.currentTarget.setPointerCapture(event.pointerId);
        }
        if (event.cancelable) event.preventDefault();
        const startFullSwipe =
          startToEndFullSwipeEnabled && resolvedStartActions.length > 0
            ? Math.max(startRevealWidth, fullSwipeThreshold(resolvedStartActions.length))
            : startRevealWidth;
        const endFullSwipe =
          endToStartFullSwipeEnabled && resolvedEndActions.length > 0
            ? Math.max(endRevealWidth, fullSwipeThreshold(resolvedEndActions.length))
            : endRevealWidth;
        const nextOffset = Math.max(
          -endFullSwipe,
          Math.min(startFullSwipe, start.offset + dx),
        );
        triggerHapticIfNeeded(nextOffset);
        updateOffset(nextOffset);
      }}
      onPointerUp={(event) => {
        onPointerUp?.(event);
        const start = origin.current;
        if (!start || start.pointerId !== event.pointerId) return;
        origin.current = null;
        if (event.currentTarget.hasPointerCapture(event.pointerId)) {
          event.currentTarget.releasePointerCapture(event.pointerId);
        }
        if (!start.dragging) return;
        const currentOffset = offsetRef.current;
        const startLimit = fullSwipeThreshold(resolvedStartActions.length);
        const endLimit = fullSwipeThreshold(resolvedEndActions.length);
        const finalEndAction = resolvedEndActions[resolvedEndActions.length - 1];
        if (
          currentOffset > 0 &&
          startToEndFullSwipeEnabled &&
          resolvedStartActions[0] &&
          Math.abs(currentOffset) >= startLimit
        ) {
          if (revealAction(resolvedStartActions[0])) {
            return;
          }
        }
        if (
          currentOffset < 0 &&
          endToStartFullSwipeEnabled &&
          finalEndAction &&
          Math.abs(currentOffset) >= endLimit
        ) {
          if (revealAction(finalEndAction)) {
            return;
          }
        }
        if (currentOffset > snapThreshold(startRevealWidth) && startRevealWidth > 0) {
          setDirection("start");
        } else if (
          currentOffset < -snapThreshold(endRevealWidth) &&
          endRevealWidth > 0
        ) {
          setDirection("end");
        } else {
          setDirection(null);
        }
      }}
      onPointerCancel={(event) => {
        onPointerCancel?.(event);
        const start = origin.current;
        if (!start || start.pointerId !== event.pointerId) return;
        origin.current = null;
        if (event.currentTarget.hasPointerCapture(event.pointerId)) {
          event.currentTarget.releasePointerCapture(event.pointerId);
        }
        updateOffset(start.offset);
      }}
      onKeyDown={(event) => {
        onKeyDown?.(event);
        if (event.defaultPrevented) return;
        if (
          !(event.target instanceof HTMLElement) ||
          !event.target.closest('[data-swipe-box-content="true"]')
        ) {
          return;
        }
        if (event.key === "Escape") {
          event.preventDefault();
          setDirection(null);
        } else if (event.key === "ArrowRight" && resolvedStartActions.length > 0) {
          event.preventDefault();
          setDirection("start");
        } else if (event.key === "ArrowLeft" && resolvedEndActions.length > 0) {
          event.preventDefault();
          setDirection("end");
        }
      }}
    >
      {renderActions(resolvedStartActions, "start", startRevealWidth, offset > 0)}
      {renderActions(resolvedEndActions, "end", endRevealWidth, offset < 0)}
      <div
        className={cn(
          "relative z-10 touch-pan-y transition-[transform,border-radius,background-color] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
          offset === 0 ? "rounded-none bg-ien-surface" : "rounded-ien-lg bg-ien-surface-variant",
        )}
        style={{
          transform: `translateX(${offset}px)`,
          transition: origin.current?.dragging
            ? "none"
            : undefined,
          height,
          minHeight:
            resolvedStartActions.length > 0 || resolvedEndActions.length > 0
              ? (height ?? 72)
              : undefined,
        }}
        tabIndex={0}
        data-swipe-box-content="true"
        aria-label="스와이프하여 보조 동작 표시"
      >
        {children}
      </div>
    </div>
  );
}

export { SwipeBox, SwipeBox as IenSwipeBox };
