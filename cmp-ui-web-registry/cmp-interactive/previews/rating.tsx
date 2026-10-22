"use client";

import * as React from "react";
import { Rating } from "@/components/ui/rating";

export default function Preview() {
  const [value, setValue] = React.useState(3);
  return (
    <div className="grid justify-items-start gap-ien-xs">
      <Rating value={value} onValueChange={setValue} size="medium" variant="Full" />
      <Rating value={4.2} readOnly size="small" variant="Compact" valueText="4.2 / 5" />
      <Rating value={value} onValueChange={setValue} size="tiny" disabled ariaLabel="변경할 수 없는 평점" />
      <p aria-live="polite" className="text-ien-caption">
        평점: {value}점
      </p>
    </div>
  );
}
