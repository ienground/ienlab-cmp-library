"use client";

import * as React from "react";
import { Plus } from "lucide-react";
import { Fab } from "@/components/ui/fab";

export default function Preview() {
  const [count, setCount] = React.useState(0);
  return (
    <div className="flex items-center gap-ien-md">
      <Fab label="추가" onClick={() => setCount((value) => value + 1)}>
        <Plus aria-hidden="true" />
      </Fab>
      <span aria-live="polite">추가한 항목: {count}</span>
    </div>
  );
}
