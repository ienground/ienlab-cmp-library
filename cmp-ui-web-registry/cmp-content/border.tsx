import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type IenBorderVariant = "full" | "padding24" | { height: number };

export interface IenBorderProps extends React.ComponentPropsWithRef<"div"> {
  variant?: IenBorderVariant;
  color?: string;
  thickness?: number | string;
}

export function IenBorder({
  variant = "full",
  color,
  thickness = "var(--ien-stroke-thin)",
  className,
  style,
  ...props
}: IenBorderProps) {
  const isHeight = typeof variant === "object";
  return (
    <div
      className={cn(
        "w-full",
        isHeight ? "bg-ien-surface-weak" : "bg-ien-border",
        variant === "padding24" &&
          "mx-ien-xl w-[calc(100%-var(--ien-spacing-xl)-var(--ien-spacing-xl))]",
        className,
      )}
      style={{
        height: isHeight ? variant.height : thickness,
        backgroundColor: isHeight ? undefined : color,
        ...style,
      }}
      role={isHeight ? undefined : "separator"}
      {...props}
    />
  );
}

export { IenBorder as Border };
