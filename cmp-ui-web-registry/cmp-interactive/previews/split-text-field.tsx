"use client";

import * as React from "react";
import { SplitTextField } from "@/components/ui/split-text-field";

export default function Preview() {
  const [value, setValue] = React.useState("");
  return (
    <div className="grid gap-ien-sm">
      <SplitTextField
        label="인증 코드"
        length={6}
        value={value}
        onValueChange={setValue}
      />
      <p aria-live="polite" className="text-ien-caption">
        입력한 글자: {value.length} / 6
      </p>
    </div>
  );
}
