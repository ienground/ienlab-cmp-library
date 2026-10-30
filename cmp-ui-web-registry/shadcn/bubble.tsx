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
import { cva } from "class-variance-authority";
import { cn } from "cn";
import { Slot } from "radix-ui";

function BubbleGroup({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="bubble-group"
      className={cn("flex min-w-0 flex-col gap-ien-sm", className)}
      {...props}
    />
  );
}

const bubbleVariants = cva(
  "group/bubble relative flex w-fit max-w-[80%] min-w-0 flex-col gap-ien-xxs data-[align=end]:self-end",
  {
    variants: {
      variant: {
        default:
          "*:data-[slot=bubble-content]:bg-ien-brand *:data-[slot=bubble-content]:bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-brand),var(--ien-on-brand)_18%),var(--ien-brand),color-mix(in_oklab,var(--ien-brand),var(--ien-on-brand)_36%))] *:data-[slot=bubble-content]:text-ien-on-brand",
        secondary:
          "*:data-[slot=bubble-content]:bg-ien-surface-variant *:data-[slot=bubble-content]:text-ien-text-primary",
        muted:
          "*:data-[slot=bubble-content]:bg-ien-surface-variant *:data-[slot=bubble-content]:text-ien-text-primary",
        tinted:
          "*:data-[slot=bubble-content]:bg-ien-brand-weak *:data-[slot=bubble-content]:text-ien-on-brand-weak",
        outline:
          "*:data-[slot=bubble-content]:border-ien-border *:data-[slot=bubble-content]:bg-ien-surface *:data-[slot=bubble-content]:text-ien-text-primary",
        ghost:
          "*:data-[slot=bubble-content]:bg-transparent *:data-[slot=bubble-content]:p-0",
        destructive:
          "*:data-[slot=bubble-content]:bg-ien-danger-weak *:data-[slot=bubble-content]:text-ien-on-danger-weak",
      },
      background: { brand: "", grey: "" },
    },
    defaultVariants: { variant: "default", background: "brand" },
  },
);

type BubbleProps = React.ComponentProps<"div"> & {
  variant?:
    | "default"
    | "secondary"
    | "muted"
    | "tinted"
    | "outline"
    | "ghost"
    | "destructive";
  align?: "start" | "end";
  background?: "brand" | "grey";
  withTail?: boolean;
};

function Bubble({
  variant,
  align = "start",
  background,
  withTail = false,
  className,
  ...props
}: BubbleProps) {
  const chosenVariant = background === "grey" ? "secondary" : variant;
  const chosenBackground =
    background ??
    (chosenVariant === "secondary" || chosenVariant === "muted"
      ? "grey"
      : "brand");
  return (
    <div
      data-slot="bubble"
      data-variant={chosenVariant ?? "default"}
      data-background={chosenBackground}
      data-align={align}
      data-with-tail={withTail}
      className={cn(
        bubbleVariants({
          variant: chosenVariant,
          background: chosenBackground,
        }),
        className,
      )}
      {...props}
    />
  );
}

function BubbleContent({
  asChild = false,
  className,
  ...props
}: React.ComponentProps<"div"> & { asChild?: boolean }) {
  const Comp = asChild ? Slot.Root : "div";
  return (
    <Comp
      data-slot="bubble-content"
      className={cn(
        "w-fit max-w-full min-w-0 overflow-hidden rounded-ien-lg border border-transparent px-ien-md py-[calc(var(--ien-spacing-sm)-var(--ien-spacing-xxxs))] text-ien-body2 leading-relaxed wrap-break-word",
        "group-data-[background=grey]/bubble:rounded-tl-ien-xs group-data-[background=brand]/bubble:rounded-tr-ien-xs",
        "group-data-[background=grey]/bubble:group-data-[with-tail=false]/bubble:rounded-bl-ien-xs group-data-[background=brand]/bubble:group-data-[with-tail=false]/bubble:rounded-br-ien-xs",
        className,
      )}
      {...props}
    />
  );
}

function BubbleReactions({
  side = "bottom",
  align = "end",
  className,
  ...props
}: React.ComponentProps<"div"> & {
  align?: "start" | "end";
  side?: "top" | "bottom";
}) {
  return (
    <div
      data-slot="bubble-reactions"
      data-align={align}
      data-side={side}
      className={cn(
        "absolute z-10 flex w-fit shrink-0 items-center justify-center gap-ien-xxs rounded-ien-full bg-ien-surface-variant px-ien-xs py-ien-xxxs text-ien-body2 shadow-sm",
        side === "top"
          ? "top-ien-none -translate-y-3/4"
          : "bottom-ien-none translate-y-3/4",
        align === "start" ? "left-3" : "right-3",
        className,
      )}
      {...props}
    />
  );
}

type IenBubbleProps = Omit<BubbleProps, "variant" | "background"> & {
  background?: "Brand" | "Grey" | "brand" | "grey";
};

function IenBubble({
  background = "Brand",
  align,
  withTail = true,
  ...props
}: IenBubbleProps) {
  const normalizedBackground = background.toLowerCase() as "brand" | "grey";
  return (
    <Bubble
      background={normalizedBackground}
      align={align ?? (normalizedBackground === "brand" ? "end" : "start")}
      withTail={withTail}
      {...props}
    />
  );
}

export type { BubbleProps, IenBubbleProps };
export { BubbleGroup, Bubble, BubbleContent, BubbleReactions, IenBubble };
