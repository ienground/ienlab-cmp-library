"use client";

import * as React from "react";
import {
  Checkbox,
  IenCircleCheckbox,
  IenDotCheckbox,
  IenLineCheckbox,
} from "@/components/ui/checkbox";

export default function Preview() {
  const [checked, setChecked] = React.useState(true);
  return (
    <div className="grid gap-ien-xxs">
      <label className="inline-flex min-h-[var(--ien-state-minimum-touch-target)] items-center gap-ien-xs text-ien-body2">
        <Checkbox
          checked={checked}
          onCheckedChange={(next) => setChecked(next === true)}
          aria-label="기본 체크박스"
        />
        기본 체크박스
      </label>
      <IenCircleCheckbox
        checked={checked}
        onCheckedChange={setChecked}
        label="원형 체크박스"
      />
      <IenDotCheckbox
        defaultChecked
        tone="success"
        label="그라데이션 점 체크박스"
      />
      <IenLineCheckbox label="라인 체크박스" />
      <IenCircleCheckbox enabled={false} defaultChecked label="비활성 선택" />
    </div>
  );
}
