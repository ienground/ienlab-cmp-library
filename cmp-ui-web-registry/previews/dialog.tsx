"use client"

import { Dialog, DialogTrigger, DialogClose, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"

export function Pattern() {
  return <Dialog><DialogTrigger asChild><Button>일반 다이얼로그</Button></DialogTrigger><DialogContent><DialogHeader><DialogTitle>작업 안내</DialogTitle><DialogDescription>관련 내용과 원하는 콘텐츠를 함께 표시합니다.</DialogDescription></DialogHeader><p>Tab으로 이동하고 Escape로 닫을 수 있습니다.</p><DialogFooter><DialogClose asChild><Button>닫기</Button></DialogClose></DialogFooter></DialogContent></Dialog>
}
export default Pattern
