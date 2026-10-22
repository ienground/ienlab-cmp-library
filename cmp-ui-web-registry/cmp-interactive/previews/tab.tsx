"use client";

import * as React from "react";
import { Tab } from "@/components/ui/tab";

export default function Preview() {
  const [value, setValue] = React.useState("overview");
  return (
    <div className="grid max-w-sm gap-ien-sm">
      <Tab
        value={value}
        onValueChange={setValue}
        fluid
        ariaLabel="문서 탭"
        items={[
          {
            value: "overview",
            label: "개요",
            content: <p>구성 요소와 주요 정보를 확인합니다.</p>,
            badge: 3,
          },
          {
            value: "activity",
            label: "활동",
            content: <p>최근 활동 내역을 표시합니다.</p>,
            redBean: true,
          },
          {
            value: "settings",
            label: "설정",
            content: <p>표시 옵션을 조정합니다.</p>,
          },
          {
            value: "history",
            label: "변경 기록",
            content: <p>최근 변경 내용을 확인합니다.</p>,
          },
          {
            value: "members",
            label: "참여자",
            content: <p>참여자와 권한을 확인합니다.</p>,
          },
        ]}
      />
      <p aria-live="polite" className="text-ien-caption">
        선택 탭: {value}
      </p>
    </div>
  );
}
