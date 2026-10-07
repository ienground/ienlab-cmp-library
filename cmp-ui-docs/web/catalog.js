import { sampleLines } from "./generated-sample-lines.js";
const uiSource = "cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/";
const sampleSource =
  "example/composeApp/src/commonMain/kotlin/zone/ien/utils/example/ui/screens/designsystem/DesignSystemScreen.kt";

function component(
  id,
  name,
  category,
  description,
  api,
  sourceFile,
  module = "cmp-ui",
  packageName,
) {
  const sampleLine = sampleLines[id] ?? 1;
  const source =
    module === "cmp-ui" ? uiSource + sourceFile : sampleSource + "#L" + sampleLine;
  const resolvedPackage =
    packageName ??
    (module === "cmp-ui"
      ? "zone.ien.utils.ui." + sourceFile.replace(/\/[^/]+\.kt$/, "").replaceAll("/", ".")
      : "androidx.compose.animation");

  return {
    id,
    name,
    category,
    description,
    api,
    module,
    packageName: resolvedPackage,
    source,
    sample: sampleSource + "#L" + sampleLine,
  };
}

export const catalog = [
  component("wheel-picker", "WheelPicker", "입력", "날짜·정확한 시각·시간/분/초 기간을 휠로 선택합니다.", ["IenDateWheelPicker", "IenTimeWheelPicker", "IenDurationWheelPicker"], "interactive/IenDateTimeWheelPicker.kt"),
  component("animated-layout", "AnimatedLayout", "레이아웃", "목록 항목이 추가되거나 제거될 때 크기와 표시 상태를 전환합니다.", ["IenAnimatedColumn", "IenAnimatedRow"], "layout/IenAnimatedLayout.kt"),
  component("animated-content", "AnimatedContent", "레이아웃", "상태가 바뀔 때 콘텐츠 전환 애니메이션을 확인합니다.", ["AnimatedContent"], "", "Compose UI"),
  component("badge", "Badge", "콘텐츠", "상태나 짧은 보조 정보를 작은 레이블로 표시합니다.", ["IenBadge"], "interactive/IenBadge.kt"),
  component("board-row", "BoardRow", "콘텐츠", "콘텐츠와 액션을 한 행에 배치하는 보드 행입니다.", ["IenBoardRow"], "list/IenBoardRow.kt"),
  component("border", "Border", "레이아웃", "구분선과 간격 변형을 확인합니다.", ["IenBorder", "IenBorderVariant"], "layout/IenLayout.kt"),
  component("bottom-info", "BottomInfo", "레이아웃", "하단 안내 영역과 그라데이션을 표시합니다.", ["IenBottomInfo"], "layout/IenLayout.kt"),
  component("bottom-sheet", "BottomSheet", "피드백", "선택 항목과 펼침 동작이 포함된 하단 시트입니다.", ["IenBottomSheet", "IenBottomSheetSelect"], "feedback/IenFeedback.kt"),
  component("bubble", "Bubble", "콘텐츠", "메시지 방향과 꼬리 유무에 따른 말풍선 변형입니다.", ["IenBubble"], "content/IenContent.kt"),
  component("button", "Button", "액션·선택", "버튼의 색상, 크기, 너비, 토글 상태를 확인합니다.", ["IenButton", "IenToggleButton"], "interactive/IenButton.kt"),
  component("card", "Card", "콘텐츠", "카드 표면과 의미에 따른 색상 변형을 표시합니다.", ["IenCard"], "content/IenCard.kt"),
  component("chip", "Chip", "액션·선택", "선택·입력·제안 목적의 칩을 비교합니다.", ["IenAssistChip", "IenFilterChip", "IenInputChip"], "interactive/IenChip.kt"),
  component("fab", "FAB", "액션·선택", "주요 동작을 강조하는 플로팅 액션 버튼입니다.", ["IenFab", "IenExtendedFab"], "interactive/IenButton.kt"),
  component("checkbox", "Checkbox", "액션·선택", "원형·선형 체크박스의 상태를 비교합니다.", ["IenCircleCheckbox", "IenLineCheckbox"], "interactive/IenSelection.kt"),
  component("highlight", "Highlight", "콘텐츠", "문장 안의 강조 구간을 표현합니다.", ["IenHighlightText"], "content/IenContent.kt"),
  component("icon-button", "IconButton", "액션·선택", "아이콘 버튼과 토글 버튼의 상태를 확인합니다.", ["IenIconButton", "IenIconToggleButton"], "interactive/IenButton.kt"),
  component("list-footer", "ListFooter", "콘텐츠", "목록 하단의 추가 정보와 구분선을 표시합니다.", ["IenListFooter"], "list/IenRows.kt"),
  component("list-header", "ListHeader", "콘텐츠", "목록 제목과 설명을 정렬해 표시합니다.", ["IenListHeader"], "list/IenRows.kt"),
  component("loading-indicator", "LoadingIndicator", "피드백", "다각형이 변하는 로딩 애니메이션을 확인합니다.", ["IenLoadingIndicator"], "feedback/IenProgress.kt"),
  component("loader", "Loader", "피드백", "대기 중 상태를 나타내는 로더를 확인합니다.", ["IenLoader"], "feedback/IenFeedback.kt"),
  component("menu", "Menu", "액션·선택", "메뉴 항목과 선택 동작을 확인합니다.", ["IenMenu"], "menu/IenMenuModal.kt"),
  component("modal", "Modal", "피드백", "화면 위에 표시되는 모달과 닫기 동작입니다.", ["IenModal"], "menu/IenMenuModal.kt"),
  component("numeric-spinner", "NumericSpinner", "입력", "숫자 증감과 크기별 스피너를 확인합니다.", ["IenNumericSpinner"], "interactive/IenInputExtras.kt"),
  component("paragraph", "Paragraph", "콘텐츠", "본문과 보조 문장을 표현하는 문단입니다.", ["IenParagraph"], "content/IenTextContent.kt"),
  component("post", "Post", "콘텐츠", "작성자·날짜·본문으로 구성된 게시물 카드입니다.", ["IenPost"], "content/IenTextContent.kt"),
  component("progress-bar", "ProgressBar", "피드백", "진행률과 두께에 따른 진행 표시를 비교합니다.", ["IenProgressBar"], "feedback/IenFeedback.kt"),
  component("progress-indicator", "ProgressIndicator", "피드백", "원형·물결·선형 진행 표시기를 비교합니다.", ["IenCircularProgressIndicator", "IenCircularWavyProgressIndicator", "IenLinearProgressIndicator"], "feedback/IenProgress.kt"),
  component("progress-stepper", "ProgressStepper", "피드백", "단계별 진행 상태와 레이아웃 변형을 표시합니다.", ["IenProgressStepper", "IenProgressStep"], "feedback/IenFeedback.kt"),
  component("rating", "Rating", "입력", "편집 가능, 읽기 전용, 비활성 별점을 비교합니다.", ["IenRating"], "interactive/IenInputExtras.kt"),
  component("result", "Result", "피드백", "완료·실패·빈 상태의 결과 화면을 구성합니다.", ["IenResult"], "feedback/IenFeedback.kt"),
  component("search-field", "SearchField", "입력", "검색어 입력, 삭제, 비활성 상태를 확인합니다.", ["IenSearchField"], "interactive/IenTextField.kt"),
  component("segmented-control", "SegmentedControl", "액션·선택", "세그먼트 선택과 정렬·크기 변형입니다.", ["IenSegmentedControl", "IenSegmentedControlItem"], "interactive/IenSelection.kt"),
  component("skeleton", "Skeleton", "피드백", "콘텐츠 로딩 형태와 반복 패턴을 표현합니다.", ["IenSkeleton", "IenSkeletonMotionGroup", "IenSkeletonPattern"], "feedback/IenFeedback.kt"),
  component("slider", "Slider", "입력", "값 범위와 단계에 따른 슬라이더 동작입니다.", ["IenSlider"], "interactive/IenControls.kt"),
  component("swipe-box", "SwipeBox", "액션·선택", "스와이프에 연결된 보조 액션을 확인합니다.", ["IenSwipeBox", "IenSwipeBoxItem"], "list/IenSwipeBox.kt"),
  component("stepper", "Stepper", "입력", "단계 이동과 텍스트·아이콘 구성을 확인합니다.", ["IenStepper", "IenStepperTexts", "IenStepperTextsType"], "interactive/IenControls.kt"),
  component("switch", "Switch", "액션·선택", "켜짐·꺼짐 상태를 전환하는 스위치입니다.", ["IenSwitch"], "interactive/IenSelection.kt"),
  component("tab", "Tab", "액션·선택", "탭 선택과 하단 탭 표시를 비교합니다.", ["IenTab"], "interactive/IenControls.kt"),
  component("table-row", "TableRow", "콘텐츠", "열 정렬과 경계선이 있는 표 행입니다.", ["IenTableRow"], "list/IenRows.kt"),
  component("text-button", "TextButton", "액션·선택", "텍스트 버튼의 크기와 강조 수준을 비교합니다.", ["IenTextButton"], "interactive/IenButton.kt"),
  component("snackbar", "Snackbar", "피드백", "기본·성공·액션·대기열 스낵바를 확인합니다.", ["IenSnackbar", "IenSnackbarHost"], "feedback/IenFeedback.kt"),
  component("toast", "Toast", "피드백", "기본·성공 토스트 표시와 닫기를 확인합니다.", ["IenToast", "showIenToast"], "feedback/IenFeedback.kt"),
  component("tooltip", "Tooltip", "피드백", "위치와 콘텐츠에 따른 툴팁을 확인합니다.", ["IenTooltip"], "screen/IenScreenParts.kt"),
  component("top", "Top", "화면 구성", "화면 상단의 제목·부제목·액션 조합입니다.", ["IenTop", "IenTopBar"], "screen/IenScreenParts.kt"),
  component("agreement", "Agreement", "화면 구성", "약관 본문과 선택 항목의 상태를 확인합니다.", ["IenAgreement", "IenAgreementCheckbox", "IenAgreementVariant"], "screen/IenScreenParts.kt"),
  component("asset", "Asset", "콘텐츠", "이미지와 아이콘을 담는 에셋 프레임입니다.", ["IenAssetFrame"], "primitives/IenAsset.kt"),
  component("bottom-bar", "BottomBar", "화면 구성", "액션과 플로팅 액션 버튼을 조합하는 화면 하단 바입니다.", ["IenBottomBar", "IenBottomBarDefaults"], "screen/IenBottomBar.kt"),
  component("chat-bottom-bar", "ChatBottomBar", "화면 구성", "메시지 입력과 전송 동작이 포함된 채팅 하단 바입니다.", ["IenChatBottomBar"], "screen/IenChatBottomBar.kt"),
  component("navigation-bar", "NavigationBar", "화면 구성", "선택 상태와 배지를 포함한 하단 내비게이션 바입니다.", ["IenNavigationBar", "IenNavigationBar2", "IenNavigationBarType", "IenNavigationBarItem", "IenNavigationBarColors", "IenNavigationBarDefaults"], "view/NavigationBar.kt"),
  component("bottom-cta", "BottomCTA", "화면 구성", "화면 하단의 주요 액션 영역을 표시합니다.", ["IenBottomCTA", "IenFixedBottomCTA"], "screen/IenScreenParts.kt"),
  component("dialog", "Dialog", "피드백", "확인·취소 동작을 포함한 다이얼로그입니다.", ["IenDialog"], "feedback/IenFeedback.kt"),
  component("alert-dialog", "AlertDialog", "피드백", "알림·확인·취소 상태에 맞는 다이얼로그 변형입니다.", ["IenAlertDialog", "IenConfirmDialog"], "dialog/IenDialogs.kt"),
  component("keypad", "Keypad", "입력", "문자·숫자·보안 키패드 입력을 확인합니다.", ["IenAlphabetKeypad", "IenNumberKeypad", "IenFullSecureKeypad"], "interactive/IenKeypads.kt"),
  component("list-row", "ListRow", "콘텐츠", "텍스트·에셋·보조 상태를 조합하는 목록 행입니다.", ["IenListRow", "IenListRowTexts", "IenListRowTextsType"], "list/IenRows.kt"),
  component("text-field", "TextField", "입력", "텍스트 입력과 입력 상태별 표현을 확인합니다.", ["IenTextField", "IenClearableTextField"], "interactive/IenTextField.kt"),
  component("split-text-field", "SplitTextField", "입력", "분할 입력 칸과 포커스 이동을 확인합니다.", ["IenSplitTextField"], "interactive/IenTextField.kt"),
  component("text-area", "TextArea", "입력", "여러 줄 입력과 높이 변형을 확인합니다.", ["IenTextArea"], "interactive/IenTextField.kt"),
  component("primitives", "Primitives", "기초", "공통 표면과 아이콘 기본 요소입니다.", ["IenSurface", "IenIcon"], "primitives/IenPrimitives.kt"),
];

export const categories = [...new Set(catalog.map((item) => item.category))];
