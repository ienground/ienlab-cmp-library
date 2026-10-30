"use client";
import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from "@/components/ui/hover-card";
export default function Preview() {
  return (
    <HoverCard>
      <HoverCardTrigger href="#profile">@ienlab</HoverCardTrigger>
      <HoverCardContent>
        <p>CMP 컴포넌트 라이브러리</p>
        <p className="text-ien-caption text-ien-text-secondary">
          React · Compose Multiplatform
        </p>
      </HoverCardContent>
    </HoverCard>
  );
}
