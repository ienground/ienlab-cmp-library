"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { ProgressDialog } from "@/components/ui/progress-dialog";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [progress, setProgress] = React.useState(0);
  React.useEffect(() => {
    if (!open) return;
    const timer = window.setInterval(
      () =>
        setProgress((value) => {
          if (value >= 100) {
            window.clearInterval(timer);
            setOpen(false);
            return 0;
          }
          return value + 10;
        }),
      300,
    );
    return () => window.clearInterval(timer);
  }, [open]);
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <Button
        onClick={() => {
          setProgress(0);
          setOpen(true);
        }}
      >
        업로드 시작
      </Button>
      <ProgressDialog open={open} title="파일 업로드 중" value={progress} />
    </div>
  );
}
