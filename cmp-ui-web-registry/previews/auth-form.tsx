"use client"

import { useState } from "react"
import { AuthForm } from "@/components/ui/auth-form"

export function Pattern() {
  const [message, setMessage] = useState("")
  return <div className="flex w-full max-w-lg flex-col gap-4"><AuthForm title="계정 확인" description="입력 UI 예시이며 실제 인증 요청은 하지 않습니다." submitLabel="계속" onSubmit={() => setMessage("입력값 검증을 통과했습니다.")} /><p aria-live="polite" className="text-sm text-muted-foreground">{message}</p></div>
}
export default Pattern
