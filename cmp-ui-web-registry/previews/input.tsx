import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"

export function Pattern() {
  return (
    <div className="mx-auto flex w-full max-w-sm flex-col gap-6">
      <form className="space-y-2">
        <Input aria-label="입력 포맷" defaultValue="123456" format={{ transform: (value) => value.replace(/(.{3})(?=.)/g, "$1-"), reset: (value) => value.replaceAll("-", "") }} />
        <Button type="reset" variant="weak" size="sm">초기값 복원</Button>
      </form>
      <Input type="search" label="검색" labelOption="sustain" clearable defaultValue="검색어" />
      <Input type="password" label="비밀번호" labelOption="sustain" defaultValue="secret" />
      <Input variant="big" aria-label="금액" placeholder="0원" />
      <Input variant="hero" aria-label="헤드라인" placeholder="제목 입력" />
      <Input label="이름" labelOption="sustain" placeholder="최대 20자까지 입력하세요" lengthLimit={{ mode: "block", maxLength: 20 }} supportingText="라벨을 항상 표시하고 초과 입력을 차단합니다." />
      <Input label="길이 초과 오류 표시" labelOption="appear" placeholder="포커스하면 라벨이 나타납니다" lengthLimit={{ mode: "error", maxLength: 10 }} supportingText="10자를 초과해도 입력할 수 있으며 오류로 표시합니다." />
    </div>
  )
}
export default Pattern;
