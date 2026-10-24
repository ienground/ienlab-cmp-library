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
    </div>
  )
}
export default Pattern
