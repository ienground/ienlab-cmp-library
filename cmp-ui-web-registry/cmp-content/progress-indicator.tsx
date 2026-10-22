import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type IenProgressStroke = { width?: number; cap?: "round" | "butt" | "square" };
export interface IenCircularProgressIndicatorProps extends Omit<React.ComponentPropsWithRef<"svg">, "stroke" | "amplitude"> {
  progress?: number;
  color?: string;
  trackColor?: string;
  strokeWidth?: number;
  strokeCap?: "round" | "butt" | "square";
  stroke?: IenProgressStroke;
  trackStroke?: IenProgressStroke;
  gapSize?: number;
  amplitude?: number | ((progress: number) => number);
  wavelength?: number;
  waveSpeed?: number;
  label?: string;
  wavy?: boolean;
}

export function IenCircularProgressIndicator({
  progress,
  color = "var(--ien-brand)",
  trackColor = "var(--ien-brand-weak)",
  strokeWidth = 4,
  strokeCap = "round",
  stroke,
  trackStroke,
  gapSize = 4,
  amplitude = 1,
  wavelength = 40,
  waveSpeed = wavelength,
  label = "진행 중",
  wavy = false,
  className,
  style,
  ...props
}: IenCircularProgressIndicatorProps) {
  const determinate = progress !== undefined;
  const normalized = Math.max(0, Math.min(1, Number.isFinite(progress) ? (progress ?? 0) : 0));
  const width = Math.max(0, stroke?.width ?? strokeWidth);
  const radius = 20 - width / 2 - (wavy ? 2 : 0);
  const circumference = 2 * Math.PI * radius;
  const waveAmplitude = wavy ? Math.max(0, Math.min(1, typeof amplitude === "function" ? amplitude(normalized) : amplitude)) * 2 : 0;
  const waves = Math.max(3, Math.round(circumference / Math.max(1, wavelength)));
  const path = Array.from({ length: 241 }, (_, index) => {
    const angle = index / 240 * Math.PI * 2 - Math.PI / 2;
    const r = radius + Math.sin(index / 240 * Math.PI * 2 * waves) * waveAmplitude;
    return `${index === 0 ? "M" : "L"}${(20 + Math.cos(angle) * r).toFixed(3)},${(20 + Math.sin(angle) * r).toFixed(3)}`;
  }).join(" ");
  const gap = Math.max(0, gapSize) / circumference;
  const activeLength = Math.max(0, (determinate ? normalized : 0.75) - gap);
  return <svg viewBox="0 0 40 40" className={cn("size-10", className)} role="progressbar" aria-label={label} aria-valuemin={determinate ? 0 : undefined} aria-valuemax={determinate ? 100 : undefined} aria-valuenow={determinate ? Math.round(normalized * 100) : undefined} style={style} {...props}>
    <circle cx="20" cy="20" r={radius} fill="none" stroke={trackColor} strokeWidth={trackStroke?.width ?? width} strokeLinecap={trackStroke?.cap ?? strokeCap} pathLength={1} strokeDasharray={`${Math.max(0, 1 - (determinate ? normalized : 0.75) - gap)} 1`} strokeDashoffset={-(determinate ? normalized : 0.75) - gap} transform="rotate(-90 20 20)" className={determinate ? "transition-[stroke-dasharray,stroke-dashoffset] duration-[var(--ien-motion-normal)] motion-reduce:transition-none" : "origin-center animate-spin motion-reduce:animate-none"} />
    <path d={path} fill="none" stroke={color} strokeWidth={width} strokeLinecap={stroke?.cap ?? strokeCap} pathLength={1} strokeDasharray={`${activeLength} 1`} className={cn("origin-center", determinate ? "transition-[stroke-dasharray] duration-[var(--ien-motion-normal)] motion-reduce:transition-none" : "animate-spin motion-reduce:animate-none", wavy && waveSpeed !== 0 && "animate-spin motion-reduce:animate-none")} style={wavy && waveSpeed !== 0 ? { animationDuration: `${Math.max(0.1, wavelength / Math.abs(waveSpeed))}s`, animationDirection: waveSpeed < 0 ? "reverse" : undefined } : undefined} />
  </svg>;
}

export interface IenLinearProgressIndicatorProps extends React.ComponentPropsWithRef<"div"> {
  progress?: number;
  color?: string;
  trackColor?: string;
  label?: string;
  strokeCap?: "round" | "butt" | "square";
}

export function IenLinearProgressIndicator({
  progress,
  color = "var(--ien-brand)",
  trackColor = "var(--ien-brand-weak)",
  label = "진행 중",
  strokeCap = "round",
  className,
  style,
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
      style={{ backgroundColor: trackColor, borderRadius: strokeCap === "round" ? undefined : 0, ...style }}
      role="progressbar"
      aria-label={label}
      aria-valuemin={determinate ? 0 : undefined}
      aria-valuemax={determinate ? 100 : undefined}
      aria-valuenow={determinate ? Math.round(normalized * 100) : undefined}
      {...props}
    >
      <div
        className={cn(
          "h-full origin-left rounded-full motion-reduce:animate-none motion-reduce:transition-none",
          determinate
            ? "w-full transition-transform duration-[var(--ien-motion-normal)] ease-[var(--ien-motion-standard-easing)]"
            : "w-2/5 animate-[ien-progress-indeterminate_1.5s_linear_infinite]",
        )}
        style={{
          transform: determinate ? `scaleX(${normalized})` : undefined,
          backgroundColor: color,
          borderRadius: strokeCap === "round" ? undefined : 0,
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
