"use client";

import * as React from "react";
import { WifiOff } from "lucide-react";
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

export type NetworkAlertDialogProps = Omit<
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
  onRetry?: () => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  message?: React.ReactNode;
  icon?: React.ReactNode;
  retryLabel?: string;
  dismissLabel?: string;
  enabledAction?: boolean;
  closeOnDimmerClick?: boolean;
  closeOnBackEvent?: boolean;
  onEntered?: () => void;
  onExited?: () => void;
};

function NetworkAlertDialog({
  open,
  visible,
  defaultOpen,
  onOpenChange,
  onDismiss,
  onDismissRequest,
  onClose,
  onRetry,
  title = "네트워크에 연결할 수 없습니다",
  description = "인터넷 연결을 확인한 뒤 다시 시도해 주세요.",
  message,
  icon = <WifiOff aria-hidden="true" />,
  retryLabel = "다시 시도",
  dismissLabel,
  enabledAction = true,
  closeOnDimmerClick = true,
  closeOnBackEvent = true,
  onEntered,
  onExited,
  ref,
  ...contentProps
}: NetworkAlertDialogProps) {
  const controlledOpen = visible ?? open;
  const handleDismiss = onDismissRequest ?? onDismiss ?? onClose;
  const action = onRetry ?? handleDismiss;
  const actionLabel = dismissLabel ?? (onRetry ? retryLabel : "닫기");
  const handleEscape = (event: Parameters<NonNullable<typeof contentProps.onEscapeKeyDown>>[0]) => {
    contentProps.onEscapeKeyDown?.(event);
    if (!closeOnBackEvent) event.preventDefault();
    else if (!event.defaultPrevented) handleDismiss?.();
  };
  const handleDimmerClick: React.PointerEventHandler<HTMLDivElement> = (event) => {
    contentProps.onDimmerClick?.(event);
    if (closeOnDimmerClick && !event.defaultPrevented) handleDismiss?.();
  };

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
          <AlertDialogMedia className="bg-ien-danger-weak text-ien-danger">
            {icon}
          </AlertDialogMedia>
          <AlertDialogTitle className="text-ien-title2">{title}</AlertDialogTitle>
          <AlertDialogDescription className="text-ien-body2 text-ien-text-secondary">
            {message ?? description}
          </AlertDialogDescription>
        </AlertDialogHeader>
        {action && (
          <AlertDialogFooter buttonLayout="vertical">
            <AlertDialogAction
              disabled={!enabledAction}
              onClick={action}
              className="w-full"
            >
              {actionLabel}
            </AlertDialogAction>
          </AlertDialogFooter>
        )}
      </AlertDialogContent>
    </AlertDialog>
  );
}

export { NetworkAlertDialog, NetworkAlertDialog as IenNetworkAlertDialog };
