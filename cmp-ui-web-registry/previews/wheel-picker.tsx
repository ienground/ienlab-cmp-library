"use client"

import { useState } from "react"
import { WheelPicker } from "@/components/ui/wheel-picker"

export function Pattern() {
  const [message, setMessage] = useState("")
  return <div className="flex w-full max-w-lg flex-col gap-4"><WheelPicker label="우선순위" options={[{value:"low", label:"낮음"}, {value:"normal", label:"보통"}, {value:"high", label:"높음"}, {value:"blocked", label:"선택 불가", disabled:true}]} defaultValue="normal" onValueChange={(value) => setMessage(String(value))} /><p aria-live="polite" className="text-sm text-muted-foreground">{message}</p></div>
}
export default Pattern
