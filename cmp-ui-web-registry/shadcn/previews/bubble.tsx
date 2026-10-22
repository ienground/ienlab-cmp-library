import {
  Bubble,
  BubbleContent,
  BubbleGroup,
  IenBubble,
} from "@/components/ui/bubble";
export default function Preview() {
  return (
    <BubbleGroup className="w-full">
      <IenBubble tone="neutral" tail="start" align="start">
        <BubbleContent>중립 톤의 시작 꼬리 말풍선입니다.</BubbleContent>
      </IenBubble>
      <IenBubble tone="brand" tail="end" align="end">
        <BubbleContent>브랜드 톤의 끝 꼬리 말풍선입니다.</BubbleContent>
      </IenBubble>
      <IenBubble tone="warning" tail="start" align="start">
        <BubbleContent>주의 톤도 같은 형태로 표시합니다.</BubbleContent>
      </IenBubble>
      <Bubble background="grey">
        <BubbleContent>기존 배경 API와 말풍선 형태도 유지합니다.</BubbleContent>
      </Bubble>
    </BubbleGroup>
  );
}
