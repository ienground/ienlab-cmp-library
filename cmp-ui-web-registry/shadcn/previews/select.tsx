"use client";
import {
  Select,
  SelectContent,
  SelectItem,
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
        <SelectItem value="light">밝게</SelectItem>
        <SelectItem value="dark">어둡게</SelectItem>
        <SelectItem value="system">시스템</SelectItem>
      </SelectContent>
    </Select>
  );
}
