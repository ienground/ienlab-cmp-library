"use client";

import * as React from "react";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";

export default function Preview() {
  const [alignment, setAlignment] = React.useState("center");
  return (
    <div className="grid gap-ien-xs">
      <ToggleGroup
        type="single"
        value={alignment}
        onValueChange={(next) => next && setAlignment(next)}
        aria-label="정렬"
      >
        <ToggleGroupItem value="left">왼쪽</ToggleGroupItem>
        <ToggleGroupItem value="center">가운데</ToggleGroupItem>
        <ToggleGroupItem value="right">오른쪽</ToggleGroupItem>
      </ToggleGroup>
      <p aria-live="polite" className="text-ien-caption">
        선택: {alignment}
      </p>
    </div>
  );
}
