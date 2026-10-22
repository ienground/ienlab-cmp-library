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
import { cn } from "@/lib/ien-utils";
import { Slot } from "radix-ui";

const markerVariants = cva(
  "group/marker relative flex min-h-4 w-full items-center gap-ien-xs text-left text-ien-body2 text-ien-text-secondary [&_svg:not([class*='size-'])]:size-[var(--ien-icon-sm)] [a]:underline [a]:underline-offset-3 [a]:hover:text-ien-text-primary",
  {
    variants: {
      variant: {
        default: "",
        separator:
          "before:mr-ien-xxs before:h-px before:min-w-0 before:flex-1 before:bg-ien-border after:ml-ien-xxs after:h-px after:min-w-0 after:flex-1 after:bg-ien-border",
        border: "border-b border-ien-border pb-ien-xs",
      },
    },
  },
);

function Marker({
  className,
  variant = "default",
  asChild = false,
  ...props
}: React.ComponentProps<"div"> &
  VariantProps<typeof markerVariants> & {
    asChild?: boolean;
  }) {
  const Comp = asChild ? Slot.Root : "div";

  return (
    <Comp
      data-slot="marker"
      data-variant={variant}
      className={cn(markerVariants({ variant, className }))}
      {...props}
    />
  );
}

function MarkerIcon({ className, ...props }: React.ComponentProps<"span">) {
  return (
    <span
      data-slot="marker-icon"
      aria-hidden="true"
      className={cn(
        "size-[var(--ien-icon-sm)] shrink-0 [&_svg:not([class*='size-'])]:size-4",
        className,
      )}
      {...props}
    />
  );
}

function MarkerContent({ className, ...props }: React.ComponentProps<"span">) {
  return (
    <span
      data-slot="marker-content"
      className={cn(
        "min-w-0 wrap-break-word group-data-[variant=separator]/marker:flex-none group-data-[variant=separator]/marker:text-center *:[a]:underline *:[a]:underline-offset-3 *:[a]:hover:text-ien-text-primary",
        className,
      )}
      {...props}
    />
  );
}

export { Marker, MarkerIcon, MarkerContent, markerVariants };
