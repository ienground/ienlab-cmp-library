"use client"

import { useState } from "react"
import { Rating } from "@/components/reui/rating"

export function Pattern() {
  const [value, setValue] = useState(3.8)
  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-col gap-2">
        <span className="text-sm text-muted-foreground">별점을 누르거나 드래그하세요. 방향키로도 조정할 수 있습니다.</span>
        <Rating value={value} onValueChange={setValue} max={5} readOnly={false} size="medium" variant="full" disabled={false} enabled ariaLabel="만족도 평가" />
      </div>
      <div className="flex flex-wrap items-center gap-4">
        <Rating value={value} readOnly variant="full" ariaLabel="전체 별점" />
        <Rating value={value} readOnly variant="compact" ariaLabel="요약 별점" />
        <Rating value={value} readOnly variant="iconOnly" ariaLabel="비례 채움 별점" />
        <Rating value={value} onValueChange={setValue} disabled ariaLabel="비활성 별점" />
      </div>
      <div className="flex flex-wrap items-center gap-4">
        <Rating value={3.8} readOnly size="tiny" ariaLabel="아주 작은 별점" />
        <Rating value={3.8} readOnly size="small" ariaLabel="작은 별점" />
        <Rating value={3.8} readOnly size="medium" ariaLabel="중간 별점" />
        <Rating value={3.8} readOnly size="large" ariaLabel="큰 별점" />
        <Rating value={3.8} readOnly size="big" ariaLabel="아주 큰 별점" />
      </div>
    </div>
  )
}
export default Pattern
