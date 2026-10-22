"use client";

import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type RatingSize = "tiny" | "small" | "medium" | "large" | "big";
export type RatingVariant = "full" | "compact" | "iconOnly" | "Full" | "Compact" | "IconOnly";

export type RatingProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "role" | "aria-label" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  value?: number;
  defaultValue?: number;
  onValueChange?: (value: number) => void;
  max?: number;
  readOnly?: boolean;
  disabled?: boolean;
  enabled?: boolean;
  size?: RatingSize;
  variant?: RatingVariant;
  ariaLabel?: string;
  valueText?: string;
  label?: string;
  className?: string;
};

const sizeStyles: Record<RatingSize, { icon: number; gap: string; text: string }> = {
  tiny: { icon: 18, gap: "gap-[2px]", text: "text-ien-caption" },
  small: { icon: 24, gap: "gap-[2px]", text: "text-ien-caption" },
  medium: { icon: 34, gap: "gap-ien-xxs", text: "text-ien-label2" },
  large: { icon: 44, gap: "gap-ien-xs", text: "text-ien-label1" },
  big: { icon: 56, gap: "gap-ien-xs", text: "text-ien-label1" },
};

function Rating({
  value,
  defaultValue = 0,
  onValueChange,
  max = 5,
  readOnly = onValueChange == null,
  disabled = false,
  enabled = true,
  size = "medium",
  variant = "full",
  ariaLabel,
  valueText,
  label,
  className,
  ref,
  ...rootProps
}: RatingProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const [hoverValue, setHoverValue] = React.useState<number | null>(null);
  const buttons = React.useRef<Array<HTMLButtonElement | null>>([]);
  const pointerActive = React.useRef(false);
  const pointerValue = React.useRef<number | null>(null);
  const pointerHandledClick = React.useRef(false);
  const gradientId = React.useId().replaceAll(":", "");
  const count = Number.isFinite(max) ? Math.max(1, Math.floor(max)) : 5;
  const requestedValue = value ?? internalValue;
  const current = Math.min(count, Math.max(0, Number.isFinite(requestedValue) ? requestedValue : 0));
  const interactive = !readOnly && enabled && !disabled && onValueChange != null;
  const displayVariant = readOnly ? variant.toLowerCase() : "full";
  const displayed = interactive ? hoverValue ?? current : current;
  const selectedIndex = Math.min(count - 1, Math.max(0, Math.round(current) - 1));
  const currentText = valueText ?? `${current % 1 === 0 ? current : current.toFixed(1)} / ${count}`;
  const resolvedLabel = ariaLabel ?? label ?? (readOnly ? "현재 별점" : "별점 선택");
  const dimensions = sizeStyles[size];

  const set = (next: number) => {
    if (!interactive) return;
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
  };

  const handleKeyDown = (
    event: React.KeyboardEvent<HTMLButtonElement>,
    rating: number,
  ) => {
    if (!interactive) return;
    let next: number | undefined;
    if (event.key === "ArrowRight" || event.key === "ArrowUp") next = Math.min(count, rating + 1);
    else if (event.key === "ArrowLeft" || event.key === "ArrowDown") next = Math.max(1, rating - 1);
    else if (event.key === "Home") next = 1;
    else if (event.key === "End") next = count;
    if (next === undefined) return;
    event.preventDefault();
    set(next);
    buttons.current[next - 1]?.focus();
  };

  const updateFromPointer = (event: React.PointerEvent<HTMLDivElement>) => {
    const bounds = event.currentTarget.getBoundingClientRect();
    if (bounds.width <= 0) return;
    const progress = Math.max(0, Math.min(0.999999, (event.clientX - bounds.left) / bounds.width));
    const next = Math.floor(progress * count) + 1;
    setHoverValue(next);
    if (pointerValue.current !== next) {
      pointerValue.current = next;
      set(next);
    }
  };

  const renderStar = (index: number, rating: number, active: boolean, fillOverride?: number) => {
    const fill = Math.max(0, Math.min(1, fillOverride ?? displayed - index));
    const isDisabled = disabled || !enabled;
    return (
      <button
        key={`${index}-${rating}`}
        ref={(element) => {
          buttons.current[index] = element;
        }}
        type="button"
        role={interactive ? "radio" : undefined}
        aria-checked={interactive ? rating === current : undefined}
        aria-disabled={!interactive || undefined}
        aria-label={`${rating} / ${count}`}
        tabIndex={interactive ? (rating === (current === 0 ? 1 : current) ? 0 : -1) : -1}
        onClick={() => {
          if (pointerHandledClick.current) {
            pointerHandledClick.current = false;
            return;
          }
          set(rating);
        }}
        onKeyDown={(event) => handleKeyDown(event, rating)}
        onMouseEnter={() => {
          if (interactive) setHoverValue(rating);
        }}
        onMouseLeave={() => setHoverValue(null)}
        className={cn(
          "relative grid shrink-0 place-items-center rounded-ien-full outline-none transition-transform duration-[var(--ien-motion-fast)] focus-visible:ring-2 focus-visible:ring-ien-brand motion-reduce:transition-none",
          interactive && "cursor-pointer active:scale-[0.86]",
          !interactive && "cursor-default",
        )}
        style={{ width: dimensions.icon, height: dimensions.icon }}
      >
        <svg
          aria-hidden="true"
          viewBox="0 0 24 24"
          className="size-full overflow-visible"
          style={{ opacity: isDisabled ? 0.42 : fill > 0 ? 1 : 0.72 }}
        >
          <defs>
            <linearGradient id={`${gradientId}-${index}`} x1="0" y1="0" x2="1" y2="1">
              <stop offset="0%" stopColor="color-mix(in srgb, var(--ien-brand), var(--ien-on-brand) 18%)" />
              <stop offset="52%" stopColor="var(--ien-brand)" />
              <stop offset="100%" stopColor="color-mix(in srgb, var(--ien-brand), var(--ien-on-brand) 36%)" />
            </linearGradient>
            <clipPath id={`${gradientId}-clip-${index}`}>
              <rect x="0" y="0" width={`${fill * 24}`} height="24" />
            </clipPath>
          </defs>
          <path
            d="M12 2.5 14.93 8.44l6.56.95-4.75 4.63 1.12 6.54L12 17.47l-5.86 3.09 1.12-6.54-4.75-4.63 6.56-.95L12 2.5Z"
            fill="none"
            stroke="var(--ien-border-strong)"
            strokeWidth="1.7"
            strokeLinejoin="round"
          />
          {fill > 0 && (
            <path
              d="M12 2.5 14.93 8.44l6.56.95-4.75 4.63 1.12 6.54L12 17.47l-5.86 3.09 1.12-6.54-4.75-4.63 6.56-.95L12 2.5Z"
              fill={`url(#${gradientId}-${index})`}
              stroke={`url(#${gradientId}-${index})`}
              strokeWidth="1.2"
              strokeLinejoin="round"
              clipPath={`url(#${gradientId}-clip-${index})`}
              className="transition-[fill] duration-[var(--ien-motion-fast)] motion-reduce:transition-none"
            />
          )}
        </svg>
        {interactive && (
          <span className="sr-only">{active ? "선택됨" : ""}</span>
        )}
      </button>
    );
  };

  return (
    <div
      {...rootProps}
      ref={ref}
      role={interactive ? "radiogroup" : undefined}
      aria-label={resolvedLabel}
      aria-valuetext={currentText}
      aria-disabled={!enabled || disabled || undefined}
      onPointerDown={(event) => {
        if (!interactive || event.button !== 0) return;
        pointerActive.current = true;
        pointerHandledClick.current = true;
        pointerValue.current = null;
        event.currentTarget.setPointerCapture(event.pointerId);
        updateFromPointer(event);
      }}
      onPointerMove={(event) => {
        if (pointerActive.current) updateFromPointer(event);
      }}
      onPointerUp={() => {
        pointerActive.current = false;
      }}
      onPointerCancel={() => {
        pointerActive.current = false;
        pointerHandledClick.current = false;
        pointerValue.current = null;
      }}
      onPointerLeave={() => {
        if (!pointerActive.current) setHoverValue(null);
      }}
      data-slot="rating"
      data-size={size}
      data-variant={displayVariant}
      className={cn("relative inline-flex items-center", dimensions.gap, className)}
    >
      {displayVariant === "full" &&
        Array.from({ length: count }, (_, index) => renderStar(index, index + 1, index + 1 <= displayed))}
      {displayVariant === "compact" && (
        <>
          {renderStar(0, 1, true, 1)}
          <span className={cn("text-ien-text-secondary", dimensions.text)}>{currentText}</span>
        </>
      )}
      {displayVariant === "icononly" && renderStar(selectedIndex, selectedIndex + 1, true, current / count)}
      <span className="sr-only">{currentText}</span>
    </div>
  );
}

export { Rating, Rating as IenRating };
