"use client";

import * as React from "react";
import { Keypad } from "@/components/ui/keypad";

export default function Preview() {
  const [value, setValue] = React.useState("");
  const [verified, setVerified] = React.useState(false);
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <Keypad
        value={value}
        onValueChange={setValue}
        maxLength={6}
        onSubmit={() => setVerified(true)}
      />
      <p aria-live="polite" className="text-ien-caption">
        {verified ? "코드를 확인했습니다." : "숫자 코드를 입력하세요."}
      </p>
    </div>
  );
}
