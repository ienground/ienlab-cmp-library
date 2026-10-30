"use client";

import * as React from "react";
import { Rating } from "@/components/ui/rating";

export default function Preview() {
  const [value, setValue] = React.useState(3);
  return (
    <div className="grid justify-items-start gap-ien-xs">
      <Rating value={value} onValueChange={setValue} />
      <p aria-live="polite" className="text-ien-caption">
        평점: {value}점
      </p>
    </div>
  );
}
