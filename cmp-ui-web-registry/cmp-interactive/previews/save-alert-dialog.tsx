"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { SaveAlertDialog } from "@/components/ui/save-alert-dialog";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [result, setResult] = React.useState("");
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <Button onClick={() => setOpen(true)}>닫기 시도</Button>
      <SaveAlertDialog
        open={open}
        onOpenChange={setOpen}
        buttonLayout="vertical"
        onSave={() => setResult("변경 사항 저장됨")}
        onDiscard={() => setResult("변경 사항 폐기됨")}
      />
      <p aria-live="polite" className="text-ien-caption">
        {result}
      </p>
    </div>
  );
}
