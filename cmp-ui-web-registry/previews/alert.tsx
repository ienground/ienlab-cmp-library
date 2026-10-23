import { Alert, AlertDescription, AlertTitle } from "@/components/reui/alert"

export function Pattern() {
  return (
    <div className="grid w-full gap-3">
      <Alert tone="info" variant="weak">
        <AlertTitle>안내</AlertTitle>
        <AlertDescription>
          의미적 tone과 약한 그라데이션을 사용합니다.
        </AlertDescription>
      </Alert>
      <Alert tone="success" variant="fill">
        <AlertTitle>완료</AlertTitle>
        <AlertDescription>
          채움 표현에서도 제목과 설명을 구분합니다.
        </AlertDescription>
      </Alert>
      <Alert tone="danger" variant="outline" useGradient={false}>
        <AlertTitle>확인이 필요합니다</AlertTitle>
        <AlertDescription>테두리 표현으로 상태를 전달합니다.</AlertDescription>
      </Alert>
    </div>
  )
}
export default Pattern
