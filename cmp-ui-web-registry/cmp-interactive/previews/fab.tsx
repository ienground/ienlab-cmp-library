"use client";

import * as React from "react";
import { Plus } from "lucide-react";
import { Fab } from "@/components/ui/fab";

export default function Preview() {
  const [count, setCount] = React.useState(0);
  return (
    <div className="grid justify-items-start gap-ien-md">
      <div className="flex flex-wrap items-center gap-ien-md">
        <Fab size="small" label="추가" onClick={() => setCount((value) => value + 1)}>
          <Plus aria-hidden="true" />
        </Fab>
        <Fab label="추가" onClick={() => setCount((value) => value + 1)}>
          <Plus aria-hidden="true" />
        </Fab>
        <Fab size="large" label="추가" onClick={() => setCount((value) => value + 1)}>
          <Plus aria-hidden="true" />
        </Fab>
        <Fab isExtended label="새 항목" onClick={() => setCount((value) => value + 1)}>
          <Plus aria-hidden="true" />
        </Fab>
      </div>
      <span aria-live="polite">추가한 항목: {count}</span>
    </div>
  );
}
