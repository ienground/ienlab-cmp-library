"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { BottomSheet } from "@/components/ui/bottom-sheet";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  return (
    <>
      <Button onClick={() => setOpen(true)}>시트 열기</Button>
      <BottomSheet
        open={open}
        onOpenChange={setOpen}
        title="선택 옵션"
        description="원하는 항목을 선택하세요."
      >
        <div className="grid gap-ien-xs py-ien-md">
          {["최근 항목", "즐겨찾기", "모든 항목"].map((item) => (
            <Button
              key={item}
              variant="weak"
              tone="neutral"
              onClick={() => setOpen(false)}
            >
              {item}
            </Button>
          ))}
        </div>
      </BottomSheet>
    </>
  );
}
