"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { TimeWheelPicker } from "@/components/ui/wheel-picker";

export type TimePickerDialogProps = Omit<
  React.ComponentProps<typeof DialogContent>,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  value?: string;
  defaultValue?: string;
  initialHour?: number;
  initialMinute?: number;
  is24Hour?: boolean;
  onValueChange?: (value: string) => void;
  onConfirm?: (hour: number, minute: number) => void;
  onDismiss?: () => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  disabled?: boolean;
};

function TimePickerDialog({
  open,
  onOpenChange,
  value,
  defaultValue,
  initialHour = 9,
  initialMinute = 0,
  is24Hour = false,
  onValueChange,
  onConfirm,
  onDismiss,
  title = "시간 선택",
  description,
  disabled = false,
  ref,
  ...contentProps
}: TimePickerDialogProps) {
  if (
    initialHour < 0 ||
    initialHour > 23 ||
    initialMinute < 0 ||
    initialMinute > 59
  ) {
    throw new RangeError("초기 시간은 00:00부터 23:59 사이여야 합니다.");
  }
  const initialTime =
    value ??
    defaultValue ??
    `${String(initialHour).padStart(2, "0")}:${String(initialMinute).padStart(2, "0")}`;
  const [internalValue, setInternalValue] = React.useState(initialTime);
  const committedTime = value ?? internalValue;
  const [draft, setDraft] = React.useState(committedTime);
  React.useEffect(() => {
    if (open) setDraft(committedTime);
  }, [open, committedTime]);
  const validTime = /^(?:[01]\d|2[0-3]):[0-5]\d(?::[0-5]\d)?$/.test(draft);
  const confirm = () => {
    if (!validTime || disabled) return;
    const [hour, minute] = draft.split(":").map(Number);
    const confirmedTime = `${String(hour).padStart(2, "0")}:${String(minute).padStart(2, "0")}`;
    if (value === undefined) setInternalValue(confirmedTime);
    onValueChange?.(confirmedTime);
    onConfirm?.(hour, minute);
    onOpenChange(false);
  };
  const dismiss = () => {
    onOpenChange(false);
    onDismiss?.();
  };
  return (
    <Dialog
      open={open}
      onOpenChange={(nextOpen) => {
        onOpenChange(nextOpen);
        if (!nextOpen) onDismiss?.();
      }}
    >
      <DialogContent
        {...contentProps}
        ref={ref}
        className="max-w-sm rounded-ien-xl border-ien-border bg-ien-surface text-ien-text-primary"
      >
        <DialogHeader>
          <DialogTitle className="text-ien-title3">{title}</DialogTitle>
          {description && <DialogDescription>{description}</DialogDescription>}
        </DialogHeader>
        <TimeWheelPicker
          value={draft}
          onValueChange={setDraft}
          use24HourFormat={is24Hour}
          showSeconds={false}
          enabled={!disabled}
        />
        <DialogFooter>
          <Button type="button" variant="weak" tone="neutral" onClick={dismiss}>
            취소
          </Button>
          <Button
            type="button"
            onClick={confirm}
            disabled={disabled || !validTime}
          >
            확인
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

export { TimePickerDialog, TimePickerDialog as IenTimePickerDialog };
