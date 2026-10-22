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
import { cn } from "@/lib/ien-utils";

export type SplitButtonAction = {
  label: string;
  onSelect: () => void;
  disabled?: boolean;
};
export type SplitButtonState = { enabled?: boolean; loading?: boolean };
export type SplitButtonProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  label: React.ReactNode;
  onClick: () => void;
  onTrailingClick?: () => void;
  actions: SplitButtonAction[];
  variant?: ButtonProps["variant"];
  tone?: ButtonProps["tone"];
  size?: "small" | "medium" | "large";
  display?: ButtonProps["display"];
  state?: SplitButtonState;
  colors?: ButtonProps["colors"];
  expanded?: boolean;
  defaultExpanded?: boolean;
  onExpandedChange?: (expanded: boolean) => void;
  trailingContent?: React.ReactNode;
  trailingContentDescription?: string;
  disabled?: boolean;
  className?: string;
};

function SplitButton({
  label,
  onClick,
  onTrailingClick,
  actions,
  variant = "fill",
  tone = "brand",
  size = "large",
  display = "inline",
  state,
  colors,
  expanded,
  defaultExpanded = false,
  onExpandedChange,
  trailingContent,
  trailingContentDescription = "추가 동작",
  disabled = false,
  className,
  ref,
  ...rootProps
}: SplitButtonProps) {
  const [internalExpanded, setInternalExpanded] = React.useState(defaultExpanded);
  const isExpanded = expanded ?? internalExpanded;
  const enabled = !disabled && state?.enabled !== false;
  const loading = state?.loading === true;
  const handleExpandedChange = (nextExpanded: boolean) => {
    if (expanded === undefined) setInternalExpanded(nextExpanded);
    onExpandedChange?.(nextExpanded);
  };
  const dimension = size === "small" ? "w-9" : size === "medium" ? "w-11" : "w-[52px]";
  const outerRadius = display === "full" ? "0px" : size === "small" ? "18px" : size === "medium" ? "22px" : "26px";
  const innerRadius = "var(--ien-radius-xs)";
  return (
    <div
      {...rootProps}
      ref={ref}
      data-slot="split-button"
      data-expanded={isExpanded}
      className={cn(display === "inline" ? "inline-flex" : "flex w-full", "items-stretch gap-[var(--ien-spacing-xxxs)]", className)}
    >
      <Button
        className="min-w-0 flex-1"
        variant={variant}
        tone={tone}
        size={size}
        display="inline"
        shape={`${outerRadius} ${innerRadius} ${innerRadius} ${outerRadius}`}
        colors={colors}
        state={{ enabled, loading }}
        disabled={!enabled}
        onClick={onClick}
      >
        {label}
      </Button>
      <DropdownMenu open={expanded} defaultOpen={defaultExpanded} onOpenChange={handleExpandedChange}>
        <DropdownMenuTrigger asChild>
          <Button
            className={cn("shrink-0 px-0", dimension)}
            variant={variant}
            tone={tone}
            size={size}
            display="inline"
            shape={`${innerRadius} ${outerRadius} ${outerRadius} ${innerRadius}`}
            colors={colors}
            state={{ enabled: enabled && !loading }}
            disabled={!enabled || loading}
            onClick={onTrailingClick}
            aria-label={trailingContentDescription}
            aria-expanded={isExpanded}
            aria-haspopup="menu"
          >
            <span
              className={cn(
                "inline-flex transition-transform duration-[var(--ien-motion-fast)] motion-reduce:transition-none",
                trailingContent === undefined && isExpanded && "rotate-180",
              )}
            >
              {trailingContent ?? <ChevronDown aria-hidden="true" />}
            </span>
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end">
          {actions.map((action) => (
            <DropdownMenuItem
              key={action.label}
              disabled={action.disabled || !enabled || loading}
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
