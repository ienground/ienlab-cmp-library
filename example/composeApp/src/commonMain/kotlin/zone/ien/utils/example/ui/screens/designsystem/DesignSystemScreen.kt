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
import zone.ien.utils.ui.components.composite.IenAgreementItemV4
import zone.ien.utils.ui.components.composite.IenAgreementV4
import zone.ien.utils.ui.components.composite.IenAlertDialog
import zone.ien.utils.ui.components.composite.IenAssetFrame
import zone.ien.utils.ui.components.composite.IenAssetFrameShape
import zone.ien.utils.ui.components.composite.IenAssetFrameSize
import zone.ien.utils.ui.components.composite.IenBoardRow
import zone.ien.utils.ui.components.composite.IenBorder
import zone.ien.utils.ui.components.composite.IenBorderVariant
import zone.ien.utils.ui.components.composite.IenBottomCTA
import zone.ien.utils.ui.components.composite.IenBottomInfo
import zone.ien.utils.ui.components.composite.IenBottomSheet
import zone.ien.utils.ui.components.composite.IenBottomSheetOption
import zone.ien.utils.ui.components.composite.IenBottomSheetSelect
import zone.ien.utils.ui.components.composite.IenBubble
import zone.ien.utils.ui.components.composite.IenBubbleBackground
import zone.ien.utils.ui.components.composite.IenConfirmDialog
import zone.ien.utils.ui.components.composite.IenDialog
import zone.ien.utils.ui.components.composite.IenDialogAction
import zone.ien.utils.ui.components.composite.IenDoubleBottomCTA
import zone.ien.utils.ui.components.composite.IenFixedBottomCTA
import zone.ien.utils.ui.components.composite.IenHighlightText
import zone.ien.utils.ui.components.composite.IenListFooter
import zone.ien.utils.ui.components.composite.IenListFooterBorder
import zone.ien.utils.ui.components.composite.IenListFooterDefaults
import zone.ien.utils.ui.components.composite.IenListHeader
import zone.ien.utils.ui.components.composite.IenListHeaderDescriptionPosition
import zone.ien.utils.ui.components.composite.IenListRow
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
import zone.ien.utils.ui.components.composite.IenSkeletonPattern
import zone.ien.utils.ui.components.composite.IenSkeletonRepeat
import zone.ien.utils.ui.components.composite.IenTableRow
import zone.ien.utils.ui.components.composite.IenToast
import zone.ien.utils.ui.components.composite.IenToastHost
import zone.ien.utils.ui.components.composite.IenTooltip
import zone.ien.utils.ui.components.composite.IenTop
import zone.ien.utils.ui.components.composite.IenTopBar
import zone.ien.utils.ui.components.composite.rememberIenBottomSheetState
import zone.ien.utils.ui.components.composite.rememberIenToastHostState
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.IenAlphabetKeyboard
import zone.ien.utils.ui.components.interactive.IenBadge
import zone.ien.utils.ui.components.interactive.IenBadgeSize
import zone.ien.utils.ui.components.interactive.IenBadgeVariant
import zone.ien.utils.ui.components.interactive.IenButton
import zone.ien.utils.ui.components.interactive.IenButtonDisplay
import zone.ien.utils.ui.components.interactive.IenButtonSize
import zone.ien.utils.ui.components.interactive.IenButtonState
import zone.ien.utils.ui.components.interactive.IenButtonVariant
import zone.ien.utils.ui.components.interactive.IenCircleCheckbox
import zone.ien.utils.ui.components.interactive.IenFieldStatus
import zone.ien.utils.ui.components.interactive.IenFullSecureKeyboard
import zone.ien.utils.ui.components.interactive.IenIconButton
import zone.ien.utils.ui.components.interactive.IenKeyboardAction
import zone.ien.utils.ui.components.interactive.IenLineCheckbox
import zone.ien.utils.ui.components.interactive.IenNumberKeypad
import zone.ien.utils.ui.components.interactive.IenNumericSpinner
import zone.ien.utils.ui.components.interactive.IenNumericSpinnerSize
import zone.ien.utils.ui.components.interactive.IenRating
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
import zone.ien.utils.ui.components.interactive.IenStepperNumberIcon
import zone.ien.utils.ui.components.interactive.IenStepperRightArrow
import zone.ien.utils.ui.components.interactive.IenStepperRightButton
import zone.ien.utils.ui.components.interactive.IenStepperTexts
import zone.ien.utils.ui.components.interactive.IenStepperTextsType
import zone.ien.utils.ui.components.interactive.IenSwitch
import zone.ien.utils.ui.components.interactive.IenTab
import zone.ien.utils.ui.components.interactive.IenTabItem
import zone.ien.utils.ui.components.interactive.IenTextArea
import zone.ien.utils.ui.components.interactive.IenTextButton
import zone.ien.utils.ui.components.interactive.IenTextField
import zone.ien.utils.ui.components.interactive.IenTextFieldState
import zone.ien.utils.ui.components.primitives.IenBorderBox
import zone.ien.utils.ui.components.primitives.IenClickable
import zone.ien.utils.ui.components.primitives.IenDivider
import zone.ien.utils.ui.components.primitives.IenIcon
import zone.ien.utils.ui.components.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.components.primitives.IenProvideTextStyle
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

@Preview
@Composable
fun DesignSystemScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    navigateToColor: () -> Unit = {}
) {
    IenTheme {
        IenScaffold(
            modifier = modifier,
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                ToastSection()
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
        var ratingValue by remember { mutableStateOf(3f) }
        ComponentSection(title = "Rating") {
            IenRating(
                value = ratingValue,
                onValueChange = { ratingValue = it },
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
        ComponentSection(title = "SearchField") {
            IenSearchField(
                value = search,
                onValueChange = { search = it },
                placeholder = "컴포넌트 검색",
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
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm)) {
                IenSkeleton(modifier = Modifier.weight(1f), height = 20.dp)
                IenSkeleton(modifier = Modifier.weight(0.65f), height = 20.dp)
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
                            backgroundColor = IenTheme.colors.brandWeak,
                        ) {
                            IenText("✓", color = IenTheme.colors.brand, style = IenTheme.typography.label2)
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
        ComponentSection(title = "Tab") {
            IenTab(
                items = listOf(IenTabItem("요약"), IenTabItem("상세"), IenTabItem("내역")),
                selectedIndex = tabSelected,
                onSelectedIndexChange = { tabSelected = it },
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
            Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                IenTextButton(text = "텍스트 버튼", onClick = {})
                IenTextButton(text = "닫기", onClick = {})
            }
        }
    }
}

@Preview
@Composable
fun ToastSection() {
    IenTheme {
        val toastHostState = rememberIenToastHostState()
        ComponentSection(title = "Toast") {
            IenToast(message = "변경사항이 저장되었습니다.", tone = IenSemanticTone.Success)
            IenButton(
                text = "토스트 호스트에 추가",
                onClick = { toastHostState.show("호스트 토스트입니다.", IenSemanticTone.Info) },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Weak,
            )
        }
        IenToastHost(state = toastHostState)
    }
}

@Preview
@Composable
fun TooltipSection() {
    IenTheme {
        ComponentSection(title = "Tooltip") {
            IenTooltip(
                text = "툴팁은 짧은 보조 설명에 사용합니다.",
                anchor = { IenBadge("도움말", variant = IenBadgeVariant.Line) },
            )
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
                    IenAgreementItemV4(id = "service", title = "서비스 이용약관", checked = true, required = true),
                    IenAgreementItemV4(id = "privacy", title = "개인정보 처리방침", checked = false, required = true),
                    IenAgreementItemV4(id = "marketing", title = "마케팅 정보 수신", checked = false, required = false, description = "혜택과 이벤트 소식을 받을 수 있습니다."),
                ),
            )
        }
        ComponentSection(title = "Agreement") {
            IenAgreementV4(
                items = agreements,
                onItemCheckedChange = { id, checked ->
                    agreements = agreements.map { if (it.id == id) it.copy(checked = checked) else it }
                },
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
        ComponentSection(title = "BottomCTA") {
            IenBottomCTA(text = "단일 CTA", onClick = {})
            IenDoubleBottomCTA(
                primaryText = "확인",
                onPrimaryClick = {},
                secondaryText = "취소",
                onSecondaryClick = {},
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
            ) {
                IenFixedBottomCTA(text = "고정 CTA", onClick = {})
            }
        }
    }
}

@Preview
@Composable
fun DialogSection() {
    IenTheme {
        var showAlert by remember { mutableStateOf(false) }
        var showConfirm by remember { mutableStateOf(false) }
        var showGenericDialog by remember { mutableStateOf(false) }

        ComponentSection(title = "Dialog") {
            IenButton(
                text = "알림 열기",
                onClick = { showAlert = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Weak,
            )
            IenButton(
                text = "확인 다이얼로그 열기",
                onClick = { showConfirm = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Line,
                tone = IenSemanticTone.Danger,
            )
            IenButton(
                text = "기본 Dialog 열기",
                onClick = { showGenericDialog = true },
                display = IenButtonDisplay.Block,
                variant = IenButtonVariant.Ghost,
            )
        }

        IenAlertDialog(
            visible = showAlert,
            title = "알림",
            message = "AlertDialog는 단일 확인 액션이 필요한 정보 전달에 사용합니다.",
            onDismissRequest = { showAlert = false },
            onConfirmClick = { showAlert = false },
        )

        IenConfirmDialog(
            visible = showConfirm,
            title = "삭제할까요?",
            message = "ConfirmDialog는 사용자의 명시적인 결정을 받아야 하는 상황에 사용합니다.",
            onDismissRequest = { showConfirm = false },
            onConfirmClick = { showConfirm = false },
            destructive = true,
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
    }
}

@Preview
@Composable
fun KeypadSection() {
    IenTheme {
        var alphabetValue by remember { mutableStateOf("") }
        var numberValue by remember { mutableStateOf("") }
        var secureValue by remember { mutableStateOf("") }
        var secureLanguage by remember { mutableStateOf(IenSecureKeyboardLanguage.English) }

        ComponentSection(title = "Keypad") {
            IenText("알파벳 입력: $alphabetValue", style = IenTheme.typography.body2)
            IenAlphabetKeyboard(
                onAction = { action ->
                    alphabetValue = applyKeyboardAction(alphabetValue, action)
                },
            )
            IenDivider()
            IenText("숫자 입력: $numberValue", style = IenTheme.typography.body2)
            IenNumberKeypad(
                onAction = { action ->
                    numberValue = applyKeyboardAction(numberValue, action)
                },
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
        }
    }
}

@Preview
@Composable
fun TextFieldSection() {
    IenTheme {
        var text by remember { mutableStateOf("") }
        ComponentSection(title = "TextField") {
            IenTextField(
                value = text,
                onValueChange = { text = it },
                label = "이름",
                placeholder = "이름을 입력하세요",
                supportingText = "공통 토큰을 사용하는 입력창입니다.",
            )
            IenTextField(
                value = "",
                onValueChange = {},
                label = "오류 상태",
                placeholder = "필수 값",
                state = IenTextFieldState(status = IenFieldStatus.Error("값을 입력해 주세요.")),
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
