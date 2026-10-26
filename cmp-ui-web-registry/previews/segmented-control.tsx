"use client"

import { useState } from "react"
import { SegmentedControl } from "@/components/ui/segmented-control"

const items = [
  { value: "all", label: "전체" },
  { value: "active", label: "진행 중" },
  { value: "done", label: "완료" },
]

export function Pattern() {
  const [value, setValue] = useState("all")
  return (
    <div className="flex w-full max-w-lg flex-col gap-6">
      <SegmentedControl items={items} value={value} onValueChange={setValue} aria-label="작업 상태" />
      <p className="text-sm text-muted-foreground" aria-live="polite">선택: {items.find((item) => item.value === value)?.label}</p>
      <SegmentedControl items={items} size="large" defaultValue="active" aria-label="큰 크기" />
      <div className="max-w-64">
        <SegmentedControl alignment="fluid" items={[...items, { value: "waiting", label: "검토 대기 중" }, { value: "blocked", label: "선택 불가", enabled: false }]} aria-label="가로 스크롤" />
      </div>
      <SegmentedControl items={items} enabled={false} aria-label="비활성화" />
    </div>
  )
}

export default Pattern
