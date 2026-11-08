package zone.ien.utils.ui.feedback

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.drawPlainBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.selected
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.icon.tabler.line.Check
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.list.IenListRow
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.screen.IenBarPlacement
import zone.ien.utils.ui.screen.LocalIenBarPlacement
import zone.ien.utils.ui.screen.LocalIenSideBarEnabled
import zone.ien.utils.ui.screen.rememberIenBarPlacement
import zone.ien.utils.ui.window.disablePlatformDialogDim
import zone.ien.utils.ui.window.ienOverlayDialogProperties
import zone.ien.utils.ui.window.ienOverlayWindowSize
import zone.ien.utils.ui.window.rememberIenOverlayWindowSize
import kotlin.math.roundToInt

/**
 * 바텀시트가 펼쳐지는 높이의 단계를 정의하는 Enum 클래스입니다.
 *
 * - [Content]: 콘텐츠의 본래 높이만큼만 펼쳐집니다.
 * - [Medium]: 기본적으로 화면 높이의 50%를 사용하며, Duo에서는 356pt 높이로 하단에 배치합니다.
 * - [Full]: 기본적으로 화면 높이의 92%를 사용하며, Duo 내부 화면에서는 상단 여백을 제외하고, 외부 화면에서는 하단 여백을 유지하며 최대 92%까지 펼칩니다.
 * Duo 외부 화면의 가로 방향에서는 [Medium]도 [Full] 높이를 사용합니다.
 */
enum class IenSheetDetent { Content, Medium, Full }

internal fun ienSheetAvailableBounds(bounds: IntRect, division: IntRect?): IntRect {
    if (division == null || !bounds.overlaps(division)) return bounds
    return if (division.height >= division.width) {
        val left = bounds.copy(right = division.left.coerceIn(bounds.left, bounds.right))
        val right = bounds.copy(left = division.right.coerceIn(bounds.left, bounds.right))
        if (left.width >= right.width) left else right
    } else {
        val top = bounds.copy(bottom = division.top.coerceIn(bounds.top, bounds.bottom))
        val bottom = bounds.copy(top = division.bottom.coerceIn(bounds.top, bounds.bottom))
        if (bottom.height >= top.height) bottom else top
    }
}

internal fun ienDuoSheetBounds(
    windowSize: IntSize,
    innerDisplay: Boolean,
    expansionProgress: Float,
    density: Density,
    division: IntRect? = null,
    contentSized: Boolean = false,
): IntRect = with(density) {
    // docs/ios27.fig의 iPhone Duo Medium/Large Detent 프레임 기준입니다.
    val available = ienSheetAvailableBounds(
        IntRect(0, 0, windowSize.width, windowSize.height),
        division.takeIf { innerDisplay },
    )
    val edgeInset = 8.dp.roundToPx()
    val horizontalInset = edgeInset.coerceAtMost(available.width / 2)
    val width = (available.width - horizontalInset * 2)
        .coerceAtMost(if (innerDisplay) 653.dp.roundToPx() else windowSize.width)
    val left = available.left + (available.width - width) / 2
    val fullTop = if (innerDisplay) 82.dp.roundToPx() else
        available.top + (available.height * (1f - IenSheetFullHeightFraction)).roundToInt()
    val top = maxOf(available.top + edgeInset, fullTop)
        .coerceAtMost(available.bottom)
    val compactLandscape = !innerDisplay && windowSize.width > windowSize.height
    val progress = if (compactLandscape && !contentSized) 1f else expansionProgress.coerceIn(0f, 1f)
    val bottomInset = (edgeInset * if (innerDisplay) (1f - progress) else 1f).roundToInt()
        .coerceAtMost(available.bottom - top)
    val bottom = available.bottom - bottomInset
    val maxHeight = bottom - top
    val mediumHeight = 356.dp.roundToPx().coerceAtMost(maxHeight)
    val height = if (contentSized) maxHeight else
        (mediumHeight + (maxHeight - mediumHeight) * progress).roundToInt()
    IntRect(left, bottom - height, left + width, bottom)
}

private const val IenSheetMediumHeightFraction = 0.5f
private const val IenSheetFullHeightFraction = 0.92f
private const val IenSheetExpansionSnapFraction = 0.5f
private fun IenSheetDetent.isExpandable(): Boolean = this != IenSheetDetent.Content

private fun IenSheetDetent.expansionProgress(): Float =
    if (this == IenSheetDetent.Full) 1f else 0f

internal fun shouldKeepIenOverlayMounted(
    visible: Boolean,
    mounted: Boolean,
): Boolean = visible || mounted

/**
 * [IenBottomSheet]의 상태(표시 여부 및 높이 단계)를 제어하고 관리하는 상태 객체입니다.
 *
 * @property visible 바텀시트의 표시 여부
 * @property detent 바텀시트가 펼쳐진 높이 단계 ([IenSheetDetent])
 */
@Stable
class IenBottomSheetState internal constructor(
    visible: Boolean,
    detent: IenSheetDetent,
) {
    var visible by mutableStateOf(visible)
        private set
    var detent by mutableStateOf(detent)
        private set

    /**
     * 바텀시트를 지정된 높이 단계로 화면에 표시합니다.
     *
     * @param detent 바텀시트를 펼칠 높이 단계 ([IenSheetDetent])
     */
    fun show(detent: IenSheetDetent = IenSheetDetent.Content) {
        this.detent = detent
        visible = true
    }

    /**
     * 바텀시트를 화면에서 숨깁니다.
     */
    fun hide() {
        visible = false
    }
}

/**
 * [IenBottomSheetState] 인스턴스를 생성하고 기억(remember)하는 헬퍼 컴포저블 함수입니다.
 *
 * @param visible 초기 표시 여부
 * @param detent 초기 높이 단계 ([IenSheetDetent])
 * @return 생성된 [IenBottomSheetState] 객체
 */
@Composable
fun rememberIenBottomSheetState(
    visible: Boolean = false,
    detent: IenSheetDetent = IenSheetDetent.Content,
) = remember { IenBottomSheetState(visible, detent) }

/**
 * 다양한 모드와 슬라이드 드래그 제스처를 지원하는 바텀시트 컴포저블입니다.
 * Duo 외부 화면에서는 본문이 시스템 예약 영역을 피하고, 대응하는 CTA는 세로 바를 사용합니다.
 * 내부 화면에서는 가로 바를 유지하며 시트를 가로 중앙·하단에 배치하고 활성 힌지 영역을 피합니다.
 *
 * @param state 바텀시트의 상태 제어 객체 ([IenBottomSheetState])
 * @param modifier 바텀시트에 적용할 [Modifier]
 * @param dismissOnScrimClick 바깥 영역(스크림) 클릭 시 바텀시트를 닫을지 여부
 * @param showDragHandle 바텀시트 상단에 드래그 핸들 바를 표시할지 여부
 * @param disableDimmer 배경을 어둡게 처리(dimming)할지 여부
 * @param disableFocusLock 포커스 잠금(Focus Lock)을 해제할지 여부
 * @param header 바텀시트 상단에 들어갈 헤더 타이틀 컴포저블
 * @param headerDescription 헤더 아래에 들어갈 설명문 컴포저블
 * @param cta 바텀시트 하단에 고정되는 작업 버튼(Call To Action) 영역 컴포저블
 * @param contentPadding 콘텐츠 영역의 내부 여백 ([PaddingValues])
 * @param content 바텀시트의 내부 콘텐츠 영역을 정의하는 컴포저블 블록
 */
@Composable
fun IenBottomSheet(
    state: IenBottomSheetState,
    modifier: Modifier = Modifier,
    dismissOnScrimClick: Boolean = true,
    showDragHandle: Boolean = true,
    disableDimmer: Boolean = false,
    disableFocusLock: Boolean = false,
    header: (@Composable () -> Unit)? = null,
    headerDescription: (@Composable () -> Unit)? = null,
    cta: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = IenTheme.spacing.xl, vertical = IenTheme.spacing.xs),
    content: @Composable ColumnScope.() -> Unit,
) {
    IenBottomSheetInternal(
        state = state,
        modifier = modifier,
        dismissOnScrimClick = dismissOnScrimClick,
        showDragHandle = showDragHandle,
        disableDimmer = disableDimmer,
        disableFocusLock = disableFocusLock,
        header = header,
        headerDescription = headerDescription,
        cta = cta,
        contentPadding = contentPadding,
        backdrop = null,
        content = content,
    )
}

/**
 * 캡처된 배경을 바텀시트 표면에 흐림 효과로 표시합니다.
 * 캡처 원본은 바텀시트 바깥의 배경 콘텐츠에만 적용해야 합니다.
 * Duo 외부 화면에서는 본문이 시스템 예약 영역을 피하고, 대응하는 CTA는 세로 바를 사용합니다.
 * 내부 화면에서는 가로 바를 유지하며 시트를 가로 중앙·하단에 배치하고 활성 힌지 영역을 피합니다.
 *
 * @param state 바텀시트의 상태 제어 객체 ([IenBottomSheetState])
 * @param backdrop 바텀시트 뒤의 화면을 캡처한 배경 레이어. null이면 기본 표면을 사용합니다.
 * @param modifier 바텀시트에 적용할 [Modifier]
 * @param dismissOnScrimClick 바깥 영역(스크림) 클릭 시 바텀시트를 닫을지 여부
 * @param showDragHandle 바텀시트 상단에 드래그 핸들 바를 표시할지 여부
 * @param disableDimmer 배경을 어둡게 처리(dimming)할지 여부
 * @param disableFocusLock 포커스 잠금(Focus Lock)을 해제할지 여부
 * @param header 바텀시트 상단에 들어갈 헤더 타이틀 컴포저블
 * @param headerDescription 헤더 아래에 들어갈 설명문 컴포저블
 * @param cta 바텀시트 하단에 고정되는 작업 버튼(Call To Action) 영역 컴포저블
 * @param contentPadding 콘텐츠 영역의 내부 여백 ([PaddingValues])
 * @param content 바텀시트의 내부 콘텐츠 영역을 정의하는 컴포저블 블록
 */
@Composable
fun IenBottomSheet(
    state: IenBottomSheetState,
    backdrop: LayerBackdrop?,
    modifier: Modifier = Modifier,
    dismissOnScrimClick: Boolean = true,
    showDragHandle: Boolean = true,
    disableDimmer: Boolean = false,
    disableFocusLock: Boolean = false,
    header: (@Composable () -> Unit)? = null,
    headerDescription: (@Composable () -> Unit)? = null,
    cta: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = IenTheme.spacing.xl, vertical = IenTheme.spacing.xs),
    content: @Composable ColumnScope.() -> Unit,
) {
    IenBottomSheetInternal(
        state = state,
        modifier = modifier,
        dismissOnScrimClick = dismissOnScrimClick,
        showDragHandle = showDragHandle,
        disableDimmer = disableDimmer,
        disableFocusLock = disableFocusLock,
        header = header,
        headerDescription = headerDescription,
        cta = cta,
        contentPadding = contentPadding,
        backdrop = backdrop,
        content = content,
    )
}

@Composable
private fun IenBottomSheetInternal(
    state: IenBottomSheetState,
    modifier: Modifier,
    dismissOnScrimClick: Boolean,
    showDragHandle: Boolean,
    disableDimmer: Boolean,
    disableFocusLock: Boolean,
    header: (@Composable () -> Unit)?,
    headerDescription: (@Composable () -> Unit)?,
    cta: (@Composable () -> Unit)?,
    contentPadding: PaddingValues,
    backdrop: LayerBackdrop?,
    content: @Composable ColumnScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val dragOffsetY = remember { Animatable(0f) }
    val expansionProgress = remember(state) { Animatable(state.detent.expansionProgress()) }
    val visibility = remember { MutableTransitionState(false) }
    if (!state.visible) visibility.targetState = false
    var wasVisible by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val thresholdPx = with(density) { 150.dp.toPx() }
    val sheetHorizontalInset = IenTheme.spacing.sm + IenTheme.spacing.xxxs

    val motion = IenTheme.motion
    val normalMillis = motion.normalMillis
    val standardEasing = motion.standardEasing
    val hostWindowSize by rememberIenOverlayWindowSize()

    val devicePlacement by rememberIenBarPlacement()

    LaunchedEffect(state.visible, state.detent) {
        val opening = state.visible && !wasVisible
        wasVisible = state.visible
        if (state.visible) {
            if (opening) {
                dragOffsetY.snapTo(0f)
                expansionProgress.snapTo(state.detent.expansionProgress())
            } else {
                expansionProgress.animateTo(
                    targetValue = state.detent.expansionProgress(),
                    animationSpec = tween(normalMillis, easing = standardEasing),
                )
            }
        }
    }

    if (!state.visible && !visibility.currentState && !visibility.targetState && visibility.isIdle) return

    val displayedExpansionProgress = if (!visibility.currentState && state.visible)
        state.detent.expansionProgress() else expansionProgress.value
    val placement = devicePlacement ?: LocalIenBarPlacement.current
    val isDuo = placement?.isHingeClosed != null
    val innerDisplay = placement?.isHingeClosed == false
    val sidePlacement = placement?.takeIf { !innerDisplay && it.sideWidth > 0.dp }
    val safeInsets = WindowInsets.safeDrawing

    Dialog(
        onDismissRequest = { state.hide() },
        properties = ienOverlayDialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),
    ) {
        disablePlatformDialogDim()
        val windowSize = hostWindowSize
        var laidOut by remember { mutableStateOf(false) }
        LaunchedEffect(state.visible, laidOut) {
            if (state.visible && laidOut) {
                withFrameNanos { }
                visibility.targetState = true
            }
        }
        val transition = rememberTransition(visibility, label = "bottom_sheet_visibility")
        val overlayAlpha by transition.animateFloat(
            transitionSpec = { tween(normalMillis, easing = standardEasing) },
            label = "bottom_sheet_overlay_alpha",
        ) { visible -> if (visible) 1f else 0f }
        val division = with(density) {
            placement?.divisionBounds?.let {
                IntRect(it.left.roundToPx(), it.top.roundToPx(), it.right.roundToPx(), it.bottom.roundToPx())
            }
        }
        val sheetBounds = if (isDuo) {
            ienDuoSheetBounds(
                windowSize = windowSize,
                innerDisplay = innerDisplay,
                expansionProgress = if (state.detent.isExpandable()) displayedExpansionProgress else 0f,
                density = density,
                division = division,
                contentSized = !state.detent.isExpandable(),
            )
        } else with(density) {
            val left = safeInsets.getLeft(this, LayoutDirection.Ltr).coerceIn(0, windowSize.width)
            val top = safeInsets.getTop(this).coerceIn(0, windowSize.height)
            val right = (windowSize.width - if (sidePlacement != null) 0 else
                safeInsets.getRight(this, LayoutDirection.Ltr)).coerceIn(left, windowSize.width)
            IntRect(left, top, right, windowSize.height)
        }
        val windowHeightPx = sheetBounds.height.toFloat()
        val expansionDistancePx = if (isDuo) {
            val medium = ienDuoSheetBounds(windowSize, innerDisplay, 0f, density, division)
            val full = ienDuoSheetBounds(windowSize, innerDisplay, 1f, density, division)
            (medium.top - full.top).toFloat().coerceAtLeast(1f)
        } else if (windowHeightPx > 0f) {
            windowHeightPx * (IenSheetFullHeightFraction - IenSheetMediumHeightFraction)
        } else {
            with(density) { 300.dp.toPx() }
        }
        val dragModifier = Modifier.pointerInput(expansionDistancePx) {
            detectVerticalDragGestures(
                onDragEnd = {
                    coroutineScope.launch {
                        val sheetHeight = size.height.toFloat()
                        if (dragOffsetY.value > thresholdPx) {
                            launch {
                                dragOffsetY.animateTo(
                                    targetValue = sheetHeight,
                                    animationSpec = tween(normalMillis, easing = standardEasing),
                                )
                            }
                            state.hide()
                        } else {
                            launch { dragOffsetY.animateTo(0f) }
                            if (state.detent.isExpandable()) {
                                val targetDetent =
                                    if (expansionProgress.value >= IenSheetExpansionSnapFraction) {
                                        IenSheetDetent.Full
                                    } else {
                                        IenSheetDetent.Medium
                                    }
                                if (state.detent == targetDetent) {
                                    expansionProgress.animateTo(
                                        targetValue = targetDetent.expansionProgress(),
                                        animationSpec = tween(normalMillis, easing = standardEasing),
                                    )
                                } else {
                                    state.show(targetDetent)
                                }
                            }
                        }
                    }
                },
                onDragCancel = {
                    coroutineScope.launch { dragOffsetY.animateTo(0f) }
                    if (state.detent.isExpandable()) {
                        coroutineScope.launch {
                            expansionProgress.animateTo(
                                targetValue = state.detent.expansionProgress(),
                                animationSpec = tween(normalMillis, easing = standardEasing),
                            )
                        }
                    }
                },
                onVerticalDrag = { change, dragAmount ->
                    change.consume()
                    coroutineScope.launch {
                        if (state.detent.isExpandable()) {
                            val currentProgress = expansionProgress.value
                            val nextProgress =
                                (currentProgress - dragAmount / expansionDistancePx).coerceIn(0f, 1f)
                            val unconsumedDrag =
                                dragAmount + (nextProgress - currentProgress) * expansionDistancePx
                            expansionProgress.snapTo(nextProgress)
                            dragOffsetY.snapTo((dragOffsetY.value + unconsumedDrag).coerceAtLeast(0f))
                        } else {
                            dragOffsetY.snapTo((dragOffsetY.value + dragAmount).coerceAtLeast(0f))
                        }
                    }
                },
            )
        }
        val overlayColor = if (disableDimmer) Color.Transparent else IenTheme.colors.overlay
        val sheetShape = ContinuousRoundedRectangle(
            topStart = IenTheme.radius.lg,
            topEnd = IenTheme.radius.lg,
            bottomStart = if (isDuo) IenTheme.radius.lg else 0.dp,
            bottomEnd = if (isDuo) IenTheme.radius.lg else 0.dp,
        )
        val sheetBackdropModifier = if (backdrop != null) {
            Modifier.drawPlainBackdrop(
                backdrop = backdrop,
                shape = { sheetShape },
                effects = { blur(24.dp.toPx()) },
            )
        } else {
            Modifier
        }
        val sheetSurfaceColor = if (backdrop != null) {
            IenTheme.colors.surfaceRaised.copy(alpha = 0.72f)
        } else {
            IenTheme.colors.surfaceRaised
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .ienOverlayWindowSize(windowSize)
                .onGloballyPositioned { laidOut = true },
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .drawBehind {
                        drawRect(overlayColor.copy(alpha = overlayColor.alpha * overlayAlpha))
                    }
                    .clickable(
                        enabled = dismissOnScrimClick && state.visible,
                        indication = null,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    ) { state.hide() },
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .layout { measurable, constraints ->
                        val child = measurable.measure(Constraints.fixed(sheetBounds.width, sheetBounds.height))
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            child.place(sheetBounds.left, sheetBounds.top)
                        }
                    }
                    .consumeWindowInsets(with(density) {
                        PaddingValues.Absolute(
                            left = sheetBounds.left.toDp(),
                            top = sheetBounds.top.toDp(),
                            right = (windowSize.width - sheetBounds.right).toDp(),
                            bottom = (windowSize.height - sheetBounds.bottom).toDp(),
                        )
                    }),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Box(
                    modifier = Modifier
                        .then(if (!isDuo && sidePlacement == null) Modifier.widthIn(max = 520.dp) else Modifier)
                        .fillMaxWidth()
                        .offset { IntOffset(0, dragOffsetY.value.roundToInt()) }
                        .graphicsLayer {
                            translationY = (size.height + windowSize.height - sheetBounds.bottom) * (1f - overlayAlpha)
                        }
                ) {
                    IenSurface(
                        modifier = sheetBackdropModifier
                            .fillMaxWidth()
                            .layout { measurable, constraints ->
                                val isExpandable = state.detent.isExpandable()
                                val progress = if (isExpandable) displayedExpansionProgress else 1f
                                val horizontalInset = if (isDuo) 0 else
                                    (sheetHorizontalInset.roundToPx() * (1f - progress))
                                        .roundToInt()
                                        .coerceAtMost(constraints.maxWidth / 2)
                                val sheetHeight =
                                    if (isExpandable && isDuo) {
                                        constraints.maxHeight
                                    } else if (isExpandable) {
                                        (
                                            constraints.maxHeight *
                                                (IenSheetMediumHeightFraction +
                                                    (IenSheetFullHeightFraction - IenSheetMediumHeightFraction) * progress)
                                        ).roundToInt().coerceIn(constraints.minHeight, constraints.maxHeight)
                                    } else {
                                        constraints.minHeight
                                    }
                                val placeable =
                                    measurable.measure(
                                        constraints.copy(
                                            minWidth = 0,
                                            maxWidth = (constraints.maxWidth - horizontalInset * 2).coerceAtLeast(0),
                                            minHeight = sheetHeight,
                                            maxHeight = if (isExpandable) sheetHeight else constraints.maxHeight,
                                        ),
                                    )
                                layout(placeable.width + horizontalInset * 2, placeable.height) {
                                    placeable.placeRelative(horizontalInset, 0)
                                }
                            }
                            .clickable(
                                indication = null,
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            ) { },
                        color = sheetSurfaceColor,
                        shape = sheetShape,
                    ) {
                        IenSheetContent(
                            placement = sidePlacement,
                            horizontalInset = if (isDuo) with(density) {
                                (windowSize.width - sheetBounds.right).toDp()
                            } else if (state.detent.isExpandable())
                                sheetHorizontalInset * (1f - displayedExpansionProgress) else 0.dp,
                            cta = cta,
                        ) {
                            IenSheetBody(
                                dragModifier = dragModifier,
                                showDragHandle = showDragHandle,
                                header = header,
                                headerDescription = headerDescription,
                                hasCta = cta != null,
                                contentPadding = contentPadding,
                                content = content,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IenSheetBody(
    dragModifier: Modifier,
    showDragHandle: Boolean,
    header: (@Composable () -> Unit)?,
    headerDescription: (@Composable () -> Unit)?,
    hasCta: Boolean,
    contentPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalIenBarPlacement provides null,
        LocalIenSideBarEnabled provides false,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.none),
        ) {
            Column(
                modifier = dragModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showDragHandle) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = IenTheme.spacing.sm)
                            .width(36.dp)
                            .height(4.dp)
                            .clip(ContinuousCapsule)
                            .background(IenTheme.colors.borderStrong),
                    )
                } else {
                    Spacer(modifier = Modifier.height(IenTheme.spacing.md))
                }

                if (header != null || headerDescription != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = IenTheme.spacing.xl, vertical = IenTheme.spacing.xs),
                        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs)
                    ) {
                        header?.invoke()
                        headerDescription?.invoke()
                    }
                    Spacer(modifier = Modifier.height(IenTheme.spacing.xs))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .padding(contentPadding)
            ) {
                content()
            }

            if (hasCta) {
                Spacer(modifier = Modifier.height(IenTheme.spacing.xs))
            } else {
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(IenTheme.spacing.md)
                )
            }
        }
    }
}

@Composable
private fun IenSheetContent(
    placement: IenBarPlacement?,
    horizontalInset: Dp,
    cta: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
) {
    val horizontalPadding = IenTheme.spacing.xl
    val bottomPadding = IenTheme.spacing.md
    val windowHeight = LocalWindowInfo.current.containerSize.height
    val sidePlacement = placement?.copy(
        sideWidth = (placement.sideWidth - horizontalInset).coerceAtLeast(0.dp),
        centerFromRight = (placement.centerFromRight - horizontalInset).coerceAtLeast(0.dp),
        controlsTop = 0.dp,
        controlsBottom = 0.dp,
    )
    SubcomposeLayout { constraints ->
        val width = constraints.maxWidth
        val sideWidth = (sidePlacement?.sideWidth?.roundToPx() ?: 0).coerceIn(0, width)
        val contentWidth = width - sideWidth
        val bottomInset = if (placement != null) {
            maxOf(placement.controlsBottom.roundToPx(), bottomPadding.roundToPx()).coerceAtMost(constraints.maxHeight)
        } else 0
        val horizontalInsetPx = horizontalPadding.roundToPx().coerceAtMost(contentWidth / 2)
        val actions = subcompose("cta") {
            CompositionLocalProvider(
                LocalIenBarPlacement provides sidePlacement,
                LocalIenSideBarEnabled provides (sidePlacement != null),
            ) {
                if (cta != null) {
                    Box(
                        Modifier
                            .then(if (sidePlacement == null) Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .padding(horizontal = horizontalPadding)
                                .padding(bottom = bottomPadding) else Modifier),
                    ) {
                        cta()
                    }
                }
            }
        }.map {
            it.measure(Constraints(
                maxWidth = if (sidePlacement == null) width else maxOf(sideWidth, contentWidth - horizontalInsetPx * 2),
                maxHeight = minOf(
                    constraints.maxHeight - bottomInset,
                    windowHeight - (placement?.controlsTop?.roundToPx() ?: 0) - bottomInset,
                ).coerceAtLeast(0),
            ))
        }
        val wideActionsHeight = actions.filter { it.width > sideWidth }.maxOfOrNull { it.height } ?: 0
        val bottomReservation = if (wideActionsHeight > 0) wideActionsHeight + bottomInset else 0
        val body = subcompose("body") {
            Box(Modifier.consumeWindowInsets(PaddingValues.Absolute(right = sidePlacement?.sideWidth ?: 0.dp))) {
                content()
            }
        }.map {
            it.measure(Constraints(
                maxWidth = contentWidth,
                minHeight = (constraints.minHeight - bottomReservation).coerceAtLeast(0),
                maxHeight = (constraints.maxHeight - bottomReservation).coerceAtLeast(0),
            ))
        }
        val bodyHeight = body.maxOfOrNull { it.height } ?: 0
        val actionsHeight = actions.maxOfOrNull { it.height } ?: 0
        val height = maxOf(bodyHeight + bottomReservation, actionsHeight + bottomInset)
            .coerceIn(constraints.minHeight, constraints.maxHeight)
        layout(width, height) {
            body.forEach { it.place(0, 0) }
            actions.forEach {
                val x = if (sidePlacement != null && it.width <= sideWidth) {
                    width - sidePlacement.centerFromRight.roundToPx() - it.width / 2
                } else {
                    (contentWidth - it.width) / 2
                }
                it.place(x.coerceIn(0, width - it.width), height - bottomInset - it.height)
            }
        }
    }
}

/**
 * [IenBottomSheetSelect]에서 사용할 개별 옵션 항목 데이터 모델입니다.
 *
 * @property name 화면에 표시될 옵션 이름
 * @property value 옵션의 실제 식별 값
 */
data class IenBottomSheetOption(
    val name: String,
    val value: String,
)

/**
 * 바텀시트 내에서 여러 옵션 리스트 중 하나를 선택할 수 있게 해주는 선택 컴포저블입니다.
 *
 * @param options 선택 가능한 [IenBottomSheetOption] 목록
 * @param value 현재 선택된 값의 식별자
 * @param onChange 새로운 옵션이 선택되었을 때 호출되는 콜백 함수
 * @param modifier 컴포저블에 적용할 [Modifier]
 */
@Composable
fun IenBottomSheetSelect(
    options: List<IenBottomSheetOption>,
    value: String?,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.none)
    ) {
        options.forEach { option ->
            val isSelected = option.value == value
            IenListRow(
                title = option.name,
                selected = isSelected,
                onClick = { onChange(option.value) },
                trailing = {
                    if (isSelected) {
                        IenIcon(
                            icon = IconData.Vector(SystemIcons.Check),
                            contentDescription = stringResource(Res.string.selected),
                            tint = IenTheme.colors.brand
                        )
                    }
                }
            )
        }
    }
}
