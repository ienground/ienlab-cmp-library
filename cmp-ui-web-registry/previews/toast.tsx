"use client"

import { Toaster, toast, snackbar } from "@/components/ui/toast"
import { Button } from "@/components/ui/button"

export function Pattern() {
  return (
    <div className="flex w-full max-w-lg flex-col gap-4">
      <Toaster />
      <Button onClick={() => toast("클립보드에 복사했습니다.")}>기본 알림</Button>
      <div className="flex flex-wrap gap-2">
        <Button variant="weak" tone="success" onClick={() => toast.success("저장했습니다.")}>성공 알림</Button>
        <Button variant="weak" tone="danger" onClick={() => toast.error("저장하지 못했습니다.")}>오류 알림</Button>
        <Button variant="weak" tone="warning" onClick={() => toast.warning("입력 내용을 확인해 주세요.")}>주의 알림</Button>
        <Button variant="weak" tone="info" onClick={() => toast.info("새로운 업데이트가 있습니다.")}>정보 알림</Button>
      </div>
      <Button variant="weak" onClick={() => snackbar("항목을 보관했습니다.", { label: "실행 취소", onClick: () => toast("실행을 취소했습니다.") })}>작업 알림</Button>
      <Button variant="line" onClick={() => toast("변경 사항을 저장했습니다. 다른 기기에서도 같은 내용을 확인할 수 있습니다.")}>긴 메시지</Button>
    </div>
  )
}
export default Pattern
