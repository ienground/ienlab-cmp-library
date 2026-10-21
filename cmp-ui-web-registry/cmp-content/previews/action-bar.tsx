"use client";
import * as React from "react";
import { IenActionBar } from "@/components/ui/action-bar";
export default function Preview() {
  const [open, setOpen] = React.useState(true);
  return (
    <div className="relative min-h-48 rounded-ien-lg border border-ien-border p-4">
      <button
        className="rounded-ien-default bg-ien-surface-weak px-3 py-2"
        onClick={() => setOpen(!open)}
      >
        선택 항목 작업
      </button>
      <IenActionBar open={open} onOpenChange={setOpen} selection="3개 선택">
        <button onClick={() => setOpen(false)}>보관</button>
        <button onClick={() => setOpen(false)}>삭제</button>
      </IenActionBar>
    </div>
  );
}
