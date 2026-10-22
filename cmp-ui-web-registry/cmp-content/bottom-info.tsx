import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type IenBottomGradient =
  "none" | "default" | { fromColor: string; toColor?: string };

export interface IenBottomInfoProps extends React.ComponentPropsWithRef<"div"> {
  bottomGradient?: IenBottomGradient;
  backgroundColor?: string;
  contentPadding?: string;
}

export function IenBottomInfo({
  bottomGradient = "default",
  backgroundColor = "var(--ien-surface-weak)",
  contentPadding = "var(--ien-spacing-xl) var(--ien-spacing-xl) var(--ien-spacing-md)",
  className,
  children,
  style,
  ...props
}: IenBottomInfoProps) {
  const gradient =
    bottomGradient === "none"
      ? null
      : bottomGradient === "default"
        ? { fromColor: backgroundColor }
        : bottomGradient;
  return (
    <div className={cn("w-full", className)} {...props}>
      <div
        className="text-ien-text-primary"
        style={{ backgroundColor, padding: contentPadding, ...style }}
      >
        {children}
      </div>
      {gradient && (
        <div
          aria-hidden
          className="h-12 w-full"
          style={{
            backgroundImage: `linear-gradient(to bottom, ${gradient.fromColor}, ${gradient.toColor ?? "transparent"})`,
          }}
        />
      )}
    </div>
  );
}

export { IenBottomInfo as BottomInfo };
