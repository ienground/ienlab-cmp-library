"use client";

import * as React from "react";
import { Keypad } from "@/components/ui/keypad";

export default function Preview() {
  const [value, setValue] = React.useState("");
  const [verified, setVerified] = React.useState(false);
  const [layout, setLayout] = React.useState<"number" | "secureNumber" | "fullSecure" | "secureKeypad">("number");
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <div className="flex flex-wrap gap-ien-xs">
        {(["number", "secureNumber", "fullSecure", "secureKeypad"] as const).map((option) => (
          <button
            key={option}
            type="button"
            aria-pressed={layout === option}
            onClick={() => setLayout(option)}
            className="rounded-ien-sm border border-ien-border px-ien-sm py-ien-xs text-ien-label2 aria-pressed:border-ien-brand aria-pressed:text-ien-brand"
          >
            {option === "number"
              ? "숫자"
              : option === "secureNumber"
                ? "보안 숫자"
                : option === "fullSecure"
                  ? "보안 키보드"
                  : "무작위 빈 칸"}
          </button>
        ))}
      </div>
      <Keypad
        value={value}
        onValueChange={setValue}
        maxLength={6}
        layout={layout}
        randomized={layout !== "number"}
        secure={layout === "secureNumber"}
        onSpecialClick={() => setVerified(false)}
        onSubmit={() => setVerified(true)}
      />
      <p aria-live="polite" className="text-ien-caption">
        {verified ? "코드를 확인했습니다." : "숫자 코드를 입력하세요."}
      </p>
    </div>
  );
}
