"use client";
import { Toggle } from "@/components/ui/toggle";
import { BoldIcon } from "lucide-react";
export default function Preview() {
  return (
    <Toggle aria-label="굵게" defaultPressed>
      <BoldIcon />
    </Toggle>
  );
}
