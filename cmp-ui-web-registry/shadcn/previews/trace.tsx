"use client";
import {
  Trace,
  TraceCollapsible,
  TraceCollapsibleContent,
  TraceCollapsibleTrigger,
  TraceDescription,
  TraceIcon,
  TraceLine,
  TraceTitle,
} from "@/components/ui/trace";
import { ChevronRightIcon, CircleCheckIcon } from "lucide-react";
export default function Preview() {
  return (
    <Trace>
      <TraceLine status="running">
        <TraceIcon>
          <CircleCheckIcon />
        </TraceIcon>
        <TraceTitle>배포 시작</TraceTitle>
        <TraceDescription>10:24</TraceDescription>
      </TraceLine>
      <TraceCollapsible>
        <TraceCollapsibleTrigger>
          <TraceLine>
            <TraceIcon>
              <ChevronRightIcon />
            </TraceIcon>
            <TraceTitle>빌드 로그</TraceTitle>
          </TraceLine>
        </TraceCollapsibleTrigger>
        <TraceCollapsibleContent>
          <TraceLine status="pending">
            <TraceTitle>자산 업로드</TraceTitle>
          </TraceLine>
        </TraceCollapsibleContent>
      </TraceCollapsible>
    </Trace>
  );
}
