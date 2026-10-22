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
"use client";

import * as React from "react";
import { ArrowDownIcon } from "lucide-react";
import { cn } from "@/lib/ien-utils";

export type MessageScrollerProps = React.ComponentProps<"div"> & {
  contentKey?: unknown;
  stickToBottom?: boolean;
  threshold?: number;
  showScrollButton?: boolean;
};
function MessageScroller({
  contentKey,
  stickToBottom = true,
  threshold = 48,
  showScrollButton = true,
  className,
  children,
  ...props
}: MessageScrollerProps) {
  const viewportRef = React.useRef<HTMLDivElement>(null);
  const [atBottom, setAtBottom] = React.useState(true);
  const updatePosition = React.useCallback(() => {
    const el = viewportRef.current;
    if (el)
      setAtBottom(
        el.scrollHeight - el.scrollTop - el.clientHeight <= threshold,
      );
  }, [threshold]);
  React.useEffect(() => {
    const el = viewportRef.current;
    if (el && stickToBottom && atBottom) el.scrollTop = el.scrollHeight;
  }, [children, contentKey, stickToBottom, atBottom]);
  const scrollToBottom = () =>
    viewportRef.current?.scrollTo({
      top: viewportRef.current.scrollHeight,
      behavior: "smooth",
    });
  return (
    <div
      data-slot="message-scroller"
      className={cn("relative min-h-0", className)}
      {...props}
    >
      <div
        ref={viewportRef}
        onScroll={updatePosition}
        className="h-full min-h-0 overflow-y-auto"
        role="log"
        aria-live="polite"
        aria-relevant="additions text"
      >
        {children}
      </div>
      {showScrollButton && !atBottom && (
        <button
          type="button"
          onClick={scrollToBottom}
          aria-label="최신 메시지로 이동"
          className="absolute right-ien-md bottom-ien-md grid size-10 place-items-center rounded-ien-full border border-ien-border bg-ien-surface-raised text-ien-text-primary shadow-lg focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand"
        >
          <ArrowDownIcon aria-hidden="true" className="size-4" />
        </button>
      )}
    </div>
  );
}

export { MessageScroller };
