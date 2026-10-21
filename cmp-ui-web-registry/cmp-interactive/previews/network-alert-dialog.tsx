"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { NetworkAlertDialog } from "@/components/ui/network-alert-dialog";

export default function Preview() {
  const [open, setOpen] = React.useState(true);
  const [attempts, setAttempts] = React.useState(0);
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <Button variant="line" tone="neutral" onClick={() => setOpen(true)}>
        연결 오류 보기
      </Button>
      <NetworkAlertDialog
        open={open}
        onOpenChange={setOpen}
        onRetry={() => {
          setAttempts((value) => value + 1);
          setOpen(false);
        }}
      />
      <p aria-live="polite" className="text-ien-caption">
        재시도: {attempts}회
      </p>
    </div>
  );
}
