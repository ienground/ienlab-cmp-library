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
  open?: boolean;
  visible?: boolean;
  defaultOpen?: boolean;
  onOpenChange?: (open: boolean) => void;
  value?: string;
  defaultValue?: string;
  initialHour?: number;
  initialMinute?: number;
  is24Hour?: boolean;
  onValueChange?: (value: string) => void;
  onConfirm?: (hour: number, minute: number) => void;
  onDismiss?: () => void;
  onDismissRequest?: () => void;
  onClose?: () => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  disabled?: boolean;
  enabledDismiss?: boolean;
};

function TimePickerDialog({
  open,
  visible,
  defaultOpen,
  onOpenChange,
  value,
  defaultValue,
  initialHour = 9,
  initialMinute = 0,
  is24Hour = false,
  onValueChange,
  onConfirm,
  onDismiss,
  onDismissRequest,
  onClose,
  title = "시간 선택",
  description,
  disabled = false,
  enabledDismiss = true,
  ref,
  ...contentProps
}: TimePickerDialogProps) {
  const controlledOpen = visible ?? open;
  const [internalOpen, setInternalOpen] = React.useState(defaultOpen ?? false);
  const isOpenControlled = controlledOpen !== undefined;
  const openState = isOpenControlled ? controlledOpen : internalOpen;
  const handleDismiss = onDismissRequest ?? onDismiss ?? onClose;

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
    if (openState) setDraft(committedTime);
  }, [openState, committedTime]);
  const validTime = /^(?:[01]\d|2[0-3]):[0-5]\d(?::[0-5]\d)?$/.test(draft);
  const updateOpen = (nextOpen: boolean, notifyDismiss = false) => {
    if (!isOpenControlled) setInternalOpen(nextOpen);
    onOpenChange?.(nextOpen);
    if (!nextOpen && notifyDismiss) handleDismiss?.();
  };
  const confirm = () => {
    if (!validTime || disabled) return;
    const [hour, minute] = draft.split(":").map(Number);
    const confirmedTime = `${String(hour).padStart(2, "0")}:${String(minute).padStart(2, "0")}`;
    if (value === undefined) setInternalValue(confirmedTime);
    onValueChange?.(confirmedTime);
    onConfirm?.(hour, minute);
    updateOpen(false);
  };
  const dismiss = () => updateOpen(false, true);
  return (
    <Dialog
      open={openState}
      onOpenChange={(nextOpen) => updateOpen(nextOpen, !nextOpen)}
    >
      <DialogContent
        {...contentProps}
        ref={ref}
        closeOnDimmerClick={contentProps.closeOnDimmerClick ?? true}
        closeOnBackEvent={contentProps.closeOnBackEvent ?? true}
        onEntered={contentProps.onEntered}
        onExited={contentProps.onExited}
        className="max-w-[360px] rounded-ien-xl border-ien-border bg-ien-surface-raised text-ien-text-primary"
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
        <DialogFooter className="flex-row">
          <Button
            type="button"
            variant="weak"
            tone="neutral"
            onClick={dismiss}
            disabled={!enabledDismiss}
            className="flex-1"
          >
            취소
          </Button>
          <Button
            type="button"
            onClick={confirm}
            disabled={disabled || !validTime}
            className="flex-1"
          >
            확인
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

export { TimePickerDialog, TimePickerDialog as IenTimePickerDialog };
