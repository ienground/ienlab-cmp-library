import * as React from "react";
import { cn } from "@/lib/ien-utils";
import { IenLoaderPrimitive } from "@/components/ui/primitives";

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
    size === "small"
      ? "var(--ien-icon-sm)"
      : size === "large"
        ? "var(--ien-icon-xl)"
        : "var(--ien-icon-lg)";
  return (
    <div
      className={cn("flex flex-col items-center gap-ien-sm", className)}
      role="status"
      aria-label={label ?? "불러오는 중"}
      {...props}
    >
      {indicator ?? (
        <IenLoaderPrimitive
          aria-hidden
          color="var(--ien-brand)"
          strokeWidth={2}
          size={iconSize}
        />
      )}
      {label != null && (
        <span className="text-ien-body2 text-ien-text-secondary">{label}</span>
      )}
    </div>
  );
}

export { IenLoader as Loader };
