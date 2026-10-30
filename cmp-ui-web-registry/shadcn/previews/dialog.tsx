"use client";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
export default function Preview() {
  return (
    <Dialog>
      <DialogTrigger>대화상자 열기</DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>알림 설정</DialogTitle>
          <DialogDescription>알림 수신 여부를 설정합니다.</DialogDescription>
        </DialogHeader>
      </DialogContent>
    </Dialog>
  );
}
