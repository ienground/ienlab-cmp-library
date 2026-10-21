"use client";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
export default function Preview() {
  return (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger>가리켜 보세요</TooltipTrigger>
        <TooltipContent tone="brand">추가 설명을 표시합니다.</TooltipContent>
      </Tooltip>
    </TooltipProvider>
  );
}
