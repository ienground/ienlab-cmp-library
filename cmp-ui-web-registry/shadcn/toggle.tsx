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
import { Toggle as TogglePrimitive } from "radix-ui";

const toggleVariants = cva(
  "group/toggle inline-flex items-center justify-center gap-ien-xxs rounded-[var(--ien-control-radius-base)] text-ien-body2 font-[var(--ien-font-weight-label1)] whitespace-nowrap transition-all outline-none hover:bg-ien-surface-variant hover:text-ien-text-primary focus-visible:border-ien-brand focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 disabled:pointer-events-none disabled:opacity-[var(--ien-state-disabled-alpha)] aria-invalid:border-ien-danger aria-invalid:ring-ien-danger/20 aria-pressed:bg-ien-surface-variant data-[state=on]:bg-ien-surface-variant dark:aria-invalid:ring-ien-danger/40 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
  {
    variants: {
      variant: {
        default: "bg-transparent",
        outline:
          "border-ien-border border-ien-surface-variant bg-transparent hover:bg-ien-surface-variant",
      },
      size: {
        default:
          "h-8 min-w-8 px-[var(--ien-control-padding-inline)] has-data-[icon=inline-end]:pr-ien-xs has-data-[icon=inline-start]:pl-2",
        sm: "h-7 min-w-7 rounded-[min(var(--ien-control-radius-md),12px)] px-[var(--ien-control-padding-inline)] text-[0.8rem] has-data-[icon=inline-end]:pr-[var(--ien-control-gap)] has-data-[icon=inline-start]:pl-[var(--ien-control-gap)] [&_svg:not([class*='size-'])]:size-3.5",
        lg: "h-9 min-w-9 px-[var(--ien-control-padding-inline)] has-data-[icon=inline-end]:pr-ien-xs has-data-[icon=inline-start]:pl-2",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  },
);

function Toggle({
  className,
  variant = "default",
  size = "default",
  ...props
}: React.ComponentProps<typeof TogglePrimitive.Root> &
  VariantProps<typeof toggleVariants>) {
  return (
    <TogglePrimitive.Root
      data-slot="toggle"
      className={cn(toggleVariants({ variant, size, className }))}
      {...props}
    />
  );
}

export { Toggle, toggleVariants };
