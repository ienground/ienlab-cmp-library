"use client";
import * as React from "react";
import {
  IenCircularProgressIndicator,
  IenCircularWavyProgressIndicator,
  IenLinearProgressIndicator,
} from "@/components/ui/progress-indicator";
export default function Preview() {
  const [value, setValue] = React.useState(0.55);
  return (
    <div className="flex flex-wrap items-center gap-5">
      <button onClick={() => setValue((v) => (v >= 1 ? 0 : v + 0.15))}>
        진행률 변경
      </button>
      <IenCircularProgressIndicator progress={value} />
      <IenCircularWavyProgressIndicator />
      <div className="w-40">
        <IenLinearProgressIndicator progress={value} />
      </div>
      <div className="w-40">
        <IenLinearProgressIndicator />
      </div>
    </div>
  );
}
