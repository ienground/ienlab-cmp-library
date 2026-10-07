package zone.ien.utils.example.ui.screens.designsystem

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.fill.Add
import zone.ien.utils.icon.material.filled.Check
import zone.ien.utils.icon.material.filled.Close
import zone.ien.utils.icon.material.filled.CloudOff as FilledCloudOff
import zone.ien.utils.icon.material.filled.Keyboard as FilledKeyboard
import zone.ien.utils.icon.material.filled.MoreVert as FilledMoreVert
import zone.ien.utils.icon.material.filled.Save as FilledSave
import zone.ien.utils.icon.material.rounded.Check as RoundedCheck
import zone.ien.utils.icon.material.rounded.CloudOff as RoundedCloudOff
import zone.ien.utils.icon.material.rounded.Keyboard as RoundedKeyboard
import zone.ien.utils.icon.material.rounded.MoreVert as RoundedMoreVert
import zone.ien.utils.icon.material.rounded.Save as RoundedSave
import zone.ien.utils.ui.screen.IenAgreementItem
import zone.ien.utils.ui.screen.IenAgreement
import zone.ien.utils.ui.screen.IenAgreementVariant
import zone.ien.utils.ui.screen.IenAgreementText
import zone.ien.utils.ui.screen.IenAgreementCheckbox
import zone.ien.utils.ui.screen.IenAgreementCheckboxVariant
import zone.ien.utils.ui.screen.IenAgreementNecessity
import zone.ien.utils.ui.screen.IenAgreementNecessityVariant
import zone.ien.utils.ui.screen.IenAgreementBadge
import zone.ien.utils.ui.screen.IenAgreementBadgeVariant
import zone.ien.utils.ui.screen.IenAgreementRightArrow
import zone.ien.utils.ui.screen.IenAgreementDescription
import zone.ien.utils.ui.screen.IenAgreementDescriptionVariant
import zone.ien.utils.ui.screen.IenAgreementGroup
import zone.ien.utils.ui.screen.IenAgreementCollapsible
import zone.ien.utils.ui.screen.IenAgreementCollapsibleTrigger
import zone.ien.utils.ui.screen.IenAgreementCollapsibleContent
import zone.ien.utils.ui.screen.IenAgreementIndentPushable
import zone.ien.utils.ui.screen.IenAgreementIndentPushableTrigger
import zone.ien.utils.ui.screen.IenAgreementIndentPushableContent
import zone.ien.utils.ui.dialog.IenAlertDialog
import zone.ien.utils.ui.primitives.IenAssetFrame
import zone.ien.utils.ui.primitives.IenAssetFrameShape
import zone.ien.utils.ui.primitives.IenAssetFrameSize
import zone.ien.utils.ui.list.IenBoardRow
import zone.ien.utils.ui.layout.IenBorder
import zone.ien.utils.ui.layout.IenBorderVariant
import zone.ien.utils.ui.layout.IenAnimatedColumn
import zone.ien.utils.ui.layout.IenAnimatedRow
import zone.ien.utils.ui.screen.IenBottomCTA
import zone.ien.utils.ui.screen.IenBottomBar
import zone.ien.utils.ui.screen.IenChatBottomBar
import zone.ien.utils.ui.screen.IenBottomCTAAnimation
import zone.ien.utils.ui.screen.IenBottomCTABackground
import zone.ien.utils.ui.screen.IenBottomCTAButton
import zone.ien.utils.ui.screen.IenBottomCTAShowAfterDelay
import zone.ien.utils.ui.layout.IenBottomInfo
import zone.ien.utils.ui.layout.IenBottomGradient
import zone.ien.utils.ui.feedback.IenBottomSheet
import zone.ien.utils.ui.feedback.IenBottomSheetOption
import zone.ien.utils.ui.feedback.IenBottomSheetSelect
import zone.ien.utils.ui.content.IenBubble
import zone.ien.utils.ui.content.IenBubbleBackground
import zone.ien.utils.ui.content.IenBubbleTail
import zone.ien.utils.ui.content.IenCard
import zone.ien.utils.ui.content.IenCardDefaults
import zone.ien.utils.ui.content.IenCardToneVariant
import zone.ien.utils.ui.content.IenCardVariant
import zone.ien.utils.ui.dialog.IenAlertDialogAlertButton
import zone.ien.utils.ui.dialog.IenAlertDialogDescription
import zone.ien.utils.ui.dialog.IenAlertDialogTitle
import zone.ien.utils.ui.dialog.IenConfirmDialog
import zone.ien.utils.ui.dialog.IenConfirmDialogCancelButton
import zone.ien.utils.ui.dialog.IenConfirmDialogConfirmButton
import zone.ien.utils.ui.dialog.IenConfirmDialogTitle
import zone.ien.utils.ui.dialog.IenDialogButtonLayout
import zone.ien.utils.ui.feedback.IenDialog
import zone.ien.utils.ui.feedback.IenDialogAction
import zone.ien.utils.ui.screen.IenDoubleBottomCTA
import zone.ien.utils.ui.screen.IenFixedBottomCTA
import zone.ien.utils.ui.screen.IenFixedDoubleBottomCTA
import zone.ien.utils.ui.content.IenHighlightText
import zone.ien.utils.ui.list.IenListFooter
import zone.ien.utils.ui.list.IenListFooterBorder
import zone.ien.utils.ui.list.IenListFooterDefaults
import zone.ien.utils.ui.list.IenListHeader
import zone.ien.utils.ui.list.IenListHeaderDescriptionPosition
import zone.ien.utils.ui.list.IenListRow
import zone.ien.utils.ui.list.IenListRowAlignment
import zone.ien.utils.ui.list.IenListRowAssetShape
import zone.ien.utils.ui.list.IenListRowAssetSize
import zone.ien.utils.ui.list.IenListRowAssetText
import zone.ien.utils.ui.list.IenListRowBorder
import zone.ien.utils.ui.list.IenListRowDisabledStyle
import zone.ien.utils.ui.list.IenListRowLoader
import zone.ien.utils.ui.list.IenListRowLoaderType
import zone.ien.utils.ui.list.IenListRowPadding
import zone.ien.utils.ui.list.IenListRowTexts
import zone.ien.utils.ui.list.IenListRowTextsType
import zone.ien.utils.ui.list.IenSwipeBox
import zone.ien.utils.ui.list.IenSwipeBoxItem
import zone.ien.utils.ui.feedback.IenCircularProgressIndicator
import zone.ien.utils.ui.feedback.IenCircularWavyProgressIndicator
import zone.ien.utils.ui.feedback.IenLinearProgressIndicator
import zone.ien.utils.ui.feedback.IenLoadingIndicator
import zone.ien.utils.ui.feedback.IenLoader
import zone.ien.utils.ui.feedback.IenLoaderSize
import zone.ien.utils.ui.menu.IenMenu
import zone.ien.utils.ui.menu.IenModal
import zone.ien.utils.ui.content.IenParagraph
import zone.ien.utils.ui.content.IenPost
import zone.ien.utils.ui.feedback.IenProgressBar
import zone.ien.utils.ui.feedback.IenProgressBarSize
import zone.ien.utils.ui.feedback.IenProgressStep
import zone.ien.utils.ui.feedback.IenProgressStepper
import zone.ien.utils.ui.feedback.IenProgressStepperPaddingTop
import zone.ien.utils.ui.feedback.IenProgressStepperVariant
import zone.ien.utils.ui.feedback.IenResult
import zone.ien.utils.ui.feedback.IenResultTone
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import zone.ien.utils.ui.feedback.IenSheetDetent
import zone.ien.utils.ui.feedback.IenSkeleton
import zone.ien.utils.ui.feedback.IenSkeletonBackground
import zone.ien.utils.ui.feedback.IenSkeletonElement
import zone.ien.utils.ui.feedback.IenSkeletonMotionGroup
import zone.ien.utils.ui.feedback.IenSkeletonPattern
import zone.ien.utils.ui.feedback.IenSkeletonPlay
import zone.ien.utils.ui.feedback.IenSkeletonRepeat
import zone.ien.utils.ui.list.IenTableRow
import zone.ien.utils.ui.list.IenTableRowAlign
import zone.ien.utils.ui.feedback.IenSnackbarHost
import zone.ien.utils.ui.feedback.IenToastDuration
import zone.ien.utils.ui.feedback.IenToastState
import zone.ien.utils.ui.feedback.LocalIenToastState
import zone.ien.utils.ui.feedback.showIenSnackbar
import zone.ien.utils.ui.feedback.showIenToast
import zone.ien.utils.ui.screen.IenTooltip
import zone.ien.utils.ui.screen.IenTooltipClipToEnd
import zone.ien.utils.ui.screen.IenTooltipMessageAlign
import zone.ien.utils.ui.screen.IenTooltipMotionVariant
import zone.ien.utils.ui.screen.IenTooltipPlacement
import zone.ien.utils.ui.screen.IenTooltipStrategy
import zone.ien.utils.ui.screen.IenTop
import zone.ien.utils.ui.screen.IenTopBar
import zone.ien.utils.ui.screen.IenTopLowerButton
import zone.ien.utils.ui.screen.IenTopLowerCTA
import zone.ien.utils.ui.screen.IenTopLowerCTAButton
import zone.ien.utils.ui.screen.IenTopRightAssetContent
import zone.ien.utils.ui.screen.IenTopRightButton
import zone.ien.utils.ui.screen.IenTopRightVerticalAlign
import zone.ien.utils.ui.screen.IenTopSelectorType
import zone.ien.utils.ui.screen.IenTopSubtitleBadge
import zone.ien.utils.ui.screen.IenTopSubtitleBadges
import zone.ien.utils.ui.screen.IenTopSubtitleParagraph
import zone.ien.utils.ui.screen.IenTopSubtitleSelector
import zone.ien.utils.ui.screen.IenTopSubtitleSize
import zone.ien.utils.ui.screen.IenTopSubtitleTextButton
import zone.ien.utils.ui.screen.IenTopTitleParagraph
import zone.ien.utils.ui.screen.IenTopTitleSelector
import zone.ien.utils.ui.screen.IenTopTitleSize
import zone.ien.utils.ui.screen.IenTopTitleTextButton
import zone.ien.utils.ui.screen.IenTopUpperAssetContent
import zone.ien.utils.ui.feedback.rememberIenBottomSheetState
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.view.IenNavigationBar
import zone.ien.utils.ui.view.IenNavigationBar2
import zone.ien.utils.ui.view.IenNavigationBarItem
import zone.ien.utils.ui.view.IenNavigationBarItemDirection
import zone.ien.utils.ui.interactive.IenAlphabetKeyboard
import zone.ien.utils.ui.interactive.IenAlphabetKeypad
import zone.ien.utils.ui.interactive.IenBadge
import zone.ien.utils.ui.interactive.IenBadgeSize
import zone.ien.utils.ui.interactive.IenBadgeVariant
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonDefault
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenAssistChip
import zone.ien.utils.ui.interactive.IenChipDefault
import zone.ien.utils.ui.interactive.IenChipState
import zone.ien.utils.ui.interactive.IenElevatedAssistChip
import zone.ien.utils.ui.interactive.IenElevatedFilterChip
import zone.ien.utils.ui.interactive.IenElevatedSuggestionChip
import zone.ien.utils.ui.interactive.IenFilterChip
import zone.ien.utils.ui.interactive.IenInputChip
import zone.ien.utils.ui.interactive.IenSuggestionChip
import zone.ien.utils.ui.interactive.IenCircleCheckbox
import zone.ien.utils.ui.interactive.IenClearableTextField
import zone.ien.utils.ui.interactive.IenFullSecureKeyboard
import zone.ien.utils.ui.interactive.IenFullSecureKeypad
import zone.ien.utils.ui.interactive.IenExtendedFab
import zone.ien.utils.ui.interactive.IenFab
import zone.ien.utils.ui.interactive.IenFabSize
import zone.ien.utils.ui.interactive.IenIconButton
import zone.ien.utils.ui.interactive.IenIconToggleButton
import zone.ien.utils.ui.interactive.IenKeyboardAction
import zone.ien.utils.ui.interactive.IenLineCheckbox
import zone.ien.utils.ui.interactive.IenNumberKeypad
import zone.ien.utils.ui.interactive.IenNumericSpinner
import zone.ien.utils.ui.interactive.IenNumericSpinnerSize
import zone.ien.utils.ui.interactive.IenRating
import zone.ien.utils.ui.interactive.IenRatingSize
import zone.ien.utils.ui.interactive.IenRatingVariant
import zone.ien.utils.ui.interactive.IenSearchField
import zone.ien.utils.ui.interactive.IenSecureKeyboardLanguage
import zone.ien.utils.ui.interactive.IenSecureKeyboardState
import zone.ien.utils.ui.interactive.IenSegmentedControl
import zone.ien.utils.ui.interactive.IenSegmentedControlAlignment
import zone.ien.utils.ui.interactive.IenSegmentedControlItem
import zone.ien.utils.ui.interactive.IenSegmentedControlSize
import zone.ien.utils.ui.interactive.IenSlider
import zone.ien.utils.ui.interactive.IenDateWheelPicker
import zone.ien.utils.ui.interactive.IenTimeWheelPicker
import zone.ien.utils.ui.interactive.IenDurationWheelPicker
import com.sunnychung.lib.multiplatform.kdatetime.KDate
import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit
import zone.ien.utils.ui.interactive.IenSplitTextField
import zone.ien.utils.ui.interactive.IenStepper
import zone.ien.utils.ui.interactive.IenStepperAssetFrame
import zone.ien.utils.ui.interactive.IenStepperAssetFrameShape
import zone.ien.utils.ui.interactive.IenStepperAssetFrameColors
import zone.ien.utils.ui.interactive.IenStepperAssetFrameDefaults
import zone.ien.utils.ui.interactive.IenStepperNumberIcon
import zone.ien.utils.ui.interactive.IenStepperRightArrow
import zone.ien.utils.ui.interactive.IenStepperRightButton
import zone.ien.utils.ui.interactive.IenStepperTexts
import zone.ien.utils.ui.interactive.IenStepperTextsType
import zone.ien.utils.ui.interactive.IenSwitch
import zone.ien.utils.ui.interactive.IenTab
import zone.ien.utils.ui.interactive.IenTabItem
import zone.ien.utils.ui.interactive.IenTabSize
import zone.ien.utils.ui.interactive.IenTextArea
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.interactive.IenTextButtonSize
import zone.ien.utils.ui.interactive.IenTextButtonVariant
import zone.ien.utils.ui.interactive.IenToggleButton
import zone.ien.utils.ui.interactive.IenToggleButtonDefault
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.interactive.IenTextFieldButton
import zone.ien.utils.ui.interactive.IenTextFieldFormat
import zone.ien.utils.ui.interactive.IenTextFieldLabelOption
import zone.ien.utils.ui.interactive.IenTextFieldLengthLimit
import zone.ien.utils.ui.interactive.IenTextFieldState
import zone.ien.utils.ui.interactive.IenTextFieldVariant
import zone.ien.utils.ui.interactive.IenPasswordTextField
import zone.ien.utils.ui.interactive.rememberIenFullSecureKeypadState
import zone.ien.utils.ui.primitives.IenBorderBox
import zone.ien.utils.ui.primitives.IenClickable
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.dialog.IenAlertDialog
import zone.ien.utils.ui.view.IenEmpty
import kotlinx.coroutines.launch
import zone.ien.utils.utils.checkDecimal
import kotlinx.coroutines.CoroutineScope

private data class DesignSystemComponent(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
)

private val DesignSystemComponents = listOf(
    DesignSystemComponent("wheel-picker", "WheelPicker", "입력", "날짜·정확한 시각·시간/분/초 기간을 휠로 선택합니다."),
    DesignSystemComponent("animated-layout", "AnimatedLayout", "레이아웃", "목록 항목이 추가되거나 제거될 때 크기와 표시 상태를 전환합니다."),
    DesignSystemComponent("animated-content", "AnimatedContent", "레이아웃", "상태가 바뀔 때 콘텐츠 전환 애니메이션을 확인합니다."),
    DesignSystemComponent("badge", "Badge", "콘텐츠", "상태나 짧은 보조 정보를 작은 레이블로 표시합니다."),
    DesignSystemComponent("board-row", "BoardRow", "콘텐츠", "콘텐츠와 액션을 한 행에 배치하는 보드 행입니다."),
    DesignSystemComponent("border", "Border", "레이아웃", "구분선과 간격 변형을 확인합니다."),
    DesignSystemComponent("bottom-info", "BottomInfo", "레이아웃", "하단 안내 영역과 그라데이션을 표시합니다."),
    DesignSystemComponent("bottom-sheet", "BottomSheet", "피드백", "선택 항목과 펼침 동작이 포함된 하단 시트입니다."),
    DesignSystemComponent("bubble", "Bubble", "콘텐츠", "메시지 방향과 꼬리 유무에 따른 말풍선 변형입니다."),
    DesignSystemComponent("button", "Button", "액션·선택", "버튼의 색상, 크기, 너비, 토글 상태를 확인합니다."),
    DesignSystemComponent("card", "Card", "콘텐츠", "카드 표면과 의미에 따른 색상 변형을 표시합니다."),
    DesignSystemComponent("chip", "Chip", "액션·선택", "선택·입력·제안 목적의 칩을 비교합니다."),
    DesignSystemComponent("fab", "FAB", "액션·선택", "주요 동작을 강조하는 플로팅 액션 버튼입니다."),
    DesignSystemComponent("checkbox", "Checkbox", "액션·선택", "원형·선형 체크박스의 상태를 비교합니다."),
    DesignSystemComponent("highlight", "Highlight", "콘텐츠", "문장 안의 강조 구간을 표현합니다."),
    DesignSystemComponent("icon-button", "IconButton", "액션·선택", "아이콘 버튼과 토글 버튼의 상태를 확인합니다."),
    DesignSystemComponent("list-footer", "ListFooter", "콘텐츠", "목록 하단의 추가 정보와 구분선을 표시합니다."),
    DesignSystemComponent("list-header", "ListHeader", "콘텐츠", "목록 제목과 설명을 정렬해 표시합니다."),
    DesignSystemComponent("loading-indicator", "LoadingIndicator", "피드백", "다각형이 변하는 로딩 애니메이션을 확인합니다."),
    DesignSystemComponent("loader", "Loader", "피드백", "대기 중 상태를 나타내는 로더를 확인합니다."),
    DesignSystemComponent("menu", "Menu", "액션·선택", "메뉴 항목과 선택 동작을 확인합니다."),
    DesignSystemComponent("modal", "Modal", "피드백", "화면 위에 표시되는 모달과 닫기 동작입니다."),
    DesignSystemComponent("numeric-spinner", "NumericSpinner", "입력", "숫자 증감과 크기별 스피너를 확인합니다."),
    DesignSystemComponent("paragraph", "Paragraph", "콘텐츠", "본문과 보조 문장을 표현하는 문단입니다."),
    DesignSystemComponent("post", "Post", "콘텐츠", "작성자·날짜·본문으로 구성된 게시물 카드입니다."),
    DesignSystemComponent("progress-bar", "ProgressBar", "피드백", "진행률과 두께에 따른 진행 표시를 비교합니다."),
    DesignSystemComponent("progress-indicator", "ProgressIndicator", "피드백", "원형·물결·선형 진행 표시기를 비교합니다."),
    DesignSystemComponent("progress-stepper", "ProgressStepper", "피드백", "단계별 진행 상태와 레이아웃 변형을 표시합니다."),
    DesignSystemComponent("rating", "Rating", "입력", "편집 가능, 읽기 전용, 비활성 별점을 비교합니다."),
    DesignSystemComponent("result", "Result", "피드백", "완료·실패·빈 상태의 결과 화면을 구성합니다."),
    DesignSystemComponent("search-field", "SearchField", "입력", "검색어 입력, 삭제, 비활성 상태를 확인합니다."),
    DesignSystemComponent("segmented-control", "SegmentedControl", "액션·선택", "세그먼트 선택과 정렬·크기 변형입니다."),
    DesignSystemComponent("skeleton", "Skeleton", "피드백", "콘텐츠 로딩 형태와 반복 패턴을 표현합니다."),
    DesignSystemComponent("slider", "Slider", "입력", "값 범위와 단계에 따른 슬라이더 동작입니다."),
    DesignSystemComponent("swipe-box", "SwipeBox", "액션·선택", "스와이프에 연결된 보조 액션을 확인합니다."),
    DesignSystemComponent("stepper", "Stepper", "입력", "단계 이동과 텍스트·아이콘 구성을 확인합니다."),
    DesignSystemComponent("switch", "Switch", "액션·선택", "켜짐·꺼짐 상태를 전환하는 스위치입니다."),
    DesignSystemComponent("tab", "Tab", "액션·선택", "탭 선택과 하단 탭 표시를 비교합니다."),
    DesignSystemComponent("table-row", "TableRow", "콘텐츠", "열 정렬과 경계선이 있는 표 행입니다."),
    DesignSystemComponent("text-button", "TextButton", "액션·선택", "텍스트 버튼의 크기와 강조 수준을 비교합니다."),
    DesignSystemComponent("snackbar", "Snackbar", "피드백", "기본·성공·액션·대기열 스낵바를 확인합니다."),
    DesignSystemComponent("toast", "Toast", "피드백", "기본·성공 토스트 표시와 닫기를 확인합니다."),
    DesignSystemComponent("tooltip", "Tooltip", "피드백", "위치와 콘텐츠에 따른 툴팁을 확인합니다."),
    DesignSystemComponent("top", "Top", "화면 구성", "화면 상단의 제목·부제목·액션 조합입니다."),
    DesignSystemComponent("agreement", "Agreement", "화면 구성", "약관 본문과 선택 항목의 상태를 확인합니다."),
    DesignSystemComponent("asset", "Asset", "콘텐츠", "이미지와 아이콘을 담는 에셋 프레임입니다."),
    DesignSystemComponent("bottom-bar", "BottomBar", "화면 구성", "액션과 플로팅 액션 버튼을 조합하는 화면 하단 바입니다."),
    DesignSystemComponent("chat-bottom-bar", "ChatBottomBar", "화면 구성", "메시지 입력과 전송 동작이 포함된 채팅 하단 바입니다."),
    DesignSystemComponent("navigation-bar", "NavigationBar", "화면 구성", "선택 상태와 배지를 포함한 하단 내비게이션 바입니다."),
    DesignSystemComponent("bottom-cta", "BottomCTA", "화면 구성", "화면 하단의 주요 액션 영역을 표시합니다."),
    DesignSystemComponent("dialog", "Dialog", "피드백", "확인·취소 동작을 포함한 다이얼로그입니다."),
    DesignSystemComponent("alert-dialog", "AlertDialog", "피드백", "알림·확인·취소 상태에 맞는 다이얼로그 변형입니다."),
    DesignSystemComponent("keypad", "Keypad", "입력", "문자·숫자·보안 키패드 입력을 확인합니다."),
    DesignSystemComponent("list-row", "ListRow", "콘텐츠", "텍스트·에셋·보조 상태를 조합하는 목록 행입니다."),
    DesignSystemComponent("text-field", "TextField", "입력", "텍스트 입력과 입력 상태별 표현을 확인합니다."),
    DesignSystemComponent("split-text-field", "SplitTextField", "입력", "분할 입력 칸과 포커스 이동을 확인합니다."),
    DesignSystemComponent("text-area", "TextArea", "입력", "여러 줄 입력과 높이 변형을 확인합니다."),
    DesignSystemComponent("primitives", "Primitives", "기초", "공통 표면과 아이콘 기본 요소입니다."),
)

private data class PlaygroundChoice(
    val label: String,
    val value: String = label,
)

private data class PlaygroundControl(
    val key: String,
    val label: String,
    val defaultValue: String,
    val isToggle: Boolean = false,
    val isTextInput: Boolean = false,
    val choices: List<PlaygroundChoice> = emptyList(),
    val minValue: Int? = null,
    val maxValue: Int? = null,
    val allowDecimal: Boolean = false,
)

private fun toggleControl(
    key: String,
    label: String,
    defaultValue: Boolean = true,
) = PlaygroundControl(
    key = key,
    label = label,
    defaultValue = defaultValue.toString(),
    isToggle = true,
)

private fun disabledControl() = toggleControl("disabled", "비활성화", false)

private fun choiceControl(
    key: String,
    label: String,
    values: List<String>,
    defaultValue: String = values.first(),
) = PlaygroundControl(
    key = key,
    label = label,
    defaultValue = defaultValue,
    choices = values.map { PlaygroundChoice(it) },
)

private fun numberControl(
    key: String,
    label: String,
    defaultValue: Int,
    minValue: Int,
    maxValue: Int,
    allowDecimal: Boolean = false,
) = PlaygroundControl(
    key = key,
    label = label,
    defaultValue = defaultValue.toString(),
    minValue = minValue,
    maxValue = maxValue,
    allowDecimal = allowDecimal,
)

private fun decimalControl(
    key: String,
    label: String,
    defaultValue: Float,
    minValue: Int,
    maxValue: Int,
) = PlaygroundControl(
    key = key,
    label = label,
    defaultValue = defaultValue.toString(),
    minValue = minValue,
    maxValue = maxValue,
    allowDecimal = true,
)

private fun textControl(
    key: String,
    label: String,
    defaultValue: String,
) = PlaygroundControl(
    key = key,
    label = label,
    defaultValue = defaultValue,
    isTextInput = true,
)

private val ComponentPlaygroundControls = mapOf(
    "wheel-picker" to listOf(
        choiceControl("type", "종류", listOf("Date", "Time", "Duration")),
        disabledControl(),
        toggleControl("use24HourFormat", "24시간제"),
        toggleControl("showHours", "시 표시"),
        toggleControl("showMinutes", "분 표시"),
        toggleControl("showSeconds", "초 표시"),
        numberControl("maxHours", "최대 시간", 99, 1, 999),
    ),
    "animated-layout" to listOf(
        numberControl("itemCount", "항목 수", 2, 1, 6),
        toggleControl("horizontal", "가로 배치", false),
    ),
    "animated-content" to listOf(
        choiceControl("contentState", "표시 상태", listOf("Content", "Loading", "Error")),
    ),
    "badge" to listOf(
        choiceControl("variant", "표시 방식", listOf("Fill", "Weak", "Line")),
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
        choiceControl("size", "크기", listOf("Small", "Medium", "Large")),
    ),
    "board-row" to listOf(toggleControl("initialOpened", "초기 펼침")),
    "border" to listOf(
        choiceControl("variant", "구분선 형태", listOf("Full", "Padding24", "Height16")),
    ),
    "bottom-sheet" to listOf(
        toggleControl("visible", "시트 표시", false),
        choiceControl("detent", "시트 높이", listOf("Content", "Medium", "Full")),
    ),
    "bubble" to listOf(
        choiceControl("background", "배경", listOf("Grey", "Brand")),
        toggleControl("withTail", "꼬리 표시"),
    ),
    "button" to listOf(
        choiceControl("variant", "표시 방식", listOf("Fill", "Weak", "Line", "Ghost")),
        choiceControl("size", "크기", listOf("Small", "Medium", "Large")),
        choiceControl("display", "배치", listOf("Full", "Block", "Inline"), "Block"),
        choiceControl("shape", "모양", listOf("Capsule", "Rounded"), "Capsule"),
        disabledControl(),
        toggleControl("loading", "로딩 상태", false),
    ),
    "card" to listOf(
        choiceControl("variant", "표면", listOf("Filled", "Outlined")),
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }),
        choiceControl("toneVariant", "색상 강도", listOf("Solid", "Weak")),
    ),
    "chip" to listOf(
        choiceControl(
            "variant",
            "유형",
            listOf(
                "Assist",
                "ElevatedAssist",
                "Loading",
                "Filter",
                "ElevatedFilter",
                "Input",
                "Suggestion",
                "ElevatedSuggestion",
                "GradientSuggestion",
            ),
        ),
        toggleControl("selected", "선택 상태", false),
        disabledControl(),
    ),
    "fab" to listOf(
        choiceControl("variant", "표시 방식", listOf("Fill", "Weak", "Line", "Ghost")),
        choiceControl("size", "크기", listOf("Small", "Regular", "Large"), "Regular"),
        disabledControl(),
        toggleControl("loading", "로딩 상태", false),
        toggleControl("extended", "확장 상태", false),
    ),
    "checkbox" to listOf(
        choiceControl("variant", "형태", listOf("Circle", "Line")),
        toggleControl("checked", "선택 상태"),
        disabledControl(),
    ),
    "icon-button" to listOf(
        choiceControl("variant", "표시 방식", listOf("Fill", "Weak", "Line", "Ghost")),
        choiceControl("size", "크기", listOf("Small", "Medium", "Large")),
        disabledControl(),
        toggleControl("loading", "로딩 상태", false),
    ),
    "loader" to listOf(
        choiceControl("size", "크기", listOf("Small", "Medium", "Large"), "Medium"),
        choiceControl("indicator", "표시기", listOf("Circular", "Loading", "Wavy")),
    ),
    "progress-indicator" to listOf(
        choiceControl("variant", "형태", listOf("Circular", "Wavy", "Linear")),
        toggleControl("determinate", "물결 진행률 지정", false),
        numberControl("progress", "물결 진행률 (%)", 64, 0, 100),
    ),
    "menu" to listOf(
        toggleControl("visible", "메뉴 표시", false),
        disabledControl(),
    ),
    "modal" to listOf(toggleControl("visible", "모달 표시", false)),
    "numeric-spinner" to listOf(
        choiceControl("size", "크기", listOf("Tiny", "Small", "Medium", "Large"), "Medium"),
        numberControl("minNumber", "최솟값", 0, 0, 20),
        numberControl("maxNumber", "최댓값", 10, 1, 50),
        disabledControl(),
    ),
    "progress-bar" to listOf(
        numberControl("progress", "진행률 (%)", 64, 0, 100),
        choiceControl("size", "두께", listOf("Light", "Normal", "Bold")),
        toggleControl("animate", "애니메이션"),
    ),
    "progress-stepper" to listOf(
        choiceControl("variant", "형태", IenProgressStepperVariant.entries.map { it.name }),
    ),
    "rating" to listOf(
        numberControl("rating", "별점", 3, 0, 5, allowDecimal = true),
        choiceControl("size", "크기", listOf("Tiny", "Small", "Medium", "Large", "Big"), "Medium"),
        choiceControl("variant", "표시 방식", listOf("Full", "Compact", "IconOnly")),
        toggleControl("readOnly", "읽기 전용", false),
        toggleControl("disabled", "비활성화", false),
    ),
    "search-field" to listOf(disabledControl()),
    "segmented-control" to listOf(
        numberControl("selectedIndex", "선택 항목", 0, 0, 2),
        choiceControl("size", "크기", listOf("Small", "Large")),
        choiceControl("alignment", "정렬", listOf("Fixed", "Fluid")),
        disabledControl(),
    ),
    "slider" to listOf(
        numberControl("value", "값 (%)", 35, 0, 100),
        disabledControl(),
    ),
    "switch" to listOf(
        toggleControl("checked", "켜짐 상태"),
        disabledControl(),
    ),
    "tab" to listOf(
        numberControl("selectedIndex", "선택 탭", 0, 0, 2),
        choiceControl("size", "크기", IenTabSize.entries.map { it.name }, "Large"),
        choiceControl("layout", "배치", listOf("Fixed", "Fluid")),
        disabledControl(),
    ),
    "text-button" to listOf(
        choiceControl("variant", "표시 방식", listOf("Clear", "Arrow", "Underline")),
        choiceControl("size", "크기", listOf("XSmall", "Small", "Medium", "Large", "XLarge", "XXLarge"), "Medium"),
        disabledControl(),
    ),
    "tooltip" to listOf(
        choiceControl("placement", "표시 위치", listOf("Top", "Bottom", "Left", "Right")),
    ),
    "top" to listOf(
        choiceControl("titleSize", "타이틀 크기", IenTopTitleSize.entries.map { it.name }),
        choiceControl("subtitleSize", "서브타이틀 크기", IenTopSubtitleSize.entries.map { it.name }),
        choiceControl("selectorType", "선택기 형태", IenTopSelectorType.entries.map { it.name }),
    ),
    "agreement" to listOf(
        choiceControl("variant", "크기", IenAgreementVariant.entries.map { it.name }, "Large"),
        choiceControl("checkboxVariant", "체크 표시", IenAgreementCheckboxVariant.entries.map { it.name }),
        choiceControl("descriptionVariant", "설명 형태", IenAgreementDescriptionVariant.entries.map { it.name }),
        choiceControl("badgeVariant", "배지 형태", IenAgreementBadgeVariant.entries.map { it.name }),
        choiceControl("necessityVariant", "필수 표시", IenAgreementNecessityVariant.entries.map { it.name }),
        disabledControl(),
    ),
    "asset" to listOf(
        choiceControl("size", "크기", listOf("Small", "Medium", "Large", "ExtraLarge")),
        choiceControl("shape", "모양", listOf("Rounded", "Circle")),
    ),
    "bottom-bar" to listOf(toggleControl("showFab", "플로팅 버튼 표시")),
    "chat-bottom-bar" to listOf(disabledControl()),
    "navigation-bar" to listOf(
        choiceControl("type", "타입", listOf("nav", "nav2")),
        toggleControl("visible", "내비게이션 표시"),
        toggleControl("vertical", "세로 배치", false),
        toggleControl("alwaysShowLabel", "레이블 항상 표시", false),
        numberControl("itemCount", "항목 수", 3, 1, 5),
        numberControl("selectedIndex", "선택 항목", 0, 0, 4),
        numberControl("badge", "배지 수", -1, -1, 120),
        disabledControl(),
    ),
    "bottom-cta" to listOf(
        toggleControl("visible", "CTA 표시"),
        toggleControl("loading", "로딩 상태", false),
        disabledControl(),
    ),
    "keypad" to listOf(disabledControl()),
    "list-row" to listOf(
        choiceControl(
            "textType",
            "텍스트 구조",
            IenListRowTextsType.entries.map { it.name },
            "OneRowTypeA",
        ),
        choiceControl("assetShape", "에셋 모양", IenListRowAssetShape.entries.map { it.name }, "Squircle"),
        choiceControl("assetSize", "에셋 크기", IenListRowAssetSize.entries.map { it.name }, "Medium"),
        choiceControl("border", "구분선", IenListRowBorder.entries.map { it.name }, "Indented"),
        choiceControl("verticalPadding", "상하 여백", IenListRowPadding.entries.map { it.name }, "Medium"),
        choiceControl("horizontalPadding", "좌우 여백", IenListRowPadding.entries.map { it.name }, "Medium"),
        choiceControl("leftAlignment", "좌측 정렬", IenListRowAlignment.entries.map { it.name }, "Center"),
        choiceControl("rightAlignment", "우측 정렬", IenListRowAlignment.entries.map { it.name }, "Center"),
        choiceControl("disabledStyle", "비활성 스타일", IenListRowDisabledStyle.entries.map { it.name }, "Type1"),
        choiceControl("touchEffectColor", "터치 효과 색상", listOf("surfaceVariant", "brandWeak", "successWeak", "dangerWeak")),
        toggleControl("showLeft", "좌측 에셋 표시"),
        toggleControl("showRight", "우측 콘텐츠 표시", false),
        toggleControl("withArrow", "화살표 표시", false),
        toggleControl("withTouchEffect", "터치 효과", false),
        toggleControl("clickable", "클릭 동작", false),
        disabledControl(),
    ),
    "text-field" to listOf(
        choiceControl("variant", "형태", IenTextFieldVariant.entries.map { it.name }),
        disabledControl(),
    ),
    "split-text-field" to listOf(
        numberControl("fieldCount", "입력 칸 수", 4, 2, 8),
        disabledControl(),
    ),
    "text-area" to listOf(
        numberControl("maxLines", "최대 줄 수", 4, 2, 12),
        disabledControl(),
    ),
    "skeleton" to listOf(
        choiceControl("pattern", "패턴", IenSkeletonPattern.entries.map { it.name }, "TopListWithIcon"),
    ),
    "primitives" to listOf(
        choiceControl("shape", "표면 형태", listOf("Rounded", "Circle")),
        disabledControl(),
    ),
)

private val AdditionalComponentPlaygroundControls = mapOf(
    "badge" to listOf(textControl("text", "레이블", "미리보기")),
    "board-row" to listOf(
        textControl("title", "제목", "배송 정보 자세히 보기"),
        toggleControl("showPrefix", "앞 배지 표시"),
    ),
    "bottom-info" to listOf(
        choiceControl("gradient", "하단 그라데이션", listOf("Default", "None", "Custom")),
        choiceControl("background", "배경", listOf("surfaceWeak", "surface", "brandWeak")),
        numberControl("padding", "안쪽 여백", 24, 0, 48),
    ),
    "bottom-sheet" to listOf(
        toggleControl("dismissOnScrimClick", "바깥 영역으로 닫기"),
        toggleControl("showDragHandle", "드래그 핸들 표시"),
        toggleControl("disableDimmer", "배경 어둡게 하지 않기", false),
    ),
    "border" to listOf(
        numberControl("thickness", "두께", 1, 1, 8),
        choiceControl("tone", "색상", IenSemanticTone.entries.map { it.name }, "Neutral"),
    ),
    "bubble" to listOf(
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Neutral"),
        textControl("text", "메시지", "의미 색상과 꼬리 방향도 조정할 수 있습니다."),
    ),
    "button" to listOf(
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
        textControl("text", "버튼 레이블", "속성 적용 미리보기"),
        numberControl("contentPadding", "안쪽 여백", 16, 0, 32),
        numberControl("verticalContentPadding", "상하 안쪽 여백", 8, 0, 32),
    ),
    "card" to listOf(
        choiceControl("shape", "모양", listOf("Rounded", "Capsule"), "Rounded"),
        numberControl("contentPadding", "안쪽 여백", 16, 0, 32),
    ),
    "chip" to listOf(textControl("text", "레이블", "Assist")),
    "fab" to listOf(
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
        textControl("text", "버튼 레이블", "작성하기"),
    ),
    "icon-button" to listOf(choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand")),
    "checkbox" to listOf(
        toggleControl("showLabel", "레이블 표시"),
        textControl("label", "레이블", "동의"),
        numberControl("size", "크기", 24, 16, 40),
    ),
    "highlight" to listOf(
        textControl("text", "문장", "Highlight는 검색 결과나 본문 안의 중요한 텍스트를 토큰 색상으로 강조합니다."),
        textControl("highlight", "강조할 단어", "Highlight"),
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
        toggleControl("ignoreCase", "대소문자 무시"),
    ),
    "list-footer" to listOf(
        choiceControl("border", "구분선", listOf("Full", "Indented", "None")),
        textControl("text", "레이블", "더 보기"),
    ),
    "list-header" to listOf(
        choiceControl("descriptionPosition", "설명 위치", listOf("Top", "Bottom")),
        toggleControl("showDescription", "설명 표시"),
        textControl("title", "제목", "최근 거래"),
        textControl("description", "설명", "타이틀 위에 보조 설명이 배치됩니다."),
    ),
    "loading-indicator" to listOf(
        numberControl("size", "크기", 48, 24, 96),
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
    ),
    "loader" to listOf(
        textControl("label", "로딩 안내", "데이터를 불러오는 중"),
        toggleControl("showLabel", "안내 문구 표시"),
    ),
    "menu" to listOf(
        choiceControl("placement", "메뉴 위치", IenMenu.Placement.entries.map { it.name }, "AnchorTopStart"),
        numberControl("offset", "앵커 간격", 8, 0, 32),
    ),
    "progress-indicator" to listOf(
        numberControl("strokeWidth", "선 두께", 4, 1, 12),
        numberControl("waveAmplitude", "물결 진폭 (%)", 100, 0, 200),
        numberControl("gapSize", "진행 간격", 4, 0, 16),
        numberControl("wavelength", "물결 길이", 40, 8, 100),
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
    ),
    "progress-bar" to listOf(
        toggleControl("showLabel", "진행률 레이블 표시", true),
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
    ),
    "progress-stepper" to listOf(
        choiceControl("paddingTop", "상단 여백", listOf("Default", "Wide"), "Wide"),
        numberControl("activeStepIndex", "현재 단계", 1, 0, 2),
        toggleControl("checkForFinish", "완료 표시", false),
        numberControl("stepCount", "단계 수", 3, 2, 5),
    ),
    "numeric-spinner" to listOf(numberControl("number", "초기 수량", 2, 0, 50)),
    "rating" to listOf(numberControl("max", "최대 별점", 5, 1, 10)),
    "search-field" to listOf(
        textControl("placeholder", "입력 안내", "검색어를 입력하세요"),
        toggleControl("fixed", "상단 고정", true),
        toggleControl("takeSpace", "공간 유지", true),
    ),
    "skeleton" to listOf(
        choiceControl("background", "배경", listOf("White", "Grey", "GreyOpacity100"), "Grey"),
        choiceControl("play", "재생 상태", listOf("Show", "Hide"), "Show"),
        numberControl("repeatCount", "마지막 항목 반복", 3, 0, 8),
        numberControl("height", "블록 높이", 0, 0, 120),
        numberControl("radius", "모서리 반경", 8, 0, 32),
    ),
    "slider" to listOf(
        numberControl("steps", "단계 수", 0, 0, 10),
        numberControl("rangeStart", "범위 시작 (%)", 0, 0, 90),
        numberControl("rangeEnd", "범위 끝 (%)", 100, 10, 100),
    ),
    "segmented-control" to listOf(numberControl("itemCount", "항목 수", 3, 2, 5)),
    "tab" to listOf(numberControl("itemGap", "탭 간격", 8, 0, 32)),
    "text-button" to listOf(choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand")),
    "tooltip" to listOf(
        textControl("text", "메시지", "툴팁은 짧은 보조 설명에 사용합니다."),
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Neutral"),
        choiceControl("messageAlign", "메시지 정렬", listOf("Left", "Center"), "Left"),
        choiceControl("motionVariant", "모션", listOf("Weak", "Strong"), "Weak"),
        choiceControl("strategy", "배치 방식", listOf("Absolute", "Fixed"), "Absolute"),
        choiceControl("clipToEnd", "끝 방향 클리핑", listOf("None", "Left", "Right"), "None"),
        toggleControl("defaultOpen", "처음부터 열기", false),
        toggleControl("dismissible", "닫기 가능", false),
        toggleControl("autoFlip", "자동 위치 조정", false),
        toggleControl("fitContentWidth", "콘텐츠 너비에 맞춤", false),
        toggleControl("openOnHover", "포인터 올릴 때 열기", false),
        toggleControl("openOnFocus", "키보드 포커스 시 열기", false),
        numberControl("anchorPosition", "화살표 위치 (%)", 50, 0, 100),
        numberControl("offset", "앵커 간격", 0, 0, 32),
        numberControl("width", "너비", 0, 0, 320),
    ),
    "top" to listOf(
        textControl("title", "제목", "결제 확인"),
        textControl("subtitle", "보조 문구", "타이틀과 보조 설명의 크기를 선택합니다."),
        choiceControl("rightVerticalAlign", "우측 정렬", listOf("Center", "End"), "Center"),
        numberControl("contentPadding", "좌우 여백", 24, 0, 48),
        numberControl("contentPaddingVertical", "상하 여백", 0, 0, 48),
        numberControl("upperGap", "상단 간격", 24, 0, 48),
        numberControl("lowerGap", "하단 간격", 24, 0, 48),
    ),
    "asset" to listOf(
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
        toggleControl("bordered", "테두리 표시", false),
    ),
    "bottom-bar" to listOf(
        choiceControl("container", "배경", listOf("surface", "surfaceWeak"), "surface"),
        numberControl("elevation", "그림자 높이", 8, 0, 24),
        choiceControl("shape", "모양", listOf("Capsule", "Rounded"), "Capsule"),
        numberControl("contentPadding", "안쪽 여백", 8, 0, 24),
    ),
    "chat-bottom-bar" to listOf(
        textControl("placeholder", "입력 안내", "메시지를 입력하세요"),
        numberControl("maxLines", "최대 줄 수", 4, 1, 8),
        toggleControl("showLeading", "첨부 버튼 표시"),
        toggleControl("showTrailing", "추가 메뉴 표시"),
    ),
    "bottom-cta" to listOf(
        choiceControl("background", "배경", listOf("Default", "None")),
        choiceControl("variant", "버튼 형태", listOf("Fill", "Weak", "Line", "Ghost"), "Fill"),
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
        textControl("text", "CTA 레이블", "아이콘 포함 CTA"),
        textControl("primaryText", "주 버튼 레이블", "확인"),
        textControl("secondaryText", "보조 버튼 레이블", "취소"),
        choiceControl("animation", "등장 모션", listOf("None", "Fade", "Scale", "Slide"), "Scale"),
        numberControl("delayMillis", "등장 지연 (ms)", 300, 0, 1000),
        numberControl("primaryButtonWeight", "주 버튼 비율", 2, 1, 4),
        numberControl("secondaryButtonWeight", "보조 버튼 비율", 1, 1, 4),
        numberControl("hideOnScrollDistanceThreshold", "스크롤 숨김 임계값", 1, 0, 16),
        toggleControl("takeSpace", "숨김 상태에서 공간 유지", false),
        toggleControl("safeArea", "안전 영역 여백"),
        toggleControl("paddingBottom", "하단 여백"),
        toggleControl("fixed", "고정 CTA 샘플 표시", true),
        toggleControl("fixedAboveKeyboard", "키보드 위에 고정", false),
        toggleControl("hideOnScroll", "스크롤 시 숨김", false),
    ),
    "dialog" to listOf(
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
        toggleControl("destructive", "위험 동작", false),
        choiceControl("buttonLayout", "버튼 배치", listOf("Horizontal", "Vertical")),
        toggleControl("closeOnDimmerClick", "바깥 영역으로 닫기"),
        toggleControl("closeOnBackEvent", "뒤로가기로 닫기"),
        textControl("title", "제목", "엔님의 의견이 잘 전달되었어요"),
        textControl("message", "내용", "소중한 의견을 바탕으로 더 간편한 서비스를 만들게요."),
        textControl("confirmText", "확인 버튼", "확인"),
        textControl("dismissText", "취소 버튼", "취소"),
    ),
    "alert-dialog" to listOf(
        choiceControl("tone", "강조 색상", IenSemanticTone.entries.map { it.name }, "Brand"),
        toggleControl("destructive", "위험 동작", false),
        choiceControl("buttonLayout", "버튼 배치", listOf("Horizontal", "Vertical")),
        toggleControl("closeOnDimmerClick", "바깥 영역으로 닫기"),
        toggleControl("closeOnBackEvent", "뒤로가기로 닫기"),
        textControl("title", "제목", "엔님의 의견이 잘 전달되었어요"),
        textControl("message", "내용", "소중한 의견을 바탕으로 더 간편한 서비스를 만들게요."),
        textControl("confirmText", "확인 버튼", "확인"),
        textControl("dismissText", "취소 버튼", "취소"),
    ),
    "keypad" to listOf(
        toggleControl("randomized", "키 순서 무작위", false),
        numberControl("columns", "알파벳 열 수", 3, 2, 6),
        numberControl("keyHeight", "키 높이", 56, 40, 80),
    ),
    "text-field" to listOf(
        toggleControl("required", "필수 입력"),
        toggleControl("hasError", "오류 상태", false),
        choiceControl("labelOption", "레이블 표시", listOf("Appear", "Sustain"), "Appear"),
        choiceControl("lengthLimit", "글자 수 제한", listOf("None", "Error", "Block"), "Error"),
        numberControl("lengthLimitCount", "최대 글자 수", 3, 1, 64),
        textControl("label", "레이블", "이름 · Required · LengthLimit.Error"),
        textControl("placeholder", "입력 안내", "이름을 입력하세요"),
    ),
    "split-text-field" to listOf(
        toggleControl("mask", "입력값 가리기", false),
        choiceControl("placeholderChar", "빈 칸 표시", listOf("•", "○", "_", "-"), "•"),
    ),
    "text-area" to listOf(
        numberControl("minLines", "최소 줄 수", 2, 1, 8),
        toggleControl("required", "필수 입력"),
        choiceControl("lengthLimit", "글자 수 제한", listOf("None", "Error", "Block"), "Block"),
        numberControl("lengthLimitCount", "최대 글자 수", 120, 1, 500),
        textControl("label", "레이블", "메모 · LengthLimit.Block"),
        textControl("placeholder", "입력 안내", "여러 줄 텍스트를 입력하세요"),
    ),
    "primitives" to listOf(
        choiceControl("surfaceColor", "표면 색상", listOf("surface", "surfaceWeak", "brandWeak")),
        numberControl("dividerThickness", "구분선 두께", 1, 1, 8),
        choiceControl("borderTone", "테두리 색상", IenSemanticTone.entries.map { it.name }, "Neutral"),
        numberControl("borderWidth", "테두리 두께", 1, 1, 8),
    ),
    "paragraph" to listOf(
        textControl("title", "제목", "문단 컴포넌트"),
        textControl("body", "본문", "Paragraph는 본문 타이포그래피와 Highlight를 함께 사용해 긴 설명을 안정적으로 표시합니다."),
        textControl("emphasis", "강조 문구", "Highlight"),
        textControl("footer", "보조 문구", "토큰 기반 줄 높이와 색상을 사용합니다."),
    ),
    "post" to listOf(
        textControl("title", "제목", "Compose Multiplatform 디자인 시스템 진행 기록"),
        textControl("author", "작성자", "IENGROUND"),
        textControl("description", "본문", "Post는 피드형 콘텐츠, 공지, 업데이트 카드의 공통 정보 구조입니다."),
        toggleControl("showAuthor", "작성자 표시"),
        toggleControl("showDescription", "본문 표시"),
        toggleControl("showMetadata", "메타데이터 표시"),
    ),
    "result" to listOf(
        choiceControl("tone", "결과 상태", listOf("Success", "Failure", "Empty", "Info"), "Success"),
        textControl("title", "제목", "처리 준비 완료"),
        textControl("description", "설명", "Result는 성공, 실패, 빈 상태 화면을 같은 정보 구조로 표현합니다."),
        toggleControl("showDescription", "설명 표시"),
        toggleControl("showPrimaryAction", "주요 동작 표시"),
        toggleControl("showSecondaryAction", "보조 동작 표시", false),
    ),
    "swipe-box" to listOf(
        numberControl("itemWidth", "액션 너비", 96, 56, 160),
        numberControl("height", "행 높이", 72, 48, 120),
        toggleControl("fullSwipe", "끝까지 밀어 실행", false),
        toggleControl("haptics", "확장 진동 피드백"),
    ),
    "stepper" to listOf(
        toggleControl("play", "단계 애니메이션"),
        decimalControl("delay", "시작 지연", 0f, 0, 2),
        decimalControl("staggerDelay", "항목 간 지연", 0.12f, 0, 1),
        choiceControl("textType", "텍스트 크기", listOf("A", "B", "C"), "A"),
    ),
    "table-row" to listOf(
        choiceControl("align", "열 정렬", listOf("SpaceBetween", "Left"), "SpaceBetween"),
        numberControl("leftRatio", "왼쪽 열 비율 (%)", 30, 10, 90),
        textControl("left", "왼쪽 값", "엔님"),
        textControl("right", "오른쪽 값", "받는 분"),
    ),
    "snackbar" to listOf(
        textControl("message", "메시지", "속성 조정 스낵바입니다."),
        choiceControl("tone", "상태", listOf("Neutral", "Success", "Warning", "Danger", "Info")),
        choiceControl("duration", "표시 시간", listOf("Short", "Long", "Indefinite"), "Long"),
        textControl("actionLabel", "액션 레이블", "확인"),
        toggleControl("showAction", "액션 버튼 표시"),
    ),
    "toast" to listOf(
        textControl("message", "메시지", "속성 조정 토스트입니다."),
        choiceControl("tone", "상태", listOf("Neutral", "Success", "Warning", "Danger", "Info")),
        choiceControl("duration", "표시 시간", listOf("Short", "Long"), "Long"),
    ),
)

private fun playgroundControls(componentId: String): List<PlaygroundControl> {
    return ComponentPlaygroundControls[componentId].orEmpty() +
        AdditionalComponentPlaygroundControls[componentId].orEmpty()
}

private fun Map<String, String>.booleanValue(key: String, defaultValue: Boolean): Boolean =
    this[key]?.toBooleanStrictOrNull() ?: defaultValue

private fun Map<String, String>.disabledValue(): Boolean = booleanValue("disabled", false)

private fun Map<String, String>.intValue(key: String, defaultValue: Int): Int =
    this[key]?.toIntOrNull() ?: defaultValue

private fun Map<String, String>.floatValue(key: String, defaultValue: Float): Float =
    this[key]?.toFloatOrNull()?.takeIf { it.isFinite() } ?: defaultValue

private fun Map<String, String>.textValue(key: String, defaultValue: String): String =
    this[key] ?: defaultValue

@Composable
private fun playgroundToneColor(tone: String): Color = when (tone) {
    "Success" -> IenTheme.colors.success
    "Warning" -> IenTheme.colors.warning
    "Danger" -> IenTheme.colors.danger
    "Info" -> IenTheme.colors.info
    "Neutral" -> IenTheme.colors.textSecondary
    else -> IenTheme.colors.brand
}

@Composable
private fun playgroundSurfaceColor(surface: String): Color = when (surface) {
    "surface" -> IenTheme.colors.surface
    "brandWeak" -> IenTheme.colors.brandWeak
    else -> IenTheme.colors.surfaceWeak
}

private fun playgroundPreviewValues(
    controls: List<PlaygroundControl>,
    values: Map<String, String>,
): Map<String, String> = controls.associate { control ->
    val value = values[control.key] ?: control.defaultValue
    val previewValue = if (control.minValue != null && control.maxValue != null) {
        if (control.allowDecimal) {
            value.toFloatOrNull()
                ?.takeIf { it.isFinite() }
                ?.coerceIn(control.minValue.toFloat(), control.maxValue.toFloat())
                ?.toString()
                ?: control.defaultValue
        } else {
            value.toIntOrNull()
                ?.coerceIn(control.minValue, control.maxValue)
                ?.toString()
                ?: control.defaultValue
        }
    } else {
        value
    }
    control.key to previewValue
}

private fun Map<String, String>.enumValue(key: String, defaultValue: String): String =
    this[key] ?: defaultValue

private fun Map<String, String>.toneValue(key: String, defaultValue: IenSemanticTone): IenSemanticTone =
    IenSemanticTone.entries.firstOrNull { it.name == enumValue(key, defaultValue.name) } ?: defaultValue

internal enum class PreviewViewport(val storageValue: String, val label: String, val maxWidth: Dp) {
    Pc("pc", "PC 1280px", 1280.dp),
    Tablet("tablet", "태블릿 768px", 768.dp),
    Mobile("mobile", "모바일 390px", 390.dp);

    companion object {
        fun fromStorageValue(value: String): PreviewViewport =
            entries.firstOrNull { it.storageValue == value } ?: Pc
    }
}

@Composable
internal fun DesignSystemPlayground(
    componentId: String,
    snackbarHostState: SnackbarHostState,
    toastState: IenToastState?,
    coroutineScope: CoroutineScope,
    showPreviewViewportControls: Boolean = false,
    initialPreviewViewport: String = PreviewViewport.Pc.storageValue,
    onPreviewViewportChange: (String) -> Unit = {},
) {
    val controls = remember(componentId) { playgroundControls(componentId) }
    val values = remember(componentId) {
        mutableStateMapOf<String, String>().apply {
            controls.forEach { control -> put(control.key, control.defaultValue) }
        }
    }
    var previewViewport by remember(componentId, initialPreviewViewport) {
        mutableStateOf(PreviewViewport.fromStorageValue(initialPreviewViewport))
    }
    val previewValues = playgroundPreviewValues(controls, values)

    Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
        IenCard(
            modifier = Modifier.fillMaxWidth(),
            variant = IenCardVariant.Outlined,
            contentPadding = PaddingValues(IenTheme.spacing.md),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                    Text("미리보기", style = IenTheme.typography.title3)
                    Text(
                        "샘플 앱과 웹 문서에서 같은 속성 조정 예제를 사용합니다.",
                        style = IenTheme.typography.body2,
                        color = IenTheme.colors.textSecondary,
                    )
                    if (showPreviewViewportControls) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                        ) {
                            IenSegmentedControl(
                                items = PreviewViewport.entries.map { viewport ->
                                    IenSegmentedControlItem(
                                        value = viewport,
                                        label = viewport.label,
                                    )
                                },
                                value = previewViewport,
                                onChange = { viewport ->
                                    previewViewport = viewport
                                    onPreviewViewportChange(viewport.storageValue)
                                },
                                alignment = IenSegmentedControlAlignment.Fluid,
                            )
                        }
                    }
                }
                IenDivider()
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Box(
                        modifier = if (showPreviewViewportControls) {
                            Modifier.widthIn(max = previewViewport.maxWidth).fillMaxWidth()
                        } else {
                            Modifier.fillMaxWidth()
                        },
                    ) {
                        CompositionLocalProvider(
                            LocalComponentSectionChrome provides false,
                            LocalComponentVariantShowcase provides true,
                        ) {
                            DesignSystemComponentPreview(
                                componentId = componentId,
                                snackbarHostState = snackbarHostState,
                                toastState = toastState,
                                coroutineScope = coroutineScope,
                                controlValues = previewValues,
                            )
                        }
                    }
                }
            }
        }

        IenCard(
            modifier = Modifier.fillMaxWidth(),
            variant = IenCardVariant.Outlined,
            contentPadding = PaddingValues(IenTheme.spacing.md),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                    ) {
                        Text("속성 조정", style = IenTheme.typography.title3)
                        Text(
                            "값을 바꾸면 미리보기에 바로 반영됩니다.",
                            style = IenTheme.typography.body2,
                            color = IenTheme.colors.textSecondary,
                        )
                    }
                    IenTextButton(
                        onClick = {
                            controls.forEach { control ->
                                values[control.key] = control.defaultValue
                            }
                        },
                    ) {
                        Text("초기화")
                    }
                }
                BoxWithConstraints {
                    val columns = if (maxWidth >= 720.dp) 2 else 1
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        maxItemsInEachRow = columns,
                        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                    ) {
                        controls.forEach { control ->
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                            ) {
                                if (control.isToggle) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            control.label,
                                            style = IenTheme.typography.label1,
                                            color = IenTheme.colors.textSecondary,
                                            modifier = Modifier.weight(1f),
                                        )
                                        IenSwitch(
                                            checked = values.booleanValue(
                                                control.key,
                                                control.defaultValue.toBooleanStrict(),
                                            ),
                                            onCheckedChange = { checked ->
                                                values[control.key] = checked.toString()
                                            },
                                        )
                                    }
                                } else if (control.choices.isNotEmpty()) {
                                    Text(
                                        control.label,
                                        style = IenTheme.typography.label1,
                                        color = IenTheme.colors.textSecondary,
                                    )
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                                        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                                    ) {
                                        control.choices.forEach { choice ->
                                            IenFilterChip(
                                                selected = values[control.key] == choice.value,
                                                onSelectedChange = { selected ->
                                                    if (selected) values[control.key] = choice.value
                                                },
                                            ) {
                                                Text(choice.label)
                                            }
                                        }
                                    }
                                } else {
                                    val minimum = control.minValue ?: Int.MIN_VALUE
                                    val maximum = control.maxValue ?: Int.MAX_VALUE
                                    IenTextField(
                                        value = values[control.key] ?: control.defaultValue,
                                        onValueChange = { value -> values[control.key] = value },
                                        modifier = Modifier.fillMaxWidth(),
                                        label = control.label,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = when {
                                                control.isTextInput -> KeyboardType.Text
                                                control.allowDecimal -> KeyboardType.Decimal
                                                else -> KeyboardType.Number
                                            },
                                        ),
                                        supportingText = if (control.isTextInput) null else "범위: $minimum–$maximum",
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        val variantControls = controls.filter { it.choices.isNotEmpty() }
        if (variantControls.isNotEmpty()) {
            IenCard(
                modifier = Modifier.fillMaxWidth(),
                variant = IenCardVariant.Outlined,
                contentPadding = PaddingValues(IenTheme.spacing.md),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                    Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                        Text("전체 변형", style = IenTheme.typography.title3)
                        Text(
                            "속성별 선택지를 실제 컴포넌트로 확인합니다.",
                            style = IenTheme.typography.body2,
                            color = IenTheme.colors.textSecondary,
                        )
                    }
                    variantControls.forEach { control ->
                        Text(control.label, style = IenTheme.typography.label1)
                        control.choices.forEach { choice ->
                            Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                                Text(choice.label, style = IenTheme.typography.caption)
                                CompositionLocalProvider(
                                    LocalComponentSectionChrome provides false,
                                    LocalComponentVariantShowcase provides true,
                                ) {
                                    DesignSystemComponentPreview(
                                        componentId = componentId,
                                        snackbarHostState = snackbarHostState,
                                        toastState = toastState,
                                        coroutineScope = coroutineScope,
                                        controlValues = previewValues + (control.key to choice.value),
                                    )
                                }
                            }
                        }
                        if (control != variantControls.last()) IenDivider()
                    }
                }
            }
        }

        if (controls.isNotEmpty()) {
            IenCard(
                modifier = Modifier.fillMaxWidth(),
                variant = IenCardVariant.Outlined,
                contentPadding = PaddingValues(IenTheme.spacing.md),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                    Text("구성 예시", style = IenTheme.typography.title3)
                    CompositionLocalProvider(LocalComponentSectionChrome provides false) {
                        DesignSystemComponentPreview(
                            componentId = componentId,
                            snackbarHostState = snackbarHostState,
                            toastState = toastState,
                            coroutineScope = coroutineScope,
                            controlValues = previewValues,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DesignSystemScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    navigateToColor: () -> Unit = {},
    componentId: String? = null,
    navigateToComponent: (String) -> Unit = {},
) {
    IenTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val toastState = LocalIenToastState.current
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        var query by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf("전체") }
        val selectedComponent = DesignSystemComponents.firstOrNull { it.id == componentId }

        IenScaffold(
            modifier = modifier,
            contentEdge = IenScaffoldContentEdge(scrollState = scrollState),
            topBar = {
                IenTopBar(
                    title = selectedComponent?.name ?: "Ien CMP UI",
                    subtitle = if (selectedComponent == null) {
                        "토큰 기반 모바일 디자인 시스템"
                    } else {
                        selectedComponent.category
                    },
                    navigationIcon = {
                        IenTextButton(onClick = navigateBack) {
                            Text(if (selectedComponent == null) "닫기" else "목록")
                        }
                    },
                    actions = {
                        IenTextButton(onClick = navigateToColor) {
                            Text("컬러 스킴")
                        }
                    },
                )
            },
            snackbarHost = {
                IenSnackbarHost(hostState = snackbarHostState)
            },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(contentPadding)
                    .padding(horizontal = IenTheme.spacing.md),
            ) {
                Spacer(Modifier.height(IenTheme.spacing.md))
                if (selectedComponent == null) {
                    DesignSystemCatalog(
                        query = query,
                        onQueryChange = { query = it },
                        selectedCategory = selectedCategory,
                        onCategoryChange = { selectedCategory = it },
                        onComponentClick = navigateToComponent,
                    )
                } else {
                    DesignSystemComponentDetail(
                        component = selectedComponent,
                        snackbarHostState = snackbarHostState,
                        toastState = toastState,
                        coroutineScope = coroutineScope,
                    )
                }
                Spacer(Modifier.height(IenTheme.spacing.lg))
            }
        }
    }
}

@Composable
private fun DesignSystemCatalog(
    query: String,
    onQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    onComponentClick: (String) -> Unit,
) {
    val categories = remember {
        listOf("전체") + DesignSystemComponents.map { it.category }.distinct()
    }
    val normalizedQuery = query.trim()
    val visibleComponents = DesignSystemComponents.filter { component ->
        (selectedCategory == "전체" || component.category == selectedCategory) &&
            (normalizedQuery.isEmpty() ||
                component.name.contains(normalizedQuery, ignoreCase = true) ||
                component.description.contains(normalizedQuery, ignoreCase = true))
    }
    val componentsByCategory = visibleComponents.groupBy { it.category }

    Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            ) {
                Text("컴포넌트", style = IenTheme.typography.title1)
                Text(
                    "구성 요소를 선택해 실제 Compose 예제를 확인하세요.",
                    style = IenTheme.typography.body2,
                    color = IenTheme.colors.textSecondary,
                )
            }
            Text(
                "${visibleComponents.size}개",
                style = IenTheme.typography.label1,
                color = IenTheme.colors.textSecondary,
            )
        }

        IenTextField(
            value = query,
            onValueChange = onQueryChange,
            label = "컴포넌트 검색",
            placeholder = "이름 또는 기능으로 검색",
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        ) {
            categories.forEach { category ->
                IenFilterChip(
                    selected = selectedCategory == category,
                    onSelectedChange = { selected ->
                        if (selected) onCategoryChange(category)
                    },
                ) {
                    Text(category)
                }
            }
        }

        if (visibleComponents.isEmpty()) {
            Text(
                "검색 결과가 없습니다.",
                style = IenTheme.typography.body2,
                color = IenTheme.colors.textSecondary,
                modifier = Modifier.padding(vertical = IenTheme.spacing.lg),
            )
        } else {
            categories.drop(1).forEach { category ->
                val components = componentsByCategory[category].orEmpty()
                if (components.isNotEmpty()) {
                    Text(category, style = IenTheme.typography.title3)
                    components.forEach { component ->
                        IenCard(
                            modifier = Modifier.fillMaxWidth(),
                            variant = IenCardVariant.Outlined,
                            contentPadding = PaddingValues(IenTheme.spacing.md),
                            onClick = { onComponentClick(component.id) },
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        component.name,
                                        style = IenTheme.typography.title3,
                                        modifier = Modifier.weight(1f),
                                    )
                                    Text(
                                        "›",
                                        style = IenTheme.typography.title2,
                                        color = IenTheme.colors.textSecondary,
                                    )
                                }
                                Text(
                                    component.description,
                                    style = IenTheme.typography.body2,
                                    color = IenTheme.colors.textSecondary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DesignSystemComponentDetail(
    component: DesignSystemComponent,
    snackbarHostState: SnackbarHostState,
    toastState: IenToastState?,
    coroutineScope: CoroutineScope,
) {
    Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
        IenBadge(component.category, variant = IenBadgeVariant.Line)
        Text(component.name, style = IenTheme.typography.title1)
        Text(
            component.description,
            style = IenTheme.typography.body1,
            color = IenTheme.colors.textSecondary,
        )
        DesignSystemPlayground(
            componentId = component.id,
            snackbarHostState = snackbarHostState,
            toastState = toastState,
            coroutineScope = coroutineScope,
        )
    }
}

@Composable
internal fun DesignSystemComponentPreview(
    componentId: String,
    snackbarHostState: SnackbarHostState,
    toastState: IenToastState?,
    coroutineScope: CoroutineScope,
    controlValues: Map<String, String>,
) {
    when (componentId) {
        "wheel-picker" -> WheelPickerSection(controlValues)
        "animated-layout" -> AnimatedLayoutSection(controlValues)
        "animated-content" -> AnimatedContentSection(controlValues)
        "badge" -> BadgeSection(controlValues)
        "board-row" -> BoardRowSection(controlValues)
        "border" -> BorderSection(controlValues)
        "bottom-info" -> BottomInfoSection(controlValues)
        "bottom-sheet" -> BottomSheetSection(controlValues)
        "bubble" -> BubbleSection(controlValues)
        "button" -> ButtonSection(controlValues)
        "card" -> CardSection(controlValues)
        "chip" -> ChipSection(controlValues)
        "fab" -> FabSection(controlValues)
        "checkbox" -> CheckboxSection(controlValues)
        "highlight" -> HighlightSection(controlValues)
        "icon-button" -> IconButtonSection(controlValues)
        "list-footer" -> ListFooterSection(controlValues)
        "list-header" -> ListHeaderSection(controlValues)
        "loading-indicator" -> LoadingIndicatorSection(controlValues)
        "loader" -> LoaderSection(controlValues)
        "menu" -> MenuSection(controlValues)
        "modal" -> ModalSection(controlValues)
        "numeric-spinner" -> NumericSpinnerSection(controlValues)
        "paragraph" -> ParagraphSection(controlValues)
        "post" -> PostSection(controlValues)
        "progress-bar" -> ProgressBarSection(controlValues)
        "progress-indicator" -> ProgressIndicatorSection(controlValues)
        "progress-stepper" -> ProgressStepperSection(controlValues)
        "rating" -> RatingSection(controlValues)
        "result" -> ResultSection(controlValues)
        "search-field" -> SearchFieldSection(controlValues)
        "segmented-control" -> SegmentedControlSection(controlValues)
        "skeleton" -> SkeletonSection(controlValues)
        "slider" -> SliderSection(controlValues)
        "swipe-box" -> SwipeBoxSection(controlValues)
        "stepper" -> StepperSection(controlValues)
        "switch" -> SwitchSection(controlValues)
        "tab" -> TabSection(controlValues)
        "table-row" -> TableRowSection(controlValues)
        "text-button" -> TextButtonSection(controlValues)
        "snackbar" -> SnackbarSection(
            controls = controlValues,
            onShowBasic = {
                coroutineScope.launch {
                    snackbarHostState.showIenSnackbar(
                        message = controlValues.textValue("message", "속성 조정 스낵바입니다."),
                        tone = controlValues.toneValue("tone", IenSemanticTone.Neutral),
                        duration = when (controlValues.enumValue("duration", "Long")) {
                            "Short" -> SnackbarDuration.Short
                            "Indefinite" -> SnackbarDuration.Indefinite
                            else -> SnackbarDuration.Long
                        },
                    )
                }
            },
            onShowSuccess = {
                coroutineScope.launch {
                    snackbarHostState.showIenSnackbar(
                        message = "성공 상태 스낵바예요",
                        tone = IenSemanticTone.Success,
                    )
                }
            },
            onShowAction = {
                coroutineScope.launch {
                    val result = snackbarHostState.showIenSnackbar(
                        message = controlValues.textValue("message", "속성 조정 스낵바입니다."),
                        actionLabel = controlValues.textValue("actionLabel", "확인")
                            .takeIf { controlValues.booleanValue("showAction", true) },
                        duration = when (controlValues.enumValue("duration", "Long")) {
                            "Short" -> SnackbarDuration.Short
                            "Indefinite" -> SnackbarDuration.Indefinite
                            else -> SnackbarDuration.Long
                        },
                        tone = controlValues.toneValue("tone", IenSemanticTone.Neutral),
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        snackbarHostState.showIenSnackbar("액션을 실행했어요")
                    }
                }
            },
            onShowCompact = {
                coroutineScope.launch {
                    snackbarHostState.showIenSnackbar(
                        message = "최대 240",
                        minWidth = null,
                        maxWidth = 240.dp,
                        fillMaxWidth = false,
                    )
                }
            },
            onShowQueued = {
                coroutineScope.launch {
                    snackbarHostState.showIenSnackbar("첫 번째 스낵바예요")
                    snackbarHostState.showIenSnackbar("두 번째는 조금 더 긴 메시지예요")
                    snackbarHostState.showIenSnackbar(
                        message = "세 번째 성공 상태 스낵바예요",
                        tone = IenSemanticTone.Success,
                    )
                }
            },
            onShowShortDuration = {
                coroutineScope.launch {
                    snackbarHostState.showIenSnackbar(
                        message = "Short duration",
                        duration = SnackbarDuration.Short,
                    )
                }
            },
            onShowLongDuration = {
                coroutineScope.launch {
                    snackbarHostState.showIenSnackbar(
                        message = "Long duration",
                        duration = SnackbarDuration.Long,
                    )
                }
            },
            onShowIndefiniteDuration = {
                coroutineScope.launch {
                    snackbarHostState.showIenSnackbar(
                        message = "직접 닫을 때까지 유지돼요",
                        actionLabel = "닫기",
                        duration = SnackbarDuration.Indefinite,
                    )
                }
            },
        )
        "toast" -> ToastSection(
            controls = controlValues,
            onShowBasic = {
                toastState?.showIenToast(
                    message = controlValues.textValue("message", "속성 조정 토스트입니다."),
                    tone = controlValues.toneValue("tone", IenSemanticTone.Neutral),
                    duration = if (controlValues.enumValue("duration", "Long") == "Short") {
                        IenToastDuration.Short
                    } else {
                        IenToastDuration.Long
                    },
                )
            },
            onShowSuccess = {
                toastState?.showIenToast(
                    message = "성공 상태 토스트예요",
                    tone = IenSemanticTone.Success,
                )
            },
            onShowLong = {
                toastState?.showIenToast(
                    message = "오래 표시되는 토스트예요",
                    duration = IenToastDuration.Long,
                )
            },
            onDismiss = { toastState?.dismiss() },
        )
        "tooltip" -> TooltipSection(controlValues)
        "top" -> TopSection(controlValues)
        "agreement" -> AgreementSection(controlValues)
        "asset" -> AssetSection(controlValues)
        "bottom-bar" -> BottomBarSection(controlValues)
        "chat-bottom-bar" -> ChatBottomBarSection(controlValues)
        "navigation-bar" -> NavigationBarSection(controlValues)
        "bottom-cta" -> BottomCTASection(controlValues)
        "dialog", "alert-dialog" -> DialogSection(controlValues)
        "keypad" -> KeypadSection(controlValues)
        "list-row" -> ListRowSection(controlValues)
        "text-field" -> TextFieldSection(controlValues)
        "split-text-field" -> SplitTextFieldSection(controlValues)
        "text-area" -> TextAreaSection(controlValues)
        "primitives" -> PrimitivesSection(controlValues)
        else -> Text("선택한 미리보기를 찾을 수 없습니다.")
    }
}

@Preview
@Composable
fun AnimatedLayoutSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        var columnItems by remember { mutableStateOf(listOf(1, 2)) }
        var rowItems by remember { mutableStateOf(listOf(1, 2)) }
        var nextColumnItem by remember { mutableIntStateOf(3) }
        var nextRowItem by remember { mutableIntStateOf(3) }

        ComponentSection(title = "AnimatedLayout") {
            val itemCount = controls.intValue("itemCount", 2).coerceIn(1, 6)
            val controlledItems = remember(itemCount) { (1..itemCount).toList() }
            Text("속성 적용 미리보기 · ${if (controls.booleanValue("horizontal", false)) "가로" else "세로"} · ${itemCount}개")
            if (controls.booleanValue("horizontal", false)) {
                IenAnimatedRow(
                    items = controlledItems,
                    key = { it },
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) { item ->
                    IenSurface(color = IenTheme.colors.brandWeak) {
                        Text("$item", modifier = Modifier.padding(IenTheme.spacing.md), color = IenTheme.colors.brand)
                    }
                }
            } else {
                IenAnimatedColumn(
                    items = controlledItems,
                    key = { it },
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) { item ->
                    IenSurface(modifier = Modifier.fillMaxWidth(), color = IenTheme.colors.brandWeak) {
                        Text("항목 $item", modifier = Modifier.padding(IenTheme.spacing.md), color = IenTheme.colors.brand)
                    }
                }
            }
            Text(
                text = "항목을 추가하거나 제거하면 레이아웃 크기와 콘텐츠가 함께 애니메이션됩니다.",
                style = IenTheme.typography.body2,
                color = IenTheme.colors.textSecondary,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            ) {
                IenButton(
                    onClick = {
                        columnItems = columnItems + nextColumnItem
                        nextColumnItem++
                    },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) {
                    Text("세로 추가")
                }
                IenButton(
                    onClick = { columnItems = columnItems.dropLast(1) },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                    state = IenButtonState(enabled = columnItems.isNotEmpty()),
                ) {
                    Text("세로 제거")
                }
                IenButton(
                    onClick = {
                        rowItems = rowItems + nextRowItem
                        nextRowItem++
                    },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) {
                    Text("가로 추가")
                }
                IenButton(
                    onClick = { rowItems = rowItems.dropLast(1) },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                    state = IenButtonState(enabled = rowItems.isNotEmpty()),
                ) {
                    Text("가로 제거")
                }
            }
            IenAnimatedColumn(
                items = columnItems,
                key = { it },
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            ) { item ->
                IenSurface(
                    modifier = Modifier.fillMaxWidth(),
                    color = IenTheme.colors.surfaceWeak,
                ) {
                    Text(
                        text = "세로 항목 $item",
                        modifier = Modifier.padding(IenTheme.spacing.md),
                    )
                }
            }
            IenAnimatedRow(
                items = rowItems,
                key = { it },
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) { item ->
                IenSurface(
                    modifier = Modifier.size(88.dp, 48.dp),
                    color = IenTheme.colors.brandWeak,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "가로 $item")
                    }
                }
            }
        }
    }
}

private enum class AnimatedContentSampleState {
    Loading,
    Error,
    Content,
}

@Preview
@Composable
fun AnimatedContentSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        val initialState = when (controls.enumValue("contentState", "Content")) {
            "Loading" -> AnimatedContentSampleState.Loading
            "Error" -> AnimatedContentSampleState.Error
            else -> AnimatedContentSampleState.Content
        }
        var state by remember(initialState) { mutableStateOf(initialState) }

        ComponentSection(title = "AnimatedContent") {
            Text(
                text = "분기만 작성하고 진입·종료 애니메이션은 한 번만 지정합니다.",
                style = IenTheme.typography.body2,
                color = IenTheme.colors.textSecondary,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            ) {
                IenButton(
                    onClick = { state = AnimatedContentSampleState.Loading },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) {
                    Text("Loading")
                }
                IenButton(
                    onClick = { state = AnimatedContentSampleState.Error },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) {
                    Text("Error")
                }
                IenButton(
                    onClick = { state = AnimatedContentSampleState.Content },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) {
                    Text("Content")
                }
            }
            AnimatedContent(
                targetState = state,
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
                transitionSpec = {
                    fadeIn(animationSpec = spring(dampingRatio = 1.2f)) togetherWith
                            fadeOut(animationSpec = spring(dampingRatio = 1.2f))
                },
                label = "animated_content",
            ) { targetState ->
                when (targetState) {
                    AnimatedContentSampleState.Loading ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(96.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            IenLoader(label = "데이터를 불러오는 중")
                        }

                    AnimatedContentSampleState.Error ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(96.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "데이터를 불러오지 못했어요")
                            IenButton(
                                onClick = { state = AnimatedContentSampleState.Loading },
                                size = IenButtonSize.Small,
                            ) {
                                Text("다시 시도")
                            }
                        }

                    AnimatedContentSampleState.Content ->
                        IenSurface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(96.dp),
                            color = IenTheme.colors.surfaceWeak,
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(text = "콘텐츠가 표시됐어요")
                            }
                        }
                }
            }
        }
    }
}

@Preview
@Composable
fun BadgeSection(controls: Map<String, String> = emptyMap()) {
    val variant = when (controls.enumValue("variant", "Fill")) {
        "Weak" -> IenBadgeVariant.Weak
        "Line" -> IenBadgeVariant.Line
        else -> IenBadgeVariant.Fill
    }
    val tone = IenSemanticTone.entries.firstOrNull {
        it.name == controls.enumValue("tone", "Brand")
    } ?: IenSemanticTone.Brand
    val size = when (controls.enumValue("size", "Medium")) {
        "Small" -> IenBadgeSize.Small
        "Large" -> IenBadgeSize.Large
        else -> IenBadgeSize.Medium
    }
    IenTheme {
        ComponentSection(title = "Badge") {
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                IenBadge(controls.textValue("text", "미리보기"), size = size, variant = variant, tone = tone)
            }
        }
    }
}

@Preview
@Composable
fun BoardRowSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        ComponentSection(title = "BoardRow") {
            IenBoardRow(
                title = { Text(controls.textValue("title", "배송 정보 자세히 보기"), style = IenTheme.typography.label1) },
                initialOpened = controls.booleanValue("initialOpened", true),
                prefix = if (controls.booleanValue("showPrefix", true)) {
                    { IenBadge("배송", size = IenBadgeSize.Small) }
                } else null,
            ) {
                Text(
                    "제한된 영역에서 상세 정보를 접고 펼치는 아코디언형 정보 구조입니다.",
                    color = IenTheme.colors.textSecondary
                )
            }
            IenBoardRow(
                title = { Text("배송 정보 자세히 보기2", style = IenTheme.typography.label1) },
                prefix = { IenBadge("배송", size = IenBadgeSize.Small) },
            ) {
                Text(
                    "제한된 영역에서 상세 정보를 접고 펼치는 아코디언형 정보 구조입니다.",
                    color = IenTheme.colors.textSecondary
                )
            }
        }
    }
}

@Preview
@Composable
fun BorderSection(controls: Map<String, String> = emptyMap()) {
    val variant = when (controls.enumValue("variant", "Full")) {
        "Padding24" -> IenBorderVariant.Padding24
        "Height16" -> IenBorderVariant.Height()
        else -> IenBorderVariant.Full
    }
    IenTheme {
        ComponentSection(title = "Border") {
            Text(controls.enumValue("variant", "Full"), style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
            IenBorder(
                variant = variant,
                color = playgroundToneColor(controls.enumValue("tone", "Neutral")),
                thickness = controls.intValue("thickness", 1).coerceIn(1, 8).dp,
            )
        }
    }
}

@Preview
@Composable
fun BottomInfoSection(controls: Map<String, String> = emptyMap()) {
    val padding = controls.intValue("padding", 24).coerceIn(0, 48).dp
    IenTheme {
        val backgroundColor = playgroundSurfaceColor(controls.enumValue("background", "surfaceWeak"))
        val bottomGradient = when (controls.enumValue("gradient", "Default")) {
            "None" -> IenBottomGradient.None
            "Custom" -> IenBottomGradient.Custom(fromColor = backgroundColor)
            else -> IenBottomGradient.Default
        }
        ComponentSection(title = "BottomInfo") {
            IenBottomInfo(
                bottomGradient = bottomGradient,
                backgroundColor = backgroundColor,
                contentPadding = PaddingValues(horizontal = padding, vertical = 16.dp),
            ) {
                Text(
                    text = "하단 안내는 결제, 확인, 폼 화면에서 보조 정보를 안정적으로 보여줍니다.",
                    style = IenTheme.typography.caption,
                    color = IenTheme.colors.textSecondary
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomSheetSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        val detent = when (controls.enumValue("detent", "Content")) {
            "Medium" -> IenSheetDetent.Medium
            "Full" -> IenSheetDetent.Full
            else -> IenSheetDetent.Content
        }
        val visible = controls.booleanValue("visible", false)
        val sheetState = rememberIenBottomSheetState(visible = visible, detent = detent)
        LaunchedEffect(visible, detent) {
            if (visible) sheetState.show(detent) else sheetState.hide()
        }
        val selectSheetState = rememberIenBottomSheetState()
        var selectedPet by remember { mutableStateOf<String?>("강아지") }

        ComponentSection(title = "BottomSheet") {
            IenButton(
                onClick = { sheetState.show(IenSheetDetent.Content) },
                display = IenButtonDisplay.Block,
            ) {
                Text("일반 바텀시트 열기")
            }
            Spacer(modifier = Modifier.height(8.dp))
            IenButton(
                onClick = { selectSheetState.show(IenSheetDetent.Content) },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
            ) {
                Text("선택형 바텀시트 열기 (선택: $selectedPet)")
            }
        }

        IenBottomSheet(
            state = sheetState,
            dismissOnScrimClick = controls.booleanValue("dismissOnScrimClick", true),
            showDragHandle = controls.booleanValue("showDragHandle", true),
            disableDimmer = controls.booleanValue("disableDimmer", false),
            header = { Text("바텀시트", style = IenTheme.typography.title3) },
            cta = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)
                ) {
                    IenButton(
                        onClick = { sheetState.hide() },
                        modifier = Modifier.weight(1f),
                        variant = IenButtonVariant.Weak,
                    ) {
                        Text("닫기")
                    }
                    IenButton(
                        onClick = { sheetState.hide() },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("확인")
                    }
                }
            },
        ) {
            Text(
                text = "공통 API는 유지하면서 Android와 iOS의 시트 감각 차이는 내부 구현에서 흡수합니다.",
                color = IenTheme.colors.textSecondary,
            )
            IenBottomInfo(
                backgroundColor = IenTheme.colors.brandWeak
            ) {
                Text(
                    text = "스크림을 누르면 닫히도록 설정되어 있습니다.",
                    style = IenTheme.typography.caption,
                    color = IenTheme.colors.brand
                )
            }
        }

        IenBottomSheet(
            state = selectSheetState,
            header = { Text("좋아하는 동물을 선택해주세요.", style = IenTheme.typography.title3) },
            contentPadding = PaddingValues(0.dp)
        ) {
            IenBottomSheetSelect(
                options = listOf(
                    IenBottomSheetOption("강아지", "강아지"),
                    IenBottomSheetOption("고양이", "고양이"),
                    IenBottomSheetOption("토끼", "토끼")
                ),
                value = selectedPet,
                onChange = {
                    selectedPet = it
                    selectSheetState.hide()
                }
            )
        }
    }
}

@Preview
@Composable
fun BubbleSection(controls: Map<String, String> = emptyMap()) {
    val background = if (controls.enumValue("background", "Grey") == "Brand") {
        IenBubbleBackground.Brand
    } else {
        IenBubbleBackground.Grey
    }
    val withTail = controls.booleanValue("withTail", true)
    IenTheme {
        ComponentSection(title = "Bubble") {
            IenBubble(background = background, withTail = withTail) {
                Text("배경과 꼬리 속성을 적용한 버블입니다.", style = IenTheme.typography.body2)
            }
            IenBubble(
                text = controls.textValue("text", "의미 색상과 꼬리 방향도 조정할 수 있습니다."),
                tone = controls.toneValue("tone", IenSemanticTone.Neutral),
                tail = if (withTail) IenBubbleTail.End else IenBubbleTail.None,
            )
        }
    }
}

@Preview
@Composable
fun ButtonSection(controls: Map<String, String> = emptyMap()) {
    val buttonVariant = when (controls.enumValue("variant", "Fill")) {
        "Weak" -> IenButtonVariant.Weak
        "Line" -> IenButtonVariant.Line
        "Ghost" -> IenButtonVariant.Ghost
        else -> IenButtonVariant.Fill
    }
    val buttonSize = when (controls.enumValue("size", "Medium")) {
        "Small" -> IenButtonSize.Small
        "Large" -> IenButtonSize.Large
        else -> IenButtonSize.Medium
    }
    val display = when (controls.enumValue("display", "Block")) {
        "Full" -> IenButtonDisplay.Full
        "Inline" -> IenButtonDisplay.Inline
        else -> IenButtonDisplay.Block
    }
    IenTheme {
        var shapeToggleChecked by remember { mutableStateOf(true) }
        var colorToggleChecked by remember { mutableStateOf(false) }
        var iconToggleChecked by remember { mutableStateOf(true) }
        ComponentSection(title = "Button") {
            IenButton(
                onClick = {},
                size = buttonSize,
                variant = buttonVariant,
                tone = controls.toneValue("tone", IenSemanticTone.Brand),
                contentPadding = PaddingValues(
                    horizontal = controls.intValue("contentPadding", 16).coerceIn(0, 32).dp,
                    vertical = controls.intValue("verticalContentPadding", 8).coerceIn(0, 32).dp,
                ),
                state = IenButtonState(
                    enabled = !controls.disabledValue(),
                    loading = controls.booleanValue("loading", false),
                ),
                shape = if (controls.enumValue("shape", "Capsule") == "Capsule") {
                    ContinuousCapsule()
                } else {
                    ContinuousRoundedRectangle(IenTheme.radius.default)
                },
                display = display,
            ) {
                Text(controls.textValue("text", "속성 적용 미리보기"))
            }
            if (!LocalComponentVariantShowcase.current) {
                IenBorder()
                Text(
                    "Colors override",
                    style = IenTheme.typography.label2,
                    color = IenTheme.colors.textSecondary
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                ) {
                    IenButton(
                        onClick = {},
                        tone = IenSemanticTone.Success,
                    ) {
                        Text("Tone only")
                    }
                    IenButton(
                        onClick = {},
                        colors = IenButtonDefault.colors(
                            container = Color(0xFF111827),
                            content = Color.White,
                            border = Color(0xFF111827),
                        ),
                    ) {
                        Text("Colors fill")
                    }
                    IenButton(
                        onClick = {},
                        colors = IenButtonDefault.colors(
                            variant = IenButtonVariant.Line,
                            content = Color(0xFFDB2777),
                            border = Color(0xFFDB2777),
                        ),
                    ) {
                        Text("Colors line")
                    }
                    IenButton(
                        onClick = {},
                        colors = IenButtonDefault.colors(
                            container = Color(0xFF2563EB),
                            content = Color.White,
                            border = Color(0xFF2563EB),
                            containerBrush = Brush.linearGradient(
                                listOf(
                                    Color(0xFF2563EB),
                                    Color(0xFF06B6D4),
                                ),
                            ),
                        ),
                    ) {
                        Text("Brush override")
                    }
                    IenButton(
                        onClick = {},
                        colors = IenButtonDefault.colors(
                            container = Color(0xFF111827),
                            content = Color.White,
                            border = Color(0xFF111827),
                            containerBrush = null,
                        ),
                    ) {
                        Text("Solid override")
                    }
                    IenButton(
                        onClick = {},
                        tone = IenSemanticTone.Warning,
                        colors = IenButtonDefault.colors(
                            tone = IenSemanticTone.Warning,
                            useGradient = false,
                        ),
                    ) {
                        Text("No gradient")
                    }
                }
                IenBorder()
                Text(
                    "Toggle",
                    style = IenTheme.typography.label2,
                    color = IenTheme.colors.textSecondary
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                ) {
                    IenToggleButton(
                        checked = shapeToggleChecked,
                        onCheckedChange = { shapeToggleChecked = it },
                        shapes = IenToggleButtonDefault.shapes(
                            checked = ContinuousCapsule(),
                            unchecked = ContinuousRoundedRectangle(IenTheme.radius.sm),
                        ),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                            Text(if (shapeToggleChecked) "Capsule" else "Rounded")
                        }
                    }
                    IenToggleButton(
                        checked = colorToggleChecked,
                        onCheckedChange = { colorToggleChecked = it },
                        shapes = IenToggleButtonDefault.shapes(
                            checked = ContinuousCapsule(),
                            unchecked = ContinuousRoundedRectangle(IenTheme.radius.default),
                        ),
                        colors = IenToggleButtonDefault.colors(
                            checkedTone = IenSemanticTone.Success,
                            uncheckedTone = IenSemanticTone.Neutral,
                            checkedBackgroundBrush = Brush.linearGradient(
                                listOf(
                                    Color(0xFF34D399),
                                    Color(0xFF10B981),
                                    Color(0xFF2DD4BF),
                                )
                            ),
                        ),
                    ) {
                        Text(if (colorToggleChecked) "Success" else "Neutral")
                    }
                    IenIconToggleButton(
                        checked = iconToggleChecked,
                        onCheckedChange = { iconToggleChecked = it },
                        shapes = IenToggleButtonDefault.shapes(
                            checked = CircleShape,
                            unchecked = ContinuousRoundedRectangle(IenTheme.radius.sm),
                        ),
                        colors = IenToggleButtonDefault.colors(
                            checkedTone = IenSemanticTone.Info,
                            uncheckedTone = IenSemanticTone.Neutral,
                        ),
                    ) {
                        IenIcon(
                            imageVector = if (iconToggleChecked) M3SystemIcons.Filled.Check else M3SystemIcons.Filled.Close,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CardSection(controls: Map<String, String> = emptyMap()) {
    val cardVariant = if (controls.enumValue("variant", "Filled") == "Outlined") {
        IenCardVariant.Outlined
    } else {
        IenCardVariant.Filled
    }
    val cardTone = IenSemanticTone.entries.firstOrNull {
        it.name == controls.enumValue("tone", "Neutral")
    } ?: IenSemanticTone.Neutral
    val cardToneVariant = if (controls.enumValue("toneVariant", "Solid") == "Weak") {
        IenCardToneVariant.Weak
    } else {
        IenCardToneVariant.Solid
    }
    IenTheme {
        var clickedCard by remember { mutableStateOf("없음") }
        val shape = if (controls.enumValue("shape", "Rounded") == "Capsule") {
            ContinuousCapsule()
        } else {
            ContinuousRoundedRectangle(IenTheme.radius.default)
        }

        ComponentSection(title = "Card") {
            IenCard(
                variant = cardVariant,
                tone = cardTone,
                toneVariant = cardToneVariant,
                shape = shape,
                contentPadding = PaddingValues(
                    controls.intValue("contentPadding", 16).coerceIn(0, 32).dp,
                ),
                onClick = {
                    clickedCard = "${controls.enumValue("tone", "Neutral")} · ${controls.enumValue("variant", "Filled")}"
                },
            ) {
                Text("${controls.enumValue("tone", "Neutral")} · ${controls.enumValue("variant", "Filled")}")
            }
            Text(
                text = "마지막 클릭 카드: $clickedCard",
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textTertiary,
            )
        }
    }
}

@Preview
@Composable
fun ChipSection(controls: Map<String, String> = emptyMap()) {
    val variant = controls.enumValue("variant", "Assist")
    val disabled = controls.disabledValue()
    IenTheme {
        var selected by remember(controls["selected"]) {
            mutableStateOf(controls.booleanValue("selected", false))
        }
        ComponentSection(title = "Chip") {
            when (variant) {
                "ElevatedAssist" -> IenElevatedAssistChip(
                    onClick = {},
                    state = IenChipState(enabled = !disabled),
                ) { Text("Elevated Assist") }
                "Loading" -> IenAssistChip(
                    onClick = {},
                    state = IenChipState(enabled = !disabled, loading = true),
                ) { Text("Loading") }
                "Filter" -> IenFilterChip(
                    selected = selected,
                    onSelectedChange = { selected = it },
                    state = IenChipState(enabled = !disabled),
                    colors = IenChipDefault.colors(
                        tone = if (selected) IenSemanticTone.Success else IenSemanticTone.Brand,
                    ),
                    leadingIcon = if (selected) {
                        { IenIcon(M3SystemIcons.Filled.Check, contentDescription = null) }
                    } else {
                        null
                    },
                ) { Text("Filter") }
                "ElevatedFilter" -> IenElevatedFilterChip(
                    selected = selected,
                    onSelectedChange = { selected = it },
                    state = IenChipState(enabled = !disabled),
                ) { Text("Elevated Filter") }
                "Input" -> IenInputChip(
                    selected = selected,
                    onSelectedChange = { selected = it },
                    state = IenChipState(enabled = !disabled),
                    avatar = {
                        Box(
                            modifier = Modifier
                                .size(IenTheme.icon.md)
                                .clip(CircleShape)
                                .background(IenTheme.colors.surfaceRaised),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("I", style = IenTheme.typography.caption)
                        }
                    },
                    trailingIcon = {
                        IenIcon(M3SystemIcons.Filled.Close, contentDescription = "입력 제거")
                    },
                ) { Text("Input") }
                "Suggestion" -> IenSuggestionChip(
                    onClick = {},
                    state = IenChipState(enabled = !disabled),
                ) { Text("Suggestion") }
                "ElevatedSuggestion" -> IenElevatedSuggestionChip(
                    onClick = {},
                    state = IenChipState(enabled = !disabled),
                    icon = { IenIcon(M3SystemIcons.Filled.Check, contentDescription = null) },
                ) { Text("Elevated Suggestion") }
                "GradientSuggestion" -> IenSuggestionChip(
                    onClick = {},
                    state = IenChipState(enabled = !disabled),
                    colors = IenChipDefault.colors(
                        container = Color(0xFF7C3AED),
                        content = Color.White,
                        containerBrush = Brush.linearGradient(
                            listOf(Color(0xFF7C3AED), Color(0xFFEC4899)),
                        ),
                    ),
                ) { Text("Gradient") }
                else -> IenAssistChip(
                    onClick = {},
                    state = IenChipState(enabled = !disabled),
                    leadingIcon = {
                        IenIcon(M3SystemIcons.Filled.Check, contentDescription = null)
                    },
                ) { Text(controls.textValue("text", "Assist")) }
            }
        }
    }
}

@Preview
@Composable
fun FabSection(controls: Map<String, String> = emptyMap()) {
    val loading = controls.booleanValue("loading", false)
    val extended = controls.booleanValue("extended", false)
    val fabSize = when (controls.enumValue("size", "Regular")) {
        "Small" -> IenFabSize.Small
        "Large" -> IenFabSize.Large
        else -> IenFabSize.Regular
    }
    val fabVariant = when (controls.enumValue("variant", "Fill")) {
        "Weak" -> IenButtonVariant.Weak
        "Line" -> IenButtonVariant.Line
        "Ghost" -> IenButtonVariant.Ghost
        else -> IenButtonVariant.Fill
    }
    val tone = controls.toneValue("tone", IenSemanticTone.Brand)
    IenTheme {
        ComponentSection(title = "FAB") {
            IenFab(
                onClick = {},
                icon = {
                    IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                },
                text = { Text(controls.textValue("text", "작성하기")) },
                isExtended = extended,
                size = fabSize,
                variant = fabVariant,
                tone = tone,
                state = IenButtonState(enabled = !controls.disabledValue(), loading = loading),
            )
            if (!LocalComponentVariantShowcase.current) {
                IenBorder()
                Text(
                    "Extended FAB",
                    style = IenTheme.typography.label2,
                    color = IenTheme.colors.textSecondary,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                ) {
                    IenExtendedFab(
                        onClick = {},
                        variant = fabVariant,
                        tone = tone,
                        state = IenButtonState(enabled = !controls.disabledValue(), loading = loading),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IenIcon(
                                imageVector = M3SystemIcons.Filled.Check,
                                contentDescription = null
                            )
                            Text(controls.textValue("text", "작성하기"))
                        }
                    }
                    IenExtendedFab(
                        onClick = {},
                        state = IenButtonState(loading = true),
                    ) {
                        Text("로딩")
                    }
                    IenExtendedFab(
                        onClick = {},
                        state = IenButtonState(enabled = false),
                        variant = IenButtonVariant.Weak,
                    ) {
                        Text("비활성")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CheckboxSection(controls: Map<String, String> = emptyMap()) {
    val isCircle = controls.enumValue("variant", "Circle") == "Circle"
    val label = controls.textValue("label", "동의")
        .takeIf { controls.booleanValue("showLabel", true) }
    val size = controls.intValue("size", 24).coerceIn(16, 40).dp
    IenTheme {
        var checked by remember(controls["checked"]) {
            mutableStateOf(controls.booleanValue("checked", true))
        }
        val enabled = !controls.disabledValue()
        ComponentSection(title = "Checkbox") {
            if (isCircle) {
                IenCircleCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    label = label,
                    enabled = enabled,
                    size = size,
                )
            } else {
                IenLineCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    label = label,
                    enabled = enabled,
                )
            }
        }
    }
}

@Preview
@Composable
fun HighlightSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        ComponentSection(title = "Highlight") {
            IenHighlightText(
                text = controls.textValue(
                    "text",
                    "Highlight는 검색 결과나 본문 안의 중요한 텍스트를 토큰 색상으로 강조합니다.",
                ),
                highlights = listOf(controls.textValue("highlight", "Highlight"), "강조"),
                tone = controls.toneValue("tone", IenSemanticTone.Brand),
                ignoreCase = controls.booleanValue("ignoreCase", true),
            )
        }
    }
}

@Preview
@Composable
fun IconButtonSection(controls: Map<String, String> = emptyMap()) {
    val variant = when (controls.enumValue("variant", "Fill")) {
        "Weak" -> IenButtonVariant.Weak
        "Line" -> IenButtonVariant.Line
        "Ghost" -> IenButtonVariant.Ghost
        else -> IenButtonVariant.Fill
    }
    val size = when (controls.enumValue("size", "Medium")) {
        "Small" -> IenButtonSize.Small
        "Large" -> IenButtonSize.Large
        else -> IenButtonSize.Medium
    }
    IenTheme {
        ComponentSection(title = "IconButton") {
            IenIconButton(
                onClick = {},
                size = size,
                variant = variant,
                tone = controls.toneValue("tone", IenSemanticTone.Brand),
                state = IenButtonState(
                    enabled = !controls.disabledValue(),
                    loading = controls.booleanValue("loading", false),
                ),
            ) {
                IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = "속성 적용 미리보기")
            }
        }
    }
}

@Preview
@Composable
fun ListFooterSection(controls: Map<String, String> = emptyMap()) {
    val border = when (controls.enumValue("border", "Full")) {
        "Indented" -> IenListFooterBorder.Indented
        "None" -> IenListFooterBorder.None
        else -> IenListFooterBorder.Full
    }
    IenTheme {
        ComponentSection(title = "ListFooter") {
            IenListFooter(
                text = controls.textValue("text", "더 보기"),
                onClick = {},
                border = border,
            )
            IenListFooter(
                text = "더 보기 (아이콘 포함)",
                onClick = {},
                border = IenListFooterBorder.Indented,
                icon = {
                    IenIcon(
                        imageVector = M3SystemIcons.Filled.Close,
                        contentDescription = null,
                        tint = IenTheme.colors.brand
                    )
                }
            )
            IenListFooter(
                text = "기타 문의 사항 확인하기",
                onClick = {},
                border = IenListFooterBorder.None,
                textColor = IenTheme.colors.textSecondary,
            )
            IenListFooter(
                onClick = {},
                border = IenListFooterBorder.None,
                hairline = {
                    IenListFooterDefaults.Hairline(color = IenTheme.colors.brand)
                },
                shadow = {
                    IenListFooterDefaults.Shadow(color = IenTheme.colors.brand.copy(alpha = 0.15f))
                },
            ) {
                IenListFooterDefaults.Text(text = "커스텀 Hairline & Shadow 피드백 (탭해보세요)")
            }
        }
    }
}

@Preview
@Composable
fun ListHeaderSection(controls: Map<String, String> = emptyMap()) {
    val descriptionPosition = if (controls.enumValue("descriptionPosition", "Top") == "Bottom") {
        IenListHeaderDescriptionPosition.Bottom
    } else {
        IenListHeaderDescriptionPosition.Top
    }
    IenTheme {
        ComponentSection(title = "ListHeader") {
            IenListHeader(
                title = controls.textValue("title", "최근 거래"),
                description = controls.textValue("description", "타이틀 위에 보조 설명이 배치됩니다.")
                    .takeIf { controls.booleanValue("showDescription", true) },
                descriptionPosition = descriptionPosition,
                right = {
                    IenTextButton(onClick = {}) {
                        Text("전체보기")
                    }
                }
            )
            IenListHeader(
                title = "자주 쓰는 계좌",
                description = "타이틀 아래에 보조 설명이 배치됩니다.",
                descriptionPosition = IenListHeaderDescriptionPosition.Bottom,
                right = {
                    IenTextButton(onClick = {}) {
                        Text("편집")
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun LoaderSection(controls: Map<String, String> = emptyMap()) {
    val selectedSize = when (controls.enumValue("size", "Medium")) {
        "Small" -> IenLoaderSize.Small
        "Large" -> IenLoaderSize.Large
        else -> IenLoaderSize.Medium
    }
    val label = controls.textValue("label", "데이터를 불러오는 중")
        .takeIf { controls.booleanValue("showLabel", true) }
    IenTheme {
        ComponentSection(title = "Loader") {
            when (controls.enumValue("indicator", "Circular")) {
                "Loading" -> IenLoader(label = label, size = selectedSize) { modifier ->
                    IenLoadingIndicator(modifier = modifier)
                }
                "Wavy" -> IenLoader(label = label, size = selectedSize) { modifier ->
                    IenCircularWavyProgressIndicator(modifier = modifier)
                }
                else -> IenLoader(label = label, size = selectedSize)
            }
            if (!LocalComponentVariantShowcase.current) {
                IenLoaderPrimitive(color = IenTheme.colors.brand)
            }
        }
    }
}

@Preview
@Composable
fun LoadingIndicatorSection(controls: Map<String, String> = emptyMap()) {
    val size = controls.intValue("size", 48).coerceIn(24, 96).dp
    IenTheme {
        ComponentSection(title = "LoadingIndicator") {
            IenLoadingIndicator(
                modifier = Modifier.size(size),
                color = playgroundToneColor(controls.enumValue("tone", "Brand")),
            )
        }
    }
}

@Preview
@Composable
fun ProgressIndicatorSection(controls: Map<String, String> = emptyMap()) {
    val progress = controls.intValue("progress", 64).coerceIn(0, 100) / 100f
    IenTheme {
        val color = playgroundToneColor(controls.enumValue("tone", "Brand"))
        ComponentSection(title = "ProgressIndicator") {
            when (controls.enumValue("variant", "Circular")) {
                "Wavy" -> if (controls.booleanValue("determinate", false)) {
                    IenCircularWavyProgressIndicator(
                        progress = { progress },
                        color = color,
                        gapSize = controls.intValue("gapSize", 4).coerceIn(0, 16).dp,
                        wavelength = controls.intValue("wavelength", 40).coerceIn(8, 100).dp,
                        amplitude = { controls.intValue("waveAmplitude", 100).coerceIn(0, 200) / 100f },
                    )
                } else {
                    IenCircularWavyProgressIndicator(
                        color = color,
                        gapSize = controls.intValue("gapSize", 4).coerceIn(0, 16).dp,
                        wavelength = controls.intValue("wavelength", 40).coerceIn(8, 100).dp,
                        amplitude = controls.intValue("waveAmplitude", 100).coerceIn(0, 200) / 100f,
                    )
                }
                "Linear" -> IenLinearProgressIndicator(color = color)
                else -> IenCircularProgressIndicator(
                    color = color,
                    strokeWidth = controls.intValue("strokeWidth", 4).coerceIn(1, 12).dp,
                    gapSize = controls.intValue("gapSize", 4).coerceIn(0, 16).dp,
                )
            }
        }
    }
}

@Preview(heightDp = 600)
@Composable
fun MenuSection(controls: Map<String, String> = emptyMap()) {
    var menuOpen by remember(controls["visible"]) { mutableStateOf(controls.booleanValue("visible", false)) }
    var checkedMenu by remember { mutableIntStateOf(1) }

    IenTheme {
        ComponentSection(title = "Menu") {
            IenMenu.Trigger(
                open = menuOpen,
                onOpen = { menuOpen = true },
                onClose = { menuOpen = false },
                placement = IenMenu.Placement.entries.firstOrNull {
                    it.name == controls.enumValue("placement", "AnchorTopStart")
                } ?: IenMenu.Placement.AnchorTopStart,
                offset = DpOffset(0.dp, controls.intValue("offset", 8).coerceIn(0, 32).dp),
                dropdown = {
                    IenMenu.Dropdown(
                        onDismissRequest = { menuOpen = false },
                        header = { IenMenu.Header(text = "작업 선택") },
                    ) {
                        IenMenu.DropdownItem(
                            text = "수정",
                            enabled = !controls.disabledValue(),
                            onClick = {
                                menuOpen = false
                            },
                            right = {
                                IenMenu.DropdownIcon(
                                    imageVector = M3SystemIcons.Filled.Close,
                                    tint = IenTheme.colors.textTertiary,
                                )
                            },
                        )
                        IenMenu.DropdownItem(
                            text = "공유",
                            onClick = {
                                menuOpen = false
                            },
                        )
                        IenMenu.DropdownCheckItem(
                            checked = checkedMenu == 1,
                            onCheckedChange = { if (it) checkedMenu = 1 },
                            text = "첫 번째 보기",
                        )
                        IenMenu.DropdownCheckItem(
                            checked = checkedMenu == 2,
                            onCheckedChange = { if (it) checkedMenu = 2 },
                            text = "두 번째 보기",
                        )
                        IenMenu.DropdownItem(
                            text = "삭제",
                            onClick = { menuOpen = false },
                            right = {
                                IenMenu.DropdownIcon(
                                    imageVector = M3SystemIcons.Filled.Close,
                                    tint = IenTheme.colors.danger,
                                )
                            },
                        )
                    }
                }
            ) {
                IenButton(
                    onClick = { menuOpen = true },
                ) {
                    Text("메뉴 열기")
                }
            }
        }
    }
}

@Preview
@Composable
fun ModalSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        var showModal by remember(controls["visible"]) { mutableStateOf(controls.booleanValue("visible", false)) }

        ComponentSection(title = "Modal") {
            IenButton(
                onClick = { showModal = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
            ) {
                Text("모달 열기")
            }
        }

        IenModal(
            open = showModal,
            onOpenChange = { showModal = it },
        ) {
            IenModal.Overlay(onClick = { showModal = false })
            IenModal.Content(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = IenTheme.spacing.lg),
            ) {
                Text(
                    text = "모달",
                    style = IenTheme.typography.title2,
                )
                Text(
                    text = "Modal은 Overlay와 Content를 조합해서 중요한 콘텐츠를 표시합니다.",
                    style = IenTheme.typography.body2,
                    color = IenTheme.colors.textSecondary,
                )
                IenButton(
                    onClick = { showModal = false },
                    display = IenButtonDisplay.Block,
                ) {
                    Text("확인")
                }
            }
        }
    }
}

@Preview
@Composable
fun NumericSpinnerSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        ComponentSection(title = "NumericSpinner") {
            val minimum = minOf(controls.intValue("minNumber", 0), controls.intValue("maxNumber", 10))
            val maximum = maxOf(controls.intValue("minNumber", 0), controls.intValue("maxNumber", 10))
            val size = when (controls.enumValue("size", "Medium")) {
                "Tiny" -> IenNumericSpinnerSize.Tiny
                "Small" -> IenNumericSpinnerSize.Small
                "Large" -> IenNumericSpinnerSize.Large
                else -> IenNumericSpinnerSize.Medium
            }
            var value by remember(minimum, maximum, controls["number"]) {
                mutableIntStateOf(controls.intValue("number", 2).coerceIn(minimum, maximum))
            }
            IenNumericSpinner(
                number = value,
                onNumberChange = { value = it },
                minNumber = minimum,
                maxNumber = maximum,
                size = size,
                disable = controls.disabledValue(),
                decreaseAriaLabel = "수량 줄이기",
                increaseAriaLabel = "수량 늘리기",
            )
        }
    }
}

@Preview
@Composable
fun ParagraphSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        ComponentSection(title = "Paragraph") {
            IenParagraph(
                title = controls.textValue("title", "문단 컴포넌트"),
                body = controls.textValue(
                    "body",
                    "Paragraph는 본문 타이포그래피와 Highlight를 함께 사용해 긴 설명을 안정적으로 표시합니다.",
                ),
                emphasis = controls.textValue("emphasis", "Highlight").takeIf { it.isNotBlank() },
                footer = controls.textValue("footer", "토큰 기반 줄 높이와 색상을 사용합니다."),
            )
        }
    }
}

@Preview
@Composable
fun PostSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        ComponentSection(title = "Post") {
            IenPost(
                title = controls.textValue("title", "Compose Multiplatform 디자인 시스템 진행 기록"),
                author = controls.textValue("author", "IENGROUND")
                    .takeIf { controls.booleanValue("showAuthor", true) },
                description = controls.textValue(
                    "description",
                    "Post는 피드형 콘텐츠, 공지, 업데이트 카드의 공통 정보 구조입니다.",
                ).takeIf { controls.booleanValue("showDescription", true) },
                metadata = if (controls.booleanValue("showMetadata", true)) {
                    {
                        IenBadge("새 글", size = IenBadgeSize.Small)
                    }
                } else null,
            )
        }
    }
}

@Preview
@Composable
fun ProgressBarSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        var progress by remember(controls["progress"]) {
            mutableStateOf(controls.intValue("progress", 64).coerceIn(0, 100) / 100f)
        }
        ComponentSection(title = "ProgressBar") {
            val size = when (controls.enumValue("size", "Normal")) {
                "Light" -> IenProgressBarSize.Light
                "Bold" -> IenProgressBarSize.Bold
                else -> IenProgressBarSize.Normal
            }
            IenProgressBar(
                progress = progress,
                size = size,
                color = playgroundToneColor(controls.enumValue("tone", "Brand")),
                animate = controls.booleanValue("animate", false),
                showLabel = controls.booleanValue("showLabel", true),
            )
            if (!LocalComponentVariantShowcase.current) {
                IenButton(
                    onClick = { progress = if (progress == 0f) 1f else 0f },
                ) {
                    Text(if (progress == 0f) "진행 시작" else "진행 리셋")
                }
            }
        }
    }
}

@Preview
@Composable
fun ProgressStepperSection(controls: Map<String, String> = emptyMap()) {
    val variant = IenProgressStepperVariant.entries.firstOrNull {
        it.name == controls.enumValue("variant", "Compact")
    } ?: IenProgressStepperVariant.Compact
    val steps = (0 until controls.intValue("stepCount", 3).coerceIn(2, 5)).map { index ->
        IenProgressStep(
            title = listOf("유심 신청", "배송 완료", "개통 완료", "상태 확인", "완료")[index],
        )
    }
    IenTheme {
        ComponentSection(title = "ProgressStepper") {
            IenProgressStepper(
                steps = steps,
                variant = variant,
                paddingTop = IenProgressStepperPaddingTop.entries.firstOrNull {
                    it.name == controls.enumValue("paddingTop", "Wide")
                } ?: IenProgressStepperPaddingTop.Wide,
                activeStepIndex = controls.intValue("activeStepIndex", 1).coerceIn(0, steps.lastIndex),
                checkForFinish = controls.booleanValue("checkForFinish", false),
            )
        }
    }
}

@Preview
@Composable
fun RatingSection(controls: Map<String, String> = emptyMap()) {
    val max = controls.intValue("max", 5).coerceIn(1, 10)
    IenTheme {
        var rating by remember(controls["rating"], max) {
            mutableStateOf(
                controls["rating"]?.toFloatOrNull()
                    ?.takeIf { it.isFinite() }
                    ?.coerceIn(0f, max.toFloat())
                    ?: 3.8f.coerceAtMost(max.toFloat()),
            )
        }
        val size = IenRatingSize.entries.firstOrNull { it.name == controls.enumValue("size", "Medium") }
            ?: IenRatingSize.Medium
        val variant = IenRatingVariant.entries.firstOrNull {
            it.name == controls.enumValue("variant", "Full")
        } ?: IenRatingVariant.Full
        ComponentSection(title = "Rating") {
            IenRating(
                value = rating,
                onValueChange = { rating = it },
                max = max,
                size = size,
                variant = variant,
                readOnly = controls.booleanValue("readOnly", false),
                disabled = controls.booleanValue("disabled", false),
                ariaLabel = "별점 평가",
            )
        }
    }
}

@Preview
@Composable
fun ResultSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        ComponentSection(title = "Result") {
            IenResult(
                title = controls.textValue("title", "처리 준비 완료"),
                description = controls.textValue(
                    "description",
                    "Result는 성공, 실패, 빈 상태 화면을 같은 정보 구조로 표현합니다.",
                ).takeIf { controls.booleanValue("showDescription", true) },
                tone = IenResultTone.entries.firstOrNull {
                    it.name == controls.enumValue("tone", "Success")
                } ?: IenResultTone.Success,
                icon = {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(IenTheme.colors.success),
                    )
                },
                primaryAction = if (controls.booleanValue("showPrimaryAction", true)) ({
                    IenButton(onClick = {}, size = IenButtonSize.Medium) {
                        Text("확인")
                    }
                }) else null,
                secondaryAction = if (controls.booleanValue("showSecondaryAction", false)) ({
                    IenTextButton(onClick = {}) { Text("다시 시도") }
                }) else null,
            )
        }
    }
}

@Preview
@Composable
fun SearchFieldSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        var search by remember { mutableStateOf("") }
        var deletableSearch by remember { mutableStateOf("샘플 검색어") }
        var fixedSearch by remember { mutableStateOf("") }
        var disabledSearch by remember { mutableStateOf("수정할 수 없는 검색어") }
        var deleteCount by remember { mutableIntStateOf(0) }
        ComponentSection(title = "SearchField") {
            IenSearchField(
                value = search,
                onValueChange = { search = it },
                placeholder = controls.textValue("placeholder", "검색어를 입력하세요"),
                state = IenTextFieldState(enabled = !controls.disabledValue()),
            )
            IenSearchField(
                value = deletableSearch,
                onValueChange = { deletableSearch = it },
                placeholder = "검색어를 입력하고 삭제 버튼을 눌러보세요",
                onDeleteClick = { deleteCount += 1 },
            )
            Text(
                text = "삭제 버튼 클릭 ${deleteCount}회",
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textTertiary,
            )
            IenSurface(
                color = IenTheme.colors.surfaceWeak,
            ) {
                Column(
                    modifier = Modifier.padding(vertical = IenTheme.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) {
                    IenSearchField(
                        value = fixedSearch,
                        onValueChange = { fixedSearch = it },
                        placeholder = "상단 고정 검색",
                        fixed = controls.booleanValue("fixed", true),
                        takeSpace = controls.booleanValue("takeSpace", true),
                    )
                    Text(
                        text = "fixed=${controls.booleanValue("fixed", true)}, takeSpace=${controls.booleanValue("takeSpace", true)} 예시입니다. 실제 화면에서는 Scaffold topBar 같은 고정 영역에 배치합니다.",
                        modifier = Modifier.padding(horizontal = IenTheme.spacing.md),
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textTertiary,
                    )
                }
            }
            IenSearchField(
                value = disabledSearch,
                onValueChange = { disabledSearch = it },
                placeholder = "비활성 검색",
                state = IenTextFieldState(enabled = false),
            )
        }
    }
}

@Preview
@Composable
fun SegmentedControlSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        val itemCount = controls.intValue("itemCount", 3).coerceIn(2, 5)
        val selectedIndex = controls.intValue("selectedIndex", 0).coerceIn(0, itemCount - 1)
        val size = if (controls.enumValue("size", "Small") == "Large") {
            IenSegmentedControlSize.Large
        } else {
            IenSegmentedControlSize.Small
        }
        val alignment = if (controls.enumValue("alignment", "Fixed") == "Fluid") {
            IenSegmentedControlAlignment.Fluid
        } else {
            IenSegmentedControlAlignment.Fixed
        }
        var selected by remember(selectedIndex) {
            mutableStateOf(listOf("all", "progress", "done", "waiting", "failed")[selectedIndex])
        }
        ComponentSection(title = "SegmentedControl") {
            IenSegmentedControl(
                items = listOf(
                    IenSegmentedControlItem(value = "all", label = "전체"),
                    IenSegmentedControlItem(value = "progress", label = "진행"),
                    IenSegmentedControlItem(value = "done", label = "완료"),
                    IenSegmentedControlItem(value = "waiting", label = "대기"),
                    IenSegmentedControlItem(value = "failed", label = "실패"),
                ).take(itemCount),
                value = selected,
                onChange = { selected = it },
                modifier = Modifier.fillMaxWidth(),
                size = size,
                alignment = alignment,
                enabled = !controls.disabledValue(),
            )
        }
    }
}

@Preview
@Composable
fun SkeletonSection(controls: Map<String, String> = emptyMap()) {
    val pattern = IenSkeletonPattern.entries.firstOrNull {
        it.name == controls.enumValue("pattern", "TopListWithIcon")
    } ?: IenSkeletonPattern.TopListWithIcon
    val background = IenSkeletonBackground.entries.firstOrNull {
        it.name == controls.enumValue("background", "Grey")
    } ?: IenSkeletonBackground.Grey
    val play = if (controls.enumValue("play", "Show") == "Hide") {
        IenSkeletonPlay.Hide
    } else {
        IenSkeletonPlay.Show
    }
    val repeatCount = controls.intValue("repeatCount", 3).coerceIn(0, 8)
    val height = controls.intValue("height", 0).coerceIn(0, 120).takeIf { it > 0 }?.dp
    val radius = controls.intValue("radius", 8).coerceIn(0, 32).dp
    IenTheme {
        ComponentSection(title = "Skeleton") {
            IenSkeleton(
                modifier = Modifier.fillMaxWidth(),
                height = height,
                radius = radius,
                pattern = pattern,
                background = background,
                play = play,
                repeatLastItemCount = IenSkeletonRepeat.Count(repeatCount),
            )
            if (!LocalComponentVariantShowcase.current) {
                IenSkeleton(
                    modifier = Modifier.fillMaxWidth(),
                    custom = listOf(
                        IenSkeletonElement.Title,
                        IenSkeletonElement.Subtitle,
                        IenSkeletonElement.Spacer(12.dp),
                        IenSkeletonElement.Card,
                        IenSkeletonElement.Spacer(8.dp),
                        IenSkeletonElement.ListWithIcon,
                    ),
                    repeatLastItemCount = IenSkeletonRepeat.Count(2),
                )
                IenSkeletonMotionGroup(modifier = Modifier.fillMaxWidth()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm)) {
                        IenSkeleton(modifier = Modifier.weight(1f), height = 20.dp)
                        IenSkeleton(modifier = Modifier.weight(0.65f), height = 20.dp)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SliderSection(controls: Map<String, String> = emptyMap()) {
    val rangeStart = controls.intValue("rangeStart", 0).coerceIn(0, 99) / 100f
    val rangeEnd = maxOf(
        controls.intValue("rangeEnd", 100).coerceIn(1, 100) / 100f,
        rangeStart + 0.01f,
    )
    val valueRange = rangeStart..rangeEnd
    IenTheme {
        var sliderValue by remember(controls["value"], rangeStart, rangeEnd) {
            mutableStateOf((controls.intValue("value", 35) / 100f).coerceIn(valueRange))
        }
        ComponentSection(title = "Slider") {
            IenSlider(
                value = sliderValue.coerceIn(valueRange),
                onValueChange = { sliderValue = it },
                label = "비율",
                valueLabel = "${(sliderValue * 100).toInt()}%",
                steps = controls.intValue("steps", 0).coerceIn(0, 10),
                valueRange = valueRange,
                enabled = !controls.disabledValue(),
            )
        }
    }
}

@Preview
@Composable
fun SwipeBoxSection(controls: Map<String, String> = emptyMap()) {
    val itemWidth = controls.intValue("itemWidth", 96).coerceIn(56, 160).dp
    val height = controls.intValue("height", 72).coerceIn(48, 120).dp
    val fullSwipe = controls.booleanValue("fullSwipe", false)
    val haptics = controls.booleanValue("haptics", true)
    IenTheme {
        var swipeActionCount by remember { mutableIntStateOf(0) }
        ComponentSection(title = "SwipeBox") {
            Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm)) {
                Text(
                    text = "끝 방향 단일 액션 · 왼쪽으로 밀기",
                    style = IenTheme.typography.label2,
                    color = IenTheme.colors.textSecondary,
                )
                IenSwipeBox(
                    modifier = Modifier
                        .fillMaxWidth(),
                    itemWidth = itemWidth,
                    height = height,
                    endToStartFullSwipeEnabled = fullSwipe,
                    expansionHapticEnabled = haptics,
                    actionItemBuilder = {
                        end(key = "end-delete") {
                            IenSwipeBoxItem(
                                onClick = { swipeActionCount += 1 },
                                tone = IenSemanticTone.Danger,
                                onClickLabel = "삭제",
                                icon = M3SystemIcons.Filled.Close,
                                label = "삭제",
                            )
                        }
                    },
                ) {
                    SwipeBoxSampleContent(title = "끝 액션 하나")
                }

                Text(
                    text = "시작 방향 단일 액션 · 오른쪽으로 밀기",
                    style = IenTheme.typography.label2,
                    color = IenTheme.colors.textSecondary,
                )
                IenSwipeBox(
                    modifier = Modifier
                        .fillMaxWidth(),
                    itemWidth = itemWidth,
                    height = height,
                    startToEndFullSwipeEnabled = fullSwipe,
                    expansionHapticEnabled = haptics,
                    actionItemBuilder = {
                        start(key = "start-complete") {
                            IenSwipeBoxItem(
                                onClick = { swipeActionCount += 1 },
                                tone = IenSemanticTone.Success,
                                onClickLabel = "완료",
                                icon = M3SystemIcons.Filled.Check,
                                label = "완료",
                            )
                        }
                    },
                ) {
                    SwipeBoxSampleContent(title = "시작 액션 하나")
                }

                Text(
                    text = "양방향 · 여러 액션",
                    style = IenTheme.typography.label2,
                    color = IenTheme.colors.textSecondary,
                )
                IenSwipeBox(
                    modifier = Modifier
                        .fillMaxWidth(),
                    itemWidth = itemWidth,
                    height = height,
                    startToEndFullSwipeEnabled = fullSwipe,
                    endToStartFullSwipeEnabled = fullSwipe,
                    expansionHapticEnabled = haptics,
                    actionItemBuilder = {
                        start(key = "start-complete") {
                            IenSwipeBoxItem(
                                onClick = { swipeActionCount += 1 },
                                tone = IenSemanticTone.Success,
                                onClickLabel = "완료",
                                icon = M3SystemIcons.Filled.Check,
                                label = "완료",
                            )
                        }
                        start(key = "start-save") {
                            IenSwipeBoxItem(
                                onClick = { swipeActionCount += 1 },
                                tone = IenSemanticTone.Brand,
                                onClickLabel = "보관",
                                icon = M3SystemIcons.Filled.FilledSave,
                                label = "보관",
                            )
                        }
                        end(key = "end-more") {
                            IenSwipeBoxItem(
                                onClick = { swipeActionCount += 1 },
                                tone = IenSemanticTone.Warning,
                                onClickLabel = "더보기",
                                icon = M3SystemIcons.Filled.FilledMoreVert,
                                label = "더보기",
                            )
                        }
                        end(key = "end-delete") {
                            IenSwipeBoxItem(
                                onClick = { swipeActionCount += 1 },
                                tone = IenSemanticTone.Danger,
                                onClickLabel = "삭제",
                                icon = M3SystemIcons.Filled.Close,
                                label = "삭제",
                            )
                        }
                    },
                ) {
                    SwipeBoxSampleContent(title = "시작 2개 · 끝 2개")
                }

                Text(
                    text = "액션 실행 ${swipeActionCount}회",
                    style = IenTheme.typography.caption,
                    color = IenTheme.colors.textTertiary,
                )
            }
        }
    }
}

@Composable
private fun SwipeBoxSampleContent(title: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = IenTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title)
    }
}

@Preview
@Composable
fun StepperSection(controls: Map<String, String> = emptyMap()) {
    val textType = controls.enumValue("textType", "A")
    IenTheme {
        ComponentSection(title = "Stepper") {
            IenStepper(
                modifier = Modifier.fillMaxWidth(),
                play = controls.booleanValue("play", true),
                delay = controls.floatValue("delay", 0f).coerceIn(0f, 1000f),
                staggerDelay = controls.floatValue("staggerDelay", 0.12f).coerceIn(0f, 1000f),
            ) {
                Row(
                    left = { IenStepperNumberIcon(number = 1) },
                    center = {
                        IenStepperTexts(
                            type = IenStepperTextsType.entries.firstOrNull { it.name == textType }
                                ?: IenStepperTextsType.A,
                            title = "IenStepperTextsType.A (label1)",
                            description = "body2 보통 본문 설명",
                        )
                    },
                    right = { IenStepperRightArrow() },
                )
                Row(
                    left = {
                        IenStepperAssetFrame(
                            shape = IenStepperAssetFrameShape.CircleMedium,
                            colors = IenStepperAssetFrameDefaults.colors(
                                backgroundColor = IenTheme.colors.brandWeak,
                                contentColor = IenTheme.colors.brand
                            ),
                        ) {
                            IenIcon(
                                imageVector = M3SystemIcons.Filled.Check,
                                contentDescription = null
                            )
                        }
                    },
                    center = {
                        IenStepperTexts(
                            type = IenStepperTextsType.B,
                            title = "IenStepperTextsType.B (title3)",
                            description = "body2 보통 본문 설명",
                        )
                    },
                    right = {
                        IenStepperRightButton(
                            text = "보기",
                            onClick = {},
                        )
                    },
                )
                Row(
                    left = { IenStepperNumberIcon(number = 3) },
                    center = {
                        IenStepperTexts(
                            type = IenStepperTextsType.C,
                            title = "IenStepperTextsType.C (body1)",
                            description = "caption 보조 캡션 설명",
                        )
                    },
                    hideLine = true,
                )
            }

            IenDivider()
            Text("IenStepperAssetFrameShape", style = IenTheme.typography.label1)
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) {
                    IenStepperAssetFrame(shape = IenStepperAssetFrameShape.CircleMedium) {
                        IenIcon(M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    Text("CircleMedium", style = IenTheme.typography.caption)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) {
                    IenStepperAssetFrame(shape = IenStepperAssetFrameShape.RoundedMedium) {
                        IenIcon(M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    Text("RoundedMedium", style = IenTheme.typography.caption)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) {
                    IenStepperAssetFrame(shape = IenStepperAssetFrameShape.CleanW24) {
                        IenIcon(M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    Text("CleanW24", style = IenTheme.typography.caption)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) {
                    IenStepperAssetFrame(shape = IenStepperAssetFrameShape.CleanW32) {
                        IenIcon(M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    Text("CleanW32", style = IenTheme.typography.caption)
                }
            }
        }
    }
}

@Preview
@Composable
fun SwitchSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        var switched by remember(controls["checked"]) {
            mutableStateOf(controls.booleanValue("checked", true))
        }
        var switchedWithTrackIcon by remember { mutableStateOf(true) }
        ComponentSection(title = "Switch") {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("자동 적용", modifier = Modifier.weight(1f))
                    IenSwitch(
                        checked = switched,
                        onCheckedChange = { switched = it },
                        enabled = !controls.disabledValue(),
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("트랙 아이콘", modifier = Modifier.weight(1f))
                    IenSwitch(
                        checked = switchedWithTrackIcon,
                        onCheckedChange = { switchedWithTrackIcon = it },
                        enabled = !controls.disabledValue(),
                        onTrackContent = {
                            IenIcon(
                                imageVector = M3SystemIcons.Filled.Check,
                                contentDescription = null,
                                size = 16.dp,
                                tint = IenTheme.colors.surface,
                            )
                        },
                        offTrackContent = {
                            IenIcon(
                                imageVector = M3SystemIcons.Filled.Close,
                                contentDescription = null,
                                size = 16.dp,
                                tint = IenTheme.colors.surface,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TabSection(controls: Map<String, String> = emptyMap()) {
    val selectedIndex = controls.intValue("selectedIndex", 0).coerceIn(0, 2)
    val size = IenTabSize.entries.firstOrNull {
        it.name == controls.enumValue("size", "Large")
    } ?: IenTabSize.Large
    val fluid = controls.enumValue("layout", "Fixed") == "Fluid"
    val enabled = !controls.disabledValue()
    IenTheme {
        var tabSelected by remember(selectedIndex) {
            mutableIntStateOf(selectedIndex)
        }
        ComponentSection(title = "Tab") {
            IenTab(
                items = listOf(
                    IenTabItem("요약", key = "summary", enabled = enabled),
                    IenTabItem("상세", key = "detail", badge = -1, enabled = enabled),
                    IenTabItem("내역", key = "history", badge = 120, enabled = enabled),
                ),
                selectedIndex = tabSelected,
                onSelectedIndexChange = { tabSelected = it },
                size = size,
                fluid = fluid,
                itemGap = controls.intValue("itemGap", if (fluid) 36 else 8).coerceIn(0, 32).dp,
                modifier = Modifier.fillMaxWidth(),
                ariaLabel = "주문 정보 탭",
            )
        }
    }
}

@Preview
@Composable
fun TableRowSection(controls: Map<String, String> = emptyMap()) {
    val align = if (controls.enumValue("align", "SpaceBetween") == "Left") {
        IenTableRowAlign.Left
    } else {
        IenTableRowAlign.SpaceBetween
    }
    val leftRatio = controls.intValue("leftRatio", 30).coerceIn(10, 90)
    IenTheme {
        ComponentSection(title = "TableRow") {
            IenTableRow(
                left = controls.textValue("left", "엔님"),
                right = controls.textValue("right", "받는 분"),
                align = align,
                leftRatio = leftRatio,
            )
            IenTableRow(
                left = "아이엔",
                right = "받는 분 통장표시",
                align = IenTableRowAlign.Left,
            )
            IenTableRow(
                left = "이체 1일 전",
                right = "미리알림",
                align = IenTableRowAlign.Left,
                leftRatio = 30,
            )
            IenTableRow(
                label = "상품 금액",
                value = "32,000원",
                description = "할인 전 금액",
            )
            IenTableRow(
                label = "최종 결제",
                value = "28,000원",
                trailing = {
                    IenBadge(
                        "할인",
                        size = IenBadgeSize.Small,
                        tone = IenSemanticTone.Success
                    )
                },
            )
        }
    }
}

@Preview
@Composable
fun TextButtonSection(controls: Map<String, String> = emptyMap()) {
    val size = IenTextButtonSize.entries.firstOrNull { it.name == controls.enumValue("size", "Medium") }
        ?: IenTextButtonSize.Medium
    val variant = IenTextButtonVariant.entries.firstOrNull {
        it.name == controls.enumValue("variant", "Clear")
    } ?: IenTextButtonVariant.Clear
    IenTheme {
        ComponentSection(title = "TextButton") {
            IenTextButton(
                size = size,
                variant = variant,
                tone = controls.toneValue("tone", IenSemanticTone.Brand),
                state = IenButtonState(enabled = !controls.disabledValue()),
                onClick = {},
            ) {
                Text("${variant.name} · ${size.name}")
            }
        }
    }
}

@Preview
@Composable
fun SnackbarSection(
    controls: Map<String, String> = emptyMap(),
    onShowBasic: () -> Unit = {},
    onShowSuccess: () -> Unit = {},
    onShowAction: () -> Unit = {},
    onShowCompact: () -> Unit = {},
    onShowQueued: () -> Unit = {},
    onShowShortDuration: () -> Unit = {},
    onShowLongDuration: () -> Unit = {},
    onShowIndefiniteDuration: () -> Unit = {},
) {
    IenTheme {
        ComponentSection(title = "Snackbar") {
            Text(
                text = "${controls.textValue("message", "기본 스낵바 메시지예요")} · ${controls.enumValue("tone", "Neutral")}",
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textSecondary,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            ) {
                IenButton(
                    onClick = onShowBasic,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("기본") }
                IenButton(
                    onClick = onShowSuccess,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("성공") }
                IenButton(
                    onClick = onShowAction,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("액션") }
                IenButton(
                    onClick = onShowCompact,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("최대폭") }
                IenButton(
                    onClick = onShowQueued,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("여러 개") }
                IenButton(
                    onClick = onShowShortDuration,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("Short") }
                IenButton(
                    onClick = onShowLongDuration,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("Long") }
                IenButton(
                    onClick = onShowIndefiniteDuration,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("Indefinite") }
            }
        }
    }
}

@Preview
@Composable
fun ToastSection(
    controls: Map<String, String> = emptyMap(),
    onShowBasic: () -> Unit = {},
    onShowSuccess: () -> Unit = {},
    onShowLong: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    IenTheme {
        ComponentSection(title = "Toast") {
            Text(
                text = "${controls.textValue("message", "기본 토스트 메시지예요")} · ${controls.enumValue("tone", "Neutral")}",
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textSecondary,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            ) {
                IenButton(
                    onClick = onShowBasic,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("기본") }
                IenButton(
                    onClick = onShowSuccess,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("성공") }
                IenButton(
                    onClick = onShowLong,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("Long") }
                IenButton(
                    onClick = onDismiss,
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                ) { Text("닫기") }
            }
        }
    }
}

@Preview
@Composable
fun TooltipSection(controls: Map<String, String> = emptyMap()) {
    val placement = when (controls.enumValue("placement", "Top")) {
        "Bottom" -> IenTooltipPlacement.Bottom
        "Left" -> IenTooltipPlacement.Left
        "Right" -> IenTooltipPlacement.Right
        else -> IenTooltipPlacement.Top
    }
    val messageAlign = if (controls.enumValue("messageAlign", "Left") == "Center") {
        IenTooltipMessageAlign.Center
    } else {
        IenTooltipMessageAlign.Left
    }
    val motionVariant = if (controls.enumValue("motionVariant", "Weak") == "Strong") {
        IenTooltipMotionVariant.Strong
    } else {
        IenTooltipMotionVariant.Weak
    }
    val strategy = IenTooltipStrategy.entries.firstOrNull {
        it.name == controls.enumValue("strategy", "Absolute")
    } ?: IenTooltipStrategy.Absolute
    val clipToEnd = IenTooltipClipToEnd.entries.firstOrNull {
        it.name == controls.enumValue("clipToEnd", "None")
    } ?: IenTooltipClipToEnd.None
    val tooltipText = controls.textValue("text", "툴팁은 짧은 보조 설명에 사용합니다.")
    val anchorPosition = controls.intValue("anchorPosition", 50).coerceIn(0, 100) / 100f
    val width = controls.intValue("width", 0).coerceIn(0, 320).takeIf { it > 0 }?.dp
    val offset = controls.intValue("offset", 0).coerceIn(0, 32).takeIf { it > 0 }?.dp
    val defaultOpen = controls.booleanValue("defaultOpen", false)
    val openOnHover = controls.booleanValue("openOnHover", false)
    val openOnFocus = controls.booleanValue("openOnFocus", false)
    if (LocalComponentVariantShowcase.current) {
        IenTheme {
            ComponentSection(title = "Tooltip") {
                IenTooltip(
                    text = tooltipText,
                    placement = placement,
                    messageAlign = messageAlign,
                    motionVariant = motionVariant,
                    strategy = strategy,
                    clipToEnd = clipToEnd,
                    anchorPositionByRatio = anchorPosition,
                    offset = offset,
                    width = width,
                    defaultOpen = defaultOpen,
                    openOnHover = openOnHover,
                    openOnFocus = openOnFocus,
                    tone = controls.toneValue("tone", IenSemanticTone.Neutral),
                    dismissible = controls.booleanValue("dismissible", false),
                    autoFlip = controls.booleanValue("autoFlip", false),
                    fitContentWidth = controls.booleanValue("fitContentWidth", false),
                    anchor = { IenBadge("도움말", variant = IenBadgeVariant.Line) },
                )
            }
        }
        return
    }
    IenTheme {
        var controlledTooltipOpen by remember { mutableStateOf(false) }
        ComponentSection(title = "Tooltip") {
            IenTooltip(
                text = tooltipText,
                placement = placement,
                messageAlign = messageAlign,
                motionVariant = motionVariant,
                strategy = strategy,
                clipToEnd = clipToEnd,
                anchorPositionByRatio = anchorPosition,
                offset = offset,
                width = width,
                defaultOpen = defaultOpen,
                openOnHover = openOnHover,
                openOnFocus = openOnFocus,
                tone = controls.toneValue("tone", IenSemanticTone.Neutral),
                dismissible = controls.booleanValue("dismissible", false),
                autoFlip = controls.booleanValue("autoFlip", false),
                fitContentWidth = controls.booleanValue("fitContentWidth", false),
                anchor = { toggle -> IenBadge("도움말", variant = IenBadgeVariant.Line) },
            )
            IenTooltip(
                text = {
                    Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs)) {
                        IenBadge("TIP", variant = IenBadgeVariant.Weak)
                        Text("Composable 콘텐츠도 사용할 수 있습니다.")
                    }
                },
                anchor = { toggle -> IenBadge("콘텐츠", variant = IenBadgeVariant.Weak) },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                IenTooltip(
                    text = "상단에 뜨는 도움말입니다.",
                    placement = IenTooltipPlacement.Top,
                    anchor = { toggle ->
                        IenButton(size = IenButtonSize.Small, onClick = toggle) {
                            Text("Top")
                        }
                    },
                )
                IenTooltip(
                    text = "중앙 정렬 툴팁은 메시지를 정중앙에 보여줍니다.",
                    messageAlign = IenTooltipMessageAlign.Center,
                    width = 180.dp,
                    anchor = { toggle ->
                        IenButton(size = IenButtonSize.Small, onClick = toggle) {
                            Text("Center")
                        }
                    },
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                IenTooltip(
                    text = "화살표 위치 0.15",
                    anchorPositionByRatio = 0.15f,
                    clipToEnd = IenTooltipClipToEnd.Left,
                    anchor = { toggle -> IenBadge("Left", variant = IenBadgeVariant.Weak) },
                )
                IenTooltip(
                    text = "강한 모션",
                    open = controlledTooltipOpen,
                    onOpenChange = { controlledTooltipOpen = it },
                    motionVariant = IenTooltipMotionVariant.Strong,
                    dismissible = true,
                    anchor = { toggle ->
                        IenButton(
                            size = IenButtonSize.Small,
                            onClick = { controlledTooltipOpen = !controlledTooltipOpen },
                        ) {
                            Text("Toggle")
                        }
                    },
                )
                IenTooltip(
                    text = "강한 모션",
                    open = controlledTooltipOpen,
                    onOpenChange = { controlledTooltipOpen = it },
                    motionVariant = IenTooltipMotionVariant.Strong,
                    dismissible = true,
                    anchor = { toggle ->
                        IenButton(
                            size = IenButtonSize.Small,
                            onClick = { controlledTooltipOpen = !controlledTooltipOpen },
                        ) {
                            Text("Toggle")
                        }
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun TopSection(controls: Map<String, String> = emptyMap()) {
    val titleSize = IenTopTitleSize.entries.firstOrNull {
        it.name == controls.enumValue("titleSize", "Default")
    } ?: IenTopTitleSize.Default
    val subtitleSize = IenTopSubtitleSize.entries.firstOrNull {
        it.name == controls.enumValue("subtitleSize", "Small")
    } ?: IenTopSubtitleSize.Small
    val selectorType = IenTopSelectorType.entries.firstOrNull {
        it.name == controls.enumValue("selectorType", "Arrow")
    } ?: IenTopSelectorType.Arrow
    val upperGap = controls.intValue("upperGap", 24).coerceIn(0, 48).dp
    val lowerGap = controls.intValue("lowerGap", 24).coerceIn(0, 48).dp
    val contentPadding = PaddingValues(
        horizontal = controls.intValue("contentPadding", 24).coerceIn(0, 48).dp,
        vertical = controls.intValue("contentPaddingVertical", 0).coerceIn(0, 48).dp,
    )
    val rightVerticalAlign = IenTopRightVerticalAlign.entries.firstOrNull {
        it.name == controls.enumValue("rightVerticalAlign", "Center")
    } ?: IenTopRightVerticalAlign.Center
    if (LocalComponentVariantShowcase.current) {
        IenTheme {
            ComponentSection(title = "Top") {
                IenTop(
                    upperGap = upperGap,
                    lowerGap = lowerGap,
                    contentPadding = contentPadding,
                    upper = {
                        IenTopSubtitleSelector(
                            text = "계좌 선택",
                            onClick = {},
                            type = selectorType,
                            size = subtitleSize,
                        )
                    },
                    title = {
                        IenTopTitleParagraph(controls.textValue("title", "결제 확인"), size = titleSize)
                    },
                    subtitleBottom = {
                        IenTopSubtitleParagraph(
                            text = controls.textValue("subtitle", "타이틀과 보조 설명의 크기를 선택합니다."),
                            size = subtitleSize,
                        )
                    },
                    rightVerticalAlign = rightVerticalAlign,
                )
            }
        }
        return
    }
        IenTheme {
            ComponentSection(title = "Top") {
                IenTop(
                    upperGap = upperGap,
                    lowerGap = lowerGap,
                    contentPadding = contentPadding,
                    title = {
                        IenTopTitleParagraph(controls.textValue("title", "결제 확인"), size = titleSize)
                    },
                    upper = {
                        IenTopSubtitleSelector(
                            text = "계좌 선택",
                            onClick = {},
                            type = selectorType,
                            size = subtitleSize,
                        )
                    },
                    subtitleBottom = {
                        IenTopSubtitleParagraph(
                            text = controls.textValue("subtitle", "타이틀과 보조 설명의 크기를 선택합니다."),
                            size = subtitleSize,
                        )
                    },
                    right = { IenBadge("v4", size = IenBadgeSize.Small) },
                    rightVerticalAlign = rightVerticalAlign,
                )
            IenTop(
                upperGap = IenTheme.spacing.md,
                lowerGap = IenTheme.spacing.md,
                upper = {
                    IenTopUpperAssetContent {
                        IenAssetFrame(
                            size = IenAssetFrameSize.Large,
                            tone = IenSemanticTone.Brand,
                            shape = IenAssetFrameShape.Circle,
                            contentDescription = "결제 자산",
                        ) {
                            Text("₩")
                        }
                    }
                },
                subtitleTop = {
                    IenTopSubtitleBadges(
                        badges = listOf(
                            IenTopSubtitleBadge("안전결제", tone = IenSemanticTone.Success),
                            IenTopSubtitleBadge("오늘", tone = IenSemanticTone.Neutral),
                        ),
                    )
                },
                title = {
                    IenTopTitleSelector(
                        text = "아이엔페이 결제",
                        onClick = {},
                    )
                },
                subtitleBottom = {
                    IenTopSubtitleParagraph(
                        text = "결제 수단과 혜택을 확인해 주세요.",
                        size = IenTopSubtitleSize.Medium,
                    )
                },
                right = {
                    IenTopRightAssetContent {
                        IenAssetFrame(
                            size = IenAssetFrameSize.Medium,
                            tone = IenSemanticTone.Info,
                            bordered = true,
                            contentDescription = "혜택",
                        ) {
                            Text("%")
                        }
                    }
                },
                rightVerticalAlign = IenTopRightVerticalAlign.Center,
                lower = {
                    IenTopLowerButton(
                        text = "혜택 보기",
                        onClick = {},
                    )
                },
            )
            IenTop(
                upperGap = IenTheme.spacing.md,
                lowerGap = IenTheme.spacing.md,
                subtitleTop = {
                    IenTopSubtitleSelector(
                        text = "계좌 선택",
                        onClick = {},
                        type = IenTopSelectorType.Arrow,
                        size = IenTopSubtitleSize.Small,
                    )
                },
                title = {
                    IenTopTitleParagraph(
                        text = "어디로 보낼까요?",
                        size = IenTopTitleSize.Large,
                    )
                },
                subtitleBottom = {
                    IenTopSubtitleTextButton(
                        text = "최근 보낸 사람 불러오기",
                        onClick = {},
                    )
                },
                right = {
                    IenTopRightButton(
                        text = "관리",
                        onClick = {},
                        size = IenButtonSize.Small,
                        variant = IenButtonVariant.Weak,
                    )
                },
                rightVerticalAlign = IenTopRightVerticalAlign.End,
                lower = {
                    IenTopLowerCTA(
                        leftButton = {
                            IenTopLowerCTAButton(
                                text = "취소",
                                onClick = {},
                                variant = IenButtonVariant.Weak,
                                tone = IenSemanticTone.Neutral,
                            )
                        },
                        rightButton = {
                            IenTopLowerCTAButton(
                                text = "다음",
                                onClick = {},
                            )
                        },
                    )
                },
            )
            IenTop(
                upperGap = IenTheme.spacing.sm,
                lowerGap = IenTheme.spacing.sm,
                title = {
                    IenTopTitleTextButton(
                        text = "선택 가능한 타이틀",
                        onClick = {},
                        variant = IenTextButtonVariant.Arrow,
                    )
                },
                subtitleBottom = {
                    IenTopSubtitleParagraph(
                        text = "title 자체가 버튼인 케이스",
                        size = IenTopSubtitleSize.Small,
                    )
                },
                right = {
                    IenBadge(
                        text = "New",
                        size = IenBadgeSize.Small,
                        variant = IenBadgeVariant.Fill,
                    )
                },
            )

            IenDivider()
            Text("IenTopSelectorType", style = IenTheme.typography.label1)
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                IenTopSubtitleSelector(
                    text = "IenTopSelectorType.Arrow",
                    onClick = {},
                    type = IenTopSelectorType.Arrow,
                )
                IenTopSubtitleSelector(
                    text = "IenTopSelectorType.Clear",
                    onClick = {},
                    type = IenTopSelectorType.Clear,
                )
            }
        }
    }
}

@Preview
@Composable
fun AgreementSection(controls: Map<String, String> = emptyMap()) {
    val enabled = !controls.disabledValue()
    val agreementVariant = IenAgreementVariant.entries.firstOrNull {
        it.name == controls.enumValue("variant", "Large")
    } ?: IenAgreementVariant.Large
    val checkboxVariant = IenAgreementCheckboxVariant.entries.firstOrNull {
        it.name == controls.enumValue("checkboxVariant", "Checkbox")
    } ?: IenAgreementCheckboxVariant.Checkbox
    val descriptionVariant = IenAgreementDescriptionVariant.entries.firstOrNull {
        it.name == controls.enumValue("descriptionVariant", "Normal")
    } ?: IenAgreementDescriptionVariant.Normal
    val badgeVariant = IenAgreementBadgeVariant.entries.firstOrNull {
        it.name == controls.enumValue("badgeVariant", "Clear")
    } ?: IenAgreementBadgeVariant.Clear
    val necessityVariant = IenAgreementNecessityVariant.entries.firstOrNull {
        it.name == controls.enumValue("necessityVariant", "Mandatory")
    } ?: IenAgreementNecessityVariant.Mandatory
    if (LocalComponentVariantShowcase.current) {
        IenTheme {
            var checked by remember { mutableStateOf(true) }
            ComponentSection(title = "Agreement (TDS v4 Spec)") {
                IenAgreement(
                    variant = agreementVariant,
                    onClick = if (enabled) ({ checked = !checked }) else null,
                    left = {
                        IenAgreementCheckbox(
                            checked = checked,
                            onCheckedChange = { checked = it },
                            variant = checkboxVariant,
                            enabled = enabled,
                        )
                    },
                    middle = {
                        IenAgreementText(
                            text = "약관 동의 · ${agreementVariant.name}",
                            necessity = { IenAgreementNecessity(necessityVariant) },
                            enabled = enabled,
                        )
                    },
                    right = { IenAgreementBadge(text = "안심", variant = badgeVariant) },
                )
                IenAgreementDescription(
                    text = "수집된 정보는 약관에 따라 안전하게 처리됩니다.",
                    variant = descriptionVariant,
                )
            }
        }
        return
    }
    IenTheme {
        var agreements by remember {
            mutableStateOf(
                listOf(
                    IenAgreementItem(
                        id = "service",
                        title = "서비스 이용약관",
                        checked = true,
                        required = true
                    ),
                    IenAgreementItem(
                        id = "privacy",
                        title = "개인정보 처리방침",
                        checked = false,
                        required = true
                    ),
                    IenAgreementItem(
                        id = "marketing",
                        title = "마케팅 정보 수신",
                        checked = false,
                        required = false,
                        description = "혜택과 이벤트 소식을 받을 수 있습니다.",
                        indent = true,
                    ),
                    IenAgreementItem(
                        id = "disabled",
                        title = "만료된 약관",
                        checked = false,
                        required = false,
                        description = "지금은 선택할 수 없습니다.",
                        enabled = false,
                    ),
                ),
            )
        }
        val displayedAgreements = agreements.map { item ->
            if (item.id == "service") item.copy(enabled = enabled) else item
        }
        var singleChecked by remember { mutableStateOf(false) }
        var dotChecked by remember { mutableStateOf(true) }

        var accordionOpen by remember { mutableStateOf(false) }
        var collapsibleChecked1 by remember { mutableStateOf(false) }
        var collapsibleChecked2 by remember { mutableStateOf(false) }

        var indentPushed by remember { mutableStateOf(true) }
        var indentChecked1 by remember { mutableStateOf(false) }
        var indentChecked2 by remember { mutableStateOf(false) }

        ComponentSection(title = "Agreement (TDS v4 Spec)") {
            Text(
                text = "1. 단일 동의 항목 (체크박스 / 도트 / 히든)",
                style = IenTheme.typography.label2,
                color = IenTheme.colors.textSecondary
            )

            IenAgreement(
                variant = agreementVariant,
                onClick = { singleChecked = !singleChecked },
                left = {
                    IenAgreementCheckbox(
                        checked = singleChecked,
                        onCheckedChange = { singleChecked = it },
                        enabled = enabled,
                    )
                },
                middle = {
                    IenAgreementText(
                        text = "서비스 필수 이용약관 동의",
                        necessity = { IenAgreementNecessity(IenAgreementNecessityVariant.Mandatory) }
                    )
                },
                right = {
                    IenAgreementBadge(text = "안심", variant = IenAgreementBadgeVariant.Clear)
                }
            )

            IenAgreement(
                variant = agreementVariant,
                onClick = { dotChecked = !dotChecked },
                left = {
                    IenAgreementCheckbox(
                        checked = dotChecked,
                        onCheckedChange = { dotChecked = it },
                        variant = IenAgreementCheckboxVariant.Dot
                    )
                },
                middle = {
                    IenAgreementText(
                        text = "이벤트 혜택 알림 및 수신 동의 (도트형)",
                        necessity = { IenAgreementNecessity(IenAgreementNecessityVariant.Optional) }
                    )
                },
                right = {
                    IenAgreementRightArrow(onClick = {})
                }
            )

            IenDivider()

            Text(
                text = "2. 접었다 펼치는 아코디언 동의 (Collapsible)",
                style = IenTheme.typography.label2,
                color = IenTheme.colors.textSecondary
            )

            IenAgreementCollapsible(
                collapsed = !accordionOpen,
                onCollapsedChange = { accordionOpen = !it }
            ) {
                IenAgreementCollapsibleTrigger {
                    IenAgreement(
                        variant = agreementVariant,
                        middle = {
                            IenAgreementText(text = "개인정보 수집 동의 (오른쪽 화살표 클릭)")
                        },
                        right = {
                            IenAgreementRightArrow()
                        }
                    )
                }
                IenAgreementCollapsibleContent {
                    IenAgreement(
                        variant = agreementVariant,
                        onClick = { collapsibleChecked1 = !collapsibleChecked1 },
                        left = {
                            IenAgreementCheckbox(
                                checked = collapsibleChecked1,
                                onCheckedChange = { collapsibleChecked1 = it })
                        },
                        middle = {
                            IenAgreementText(text = "이름, 전화번호 수집 동의")
                        }
                    )
                    IenAgreement(
                        variant = agreementVariant,
                        onClick = { collapsibleChecked2 = !collapsibleChecked2 },
                        left = {
                            IenAgreementCheckbox(
                                checked = collapsibleChecked2,
                                onCheckedChange = { collapsibleChecked2 = it })
                        },
                        middle = {
                            IenAgreementText(text = "이메일, 배송지 주소 수집 동의")
                        }
                    )
                    IenAgreementDescription(
                        text = "수집된 개인정보는 서비스 배송 목적으로만 활용되며, 탈퇴 시 즉시 파기됩니다. (DescriptionVariant.Box)",
                        variant = IenAgreementDescriptionVariant.Box
                    )
                    IenAgreementDescription(
                        text = "수집된 개인정보는 서비스 배송 목적으로만 활용되며, 탈퇴 시 즉시 파기됩니다. (DescriptionVariant.Normal)",
                        variant = IenAgreementDescriptionVariant.Normal
                    )
                }
            }

            IenDivider()

            Text(
                text = "3. 여러 동의 항목 그룹화 (Group)",
                style = IenTheme.typography.label2,
                color = IenTheme.colors.textSecondary
            )

            IenAgreementGroup {
                IenAgreement(
                    variant = agreementVariant,
                    left = {
                        IenAgreementCheckbox(
                            checked = false,
                            onCheckedChange = {},
                            variant = IenAgreementCheckboxVariant.Hidden
                        )
                    },
                    middle = { IenAgreementText(text = "카드상품 이외의 부수서비스 안내 등을 위한 수집/이용") }
                )
                IenAgreement(
                    variant = agreementVariant,
                    left = {
                        IenAgreementCheckbox(
                            checked = false,
                            onCheckedChange = {},
                            variant = IenAgreementCheckboxVariant.Hidden
                        )
                    },
                    middle = { IenAgreementText(text = "개인(신용)정보 수집/이용") }
                )
                IenAgreement(
                    variant = agreementVariant,
                    left = {
                        IenAgreementCheckbox(
                            checked = false,
                            onCheckedChange = {},
                            variant = IenAgreementCheckboxVariant.Hidden
                        )
                    },
                    middle = { IenAgreementText(text = "전자적 매체를 통한 광고성 정보 수신") }
                )
            }

            IenDivider()

            Text(
                text = "4. 동적 들여쓰기 동의 (IndentPushable)",
                style = IenTheme.typography.label2,
                color = IenTheme.colors.textSecondary
            )

            IenAgreementIndentPushable(
                pushed = indentPushed,
                onPushedChange = { indentPushed = it },
            ) {
                IenAgreementIndentPushableTrigger {
                    IenAgreement(
                        variant = agreementVariant,
                        middle = {
                            IenAgreementText(text = "들여쓰기 컨트롤 헤더 (클릭 시 하위 들여쓰기 토글)")
                        },
                        right = {
                            IenAgreementBadge(
                                text = if (indentPushed) "들여쓰기 켬" else "들여쓰기 끔",
                                variant = IenAgreementBadgeVariant.Fill
                            )
                        }
                    )
                }
                IenAgreementIndentPushableContent {
                    IenAgreement(
                        variant = agreementVariant,
                        onClick = { indentChecked1 = !indentChecked1 },
                        left = {
                            IenAgreementCheckbox(
                                checked = indentChecked1,
                                onCheckedChange = { indentChecked1 = it },
                                variant = IenAgreementCheckboxVariant.Dot
                            )
                        },
                        middle = {
                            IenAgreementText(text = "고유식별정보 수집/이용 동의")
                        }
                    )
                    IenAgreement(
                        variant = agreementVariant,
                        onClick = { indentChecked2 = !indentChecked2 },
                        left = {
                            IenAgreementCheckbox(
                                checked = indentChecked2,
                                onCheckedChange = { indentChecked2 = it },
                                variant = IenAgreementCheckboxVariant.Dot
                            )
                        },
                        middle = {
                            IenAgreementText(text = "개인(신용)정보 수집/이용 동의")
                        }
                    )
                }
            }

            IenDivider()

            Text(
                text = "5. 기존 리스트형 어댑터 동의 (하위 호환용)",
                style = IenTheme.typography.label2,
                color = IenTheme.colors.textSecondary
            )

            IenAgreement(
                items = displayedAgreements,
                onItemCheckedChange = { id, checked ->
                    agreements =
                        agreements.map { if (it.id == id) it.copy(checked = checked) else it }
                }
            )

            IenDivider()
            Text(text = "6. 선택한 크기 변형", style = IenTheme.typography.label1)
            IenAgreement(
                variant = agreementVariant,
                left = { IenAgreementCheckbox(checked = true, onCheckedChange = {}) },
                middle = { IenAgreementText(text = "IenAgreementVariant.${agreementVariant.name}") },
            )

            IenDivider()
            Text(
                text = "7. IenAgreementBadgeVariant & CheckboxVariant",
                style = IenTheme.typography.label1
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IenAgreementBadge(text = "Badge.Clear", variant = IenAgreementBadgeVariant.Clear)
                IenAgreementBadge(text = "Badge.Fill", variant = IenAgreementBadgeVariant.Fill)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IenAgreementCheckbox(
                    checked = true,
                    onCheckedChange = {},
                    variant = IenAgreementCheckboxVariant.Checkbox
                )
                Text("Checkbox", style = IenTheme.typography.caption)
                IenAgreementCheckbox(
                    checked = true,
                    onCheckedChange = {},
                    variant = IenAgreementCheckboxVariant.Dot
                )
                Text("Dot", style = IenTheme.typography.caption)
                IenAgreementCheckbox(
                    checked = true,
                    onCheckedChange = {},
                    variant = IenAgreementCheckboxVariant.Hidden
                )
                Text("Hidden", style = IenTheme.typography.caption)
            }
        }
    }
}

@Preview
@Composable
fun AssetSection(controls: Map<String, String> = emptyMap()) {
    val size = when (controls.enumValue("size", "Medium")) {
        "Small" -> IenAssetFrameSize.Small
        "Large" -> IenAssetFrameSize.Large
        "ExtraLarge" -> IenAssetFrameSize.ExtraLarge
        else -> IenAssetFrameSize.Medium
    }
    val shape = if (controls.enumValue("shape", "Rounded") == "Circle") {
        IenAssetFrameShape.Circle
    } else {
        IenAssetFrameShape.Rounded
    }
    val tone = controls.toneValue("tone", IenSemanticTone.Brand)
    IenTheme {
        ComponentSection(title = "Asset") {
            IenAssetFrame(
                size = size,
                shape = shape,
                tone = tone,
                bordered = controls.booleanValue("bordered", false),
            ) {
                Text("IEN")
            }
            if (!LocalComponentVariantShowcase.current) {
                IenEmpty(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    icon = {
                        this.size = IenAssetFrameSize.Large
                        this.tone = IenSemanticTone.Success
                        this.shape = IenAssetFrameShape.Circle
                        this.contentDescription = "빈 상태 아이콘"
                        content { contentModifier ->
                            IenIcon(
                                imageVector = M3SystemIcons.Filled.FilledSave,
                                contentDescription = null,
                                modifier = contentModifier,
                            )
                        }
                    },
                    title = { Text("표시할 데이터가 없어요") },
                    content = { Text("새로운 데이터가 추가되면 이곳에 표시됩니다.") },
                    buttons = {
                        IenButton(onClick = {}) {
                            Text("새로고침")
                        }
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomBarSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        val shape = if (controls.enumValue("shape", "Capsule") == "Rounded") {
            ContinuousRoundedRectangle(IenTheme.radius.default)
        } else {
            ContinuousCapsule()
        }
        ComponentSection(title = "BottomBar") {
            Text("기본 하단 바", style = IenTheme.typography.label1)
            IenBottomBar(
                actions = {
                    IenIconButton(
                        onClick = {},
                        variant = IenButtonVariant.Ghost,
                        tone = IenSemanticTone.Neutral,
                    ) {
                        IenIcon(
                            imageVector = M3SystemIcons.ArrowBack,
                            contentDescription = "뒤로",
                        )
                    }
                    Text(
                        text = "샘플 하단 바",
                        modifier = Modifier.weight(1f),
                        style = IenTheme.typography.label1,
                    )
                    IenIconButton(
                        onClick = {},
                        variant = IenButtonVariant.Ghost,
                        tone = IenSemanticTone.Neutral,
                    ) {
                        IenIcon(
                            imageVector = M3SystemIcons.MoreVert,
                            contentDescription = "더 보기",
                        )
                    }
                },
                floatingActionButton = if (controls.booleanValue("showFab", true)) {
                    {
                        IenFab(onClick = {}) {
                            IenIcon(
                                imageVector = M3SystemIcons.Filled.Check,
                                contentDescription = "확인",
                            )
                        }
                    }
                } else null,
                containerColor = playgroundSurfaceColor(controls.enumValue("container", "surface")),
                shape = shape,
                elevation = controls.intValue("elevation", 8).coerceIn(0, 24).dp,
                contentPadding = PaddingValues(controls.intValue("contentPadding", 8).coerceIn(0, 24).dp),
            )
        }
    }
}

@Preview
@Composable
fun ChatBottomBarSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        ComponentSection(title = "ChatBottomBar") {
            ChatBottomBarExample(
                enabled = !controls.disabledValue(),
                placeholder = controls.textValue("placeholder", "메시지를 입력하세요"),
                maxLines = controls.intValue("maxLines", 4).coerceIn(1, 8),
                showLeading = controls.booleanValue("showLeading", true),
                showTrailing = controls.booleanValue("showTrailing", true),
            )
        }
    }
}

@Composable
private fun ChatBottomBarExample(
    enabled: Boolean = true,
    placeholder: String = "메시지를 입력하세요",
    maxLines: Int = 4,
    showLeading: Boolean = true,
    showTrailing: Boolean = true,
) {
    var chatMessage by remember { mutableStateOf("") }
    var sentMessage by remember { mutableStateOf<String?>(null) }

    Text("채팅방 하단 바", style = IenTheme.typography.label1)
    Text(
        "텍스트 대신 메시지 입력창을 배치한 채팅방 하단 바입니다.",
        style = IenTheme.typography.caption,
        color = IenTheme.colors.textSecondary,
    )
    sentMessage?.let { message ->
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            IenBubble(background = IenBubbleBackground.Brand) {
                Text(message)
            }
        }
    }
    IenChatBottomBar(
        value = chatMessage,
        onValueChange = { chatMessage = it },
        onSend = {
            sentMessage = chatMessage.trim()
            chatMessage = ""
        },
        placeholder = placeholder,
        maxLines = maxLines,
        leadingContent = if (showLeading) ({
            IenIconButton(
                onClick = {},
                variant = IenButtonVariant.Ghost,
                tone = IenSemanticTone.Neutral,
            ) {
                IenIcon(
                    imageVector = RemixIcons.Fill.Add,
                    contentDescription = "첨부",
                )
            }
        }) else null,
        trailingContent = if (showTrailing) ({
            IenIconButton(
                onClick = {},
                variant = IenButtonVariant.Ghost,
                tone = IenSemanticTone.Neutral,
            ) {
                IenIcon(
                    imageVector = M3SystemIcons.MoreVert,
                    contentDescription = "더 보기",
                )
            }
        }) else null,
        inputState = IenTextFieldState(enabled = enabled),
        sendState = IenButtonState(enabled = enabled && chatMessage.isNotBlank()),
    )
}

@Preview
@Composable
fun NavigationBarSection(controls: Map<String, String> = emptyMap()) {
    val itemCount = controls.intValue("itemCount", 3).coerceIn(1, 5)
    var selectedTabIndex by remember(controls["selectedIndex"], itemCount) {
        mutableIntStateOf(controls.intValue("selectedIndex", 0).coerceIn(0, itemCount - 1))
    }
    val direction = if (controls.booleanValue("vertical", false)) {
        IenNavigationBarItemDirection.Vertical
    } else {
        IenNavigationBarItemDirection.Horizontal
    }
    val visible = controls.booleanValue("visible", true)
    val alwaysShowLabel = controls.booleanValue("alwaysShowLabel", false)
    val badge = controls.intValue("badge", -1)
    val enabled = !controls.disabledValue()
    val items = listOf(
        "홈" to M3SystemIcons.Save,
        "기록" to M3SystemIcons.Schedule,
        "설정" to M3SystemIcons.Delete,
        "더보기" to M3SystemIcons.MoreVert,
        "보관함" to M3SystemIcons.Save,
    )

    IenTheme {
        ComponentSection(title = "NavigationBar") {
            Text(
                "항목을 선택해 하단 내비게이션 상태를 바꿔 보세요.",
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textSecondary,
            )
            if (controls.enumValue("type", "nav") == "nav") {
                IenNavigationBar(
                    selectedIndex = selectedTabIndex.coerceIn(0, itemCount - 1),
                    itemCount = itemCount,
                    windowInsets = WindowInsets(0.dp),
                    visible = visible,
                ) {
                    items.take(itemCount).forEachIndexed { index, (label, icon) ->
                        IenNavigationBarItem(
                            index = index,
                            onClick = { selectedTabIndex = index },
                            icon = { Icon(icon, contentDescription = null) },
                            label = { Text(label) },
                            direction = direction,
                            alwaysShowLabel = alwaysShowLabel,
                            enabled = enabled,
                            badge = if (index == 1) badge else 0,
                        )
                    }
                }
            } else {
                val floatingTabItems = listOf(
                    IenTabItem("홈", key = "home", icon = M3SystemIcons.Rounded.RoundedKeyboard, selectedIcon = M3SystemIcons.Filled.FilledKeyboard, badge = -1, enabled = enabled),
                    IenTabItem("혜택", key = "benefit", icon = M3SystemIcons.Rounded.RoundedCheck, selectedIcon = M3SystemIcons.Filled.Check, badge = 1, enabled = enabled),
                    IenTabItem("아이엔페이", key = "pay", icon = M3SystemIcons.Rounded.RoundedSave, selectedIcon = M3SystemIcons.Filled.FilledSave, badge = 99, enabled = enabled),
                    IenTabItem("증권", key = "stock", icon = M3SystemIcons.Rounded.RoundedCloudOff, selectedIcon = M3SystemIcons.Filled.FilledCloudOff, badge = 120, enabled = enabled),
                    IenTabItem("전체", key = "all", icon = M3SystemIcons.Rounded.RoundedMoreVert, selectedIcon = M3SystemIcons.Filled.FilledMoreVert, enabled = enabled),
                )
                IenNavigationBar2(
                    selectedIndex = selectedTabIndex.coerceIn(0, itemCount - 1),
                    itemCount = itemCount,
                    windowInsets = WindowInsets(0.dp),
                    visible = visible,
                ) {
                    floatingTabItems.take(itemCount).forEachIndexed { index, item ->
                        IenNavigationBarItem(
                            index = index,
                            onClick = { selectedTabIndex = index },
                            icon = { item.icon?.let { Icon(it, contentDescription = null) } },
                            selectedIcon = item.selectedIcon?.let { selectedIcon ->
                                { Icon(selectedIcon, contentDescription = null) }
                            },
                            label = { Text(item.text) },
                            direction = direction,
                            alwaysShowLabel = alwaysShowLabel,
                            enabled = item.enabled,
                            badge = if (index == 0) badge else item.badge,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomCTASection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        var showAnimatedCTA by remember { mutableStateOf(true) }
        var isLoadingCTA by remember(controls["loading"]) { mutableStateOf(controls.booleanValue("loading", false)) }
        val visible = controls.booleanValue("visible", true)
        val enabled = !controls.disabledValue()
        val background = if (controls.enumValue("background", "Default") == "None") {
            IenBottomCTABackground.None
        } else {
            IenBottomCTABackground.Default
        }
        val safeArea = controls.booleanValue("safeArea", true)
        val paddingBottom = controls.booleanValue("paddingBottom", true)
        val hideOnScroll = controls.booleanValue("hideOnScroll", false)
        val hideOnScrollDistanceThreshold = controls.intValue("hideOnScrollDistanceThreshold", 1)
            .coerceIn(0, 16)
            .toFloat()
        val fixedAboveKeyboard = controls.booleanValue("fixedAboveKeyboard", false)
        val animation = when (controls.enumValue("animation", "Scale")) {
            "Fade" -> IenBottomCTAAnimation.Fade
            "Slide" -> IenBottomCTAAnimation.Slide
            "Scale" -> IenBottomCTAAnimation.Scale
            else -> null
        }
        val showAfterDelay = animation?.let {
            IenBottomCTAShowAfterDelay(
                animation = it,
                delayMillis = controls.intValue("delayMillis", 300).coerceIn(0, 1000),
            )
        }
        val takeSpace = controls.booleanValue("takeSpace", false)
        val variant = when (controls.enumValue("variant", "Fill")) {
            "Weak" -> IenButtonVariant.Weak
            "Line" -> IenButtonVariant.Line
            "Ghost" -> IenButtonVariant.Ghost
            else -> IenButtonVariant.Fill
        }
        val tone = controls.toneValue("tone", IenSemanticTone.Brand)
        ComponentSection(title = "BottomCTA") {
            IenButton(
                onClick = { isLoadingCTA = !isLoadingCTA },
                size = IenButtonSize.Small,
                variant = IenButtonVariant.Weak,
            ) {
                Text(if (isLoadingCTA) "CTA 로딩 상태 해제" else "CTA 로딩 상태 활성화")
            }
            if (visible) IenBottomCTA(
                text = controls.textValue("text", "아이콘 포함 CTA"),
                onClick = {},
                icon = {
                    IenIcon(
                        imageVector = M3SystemIcons.Filled.Check,
                        contentDescription = null,
                        size = IenTheme.icon.md,
                    )
                },
                state = IenButtonState(enabled = enabled, loading = isLoadingCTA),
                variant = variant,
                tone = tone,
                background = background,
                hasSafeAreaPadding = safeArea,
                hasPaddingBottom = paddingBottom,
            )
            if (visible) IenBottomCTA(
                text = "단일 CTA",
                onClick = {},
                state = IenButtonState(enabled = enabled, loading = isLoadingCTA),
                variant = variant,
                tone = tone,
                background = background,
                hasSafeAreaPadding = safeArea,
                hasPaddingBottom = paddingBottom,
                topAccessory = {
                    Text(
                        text = "상단 액세서리: 결제 전 안내 문구",
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textSecondary,
                    )
                },
                bottomAccessory = {
                    Text(
                        text = "하단 액세서리: 약관 및 수수료 안내",
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textTertiary,
                    )
                },
            )
            if (visible) IenBottomCTA(
                text = "배경 없는 CTA",
                onClick = {},
                background = IenBottomCTABackground.None,
                hasSafeAreaPadding = false,
                hasPaddingBottom = false,
                variant = IenButtonVariant.Weak,
            )
            IenButton(
                onClick = { showAnimatedCTA = !showAnimatedCTA },
                size = IenButtonSize.Small,
                variant = IenButtonVariant.Weak,
            ) {
                Text(if (showAnimatedCTA) "애니메이션 CTA 숨기기" else "애니메이션 CTA 보이기")
            }
            if (visible) IenBottomCTA(
                text = "지연 등장 CTA",
                onClick = {},
                show = showAnimatedCTA,
                takeSpace = takeSpace,
                showAfterDelay = showAfterDelay,
                hideOnScroll = hideOnScroll,
                hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
                scrollDelta = if (showAnimatedCTA) 0f else 4f,
            )
            if (visible) IenDoubleBottomCTA(
                primaryText = controls.textValue("primaryText", "확인"),
                onPrimaryClick = {},
                secondaryText = controls.textValue("secondaryText", "취소"),
                onSecondaryClick = {},
                primaryState = IenButtonState(enabled = enabled),
                secondaryState = IenButtonState(enabled = enabled),
                background = background,
                hasSafeAreaPadding = safeArea,
                hasPaddingBottom = paddingBottom,
                primaryButtonWeight = controls.intValue("primaryButtonWeight", 2).coerceIn(1, 4).toFloat(),
                secondaryButtonWeight = controls.intValue("secondaryButtonWeight", 1).coerceIn(1, 4).toFloat(),
                topAccessory = {
                    Text(
                        text = "버튼 너비 비율: 부 버튼 1, 주 버튼 2",
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textSecondary,
                    )
                },
            )
            if (visible) IenDoubleBottomCTA(
                background = IenBottomCTABackground.None,
                hasPaddingBottom = false,
                leftButton = {
                    IenBottomCTAButton(
                        text = "삭제",
                        onClick = {},
                        variant = IenButtonVariant.Weak,
                        tone = IenSemanticTone.Danger,
                    )
                },
                rightButton = {
                    IenBottomCTAButton(
                        text = "저장",
                        onClick = {},
                    )
                },
            )
            if (controls.booleanValue("fixed", true)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(176.dp)
                        .background(IenTheme.colors.surfaceWeak),
                ) {
                    Text(
                        text = "FixedBottomCTA.Single",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(IenTheme.spacing.md),
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textSecondary,
                    )
                    IenFixedBottomCTA(
                        text = "고정 CTA",
                        onClick = {},
                        variant = variant,
                        tone = tone,
                        background = background,
                        hasSafeAreaPadding = safeArea,
                        hasPaddingBottom = paddingBottom,
                        topAccessory = {
                            Text(
                                text = "fixedAboveKeyboard=$fixedAboveKeyboard",
                                style = IenTheme.typography.caption,
                                color = IenTheme.colors.textSecondary,
                            )
                        },
                        fixedAboveKeyboard = fixedAboveKeyboard,
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(176.dp)
                        .background(IenTheme.colors.surfaceWeak),
                ) {
                    Text(
                        text = "FixedBottomCTA.Double · 비율 1:2",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(IenTheme.spacing.md),
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textSecondary,
                    )
                    IenFixedDoubleBottomCTA(
                        hideOnScroll = hideOnScroll,
                        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
                        scrollDelta = 0f,
                        leftButtonWeight = controls.intValue("secondaryButtonWeight", 1).coerceIn(1, 4).toFloat(),
                        rightButtonWeight = controls.intValue("primaryButtonWeight", 2).coerceIn(1, 4).toFloat(),
                        leftButton = {
                            IenBottomCTAButton(
                                text = controls.textValue("secondaryText", "취소"),
                                onClick = {},
                                variant = IenButtonVariant.Weak,
                                tone = IenSemanticTone.Neutral,
                            )
                        },
                        rightButton = {
                            IenBottomCTAButton(
                                text = controls.textValue("primaryText", "확인"),
                                onClick = {},
                            )
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        var showAlert by remember { mutableStateOf(false) }
        var showAlertWiggle by remember { mutableStateOf(false) }
        var showAlertLong by remember { mutableStateOf(false) }
        var showConfirm by remember { mutableStateOf(false) }
        var showConfirmLong by remember { mutableStateOf(false) }
        var showConfirmNoDescription by remember { mutableStateOf(false) }
        var showGenericDialog by remember { mutableStateOf(false) }
        var showM3OneButton by remember { mutableStateOf(false) }
        var showM3OneButtonDestructive by remember { mutableStateOf(false) }
        var showM3TwoButtonHorizontal by remember { mutableStateOf(false) }
        var showM3TwoButtonVerticalDestructive by remember { mutableStateOf(false) }
        var showM3ThreeButtonHorizontal by remember { mutableStateOf(false) }
        var showM3ThreeButtonVerticalDestructive by remember { mutableStateOf(false) }
        var dialogEventText by remember { mutableStateOf("대기 중") }

        ComponentSection(title = "Dialog") {
            IenButton(
                onClick = { showAlert = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Weak,
            ) { Text("AlertDialog 기본") }
            IenButton(
                onClick = { showAlertWiggle = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Weak,
            ) { Text("AlertDialog 딤 클릭 방지") }
            IenButton(
                onClick = { showAlertLong = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
            ) { Text("AlertDialog 긴 콘텐츠") }
            IenButton(
                onClick = { showConfirm = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
                tone = IenSemanticTone.Danger,
            ) { Text("ConfirmDialog 기본") }
            IenButton(
                onClick = { showConfirmLong = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
            ) { Text("ConfirmDialog 긴 버튼") }
            IenButton(
                onClick = { showConfirmNoDescription = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Ghost,
            ) { Text("ConfirmDialog 설명 없음") }
            IenButton(
                onClick = { showGenericDialog = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Ghost,
            ) { Text("기본 Dialog 열기") }
            Text(
                text = "IenAlertDialog 호환 API",
                style = IenTheme.typography.label1,
                color = IenTheme.colors.textSecondary,
            )
            IenButton(
                onClick = { showM3OneButton = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Weak,
            ) { Text("M3 1버튼 기본") }
            IenButton(
                onClick = { showM3OneButtonDestructive = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Weak,
                tone = IenSemanticTone.Danger,
            ) { Text("M3 1버튼 destructive") }
            IenButton(
                onClick = { showM3TwoButtonHorizontal = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
            ) { Text("M3 2버튼 Horizontal") }
            IenButton(
                onClick = { showM3TwoButtonVerticalDestructive = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
                tone = IenSemanticTone.Danger,
            ) { Text("M3 2버튼 Vertical destructive") }
            IenButton(
                onClick = { showM3ThreeButtonHorizontal = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Ghost,
            ) { Text("M3 3버튼 Horizontal") }
            IenButton(
                onClick = { showM3ThreeButtonVerticalDestructive = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Ghost,
                tone = IenSemanticTone.Danger,
            ) { Text("M3 3버튼 Vertical destructive") }
            Text(
                text = "이벤트: $dialogEventText",
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textTertiary,
            )
        }

        IenAlertDialog(
            visible = showAlert,
            title = controls.textValue("title", "엔님의 의견이 잘 전달되었어요"),
            message = controls.textValue("message", "소중한 의견을 바탕으로 더 간편한 서비스를 만들게요."),
            confirmText = controls.textValue("confirmText", "확인"),
            onDismissRequest = { showAlert = false },
            onConfirmClick = { showAlert = false },
            tone = controls.toneValue("tone", IenSemanticTone.Brand),
            closeOnDimmerClick = controls.booleanValue("closeOnDimmerClick", true),
            closeOnBackEvent = controls.booleanValue("closeOnBackEvent", true),
            onEntered = { dialogEventText = "AlertDialog 열림" },
            onExited = { dialogEventText = "AlertDialog 닫힘" },
        )

        IenAlertDialog(
            visible = showAlertWiggle,
            onClose = { showAlertWiggle = false },
            closeOnDimmerClick = false,
            closeOnBackEvent = true,
            title = {
                IenAlertDialogTitle("외부 영역으로 닫히지 않아요")
            },
            description = {
                IenAlertDialogDescription("확인 버튼이나 뒤로가기 이벤트로만 닫히는 알림입니다.")
            },
            alertButton = {
                IenAlertDialogAlertButton(
                    text = "확인",
                    onClick = { showAlertWiggle = false },
                )
            },
        )

        IenAlertDialog(
            visible = showAlertLong,
            title = "30글자 이상의 아주 긴 제목도 자연스럽게 줄바꿈되어 표시됩니다",
            message = List(12) { "소중한 의견을 바탕으로 더 간편한 서비스를 만들게요." }.joinToString("\n"),
            confirmText = "30글자 이상의 아주 긴 확인 버튼 레이블입니다",
            onDismissRequest = { showAlertLong = false },
            onConfirmClick = { showAlertLong = false },
        )

        IenConfirmDialog(
            visible = showConfirm,
            title = "삭제할까요?",
            message = "ConfirmDialog는 사용자의 명시적인 결정을 받아야 하는 상황에 사용합니다.",
            confirmText = controls.textValue("confirmText", "확인"),
            dismissText = controls.textValue("dismissText", "취소"),
            onDismissRequest = { showConfirm = false },
            onConfirmClick = { showConfirm = false },
            destructive = controls.booleanValue("destructive", false),
            closeOnDimmerClick = controls.booleanValue("closeOnDimmerClick", true),
            closeOnBackEvent = controls.booleanValue("closeOnBackEvent", true),
            buttonLayout = IenDialogButtonLayout.entries.firstOrNull {
                it.name == controls.enumValue("buttonLayout", "Horizontal")
            } ?: IenDialogButtonLayout.Horizontal,
        )

        IenConfirmDialog(
            visible = showConfirmLong,
            title = "정말 계속할까요?",
            message = "버튼 레이블이 길어지는 경우 세로 배치를 사용하면 버튼 영역이 안정적으로 유지됩니다.",
            onDismissRequest = { showConfirmLong = false },
            onConfirmClick = { showConfirmLong = false },
            dismissText = "아니오, 취소해주세요",
            confirmText = "예, 알겠습니다",
            buttonLayout = IenDialogButtonLayout.Vertical,
        )

        IenConfirmDialog(
            visible = showConfirmNoDescription,
            onClose = { showConfirmNoDescription = false },
            closeOnDimmerClick = false,
            title = {
                IenConfirmDialogTitle("설명 없이 진행할까요?")
            },
            cancelButton = {
                IenConfirmDialogCancelButton(
                    text = "아니오",
                    onClick = { showConfirmNoDescription = false },
                )
            },
            confirmButton = {
                IenConfirmDialogConfirmButton(
                    text = "예",
                    onClick = { showConfirmNoDescription = false },
                )
            },
        )

        IenDialog(
            visible = showGenericDialog,
            onDismissRequest = { showGenericDialog = false },
            title = "기본 Dialog",
            message = "IenDialog는 가장 단순한 확인/취소 구조를 제공합니다.",
            confirm = IenDialogAction(
                text = controls.textValue("confirmText", "확인"),
                onClick = { showGenericDialog = false },
            ),
            dismiss = IenDialogAction(
                text = controls.textValue("dismissText", "취소"),
                onClick = { showGenericDialog = false },
            ),
        )

        IenAlertDialog(
            visible = showM3OneButton,
            title = "1버튼 알림",
            message = "기존 IenAlertDialog 단일 버튼 API가 IEN AlertDialog 디자인으로 표시됩니다.",
            textDismiss = controls.textValue("confirmText", "확인"),
            onDismiss = { showM3OneButton = false },
            tone = IenSemanticTone.Brand,
        )

        IenAlertDialog(
            visible = showM3OneButtonDestructive,
            title = "위험 알림",
            message = "isDestructive를 켜면 아이콘과 액션 톤이 Danger로 표시됩니다.",
            textDismiss = "삭제 확인",
            onDismiss = { showM3OneButtonDestructive = false },
            isDestructive = true,
        )

        IenAlertDialog(
            visible = showM3TwoButtonHorizontal,
            title = "2버튼 가로 배치",
            message = "buttonLayout 기본값은 Horizontal입니다.",
            textDismiss = controls.textValue("dismissText", "취소"),
            onDismiss = { showM3TwoButtonHorizontal = false },
            textConfirm = controls.textValue("confirmText", "확인"),
            onConfirm = { showM3TwoButtonHorizontal = false },
            tone = controls.toneValue("tone", IenSemanticTone.Brand),
            isDestructive = controls.booleanValue("destructive", false),
            buttonLayout = IenDialogButtonLayout.entries.firstOrNull {
                it.name == controls.enumValue("buttonLayout", "Horizontal")
            } ?: IenDialogButtonLayout.Horizontal,
        )

        IenAlertDialog(
            visible = showM3TwoButtonVerticalDestructive,
            title = "2버튼 세로 배치",
            message = "긴 버튼이나 위험 액션은 Vertical과 destructive 조합으로 확인할 수 있습니다.",
            textDismiss = "아니오, 취소할게요",
            onDismiss = { showM3TwoButtonVerticalDestructive = false },
            textConfirm = "예, 삭제할게요",
            onConfirm = { showM3TwoButtonVerticalDestructive = false },
            isDestructive = true,
            buttonLayout = IenDialogButtonLayout.Vertical,
        )

        IenAlertDialog(
            visible = showM3ThreeButtonHorizontal,
            title = "3버튼 가로 배치",
            message = "중립 버튼과 부정/긍정 버튼을 함께 사용하는 형태입니다.",
            textNeutral = "나중에",
            onNeutral = { showM3ThreeButtonHorizontal = false },
            textNegative = "취소",
            onNegative = { showM3ThreeButtonHorizontal = false },
            textPositive = "저장",
            onPositive = { showM3ThreeButtonHorizontal = false },
            buttonLayout = IenDialogButtonLayout.Horizontal,
        )

        IenAlertDialog(
            visible = showM3ThreeButtonVerticalDestructive,
            title = "3버튼 세로 배치",
            message = "중립 버튼은 상단 텍스트 버튼으로 두고, 긍정/부정 버튼은 세로 배치됩니다.",
            textNeutral = "자세히 보기",
            onNeutral = { showM3ThreeButtonVerticalDestructive = false },
            textNegative = "취소",
            onNegative = { showM3ThreeButtonVerticalDestructive = false },
            textPositive = "초기화",
            onPositive = { showM3ThreeButtonVerticalDestructive = false },
            isDestructive = true,
            buttonLayout = IenDialogButtonLayout.Vertical,
        )
    }
}

@Preview
@Composable
fun KeypadSection(controls: Map<String, String> = emptyMap()) {
    val enabled = !controls.disabledValue()
    IenTheme {
        var alphabetValue by remember { mutableStateOf("") }
        var customAlphabetValue by remember { mutableStateOf("") }
        var numberValue by remember { mutableStateOf("") }
        var customNumberValue by remember { mutableStateOf("") }
        var secureNumberValue by remember { mutableStateOf("") }
        var secureNoiseValue by remember { mutableStateOf("") }
        var secureValue by remember { mutableStateOf("") }
        var fullSecureValue by remember { mutableStateOf("") }
        var secureLanguage by remember { mutableStateOf(IenSecureKeyboardLanguage.English) }
        val fullSecureKeypadState = rememberIenFullSecureKeypadState()

        ComponentSection(title = "Keypad") {
            Text("Alphabet Keypad: $alphabetValue", style = IenTheme.typography.body2)
            IenAlphabetKeypad(
                onKeyClick = { alphabetValue += it },
                onBackspaceClick = { alphabetValue = alphabetValue.dropLast(1) },
                columns = controls.intValue("columns", 3).coerceIn(2, 6),
                keyHeight = controls.intValue("keyHeight", 56).coerceIn(40, 80).dp,
                enabled = enabled,
            )
            IenDivider()
            Text("커스텀 배열: $customAlphabetValue", style = IenTheme.typography.body2)
            IenAlphabetKeypad(
                alphabets = listOf(
                    "z", "y", "x",
                    "w", "v", "u",
                    "t", "s", "r",
                    "q", "p", "o",
                    "n", "m", "l",
                    "k", "j", "i",
                    "h", "g", "f",
                    "e", "d", "c",
                    "b", "a",
                ),
                onKeyClick = { customAlphabetValue += it },
                onBackspaceClick = { customAlphabetValue = customAlphabetValue.dropLast(1) },
                keyHeight = controls.intValue("keyHeight", 56).coerceIn(40, 80).dp,
                enabled = enabled,
            )
            IenDivider()
            Text("보안 알파벳 키보드: $alphabetValue", style = IenTheme.typography.body2)
            IenAlphabetKeyboard(
                onAction = { action ->
                    alphabetValue = applyKeyboardAction(alphabetValue, action)
                },
                randomized = controls.booleanValue("randomized", false),
                enabled = enabled,
            )
            IenDivider()
            Text("Number Keypad: $numberValue", style = IenTheme.typography.body2)
            IenNumberKeypad(
                onKeyClick = { numberValue += it },
                onBackspaceClick = { numberValue = numberValue.dropLast(1) },
                keyHeight = controls.intValue("keyHeight", 56).coerceIn(40, 80).dp,
                enabled = enabled,
            )
            IenDivider()
            Text("커스텀 숫자 배열: $customNumberValue", style = IenTheme.typography.body2)
            IenNumberKeypad(
                numbers = listOf(1, 3, 5, 7, 9, 2, 4, 6, 8, 0),
                onKeyClick = { customNumberValue += it },
                onBackspaceClick = { customNumberValue = customNumberValue.dropLast(1) },
                enabled = enabled,
            )
            IenDivider()
            Text(
                text = "보안 숫자 입력: $secureNumberValue / 더미: $secureNoiseValue",
                style = IenTheme.typography.body2,
            )
            IenNumberKeypad(
                secure = true,
                onKeyClick = { secureNumberValue += it },
                onBackspaceClick = {
                    secureNumberValue = secureNumberValue.dropLast(1)
                    secureNoiseValue = ""
                },
                onSecureNoiseKeyClick = {
                    secureNoiseValue = (secureNoiseValue + it).takeLast(8)
                },
                keyHeight = controls.intValue("keyHeight", 56).coerceIn(40, 80).dp,
                enabled = enabled,
            )
            IenDivider()
            Text("Full Secure Keypad: $fullSecureValue", style = IenTheme.typography.body2)
            IenFullSecureKeypad(
                state = fullSecureKeypadState,
                onKeyClick = { fullSecureValue += it },
                onBackspaceClick = { fullSecureValue = fullSecureValue.dropLast(1) },
                onSpaceClick = { fullSecureValue += " " },
                onSubmit = { fullSecureKeypadState.reorderEmptyCells() },
                submitButtonText = "공백 옮기기",
                submitDisabled = fullSecureValue.isEmpty(),
                enabled = enabled,
            )
            IenDivider()
            IenFullSecureKeyboard(
                state = IenSecureKeyboardState(
                    value = secureValue,
                    language = secureLanguage,
                ),
                onAction = { action ->
                    secureValue = applyKeyboardAction(secureValue, action)
                },
                onLanguageChange = { secureLanguage = it },
                enabled = enabled,
            )
        }
    }
}

@Preview
@Composable
fun ListRowSection(controls: Map<String, String> = emptyMap()) {
    val selectedTextType = IenListRowTextsType.entries.firstOrNull {
        it.name == controls.enumValue("textType", "OneRowTypeA")
    } ?: IenListRowTextsType.OneRowTypeA
    val assetShape = IenListRowAssetShape.entries.firstOrNull {
        it.name == controls.enumValue("assetShape", "Squircle")
    } ?: IenListRowAssetShape.Squircle
    val assetSize = IenListRowAssetSize.entries.firstOrNull {
        it.name == controls.enumValue("assetSize", "Medium")
    } ?: IenListRowAssetSize.Medium
    val border = IenListRowBorder.entries.firstOrNull {
        it.name == controls.enumValue("border", "Indented")
    } ?: IenListRowBorder.Indented
    val verticalPadding = IenListRowPadding.entries.firstOrNull {
        it.name == controls.enumValue("verticalPadding", "Medium")
    } ?: IenListRowPadding.Medium
    val horizontalPadding = IenListRowPadding.entries.firstOrNull {
        it.name == controls.enumValue("horizontalPadding", "Medium")
    } ?: IenListRowPadding.Medium
    val leftAlignment = IenListRowAlignment.entries.firstOrNull {
        it.name == controls.enumValue("leftAlignment", "Center")
    } ?: IenListRowAlignment.Center
    val rightAlignment = IenListRowAlignment.entries.firstOrNull {
        it.name == controls.enumValue("rightAlignment", "Center")
    } ?: IenListRowAlignment.Center
    val disabledStyle = IenListRowDisabledStyle.entries.firstOrNull {
        it.name == controls.enumValue("disabledStyle", "Type1")
    } ?: IenListRowDisabledStyle.Type1
    val enabled = !controls.disabledValue()
    if (LocalComponentVariantShowcase.current) {
        IenTheme {
            val touchEffectColor = when (controls.enumValue("touchEffectColor", "surfaceVariant")) {
                "brandWeak" -> IenTheme.colors.brandWeak
                "successWeak" -> IenTheme.colors.successWeak
                "dangerWeak" -> IenTheme.colors.dangerWeak
                else -> IenTheme.colors.surfaceVariant
            }
            var clickCount by remember { mutableIntStateOf(0) }
            ComponentSection(title = "ListRow") {
                IenListRow(
                    left = if (controls.booleanValue("showLeft", true)) ({
                        IenListRowAssetText(
                            text = "IEN",
                            shape = assetShape,
                            size = assetSize,
                        )
                    }) else null,
                    contents = {
                        IenListRowTexts(
                            type = selectedTextType,
                            top = {
                                Text(
                                    if (clickCount == 0) "${selectedTextType.name} (Top)"
                                    else "${selectedTextType.name} · 클릭 ${clickCount}회",
                                )
                            },
                            middle = { Text("${selectedTextType.name} (Middle)") },
                            bottom = { Text("${selectedTextType.name} (Bottom)") },
                        )
                    },
                    right = if (controls.booleanValue("showRight", false)) ({
                        Text("상세", style = IenTheme.typography.label1)
                    }) else null,
                    border = border,
                    disabled = !enabled,
                    disabledStyle = disabledStyle,
                    verticalPadding = verticalPadding,
                    horizontalPadding = horizontalPadding,
                    leftAlignment = leftAlignment,
                    rightAlignment = rightAlignment,
                    withArrow = controls.booleanValue("withArrow", false),
                    withTouchEffect = controls.booleanValue("withTouchEffect", false),
                    touchEffectColor = touchEffectColor,
                    onClick = if (controls.booleanValue("clickable", false)) ({
                        clickCount += 1
                    }) else null,
                )
            }
        }
        return
    }
    IenTheme {
        ComponentSection(title = "ListRow") {
            IenListRow(
                title = "속성 적용 미리보기",
                subtitle = "비활성화 = ${!enabled}",
                enabled = enabled,
                withArrow = true,
                onClick = {},
            )
            IenListRow(
                title = "아이엔페이 결제",
                subtitle = "오늘 12:30",
                trailing = { Text("28,000원", style = IenTheme.typography.label1) },
            )
            IenListRow(
                title = "선택된 계좌",
                subtitle = "입출금 통장",
                selected = true,
                trailing = { IenBadge("기본", size = IenBadgeSize.Small) },
            )
            IenListRow(
                left = {
                    IenListRowAssetText(
                        text = "오늘",
                        shape = IenListRowAssetShape.Squircle,
                    )
                },
                contents = {
                    IenListRowTexts(
                        type = IenListRowTextsType.TwoRowTypeA,
                        top = { Text("ListRow.Texts") },
                        bottom = { Text("터치 색상: brandWeak") },
                    )
                },
                right = {
                    IenButton(
                        onClick = {},
                        size = IenButtonSize.Small,
                        variant = IenButtonVariant.Weak,
                    ) {
                        Text("Button")
                    }
                },
                withArrow = true,
                withTouchEffect = true,
                touchEffectColor = IenTheme.colors.brandWeak,
                onClick = {},
            )
            IenListRow(
                contents = {
                    IenListRowTexts(
                        type = IenListRowTextsType.ThreeRowTypeC,
                        top = { Text("긴 정보가 들어가는 행") },
                        middle = { Text("중간 설명 텍스트") },
                        bottom = { Text("아래 보조 텍스트") },
                    )
                },
                right = {
                    IenListRowTexts(
                        type = IenListRowTextsType.RightTwoRowTypeA,
                        top = { Text("28,000원") },
                        bottom = { Text("오늘") },
                    )
                },
                leftAlignment = IenListRowAlignment.Top,
                rightAlignment = IenListRowAlignment.Top,
                verticalPadding = IenListRowPadding.Large,
            )
            IenListRow(
                left = {
                    IenListRowAssetText(
                        text = "NEW",
                        shape = IenListRowAssetShape.Squircle,
                    )
                },
                contents = {
                    IenListRowTexts(
                        type = IenListRowTextsType.TwoRowTypeG,
                        top = { Text("강조형 두 줄 제목") },
                        bottom = { Text("TwoRowTypeG · title2 Bold") },
                    )
                },
                withArrow = true,
            )
            IenListRow(
                contents = {
                    IenListRowTexts(
                        type = IenListRowTextsType.OneRowTypeA,
                        top = { Text("비활성 Type2") },
                    )
                },
                right = {
                    IenBadge("불가", size = IenBadgeSize.Small)
                },
                disabled = true,
                disabledStyle = IenListRowDisabledStyle.Type2,
                border = IenListRowBorder.Indented,
            )
            IenListRow(
                contents = {
                    IenListRowTexts(
                        type = IenListRowTextsType.OneRowTypeA,
                        top = { Text("작은 좌우 패딩과 border 없음") },
                    )
                },
                horizontalPadding = IenListRowPadding.Small,
                border = IenListRowBorder.None,
                withArrow = true,
            )
            IenListRowLoader(
                type = IenListRowLoaderType.Circle,
                verticalPadding = IenListRowPadding.ExtraSmall
            )
            IenListRowLoader(type = IenListRowLoaderType.Bar)

            IenDivider()
            Text("IenListRowTextsType", style = IenTheme.typography.label1)
            IenListRow(
                contents = {
                    IenListRowTexts(
                        type = selectedTextType,
                        top = { Text("${selectedTextType.name} (Top)") },
                        middle = { Text("${selectedTextType.name} (Middle)") },
                        bottom = { Text("${selectedTextType.name} (Bottom)") },
                    )
                },
                border = IenListRowBorder.Indented,
            )

            IenDivider()
            Text("IenListRowDisabledStyle", style = IenTheme.typography.label1)
            IenListRow(
                contents = {
                    IenListRowTexts(
                        type = IenListRowTextsType.OneRowTypeA,
                        top = "IenListRowDisabledStyle.Type1 (기본 투명 배경)",
                    )
                },
                disabled = true,
                disabledStyle = IenListRowDisabledStyle.Type1,
                border = IenListRowBorder.Indented,
            )
            IenListRow(
                contents = {
                    IenListRowTexts(
                        type = IenListRowTextsType.OneRowTypeA,
                        top = "IenListRowDisabledStyle.Type2 (surfaceWeak 배경)",
                    )
                },
                disabled = true,
                disabledStyle = IenListRowDisabledStyle.Type2,
                border = IenListRowBorder.Indented,
            )

            IenDivider()
            Text("IenListRowAssetShape & Size", style = IenTheme.typography.label1)
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                IenListRowAssetText(
                    text = "Square",
                    shape = IenListRowAssetShape.Square,
                    size = IenListRowAssetSize.XSmall
                )
                IenListRowAssetText(
                    text = "Card",
                    shape = IenListRowAssetShape.Card,
                    size = IenListRowAssetSize.Small
                )
                IenListRowAssetText(
                    text = "Squircle",
                    shape = IenListRowAssetShape.Squircle,
                    size = IenListRowAssetSize.Medium
                )
                IenListRowAssetText(
                    text = "Circle",
                    shape = IenListRowAssetShape.Circle,
                    size = IenListRowAssetSize.Medium
                )
                IenListRowAssetText(
                    text = "Original",
                    shape = IenListRowAssetShape.Original,
                    size = IenListRowAssetSize.Medium
                )
            }

            IenDivider()
            Text("IenListRowBorder", style = IenTheme.typography.label1)
            IenListRow(
                contents = {
                    IenListRowTexts(
                        top = "IenListRowBorder.None",
                        type = IenListRowTextsType.OneRowTypeA
                    )
                },
                border = IenListRowBorder.None,
            )
            IenListRow(
                contents = {
                    IenListRowTexts(
                        top = "IenListRowBorder.Indented",
                        type = IenListRowTextsType.OneRowTypeA
                    )
                },
                border = IenListRowBorder.Indented,
            )
        }
    }
}

@Preview
@Composable
fun TextFieldSection(controls: Map<String, String> = emptyMap()) {
    val enabled = !controls.disabledValue()
    val required = controls.booleanValue("required", true)
    val label = controls.textValue("label", "이름 · Required · LengthLimit.Error")
    val placeholder = controls.textValue("placeholder", "이름을 입력하세요")
    val labelOption = if (controls.enumValue("labelOption", "Appear") == "Sustain") {
        IenTextFieldLabelOption.Sustain
    } else {
        IenTextFieldLabelOption.Appear
    }
    val lengthLimitCount = controls.intValue("lengthLimitCount", 3).coerceIn(1, 64)
    val lengthLimit = when (controls.enumValue("lengthLimit", "Error")) {
        "Block" -> IenTextFieldLengthLimit.Block(lengthLimitCount)
        "None" -> IenTextFieldLengthLimit.None
        else -> IenTextFieldLengthLimit.Error(lengthLimitCount)
    }
    val forceError = controls.booleanValue("hasError", false)
    val variant = IenTextFieldVariant.entries.firstOrNull {
        it.name == controls.enumValue("variant", "Box")
    } ?: IenTextFieldVariant.Box
    if (LocalComponentVariantShowcase.current) {
        IenTheme {
            var value by remember { mutableStateOf("") }
            ComponentSection(title = "TextField") {
                IenTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = label,
                    required = required,
                    labelOption = labelOption,
                    placeholder = placeholder,
                    variant = variant,
                    hasError = forceError,
                    lengthLimit = lengthLimit,
                    state = IenTextFieldState(enabled = enabled),
                )
            }
        }
        return
    }
    IenTheme {
        var text by remember { mutableStateOf("") }
        var number by remember { mutableStateOf("") }
        var lineText by remember { mutableStateOf("서울") }
        var bigText by remember { mutableStateOf("") }
        var amountText by remember { mutableStateOf("1200000") }
        var clearText by remember { mutableStateOf("지울 수 있는 값") }
        var passwordText by remember { mutableStateOf("") }
        var selectedBank by remember { mutableStateOf("은행 선택") }
        val numberFormat = IenTextFieldFormat(
            transform = { value ->
                value
                    .filter { it.isDigit() }
                    .reversed()
                    .chunked(3)
                    .joinToString(",")
                    .reversed()
            },
            reset = { formattedValue -> formattedValue.filter { it.isDigit() } },
        )
        ComponentSection(title = "TextField") {
            IenTextField(
                value = text,
                onValueChange = { text = it },
                label = label,
                required = required,
                labelOption = labelOption,
                placeholder = placeholder,
                variant = variant,
                state = IenTextFieldState(enabled = enabled),
                hasError = forceError || (lengthLimit is IenTextFieldLengthLimit.Error && text.length > lengthLimitCount),
                help = if (forceError || text.length > lengthLimitCount) {
                    "이름은 ${lengthLimitCount}글자 이하로 입력해주세요."
                } else {
                    "값이 들어오거나 포커스되면 라벨이 나타납니다."
                },
                lengthLimit = lengthLimit,
            )
            IenTextField(
                value = number,
                onValueChange = { if (it.checkDecimal()) number = it },
                label = "이름 · Required · LengthLimit.Error",
                required = true,
                placeholder = "이름을 입력하세요",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                lengthLimit = IenTextFieldLengthLimit.Error(3),
            )
            IenTextField(
                value = lineText,
                onValueChange = { lineText = it },
                label = "주소 · LengthLimit.None",
                labelOption = IenTextFieldLabelOption.Sustain,
                placeholder = "주소를 입력하세요",
                variant = IenTextFieldVariant.Line,
                suffix = "시",
                help = "길이 제한과 카운터를 사용하지 않습니다.",
                lengthLimit = IenTextFieldLengthLimit.None,
            )
            IenTextField(
                value = bigText,
                onValueChange = { bigText = it },
                label = "큰 금액",
                placeholder = "0",
                variant = IenTextFieldVariant.Big,
                prefix = "₩",
                suffix = "원",
                format = numberFormat,
                help = "format.transform/reset으로 표시값과 원본값을 분리합니다.",
            )
            IenTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = "Hero 입력",
                labelOption = IenTextFieldLabelOption.Sustain,
                placeholder = "0",
                variant = IenTextFieldVariant.Hero,
                suffix = "원",
                format = numberFormat,
                paddingTop = 24.dp,
                paddingBottom = 24.dp,
            )
            IenClearableTextField(
                value = clearText,
                onValueChange = { clearText = it },
                onClear = {},
                label = "Clearable · LengthLimit.Block",
                labelOption = IenTextFieldLabelOption.Sustain,
                placeholder = "입력 후 지울 수 있어요",
                lengthLimit = IenTextFieldLengthLimit.Block(20),
            )
            IenPasswordTextField(
                value = passwordText,
                onValueChange = { passwordText = it },
                label = "비밀번호 · LengthLimit.Block",
                required = true,
                placeholder = "비밀번호 입력",
                help = "보기/숨김 토글을 제공합니다.",
                lengthLimit = IenTextFieldLengthLimit.Block(16),
            )
            IenTextFieldButton(
                value = selectedBank,
                onClick = { selectedBank = if (selectedBank == "은행 선택") "아이엔뱅크" else "은행 선택" },
                label = "계좌 · LengthLimit.None",
                labelOption = IenTextFieldLabelOption.Sustain,
                help = "읽기 전용 선택 필드입니다.",
                lengthLimit = IenTextFieldLengthLimit.None,
            )
            IenTextField(
                value = "",
                onValueChange = {},
                label = "오류 상태",
                placeholder = "필수 값",
                hasError = true,
                help = "값을 입력해 주세요.",
            )
        }
    }
}

@Preview
@Composable
fun SplitTextFieldSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        var splitText by remember { mutableStateOf("") }
        ComponentSection(title = "SplitTextField") {
            IenSplitTextField(
                value = splitText,
                onValueChange = { splitText = it },
                length = controls.intValue("fieldCount", 4).coerceIn(2, 8),
                placeholderChar = controls.textValue("placeholderChar", "•").firstOrNull() ?: '•',
                mask = controls.booleanValue("mask", false),
                state = IenTextFieldState(enabled = !controls.disabledValue()),
            )
        }
    }
}

@Preview
@Composable
fun TextAreaSection(controls: Map<String, String> = emptyMap()) {
    val maxLines = controls.intValue("maxLines", 4).coerceIn(2, 12)
    val minLines = controls.intValue("minLines", 2).coerceIn(1, maxLines)
    val label = controls.textValue("label", "메모 · LengthLimit.Block")
    val placeholder = controls.textValue("placeholder", "여러 줄 텍스트를 입력하세요")
    val lengthLimitCount = controls.intValue("lengthLimitCount", 120).coerceIn(1, 500)
    val lengthLimit = when (controls.enumValue("lengthLimit", "Block")) {
        "None" -> IenTextFieldLengthLimit.None
        "Error" -> IenTextFieldLengthLimit.Error(lengthLimitCount)
        else -> IenTextFieldLengthLimit.Block(lengthLimitCount)
    }
    IenTheme {
        var textArea by remember { mutableStateOf("") }
        ComponentSection(title = "TextArea") {
            IenTextArea(
                value = textArea,
                onValueChange = { textArea = it },
                modifier = Modifier.height((maxLines * 48).dp),
                label = label,
                required = controls.booleanValue("required", true),
                placeholder = placeholder,
                supportingText = "TextArea는 TextField 토큰과 상태 모델을 공유합니다.",
                state = IenTextFieldState(enabled = !controls.disabledValue()),
                lengthLimit = lengthLimit,
                minLines = minLines,
                maxLines = maxLines,
            )
        }
    }
}

@Preview
@Composable
fun PrimitivesSection(controls: Map<String, String> = emptyMap()) {
    IenTheme {
        val shape = if (controls.enumValue("shape", "Rounded") == "Circle") {
            CircleShape
        } else {
            ContinuousRoundedRectangle(IenTheme.radius.default)
        }
        ComponentSection(title = "Primitives") {
            IenProvideTextStyle(
                style = IenTheme.typography.label1,
                color = IenTheme.colors.brand,
            ) {
                Text("ProvideTextStyle 적용 텍스트", color = IenTheme.colors.brand)
            }
            IenBorderBox(
                shape = shape,
                color = playgroundToneColor(controls.enumValue("borderTone", "Neutral")),
                width = controls.intValue("borderWidth", 1).coerceIn(1, 8).dp,
            ) {
                Text("BorderBox 프리미티브", color = IenTheme.colors.textSecondary)
            }
            IenDivider(thickness = controls.intValue("dividerThickness", 1).coerceIn(1, 8).dp)
            IenSurface(color = playgroundSurfaceColor(controls.enumValue("surfaceColor", "brandWeak")), shape = shape) {
                Text("${controls.enumValue("shape", "Rounded")} 표면", modifier = Modifier.padding(IenTheme.spacing.md), color = IenTheme.colors.brand)
            }
            if (!LocalComponentVariantShowcase.current) {
                IenClickable(onClick = {}, enabled = !controls.disabledValue()) {
                    IenSurface(color = IenTheme.colors.brandWeak) {
                        Text(
                            text = "Clickable container",
                            modifier = Modifier.padding(IenTheme.spacing.md),
                            color = IenTheme.colors.brand,
                        )
                    }
                }
            }
        }
    }
}

private fun applyKeyboardAction(
    value: String,
    action: IenKeyboardAction,
): String = when (action) {
    is IenKeyboardAction.Input -> value + action.text
    IenKeyboardAction.Backspace -> value.dropLast(1)
    IenKeyboardAction.Space -> "$value "
    IenKeyboardAction.Clear -> ""
    IenKeyboardAction.Done -> value
}

@Composable
private fun ComponentSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!LocalComponentSectionChrome.current) {
        Column(
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            content = content,
        )
        return
    }

    IenSurface(
        modifier = Modifier.fillMaxWidth(),
        color = IenTheme.colors.surface,
        tonalElevation = IenTheme.elevation.raised,
    ) {
        Column(
            modifier = Modifier.padding(IenTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
        ) {
            Text(title, style = IenTheme.typography.title3)
            IenDivider()
            content()
        }
    }
}

internal val LocalComponentSectionChrome = staticCompositionLocalOf { true }
internal val LocalComponentVariantShowcase = staticCompositionLocalOf { false }

@Preview
@Composable
fun WheelPickerSection(controls: Map<String, String> = emptyMap()) {
    val type = controls.enumValue("type", "Date")
    val enabled = !controls.disabledValue()
    val use24HourFormat = controls.booleanValue("use24HourFormat", true)
    val showHours = controls.booleanValue("showHours", true)
    val showMinutes = controls.booleanValue("showMinutes", true)
    val showSeconds = controls.booleanValue("showSeconds", true)
    val maxHours = controls.intValue("maxHours", 99).coerceAtLeast(0)
    IenTheme {
        var date by remember { mutableStateOf(KDate(2024, 2, 29)) }
        var time by remember {
            mutableStateOf(
                KDuration.of(
                    13 * 3600 + 5 * 60 + 5,
                    KFixedTimeUnit.Second
                )
            )
        }
        var duration by remember {
            mutableStateOf(
                KDuration.of(
                    1 * 3600 + 5 * 60 + 5,
                    KFixedTimeUnit.Second
                )
            )
        }
        val dateLabel = "${date.year}-${date.month.toString().padStart(2, '0')}-${
            date.day.toString().padStart(2, '0')
        }"
        val timeLabel = listOf(
            time.hourPart(),
            time.minutePart(),
            time.secondPart()
        ).joinToString(":") { it.toString().padStart(2, '0') }
        val durationLabel = "${duration.toHours().toString().padStart(2, '0')}:${
            duration.minutePart().toString().padStart(2, '0')
        }:${duration.secondPart().toString().padStart(2, '0')}"
        ComponentSection(title = "WheelPicker") {
            Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm)) {
                when (type) {
                    "Time" -> {
                        Text("시각 · $timeLabel", style = IenTheme.typography.label1)
                        if (showHours || showMinutes || showSeconds) {
                            IenTimeWheelPicker(
                                time,
                                { time = it },
                                Modifier.fillMaxWidth(),
                                use24HourFormat = use24HourFormat,
                                showHours = showHours,
                                showMinutes = showMinutes,
                                showSeconds = showSeconds,
                                enabled = enabled,
                            )
                        } else {
                            Text("시각 필드를 하나 이상 선택하세요.", color = IenTheme.colors.textSecondary)
                        }
                    }

                    "Duration" -> {
                        Text("기간 · $durationLabel", style = IenTheme.typography.label1)
                        if (showHours || showMinutes || showSeconds) {
                            val durationHours = duration.toHours().coerceAtMost(maxHours.toLong())
                            IenDurationWheelPicker(
                                KDuration.of(
                                    durationHours * 3600 + duration.minutePart() * 60 + duration.secondPart(),
                                    KFixedTimeUnit.Second,
                                ),
                                { duration = it },
                                Modifier.fillMaxWidth(),
                                showHours = showHours,
                                showMinutes = showMinutes,
                                showSeconds = showSeconds,
                                maxHours = maxHours,
                                enabled = enabled,
                            )
                        } else {
                            Text("기간 필드를 하나 이상 선택하세요.", color = IenTheme.colors.textSecondary)
                        }
                    }

                    else -> {
                        Text("날짜 · $dateLabel", style = IenTheme.typography.label1)
                        IenDateWheelPicker(date, { date = it }, Modifier.fillMaxWidth(), enabled = enabled)
                    }
                }
            }
        }
    }
}
