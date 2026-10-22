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

const badgeVariants = cva(
  "group/badge inline-flex w-fit shrink-0 items-center justify-center gap-ien-xxs rounded-ien-full border px-ien-xs py-ien-xxxs text-ien-caption font-[var(--ien-font-weight-label1)] whitespace-nowrap transition-colors [&>svg]:size-3",
  {
    variants: {
      variant: {
        fill: "border-transparent",
        weak: "border-transparent",
        line: "bg-transparent",
        default: "border-transparent",
        secondary: "border-transparent",
        destructive: "border-transparent",
        outline: "bg-transparent",
        ghost: "border-transparent",
        link: "border-transparent bg-transparent underline-offset-4 hover:underline",
      },
      tone: {
        neutral:
          "data-[variant=fill]:bg-ien-text-primary data-[variant=fill]:text-ien-surface-raised data-[variant=weak]:bg-ien-surface-weak data-[variant=weak]:text-ien-text-primary data-[variant=line]:border-ien-border data-[variant=line]:text-ien-text-primary",
        brand:
          "data-[variant=fill]:bg-ien-brand data-[variant=fill]:text-ien-on-brand data-[variant=weak]:bg-ien-brand-weak data-[variant=weak]:text-ien-on-brand-weak data-[variant=line]:border-ien-brand data-[variant=line]:text-ien-brand",
        success:
          "data-[variant=fill]:bg-ien-success data-[variant=fill]:text-ien-on-success data-[variant=weak]:bg-ien-success-weak data-[variant=weak]:text-ien-on-success-weak data-[variant=line]:border-ien-success data-[variant=line]:text-ien-success",
        warning:
          "data-[variant=fill]:bg-ien-warning data-[variant=fill]:text-ien-on-warning data-[variant=weak]:bg-ien-warning-weak data-[variant=weak]:text-ien-on-warning-weak data-[variant=line]:border-ien-warning data-[variant=line]:text-ien-warning",
        danger:
          "data-[variant=fill]:bg-ien-danger data-[variant=fill]:text-ien-on-danger data-[variant=weak]:bg-ien-danger-weak data-[variant=weak]:text-ien-on-danger-weak data-[variant=line]:border-ien-danger data-[variant=line]:text-ien-danger",
        info: "data-[variant=fill]:bg-ien-info data-[variant=fill]:text-ien-on-info data-[variant=weak]:bg-ien-info-weak data-[variant=weak]:text-ien-on-info-weak data-[variant=line]:border-ien-info data-[variant=line]:text-ien-info",
      },
      size: {
        small: "min-h-5 text-ien-caption",
        medium: "min-h-6 text-ien-label2",
        large: "min-h-7 px-ien-sm text-ien-label1",
      },
    },
    defaultVariants: { variant: "fill", tone: "neutral", size: "medium" },
  },
);
type BadgeProps = React.ComponentProps<"span"> &
  VariantProps<typeof badgeVariants> & { asChild?: boolean };
function Badge({
  className,
  variant,
  tone = "neutral",
  size = "medium",
  asChild = false,
  ...props
}: BadgeProps) {
  const selectedVariant =
    variant === undefined || variant === "default"
      ? "fill"
      : variant === "secondary"
        ? "weak"
        : variant === "destructive"
          ? "fill"
          : variant === "outline"
            ? "line"
            : variant === "ghost" || variant === "link"
              ? "line"
              : variant;
  const selectedTone = variant === "destructive" ? "danger" : tone;
  const Comp = asChild ? Slot.Root : "span";
  return (
    <Comp
      data-slot="badge"
      data-variant={selectedVariant}
      data-tone={selectedTone}
      data-size={size}
      className={cn(
        badgeVariants({ variant: selectedVariant, tone: selectedTone, size }),
        className,
      )}
      {...props}
    />
  );
}
type IenBadgeProps = Omit<BadgeProps, "variant" | "size"> & {
  variant?: "Fill" | "Weak" | "Line" | "fill" | "weak" | "line";
  size?: "Small" | "Medium" | "Large" | "small" | "medium" | "large";
  text?: React.ReactNode;
  leadingIcon?: React.ReactNode;
};
function IenBadge({
  text,
  leadingIcon,
  variant = "Weak",
  tone = "brand",
  size = "Medium",
  style,
  ...props
}: IenBadgeProps) {
  const normalizedVariant = variant.toLowerCase() as "fill" | "weak" | "line";
  const normalizedSize = (
    { small: "small", medium: "medium", large: "large" } as const
  )[size.toLowerCase() as "small" | "medium" | "large"];
  const container =
    tone === "neutral"
      ? normalizedVariant === "fill"
        ? "var(--ien-text-primary)"
        : "var(--ien-surface-weak)"
      : `var(--ien-${tone}${normalizedVariant === "weak" ? "-weak" : ""})`;
  const content =
    tone === "neutral"
      ? normalizedVariant === "fill"
        ? "var(--ien-surface-raised)"
        : "var(--ien-text-primary)"
      : normalizedVariant === "fill"
        ? `var(--ien-on-${tone})`
        : `var(--ien-${tone})`;
  const backgroundImage =
    normalizedVariant === "fill"
      ? `linear-gradient(135deg, color-mix(in oklab, ${container}, ${content} 18%), ${container}, color-mix(in oklab, ${container}, ${content} 36%))`
      : normalizedVariant === "weak"
        ? `linear-gradient(135deg, color-mix(in oklab, ${container}, ${content} 6%), ${container}, ${container})`
        : undefined;
  return (
    <Badge
      variant={normalizedVariant}
      tone={tone}
      size={normalizedSize}
      style={{
        ...style,
        backgroundImage: style?.backgroundImage ?? backgroundImage,
      }}
      {...props}
    >
      {leadingIcon}
      {text ?? props.children}
    </Badge>
  );
}
export type { BadgeProps, IenBadgeProps };
export { Badge, IenBadge, badgeVariants };
