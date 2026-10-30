"use client";

import * as React from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Progress } from "@/components/ui/progress";

export type ProgressDialogProps = Omit<
  React.ComponentProps<typeof DialogContent>,
  "children" | "ref" | "className" | "onEscapeKeyDown" | "onInteractOutside"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open: boolean;
  title?: React.ReactNode;
  description?: React.ReactNode;
  value?: number;
  onOpenChange?: (open: boolean) => void;
  onEscapeKeyDown?: React.ComponentProps<
    typeof DialogContent
  >["onEscapeKeyDown"];
  onInteractOutside?: React.ComponentProps<
    typeof DialogContent
  >["onInteractOutside"];
};

function ProgressDialog({
  open,
  title = "처리 중",
  description,
  value,
  onOpenChange,
  ref,
  onEscapeKeyDown,
  onInteractOutside,
  ...contentProps
}: ProgressDialogProps) {
  const bounded =
    value === undefined ? undefined : Math.max(0, Math.min(100, value));
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent
        {...contentProps}
        ref={ref}
        showCloseButton={false}
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
          <Progress
            value={bounded}
            aria-label={typeof title === "string" ? title : "진행 중"}
          />
          {bounded !== undefined && (
            <span className="text-right text-ien-caption text-ien-text-secondary">
              {Math.round(bounded)}%
            </span>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
}

export { ProgressDialog, ProgressDialog as IenProgressDialog };
