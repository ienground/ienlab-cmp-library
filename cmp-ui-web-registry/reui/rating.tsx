"use client"

import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"
import { useEffect, useId, useRef, useState, type ComponentProps, type PointerEvent } from "react"
import { cva, type VariantProps } from "class-variance-authority"
import { cn } from "cn"

const ratingStarPath = "M12 0.96 15.113 7.715 22.511 8.59 17.044 13.637 18.474 20.93 12 17.299 5.526 20.93 6.956 13.637 1.489 8.59 8.887 7.715Z"
const ratingStarLinePath = "M354 673 480 597 606 674 573 530 684 434 538 421 480 285 422 420 276 433 387 530 354 673Z M233 840 298 559 80 370 368 345 480 80 592 345 880 370 662 559 727 840 480 691Z"

const ratingVariants = cva("inline-flex w-fit items-center", {
  variants: {
    size: {
      tiny: "gap-0.5",
      small: "gap-0.5",
      medium: "gap-(--ien-spacing-xxs)",
      large: "gap-(--ien-spacing-xs)",
      big: "gap-(--ien-spacing-xs)",
      sm: "gap-0.5",
      default: "gap-(--ien-spacing-xxs)",
      lg: "gap-(--ien-spacing-xs)",
    },
  },
  defaultVariants: { size: "medium" },
})
const starVariants = cva("relative block shrink-0", {
  variants: {
    size: {
      tiny: "size-[18px]", small: "size-6", medium: "size-[34px]", large: "size-11", big: "size-14",
      sm: "size-6", default: "size-[34px]", lg: "size-11",
    },
  },
  defaultVariants: { size: "medium" },
})
const valueVariants = cva("tabular-nums text-(--ien-text-secondary)", {
  variants: {
    size: {
      tiny: "text-(length:--ien-font-size-caption) font-(--ien-font-weight-caption)",
      small: "text-(length:--ien-font-size-caption) font-(--ien-font-weight-caption)",
      medium: "text-(length:--ien-font-size-label2) font-(--ien-font-weight-label2)",
      large: "text-(length:--ien-font-size-label1) font-(--ien-font-weight-label1)",
      big: "text-(length:--ien-font-size-label1) font-(--ien-font-weight-label1)",
      sm: "text-(length:--ien-font-size-caption)", default: "text-(length:--ien-font-size-label2)", lg: "text-(length:--ien-font-size-label1)",
    },
  },
  defaultVariants: { size: "medium" },
})
export type RatingProps = Omit<ComponentProps<"div">, "defaultValue"> &
  VariantProps<typeof ratingVariants> & IenToneProps & {
    /** 현재 별점입니다. 0부터 max까지의 소수 값을 비례해서 채웁니다. */
    value?: number
    /** 제어하지 않는 별점의 초기 값입니다. */
    defaultValue?: number
    /** 사용자가 클릭하거나 드래그하거나 키보드로 점수를 변경하면 호출합니다. */
    onValueChange?: (value: number) => void
    /** 별의 최대 개수입니다. 기본값은 5입니다. */
    max?: number
    /** 콜백이 없으면 기본적으로 읽기 전용입니다. */
    readOnly?: boolean
    /** 읽기 전용일 때 전체 별, 별 하나와 값, 비례 채움 별 하나를 표시합니다. */
    variant?: "full" | "compact" | "iconOnly"
    disabled?: boolean
    enabled?: boolean
    ariaLabel?: string
    /** 보조기기에 전달하는 점수 설명입니다. */
    valueText?: string
    /** 기존 웹 API와의 호환을 위한 현재 별점입니다. value를 우선합니다. */
    rating?: number
    maxRating?: number
    editable?: boolean
    onRatingChange?: (rating: number) => void
    showValue?: boolean
    /** 기존 웹 API와 동일하게 숫자 텍스트에 적용합니다. */
    starClassName?: string
  }

function Rating({
  tone: ienTone,
  useGradient: ienUseGradient,
  value,
  defaultValue = 0,
  onValueChange,
  max,
  readOnly,
  variant = "full",
  disabled = false,
  enabled = true,
  ariaLabel,
  valueText,
  rating,
  maxRating = 5,
  editable,
  onRatingChange,
  showValue = false,
  size = "medium",
  className,
  starClassName,
  children,
  ref,
  ...props
}: RatingProps) {
  const ienTheme = useIenTheme({ tone: ienTone ?? "brand", useGradient: ienUseGradient })
  const id = useId()
  const root = useRef<HTMLDivElement>(null)
  const shake = useRef<Animation | null>(null)
  useEffect(() => () => shake.current?.cancel(), [])
  const activePointer = useRef<number | null>(null)
  const lastPointerValue = useRef<number | null>(null)
  const [internalValue, setInternalValue] = useState(defaultValue)
  const [pressed, setPressed] = useState(false)
  const itemCount = Math.max(1, Math.floor(Number.isFinite(max ?? maxRating) ? (max ?? maxRating) : 5))
  const current = value ?? rating ?? internalValue
  const resolvedValue = Math.min(itemCount, Math.max(0, Number.isFinite(current) ? current : 0))
  const isReadOnly = readOnly ?? (editable !== undefined ? !editable : !onValueChange && !onRatingChange)
  const isDisabled = disabled || !enabled
  const interactive = !isReadOnly && !isDisabled
  const displayVariant = isReadOnly ? variant : "full"
  const displayValue = resolvedValue
  const label = ariaLabel ?? props["aria-label"] ?? (isReadOnly ? "현재 별점" : "별점 평가")
  const description = valueText ?? `${itemCount}점 만점에 ${Number(resolvedValue.toFixed(1))}점`
  const text = Number(displayValue.toFixed(1)).toString()

  function change(next: number) {
    if (!interactive) return
    const clamped = Math.min(itemCount, Math.max(0, next))
    if (value === undefined && rating === undefined) setInternalValue(clamped)
    onValueChange?.(clamped)
    if (onRatingChange !== onValueChange) onRatingChange?.(clamped)
  }
  function pointerValue(event: PointerEvent<HTMLDivElement>) {
    const rect = event.currentTarget.getBoundingClientRect()
    const stars = event.currentTarget.querySelector<HTMLElement>('[data-slot="rating-stars"]')?.getBoundingClientRect() ?? rect
    const progress = Math.max(0, Math.min(1, (event.clientX - stars.left) / stars.width))
    return Math.min(itemCount, Math.floor(progress * itemCount) + 1)
  }
  function releaseAnimation(clickedValue: number) {
    if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) return
    root.current?.querySelectorAll<HTMLElement>('[data-slot="rating-star"]').forEach((star, index) => {
      if (index >= clickedValue) return
      star.getAnimations().forEach((animation) => animation.cancel())
      star.animate([
        { transform: "scale(0.9)", filter: "drop-shadow(0 0 0.4em var(--ien-control-color))" },
        { transform: `scale(${index === clickedValue - 1 ? 1.26 : 1.2})`, offset: 0.45 },
        { transform: "scale(1)", filter: "drop-shadow(0 0 0 transparent)" },
      ], { duration: 360, delay: Math.min(48, Math.abs(index - clickedValue + 1) * 12), easing: "cubic-bezier(0.2, 0, 0, 1)" })
    })
  }

  return (
    <IenThemeContext.Provider value={ienTheme}>
      <div
        {...props}
        ref={(node) => {
          root.current = node
          if (typeof ref === "function") return ref(node)
          if (ref) ref.current = node
        }}
        data-slot="rating"
        data-ien=""
        data-ien-tone={ienTheme.tone}
        data-ien-gradient={ienTheme.useGradient}
        data-size={size}
        data-variant={displayVariant}
        data-disabled={isDisabled || undefined}
        role={isReadOnly ? "img" : "slider"}
        tabIndex={interactive ? props.tabIndex ?? 0 : undefined}
        aria-label={isReadOnly ? `${label}: ${description}` : label}
        aria-disabled={isDisabled || undefined}
        aria-valuemin={isReadOnly ? undefined : 0}
        aria-valuemax={isReadOnly ? undefined : itemCount}
        aria-valuenow={isReadOnly ? undefined : resolvedValue}
        aria-valuetext={isReadOnly ? undefined : description}
        className={cn(ratingVariants({ size }), "select-none rounded-xs outline-none focus-visible:ring-3 focus-visible:ring-ring/50", interactive && "touch-none cursor-pointer", className)}
        onPointerDown={(event) => {
          props.onPointerDown?.(event)
          if (event.defaultPrevented || event.button !== 0) return
          if (isDisabled && !window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
            shake.current?.cancel()
            shake.current = event.currentTarget.animate([
              { transform: "translateX(0)" }, { transform: "translateX(-6px)" },
              { transform: "translateX(6px)" }, { transform: "translateX(-3.6px)" },
              { transform: "translateX(3.6px)" }, { transform: "translateX(0)" },
            ], { duration: 200 })
          }
          if (!interactive) return
          activePointer.current = event.pointerId
          event.currentTarget.setPointerCapture(event.pointerId)
          setPressed(true)
          const next = pointerValue(event)
          lastPointerValue.current = next
          change(next)
        }}
        onPointerMove={(event) => {
          props.onPointerMove?.(event)
          if (event.defaultPrevented || !interactive || activePointer.current !== event.pointerId) return
          const next = pointerValue(event)
          if (lastPointerValue.current !== next) {
            lastPointerValue.current = next
            change(next)
          }
        }}
        onPointerUp={(event) => {
          props.onPointerUp?.(event)
          if (activePointer.current !== event.pointerId) return
          activePointer.current = null
          setPressed(false)
          if (event.currentTarget.hasPointerCapture(event.pointerId)) event.currentTarget.releasePointerCapture(event.pointerId)
          if (interactive) releaseAnimation(lastPointerValue.current ?? resolvedValue)
        }}
        onPointerCancel={(event) => {
          props.onPointerCancel?.(event)
          activePointer.current = null
          setPressed(false)
        }}
        onLostPointerCapture={(event) => {
          props.onLostPointerCapture?.(event)
          activePointer.current = null
          setPressed(false)
        }}
        onPointerLeave={(event) => {
          props.onPointerLeave?.(event)
        }}
        onKeyDown={(event) => {
          props.onKeyDown?.(event)
          if (event.defaultPrevented || !interactive) return
          const next = ({ ArrowRight: resolvedValue + 1, ArrowUp: resolvedValue + 1, ArrowLeft: resolvedValue - 1, ArrowDown: resolvedValue - 1, Home: 0, End: itemCount } as Record<string, number>)[event.key]
          if (next === undefined) return
          event.preventDefault()
          change(next)
          releaseAnimation(Math.min(itemCount, Math.max(0, next)))
        }}
      >
        <div data-slot="rating-stars" aria-hidden="true" className={ratingVariants({ size })}>
          {Array.from({ length: displayVariant === "full" ? itemCount : 1 }, (_, index) => {
            const fraction = displayVariant === "compact" ? 1 : displayVariant === "iconOnly" ? resolvedValue / itemCount : Math.min(1, Math.max(0, displayValue - index))
            const filled = fraction > 0
            const gradientId = `${id}-gradient-${index}`
            return (
              <span key={index} data-slot="rating-star" data-fill={fraction} className={starVariants({ size })}
                style={{ opacity: isDisabled ? 0.42 : filled ? 1 : 0.72, transform: `scale(${filled ? pressed && interactive ? 0.86 : 1 : 0.92})`, transition: "transform var(--ien-motion-fast) var(--ien-motion-standard-easing), opacity var(--ien-motion-fast)" }}>
                <svg aria-hidden="true" focusable="false" className="absolute size-0">
                  <defs>
                    <linearGradient id={gradientId} x1="0" y1="0" x2="1" y2="1">
                      <stop offset="0%" stopColor={ienTheme.useGradient === false ? "var(--ien-control-color)" : "color-mix(in srgb, var(--ien-control-color), var(--ien-control-on) 18%)"} />
                      <stop offset="50%" stopColor="var(--ien-control-color)" />
                      <stop offset="100%" stopColor={ienTheme.useGradient === false ? "var(--ien-control-color)" : "color-mix(in srgb, var(--ien-control-color), var(--ien-control-on) 36%)"} />
                    </linearGradient>
                  </defs>
                </svg>
                {filled ? (
                  <>
                    {fraction < 1 && (
                      <svg data-slot="rating-star-empty" aria-hidden="true" focusable="false" viewBox="0 0 24 24" className="size-full">
                        <path d={ratingStarPath} fill="var(--ien-border-strong)" />
                      </svg>
                    )}
                    <span
                      data-slot="rating-star-filled"
                      className="absolute inset-0 overflow-hidden"
                      style={{ clipPath: `inset(0 ${(1 - fraction) * 100}% 0 0)` }}
                    >
                      <svg aria-hidden="true" focusable="false" viewBox="0 0 24 24" className="size-full">
                        <path d={ratingStarPath} fill={`url(#${gradientId})`} />
                      </svg>
                    </span>
                  </>
                ) : (
                  <svg data-slot="rating-star-empty" aria-hidden="true" focusable="false" viewBox="0 0 960 960" className="size-full">
                    <path d={ratingStarLinePath} fill="var(--ien-border-strong)" fillRule="evenodd" />
                  </svg>
                )}
              </span>
            )
          })}
        </div>
        {(displayVariant === "compact" || showValue) && <span data-slot="rating-value" aria-hidden="true" className={cn(valueVariants({ size }), isDisabled && "text-(--ien-text-disabled)", starClassName)}>{text}</span>}
        {children}
      </div>
    </IenThemeContext.Provider>
  )
}

export { Rating }
