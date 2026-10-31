"use client"

import { useState } from "react"
import { SaveAlertDialog, DeleteAlertDialog, UpdateAlertDialog, NetworkAlertDialog, ProgressDialog, TextFieldDialog, DatePickerDialog, TimePickerDialog } from "@/components/ui/dialog-presets"
import { Button } from "@/components/ui/button"

export function Pattern() {
  const [active, setActive] = useState<string | null>(null)
  const [message, setMessage] = useState("")
  const close = (open: boolean) => { if (!open) setActive(null) }
  return <div className="flex max-w-lg flex-wrap gap-ien-sm">
    {["저장", "삭제", "업데이트", "네트워크", "진행", "날짜", "시각", "텍스트 입력"].map((name) => <Button key={name} variant="weak" onClick={() => setActive(name)}>{name}</Button>)}
    <SaveAlertDialog open={active === "저장"} onOpenChange={close} onSave={() => setMessage("저장했습니다.")} onUnsave={() => setMessage("저장하지 않고 닫았습니다.")} />
    <DeleteAlertDialog open={active === "삭제"} onOpenChange={close} onConfirm={() => setMessage("삭제했습니다.")} />
    <UpdateAlertDialog open={active === "업데이트"} onOpenChange={close} appName="IENLAB" />
    <NetworkAlertDialog open={active === "네트워크"} onOpenChange={close} onRetry={() => setMessage("다시 시도합니다.")} />
    <ProgressDialog open={active === "진행"} onOpenChange={close} title="파일 처리 중" progress={0.4} onCancel={() => setMessage("취소했습니다.")} />
    <DatePickerDialog open={active === "날짜"} onOpenChange={close} value="2026-10-30" onConfirm={setMessage} />
    <TimePickerDialog open={active === "시각"} onOpenChange={close} value="09:30" onConfirm={setMessage} />
    <TextFieldDialog open={active === "텍스트 입력"} onOpenChange={close} title="이름 변경" fields={[{ name: "name", label: "이름", initialValue: "홍길동", required: true, validate: (value) => value.trim().length < 2 ? "두 글자 이상 입력해주세요." : undefined }]} onConfirm={(values) => setMessage(values.name)} />
    <p aria-live="polite" className="w-full text-sm text-muted-foreground">{message}</p>
  </div>
}
export default Pattern
