"use client";

import * as React from "react";
import { cn } from "@/lib/ien-utils";

export type SegmentedControlSize = "small" | "large";
export type SegmentedControlAlignment = "fixed" | "fluid";

export type SegmentedControlOption<T = string> = {
  value: T | null;
  label: React.ReactNode;
  enabled?: boolean;
  disabled?: boolean;
  size?: SegmentedControlSize;
};

export type SegmentedControlProps<T = string> = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "onChange"
> & {
  ref?: React.Ref<HTMLDivElement>;
  options: SegmentedControlOption<T>[];
  value?: T | null;
  defaultValue?: T | null;
  onValueChange?: (value: T | null) => void;
  onChange?: (value: T | null) => void;
  size?: SegmentedControlSize;
  alignment?: SegmentedControlAlignment;
  isFluid?: boolean;
  isValueControlled?: boolean;
  fullWidth?: boolean;
  enabled?: boolean;
  disabled?: boolean;
};

function SegmentedControl<T = string>({
  options,
  value,
  defaultValue,
  onValueChange,
  onChange,
  size = "small",
  alignment = "fixed",
  isFluid,
  isValueControlled,
  fullWidth,
  enabled = true,
  disabled = false,
  className,
  ref,
  ...rootProps
}: SegmentedControlProps<T>) {
  const fluid = isFluid ?? alignment === "fluid";
  const interactionDisabled = disabled || !enabled;
  const controlled = isValueControlled ?? (value !== null && value !== undefined);
  const initialValue = React.useMemo(() => {
    if (defaultValue !== undefined) return defaultValue;
    const firstEnabled = options.find(
      (option) => option.enabled !== false && !option.disabled,
    );
    return firstEnabled ? firstEnabled.value : (options[0]?.value ?? null);
  }, [defaultValue, options]);
  const [localValue, setLocalValue] = React.useState<T | null>(initialValue);
  React.useEffect(() => {
    if (!controlled) setLocalValue(initialValue);
  }, [controlled, initialValue]);
  const selectedValue = controlled ? (value ?? null) : localValue;
  const viewportRef = React.useRef<HTMLDivElement | null>(null);
  const trackRef = React.useRef<HTMLDivElement | null>(null);
  const optionRefs = React.useRef<Array<HTMLButtonElement | null>>([]);
  const [indicator, setIndicator] = React.useState<{ x: number; width: number } | null>(null);
  const selectedIndex = options.findIndex((option) => option.value === selectedValue);

  const measureIndicator = React.useCallback(() => {
    const selected = optionRefs.current[selectedIndex];
    const track = trackRef.current;
    if (!selected || !track) {
      setIndicator(null);
      return;
    }
    setIndicator({ x: selected.offsetLeft, width: selected.offsetWidth });
  }, [selectedIndex]);

  React.useLayoutEffect(() => {
    measureIndicator();
    const observer = new ResizeObserver(measureIndicator);
    if (trackRef.current) observer.observe(trackRef.current);
    optionRefs.current.forEach((option) => option && observer.observe(option));
    return () => observer.disconnect();
  }, [measureIndicator, options, size]);

  React.useEffect(() => {
    if (!fluid || selectedIndex < 0) return;
    const option = optionRefs.current[selectedIndex];
    const viewport = viewportRef.current;
    if (!option || !viewport) return;
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    option.scrollIntoView({
      behavior: reduceMotion ? "auto" : "smooth",
      block: "nearest",
      inline: "center",
    });
  }, [fluid, selectedIndex]);

  const selectOption = React.useCallback(
    (index: number) => {
      const option = options[index];
      if (!option || interactionDisabled || option.disabled || option.enabled === false) return;
      if (option.value === selectedValue) return;
      if (!controlled) setLocalValue(option.value);
      onValueChange?.(option.value);
      onChange?.(option.value);
    },
    [controlled, interactionDisabled, onChange, onValueChange, options, selectedValue],
  );
  const firstEnabledIndex = options.findIndex(
    (option) => option.enabled !== false && !option.disabled && !interactionDisabled,
  );

  const focusEnabledOption = (from: number, direction: 1 | -1) => {
    for (let step = 1; step <= options.length; step += 1) {
      const index = (from + direction * step + options.length) % options.length;
      const option = options[index];
      if (option && option.enabled !== false && !option.disabled && !interactionDisabled) {
        optionRefs.current[index]?.focus();
        selectOption(index);
        return;
      }
    }
  };
  React.useImperativeHandle(ref, () => viewportRef.current!, [ref]);

  return (
    <div
      {...rootProps}
      ref={viewportRef}
      role="radiogroup"
      aria-orientation="horizontal"
      data-slot="segmented-control"
      data-alignment={fluid ? "fluid" : "fixed"}
      aria-disabled={interactionDisabled || undefined}
      className={cn(
        "max-w-full overflow-x-auto rounded-ien-full bg-[#f2f4f6] p-ien-xxs dark:bg-[#20252b]",
        !fluid && fullWidth !== false && "w-full",
        className,
      )}
    >
      <div
        ref={trackRef}
        className={cn(
          "relative flex min-w-full items-center gap-ien-xxs",
          fluid && "w-max",
        )}
      >
        {indicator && (
          <span
            aria-hidden="true"
            className="pointer-events-none absolute inset-y-0 left-0 rounded-ien-full bg-white shadow-[0_var(--ien-elevation-raised)_calc(var(--ien-elevation-raised)*2)_rgb(0_0_0/0.12)] transition-[transform,width] duration-[var(--ien-motion-spring-duration)] [transition-timing-function:var(--ien-motion-spring-medium-bouncy)] motion-reduce:transition-none dark:bg-[#343a42]"
            style={{
              width: indicator.width,
              transform: `translateX(${indicator.x}px)`,
            }}
          />
        )}
        {options.map((option, index) => {
          const optionDisabled = interactionDisabled || option.disabled || option.enabled === false;
          const selected = option.value === selectedValue;
          const itemSize = option.size ?? size;

          return (
            <button
              key={`${String(option.value)}-${index}`}
              ref={(node) => {
                optionRefs.current[index] = node;
              }}
              type="button"
              role="radio"
              aria-checked={selected}
              disabled={optionDisabled}
              tabIndex={
                selected || (selectedIndex < 0 && index === firstEnabledIndex) ? 0 : -1
              }
              onClick={() => selectOption(index)}
              onKeyDown={(event) => {
                if (event.key === "ArrowRight") {
                  event.preventDefault();
                  focusEnabledOption(index, 1);
                } else if (event.key === "ArrowLeft") {
                  event.preventDefault();
                  focusEnabledOption(index, -1);
                } else if (event.key === "Home") {
                  event.preventDefault();
                  const first = options.findIndex(
                      (item) => item.enabled !== false && !item.disabled && !interactionDisabled,
                  );
                  if (first >= 0) {
                    optionRefs.current[first]?.focus();
                    selectOption(first);
                  }
                } else if (event.key === "End") {
                  event.preventDefault();
                  let last = -1;
                  for (let optionIndex = options.length - 1; optionIndex >= 0; optionIndex -= 1) {
                    const item = options[optionIndex];
                    if (item && item.enabled !== false && !item.disabled && !interactionDisabled) {
                      last = optionIndex;
                      break;
                    }
                  }
                  if (last >= 0) {
                    optionRefs.current[last]?.focus();
                    selectOption(last);
                  }
                }
              }}
              className={cn(
                "relative z-10 flex shrink-0 items-center justify-center whitespace-nowrap rounded-ien-full px-ien-sm text-ien-text-secondary transition-[color,scale] duration-[var(--ien-motion-fast)] active:scale-[0.92] focus-visible:outline-none focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 disabled:cursor-not-allowed disabled:opacity-[var(--ien-state-disabled-alpha)]",
                !fluid && "min-w-0 flex-1",
                fluid && (itemSize === "small" ? "min-w-16" : "min-w-20"),
                itemSize === "small"
                  ? "min-h-8 px-ien-sm text-ien-label2"
                  : "min-h-10 px-ien-md text-ien-label1",
                selected && "text-ien-text-primary",
              )}
            >
              {option.label}
            </button>
          );
        })}
      </div>
    </div>
  );
}

export { SegmentedControl, SegmentedControl as IenSegmentedControl };
