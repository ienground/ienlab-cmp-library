"use client";

import * as React from "react";
import { ChatBottomBar } from "@/components/ui/chat-bottom-bar";

export default function Preview() {
  const [value, setValue] = React.useState("");
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
      <ChatBottomBar
        value={value}
        onValueChange={setValue}
        onSend={(message) => setMessages((items) => [...items, message])}
      />
    </div>
  );
}
