import { LoadingIndicator } from "@/components/ui/loading-indicator"

export function Pattern() {
  return (
    <div className="flex items-center justify-center gap-6">
      <LoadingIndicator size={32} aria-label="작은 로딩 표시" />
      <LoadingIndicator aria-label="기본 로딩 표시" />
      <LoadingIndicator size={64} aria-label="큰 로딩 표시" />
    </div>
  )
}
export default Pattern
