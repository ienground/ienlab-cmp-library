"use client";
import {
  Popover,
  PopoverContent,
  PopoverDescription,
  PopoverHeader,
  PopoverTitle,
  PopoverTrigger,
} from "@/components/ui/popover";
export default function Preview() {
  return (
    <Popover>
      <PopoverTrigger>도움말 열기</PopoverTrigger>
      <PopoverContent>
        <PopoverHeader>
          <PopoverTitle>빠른 안내</PopoverTitle>
          <PopoverDescription>
            버튼을 눌러 내용을 표시합니다.
          </PopoverDescription>
        </PopoverHeader>
      </PopoverContent>
    </Popover>
  );
}
