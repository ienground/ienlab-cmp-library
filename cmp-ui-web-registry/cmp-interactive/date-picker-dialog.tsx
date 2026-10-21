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
import { DateWheelPicker } from "@/components/ui/wheel-picker";

export type DatePickerDialogProps = Omit<
  React.ComponentProps<typeof DialogContent>,
  "children" | "ref" | "className" | "value" | "defaultValue"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onDismiss?: () => void;
  value?: Date;
  defaultValue?: Date;
  onValueChange?: (value: Date) => void;
  onConfirm?: (selectedDateMillis: number) => void;
  yearRange?: readonly [number, number];
  min?: Date;
  max?: Date;
  isDateDisabled?: (date: Date) => boolean;
  title?: React.ReactNode;
  description?: React.ReactNode;
  disabled?: boolean;
};

const fallbackDate = new Date(2000, 0, 1);
const localDateOnly = (date: Date) =>
  new Date(date.getFullYear(), date.getMonth(), date.getDate());

function DatePickerDialog({
  open,
  onOpenChange,
  onDismiss,
  value,
  defaultValue,
  onValueChange,
  onConfirm,
  yearRange = [1900, 2100],
  min,
  max,
  isDateDisabled,
  title = "날짜 선택",
  description,
  disabled = false,
  ref,
  ...contentProps
}: DatePickerDialogProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const committedDate = value ?? internalValue;
  const [draft, setDraft] = React.useState(committedDate ?? fallbackDate);
  const [hasSelection, setHasSelection] = React.useState(
    committedDate !== undefined,
  );

  React.useEffect(() => {
    if (open) {
      setDraft(committedDate ?? fallbackDate);
      setHasSelection(committedDate !== undefined);
    }
  }, [committedDate, open]);

  const selectedDay = localDateOnly(draft);
  const selectedDateIsValid =
    (!min || selectedDay >= localDateOnly(min)) &&
    (!max || selectedDay <= localDateOnly(max)) &&
    selectedDay.getFullYear() >= yearRange[0] &&
    selectedDay.getFullYear() <= yearRange[1] &&
    !isDateDisabled?.(selectedDay);

  const dismiss = () => {
    onOpenChange(false);
    onDismiss?.();
  };

  const confirm = () => {
    if (!hasSelection || !selectedDateIsValid || disabled) return;
    if (value === undefined) setInternalValue(draft);
    onValueChange?.(draft);
    onConfirm?.(
      Date.UTC(
        selectedDay.getFullYear(),
        selectedDay.getMonth(),
        selectedDay.getDate(),
      ),
    );
    onOpenChange(false);
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
        <DateWheelPicker
          value={draft}
          min={min}
          max={max}
          yearRange={yearRange}
          isDateDisabled={isDateDisabled}
          enabled={!disabled}
          onValueChange={(date) => {
            setDraft(date);
            setHasSelection(true);
          }}
        />
        <DialogFooter>
          <Button type="button" variant="weak" tone="neutral" onClick={dismiss}>
            취소
          </Button>
          <Button
            type="button"
            onClick={confirm}
            disabled={disabled || !hasSelection || !selectedDateIsValid}
          >
            확인
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

export { DatePickerDialog, DatePickerDialog as IenDatePickerDialog };
