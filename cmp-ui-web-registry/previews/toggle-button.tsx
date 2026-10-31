"use client"

import { ToggleButton, IconToggleButton } from "@/components/ui/toggle-button"

export function Pattern() {
  return <div className="flex w-full max-w-lg flex-col gap-4"><><ToggleButton>즐겨찾기</ToggleButton><ToggleButton defaultPressed>선택됨</ToggleButton><IconToggleButton aria-label="즐겨찾기">★</IconToggleButton><ToggleButton disabled>사용 불가</ToggleButton></></div>
}
export default Pattern
