import type {
  CascaderLabels,
  CascaderMode,
} from "@/components/reui/cascader/cascader-types"
import type { IenLanguage } from "@/lib/ien-theme"

/** Name of the root level. Hoisted so the root announcement can reuse it. */
const ROOT_LEVEL = "Top level"

/** Hoisted for the same reason: several defaults end in an item count. */
const itemCount = (count: number) =>
  `${count} ${count === 1 ? "item" : "items"}`

/**
 * English defaults. Every string the primitive can render lives here, so a
 * consumer can translate the whole surface by passing `labels`.
 */
export const CASCADER_LABELS: CascaderLabels = {
  // Deliberately NOT lowercased. `toLowerCase()` is locale-hostile - it maps
  // Turkish "İ" to a two-code-point sequence and German "İstanbul" style
  // proper nouns lose their casing - and a label is already written the way
  // its author wants it read.
  search: (parentLabel) =>
    parentLabel ? `Search ${parentLabel}...` : "Search...",
  back: "Back",
  loading: "Loading...",
  loadingMore: "Loading more...",
  loadMore: "Load more",
  error: "Could not load items.",
  retry: "Retry",
  empty: "No results found.",
  selectedCount: (count) => `${count} selected`,
  breadcrumbLabel: "Breadcrumb",
  chipsLabel: "Selected items",
  removeChip: (label) => `Remove ${label}`,
  pathSeparator: "/",
  rootLevel: ROOT_LEVEL,
  itemCount,
  branchAffordance: "submenu",
  selectedState: "selected",
  partiallySelectedState: "partially selected",
  columnsLabel: "Levels",
  actionsLabel: "Actions",
  submenuAffordance: "opens a menu",
  panelLabel: "Options",
  keyboardHint: (mode: CascaderMode, dir: "ltr" | "rtl") => {
    // "Deeper" is the direction the text runs, so the level keys mirror in
    // RTL and the hint has to name the mirrored pair there - an LTR-worded
    // hint would teach exactly the wrong keys.
    const open = dir === "rtl" ? "Left" : "Right"
    const back = dir === "rtl" ? "Right" : "Left"
    if (mode === "tree") {
      return `Use the ${open} Arrow key to expand and the ${back} Arrow key to collapse.`
    }
    if (mode === "columns") {
      return `Use the ${open} Arrow key to open the next column and the ${back} Arrow key to go back.`
    }
    return `Use the ${open} Arrow key to open a branch and the ${back} Arrow key to go back.`
  },
  rootAnnouncement: (count) => `${ROOT_LEVEL}, ${itemCount(count)}`,
  expandedAnnouncement: (label, count) =>
    `${label} expanded, ${itemCount(count)}`,
  collapsedAnnouncement: (label) => `${label} collapsed`,
  levelAnnouncement: (parentLabel, depth, count) =>
    `${parentLabel}, level ${depth}, ${itemCount(count)}`,
  resultsAnnouncement: (count) =>
    count === 1 ? "1 result" : `${count} results`,
  maxReachedAnnouncement: (max) => `Selection limit of ${max} reached`,
  cascadeAnnouncement: (label, count, selecting) =>
    `${label} ${selecting ? "selected" : "deselected"}, ${itemCount(count)} followed`,
  searchingAnnouncement: "Searching...",
}

const CASCADER_LABELS_KO: CascaderLabels = {
  search: (parentLabel) => parentLabel ? `${parentLabel} 검색...` : "검색...",
  back: "뒤로",
  loading: "불러오는 중...",
  loadingMore: "더 불러오는 중...",
  loadMore: "더 불러오기",
  error: "항목을 불러오지 못했습니다.",
  retry: "다시 시도",
  empty: "검색 결과가 없습니다.",
  selectedCount: (count) => `${count}개 선택됨`,
  breadcrumbLabel: "경로",
  chipsLabel: "선택한 항목",
  removeChip: (label) => `${label} 선택 해제`,
  pathSeparator: "/",
  rootLevel: "최상위",
  itemCount: (count) => `${count}개 항목`,
  branchAffordance: "하위 메뉴",
  selectedState: "선택됨",
  partiallySelectedState: "일부 선택됨",
  columnsLabel: "단계",
  actionsLabel: "작업",
  submenuAffordance: "메뉴 열기",
  panelLabel: "옵션",
  keyboardHint: (mode, dir) => {
    const open = dir === "rtl" ? "왼쪽" : "오른쪽"
    const back = dir === "rtl" ? "오른쪽" : "왼쪽"
    if (mode === "tree") return `${open} 화살표 키로 펼치고 ${back} 화살표 키로 접습니다.`
    if (mode === "columns") return `${open} 화살표 키로 다음 열을 열고 ${back} 화살표 키로 돌아갑니다.`
    return `${open} 화살표 키로 하위 항목을 열고 ${back} 화살표 키로 돌아갑니다.`
  },
  rootAnnouncement: (count) => `최상위, ${count}개 항목`,
  expandedAnnouncement: (label, count) => `${label} 펼침, ${count}개 항목`,
  collapsedAnnouncement: (label) => `${label} 접힘`,
  levelAnnouncement: (parentLabel, depth, count) => `${parentLabel}, ${depth}단계, ${count}개 항목`,
  resultsAnnouncement: (count) => `${count}개 결과`,
  maxReachedAnnouncement: (max) => `최대 ${max}개까지 선택할 수 있습니다.`,
  cascadeAnnouncement: (label, count, selecting) => `${label} ${selecting ? "선택됨" : "선택 해제됨"}, ${count}개 항목에 적용됨`,
  searchingAnnouncement: "검색 중...",
}

/**
 * Shallow-merges consumer overrides over the defaults, so `labels` can carry a
 * single key without restating the rest.
 */
export function resolveCascaderLabels(
  labels?: Partial<CascaderLabels>,
  language: IenLanguage = "en",
): CascaderLabels {
  const defaults = language === "ko" ? CASCADER_LABELS_KO : CASCADER_LABELS
  if (!labels) return defaults
  return { ...defaults, ...labels }
}

/** Resolves the search placeholder, which may be a string or a function. */
export function resolveCascaderSearchLabel(
  labels: CascaderLabels,
  parentLabel?: string
): string {
  return typeof labels.search === "function"
    ? labels.search(parentLabel)
    : labels.search
}
