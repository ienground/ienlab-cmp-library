"use client";
import {
  ResizableHandle,
  ResizablePanel,
  ResizablePanelGroup,
} from "@/components/ui/resizable";
export default function Preview() {
  return (
    <ResizablePanelGroup
      orientation="horizontal"
      className="min-h-40 rounded-ien-default border border-ien-border"
    >
      <ResizablePanel defaultSize={40} minSize={25}>
        <div className="grid h-full place-items-center">목록</div>
      </ResizablePanel>
      <ResizableHandle withHandle />
      <ResizablePanel defaultSize={60} minSize={30}>
        <div className="grid h-full place-items-center">상세 내용</div>
      </ResizablePanel>
    </ResizablePanelGroup>
  );
}
