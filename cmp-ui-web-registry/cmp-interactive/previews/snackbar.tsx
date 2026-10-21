"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { Snackbar } from "@/components/ui/snackbar";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [message, setMessage] = React.useState("항목을 저장했습니다.");
  return (
    <div>
      <Button
        onClick={() => {
          setMessage("항목을 저장했습니다.");
          setOpen(true);
        }}
      >
        저장하기
      </Button>
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
  );
}
