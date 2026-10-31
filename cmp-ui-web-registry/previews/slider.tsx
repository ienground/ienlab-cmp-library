"use client"

import { useState } from "react"
import { Slider, RangeSlider } from "@/components/ui/slider"

export function Pattern() {
  const [amount, setAmount] = useState([40])
  return <div className="flex w-full max-w-lg flex-col gap-4"><><Slider label="음량" value={amount} onValueChange={setAmount} valueLabel={amount[0]} aria-label="음량" /><RangeSlider label="가격 범위" defaultValue={[20, 80]} /><Slider label="사용 불가" defaultValue={[60]} disabled /></></div>
}
export default Pattern
