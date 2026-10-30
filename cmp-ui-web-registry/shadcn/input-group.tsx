"use client";

/*
 * MIT License
 *
 * Copyright (c) 2023 shadcn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "cn";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";

function InputGroup({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="input-group"
      role="group"
      className={cn(
        "group/input-group relative flex h-8 w-full min-w-0 items-center rounded-[var(--ien-control-radius-base)] border-ien-border border-ien-surface-variant transition-colors outline-none in-data-[slot=combobox-content]:focus-within:border-inherit in-data-[slot=combobox-content]:focus-within:ring-0 has-disabled:bg-ien-surface-variant/50 has-disabled:opacity-[var(--ien-state-disabled-alpha)] has-[[data-slot=input-group-control]:focus-visible]:border-ien-brand has-[[data-slot=input-group-control]:focus-visible]:ring-[length:var(--ien-control-focus-ring-width)] has-[[data-slot=input-group-control]:focus-visible]:ring-ien-brand/50 has-[[data-slot][aria-invalid=true]]:border-ien-danger has-[[data-slot][aria-invalid=true]]:ring-[length:var(--ien-control-focus-ring-width)] has-[[data-slot][aria-invalid=true]]:ring-ien-danger/20 has-[>[data-align=block-end]]:h-auto has-[>[data-align=block-end]]:flex-col has-[>[data-align=block-start]]:h-auto has-[>[data-align=block-start]]:flex-col has-[>textarea]:h-auto dark:bg-ien-surface-variant/30 dark:has-disabled:bg-ien-surface-variant/80 dark:has-[[data-slot][aria-invalid=true]]:ring-ien-danger/40 has-[>[data-align=block-end]]:[&>input]:pt-ien-sm has-[>[data-align=block-start]]:[&>input]:pb-ien-sm has-[>[data-align=inline-end]]:[&>input]:pr-[var(--ien-control-gap)] has-[>[data-align=inline-start]]:[&>input]:pl-ien-xxs",
        className,
      )}
      {...props}
    />
  );
}

const inputGroupAddonVariants = cva(
  "flex h-auto cursor-text items-center justify-center gap-ien-xs py-[var(--ien-control-gap)] text-ien-body2 font-[var(--ien-font-weight-label1)] text-ien-text-secondary select-none group-data-[disabled=true]/input-group:opacity-50 [&>kbd]:rounded-[calc(var(--ien-control-radius-base)-5px)] [&>svg:not([class*='size-'])]:size-4",
  {
    variants: {
      align: {
        "inline-start":
          "order-first pl-ien-xs has-[>button]:ml-[-0.3rem] has-[>kbd]:ml-[-0.15rem]",
        "inline-end":
          "order-last pr-ien-xs has-[>button]:mr-[-0.3rem] has-[>kbd]:mr-[-0.15rem]",
        "block-start":
          "order-first w-full justify-start px-[var(--ien-control-padding-inline)] pt-ien-xs group-has-[>input]/input-group:pt-ien-xs [.border-b]:pb-2",
        "block-end":
          "order-last w-full justify-start px-[var(--ien-control-padding-inline)] pb-ien-xs group-has-[>input]/input-group:pb-ien-xs [.border-t]:pt-2",
      },
    },
    defaultVariants: {
      align: "inline-start",
    },
  },
);

function InputGroupAddon({
  className,
  align = "inline-start",
  ...props
}: React.ComponentProps<"div"> & VariantProps<typeof inputGroupAddonVariants>) {
  return (
    <div
      role="group"
      data-slot="input-group-addon"
      data-align={align}
      className={cn(inputGroupAddonVariants({ align }), className)}
      onClick={(e) => {
        if ((e.target as HTMLElement).closest("button")) {
          return;
        }
        e.currentTarget.parentElement?.querySelector("input")?.focus();
      }}
      {...props}
    />
  );
}

const inputGroupButtonVariants = cva(
  "flex items-center gap-ien-xs text-ien-body2 shadow-none",
  {
    variants: {
      size: {
        xs: "h-6 gap-ien-xxs rounded-[calc(var(--ien-control-radius-base)-3px)] px-[var(--ien-control-gap)] [&>svg:not([class*='size-'])]:size-3.5",
        sm: "",
        "icon-xs":
          "size-[var(--ien-icon-lg)] rounded-[calc(var(--ien-control-radius-base)-3px)] p-ien-none has-[>svg]:p-0",
        "icon-sm": "size-8 p-ien-none has-[>svg]:p-0",
      },
    },
    defaultVariants: {
      size: "xs",
    },
  },
);

function InputGroupButton({
  className,
  type = "button",
  variant = "ghost",
  size = "xs",
  ...props
}: Omit<React.ComponentProps<typeof Button>, "size"> &
  VariantProps<typeof inputGroupButtonVariants>) {
  return (
    <Button
      type={type}
      data-size={size}
      variant={variant}
      className={cn(inputGroupButtonVariants({ size }), className)}
      {...props}
    />
  );
}

function InputGroupText({ className, ...props }: React.ComponentProps<"span">) {
  return (
    <span
      className={cn(
        "flex items-center gap-ien-xs text-ien-body2 text-ien-text-secondary [&_svg]:pointer-events-none [&_svg:not([class*='size-'])]:size-4",
        className,
      )}
      {...props}
    />
  );
}

function InputGroupInput({
  className,
  ...props
}: React.ComponentProps<"input">) {
  return (
    <Input
      data-slot="input-group-control"
      className={cn(
        "flex-1 rounded-ien-none border-0 bg-transparent shadow-none ring-0 focus-visible:ring-0 disabled:bg-transparent aria-invalid:ring-0 dark:bg-transparent dark:disabled:bg-transparent",
        className,
      )}
      {...props}
    />
  );
}

function InputGroupTextarea({
  className,
  ...props
}: React.ComponentProps<"textarea">) {
  return (
    <Textarea
      data-slot="input-group-control"
      className={cn(
        "flex-1 resize-none rounded-ien-none border-0 bg-transparent py-ien-xs shadow-none ring-0 focus-visible:ring-0 disabled:bg-transparent aria-invalid:ring-0 dark:bg-transparent dark:disabled:bg-transparent",
        className,
      )}
      {...props}
    />
  );
}

export {
  InputGroup,
  InputGroupAddon,
  InputGroupButton,
  InputGroupText,
  InputGroupInput,
  InputGroupTextarea,
};
