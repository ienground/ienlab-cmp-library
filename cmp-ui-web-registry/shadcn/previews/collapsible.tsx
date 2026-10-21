"use client";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
export default function Preview() {
  return (
    <Collapsible>
      <CollapsibleTrigger>세부 정보 표시</CollapsibleTrigger>
      <CollapsibleContent className="pt-ien-xs text-ien-body2 text-ien-text-secondary">
        열고 닫을 수 있는 내용입니다.
      </CollapsibleContent>
    </Collapsible>
  );
}
