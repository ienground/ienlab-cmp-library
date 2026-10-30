"use client";

import * as React from "react";
import { cn } from "cn";

export type WheelPickerOption<T extends string | number = string> = {
  value: T;
  label: React.ReactNode;
  disabled?: boolean;
};

export type WheelPickerProps<T extends string | number = string> = Omit<
  React.ComponentProps<"div">,
  "children"
> & {
  options: WheelPickerOption<T>[];
  value?: T;
  defaultValue?: T;
  onValueChange?: (value: T) => void;
  label: string;
  visibleCount?: number;
  disabled?: boolean;
};

function WheelPicker<T extends string | number>({
  options,
  value,
  defaultValue,
  onValueChange,
  label,
  visibleCount = 5,
  disabled = false,
  className,
  onKeyDown,
  onWheel,
  onPointerDown,
  onPointerUp,
  onPointerCancel,
  style,
  ...divProps
}: WheelPickerProps<T>) {
  const [internalValue, setInternalValue] = React.useState<T | undefined>(
    defaultValue,
  );
  const selected =
    value ?? internalValue ?? options.find((option) => !option.disabled)?.value;
  const selectedIndex = options.findIndex(
    (option) => option.value === selected,
  );
  const dragStart = React.useRef<number | null>(null);
  const idPrefix = React.useId().replaceAll(":", "");
  const controlled = value !== undefined;
  const rows = Math.max(3, visibleCount | 1);

  const select = (next: T) => {
    const option = options.find((item) => item.value === next);
    if (disabled || option?.disabled) return;
    if (!controlled) setInternalValue(next);
    onValueChange?.(next);
  };

  const move = (direction: -1 | 1, count = 1) => {
    let nextIndex = selectedIndex;
    for (let moveIndex = 0; moveIndex < count; moveIndex += 1) {
      let candidate = nextIndex + direction;
      while (
        candidate >= 0 &&
        candidate < options.length &&
        options[candidate].disabled
      )
        candidate += direction;
      if (candidate < 0 || candidate >= options.length) break;
      nextIndex = candidate;
    }
    if (
      nextIndex >= 0 &&
      nextIndex < options.length &&
      nextIndex !== selectedIndex
    )
      select(options[nextIndex].value);
  };

  const step = (direction: -1 | 1) => {
    let nextIndex =
      selectedIndex < 0 && direction > 0 ? 0 : selectedIndex + direction;
    while (nextIndex >= 0 && nextIndex < options.length) {
      if (!options[nextIndex].disabled) {
        select(options[nextIndex].value);
        return;
      }
      nextIndex += direction;
    }
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLDivElement>) => {
    if (event.key === "ArrowDown") {
      event.preventDefault();
      step(1);
    } else if (event.key === "ArrowUp") {
      event.preventDefault();
      step(-1);
    } else if (event.key === "Home") {
      event.preventDefault();
      const first = options.find((option) => !option.disabled);
      if (first) select(first.value);
    } else if (event.key === "End") {
      event.preventDefault();
      const last = [...options].reverse().find((option) => !option.disabled);
      if (last) select(last.value);
    }
  };

  const height = rows * 2.75;
  const radius = Math.floor(rows / 2);
  const visibleOptions = Array.from({ length: rows }, (_, slot) => {
    const index = (selectedIndex < 0 ? 0 : selectedIndex - radius) + slot;
    return index >= 0 && index < options.length
      ? { option: options[index], index }
      : null;
  });

  return (
    <div
      {...divProps}
      role="listbox"
      aria-label={label}
      aria-activedescendant={
        selectedIndex < 0 ? undefined : idPrefix + "-option-" + selectedIndex
      }
      aria-disabled={disabled || undefined}
      tabIndex={disabled ? -1 : 0}
      onKeyDown={(event) => {
        onKeyDown?.(event);
        if (!event.defaultPrevented) handleKeyDown(event);
      }}
      onWheel={(event) => {
        onWheel?.(event);
        if (event.defaultPrevented) return;
        if (disabled || event.deltaY === 0) return;
        event.preventDefault();
        step(event.deltaY > 0 ? 1 : -1);
      }}
      onPointerDown={(event) => {
        onPointerDown?.(event);
        if (event.defaultPrevented) return;
        if (!disabled) dragStart.current = event.clientY;
      }}
      onPointerUp={(event) => {
        onPointerUp?.(event);
        if (event.defaultPrevented) return;
        if (dragStart.current === null) return;
        const distance = dragStart.current - event.clientY;
        const count = Math.min(5, Math.floor(Math.abs(distance) / 32));
        if (count > 0) move(distance > 0 ? 1 : -1, count);
        dragStart.current = null;
      }}
      onPointerCancel={(event) => {
        onPointerCancel?.(event);
        if (!event.defaultPrevented) dragStart.current = null;
      }}
      className={cn(
        "relative grid w-full touch-none select-none overflow-hidden rounded-ien-default border border-ien-border bg-ien-surface text-ien-text-primary outline-none focus-visible:ring-2 focus-visible:ring-ien-brand",
        disabled && "cursor-not-allowed opacity-50",
        className,
      )}
      style={{
        ...style,
        height: height + "rem",
        gridTemplateRows: "repeat(" + rows + ", minmax(0, 1fr))",
      }}
    >
      <div
        aria-hidden
        className="pointer-events-none absolute inset-x-2 top-1/2 z-0 h-[var(--ien-state-minimum-touch-target)] -translate-y-1/2 rounded-ien-sm border-y border-ien-border bg-ien-surface-weak"
      />
      {visibleOptions.map((slot, slotIndex) => {
        if (!slot) return <span key={"empty-" + slotIndex} aria-hidden />;
        const option = slot.option;
        const active = slot.index === selectedIndex;
        return (
          <button
            id={idPrefix + "-option-" + slot.index}
            key={option.value}
            type="button"
            role="option"
            aria-selected={active}
            disabled={disabled || option.disabled}
            tabIndex={-1}
            onClick={() => select(option.value)}
            className={cn(
              "relative z-10 min-h-[var(--ien-state-minimum-touch-target)] px-ien-sm text-center text-ien-body1 transition-colors duration-[var(--ien-motion-fast)] disabled:cursor-not-allowed disabled:text-ien-text-disabled",
              active
                ? "font-semibold text-ien-text-primary"
                : "text-ien-text-tertiary",
            )}
          >
            {option.label}
          </button>
        );
      })}
    </div>
  );
}

export type DateWheelPickerProps = Omit<
  React.ComponentProps<"div">,
  "children"
> & {
  value: Date;
  onValueChange: (value: Date) => void;
  min?: Date;
  max?: Date;
  yearRange?: readonly [number, number];
  isDateDisabled?: (date: Date) => boolean;
  enabled?: boolean;
  visibleCount?: number;
};

function dateOnly(year: number, month: number, day: number) {
  const date = new Date(0);
  date.setHours(0, 0, 0, 0);
  date.setFullYear(year, month - 1, day);
  return date;
}

function daysInMonth(year: number, month: number) {
  return dateOnly(year, month + 1, 0).getDate();
}

function DateWheelPicker({
  value,
  onValueChange,
  min,
  max,
  yearRange = [1900, 2100],
  isDateDisabled,
  enabled = true,
  visibleCount = 5,
  className,
  ...divProps
}: DateWheelPickerProps) {
  const [firstYear, lastYear] = yearRange;
  if (firstYear < 1 || lastYear > 9999 || firstYear > lastYear) {
    throw new RangeError(
      "연도 범위는 1부터 9999 사이의 오름차순이어야 합니다.",
    );
  }
  const rangeStart = dateOnly(firstYear, 1, 1);
  const rangeEnd = dateOnly(lastYear, 12, 31);
  const minimum = min
    ? dateOnly(min.getFullYear(), min.getMonth() + 1, min.getDate())
    : rangeStart;
  const maximum = max
    ? dateOnly(max.getFullYear(), max.getMonth() + 1, max.getDate())
    : rangeEnd;
  const lowerBound = minimum > rangeStart ? minimum : rangeStart;
  const upperBound = maximum < rangeEnd ? maximum : rangeEnd;
  const validRange = lowerBound <= upperBound;

  const isSelectable = (date: Date) =>
    validRange &&
    date >= lowerBound &&
    date <= upperBound &&
    !isDateDisabled?.(date);

  const yearOptions = React.useMemo(() => {
    if (!validRange) return [];
    const first = lowerBound.getFullYear();
    const last = upperBound.getFullYear();
    const years = Array.from(
      { length: last - first + 1 },
      (_, index) => first + index,
    );
    if (!isDateDisabled) return years;
    return years.filter((year) => {
      for (let month = 1; month <= 12; month += 1) {
        for (let day = 1; day <= daysInMonth(year, month); day += 1) {
          if (isSelectable(dateOnly(year, month, day))) return true;
        }
      }
      return false;
    });
  }, [validRange, lowerBound.getTime(), upperBound.getTime(), isDateDisabled]);

  const nearestInYear = (year: number, reference: Date) => {
    let nearest: Date | undefined;
    let nearestDistance = Number.POSITIVE_INFINITY;
    for (let month = 1; month <= 12; month += 1) {
      for (let day = 1; day <= daysInMonth(year, month); day += 1) {
        const candidate = dateOnly(year, month, day);
        if (!isSelectable(candidate)) continue;
        const distance = Math.abs(candidate.getTime() - reference.getTime());
        if (distance < nearestDistance) {
          nearest = candidate;
          nearestDistance = distance;
        }
      }
    }
    return nearest;
  };

  const rawDate = dateOnly(
    value.getFullYear(),
    value.getMonth() + 1,
    value.getDate(),
  );
  let selectedDate = isSelectable(rawDate) ? rawDate : undefined;
  if (!selectedDate && yearOptions.length > 0) {
    const closestYear = [...yearOptions].sort(
      (left, right) =>
        Math.abs(left - rawDate.getFullYear()) -
        Math.abs(right - rawDate.getFullYear()),
    )[0];
    selectedDate = nearestInYear(closestYear, rawDate);
  }
  if (!selectedDate)
    return (
      <p role="status" className="text-ien-body2 text-ien-text-secondary">
        선택할 수 있는 날짜가 없습니다.
      </p>
    );

  const selectedYear = selectedDate.getFullYear();
  const selectedMonth = selectedDate.getMonth() + 1;
  const selectedDay = selectedDate.getDate();
  const monthOptions = Array.from(
    { length: 12 },
    (_, index) => index + 1,
  ).filter((month) => {
    for (let day = 1; day <= daysInMonth(selectedYear, month); day += 1) {
      if (isSelectable(dateOnly(selectedYear, month, day))) return true;
    }
    return false;
  });
  const dayOptions = Array.from(
    { length: daysInMonth(selectedYear, selectedMonth) },
    (_, index) => index + 1,
  ).filter((day) => isSelectable(dateOnly(selectedYear, selectedMonth, day)));
  const setDate = (next: Date | undefined) => {
    if (next) onValueChange(next);
  };

  return (
    <div
      {...divProps}
      className={cn("grid grid-cols-3 gap-[var(--ien-control-gap)]", className)}
    >
      <WheelPicker
        label="연도"
        value={selectedYear}
        options={yearOptions.map((year) => ({
          value: year,
          label: year + "년",
        }))}
        visibleCount={visibleCount}
        disabled={!enabled}
        onValueChange={(year) => {
          const day = Math.min(selectedDay, daysInMonth(year, selectedMonth));
          setDate(nearestInYear(year, dateOnly(year, selectedMonth, day)));
        }}
      />
      <WheelPicker
        label="월"
        value={selectedMonth}
        options={monthOptions.map((month) => ({
          value: month,
          label: month + "월",
        }))}
        visibleCount={visibleCount}
        disabled={!enabled}
        onValueChange={(month) => {
          const nearestDay = Array.from(
            { length: daysInMonth(selectedYear, month) },
            (_, index) => index + 1,
          )
            .filter((day) => isSelectable(dateOnly(selectedYear, month, day)))
            .sort(
              (left, right) =>
                Math.abs(left - selectedDay) - Math.abs(right - selectedDay),
            )[0];
          if (nearestDay)
            onValueChange(dateOnly(selectedYear, month, nearestDay));
        }}
      />
      <WheelPicker
        label="일"
        value={selectedDay}
        options={dayOptions.map((day) => ({ value: day, label: day + "일" }))}
        visibleCount={visibleCount}
        disabled={!enabled}
        onValueChange={(day) =>
          onValueChange(dateOnly(selectedYear, selectedMonth, day))
        }
      />
    </div>
  );
}

export type TimeWheelField = "hour" | "minute" | "second";
export type TimeWheelPickerProps = Omit<
  React.ComponentProps<"div">,
  "children"
> & {
  value: string;
  onValueChange: (value: string) => void;
  use24HourFormat?: boolean;
  showHours?: boolean;
  showMinutes?: boolean;
  showSeconds?: boolean;
  fields?: TimeWheelField[];
  enabled?: boolean;
  visibleCount?: number;
};

function parseTime(value: string) {
  const match = /^(\d{1,2}):(\d{2})(?::(\d{2}))?$/.exec(value);
  if (!match) return { hour: 9, minute: 0, second: 0 };
  const hour = Number(match[1]);
  const minute = Number(match[2]);
  const second = Number(match[3] ?? 0);
  if (hour > 23 || minute > 59 || second > 59)
    return { hour: 9, minute: 0, second: 0 };
  return { hour, minute, second };
}

function formatTime(hour: number, minute: number, second: number) {
  return [hour, minute, second]
    .map((value) => String(value).padStart(2, "0"))
    .join(":");
}

function TimeWheelPicker({
  value,
  onValueChange,
  use24HourFormat = true,
  showHours,
  showMinutes,
  showSeconds,
  fields,
  enabled = true,
  visibleCount = 5,
  className,
  ...divProps
}: TimeWheelPickerProps) {
  const showHourWheel = showHours ?? (fields ? fields.includes("hour") : true);
  const showMinuteWheel =
    showMinutes ?? (fields ? fields.includes("minute") : true);
  const showSecondWheel =
    showSeconds ?? (fields ? fields.includes("second") : true);
  if (!showHourWheel && !showMinuteWheel && !showSecondWheel) {
    throw new RangeError("적어도 하나의 시각 필드를 표시해야 합니다.");
  }
  const { hour, minute, second } = parseTime(value);
  const displayedHour = use24HourFormat ? hour : hour % 12 || 12;
  const emit = (nextHour: number, nextMinute = minute, nextSecond = second) => {
    onValueChange(formatTime(nextHour, nextMinute, nextSecond));
  };
  const hourOptions = use24HourFormat
    ? Array.from({ length: 24 }, (_, index) => index)
    : Array.from({ length: 12 }, (_, index) => index + 1);
  const minuteOptions = Array.from({ length: 60 }, (_, index) => index);

  return (
    <div
      {...divProps}
      className={cn("flex min-w-0 gap-[var(--ien-control-gap)]", className)}
    >
      {showHourWheel && !use24HourFormat && (
        <WheelPicker
          label="오전 또는 오후"
          value={hour < 12 ? "AM" : "PM"}
          options={[
            { value: "AM", label: "오전" },
            { value: "PM", label: "오후" },
          ]}
          visibleCount={visibleCount}
          disabled={!enabled}
          className="min-w-0 flex-1"
          onValueChange={(period) =>
            emit((displayedHour % 12) + (period === "PM" ? 12 : 0))
          }
        />
      )}
      {showHourWheel && (
        <WheelPicker
          label="시"
          value={displayedHour}
          options={hourOptions.map((item) => ({
            value: item,
            label: String(item).padStart(2, "0"),
          }))}
          visibleCount={visibleCount}
          disabled={!enabled}
          className="min-w-0 flex-1"
          onValueChange={(nextHour) =>
            emit(
              use24HourFormat
                ? nextHour
                : (nextHour % 12) + (hour >= 12 ? 12 : 0),
            )
          }
        />
      )}
      {showMinuteWheel && (
        <WheelPicker
          label="분"
          value={minute}
          options={minuteOptions.map((item) => ({
            value: item,
            label: String(item).padStart(2, "0"),
          }))}
          visibleCount={visibleCount}
          disabled={!enabled}
          className="min-w-0 flex-1"
          onValueChange={(nextMinute) => emit(hour, nextMinute)}
        />
      )}
      {showSecondWheel && (
        <WheelPicker
          label="초"
          value={second}
          options={minuteOptions.map((item) => ({
            value: item,
            label: String(item).padStart(2, "0"),
          }))}
          visibleCount={visibleCount}
          disabled={!enabled}
          className="min-w-0 flex-1"
          onValueChange={(nextSecond) => emit(hour, minute, nextSecond)}
        />
      )}
    </div>
  );
}

export type DurationWheelField = "hour" | "minute" | "second";
export type DurationWheelPickerProps = Omit<
  React.ComponentProps<"div">,
  "children"
> & {
  value: number;
  onValueChange: (value: number) => void;
  showHours?: boolean;
  showMinutes?: boolean;
  showSeconds?: boolean;
  fields?: DurationWheelField[];
  maxHours?: number;
  enabled?: boolean;
  visibleCount?: number;
};

function DurationWheelPicker({
  value,
  onValueChange,
  showHours,
  showMinutes,
  showSeconds,
  fields,
  maxHours = 23,
  enabled = true,
  visibleCount = 5,
  className,
  ...divProps
}: DurationWheelPickerProps) {
  const showHourWheel = showHours ?? (fields ? fields.includes("hour") : true);
  const showMinuteWheel =
    showMinutes ?? (fields ? fields.includes("minute") : true);
  const showSecondWheel =
    showSeconds ?? (fields ? fields.includes("second") : true);
  if (!showHourWheel && !showMinuteWheel && !showSecondWheel) {
    throw new RangeError("적어도 하나의 기간 필드를 표시해야 합니다.");
  }
  if (!Number.isFinite(value) || !Number.isInteger(value) || value < 0) {
    throw new RangeError("기간은 음수가 아닌 정수 초여야 합니다.");
  }
  if (
    showHourWheel &&
    (!Number.isInteger(maxHours) || maxHours < 0 || maxHours > 9999)
  ) {
    throw new RangeError("최대 시간은 0부터 9999 사이의 정수여야 합니다.");
  }
  const hourLimit = Math.max(0, Math.min(9999, Math.floor(maxHours)));
  const totalSeconds = value;
  const hour = Math.floor(totalSeconds / 3600);
  if (showHourWheel && hour > hourLimit) {
    throw new RangeError("기간의 시간이 선택 가능한 최대 시간을 초과했습니다.");
  }
  const minute = Math.floor(totalSeconds / 60) % 60;
  const second = totalSeconds % 60;
  const emit = (nextHour: number, nextMinute = minute, nextSecond = second) => {
    onValueChange(nextHour * 3600 + nextMinute * 60 + nextSecond);
  };
  const minuteOptions = Array.from({ length: 60 }, (_, index) => index);

  return (
    <div
      {...divProps}
      className={cn("flex min-w-0 gap-[var(--ien-control-gap)]", className)}
    >
      {showHourWheel && (
        <WheelPicker
          label="시간"
          value={hour}
          options={Array.from({ length: hourLimit + 1 }, (_, item) => ({
            value: item,
            label: item + "시간",
          }))}
          visibleCount={visibleCount}
          disabled={!enabled}
          className="min-w-0 flex-[1.4]"
          onValueChange={(nextHour) => emit(nextHour)}
        />
      )}
      {showMinuteWheel && (
        <WheelPicker
          label="분"
          value={minute}
          options={minuteOptions.map((item) => ({
            value: item,
            label: String(item).padStart(2, "0") + "분",
          }))}
          visibleCount={visibleCount}
          disabled={!enabled}
          className="min-w-0 flex-1"
          onValueChange={(nextMinute) => emit(hour, nextMinute)}
        />
      )}
      {showSecondWheel && (
        <WheelPicker
          label="초"
          value={second}
          options={minuteOptions.map((item) => ({
            value: item,
            label: String(item).padStart(2, "0") + "초",
          }))}
          visibleCount={visibleCount}
          disabled={!enabled}
          className="min-w-0 flex-1"
          onValueChange={(nextSecond) => emit(hour, minute, nextSecond)}
        />
      )}
    </div>
  );
}

export {
  WheelPicker,
  WheelPicker as IenWheelPicker,
  DateWheelPicker,
  DateWheelPicker as IenDateWheelPicker,
  TimeWheelPicker,
  TimeWheelPicker as IenTimeWheelPicker,
  DurationWheelPicker,
  DurationWheelPicker as IenDurationWheelPicker,
};
