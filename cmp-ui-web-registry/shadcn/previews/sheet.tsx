"use client";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
export default function Preview() {
  return (
    <Sheet>
      <SheetTrigger>설정 열기</SheetTrigger>
      <SheetContent side="right">
        <SheetHeader>
          <SheetTitle>설정</SheetTitle>
          <SheetDescription>계정 설정을 관리합니다.</SheetDescription>
        </SheetHeader>
      </SheetContent>
    </Sheet>
  );
}
