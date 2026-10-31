import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
  DropdownMenuCheckboxItem,
  DropdownMenuRadioGroup,
  DropdownMenuRadioItem,
  DropdownMenuSub,
  DropdownMenuSubTrigger,
  DropdownMenuSubContent,
} from "@/components/ui/dropdown-menu"

export function Pattern() {
  const [notifications, setNotifications] = useState(true)
  const [theme, setTheme] = useState("system")
  return (
    <div className="flex items-center justify-center">
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button variant="line" className="w-fit">
            메뉴 열기
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent>
          <DropdownMenuGroup>
            <DropdownMenuLabel>내 계정</DropdownMenuLabel>
            <DropdownMenuItem selected><UserIcon />프로필</DropdownMenuItem>
            <DropdownMenuItem><SettingsIcon />설정</DropdownMenuItem>
            <DropdownMenuItem disabled>사용할 수 없는 항목</DropdownMenuItem>
          </DropdownMenuGroup>
          <DropdownMenuSeparator />
          <DropdownMenuGroup>
            <DropdownMenuCheckboxItem checked={notifications} onCheckedChange={setNotifications}>알림 받기</DropdownMenuCheckboxItem>
            <DropdownMenuSub>
              <DropdownMenuSubTrigger>테마</DropdownMenuSubTrigger>
              <DropdownMenuSubContent>
                <DropdownMenuRadioGroup value={theme} onValueChange={setTheme}>
                  <DropdownMenuRadioItem value="system">시스템 설정</DropdownMenuRadioItem>
                  <DropdownMenuRadioItem value="light">라이트</DropdownMenuRadioItem>
                  <DropdownMenuRadioItem value="dark">다크</DropdownMenuRadioItem>
                </DropdownMenuRadioGroup>
              </DropdownMenuSubContent>
            </DropdownMenuSub>
            <DropdownMenuItem variant="destructive">삭제</DropdownMenuItem>
          </DropdownMenuGroup>
        </DropdownMenuContent>
      </DropdownMenu>
    </div>
  )
}
export default Pattern;
"use client"

import { useState } from "react"
import { IconUser as UserIcon, IconSettings as SettingsIcon } from "@tabler/icons-react"
