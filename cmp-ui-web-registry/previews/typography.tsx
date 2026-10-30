import { Typography } from "@/components/ui/typography"

export function Pattern() {
  return (
    <div className="space-y-4">
      <Typography variant="display" asChild><h1>페이지 제목</h1></Typography>
      <Typography variant="title1" asChild><h2>섹션 제목</h2></Typography>
      <Typography variant="title2" asChild><h3>소제목</h3></Typography>
      <Typography variant="title3" asChild><h4>세부 제목</h4></Typography>
      <Typography>본문 내용입니다.</Typography>
      <Typography variant="caption">보조 설명입니다.</Typography>
    </div>
  )
}
export default Pattern
