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
          <div className="rounded-ien-default bg-ien-surface-variant p-ien-sm">
            자료를 확인해 주세요.
          </div>
          <MessageFooter>읽음</MessageFooter>
        </MessageContent>
      </Message>
      <Message align="end">
        <MessageContent>
          <div className="rounded-ien-default bg-ien-brand p-ien-sm text-ien-on-brand">
            확인했습니다.
          </div>
          <MessageFooter>전송됨</MessageFooter>
        </MessageContent>
      </Message>
    </MessageGroup>
  );
}
