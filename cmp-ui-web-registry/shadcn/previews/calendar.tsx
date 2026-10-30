"use client";
import { Calendar } from "@/components/ui/calendar";
export default function Preview() {
  return (
    <Calendar
      mode="single"
      className="rounded-ien-default border border-ien-border"
    />
  );
}
