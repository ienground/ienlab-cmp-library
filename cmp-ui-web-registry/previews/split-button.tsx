"use client"

import { useState } from "react"
import { SplitButton } from "@/components/ui/split-button"
import { Button } from "@/components/ui/button"
import { DropdownMenuItem } from "@/components/ui/dropdown-menu"

export function Pattern() {
  const [message, setMessage] = useState("")
  return <div className="flex w-full max-w-lg flex-col gap-4"><SplitButton menu={<><DropdownMenuItem>다른 이름으로 저장</DropdownMenuItem><DropdownMenuItem>내보내기</DropdownMenuItem></>} onClick={() => setMessage("저장했습니다.")}>저장</SplitButton><p aria-live="polite" className="text-sm text-muted-foreground">{message}</p></div>
}
export default Pattern
