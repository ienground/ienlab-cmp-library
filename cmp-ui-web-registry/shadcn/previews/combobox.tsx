"use client";
import * as React from "react";
import { Combobox } from "@/components/ui/combobox";
export default function Preview() {
  const [value, setValue] = React.useState("");
  return (
    <div className="grid max-w-xs gap-ien-xs">
      <Combobox
        value={value}
        onValueChange={setValue}
        options={[
          { value: "react", label: "React" },
          { value: "next", label: "Next.js" },
          { value: "vite", label: "Vite" },
        ]}
      />
      <span className="text-ien-caption">선택: {value || "없음"}</span>
    </div>
  );
}
