"use client"

import { useState } from "react"
import { useIenLanguage } from "@/lib/ien-theme-context"
import { DurationPicker } from "@/components/reui/duration-picker"
import { Card } from "@/components/ui/card"

export function Pattern() {
  const language = useIenLanguage("ko")
  const [value, setValue] = useState<number | null>(5400)
  return (
    <div className="flex flex-col items-center gap-3">
      <Card contentPadding="none" className="w-fit">
        <DurationPicker inline maxHours={48} value={value} onValueChange={setValue} aria-label={language === "ko" ? "기간 선택" : "Select duration"} />
      </Card>
      <p role="status" className="text-muted-foreground text-sm">{value === null ? (language === "ko" ? "기간을 선택하세요" : "Select a duration") : language === "ko" ? `${value}초` : `${value} seconds`}</p>
    </div>
  )
}
export default Pattern
