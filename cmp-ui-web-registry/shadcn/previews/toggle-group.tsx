"use client";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";
export default function Preview() {
  return (
    <ToggleGroup type="single" defaultValue="center" aria-label="정렬">
      <ToggleGroupItem value="left">왼쪽</ToggleGroupItem>
      <ToggleGroupItem value="center">가운데</ToggleGroupItem>
      <ToggleGroupItem value="right">오른쪽</ToggleGroupItem>
    </ToggleGroup>
  );
}
