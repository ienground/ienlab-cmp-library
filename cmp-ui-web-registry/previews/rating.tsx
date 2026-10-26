"use client"

import { useState } from "react"
import { useIenLanguage } from "@/lib/ien-theme-context"
import { Rating } from "@/components/reui/rating"

export function Pattern() {
  const language = useIenLanguage("ko")
  const [value, setValue] = useState(3.8)
  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-col gap-2">
        <span className="text-sm text-muted-foreground">{language === "ko" ? "별점을 누르거나 드래그하세요. 방향키로도 조정할 수 있습니다." : "Click or drag to rate. You can also use the arrow keys."}</span>
        <Rating value={value} onValueChange={setValue} max={5} readOnly={false} size="medium" variant="full" disabled={false} enabled ariaLabel={language === "ko" ? "만족도 평가" : "Rate satisfaction"} />
      </div>
      <div className="flex flex-wrap items-center gap-4">
        <Rating value={value} readOnly variant="full" ariaLabel={language === "ko" ? "전체 별점" : "Full rating"} />
        <Rating value={value} readOnly variant="compact" ariaLabel={language === "ko" ? "요약 별점" : "Compact rating"} />
        <Rating value={value} readOnly variant="iconOnly" ariaLabel={language === "ko" ? "비례 채움 별점" : "Proportional rating"} />
        <Rating value={value} onValueChange={setValue} disabled ariaLabel={language === "ko" ? "비활성 별점" : "Disabled rating"} />
      </div>
      <div className="flex flex-wrap items-center gap-4">
        <Rating value={3.8} readOnly size="tiny" ariaLabel={language === "ko" ? "아주 작은 별점" : "Tiny rating"} />
        <Rating value={3.8} readOnly size="small" ariaLabel={language === "ko" ? "작은 별점" : "Small rating"} />
        <Rating value={3.8} readOnly size="medium" ariaLabel={language === "ko" ? "중간 별점" : "Medium rating"} />
        <Rating value={3.8} readOnly size="large" ariaLabel={language === "ko" ? "큰 별점" : "Large rating"} />
        <Rating value={3.8} readOnly size="big" ariaLabel={language === "ko" ? "아주 큰 별점" : "Big rating"} />
      </div>
    </div>
  )
}
export default Pattern
