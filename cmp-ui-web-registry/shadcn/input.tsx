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
import { cn } from "@/lib/ien-utils";

function Input({ className, type, ...props }: React.ComponentProps<"input">) {
  return (
    <input
      type={type}
      data-slot="input"
      className={cn(
        "h-8 w-full min-w-0 rounded-[var(--ien-control-radius-base)] border-ien-border border-ien-surface-variant bg-transparent px-[var(--ien-control-padding-inline)] py-ien-xxs text-ien-body1 transition-colors outline-none file:inline-flex file:h-6 file:border-0 file:bg-transparent file:text-ien-body2 file:font-[var(--ien-font-weight-label1)] file:text-ien-text-primary placeholder:text-ien-text-secondary focus-visible:border-ien-brand focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 disabled:pointer-events-none disabled:cursor-not-allowed disabled:bg-ien-surface-variant/50 disabled:opacity-[var(--ien-state-disabled-alpha)] aria-invalid:border-ien-danger aria-invalid:ring-[length:var(--ien-control-focus-ring-width)] aria-invalid:ring-ien-danger/20 md:text-ien-body2 dark:bg-ien-surface-variant/30 dark:disabled:bg-ien-surface-variant/80 dark:aria-invalid:border-ien-danger/50 dark:aria-invalid:ring-ien-danger/40",
        className,
      )}
      {...props}
    />
  );
}

export { Input };
