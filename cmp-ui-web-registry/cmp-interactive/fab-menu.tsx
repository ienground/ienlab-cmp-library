"use client";

import * as React from "react";
import { Plus, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "cn";

export type FabMenuAction = {
  label: string;
  icon?: React.ReactNode;
  onSelect: () => void;
  disabled?: boolean;
};
export type FabMenuProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  actions: FabMenuAction[];
  label?: string;
  className?: string;
};

function FabMenu({
  actions,
  label = "빠른 동작",
  className,
  ref,
  ...rootProps
}: FabMenuProps) {
  const [open, setOpen] = React.useState(false);
  return (
    <div
      {...rootProps}
      ref={ref}
      data-slot="fab-menu"
      className={cn("relative flex flex-col items-end gap-ien-sm", className)}
    >
      {open && (
        <div
          className="flex flex-col items-end gap-ien-xs"
          role="group"
          aria-label={label}
        >
          {actions.map((action) => (
            <Button
              key={action.label}
              variant="weak"
              tone="neutral"
              disabled={action.disabled}
              onClick={() => {
                action.onSelect();
                setOpen(false);
              }}
              className="min-h-[var(--ien-state-minimum-touch-target)] gap-ien-xs rounded-ien-full shadow-[0_var(--ien-elevation-raised)_calc(var(--ien-elevation-raised)*2)_rgb(0_0_0/0.12)]"
            >
              {action.icon}
              <span>{action.label}</span>
            </Button>
          ))}
        </div>
      )}
      <Button
        variant="fill"
        tone="brand"
        size="icon-lg"
        aria-label={open ? "빠른 동작 닫기" : label}
        aria-expanded={open}
        onClick={() => setOpen((value) => !value)}
        className="size-14 rounded-ien-xl shadow-[0_var(--ien-elevation-floating)_calc(var(--ien-elevation-floating)*2)_rgb(0_0_0/0.16)]"
      >
        {open ? <X aria-hidden="true" /> : <Plus aria-hidden="true" />}
      </Button>
    </div>
  );
}

export { FabMenu, FabMenu as IenFabMenu };
