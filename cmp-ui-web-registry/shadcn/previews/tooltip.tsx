"use client";
import {
  Tooltip,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
export default function Preview() {
  return (
    <TooltipProvider>
      <Tooltip
        tone="info"
        placement="top"
        offset={8}
        messageAlign="center"
        fitContentWidth
        text="추가 설명을 표시합니다."
      >
        <TooltipTrigger>가리켜 보세요</TooltipTrigger>
      </Tooltip>
    </TooltipProvider>
  );
}
