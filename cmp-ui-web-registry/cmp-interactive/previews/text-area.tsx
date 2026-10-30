"use client";

import * as React from "react";
import { TextArea } from "@/components/ui/text-area";

export default function Preview() {
  const [value, setValue] = React.useState("");
  return (
    <div className="grid max-w-md gap-ien-xs">
      <TextArea
        label="메모"
        value={value}
        onChange={(event) => setValue(event.currentTarget.value)}
        maxLength={120}
        placeholder="메모를 입력하세요."
        helperText={value.length + " / 120"}
      />
      <p aria-live="polite" className="text-ien-caption">
        미리보기: {value || "내용 없음"}
      </p>
    </div>
  );
}
