"use client"

import * as React from "react"
import { IconCheck, IconX } from "@tabler/icons-react"
import { cn } from "cn"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenContinuousCorners, useIenLanguage, useIenTheme } from "@/lib/ien-theme-context"
import { ProgressIndicator } from "@/components/ui/progress-indicator"
import { useIenAnimatedSize } from "@/lib/ien-animated-size"

export type ChipKind = "assist" | "filter" | "input" | "suggestion"

export type ChipProps = Omit<React.ComponentProps<"button">, "type"> & IenToneProps & {
  kind?: ChipKind
  selected?: boolean
  defaultSelected?: boolean
  onSelectedChange?: (selected: boolean) => void
  elevated?: boolean
  loading?: boolean
  leadingIcon?: React.ReactNode
  /** Filter Chip의 선택 시 leading 영역을 대체합니다. 기본값은 체크이며 null이면 숨깁니다. */
  selectedIcon?: React.ReactNode
  trailingIcon?: React.ReactNode
  /** Input Chip에서는 선택 여부와 관계없이 leadingIcon보다 우선합니다. */
  avatar?: React.ReactNode
  onRemove?: () => void
  removeLabel?: string
}

function Chip({
  kind = "assist", selected, defaultSelected = false, onSelectedChange,
  elevated = false, loading = false, leadingIcon, selectedIcon = <IconCheck />, trailingIcon, avatar,
  onRemove, removeLabel, disabled = false, tone, useGradient,
  className, style, children, onClick, ...props
}: ChipProps) {
  const theme = useIenTheme({ tone, useGradient })
  const animatedSizeRef = useIenAnimatedSize<HTMLSpanElement>()
  const language = useIenLanguage("ko")
  const labelId = React.useId()
  const removeId = React.useId()
  const [localSelected, setLocalSelected] = React.useState(defaultSelected)
  const selectable = kind === "filter" || kind === "input"
  const isSelected = selectable && (selected ?? localSelected)
  const blocked = disabled || loading
  const leading = kind === "filter" && isSelected ? selectedIcon : kind === "input" ? avatar ?? leadingIcon : leadingIcon

  return (
    <>
      <IenContinuousCorners />
      <span
        ref={animatedSizeRef}
        data-slot="chip"
        data-kind={kind}
        data-state={isSelected ? "selected" : "unselected"}
        data-disabled={disabled || undefined}
        data-loading={loading || undefined}
        data-elevated={elevated || undefined}
        data-ien=""
        data-ien-shape="capsule"
        data-ien-typography="label2"
        data-ien-tone={theme.tone ?? "brand"}
        data-ien-gradient={theme.useGradient}
        className={cn(
          "group/chip relative inline-flex w-fit max-w-full shrink-0 items-center rounded-full border border-transparent bg-clip-border text-[length:var(--ien-font-size-label2)] leading-(--ien-line-height-label2) transition-[color,background-color,box-shadow] motion-reduce:transition-none [&_svg]:pointer-events-none [&_svg]:size-(--ien-icon-sm) [&_svg]:shrink-0",
          isSelected ? "ien-fill" : "ien-weak",
          elevated && !disabled && "shadow-sm",
          className,
        )}
        style={{
          ...(disabled ? {
            backgroundColor: "var(--ien-surface-variant)",
            backgroundImage: "none",
            color: "var(--ien-text-disabled)",
            borderColor: "transparent",
          } : {}),
          ...style,
        }}
      >
        <button
          {...props}
          type="button"
          data-slot="chip-action"
          disabled={blocked}
          aria-pressed={selectable ? isSelected : undefined}
          aria-busy={loading || undefined}
          className="relative inline-flex min-w-0 items-center justify-center rounded-full bg-transparent px-(--ien-spacing-sm) py-[calc(var(--ien-spacing-xxs)+var(--ien-spacing-xxxs))] text-inherit outline-none focus-visible:ring-3 focus-visible:ring-ring/50 enabled:cursor-pointer enabled:active:scale-(--ien-button-pressed-scale) disabled:cursor-default motion-reduce:transition-none"
          onClick={(event) => {
            if (blocked) return
            onClick?.(event)
            if (!event.defaultPrevented && selectable) {
              const next = !isSelected
              if (selected === undefined) setLocalSelected(next)
              onSelectedChange?.(next)
            }
          }}
        >
          <span className={cn("inline-flex min-w-0 items-center gap-(--ien-spacing-xxs)", loading && "opacity-0")}>
            {leading != null && <span data-slot="chip-leading" aria-hidden="true" className="inline-flex shrink-0">{leading}</span>}
            <span id={labelId} data-slot="chip-label" className="min-w-0 truncate">{children}</span>
            {trailingIcon != null && <span data-slot="chip-trailing" aria-hidden="true" className="inline-flex shrink-0">{trailingIcon}</span>}
          </span>
          {loading && <span aria-hidden="true" className="pointer-events-none absolute inset-0 flex items-center justify-center"><ProgressIndicator size={16} strokeWidth={2} color="currentColor" trackColor="transparent" /></span>}
        </button>
        {onRemove && (
          <button
            type="button"
            data-slot="chip-remove"
            disabled={blocked}
            aria-labelledby={`${removeId} ${labelId}`}
            onClick={(event) => {
              event.stopPropagation()
              if (!blocked) onRemove()
            }}
            className="relative me-(--ien-spacing-xs) inline-flex shrink-0 items-center justify-center rounded-full bg-transparent text-inherit outline-none after:absolute after:-inset-(--ien-spacing-xxs) focus-visible:ring-3 focus-visible:ring-ring/50 enabled:cursor-pointer disabled:cursor-default"
          >
            <IconX aria-hidden="true" />
            <span id={removeId} className="sr-only">{removeLabel ?? (language === "ko" ? "삭제" : "Remove")}</span>
          </button>
        )}
      </span>
    </>
  )
}

export { Chip }
