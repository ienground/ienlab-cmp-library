import { Button } from "@/components/ui/button"
import {
  Sheet,
  SheetTrigger,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetDescription,
  SheetSelectionList,
} from "@/components/ui/sheet"

export function Pattern() {
  return (
    <Sheet>
      <SheetTrigger asChild>
        <Button variant="line">패널 열기</Button>
      </SheetTrigger>
      <SheetContent side="bottom">
        <SheetHeader>
          <SheetTitle>패널 제목</SheetTitle>
          <SheetDescription>추가 정보와 작업을 표시하는 패널입니다.</SheetDescription>
        </SheetHeader>
        <SheetSelectionList aria-label="정렬 방식" defaultValue="recent" items={[{ value: "recent", label: "최신순" }, { value: "name", label: "이름순" }, { value: "blocked", label: "선택 불가", disabled: true }]} />
      </SheetContent>
    </Sheet>
  )
}
export default Pattern
