"use client"

import { Toaster, toast, snackbar } from "@/components/ui/toast"
import { Button } from "@/components/ui/button"

export function Pattern() {
  return <div className="flex w-full max-w-lg flex-col gap-4"><><Toaster /><Button onClick={() => toast.success("저장했습니다.")}>성공 알림</Button><Button variant="weak" onClick={() => snackbar("항목을 보관했습니다.", { label: "실행 취소", onClick: () => toast("실행을 취소했습니다.") })}>작업 알림</Button></></div>
}
export default Pattern
