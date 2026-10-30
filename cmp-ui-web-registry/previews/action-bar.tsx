"use client"

import { useState } from "react"
import { ActionBar, ActionBarButton } from "@/components/ui/action-bar"
import { Button } from "@/components/ui/button"

export function Pattern() {
  const [message, setMessage] = useState("")
  const [open, setOpen] = useState(true)
  return <div className="flex w-full max-w-lg flex-col gap-4"><><Button variant="weak" onClick={() => setOpen(true)}>선택 작업 열기</Button><ActionBar open={open} onOpenChange={setOpen} selection="2개 선택"><ActionBarButton size="sm" variant="weak" onClick={() => setMessage("보관했습니다.")}>보관</ActionBarButton><ActionBarButton size="sm" tone="danger" variant="ghost" onClick={() => setOpen(false)}>선택 해제</ActionBarButton></ActionBar></><p aria-live="polite" className="text-sm text-muted-foreground">{message}</p></div>
}
export default Pattern
