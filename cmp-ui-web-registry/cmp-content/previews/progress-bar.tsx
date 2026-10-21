"use client";
import * as React from "react";
import { IenProgressBar } from "@/components/ui/progress-bar";
export default function Preview() {
  const [value, setValue] = React.useState(0.42);
  return (
    <div className="space-y-3">
      <IenProgressBar
        progress={value}
        animate
        showLabel
        contentDescription="업로드 진행률"
      />
      <button
        onClick={() => setValue((v) => (v >= 1 ? 0 : Math.min(1, v + 0.2)))}
      >
        진행률 높이기
      </button>
    </div>
  );
}
