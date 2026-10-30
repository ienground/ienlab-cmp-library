"use client"

import { Result } from "@/components/ui/result"
import { Button } from "@/components/ui/button"

export function Pattern() {
  return <div className="flex w-full max-w-lg flex-col gap-4"><><Result title="저장했습니다" description="변경 사항이 반영되었습니다." tone="success" primaryAction={<Button>확인</Button>} /><Result title="불러오지 못했습니다" tone="failure" primaryAction={<Button variant="weak">다시 시도</Button>} /></></div>
}
export default Pattern
