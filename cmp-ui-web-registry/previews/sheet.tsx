import { Button } from "@/components/ui/button"
import {
  Sheet,
  SheetTrigger,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetDescription,
} from "@/components/ui/sheet"

export function Pattern() {
  return (
    <Sheet>
      <SheetTrigger asChild>
        <Button variant="line">패널 열기</Button>
      </SheetTrigger>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>패널 제목</SheetTitle>
          <SheetDescription>추가 정보와 작업을 표시하는 패널입니다.</SheetDescription>
        </SheetHeader>
      </SheetContent>
    </Sheet>
  )
}
export default Pattern
