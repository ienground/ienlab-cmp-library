"use client"

import { useState } from "react"
import { Agreement } from "@/components/ui/agreement"

export function Pattern() {
  const [message, setMessage] = useState("")
  return <div className="flex w-full max-w-lg flex-col gap-4"><Agreement items={[{ value: "terms", label: "서비스 이용약관", required: true, description: "서비스 이용에 필요한 약관입니다." }, { value: "privacy", label: "개인정보 처리", required: true }, { value: "marketing", label: "혜택 알림 수신", description: "선택 동의는 언제든 철회할 수 있습니다." }]} onValueChange={(value) => setMessage(value.join(", "))} /><p aria-live="polite" className="text-sm text-muted-foreground">{message}</p></div>
}
export default Pattern
