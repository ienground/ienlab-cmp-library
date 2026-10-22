"use client";

import * as React from "react";
import { ChatBottomBar } from "@/components/ui/chat-bottom-bar";

export default function Preview() {
  const [value, setValue] = React.useState("");
  const [visible, setVisible] = React.useState(true);
  const [messages, setMessages] = React.useState([
    "안녕하세요! 무엇을 도와드릴까요?",
  ]);
  return (
    <div className="grid gap-ien-sm rounded-ien-default border border-ien-border">
      <ul aria-live="polite" className="grid gap-ien-xs p-ien-sm">
        {messages.map((message, index) => (
          <li
            key={index}
            className="w-fit rounded-ien-default bg-ien-surface-weak px-ien-sm py-ien-xs text-ien-body2"
          >
            {message}
          </li>
        ))}
      </ul>
      <button
        type="button"
        aria-pressed={visible}
        onClick={() => setVisible((current) => !current)}
        className="justify-self-start text-ien-caption text-ien-brand"
      >
        입력창 {visible ? "숨기기" : "보이기"}
      </button>
      <ChatBottomBar
        value={value}
        onValueChange={setValue}
        inputState={{ enabled: true, readOnly: false, status: { type: "normal" } }}
        sendState={{ enabled: value.trim().length > 0, loading: false }}
        maxLines={4}
        visible={visible}
        leadingContent={
          <button type="button" aria-label="첨부 파일 추가" className="size-9 shrink-0 rounded-full">
            +
          </button>
        }
        trailingContent={
          <button type="button" aria-label="이모지 추가" className="size-9 shrink-0 rounded-full">
            ☺
          </button>
        }
        onSend={(message) => setMessages((items) => [...items, message])}
      />
    </div>
  );
}
