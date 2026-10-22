"use client";

import * as React from "react";
import { CheckIcon, XIcon } from "lucide-react";
import { IenSwitch } from "@/components/ui/switch";

export default function Preview() {
  const [checked, setChecked] = React.useState(true);

  return (
    <div className="grid gap-ien-sm">
      <div className="flex items-center gap-ien-sm">
        <IenSwitch
          checked={checked}
          onCheckedChange={setChecked}
          onTrackContent={<CheckIcon className="size-3" />}
          offTrackContent={<XIcon className="size-3" />}
          aria-label="알림 사용"
        />
        <span className="text-ien-body2">알림 {checked ? "사용" : "중지"}</span>
      </div>
      <div className="flex items-center gap-ien-sm">
        <IenSwitch enabled={false} defaultChecked aria-label="잠긴 알림" />
        <span className="text-ien-body2">비활성 상태</span>
      </div>
    </div>
  );
}
