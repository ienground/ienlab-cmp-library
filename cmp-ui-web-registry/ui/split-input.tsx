"use client"

import * as React from "react"
import { Input } from "@/components/ui/input"
import { cn } from "cn"

export type SplitInputProps = Omit<React.ComponentProps<"div">, "onChange" | "defaultValue"> & {
  value?: string[]
  defaultValue?: string[]
  onValueChange?: (value: string[]) => void
  lengths: number[]
  labels: string[]
  separator?: React.ReactNode
  disabled?: boolean
  name?: string
  inputMode?: React.ComponentProps<"input">["inputMode"]
}
function SplitInput({ value, defaultValue, onValueChange, lengths, labels, separator = "−", disabled, name, inputMode = "numeric", className, ...props }: SplitInputProps) {
  if (lengths.length === 0 || lengths.some((length) => !Number.isInteger(length) || length < 1) || labels.length !== lengths.length) throw new RangeError("각 입력 길이는 양의 정수이며 labels와 같은 개수여야 합니다.")
  const [local, setLocal] = React.useState(defaultValue ?? lengths.map(() => ""))
  const values = value ?? local
  const inputs = React.useRef<(HTMLInputElement | null)[]>([])
  const update = (index: number, next: string) => {
    const result = lengths.map((_, i) => i === index ? next : values[i] ?? "")
    if (value === undefined) setLocal(result)
    onValueChange?.(result)
  }
  return <div {...props} role="group" data-slot="split-input" className={cn("flex items-center gap-2", className)}>
    {lengths.map((length, index) => <React.Fragment key={index}>
      {index > 0 && <span aria-hidden="true">{separator}</span>}
      <Input ref={(node) => { inputs.current[index] = node }} aria-label={labels[index]} value={values[index] ?? ""} maxLength={length} disabled={disabled} name={name ? `${name}[${index}]` : undefined} inputMode={inputMode} className="min-w-0 text-center" onValueChange={(next) => { update(index, next); if (next.length === length) inputs.current[index + 1]?.focus() }} onKeyDown={(event) => { if (event.key === "Backspace" && !event.currentTarget.value) inputs.current[index - 1]?.focus() }} onPaste={(event) => {
        const text = event.clipboardData.getData("text").replace(/\s|-/g, "")
        if (text.length <= length) return
        event.preventDefault()
        let offset = 0
        const next = lengths.map((size, i) => { if (i < index) return values[i] ?? ""; const part = text.slice(offset, offset + size); offset += size; return part })
        if (value === undefined) setLocal(next)
        onValueChange?.(next)
        inputs.current[Math.min(lengths.length - 1, next.reduce((last, part, index) => part.length > 0 ? index : last, 0))]?.focus()
      }} />
    </React.Fragment>)}
  </div>
}
export { SplitInput }
