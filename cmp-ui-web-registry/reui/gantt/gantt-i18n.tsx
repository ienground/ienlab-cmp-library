// Title: Gantt I18n
// Description: Default UI texts, date-format strings, and formatter functions for the gantt, fully overridable per key.

import type {
  GanttDateRange,
  GanttScale,
} from "@/components/reui/gantt/gantt-types"
import type { IenLanguage } from "@/lib/ien-theme"
import {
  format,
  isSameMonth,
  isSameYear,
  subMilliseconds,
  type Locale,
} from "date-fns"

interface GanttI18nConfig {
  labels: {
    today: string
    previous: string
    next: string
    addEvent: string
    /** "Add task" hint at the foot of the tree. */
    addTask: string
    allDay: string
    loading: string
    event: string
    events: (count: number) => string
    week: (weekNumber: number) => string
    resources: string
    goToDate: string
    /** Hover hint over empty row space, click-only create. */
    scheduleHint: string
    /** Same hint where dragCreate is on and a drag paints a range. */
    scheduleHintDrag: string
    reorder: string
    /** Scale switcher label ("Timeline scale"). */
    selectView: string
    zoomIn: string
    zoomOut: string
    /** Aria-label of the tree/timeline splitter. */
    resizePanel: string
    /** Aria-label of the off-screen bar chips. */
    jumpToBar: (title: string) => string
    /** Read to screen readers as part of the bar label. */
    progress: (percent: number) => string
    /** Live duration readout on the resize indicator. */
    durationDays: (days: number) => string
    /** Appended to the bar aria-label when its segment is clipped by the range. */
    continues: string
    /** Names the planned (baseline) range in the bar tooltip and aria-label. */
    planned: (rangeLabel: string) => string
    /** Read to screen readers on a zero-duration (milestone) bar. */
    milestone: string
    scales: {
      day: string
      week: string
      month: string
      quarter: string
      year: string
    }
  }
  /** date-fns format strings, applied with the gantt `locale`. */
  formats: {
    monthTitle: string
    dayTitle: string
    timeGutter: string
    eventTime: string
  }
  functions: {
    formatTitle: (
      scale: GanttScale,
      ctx: {
        date: Date
        activeRange: GanttDateRange
        visibleRange: GanttDateRange
        locale?: Locale
      }
    ) => string
    formatEventTime: (
      start: Date,
      end: Date,
      allDay: boolean,
      locale?: Locale
    ) => string
    formatDayRange: (range: GanttDateRange, locale?: Locale) => string
    /** Composes the bar's screen-reader label from its localized parts. */
    formatEventAriaLabel: (parts: {
      title: string
      timeLabel: string
      /** Localized milestone clause, from `labels.milestone`. */
      milestoneLabel?: string
      rowTitle?: string
      progressLabel?: string
      /** Localized planned-range clause, from `labels.planned`. */
      plannedLabel?: string
      continues: boolean
    }) => string
  }
}

const DEFAULT_LABELS: GanttI18nConfig["labels"] = {
  today: "Today",
  previous: "Previous",
  next: "Next",
  addEvent: "Add event",
  addTask: "Add task",
  allDay: "All day",
  loading: "Loading events",
  event: "event",
  events: (count) => (count === 1 ? "1 event" : `${count} events`),
  week: (weekNumber) => `W${weekNumber}`,
  resources: "Resources",
  goToDate: "Go to date",
  scheduleHint: "Click to add a schedule",
  scheduleHintDrag: "Click or drag to add a schedule",
  reorder: "Reorder",
  selectView: "Select view",
  zoomIn: "Zoom in",
  zoomOut: "Zoom out",
  resizePanel: "Resize panel",
  jumpToBar: (title) => `Scroll to "${title}"`,
  progress: (percent) => `${percent}% complete`,
  durationDays: (days) => (days === 1 ? "1 day" : `${days} days`),
  continues: "continues",
  planned: (rangeLabel) => `Planned ${rangeLabel}`,
  milestone: "milestone",
  scales: {
    day: "Day",
    week: "Week",
    month: "Month",
    quarter: "Quarter",
    year: "Year",
  },
}

const DEFAULT_FORMATS: GanttI18nConfig["formats"] = {
  monthTitle: "MMMM yyyy",
  dayTitle: "EEEE, MMMM d, yyyy",
  timeGutter: "h a",
  eventTime: "h:mm a",
}

const KO_LABELS: GanttI18nConfig["labels"] = {
  ...DEFAULT_LABELS,
  today: "오늘",
  previous: "이전",
  next: "다음",
  addEvent: "일정 추가",
  addTask: "작업 추가",
  allDay: "하루 종일",
  loading: "일정을 불러오는 중",
  event: "일정",
  events: (count) => `${count}개 일정`,
  week: (weekNumber) => `${weekNumber}주`,
  resources: "리소스",
  goToDate: "날짜로 이동",
  scheduleHint: "클릭하여 일정 추가",
  scheduleHintDrag: "클릭하거나 끌어서 일정 추가",
  reorder: "순서 변경",
  selectView: "보기 선택",
  zoomIn: "확대",
  zoomOut: "축소",
  resizePanel: "패널 크기 조절",
  jumpToBar: (title) => `"${title}" 일정으로 이동`,
  progress: (percent) => `${percent}% 완료`,
  durationDays: (days) => `${days}일`,
  continues: "계속됨",
  planned: (rangeLabel) => `예정 ${rangeLabel}`,
  milestone: "마일스톤",
  scales: { day: "일", week: "주", month: "월", quarter: "분기", year: "년" },
}

const KO_FORMATS: GanttI18nConfig["formats"] = {
  ...DEFAULT_FORMATS,
  monthTitle: "yyyy년 M월",
  dayTitle: "yyyy년 M월 d일 EEEE",
  timeGutter: "a h시",
  eventTime: "a h:mm",
}

/**
 * Default formatting functions BOUND to a config's labels/formats, so that
 * `formats` overrides flow into the default renderers (a consumer overriding
 * formats.eventTime without replacing formatEventTime still sees it applied).
 */
function makeDefaultGanttFunctions(
  cfg: Pick<GanttI18nConfig, "labels" | "formats">
): GanttI18nConfig["functions"] {
  return {
    formatTitle: (scale, { date, activeRange, locale }) => {
      const opts = { locale }
      if (scale === "day") {
        return format(date, cfg.formats.dayTitle, opts)
      }
      if (scale === "month") {
        return format(date, cfg.formats.monthTitle, opts)
      }
      if (scale === "quarter") {
        return format(date, "QQQ yyyy", opts)
      }
      if (scale === "year") {
        return format(date, "yyyy", opts)
      }
      // week: smart range label, last day is activeRange.end - 1ms.
      // subMilliseconds keeps the zoned date type (a plain new Date(ms)
      // would flip the label to the machine zone near midnight)
      const rangeEnd = subMilliseconds(activeRange.end, 1)
      const start = activeRange.start
      if (isSameMonth(start, rangeEnd)) {
        return `${format(start, "MMMM d", opts)} - ${format(rangeEnd, "d, yyyy", opts)}`
      }
      if (isSameYear(start, rangeEnd)) {
        return `${format(start, "MMM d", opts)} - ${format(rangeEnd, "MMM d, yyyy", opts)}`
      }
      return `${format(start, "MMM d, yyyy", opts)} - ${format(rangeEnd, "MMM d, yyyy", opts)}`
    },
    formatEventTime: (start, end, allDay, locale) => {
      const opts = { locale }
      if (end.getTime() === start.getTime()) {
        // a milestone is an instant, not a range - "9:00 AM - 9:00 AM" reads
        // like a data bug
        return allDay
          ? format(start, "MMM d, yyyy", opts)
          : format(start, `MMM d, ${cfg.formats.eventTime}`, opts)
      }
      if (allDay) {
        // a gantt bar is a DATE RANGE: show it, never a bare "All day".
        // Ends are exclusive midnights, so the last shown day is end - 1ms;
        // subMilliseconds keeps the caller's zoned date type intact.
        const last =
          end.getTime() - 1 >= start.getTime() ? subMilliseconds(end, 1) : start
        const sameDay =
          format(start, "yyyy-MM-dd") === format(last, "yyyy-MM-dd")
        if (sameDay) return format(start, "MMM d, yyyy", opts)
        if (isSameYear(start, last)) {
          return `${format(start, "MMM d", opts)} - ${format(last, "MMM d, yyyy", opts)}`
        }
        return `${format(start, "MMM d, yyyy", opts)} - ${format(last, "MMM d, yyyy", opts)}`
      }
      const fmt = cfg.formats.eventTime
      // Multi-day timed events carry the date on both sides. Compare calendar
      // days off the last rendered instant (end is exclusive, so a 14:00 to
      // midnight bar still ends on the start day). Elapsed ms would miss an
      // exactly-24h bar and a DST day that only runs 23 hours.
      const lastInstant =
        end.getTime() - 1 >= start.getTime() ? subMilliseconds(end, 1) : start
      if (format(start, "yyyy-MM-dd") !== format(lastInstant, "yyyy-MM-dd")) {
        return `${format(start, `MMM d, ${fmt}`, opts)} - ${format(end, `MMM d, ${fmt}`, opts)}`
      }
      return `${format(start, fmt, opts)} - ${format(end, fmt, opts)}`
    },
    formatDayRange: (range, locale) => {
      const opts = { locale }
      const rangeEnd = subMilliseconds(range.end, 1)
      return `${format(range.start, "MMM d", opts)} - ${format(rangeEnd, "MMM d", opts)}`
    },
    formatEventAriaLabel: ({
      title,
      timeLabel,
      milestoneLabel,
      rowTitle,
      progressLabel,
      plannedLabel,
      continues,
    }) =>
      [
        title,
        timeLabel,
        milestoneLabel,
        rowTitle,
        progressLabel,
        plannedLabel,
        continues ? cfg.labels.continues : undefined,
      ]
        .filter(Boolean)
        .join(", "),
  }
}

const DEFAULT_GANTT_I18N: GanttI18nConfig = {
  labels: DEFAULT_LABELS,
  formats: DEFAULT_FORMATS,
  functions: makeDefaultGanttFunctions({
    labels: DEFAULT_LABELS,
    formats: DEFAULT_FORMATS,
  }),
}
const KO_GANTT_I18N: GanttI18nConfig = {
  labels: KO_LABELS,
  formats: KO_FORMATS,
  functions: makeDefaultGanttFunctions({ labels: KO_LABELS, formats: KO_FORMATS }),
}

/** Deep-partial override shape: replace individual keys, never sections. */
interface GanttI18nOverrides {
  labels?: Partial<Omit<GanttI18nConfig["labels"], "scales">> & {
    scales?: Partial<GanttI18nConfig["labels"]["scales"]>
  }
  formats?: Partial<GanttI18nConfig["formats"]>
  functions?: Partial<GanttI18nConfig["functions"]>
}

/**
 * Shallow merge per nested object, matching the filters.tsx i18n contract:
 * a partial override replaces individual keys, never whole sections. Default
 * functions are re-bound to the MERGED labels/formats so a `formats` (or
 * `labels.continues`) override reaches the default renderers; explicit
 * `functions` overrides still win.
 */
function mergeGanttI18n(
  overrides?: GanttI18nOverrides,
  language: IenLanguage = "en",
): GanttI18nConfig {
  const base = language === "ko" ? KO_GANTT_I18N : DEFAULT_GANTT_I18N
  if (!overrides) return base
  const labels = {
    ...base.labels,
    ...overrides.labels,
    // nested section: replace individual scale names, never the whole set
    scales: {
      ...base.labels.scales,
      ...overrides.labels?.scales,
    },
  }
  const formats = { ...base.formats, ...overrides.formats }
  return {
    labels,
    formats,
    functions: {
      ...makeDefaultGanttFunctions({ labels, formats }),
      ...overrides.functions,
    },
  }
}

export { DEFAULT_GANTT_I18N, mergeGanttI18n }
export type { GanttI18nConfig, GanttI18nOverrides }
