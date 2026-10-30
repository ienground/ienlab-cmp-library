"use client";
import * as React from "react";
import { IenResult } from "@/components/ui/result";
export default function Preview() {
  const [tone, setTone] = React.useState<
    "success" | "failure" | "empty" | "info"
  >("success");
  return (
    <div className="space-y-3">
      <div className="flex gap-2">
        {(["success", "failure", "empty", "info"] as const).map((value) => (
          <button key={value} onClick={() => setTone(value)}>
            {value}
          </button>
        ))}
      </div>
      <IenResult
        tone={tone}
        title="저장이 완료되었습니다"
        description="변경 사항을 안전하게 저장했습니다."
        primaryAction={
          <button onClick={() => setTone("info")}>계속하기</button>
        }
      />
    </div>
  );
}
