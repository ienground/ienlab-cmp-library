"use client"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"

import type { HTMLAttributes, TimeHTMLAttributes } from "react"
import { createContext, useCallback, useContext, useState } from "react"
import { Slot } from "radix-ui"

import { cn } from "cn"

// Types
type TimelineContextValue = {
  activeStep: number
  setActiveStep: (step: number) => void
}

// Context
const TimelineContext = createContext<TimelineContextValue | undefined>(
  undefined
)

const useTimeline = () => {
  const context = useContext(TimelineContext)
  if (!context) {
    throw new Error("useTimeline must be used within a Timeline")
  }
  return context
}

// Components
interface TimelineProps extends HTMLAttributes<HTMLDivElement> {
  tone?: IenToneProps["tone"]
  useGradient?: boolean

  defaultValue?: number
  value?: number
  onValueChange?: (value: number) => void
  orientation?: "horizontal" | "vertical"
}

function Timeline({
  tone: ienTone,
  useGradient: ienUseGradient,
  defaultValue = 1,
  value,
  onValueChange,
  orientation = "vertical",
  className,
  children,
  ...props
}: TimelineProps & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const [activeStep, setInternalStep] = useState(defaultValue)

  const setActiveStep = useCallback(
    (step: number) => {
      if (value === undefined) {
        setInternalStep(step)
      }
      onValueChange?.(step)
    },
    [value, onValueChange]
  )

  const currentStep = value ?? activeStep

  return (
    <IenThemeContext.Provider value={ienTheme}>
      <TimelineContext.Provider
      value={{ activeStep: currentStep, setActiveStep }}
    >
        <div
          className={cn(
          "group/timeline flex data-[orientation=horizontal]:w-full data-[orientation=horizontal]:flex-row data-[orientation=vertical]:flex-col",
          className
        )}
          data-orientation={orientation}
          data-slot="timeline"
          data-ien=""
          data-ien-tone={ienTheme.tone}
          data-ien-gradient={ienTheme.useGradient}
          {...props}
        >
          {children}
        </div>
      </TimelineContext.Provider>
    </IenThemeContext.Provider>
  )
}

// TimelineContent
function TimelineContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: HTMLAttributes<HTMLDivElement> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <div
      className={cn("text-muted-foreground text-sm", className)}
      data-slot="timeline-content"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    />
  )
}

// TimelineDate
// TimeHTMLAttributes (not the generic HTMLAttributes) so the <time>-specific `dateTime`
// attribute is accepted — matches the base wrapper's useRender.ComponentProps<"time">.
interface TimelineDateProps extends TimeHTMLAttributes<HTMLTimeElement> {
  tone?: IenToneProps["tone"]
  useGradient?: boolean

  asChild?: boolean
}

function TimelineDate({
  tone: ienTone,
  useGradient: ienUseGradient,
  asChild = false,
  className,
  ...props
}: TimelineDateProps & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const Comp = asChild ? Slot.Root : "time"

  return (
    <Comp
      className={cn(
        "text-muted-foreground mb-1 block text-xs font-medium group-data-[orientation=vertical]/timeline:max-sm:h-4",
        className
      )}
      data-slot="timeline-date"
      data-ien-typography="caption"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    />
  )
}

// TimelineHeader
function TimelineHeader({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: HTMLAttributes<HTMLDivElement> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <div
      className={cn(className)}
      data-slot="timeline-header"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    />
  )
}

// TimelineIndicator
interface TimelineIndicatorProps extends HTMLAttributes<HTMLDivElement> {
  tone?: IenToneProps["tone"]
  useGradient?: boolean

  asChild?: boolean
}

function TimelineIndicator({
  tone: ienTone,
  useGradient: ienUseGradient,
  asChild = false,
  className,
  children,
  ...props
}: TimelineIndicatorProps & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const Comp = asChild ? Slot.Root : "div"

  return (
    <Comp
      aria-hidden="true"
      className={cn(
        "border-primary/20 group-data-completed/timeline-item:border-primary absolute size-4 rounded-full border-2 group-data-[orientation=horizontal]/timeline:-top-6 group-data-[orientation=horizontal]/timeline:left-0 group-data-[orientation=horizontal]/timeline:-translate-y-1/2 group-data-[orientation=vertical]/timeline:top-0 group-data-[orientation=vertical]/timeline:-left-6 group-data-[orientation=vertical]/timeline:-translate-x-1/2",
        className
      )}
      data-slot="timeline-indicator"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    >
      {children}
    </Comp>
  )
}

// TimelineItem
interface TimelineItemProps extends HTMLAttributes<HTMLDivElement> {
  tone?: IenToneProps["tone"]
  useGradient?: boolean

  step: number
}

function TimelineItem({
  tone: ienTone,
  useGradient: ienUseGradient,
  step,
  className,
  ...props
}: TimelineItemProps & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const { activeStep } = useTimeline()

  return (
    <div
      className={cn(
        "group/timeline-item has-[+[data-completed]]:**:data-[slot=timeline-separator]:bg-primary relative flex flex-1 flex-col gap-0.5 group-data-[orientation=horizontal]/timeline:mt-8 group-data-[orientation=horizontal]/timeline:not-last:pe-8 group-data-[orientation=vertical]/timeline:ms-8 group-data-[orientation=vertical]/timeline:not-last:pb-6",
        className
      )}
      data-completed={step <= activeStep || undefined}
      data-slot="timeline-item"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    />
  )
}

// TimelineSeparator
function TimelineSeparator({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: HTMLAttributes<HTMLDivElement> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <div
      aria-hidden="true"
      className={cn(
        "bg-primary/10 absolute self-start group-last/timeline-item:hidden group-data-[orientation=horizontal]/timeline:-top-6 group-data-[orientation=horizontal]/timeline:h-0.5 group-data-[orientation=horizontal]/timeline:w-[calc(100%-1rem-0.25rem)] group-data-[orientation=horizontal]/timeline:translate-x-4.5 group-data-[orientation=horizontal]/timeline:-translate-y-1/2 group-data-[orientation=vertical]/timeline:-left-6 group-data-[orientation=vertical]/timeline:h-[calc(100%-1rem-0.25rem)] group-data-[orientation=vertical]/timeline:w-0.5 group-data-[orientation=vertical]/timeline:-translate-x-1/2 group-data-[orientation=vertical]/timeline:translate-y-4.5",
        className
      )}
      data-slot="timeline-separator"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    />
  )
}

// TimelineTitle
function TimelineTitle({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: HTMLAttributes<HTMLHeadingElement> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <h3
      className={cn("text-sm font-medium", className)}
      data-slot="timeline-title"
      data-ien-typography="title3"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      {...props}
    />
  )
}

export {
  Timeline,
  TimelineContent,
  TimelineDate,
  TimelineHeader,
  TimelineIndicator,
  TimelineItem,
  TimelineSeparator,
  TimelineTitle,
}
