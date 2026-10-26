"use client"

import { useEffect, useState } from "react"
import { useIenLanguage } from "@/lib/ien-theme-context"
import {
  DateSelector,
  type DateSelectorValue,
} from "@/components/reui/date-selector"
import { format } from "date-fns"

import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Card, CardContent } from "@/components/ui/card"

export function Pattern() {
  const language = useIenLanguage("ko")
  const [locale, setLocale] = useState<"ko" | "en">(language)
  const [value, setValue] = useState<DateSelectorValue | undefined>()

  useEffect(() => setLocale(language), [language])

  return (
    <div className="flex w-full flex-col items-center gap-5">
      <Tabs value={locale} onValueChange={(value) => {
        if (value === "ko" || value === "en") setLocale(value)
      }} aria-label="언어 / Language">
        <TabsList>
          <TabsTrigger value="ko">한국어</TabsTrigger>
          <TabsTrigger value="en">English</TabsTrigger>
        </TabsList>
      </Tabs>
      <Card className="p-0">
        <CardContent className="p-3">
          <DateSelector
            locale={locale}
            value={value}
            onChange={setValue}
            label={locale === "ko" ? "마감일" : "Due date"}
            inputHint={locale === "ko" ? "예: 2025, Q4, 2025-10-05" : "Try: 2025, Q4, 05/10/2025"}
          />
        </CardContent>
      </Card>

      {value ? (
        <pre className="bg-muted w-full overflow-auto rounded-md p-3 font-mono text-xs md:w-[500px]">
          {JSON.stringify(
            value,
            (key, val) => {
              if (val instanceof Date) {
                return format(val, "MM/dd/yyyy")
              }
              return val
            },
            2
          )}
        </pre>
      ) : (
        <div className="text-muted-foreground text-sm">
          {locale === "ko" ? "날짜를 선택하면 선택한 값이 표시됩니다." : "Select a date to see the selected value."}
        </div>
      )}
    </div>
  )
}
export default Pattern;
