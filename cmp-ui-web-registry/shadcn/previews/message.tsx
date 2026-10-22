import { BubbleContent, IenBubble } from "@/components/ui/bubble";
import {
  Message,
  MessageAvatar,
  MessageContent,
  MessageFooter,
  MessageGroup,
  MessageHeader,
} from "@/components/ui/message";
export default function Preview() {
  return (
    <MessageGroup>
      <Message>
        <MessageAvatar className="size-8">김</MessageAvatar>
        <MessageContent>
          <MessageHeader>지민 · 오전 10:24</MessageHeader>
          <IenBubble tone="neutral" tail="start">
            <BubbleContent>자료를 확인해 주세요.</BubbleContent>
          </IenBubble>
          <MessageFooter>읽음</MessageFooter>
        </MessageContent>
      </Message>
      <Message align="end">
        <MessageContent>
          <IenBubble tone="brand" tail="end">
            <BubbleContent>확인했습니다.</BubbleContent>
          </IenBubble>
          <MessageFooter>전송됨</MessageFooter>
        </MessageContent>
      </Message>
    </MessageGroup>
  );
}
