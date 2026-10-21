"use client";
import { Button } from "@/components/ui/button";
import { Toaster } from "@/components/ui/sonner";
import { toast } from "sonner";
export default function Preview() {
  return (
    <div className="flex gap-ien-sm">
      <Button onClick={() => toast.success("변경 내용이 저장되었습니다.")}>
        성공 알림
      </Button>
      <Button
        variant="line"
        tone="neutral"
        onClick={() => toast.info("새 항목이 있습니다.")}
      >
        정보 알림
      </Button>
      <Toaster />
    </div>
  );
}
