"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import {
  Snackbar,
  IenToastProvider,
  useIenToast,
} from "@/components/ui/snackbar";

function ToastActions() {
  const { show } = useIenToast();
  return (
    <>
      <Button
        variant="weak"
        tone="success"
        onClick={() => show("저장했습니다.", "success")}
      >
        성공 알림
      </Button>
      <Button
        variant="weak"
        tone="warning"
        onClick={() => show("입력값을 확인하세요.", "warning", "long")}
      >
        주의 알림
      </Button>
    </>
  );
}

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [message, setMessage] = React.useState("항목을 저장했습니다.");
  return (
    <IenToastProvider>
      <div className="flex flex-wrap gap-ien-sm">
        <Button
          onClick={() => {
            setMessage("항목을 저장했습니다.");
            setOpen(true);
          }}
        >
          저장하기
        </Button>
        <ToastActions />
        <Snackbar
          open={open}
          onOpenChange={setOpen}
          message={message}
          action={{
            label: "실행 취소",
            onClick: () => setMessage("저장을 취소했습니다."),
          }}
        />
      </div>
    </IenToastProvider>
  );
}
