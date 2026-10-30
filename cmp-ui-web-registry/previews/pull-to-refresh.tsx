"use client"

import { useEffect, useRef, useState } from "react"
import { PullToRefresh } from "@/components/ui/pull-to-refresh"

export function Pattern() {
  const [isRefreshing, setIsRefreshing] = useState(false)
  const [revision, setRevision] = useState(0)
  const timer = useRef<ReturnType<typeof setTimeout> | undefined>(undefined)
  useEffect(() => () => clearTimeout(timer.current), [])
  const refresh = () => {
    setIsRefreshing(true)
    // 프리뷰에서만 요청 시간을 재현합니다. 실제 사용 시 데이터 요청의 finally에서 종료합니다.
    timer.current = setTimeout(() => {
      setRevision((value) => value + 1)
      setIsRefreshing(false)
    }, 1400)
  }
  return (
    <div className="w-full max-w-md space-y-3">
      <p className="text-sm text-muted-foreground">목록 맨 위에서 아래로 당기거나 위로 더 스크롤해 보세요. 마우스 드래그 또는 목록에 포커스한 뒤 Alt+R로도 갱신할 수 있습니다.</p>
      <p className="text-sm text-muted-foreground" aria-live="polite">갱신 횟수: {revision}</p>
      <PullToRefresh isRefreshing={isRefreshing} onRefresh={refresh} className="h-80 rounded-2xl border border-(--ien-border) bg-(--ien-surface)" aria-label="최근 활동 목록">
        <ul className="divide-y divide-(--ien-border) px-4">
          {Array.from({ length: 12 }, (_, index) => (
            <li key={index} className="flex min-h-19 items-center gap-3 py-4">
              <span className="flex size-11 shrink-0 items-center justify-center rounded-xl bg-(--ien-brand-weak) text-(--ien-brand)" aria-hidden="true">{index + 1}</span>
              <div className="min-w-0">
                <p className="text-base font-semibold">최근 활동 {index + 1}</p>
                <p className="text-sm text-muted-foreground">{revision === 0 ? "기존 항목을 유지하며 새로고침합니다." : `${revision}번째 갱신 내용입니다.`}</p>
              </div>
            </li>
          ))}
        </ul>
      </PullToRefresh>
    </div>
  )
}

export default Pattern
