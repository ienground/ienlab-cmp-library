"use client";

import * as React from "react";
import { NumericSpinner } from "@/components/ui/numeric-spinner";

export default function Preview() {
  const [value, setValue] = React.useState(2);
  return (
    <div className="grid justify-items-start gap-ien-xs">
      <NumericSpinner
        label="수량"
        value={value}
        min={0}
        max={10}
        onValueChange={setValue}
        numberFormatter={(current) => `${current}개`}
      />
      <NumericSpinner
        label="추가 수량"
        defaultNumber={2}
        minNumber={0}
        maxNumber={5}
        size="small"
        disable
      />
      <p aria-live="polite" className="text-ien-caption">
        현재 수량: {value}
      </p>
    </div>
  );
}
