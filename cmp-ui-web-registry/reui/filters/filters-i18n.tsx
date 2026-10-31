import type {
  FilterDraftStep,
  FilterIssue,
  FilterIssueReason,
  FilterLabels,
} from "@/components/reui/filters/filters-types"
import type { IenLanguage } from "@/lib/ien-theme"

/**
 * The shipped English copy. `stepAnnouncement` (consumer-composed create
 * wizard) and `showRecords` (a consumer's own panel heading) are unread by the
 * shipped chrome and kept on purpose; do not prune them as dead keys.
 */
export const DEFAULT_FILTER_LABELS: FilterLabels = {
  addFilter: "Add filter",
  advancedFilter: "Advanced filter",
  showRecords: "In this view, show records",
  builderEmpty: "No filters yet",
  builderEmptyHint: "Add a filter to narrow down what you see.",
  addCondition: "Add filter",
  addConditionGroup: "Add group",
  addToGroup: "Add filter to this group",
  removeGroup: "Remove group",
  wrapInGroup: "Wrap in group",
  ungroup: "Ungroup",
  moveToTopLevel: "Move to top",
  moveToGroup: (position) => `Move to group ${position}`,
  reorder: "Reorder",
  reorderHint: "Press Alt with Arrow Up or Arrow Down to reorder",
  groupAll: "All of the following are true...",
  groupAny: "Any of the following are true...",
  groupPlaceholder: "Drag filters here",
  rowLabel: (condition, depth) => `${condition}, level ${depth}`,
  groupLabel: (description, depth) => `${description} level ${depth}`,
  groupAnnouncement: (added) => (added ? "Group added" : "Group removed"),
  reorderAnnouncement: (label, position, total) =>
    `${label} moved to position ${position} of ${total}`,
  // Destination FIRST after the verb: it is the half a plain reorder cannot
  // say and the half the user cannot see once the row has stopped moving.
  // Position stays last, so this still ends on the "of N" a reorder ends on.
  moveAnnouncement: (label, destination, position, total) =>
    `${label} moved into ${destination}, position ${position} of ${total}`,
  clearAll: "Clear all",
  groupMenu: "Group options",
  searchFields: "Search attributes...",
  searchOperators: "Search operators...",
  searchOptions: "Search...",
  back: "Back",
  clear: "Clear",
  apply: "Apply",
  discard: "Discard changes",
  empty: "No results",
  loading: "Loading...",
  loadingMore: "Loading more...",
  loadMore: "Load more",
  error: "Could not load",
  retry: "Retry",
  where: "Where",
  and: "And",
  or: "Or",
  combinator: "Change combinator",
  // The word first: it is what the control SAYS and what a truncated pill
  // stops showing. The action follows, so the name still names an action.
  combinatorLabel: (word) => `${word}, change combinator`,
  duplicate: "Duplicate",
  negate: "Negate",
  convertToAdvanced: "Advanced editor",
  remove: "Remove",
  chipMenu: (fieldLabel) => `${fieldLabel} filter options`,
  filtersLabel: "Filters",
  filterLabel: (condition) => condition,
  readOnly: "Read only. These filters cannot be changed.",
  pathSeparator: " > ",
  valuePlaceholder: "enter text...",
  selectPlaceholder: "Select...",
  noValue: "no value",
  selectCondition: "Select condition",
  incomplete: "incomplete filter",
  branchAffordance: "opens a list",
  exclusiveHint: "cannot be combined with the other options",
  exclusiveAnnouncement: (label, cleared) =>
    cleared === 1
      ? `${label} selected. 1 other selection cleared.`
      : `${label} selected. ${cleared} other selections cleared.`,
  itemCount: (count) => `${count} items`,
  fieldsLabel: "Attributes",
  resultsAnnouncement: (count) =>
    count === 1 ? "1 result" : `${count} results`,
  actionsLabel: "Actions",
  stepAnnouncement: (step, label) => {
    if (step === "field") return `Choose an attribute. ${label}`
    if (step === "operator") return `Choose a condition for ${label}`
    return `Enter a value for ${label}`
  },
  countAnnouncement: (count) =>
    count === 1 ? "1 filter applied" : `${count} filters applied`,
  valueCount: (count) => `${count} selected`,
  valueDetail: (summary, values) => `${summary}: ${values.join(", ")}`,
  valueRange: (from, to) => `${from} to ${to}`,
  rangeFrom: (fieldLabel) => `${fieldLabel} from`,
  rangeTo: (fieldLabel) => `${fieldLabel} to`,
  rangeSeparator: "to",
  negated: (operatorLabel) => `not ${operatorLabel}`,
  issueOperator: "Choose a condition",
  issueValue: "Enter a value",
  issueRange: "Enter both ends of the range",
  issueRangeOrder: "The end of the range comes before its start",
  issueEmptyGroup: "This group has no conditions yet",
  // "Row", not "condition": an empty GROUP is counted here too and a group is
  // not a condition. Rule and group divs alike carry `data-slot="filter-row"`.
  issueSummary: (count) =>
    count === 1 ? "1 row needs attention" : `${count} rows need attention`,
}

const FILTER_LABELS_KO: FilterLabels = {
  addFilter: "필터 추가",
  advancedFilter: "고급 필터",
  showRecords: "이 보기에서 표시할 항목",
  builderEmpty: "필터가 없습니다",
  builderEmptyHint: "표시할 항목을 좁히려면 필터를 추가하세요.",
  addCondition: "필터 추가",
  addConditionGroup: "그룹 추가",
  addToGroup: "그룹에 필터 추가",
  removeGroup: "그룹 삭제",
  wrapInGroup: "그룹으로 묶기",
  ungroup: "그룹 해제",
  moveToTopLevel: "최상위로 이동",
  moveToGroup: (position) => `${position} 그룹으로 이동`,
  reorder: "순서 변경",
  reorderHint: "Alt 키와 위쪽 또는 아래쪽 화살표 키를 함께 눌러 순서를 변경합니다",
  groupAll: "다음 조건을 모두 만족",
  groupAny: "다음 조건 중 하나 이상 만족",
  groupPlaceholder: "필터를 여기에 놓으세요",
  rowLabel: (condition, depth) => `${condition}, ${depth}단계`,
  groupLabel: (description, depth) => `${description} ${depth}단계`,
  groupAnnouncement: (added) => added ? "그룹 추가됨" : "그룹 삭제됨",
  reorderAnnouncement: (label, position, total) => `${label}, ${total}개 중 ${position}번째로 이동됨`,
  moveAnnouncement: (label, destination, position, total) => `${label}, ${destination}으로 이동됨, ${total}개 중 ${position}번째`,
  clearAll: "모두 지우기",
  groupMenu: "그룹 옵션",
  searchFields: "속성 검색...",
  searchOperators: "조건 검색...",
  searchOptions: "검색...",
  back: "뒤로",
  clear: "지우기",
  apply: "적용",
  discard: "변경사항 취소",
  empty: "결과가 없습니다",
  loading: "불러오는 중...",
  loadingMore: "더 불러오는 중...",
  loadMore: "더 불러오기",
  error: "불러오지 못했습니다",
  retry: "다시 시도",
  where: "조건",
  and: "그리고",
  or: "또는",
  combinator: "조건 결합 방식 변경",
  combinatorLabel: (word) => `${word}, 조건 결합 방식 변경`,
  duplicate: "복제",
  negate: "반대로 설정",
  convertToAdvanced: "고급 편집기",
  remove: "삭제",
  chipMenu: (fieldLabel) => `${fieldLabel} 필터 옵션`,
  filtersLabel: "필터",
  filterLabel: (condition) => condition,
  readOnly: "읽기 전용입니다. 이 필터는 변경할 수 없습니다.",
  pathSeparator: " > ",
  valuePlaceholder: "값 입력...",
  selectPlaceholder: "선택...",
  noValue: "값 없음",
  selectCondition: "조건 선택",
  incomplete: "필터가 완성되지 않았습니다",
  branchAffordance: "목록 열기",
  exclusiveHint: "다른 옵션과 함께 선택할 수 없습니다",
  exclusiveAnnouncement: (label, cleared) => `${label} 선택됨. 다른 선택 ${cleared}개 해제됨.`,
  itemCount: (count) => `${count}개 항목`,
  fieldsLabel: "속성",
  resultsAnnouncement: (count) => `${count}개 결과`,
  actionsLabel: "작업",
  stepAnnouncement: (step, label) => {
    if (step === "field") return `속성을 선택하세요. ${label}`
    if (step === "operator") return `${label}의 조건을 선택하세요`
    return `${label}의 값을 입력하세요`
  },
  countAnnouncement: (count) => `필터 ${count}개 적용됨`,
  valueCount: (count) => `${count}개 선택됨`,
  valueDetail: (summary, values) => `${summary}: ${values.join(", ")}`,
  valueRange: (from, to) => `${from}부터 ${to}까지`,
  rangeFrom: (fieldLabel) => `${fieldLabel} 시작`,
  rangeTo: (fieldLabel) => `${fieldLabel} 끝`,
  rangeSeparator: "부터",
  negated: (operatorLabel) => `${operatorLabel} 아님`,
  issueOperator: "조건을 선택하세요",
  issueValue: "값을 입력하세요",
  issueRange: "범위의 시작과 끝을 모두 입력하세요",
  issueRangeOrder: "범위의 끝이 시작보다 앞에 있습니다",
  issueEmptyGroup: "그룹에 조건이 없습니다",
  issueSummary: (count) => `${count}개 행을 확인해야 합니다`,
}

/**
 * Shallow merge over the defaults, like the cascader's. A deep merge would leak
 * the default back into a replaced function-valued label for the arguments the
 * consumer did not think about.
 */
export function resolveFilterLabels(
  labels?: Partial<FilterLabels>,
  language: IenLanguage = "en",
): FilterLabels {
  const defaults = language === "ko" ? FILTER_LABELS_KO : DEFAULT_FILTER_LABELS
  if (!labels) return defaults
  return { ...defaults, ...labels }
}

/**
 * The sentence for one issue. Takes the ISSUE, not just the reason: a field's
 * `validate` supplies its own words, so `custom` has nothing to look up.
 */
export function filterIssueLabel(
  issue: Pick<FilterIssue, "reason" | "message"> | FilterIssueReason,
  labels: FilterLabels
): string {
  const reason = typeof issue === "string" ? issue : issue.reason
  if (typeof issue !== "string" && issue.message) return issue.message
  if (reason === "missing-operator") return labels.issueOperator
  if (reason === "incomplete-range") return labels.issueRange
  if (reason === "reversed-range") return labels.issueRangeOrder
  if (reason === "empty-group") return labels.issueEmptyGroup
  return labels.issueValue
}

/** The search placeholder for a step's panel. */
export function stepPlaceholder(
  step: FilterDraftStep,
  labels: FilterLabels
): string {
  if (step === "field") return labels.searchFields
  if (step === "operator") return labels.searchOperators
  return labels.searchOptions
}
