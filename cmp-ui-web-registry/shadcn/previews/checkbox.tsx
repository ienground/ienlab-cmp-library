"use client";
import * as React from "react";
import {
  IenCircleCheckbox,
  IenDotCheckbox,
  IenLineCheckbox,
} from "@/components/ui/checkbox";
export default function Preview() {
  const [checked, setChecked] = React.useState(true);
  return (
    <div className="grid gap-ien-xxs">
      <IenCircleCheckbox
        checked={checked}
        onCheckedChange={setChecked}
        label="서비스 이용에 동의합니다."
      />
      <IenDotCheckbox defaultChecked label="점 형태 선택" />
      <IenLineCheckbox label="라인 형태 선택" />
    </div>
  );
}
