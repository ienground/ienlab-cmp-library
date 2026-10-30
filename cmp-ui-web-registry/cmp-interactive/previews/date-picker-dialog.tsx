"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { DatePickerDialog } from "@/components/ui/date-picker-dialog";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [date, setDate] = React.useState<Date>();
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <Button variant="weak" tone="neutral" onClick={() => setOpen(true)}>
        날짜 선택
      </Button>
      <DatePickerDialog
        open={open}
        onOpenChange={setOpen}
        value={date}
        onValueChange={setDate}
      />
      <p aria-live="polite" className="text-ien-caption">
        {date ? date.toLocaleDateString() : "날짜를 선택하지 않았습니다."}
      </p>
    </div>
  );
}
