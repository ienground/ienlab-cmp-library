import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type IenSemanticTone =
  "neutral" | "brand" | "success" | "warning" | "danger" | "info";

export type IenAssetFrameSize = "small" | "medium" | "large" | "extraLarge";
export type IenAssetFrameShape = "rounded" | "circle";
export type IenAssetFrameAlignment =
  | "topStart"
  | "topCenter"
  | "topEnd"
  | "centerStart"
  | "center"
  | "centerEnd"
  | "bottomStart"
  | "bottomCenter"
  | "bottomEnd"
  | "start"
  | "end";

export interface IenAssetFrameProps extends React.ComponentPropsWithRef<"div"> {
  size?: IenAssetFrameSize;
  tone?: IenSemanticTone;
  shape?: IenAssetFrameShape;
  bordered?: boolean;
  contentDescription?: string;
  contentAlignment?: IenAssetFrameAlignment;
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
  style,
  ...props
}: IenAssetFrameProps) {
  const contentColor =
    tone === "neutral" ? "var(--ien-text-primary)" : `var(--ien-${tone})`;
  const containerColor =
    tone === "neutral" ? "var(--ien-surface-weak)" : `var(--ien-${tone}-weak)`;
  const alignment: Record<IenAssetFrameAlignment, string> = {
    topStart: "start start",
    topCenter: "start center",
    topEnd: "start end",
    centerStart: "center start",
    center: "center center",
    centerEnd: "center end",
    bottomStart: "end start",
    bottomCenter: "end center",
    bottomEnd: "end end",
    start: "start start",
    end: "end end",
  };
  return (
    <div
      className={cn(
        "grid shrink-0 place-items-center text-ien-title3",
        sizeClass[size],
        shape === "circle" ? "rounded-full" : "rounded-ien-lg",
        bordered && "border border-ien-border",
        className,
      )}
      style={{
        placeItems: alignment[contentAlignment],
        color: contentColor,
        backgroundColor: containerColor,
        ...style,
      }}
      role={contentDescription ? "img" : undefined}
      aria-label={contentDescription}
      {...props}
    />
  );
}

export { IenAssetFrame as AssetFrame };
