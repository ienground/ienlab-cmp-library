"use client";

import * as React from "react";
import { Star } from "lucide-react";
import { cn } from "cn";

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
  label?: string;
  className?: string;
};

function Rating({
  value,
  defaultValue = 0,
  onValueChange,
  max = 5,
  readOnly = false,
  disabled = false,
  label = "별점",
  className,
  ref,
  ...rootProps
}: RatingProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const [hoverValue, setHoverValue] = React.useState<number | null>(null);
  const buttons = React.useRef<Array<HTMLButtonElement | null>>([]);
  const count = Number.isFinite(max) ? Math.max(0, Math.floor(max)) : 5;
  const current = Math.min(count, Math.max(0, value ?? internalValue));
  const displayed = hoverValue ?? current;

  const set = (next: number) => {
    if (readOnly || disabled) return;
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
  };

  const handleKeyDown = (
    event: React.KeyboardEvent<HTMLButtonElement>,
    rating: number,
  ) => {
    if (readOnly || disabled || count === 0) return;
    let next: number | undefined;
    if (event.key === "ArrowRight" || event.key === "ArrowUp")
      next = Math.min(count, rating + 1);
    else if (event.key === "ArrowLeft" || event.key === "ArrowDown")
      next = Math.max(1, rating - 1);
    else if (event.key === "Home") next = 1;
    else if (event.key === "End") next = count;
    if (next === undefined) return;
    event.preventDefault();
    set(next);
    buttons.current[next - 1]?.focus();
  };

  return (
    <div
      {...rootProps}
      ref={ref}
      role="radiogroup"
      aria-label={label}
      className={cn("inline-flex items-center gap-ien-xxs", className)}
    >
      {Array.from({ length: count }, (_, index) => {
        const rating = index + 1;
        const active = rating <= displayed;
        return (
          <button
            key={rating}
            ref={(element) => {
              buttons.current[index] = element;
            }}
            type="button"
            role="radio"
            aria-checked={rating === current}
            aria-disabled={readOnly || disabled || undefined}
            aria-label={rating + " / " + count}
            tabIndex={rating === (current === 0 ? 1 : current) ? 0 : -1}
            disabled={disabled}
            onClick={() => set(rating)}
            onKeyDown={(event) => handleKeyDown(event, rating)}
            onMouseEnter={() => {
              if (!readOnly && !disabled) setHoverValue(rating);
            }}
            onMouseLeave={() => {
              setHoverValue(null);
            }}
            className="grid size-9 place-items-center rounded-ien-full text-ien-warning outline-none transition-colors duration-[var(--ien-motion-fast)] hover:bg-ien-warning-weak focus-visible:ring-2 focus-visible:ring-ien-brand disabled:cursor-default disabled:opacity-60"
          >
            <Star
              aria-hidden="true"
              className={cn(
                "size-5",
                active
                  ? "fill-ien-warning text-ien-warning"
                  : "text-ien-text-tertiary",
              )}
            />
          </button>
        );
      })}
      <span className="sr-only">
        {current} / {count}
      </span>
    </div>
  );
}

export { Rating, Rating as IenRating };
