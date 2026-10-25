"use client"

import { useState } from "react"
import { Textarea } from "@/components/ui/textarea"

export function Pattern() {
  const [value, setValue] = useState("")

  return (
    <div className="mx-auto flex w-full max-w-sm flex-col gap-6">
      <Textarea
        label="내용"
        labelOption="sustain"
        required
        placeholder="최대 200자까지 입력하세요"
        value={value}
        onValueChange={setValue}
        lengthLimit={{ mode: "block", maxLength: 200 }}
        supportingText="최대 길이를 넘는 입력을 차단합니다."
        minLines={4}
        maxLines={8}
      />
      <Textarea
        label="길이 초과 오류 표시"
        placeholder="20자를 초과해 입력해 보세요"
        lengthLimit={{ mode: "error", maxLength: 20 }}
        supportingText="20자를 초과하면 오류 상태로 표시됩니다."
      />
    </div>
  )
}
export default Pattern
