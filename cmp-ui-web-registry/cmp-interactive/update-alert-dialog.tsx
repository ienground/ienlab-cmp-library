"use client";

import * as React from "react";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogMedia,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

export type UpdateAlertDialogProps = Omit<
  React.ComponentProps<typeof AlertDialogContent>,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open?: boolean;
  visible?: boolean;
  defaultOpen?: boolean;
  onOpenChange?: (open: boolean) => void;
  onUpdate?: () => void;
  onDismiss?: () => void;
  onDismissRequest?: () => void;
  onClose?: () => void;
  onLater?: () => void;
  appName?: string;
  title?: React.ReactNode;
  description?: React.ReactNode;
  message?: React.ReactNode;
  icon?: React.ReactNode;
  updateLabel?: string;
  laterLabel?: string;
  dismissLabel?: string;
  enabledUpdate?: boolean;
  enabledDismiss?: boolean;
  buttonLayout?: "horizontal" | "vertical";
  closeOnDimmerClick?: boolean;
  closeOnBackEvent?: boolean;
  onEntered?: () => void;
  onExited?: () => void;
};

function UpdateAlertDialog({
  open,
  visible,
  defaultOpen,
  onOpenChange,
  onUpdate,
  onDismiss,
  onDismissRequest,
  onClose,
  onLater,
  appName,
  title = "업데이트가 있습니다",
  description,
  message,
  icon,
  updateLabel = "업데이트",
  laterLabel = "나중에",
  dismissLabel,
  enabledUpdate = true,
  enabledDismiss = true,
  buttonLayout = "horizontal",
  closeOnDimmerClick = true,
  closeOnBackEvent = true,
  onEntered,
  onExited,
  ref,
  ...contentProps
}: UpdateAlertDialogProps) {
  const controlledOpen = visible ?? open;
  const handleDismiss = onDismissRequest ?? onDismiss ?? onClose;
  const resolvedMessage =
    message ?? description ??
    (appName
      ? `${appName}의 새 버전을 사용할 수 있습니다.`
      : "새 버전을 사용할 수 있습니다.");
  const handleEscape = (event: Parameters<NonNullable<typeof contentProps.onEscapeKeyDown>>[0]) => {
    contentProps.onEscapeKeyDown?.(event);
    if (!closeOnBackEvent) event.preventDefault();
    else if (!event.defaultPrevented) handleDismiss?.();
  };
  const handleDimmerClick: React.PointerEventHandler<HTMLDivElement> = (event) => {
    contentProps.onDimmerClick?.(event);
    if (closeOnDimmerClick && !event.defaultPrevented) handleDismiss?.();
  };
  const updateAction = (
    <AlertDialogAction
      variant="fill"
      tone="brand"
      disabled={!enabledUpdate}
      onClick={onUpdate ?? handleDismiss}
      className={buttonLayout === "horizontal" ? "flex-1" : "w-full"}
    >
      {dismissLabel ?? updateLabel}
    </AlertDialogAction>
  );
  const laterAction = (
    <AlertDialogAction
      variant="line"
      tone="neutral"
      disabled={!enabledDismiss}
      onClick={onLater}
      className={buttonLayout === "horizontal" ? "flex-1" : "w-full"}
    >
      {laterLabel}
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
            {resolvedMessage}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter buttonLayout={buttonLayout}>
          {buttonLayout === "horizontal"
            ? [laterAction, updateAction]
            : [updateAction, laterAction]}
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}

export { UpdateAlertDialog, UpdateAlertDialog as IenUpdateAlertDialog };
