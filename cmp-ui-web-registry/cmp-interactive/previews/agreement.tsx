"use client";

import * as React from "react";
import { Agreement } from "@/components/ui/agreement";

export default function Preview() {
  const [value, setValue] = React.useState<string[]>([]);
  return (
    <div className="grid gap-ien-sm">
      <Agreement
        value={value}
        onValueChange={setValue}
        items={[
          { id: "terms", label: "서비스 이용약관 동의", required: true },
          { id: "privacy", label: "개인정보 처리 동의", required: true },
          {
            id: "marketing",
            label: "혜택 및 소식 수신 동의",
            description: "선택 항목입니다.",
          },
        ]}
      />
      <p aria-live="polite" className="text-ien-caption">
        동의 {value.length}개
      </p>
    </div>
  );
}
