"use client";

import * as React from "react";
import { X } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Toast,
  ToastAction,
  ToastClose,
  ToastDescription,
  ToastProvider,
  ToastViewport,
} from "@/components/ui/toast";
import { cn } from "@/lib/ien-utils";

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
  return (
    <ToastProvider duration={duration > 0 ? duration : Infinity}>
      <Toast
        open={open}
        defaultOpen={defaultOpen}
        onOpenChange={onOpenChange}
        tone={tone}
        className={cn("max-w-xl items-center", className)}
        asChild
      >
        <div {...rootProps} ref={ref} data-slot="snackbar">
          <ToastDescription asChild>
            <span className="mt-0 min-w-0 flex-1 opacity-100">{message}</span>
          </ToastDescription>
          {action && (
            <ToastAction altText={action.label} asChild>
              <Button
                type="button"
                variant="ghost"
                tone={tone === "neutral" ? "brand" : tone}
                size="sm"
                onClick={action.onClick}
              >
                {action.label}
              </Button>
            </ToastAction>
          )}
          <ToastClose className="opacity-100" asChild>
            <Button
              type="button"
              variant="ghost"
              tone="neutral"
              size="icon-xs"
              aria-label="알림 닫기"
              className="shrink-0"
            >
              <X aria-hidden="true" />
            </Button>
          </ToastClose>
        </div>
      </Toast>
      <ToastViewport
        className="inset-x-4 bottom-4 mx-auto w-auto max-w-xl sm:inset-x-auto sm:right-6"
        asChild
      >
        <div />
      </ToastViewport>
    </ToastProvider>
  );
}

export { Snackbar, Snackbar as IenSnackbar };
export {
  IenToast,
  IenToastHost,
  IenToastProvider,
  useIenToast,
  useIenToastState,
} from "@/components/ui/toast";
export type {
  IenToastData,
  IenToastDuration,
  IenToastState,
  IenToastTone,
} from "@/components/ui/toast";
