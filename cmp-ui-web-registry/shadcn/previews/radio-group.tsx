"use client";
import * as React from "react";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
export default function Preview() {
  const [v, setV] = React.useState("comfortable");
  return (
    <RadioGroup value={v} onValueChange={setV} className="grid gap-ien-sm">
      <Label htmlFor="compact">
        <RadioGroupItem id="compact" value="compact" /> 간결하게
      </Label>
      <Label htmlFor="comfortable">
        <RadioGroupItem id="comfortable" value="comfortable" /> 편안하게
      </Label>
    </RadioGroup>
  );
}
