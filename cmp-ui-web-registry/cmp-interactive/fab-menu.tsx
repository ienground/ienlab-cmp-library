"use client";

import * as React from "react";
import { Plus, X } from "lucide-react";
import { Button, type ButtonProps } from "@/components/ui/button";
import { Fab, type FabProps } from "@/components/ui/fab";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { cn } from "@/lib/ien-utils";

type FabMenuActionBase = {
  key?: React.Key;
  label: React.ReactNode;
  icon?: React.ReactNode;
  disabled?: boolean;
  tone?: ButtonProps["tone"];
  variant?: ButtonProps["variant"];
  colors?: ButtonProps["colors"];
  containerColor?: string;
  contentColor?: string;
  state?: {
    enabled?: boolean;
    loading?: boolean;
  };
  onClickLabel?: string;
};
export type FabMenuAction = FabMenuActionBase &
  (
    | { onSelect: () => void; onClick?: never }
    | { onSelect?: never; onClick: () => void }
  );
export type FabMenuProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  actions?: FabMenuAction[];
  children?: React.ReactNode;
  label?: string;
  closeLabel?: string;
  expanded?: boolean;
  defaultExpanded?: boolean;
  onExpandedChange?: (expanded: boolean) => void;
  horizontalAlignment?: "start" | "center" | "end";
  button?: React.ReactElement;
  fabProps?: Omit<FabProps, "label" | "onClick" | "children" | "aria-label">;
  className?: string;
};

function FabMenu({
  actions,
  children,
  label = "빠른 동작",
  closeLabel = "빠른 동작 닫기",
  expanded,
  defaultExpanded = false,
  onExpandedChange,
  horizontalAlignment = "end",
  button,
  fabProps,
  className,
  ref,
  ...rootProps
}: FabMenuProps) {
  const [internalExpanded, setInternalExpanded] = React.useState(defaultExpanded);
  const open = expanded ?? internalExpanded;
  const setOpen = (nextExpanded: boolean) => {
    if (expanded === undefined) setInternalExpanded(nextExpanded);
    onExpandedChange?.(nextExpanded);
  };
  return (
    <div
      {...rootProps}
      ref={ref}
      data-slot="fab-menu"
      className={cn("relative flex flex-col items-end gap-ien-sm", className)}
    >
      <Popover open={open} onOpenChange={setOpen}>
        <PopoverContent
          side="top"
          align={horizontalAlignment}
          role="group"
          aria-label={label}
          className="flex w-auto flex-col items-end gap-ien-xs border-0 bg-transparent p-0 shadow-none ring-0 duration-[var(--ien-motion-normal)] ease-[var(--ien-motion-standard-easing)] motion-reduce:animate-none"
        >
          {actions?.map((action, index) => (
            <Button
              key={action.key ?? `${index}-${String(action.label)}`}
              type="button"
              variant={action.variant ?? "weak"}
              tone={action.tone ?? "brand"}
              colors={
                action.colors ??
                (action.containerColor || action.contentColor
                  ? {
                      container: action.containerColor,
                      content: action.contentColor,
                    }
                  : undefined)
              }
              state={{
                enabled: action.state?.enabled ?? !action.disabled,
                loading: action.state?.loading ?? false,
              }}
              disabled={action.disabled || action.state?.enabled === false}
              aria-label={action.onClickLabel}
              onClick={() => {
                (action.onSelect ?? action.onClick)?.();
                setOpen(false);
              }}
              className="min-h-[var(--ien-state-minimum-touch-target)] gap-ien-xs rounded-ien-full text-ien-label1 shadow-[0_var(--ien-elevation-raised)_calc(var(--ien-elevation-raised)*2)_rgb(0_0_0/0.12)] animate-in fade-in-0 slide-in-from-bottom-2 duration-[var(--ien-motion-normal)] motion-reduce:animate-none"
              style={{
                animationDelay: `${Math.max(0, actions.length - index - 1) * 40}ms`,
              }}
            >
              {action.icon && <span aria-hidden="true">{action.icon}</span>}
              <span>{action.label}</span>
            </Button>
          ))}
          {children}
        </PopoverContent>
        <PopoverTrigger asChild>
          {button ?? (
            <Fab
              {...fabProps}
              type="button"
              label={open ? closeLabel : label}
              aria-label={open ? closeLabel : label}
              size={fabProps?.size ?? "regular"}
              className={cn(
                "shadow-[0_var(--ien-elevation-floating)_calc(var(--ien-elevation-floating)*2)_rgb(0_0_0/0.16)]",
                fabProps?.className,
              )}
            >
              <span className="grid place-items-center">
                <Plus
                  aria-hidden="true"
                  className={cn(
                    "col-start-1 row-start-1 transition-[opacity,scale] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
                    open ? "scale-75 opacity-0" : "scale-100 opacity-100",
                  )}
                />
                <X
                  aria-hidden="true"
                  className={cn(
                    "col-start-1 row-start-1 transition-[opacity,scale] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
                    open ? "scale-100 opacity-100" : "scale-75 opacity-0",
                  )}
                />
              </span>
            </Fab>
          )}
        </PopoverTrigger>
      </Popover>
    </div>
  );
}

export { FabMenu, FabMenu as IenFabMenu };
