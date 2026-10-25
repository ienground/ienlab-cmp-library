import { Field, FieldLabel } from "@/components/ui/field"
import { Switch } from "@/components/ui/switch"
import { CheckIcon, XIcon } from "lucide-react"

export function Pattern() {
  return (
    <div className="mx-auto flex w-full max-w-xs flex-col gap-5">
      <Field orientation="horizontal">
        <FieldLabel htmlFor="switch-basic">자동 적용</FieldLabel>
        <Switch id="switch-basic" defaultChecked aria-label="자동 적용" />
      </Field>
      <Field orientation="horizontal">
        <FieldLabel htmlFor="switch-track">트랙 아이콘</FieldLabel>
        <Switch id="switch-track" defaultChecked aria-label="트랙 아이콘"
          onTrackContent={<CheckIcon />} offTrackContent={<XIcon />} />
      </Field>
      <Field orientation="horizontal">
        <FieldLabel htmlFor="switch-thumb">손잡이 아이콘</FieldLabel>
        <Switch id="switch-thumb" aria-label="손잡이 아이콘" thumbContent={<CheckIcon />} />
      </Field>
      <Field orientation="horizontal">
        <FieldLabel htmlFor="switch-disabled">비활성</FieldLabel>
        <Switch id="switch-disabled" enabled={false} defaultChecked aria-label="비활성" />
      </Field>
    </div>
  )
}
export default Pattern
