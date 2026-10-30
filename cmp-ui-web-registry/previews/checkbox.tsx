import { Checkbox } from "@/components/ui/checkbox"

export function Pattern() {
  return (
    <div className="grid gap-4 sm:grid-cols-3">
      <div className="flex flex-col gap-1">
        <Checkbox label="원형 체크" />
        <Checkbox label="선택됨" defaultChecked />
        <Checkbox label="비활성" enabled={false} />
        <Checkbox label="비활성 선택됨" defaultChecked enabled={false} />
      </div>
      <div className="flex flex-col gap-1">
        <Checkbox variant="dot" label="점 체크" />
        <Checkbox variant="dot" label="선택됨" defaultChecked />
        <Checkbox variant="dot" label="비활성" enabled={false} />
      </div>
      <div className="flex flex-col gap-1">
        <Checkbox variant="line" label="라인 체크" />
        <Checkbox variant="line" label="선택됨" defaultChecked />
        <Checkbox variant="line" label="비활성" enabled={false} />
        <Checkbox label="일부 선택됨" defaultChecked="indeterminate" />
      </div>
    </div>
  )
}
export default Pattern
