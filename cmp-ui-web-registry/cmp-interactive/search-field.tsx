"use client";

import * as React from "react";
import { Search, X } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/ien-utils";

export type SearchFieldProps = Omit<
  React.ComponentProps<"input">,
  "type" | "value" | "defaultValue" | "onChange" | "content"
> & {
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  onSearch?: (value: string) => void;
  className?: string;
  state?: { enabled?: boolean; readOnly?: boolean };
  contentDescription?: string;
  fixed?: boolean;
  takeSpace?: boolean;
  onDeleteClick?: () => void;
  leading?: React.ReactNode;
  trailing?: React.ReactNode;
  deleteButton?: React.ReactNode;
};

function SearchField({
  value,
  defaultValue = "",
  onValueChange,
  onSearch,
  className,
  onKeyDown,
  disabled,
  readOnly,
  state,
  contentDescription,
  fixed = false,
  takeSpace = true,
  onDeleteClick,
  leading = <Search aria-hidden="true" className="size-[18px] shrink-0 text-ien-text-tertiary" />,
  trailing,
  deleteButton,
  ...props
}: SearchFieldProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const current = value ?? internalValue;
  const isDisabled = disabled || state?.enabled === false;
  const isReadOnly = readOnly || state?.readOnly === true;
  const update = (next: string) => {
    if (isDisabled || isReadOnly) return;
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
  };
  return (
    <form
      role="search"
      onSubmit={(event) => {
        event.preventDefault();
        if (!isDisabled) onSearch?.(current);
      }}
      data-fixed={fixed}
      data-take-space={takeSpace}
      className={cn(fixed && "px-ien-md py-ien-sm", className)}
    >
      <div className="flex h-11 items-center rounded-ien-lg bg-ien-surface-variant pl-[14px] pr-ien-xs">
      {leading}
      <Input
        {...props}
        type="search"
        aria-label={props["aria-label"] ?? contentDescription ?? "검색"}
        value={current}
        disabled={isDisabled}
        readOnly={isReadOnly}
        onChange={(event) => update(event.currentTarget.value)}
        onKeyDown={(event) => {
          onKeyDown?.(event);
          if (event.key === "Escape" && !event.defaultPrevented) update("");
        }}
        placeholder={props.placeholder ?? "검색"}
        className="h-6 min-w-0 flex-1 rounded-none border-0 bg-transparent px-ien-xs py-0 text-ien-body2 shadow-none focus-visible:ring-0 dark:bg-transparent [&::-webkit-search-cancel-button]:hidden"
      />
      {trailing ?? (current && !isDisabled && !isReadOnly && (deleteButton ?? (
        <Button
          type="button"
          variant="ghost"
          tone="neutral"
          size="icon-sm"
          aria-label="검색어 지우기"
          disabled={disabled}
          onClick={() => { update(""); onDeleteClick?.(); }}
          className="size-7 rounded-ien-full p-0 text-ien-text-secondary"
        >
          <X aria-hidden="true" />
        </Button>
      )))}
      </div>
    </form>
  );
}

export { SearchField, SearchField as IenSearchField };
