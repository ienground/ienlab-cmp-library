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
import { cn } from "@/lib/ien-utils";
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

type BubbleTone = "neutral" | "brand" | "success" | "warning" | "danger" | "info";
type BubbleTail = "none" | "start" | "end";

const bubbleVariants = cva(
  "group/bubble relative flex w-fit max-w-[80%] min-w-0 flex-col gap-ien-xxs data-[align=end]:self-end after:absolute after:bottom-ien-xxs after:size-2 after:rotate-45 after:rounded-[1px] after:content-[''] data-[tail=none]:after:hidden data-[tail=start]:after:start-1 data-[tail=start]:after:end-auto data-[tail=end]:after:end-1 data-[tail=end]:after:start-auto data-[background=grey]:after:bg-ien-surface-variant data-[background=brand]:after:bg-ien-brand data-[tone=neutral]:after:bg-ien-surface-weak data-[tone=brand]:after:bg-ien-brand-weak data-[tone=success]:after:bg-ien-success-weak data-[tone=warning]:after:bg-ien-warning-weak data-[tone=danger]:after:bg-ien-danger-weak data-[tone=info]:after:bg-ien-info-weak",
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
        semantic: "",
      },
      background: { brand: "", grey: "", semantic: "" },
      tone: {
        neutral: "",
        brand: "",
        success: "",
        warning: "",
        danger: "",
        info: "",
      },
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
  tone?: BubbleTone;
  tail?: BubbleTail;
};

function Bubble({
  variant,
  align = "start",
  background,
  withTail = false,
  tone,
  tail = "none",
  className,
  ...props
}: BubbleProps) {
  const chosenVariant = tone
    ? "semantic"
    : background === "grey"
      ? "secondary"
      : variant;
  const chosenBackground = tone
    ? "semantic"
    : background ??
      (chosenVariant === "secondary" || chosenVariant === "muted"
        ? "grey"
        : "brand");
  return (
    <div
      data-slot="bubble"
      data-variant={chosenVariant ?? "default"}
      data-background={chosenBackground}
      data-tone={tone}
      data-align={align}
      data-with-tail={withTail}
      data-tail={tail}
      className={cn(
        bubbleVariants({
          variant: chosenVariant,
          background: chosenBackground,
          tone,
        }),
        className,
      )}
      {...props}
    />
  );
}

type BubbleContentProps = React.ComponentProps<"div"> & {
  asChild?: boolean;
  contentPadding?: React.CSSProperties["padding"];
};

function BubbleContent({
  asChild = false,
  contentPadding,
  style,
  className,
  ...props
}: BubbleContentProps) {
  const Comp = asChild ? Slot.Root : "div";
  return (
    <Comp
      data-slot="bubble-content"
      className={cn(
        "w-fit max-w-full min-w-0 overflow-hidden rounded-ien-lg border border-transparent px-ien-md py-[calc(var(--ien-spacing-sm)-var(--ien-spacing-xxxs))] text-ien-body2 leading-relaxed wrap-break-word",
        "group-data-[background=grey]/bubble:rounded-ss-ien-xs group-data-[background=brand]/bubble:rounded-se-ien-xs",
        "group-data-[background=grey]/bubble:group-data-[with-tail=false]/bubble:rounded-es-ien-xs group-data-[background=brand]/bubble:group-data-[with-tail=false]/bubble:rounded-ee-ien-xs",
        "group-data-[tone=neutral]/bubble:rounded-ien-lg group-data-[tone=neutral]/bubble:bg-ien-surface-weak group-data-[tone=neutral]/bubble:px-[calc(var(--ien-spacing-sm)+var(--ien-spacing-xxxs))] group-data-[tone=neutral]/bubble:text-ien-text-primary",
        "group-data-[tone=brand]/bubble:rounded-ien-lg group-data-[tone=brand]/bubble:bg-ien-brand-weak group-data-[tone=brand]/bubble:px-[calc(var(--ien-spacing-sm)+var(--ien-spacing-xxxs))] group-data-[tone=brand]/bubble:text-ien-brand",
        "group-data-[tone=success]/bubble:rounded-ien-lg group-data-[tone=success]/bubble:bg-ien-success-weak group-data-[tone=success]/bubble:px-[calc(var(--ien-spacing-sm)+var(--ien-spacing-xxxs))] group-data-[tone=success]/bubble:text-ien-success",
        "group-data-[tone=warning]/bubble:rounded-ien-lg group-data-[tone=warning]/bubble:bg-ien-warning-weak group-data-[tone=warning]/bubble:px-[calc(var(--ien-spacing-sm)+var(--ien-spacing-xxxs))] group-data-[tone=warning]/bubble:text-ien-warning",
        "group-data-[tone=danger]/bubble:rounded-ien-lg group-data-[tone=danger]/bubble:bg-ien-danger-weak group-data-[tone=danger]/bubble:px-[calc(var(--ien-spacing-sm)+var(--ien-spacing-xxxs))] group-data-[tone=danger]/bubble:text-ien-danger",
        "group-data-[tone=info]/bubble:rounded-ien-lg group-data-[tone=info]/bubble:bg-ien-info-weak group-data-[tone=info]/bubble:px-[calc(var(--ien-spacing-sm)+var(--ien-spacing-xxxs))] group-data-[tone=info]/bubble:text-ien-info",
        "group-data-[tail=start]/bubble:rounded-ien-lg group-data-[tail=start]/bubble:py-[calc(var(--ien-spacing-sm)-var(--ien-spacing-xxxs))] group-data-[tail=end]/bubble:rounded-ien-lg group-data-[tail=end]/bubble:py-[calc(var(--ien-spacing-sm)-var(--ien-spacing-xxxs))]",
        className,
      )}
      style={contentPadding === undefined ? style : { ...style, padding: contentPadding }}
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
        align === "start" ? "start-3" : "end-3",
        className,
      )}
      {...props}
    />
  );
}

type IenBubbleProps = Omit<BubbleProps, "variant" | "background" | "tone" | "tail"> & {
  background?: "Brand" | "Grey" | "brand" | "grey";
  tone?: "Neutral" | "Brand" | "Success" | "Warning" | "Danger" | "Info" | BubbleTone;
  tail?: "None" | "Start" | "End" | BubbleTail;
  text?: string;
  contentPadding?: React.CSSProperties["padding"];
};

function IenBubble({
  background,
  tone,
  align,
  withTail = true,
  tail = "none",
  text,
  children,
  contentPadding,
  ...props
}: IenBubbleProps) {
  const normalizedBackground = background?.toLowerCase() as "brand" | "grey" | undefined;
  const normalizedTone = tone
    ? (tone.toLowerCase() as BubbleTone)
    : normalizedBackground
      ? undefined
      : "neutral";
  const normalizedTail = tail.toLowerCase() as BubbleTail;
  const existingContent =
    React.Children.count(children) === 1 &&
    React.isValidElement<BubbleContentProps>(children) &&
    children.type === BubbleContent
      ? children
      : undefined;
  return (
    <Bubble
      background={normalizedBackground}
      tone={normalizedTone}
      align={
        align ??
          (normalizedBackground === "brand" || normalizedTail === "end"
            ? "end"
            : "start")
      }
      withTail={withTail}
      tail={normalizedTail}
      {...props}
    >
      {text !== undefined ? (
        <BubbleContent contentPadding={contentPadding}>{text}</BubbleContent>
      ) : existingContent ? (
        React.cloneElement(existingContent, {
          contentPadding: contentPadding ?? existingContent.props.contentPadding,
        })
      ) : (
        <BubbleContent contentPadding={contentPadding}>{children}</BubbleContent>
      )}
    </Bubble>
  );
}

export type { BubbleProps, BubbleTail, BubbleTone, IenBubbleProps };
export { BubbleGroup, Bubble, BubbleContent, BubbleReactions, IenBubble };
