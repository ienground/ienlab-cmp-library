import * as React from "react";
import { cn } from "cn";

export interface IenCircularProgressIndicatorProps extends React.ComponentPropsWithRef<"svg"> {
  progress?: number;
  color?: string;
  trackColor?: string;
  strokeWidth?: number;
  gapSize?: number;
  label?: string;
  wavy?: boolean;
}

export function IenCircularProgressIndicator({
  progress,
  color = "var(--ien-brand)",
  trackColor = "var(--ien-brand-weak)",
  strokeWidth = 4,
  gapSize = 4,
  label = "진행 중",
  wavy = false,
  className,
  ...props
}: IenCircularProgressIndicatorProps) {
  const determinate = progress !== undefined;
  const normalized = Math.max(
    0,
    Math.min(1, Number.isFinite(progress) ? (progress ?? 0) : 0),
  );
  const radius = 20 - strokeWidth / 2;
  const circumference = 2 * Math.PI * radius;
  return (
    <svg
      viewBox="0 0 40 40"
      className={cn("size-10", className)}
      role="progressbar"
      aria-label={label}
      aria-valuemin={determinate ? 0 : undefined}
      aria-valuemax={determinate ? 100 : undefined}
      aria-valuenow={determinate ? Math.round(normalized * 100) : undefined}
      {...props}
    >
      <circle
        cx="20"
        cy="20"
        r={radius}
        fill="none"
        stroke={trackColor}
        strokeWidth={strokeWidth}
      />
      <circle
        cx="20"
        cy="20"
        r={radius}
        fill="none"
        stroke={color}
        strokeWidth={strokeWidth}
        strokeLinecap="round"
        strokeDasharray={
          wavy
            ? "4 2"
            : `${Math.max(0, circumference - gapSize)} ${circumference}`
        }
        strokeDashoffset={
          determinate ? circumference * (1 - normalized) : circumference * 0.25
        }
        transform="rotate(-90 20 20)"
        className={
          determinate
            ? "transition-[stroke-dashoffset] duration-[var(--ien-motion-normal)]"
            : "origin-center animate-spin"
        }
      />
    </svg>
  );
}

export interface IenLinearProgressIndicatorProps extends React.ComponentPropsWithRef<"div"> {
  progress?: number;
  color?: string;
  trackColor?: string;
  label?: string;
}

export function IenLinearProgressIndicator({
  progress,
  color = "var(--ien-brand)",
  trackColor = "var(--ien-brand-weak)",
  label = "진행 중",
  className,
  ...props
}: IenLinearProgressIndicatorProps) {
  const determinate = progress !== undefined;
  const normalized = Math.max(
    0,
    Math.min(1, Number.isFinite(progress) ? (progress ?? 0) : 0),
  );
  return (
    <div
      className={cn("h-1 w-full overflow-hidden rounded-full", className)}
      style={{ backgroundColor: trackColor }}
      role="progressbar"
      aria-label={label}
      aria-valuemin={determinate ? 0 : undefined}
      aria-valuemax={determinate ? 100 : undefined}
      aria-valuenow={determinate ? Math.round(normalized * 100) : undefined}
      {...props}
    >
      <div
        className={cn(
          "h-full rounded-full",
          determinate
            ? "transition-[width] duration-[var(--ien-motion-normal)]"
            : "w-2/5 animate-pulse",
        )}
        style={{
          width: determinate ? `${normalized * 100}%` : undefined,
          backgroundColor: color,
        }}
      />
    </div>
  );
}

export function IenCircularWavyProgressIndicator({
  progress,
  label = "진행 중",
  className,
  ...props
}: IenCircularProgressIndicatorProps) {
  return (
    <IenCircularProgressIndicator
      progress={progress}
      label={label}
      wavy
      className={className}
      {...props}
    />
  );
}

export {
  IenCircularProgressIndicator as CircularProgressIndicator,
  IenCircularWavyProgressIndicator as CircularWavyProgressIndicator,
  IenLinearProgressIndicator as LinearProgressIndicator,
};
