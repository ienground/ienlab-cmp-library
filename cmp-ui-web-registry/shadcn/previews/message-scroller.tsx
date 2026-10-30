"use client";
import { MessageScroller } from "@/components/ui/message-scroller";
import { Message, MessageContent } from "@/components/ui/message";
export default function Preview() {
  return (
    <MessageScroller className="h-48 rounded-ien-default border border-ien-border p-ien-sm">
      {Array.from({ length: 8 }, (_, i) => (
        <Message key={i} align={i % 2 ? "end" : "start"}>
          <MessageContent>
            <div className="rounded-ien-default bg-ien-surface-variant p-ien-xs">
              메시지 {i + 1}
            </div>
          </MessageContent>
        </Message>
      ))}
    </MessageScroller>
  );
}
