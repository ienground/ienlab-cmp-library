import {
  XScrollArea,
  XScrollAreaContent,
  XScrollAreaNext,
  XScrollAreaPrevious,
} from "@/components/ui/x-scroll-area"

const pages = ["로그인", "대시보드", "설정", "결제", "온보딩", "장바구니", "상품 상세", "검색"]

export function Pattern() {
  return (
    <XScrollArea className="w-full max-w-xl" aria-label="화면 예시">
      <XScrollAreaContent>
        {pages.map((page) => (
          <div key={page} className="flex h-32 w-36 shrink-0 items-center justify-center rounded-lg border bg-card text-sm font-medium">
            {page}
          </div>
        ))}
      </XScrollAreaContent>
      <XScrollAreaPrevious />
      <XScrollAreaNext />
    </XScrollArea>
  )
}
export default Pattern
