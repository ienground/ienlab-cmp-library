"use client"

import * as React from "react"
import { IenWheelColumn } from "@/lib/ien-wheel-picker"

export type WheelPickerProps = Omit<React.ComponentProps<typeof IenWheelColumn>, "selectedIndex" | "onSelectIndex"> & {
  value?: string | number
  defaultValue?: string | number
  onValueChange?: (value: string | number) => void
  visibleRows?: number
}
function WheelPicker({ options, value, defaultValue, onValueChange, visibleRows = 5, style, ...props }: WheelPickerProps) {
  if (!Number.isInteger(visibleRows) || visibleRows < 3 || visibleRows > 11 || visibleRows % 2 === 0) throw new RangeError("visibleRows는 3..11 사이의 홀수여야 합니다.")
  const [local, setLocal] = React.useState(defaultValue ?? options.find((option) => !option.disabled)?.value)
  const selected = value ?? local
  return <IenWheelColumn {...props} style={{ "--time-picker-rows": visibleRows, ...style } as React.CSSProperties} options={options} selectedIndex={options.findIndex((option) => option.value === selected)} onSelectIndex={(index) => { const next = options[index]?.value; if (next === undefined) return; if (value === undefined) setLocal(next); onValueChange?.(next) }} />
}
export { WheelPicker }
