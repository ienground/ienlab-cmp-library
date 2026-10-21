"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { DeleteAlertDialog } from "@/components/ui/delete-alert-dialog";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [exists, setExists] = React.useState(true);
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <Button
        variant="line"
        tone="danger"
        disabled={!exists}
        onClick={() => setOpen(true)}
      >
        항목 삭제
      </Button>
      <DeleteAlertDialog
        open={open}
        onOpenChange={setOpen}
        onConfirm={() => setExists(false)}
      />
      <p aria-live="polite" className="text-ien-caption">
        {exists ? "항목이 있습니다." : "항목을 삭제했습니다."}
      </p>
    </div>
  );
}
