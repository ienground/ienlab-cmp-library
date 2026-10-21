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

function Empty({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="empty"
      role="status"
      className={cn(
        "flex w-full min-w-0 flex-col items-center justify-center gap-ien-md rounded-ien-default border border-dashed border-ien-border p-ien-xl text-center",
        className,
      )}
      {...props}
    />
  );
}
function EmptyHeader({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="empty-header"
      className={cn(
        "flex max-w-sm flex-col items-center gap-ien-xs",
        className,
      )}
      {...props}
    />
  );
}
const emptyMediaVariants = cva(
  "mb-ien-xs flex shrink-0 items-center justify-center [&_svg]:shrink-0",
  {
    variants: {
      variant: {
        default: "bg-transparent",
        icon: "size-10 rounded-ien-default bg-ien-surface-variant text-ien-text-primary [&_svg:not([class*='size-'])]:size-5",
      },
    },
    defaultVariants: { variant: "default" },
  },
);
function EmptyMedia({
  className,
  variant = "default",
  ...props
}: React.ComponentProps<"div"> & VariantProps<typeof emptyMediaVariants>) {
  return (
    <div
      data-slot="empty-media"
      className={cn(emptyMediaVariants({ variant }), className)}
      {...props}
    />
  );
}
function EmptyTitle({ className, ...props }: React.ComponentProps<"h3">) {
  return (
    <h3
      data-slot="empty-title"
      className={cn(
        "text-ien-body1 font-[var(--ien-font-weight-label1)] text-ien-text-primary",
        className,
      )}
      {...props}
    />
  );
}
function EmptyDescription({ className, ...props }: React.ComponentProps<"p">) {
  return (
    <p
      data-slot="empty-description"
      className={cn(
        "max-w-sm text-ien-body2 text-ien-text-secondary",
        className,
      )}
      {...props}
    />
  );
}
function EmptyContent({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="empty-content"
      className={cn(
        "flex w-full max-w-sm min-w-0 flex-col items-center gap-ien-sm",
        className,
      )}
      {...props}
    />
  );
}
type IenEmptyIconScope = {
  modifier?: string;
  size?: "small" | "medium" | "large" | "extraLarge";
  tone?: "neutral" | "brand" | "success" | "warning" | "danger" | "info";
  shape?: "rounded" | "circle";
  bordered?: boolean;
  contentDescription?: string;
  contentAlignment?: "start" | "center" | "end";
  content: (children: React.ReactNode) => React.ReactNode;
};
type IenEmptyProps = {
  icon?: React.ReactNode | ((scope: IenEmptyIconScope) => React.ReactNode);
  title: React.ReactNode;
  content?: React.ReactNode;
  buttons?: React.ReactNode;
  className?: string;
  children?: React.ReactNode;
  iconFrame?: Omit<IenEmptyIconScope, "modifier" | "content">;
};
const iconFrameToneClasses = {
  neutral: "bg-ien-surface-weak text-ien-text-primary",
  brand: "bg-ien-brand-weak text-ien-brand",
  success: "bg-ien-success-weak text-ien-success",
  warning: "bg-ien-warning-weak text-ien-warning",
  danger: "bg-ien-danger-weak text-ien-danger",
  info: "bg-ien-info-weak text-ien-info",
} as const;
function IenEmpty({
  icon,
  title,
  content,
  buttons,
  className,
  children,
  iconFrame,
}: IenEmptyProps) {
  const frame = {
    size: "large" as const,
    tone: "neutral" as const,
    shape: "rounded" as const,
    bordered: false,
    contentAlignment: "center" as const,
    ...iconFrame,
  };
  let iconScope: IenEmptyIconScope;
  iconScope = {
    ...frame,
    modifier: undefined,
    content: (children) => {
      const sizeClass = {
        small: "size-8",
        medium: "size-11",
        large: "size-14",
        extraLarge: "size-[4.5rem]",
      }[frame.size ?? "large"];
      const toneClass = iconFrameToneClasses[frame.tone ?? "neutral"];
      return (
        <span
          role={frame.contentDescription ? "img" : undefined}
          aria-label={frame.contentDescription}
          className={cn(
            "grid shrink-0 place-items-center text-ien-title3",
            sizeClass,
            frame.shape === "circle" ? "rounded-ien-full" : "rounded-ien-lg",
            frame.bordered && "border border-ien-border",
            frame.contentAlignment === "start" && "place-items-start",
            frame.contentAlignment === "end" && "place-items-end",
            toneClass,
            iconScope.modifier,
          )}
        >
          {children}
        </span>
      );
    },
  };
  const iconContent =
    typeof icon === "function"
      ? icon(iconScope)
      : icon
        ? iconScope.content(icon)
        : null;
  return (
    <div
      role="status"
      aria-live="polite"
      className={cn(
        "flex w-full min-w-0 flex-col items-center justify-center gap-ien-xs text-center",
        className,
      )}
    >
      {iconContent}
      <div className="pt-ien-xs text-ien-title2 font-bold text-ien-text-primary">
        {title}
      </div>
      {content != null && (
        <div className="text-ien-body2 text-ien-text-secondary">{content}</div>
      )}
      {children}
      {buttons != null && (
        <div className="flex w-full items-center justify-center gap-ien-xs p-ien-md">
          {buttons}
        </div>
      )}
    </div>
  );
}
export type { IenEmptyProps, IenEmptyIconScope };
export {
  Empty,
  EmptyHeader,
  EmptyTitle,
  EmptyDescription,
  EmptyContent,
  EmptyMedia,
  IenEmpty,
};
