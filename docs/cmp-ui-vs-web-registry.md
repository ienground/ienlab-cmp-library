# CMP UI와 웹 컴포넌트 비교

## 분석 기준

- 분석일: 2026-10-26
- 기준: 분석 당시 스테이징된 변경을 포함한 작업 트리의 소스와 공개 API
- CMP 주 범위: `cmp-ui`, 문서 카탈로그
- CMP 보조 범위: `cmp-pref`, `cmp-adaptive`, `cmp-date`
- 웹 범위: `cmp-ui-web-registry`의 설치 항목과 실제 구현
- 검증 범위: 기능·API는 현재 소스와 레지스트리를 대조. 디자인은 토큰·스타일 소스 비교와 로컬 Chrome의 대표 프리뷰 확인을 병행. 최근 구현 작업에서 타입 검사·사이트 빌드·설치 레지스트리 생성을 통과했으며, 이 문서 갱신에서는 빌드·자동 테스트를 재실행하지 않음

이 문서에서 **대응 없음**은 현재 라이브러리가 바로 사용할 수 있는 대응 컴포넌트를 제공하지 않는다는 뜻입니다. 기반 라이브러리나 기존 컴포넌트를 조합해 구현할 수 있는 가능성과는 구분합니다. **대응 존재**도 외형·애니메이션·접근성까지 동일하게 검증되었다는 뜻은 아닙니다.

## 현재 구성과 결론

| 구분 | 구성 |
| --- | --- |
| CMP 문서 카탈로그 | 73개 항목. Compose 기본 API인 `AnimatedContent` 예제도 포함 |
| 웹 컴포넌트 레지스트리 | UI 60개, 훅 5개, 공통 라이브러리 3개로 총 68개 |
| 웹 프로젝트 레지스트리 | 기본 프로젝트 `ienlab` 1개. 위 컴포넌트 집계와 별도 |

CMP는 하나의 카탈로그 항목에 여러 컴포넌트를 묶고, 웹은 복합 컴포넌트를 여러 설치 항목으로 나누기도 합니다. 따라서 60÷73을 구현률로 계산할 수 없습니다.

기본 입력·선택·로딩 컴포넌트는 양쪽에 상당 부분 구현되어 있습니다. 웹에서 부족한 영역은 CMP의 리스트·다이얼로그·알림·화면 구성 컴포넌트입니다. 반대로 데이터 테이블·일정·보드·계층 선택 같은 복잡한 데이터 UI는 웹이 더 풍부합니다.

웹은 REUI와 shadcn/ui 기반 구조에 IEN 디자인과 CMP 기능을 반영한 상태입니다. 웹 UI 60개 모두 `ienDesign: true`로 등록되어 있지만, 이 메타데이터만으로 시각적 대응 완료를 판단하지 않습니다.

웹 문서의 컴포넌트 분류도 CMP와 같은 용도 기준인 `액션·선택`, `입력`, `레이아웃`, `콘텐츠`, `피드백`, `화면 구성`, `기초` 순서를 사용합니다. 웹 전용 컴포넌트도 역할에 따라 분류하며, 항목이 없는 분류는 표시하지 않습니다. REUI·shadcn/ui 출처 정보는 분류와 별도로 유지합니다.

## 최근 구현·수정 반영 사항

아래는 현재 소스에 반영된 내용입니다. 구현 여부와 실제 화면·상호작용 검증 여부를 구분합니다.

| 영역 | 반영 내용 | 확인 범위 |
| --- | --- | --- |
| CMP·웹 Playground | 미리보기 옆에 컨트롤을 배치하고 각각 스크롤. 좁은 화면에서는 세로 배치. 웹 미리보기 너비는 데스크톱·태블릿·모바일 단일 선택 Segmented 형태로 제공 | 현재 구현 및 앞선 대표 화면 확인. 웹 너비 선택은 Radix ToggleGroup·Button 조합이며 신규 `SegmentedControl`을 사용하는 것은 아님 |
| 웹 Button 로딩 | loading과 disabled의 불투명도 분리, 색상·너비 유지, 아이콘만 로더로 교체, 아이콘 없는 경우 전체 전환. `asChild` 링크의 href·탭 이동·클릭·키보드 활성화 제한. Playground에 아이콘 선택 추가 | 구현·타입 검사·빌드 확인. 수정 후 모든 variant·링크 상호작용 재검증은 남음 |
| 웹 ProgressIndicator | 전달한 색상이 기본 brand에 덮이지 않도록 적용. 버튼 로더는 `currentColor`·투명 트랙 사용 | 구현·빌드 확인 |
| 웹 Chip | Assist·Filter·Input·Suggestion, elevated, 선택·로딩·아이콘·avatar·삭제 제공. 설치 항목·Preview·Playground·CMP 매핑 추가 | 구현 및 앞선 빌드 확인. 모든 상태의 브라우저 검증은 별도 |
| CMP·웹 Filter Chip | 선택하면 leading에 기본 체크 표시. `selectedIcon`으로 교체하거나 null로 숨김. ElevatedFilter도 적용하며, Input·Assist·Suggestion에는 자동 체크를 적용하지 않음 | 양쪽 소스 확인. 플랫폼별 전체 상태 재검증은 별도 |
| 웹 메뉴 | DropdownMenu·ContextMenu·Select·Combobox·Autocomplete의 항목에 공통 body2 14px/20px, 아이콘 16px, 그룹 레이블 caption 12px/16px 적용. Select Group 중복 패딩 제거. 메뉴 안쪽 여백 8px, 공통 바깥 radius 28px로 확대. Popover도 같은 표면·패딩 기준 적용 | 소스·타입 검사·빌드 확인. Dropdown 대표 화면은 확인했으나 최종 여백 수정 후 모든 메뉴 재확인은 남음 |
| 웹 SegmentedControl | 전용 공개 컴포넌트·설치 항목·Preview·Playground·CMP 매핑 추가. 40/48px, Fixed/Fluid, 개별 크기·활성화, 방향키·단일 선택, 선택 항목 자동 노출. 선택 표시·포커스 영역에 CMP 캡슐 곡선 적용. 위치·너비에 800ms medium-bouncy 토큰, 비활성 항목 클릭 시 해당 항목만 흔들림. Fixed는 overflow hidden, Fluid는 가로 스크롤을 유지하며 스크롤바를 숨기고 트랙 밖 모션을 자름 | 브라우저에서 대표 크기·캡슐 모양·선택·방향키 포커스 및 Space 선택 확인. 마지막 overflow·비활성 흔들림 수정 후 화면 재확인과 bouncy 프레임 검증은 남음 |
| 웹 Calendar | 좌우 24px·상하 16px 패딩. 날짜 7열의 최소 너비 확보, 주 번호 표시 시 8열 기준 적용 | 소스·타입 검사·사이트 빌드 확인. 최종 여백의 화면 재확인은 남음 |
| 웹 Switch 기본 콘텐츠 | CMP·웹 모두 `onTrackContent` 기본 미지정. 공통 `trackContent`를 지정한 경우에만 대체 콘텐츠 사용 | 기본 API 확인. Playground의 아이콘 예시는 라이브러리 기본값과 구분 |

근거: [웹 Playground](../cmp-ui-docs/web/web-playground.tsx), [웹 Button](../cmp-ui-web-registry/ui/button.tsx), [웹 Chip](../cmp-ui-web-registry/ui/chip.tsx), [CMP Chip](../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/interactive/IenChip.kt), [공통 메뉴 스타일·흔들림](../cmp-ui-web-registry/lib/ien-menu.ts), [웹 Select](../cmp-ui-web-registry/ui/select.tsx), [웹 SegmentedControl](../cmp-ui-web-registry/ui/segmented-control.tsx), [웹 Calendar](../cmp-ui-web-registry/ui/calendar.tsx), [웹 Switch](../cmp-ui-web-registry/ui/switch.tsx).

## 양쪽에 존재하는 주요 기능

| 기능 | CMP | 웹 | 비교 |
| --- | --- | --- | --- |
| 칩 | Assist / Filter / Input / Suggestion 및 elevated 변형 | `Chip kind="assist / filter / input / suggestion"` | 선택·아이콘·avatar·삭제·로딩·elevated 제공. 양쪽 모두 Filter Chip 선택 시 기본 체크를 leading에 표시하며 `selectedIcon`으로 교체하거나 null로 숨길 수 있음. 웹의 `onRemove`는 독립된 삭제 버튼이며 인라인 텍스트 편집은 제공하지 않음 |
| 기본 버튼 | `IenButton` | `Button` | `fill / weak / line / ghost` 제공 |
| 배지 | `IenBadge` | `Badge` | 양쪽에 크기·색상·표현 변형 존재 |
| 카드 | `IenCard` | `Card` | 웹도 채움·테두리, 색상, 여백, 모서리, elevation 제공 |
| 체크박스 | `IenCircleCheckbox`, `IenDotCheckbox`, `IenLineCheckbox` | `Checkbox` | 웹도 `circle / dot / line` 제공 |
| 라디오 | `IenRadioButton`, `IenRadioGroup` | `RadioGroup` | 기본 선택 기능 대응 |
| 스위치 | `IenSwitch` | `Switch` | 기본 트랙 콘텐츠는 없음. `onTrackContent`·`offTrackContent`로 상태별 콘텐츠를 지정하며, `trackContent`는 양쪽의 대체 콘텐츠로 사용 |
| 분할 선택 | `IenSegmentedControl` | `SegmentedControl` | Small/Large(40/48), Fixed/Fluid, 제어·비제어 선택, 개별 비활성화 제공. 캡슐 선택 표시·bouncy 이동·비활성 항목 클릭 흔들림 구현 |
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
| 당김 갱신 | `IenPullToRefreshBox`, `IenPullToRefreshIndicator` | `PullToRefresh`, `PullToRefreshIndicator` | 56px 기준·최대 96px 이동·세 점 표시. 터치·마우스 당김·휠/트랙패드 상단 오버스크롤·Alt+R, 외부 갱신 상태와 커스텀 인디케이터 제공. 요청·오류 처리는 호출자가 담당 |

## 존재하지만 기능이나 API가 다른 영역

| 영역 | 현재 차이 및 부족한 부분 |
| --- | --- |
| 버튼 로딩 | 양쪽 모두 `loading` 상태와 상호작용 제한을 내장합니다. 웹 `Button`은 색상·콘텐츠 너비를 유지합니다. SVG 또는 `data-icon`으로 식별한 아이콘이 있으면 첫 아이콘만 원형 로더로 바꾸고 텍스트는 유지하며, 없으면 전체 콘텐츠를 로더로 전환합니다. 로더는 `currentColor`를 사용하고 `asChild` 링크의 클릭·키보드 활성화도 제한합니다. |
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
| 플로팅 액션 | `IenFab`, `IenExtendedFab`, `IenFabMenu` | 대응 전용 컴포넌트 없음 |
| 액션 바 | `IenActionBar` | 대응 없음 |
| 리스트 | `IenListRow`, Header / Footer, `IenBoardRow`, `IenTableRow`, Section | CMP의 행·섹션 API에 대응하는 컴포넌트 없음 |
| 스와이프 | `IenSwipeBox` | 대응 없음. 이번 웹 구현 범위에서 제외 |
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

## 디자인 비교

### 확인 범위와 판정 기준

2026-10-26의 현재 소스를 기준으로 비교했습니다. CMP의 `dp / sp`와 웹의 CSS `px`는 같은 물리 크기를 보장하지 않습니다. 아래 숫자는 기본 밀도·글자 배율과 웹 루트 글꼴 16px를 전제로 한 설계 규격 비교입니다. 프로젝트 설정과 사용자 글꼴 배율을 적용하면 달라질 수 있습니다.

- **소스 확인**: 기본 토큰, 컴포넌트의 실제 스타일, 상태 표현, 모션 설정을 확인했습니다.
- **브라우저 확인**: `http://localhost:4800`에서 라이트 테마의 CMP Button·TextField 화면과 웹 Button·Input·Badge·Tabs 화면을 확인했습니다. 웹은 DOM의 실제 크기와 계산된 스타일도 읽었습니다.
- **미검증**: Android·iOS 실기기, 모든 컴포넌트의 다크 테마·모바일·키보드 포커스, 모션의 프레임별 일치 여부는 확인하지 않았습니다. CMP 프리뷰 컨트롤에 선택된 값과 컴포넌트 API 기본값도 구분했습니다.

디자인 차이가 가장 큰 부분은 **타이포그래피와 버튼·입력창의 밀도**입니다. 색상·간격·모서리 토큰과 일부 선택 컨트롤의 규격은 많이 공유하지만, 같은 토큰 이름이나 `data-ien-typography` 지정이 실제 크기 일치까지 보장하지는 않습니다.

### 공통 토큰과 렌더링 방식

| 항목 | CMP | 웹 | 판정 |
| --- | --- | --- | --- |
| 색상 | `IenLightColorTokens`, `IenDarkColorTokens` | 생성기가 CMP 색상에서 CSS 변수를 추출 | 기본 팔레트의 원본은 공유. 컴포넌트별 사용 방식·투명도는 별도 비교 필요 |
| 간격 | 0 / 2 / 4 / 8 / 12 / 16 / 20 / 24 / 32 / 40dp | CMP에서 추출한 값을 rem으로 변환 | 토큰 단계는 대응. 컴포넌트의 고정 Tailwind 여백까지 자동으로 대응하지는 않음 |
| 모서리 반경 | 0 / 4 / 8 / 12 / 16 / 24 / 999dp | CMP에서 추출. `radius-md → ien-radius-sm` 등 별도 별칭 사용 | IEN 이름과 shadcn 별칭 이름을 구분해야 함 |
| 연속 곡률 | `ContinuousRoundedRectangle`, `ContinuousCapsule` | 네이티브 `corner-shape` 및 `@lisse/core`·SVG 보정 | 구현 경로가 다름. 반경이 같아도 모든 종횡비·브라우저에서 동일한 윤곽이라고 단정할 수 없음 |
| 선 두께 | 0.5 / 1 / 1.5 / 2dp. 테두리를 경로 안쪽에 직접 그림 | 공유 토큰과 CSS border 사용 | 웹 border는 박스 계산에 참여하므로 외곽 크기·내부 공간이 달라질 수 있음 |
| elevation | 0 / 4 / 12 / 24dp | 수치와 표면 tint 변수를 생성 | tonal elevation과 그림자를 구분해야 함. CMP `IenSurface`도 `tonalElevation`만으로 외부 그림자를 추가하지 않음 |
| 기본 모션 | 80 / 160 / 240 / 360ms, `(0.2, 0, 0, 1)` | CMP에서 추출 | 공통 시간은 대응. 실제 컴포넌트에는 별도 spring·150ms·200ms 등도 혼재 |
| 상태 토큰 | disabled 0.38, pressed 0.10, focused 0.12, selected 0.14, 최소 터치 44dp | 같은 상태 토큰 생성 | 토큰의 존재와 각 컨트롤 적용 여부는 별개 |
| 글꼴 | 기본 `TextStyle`은 폰트 패밀리 미지정. 문서 앱은 Pretendard 리소스 주입 | Pretendard, 코드에는 JetBrains Mono | 문서 프리뷰의 글꼴과 라이브러리 기본 계약을 구분해야 함. 텍스트 렌더러도 Skia·DOM으로 다름 |

근거: [CMP 테마](../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/foundation/IenTheme.kt), [CMP 색상](../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/foundation/IenColorScheme.kt), [토큰 생성기](../cmp-ui-docs/scripts/generate-api-docs.mjs), [웹 토큰](../cmp-ui-web-registry/tokens.json), [연속 곡률 구현](../cmp-ui-web-registry/lib/ien-continuous-corners.ts), [CMP Surface·테두리](../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/primitives/IenPrimitives.kt).

### 타이포그래피 규격

표의 값은 `글자 크기 / 행간 / 굵기`입니다. CMP는 sp, 웹은 루트 16px 기준 CSS px입니다.

| 단계 | CMP 기본값 | 웹 기본값 | 차이 |
| --- | --- | --- | --- |
| display | 30 / 38 / 700 | 24 / 32 / 700 | 웹 크기 -6, 행간 -6 |
| title1 | 24 / 32 / 700 | 20 / 28 / 600 | 웹 크기 -4, 행간 -4, 굵기 감소 |
| title2 | 20 / 28 / 600 | 18 / 28 / 600 | 웹 크기 -2 |
| title3 | 18 / 26 / 600 | 16 / 24 / 600 | 웹 크기 -2, 행간 -2 |
| body1 | 16 / 24 / 400 | 14 / 20 / 400 | 웹 크기 -2, 행간 -4 |
| body2 | 15 / 22 / 400 | 14 / 20 / 400 | 웹 크기 -1, 행간 -2 |
| label1 | 14 / 20 / 600 | 14 / 20 / 500 | 웹 굵기 감소 |
| label2 | 13 / 18 / 600 | 12 / 16 / 500 | 웹 크기 -1, 행간 -2, 굵기 감소 |
| caption | 12 / 16 / 400 | 12 / 16 / 400 | 토큰 규격 동일 |

웹 타이포그래피는 `tokens.json`에 별도로 정의되어 있고 생성기는 이 값을 사용합니다. CMP에서 추출한 타이포그래피와 이름은 같아도 수치는 다릅니다.

또한 `data-ien-typography`용 규칙은 폰트 패밀리·굵기를 지정하며 크기·행간까지 강제하지 않습니다. 실제 크기는 `text-sm`, `text-base`, `text-ien-*` 같은 클래스와 상속에 따라 달라집니다. 예를 들어 웹 `CardTitle`은 `title2`로 표시되지만 `text-base`를 사용하고, `EmptyTitle`은 `title2` 표시와 함께 `text-sm`을 사용합니다. 단계 메타데이터만 보고 18px이라고 판단하면 안 됩니다.

근거: [CMP 기본 타이포그래피](../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/foundation/IenTheme.kt), [웹 타이포그래피·공통 규칙](../cmp-ui-web-registry/tokens.json), [웹 Card](../cmp-ui-web-registry/ui/card.tsx), [웹 Empty](../cmp-ui-web-registry/ui/empty.tsx).

### 컴포넌트별 외형 차이

| 컴포넌트 | CMP 기준 | 웹 기준 | 디자인 영향·확인 수준 |
| --- | --- | --- | --- |
| 기본 버튼 | Small / Medium / Large 최소 높이 36 / 44 / 52dp. 기본 Large. 좌우 패딩 12 / 16 / 20dp | `xs / sm / default / lg` 높이 24 / 28 / 32 / 36px. 기본 좌우 패딩 10px | **차이 큼**. 웹 기본 버튼은 실제 32px·14px/20px·굵기 500으로 측정. CMP 기본 label1은 굵기 600 |
| 버튼 높이 토큰 | 기본 Large 52dp | `ien-button-height-default`는 52px 상당이지만 실제 `Button`은 `h-8` 사용 | **선언과 적용이 다름**. 높이 토큰이 있다는 이유로 대응 완료로 집계하면 안 됨 |
| 버튼 모서리 | 기본 12dp 연속 곡률. Full 배치는 직각. 호출자가 Shape 지정 가능 | 기본 12px 상당. 크기별 class와 ButtonGroup 규칙 존재 | 기본 반경은 유사하나 Full·Capsule 등 표현 선택과 곡률 렌더러는 다름 |
| 배지 기본 표현 | 기본 `Weak`, Medium, Brand | 기본 `fill`, medium. tone 미지정 시 공통 제어 색상 사용 | **기본 강조도가 다름**. 같은 최소 호출로 같은 외형이 나오지 않음 |
| 배지 크기 | Small / Medium / Large 패딩은 좌우 6 / 8 / 10dp, 위아래 2 / 3 / 5dp. Fill·Weak에는 별도 border 없음 | 같은 패딩 수치에 모든 variant가 1px border 박스를 사용 | 웹 실측 높이 22 / 24 / 32px. CMP 토큰 행간+패딩은 20 / 24 / 30dp. Medium은 전체 높이가 같아도 글자 크기·행간은 다름 |
| 카드 | 기본 반경 16dp, 여백 16dp. 자식은 Box에 배치 | 기본 반경·여백은 대응. 루트는 flex-column과 기본 16px gap, Header / Footer 규칙 제공 | 표면 의도는 가깝지만 여러 자식의 간격·제목 크기·Footer 여백에서 차이 발생 |
| 입력창 | 기본 Box 단일 행 높이 52dp, 본문 16sp/24sp, 좌우 14dp | 기본 boxed 높이 32px, 데스크톱 본문 14px/20px, 좌우 10px | **차이 큼**. 웹 실측 높이·글꼴·여백 확인. 웹 `text-base md:text-sm`은 화면 폭에 따라 글자 크기도 달라짐 |
| 입력 변형 | Box / Line / Big / Hero. Big·Hero는 title2·display와 큰 여백 사용 | boxed / underline / plain | 웹의 plain은 Big·Hero와 다른 표현. 큰 금액·헤드라인 입력 디자인이 빠져 있음 |
| 체크박스·라디오 | 체크박스 circle / dot / line, 기본 circle 24dp·dot 8dp. 라디오와 함께 IEN 색상·선 사용 | 대응 변형과 크기, 1.5px 선, 원형 표시 제공 | 규격은 가까운 편. 웹도 brand를 직접 참조하는 부분이 있어 `tone` 속성만으로 모든 선택 표시 색상이 바뀐다고 판단하면 안 됨 |
| 스위치 | Material3 Switch 기반. 52×32 규격을 전제로 트랙 콘텐츠 배치 | 기본 52×32px, 손잡이 16→24px·눌림 28px. 추가 sm 24×14px | 웹이 기본 비례를 재현. sm은 CMP의 별도 크기 API와 대응하지 않음. 모션은 별도 확인 필요 |
| 탭 | Small / Large 높이 40 / 48dp. 선택 선 두께 2dp, 폭은 항목 폭-24dp·최소 20dp | 같은 높이·선 계산을 구현 | 웹 실측 높이 40 / 48px, 선택 선 2px 확인. Small 글자는 CMP 13sp/18sp, 웹 12px/16px로 차이 |
| 별점 | 아이콘 18 / 24 / 34 / 44 / 56dp, 간격 2 / 2 / 4 / 8 / 8dp | 같은 크기·간격 수치와 비례 채움 구현 | 도형 크기는 가까움. 숫자 레이블의 타이포그래피는 label2 차이의 영향을 받음 |
| 날짜·기간 휠 | 기본 5행, 행 높이는 최소 44dp와 title3 크기를 고려. 텍스트는 title3 | 기본 5행·행 높이 44px, 원통 변형·선택 배경 구현. 공통 휠 항목은 글자 크기를 직접 지정하지 않음 | 행 규격은 유사하나 웹 텍스트는 상속에 따라 달라짐. 팝오버·하단 초기화/확인 버튼은 CMP 휠 단독 구조와 별도 |
| 스켈레톤 | 패턴과 텍스트 단계별 크기, 위상차가 있는 밝기·크기 변화 | 패턴·위상차·밝기·크기 변화 구현 | 레이아웃과 모션 의도는 유사. 웹 body·title 토큰 차이로 텍스트 블록 높이·전체 밀도는 달라질 수 있음 |
| 진행·로딩 표시 | Compose Material3 원형·선형·물결·도형 변형 | SVG·Material 웹 구현과 별도 타이밍·도형 라이브러리 사용 | 대응 표현 존재. 같은 이름만으로 파형·회전 위상·선 끝·프레임 일치까지 확인된 것은 아님 |
| 툴팁 | 12dp 반경, 화살표를 포함한 Shape, 방향별 패딩, 16dp 그림자 | 12px 반경, 별도 12×6px 화살표, 좌우 16px·상하 10px, `0 8px 16px #001d3a26` 그림자 | 몸체·화살표 연결과 그림자 퍼짐이 다른 구현. 글자도 CMP 13sp/18sp, 웹 12px/16px 규격 |
| 시트 | 하단 시트의 높이 단계·확장·드래그와 전용 헤더·선택 목록 | 기본 오른쪽 패널. bottom으로 지정해야 하단 배치 | 초기 외형부터 다름. 웹은 사방향 경계선·슬라이드·닫기 버튼 구조이며 CMP 바텀시트 디자인과 동일하지 않음 |
| 빈 상태 | 기본 항목 간격 8dp, 제목 title2·Bold, 아이콘 프레임과 버튼 슬롯 제공 | 루트 여백 24px·간격 16px, EmptyTitle은 `text-sm font-medium`, icon 변형은 32px 박스·16px 아이콘 | 기능 대응은 있지만 제목 강조·아이콘 비례·전체 여백의 차이가 큼 |

근거: [CMP 버튼](../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/interactive/IenButton.kt), [웹 버튼](../cmp-ui-web-registry/ui/button.tsx), [CMP 배지](../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/interactive/IenBadge.kt), [웹 배지](../cmp-ui-web-registry/reui/badge.tsx), [CMP 입력](../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/interactive/IenTextField.kt), [웹 입력](../cmp-ui-web-registry/ui/input.tsx), [CMP 선택 컨트롤](../cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/interactive/IenSelection.kt), [웹 탭](../cmp-ui-web-registry/ui/tabs.tsx), [웹 휠](../cmp-ui-web-registry/lib/ien-wheel-picker.tsx).

### 색상·상태·모션 차이

| 항목 | 비교 결과 | 확인 수준 |
| --- | --- | --- |
| 기본 색상 | 라이트 brand `#7C4DFF`, textPrimary `#191F28`, 다크 brand `#B69CFF` 등은 CMP 원본에서 생성. 라이트 웹 버튼의 brand 배경도 `rgb(124,77,255)`로 측정 | 소스 및 라이트 웹 실측 |
| 그라데이션 | 양쪽 모두 Fill의 전경색 혼합 18%·36%, Weak의 6%·10%를 사용. CMP `Brush.linearGradient`와 CSS `linear-gradient(135deg, …)`는 좌표·박스·색 보간 경로가 달라 픽셀 일치를 보장하지 않음 | 계수·구현 소스 확인. 모든 종횡비의 픽셀 비교 미실행 |
| 버튼 비활성 | CMP는 surfaceVariant·textDisabled 등 전용 색상으로 바꾸고 그라데이션을 제거. 웹은 원래 tone·그라데이션을 유지하며 명시적 disabled에 `--ien-state-disabled-alpha` 적용 | 소스 확인. 같은 비활성 상태라도 채도·배경 합성 방식이 다름 |
| 버튼 로딩 | 양쪽 모두 활성 색상을 유지하며 입력을 제한. 웹은 loading과 명시적 disabled의 불투명도를 구분하고 기존 콘텐츠로 너비를 보존함. 식별 가능한 첫 아이콘만 로더로 교체하며, 아이콘이 없으면 전체 콘텐츠를 전환. 로더는 currentColor 사용. asChild 링크는 차단 중 href와 탭 이동도 제한 | 수정 소스 확인. 수정 후 브라우저의 실제 외형·클릭·키보드 검증은 미실행 |
| 버튼 눌림 | 양쪽 축소 목표는 0.975. CMP는 검정 오버레이 8% 또는 4%도 표시. 웹 기본 active 규칙에는 축소가 있고 hover에서는 brightness 0.96 사용 | 소스 확인. 눌림·hover의 명암 표현이 같지는 않음 |
| 버튼 스프링 | CMP는 `StiffnessMedium`·`DampingRatioNoBouncy`. 웹은 stiffness 1500·dampingRatio 1을 300ms CSS `linear()`로 표본화 | 목표 특성은 유사. Compose의 실제 수렴 시간·중단 처리와 고정 CSS 구간은 다름 |
| 로딩 전환 | CMP는 콘텐츠·로더의 전환을 Compose 애니메이션으로 구성. 웹은 아이콘 또는 전체 콘텐츠를 유지한 채 opacity·scale을 160ms 전환 | 소스 확인. 폭 유지 방식과 종료 시점의 세부 동작까지 동일하다고 판정하지 않음 |
| 입력 오류 | CMP는 오류 상태 진입 시 -8→8→-6→6→-3→3→0 이동을 각 40ms로 실행. 웹은 테두리·도움말·ring으로 표시하며 같은 오류 진입 흔들림 없음 | 소스 확인 |
| 키보드 포커스 | 웹은 버튼·입력 등에 명시적인 3px focus-visible ring 사용. CMP 버튼에는 동일한 CSS ring에 대응하는 별도 테두리 애니메이션이 없음 | 소스 확인. 네이티브 포커스 표시의 실제 외형은 플랫폼 검증 필요 |
| 탭 선택 선 | CMP는 `MediumBouncy / MediumLow` spring, 웹은 stiffness 400·dampingRatio 0.5 표본을 800ms로 재생 | 소스 및 웹 계산 스타일 0.8s 확인. 이동·크기 변화의 수렴 특성은 다름 |
| 스위치 | 웹 손잡이·트랙 콘텐츠에는 300ms 표본 spring 사용. CMP 트랙 콘텐츠는 `NoBouncy / Medium` spring이며 손잡이는 Material3 동작 사용 | 소스 확인. 고정 크기가 비슷해도 움직임의 완전 일치는 미검증 |
| 모션 줄이기 | 웹은 여러 컴포넌트에 `prefers-reduced-motion` 대응을 명시. CMP는 Compose·플랫폼 애니메이션 처리에 의존하는 부분 존재 | 소스 확인. OS별 모션 축소 결과는 미검증 |

### 디자인 정렬 시 우선 확인할 항목

1. **타이포그래피 기준 결정**: 웹 전용으로 축소한 단계와 CMP 동일 단계 중 의도한 규격을 결정합니다. 모든 플랫폼의 수치를 무조건 같게 바꾸는 것은 이 분석의 결론이 아닙니다.
2. **버튼·입력창 밀도 정렬**: 실제 `h-8`·패딩·글자 크기와 선언된 높이 토큰의 관계를 정리합니다. 특히 기본 버튼·입력 32px와 CMP 52dp 차이를 우선 다룹니다.
3. **상태 표현 정렬**: 비활성 색상, 로딩 중 opacity, 눌림 오버레이, 오류 흔들림, 포커스 표시를 각각 비교합니다.
4. **타이포그래피 표기와 실제 클래스 정렬**: CardTitle·EmptyTitle·휠처럼 메타데이터나 토큰 이름만으로 실제 크기를 알 수 없는 부분을 확인합니다.
5. **곡률·테두리·모션의 화면 검증**: Fill / Weak / Line / Ghost와 캡슐을 모두 확인하고, 다크 테마·모바일·실기기·상태 전환에서 차이를 재확인합니다.

이 절은 남아 있는 디자인 차이를 기록합니다. 최근 실제 스타일 변경과 검증 범위는 위의 「최근 구현·수정 반영 사항」을 함께 참고합니다.

## 이번 웹 구현 범위 조정

- Dialog 계열은 보류합니다.
- FAB·FAB Menu, 전용 키패드, SwipeBox, 앱 전용 Scaffold·TopBar·BottomBar·NavigationBar, 모바일 전용 하단 구성, 네이티브 권한 안내, PrefsScreen·플랫폼 저장 연동, 범용 AnimatedRow·AnimatedColumn은 제외합니다. 기존 웹 컴포넌트는 삭제하지 않습니다.
- Sheet의 기본 하단 배치와 선택 목록은 유지하되, 모바일식 다단 높이·드래그 확장은 제외합니다.
- PullToRefresh는 예외적으로 구현했습니다. 설치 항목·CMP 매핑·Preview·Playground를 연결했습니다. 상단에서 위쪽 휠/트랙패드 입력을 누적하고, 표시 거리가 56px를 넘은 뒤 입력이 180ms 멈추면 갱신합니다. 브라우저의 터치 제스처·실기기 검증은 별도로 남아 있습니다.

## 문서와 매핑의 부족한 부분

### CMP 연결 메타데이터

웹 UI 60개 중 `cmpIds`가 설정된 것은 다음 7개입니다.

- `chip`
- `rating`
- `skeleton`
- `progress-indicator`
- `loading-indicator`
- `segmented-control`
- `pull-to-refresh`

버튼·카드·체크박스·탭·휠 선택기 등은 실제 대응 구현이 있지만 `cmpIds`가 비어 있습니다. 문서 생성기는 이 필드로 CMP 카탈로그 연결을 찾으므로 실제 대응 범위가 문서에 충분히 드러나지 않습니다.

매핑 시에는 이름보다 역할을 기준으로 확인해야 합니다. 예를 들어 숫자형 `IenStepper`는 웹 `NumberField`, 진행형 `IenStepper`는 웹 `Stepper`·`Timeline`과 비교해야 합니다. 세 휠 선택기는 CMP 카탈로그의 `wheel-picker` 한 항목으로 묶여 있습니다.

### 문서와 현재 구현의 차이

이 문서의 이전 내용은 REUI 기본 구현을 가져온 당시 상태를 설명하며 IEN 디자인과 CMP 일대일 매핑을 포함하지 않는다고 안내했습니다. 현재 소스에는 IEN 디자인과 CMP 대응 API가 반영되어 있으므로 현재 비교 내용으로 갱신했습니다. 다만 실제 화면의 시각적 일치 여부는 별도 검증이 필요합니다.

## 보완 우선순위 제안

웹의 CMP 대응을 높이는 목적일 때의 우선순위입니다. 구현이 확정된 작업 목록은 아닙니다.

1. **Dialog / AlertDialog, Toast / Snackbar**: 기본 사용자 흐름에서 빠진 피드백 수단을 보완합니다.
2. **Slider / RangeSlider**: 기본 입력·선택 영역을 보완합니다.
3. **ListRow / Section, 버튼 toggle·아이콘 toggle, 입력 확장**: 반복적인 개별 구현을 줄이고 상태별 표현을 정렬합니다.
4. **BottomSheet 높이·드래그, 화면 골격·CTA**: CMP 화면 구성 패턴을 대응합니다.
5. **`cmpIds` 정비**: 확인된 대응 관계를 문서 생성에 반영합니다.

### 남은 작업 정리

- **새 대응 컴포넌트**: Toast·Snackbar, Slider·RangeSlider, ListRow·Section, ActionBar, 약관 등. Dialog 계열은 보류하며, 위의 「이번 웹 구현 범위 조정」에서 제외한 항목은 수행하지 않습니다.
- **기존 API 확장**: 버튼·아이콘 toggle, 전용 Split Button, 검색·비밀번호·분할 입력과 포맷, 바텀시트 선택 목록, 휠 선택기 사이의 확인 절차·폼·열림 제어 차이.
- **디자인 정렬**: 웹 타이포그래피와 버튼·입력 높이, CardTitle·EmptyTitle·휠의 실제 텍스트 규격, 비활성·포커스·오류 상태 표현. 메뉴·캘린더 여백과 SegmentedControl 캡슐·모션은 구현에 반영했으므로 신규 구현 대기 항목으로 취급하지 않음.
- **문서 연결**: 아직 `cmpIds`가 없는 실제 대응 컴포넌트의 매핑. 현재 매핑은 7개이며 대응 UI 전체가 아님.
- **추가 검증**: 최종 메뉴·캘린더 여백, SegmentedControl 양끝 bouncy 넘침·Fluid 스크롤·비활성 흔들림, Button의 모든 variant·아이콘·asChild 링크. 다크 테마·모바일·접근성·실기기 및 자동 테스트 전체 결과는 별도 확인 필요.

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
