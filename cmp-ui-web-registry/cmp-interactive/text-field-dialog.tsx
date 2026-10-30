"use client";

import * as React from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

export type TextFieldDialogProps = Omit<
  React.ComponentProps<typeof DialogContent>,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  value?: string;
  defaultValue?: string;
  onConfirm: (value: string) => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  label?: string;
  placeholder?: string;
  confirmLabel?: string;
  disabled?: boolean;
  inputRef?: React.Ref<HTMLInputElement>;
};

function TextFieldDialog({
  open,
  onOpenChange,
  value,
  defaultValue = "",
  onConfirm,
  title = "입력",
  description,
  label = "내용",
  placeholder,
  confirmLabel = "확인",
  disabled = false,
  ref,
  inputRef,
  ...contentProps
}: TextFieldDialogProps) {
  const [draft, setDraft] = React.useState(value ?? defaultValue);
  React.useEffect(() => {
    if (open) setDraft(value ?? defaultValue);
  }, [open, value, defaultValue]);
  const confirm = (event: React.FormEvent) => {
    event.preventDefault();
    if (disabled) return;
    onConfirm(draft);
    onOpenChange(false);
  };
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent
        {...contentProps}
        ref={ref}
        className="max-w-sm rounded-ien-xl border-ien-border bg-ien-surface text-ien-text-primary"
      >
        <form onSubmit={confirm} className="grid gap-ien-md">
          <DialogHeader>
            <DialogTitle className="text-ien-title3">{title}</DialogTitle>
            {description && (
              <DialogDescription>{description}</DialogDescription>
            )}
          </DialogHeader>
          <label className="grid gap-ien-xs text-ien-label2">
            {label}
            <Input
              ref={inputRef}
              autoFocus
              value={draft}
              placeholder={placeholder}
              disabled={disabled}
              onChange={(event) => setDraft(event.currentTarget.value)}
            />
          </label>
          <DialogFooter>
            <Button
              type="button"
              variant="weak"
              tone="neutral"
              onClick={() => onOpenChange(false)}
            >
              취소
            </Button>
            <Button type="submit" disabled={disabled}>
              {confirmLabel}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

export { TextFieldDialog, TextFieldDialog as IenTextFieldDialog };
