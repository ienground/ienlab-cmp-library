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
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

export type DeleteAlertDialogProps = Omit<
  React.ComponentProps<typeof AlertDialogContent>,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onConfirm: () => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  confirmLabel?: string;
  cancelLabel?: string;
};

function DeleteAlertDialog({
  open,
  onOpenChange,
  onConfirm,
  title = "삭제하시겠습니까?",
  description = "삭제한 항목은 복구할 수 없습니다.",
  confirmLabel = "삭제",
  cancelLabel = "취소",
  ref,
  ...contentProps
}: DeleteAlertDialogProps) {
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
            {description}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>{cancelLabel}</AlertDialogCancel>
          <AlertDialogAction
            onClick={onConfirm}
            className="bg-ien-danger text-ien-on-danger hover:bg-ien-danger/90"
          >
            {confirmLabel}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}

export { DeleteAlertDialog, DeleteAlertDialog as IenDeleteAlertDialog };
