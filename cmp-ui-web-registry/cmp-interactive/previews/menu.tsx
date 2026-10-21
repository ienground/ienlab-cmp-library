"use client";

import * as React from "react";
import { Menu } from "@/components/ui/menu";

export default function Preview() {
  const [selected, setSelected] =
    React.useState("메뉴를 열고 동작을 고르세요.");
  return (
    <div className="flex items-center gap-ien-sm">
      <Menu
        items={[
          { label: "복사", onSelect: () => setSelected("복사했습니다.") },
          { label: "보관", onSelect: () => setSelected("보관했습니다.") },
          {
            label: "삭제",
            destructive: true,
            separatorBefore: true,
            onSelect: () => setSelected("삭제를 선택했습니다."),
          },
        ]}
      />
      <span aria-live="polite">{selected}</span>
    </div>
  );
}
