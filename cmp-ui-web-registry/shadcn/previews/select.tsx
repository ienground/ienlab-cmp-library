"use client";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
export default function Preview() {
  return (
    <Select defaultValue="system">
      <SelectTrigger className="w-48">
        <SelectValue placeholder="테마 선택" />
      </SelectTrigger>
      <SelectContent>
        <SelectGroup>
          <SelectLabel>테마</SelectLabel>
          <SelectItem value="light">밝게</SelectItem>
          <SelectItem value="dark">어둡게</SelectItem>
          <SelectItem value="system">시스템</SelectItem>
          <SelectItem value="contrast" disabled>
            고대비 (준비 중)
          </SelectItem>
        </SelectGroup>
      </SelectContent>
    </Select>
  );
}
