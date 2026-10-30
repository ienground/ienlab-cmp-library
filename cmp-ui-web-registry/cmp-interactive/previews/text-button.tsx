"use client";

import * as React from "react";
import { TextButton } from "@/components/ui/text-button";

export default function Preview() {
  const [message, setMessage] = React.useState("버튼을 눌러 보세요.");
  return (
    <div className="grid justify-items-start gap-ien-xs">
      <TextButton
        emphasis="arrow"
        onClick={() => setMessage("더 보기를 선택했습니다.")}
      >
        더 보기
      </TextButton>
      <p aria-live="polite" className="text-ien-caption">
        {message}
      </p>
    </div>
  );
}
