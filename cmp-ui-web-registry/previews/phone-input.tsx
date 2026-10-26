"use client"

import { useIenLanguage } from "@/lib/ien-theme-context"
import { PhoneInput } from "@/components/reui/phone-input"

export function Pattern() {
  const language = useIenLanguage()
  return <PhoneInput placeholder={language === "ko" ? "전화번호를 입력하세요" : "Enter phone number"} />
}
export default Pattern;
