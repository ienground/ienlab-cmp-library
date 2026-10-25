import { ProgressIndicator } from "@/components/ui/progress-indicator"

export function Pattern() {
  return (
    <div className="flex w-full max-w-sm flex-col gap-6">
      <div className="flex items-center justify-center gap-6">
        <ProgressIndicator aria-label="원형 불확정 진행" />
        <ProgressIndicator shape="wavy" aria-label="물결형 불확정 진행" />
        <ProgressIndicator shape="wavy" progress={0.65} aria-label="65% 진행" />
      </div>
      <ProgressIndicator variant="linear" aria-label="선형 불확정 진행" />
      <ProgressIndicator variant="linear" shape="wavy" progress={0.65} aria-label="물결형 65% 진행" />
    </div>
  )
}
export default Pattern
