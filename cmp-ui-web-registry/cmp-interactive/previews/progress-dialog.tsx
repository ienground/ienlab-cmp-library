"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { ProgressDialog } from "@/components/ui/progress-dialog";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [variant, setVariant] = React.useState<"loading" | "wavy" | "circular" | "linear">("wavy");
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <div className="flex flex-wrap gap-ien-xs">
        {(["loading", "wavy", "circular", "linear"] as const).map((item) => (
          <Button
            key={item}
            variant={variant === item ? "fill" : "weak"}
            tone={variant === item ? "brand" : "neutral"}
            onClick={() => setVariant(item)}
          >
            {item}
          </Button>
        ))}
      </div>
      <Button
        onClick={() => setOpen(true)}
      >
        진행 상태 보기
      </Button>
      <ProgressDialog
        open={open}
        title="파일 업로드 중"
        description="잠시 기다려 주세요."
        variant={variant}
        progress={0.64}
      />
    </div>
  );
}
