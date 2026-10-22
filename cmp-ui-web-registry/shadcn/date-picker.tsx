/*
 * MIT License
 *
 * Copyright (c) 2023 shadcn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
"use client";

import * as React from "react";
import { format } from "date-fns";
import { CalendarIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Calendar } from "@/components/ui/calendar";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { cn } from "@/lib/ien-utils";

export type DatePickerProps = {
  value?: Date;
  defaultValue?: Date;
  onValueChange?: (date: Date | undefined) => void;
  placeholder?: string;
  dateFormat?: string;
  disabled?: boolean;
  fromDate?: Date;
  toDate?: Date;
  className?: string;
};
function DatePicker({
  value,
  defaultValue,
  onValueChange,
  placeholder = "날짜 선택",
  dateFormat = "PPP",
  disabled,
  fromDate,
  toDate,
  className,
}: DatePickerProps) {
  const [internalDate, setInternalDate] = React.useState<Date | undefined>(
    defaultValue,
  );
  const [open, setOpen] = React.useState(false);
  const selected = value === undefined ? internalDate : value;
  const selectDate = (date: Date | undefined) => {
    if (value === undefined) setInternalDate(date);
    onValueChange?.(date);
    if (date) setOpen(false);
  };
  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          type="button"
          variant="line"
          tone="neutral"
          disabled={disabled}
          className={cn(
            "w-full justify-start text-left font-normal",
            !selected && "text-ien-text-secondary",
            className,
          )}
        >
          <CalendarIcon aria-hidden="true" className="mr-ien-xs size-4" />
          {selected ? format(selected, dateFormat) : placeholder}
        </Button>
      </PopoverTrigger>
      <PopoverContent align="start" className="w-auto p-ien-xxs">
        <Calendar
          mode="single"
          selected={selected}
          onSelect={selectDate}
          startMonth={fromDate}
          endMonth={toDate}
        />
      </PopoverContent>
    </Popover>
  );
}

export { DatePicker };
