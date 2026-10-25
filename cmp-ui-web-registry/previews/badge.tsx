import { Badge } from "@/components/reui/badge"
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
      <div className="flex flex-wrap items-center gap-2">
        <Badge size="small">Small</Badge>
        <Badge size="medium">Medium</Badge>
        <Badge size="large">Large</Badge>
      </div>
      {variants.map((variant) => (
        <div key={variant} className="flex flex-wrap gap-2">
          {tones.map((tone) => (
            <Badge key={tone} tone={tone} variant={variant}>
              {tone}
            </Badge>
          ))}
        </div>
      ))}
    </div>
  )
}
export default Pattern
