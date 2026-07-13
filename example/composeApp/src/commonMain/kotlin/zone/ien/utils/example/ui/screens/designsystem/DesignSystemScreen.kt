package zone.ien.utils.example.ui.screens.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons
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
import zone.ien.utils.ui.components.composite.IenAgreementItem
import zone.ien.utils.ui.components.composite.IenAgreement
import zone.ien.utils.ui.components.composite.IenAgreementVariant
import zone.ien.utils.ui.components.composite.IenAgreementText
import zone.ien.utils.ui.components.composite.IenAgreementCheckbox
import zone.ien.utils.ui.components.composite.IenAgreementCheckboxVariant
import zone.ien.utils.ui.components.composite.IenAgreementNecessity
import zone.ien.utils.ui.components.composite.IenAgreementNecessityVariant
import zone.ien.utils.ui.components.composite.IenAgreementBadge
import zone.ien.utils.ui.components.composite.IenAgreementBadgeVariant
import zone.ien.utils.ui.components.composite.IenAgreementRightArrow
import zone.ien.utils.ui.components.composite.IenAgreementDescription
import zone.ien.utils.ui.components.composite.IenAgreementDescriptionVariant
import zone.ien.utils.ui.components.composite.IenAgreementGroup
import zone.ien.utils.ui.components.composite.IenAgreementCollapsible
import zone.ien.utils.ui.components.composite.IenAgreementCollapsibleTrigger
import zone.ien.utils.ui.components.composite.IenAgreementCollapsibleContent
import zone.ien.utils.ui.components.composite.IenAgreementIndentPushable
import zone.ien.utils.ui.components.composite.IenAgreementIndentPushableTrigger
import zone.ien.utils.ui.components.composite.IenAgreementIndentPushableContent
import zone.ien.utils.ui.components.composite.IenAlertDialog
import zone.ien.utils.ui.components.composite.IenAssetFrame
import zone.ien.utils.ui.components.composite.IenAssetFrameShape
import zone.ien.utils.ui.components.composite.IenAssetFrameSize
import zone.ien.utils.ui.components.composite.IenBoardRow
import zone.ien.utils.ui.components.composite.IenBorder
import zone.ien.utils.ui.components.composite.IenBorderVariant
import zone.ien.utils.ui.components.composite.IenBottomCTA
import zone.ien.utils.ui.components.composite.IenBottomCTAAnimation
import zone.ien.utils.ui.components.composite.IenBottomCTABackground
import zone.ien.utils.ui.components.composite.IenBottomCTAButton
import zone.ien.utils.ui.components.composite.IenBottomCTAShowAfterDelay
import zone.ien.utils.ui.components.composite.IenBottomInfo
import zone.ien.utils.ui.components.composite.IenBottomSheet
import zone.ien.utils.ui.components.composite.IenBottomSheetOption
import zone.ien.utils.ui.components.composite.IenBottomSheetSelect
import zone.ien.utils.ui.components.composite.IenBubble
import zone.ien.utils.ui.components.composite.IenBubbleBackground
import zone.ien.utils.ui.components.composite.IenAlertDialogAlertButton
import zone.ien.utils.ui.components.composite.IenAlertDialogDescription
import zone.ien.utils.ui.components.composite.IenAlertDialogTitle
import zone.ien.utils.ui.components.composite.IenConfirmDialog
import zone.ien.utils.ui.components.composite.IenConfirmDialogCancelButton
import zone.ien.utils.ui.components.composite.IenConfirmDialogConfirmButton
import zone.ien.utils.ui.components.composite.IenConfirmDialogTitle
import zone.ien.utils.ui.components.composite.IenDialogButtonLayout
import zone.ien.utils.ui.components.composite.IenDialog
import zone.ien.utils.ui.components.composite.IenDialogAction
import zone.ien.utils.ui.components.composite.IenDoubleBottomCTA
import zone.ien.utils.ui.components.composite.IenFixedBottomCTA
import zone.ien.utils.ui.components.composite.IenFixedDoubleBottomCTA
import zone.ien.utils.ui.components.composite.IenHighlightText
import zone.ien.utils.ui.components.composite.IenListFooter
import zone.ien.utils.ui.components.composite.IenListFooterBorder
import zone.ien.utils.ui.components.composite.IenListFooterDefaults
import zone.ien.utils.ui.components.composite.IenListHeader
import zone.ien.utils.ui.components.composite.IenListHeaderDescriptionPosition
import zone.ien.utils.ui.components.composite.IenListRow
import zone.ien.utils.ui.components.composite.IenListRowAlignment
import zone.ien.utils.ui.components.composite.IenListRowAssetShape
import zone.ien.utils.ui.components.composite.IenListRowAssetText
import zone.ien.utils.ui.components.composite.IenListRowBorder
import zone.ien.utils.ui.components.composite.IenListRowDisabledStyle
import zone.ien.utils.ui.components.composite.IenListRowLoader
import zone.ien.utils.ui.components.composite.IenListRowLoaderType
import zone.ien.utils.ui.components.composite.IenListRowPadding
import zone.ien.utils.ui.components.composite.IenListRowTexts
import zone.ien.utils.ui.components.composite.IenListRowTextsType
import zone.ien.utils.ui.components.composite.IenLoader
import zone.ien.utils.ui.components.composite.IenMenu
import zone.ien.utils.ui.components.composite.IenModal
import zone.ien.utils.ui.components.composite.IenParagraph
import zone.ien.utils.ui.components.composite.IenPost
import zone.ien.utils.ui.components.composite.IenProgressBar
import zone.ien.utils.ui.components.composite.IenProgressBarSize
import zone.ien.utils.ui.components.composite.IenProgressStep
import zone.ien.utils.ui.components.composite.IenProgressStepper
import zone.ien.utils.ui.components.composite.IenProgressStepperPaddingTop
import zone.ien.utils.ui.components.composite.IenProgressStepperVariant
import zone.ien.utils.ui.components.composite.IenResult
import zone.ien.utils.ui.components.composite.IenResultTone
import zone.ien.utils.ui.components.composite.IenScaffold
import zone.ien.utils.ui.components.composite.IenSheetDetent
import zone.ien.utils.ui.components.composite.IenSkeleton
import zone.ien.utils.ui.components.composite.IenSkeletonBackground
import zone.ien.utils.ui.components.composite.IenSkeletonElement
import zone.ien.utils.ui.components.composite.IenSkeletonMotionGroup
import zone.ien.utils.ui.components.composite.IenSkeletonPattern
import zone.ien.utils.ui.components.composite.IenSkeletonRepeat
import zone.ien.utils.ui.components.composite.IenTableRow
import zone.ien.utils.ui.components.composite.IenTableRowAlign
import zone.ien.utils.ui.components.composite.IenToast
import zone.ien.utils.ui.components.composite.IenToastAction
import zone.ien.utils.ui.components.composite.IenToastIcon
import zone.ien.utils.ui.components.composite.IenToastPosition
import zone.ien.utils.ui.components.composite.IenTooltip
import zone.ien.utils.ui.components.composite.IenTooltipClipToEnd
import zone.ien.utils.ui.components.composite.IenTooltipMessageAlign
import zone.ien.utils.ui.components.composite.IenTooltipMotionVariant
import zone.ien.utils.ui.components.composite.IenTooltipPlacement
import zone.ien.utils.ui.components.composite.IenTop
import zone.ien.utils.ui.components.composite.IenTopBar
import zone.ien.utils.ui.components.composite.IenTopLowerButton
import zone.ien.utils.ui.components.composite.IenTopLowerCTA
import zone.ien.utils.ui.components.composite.IenTopLowerCTAButton
import zone.ien.utils.ui.components.composite.IenTopRightAssetContent
import zone.ien.utils.ui.components.composite.IenTopRightButton
import zone.ien.utils.ui.components.composite.IenTopRightVerticalAlign
import zone.ien.utils.ui.components.composite.IenTopSelectorType
import zone.ien.utils.ui.components.composite.IenTopSubtitleBadge
import zone.ien.utils.ui.components.composite.IenTopSubtitleBadges
import zone.ien.utils.ui.components.composite.IenTopSubtitleParagraph
import zone.ien.utils.ui.components.composite.IenTopSubtitleSelector
import zone.ien.utils.ui.components.composite.IenTopSubtitleSize
import zone.ien.utils.ui.components.composite.IenTopSubtitleTextButton
import zone.ien.utils.ui.components.composite.IenTopTitleParagraph
import zone.ien.utils.ui.components.composite.IenTopTitleSelector
import zone.ien.utils.ui.components.composite.IenTopTitleSize
import zone.ien.utils.ui.components.composite.IenTopTitleTextButton
import zone.ien.utils.ui.components.composite.IenTopUpperAssetContent
import zone.ien.utils.ui.components.composite.rememberIenBottomSheetState
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.IenAlphabetKeyboard
import zone.ien.utils.ui.components.interactive.IenAlphabetKeypad
import zone.ien.utils.ui.components.interactive.IenBadge
import zone.ien.utils.ui.components.interactive.IenBadgeSize
import zone.ien.utils.ui.components.interactive.IenBadgeVariant
import zone.ien.utils.ui.components.interactive.IenButton
import zone.ien.utils.ui.components.interactive.IenButtonDisplay
import zone.ien.utils.ui.components.interactive.IenButtonSize
import zone.ien.utils.ui.components.interactive.IenButtonState
import zone.ien.utils.ui.components.interactive.IenButtonVariant
import zone.ien.utils.ui.components.interactive.IenCircleCheckbox
import zone.ien.utils.ui.components.interactive.IenClearableTextField
import zone.ien.utils.ui.components.interactive.IenFullSecureKeyboard
import zone.ien.utils.ui.components.interactive.IenFullSecureKeypad
import zone.ien.utils.ui.components.interactive.IenExtendedFab
import zone.ien.utils.ui.components.interactive.IenFab
import zone.ien.utils.ui.components.interactive.IenFabSize
import zone.ien.utils.ui.components.interactive.IenFloatingTabBar
import zone.ien.utils.ui.components.interactive.IenIconButton
import zone.ien.utils.ui.components.interactive.IenKeyboardAction
import zone.ien.utils.ui.components.interactive.IenLineCheckbox
import zone.ien.utils.ui.components.interactive.IenNumberKeypad
import zone.ien.utils.ui.components.interactive.IenNumericSpinner
import zone.ien.utils.ui.components.interactive.IenNumericSpinnerSize
import zone.ien.utils.ui.components.interactive.IenRating
import zone.ien.utils.ui.components.interactive.IenRatingSize
import zone.ien.utils.ui.components.interactive.IenRatingVariant
import zone.ien.utils.ui.components.interactive.IenSearchField
import zone.ien.utils.ui.components.interactive.IenSecureKeyboardLanguage
import zone.ien.utils.ui.components.interactive.IenSecureKeyboardState
import zone.ien.utils.ui.components.interactive.IenSegmentedControl
import zone.ien.utils.ui.components.interactive.IenSegmentedControlAlignment
import zone.ien.utils.ui.components.interactive.IenSegmentedControlItem
import zone.ien.utils.ui.components.interactive.IenSegmentedControlSize
import zone.ien.utils.ui.components.interactive.IenSlider
import zone.ien.utils.ui.components.interactive.IenSplitTextField
import zone.ien.utils.ui.components.interactive.IenStepper
import zone.ien.utils.ui.components.interactive.IenStepperAssetFrame
import zone.ien.utils.ui.components.interactive.IenStepperAssetFrameShape
import zone.ien.utils.ui.components.interactive.IenStepperAssetFrameColors
import zone.ien.utils.ui.components.interactive.IenStepperAssetFrameDefaults
import zone.ien.utils.ui.components.interactive.IenStepperNumberIcon
import zone.ien.utils.ui.components.interactive.IenStepperRightArrow
import zone.ien.utils.ui.components.interactive.IenStepperRightButton
import zone.ien.utils.ui.components.interactive.IenStepperTexts
import zone.ien.utils.ui.components.interactive.IenStepperTextsType
import zone.ien.utils.ui.components.interactive.IenSwitch
import zone.ien.utils.ui.components.interactive.IenTab
import zone.ien.utils.ui.components.interactive.IenTabItem
import zone.ien.utils.ui.components.interactive.IenTabSize
import zone.ien.utils.ui.components.interactive.IenTextArea
import zone.ien.utils.ui.components.interactive.IenTextButton
import zone.ien.utils.ui.components.interactive.IenTextButtonSize
import zone.ien.utils.ui.components.interactive.IenTextButtonVariant
import zone.ien.utils.ui.components.interactive.IenTextField
import zone.ien.utils.ui.components.interactive.IenTextFieldButton
import zone.ien.utils.ui.components.interactive.IenTextFieldFormat
import zone.ien.utils.ui.components.interactive.IenTextFieldLabelOption
import zone.ien.utils.ui.components.interactive.IenTextFieldState
import zone.ien.utils.ui.components.interactive.IenTextFieldVariant
import zone.ien.utils.ui.components.interactive.IenPasswordTextField
import zone.ien.utils.ui.components.interactive.rememberIenFullSecureKeypadState
import zone.ien.utils.ui.components.primitives.IenBorderBox
import zone.ien.utils.ui.components.primitives.IenClickable
import zone.ien.utils.ui.components.primitives.IenDivider
import zone.ien.utils.ui.components.primitives.IenIcon
import zone.ien.utils.ui.components.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.components.primitives.IenProvideTextStyle
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText
import zone.ien.utils.ui.dialog.M3AlertDialog

@Preview
@Composable
fun DesignSystemScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    navigateToColor: () -> Unit = {}
) {
    IenTheme {
        var showTopToast by remember { mutableStateOf(false) }
        var showBottomToast by remember { mutableStateOf(false) }
        var showIconToast by remember { mutableStateOf(false) }
        var showActionToast by remember { mutableStateOf(false) }
        var showCtaToast by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        IenScaffold(
            modifier = modifier,
            contentEdgeBlurTopProgress = (scrollState.value / 48f).coerceIn(0f, 1f),
            topBar = {
                IenTopBar(
                    title = "Ien CMP UI",
                    subtitle = "토큰 기반 모바일 디자인 시스템",
                    navigationIcon = { IenTextButton(text = "닫기", onClick = navigateBack) },
                    actions = {
                        IenIconButton(
                            onClick = navigateToColor,
                            variant = IenButtonVariant.Ghost
                        ) {
                            IenIcon(
                                imageVector = M3SystemIcons.Keyboard,
                                contentDescription = null
                            )
                        }
                    },
                )
            },
            bottomBar = {
                IenBottomCTA(text = "샘플 하단 CTA", onClick = {})
            },
        ) { contentPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(contentPadding),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                ) {
                    BadgeSection()
                    BoardRowSection()
                    BorderSection()
                    BottomInfoSection()
                    BottomSheetSection()
                    BubbleSection()
                    ButtonSection()
                    CheckboxSection()
                    FabSection()
                    HighlightSection()
                    IconButtonSection()
                    ListFooterSection()
                    ListHeaderSection()
                    LoaderSection()
                    MenuSection()
                    ModalSection()
                    NumericSpinnerSection()
                    ParagraphSection()
                    PostSection()
                    ProgressBarSection()
                    ProgressStepperSection()
                    RatingSection()
                    ResultSection()
                    SearchFieldSection()
                    SegmentedControlSection()
                    SkeletonSection()
                    SliderSection()
                    StepperSection()
                    SwitchSection()
                    TabSection()
                    TableRowSection()
                    TextButtonSection()
                    ToastSection(
                        showTopToast = showTopToast,
                        showBottomToast = showBottomToast,
                        showIconToast = showIconToast,
                        showActionToast = showActionToast,
                        showCtaToast = showCtaToast,
                        onShowTopToastChange = { showTopToast = it },
                        onShowBottomToastChange = { showBottomToast = it },
                        onShowIconToastChange = { showIconToast = it },
                        onShowActionToastChange = { showActionToast = it },
                        onShowCtaToastChange = { showCtaToast = it },
                    )
                    TooltipSection()
                    TopSection()
                    AgreementSection()
                    AssetSection()
                    BottomCTASection()
                    DialogSection()
                    KeypadSection()
                    ListRowSection()
                    TextFieldSection()
                    SplitTextFieldSection()
                    TextAreaSection()
                    PrimitivesSection()

                    Spacer(modifier = Modifier.height(IenTheme.spacing.md))
                }

                IenToast(
                    open = showTopToast,
                    position = IenToastPosition.Top,
                    text = "상단 토스트 메시지예요",
                    onClose = { showTopToast = false },
                )
                IenToast(
                    open = showBottomToast,
                    position = IenToastPosition.Bottom,
                    text = "하단 토스트 메시지예요",
                    onClose = { showBottomToast = false },
                )
                IenToast(
                    open = showIconToast,
                    position = IenToastPosition.Top,
                    text = "아이콘이 포함된 토스트예요",
                    leftAddon = { IenToastIcon(tone = IenSemanticTone.Success) },
                    onClose = { showIconToast = false },
                )
                IenToast(
                    open = showActionToast,
                    position = IenToastPosition.Bottom,
                    text = "버튼이 포함된 토스트예요",
                    button = IenToastAction("확인") { showActionToast = false },
                    onClose = { showActionToast = false },
                )
                IenToast(
                    open = showCtaToast,
                    position = IenToastPosition.Bottom,
                    text = "CTA 버튼 위에 표시되는 토스트예요",
                    higherThanCTA = true,
                    onClose = { showCtaToast = false },
                )
            }
        }
    }
}

@Preview
@Composable
fun BadgeSection() {
    IenTheme {
        ComponentSection(title = "Badge") {
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                IenBadge("NEW", variant = IenBadgeVariant.Fill)
                IenBadge("주의", tone = IenSemanticTone.Warning)
                IenBadge("오류", size = IenBadgeSize.Large, variant = IenBadgeVariant.Line, tone = IenSemanticTone.Danger)
            }
        }
    }
}

@Preview
@Composable
fun BoardRowSection() {
    IenTheme {
        ComponentSection(title = "BoardRow") {
            IenBoardRow(
                title = { IenText("배송 정보 자세히 보기", style = IenTheme.typography.label1) },
                initialOpened = true,
                prefix = { IenBadge("배송", size = IenBadgeSize.Small) },
            ) {
                IenText("제한된 영역에서 상세 정보를 접고 펼치는 아코디언형 정보 구조입니다.", color = IenTheme.colors.textSecondary)
            }
            IenBoardRow(
                title = { IenText("배송 정보 자세히 보기2", style = IenTheme.typography.label1) },
                prefix = { IenBadge("배송", size = IenBadgeSize.Small) },
            ) {
                IenText("제한된 영역에서 상세 정보를 접고 펼치는 아코디언형 정보 구조입니다.", color = IenTheme.colors.textSecondary)
            }
        }
    }
}

@Preview
@Composable
fun BorderSection() {
    IenTheme {
        ComponentSection(title = "Border") {
            IenText("Full", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
            IenBorder()
            IenText("Padding24", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
            IenBorder(variant = IenBorderVariant.Padding24)
            IenText("Height16", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
            IenBorder(variant = IenBorderVariant.Height())
        }
    }
}

@Preview
@Composable
fun BottomInfoSection() {
    IenTheme {
        ComponentSection(title = "BottomInfo") {
            IenBottomInfo {
                IenText(
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
fun BottomSheetSection() {
    IenTheme {
        val sheetState = rememberIenBottomSheetState()
        val selectSheetState = rememberIenBottomSheetState()
        var selectedPet by remember { mutableStateOf<String?>("강아지") }

        ComponentSection(title = "BottomSheet") {
            IenButton(
                text = "일반 바텀시트 열기",
                onClick = { sheetState.show(IenSheetDetent.Content) },
                display = IenButtonDisplay.Block,
            )
            Spacer(modifier = Modifier.height(8.dp))
            IenButton(
                text = "선택형 바텀시트 열기 (선택: $selectedPet)",
                onClick = { selectSheetState.show(IenSheetDetent.Content) },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
            )
        }

        IenBottomSheet(
            state = sheetState,
            header = { IenText("바텀시트", style = IenTheme.typography.title3) },
            cta = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)
                ) {
                    IenButton(
                        text = "닫기",
                        onClick = { sheetState.hide() },
                        modifier = Modifier.weight(1f),
                        variant = IenButtonVariant.Weak,
                    )
                    IenButton(
                        text = "확인",
                        onClick = { sheetState.hide() },
                        modifier = Modifier.weight(1f),
                    )
                }
            },
        ) {
            IenText(
                text = "공통 API는 유지하면서 Android와 iOS의 시트 감각 차이는 내부 구현에서 흡수합니다.",
                color = IenTheme.colors.textSecondary,
            )
            IenBottomInfo(
                backgroundColor = IenTheme.colors.brandWeak
            ) {
                IenText(
                    text = "스크림을 누르면 닫히도록 설정되어 있습니다.",
                    style = IenTheme.typography.caption,
                    color = IenTheme.colors.brand
                )
            }
        }

        IenBottomSheet(
            state = selectSheetState,
            header = { IenText("좋아하는 동물을 선택해주세요.", style = IenTheme.typography.title3) },
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
fun BubbleSection() {
    IenTheme {
        ComponentSection(title = "Bubble") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    IenBubble(background = IenBubbleBackground.Grey, withTail = false) {
                        IenText("꼬리가 없는 회색 버블입니다.", style = IenTheme.typography.body2)
                    }
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    IenBubble(background = IenBubbleBackground.Grey, withTail = true) {
                        IenText("안녕하세요! 상대방이 보내는 회색 버블(grey)입니다.", style = IenTheme.typography.body2)
                    }
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    IenBubble(background = IenBubbleBackground.Brand, withTail = false) {
                        IenText("꼬리가 없는 파란색 버블입니다.", style = IenTheme.typography.body2)
                    }
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    IenBubble(background = IenBubbleBackground.Brand, withTail = true) {
                        IenText("반가워요! 제가 보내는 파란색 버블(blue)입니다.", style = IenTheme.typography.body2)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ButtonSection() {
    IenTheme {
        ComponentSection(title = "Button") {
            IenButton(text = "주요 액션", onClick = {}, display = IenButtonDisplay.Block, size = IenButtonSize.Small)
            IenButton(text = "주요 액션2", onClick = {}, display = IenButtonDisplay.Block, size = IenButtonSize.Medium)
            IenButton(text = "주요 액션3", onClick = {}, display = IenButtonDisplay.Block, size = IenButtonSize.Large)
            IenButton(text = "Full", onClick = {}, size = IenButtonSize.Large, display = IenButtonDisplay.Full)
            IenButton(text = "Block", onClick = {}, size = IenButtonSize.Large, display = IenButtonDisplay.Block)
            IenButton(text = "Inline", onClick = {}, size = IenButtonSize.Large, display = IenButtonDisplay.Inline)
            IenButton(text = "주요 액션", onClick = {}, display = IenButtonDisplay.Block, state = IenButtonState(enabled = false))
            IenButton(text = "주요 액션", onClick = {}, display = IenButtonDisplay.Block, state = IenButtonState(loading = true))
            IenButton(text = "보조 액션", onClick = {}, display = IenButtonDisplay.Block, variant = IenButtonVariant.Weak)
            IenButton(text = "보조 액션", onClick = {}, display = IenButtonDisplay.Block, variant = IenButtonVariant.Line)
            IenButton(text = "로딩", onClick = {}, size = IenButtonSize.Medium, state = IenButtonState(loading = true))
        }
    }
}

@Preview
@Composable
fun FabSection() {
    IenTheme {
        ComponentSection(title = "FAB") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                IenText("Sizes", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IenFab(onClick = {}, size = IenFabSize.Small) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenFab(onClick = {}, size = IenFabSize.Regular) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenFab(onClick = {}, size = IenFabSize.Large) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                }
                IenBorder()
                IenText("Variants", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IenFab(onClick = {}, variant = IenButtonVariant.Fill) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenFab(onClick = {}, variant = IenButtonVariant.Weak) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenFab(onClick = {}, variant = IenButtonVariant.Line) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenFab(onClick = {}, variant = IenButtonVariant.Ghost) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                }
                IenBorder()
                IenText("Extended / States", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                ) {
                    IenExtendedFab(
                        text = "작성하기",
                        onClick = {},
                        icon = {
                            IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                        },
                    )
                    IenExtendedFab(
                        text = "로딩",
                        onClick = {},
                        state = IenButtonState(loading = true),
                    )
                    IenExtendedFab(
                        text = "비활성",
                        onClick = {},
                        state = IenButtonState(enabled = false),
                        variant = IenButtonVariant.Weak,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CheckboxSection() {
    IenTheme {
        var checked by remember { mutableStateOf(true) }
        ComponentSection(title = "Checkbox") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                IenText("Circle Checkbox", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IenCircleCheckbox(checked = checked, onCheckedChange = { checked = it }, label = "동의 (제어)")
                    IenCircleCheckbox(defaultChecked = true, label = "비제어(초기참)")
                    IenCircleCheckbox(checked = true, label = "비활성(선택-흔들림)", enabled = false)
                    IenCircleCheckbox(checked = false, label = "비활성(해제-흔들림)", enabled = false)
                }
                IenBorder()
                IenText("Line Checkbox", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IenLineCheckbox(checked = checked, onCheckedChange = { checked = it }, label = "동의 (제어)")
                    IenLineCheckbox(defaultChecked = true, label = "비제어(초기참)")
                    IenLineCheckbox(checked = true, label = "비활성(선택-흔들림)", enabled = false)
                    IenLineCheckbox(checked = false, label = "비활성(해제-흔들림)", enabled = false)
                }
            }
        }
    }
}

@Preview
@Composable
fun HighlightSection() {
    IenTheme {
        ComponentSection(title = "Highlight") {
            IenHighlightText(
                text = "Highlight는 검색 결과나 본문 안의 중요한 텍스트를 토큰 색상으로 강조합니다.",
                highlights = listOf("Highlight", "강조"),
            )
        }
    }
}

@Preview
@Composable
fun IconButtonSection() {
    IenTheme {
        ComponentSection(title = "IconButton") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                IenText("Sizes (Large, Medium, Small)", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IenIconButton(onClick = {}, size = IenButtonSize.Large, variant = IenButtonVariant.Fill) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenIconButton(onClick = {}, size = IenButtonSize.Medium, variant = IenButtonVariant.Fill) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenIconButton(onClick = {}, size = IenButtonSize.Small, variant = IenButtonVariant.Fill) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                }
                IenBorder()
                IenText("Variants (Fill, Weak, Line, Ghost)", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                    IenIconButton(onClick = {}, variant = IenButtonVariant.Fill) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenIconButton(onClick = {}, variant = IenButtonVariant.Weak) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenIconButton(onClick = {}, variant = IenButtonVariant.Line) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenIconButton(onClick = {}, variant = IenButtonVariant.Ghost) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                }
                IenBorder()
                IenText("States (Loading, Disabled)", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                    IenIconButton(onClick = {}, state = IenButtonState(loading = true), variant = IenButtonVariant.Fill) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                    IenIconButton(onClick = {}, state = IenButtonState(enabled = false), variant = IenButtonVariant.Fill) {
                        IenIcon(imageVector = M3SystemIcons.Filled.Check, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ListFooterSection() {
    IenTheme {
        ComponentSection(title = "ListFooter") {
            IenListFooter(
                text = "더 보기",
                onClick = {},
                border = IenListFooterBorder.Full
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
fun ListHeaderSection() {
    IenTheme {
        ComponentSection(title = "ListHeader") {
            IenListHeader(
                title = "최근 거래",
                description = "타이틀 위에 보조 설명이 배치됩니다.",
                descriptionPosition = IenListHeaderDescriptionPosition.Top,
                right = {
                    IenTextButton(text = "전체보기", onClick = {})
                }
            )
            IenListHeader(
                title = "자주 쓰는 계좌",
                description = "타이틀 아래에 보조 설명이 배치됩니다.",
                descriptionPosition = IenListHeaderDescriptionPosition.Bottom,
                right = {
                    IenTextButton(text = "편집", onClick = {})
                }
            )
        }
    }
}

@Preview
@Composable
fun LoaderSection() {
    IenTheme {
        ComponentSection(title = "Loader") {
            IenLoader(label = "데이터를 불러오는 중")
            IenLoaderPrimitive(color = IenTheme.colors.brand)
        }
    }
}

@Preview
@Composable
fun MenuSection() {
    var menuOpen by remember { mutableStateOf(false) }
    var checkedMenu by remember { mutableIntStateOf(1) }

    IenTheme {
        ComponentSection(title = "Menu") {
            IenMenu.Trigger(
                open = menuOpen,
                onOpen = { menuOpen = true },
                onClose = { menuOpen = false },
                placement = IenMenu.Placement.BottomStart,
                offset = DpOffset(0.dp, 8.dp),
                dropdown = {
                    IenMenu.Dropdown(
                        onDismissRequest = { menuOpen = false },
                        header = { IenMenu.Header(text = "작업 선택") },
                    ) {
                        IenMenu.DropdownItem(
                            text = "수정",
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
                    text = "메뉴 열기",
                    onClick = { menuOpen = true },
                )
            }
        }
    }
}

@Preview
@Composable
fun ModalSection() {
    IenTheme {
        var showModal by remember { mutableStateOf(false) }

        ComponentSection(title = "Modal") {
            IenButton(
                text = "모달 열기",
                onClick = { showModal = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
            )
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
                IenText(
                    text = "모달",
                    style = IenTheme.typography.title2,
                )
                IenText(
                    text = "Modal은 Overlay와 Content를 조합해서 중요한 콘텐츠를 표시합니다.",
                    style = IenTheme.typography.body2,
                    color = IenTheme.colors.textSecondary,
                )
                IenButton(
                    text = "확인",
                    onClick = { showModal = false },
                    display = IenButtonDisplay.Block,
                )
            }
        }
    }
}

@Preview
@Composable
fun NumericSpinnerSection() {
    IenTheme {
        var spinnerValue by remember { mutableIntStateOf(2) }
        var tinySpinnerValue by remember { mutableIntStateOf(0) }
        ComponentSection(title = "NumericSpinner") {
            IenNumericSpinner(
                number = spinnerValue,
                onNumberChange = { spinnerValue = it },
                minNumber = 1,
                maxNumber = 9,
                size = IenNumericSpinnerSize.Large,
                decreaseAriaLabel = "수량 줄이기",
                increaseAriaLabel = "수량 늘리기",
            )
            IenNumericSpinner(
                number = tinySpinnerValue,
                onNumberChange = { tinySpinnerValue = it },
                minNumber = 0,
                maxNumber = 3,
                size = IenNumericSpinnerSize.Tiny,
                decreaseAriaLabel = "작은 값 줄이기",
                increaseAriaLabel = "작은 값 늘리기",
            )
        }
    }
}

@Preview
@Composable
fun ParagraphSection() {
    IenTheme {
        ComponentSection(title = "Paragraph") {
            IenParagraph(
                title = "문단 컴포넌트",
                body = "Paragraph는 본문 타이포그래피와 Highlight를 함께 사용해 긴 설명을 안정적으로 표시합니다.",
                emphasis = "Highlight",
                footer = "토큰 기반 줄 높이와 색상을 사용합니다.",
            )
        }
    }
}

@Preview
@Composable
fun PostSection() {
    IenTheme {
        ComponentSection(title = "Post") {
            IenPost(
                title = "Compose Multiplatform 디자인 시스템 진행 기록",
                author = "IENGROUND",
                description = "Post는 피드형 콘텐츠, 공지, 업데이트 카드의 공통 정보 구조입니다.",
                metadata = {
                    IenBadge("새 글", size = IenBadgeSize.Small)
                },
            )
        }
    }
}

@Preview
@Composable
fun ProgressBarSection() {
    IenTheme {
        var animatedProgress by remember { mutableStateOf(0f) }
        ComponentSection(title = "ProgressBar") {
            IenProgressBar(
                progress = 0.64f,
                size = IenProgressBarSize.Light,
                color = IenTheme.colors.brand,
                contentDescription = "얇은 진행률",
            )
            IenProgressBar(
                progress = 0.64f,
                size = IenProgressBarSize.Normal,
                color = IenTheme.colors.success,
                contentDescription = "기본 진행률",
            )
            IenProgressBar(
                progress = 0.64f,
                size = IenProgressBarSize.Bold,
                color = IenTheme.colors.danger,
                showLabel = true,
                contentDescription = "굵은 업로드 진행률",
            )
            IenProgressBar(
                progress = animatedProgress,
                size = IenProgressBarSize.Bold,
                animate = true,
                contentDescription = "애니메이션 진행률",
            )
            IenButton(
                text = if (animatedProgress == 0f) "애니메이션 시작" else "애니메이션 리셋",
                onClick = { animatedProgress = if (animatedProgress == 0f) 1f else 0f },
            )
        }
    }
}

@Preview
@Composable
fun ProgressStepperSection() {
    IenTheme {
        ComponentSection(title = "ProgressStepper") {
            IenProgressStepper(
                steps = listOf(
                    IenProgressStep(title = "유심 신청"),
                    IenProgressStep(title = "배송 완료"),
                    IenProgressStep(title = "개통 완료"),
                ),
                variant = IenProgressStepperVariant.Compact,
                activeStepIndex = 1,
            )
            IenProgressStepper(
                steps = listOf(
                    IenProgressStep(title = "첫 번째"),
                    IenProgressStep(title = "두 번째"),
                    IenProgressStep(title = "세 번째"),
                    IenProgressStep(title = "마지막"),
                ),
                variant = IenProgressStepperVariant.Icon,
                paddingTop = IenProgressStepperPaddingTop.Wide,
                activeStepIndex = 2,
                checkForFinish = true,
            )
            IenProgressStepper(
                steps = listOf(
                    IenProgressStep(),
                    IenProgressStep(),
                    IenProgressStep(),
                    IenProgressStep(),
                ),
                variant = IenProgressStepperVariant.Compact,
                activeStepIndex = 1,
            )
            IenProgressStepper(
                steps = listOf(
                    IenProgressStep(
                        title = "아이콘",
                        icon = {
                            IenIcon(
                                imageVector = M3SystemIcons.Filled.Check,
                                contentDescription = null,
                                tint = IenTheme.colors.onBrand,
                                size = IenTheme.icon.sm,
                            )
                        },
                    ),
                    IenProgressStep(title = "진행"),
                    IenProgressStep(title = "완료"),
                ),
                variant = IenProgressStepperVariant.Icon,
                activeStepIndex = 1,
            )
        }
    }
}

@Preview
@Composable
fun RatingSection() {
    IenTheme {
        var mediumRating by remember { mutableStateOf(3f) }
        var largeRating by remember { mutableStateOf(4f) }
        var bigRating by remember { mutableStateOf(5f) }
        ComponentSection(title = "Rating") {
            IenText(
                text = "Editable",
                style = IenTheme.typography.label1,
                color = IenTheme.colors.textSecondary,
            )
            IenRating(
                value = mediumRating,
                onValueChange = { mediumRating = it },
                size = IenRatingSize.Medium,
                ariaLabel = "중간 별점 평가",
            )
            IenRating(
                value = largeRating,
                onValueChange = { largeRating = it },
                size = IenRatingSize.Large,
                ariaLabel = "큰 별점 평가",
            )
            IenRating(
                value = bigRating,
                onValueChange = { bigRating = it },
                size = IenRatingSize.Big,
                ariaLabel = "아주 큰 별점 평가",
            )
            IenText(
                text = "ReadOnly variants",
                style = IenTheme.typography.label1,
                color = IenTheme.colors.textSecondary,
            )
            IenRating(
                readOnly = true,
                value = 4.5f,
                size = IenRatingSize.Tiny,
                variant = IenRatingVariant.Full,
            )
            IenRating(
                readOnly = true,
                value = 4.5f,
                size = IenRatingSize.Small,
                variant = IenRatingVariant.Full,
            )
            IenRating(
                readOnly = true,
                value = 4.5f,
                size = IenRatingSize.Medium,
                variant = IenRatingVariant.Full,
            )
            IenRating(
                readOnly = true,
                value = 4.5f,
                size = IenRatingSize.Large,
                variant = IenRatingVariant.Compact,
            )
            IenRating(
                readOnly = true,
                value = 4.5f,
                size = IenRatingSize.Big,
                variant = IenRatingVariant.IconOnly,
            )
            IenText(
                text = "Disabled",
                style = IenTheme.typography.label1,
                color = IenTheme.colors.textSecondary,
            )
            IenRating(
                value = 3f,
                onValueChange = {},
                size = IenRatingSize.Medium,
                disabled = true,
            )
        }
    }
}

@Preview
@Composable
fun ResultSection() {
    IenTheme {
        ComponentSection(title = "Result") {
            IenResult(
                title = "처리 준비 완료",
                description = "Result는 성공, 실패, 빈 상태 화면을 같은 정보 구조로 표현합니다.",
                tone = IenResultTone.Success,
                icon = {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(IenTheme.colors.success),
                    )
                },
                primaryAction = {
                    IenButton(text = "확인", onClick = {}, size = IenButtonSize.Medium)
                },
            )
        }
    }
}

@Preview
@Composable
fun SearchFieldSection() {
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
                placeholder = "컴포넌트 검색",
            )
            IenSearchField(
                value = deletableSearch,
                onValueChange = { deletableSearch = it },
                placeholder = "검색어를 입력하고 삭제 버튼을 눌러보세요",
                onDeleteClick = { deleteCount += 1 },
            )
            IenText(
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
                        fixed = true,
                        takeSpace = true,
                    )
                    IenText(
                        text = "fixed=true, takeSpace=true 예시입니다. 실제 화면에서는 Scaffold topBar 같은 고정 영역에 배치합니다.",
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
fun SegmentedControlSection() {
    IenTheme {
        var selected by remember { mutableStateOf("all") }
        ComponentSection(title = "SegmentedControl") {
            IenSegmentedControl(
                items = listOf(
                    IenSegmentedControlItem(value = "all", label = "전체"),
                    IenSegmentedControlItem(value = "progress", label = "진행"),
                    IenSegmentedControlItem(value = "done", label = "완료"),
                ),
                value = selected,
                onChange = { selected = it },
                modifier = Modifier.fillMaxWidth(),
                size = IenSegmentedControlSize.Small,
                alignment = IenSegmentedControlAlignment.Fixed,
            )
            IenSegmentedControl(
                items = listOf(
                    IenSegmentedControlItem(value = "today", label = "오늘"),
                    IenSegmentedControlItem(value = "week", label = "이번 주"),
                    IenSegmentedControlItem(value = "month", label = "이번 달"),
                    IenSegmentedControlItem(value = "quarter", label = "분기"),
                    IenSegmentedControlItem(value = "year", label = "올해"),
                ),
                defaultValue = "today",
                modifier = Modifier.fillMaxWidth(),
                size = IenSegmentedControlSize.Large,
                alignment = IenSegmentedControlAlignment.Fluid,
            )
        }
    }
}

@Preview
@Composable
fun SkeletonSection() {
    IenTheme {
        ComponentSection(title = "Skeleton") {
            IenSkeleton(
                modifier = Modifier.fillMaxWidth(),
                pattern = IenSkeletonPattern.TopListWithIcon,
                repeatLastItemCount = IenSkeletonRepeat.Count(3),
            )
            IenSkeleton(
                modifier = Modifier.fillMaxWidth(),
                pattern = IenSkeletonPattern.CardOnly,
                background = IenSkeletonBackground.GreyOpacity100,
            )
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

@Preview
@Composable
fun SliderSection() {
    IenTheme {
        var sliderValue by remember { mutableStateOf(0.35f) }
        ComponentSection(title = "Slider") {
            IenSlider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                label = "비율",
                valueLabel = "${(sliderValue * 100).toInt()}%",
            )
        }
    }
}

@Preview
@Composable
fun StepperSection() {
    IenTheme {
        ComponentSection(title = "Stepper") {
            IenStepper(
                modifier = Modifier.fillMaxWidth(),
                staggerDelay = 0.12f,
            ) {
                Row(
                    left = { IenStepperNumberIcon(number = 1) },
                    center = {
                        IenStepperTexts(
                            type = IenStepperTextsType.A,
                            title = "주문 접수",
                            description = "결제와 배송 정보를 확인했어요.",
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
                            title = "상품 준비 중",
                            description = "판매자가 상품을 포장하고 있어요.",
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
                            title = "배송 시작 예정",
                            description = "운송장이 등록되면 알림을 보내드릴게요.",
                        )
                    },
                    hideLine = true,
                )
            }
        }
    }
}

@Preview
@Composable
fun SwitchSection() {
    IenTheme {
        var switched by remember { mutableStateOf(true) }
        ComponentSection(title = "Switch") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IenText("자동 적용", modifier = Modifier.weight(1f))
                IenSwitch(checked = switched, onCheckedChange = { switched = it })
            }
        }
    }
}

@Preview
@Composable
fun TabSection() {
    IenTheme {
        var tabSelected by remember { mutableIntStateOf(0) }
        var smallTabSelected by remember { mutableIntStateOf(0) }
        var fluidTabSelected by remember { mutableIntStateOf(0) }
        var floatingTabSelected by remember { mutableIntStateOf(0) }
        ComponentSection(title = "Tab") {
            IenFloatingTabBar(
                items = listOf(
                    IenTabItem("홈", key = "home", icon = M3SystemIcons.Rounded.RoundedKeyboard, selectedIcon = M3SystemIcons.Filled.FilledKeyboard),
                    IenTabItem("혜택", key = "benefit", icon = M3SystemIcons.Rounded.RoundedCheck, selectedIcon = M3SystemIcons.Filled.Check),
                    IenTabItem("토스페이", key = "pay", icon = M3SystemIcons.Rounded.RoundedSave, selectedIcon = M3SystemIcons.Filled.FilledSave),
                    IenTabItem("증권", key = "stock", icon = M3SystemIcons.Rounded.RoundedCloudOff, selectedIcon = M3SystemIcons.Filled.FilledCloudOff),
                    IenTabItem("전체", key = "all", icon = M3SystemIcons.Rounded.RoundedMoreVert, selectedIcon = M3SystemIcons.Filled.FilledMoreVert),
                ),
                selectedIndex = floatingTabSelected,
                onSelectedIndexChange = { floatingTabSelected = it },
                ariaLabel = "모바일 하단 탭바",
            )
            IenTab(
                items = listOf(
                    IenTabItem("요약", key = "summary"),
                    IenTabItem("상세", key = "detail", redBean = true),
                    IenTabItem("내역", key = "history"),
                ),
                selectedIndex = tabSelected,
                onSelectedIndexChange = { tabSelected = it },
                size = IenTabSize.Large,
                ariaLabel = "주문 정보 탭",
            )
            IenTab(
                items = listOf(IenTabItem("작게"), IenTabItem("선택됨"), IenTabItem("비활성", enabled = false)),
                selectedIndex = smallTabSelected,
                onSelectedIndexChange = { smallTabSelected = it },
                size = IenTabSize.Small,
                itemGap = 8.dp,
                modifier = Modifier.fillMaxWidth(),
            )
            IenTab(
                items = List(12) { index ->
                    IenTabItem(
                        text = if (index == 0) "탭1" else "긴텍스트",
                        key = index,
                        redBean = index == 3,
                    )
                },
                selectedIndex = fluidTabSelected,
                onSelectedIndexChange = { fluidTabSelected = it },
                fluid = true,
                itemGap = 36.dp,
            )
        }
    }
}

@Preview
@Composable
fun TableRowSection() {
    IenTheme {
        ComponentSection(title = "TableRow") {
            IenTableRow(
                left = "김토스",
                right = "받는 분",
                align = IenTableRowAlign.SpaceBetween,
            )
            IenTableRow(
                left = "강토스",
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
                trailing = { IenBadge("할인", size = IenBadgeSize.Small, tone = IenSemanticTone.Success) },
            )
        }
    }
}

@Preview
@Composable
fun TextButtonSection() {
    IenTheme {
        ComponentSection(title = "TextButton") {
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                IenTextButton(text = "텍스트 버튼", size = IenTextButtonSize.XSmall, onClick = {})
                IenTextButton(text = "텍스트 버튼", size = IenTextButtonSize.Small, onClick = {})
                IenTextButton(text = "텍스트 버튼", size = IenTextButtonSize.Medium, onClick = {})
            }
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                IenTextButton(text = "텍스트 버튼", size = IenTextButtonSize.Large, onClick = {})
                IenTextButton(text = "텍스트 버튼", size = IenTextButtonSize.XLarge, onClick = {})
                IenTextButton(text = "텍스트 버튼", size = IenTextButtonSize.XXLarge, onClick = {})
            }
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                IenTextButton(
                    text = "화살표",
                    size = IenTextButtonSize.Medium,
                    variant = IenTextButtonVariant.Arrow,
                    onClick = {},
                )
                IenTextButton(
                    text = "밑줄",
                    size = IenTextButtonSize.XLarge,
                    variant = IenTextButtonVariant.Underline,
                    onClick = {},
                )
                IenTextButton(
                    text = "비활성",
                    size = IenTextButtonSize.XXLarge,
                    disabled = true,
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
fun ToastSection(
    showTopToast: Boolean = false,
    showBottomToast: Boolean = false,
    showIconToast: Boolean = false,
    showActionToast: Boolean = false,
    showCtaToast: Boolean = false,
    onShowTopToastChange: (Boolean) -> Unit = {},
    onShowBottomToastChange: (Boolean) -> Unit = {},
    onShowIconToastChange: (Boolean) -> Unit = {},
    onShowActionToastChange: (Boolean) -> Unit = {},
    onShowCtaToastChange: (Boolean) -> Unit = {},
) {
    IenTheme {
        ComponentSection(title = "Toast") {
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm)) {
                IenButton(
                    text = "상단",
                    onClick = { onShowTopToastChange(true) },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                )
                IenButton(
                    text = "하단",
                    onClick = { onShowBottomToastChange(true) },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                )
                IenButton(
                    text = "아이콘",
                    onClick = { onShowIconToastChange(true) },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm)) {
                IenButton(
                    text = "버튼",
                    onClick = { onShowActionToastChange(true) },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                )
                IenButton(
                    text = "CTA 위",
                    onClick = { onShowCtaToastChange(true) },
                    size = IenButtonSize.Small,
                    variant = IenButtonVariant.Weak,
                )
            }
        }
    }
}

@Preview
@Composable
fun TooltipSection() {
    IenTheme {
        var controlledTooltipOpen by remember { mutableStateOf(false) }
        ComponentSection(title = "Tooltip") {
            IenTooltip(
                text = "툴팁은 짧은 보조 설명에 사용합니다.",
                anchor = { toggle -> IenBadge("도움말", variant = IenBadgeVariant.Line) },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                IenTooltip(
                    text = "상단에 뜨는 도움말입니다.",
                    placement = IenTooltipPlacement.Top,
                    anchor = { toggle -> IenButton(text = "Top", size = IenButtonSize.Small, onClick = toggle) },
                )
                IenTooltip(
                    text = "중앙 정렬 툴팁은 메시지를 정중앙에 보여줍니다.",
                    messageAlign = IenTooltipMessageAlign.Center,
                    width = 180.dp,
                    anchor = { toggle -> IenButton(text = "Center", size = IenButtonSize.Small, onClick = toggle) },
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
                            text = "Toggle",
                            size = IenButtonSize.Small,
                            onClick = { controlledTooltipOpen = !controlledTooltipOpen },
                        )
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun TopSection() {
    IenTheme {
        ComponentSection(title = "Top") {
            IenTop(
                title = "결제 확인",
                subtitle = "Top은 화면 제목과 액션을 하나의 정보 구조로 묶습니다.",
                navigation = { IenTextButton(text = "뒤로", onClick = {}) },
                actions = { IenBadge("v4", size = IenBadgeSize.Small) },
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
                            IenText("₩")
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
                        text = "토스페이 결제",
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
                            IenText("%")
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
        }
    }
}

@Preview
@Composable
fun AgreementSection() {
    IenTheme {
        var agreements by remember {
            mutableStateOf(
                listOf(
                    IenAgreementItem(id = "service", title = "서비스 이용약관", checked = true, required = true),
                    IenAgreementItem(id = "privacy", title = "개인정보 처리방침", checked = false, required = true),
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
        var singleChecked by remember { mutableStateOf(false) }
        var dotChecked by remember { mutableStateOf(true) }
        
        var accordionOpen by remember { mutableStateOf(false) }
        var collapsibleChecked1 by remember { mutableStateOf(false) }
        var collapsibleChecked2 by remember { mutableStateOf(false) }

        var indentPushed by remember { mutableStateOf(true) }
        var indentChecked1 by remember { mutableStateOf(false) }
        var indentChecked2 by remember { mutableStateOf(false) }

        ComponentSection(title = "Agreement (TDS v4 Spec)") {
            IenText(text = "1. 단일 동의 항목 (체크박스 / 도트 / 히든)", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
            
            IenAgreement(
                variant = IenAgreementVariant.Large,
                onClick = { singleChecked = !singleChecked },
                left = {
                    IenAgreementCheckbox(
                        checked = singleChecked,
                        onCheckedChange = { singleChecked = it }
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
                variant = IenAgreementVariant.Medium,
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

            IenText(text = "2. 접었다 펼치는 아코디언 동의 (Collapsible)", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
            
            IenAgreementCollapsible(
                collapsed = !accordionOpen,
                onCollapsedChange = { accordionOpen = !it }
            ) {
                IenAgreementCollapsibleTrigger {
                    IenAgreement(
                        variant = IenAgreementVariant.Large,
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
                        variant = IenAgreementVariant.Small,
                        onClick = { collapsibleChecked1 = !collapsibleChecked1 },
                        left = {
                            IenAgreementCheckbox(checked = collapsibleChecked1, onCheckedChange = { collapsibleChecked1 = it })
                        },
                        middle = {
                            IenAgreementText(text = "이름, 전화번호 수집 동의")
                        }
                    )
                    IenAgreement(
                        variant = IenAgreementVariant.Small,
                        onClick = { collapsibleChecked2 = !collapsibleChecked2 },
                        left = {
                            IenAgreementCheckbox(checked = collapsibleChecked2, onCheckedChange = { collapsibleChecked2 = it })
                        },
                        middle = {
                            IenAgreementText(text = "이메일, 배송지 주소 수집 동의")
                        }
                    )
                    IenAgreementDescription(
                        text = "수집된 개인정보는 서비스 배송 목적으로만 활용되며, 탈퇴 시 즉시 파기됩니다.",
                        variant = IenAgreementDescriptionVariant.Box
                    )
                }
            }

            IenDivider()

            IenText(text = "3. 여러 동의 항목 그룹화 (Group)", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)

            IenAgreementGroup {
                IenAgreement(
                    variant = IenAgreementVariant.Large,
                    left = { IenAgreementCheckbox(checked = false, onCheckedChange = {}, variant = IenAgreementCheckboxVariant.Hidden) },
                    middle = { IenAgreementText(text = "카드상품 이외의 부수서비스 안내 등을 위한 수집/이용") }
                )
                IenAgreement(
                    variant = IenAgreementVariant.Small,
                    left = { IenAgreementCheckbox(checked = false, onCheckedChange = {}, variant = IenAgreementCheckboxVariant.Hidden) },
                    middle = { IenAgreementText(text = "개인(신용)정보 수집/이용") }
                )
                IenAgreement(
                    variant = IenAgreementVariant.SmallLast,
                    left = { IenAgreementCheckbox(checked = false, onCheckedChange = {}, variant = IenAgreementCheckboxVariant.Hidden) },
                    middle = { IenAgreementText(text = "전자적 매체를 통한 광고성 정보 수신") }
                )
            }

            IenDivider()

            IenText(text = "4. 동적 들여쓰기 동의 (IndentPushable)", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
            
            IenAgreementIndentPushable(
                pushed = indentPushed,
                onPushedChange = { indentPushed = it },
            ) {
                IenAgreementIndentPushableTrigger {
                    IenAgreement(
                        variant = IenAgreementVariant.Large,
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
                        variant = IenAgreementVariant.Small,
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
                        variant = IenAgreementVariant.Small,
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

            IenText(text = "5. 기존 리스트형 어댑터 동의 (하위 호환용)", style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
            
            IenAgreement(
                items = agreements,
                onItemCheckedChange = { id, checked ->
                    agreements = agreements.map { if (it.id == id) it.copy(checked = checked) else it }
                }
            )
        }
    }
}

@Preview
@Composable
fun AssetSection() {
    IenTheme {
        ComponentSection(title = "Asset") {
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                IenAssetFrame(size = IenAssetFrameSize.Large, tone = IenSemanticTone.Brand, contentDescription = "카드 이모지") {
                    IenText("💳", style = IenTheme.typography.title2)
                }
                IenAssetFrame(
                    size = IenAssetFrameSize.Large,
                    shape = IenAssetFrameShape.Circle,
                    tone = IenSemanticTone.Success,
                    bordered = true,
                    contentDescription = "확인 아이콘",
                ) {
                    IenIcon(
                        imageVector = M3SystemIcons.Filled.Check,
                        contentDescription = null,
                        size = IenTheme.icon.lg,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomCTASection() {
    IenTheme {
        var showAnimatedCTA by remember { mutableStateOf(true) }
        ComponentSection(title = "BottomCTA") {
            IenBottomCTA(
                text = "단일 CTA",
                onClick = {},
                topAccessory = {
                    IenText(
                        text = "상단 액세서리: 결제 전 안내 문구",
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textSecondary,
                    )
                },
                bottomAccessory = {
                    IenText(
                        text = "하단 액세서리: 약관 및 수수료 안내",
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textTertiary,
                    )
                },
            )
            IenBottomCTA(
                text = "배경 없는 CTA",
                onClick = {},
                background = IenBottomCTABackground.None,
                hasSafeAreaPadding = false,
                hasPaddingBottom = false,
                variant = IenButtonVariant.Weak,
            )
            IenButton(
                text = if (showAnimatedCTA) "애니메이션 CTA 숨기기" else "애니메이션 CTA 보이기",
                onClick = { showAnimatedCTA = !showAnimatedCTA },
                size = IenButtonSize.Small,
                variant = IenButtonVariant.Weak,
            )
            IenBottomCTA(
                text = "지연 등장 CTA",
                onClick = {},
                show = showAnimatedCTA,
                showAfterDelay = IenBottomCTAShowAfterDelay(
                    animation = IenBottomCTAAnimation.Scale,
                    delayMillis = 300,
                ),
                hideOnScroll = true,
                scrollDelta = if (showAnimatedCTA) 0f else 4f,
            )
            IenDoubleBottomCTA(
                primaryText = "확인",
                onPrimaryClick = {},
                secondaryText = "취소",
                onSecondaryClick = {},
                topAccessory = {
                    IenText(
                        text = "Double은 좌우 버튼 슬롯을 함께 제공합니다.",
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textSecondary,
                    )
                },
            )
            IenDoubleBottomCTA(
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
                    .background(IenTheme.colors.surfaceWeak),
            ) {
                IenText(
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
                    topAccessory = {
                        IenText(
                            text = "fixedAboveKeyboard=true",
                            style = IenTheme.typography.caption,
                            color = IenTheme.colors.textSecondary,
                        )
                    },
                    fixedAboveKeyboard = true,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
                    .background(IenTheme.colors.surfaceWeak),
            ) {
                IenText(
                    text = "FixedBottomCTA.Double",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(IenTheme.spacing.md),
                    style = IenTheme.typography.caption,
                    color = IenTheme.colors.textSecondary,
                )
                IenFixedDoubleBottomCTA(
                    hideOnScroll = true,
                    scrollDelta = 0f,
                    leftButton = {
                        IenBottomCTAButton(
                            text = "취소",
                            onClick = {},
                            variant = IenButtonVariant.Weak,
                            tone = IenSemanticTone.Neutral,
                        )
                    },
                    rightButton = {
                        IenBottomCTAButton(
                            text = "확인",
                            onClick = {},
                        )
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun DialogSection() {
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
                text = "AlertDialog 기본",
                onClick = { showAlert = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Weak,
            )
            IenButton(
                text = "AlertDialog 딤 클릭 방지",
                onClick = { showAlertWiggle = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Weak,
            )
            IenButton(
                text = "AlertDialog 긴 콘텐츠",
                onClick = { showAlertLong = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
            )
            IenButton(
                text = "ConfirmDialog 기본",
                onClick = { showConfirm = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
                tone = IenSemanticTone.Danger,
            )
            IenButton(
                text = "ConfirmDialog 긴 버튼",
                onClick = { showConfirmLong = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
            )
            IenButton(
                text = "ConfirmDialog 설명 없음",
                onClick = { showConfirmNoDescription = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Ghost,
            )
            IenButton(
                text = "기본 Dialog 열기",
                onClick = { showGenericDialog = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Ghost,
            )
            IenText(
                text = "M3AlertDialog 호환 API",
                style = IenTheme.typography.label1,
                color = IenTheme.colors.textSecondary,
            )
            IenButton(
                text = "M3 1버튼 기본",
                onClick = { showM3OneButton = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Weak,
            )
            IenButton(
                text = "M3 1버튼 destructive",
                onClick = { showM3OneButtonDestructive = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Weak,
                tone = IenSemanticTone.Danger,
            )
            IenButton(
                text = "M3 2버튼 Horizontal",
                onClick = { showM3TwoButtonHorizontal = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
            )
            IenButton(
                text = "M3 2버튼 Vertical destructive",
                onClick = { showM3TwoButtonVerticalDestructive = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
                tone = IenSemanticTone.Danger,
            )
            IenButton(
                text = "M3 3버튼 Horizontal",
                onClick = { showM3ThreeButtonHorizontal = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Ghost,
            )
            IenButton(
                text = "M3 3버튼 Vertical destructive",
                onClick = { showM3ThreeButtonVerticalDestructive = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Ghost,
                tone = IenSemanticTone.Danger,
            )
            IenText(
                text = "이벤트: $dialogEventText",
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textTertiary,
            )
        }

        IenAlertDialog(
            visible = showAlert,
            title = "김토스님의 의견이\n잘 전달되었어요",
            message = "소중한 의견을 바탕으로 더 간편한 서비스를 만들게요.",
            onDismissRequest = { showAlert = false },
            onConfirmClick = { showAlert = false },
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
            onDismissRequest = { showConfirm = false },
            onConfirmClick = { showConfirm = false },
            destructive = true,
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
                text = "확인",
                onClick = { showGenericDialog = false },
            ),
            dismiss = IenDialogAction(
                text = "취소",
                onClick = { showGenericDialog = false },
            ),
        )

        M3AlertDialog(
            visible = showM3OneButton,
            title = "1버튼 알림",
            message = "기존 M3AlertDialog 단일 버튼 API가 IEN AlertDialog 디자인으로 표시됩니다.",
            textDismiss = "확인",
            onDismiss = { showM3OneButton = false },
            tone = IenSemanticTone.Brand,
        )

        M3AlertDialog(
            visible = showM3OneButtonDestructive,
            title = "위험 알림",
            message = "isDestructive를 켜면 아이콘과 액션 톤이 Danger로 표시됩니다.",
            textDismiss = "삭제 확인",
            onDismiss = { showM3OneButtonDestructive = false },
            isDestructive = true,
        )

        M3AlertDialog(
            visible = showM3TwoButtonHorizontal,
            title = "2버튼 가로 배치",
            message = "buttonLayout 기본값은 Horizontal입니다.",
            textDismiss = "취소",
            onDismiss = { showM3TwoButtonHorizontal = false },
            textConfirm = "확인",
            onConfirm = { showM3TwoButtonHorizontal = false },
            buttonLayout = IenDialogButtonLayout.Horizontal,
        )

        M3AlertDialog(
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

        M3AlertDialog(
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

        M3AlertDialog(
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
fun KeypadSection() {
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
            IenText("Alphabet Keypad: $alphabetValue", style = IenTheme.typography.body2)
            IenAlphabetKeypad(
                onKeyClick = { alphabetValue += it },
                onBackspaceClick = { alphabetValue = alphabetValue.dropLast(1) },
            )
            IenDivider()
            IenText("커스텀 배열: $customAlphabetValue", style = IenTheme.typography.body2)
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
            )
            IenDivider()
            IenText("보안 알파벳 키보드: $alphabetValue", style = IenTheme.typography.body2)
            IenAlphabetKeyboard(
                onAction = { action ->
                    alphabetValue = applyKeyboardAction(alphabetValue, action)
                },
            )
            IenDivider()
            IenText("Number Keypad: $numberValue", style = IenTheme.typography.body2)
            IenNumberKeypad(
                onKeyClick = { numberValue += it },
                onBackspaceClick = { numberValue = numberValue.dropLast(1) },
            )
            IenDivider()
            IenText("커스텀 숫자 배열: $customNumberValue", style = IenTheme.typography.body2)
            IenNumberKeypad(
                numbers = listOf(1, 3, 5, 7, 9, 2, 4, 6, 8, 0),
                onKeyClick = { customNumberValue += it },
                onBackspaceClick = { customNumberValue = customNumberValue.dropLast(1) },
            )
            IenDivider()
            IenText(
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
            )
            IenDivider()
            IenText("Full Secure Keypad: $fullSecureValue", style = IenTheme.typography.body2)
            IenFullSecureKeypad(
                state = fullSecureKeypadState,
                onKeyClick = { fullSecureValue += it },
                onBackspaceClick = { fullSecureValue = fullSecureValue.dropLast(1) },
                onSpaceClick = { fullSecureValue += " " },
                onSubmit = { fullSecureKeypadState.reorderEmptyCells() },
                submitButtonText = "공백 옮기기",
                submitDisabled = fullSecureValue.isEmpty(),
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
            )
        }
    }
}

@Preview
@Composable
fun ListRowSection() {
    IenTheme {
        ComponentSection(title = "ListRow") {
            IenListRow(
                title = "토스페이 결제",
                subtitle = "오늘 12:30",
                trailing = { IenText("28,000원", style = IenTheme.typography.label1) },
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
                        top = "ListRow.Texts",
                        bottom = "left / contents / right 슬롯 구성",
                    )
                },
                right = {
                    IenButton(
                        text = "Button",
                        onClick = {},
                        size = IenButtonSize.Small,
                        variant = IenButtonVariant.Weak,
                    )
                },
                withArrow = true,
                withTouchEffect = true,
                onClick = {},
            )
            IenListRow(
                contents = {
                    IenListRowTexts(
                        type = IenListRowTextsType.ThreeRowTypeC,
                        top = "긴 정보가 들어가는 행",
                        middle = "중간 설명 텍스트",
                        bottom = "아래 보조 텍스트",
                    )
                },
                right = {
                    IenListRowTexts(
                        type = IenListRowTextsType.RightTwoRowTypeA,
                        top = "28,000원",
                        bottom = "오늘",
                    )
                },
                leftAlignment = IenListRowAlignment.Top,
                rightAlignment = IenListRowAlignment.Top,
                verticalPadding = IenListRowPadding.Large,
            )
            IenListRow(
                contents = {
                    IenListRowTexts(
                        type = IenListRowTextsType.OneRowTypeA,
                        top = "비활성 Type2",
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
                        top = "작은 좌우 패딩과 border 없음",
                    )
                },
                horizontalPadding = IenListRowPadding.Small,
                border = IenListRowBorder.None,
                withArrow = true,
            )
            IenListRowLoader(type = IenListRowLoaderType.Circle, verticalPadding = IenListRowPadding.ExtraSmall)
            IenListRowLoader(type = IenListRowLoaderType.Bar)
        }
    }
}

@Preview
@Composable
fun TextFieldSection() {
    IenTheme {
        var text by remember { mutableStateOf("") }
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
                label = "이름",
                placeholder = "이름을 입력하세요",
                hasError = text.length >= 4,
                help = if (text.length >= 4) "이름은 3글자 이하로 입력해주세요." else "값이 들어오거나 포커스되면 라벨이 나타납니다.",
            )
            IenTextField(
                value = lineText,
                onValueChange = { lineText = it },
                label = "주소",
                labelOption = IenTextFieldLabelOption.Sustain,
                placeholder = "주소를 입력하세요",
                variant = IenTextFieldVariant.Line,
                suffix = "시",
                help = "line variant + sustain label",
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
                label = "Clearable",
                labelOption = IenTextFieldLabelOption.Sustain,
                placeholder = "입력 후 지울 수 있어요",
            )
            IenPasswordTextField(
                value = passwordText,
                onValueChange = { passwordText = it },
                label = "비밀번호",
                placeholder = "비밀번호 입력",
                help = "보기/숨김 토글을 제공합니다.",
            )
            IenTextFieldButton(
                value = selectedBank,
                onClick = { selectedBank = if (selectedBank == "은행 선택") "아이엔뱅크" else "은행 선택" },
                label = "계좌",
                labelOption = IenTextFieldLabelOption.Sustain,
                help = "읽기 전용 선택 필드입니다.",
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
fun SplitTextFieldSection() {
    IenTheme {
        var splitText by remember { mutableStateOf("") }
        ComponentSection(title = "SplitTextField") {
            IenSplitTextField(
                value = splitText,
                onValueChange = { splitText = it },
                length = 6,
            )
        }
    }
}

@Preview
@Composable
fun TextAreaSection() {
    IenTheme {
        var textArea by remember { mutableStateOf("") }
        ComponentSection(title = "TextArea") {
            IenTextArea(
                value = textArea,
                onValueChange = { textArea = it },
                label = "메모",
                placeholder = "여러 줄 텍스트를 입력하세요",
                supportingText = "TextArea는 TextField 토큰과 상태 모델을 공유합니다.",
            )
        }
    }
}

@Preview
@Composable
fun PrimitivesSection() {
    IenTheme {
        ComponentSection(title = "Primitives") {
            IenProvideTextStyle(
                style = IenTheme.typography.label1,
                color = IenTheme.colors.brand,
            ) {
                IenText("ProvideTextStyle 적용 텍스트", color = IenTheme.colors.brand)
            }
            IenBorderBox {
                IenText("BorderBox 프리미티브", color = IenTheme.colors.textSecondary)
            }
            IenClickable(onClick = {}) {
                IenSurface(color = IenTheme.colors.brandWeak) {
                    IenText(
                        text = "Clickable container",
                        modifier = Modifier.padding(IenTheme.spacing.md),
                        color = IenTheme.colors.brand,
                    )
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
    IenSurface(
        modifier = Modifier.fillMaxWidth(),
        color = IenTheme.colors.surface,
        tonalElevation = IenTheme.elevation.raised,
    ) {
        Column(
            modifier = Modifier.padding(IenTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
        ) {
            IenText(title, style = IenTheme.typography.title3)
            IenDivider()
            content()
        }
    }
}
