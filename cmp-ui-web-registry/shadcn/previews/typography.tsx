import {
  TypographyBlockquote,
  TypographyH2,
  TypographyInlineCode,
  TypographyLead,
  TypographyList,
  TypographyP,
} from "@/components/ui/typography";
export default function Preview() {
  return (
    <article className="grid gap-ien-md">
      <TypographyH2>기본 문서 스타일</TypographyH2>
      <TypographyLead>읽기 쉬운 설명을 위한 타이포그래피입니다.</TypographyLead>
      <TypographyP>
        문장과 <TypographyInlineCode>inline code</TypographyInlineCode>를 사용할
        수 있습니다.
      </TypographyP>
      <TypographyList>
        <li>첫 번째 항목</li>
        <li>두 번째 항목</li>
      </TypographyList>
      <TypographyBlockquote>중요한 내용을 강조합니다.</TypographyBlockquote>
    </article>
  );
}
