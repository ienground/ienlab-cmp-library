import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type IenProgressBarSize = "light" | "normal" | "bold";

export interface IenProgressBarProps extends React.ComponentPropsWithRef<"div"> {
  progress: number;
  size?: IenProgressBarSize;
  color?: string;
  animate?: boolean;
  contentDescription?: string;
  showLabel?: boolean;
}

export function IenProgressBar({
  progress,
  size = "normal",
  color = "var(--ien-brand)",
  animate = false,
  contentDescription,
  showLabel = false,
  className,
  ...props
}: IenProgressBarProps) {
  const safeProgress = Math.max(
    0,
    Math.min(1, Number.isFinite(progress) ? progress : 0),
  );
  const height = size === "light" ? 2 : size === "bold" ? 8 : 4;
  const percent = Math.round(safeProgress * 100);
  return (
    <div className={cn("flex flex-col gap-ien-xs", className)} {...props}>
      <div
        className="h-[var(--ien-progress-height)] w-full overflow-hidden rounded-full bg-ien-brand/15"
        style={
          { "--ien-progress-height": `${height}px` } as React.CSSProperties
        }
        role="progressbar"
        aria-label={contentDescription}
        aria-valuemin={0}
        aria-valuemax={100}
        aria-valuenow={percent}
      >
        <div
          className="h-full w-full origin-left rounded-full bg-[image:linear-gradient(120deg,color-mix(in_oklab,var(--ien-progress-color),white_18%),var(--ien-progress-color),color-mix(in_oklab,var(--ien-progress-color),black_8%))] motion-reduce:transition-none!"
          style={
            {
              transform: `scaleX(${safeProgress})`,
              "--ien-progress-color": color,
              transition: animate
                ? "transform var(--ien-motion-normal) var(--ien-motion-standard-easing)"
                : "none",
            } as React.CSSProperties
          }
        />
      </div>
      {showLabel && (
        <span className="text-ien-caption text-ien-text-secondary">
          {percent}%
        </span>
      )}
    </div>
  );
}

export { IenProgressBar as ProgressBar };
