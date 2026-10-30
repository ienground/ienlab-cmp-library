"use client";
import * as React from "react";
import { IenBottomBar } from "@/components/ui/bottom-bar";
export default function Preview() {
  const [action, setAction] = React.useState("대기 중");
  return (
    <div className="relative min-h-36 rounded-ien-lg bg-ien-surface-weak p-4">
      <IenBottomBar
        actions={
          <>
            <button onClick={() => setAction("홈")}>홈</button>
            <button onClick={() => setAction("검색")}>검색</button>
          </>
        }
        floatingActionButton={
          <button aria-label="추가" onClick={() => setAction("추가")}>
            ＋
          </button>
        }
      />
      <p className="mt-20">마지막 동작: {action}</p>
    </div>
  );
}
