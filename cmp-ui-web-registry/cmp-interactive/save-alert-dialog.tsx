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

export type SaveAlertDialogProps = Omit<
  React.ComponentProps<typeof AlertDialogContent>,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSave: () => void;
  onDiscard: () => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  saveLabel?: string;
  discardLabel?: string;
  cancelLabel?: string;
};

function SaveAlertDialog({
  open,
  onOpenChange,
  onSave,
  onDiscard,
  title = "변경 사항 저장",
  description = "저장하지 않은 변경 사항이 있습니다.",
  saveLabel = "저장",
  discardLabel = "변경 사항 버리기",
  cancelLabel = "취소",
  ref,
  ...contentProps
}: SaveAlertDialogProps) {
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
            onClick={onDiscard}
            className="bg-ien-surface-weak text-ien-text-primary hover:bg-ien-border"
          >
            {discardLabel}
          </AlertDialogAction>
          <AlertDialogAction onClick={onSave}>{saveLabel}</AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}

export { SaveAlertDialog, SaveAlertDialog as IenSaveAlertDialog };
