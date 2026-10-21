"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { TimePickerDialog } from "@/components/ui/time-picker-dialog";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [time, setTime] = React.useState("09:30");
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <Button variant="weak" tone="neutral" onClick={() => setOpen(true)}>
        시간 선택
      </Button>
      <TimePickerDialog
        open={open}
        onOpenChange={setOpen}
        value={time}
        onValueChange={setTime}
      />
      <p aria-live="polite" className="text-ien-caption">
        선택 시간: {time}
      </p>
    </div>
  );
}
