"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import {
  BottomSheet,
  useBottomSheetState,
} from "@/components/ui/bottom-sheet";

export default function Preview() {
  const sheet = useBottomSheetState();
  return (
    <>
      <Button onClick={() => sheet.show("medium")}>시트 열기</Button>
      <BottomSheet
        state={sheet}
        header={<p className="text-ien-title3">선택 옵션</p>}
        headerDescription="원하는 항목을 선택하세요."
        cta={
          <Button className="w-full" onClick={() => sheet.hide()}>
            선택 완료
          </Button>
        }
      >
        <div className="grid gap-ien-xs py-ien-md">
          {["최근 항목", "즐겨찾기", "모든 항목"].map((item) => (
            <Button
              key={item}
              variant="weak"
              tone="neutral"
              onClick={() => sheet.hide()}
            >
              {item}
            </Button>
          ))}
        </div>
      </BottomSheet>
    </>
  );
}
