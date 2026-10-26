"use client"

import * as React from "react"
import { cn } from "cn"
import { Checkbox } from "@/components/ui/checkbox"
import { useIenLanguage } from "@/lib/ien-theme-context"

export interface AgreementItem { value: string; label: React.ReactNode; required?: boolean; description?: React.ReactNode; disabled?: boolean }
export type AgreementProps = Omit<React.ComponentProps<"fieldset">, "onChange" | "defaultValue"> & {
  items: AgreementItem[]
  value?: string[]
  defaultValue?: string[]
  onValueChange?: (value: string[]) => void
  label?: React.ReactNode
  name?: string
}
function Agreement({ items, value, defaultValue = [], onValueChange, label, name, disabled, className, ...props }: AgreementProps) {
  const language = useIenLanguage("ko")
  const [local, setLocal] = React.useState(defaultValue)
  const selected = new Set(value ?? local)
  const editable = items.filter((item) => !item.disabled)
  const all = editable.length > 0 && editable.every((item) => selected.has(item.value))
  const partial = !all && editable.some((item) => selected.has(item.value))
  const update = (next: Set<string>) => { const result = items.filter((item) => next.has(item.value)).map((item) => item.value); if (value === undefined) setLocal(result); onValueChange?.(result) }
  return <fieldset {...props} disabled={disabled} data-slot="agreement" className={cn("min-w-0 space-y-2", className)}>
    <legend className="sr-only">{label ?? (language === "ko" ? "약관 동의" : "Agreements")}</legend>
    <label className="flex min-h-14 cursor-pointer items-center gap-3 rounded-xl bg-(--ien-surface-weak) px-4 py-3 font-semibold">
      <Checkbox checked={all ? true : partial ? "indeterminate" : false} disabled={disabled || editable.length === 0} onCheckedChange={() => { const next = new Set(selected); for (const item of editable) { if (all) next.delete(item.value); else next.add(item.value) }; update(next) }} />
      {label ?? (language === "ko" ? "전체 동의" : "Agree to all")}
    </label>
    {items.map((item) => <div key={item.value} className="px-4 py-2">
      <label className={cn("flex min-h-11 cursor-pointer items-center gap-3 text-[length:var(--ien-font-size-body2)]", (disabled || item.disabled) && "cursor-default opacity-(--ien-state-disabled-alpha)")}>
        <Checkbox checked={selected.has(item.value)} disabled={disabled || item.disabled} name={name} value={item.value} required={item.required} onCheckedChange={(checked) => { const next = new Set(selected); if (checked === true) next.add(item.value); else next.delete(item.value); update(next) }} />
        <span>{item.required ? (language === "ko" ? "[필수] " : "[Required] ") : (language === "ko" ? "[선택] " : "[Optional] ")}{item.label}</span>
      </label>
      {item.description && <details className="ms-9 text-[length:var(--ien-font-size-caption)] text-(--ien-text-secondary)"><summary className="cursor-pointer py-2">{language === "ko" ? "내용 보기" : "View details"}</summary><div className="py-2">{item.description}</div></details>}
    </div>)}
  </fieldset>
}
export { Agreement }
