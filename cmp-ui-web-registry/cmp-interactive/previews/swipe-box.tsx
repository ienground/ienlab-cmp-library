"use client";

import * as React from "react";
import { SwipeBox } from "@/components/ui/swipe-box";

export default function Preview() {
  const [message, setMessage] = React.useState(
    "카드를 좌우로 밀어 동작을 표시하세요.",
  );
  return (
    <div className="grid gap-ien-sm">
      <SwipeBox
        startAction={{
          label: "보관",
          onClick: () => setMessage("항목을 보관했습니다."),
        }}
        endAction={{
          label: "삭제",
          tone: "danger",
          onClick: () => setMessage("삭제 동작을 선택했습니다."),
        }}
      >
        <div className="p-ien-lg">{message}</div>
      </SwipeBox>
      <p aria-live="polite" className="text-ien-caption">
        {message}
      </p>
    </div>
  );
}
