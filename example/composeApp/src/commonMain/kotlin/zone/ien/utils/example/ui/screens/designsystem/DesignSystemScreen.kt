package zone.ien.utils.example.ui.screens.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.composite.IenAssetFrame
import zone.ien.utils.ui.components.composite.IenAssetFrameShape
import zone.ien.utils.ui.components.composite.IenAssetFrameSize
import zone.ien.utils.ui.components.composite.IenBoardRow
import zone.ien.utils.ui.components.composite.IenBorder
import zone.ien.utils.ui.components.composite.IenBorderSide
import zone.ien.utils.ui.components.composite.IenBorderSpec
import zone.ien.utils.ui.components.composite.IenAgreementItemV4
import zone.ien.utils.ui.components.composite.IenAgreementV4
import zone.ien.utils.ui.components.composite.IenAlertDialog
import zone.ien.utils.ui.components.composite.IenBottomCTA
import zone.ien.utils.ui.components.composite.IenBottomInfo
import zone.ien.utils.ui.components.composite.IenBottomSheet
import zone.ien.utils.ui.components.composite.IenBubble
import zone.ien.utils.ui.components.composite.IenBubbleTail
import zone.ien.utils.ui.components.composite.IenConfirmDialog
import zone.ien.utils.ui.components.composite.IenDialog
import zone.ien.utils.ui.components.composite.IenDialogAction
import zone.ien.utils.ui.components.composite.IenDoubleBottomCTA
import zone.ien.utils.ui.components.composite.IenFixedBottomCTA
import zone.ien.utils.ui.components.composite.IenGridList
import zone.ien.utils.ui.components.composite.IenHighlightText
import zone.ien.utils.ui.components.composite.IenListFooter
import zone.ien.utils.ui.components.composite.IenListHeader
import zone.ien.utils.ui.components.composite.IenListRow
import zone.ien.utils.ui.components.composite.IenLoader
import zone.ien.utils.ui.components.composite.IenMenu
import zone.ien.utils.ui.components.composite.IenMenuItem
import zone.ien.utils.ui.components.composite.IenModal
import zone.ien.utils.ui.components.composite.IenParagraph
import zone.ien.utils.ui.components.composite.IenPost
import zone.ien.utils.ui.components.composite.IenProgressBar
import zone.ien.utils.ui.components.composite.IenProgressStep
import zone.ien.utils.ui.components.composite.IenProgressStepper
import zone.ien.utils.ui.components.composite.IenResult
import zone.ien.utils.ui.components.composite.IenResultTone
import zone.ien.utils.ui.components.composite.IenScaffold
import zone.ien.utils.ui.components.composite.IenSheetDetent
import zone.ien.utils.ui.components.composite.IenSkeleton
import zone.ien.utils.ui.components.composite.IenStepStatus
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
import zone.ien.utils.ui.components.interactive.IenBadge
import zone.ien.utils.ui.components.interactive.IenBadgeSize
import zone.ien.utils.ui.components.interactive.IenBadgeVariant
import zone.ien.utils.ui.components.interactive.IenButton
import zone.ien.utils.ui.components.interactive.IenButtonSize
import zone.ien.utils.ui.components.interactive.IenButtonState
import zone.ien.utils.ui.components.interactive.IenButtonVariant
import zone.ien.utils.ui.components.interactive.IenCheckbox
import zone.ien.utils.ui.components.interactive.IenIconButton
import zone.ien.utils.ui.components.interactive.IenAlphabetKeyboard
import zone.ien.utils.ui.components.interactive.IenFullSecureKeyboard
import zone.ien.utils.ui.components.interactive.IenKeyboardAction
import zone.ien.utils.ui.components.interactive.IenNumberKeypad
import zone.ien.utils.ui.components.interactive.IenNumericSpinner
import zone.ien.utils.ui.components.interactive.IenNumericSpinnerRange
import zone.ien.utils.ui.components.interactive.IenRating
import zone.ien.utils.ui.components.interactive.IenSearchField
import zone.ien.utils.ui.components.interactive.IenSecureKeyboardLanguage
import zone.ien.utils.ui.components.interactive.IenSecureKeyboardState
import zone.ien.utils.ui.components.interactive.IenSegmentedControl
import zone.ien.utils.ui.components.interactive.IenSlider
import zone.ien.utils.ui.components.interactive.IenSplitTextField
import zone.ien.utils.ui.components.interactive.IenStepper
import zone.ien.utils.ui.components.interactive.IenStepperRange
import zone.ien.utils.ui.components.interactive.IenSwitch
import zone.ien.utils.ui.components.interactive.IenTab
import zone.ien.utils.ui.components.interactive.IenTabItem
import zone.ien.utils.ui.components.interactive.IenTextArea
import zone.ien.utils.ui.components.interactive.IenTextButton
import zone.ien.utils.ui.components.interactive.IenTextField
import zone.ien.utils.ui.components.interactive.IenFieldStatus
import zone.ien.utils.ui.components.interactive.IenTextFieldState
import zone.ien.utils.ui.components.primitives.IenBorderBox
import zone.ien.utils.ui.components.primitives.IenClickable
import zone.ien.utils.ui.components.primitives.IenDivider
import zone.ien.utils.ui.components.primitives.IenIcon
import zone.ien.utils.ui.components.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.components.primitives.IenProvideTextStyle
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.material.filled.Check

@Preview
@Composable
fun DesignSystemScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
) {
    IenTheme {
        val sheetState = rememberIenBottomSheetState()
        val toastHostState = rememberIenToastHostState()
        var showModal by remember { mutableStateOf(false) }
        var showAlert by remember { mutableStateOf(false) }
        var showConfirm by remember { mutableStateOf(false) }
        var showGenericDialog by remember { mutableStateOf(false) }
        IenScaffold(
            modifier = modifier,
            topBar = {
                IenTopBar(
                    title = "Ien CMP UI",
                    subtitle = "토큰 기반 모바일 디자인 시스템",
                    navigationIcon = { IenTextButton(text = "닫기", onClick = navigateBack) },
                    actions = { IenBadge("샘플", size = IenBadgeSize.Small) },
                )
            },
            bottomBar = {
                IenBottomCTA(text = "샘플 하단 CTA", onClick = {})
            },
            floating = {
                IenBottomSheet(
                    state = sheetState,
                    title = { IenText("바텀시트", style = IenTheme.typography.title3) },
                    actions = {
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
                    },
                ) {
                    IenText(
                        text = "공통 API는 유지하면서 Android와 iOS의 시트 감각 차이는 내부 구현에서 흡수합니다.",
                        color = IenTheme.colors.textSecondary,
                    )
                    IenBottomInfo(
                        text = "스크림을 누르면 닫히도록 설정되어 있습니다.",
                        tone = IenSemanticTone.Brand,
                    )
                }

                IenModal(
                    visible = showModal,
                    onDismissRequest = { showModal = false },
                    title = "모달",
                    description = "Modal은 화면 맥락을 잠시 멈추고 중요한 선택이나 정보를 전달합니다.",
                    primaryActionText = "확인",
                    onPrimaryActionClick = { showModal = false },
                    secondaryActionText = "취소",
                    onSecondaryActionClick = { showModal = false },
                ) {
                    IenBottomInfo(
                        text = "Dialog보다 자유로운 콘텐츠 슬롯을 가진 오버레이입니다.",
                        tone = IenSemanticTone.Info,
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

                IenToastHost(state = toastHostState)
            },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding)
                ,
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            ) {
                ComponentSection(title = "Badge") {
                    Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                        IenBadge("NEW", variant = IenBadgeVariant.Fill)
                        IenBadge("주의", tone = IenSemanticTone.Warning)
                        IenBadge("오류", size = IenBadgeSize.Large, variant = IenBadgeVariant.Line, tone = IenSemanticTone.Danger)
                    }
                }

                ComponentSection(title = "BoardRow") {
                    IenBoardRow(
                        title = "배송 정보 자세히 보기",
                        prefix = { IenBadge("배송", size = IenBadgeSize.Small) },
                        icon = { opened -> IenBadge(if (opened) "열림" else "닫힘", size = IenBadgeSize.Small) },
                    ) {
                        IenText("제한된 영역에서 상세 정보를 접고 펼치는 아코디언형 정보 구조입니다.", color = IenTheme.colors.textSecondary)
                    }
                }

                ComponentSection(title = "Border") {
                    IenBorder(
                        spec = IenBorderSpec(side = IenBorderSide.All),
                    ) {
                        IenText("Border는 테두리 방향과 굵기를 토큰으로 제어합니다.", color = IenTheme.colors.textSecondary)
                    }
                    IenBorder(
                        spec = IenBorderSpec(side = IenBorderSide.Start, color = IenTheme.colors.brand, width = IenTheme.stroke.thick),
                    ) {
                        IenText("Start border", style = IenTheme.typography.label1)
                    }
                }

                ComponentSection(title = "BottomInfo") {
                    IenBottomInfo(
                        text = "하단 안내는 결제, 확인, 폼 화면에서 보조 정보를 안정적으로 보여줍니다.",
                        tone = IenSemanticTone.Info,
                    )
                }

                ComponentSection(title = "BottomSheet") {
                    IenButton(
                        text = "바텀시트 열기",
                        onClick = { sheetState.show(IenSheetDetent.Content) },
                        fullWidth = true,
                    )
                }

                ComponentSection(title = "Bubble") {
                    IenBubble(
                        text = "Bubble은 짧은 안내와 말풍선형 피드백에 사용합니다.",
                        tone = IenSemanticTone.Brand,
                        tail = IenBubbleTail.Start,
                    )
                }

                ComponentSection(title = "Button") {
                    IenButton(text = "주요 액션", onClick = {}, fullWidth = true)
                    IenButton(text = "보조 액션", onClick = {}, fullWidth = true, variant = IenButtonVariant.Weak)
                    IenButton(text = "로딩", onClick = {}, size = IenButtonSize.Medium, state = IenButtonState(loading = true))
                }

                var checked by remember { mutableStateOf(true) }
                ComponentSection(title = "Checkbox") {
                    IenCheckbox(checked = checked, onCheckedChange = { checked = it }, label = "약관에 동의합니다")
                }

                ComponentSection(title = "GridList") {
                    IenGridList(
                        items = listOf("Badge", "Button", "TextField", "BoardRow", "Bubble", "BottomInfo"),
                        columns = 2,
                    ) { item, index ->
                        IenSurface(
                            color = if (index % 2 == 0) IenTheme.colors.surfaceWeak else IenTheme.colors.brandWeak,
                        ) {
                            IenText(
                                text = item,
                                modifier = Modifier.padding(IenTheme.spacing.md),
                                style = IenTheme.typography.label1,
                            )
                        }
                    }
                }

                ComponentSection(title = "Highlight") {
                    IenHighlightText(
                        text = "Highlight는 검색 결과나 본문 안의 중요한 텍스트를 토큰 색상으로 강조합니다.",
                        highlights = listOf("Highlight", "강조"),
                    )
                }

                ComponentSection(title = "IconButton") {
                    Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                        IenIconButton(onClick = {}, variant = IenButtonVariant.Weak) {
                            IenIcon(
                                imageVector = M3SystemIcons.Filled.Check,
                                contentDescription = "확인",
                            )
                        }
                        IenIconButton(onClick = {}, variant = IenButtonVariant.Line) {
                            IenText("✓", style = IenTheme.typography.title3)
                        }
                    }
                }

                ComponentSection(title = "ListFooter") {
                    IenListFooter(
                        text = "최근 3개월 내역만 표시됩니다.",
                        actionText = "도움말",
                        onActionClick = {},
                    )
                }

                ComponentSection(title = "ListHeader") {
                    IenListHeader(
                        title = "최근 거래",
                        description = "섹션 제목, 설명, 액션을 한 줄 구조로 정리합니다.",
                        actionText = "전체",
                        onActionClick = {},
                    )
                }

                ComponentSection(title = "Loader") {
                    IenLoader(label = "데이터를 불러오는 중")
                    IenLoaderPrimitive(color = IenTheme.colors.brand)
                }

                ComponentSection(title = "Menu") {
                    IenMenu(
                        items = listOf(
                            IenMenuItem(title = "수정", description = "현재 항목을 편집합니다.", onClick = {}),
                            IenMenuItem(title = "삭제", tone = IenSemanticTone.Danger, onClick = {}),
                        ),
                        header = {
                            IenText("작업 메뉴", style = IenTheme.typography.label1)
                        },
                    )
                }

                ComponentSection(title = "Modal") {
                    IenButton(
                        text = "모달 열기",
                        onClick = { showModal = true },
                        fullWidth = true,
                        variant = IenButtonVariant.Line,
                    )
                }

                var spinnerValue by remember { mutableIntStateOf(2) }
                ComponentSection(title = "NumericSpinner") {
                    IenNumericSpinner(
                        value = spinnerValue,
                        onValueChange = { spinnerValue = it },
                        range = IenNumericSpinnerRange(min = 1, max = 9),
                        label = "수량",
                    )
                }

                ComponentSection(title = "Paragraph") {
                    IenParagraph(
                        title = "문단 컴포넌트",
                        body = "Paragraph는 본문 타이포그래피와 Highlight를 함께 사용해 긴 설명을 안정적으로 표시합니다.",
                        emphasis = "Highlight",
                        footer = "토큰 기반 줄 높이와 색상을 사용합니다.",
                    )
                }

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

                ComponentSection(title = "ProgressBar") {
                    IenProgressBar(
                        progress = 0.64f,
                        showLabel = true,
                        contentDescription = "업로드 진행률",
                    )
                }

                ComponentSection(title = "ProgressStepper") {
                    IenProgressStepper(
                        steps = listOf(
                            IenProgressStep("입력", IenStepStatus.Done),
                            IenProgressStep("확인", IenStepStatus.Current),
                            IenProgressStep("완료", IenStepStatus.Pending),
                        ),
                    )
                }

                var ratingValue by remember { mutableStateOf(3f) }
                ComponentSection(title = "Rating") {
                    IenRating(
                        value = ratingValue,
                        onValueChange = { ratingValue = it },
                    )
                }

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

                var search by remember { mutableStateOf("") }
                ComponentSection(title = "SearchField") {
                    IenSearchField(
                        value = search,
                        onValueChange = { search = it },
                        placeholder = "컴포넌트 검색",
                    )
                }

                var selected by remember { mutableIntStateOf(0) }
                ComponentSection(title = "SegmentedControl") {
                    IenSegmentedControl(
                        items = listOf("전체", "진행", "완료"),
                        selectedIndex = selected,
                        onSelectedIndexChange = { selected = it },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                ComponentSection(title = "Skeleton") {
                    IenSkeleton(modifier = Modifier.fillMaxWidth(), height = 20.dp)
                    IenSkeleton(modifier = Modifier.fillMaxWidth(0.65f), height = 20.dp)
                }

                var sliderValue by remember { mutableStateOf(0.35f) }
                ComponentSection(title = "Slider") {
                    IenSlider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        label = "비율",
                        valueLabel = "${(sliderValue * 100).toInt()}%",
                    )
                }

                var stepperValue by remember { mutableIntStateOf(1) }
                ComponentSection(title = "Stepper") {
                    IenStepper(
                        value = stepperValue,
                        onValueChange = { stepperValue = it },
                        range = IenStepperRange(min = 0, max = 5),
                        label = "단계",
                    )
                }

                var switched by remember { mutableStateOf(true) }
                ComponentSection(title = "Switch") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IenText("자동 적용", modifier = Modifier.weight(1f))
                        IenSwitch(checked = switched, onCheckedChange = { switched = it })
                    }
                }

                var tabSelected by remember { mutableIntStateOf(0) }
                ComponentSection(title = "Tab") {
                    IenTab(
                        items = listOf(IenTabItem("요약"), IenTabItem("상세"), IenTabItem("내역")),
                        selectedIndex = tabSelected,
                        onSelectedIndexChange = { tabSelected = it },
                    )
                }

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

                ComponentSection(title = "TextButton") {
                    Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                        IenTextButton(text = "텍스트 버튼", onClick = {})
                        IenTextButton(text = "닫기", onClick = navigateBack)
                    }
                }

                ComponentSection(title = "Toast") {
                    IenToast(message = "변경사항이 저장되었습니다.", tone = IenSemanticTone.Success)
                    IenButton(
                        text = "토스트 호스트에 추가",
                        onClick = { toastHostState.show("호스트 토스트입니다.", IenSemanticTone.Info) },
                        fullWidth = true,
                        variant = IenButtonVariant.Weak,
                    )
                }

                ComponentSection(title = "Tooltip") {
                    IenTooltip(
                        text = "툴팁은 짧은 보조 설명에 사용합니다.",
                        anchor = { IenBadge("도움말", variant = IenBadgeVariant.Line) },
                    )
                }

                ComponentSection(title = "Top") {
                    IenTop(
                        title = "결제 확인",
                        subtitle = "Top은 화면 제목과 액션을 하나의 정보 구조로 묶습니다.",
                        navigation = { IenTextButton(text = "뒤로", onClick = {}) },
                        actions = { IenBadge("v4", size = IenBadgeSize.Small) },
                    )
                }

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

                ComponentSection(title = "Dialog") {
                    IenButton(
                        text = "알림 열기",
                        onClick = { showAlert = true },
                        fullWidth = true,
                        variant = IenButtonVariant.Weak,
                    )
                    IenButton(
                        text = "확인 다이얼로그 열기",
                        onClick = { showConfirm = true },
                        fullWidth = true,
                        variant = IenButtonVariant.Line,
                        tone = IenSemanticTone.Danger,
                    )
                    IenButton(
                        text = "기본 Dialog 열기",
                        onClick = { showGenericDialog = true },
                        fullWidth = true,
                        variant = IenButtonVariant.Ghost,
                    )
                }

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

                var splitText by remember { mutableStateOf("") }
                ComponentSection(title = "SplitTextField") {
                    IenSplitTextField(
                        value = splitText,
                        onValueChange = { splitText = it },
                        length = 6,
                    )
                }

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

                Spacer(Modifier.height(IenTheme.spacing.md))
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
