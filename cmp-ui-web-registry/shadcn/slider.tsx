"use client";

/*
 * MIT License
 *
 * Copyright (c) 2023 shadcn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import * as React from "react";
import { cn } from "@/lib/ien-utils";
import { Slider as SliderPrimitive } from "radix-ui";

type SliderProps = React.ComponentProps<typeof SliderPrimitive.Root> & {
  thumbLabels?: string[];
};
function Slider({
  className,
  defaultValue,
  value,
  min = 0,
  max = 100,
  thumbLabels,
  ...props
}: SliderProps) {
  const values = Array.isArray(value)
    ? value
    : Array.isArray(defaultValue)
      ? defaultValue
      : [min, max];
  return (
    <SliderPrimitive.Root
      data-slot="slider"
      defaultValue={defaultValue}
      value={value}
      min={min}
      max={max}
      className={cn(
        "relative flex w-full touch-none items-center select-none data-disabled:opacity-[var(--ien-state-disabled-alpha)] data-[orientation=vertical]:h-full data-[orientation=vertical]:min-h-40 data-[orientation=vertical]:w-auto data-[orientation=vertical]:flex-col",
        className,
      )}
      {...props}
    >
      <SliderPrimitive.Track
        data-slot="slider-track"
        className="relative grow overflow-hidden rounded-ien-full bg-ien-surface-variant data-[orientation=horizontal]:h-1 data-[orientation=horizontal]:w-full data-[orientation=vertical]:h-full data-[orientation=vertical]:w-1"
      >
        <SliderPrimitive.Range
          data-slot="slider-range"
          className="absolute bg-ien-brand data-[orientation=horizontal]:h-full data-[orientation=vertical]:w-full"
        />
      </SliderPrimitive.Track>
      {values.map((_, index) => (
        <SliderPrimitive.Thumb
          data-slot="slider-thumb"
          key={index}
          aria-label={
            thumbLabels?.[index] ?? props["aria-label"] ?? `값 ${index + 1}`
          }
          className="relative block size-[var(--ien-icon-xs)] shrink-0 rounded-ien-full border-2 border-ien-brand bg-ien-surface-raised ring-ien-brand/50 transition-shadow hover:ring-2 focus-visible:outline-none focus-visible:ring-2 disabled:pointer-events-none disabled:opacity-[var(--ien-state-disabled-alpha)]"
        />
      ))}
    </SliderPrimitive.Root>
  );
}
type IenSliderProps = Omit<
  SliderProps,
  | "value"
  | "defaultValue"
  | "onValueChange"
  | "onValueCommit"
  | "disabled"
  | "thumbLabels"
  | "min"
  | "max"
> & {
  value: number;
  onValueChange: (value: number) => void;
  valueRange?: [number, number];
  steps?: number;
  enabled?: boolean;
  label?: string;
  valueLabel?: string;
  onValueChangeFinished?: () => void;
  sliderClassName?: string;
};
function IenSlider({
  value,
  onValueChange,
  valueRange = [0, 1],
  steps = 0,
  enabled = true,
  label,
  valueLabel,
  onValueChangeFinished,
  className,
  sliderClassName,
  ...props
}: IenSliderProps) {
  const current = Math.min(valueRange[1], Math.max(valueRange[0], value));
  return (
    <div className={cn("flex w-full items-center gap-ien-sm", className)}>
      {label && (
        <span className="min-w-0 flex-[0.8] text-ien-body2">{label}</span>
      )}
      <Slider
        min={valueRange[0]}
        max={valueRange[1]}
        step={steps ? (valueRange[1] - valueRange[0]) / (steps + 1) : undefined}
        value={[current]}
        onValueChange={(values) => onValueChange(values[0] ?? valueRange[0])}
        onValueCommit={onValueChangeFinished}
        disabled={!enabled}
        thumbLabels={[props["aria-label"] ?? label ?? "값"]}
        className={cn("flex-[1.4]", sliderClassName)}
        {...props}
      />
      {valueLabel && (
        <span className="min-w-0 text-right text-ien-label2 text-ien-text-secondary">
          {valueLabel}
        </span>
      )}
    </div>
  );
}
type IenRangeSliderProps = Omit<
  SliderProps,
  | "value"
  | "defaultValue"
  | "onValueChange"
  | "onValueCommit"
  | "disabled"
  | "thumbLabels"
  | "min"
  | "max"
> & {
  value: [number, number];
  onValueChange: (value: [number, number]) => void;
  valueRange?: [number, number];
  steps?: number;
  enabled?: boolean;
  label?: string;
  valueLabel?: string;
  onValueChangeFinished?: () => void;
  sliderClassName?: string;
};
function IenRangeSlider({
  value,
  onValueChange,
  valueRange = [0, 1],
  steps = 0,
  enabled = true,
  label,
  valueLabel,
  onValueChangeFinished,
  className,
  sliderClassName,
  ...props
}: IenRangeSliderProps) {
  const current: [number, number] = [
    Math.max(valueRange[0], Math.min(valueRange[1], value[0])),
    Math.max(valueRange[0], Math.min(valueRange[1], value[1])),
  ];
  return (
    <div className={cn("flex w-full items-center gap-ien-sm", className)}>
      {label && (
        <span className="min-w-0 flex-[0.8] text-ien-body2">{label}</span>
      )}
      <Slider
        min={valueRange[0]}
        max={valueRange[1]}
        step={steps ? (valueRange[1] - valueRange[0]) / (steps + 1) : undefined}
        value={current}
        onValueChange={(values) =>
          onValueChange([
            values[0] ?? valueRange[0],
            values[1] ?? valueRange[1],
          ])
        }
        onValueCommit={onValueChangeFinished}
        disabled={!enabled}
        thumbLabels={[`${label ?? "범위"} 시작값`, `${label ?? "범위"} 끝값`]}
        className={cn("flex-[1.4]", sliderClassName)}
        {...props}
      />
      {valueLabel && (
        <span className="min-w-0 text-right text-ien-label2 text-ien-text-secondary">
          {valueLabel}
        </span>
      )}
    </div>
  );
}
export type { IenSliderProps, IenRangeSliderProps };
export { Slider, IenSlider, IenRangeSlider };
