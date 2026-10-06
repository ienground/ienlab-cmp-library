package zone.ien.utils.docs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font
import zone.ien.utils.docs.generated.resources.Pretendard_Regular
import zone.ien.utils.docs.generated.resources.Res
import zone.ien.utils.example.ui.screens.designsystem.*
import zone.ien.utils.ui.feedback.IenSnackbarHost
import zone.ien.utils.ui.feedback.IenToastDuration
import zone.ien.utils.ui.feedback.IenToastProvider
import zone.ien.utils.ui.feedback.rememberIenToastState
import zone.ien.utils.ui.feedback.showIenSnackbar
import zone.ien.utils.ui.feedback.showIenToast
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenColorScheme
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.defaultIenTokens
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.utils.getIenTypography

@Composable
fun DocsApp(
    componentId: String,
    darkTheme: Boolean = false,
    colors: IenColorScheme? = null,
    onContentHeight: (Int) -> Unit = {},
) {
    val defaultTokens = defaultIenTokens()
    val docsTokens = defaultTokens.copy(
        lightColors = if (!darkTheme) colors ?: defaultTokens.lightColors else defaultTokens.lightColors,
        darkColors = if (darkTheme) colors ?: defaultTokens.darkColors else defaultTokens.darkColors,
        typography = getIenTypography(FontFamily(Font(Res.font.Pretendard_Regular))),
    )

    IenTheme(tokens = docsTokens, darkTheme = darkTheme) {
        val density = LocalDensity.current
        val snackbarHostState = remember { SnackbarHostState() }
        val toastState = rememberIenToastState()
        val coroutineScope = rememberCoroutineScope()

        IenToastProvider(state = toastState) {
            IenProvideTextStyle(
                style = IenTheme.typography.body2,
                color = IenTheme.colors.textPrimary,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(IenTheme.colors.background),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(unbounded = true)
                            .padding(24.dp)
                            .onSizeChanged { size ->
                                onContentHeight(with(density) { size.height.toDp().value.roundToInt() })
                            },
                    ) {
                        CompositionLocalProvider(LocalComponentSectionChrome provides false) {
                            ComponentPreview(
                                componentId = componentId,
                                snackbarHostState = snackbarHostState,
                                toastState = toastState,
                                coroutineScope = coroutineScope,
                            )
                        }
                    }
                    IenSnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ComponentPreview(
    componentId: String,
    snackbarHostState: SnackbarHostState,
    toastState: zone.ien.utils.ui.feedback.IenToastState,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
) {
    when (componentId) {
        "animated-layout" -> AnimatedLayoutSection()
        "animated-content" -> AnimatedContentSection()
        "badge" -> BadgeSection()
        "board-row" -> BoardRowSection()
        "border" -> BorderSection()
        "bottom-info" -> BottomInfoSection()
        "bottom-sheet" -> BottomSheetSection()
        "bubble" -> BubbleSection()
        "button" -> ButtonSection()
        "card" -> CardSection()
        "chip" -> ChipSection()
        "fab" -> FabSection()
        "checkbox" -> CheckboxSection()
        "highlight" -> HighlightSection()
        "icon-button" -> IconButtonSection()
        "list-footer" -> ListFooterSection()
        "list-header" -> ListHeaderSection()
        "loader" -> LoaderSection()
        "menu" -> MenuSection()
        "modal" -> ModalSection()
        "numeric-spinner" -> NumericSpinnerSection()
        "paragraph" -> ParagraphSection()
        "post" -> PostSection()
        "progress-bar" -> ProgressBarSection()
        "progress-stepper" -> ProgressStepperSection()
        "rating" -> RatingSection()
        "result" -> ResultSection()
        "search-field" -> SearchFieldSection()
        "segmented-control" -> SegmentedControlSection()
        "skeleton" -> SkeletonSection()
        "slider" -> SliderSection()
        "wheel-picker" -> WheelPickerSection()
        "swipe-box" -> SwipeBoxSection()
        "stepper" -> StepperSection()
        "switch" -> SwitchSection()
        "tab" -> TabSection()
        "table-row" -> TableRowSection()
        "text-button" -> TextButtonSection()
        "snackbar" -> SnackbarSection(
            onShowBasic = {
                coroutineScope.launch {
                    snackbarHostState.showIenSnackbar("기본 스낵바 메시지예요")
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
                        message = "버튼이 포함된 스낵바예요",
                        actionLabel = "확인",
                        duration = SnackbarDuration.Long,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        snackbarHostState.showIenSnackbar("확인을 눌렀어요")
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
            onShowBasic = { toastState.showIenToast("기본 토스트 메시지예요") },
            onShowSuccess = {
                toastState.showIenToast(
                    message = "성공 상태 토스트예요",
                    tone = IenSemanticTone.Success,
                )
            },
            onShowLong = {
                toastState.showIenToast(
                    message = "오래 표시되는 토스트예요",
                    duration = IenToastDuration.Long,
                )
            },
            onDismiss = toastState::dismiss,
        )
        "tooltip" -> TooltipSection()
        "top" -> TopSection()
        "agreement" -> AgreementSection()
        "asset" -> AssetSection()
        "bottom-bar" -> BottomBarSection()
        "chat-bottom-bar" -> ChatBottomBarSection()
        "navigation-bar" -> NavigationBarSection()
        "bottom-cta" -> BottomCTASection()
        "dialog" -> DialogSection()
        "alert-dialog" -> DialogSection()
        "keypad" -> KeypadSection()
        "list-row" -> ListRowSection()
        "text-field" -> TextFieldSection()
        "split-text-field" -> SplitTextFieldSection()
        "text-area" -> TextAreaSection()
        "primitives" -> PrimitivesSection()
        else -> Text("선택한 미리보기를 찾을 수 없습니다.")
    }
}
