import * as React from "react";
import { cn } from "@/lib/ien-utils";
import { IenHighlightText } from "./highlight";
import type { IenSemanticTone } from "@/components/ui/highlight";

export interface IenParagraphProps extends React.ComponentPropsWithRef<"div"> {
  title?: string;
  body: string;
  emphasis?: readonly string[];
  footer?: string;
  tone?: IenSemanticTone;
  bold?: boolean;
}

export function IenParagraph({
  title,
  body,
  emphasis = [],
  footer,
  tone = "brand",
  bold = false,
  className,
  ...props
}: IenParagraphProps) {
  return (
    <div
      className={cn("flex w-full flex-col gap-ien-xs", className)}
      {...props}
    >
      {title != null && (
        <h3 className="text-ien-title3 font-semibold text-ien-text-primary">
          {title}
        </h3>
      )}
      {emphasis.some(Boolean) ? (
        <IenHighlightText
          text={body}
          highlights={emphasis}
          tone={tone}
          bold={bold}
        />
      ) : (
        <p className="text-ien-body2 text-ien-text-secondary">{body}</p>
      )}
      {footer != null && (
        <p className="text-ien-caption text-ien-text-tertiary">{footer}</p>
      )}
    </div>
  );
}

export { IenParagraph as Paragraph };
