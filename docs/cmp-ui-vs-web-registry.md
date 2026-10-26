# CMP UI와 웹 컴포넌트 비교

## 분석 기준

- 분석일: 2026-10-26
- 기준: 분석 당시 스테이징된 변경을 포함한 작업 트리의 소스와 공개 API
- CMP 주 범위: `cmp-ui`, 문서 카탈로그
- CMP 보조 범위: `cmp-pref`, `cmp-adaptive`, `cmp-date`
- 웹 범위: `cmp-ui-web-registry`의 설치 항목과 실제 구현
- 검증 범위: 정적 소스 분석. 빌드·테스트·브라우저 시각 비교는 수행하지 않음

이 문서에서 **대응 없음**은 현재 라이브러리가 바로 사용할 수 있는 대응 컴포넌트를 제공하지 않는다는 뜻입니다. 기반 라이브러리나 기존 컴포넌트를 조합해 구현할 수 있는 가능성과는 구분합니다. **대응 존재**도 외형·애니메이션·접근성까지 동일하게 검증되었다는 뜻은 아닙니다.

## 현재 구성과 결론

| 구분 | 구성 |
| --- | --- |
| CMP 문서 카탈로그 | 73개 항목. Compose 기본 API인 `AnimatedContent` 예제도 포함 |
| 웹 컴포넌트 레지스트리 | UI 57개, 훅 5개, 공통 라이브러리 3개로 총 65개 |
| 웹 프로젝트 레지스트리 | 기본 프로젝트 `ienlab` 1개. 위 컴포넌트 집계와 별도 |

CMP는 하나의 카탈로그 항목에 여러 컴포넌트를 묶고, 웹은 복합 컴포넌트를 여러 설치 항목으로 나누기도 합니다. 따라서 57÷73을 구현률로 계산할 수 없습니다.

기본 입력·선택·로딩 컴포넌트는 양쪽에 상당 부분 구현되어 있습니다. 웹에서 부족한 영역은 CMP의 리스트·다이얼로그·알림·화면 구성 컴포넌트입니다. 반대로 데이터 테이블·일정·보드·계층 선택 같은 복잡한 데이터 UI는 웹이 더 풍부합니다.

웹은 REUI와 shadcn/ui 기반 구조에 IEN 디자인과 CMP 기능을 반영한 상태입니다. 웹 UI 57개 모두 `ienDesign: true`로 등록되어 있지만, 이 메타데이터만으로 시각적 대응 완료를 판단하지 않습니다.

## 양쪽에 존재하는 주요 기능

| 기능 | CMP | 웹 | 비교 |
| --- | --- | --- | --- |
| 기본 버튼 | `IenButton` | `Button` | `fill / weak / line / ghost` 제공 |
| 배지 | `IenBadge` | `Badge` | 양쪽에 크기·색상·표현 변형 존재 |
| 카드 | `IenCard` | `Card` | 웹도 채움·테두리, 색상, 여백, 모서리, elevation 제공 |
| 체크박스 | `IenCircleCheckbox`, `IenDotCheckbox`, `IenLineCheckbox` | `Checkbox` | 웹도 `circle / dot / line` 제공 |
| 라디오 | `IenRadioButton`, `IenRadioGroup` | `RadioGroup` | 기본 선택 기능 대응 |
| 스위치 | `IenSwitch` | `Switch` | 기본 선택 기능 대응 |
| 탭 | `IenTab` | `Tabs` | 웹도 항목 배열, 인덱스 제어, 아이콘, 배지, 알림 점 제공 |
| 텍스트 입력 | `IenTextField`, `IenTextArea` | `Input`, `Textarea` | 레이블, 오류·성공 상태, 도움말, 길이 제한 제공 |
| 별점 | `IenRating` | `Rating` | 읽기·편집, 소수 채움, 세 표시 방식 등 상당 부분 대응 |
| 날짜 휠 | `IenDateWheelPicker` | `DatePicker` | 연·월·일, 연도 범위, 선택 불가 날짜와 말일 보정 제공 |
| 시각 휠 | `IenTimeWheelPicker` | `TimePicker` | 시·분·초, 12·24시간제 제공. 웹은 시간 제한·간격·확인 절차도 제공 |
| 기간 휠 | `IenDurationWheelPicker` | `DurationPicker` | 시·분·초 표시 제어와 최대 시간 설정 제공 |
| 스켈레톤 | `IenSkeleton` | `Skeleton` | 패턴, 사용자 정의 요소, 반복, 배경, 표시 상태 제공 |
| 진행 표시 | `IenProgressIndicator` | `ProgressIndicator` | 원형·선형, 평면·물결, 확정·불확정 제공 |
| 로딩 애니메이션 | `IenLoadingIndicator` | `LoadingIndicator` | 웹도 도형 변형·회전 구현 |
| 메뉴 | `IenMenu`, `IenDropdownMenu`, `IenActionsMenu` | `DropdownMenu`, `ContextMenu` 등 | 대응 구현 존재. 호출 구조와 조합 방식은 다름 |
| 툴팁 | `IenTooltip` | `Tooltip` | 표시·위치·너비·정렬 대응. 세부 옵션 차이 존재 |
| 빈 상태 | `IenEmpty` | `Empty` | 대응 구현 존재 |

## 존재하지만 기능이나 API가 다른 영역

| 영역 | 현재 차이 및 부족한 부분 |
| --- | --- |
| 버튼 로딩 | CMP는 `loading` 상태와 상호작용 제한을 내장합니다. 웹 `Button`에는 대응 전용 속성이 없어 호출 측 조합이 필요합니다. |
| 토글·아이콘 버튼 | 웹은 아이콘 크기의 `Button`을 제공합니다. CMP의 `IenToggleButton`, `IenIconToggleButton`에 대응하는 선택 상태 API는 없습니다. |
| Split Button | 웹 `ButtonGroup`과 메뉴로 조합할 수 있지만, CMP처럼 주 동작과 보조 메뉴를 묶은 전용 API는 없습니다. |
| 입력 확장 | CMP의 지우기·비밀번호 표시 전환·검색·분할 입력을 묶어 제공하는 웹 전용 API가 부족합니다. `type="password"`만으로 표시 전환까지 제공하지는 않습니다. |
| 입력 포맷 | 웹 `Input`에는 CMP `IenTextFieldFormat`과 직접 대응하는 변환 API가 없습니다. 접두·접미 콘텐츠는 `InputGroup` 등으로 조합해야 합니다. |
| 바텀시트 | 웹 `Sheet side="bottom"`은 존재합니다. CMP의 높이 단계, 드래그 확장·축소, 선택형 시트 API는 없습니다. |
| 툴팁 | 웹도 위치·너비·정렬은 지원합니다. CMP의 `motionVariant`, `clipToEnd`, 앵커 비율 등은 직접 대응하지 않습니다. |
| 숫자 증감 | CMP `IenNumericSpinner`와 숫자형 `IenStepper`는 웹 `NumberField`가 대응합니다. 웹 `Stepper`는 단계 표시 컴포넌트입니다. |
| 단계 표시 | CMP `IenStepper`에는 숫자 증감과 단계 표시 오버로드가 있습니다. 웹 `Stepper`·`Timeline`과 역할이 겹치지만 등장 애니메이션과 행 구성 API가 같지는 않습니다. |
| 진행 단계 상태 | 웹 `Stepper`는 존재하지만 CMP `IenProgressStepper`의 `Error` 상태와 동일한 상태 모델은 아닙니다. |
| 진행 바·간단한 로더 | 웹 `ProgressIndicator`로 선형 진행률과 회전을 표현할 수 있습니다. CMP `IenProgressBar`, `IenLoader`와 동일한 전용 크기·표현 API로 묶여 있지는 않습니다. |
| 결과 화면 | 웹 `Empty`, `Alert`로 일부 표현할 수 있지만, CMP `IenResult`의 성공·실패·안내와 액션을 묶은 API는 없습니다. |
| 일반 휠 | 웹 공통 라이브러리에 `IenWheelColumn`은 있습니다. CMP `IenWheelPicker`처럼 독립 설치 항목으로 제공하지 않으며, 표시 행 수·햅틱 등 API도 다릅니다. |
| 콘텐츠·자산 | 웹 `Typography`, `Avatar`, `IconTile`, `IconStack` 등으로 일부 역할을 조합할 수 있습니다. CMP의 Paragraph / Post / AssetFrame과 구성·크기 API가 같지는 않습니다. |

### 웹 선택기 사이의 API 차이

| 기능 | DatePicker | TimePicker | DurationPicker |
| --- | --- | --- | --- |
| 제어·비제어 값 | 제공 | 제공 | 제공 |
| 빈 값·초기화 | 제공 | 제공 | 제공 |
| 인라인 구성 | `inline` 제공 | 하위 패널 조합으로 구성 | `inline` 제공 |
| 열림 상태 외부 제어 | 전용 API 없음 | `open`, `onOpenChange` 제공 | 전용 API 없음 |
| 확인 전 임시 선택 | 변경 즉시 반영. 확인 버튼은 닫기 역할 | `requireConfirm` 제공 | 변경 즉시 반영. 확인 버튼은 닫기 역할 |
| 네이티브 폼 연동 | `name / form / required` 전용 API 없음 | 제공 | `name / form / required` 전용 API 없음 |

## CMP에는 있고 웹에 대응 컴포넌트가 없는 영역

| 영역 | CMP 구현 | 웹 현황 |
| --- | --- | --- |
| 일반·확인 다이얼로그 | `IenDialog`, `IenModal`, `IenAlertDialog`, `IenConfirmDialog` | 대응 전용 설치 항목 없음 |
| 목적별 다이얼로그 | 저장·삭제·업데이트·네트워크·진행·텍스트 입력 다이얼로그 | 대응 전용 설치 항목 없음 |
| 날짜·시간 다이얼로그 | `IenDatePickerDialog`, `IenTimePickerDialog` | 선택기는 있으나 다이얼로그 형태의 전용 API 없음 |
| 일시적 알림 | `IenSnackbar`, `IenToast`와 Host·Provider·표시 상태 관리 | 대응 없음 |
| 슬라이더 | `IenSlider`, `IenRangeSlider` | 대응 UI 설치 항목 없음. `use-slider-input` 훅과 구분 필요 |
| 칩 | Assist / Filter / Input / Suggestion 및 elevated 변형 | 대응 전용 컴포넌트 없음 |
| 분할 선택 | `IenSegmentedControl` | 대응 전용 컴포넌트 없음 |
| 플로팅 액션 | `IenFab`, `IenExtendedFab`, `IenFabMenu` | 대응 전용 컴포넌트 없음 |
| 액션 바 | `IenActionBar` | 대응 없음 |
| 리스트 | `IenListRow`, Header / Footer, `IenBoardRow`, `IenTableRow`, Section | CMP의 행·섹션 API에 대응하는 컴포넌트 없음 |
| 스와이프·새로고침 | `IenSwipeBox`, `IenPullToRefreshBox` | 대응 없음 |
| 화면 골격 | Scaffold, TopBar, BottomBar, NavigationBar | 대응 전용 컴포넌트 없음 |
| 하단 작업 영역 | BottomCTA, DoubleBottomCTA, ChatBottomBar, BottomInfo | 대응 전용 컴포넌트 없음 |
| 약관 | 필수·선택, 전체 동의, 펼침 등을 포함한 `IenAgreement` | 체크박스 기반의 전용 약관 컴포넌트 없음 |
| 키패드 | 숫자·알파벳·보안 키패드 | 대응 없음 |
| 콘텐츠 조합 | Bubble, HighlightText, Paragraph, Post | 대응 전용 API 없음. 기본 텍스트 표현은 조합 가능 |
| 애니메이션 레이아웃 | `IenAnimatedColumn`, `IenAnimatedRow` | 대응 범용 컴포넌트 없음 |
| 설정 화면 | `cmp-pref`의 PrefsScreen / Group 및 설정 항목 | 설정 화면과 저장 연동을 묶은 대응 API 없음 |
| 인증 폼·권한 안내 | `IenAuthForm`, `IenPermissionScreen` | 대응 전용 컴포넌트 없음. 플랫폼별 필요성 판단 필요 |

웹 `Alert`는 화면 안의 안내 영역이며 확인·취소를 받는 AlertDialog와 다릅니다. `Sheet`가 내부적으로 Radix Dialog를 사용하더라도, 일반 다이얼로그 설치 항목을 제공하는 상태로 집계하지 않습니다.

## 웹이 더 풍부한 영역

| 영역 | 웹 구현 | CMP 현황 |
| --- | --- | --- |
| 데이터 테이블 | `DataGrid` | `IenTableRow`는 있으나 정렬·필터·페이지·가상화를 묶은 대응 구현 없음 |
| 업무 일정 | `EventCalendar`, `Gantt` | 대응 전용 컴포넌트 없음 |
| 작업 보드 | `Kanban` | 대응 전용 컴포넌트 없음 |
| 계층 선택 | `Tree`, `Cascader` | 대응 전용 컴포넌트 없음 |
| 복합 검색·선택 | `Autocomplete`, `Combobox`, `Filters` | 기존 Dropdown 계열보다 기능 범위가 넓음 |
| 날짜 탐색 | `Calendar`, `DateSelector` | 날짜 다이얼로그·휠과 별개로 기간 탐색·필터 기능 제공 |
| 특수 입력 | `ColorPicker`, `EmojiPicker`, `SignaturePad`, `PhoneInput` | 동일한 전용 UI 없음. 전화번호 변환 유틸리티는 존재 |
| 웹 문서·탐색 | `CodeBlock`, `Scrollspy` | 직접 대응하는 전용 컴포넌트 없음 |
| 재정렬 | `Sortable` | 대응 범용 컴포넌트 없음 |

플랫폼 용도 차이가 크므로 이 항목 모두를 CMP에 추가해야 하는 결손으로 판단하지 않습니다. 특히 네이티브 권한·키패드·햅틱과 웹의 문서 탐색·복잡한 데이터 UI는 필요성을 별도로 판단해야 합니다.

## 문서와 매핑의 부족한 부분

### CMP 연결 메타데이터

웹 UI 57개 중 `cmpIds`가 설정된 것은 다음 4개입니다.

- `rating`
- `skeleton`
- `progress-indicator`
- `loading-indicator`

버튼·카드·체크박스·탭·휠 선택기 등은 실제 대응 구현이 있지만 `cmpIds`가 비어 있습니다. 문서 생성기는 이 필드로 CMP 카탈로그 연결을 찾으므로 실제 대응 범위가 문서에 충분히 드러나지 않습니다.

매핑 시에는 이름보다 역할을 기준으로 확인해야 합니다. 예를 들어 숫자형 `IenStepper`는 웹 `NumberField`, 진행형 `IenStepper`는 웹 `Stepper`·`Timeline`과 비교해야 합니다. 세 휠 선택기는 CMP 카탈로그의 `wheel-picker` 한 항목으로 묶여 있습니다.

### 문서와 현재 구현의 차이

이 문서의 이전 내용은 REUI 기본 구현을 가져온 당시 상태를 설명하며 IEN 디자인과 CMP 일대일 매핑을 포함하지 않는다고 안내했습니다. 현재 소스에는 IEN 디자인과 CMP 대응 API가 반영되어 있으므로 현재 비교 내용으로 갱신했습니다. 다만 실제 화면의 시각적 일치 여부는 별도 검증이 필요합니다.

## 보완 우선순위 제안

웹의 CMP 대응을 높이는 목적일 때의 우선순위입니다. 구현이 확정된 작업 목록은 아닙니다.

1. **Dialog / AlertDialog, Toast / Snackbar**: 기본 사용자 흐름에서 빠진 피드백 수단을 보완합니다.
2. **Slider / RangeSlider, Chip, SegmentedControl**: 기본 입력·선택 영역을 보완합니다.
3. **ListRow / Section, 버튼 loading·toggle, 입력 확장**: 반복적인 개별 구현을 줄입니다.
4. **BottomSheet 높이·드래그, 화면 골격·CTA**: CMP 화면 구성 패턴을 대응합니다.
5. **`cmpIds` 정비**: 확인된 대응 관계를 문서 생성에 반영합니다.

## 근거 소스와 가져오기 정보

- [CMP 문서 카탈로그](../cmp-ui-docs/web/catalog.js)
- [CMP UI 구현](../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui)
- [CMP 설정 컴포넌트](../cmp-pref/src/commonMain/kotlin/zone/ien/utils/pref)
- [루트 레지스트리](../registry.json)
- [웹 컴포넌트 레지스트리](../cmp-ui-web-registry/registry.json)
- [웹 프로젝트 레지스트리](../cmp-ui-web-registry/projects/registry.json)
- [웹 기본 UI 구현](../cmp-ui-web-registry/ui)
- [웹 REUI 기반 구현](../cmp-ui-web-registry/reui)
- [웹 공통 라이브러리](../cmp-ui-web-registry/lib)
- [웹 문서 생성·CMP 연결 처리](../cmp-ui-docs/scripts/registry.mjs)

REUI 가져오기 원본은 `https://github.com/keenthemes/reui`이며, 레지스트리에 기록된 기준 커밋은 `067aa0dc7f8bbfec1cd76fc5ad31f842be5f569a`, 스타일은 `radix-nova`입니다. REUI 컴포넌트는 `components/reui`, 기반 shadcn/ui 컴포넌트는 `components/ui` 경로를 사용합니다.

가져오기 명령은 `npm --prefix cmp-ui-docs run import:reui`입니다. 현재 가져오기 스크립트는 `ienDesign` 항목이 있으면 보호 절차를 적용하므로 실행 전 스크립트와 옵션을 확인해야 합니다. 이 분석 과정에서는 가져오기를 실행하지 않았습니다.
