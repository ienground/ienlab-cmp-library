"use client";

import * as React from "react";
import { PullToRefresh } from "@/components/ui/pull-to-refresh";

export default function Preview() {
  const [items, setItems] = React.useState([
    "새 항목 1",
    "새 항목 2",
    "새 항목 3",
  ]);
  return (
    <PullToRefresh
      onRefresh={() =>
        setItems((items) => [
          "새로 고침된 항목 " + (items.length + 1),
          ...items,
        ])
      }
      className="h-56 rounded-ien-default border border-ien-border"
    >
      <ul className="grid gap-ien-xs p-ien-md">
        {items.map((item, index) => (
          <li
            key={item + index}
            className="rounded-ien-sm bg-ien-surface-weak p-ien-sm"
          >
            {item}
          </li>
        ))}
      </ul>
    </PullToRefresh>
  );
}
