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
import { cn } from "@/lib/ien-utils";

const languageKeywords: Record<string, ReadonlySet<string>> = {
  javascript: new Set([
    "async", "await", "break", "case", "catch", "class", "const", "continue",
    "default", "delete", "do", "else", "export", "extends", "false", "finally",
    "for", "from", "function", "if", "import", "in", "instanceof", "let", "new",
    "null", "of", "return", "static", "super", "switch", "this", "throw", "true",
    "try", "typeof", "undefined", "var", "void", "while", "with", "yield",
  ]),
  typescript: new Set([
    "any", "as", "asserts", "async", "await", "boolean", "break", "case", "catch",
    "class", "const", "continue", "declare", "default", "delete", "do", "else",
    "enum", "export", "extends", "false", "finally", "for", "from", "function",
    "if", "implements", "import", "in", "infer", "instanceof", "interface", "is",
    "keyof", "let", "namespace", "never", "new", "null", "number", "object", "of",
    "private", "protected", "public", "readonly", "return", "static", "string",
    "super", "switch", "this", "throw", "true", "try", "type", "typeof", "undefined",
    "unique", "unknown", "var", "void", "while", "with", "yield",
  ]),
  kotlin: new Set([
    "as", "break", "by", "catch", "class", "const", "continue", "data", "do", "else",
    "enum", "false", "field", "file", "final", "finally", "for", "fun", "get", "if",
    "import", "in", "init", "inline", "interface", "internal", "is", "lateinit", "object",
    "open", "operator", "out", "override", "package", "private", "protected", "public",
    "return", "sealed", "set", "suspend", "this", "throw", "true", "try", "typealias",
    "val", "var", "when", "where", "while",
  ]),
};

type SyntaxToken = {
  value: string;
  kind?: "annotation" | "comment" | "function" | "keyword" | "number" | "property" | "string" | "type";
};

const syntaxTokenClasses: Record<NonNullable<SyntaxToken["kind"]>, string> = {
  annotation: "text-[var(--ien-syntax-annotation)]",
  comment: "text-[var(--ien-syntax-comment)]",
  function: "text-[var(--ien-syntax-function)]",
  keyword: "text-[var(--ien-syntax-keyword)]",
  number: "text-[var(--ien-syntax-number)]",
  property: "text-[var(--ien-syntax-property)]",
  string: "text-[var(--ien-syntax-string)]",
  type: "text-[var(--ien-syntax-type)]",
};

function tokenize(code: string, language?: string): SyntaxToken[] {
  const normalizedLanguage = language?.toLowerCase().replace(/[^a-z]/g, "");
  const keywordSet = languageKeywords[
    normalizedLanguage === "js" || normalizedLanguage === "jsx"
      ? "javascript"
      : normalizedLanguage === "ts" || normalizedLanguage === "tsx"
        ? "typescript"
        : normalizedLanguage ?? ""
  ];
  const expression = /(\/\/[^\n]*|\/\*[\s\S]*?\*\/|<!--[\s\S]*?-->|"""[\s\S]*?"""|`(?:\\.|[^`\\])*`|"(?:\\.|[^"\\])*"|'(?:\\.|[^'\\])*'|@[A-Za-z_$][\w$]*|0[xX][\da-fA-F]+|\b\d+(?:\.\d+)?(?:[eE][+-]?\d+)?[fFdDlL]?\b|[A-Za-z_$][\w$]*|\s+|.)/g;
  const tokens: SyntaxToken[] = [];
  let match: RegExpExecArray | null;

  while ((match = expression.exec(code)) !== null) {
    const value = match[0];
    if (/^\s+$/.test(value)) {
      tokens.push({ value });
      continue;
    }

    let kind: SyntaxToken["kind"];
    if (value.startsWith("//") || value.startsWith("/*") || value.startsWith("<!--")) {
      kind = "comment";
    } else if (value.startsWith("\"") || value.startsWith("'") || value.startsWith("`")) {
      kind = "string";
    } else if (value.startsWith("@")) {
      kind = "annotation";
    } else if (/^(?:0[xX][\da-fA-F]+|\d)/.test(value)) {
      kind = "number";
    } else if (keywordSet?.has(value)) {
      kind = "keyword";
    } else if (/^[A-Z]/.test(value)) {
      kind = "type";
    } else if (/^\s*\(/.test(code.slice(expression.lastIndex))) {
      kind = "function";
    } else if (/^\s*[:=]/.test(code.slice(expression.lastIndex))) {
      kind = "property";
    }

    tokens.push({ value, kind });
  }

  return tokens;
}

function HighlightedCode({ code, language }: { code: string; language?: string }) {
  return (
    <>
      {tokenize(code, language).map(({ value, kind }, index) =>
        kind ? (
          <span
            className={syntaxTokenClasses[kind]}
            key={`${index}-${kind}`}
          >
            {value}
          </span>
        ) : (
          <React.Fragment key={index}>{value}</React.Fragment>
        ),
      )}
    </>
  );
}

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
  const lines = code.split("\n");
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
        aria-label={filename ? `${filename} 코드` : `${language ?? "소스"} 코드`}
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
                    <HighlightedCode code={line} language={language} />
                  </span>
                </span>
              ))
            : <HighlightedCode code={code} language={language} />}
        </code>
      </pre>
    </section>
  );
}

export { CodeBlock, HighlightedCode };
