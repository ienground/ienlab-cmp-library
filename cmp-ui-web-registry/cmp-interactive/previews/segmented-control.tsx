"use client";

import * as React from "react";
import { SegmentedControl } from "@/components/ui/segmented-control";

export default function Preview() {
  const [value, setValue] = React.useState("day");
  return (
    <div className="grid justify-items-start gap-ien-xs">
      <SegmentedControl
        value={value}
        onValueChange={setValue}
        options={[
          { value: "day", label: "일" },
          { value: "week", label: "주" },
          { value: "month", label: "월" },
        ]}
      />
      <p aria-live="polite" className="text-ien-caption">
        보기: {value}
      </p>
    </div>
  );
}
