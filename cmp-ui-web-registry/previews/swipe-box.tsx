"use client"

import { IconArchive, IconCheck, IconTrash } from "@tabler/icons-react"
import { useState } from "react"
import { SwipeBox } from "@/components/ui/swipe-box"
import { ListRow } from "@/components/ui/list-row"

export function Pattern() {
  const [message, setMessage] = useState("")
  return <div className="w-full max-w-lg space-y-ien-md">
    <p className="text-sm text-muted-foreground">행을 좌우로 밀어 액션을 확인하세요. 키보드 Tab으로 작업 버튼에 접근할 수 있습니다.</p>
    <SwipeBox startActions={[{ id: "read", label: "읽음", icon: <IconCheck />, tone: "success", onClick: () => setMessage("읽음으로 표시했습니다.") }]} endActions={[{ id: "archive", label: "보관", icon: <IconArchive />, onClick: () => setMessage("보관했습니다.") }, { id: "delete", label: "삭제", icon: <IconTrash />, tone: "danger", onClick: () => setMessage("삭제했습니다.") }]}>
      <ListRow title="새 메시지" description="일정 확인 부탁드립니다." />
    </SwipeBox>
    <SwipeBox disabled endActions={[{ id: "delete", label: "삭제", icon: <IconTrash />, tone: "danger", onClick: () => setMessage("삭제했습니다.") }]}><ListRow title="비활성화된 행" /></SwipeBox>
    <p aria-live="polite" className="text-sm text-muted-foreground">{message}</p>
  </div>
}
export default Pattern
