"use client";

import * as React from "react";
import { IenSwitch } from "@/components/ui/switch";

export default function Preview() {
  const [checked, setChecked] = React.useState(true);

  return (
    <div className="flex items-center gap-ien-sm">
      <IenSwitch
        checked={checked}
        onCheckedChange={setChecked}
        aria-label="알림 사용"
      />
      <span>알림 {checked ? "사용" : "중지"}</span>
    </div>
  );
}
