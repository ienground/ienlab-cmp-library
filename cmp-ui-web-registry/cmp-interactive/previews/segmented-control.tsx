"use client";

import * as React from "react";
import { SegmentedControl } from "@/components/ui/segmented-control";

export default function Preview() {
  const [fixedValue, setFixedValue] = React.useState("day");
  const [fluidValue, setFluidValue] = React.useState("recent");
  return (
    <div className="grid gap-ien-sm">
      <SegmentedControl
        size="small"
        value={fixedValue}
        onValueChange={(nextValue) => {
          if (nextValue !== null) setFixedValue(nextValue);
        }}
        options={[
          { value: "day", label: "일" },
          { value: "week", label: "주" },
          { value: "month", label: "월" },
        ]}
      />
      <SegmentedControl
        alignment="fluid"
        size="large"
        value={fluidValue}
        onValueChange={(nextValue) => {
          if (nextValue !== null) setFluidValue(nextValue);
        }}
        options={[
          { value: "recent", label: "최근 항목" },
          { value: "shared", label: "공유 항목" },
          { value: "archived", label: "보관된 항목" },
          { value: "disabled", label: "사용 불가", enabled: false },
        ]}
      />
      <p aria-live="polite" className="text-ien-caption">
        선택: {fixedValue} · {fluidValue}
      </p>
    </div>
  );
}
