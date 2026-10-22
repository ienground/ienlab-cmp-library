"use client";

import * as React from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { IenCircularProgressIndicator } from "@/components/ui/progress-indicator";
import { IenLinearProgressIndicator } from "@/components/ui/progress-indicator";
import { IenLoadingIndicator } from "@/components/ui/loading-indicator";

export type ProgressDialogVariant = "loading" | "wavy" | "circular" | "linear";

export type ProgressDialogProps = Omit<
  React.ComponentProps<typeof DialogContent>,
  "children" | "ref" | "className" | "onEscapeKeyDown" | "onInteractOutside"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open?: boolean;
  visible?: boolean;
  defaultOpen?: boolean;
  title?: React.ReactNode;
  description?: React.ReactNode;
  value?: number;
  progress?: number | (() => number);
  variant?: ProgressDialogVariant;
  isLoadingIndicator?: boolean;
  isWavyIndicator?: boolean;
  onOpenChange?: (open: boolean) => void;
  onEntered?: () => void;
  onExited?: () => void;
  onEscapeKeyDown?: React.ComponentProps<
    typeof DialogContent
  >["onEscapeKeyDown"];
  onInteractOutside?: React.ComponentProps<
    typeof DialogContent
  >["onInteractOutside"];
};

function ProgressDialog({
  open,
  visible,
  defaultOpen,
  title = "처리 중",
  description,
  value,
  progress,
  variant,
  isLoadingIndicator = true,
  isWavyIndicator = true,
  onOpenChange,
  onEntered,
  onExited,
  ref,
  onEscapeKeyDown,
  onInteractOutside,
  ...contentProps
}: ProgressDialogProps) {
  const controlledOpen = visible ?? open;
  const progressValue = typeof progress === "function" ? progress() : progress;
  const boundedProgress =
    progressValue === undefined
      ? undefined
      : Math.max(0, Math.min(1, progressValue));
  const boundedValue =
    value === undefined ? undefined : Math.max(0, Math.min(100, value));
  const resolvedVariant =
    variant ??
    (value !== undefined
      ? "linear"
      : progress !== undefined
        ? isWavyIndicator
          ? "wavy"
          : "circular"
        : isLoadingIndicator
          ? "loading"
          : isWavyIndicator
            ? "wavy"
            : "circular");
  return (
    <Dialog
      open={controlledOpen}
      defaultOpen={controlledOpen === undefined ? defaultOpen : undefined}
      onOpenChange={onOpenChange}
    >
      <DialogContent
        {...contentProps}
        ref={ref}
        showCloseButton={false}
        closeOnBackEvent={false}
        closeOnDimmerClick={false}
        onEntered={onEntered}
        onExited={onExited}
        onEscapeKeyDown={(event) => {
          onEscapeKeyDown?.(event);
          event.preventDefault();
        }}
        onInteractOutside={(event) => {
          onInteractOutside?.(event);
          event.preventDefault();
        }}
        className="max-w-sm rounded-ien-xl border-ien-border bg-ien-surface text-ien-text-primary"
      >
        <DialogHeader>
          <DialogTitle className="text-ien-title3">{title}</DialogTitle>
          {description && (
            <DialogDescription className="text-ien-body2 text-ien-text-secondary">
              {description}
            </DialogDescription>
          )}
        </DialogHeader>
        <div role="status" aria-live="polite" className="grid gap-ien-xs">
          {resolvedVariant === "loading" && (
            <IenLoadingIndicator
              aria-label={typeof title === "string" ? title : "진행 중"}
              className="mx-auto"
            />
          )}
          {(resolvedVariant === "wavy" || resolvedVariant === "circular") && (
            <IenCircularProgressIndicator
              progress={boundedProgress}
              wavy={resolvedVariant === "wavy"}
              aria-label={typeof title === "string" ? title : "진행 중"}
              className="mx-auto size-12"
            />
          )}
          {resolvedVariant === "linear" && (
            <IenLinearProgressIndicator
              progress={
                boundedProgress ??
                (boundedValue === undefined ? undefined : boundedValue / 100)
              }
              aria-label={typeof title === "string" ? title : "진행 중"}
            />
          )}
          {boundedValue !== undefined && resolvedVariant === "linear" && (
            <span className="text-right text-ien-caption text-ien-text-secondary">
              {Math.round(boundedValue)}%
            </span>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
}

export { ProgressDialog, ProgressDialog as IenProgressDialog };
