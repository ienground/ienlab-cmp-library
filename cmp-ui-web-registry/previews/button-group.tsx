import { Button } from "@/components/ui/button"
import { ButtonGroup } from "@/components/ui/button-group"

export function Pattern() {
  return (
    <div className="flex flex-col items-start gap-ien-lg">
      <ButtonGroup aria-label="가로 버튼 그룹">
        <Button variant="line">첫 번째</Button>
        <Button variant="line">가운데</Button>
        <Button variant="line">마지막</Button>
      </ButtonGroup>
      <ButtonGroup orientation="vertical" aria-label="세로 캡슐 버튼 그룹">
        <Button variant="line" shape="capsule">첫 번째</Button>
        <Button variant="line" shape="capsule">가운데</Button>
        <Button variant="line" shape="capsule">마지막</Button>
      </ButtonGroup>
      <ButtonGroup aria-label="작업 버튼 그룹">
        <ButtonGroup>
          <Button variant="line" size="icon" aria-label="뒤로">←</Button>
        </ButtonGroup>
        <ButtonGroup>
          <Button variant="line">보관</Button>
          <Button variant="line">신고</Button>
        </ButtonGroup>
        <ButtonGroup>
          <Button variant="line">나중에</Button>
          <Button variant="line" size="icon" aria-label="추가 작업">…</Button>
        </ButtonGroup>
      </ButtonGroup>
    </div>
  )
}
export default Pattern;
