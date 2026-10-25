import { Input } from "@/components/ui/input"

export function Pattern() {
  return (
    <div className="mx-auto flex w-full max-w-sm flex-col gap-6">
      <Input label="이름" labelOption="sustain" placeholder="최대 20자까지 입력하세요" lengthLimit={{ mode: "block", maxLength: 20 }} supportingText="라벨을 항상 표시하고 초과 입력을 차단합니다." />
      <Input label="길이 초과 오류 표시" labelOption="appear" placeholder="포커스하면 라벨이 나타납니다" lengthLimit={{ mode: "error", maxLength: 10 }} supportingText="10자를 초과해도 입력할 수 있으며 오류로 표시합니다." />
    </div>
  )
}
export default Pattern;
