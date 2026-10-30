"use client";

import * as React from "react";
import { LoaderCircle, RefreshCw } from "lucide-react";
import { cn } from "cn";

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
  children: React.ReactNode;
  threshold?: number;
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
  children,
  threshold = 64,
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
  React.useEffect(() => {
    const element = contentRef.current;
    if (!element) return;
    const handleTouchMove = (event: TouchEvent) => {
      if (busy || event.defaultPrevented || startY.current === null) return;
      if (event.touches.length !== 1) {
        startY.current = null;
        setPullDistance(0);
        return;
      }
      if (event.touches[0].clientY <= startY.current) {
        startY.current = null;
        setPullDistance(0);
        return;
      }
      if (event.cancelable) event.preventDefault();
    };
    element.addEventListener("touchmove", handleTouchMove, { passive: false });
    return () => element.removeEventListener("touchmove", handleTouchMove);
  }, [busy]);
  const finish = React.useCallback(async () => {
    if (busy) return;
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
  }, [busy, onRefresh, onRefreshError, refreshing]);

  return (
    <div
      {...rootProps}
      ref={ref}
      data-slot="pull-to-refresh"
      className={cn("relative min-h-0 overflow-hidden", className)}
      onPointerDown={(event) => {
        onPointerDown?.(event);
        if (event.defaultPrevented) return;
        if (
          !busy &&
          contentRef.current?.scrollTop === 0 &&
          event.pointerType !== "mouse"
        )
          startY.current = event.clientY;
      }}
      onPointerMove={(event) => {
        onPointerMove?.(event);
        if (event.defaultPrevented) return;
        if (startY.current === null || busy) return;
        const distance = Math.max(
          0,
          Math.min(threshold * 1.5, event.clientY - startY.current),
        );
        setPullDistance(distance);
      }}
      onPointerUp={(event) => {
        onPointerUp?.(event);
        if (event.defaultPrevented) return;
        startY.current = null;
        if (pullDistance >= threshold) void finish();
        setPullDistance(0);
      }}
      onPointerCancel={(event) => {
        onPointerCancel?.(event);
        if (!event.defaultPrevented) {
          startY.current = null;
          setPullDistance(0);
        }
      }}
    >
      <div
        role="status"
        aria-live="polite"
        className="pointer-events-none absolute inset-x-0 top-0 z-10 flex justify-center overflow-hidden"
        style={{ height: `${Math.max(pullDistance, busy ? 44 : 0)}px` }}
      >
        <span className="sr-only">
          {busy
            ? "새로고침 중입니다."
            : pullDistance >= threshold
              ? "손을 놓으면 새로고침합니다."
              : pullDistance > 0
                ? "새로고침하려면 아래로 당기세요."
                : ""}
        </span>
        <div
          aria-hidden="true"
          className="grid size-10 place-items-center rounded-ien-full bg-ien-surface text-ien-brand shadow-[0_var(--ien-elevation-raised)_calc(var(--ien-elevation-raised)*2)_rgb(0_0_0/0.12)]"
        >
          {busy ? (
            <LoaderCircle className="size-5 animate-spin" aria-hidden="true" />
          ) : (
            <RefreshCw
              className="size-5"
              style={{
                transform: `rotate(${Math.min(180, (pullDistance / threshold) * 180)}deg)`,
              }}
              aria-hidden="true"
            />
          )}
        </div>
      </div>
      <div
        ref={setContentRef}
        className={cn(
          "h-full min-h-0 overflow-auto overscroll-contain",
          contentClassName,
        )}
        style={{
          transform: `translateY(${busy ? 44 : pullDistance}px)`,
          transition:
            startY.current === null
              ? "transform var(--ien-motion-fast) var(--ien-motion-standard-easing)"
              : "none",
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
