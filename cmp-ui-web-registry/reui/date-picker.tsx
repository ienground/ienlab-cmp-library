"use client"

import { useMemo, useState } from "react"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenWheelColumn, IenWheelPickerFrame, formatPickerUnit, pickerLabels, type PickerLocale } from "@/lib/ien-wheel-picker"

type CalendarDate = { year: number; month: number; day: number }

const daysInMonth = (year: number, month: number) => month === 2
  ? (year % 4 === 0 && (year % 100 !== 0 || year % 400 === 0) ? 29 : 28)
  : [4, 6, 9, 11].includes(month) ? 30 : 31

function formatDate({ year, month, day }: CalendarDate) {
  return `${String(year).padStart(4, "0")}-${String(month).padStart(2, "0")}-${String(day).padStart(2, "0")}`
}

function parseDate(value: string | null | undefined): CalendarDate | null {
  const match = value?.match(/^(\d{4})-(\d{2})-(\d{2})$/)
  if (!match) return null
  const [, y, m, d] = match
  const year = Number(y), month = Number(m), day = Number(d)
  return year >= 1 && year <= 9999 && month >= 1 && month <= 12 && day >= 1 && day <= daysInMonth(year, month) ? { year, month, day } : null
}

function today(): CalendarDate {
  const date = new Date()
  return { year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate() }
}

function timestamp(value: CalendarDate) {
  const date = new Date(0)
  date.setUTCFullYear(value.year, value.month - 1, value.day)
  return date.getTime()
}

export interface DatePickerProps extends IenToneProps {
  /** 시간대가 없는 YYYY-MM-DD 날짜입니다. null은 선택하지 않은 상태입니다. */
  value?: string | null
  defaultValue?: string | null
  onValueChange?: (value: string | null) => void
  locale?: PickerLocale
  /** 포함되는 연도 범위입니다. 각 값은 1..9999입니다. */
  yearRange?: { start: number; end: number }
  min?: string
  max?: string
  isDateDisabled?: (date: string) => boolean
  isYearDisabled?: (year: number) => boolean
  disabled?: boolean
  readOnly?: boolean
  fade?: boolean
  inline?: boolean
  className?: string
  "aria-label"?: string
}

/** CMP 연·월·일 휠입니다. 월·연도 변경 시 말일과 선택 가능 날짜를 보정합니다. */
export function DatePicker({
  value, defaultValue = null, onValueChange, locale = "ko", yearRange = { start: 1900, end: 2100 },
  min, max, isDateDisabled, isYearDisabled, disabled = false, readOnly = false, fade = true, inline = false,
  className, "aria-label": label, tone, useGradient,
}: DatePickerProps) {
  const [uncontrolled, setUncontrolled] = useState<string | null>(defaultValue)
  const [reference] = useState(today)
  const { start, end } = yearRange
  if (!Number.isInteger(start) || !Number.isInteger(end) || start < 1 || end > 9999 || start > end) throw new RangeError("yearRange는 1..9999 안의 유효한 범위여야 합니다.")
  const raw = value !== undefined ? value : uncontrolled
  const parsed = parseDate(raw)
  const availability = useMemo(() => {
    const minimum = parseDate(min), maximum = parseDate(max)
    const minValue = minimum ? formatDate(minimum) : undefined
    const maxValue = maximum ? formatDate(maximum) : undefined
    const cache = new Map<number, number[][]>()
    const months = (year: number) => {
      const existing = cache.get(year)
      if (existing) return existing
      const result = Array.from({ length: 12 }, (_, index) => {
        if (isYearDisabled?.(year) || (minimum && year < minimum.year) || (maximum && year > maximum.year)) return []
        const month = index + 1
        return Array.from({ length: daysInMonth(year, month) }, (_, i) => i + 1).filter((day) => {
          const date = formatDate({ year, month, day })
          return (!minValue || date >= minValue) && (!maxValue || date <= maxValue) && !isDateDisabled?.(date)
        })
      })
      cache.set(year, result)
      return result
    }
    const years = Array.from({ length: end - start + 1 }, (_, index) => start + index).filter((year) => {
      if (isYearDisabled?.(year) || (minimum && year < minimum.year) || (maximum && year > maximum.year)) return false
      if (!isDateDisabled) return !minValue || !maxValue || minValue <= maxValue
      return months(year).some((days) => days.length > 0)
    })
    return { years, months }
  }, [start, end, min, max, isDateDisabled, isYearDisabled])

  const nearest = (preferred: CalendarDate, onlyYear?: number, onlyMonth?: number): CalendarDate | null => {
    const year = Math.max(start, Math.min(end, preferred.year))
    const month = Math.max(1, Math.min(12, preferred.month))
    const target = { year, month, day: Math.min(preferred.day, daysInMonth(year, month)) }
    const candidates = onlyYear === undefined
      ? [...new Set([availability.years.filter((item) => item <= year).at(-1), availability.years.find((item) => item >= year)])].filter((item): item is number => item !== undefined)
      : [onlyYear]
    let result: CalendarDate | null = null
    let distance = Infinity
    const desired = timestamp(target)
    for (const candidate of candidates) {
      const months = availability.months(candidate)
      for (let index = 0; index < 12; index++) {
        if (onlyMonth !== undefined && onlyMonth !== index + 1) continue
        for (const day of months[index]) {
          const date = { year: candidate, month: index + 1, day }
          const difference = Math.abs(timestamp(date) - desired)
          if (difference < distance) { result = date; distance = difference }
        }
      }
    }
    return result
  }
  const display = nearest(parsed ?? reference) ?? { year: start, month: 1, day: 1 }
  const hasDates = availability.years.length > 0
  const years = hasDates ? availability.years : [display.year]
  const monthDays = availability.months(display.year)
  const months = Array.from({ length: 12 }, (_, index) => index + 1).filter((month) => !hasDates || monthDays[month - 1].length > 0)
  const days = hasDates ? monthDays[display.month - 1] : [display.day]
  const select = (next: CalendarDate | null) => {
    if (disabled || readOnly || !hasDates) return
    const result = next ? formatDate(next) : null
    if (value === undefined) setUncontrolled(result)
    if (result !== raw) onValueChange?.(result)
  }
  const text = pickerLabels[locale]

  return <IenWheelPickerFrame locale={locale} slotPrefix="date-picker" label={label ?? text.date}
    displayValue={parsed ? (["year", "month", "day"] as const).map((unit) => formatPickerUnit(display[unit], unit, locale)).join(" ") : ""}
    placeholder={text.date} inline={inline} disabled={disabled || !hasDates} readOnly={readOnly} onClear={() => select(null)}
    quickAction={{ label: text.today, onClick: () => { const next = nearest(today()); if (next) select(next) } }}
    className={className} tone={tone} useGradient={useGradient}>
    {([
      { unit: "year", values: years, weight: 1.4 },
      { unit: "month", values: months, weight: 1 },
      { unit: "day", values: days, weight: 1 },
    ] as const).map(({ unit, values, weight }) =>
      <IenWheelColumn key={unit} slotPrefix="date-picker" data-type={unit} tone={tone} useGradient={useGradient}
        label={text[unit]} options={values.map((item) => ({ value: item, label: formatPickerUnit(item, unit, locale) }))}
        selectedIndex={parsed ? values.indexOf(display[unit]) : -1} highlightedIndex={Math.max(0, values.indexOf(display[unit]))}
        disabled={disabled || !hasDates} readOnly={readOnly} fade={fade} style={{ flexGrow: weight }}
        onSelectIndex={(index) => {
          const next = { ...display, [unit]: values[index] }
          const result = unit === "year" ? nearest(next, next.year) : unit === "month" ? nearest(next, next.year, next.month) : next
          if (result) select(result)
        }} />
    )}
  </IenWheelPickerFrame>
}
