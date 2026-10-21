"use client";
import * as React from "react";
import { DatePicker } from "@/components/ui/date-picker";
export default function Preview() {
  const [date, setDate] = React.useState<Date>();
  return (
    <div className="grid max-w-xs gap-ien-xs">
      <DatePicker value={date} onValueChange={setDate} />
      <span className="text-ien-caption text-ien-text-secondary">
        {date?.toLocaleDateString("ko-KR") ?? "날짜를 선택하세요."}
      </span>
    </div>
  );
}
