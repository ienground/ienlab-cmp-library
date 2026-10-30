"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { Modal } from "@/components/ui/modal";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [saved, setSaved] = React.useState(false);
  return (
    <>
      <Button onClick={() => setOpen(true)}>프로필 편집</Button>
      <Modal
        open={open}
        onOpenChange={setOpen}
        title="프로필 편집"
        description="변경 후 저장을 누르세요."
        footer={
          <>
            <Button
              variant="weak"
              tone="neutral"
              onClick={() => setOpen(false)}
            >
              취소
            </Button>
            <Button
              onClick={() => {
                setSaved(true);
                setOpen(false);
              }}
            >
              저장
            </Button>
          </>
        }
      >
        <p>이름과 소개를 수정할 수 있는 모달 콘텐츠입니다.</p>
      </Modal>
      <p aria-live="polite" className="mt-ien-sm text-ien-caption">
        {saved ? "변경 사항을 저장했습니다." : ""}
      </p>
    </>
  );
}
