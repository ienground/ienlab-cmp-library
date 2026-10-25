"use client"

import * as React from "react"

export const menuSurfaceClassName = "relative z-50 rounded-(--ien-radius-xl) border border-[color-mix(in_srgb,var(--ien-border)_35%,transparent)] bg-(--ien-surface-raised) text-(--ien-text-primary) shadow-lg outline-none"

export const menuMotionClassName = "data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=open]:fade-in-0 data-[state=closed]:fade-out-0 data-[state=open]:zoom-in-92 data-[state=closed]:zoom-out-92 data-[state=open]:duration-(--ien-motion-normal) data-[state=closed]:duration-(--ien-motion-fast) ease-(--ien-motion-standard-easing) motion-reduce:animate-none"

export const menuBaseMotionClassName = "transition-[scale,opacity] duration-(--ien-motion-normal) ease-(--ien-motion-standard-easing) data-starting-style:scale-92 data-starting-style:opacity-0 data-ending-style:scale-92 data-ending-style:opacity-0 data-ending-style:duration-(--ien-motion-fast) motion-reduce:transition-none"

export const menuItemClassName = "group/dropdown-menu-item relative isolate flex min-h-8 cursor-default items-center gap-(--ien-spacing-xs) px-(--ien-spacing-xs) py-1.5 font-normal text-(--ien-text-primary) outline-none select-none data-inset:ps-[calc(var(--ien-spacing-xs)*2+var(--ien-icon-xs))] data-disabled:text-(--ien-text-disabled) [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-(--ien-icon-xs) [&_svg]:text-(--ien-text-tertiary) data-disabled:[&_svg]:text-(--ien-text-disabled) before:pointer-events-none before:absolute before:inset-0 before:-z-10 before:rounded-(--ien-radius-sm) before:[corner-shape:squircle] before:bg-(--ien-surface-variant) before:opacity-0 before:transition-opacity before:duration-150 data-highlighted:not-data-disabled:before:opacity-100 data-[state=open]:not-data-disabled:before:opacity-100 motion-reduce:before:transition-none"

export const menuSelectedClassName = "data-selected:not-data-disabled:text-(--ien-brand) data-selected:font-semibold data-selected:before:bg-(--ien-brand-weak) data-selected:before:opacity-40 data-selected:data-highlighted:not-data-disabled:before:opacity-100"

export const menuCheckedClassName = "data-[state=checked]:not-data-disabled:text-(--ien-brand) data-[state=checked]:font-semibold data-[state=checked]:before:bg-(--ien-brand-weak) data-[state=checked]:before:opacity-40 data-[state=checked]:data-highlighted:not-data-disabled:before:opacity-100 aria-selected:not-data-disabled:text-(--ien-brand) aria-selected:font-semibold aria-selected:before:bg-(--ien-brand-weak) aria-selected:before:opacity-40 aria-selected:data-highlighted:not-data-disabled:before:opacity-100"

export const menuLabelClassName = "px-(--ien-spacing-xs) py-(--ien-spacing-xxs) font-normal text-(--ien-text-tertiary) data-inset:ps-[calc(var(--ien-spacing-xs)*2+var(--ien-icon-xs))]"

export const menuSeparatorClassName = "mx-(--ien-spacing-xs) my-(--ien-spacing-xxs) h-(--ien-stroke-thin) bg-[color-mix(in_srgb,var(--ien-border)_48%,transparent)]"

export const menuIndicatorClassName = "pointer-events-none flex size-(--ien-icon-xs) shrink-0 items-center justify-center text-(--ien-brand) [[data-disabled]_&]:text-(--ien-text-disabled) [&_svg]:text-current"

export function useDisabledMenuShake(onPointerDown?: React.PointerEventHandler<HTMLDivElement>) {
  const animation = React.useRef<Animation | null>(null)
  React.useEffect(() => () => animation.current?.cancel(), [])
  return (event: React.PointerEvent<HTMLDivElement>) => {
    onPointerDown?.(event)
    if (event.defaultPrevented || !event.currentTarget.hasAttribute("data-disabled") || window.matchMedia("(prefers-reduced-motion: reduce)").matches) return
    animation.current?.cancel()
    animation.current = event.currentTarget.animate([
      { transform: "translateX(0)", offset: 0 },
      { transform: "translateX(-6px)", offset: 0.15 },
      { transform: "translateX(6px)", offset: 0.35 },
      { transform: "translateX(-3.6px)", offset: 0.55 },
      { transform: "translateX(3.6px)", offset: 0.75 },
      { transform: "translateX(0)", offset: 1 },
    ], { duration: 200 })
  }
}
