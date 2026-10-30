"use client";
import * as React from "react";
import { IenRangeSlider, IenSlider } from "@/components/ui/slider";
export default function Preview() {
  const [value, setValue] = React.useState(35);
  const [range, setRange] = React.useState<[number, number]>([20, 75]);
  return (
    <div className="grid gap-ien-xl">
      <IenSlider
        label="볼륨"
        value={value}
        valueRange={[0, 100]}
        onValueChange={setValue}
        valueLabel={`${value}%`}
      />
      <IenRangeSlider
        label="가격 범위"
        value={range}
        valueRange={[0, 100]}
        onValueChange={setRange}
        valueLabel={`${range[0]}–${range[1]}`}
      />
    </div>
  );
}
