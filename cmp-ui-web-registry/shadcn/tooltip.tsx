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
import { cn } from "@/lib/ien-utils";
import { Tooltip as TooltipPrimitive } from "radix-ui";

type TooltipTone =
  "neutral" | "brand" | "success" | "warning" | "danger" | "info";
type TooltipMotionVariant = "weak" | "strong";
type TooltipBehavior = { openOnHover: boolean; openOnFocus: boolean };
const TooltipBehaviorContext = React.createContext<TooltipBehavior>({
  openOnHover: true,
  openOnFocus: true,
});
function TooltipProvider({
  delayDuration = 300,
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Provider>) {
  return (
    <TooltipPrimitive.Provider
      data-slot="tooltip-provider"
      delayDuration={delayDuration}
      {...props}
    />
  );
}
type TooltipProps = Omit<
  React.ComponentProps<typeof TooltipPrimitive.Root>,
  "open" | "defaultOpen"
> & {
  open?: boolean;
  defaultOpen?: boolean;
  visible?: boolean;
  text?: React.ReactNode;
  tone?: TooltipTone;
  placement?: "top" | "right" | "bottom" | "left";
  offset?: number;
  width?: number | string;
  fitContentWidth?: boolean;
  messageAlign?: "left" | "center" | "right";
  motionVariant?: TooltipMotionVariant;
  anchorPositionByRatio?: number;
  openOnHover?: boolean;
  openOnFocus?: boolean;
  dismissible?: boolean;
  autoFlip?: boolean;
  strategy?: "absolute" | "fixed";
  clipToEnd?: "none" | "left" | "right";
};

function Tooltip({
  open,
  visible,
  defaultOpen,
  text,
  tone,
  placement,
  offset,
  width,
  fitContentWidth,
  messageAlign,
  motionVariant,
  anchorPositionByRatio,
  openOnHover = true,
  openOnFocus = true,
  dismissible,
  autoFlip,
  strategy,
  clipToEnd,
  children,
  ...props
}: TooltipProps) {
  const controlledOpen = visible ?? open;
  return (
    <TooltipBehaviorContext.Provider value={{ openOnHover, openOnFocus }}>
      <TooltipPrimitive.Root
        data-slot="tooltip"
        open={controlledOpen}
        defaultOpen={controlledOpen === undefined ? defaultOpen : undefined}
        {...props}
      >
        {children}
        {text !== undefined && (
          <TooltipContent
            tone={tone}
            placement={placement}
            offset={offset}
            width={width}
            fitContentWidth={fitContentWidth}
            messageAlign={messageAlign}
            motionVariant={motionVariant}
            anchorPositionByRatio={anchorPositionByRatio}
            dismissible={dismissible}
            autoFlip={autoFlip}
            strategy={strategy}
            clipToEnd={clipToEnd}
          >
            {text}
          </TooltipContent>
        )}
      </TooltipPrimitive.Root>
    </TooltipBehaviorContext.Provider>
  );
}
function TooltipTrigger({
  onPointerMove,
  onPointerLeave,
  onFocus,
  onBlur,
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Trigger>) {
  const { openOnHover, openOnFocus } = React.useContext(TooltipBehaviorContext);
  return (
    <TooltipPrimitive.Trigger
      data-slot="tooltip-trigger"
      {...props}
      onPointerMove={(event) => {
        onPointerMove?.(event);
        if (!openOnHover) event.preventDefault();
      }}
      onPointerLeave={(event) => {
        onPointerLeave?.(event);
        if (!openOnHover) event.preventDefault();
      }}
      onFocus={(event) => {
        onFocus?.(event);
        if (!openOnFocus) event.preventDefault();
      }}
      onBlur={(event) => {
        onBlur?.(event);
        if (!openOnFocus) event.preventDefault();
      }}
    />
  );
}
function TooltipContent({
  className,
  sideOffset = 4,
  tone = "neutral",
  placement,
  offset,
  width,
  fitContentWidth = false,
  messageAlign = "left",
  autoFlip = false,
  motionVariant = "weak",
  anchorPositionByRatio = 0.5,
  dismissible = false,
  strategy,
  clipToEnd = "none",
  avoidCollisions,
  align,
  style,
  children,
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Content> & {
  tone?: TooltipTone;
  placement?: "top" | "right" | "bottom" | "left";
  offset?: number;
  width?: number | string;
  fitContentWidth?: boolean;
  motionVariant?: TooltipMotionVariant;
  anchorPositionByRatio?: number;
  dismissible?: boolean;
  strategy?: "absolute" | "fixed";
  messageAlign?: "left" | "center" | "right";
  autoFlip?: boolean;
  clipToEnd?: "none" | "left" | "right";
}) {
  const styles = {
    neutral: "bg-ien-surface-raised text-ien-text-primary",
    brand: "bg-ien-brand text-ien-on-brand",
    success: "bg-ien-success text-ien-on-success",
    warning: "bg-ien-warning text-ien-on-warning",
    danger: "bg-ien-danger text-ien-on-danger",
    info: "bg-ien-info text-ien-on-info",
  }[tone];
  const boundedAnchorRatio = Math.max(0, Math.min(1, anchorPositionByRatio));
  const resolvedAlign =
    align ??
    (clipToEnd === "left" || boundedAnchorRatio < 0.34
      ? "start"
      : clipToEnd === "right" || boundedAnchorRatio > 0.66
        ? "end"
        : "center");
  return (
    <TooltipPrimitive.Portal>
      <TooltipPrimitive.Content
        data-slot="tooltip-content"
        data-tone={tone}
        data-placement={placement ?? props.side ?? "bottom"}
        data-message-align={messageAlign}
        data-motion-variant={motionVariant}
        data-strategy={strategy}
        data-dismissible={dismissible}
        side={placement ?? props.side}
        sideOffset={offset ?? sideOffset}
        align={resolvedAlign}
        avoidCollisions={avoidCollisions ?? autoFlip}
        className={cn(
          "z-50 inline-flex max-w-[280px] origin-[var(--radix-tooltip-content-transform-origin)] items-center gap-ien-xs rounded-ien-default px-ien-md py-ien-sm text-ien-caption shadow-lg data-[message-align=left]:text-left data-[message-align=center]:text-center data-[message-align=right]:text-right data-[state=delayed-open]:animate-in data-[state=delayed-open]:fade-in-0 data-[state=delayed-open]:zoom-in-95 data-[state=open]:animate-in data-[state=open]:fade-in-0 data-[state=open]:zoom-in-95 data-[state=closed]:animate-out data-[state=closed]:fade-out-0 motion-reduce:animate-none",
          motionVariant === "strong" && "data-[state=delayed-open]:zoom-in-90 data-[state=open]:zoom-in-90",
          fitContentWidth ? "w-fit" : "w-full",
          styles,
          className,
        )}
        style={{
          ...style,
          position: strategy,
          width: width ?? style?.width,
          "--ien-tooltip-anchor-ratio": boundedAnchorRatio,
        } as React.CSSProperties}
        {...props}
      >
        {children}
        <TooltipPrimitive.Arrow className="size-2.5 rotate-45 fill-current" />
      </TooltipPrimitive.Content>
    </TooltipPrimitive.Portal>
  );
}
export type { TooltipTone };
export {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
  Tooltip as IenTooltip,
  TooltipContent as IenTooltipContent,
};
