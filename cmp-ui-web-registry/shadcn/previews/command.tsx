"use client";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
  CommandShortcut,
} from "@/components/ui/command";
import { CalendarIcon } from "lucide-react";
export default function Preview() {
  return (
    <Command className="rounded-ien-default border border-ien-border">
      <CommandInput placeholder="명령 검색…" />
      <CommandList>
        <CommandEmpty>결과가 없습니다.</CommandEmpty>
        <CommandGroup heading="이동">
          <CommandItem>
            <CalendarIcon />
            캘린더<CommandShortcut>⌘K</CommandShortcut>
          </CommandItem>
          <CommandItem>설정</CommandItem>
        </CommandGroup>
      </CommandList>
    </Command>
  );
}
