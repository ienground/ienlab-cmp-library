"use client";

import * as React from "react";
import { MoreHorizontal } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

export type MenuItem = {
  label: React.ReactNode;
  onSelect: () => void;
  disabled?: boolean;
  destructive?: boolean;
  separatorBefore?: boolean;
};
export type MenuProps = Omit<
  React.ComponentProps<typeof DropdownMenuTrigger>,
  "asChild" | "children" | "ref"
> & {
  ref?: React.Ref<HTMLButtonElement>;
  items: MenuItem[];
  trigger?: React.ReactNode;
  label?: string;
  header?: React.ReactNode;
  footer?: React.ReactNode;
  align?: "start" | "center" | "end";
};

function Menu({
  items,
  trigger,
  label = "메뉴 열기",
  align = "end",
  header,
  footer,
  ref,
  ...triggerProps
}: MenuProps) {
  return (
    <DropdownMenu>
      <DropdownMenuTrigger {...triggerProps} ref={ref} asChild>
        {trigger ?? (
          <Button
            type="button"
            variant="ghost"
            tone="neutral"
            size="icon"
            aria-label={label}
          >
            <MoreHorizontal aria-hidden="true" />
          </Button>
        )}
      </DropdownMenuTrigger>
      <DropdownMenuContent align={align}>
        {header}
        {items.map((item, index) => (
          <React.Fragment key={index}>
            {item.separatorBefore && <DropdownMenuSeparator />}
            <DropdownMenuItem
              disabled={item.disabled}
              onSelect={item.onSelect}
              className={
                item.destructive
                  ? "text-ien-danger focus:text-ien-danger"
                  : undefined
              }
            >
              {item.label}
            </DropdownMenuItem>
          </React.Fragment>
        ))}
        {footer}
      </DropdownMenuContent>
    </DropdownMenu>
  );
}

export { Menu, Menu as IenMenu };
