"use client";

import * as React from "react";
import { PullToRefresh } from "@/components/ui/pull-to-refresh";

export default function Preview() {
  const [refreshing, setRefreshing] = React.useState(false);
  const [distanceFraction, setDistanceFraction] = React.useState(0);
  const [items, setItems] = React.useState([
    "새 항목 1",
    "새 항목 2",
    "새 항목 3",
  ]);
  return (
    <PullToRefresh
      state={{ distanceFraction, onDistanceFractionChange: setDistanceFraction }}
      refreshing={refreshing}
      onRefresh={async () => {
        setRefreshing(true);
        await new Promise((resolve) => window.setTimeout(resolve, 500));
        setItems((current) => ["새로 고침된 항목 " + (current.length + 1), ...current]);
        setRefreshing(false);
      }}
      threshold={56}
      maxDistance={56}
      contentPadding={{ paddingTop: 8, paddingInline: 12, paddingBottom: 12 }}
      className="h-56 rounded-ien-default border border-ien-border"
    >
      <ul className="grid gap-ien-xs">
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
