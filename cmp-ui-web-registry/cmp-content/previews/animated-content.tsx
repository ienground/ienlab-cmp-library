"use client";
import * as React from "react";
import { IenAnimatedContent } from "@/components/ui/animated-content";
export default function Preview() {
  const [state, setState] = React.useState("대기 중");
  return (
    <div className="space-y-3">
      <button
        onClick={() =>
          setState((current) =>
            current === "대기 중" ? "처리 완료" : "대기 중",
          )
        }
      >
        상태 전환
      </button>
      <IenAnimatedContent
        targetState={state}
        transition="fade-up"
        className="rounded-ien-default bg-ien-surface-weak p-4"
      >
        {state}
      </IenAnimatedContent>
    </div>
  );
}
