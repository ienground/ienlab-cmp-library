"use client";

import * as React from "react";
import { SearchField } from "@/components/ui/search-field";

export default function Preview() {
  const [query, setQuery] = React.useState("");
  const [submitted, setSubmitted] = React.useState("");
  return (
    <div className="grid max-w-md gap-ien-xs">
      <SearchField
        value={query}
        onValueChange={setQuery}
        onSearch={setSubmitted}
        placeholder="도서 검색"
      />
      <p aria-live="polite" className="text-ien-caption">
        {submitted
          ? "검색어: " + submitted
          : "검색어를 입력하고 Enter를 누르세요."}
      </p>
    </div>
  );
}
