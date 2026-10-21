"use client";

import * as React from "react";
import { cn } from "cn";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

export type ModalProps = Omit<
  React.ComponentProps<typeof DialogContent>,
  "children" | "ref" | "className" | "onEscapeKeyDown" | "onInteractOutside"
> & {
  ref?: React.Ref<HTMLDivElement>;
  onEscapeKeyDown?: React.ComponentProps<
    typeof DialogContent
  >["onEscapeKeyDown"];
  onInteractOutside?: React.ComponentProps<
    typeof DialogContent
  >["onInteractOutside"];
  open?: boolean;
  defaultOpen?: boolean;
  onOpenChange?: (open: boolean) => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  children: React.ReactNode;
  footer?: React.ReactNode;
  className?: string;
  dismissible?: boolean;
};

function Modal({
  open,
  defaultOpen,
  onOpenChange,
  title,
  description,
  children,
  footer,
  className,
  dismissible = true,
  ref,
  onEscapeKeyDown,
  onInteractOutside,
  ...contentProps
}: ModalProps) {
  return (
    <Dialog open={open} defaultOpen={defaultOpen} onOpenChange={onOpenChange}>
      <DialogContent
        {...contentProps}
        ref={ref}
        className={cn(
          "max-h-[min(85dvh,48rem)] overflow-y-auto rounded-ien-xl border-ien-border bg-ien-surface text-ien-text-primary",
          className,
        )}
        onEscapeKeyDown={(event) => {
          onEscapeKeyDown?.(event);
          if (!dismissible) event.preventDefault();
        }}
        onInteractOutside={(event) => {
          onInteractOutside?.(event);
          if (!dismissible) event.preventDefault();
        }}
      >
        {(title || description) && (
          <DialogHeader>
            {title && (
              <DialogTitle className="text-ien-title3 text-ien-text-primary">
                {title}
              </DialogTitle>
            )}
            {description && (
              <DialogDescription className="text-ien-body2 text-ien-text-secondary">
                {description}
              </DialogDescription>
            )}
          </DialogHeader>
        )}
        <div className="text-ien-body2">{children}</div>
        {footer && <DialogFooter>{footer}</DialogFooter>}
      </DialogContent>
    </Dialog>
  );
}

export { Modal, Modal as IenModal };
