"use client"

import { useState } from "react"
import { DurationPicker } from "@/components/reui/duration-picker"
import { Card } from "@/components/ui/card"

export function Pattern() {
  const [value, setValue] = useState<number | null>(5400)
  return (
    <div className="flex flex-col items-center gap-3">
      <Card contentPadding="none" className="w-fit">
        <DurationPicker locale="ko" inline maxHours={48} value={value} onValueChange={setValue} aria-label="기간 선택" />
      </Card>
      <p role="status" className="text-muted-foreground text-sm">{value === null ? "기간을 선택하세요" : `${value}초`}</p>
    </div>
  )
}
export default Pattern
