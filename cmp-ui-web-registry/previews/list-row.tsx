"use client"

import { useState } from "react"
import { ListRow, ListHeader, ListFooter, BoardRow, TableRow } from "@/components/ui/list-row"
import { Button } from "@/components/ui/button"
import { Switch } from "@/components/ui/switch"

export function Pattern() {
  const [message, setMessage] = useState("")
  return <div className="flex w-full max-w-lg flex-col gap-4"><><div className="w-full"><ListHeader>계정</ListHeader><ListRow title="프로필" description="프로필 정보를 관리합니다." leading="●" trailing="›" border="indented" onClick={() => setMessage("프로필 선택")} /><ListRow title="알림" trailing={<Switch aria-label="알림 허용" />} /><ListRow title="사용 불가" disabled onClick={() => {}} /><BoardRow title="공지사항" description="서비스 이용 안내" metadata="오늘" /><ListFooter>계정 정보는 안전하게 보관됩니다.</ListFooter></div><table className="w-full"><tbody><TableRow><th>상태</th><td>정상</td></TableRow></tbody></table></><p aria-live="polite" className="text-sm text-muted-foreground">{message}</p></div>
}
export default Pattern
