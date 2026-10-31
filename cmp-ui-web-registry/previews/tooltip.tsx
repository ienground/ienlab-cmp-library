import { Badge } from "@/components/reui/badge"
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip"

export function Pattern() {
  return (
    <div className="flex items-center justify-center">
      <TooltipProvider>
        <Tooltip defaultOpen>
          <TooltipTrigger asChild>
            <Badge asChild variant="line">
              <button type="button">도움말</button>
            </Badge>
          </TooltipTrigger>
          <TooltipContent side="top">
            툴팁은 짧은 보조 설명에 사용합니다.
          </TooltipContent>
        </Tooltip>
      </TooltipProvider>
    </div>
  )
}
export default Pattern;
