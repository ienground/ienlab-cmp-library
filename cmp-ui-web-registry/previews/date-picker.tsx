"use client"

import { useState } from "react"
import { DatePicker } from "@/components/reui/date-picker"
import { Card } from "@/components/ui/card"

export function Pattern() {
  const [value, setValue] = useState<string | null>("2024-02-29")
  return (
    <div className="flex flex-col items-center gap-3">
      <Card contentPadding="none" className="w-fit">
        <DatePicker locale="ko" inline value={value} onValueChange={setValue} aria-label="날짜 선택" />
      </Card>
      <p role="status" className="text-muted-foreground text-sm">{value ?? "날짜를 선택하세요"}</p>
    </div>
  )
}
export default Pattern
