import { BottomInfo } from "@/components/ui/bottom-info"

export function Pattern() {
  return <div className="w-full max-w-lg space-y-ien-lg">
    <BottomInfo>입력한 내용은 확인 후 저장됩니다.</BottomInfo>
    <BottomInfo gradient="none">그라데이션 없이 안내를 표시할 수 있습니다.</BottomInfo>
  </div>
}
export default Pattern
