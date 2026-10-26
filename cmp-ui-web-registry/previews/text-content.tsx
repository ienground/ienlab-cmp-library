"use client"

import { HighlightText, Paragraph, Post, Bubble } from "@/components/ui/text-content"
import { Button } from "@/components/ui/button"

export function Pattern() {
  return <div className="flex w-full max-w-lg flex-col gap-4"><><Paragraph title="문단 제목" body="중요한 내용을 강조해 안내합니다." emphasis={["중요한 내용"]} footer="보조 설명입니다." bold /><Post title="새로운 소식" author="아이엔랩" description="포스트의 본문과 미디어를 함께 구성합니다." metadata="오늘" action={<Button variant="ghost">자세히 보기</Button>} /><Bubble tone="brand">안녕하세요.</Bubble><HighlightText text="일치하는 문자열을 안전하게 강조합니다." highlights={["문자열"]} /></></div>
}
export default Pattern
