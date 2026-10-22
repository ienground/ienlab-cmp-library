"use client";

import * as React from "react";
import { Copy, Pencil, Trash2 } from "lucide-react";
import { FabMenu } from "@/components/ui/fab-menu";

export default function Preview() {
  const [action, setAction] = React.useState("동작을 선택하세요.");
  return (
    <div className="grid min-h-64 content-end justify-items-end gap-ien-sm">
      <FabMenu
        actions={[
          {
            label: "편집",
            icon: <Pencil aria-hidden="true" />,
            onSelect: () => setAction("편집을 선택했습니다."),
          },
          {
            label: "복사",
            icon: <Copy aria-hidden="true" />,
            onSelect: () => setAction("복사했습니다."),
          },
          {
            label: "삭제",
            icon: <Trash2 aria-hidden="true" />,
            onSelect: () => setAction("삭제를 선택했습니다."),
          },
        ]}
      />
      <p aria-live="polite">{action}</p>
    </div>
  );
}
