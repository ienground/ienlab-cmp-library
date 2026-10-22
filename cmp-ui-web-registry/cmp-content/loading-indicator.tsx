"use client";

import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type IenLoadingPolygon = readonly { x: number; y: number }[];
export interface IenLoadingIndicatorProps extends React.ComponentPropsWithRef<"svg"> {
  color?: string;
  label?: string;
  polygons?: readonly IenLoadingPolygon[] | null;
}
const defaultPolygons: readonly IenLoadingPolygon[] = [3, 4, 6, 8, 5].map((lobes, shape) =>
  Array.from({ length: 64 }, (_, index) => {
    const angle = index / 64 * Math.PI * 2;
    const radius = 0.78 + Math.cos(angle * lobes) * (shape === 1 ? 0.08 : 0.17);
    return { x: Math.cos(angle) * radius, y: Math.sin(angle) * radius };
  }),
);
function polygonPath(points: IenLoadingPolygon) {
  if (points.length < 3) return "";
  const lengths = points.map((point, index) => Math.hypot(point.x - points[(index + 1) % points.length].x, point.y - points[(index + 1) % points.length].y));
  const total = lengths.reduce((sum, length) => sum + length, 0);
  if (total === 0) return "";
  return Array.from({ length: 64 }, (_, index) => {
    let distance = total * index / 64;
    let segment = 0;
    while (segment < lengths.length - 1 && distance > lengths[segment]) distance -= lengths[segment++];
    const start = points[segment];
    const end = points[(segment + 1) % points.length];
    const t = lengths[segment] ? distance / lengths[segment] : 0;
    return `${index === 0 ? "M" : "L"}${(24 + (start.x + (end.x - start.x) * t) * 22).toFixed(3)},${(24 + (start.y + (end.y - start.y) * t) * 22).toFixed(3)}`;
  }).join(" ") + " Z";
}
export function IenLoadingIndicator({ color = "var(--ien-brand)", label = "불러오는 중", polygons, className, ...props }: IenLoadingIndicatorProps) {
  const pathRef = React.useRef<SVGPathElement>(null);
  const shapes = polygons ?? defaultPolygons;
  const paths = shapes.map(polygonPath).filter(Boolean);
  const key = paths.join("|");
  React.useEffect(() => {
    const path = pathRef.current;
    const preference = window.matchMedia("(prefers-reduced-motion: reduce)");
    if (!path || paths.length < 2) return;
    let animation: Animation | undefined;
    const sync = () => {
      animation?.cancel();
      if (!preference.matches) animation = path.animate([...paths, paths[0]].map((d) => ({ d: `path("${d}")` })), { duration: paths.length * 650, iterations: Infinity, easing: "ease-in-out" });
    };
    sync();
    preference.addEventListener("change", sync);
    return () => { animation?.cancel(); preference.removeEventListener("change", sync); };
  }, [key]);
  return <svg viewBox="0 0 48 48" className={cn("size-12 animate-spin [animation-duration:3s] motion-reduce:animate-none", className)} role="status" aria-label={label} {...props}><path ref={pathRef} d={paths[0]} fill={color} /></svg>;
}
export { IenLoadingIndicator as LoadingIndicator };
