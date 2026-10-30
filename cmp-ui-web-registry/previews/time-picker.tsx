"use client"

import { useState } from "react"
import { useIenLanguage } from "@/lib/ien-theme-context"
import {
  TimePicker,
  TimePickerPanel,
} from "@/components/reui/time-picker"

import { Card } from "@/components/ui/card"

export function Pattern() {
  const language = useIenLanguage("ko")
  const [value, setValue] = useState<string | null>("14:30")

  return (
    <div className="flex flex-col items-center gap-3">
      <Card contentPadding="none" className="w-fit">
        <TimePicker
          value={value}
          onValueChange={setValue}
          aria-label={language === "ko" ? "회의 시간" : "Meeting time"}
        >
          <TimePickerPanel />
        </TimePicker>
      </Card>
      <p role="status" className="text-muted-foreground text-sm">
        {value
          ? language === "ko" ? `회의 시간: ${value}` : `Meeting time: ${value}`
          : language === "ko" ? "시간을 선택하세요" : "Select a time"}
      </p>
    </div>
  )
}
export default Pattern;
