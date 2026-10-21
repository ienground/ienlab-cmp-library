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
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

export type NetworkAlertDialogProps = Omit<
  React.ComponentProps<typeof AlertDialogContent>,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onRetry: () => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  retryLabel?: string;
};

function NetworkAlertDialog({
  open,
  onOpenChange,
  onRetry,
  title = "네트워크에 연결할 수 없습니다",
  description = "인터넷 연결을 확인한 뒤 다시 시도해 주세요.",
  retryLabel = "다시 시도",
  ref,
  ...contentProps
}: NetworkAlertDialogProps) {
  return (
    <AlertDialog open={open} onOpenChange={onOpenChange}>
      <AlertDialogContent
        {...contentProps}
        ref={ref}
        className="rounded-ien-xl border-ien-border bg-ien-surface text-ien-text-primary"
      >
        <AlertDialogHeader>
          <div className="grid size-[var(--ien-state-minimum-touch-target)] place-items-center rounded-ien-full bg-ien-danger-weak text-ien-danger">
            <WifiOff aria-hidden="true" />
          </div>
          <AlertDialogTitle className="text-ien-title3">
            {title}
          </AlertDialogTitle>
          <AlertDialogDescription className="text-ien-body2 text-ien-text-secondary">
            {description}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogAction onClick={onRetry}>{retryLabel}</AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}

export { NetworkAlertDialog, NetworkAlertDialog as IenNetworkAlertDialog };
