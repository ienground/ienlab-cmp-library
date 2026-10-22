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

export type SaveAlertDialogProps = Omit<
  React.ComponentProps<typeof AlertDialogContent>,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open?: boolean;
  visible?: boolean;
  defaultOpen?: boolean;
  onOpenChange?: (open: boolean) => void;
  onDismissRequest?: () => void;
  onClose?: () => void;
  onCancel?: () => void;
  onSave: () => void;
  onUnsave?: () => void;
  onDiscard?: () => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  message?: React.ReactNode;
  icon?: React.ReactNode;
  saveLabel?: string;
  discardLabel?: string;
  cancelLabel?: string;
  textPositive?: string;
  textNeutral?: string;
  textNegative?: string;
  enabledSave?: boolean;
  enabledUnsave?: boolean;
  enabledDismiss?: boolean;
  buttonLayout?: "horizontal" | "vertical";
  closeOnDimmerClick?: boolean;
  closeOnBackEvent?: boolean;
  onEntered?: () => void;
  onExited?: () => void;
};

function SaveAlertDialog({
  open,
  visible,
  defaultOpen,
  onOpenChange,
  onDismissRequest,
  onClose,
  onCancel,
  onSave,
  onUnsave,
  onDiscard,
  title = "변경 사항 저장",
  description = "저장하지 않은 변경 사항이 있습니다.",
  message,
  icon,
  saveLabel,
  discardLabel,
  cancelLabel,
  textPositive,
  textNeutral,
  textNegative,
  enabledSave = true,
  enabledUnsave = true,
  enabledDismiss = true,
  buttonLayout = "vertical",
  closeOnDimmerClick = true,
  closeOnBackEvent = true,
  onEntered,
  onExited,
  ref,
  ...contentProps
}: SaveAlertDialogProps) {
  const controlledOpen = visible ?? open;
  const handleDismissRequest = onDismissRequest ?? onClose;
  const handleEscape = (event: Parameters<NonNullable<typeof contentProps.onEscapeKeyDown>>[0]) => {
    contentProps.onEscapeKeyDown?.(event);
    if (!closeOnBackEvent) event.preventDefault();
    else if (!event.defaultPrevented) handleDismissRequest?.();
  };
  const handleDimmerClick: React.PointerEventHandler<HTMLDivElement> = (event) => {
    contentProps.onDimmerClick?.(event);
    if (closeOnDimmerClick && !event.defaultPrevented) handleDismissRequest?.();
  };
  const unsave = onUnsave ?? onDiscard ?? (() => {});
  const actions = {
    unsave: (
      <AlertDialogAction
        key="unsave"
        variant="line"
        tone="danger"
        disabled={!enabledUnsave}
        onClick={unsave}
        className={buttonLayout === "horizontal" ? "flex-1" : "w-full"}
      >
        {textNeutral ?? discardLabel ?? "변경 사항 버리기"}
      </AlertDialogAction>
    ),
    cancel: (
      <AlertDialogCancel
        key="cancel"
        variant="ghost"
        tone="neutral"
        disabled={!enabledDismiss}
        onClick={() => {
          onCancel?.();
          handleDismissRequest?.();
        }}
        className={buttonLayout === "horizontal" ? "flex-1" : "w-full"}
      >
        {textNegative ?? cancelLabel ?? "취소"}
      </AlertDialogCancel>
    ),
    save: (
      <AlertDialogAction
        key="save"
        disabled={!enabledSave}
        onClick={onSave}
        className={buttonLayout === "horizontal" ? "flex-1" : "w-full"}
      >
        {textPositive ?? saveLabel ?? "저장"}
      </AlertDialogAction>
    ),
  };
  const buttons =
    buttonLayout === "horizontal"
      ? [actions.unsave, actions.cancel, actions.save]
      : [actions.save, actions.unsave, actions.cancel];

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
        <AlertDialogFooter buttonLayout={buttonLayout}>{buttons}</AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}

export { SaveAlertDialog, SaveAlertDialog as IenSaveAlertDialog };
