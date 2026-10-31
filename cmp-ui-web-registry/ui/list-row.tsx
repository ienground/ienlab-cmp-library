"use client"

import * as React from "react"
import { cn } from "cn"
import { Slot } from "radix-ui"

export type ListRowProps = Omit<React.ComponentProps<"div">, "title"> & {
  title?: React.ReactNode
  description?: React.ReactNode
  leading?: React.ReactNode
  trailing?: React.ReactNode
  disabled?: boolean
  border?: "none" | "indented"
  alignment?: "top" | "center"
  asChild?: boolean
}

function ListRow({ title, description, leading, trailing, disabled = false, border = "none", alignment = "center", asChild, children, className, onClick, onKeyDown, ...props }: ListRowProps) {
  const Comp = asChild ? Slot.Root : "div"
  return <Comp {...props} role={props.role ?? (onClick ? "button" : undefined)} tabIndex={props.tabIndex ?? (onClick && !disabled ? 0 : undefined)}
    data-slot="list-row" aria-disabled={disabled || undefined}
    onClick={(event) => { if (disabled) { event.preventDefault(); return }; onClick?.(event) }}
    onKeyDown={(event) => {
      onKeyDown?.(event)
      if (!event.defaultPrevented && onClick && !disabled && event.target === event.currentTarget && (event.key === "Enter" || event.key === " ")) { event.preventDefault(); event.currentTarget.click() }
    }}
    className={cn("relative flex min-h-14 w-full gap-3 px-4 py-3 text-(--ien-text-primary) outline-none focus-visible:ring-2 focus-visible:ring-ring", alignment === "top" ? "items-start" : "items-center", border === "indented" && "after:absolute after:inset-x-4 after:bottom-0 after:h-px after:bg-(--ien-border)", onClick && !disabled && "cursor-pointer transition-colors hover:bg-(--ien-surface-weak) active:bg-(--ien-surface-variant)", disabled && "opacity-(--ien-state-disabled-alpha)", className)}>
    {asChild ? children : <>
      {leading != null && <div className="shrink-0">{leading}</div>}
      <div className="min-w-0 flex-1 space-y-1">{title != null && <div className="text-[length:var(--ien-font-size-body1)] leading-(--ien-line-height-body1)">{title}</div>}{description != null && <div className="text-[length:var(--ien-font-size-body2)] leading-(--ien-line-height-body2) text-(--ien-text-secondary)">{description}</div>}{children}</div>
      {trailing != null && <div className="shrink-0 text-[length:var(--ien-font-size-label2)] text-(--ien-text-secondary)">{trailing}</div>}
    </>}
  </Comp>
}

function ListHeader({ className, ...props }: React.ComponentProps<"div">) {
  return <div {...props} data-slot="list-header" className={cn("px-4 pb-2 pt-5 text-[length:var(--ien-font-size-title3)] leading-(--ien-line-height-title3) font-semibold", className)} />
}
function ListFooter({ className, ...props }: React.ComponentProps<"div">) {
  return <div {...props} data-slot="list-footer" className={cn("px-4 py-2 text-[length:var(--ien-font-size-caption)] leading-(--ien-line-height-caption) text-(--ien-text-tertiary)", className)} />
}
function BoardRow({ metadata, ...props }: ListRowProps & { metadata?: React.ReactNode }) {
  return <ListRow {...props} alignment="top" trailing={props.trailing ?? metadata} />
}
function TableRow({ className, ...props }: React.ComponentProps<"tr">) {
  return <tr {...props} data-slot="table-row" className={cn("border-b border-(--ien-border) text-[length:var(--ien-font-size-body2)] [&>td]:px-4 [&>td]:py-3 [&>th]:px-4 [&>th]:py-3 [&>th]:text-start", className)} />
}

export { ListRow, ListHeader, ListFooter, BoardRow, TableRow }
