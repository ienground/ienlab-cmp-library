"use client";

import * as React from "react";
import { RadioButton } from "@/components/ui/radio-button";

export default function Preview() {
  const [value, setValue] = React.useState("standard");
  return (
    <div className="grid gap-ien-sm">
      <RadioButton
        name="delivery"
        value={value}
        onValueChange={setValue}
        options={[
          { value: "standard", label: "일반 배송", description: "2~3일 소요" },
          { value: "express", label: "빠른 배송", description: "내일 도착" },
        ]}
      />
      <p aria-live="polite" className="text-ien-caption">
        선택: {value}
      </p>
    </div>
  );
}
