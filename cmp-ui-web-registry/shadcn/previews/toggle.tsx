"use client";

import * as React from "react";
import { BoldIcon, StarIcon } from "lucide-react";
import { IenToggleButton } from "@/components/ui/toggle";

export default function Preview() {
  const [bold, setBold] = React.useState(true);

  return (
    <div className="flex flex-wrap items-center gap-ien-xs">
      <IenToggleButton
        checked={bold}
        onCheckedChange={setBold}
        aria-label="굵게"
      >
        <BoldIcon />
        굵게
      </IenToggleButton>
      <IenToggleButton
        defaultChecked
        checkedTone="warning"
        uncheckedTone="neutral"
        aria-label="즐겨찾기"
      >
        <StarIcon />
        즐겨찾기
      </IenToggleButton>
      <IenToggleButton
        enabled={false}
        defaultChecked
        aria-label="비활성 토글"
      >
        비활성
      </IenToggleButton>
    </div>
  );
}
