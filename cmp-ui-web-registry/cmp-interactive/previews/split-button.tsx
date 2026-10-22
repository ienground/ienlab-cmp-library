"use client";

import * as React from "react";
import { SplitButton } from "@/components/ui/split-button";

export default function Preview() {
  const [expanded, setExpanded] = React.useState(false);
  const [message, setMessage] = React.useState("저장할 수 있습니다.");
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <SplitButton
        label="저장"
        size="large"
        display="inline"
        state={{ enabled: true, loading: false }}
        expanded={expanded}
        onExpandedChange={setExpanded}
        onClick={() => setMessage("저장했습니다.")}
        actions={[
          {
            label: "다른 이름으로 저장",
            onSelect: () => setMessage("다른 이름으로 저장을 선택했습니다."),
          },
          {
            label: "초안 저장",
            onSelect: () => setMessage("초안을 저장했습니다."),
          },
        ]}
      />
      <SplitButton
        label="공유"
        size="small"
        display="block"
        onClick={() => setMessage("공유 동작을 실행했습니다.")}
        actions={[
          { label: "링크 복사", onSelect: () => setMessage("링크를 복사했습니다.") },
        ]}
      />
      <p aria-live="polite" className="text-ien-body2">
        {message}
      </p>
    </div>
  );
}
