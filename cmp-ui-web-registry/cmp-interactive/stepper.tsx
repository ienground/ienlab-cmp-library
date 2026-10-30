"use client";

import * as React from "react";
import { Check } from "lucide-react";
import { cn } from "cn";

export type StepperStep = {
  label: React.ReactNode;
  description?: React.ReactNode;
  disabled?: boolean;
};
export type StepperProps = Omit<
  React.ComponentProps<"ol">,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLOListElement>;
  steps: StepperStep[];
  currentStep?: number;
  defaultStep?: number;
  onStepChange?: (step: number) => void;
  orientation?: "horizontal" | "vertical";
  allowNavigation?: boolean;
  className?: string;
};

function Stepper({
  steps,
  currentStep,
  defaultStep = 0,
  onStepChange,
  orientation = "horizontal",
  allowNavigation = false,
  className,
  ref,
  "aria-label": ariaLabel,
  ...listProps
}: StepperProps) {
  const [internalStep, setInternalStep] = React.useState(defaultStep);
  const activeStep = Math.min(
    Math.max(currentStep ?? internalStep, 0),
    Math.max(steps.length - 1, 0),
  );
  const select = (index: number) => {
    if (!allowNavigation || steps[index].disabled) return;
    if (currentStep === undefined) setInternalStep(index);
    onStepChange?.(index);
  };
  return (
    <ol
      {...listProps}
      ref={ref}
      aria-label={ariaLabel ?? "진행 단계"}
      className={cn(
        "flex gap-ien-sm",
        orientation === "vertical" ? "flex-col" : "items-start",
        className,
      )}
    >
      {steps.map((step, index) => {
        const done = index < activeStep;
        const current = index === activeStep;
        const content = (
          <>
            <span
              className={cn(
                "grid size-8 shrink-0 place-items-center rounded-ien-full border text-ien-label2",
                done
                  ? "border-ien-brand bg-ien-brand text-ien-on-brand"
                  : current
                    ? "border-ien-brand bg-ien-brand-weak text-ien-on-brand-weak"
                    : "border-ien-border-strong text-ien-text-secondary",
              )}
            >
              {done ? (
                <Check aria-hidden="true" className="size-4" />
              ) : (
                index + 1
              )}
            </span>
            <span className="grid gap-[var(--ien-spacing-xxxs)] text-left">
              <span
                className={cn(
                  "text-ien-label2",
                  current ? "text-ien-text-primary" : "text-ien-text-secondary",
                )}
              >
                {step.label}
              </span>
              {step.description && (
                <span className="text-ien-caption text-ien-text-tertiary">
                  {step.description}
                </span>
              )}
            </span>
          </>
        );
        return (
          <li
            key={index}
            aria-current={current ? "step" : undefined}
            className={cn(
              "flex gap-ien-xs",
              orientation === "horizontal" && "flex-1",
            )}
          >
            {allowNavigation ? (
              <button
                type="button"
                onClick={() => select(index)}
                disabled={step.disabled}
                className="flex items-start gap-ien-xs text-left disabled:cursor-not-allowed disabled:opacity-50"
              >
                {content}
              </button>
            ) : (
              <div className="flex items-start gap-ien-xs">{content}</div>
            )}
            {index < steps.length - 1 && orientation === "horizontal" && (
              <span
                aria-hidden="true"
                className="mt-ien-md h-px min-w-3 flex-1 bg-ien-border"
              />
            )}
          </li>
        );
      })}
    </ol>
  );
}

export { Stepper, Stepper as IenStepper };
