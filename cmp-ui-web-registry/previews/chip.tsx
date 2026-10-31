"use client"

import { useState } from "react"
import { IconPlus, IconSparkles } from "@tabler/icons-react"
import { Chip, type ChipKind } from "@/components/ui/chip"
import { Button } from "@/components/ui/button"

const kinds: ChipKind[] = ["assist", "filter", "input", "suggestion"]

export function Pattern() {
  const [selected, setSelected] = useState(false)
  const [items, setItems] = useState(["Kotlin", "Compose", "React"])
  const [action, setAction] = useState("작업을 선택하세요.")

  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-wrap items-center gap-3">
        <Chip leadingIcon={<IconPlus />} onClick={() => setAction("Assist 작업을 실행했습니다.")}>Assist</Chip>
        <Chip kind="filter" selected={selected} onSelectedChange={setSelected}>Filter</Chip>
        <Chip kind="filter" defaultSelected selectedIcon={<IconSparkles />}>커스텀 선택 아이콘</Chip>
        <Chip kind="filter" defaultSelected selectedIcon={null}>선택 아이콘 없음</Chip>
        <Chip kind="suggestion" leadingIcon={<IconSparkles />} onClick={() => setAction("추천 작업을 실행했습니다.")}>Suggestion</Chip>
      </div>
      <p className="text-sm text-muted-foreground" aria-live="polite">{action} 필터: {selected ? "선택됨" : "선택 안 됨"}</p>
      <div className="flex flex-wrap items-center gap-3">
        {items.map((item) => <Chip key={item} kind="input" defaultSelected avatar={<span aria-hidden="true">{item[0]}</span>} onRemove={() => setItems((previous) => previous.filter((value) => value !== item))}>{item}</Chip>)}
        {items.length < 3 && <Button variant="ghost" size="sm" onClick={() => setItems(["Kotlin", "Compose", "React"])}>항목 복원</Button>}
      </div>
      <div className="flex flex-wrap items-center gap-3">
        {kinds.map((kind) => <Chip key={kind} kind={kind} elevated defaultSelected>{kind} elevated</Chip>)}
      </div>
      <div className="flex flex-wrap items-center gap-3">
        <Chip disabled>비활성화</Chip>
        <Chip kind="filter" selected disabled>선택·비활성화</Chip>
        <Chip loading>불러오는 중</Chip>
        <Chip kind="input" loading defaultSelected onRemove={() => setAction("항목을 삭제했습니다.")}>선택·로딩</Chip>
      </div>
    </div>
  )
}
export default Pattern
