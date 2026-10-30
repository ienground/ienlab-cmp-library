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
import { cn } from "cn";

function TypographyH1({ className, ...props }: React.ComponentProps<"h1">) {
  return (
    <h1
      className={cn(
        "scroll-m-20 text-ien-title1 font-[var(--ien-font-weight-title1)] tracking-tight text-ien-text-primary",
        className,
      )}
      {...props}
    />
  );
}
function TypographyH2({ className, ...props }: React.ComponentProps<"h2">) {
  return (
    <h2
      className={cn(
        "scroll-m-20 border-b border-ien-border pb-ien-xs text-ien-title2 font-[var(--ien-font-weight-title2)] tracking-tight text-ien-text-primary",
        className,
      )}
      {...props}
    />
  );
}
function TypographyH3({ className, ...props }: React.ComponentProps<"h3">) {
  return (
    <h3
      className={cn(
        "scroll-m-20 text-ien-title3 font-[var(--ien-font-weight-title3)] tracking-tight text-ien-text-primary",
        className,
      )}
      {...props}
    />
  );
}
function TypographyH4({ className, ...props }: React.ComponentProps<"h4">) {
  return (
    <h4
      className={cn(
        "scroll-m-20 text-ien-body1 font-[var(--ien-font-weight-label1)] tracking-tight text-ien-text-primary",
        className,
      )}
      {...props}
    />
  );
}
function TypographyP({ className, ...props }: React.ComponentProps<"p">) {
  return (
    <p
      className={cn(
        "text-ien-body2 leading-relaxed text-ien-text-primary [&:not(:first-child)]:mt-ien-md",
        className,
      )}
      {...props}
    />
  );
}
function TypographyLead({ className, ...props }: React.ComponentProps<"p">) {
  return (
    <p
      className={cn("text-ien-body1 text-ien-text-secondary", className)}
      {...props}
    />
  );
}
function TypographyLarge({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      className={cn(
        "text-ien-label1 font-[var(--ien-font-weight-label1)] text-ien-text-primary",
        className,
      )}
      {...props}
    />
  );
}
function TypographySmall({
  className,
  ...props
}: React.ComponentProps<"small">) {
  return (
    <small
      className={cn(
        "text-ien-caption font-[var(--ien-font-weight-label1)] leading-none text-ien-text-primary",
        className,
      )}
      {...props}
    />
  );
}
function TypographyMuted({ className, ...props }: React.ComponentProps<"p">) {
  return (
    <p
      className={cn("text-ien-body2 text-ien-text-secondary", className)}
      {...props}
    />
  );
}
function TypographyBlockquote({
  className,
  ...props
}: React.ComponentProps<"blockquote">) {
  return (
    <blockquote
      className={cn(
        "mt-ien-md border-l-2 border-ien-brand pl-ien-md text-ien-body2 italic text-ien-text-secondary",
        className,
      )}
      {...props}
    />
  );
}
function TypographyList({ className, ...props }: React.ComponentProps<"ul">) {
  return (
    <ul
      className={cn(
        "my-ien-md ml-ien-lg list-disc [&>li]:mt-ien-xs",
        className,
      )}
      {...props}
    />
  );
}
function TypographyInlineCode({
  className,
  ...props
}: React.ComponentProps<"code">) {
  return (
    <code
      className={cn(
        "relative rounded-ien-xs bg-ien-surface-variant px-ien-xxs py-[2px] font-ien-mono text-ien-caption text-ien-text-primary",
        className,
      )}
      {...props}
    />
  );
}

export {
  TypographyH1,
  TypographyH2,
  TypographyH3,
  TypographyH4,
  TypographyP,
  TypographyLead,
  TypographyLarge,
  TypographySmall,
  TypographyMuted,
  TypographyBlockquote,
  TypographyList,
  TypographyInlineCode,
};
