import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type IenSemanticTone =
  "neutral" | "brand" | "success" | "warning" | "danger" | "info";

export interface IenHighlightTextProps extends React.ComponentPropsWithRef<"span"> {
  text: string;
  highlights: readonly string[];
  tone?: IenSemanticTone;
  ignoreCase?: boolean;
  bold?: boolean;
}

export function IenHighlightText({
  text,
  highlights,
  tone = "brand",
  ignoreCase = true,
  bold = false,
  className,
  ...props
}: IenHighlightTextProps) {
  const queries = highlights.filter((query) => query.length > 0);
  const flags = ignoreCase ? "giu" : "gu";
  const escaped = queries.map((query) =>
    query.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"),
  );
  if (escaped.length === 0)
    return (
      <span
        className={cn("text-ien-body2 text-ien-text-primary", className)}
        {...props}
      >
        {text}
      </span>
    );
  const expression = new RegExp(`(${escaped.join("|")})`, flags);
  const parts = text.split(expression);
  return (
    <span
      className={cn("text-ien-body2 text-ien-text-primary", className)}
      {...props}
    >
      {parts.map((part, index) => {
        const matched = queries.some((query) =>
          ignoreCase
            ? part.toLocaleLowerCase() === query.toLocaleLowerCase()
            : part === query,
        );
        const color =
          tone === "neutral" ? "var(--ien-text-primary)" : `var(--ien-${tone})`;
        return matched ? (
          <mark
            key={`${index}-${part}`}
            className={cn("bg-transparent", bold && "font-bold")}
            style={{ color }}
          >
            {part}
          </mark>
        ) : (
          <React.Fragment key={`${index}-${part}`}>{part}</React.Fragment>
        );
      })}
    </span>
  );
}

export { IenHighlightText as HighlightText };
