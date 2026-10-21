"use client";

import * as React from "react";
import { X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "cn";

export type SnackbarProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "role" | "aria-live" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open?: boolean;
  defaultOpen?: boolean;
  onOpenChange?: (open: boolean) => void;
  message: React.ReactNode;
  action?: { label: string; onClick: () => void };
  duration?: number;
  tone?: "neutral" | "success" | "warning" | "danger" | "info";
  className?: string;
};

function Snackbar({
  open,
  defaultOpen = false,
  onOpenChange,
  message,
  action,
  duration = 5000,
  tone = "neutral",
  className,
  ref,
  ...rootProps
}: SnackbarProps) {
  const [internalOpen, setInternalOpen] = React.useState(defaultOpen);
  const isOpen = open ?? internalOpen;
  const setOpen = React.useCallback(
    (next: boolean) => {
      if (open === undefined) setInternalOpen(next);
      onOpenChange?.(next);
    },
    [onOpenChange, open],
  );
  React.useEffect(() => {
    if (!isOpen || duration <= 0) return;
    const timer = window.setTimeout(() => setOpen(false), duration);
    return () => window.clearTimeout(timer);
  }, [isOpen, duration, setOpen]);
  if (!isOpen) return null;
  return (
    <div
      {...rootProps}
      ref={ref}
      role="status"
      aria-live="polite"
      data-slot="snackbar"
      className={cn(
        "fixed inset-x-4 bottom-4 z-50 mx-auto flex max-w-xl items-center gap-ien-md rounded-ien-default border border-ien-border bg-ien-surface-raised p-ien-md text-ien-body2 text-ien-text-primary shadow-[0_var(--ien-elevation-floating)_calc(var(--ien-elevation-floating)*2)_rgb(0_0_0/0.16)] sm:inset-x-auto sm:right-6",
        className,
      )}
    >
      <span className="min-w-0 flex-1">{message}</span>
      {action && (
        <Button
          type="button"
          variant="ghost"
          tone={tone === "neutral" ? "brand" : tone}
          size="sm"
          onClick={() => {
            action.onClick();
            setOpen(false);
          }}
        >
          {action.label}
        </Button>
      )}
      <Button
        type="button"
        variant="ghost"
        tone="neutral"
        size="icon-xs"
        aria-label="알림 닫기"
        onClick={() => setOpen(false)}
        className="shrink-0"
      >
        <X aria-hidden="true" />
      </Button>
    </div>
  );
}

export { Snackbar, Snackbar as IenSnackbar };
