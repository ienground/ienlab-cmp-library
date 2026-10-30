"use client";
import * as React from "react";
import { IenListFooter } from "@/components/ui/list-footer";
export default function Preview() {
  const [count, setCount] = React.useState(3);
  return (
    <div className="rounded-ien-default border border-ien-border">
      <p className="p-4">표시된 항목 {count}개</p>
      <IenListFooter
        border="indented"
        icon={<span aria-hidden>›</span>}
        onClick={() => setCount((value) => value + 1)}
      >
        더 보기
      </IenListFooter>
    </div>
  );
}
