"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { UpdateAlertDialog } from "@/components/ui/update-alert-dialog";

export default function Preview() {
  const [open, setOpen] = React.useState(true);
  const [updated, setUpdated] = React.useState(false);
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <Button onClick={() => setOpen(true)}>업데이트 알림 보기</Button>
      <UpdateAlertDialog
        open={open}
        onOpenChange={setOpen}
        appName="Ien 앱"
        onUpdate={() => setUpdated(true)}
      />
      <p aria-live="polite" className="text-ien-caption">
        {updated ? "업데이트를 시작했습니다." : "새 버전 확인"}
      </p>
    </div>
  );
}
