import {
  Bubble,
  BubbleContent,
  BubbleGroup,
  IenBubble,
} from "@/components/ui/bubble";
export default function Preview() {
  return (
    <BubbleGroup>
      <Bubble background="grey">
        <BubbleContent>안녕하세요. 확인하고 있어요.</BubbleContent>
      </Bubble>
      <IenBubble background="Brand">
        <BubbleContent>IEN 말풍선입니다.</BubbleContent>
      </IenBubble>
      <IenBubble background="Grey">
        <BubbleContent>기본 꼬리와 정렬을 확인할 수 있습니다.</BubbleContent>
      </IenBubble>
    </BubbleGroup>
  );
}
