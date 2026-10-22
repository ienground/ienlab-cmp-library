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
  open?: boolean;
  visible?: boolean;
  defaultOpen?: boolean;
  onOpenChange?: (open: boolean) => void;
  onDismiss?: () => void;
  onDismissRequest?: () => void;
  onClose?: () => void;
  value?: Date;
  defaultValue?: Date;
  initialSelectedDateMillis?: number | null;
  initialDisplayedMonthMillis?: number | null;
  onValueChange?: (value: Date) => void;
  onConfirm?: (selectedDateMillis: number) => void;
  yearRange?: readonly [number, number];
  selectableDates?: {
    isSelectableYear?: (year: number) => boolean;
    isSelectableDate?: (utcDateMillis: number) => boolean;
  };
  min?: Date;
  max?: Date;
  isDateDisabled?: (date: Date) => boolean;
  title?: React.ReactNode;
  description?: React.ReactNode;
  disabled?: boolean;
  enabledDismiss?: boolean;
  closeOnDimmerClick?: boolean;
  closeOnBackEvent?: boolean;
  onEntered?: () => void;
  onExited?: () => void;
};

const localDateOnly = (date: Date) =>
  new Date(date.getFullYear(), date.getMonth(), date.getDate());
const utcMillisToDate = (millis: number) => {
  const value = new Date(millis);
  return new Date(value.getUTCFullYear(), value.getUTCMonth(), value.getUTCDate());
};

function DatePickerDialog({
  open,
  visible,
  defaultOpen,
  onOpenChange,
  onDismiss,
  onDismissRequest,
  onClose,
  value,
  defaultValue,
  initialSelectedDateMillis,
  initialDisplayedMonthMillis,
  onValueChange,
  onConfirm,
  yearRange = [1900, 2100],
  selectableDates,
  min,
  max,
  isDateDisabled,
  title = "날짜 선택",
  description,
  disabled = false,
  enabledDismiss = true,
  closeOnDimmerClick = true,
  closeOnBackEvent = true,
  onEntered,
  onExited,
  ref,
  ...contentProps
}: DatePickerDialogProps) {
  const controlledOpen = visible ?? open;
  const [internalOpen, setInternalOpen] = React.useState(defaultOpen ?? false);
  const isOpenControlled = controlledOpen !== undefined;
  const openState = isOpenControlled ? controlledOpen : internalOpen;
  const initialSelected = React.useMemo(
    () =>
      defaultValue ??
      (initialSelectedDateMillis == null
        ? undefined
        : utcMillisToDate(initialSelectedDateMillis)),
    [defaultValue, initialSelectedDateMillis],
  );
  const displayedMonth = React.useMemo(
    () =>
      initialDisplayedMonthMillis == null
        ? undefined
        : utcMillisToDate(initialDisplayedMonthMillis),
    [initialDisplayedMonthMillis],
  );
  const [internalValue, setInternalValue] = React.useState(initialSelected);
  const committedDate = value ?? internalValue;
  const [draft, setDraft] = React.useState(
    committedDate ?? displayedMonth ?? new Date(),
  );
  const [hasSelection, setHasSelection] = React.useState(
    committedDate !== undefined,
  );
  const handleDismiss = onDismissRequest ?? onDismiss ?? onClose;

  React.useEffect(() => {
    if (openState) {
      setDraft(committedDate ?? displayedMonth ?? new Date());
      setHasSelection(committedDate !== undefined);
    }
  }, [committedDate, openState, displayedMonth]);

  const selectedDay = localDateOnly(draft);
  const selectedDateMillis = Date.UTC(
    selectedDay.getFullYear(),
    selectedDay.getMonth(),
    selectedDay.getDate(),
  );
  const selectedDateIsValid =
    (!min || selectedDay >= localDateOnly(min)) &&
    (!max || selectedDay <= localDateOnly(max)) &&
    selectedDay.getFullYear() >= yearRange[0] &&
    selectedDay.getFullYear() <= yearRange[1] &&
    (selectableDates?.isSelectableYear?.(selectedDay.getFullYear()) ?? true) &&
    (selectableDates?.isSelectableDate?.(selectedDateMillis) ?? true) &&
    !isDateDisabled?.(selectedDay);

  const updateOpen = (nextOpen: boolean, notifyDismiss = false) => {
    if (!isOpenControlled) setInternalOpen(nextOpen);
    onOpenChange?.(nextOpen);
    if (!nextOpen && notifyDismiss) handleDismiss?.();
  };

  const dismiss = () => updateOpen(false, true);

  const confirm = () => {
    if (!hasSelection || !selectedDateIsValid || disabled) return;
    if (value === undefined) setInternalValue(draft);
    onValueChange?.(draft);
    onConfirm?.(selectedDateMillis);
    updateOpen(false);
  };

  return (
    <Dialog
      open={openState}
      onOpenChange={(nextOpen) => updateOpen(nextOpen, !nextOpen)}
    >
      <DialogContent
        {...contentProps}
        ref={ref}
        showCloseButton={false}
        closeOnDimmerClick={closeOnDimmerClick}
        closeOnBackEvent={closeOnBackEvent}
        onEntered={onEntered}
        onExited={onExited}
        className="max-h-[568px] max-w-[360px] gap-ien-md overflow-y-auto rounded-ien-xl border-ien-border bg-ien-surface-raised p-0 text-ien-text-primary"
      >
        <DialogHeader className="px-ien-xl pt-ien-md">
          <DialogTitle className="text-ien-title2">{title}</DialogTitle>
          {description && <DialogDescription>{description}</DialogDescription>}
        </DialogHeader>
        <DateWheelPicker
          value={draft}
          min={min}
          max={max}
          yearRange={yearRange}
          isDateDisabled={(date) => {
            const localDate = localDateOnly(date);
            const dateMillis = Date.UTC(
              localDate.getFullYear(),
              localDate.getMonth(),
              localDate.getDate(),
            );
            return (
              selectableDates?.isSelectableYear?.(localDate.getFullYear()) ===
                false ||
              selectableDates?.isSelectableDate?.(dateMillis) === false ||
              Boolean(isDateDisabled?.(localDate))
            );
          }}
          enabled={!disabled}
          onValueChange={(date) => {
            setDraft(date);
            setHasSelection(true);
          }}
        />
        <DialogFooter className="mx-0 mb-0 flex-row border-t-0 bg-transparent px-ien-xl pb-ien-xl">
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
            disabled={disabled || !hasSelection || !selectedDateIsValid}
            className="flex-1"
          >
            확인
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

export { DatePickerDialog, DatePickerDialog as IenDatePickerDialog };
