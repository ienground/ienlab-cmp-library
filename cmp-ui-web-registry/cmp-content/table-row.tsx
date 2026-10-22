import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type IenTableRowAlign = "left" | "spaceBetween";

export interface IenTableRowScope {
  align: IenTableRowAlign;
}

export interface IenTableRowProps extends React.ComponentPropsWithRef<"div"> {
  left?: React.ReactNode;
  right?: React.ReactNode;
  label?: string;
  value?: string;
  description?: string;
  leading?: React.ReactNode;
  trailing?: React.ReactNode;
  align?: IenTableRowAlign;
  leftRatio?: number;
  renderLeft?: (scope: IenTableRowScope) => React.ReactNode;
  renderRight?: (scope: IenTableRowScope) => React.ReactNode;
}

export function IenTableRow({
  left,
  right,
  label,
  value,
  description,
  leading,
  trailing,
  align = "spaceBetween",
  leftRatio,
  renderLeft,
  renderRight,
  className,
  ...props
}: IenTableRowProps) {
  const scope = { align };
  const leftText = left ?? label ?? "";
  const rightText = right ?? value ?? "";
  const legacy =
    label != null ||
    value != null ||
    description != null ||
    leading != null ||
    trailing != null;
  const ratio =
    leftRatio == null ? undefined : Math.max(1, Math.min(99, leftRatio));
  return (
    <div
      className={cn(
        "flex w-full items-start px-ien-md py-ien-sm text-ien-body2",
        align === "left" && "gap-ien-md",
        className,
      )}
      {...props}
    >
      <div
        className={cn(
          "min-w-0",
          ratio == null && align === "spaceBetween" ? "flex-1" : "",
        )}
        style={ratio == null ? undefined : { flex: ratio }}
      >
        {renderLeft ? (
          renderLeft(scope)
        ) : legacy ? (
          <div className="flex items-start gap-ien-sm">
            {leading}
            <div className="min-w-0">
              <div className="text-ien-text-secondary">{leftText}</div>
              {description != null && (
                <div className="text-ien-caption text-ien-text-tertiary">
                  {description}
                </div>
              )}
            </div>
          </div>
        ) : (
          <span className="line-clamp-2 text-ien-text-primary">{leftText}</span>
        )}
      </div>
      <div
        className={cn(
          "min-w-0",
          ratio == null && align === "spaceBetween" ? "flex-1 text-right" : "",
        )}
        style={ratio == null ? undefined : { flex: 100 - ratio }}
      >
        {renderRight ? (
          renderRight(scope)
        ) : legacy ? (
          <div
            className={cn(
              "flex items-center gap-ien-xs",
              align === "spaceBetween" && "justify-end",
            )}
          >
            <span className="line-clamp-2 text-ien-text-primary">
              {rightText}
            </span>
            {trailing}
          </div>
        ) : (
          <span className="line-clamp-2 text-ien-text-secondary">
            {rightText}
          </span>
        )}
      </div>
    </div>
  );
}

export { IenTableRow as TableRow };
