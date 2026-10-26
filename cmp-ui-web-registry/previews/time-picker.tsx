"use client"

import { useState } from "react"
import {
  TimePicker,
  TimePickerPanel,
} from "@/components/reui/time-picker"

import { Card } from "@/components/ui/card"

export function Pattern() {
  const [value, setValue] = useState<string | null>("14:30")

  return (
    <div className="flex flex-col items-center gap-3">
      <Card contentPadding="none" className="w-fit">
        <TimePicker
          locale="ko"
          value={value}
          onValueChange={setValue}
          aria-label="회의 시간"
        >
          <TimePickerPanel />
        </TimePicker>
      </Card>
      <p role="status" className="text-muted-foreground text-sm">
        {value ? `회의 시간: ${value}` : "시간을 선택하세요"}
      </p>
    </div>
  )
}
export default Pattern;
