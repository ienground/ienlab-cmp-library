"use client";

import * as React from "react";
import { Check } from "lucide-react";
import { cn } from "@/lib/ien-utils";

export type StepperRange = { min?: number; max?: number; step?: number };
export type StepperStep = {
  label?: React.ReactNode;
  title?: React.ReactNode;
  description?: React.ReactNode;
  left?: React.ReactNode;
  right?: React.ReactNode;
  hideLine?: boolean;
  disabled?: boolean;
};
export type StepperProps = Omit<
  React.ComponentProps<"ol">,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLOListElement>;
  steps?: StepperStep[];
  currentStep?: number;
  defaultStep?: number;
  onStepChange?: (step: number) => void;
  orientation?: "horizontal" | "vertical";
  allowNavigation?: boolean;
  value?: number;
  defaultValue?: number;
  onValueChange?: (value: number) => void;
  range?: StepperRange;
  enabled?: boolean;
  label?: string;
  play?: boolean;
  delay?: number;
  staggerDelay?: number;
  className?: string;
};

function Stepper({
  steps = [],
  currentStep,
  defaultStep = 0,
  onStepChange,
  orientation = "horizontal",
  allowNavigation = false,
  value,
  defaultValue = 0,
  onValueChange,
  range = {},
  enabled = true,
  label,
  play = true,
  delay = 0,
  staggerDelay = 0.1,
  className,
  ref,
  "aria-label": ariaLabel,
  ...listProps
}: StepperProps) {
  const [internalStep, setInternalStep] = React.useState(defaultStep);
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const [visibleSteps, setVisibleSteps] = React.useState<boolean[]>(() =>
    Array(steps.length).fill(!play),
  );
  const isValueMode = value !== undefined || onValueChange !== undefined;
  const min = range.min ?? -2_147_483_648;
  const max = range.max ?? 2_147_483_647;
  const stepSize = range.step ?? 1;
  if (
    !Number.isInteger(min) ||
    !Number.isInteger(max) ||
    !Number.isInteger(stepSize) ||
    min > max ||
    stepSize <= 0
  ) {
    throw new Error("range는 정수 범위와 0보다 큰 step을 사용해야 합니다.");
  }
  const animationDelay = Number.isFinite(delay) ? Math.max(0, delay) : 0;
  const animationStagger = Number.isFinite(staggerDelay)
    ? Math.max(0, staggerDelay)
    : 0;

  React.useEffect(() => {
    if (
      !play ||
      (typeof window.matchMedia === "function" &&
        window.matchMedia("(prefers-reduced-motion: reduce)").matches)
    ) {
      setVisibleSteps(Array(steps.length).fill(true));
      return;
    }
    setVisibleSteps(Array(steps.length).fill(false));
    const timers = steps.map((_, index) =>
      window.setTimeout(
        () => setVisibleSteps((current) => current.map((shown, i) => shown || i === index)),
        animationDelay * 1000 + index * animationStagger * 1000,
      ),
    );
    return () => timers.forEach(window.clearTimeout);
  }, [animationDelay, animationStagger, play, steps.length]);

  const activeStep = Math.min(
    Math.max(currentStep ?? internalStep, 0),
    Math.max(steps.length - 1, 0),
  );
  const select = (index: number) => {
    if (!allowNavigation || !enabled || steps[index]?.disabled) return;
    if (currentStep === undefined) setInternalStep(index);
    onStepChange?.(index);
  };
  const currentValue = Math.min(Math.max(value ?? internalValue, min), max);
  const changeValue = (delta: number) => {
    if (!enabled) return;
    const next = Math.min(Math.max(currentValue + delta, min), max);
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
  };

  if (isValueMode) {
    return (
      <ol
        {...listProps}
        ref={ref}
        aria-label={ariaLabel ?? label ?? "값 조절"}
        aria-disabled={!enabled || undefined}
        className={cn("flex list-none items-center gap-ien-sm p-0", className)}
        role="group"
      >
        <li className="flex w-full list-none items-center gap-ien-sm">
          {label && <span className="min-w-0 flex-1 text-ien-body2">{label}</span>}
          <button
            type="button"
            aria-label="값 감소"
            disabled={!enabled || currentValue <= min}
            onClick={() => changeValue(-stepSize)}
            className="grid size-[var(--ien-state-minimum-touch-target)] place-items-center rounded-ien-full border border-ien-border bg-ien-brand-weak text-ien-title3 text-ien-brand disabled:bg-ien-surface-weak disabled:text-ien-text-disabled"
          >
            −
          </button>
          <output aria-live="polite" className="min-w-[2ch] text-center text-ien-label1">
            {currentValue}
          </output>
          <button
            type="button"
            aria-label="값 증가"
            disabled={!enabled || currentValue >= max}
            onClick={() => changeValue(stepSize)}
            className="grid size-[var(--ien-state-minimum-touch-target)] place-items-center rounded-ien-full border border-ien-border bg-ien-brand-weak text-ien-title3 text-ien-brand disabled:bg-ien-surface-weak disabled:text-ien-text-disabled"
          >
            +
          </button>
        </li>
      </ol>
    );
  }

  return (
    <ol
      {...listProps}
      ref={ref}
      aria-label={ariaLabel ?? label ?? "진행 단계"}
      aria-disabled={!enabled || undefined}
      className={cn(
        "flex gap-ien-sm",
        orientation === "vertical" ? "flex-col" : "items-start",
        className,
      )}
    >
      {steps.map((step, index) => {
        const done = index < activeStep;
        const current = index === activeStep;
        const interactive = allowNavigation && enabled && !step.disabled;
        const title = step.title ?? step.label;
        const marker = (
          <span
            className={cn(
              "grid size-7 shrink-0 place-items-center rounded-ien-full border text-ien-label2",
              done
                ? "border-ien-brand bg-ien-brand text-ien-on-brand"
                : current
                  ? "border-ien-brand bg-ien-brand-weak text-ien-brand"
                  : "border-ien-border-strong text-ien-text-secondary",
            )}
          >
            {done ? <Check aria-hidden="true" className="size-4" /> : index + 1}
          </span>
        );
        const timelineContent = (
          <div className="flex w-full items-start gap-ien-md text-left">
            <div className="flex w-10 shrink-0 flex-col items-center">
              <div className="grid size-10 place-items-center">{step.left ?? marker}</div>
              {!step.hideLine && index < steps.length - 1 && (
                <span
                  aria-hidden="true"
                  className="mt-[calc(var(--ien-spacing-xxs)+var(--ien-spacing-xxxs))] h-7 w-[var(--ien-stroke-thick)] rounded-ien-full bg-ien-border"
                  style={{ opacity: 0.85 }}
                />
              )}
            </div>
            <div className="grid min-w-0 flex-1 gap-[var(--ien-spacing-xxs)] pb-ien-md pt-ien-xxxs">
              <span className={cn("text-ien-label1", current ? "text-ien-text-primary" : "text-ien-text-secondary")}>
                {title}
              </span>
              {step.description && (
                <span className="text-ien-body2 text-ien-text-secondary">
                  {step.description}
                </span>
              )}
            </div>
            {step.right && (
              <span className="mt-ien-xxxs flex h-10 shrink-0 items-center">
                {step.right}
              </span>
            )}
          </div>
        );
        const horizontalContent = (
          <div className="flex min-w-0 items-start gap-ien-xs text-left">
            {step.left ?? marker}
            <span className="grid min-w-0 gap-[var(--ien-spacing-xxxs)]">
              <span className={cn("text-ien-label2", current ? "text-ien-text-primary" : "text-ien-text-secondary")}>
                {title}
              </span>
              {step.description && <span className="text-ien-caption text-ien-text-tertiary">{step.description}</span>}
            </span>
            {step.right}
          </div>
        );
        const content = orientation === "vertical" ? timelineContent : horizontalContent;
        return (
          <li
            key={index}
            aria-current={current ? "step" : undefined}
            className={cn(
              "transition-[opacity,translate] duration-[var(--ien-motion-normal)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
              orientation === "vertical" && "min-h-[72px] w-full py-ien-xxs",
              orientation === "horizontal" && "flex gap-ien-xs",
              orientation === "horizontal" && "flex-1",
            )}
            style={{
              opacity: visibleSteps[index] ? 1 : 0,
              translate: visibleSteps[index] ? "0 0" : "0 20%",
            }}
          >
            {allowNavigation ? (
              <button
                type="button"
                onClick={() => select(index)}
                disabled={!interactive}
                className={cn(
                  "text-left disabled:cursor-not-allowed disabled:opacity-50",
                  orientation === "vertical" ? "w-full" : "flex items-start gap-ien-xs",
                )}
              >
                {content}
              </button>
            ) : content}
            {index < steps.length - 1 && orientation === "horizontal" && (
              <span aria-hidden="true" className="mt-ien-md h-px min-w-3 flex-1 bg-ien-border" />
            )}
          </li>
        );
      })}
    </ol>
  );
}

export { Stepper, Stepper as IenStepper };
