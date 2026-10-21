"use client";

import * as React from "react";
import { Search, X } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { cn } from "cn";

export type SearchFieldProps = Omit<
  React.ComponentProps<"input">,
  "type" | "value" | "defaultValue" | "onChange"
> & {
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  onSearch?: (value: string) => void;
  className?: string;
};

function SearchField({
  value,
  defaultValue = "",
  onValueChange,
  onSearch,
  className,
  onKeyDown,
  disabled,
  ...props
}: SearchFieldProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const current = value ?? internalValue;
  const update = (next: string) => {
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
  };
  return (
    <form
      role="search"
      onSubmit={(event) => {
        event.preventDefault();
        onSearch?.(current);
      }}
      className={cn("relative flex items-center", className)}
    >
      <Search
        aria-hidden="true"
        className="pointer-events-none absolute left-3 size-4 text-ien-text-tertiary"
      />
      <Input
        {...props}
        type="search"
        aria-label={props["aria-label"] ?? "검색"}
        value={current}
        disabled={disabled}
        onChange={(event) => update(event.currentTarget.value)}
        onKeyDown={(event) => {
          onKeyDown?.(event);
          if (event.key === "Escape" && !event.defaultPrevented) update("");
        }}
        placeholder={props.placeholder ?? "검색"}
        className="h-[var(--ien-state-minimum-touch-target)] rounded-ien-full bg-ien-surface pl-ien-xxxl pr-[var(--ien-state-minimum-touch-target)] text-ien-body2"
      />
      {current && (
        <Button
          type="button"
          variant="ghost"
          tone="neutral"
          size="icon-sm"
          aria-label="검색어 지우기"
          disabled={disabled}
          onClick={() => update("")}
          className="absolute right-1 rounded-ien-full"
        >
          <X aria-hidden="true" />
        </Button>
      )}
    </form>
  );
}

export { SearchField, SearchField as IenSearchField };
