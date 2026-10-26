"use client"

import { useId, useLayoutEffect, useEffect, useRef, useState } from "react"
import type { ComponentProps, KeyboardEvent, ReactNode } from "react"
import { cn } from "cn"
import type { IenToneProps } from "@/lib/ien-theme"
import { useIenTheme } from "@/lib/ien-theme-context"
import { Button } from "@/components/ui/button"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"

export type PickerLocale = "ko" | "en"

export interface WheelPickerValueOptions<T> {
  value?: T
  defaultValue: T
  onValueChange?: (value: T) => void
  open?: boolean
  onOpenChange?: (open: boolean) => void
  requireConfirm?: boolean
  inline?: boolean
}

/** 확인 전 선택값과 실제 폼 값을 분리하고 닫기·취소 시 임시 선택을 버립니다. */
export function useWheelPickerValue<T>({ value, defaultValue, onValueChange, open, onOpenChange, requireConfirm, inline }: WheelPickerValueOptions<T>) {
  const fieldRef = useRef<HTMLInputElement>(null)
  const [local, setLocal] = useState(defaultValue)
  const actual = value === undefined ? local : value
  const [localOpen, setLocalOpen] = useState(false)
  const isOpen = open ?? localOpen
  const [draft, setDraft] = useState(actual)
  const deferred = requireConfirm && !inline && isOpen
  useEffect(() => { setDraft(actual) }, [actual])
  useEffect(() => {
    const form = fieldRef.current?.form
    if (!form || value !== undefined) return
    const reset = (event: Event) => queueMicrotask(() => {
      if (!event.defaultPrevented) { setLocal(defaultValue); setDraft(defaultValue); setLocalOpen(false) }
    })
    form.addEventListener("reset", reset)
    return () => form.removeEventListener("reset", reset)
  }, [value, defaultValue])
  const commit = (next: T) => { if (value === undefined) setLocal(next); if (!Object.is(next, actual)) onValueChange?.(next) }
  return {
    fieldRef, actual, selected: deferred ? draft : actual, open: isOpen,
    setOpen(next: boolean) { if (next) setDraft(actual); if (open === undefined) setLocalOpen(next); onOpenChange?.(next) },
    select(next: T) { if (deferred) setDraft(next); else commit(next) },
    confirm() { if (deferred) commit(draft) },
  }
}

export const pickerLabels = {
  ko: { clear: "초기화", confirm: "확인", now: "현재 시간", today: "오늘", date: "날짜 선택", duration: "기간 선택", year: "년", month: "월", day: "일", hour: "시간", minute: "분", second: "초" },
  en: { clear: "Clear", confirm: "OK", now: "Now", today: "Today", date: "Choose date", duration: "Choose duration", year: "Year", month: "Month", day: "Day", hour: "Hour", minute: "Minute", second: "Second" },
} as const

export function formatPickerUnit(value: number, unit: "year" | "month" | "day" | "hour" | "minute" | "second", locale: PickerLocale, duration = false) {
  const label = pickerLabels[locale][unit]
  if (locale === "ko") return `${value}${label}`
  if (duration) return `${value} ${label.toLowerCase()}${value === 1 ? "" : "s"}`
  return `${label} ${value}`
}

type WheelOption = { value: string | number; label: string; disabled?: boolean }

export interface IenWheelColumnProps extends Omit<ComponentProps<"div">, "children" | "onSelect">, IenToneProps {
  options: WheelOption[]
  selectedIndex: number
  highlightedIndex?: number
  onSelectIndex: (index: number) => void
  label: ReactNode
  describedBy?: string
  disabled?: boolean
  readOnly?: boolean
  fade?: boolean
  slotPrefix?: string
  onListKeyDown?: (event: KeyboardEvent<HTMLDivElement>) => void
}

function syncWheel(list: HTMLElement, fade: boolean) {
  const radius = list.clientHeight / 2
  if (!radius) return
  const scrollTop = list.scrollTop
  const rows = Array.from(list.querySelectorAll<HTMLElement>('[role="option"]'), (row) => ({
    row, height: row.offsetHeight, center: row.offsetTop + row.offsetHeight / 2,
  }))
  for (const { row, height, center } of rows) {
    const distance = center - scrollTop - radius
    const offset = Math.max(-1, Math.min(1, distance / radius))
    const translation = Math.sin(offset * Math.PI / 2) * (2 * radius / Math.PI) * 1.24 - distance
    const text = row.firstElementChild
    if (text instanceof HTMLElement) {
      text.style.transform = `translateY(${translation}px) rotateX(${-90 * offset}deg) scaleX(${1 - offset * offset * 0.37})`
      text.style.opacity = String(fade ? 1 - Math.abs(offset) : 1)
      text.style.visibility = Math.abs(distance) > radius + height / 2 ? "hidden" : ""
    }
    row.toggleAttribute("data-wheel-center", Math.abs(distance) < height / 2)
  }
}

/** 세 선택기가 공유하는 CMP 원통 휠입니다. 긴 연도·시간 목록은 보이는 부분만 렌더링합니다. */
export function IenWheelColumn({
  options, selectedIndex, highlightedIndex = Math.max(0, selectedIndex), onSelectIndex,
  label, describedBy, disabled = false, readOnly = false, fade = true,
  slotPrefix = "wheel-picker", onListKeyDown, tone, useGradient, className, ...props
}: IenWheelColumnProps) {
  const theme = useIenTheme({ tone, useGradient })
  const id = useId()
  const listRef = useRef<HTMLDivElement>(null)
  const userScrolling = useRef(false)
  const animateCenter = useRef(false)
  const timer = useRef<ReturnType<typeof setTimeout> | null>(null)
  const settleRef = useRef<() => void>(() => {})
  const typed = useRef({ text: "", at: 0 })
  const [windowIndex, setWindowIndex] = useState(highlightedIndex)
  const virtual = options.length > 300
  const start = virtual ? Math.max(0, windowIndex - 10) : 0
  const end = virtual ? Math.min(options.length, windowIndex + 11) : options.length
  const editable = !disabled && !readOnly
  const highlightedValue = options[highlightedIndex]?.value
  const optionId = (index: number) => `${id}-option-${index}`
  const rowHeight = (list: HTMLElement) => list.querySelector<HTMLElement>('[role="option"]')?.offsetHeight ?? 44

  useLayoutEffect(() => {
    const list = listRef.current
    if (!list) return
    userScrolling.current = false
    if (timer.current) clearTimeout(timer.current)
    const center = () => {
      list.scrollTo({ top: Math.max(0, highlightedIndex) * rowHeight(list), behavior:
        animateCenter.current && !window.matchMedia("(prefers-reduced-motion: reduce)").matches ? "smooth" : "instant" })
      animateCenter.current = false
      syncWheel(list, fade)
    }
    if (virtual) setWindowIndex(highlightedIndex)
    center()
    if (typeof ResizeObserver === "undefined") return
    let height = list.clientHeight
    const observer = new ResizeObserver(() => {
      if (height === list.clientHeight) return
      height = list.clientHeight
      center()
    })
    observer.observe(list)
    return () => observer.disconnect()
  }, [highlightedIndex, highlightedValue, selectedIndex, options.length, fade, virtual])

  useLayoutEffect(() => {
    if (listRef.current) syncWheel(listRef.current, fade)
  }, [start, end, fade])

  useEffect(() => () => { if (timer.current) clearTimeout(timer.current) }, [])

  const choose = (index: number, animate = false) => {
    if (!editable || !options[index] || options[index].disabled) return
    userScrolling.current = false
    if (timer.current) clearTimeout(timer.current)
    animateCenter.current = animate && index !== highlightedIndex
    onSelectIndex(index)
  }

  settleRef.current = () => {
    const list = listRef.current
    if (!list || !editable || !userScrolling.current) return
    userScrolling.current = false
    if (timer.current) clearTimeout(timer.current)
    const center = Math.round(list.scrollTop / rowHeight(list))
    let nearest = -1
    for (let index = 0; index < options.length; index++) {
      if (!options[index].disabled && (nearest === -1 || Math.abs(index - center) < Math.abs(nearest - center))) nearest = index
    }
    if (nearest === -1) return
    if (nearest !== selectedIndex) choose(nearest)
    else list.scrollTo({ top: nearest * rowHeight(list), behavior: "smooth" })
  }

  const onKeyDown = (event: KeyboardEvent<HTMLDivElement>) => {
    if (onListKeyDown) return onListKeyDown(event)
    if (!editable || event.ctrlKey || event.metaKey || event.altKey || event.nativeEvent.isComposing) return
    const enabled = options.flatMap((option, index) => option.disabled ? [] : [index])
    let next: number | undefined
    switch (event.key) {
      case "ArrowDown": next = enabled.find((index) => index > highlightedIndex); break
      case "ArrowUp": next = enabled.filter((index) => index < highlightedIndex).at(-1); break
      case "Home": next = enabled[0]; break
      case "End": next = enabled.at(-1); break
      case "PageDown": next = enabled.find((index) => index >= highlightedIndex + 5) ?? enabled.at(-1); break
      case "PageUp": next = enabled.filter((index) => index <= highlightedIndex - 5).at(-1) ?? enabled[0]; break
      case " ": next = highlightedIndex; break
      case "ArrowLeft":
      case "ArrowRight": {
        const lists = Array.from(event.currentTarget.parentElement?.parentElement?.querySelectorAll<HTMLElement>('[role="listbox"]') ?? [])
        lists[lists.indexOf(event.currentTarget) + (event.key === "ArrowRight" ? 1 : -1)]?.focus()
        event.preventDefault()
        return
      }
      default: {
        if (!/^\d$/.test(event.key)) return
        const previous = typed.current
        const text = event.timeStamp - previous.at < 1000 ? previous.text + event.key : event.key
        next = enabled.find((index) => String(options[index].value) === text)
        typed.current = { text: next === undefined ? event.key : text, at: event.timeStamp }
        next ??= enabled.find((index) => String(options[index].value) === event.key)
      }
    }
    event.preventDefault()
    if (next !== undefined) choose(next)
  }

  return (
    <div data-slot={`${slotPrefix}-column`} data-ien="" data-ien-tone={theme.tone} data-ien-gradient={theme.useGradient}
      aria-disabled={disabled || undefined}
      className={cn("relative flex min-w-20 flex-1 flex-col overflow-hidden [--time-picker-option-height:var(--ien-state-minimum-touch-target,44px)] aria-disabled:opacity-(--ien-state-disabled-alpha)", className)} {...props}>
      <div id={`${id}-label`} data-slot={`${slotPrefix}-column-label`} className="sr-only">{label}</div>
      <div aria-hidden="true" data-slot={`${slotPrefix}-selection`}
        className="pointer-events-none absolute inset-x-0 top-1/2 h-(--time-picker-option-height) -translate-y-1/2 rounded-(--ien-radius-sm) bg-[color-mix(in_srgb,var(--ien-control-color)_calc(var(--ien-state-selected-alpha)*100%),transparent)]" />
      <div ref={listRef} role="listbox" aria-labelledby={`${id}-label`} aria-describedby={describedBy}
        aria-activedescendant={highlightedIndex >= start && highlightedIndex < end ? optionId(highlightedIndex) : undefined}
        aria-disabled={disabled || undefined} aria-readonly={readOnly || undefined} tabIndex={disabled ? -1 : 0}
        data-slot={`${slotPrefix}-column-list`} onKeyDown={disabled ? undefined : onKeyDown}
        onWheel={() => { if (editable) userScrolling.current = true }}
        onPointerDown={() => { if (editable) userScrolling.current = true }}
        onScroll={(event) => {
          const list = event.currentTarget
          syncWheel(list, fade)
          if (virtual) setWindowIndex(Math.round(list.scrollTop / rowHeight(list)))
          if (!editable || !userScrolling.current) return
          if (timer.current) clearTimeout(timer.current)
          timer.current = setTimeout(() => settleRef.current(), 350)
        }} onScrollEnd={() => settleRef.current()}
        className="relative flex h-[calc(var(--time-picker-option-height)*var(--time-picker-rows,5))] flex-col overflow-y-auto overscroll-contain py-[calc(var(--time-picker-option-height)*(var(--time-picker-rows,5)-1)/2)] outline-none [scroll-snap-type:y_mandatory] [scrollbar-width:none] [perspective:800px] aria-disabled:overflow-hidden aria-readonly:overflow-hidden focus-visible:ring-2 focus-visible:ring-inset focus-visible:ring-ring rounded-(--ien-radius-sm) [&::-webkit-scrollbar]:hidden">
        {start > 0 && <div aria-hidden="true" className="shrink-0" style={{ height: `calc(var(--time-picker-option-height) * ${start})` }} />}
        {options.slice(start, end).map((option, at) => {
          const index = start + at
          return <div key={option.value} id={optionId(index)} role="option" aria-label={option.label}
            aria-selected={index === selectedIndex} aria-disabled={option.disabled || undefined}
            aria-posinset={index + 1} aria-setsize={options.length} data-slot={`${slotPrefix}-option`}
            data-ien-typography="title3" data-highlighted={index === highlightedIndex ? "" : undefined}
            onClick={() => choose(index, true)}
            className="text-[length:var(--ien-font-size-title3)] leading-(--ien-line-height-title3) flex h-(--time-picker-option-height) shrink-0 cursor-default items-center justify-center px-(--ien-spacing-xs) text-center whitespace-nowrap tabular-nums select-none [scroll-snap-align:center] text-(--ien-text-secondary) data-wheel-center:text-(--ien-control-color) aria-disabled:opacity-(--ien-state-disabled-alpha) forced-colors:data-wheel-center:text-[Highlight]">
            <span data-slot={`${slotPrefix}-option-text`} className="flex size-full items-center justify-center">{option.label}</span>
          </div>
        })}
        {end < options.length && <div aria-hidden="true" className="shrink-0" style={{ height: `calc(var(--time-picker-option-height) * ${options.length - end})` }} />}
      </div>
    </div>
  )
}

interface IenWheelPickerFrameProps extends IenToneProps {
  children: ReactNode
  locale: PickerLocale
  label: string
  displayValue: string
  placeholder: string
  inline: boolean
  disabled: boolean
  readOnly: boolean
  className?: string
  onClear: () => void
  quickAction?: { label: string; onClick: () => void }
  slotPrefix: string
  open?: boolean
  onOpenChange?: (open: boolean) => void
  onConfirm?: () => void
}

/** 팝오버와 인라인 선택기의 공통 버튼·배치를 유지합니다. */
export function IenWheelPickerFrame({ children, locale, label, displayValue, placeholder, inline, disabled, readOnly, className, onClear, quickAction, slotPrefix, tone, useGradient, open: controlledOpen, onOpenChange, onConfirm }: IenWheelPickerFrameProps) {
  const theme = useIenTheme({ tone, useGradient })
  const [localOpen, setLocalOpen] = useState(false)
  const open = controlledOpen ?? localOpen
  const setOpen = (next: boolean) => { if (controlledOpen === undefined) setLocalOpen(next); onOpenChange?.(next) }
  const text = pickerLabels[locale]
  const panel = <div role="group" lang={locale} aria-label={label} data-slot={`${slotPrefix}-panel`} data-ien=""
    onKeyDown={(event) => {
      if (!inline && !event.defaultPrevented && event.key === "Enter" && event.target instanceof HTMLElement && event.target.getAttribute("role") === "listbox") {
        event.preventDefault()
        onConfirm?.()
        setOpen(false)
      }
    }}
    data-ien-tone={theme.tone} data-ien-gradient={theme.useGradient} className={cn("flex min-w-72 flex-col", inline && className)}>
    <div data-slot={`${slotPrefix}-columns`} className="flex gap-(--ien-spacing-xxs) p-(--ien-spacing-xs)">{children}</div>
    <div className="flex items-center justify-between gap-(--ien-spacing-xs) p-(--ien-spacing-xs)">
      {quickAction && <Button type="button" variant="weak" tone="brand" size="sm" disabled={disabled || readOnly} onClick={quickAction.onClick}>{quickAction.label}</Button>}
      <Button type="button" variant="weak" tone="neutral" size="sm" disabled={disabled || readOnly} onClick={onClear}>{text.clear}</Button>
      {!inline && <Button type="button" variant="fill" tone="brand" size="sm" disabled={disabled} onClick={() => { onConfirm?.(); setOpen(false) }}>{text.confirm}</Button>}
    </div>
  </div>
  if (inline) return panel
  return <Popover open={open} onOpenChange={setOpen}>
    <PopoverTrigger asChild><Button type="button" variant="line" tone={theme.tone} disabled={disabled} aria-label={label} className={className}>{displayValue || placeholder}</Button></PopoverTrigger>
    <PopoverContent align="start" aria-label={label} className="w-auto gap-0 overflow-hidden p-0">{panel}</PopoverContent>
  </Popover>
}
