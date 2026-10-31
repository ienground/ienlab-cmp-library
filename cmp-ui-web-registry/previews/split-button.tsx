"use client"

import { useState } from "react"
import { SplitButton } from "@/components/ui/split-button"
import { DropdownMenuItem } from "@/components/ui/dropdown-menu"

export function Pattern() {
  const [message, setMessage] = useState("")
  const menu = <><DropdownMenuItem>다른 이름으로 저장</DropdownMenuItem><DropdownMenuItem>내보내기</DropdownMenuItem></>
  return <div className="flex w-full max-w-lg flex-col gap-ien-lg">
    <div className="flex flex-wrap items-center gap-ien-sm">
      {(["fill", "weak", "line", "ghost"] as const).map((variant) => (
        <SplitButton key={variant} variant={variant} menu={menu} onClick={() => setMessage("저장했습니다.")}>{variant}</SplitButton>
      ))}
    </div>
    <div className="flex flex-wrap items-center gap-ien-sm">
      {(["sm", "md", "lg"] as const).map((size) => (
        <SplitButton key={size} size={size} menu={menu}>{size}</SplitButton>
      ))}
    </div>
    <div className="flex flex-wrap items-center gap-ien-sm">
      <SplitButton disabled menu={menu}>비활성화</SplitButton>
      <SplitButton loading menu={menu}>저장 중</SplitButton>
    </div>
    <SplitButton display="block" menu={menu}>전체 너비</SplitButton>
    <SplitButton display="full" menu={menu}>모서리 없는 전체 너비</SplitButton>
    <p aria-live="polite" className="text-sm text-muted-foreground">{message}</p>
  </div>
}
export default Pattern
