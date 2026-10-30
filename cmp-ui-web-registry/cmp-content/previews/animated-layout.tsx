"use client";
import * as React from "react";
import { IenAnimatedColumn } from "@/components/ui/animated-layout";
export default function Preview() {
  const [items, setItems] = React.useState([
    "첫 번째 항목",
    "두 번째 항목",
    "세 번째 항목",
  ]);
  return (
    <div className="space-y-3">
      <div className="flex gap-2">
        <button
          onClick={() =>
            setItems((value) => [...value, `새 항목 ${value.length + 1}`])
          }
        >
          추가
        </button>
        <button onClick={() => setItems((value) => value.slice(0, -1))}>
          제거
        </button>
        <button onClick={() => setItems((value) => [...value].reverse())}>
          순서 바꾸기
        </button>
      </div>
      <IenAnimatedColumn
        items={items}
        itemKey={(item) => item}
        gap={8}
        renderItem={(item) => (
          <div className="rounded-ien-default bg-ien-surface-weak p-3">
            {item}
          </div>
        )}
      />
    </div>
  );
}
