import * as React from "react";
import { Check } from "lucide-react";
import { cn } from "@/lib/ien-utils";

export type IenStepStatus = "pending" | "current" | "done" | "error";
export type IenProgressStepperVariant = "compact" | "icon";
export type IenProgressStepperPaddingTop = "default" | "wide";

export interface IenProgressStep {
  title?: string;
  status?: IenStepStatus;
  icon?: React.ReactNode;
}

export interface IenProgressStepperProps extends React.ComponentPropsWithRef<"ol"> {
  steps: readonly IenProgressStep[];
  variant?: IenProgressStepperVariant;
  paddingTop?: IenProgressStepperPaddingTop;
  activeStepIndex?: number;
  checkForFinish?: boolean;
}

export function IenProgressStepper({
  steps,
  variant = "compact",
  paddingTop = "default",
  activeStepIndex = 0,
  checkForFinish = false,
  className,
  ...props
}: IenProgressStepperProps) {
  const active = Math.max(
    0,
    Math.min(activeStepIndex, Math.max(steps.length - 1, 0)),
  );
  return (
    <ol
      aria-label={`전체 ${steps.length}단계 중 ${steps.length === 0 ? 0 : active + 1}단계`}
      className={cn(
        "flex w-full",
        paddingTop === "wide" ? "pt-ien-xl" : "pt-ien-md",
        className,
      )}
      {...props}
    >
      {steps.map((step, index) => {
        const status =
          step.status ??
          (index < active ? "done" : index === active ? "current" : "pending");
        const statusColor =
          status === "error"
            ? "bg-ien-danger text-ien-on-danger"
            : status === "pending"
              ? "bg-ien-surface-variant text-ien-text-tertiary"
              : "bg-ien-brand text-ien-on-brand";
        return (
          <li
            key={`${step.title ?? "step"}-${index}`}
            className="relative flex min-w-0 flex-1 flex-col items-center text-center"
          >
            {index > 0 && (
              <span
                aria-hidden
                className={cn(
                  "absolute left-0 h-px w-1/2",
                  variant === "icon" ? "top-3.5" : "top-[5px]",
                  index <= active ? "bg-ien-brand bg-[linear-gradient(to_bottom_right,color-mix(in_oklab,var(--ien-brand),var(--ien-on-brand)_18%),var(--ien-brand),color-mix(in_oklab,var(--ien-brand),var(--ien-on-brand)_36%))]" : "bg-ien-border",
                )}
              />
            )}
            {index < steps.length - 1 && (
              <span
                aria-hidden
                className={cn(
                  "absolute right-0 h-px w-1/2",
                  variant === "icon" ? "top-3.5" : "top-[5px]",
                  index < active ? "bg-ien-brand bg-[linear-gradient(to_bottom_right,color-mix(in_oklab,var(--ien-brand),var(--ien-on-brand)_18%),var(--ien-brand),color-mix(in_oklab,var(--ien-brand),var(--ien-on-brand)_36%))]" : "bg-ien-border",
                )}
              />
            )}
            <span
              className={cn(
                "relative z-[1] grid shrink-0 place-items-center rounded-full",
                variant === "icon" ? "size-7" : "size-2.5",
                statusColor,
                status !== "pending" && "bg-[linear-gradient(to_bottom_right,color-mix(in_oklab,var(--ien-step-tone),var(--ien-step-on-tone)_18%),var(--ien-step-tone),color-mix(in_oklab,var(--ien-step-tone),var(--ien-step-on-tone)_36%))]",
              )}
              style={{ "--ien-step-tone": status === "error" ? "var(--ien-danger)" : "var(--ien-brand)", "--ien-step-on-tone": status === "error" ? "var(--ien-on-danger)" : "var(--ien-on-brand)" } as React.CSSProperties}
              aria-current={status === "current" ? "step" : undefined}
            >
              {variant === "icon" &&
                (checkForFinish && index < active ? (
                  <Check aria-hidden className="size-4" />
                ) : (
                  (step.icon ??
                  (status === "current" ? (
                    <span className="size-2 rounded-full bg-current" />
                  ) : null))
                ))}
            </span>
            {step.title != null && (
              <span
                className={cn(
                  "mt-ien-xs max-w-full line-clamp-2 text-ien-caption",
                  status === "pending"
                    ? "text-ien-text-tertiary"
                    : status === "error"
                      ? "text-ien-danger"
                      : status === "current" ? "text-ien-text-primary" : "text-ien-text-secondary",
                )}
              >
                {step.title}
              </span>
            )}
          </li>
        );
      })}
    </ol>
  );
}

export { IenProgressStepper as ProgressStepper };
