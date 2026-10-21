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
import { CheckIcon, CopyIcon } from "lucide-react";
import { cn } from "cn";

export type CodeBlockProps = React.ComponentProps<"div"> & {
  code: string;
  language?: string;
  filename?: string;
  showLineNumbers?: boolean;
  copyLabel?: string;
};

function CodeBlock({
  code,
  language,
  filename,
  showLineNumbers = false,
  copyLabel = "코드 복사",
  className,
  ...props
}: CodeBlockProps) {
  const [copied, setCopied] = React.useState(false);
  const copy = async () => {
    if (!navigator.clipboard?.writeText) return;
    await navigator.clipboard.writeText(code);
    setCopied(true);
    window.setTimeout(() => setCopied(false), 1600);
  };
  const lines = code.replace(/\n$/, "").split("\n");
  return (
    <section
      data-slot="code-block"
      className={cn(
        "overflow-hidden rounded-ien-default border border-ien-border bg-ien-surface text-ien-body2",
        className,
      )}
      {...props}
    >
      <header className="flex min-h-10 items-center justify-between gap-ien-sm border-b border-ien-border px-ien-md">
        <div className="flex min-w-0 items-center gap-ien-sm">
          {filename && (
            <span className="truncate text-ien-label2 text-ien-text-primary">
              {filename}
            </span>
          )}
          {language && (
            <span className="text-ien-caption text-ien-text-secondary">
              {language}
            </span>
          )}
        </div>
        <button
          type="button"
          onClick={copy}
          aria-label={copied ? "복사 완료" : copyLabel}
          className="inline-flex min-h-8 items-center gap-ien-xxs rounded-ien-sm px-ien-xs text-ien-caption text-ien-text-secondary transition-colors hover:bg-ien-surface-variant hover:text-ien-text-primary focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand"
        >
          {copied ? (
            <CheckIcon aria-hidden="true" className="size-4" />
          ) : (
            <CopyIcon aria-hidden="true" className="size-4" />
          )}
          {copied ? "복사됨" : copyLabel}
        </button>
      </header>
      <pre
        className="overflow-x-auto p-ien-md font-ien-mono text-ien-caption leading-relaxed"
        tabIndex={0}
      >
        <code>
          {showLineNumbers
            ? lines.map((line, index) => (
                <span key={index} className="table-row">
                  <span
                    aria-hidden="true"
                    className="table-cell select-none pr-ien-md text-right text-ien-text-tertiary"
                  >
                    {index + 1}
                  </span>
                  <span className="table-cell">
                    {line}
                    {index < lines.length - 1 ? "\n" : ""}
                  </span>
                </span>
              ))
            : code}
        </code>
      </pre>
    </section>
  );
}

export { CodeBlock };
