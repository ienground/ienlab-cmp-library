"use client";

import * as React from "react";
import { TextField } from "@/components/ui/text-field";

export default function Preview() {
  const [value, setValue] = React.useState("");
  return (
    <div className="grid max-w-sm gap-ien-xs">
      <TextField
        label="이메일"
        type="email"
        value={value}
        onValueChange={setValue}
        placeholder="name@example.com"
        helperText="연락 가능한 주소를 입력하세요."
      />
      <p aria-live="polite" className="text-ien-caption">
        입력 길이: {value.length}
      </p>
    </div>
  );
}
