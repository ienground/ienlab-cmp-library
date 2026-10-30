"use client"

import { useState } from "react"
import { AlertDialog, ConfirmDialog } from "@/components/ui/alert-dialog"
import { Button } from "@/components/ui/button"

export function Pattern() {
  const [active, setActive] = useState<"alert" | "confirm" | null>(null)
  const [message, setMessage] = useState("")
  return <div className="flex flex-wrap gap-ien-sm">
    <Button onClick={() => setActive("alert")}>알림</Button><Button variant="weak" onClick={() => setActive("confirm")}>확인·취소</Button>
    <AlertDialog open={active === "alert"} onOpenChange={(open) => { if (!open) setActive(null) }} title="저장 완료" message="변경 사항을 저장했습니다." />
    <ConfirmDialog open={active === "confirm"} onOpenChange={(open) => { if (!open) setActive(null) }} title="변경 사항 적용" message="선택한 설정을 적용할까요?" onConfirm={() => setMessage("적용했습니다.")} />
    <p aria-live="polite" className="w-full text-sm text-muted-foreground">{message}</p>
  </div>
}
export default Pattern
