import { useState } from "react"
import { Button } from "@/components/ui/button"
import type { IenTone, IenVariant } from "@/lib/ien-theme"

const tones: IenTone[] = [
  "neutral",
  "brand",
  "success",
  "warning",
  "danger",
  "info",
]
const variants: IenVariant[] = ["fill", "weak", "line", "ghost"]

export function Pattern() {
  const [loading, setLoading] = useState(true)

  return (
    <div className="flex flex-col gap-3">
      {variants.map((variant) => (
        <div key={variant} className="flex flex-wrap gap-2">
          {tones.map((tone) => (
            <Button key={tone} tone={tone} variant={variant}>
              {tone}
            </Button>
          ))}
        </div>
      ))}
      <Button tone="brand" useGradient={false}>
        단색 채움
      </Button>
      <Button variant="line" onClick={() => setLoading((value) => !value)}>
        {loading ? "로딩 해제" : "로딩 시작"}
      </Button>
      {variants.map((variant) => (
        <div key={`loading-${variant}`} className="flex flex-wrap gap-2">
          {tones.map((tone) => (
            <Button key={tone} tone={tone} variant={variant} loading={loading}>
              {tone} 저장
            </Button>
          ))}
        </div>
      ))}
      <div className="flex flex-wrap gap-2">
        <Button size="xs" loading={loading}>아주 작게</Button>
        <Button size="sm" loading={loading}>작게</Button>
        <Button size="lg" loading={loading}>크게</Button>
        <Button size="icon" loading={loading} aria-label="즐겨찾기">★</Button>
        <Button asChild loading={loading}><a href="#button">링크 버튼</a></Button>
        <Button disabled loading={loading}>비활성 버튼</Button>
      </div>
    </div>
  )
}
export default Pattern
