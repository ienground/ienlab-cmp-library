"use client"

import type { ComponentProps } from "react"
import { cn } from "cn"

export type BottomInfoProps = ComponentProps<"div"> & {
  gradient?: "none" | "default" | { from: string; to?: string }
  backgroundColor?: string
}

function BottomInfo({ gradient = "default", backgroundColor = "var(--ien-surface-weak)", children, className, ...props }: BottomInfoProps) {
  return <div {...props} data-slot="bottom-info" className={cn("w-full", className)}>
    <div className="px-ien-xl pt-ien-xl pb-ien-md text-[length:var(--ien-font-size-body2)] text-(--ien-text-secondary)" style={{ backgroundColor }}>{children}</div>
    {gradient !== "none" && <div aria-hidden="true" className="h-12" style={{ background: `linear-gradient(to bottom, ${typeof gradient === "object" ? gradient.from : backgroundColor}, ${typeof gradient === "object" ? gradient.to ?? "transparent" : "transparent"})` }} />}
  </div>
}

export { BottomInfo }
