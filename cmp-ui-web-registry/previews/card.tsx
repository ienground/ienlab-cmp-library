"use client"

import { useState } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"

const variants = [
  { variant: "filled", toneVariant: "solid", title: "Filled · Solid" },
  { variant: "filled", toneVariant: "weak", title: "Filled · Weak" },
  { variant: "outlined", toneVariant: "solid", title: "Outlined · Solid" },
  { variant: "outlined", toneVariant: "weak", title: "Outlined · Weak" },
] as const

export function Pattern() {
  const [clicks, setClicks] = useState(0)
  return (
    <div className="grid w-full gap-4 sm:grid-cols-2">
      {variants.map(({ variant, toneVariant, title }) => (
        <Card key={title} variant={variant} tone="brand" toneVariant={toneVariant}>
          <CardHeader>
            <CardTitle>{title}</CardTitle>
            <CardDescription>브랜드 톤의 {toneVariant === "solid" ? "기본" : "약한"} 표현</CardDescription>
          </CardHeader>
          <CardContent>16px continuous 코너와 기본 콘텐츠 여백을 적용합니다.</CardContent>
        </Card>
      ))}
      <Card onClick={() => setClicks((value) => value + 1)} aria-label="카드 선택">
        <CardHeader>
          <CardTitle>클릭 가능한 기본 카드</CardTitle>
          <CardDescription>Neutral · Filled · Solid</CardDescription>
        </CardHeader>
        <CardContent><span role="status">선택 횟수: {clicks}</span> · Enter와 Space로도 선택할 수 있습니다.</CardContent>
      </Card>
      <Card variant="outlined" tonalElevation="raised" contentPadding={{ top: "lg", bottom: "lg", start: "xl", end: "xl" }} shape={{ radius: "xl" }} colors={{ border: "success" }}>
        <CardHeader>
          <CardTitle>사용자 지정 카드</CardTitle>
          <CardDescription>Outlined · Neutral · Tonal elevation raised</CardDescription>
        </CardHeader>
        <CardContent>여백·모서리·테두리 색상을 개별 지정할 수 있습니다.</CardContent>
      </Card>
    </div>
  )
}
export default Pattern
