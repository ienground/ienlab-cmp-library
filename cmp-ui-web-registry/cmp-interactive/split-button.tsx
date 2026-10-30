"use client";

import * as React from "react";
import { ChevronDown } from "lucide-react";
import { Button, type ButtonProps } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { cn } from "cn";

export type SplitButtonAction = {
  label: string;
  onSelect: () => void;
  disabled?: boolean;
};
export type SplitButtonProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  label: React.ReactNode;
  onClick: () => void;
  actions: SplitButtonAction[];
  variant?: ButtonProps["variant"];
  tone?: ButtonProps["tone"];
  disabled?: boolean;
  className?: string;
};

function SplitButton({
  label,
  onClick,
  actions,
  variant = "fill",
  tone = "brand",
  disabled = false,
  className,
  ref,
  ...rootProps
}: SplitButtonProps) {
  return (
    <div
      {...rootProps}
      ref={ref}
      data-slot="split-button"
      className={cn("inline-flex", className)}
    >
      <Button
        className="rounded-r-none"
        variant={variant}
        tone={tone}
        disabled={disabled}
        onClick={onClick}
      >
        {label}
      </Button>
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            className="rounded-l-none border-l border-ien-on-brand/25 px-ien-xs"
            variant={variant}
            tone={tone}
            disabled={disabled}
            aria-label="추가 동작 열기"
          >
            <ChevronDown aria-hidden="true" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end">
          {actions.map((action) => (
            <DropdownMenuItem
              key={action.label}
              disabled={action.disabled}
              onSelect={action.onSelect}
            >
              {action.label}
            </DropdownMenuItem>
          ))}
        </DropdownMenuContent>
      </DropdownMenu>
    </div>
  );
}

export { SplitButton, SplitButton as IenSplitButton };
