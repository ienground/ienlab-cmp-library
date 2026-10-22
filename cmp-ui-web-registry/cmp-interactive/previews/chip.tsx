"use client";

import * as React from "react";
import { XIcon } from "lucide-react";
import { Chip } from "@/components/ui/chip";

export default function Preview() {
  const [selected, setSelected] = React.useState(false);
  const [selectedTags, setSelectedTags] = React.useState(["Compose", "React"]);
  const [opened, setOpened] = React.useState(false);
  const [suggestionApplied, setSuggestionApplied] = React.useState(false);
  const [tags, setTags] = React.useState(["Compose", "React"]);

  return (
    <div className="flex flex-wrap items-center gap-ien-xs">
      <Chip
        kind="filter"
        tone="brand"
        selected={selected}
        onSelectedChange={setSelected}
      >
        즐겨찾기
      </Chip>
      <Chip
        kind="assist"
        tone="success"
        leadingIcon={<XIcon className="size-4" />}
        onClick={() => setOpened(true)}
      >
        {opened ? "도구 열림" : "도구 열기"}
      </Chip>
      {tags.map((tag) => (
        <Chip
          key={tag}
          kind="input"
          selected={selectedTags.includes(tag)}
          onSelectedChange={(next) =>
            setSelectedTags((items) =>
              next ? [...items, tag] : items.filter((item) => item !== tag),
            )
          }
          onRemove={() => {
            setTags((items) => items.filter((item) => item !== tag));
            setSelectedTags((items) => items.filter((item) => item !== tag));
          }}
        >
          {tag}
        </Chip>
      ))}
      <Chip
        kind="suggestion"
        loading
        tone="warning"
        icon={<XIcon className="size-4" />}
      >
        제안 로딩
      </Chip>
      <Chip
        kind="suggestion"
        tone="info"
        icon={<XIcon className="size-4" />}
        onClick={() => setSuggestionApplied(true)}
      >
        {suggestionApplied ? "제안 적용됨" : "제안 적용"}
      </Chip>
      <span aria-live="polite" className="text-ien-caption">
        필터 {selected ? "적용됨" : "해제됨"} · 도구 {opened ? "열림" : "닫힘"}
      </span>
    </div>
  );
}
