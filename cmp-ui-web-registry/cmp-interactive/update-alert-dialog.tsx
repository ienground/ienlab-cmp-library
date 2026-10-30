"use client";

import * as React from "react";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

export type UpdateAlertDialogProps = Omit<
  React.ComponentProps<typeof AlertDialogContent>,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onUpdate: () => void;
  appName?: string;
  title?: React.ReactNode;
  description?: React.ReactNode;
  updateLabel?: string;
  laterLabel?: string;
};

function UpdateAlertDialog({
  open,
  onOpenChange,
  onUpdate,
  appName,
  title = "업데이트가 있습니다",
  description,
  updateLabel = "업데이트",
  laterLabel = "나중에",
  ref,
  ...contentProps
}: UpdateAlertDialogProps) {
  const message =
    description ??
    (appName
      ? `${appName}의 새 버전을 사용할 수 있습니다.`
      : "새 버전을 사용할 수 있습니다.");
  return (
    <AlertDialog open={open} onOpenChange={onOpenChange}>
      <AlertDialogContent
        {...contentProps}
        ref={ref}
        className="rounded-ien-xl border-ien-border bg-ien-surface text-ien-text-primary"
      >
        <AlertDialogHeader>
          <AlertDialogTitle className="text-ien-title3">
            {title}
          </AlertDialogTitle>
          <AlertDialogDescription className="text-ien-body2 text-ien-text-secondary">
            {message}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogAction
            onClick={() => onOpenChange(false)}
            className="bg-ien-surface-weak text-ien-text-primary hover:bg-ien-border"
          >
            {laterLabel}
          </AlertDialogAction>
          <AlertDialogAction onClick={onUpdate}>
            {updateLabel}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}

export { UpdateAlertDialog, UpdateAlertDialog as IenUpdateAlertDialog };
