"use client"

import { Section } from "@/components/ui/section"
import { Button } from "@/components/ui/button"
import { ListRow } from "@/components/ui/list-row"
import { Switch } from "@/components/ui/switch"

export function Pattern() {
  return <div className="flex w-full max-w-lg flex-col gap-4"><Section title="설정" description="서비스 이용 환경을 변경합니다." footer="변경 사항은 즉시 반영됩니다."><ListRow title="알림" trailing={<Switch aria-label="알림" />} border="indented" /><ListRow title="화면 설정" description="라이트·다크 모드를 설정합니다." /></Section></div>
}
export default Pattern
