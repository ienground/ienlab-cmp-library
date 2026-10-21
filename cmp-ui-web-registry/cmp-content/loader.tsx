import * as React from "react";
import { cn } from "cn";

export type IenLoaderSize = "small" | "medium" | "large";

export interface IenLoaderProps extends React.ComponentPropsWithRef<"div"> {
  size?: IenLoaderSize;
  label?: string;
  indicator?: React.ReactNode;
}

export function IenLoader({
  size = "medium",
  label,
  indicator,
  className,
  ...props
}: IenLoaderProps) {
  const iconSize =
    size === "small" ? "size-4" : size === "large" ? "size-8" : "size-6";
  return (
    <div
      className={cn("flex flex-col items-center gap-ien-sm", className)}
      role="status"
      aria-label={label ?? "불러오는 중"}
      {...props}
    >
      {indicator ?? (
        <span
          aria-hidden
          className={cn(
            "inline-block animate-spin rounded-full border-2 border-ien-brand border-r-transparent",
            iconSize,
          )}
        />
      )}
      {label != null && (
        <span className="text-ien-body2 text-ien-text-secondary">{label}</span>
      )}
    </div>
  );
}

export { IenLoader as Loader };
