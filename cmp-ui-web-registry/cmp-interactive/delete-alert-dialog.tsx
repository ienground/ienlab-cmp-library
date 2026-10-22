"use client";

import * as React from "react";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogMedia,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

export type DeleteAlertDialogProps = Omit<
  React.ComponentProps<typeof AlertDialogContent>,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open?: boolean;
  visible?: boolean;
  defaultOpen?: boolean;
  onOpenChange?: (open: boolean) => void;
  onDismiss?: () => void;
  onDismissRequest?: () => void;
  onClose?: () => void;
  onConfirm: () => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  message?: React.ReactNode;
  icon?: React.ReactNode;
  confirmLabel?: string;
  cancelLabel?: string;
  textConfirm?: string;
  textDismiss?: string;
  enabledConfirm?: boolean;
  enabledDismiss?: boolean;
  buttonLayout?: "horizontal" | "vertical";
  closeOnDimmerClick?: boolean;
  closeOnBackEvent?: boolean;
  onEntered?: () => void;
  onExited?: () => void;
};

function DeleteAlertDialog({
  open,
  visible,
  defaultOpen,
  onOpenChange,
  onDismiss,
  onDismissRequest,
  onClose,
  onConfirm,
  title = "삭제하시겠습니까?",
  description = "삭제한 항목은 복구할 수 없습니다.",
  message,
  icon,
  confirmLabel = "삭제",
  cancelLabel = "취소",
  textConfirm,
  textDismiss,
  enabledConfirm = true,
  enabledDismiss = true,
  buttonLayout = "horizontal",
  closeOnDimmerClick = true,
  closeOnBackEvent = true,
  onEntered,
  onExited,
  ref,
  ...contentProps
}: DeleteAlertDialogProps) {
  const controlledOpen = visible ?? open;
  const handleDismiss = onDismissRequest ?? onDismiss ?? onClose;
  const handleEscape = (event: Parameters<NonNullable<typeof contentProps.onEscapeKeyDown>>[0]) => {
    contentProps.onEscapeKeyDown?.(event);
    if (!closeOnBackEvent) event.preventDefault();
    else if (!event.defaultPrevented) handleDismiss?.();
  };
  const handleDimmerClick: React.PointerEventHandler<HTMLDivElement> = (event) => {
    contentProps.onDimmerClick?.(event);
    if (closeOnDimmerClick && !event.defaultPrevented) handleDismiss?.();
  };
  const cancelAction = (
    <AlertDialogCancel
      variant="line"
      tone="neutral"
      disabled={!enabledDismiss}
      onClick={handleDismiss}
      className={buttonLayout === "horizontal" ? "flex-1" : "w-full"}
    >
      {textDismiss ?? cancelLabel}
    </AlertDialogCancel>
  );
  const confirmAction = (
    <AlertDialogAction
      variant="fill"
      tone="danger"
      disabled={!enabledConfirm}
      onClick={onConfirm}
      className={buttonLayout === "horizontal" ? "flex-1" : "w-full"}
    >
      {textConfirm ?? confirmLabel}
    </AlertDialogAction>
  );

  return (
    <AlertDialog
      open={controlledOpen}
      defaultOpen={controlledOpen === undefined ? defaultOpen : undefined}
      onOpenChange={onOpenChange}
    >
      <AlertDialogContent
        {...contentProps}
        ref={ref}
        onEscapeKeyDown={handleEscape}
        onDimmerClick={handleDimmerClick}
        closeOnBackEvent={closeOnBackEvent}
        closeOnDimmerClick={closeOnDimmerClick}
        onEntered={onEntered}
        onExited={onExited}
        className="max-w-xs rounded-ien-xl border-ien-border bg-ien-surface-raised text-ien-text-primary"
      >
        <AlertDialogHeader className="place-items-center text-center">
          {icon && <AlertDialogMedia>{icon}</AlertDialogMedia>}
          <AlertDialogTitle className="text-ien-title2">{title}</AlertDialogTitle>
          <AlertDialogDescription className="text-ien-body2 text-ien-text-secondary">
            {message ?? description}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter buttonLayout={buttonLayout}>
          {buttonLayout === "horizontal" ? [cancelAction, confirmAction] : [confirmAction, cancelAction]}
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}

export { DeleteAlertDialog, DeleteAlertDialog as IenDeleteAlertDialog };
