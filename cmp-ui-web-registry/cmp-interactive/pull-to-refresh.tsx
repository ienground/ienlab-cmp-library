"use client";

import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type PullToRefreshState = {
  distanceFraction?: number;
  onDistanceFractionChange?: (fraction: number) => void;
};
export type PullToRefreshIndicatorState = {
  distanceFraction: number;
  pullDistance: number;
  refreshing: boolean;
  ready: boolean;
};
export type PullToRefreshContentPadding =
  | number
  | string
  | Pick<
      React.CSSProperties,
      "padding" | "paddingInline" | "paddingBlock" | "paddingTop" | "paddingRight" | "paddingBottom" | "paddingLeft"
    >;
export type PullToRefreshContentAlignment = React.CSSProperties["placeItems"];

export type PullToRefreshProps = Omit<
  React.ComponentProps<"div">,
  | "children"
  | "ref"
  | "className"
  | "onPointerDown"
  | "onPointerMove"
  | "onPointerUp"
  | "onPointerCancel"
> & {
  ref?: React.Ref<HTMLDivElement>;
  contentRef?: React.Ref<HTMLDivElement>;
  onRefresh: () => void | Promise<void>;
  refreshing?: boolean;
  state?: PullToRefreshState;
  enabled?: boolean;
  children: React.ReactNode;
  threshold?: number;
  maxDistance?: number;
  topPadding?: number | string;
  contentPadding?: PullToRefreshContentPadding;
  contentAlignment?: PullToRefreshContentAlignment;
  indicator?: React.ReactNode | ((state: PullToRefreshIndicatorState) => React.ReactNode);
  indicatorContainerColor?: string;
  indicatorColor?: string;
  className?: string;
  contentClassName?: string;
  label?: string;
  onRefreshError?: (error: unknown) => void;
  onPointerDown?: React.PointerEventHandler<HTMLDivElement>;
  onPointerMove?: React.PointerEventHandler<HTMLDivElement>;
  onPointerUp?: React.PointerEventHandler<HTMLDivElement>;
  onPointerCancel?: React.PointerEventHandler<HTMLDivElement>;
};

function PullToRefresh({
  onRefresh,
  refreshing,
  state,
  enabled = true,
  children,
  threshold = 56,
  maxDistance,
  topPadding,
  contentPadding,
  contentAlignment = "start",
  indicator,
  indicatorContainerColor,
  indicatorColor,
  className,
  contentClassName,
  label = "당겨서 새로고침",
  onRefreshError,
  ref,
  contentRef: userContentRef,
  onPointerDown,
  onPointerMove,
  onPointerUp,
  onPointerCancel,
  ...rootProps
}: PullToRefreshProps) {
  const [internalRefreshing, setInternalRefreshing] = React.useState(false);
  const [pullDistance, setPullDistance] = React.useState(0);
  const [refreshError, setRefreshError] = React.useState<string | undefined>(
    undefined,
  );
  const startY = React.useRef<number | null>(null);
  const pullDistanceRef = React.useRef(0);
  const contentRef = React.useRef<HTMLDivElement>(null);
  const setContentRef = React.useCallback<React.RefCallback<HTMLDivElement>>(
    (element) => {
      contentRef.current = element;
      if (typeof userContentRef === "function") {
        const cleanup = userContentRef(element);
        return () => {
          contentRef.current = null;
          if (typeof cleanup === "function") cleanup();
          else userContentRef(null);
        };
      }
      if (userContentRef) userContentRef.current = element;
      return () => {
        contentRef.current = null;
        if (userContentRef) userContentRef.current = null;
      };
    },
    [userContentRef],
  );
  const busy = refreshing ?? internalRefreshing;
  const indicatorDistance = maxDistance ?? threshold;
  if (!Number.isFinite(threshold) || !Number.isFinite(indicatorDistance) || threshold <= 0 || indicatorDistance <= 0) {
    throw new Error("threshold와 maxDistance는 0보다 커야 합니다.");
  }
  const controlledFraction = state?.distanceFraction;
  const safeControlledFraction =
    typeof controlledFraction === "number" && Number.isFinite(controlledFraction)
    ? Math.max(0, controlledFraction)
    : undefined;
  const fraction = Math.max(
    0,
    Math.min(
      1,
      safeControlledFraction !== undefined
        ? safeControlledFraction
        : pullDistance / indicatorDistance,
    ),
  );
  const visualPullDistance = Math.min(
    Math.max(96, threshold),
    safeControlledFraction === undefined
      ? pullDistance
      : safeControlledFraction * indicatorDistance,
  );
  const indicatorState = {
    distanceFraction: fraction,
    pullDistance: visualPullDistance,
    refreshing: busy,
    ready: busy || fraction >= 1,
  };
  const resolvedTopPadding =
    topPadding ??
    (contentPadding && typeof contentPadding === "object"
      ? contentPadding.paddingTop ??
        contentPadding.paddingBlock ??
        contentPadding.padding
      : contentPadding);
  const contentPaddingStyle: Pick<React.CSSProperties, "padding" | "paddingInline" | "paddingBlock" | "paddingTop" | "paddingRight" | "paddingBottom" | "paddingLeft"> =
    typeof contentPadding === "object" && contentPadding !== null
      ? contentPadding
      : { padding: contentPadding };
  const topPaddingCss =
    typeof resolvedTopPadding === "number"
      ? `${resolvedTopPadding}px`
      : String(resolvedTopPadding ?? "0px");
  const finish = React.useCallback(async () => {
    if (busy || !enabled) return;
    setRefreshError(undefined);
    if (refreshing === undefined) setInternalRefreshing(true);
    try {
      await onRefresh();
    } catch (error) {
      setRefreshError(
        error instanceof Error && error.message
          ? error.message
          : "새로고침 중 오류가 발생했습니다.",
      );
      onRefreshError?.(error);
    } finally {
      if (refreshing === undefined) setInternalRefreshing(false);
    }
  }, [busy, enabled, onRefresh, onRefreshError, refreshing]);

  const updateDistance = (distance: number) => {
    const nextDistance = Math.max(0, Math.min(Math.max(96, threshold), distance));
    pullDistanceRef.current = nextDistance;
    setPullDistance(nextDistance);
    state?.onDistanceFractionChange?.(
      Math.max(0, Math.min(1, nextDistance / indicatorDistance)),
    );
  };

  const pullIndicator =
    typeof indicator === "function" ? indicator(indicatorState) : indicator;
  const restingTop = `calc(${topPaddingCss} + 8px)`;
  const pullTop = `calc(${fraction} * (${indicatorDistance}px + ${topPaddingCss}) - 28px)`;
  const indicatorProgress = busy ? 1 : fraction;

  return (
    <div
      {...rootProps}
      ref={ref}
      data-slot="pull-to-refresh"
      className={cn("relative min-h-0 overflow-hidden", className)}
      aria-busy={busy || undefined}
      aria-label={rootProps["aria-label"] ?? label}
      role={rootProps.role ?? "region"}
      onPointerDown={(event) => {
        onPointerDown?.(event);
        if (event.defaultPrevented) return;
        if (
          enabled &&
          !busy &&
          contentRef.current?.scrollTop === 0 &&
          event.pointerType !== "mouse"
        ) {
          startY.current = event.clientY;
          event.currentTarget.setPointerCapture(event.pointerId);
        }
      }}
      onPointerMove={(event) => {
        onPointerMove?.(event);
        if (event.defaultPrevented) return;
        if (startY.current === null || busy || !enabled) return;
        updateDistance(event.clientY - startY.current);
      }}
      onPointerUp={(event) => {
        onPointerUp?.(event);
        const shouldRefresh =
          !event.defaultPrevented &&
          Math.max(pullDistanceRef.current, visualPullDistance) >= threshold;
        startY.current = null;
        updateDistance(0);
        if (shouldRefresh) void finish();
      }}
      onPointerCancel={(event) => {
        onPointerCancel?.(event);
        startY.current = null;
        updateDistance(0);
      }}
    >
      <button
        type="button"
        className="sr-only focus:not-sr-only focus:static focus:h-auto focus:w-auto"
        disabled={!enabled || busy}
        onClick={() => void finish()}
      >
        {label}
      </button>
      {visualPullDistance > 0 || busy ? (
        <div
          role="status"
          aria-live="polite"
          className="pointer-events-none absolute inset-x-0 top-0 z-10 flex justify-center overflow-visible"
          style={{ height: `${Math.max(visualPullDistance, busy ? 44 : 0)}px` }}
        >
          <span className="sr-only">
            {busy
              ? "새로고침 중입니다."
              : fraction >= 1
                ? "손을 놓으면 새로고침합니다."
                : "새로고침하려면 아래로 당기세요."}
          </span>
          {pullIndicator ?? (
            <div
              aria-hidden="true"
              className={cn(
                "grid size-9 place-items-center rounded-ien-full border border-ien-border transition-colors duration-[var(--ien-motion-normal)] shadow-[0_var(--ien-elevation-raised)_calc(var(--ien-elevation-raised)*2)_rgb(0_0_0/0.12)] motion-reduce:transition-none",
                fraction >= 1 && "bg-ien-brand-weak",
                fraction < 1 && "bg-ien-surface",
              )}
              style={{
                width: `${36 + 36 * indicatorProgress}px`,
                backgroundColor: indicatorContainerColor,
                color: indicatorColor,
                transform: `translateY(${busy ? restingTop : pullTop})`,
              }}
            >
              <span className="flex w-full items-center justify-center gap-2" aria-hidden="true">
                {[0, 1, 2].map((dot) => (
                  <span
                    key={dot}
                    className={cn(
                      "size-2 shrink-0 rounded-full bg-current",
                      busy && "animate-bounce motion-reduce:animate-none",
                    )}
                    style={{
                      opacity: busy || dot === 1 ? 1 : fraction,
                      scale: busy ? undefined : fraction,
                      animationDelay: busy ? `${dot * 120}ms` : undefined,
                    }}
                  />
                ))}
              </span>
            </div>
          )}
        </div>
      ) : null}
      <div
        ref={setContentRef}
        className={cn(
          "h-full min-h-0 overflow-auto overscroll-contain motion-reduce:transition-none",
          startY.current === null
            ? "transition-transform duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)]"
            : "transition-none",
          contentClassName,
        )}
        style={{
          ...contentPaddingStyle,
          display: "grid",
          placeItems: contentAlignment,
          transform: `translateY(${busy ? 44 : Math.min(visualPullDistance, Math.max(96, threshold))}px)`,
          willChange: pullDistance > 0 || busy ? "transform" : undefined,
        }}
      >
        {children}
      </div>
      {refreshError && (
        <p
          role="alert"
          aria-live="assertive"
          className="absolute inset-x-3 bottom-3 rounded-ien-default bg-ien-danger-weak p-ien-sm text-ien-body2 text-ien-danger"
        >
          {refreshError}
        </p>
      )}
    </div>
  );
}

export { PullToRefresh, PullToRefresh as IenPullToRefresh };
