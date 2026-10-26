"use client"

import { useState } from "react"
import { useIenLanguage } from "@/lib/ien-theme-context"
import { DatePicker } from "@/components/reui/date-picker"
import { Card } from "@/components/ui/card"

export function Pattern() {
  const language = useIenLanguage("ko")
  const [value, setValue] = useState<string | null>("2024-02-29")
  return (
    <div className="flex flex-col items-center gap-3">
      <Card contentPadding="none" className="w-fit">
        <DatePicker inline value={value} onValueChange={setValue} aria-label={language === "ko" ? "날짜 선택" : "Select date"} />
      </Card>
      <DatePicker value={value} onValueChange={setValue} requireConfirm name="date" aria-label="확인 후 반영할 날짜" />
      <p role="status" className="text-muted-foreground text-sm">{value ?? (language === "ko" ? "날짜를 선택하세요" : "Select a date")}</p>
    </div>
  )
}
export default Pattern
