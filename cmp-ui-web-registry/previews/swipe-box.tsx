"use client"

import { useState } from "react"
import { SwipeBox } from "@/components/ui/swipe-box"
import { ListRow } from "@/components/ui/list-row"

export function Pattern() {
  const [message, setMessage] = useState("")
  return <div className="w-full max-w-lg space-y-ien-md">
    <p className="text-sm text-muted-foreground">행을 좌우로 밀거나 작업 버튼을 누르세요.</p>
    <SwipeBox startActions={[{ id: "read", label: "읽음", onClick: () => setMessage("읽음으로 표시했습니다.") }]} endActions={[{ id: "archive", label: "보관", onClick: () => setMessage("보관했습니다.") }, { id: "delete", label: "삭제", tone: "danger", onClick: () => setMessage("삭제했습니다.") }]}>
      <ListRow title="새 메시지" description="일정 확인 부탁드립니다." />
    </SwipeBox>
    <SwipeBox disabled endActions={[{ id: "delete", label: "삭제", tone: "danger", onClick: () => setMessage("삭제했습니다.") }]}><ListRow title="비활성화된 행" /></SwipeBox>
    <p aria-live="polite" className="text-sm text-muted-foreground">{message}</p>
  </div>
}
export default Pattern
