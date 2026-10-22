"use client";
import * as React from "react";
import type { DateRange } from "react-day-picker";
import { Calendar } from "@/components/ui/calendar";
export default function Preview() {
  const [range, setRange] = React.useState<DateRange>();
  return (
    <Calendar
      mode="range"
      selected={range}
      onSelect={setRange}
      disabled={{ dayOfWeek: [0] }}
      className="rounded-ien-default border border-ien-border"
    />
  );
}
