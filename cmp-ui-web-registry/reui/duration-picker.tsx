"use client"

import { useState } from "react"
import type { IenToneProps } from "@/lib/ien-theme"
import { useIenLanguage } from "@/lib/ien-theme-context"
import { IenWheelColumn, IenWheelPickerFrame, useWheelPickerValue, formatPickerUnit, pickerLabels, type PickerLocale } from "@/lib/ien-wheel-picker"

export interface DurationPickerProps extends IenToneProps {
  /** 기간을 정수 초로 전달합니다. null은 선택하지 않은 상태입니다. */
  value?: number | null
  defaultValue?: number | null
  onValueChange?: (seconds: number | null) => void
  locale?: PickerLocale
  showHours?: boolean
  showMinutes?: boolean
  showSeconds?: boolean
  /** 시간 열의 최댓값입니다. 0부터 9999까지 지정할 수 있습니다. */
  maxHours?: number
  disabled?: boolean
  readOnly?: boolean
  fade?: boolean
  inline?: boolean
  className?: string
  open?: boolean
  onOpenChange?: (open: boolean) => void
  requireConfirm?: boolean
  name?: string
  form?: string
  required?: boolean
  "aria-label"?: string
}

/** 시각과 구별되는 기간 선택기입니다. 24시간 이상의 기간을 지원합니다. */
export function DurationPicker({
  value, defaultValue = 0, onValueChange, locale: localeProp,
  showHours = true, showMinutes = true, showSeconds = true, maxHours = 23,
  disabled = false, readOnly = false, fade = true, inline = false,
  className, "aria-label": label, tone, useGradient, open, onOpenChange, requireConfirm = false, name, form, required,
}: DurationPickerProps) {
  const locale = useIenLanguage("ko", localeProp)
  const selection = useWheelPickerValue<number | null>({ value, defaultValue, onValueChange, open, onOpenChange, requireConfirm, inline })
  if (!showHours && !showMinutes && !showSeconds) throw new RangeError("적어도 하나의 필드를 표시해야 합니다.")
  if (!Number.isInteger(maxHours) || maxHours < 0 || maxHours > 9999) throw new RangeError("maxHours는 0..9999의 정수여야 합니다.")
  const raw = selection.selected
  const seconds = raw === null || !Number.isFinite(raw) ? null : Math.max(0, Math.min(Math.trunc(raw), showHours ? maxHours * 3600 + 3599 : Number.MAX_SAFE_INTEGER))
  const parts = { hour: Math.floor((seconds ?? 0) / 3600), minute: Math.floor((seconds ?? 0) / 60) % 60, second: (seconds ?? 0) % 60 }
  const text = pickerLabels[locale]
  const fields = [
    { unit: "hour", visible: showHours, count: maxHours + 1, weight: showMinutes || showSeconds ? 1.4 : 1 },
    { unit: "minute", visible: showMinutes, count: 60, weight: 1 },
    { unit: "second", visible: showSeconds, count: 60, weight: 1 },
  ] as const
  const select = (next: number | null) => {
    if (disabled || readOnly) return
    selection.select(next)
  }

  return <>
    {(name || required) && <input ref={selection.fieldRef} type="text" name={name} form={form} required={required} disabled={disabled} value={selection.actual ?? ""} onChange={() => {}} tabIndex={-1} aria-label={label ?? text.duration} onFocus={() => selection.setOpen(true)} className="pointer-events-none absolute size-px opacity-0" />}
    <IenWheelPickerFrame open={selection.open} onOpenChange={selection.setOpen} onConfirm={selection.confirm} locale={locale} slotPrefix="duration-picker" label={label ?? text.duration}
    displayValue={seconds === null ? "" : fields.filter((field) => field.visible).map(({ unit }) => formatPickerUnit(parts[unit], unit, locale, true)).join(" ")}
    placeholder={text.duration} inline={inline} disabled={disabled} readOnly={readOnly} onClear={() => select(null)}
    className={className} tone={tone} useGradient={useGradient}>
    {fields.filter((field) => field.visible).map(({ unit, count, weight }) =>
      <IenWheelColumn key={unit} slotPrefix="duration-picker" data-type={unit} tone={tone} useGradient={useGradient}
        label={text[unit]} options={Array.from({ length: count }, (_, index) => ({ value: index, label: formatPickerUnit(index, unit, locale, true) }))}
        selectedIndex={seconds === null ? -1 : parts[unit]} highlightedIndex={parts[unit]}
        disabled={disabled} readOnly={readOnly} fade={fade} style={{ flexGrow: weight }}
        onSelectIndex={(index) => {
          const next = { ...parts, [unit]: index }
          select(next.hour * 3600 + next.minute * 60 + next.second)
        }} />
    )}
  </IenWheelPickerFrame></>
}
