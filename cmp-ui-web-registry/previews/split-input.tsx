"use client"

import { useState } from "react"
import { SplitInput } from "@/components/ui/split-input"

export function Pattern() {
  const [message, setMessage] = useState("")
  return <div className="flex w-full max-w-lg flex-col gap-4"><SplitInput lengths={[3, 4, 4]} labels={["전화번호 앞자리", "전화번호 가운데 자리", "전화번호 끝자리"]} onValueChange={(values) => setMessage(values.join("-"))} /><p aria-live="polite" className="text-sm text-muted-foreground">{message}</p></div>
}
export default Pattern
