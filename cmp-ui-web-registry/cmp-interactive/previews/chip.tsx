"use client";

import * as React from "react";
import { Chip } from "@/components/ui/chip";

export default function Preview() {
  const [selected, setSelected] = React.useState(false);
  const [tags, setTags] = React.useState(["Compose", "React"]);
  return (
    <div className="flex flex-wrap items-center gap-ien-xs">
      <Chip kind="filter" selected={selected} onSelectedChange={setSelected}>
        즐겨찾기
      </Chip>
      {tags.map((tag) => (
        <Chip
          key={tag}
          kind="input"
          onRemove={() =>
            setTags((items) => items.filter((item) => item !== tag))
          }
        >
          {tag}
        </Chip>
      ))}
      <span aria-live="polite" className="text-ien-caption">
        필터 {selected ? "적용됨" : "해제됨"}
      </span>
    </div>
  );
}
