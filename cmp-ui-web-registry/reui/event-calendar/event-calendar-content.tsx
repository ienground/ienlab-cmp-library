// Title: Event Calendar Content
// Description: Active-view switchboard rendering month, week, day, N-days, or agenda; swappable per view via the components prop.

"use client"
import type { IenToneProps } from "@/lib/ien-theme"
import { useIenTheme } from "@/lib/ien-theme-context"

import { type ComponentType, type HTMLAttributes, type ReactNode } from "react"
import {
  useEventCalendarSelector,
  useEventCalendarViewConfig,
} from "@/components/reui/event-calendar/event-calendar"
import { EventCalendarAgendaView } from "@/components/reui/event-calendar/event-calendar-agenda-view"
import { EventCalendarMonthView } from "@/components/reui/event-calendar/event-calendar-month-view"
import { EventCalendarResourceView } from "@/components/reui/event-calendar/event-calendar-resource-view"
import {
  EventCalendarDaysView,
  EventCalendarDayView,
  EventCalendarWeekView,
} from "@/components/reui/event-calendar/event-calendar-time-grid"
import type { CalendarView } from "@/components/reui/event-calendar/event-calendar-types"
import { Slot } from "radix-ui"

import { cn } from "cn"

const DEFAULT_VIEW_COMPONENTS: Record<CalendarView, ComponentType> = {
  month: EventCalendarMonthView,
  week: EventCalendarWeekView,
  day: EventCalendarDayView,
  days: EventCalendarDaysView,
  agenda: EventCalendarAgendaView,
  resource: EventCalendarResourceView,
}

interface EventCalendarContentProps extends HTMLAttributes<HTMLDivElement> {
  tone?: IenToneProps["tone"]
  useGradient?: boolean

  /** Swap individual view implementations. */
  components?: Partial<Record<CalendarView, ComponentType>>
  /** Replaces the switchboard entirely; read useEventCalendarView() inside. */
  children?: ReactNode
  asChild?: boolean
}

function EventCalendarContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  asChild = false,
  components,
  children,
  ...props
}: EventCalendarContentProps & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const viewConfig = useEventCalendarViewConfig()
  const view = useEventCalendarSelector((state) => state.view)
  const loading = useEventCalendarSelector((state) => state.loading)

  const resolved = {
    ...DEFAULT_VIEW_COMPONENTS,
    ...viewConfig.components,
    ...components,
  }
  // A spread copies keys that hold `undefined`, so `components={{ month: isPro
  // ? ProMonth : undefined }}` would erase the default and render <undefined />.
  const ActiveView = resolved[view] ?? DEFAULT_VIEW_COMPONENTS[view]

  const Comp = asChild ? Slot.Root : "div"

  return (
    <Comp
      data-slot="event-calendar-content"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-view={view}
      data-loading={loading || undefined}
      className={cn(
        "relative flex min-h-0 min-w-0 flex-1 flex-col",
        "data-loading:pointer-events-none data-loading:opacity-60",
        viewConfig.classNames?.content,
        className
      )}
      {...props}
    >
      {children ?? <ActiveView />}
    </Comp>
  )
}

export { DEFAULT_VIEW_COMPONENTS, EventCalendarContent }
export type { EventCalendarContentProps }
