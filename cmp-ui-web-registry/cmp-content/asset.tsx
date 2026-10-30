import * as React from "react";
import { cn } from "cn";

export type IenSemanticTone =
  "neutral" | "brand" | "success" | "warning" | "danger" | "info";

export type IenAssetFrameSize = "small" | "medium" | "large" | "extraLarge";
export type IenAssetFrameShape = "rounded" | "circle";

export interface IenAssetFrameProps extends React.ComponentPropsWithRef<"div"> {
  size?: IenAssetFrameSize;
  tone?: IenSemanticTone;
  shape?: IenAssetFrameShape;
  bordered?: boolean;
  contentDescription?: string;
  contentAlignment?: "start" | "center" | "end";
}

const sizeClass: Record<IenAssetFrameSize, string> = {
  small: "size-8",
  medium: "size-11",
  large: "size-14",
  extraLarge: "size-[4.5rem]",
};

export function IenAssetFrame({
  size = "medium",
  tone = "neutral",
  shape = "rounded",
  bordered = false,
  contentDescription,
  contentAlignment = "center",
  className,
  ...props
}: IenAssetFrameProps) {
  const contentColor =
    tone === "neutral" ? "var(--ien-text-primary)" : `var(--ien-${tone})`;
  const containerColor =
    tone === "neutral" ? "var(--ien-surface-weak)" : `var(--ien-${tone}-weak)`;
  return (
    <div
      className={cn(
        "grid shrink-0 place-items-center text-ien-title3",
        sizeClass[size],
        shape === "circle" ? "rounded-full" : "rounded-ien-lg",
        bordered && "border border-ien-border",
        contentAlignment === "start" && "place-items-start",
        contentAlignment === "end" && "place-items-end",
        className,
      )}
      style={{ color: contentColor, backgroundColor: containerColor }}
      role={contentDescription ? "img" : undefined}
      aria-label={contentDescription}
      {...props}
    />
  );
}

export { IenAssetFrame as AssetFrame };
