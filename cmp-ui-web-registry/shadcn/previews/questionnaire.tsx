"use client";
import * as React from "react";
import { Questionnaire } from "@/components/ui/questionnaire";
export default function Preview() {
  const [done, setDone] = React.useState(false);
  return done ? (
    <p role="status">답변을 제출했습니다.</p>
  ) : (
    <Questionnaire
      questions={[
        {
          id: "device",
          title: "주로 어떤 기기를 사용하나요?",
          options: [
            { value: "phone", label: "스마트폰" },
            { value: "desktop", label: "데스크톱" },
          ],
        },
      ]}
      onComplete={() => setDone(true)}
    />
  );
}
