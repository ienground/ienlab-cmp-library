"use client";
import {
  ContextMenu,
  ContextMenuContent,
  ContextMenuItem,
  ContextMenuTrigger,
} from "@/components/ui/context-menu";
export default function Preview() {
  return (
    <ContextMenu>
      <ContextMenuTrigger className="grid min-h-32 place-items-center rounded-ien-default border border-dashed border-ien-border">
        여기에서 오른쪽 클릭
      </ContextMenuTrigger>
      <ContextMenuContent>
        <ContextMenuItem>열기</ContextMenuItem>
        <ContextMenuItem>이름 바꾸기</ContextMenuItem>
      </ContextMenuContent>
    </ContextMenu>
  );
}
