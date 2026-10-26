import type { IenLanguage } from "@/lib/ien-theme"

/**
 * Every built-in user-facing string in the data grid, overridable in one
 * place: `<DataGrid i18n={{ labels: { ... } }}>`. The shape follows the
 * event-calendar and gantt convention - a nested config merged per section,
 * with functions for anything interpolated so pluralization and word order
 * live in the label, never in string concatenation at a call site.
 *
 * Precedence, most specific wins: a component-level prop that already
 * existed (`rowCreateLabel`, `loadingMessage`, the pagination label props)
 * beats the matching `i18n` label, which beats the built-in default - so
 * adopting `i18n` is never a breaking change.
 */

export interface DataGridI18nLabels {
  /* The column header menu. */
  sortAscending: string
  sortDescending: string
  pinColumnStart: string
  pinColumnEnd: string
  moveColumnStart: string
  moveColumnEnd: string
  columnsMenu: string
  unpinColumn: (title: string) => string
  toggleColumns: string
  /* Row and cell affordances. */
  rowCreate: string
  pinRow: string
  unpinRow: string
  selectRow: string
  selectAll: string
  expandRow: string
  collapseRow: string
  dragToReorder: string
  dragToReorderRow: string
  reorderingUnavailable: string
  /* Grid states. */
  loading: string
  empty: string
  allRowsLoaded: string
  /* Pagination. */
  rowsPerPage: string
  paginationInfo: (info: { from: number; to: number; count: number }) => string
  selectionCount: (count: number) => string
  clearSelection: string
  previousPage: string
  nextPage: string
  goToPage: (page: number) => string
  paginationEllipsis: string
  /* The faceted column filter. */
  filterSelectedCount: (count: number) => string
  filterNoResults: string
  filterClear: string
}

export interface DataGridI18nConfig {
  labels: DataGridI18nLabels
}

export type DataGridI18nOverrides = {
  labels?: Partial<DataGridI18nLabels>
}

const DEFAULT_DATA_GRID_LABELS: DataGridI18nLabels = {
  sortAscending: "Asc",
  sortDescending: "Desc",
  pinColumnStart: "Pin to left",
  pinColumnEnd: "Pin to right",
  moveColumnStart: "Move to left",
  moveColumnEnd: "Move to right",
  columnsMenu: "Columns",
  unpinColumn: (title) => `Unpin ${title} column`,
  toggleColumns: "Toggle Columns",
  rowCreate: "Add row",
  pinRow: "Pin row",
  unpinRow: "Unpin row",
  selectRow: "Select row",
  selectAll: "Select all",
  expandRow: "Expand row",
  collapseRow: "Collapse row",
  dragToReorder: "Drag to reorder",
  dragToReorderRow: "Drag to reorder row",
  reorderingUnavailable: "Reordering unavailable",
  loading: "Loading...",
  empty: "No data available",
  allRowsLoaded: "All records loaded",
  rowsPerPage: "Rows per page",
  paginationInfo: ({ from, to, count }) => `${from} - ${to} of ${count}`,
  selectionCount: (count) => `${count} selected`,
  clearSelection: "Clear selection",
  previousPage: "Go to previous page",
  nextPage: "Go to next page",
  goToPage: (page) => `Go to page ${page}`,
  paginationEllipsis: "...",
  filterSelectedCount: (count) => `${count} selected`,
  filterNoResults: "No results found.",
  filterClear: "Clear filters",
}

const DATA_GRID_LABELS_KO: DataGridI18nLabels = {
  sortAscending: "오름차순",
  sortDescending: "내림차순",
  pinColumnStart: "왼쪽에 고정",
  pinColumnEnd: "오른쪽에 고정",
  moveColumnStart: "왼쪽으로 이동",
  moveColumnEnd: "오른쪽으로 이동",
  columnsMenu: "열",
  unpinColumn: (title) => `${title} 열 고정 해제`,
  toggleColumns: "열 표시 설정",
  rowCreate: "행 추가",
  pinRow: "행 고정",
  unpinRow: "행 고정 해제",
  selectRow: "행 선택",
  selectAll: "모두 선택",
  expandRow: "행 펼치기",
  collapseRow: "행 접기",
  dragToReorder: "끌어서 순서 변경",
  dragToReorderRow: "끌어서 행 순서 변경",
  reorderingUnavailable: "순서를 변경할 수 없습니다",
  loading: "불러오는 중...",
  empty: "표시할 데이터가 없습니다",
  allRowsLoaded: "모든 항목을 불러왔습니다",
  rowsPerPage: "페이지당 행 수",
  paginationInfo: ({ from, to, count }) => `${count}개 중 ${from}–${to}`,
  selectionCount: (count) => `${count}개 선택됨`,
  clearSelection: "선택 지우기",
  previousPage: "이전 페이지로 이동",
  nextPage: "다음 페이지로 이동",
  goToPage: (page) => `${page}페이지로 이동`,
  paginationEllipsis: "...",
  filterSelectedCount: (count) => `${count}개 선택됨`,
  filterNoResults: "결과가 없습니다.",
  filterClear: "필터 지우기",
}

const DEFAULT_DATA_GRID_I18N: DataGridI18nConfig = Object.freeze({
  labels: Object.freeze(DEFAULT_DATA_GRID_LABELS),
})
const DATA_GRID_I18N_KO: DataGridI18nConfig = Object.freeze({
  labels: Object.freeze(DATA_GRID_LABELS_KO),
})

/**
 * A shallow merge per section, deliberately: a deep merge would leak a
 * default back into a function-valued label the consumer replaced. With no
 * overrides the frozen default is returned as-is, so the merge is free to
 * run on every render without producing a new identity.
 */
export function mergeDataGridI18n(
  overrides?: DataGridI18nOverrides,
  language: IenLanguage = "en",
): DataGridI18nConfig {
  const defaults = language === "ko" ? DATA_GRID_I18N_KO : DEFAULT_DATA_GRID_I18N
  if (!overrides?.labels) return defaults
  return {
    labels: { ...defaults.labels, ...overrides.labels },
  }
}
