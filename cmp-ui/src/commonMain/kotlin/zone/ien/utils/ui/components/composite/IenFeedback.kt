package zone.ien.utils.ui.components.composite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.material.filled.Check
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenIcon
import zone.ien.utils.ui.components.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

enum class IenSheetDetent { Content, Medium, Full }

@Stable
class IenBottomSheetState internal constructor(
    visible: Boolean,
    detent: IenSheetDetent,
) {
    var visible by mutableStateOf(visible)
        private set
    var detent by mutableStateOf(detent)
        private set

    fun show(detent: IenSheetDetent = IenSheetDetent.Content) {
        this.detent = detent
        visible = true
    }

    fun hide() {
        visible = false
    }
}

@Composable
fun rememberIenBottomSheetState(
    visible: Boolean = false,
    detent: IenSheetDetent = IenSheetDetent.Content,
) = remember { IenBottomSheetState(visible, detent) }

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
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val dragOffsetY = remember { Animatable(0f) }
    val density = LocalDensity.current
    val thresholdPx = with(density) { 150.dp.toPx() }

    val motion = IenTheme.motion
    val normalMillis = motion.normalMillis
    val standardEasing = motion.standardEasing

    LaunchedEffect(state.visible) {
        if (state.visible) {
            dragOffsetY.snapTo(0f)
        }
    }

    val dragModifier = Modifier.pointerInput(Unit) {
        detectVerticalDragGestures(
            onDragEnd = {
                coroutineScope.launch {
                    val sheetHeight = size.height.toFloat()
                    if (dragOffsetY.value > thresholdPx) {
                        launch {
                            dragOffsetY.animateTo(
                                targetValue = sheetHeight,
                                animationSpec = tween(normalMillis, easing = standardEasing)
                            )
                        }
                        state.hide()
                    } else {
                        dragOffsetY.animateTo(0f)
                    }
                }
            },
            onDragCancel = {
                coroutineScope.launch { dragOffsetY.animateTo(0f) }
            },
            onVerticalDrag = { change, dragAmount ->
                change.consume()
                coroutineScope.launch {
                    dragOffsetY.snapTo((dragOffsetY.value + dragAmount).coerceAtLeast(0f))
                }
            }
        )
    }

    AnimatedVisibility(
        visible = state.visible,
        enter = fadeIn(tween(IenTheme.motion.fastMillis)),
        exit = fadeOut(tween(IenTheme.motion.fastMillis)),
    ) {
        val overlayColor = if (disableDimmer) Color.Transparent else IenTheme.colors.overlay
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(overlayColor)
                .then(if (dismissOnScrimClick) dragModifier else Modifier)
                .clickable(
                    enabled = dismissOnScrimClick,
                    indication = null,
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                ) { state.hide() },
            contentAlignment = Alignment.BottomCenter,
        ) {
            AnimatedVisibility(
                visible = state.visible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(IenTheme.motion.normalMillis, easing = IenTheme.motion.standardEasing)
                ) + fadeIn(tween(IenTheme.motion.fastMillis)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(IenTheme.motion.normalMillis, easing = IenTheme.motion.standardEasing)
                ) + fadeOut(tween(IenTheme.motion.fastMillis)),
                modifier = Modifier
                    .widthIn(max = 520.dp)
                    .fillMaxWidth()
                    .offset { IntOffset(0, dragOffsetY.value.roundToInt()) }
            ) {
                IenSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        ) { },
                    color = IenTheme.colors.surfaceRaised,
                    shape = RoundedCornerShape(topStart = IenTheme.radius.lg, topEnd = IenTheme.radius.lg),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        Column(
                            modifier = dragModifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (showDragHandle) {
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 12.dp)
                                        .width(36.dp)
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(IenTheme.radius.full))
                                        .background(IenTheme.colors.borderStrong),
                                )
                            } else {
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            if (header != null || headerDescription != null) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp, vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    header?.invoke()
                                    headerDescription?.invoke()
                                }
                                Spacer(modifier = Modifier.height(8.dp))
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

                        if (cta != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                                    .padding(horizontal = 24.dp)
                                    .padding(bottom = 16.dp)
                            ) {
                                cta()
                            }
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .navigationBarsPadding()
                                    .height(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

data class IenBottomSheetOption(
    val name: String,
    val value: String,
)

@Composable
fun IenBottomSheetSelect(
    options: List<IenBottomSheetOption>,
    value: String?,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
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
                            imageVector = M3SystemIcons.Filled.Check,
                            contentDescription = "선택됨",
                            tint = IenTheme.colors.brand
                        )
                    }
                }
            )
        }
    }
}

@Immutable
data class IenDialogAction(
    val text: String,
    val onClick: () -> Unit,
    val tone: IenSemanticTone = IenSemanticTone.Brand,
)

@Composable
fun IenDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    confirm: IenDialogAction,
    modifier: Modifier = Modifier,
    dismiss: IenDialogAction? = null,
) {
    if (!visible) return
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { IenText(title, style = IenTheme.typography.title3) },
        text = { IenText(message, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary) },
        confirmButton = {
            TextButton(onClick = confirm.onClick) {
                IenText(confirm.text, style = IenTheme.typography.label1, color = IenTheme.colors.brand)
            }
        },
        dismissButton = dismiss?.let { action ->
            {
                TextButton(onClick = action.onClick) {
                    IenText(action.text, style = IenTheme.typography.label1, color = IenTheme.colors.textSecondary)
                }
            }
        },
    )
}

@Stable
class IenToastHostState {
    internal val toasts = mutableStateListOf<IenToastData>()

    fun show(message: String, tone: IenSemanticTone = IenSemanticTone.Neutral) {
        toasts.add(IenToastData(message, tone))
    }

    fun dismiss(data: IenToastData) {
        toasts.remove(data)
    }
}

@Immutable
data class IenToastData(
    val message: String,
    val tone: IenSemanticTone,
)

@Composable
fun IenToast(
    message: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
) {
    val container = when (tone) {
        IenSemanticTone.Neutral -> IenTheme.colors.textPrimary
        IenSemanticTone.Brand -> IenTheme.colors.brand
        IenSemanticTone.Success -> IenTheme.colors.success
        IenSemanticTone.Warning -> IenTheme.colors.warning
        IenSemanticTone.Danger -> IenTheme.colors.danger
        IenSemanticTone.Info -> IenTheme.colors.info
    }
    IenSurface(
        modifier = modifier.semantics {
            liveRegion = LiveRegionMode.Polite
            contentDescription = message
        },
        color = container,
        contentColor = IenTheme.colors.surface,
        shape = RoundedCornerShape(IenTheme.radius.default),
    ) {
        IenText(
            text = message,
            modifier = Modifier.padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
            color = IenTheme.colors.surface,
            style = IenTheme.typography.body2,
        )
    }
}

@Composable
fun rememberIenToastHostState() = remember { IenToastHostState() }

@Composable
fun IenToastHost(
    state: IenToastHostState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(IenTheme.spacing.lg),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
            state.toasts.takeLast(3).forEach { toast ->
                IenToast(message = toast.message, tone = toast.tone)
            }
        }
    }
}

@Composable
fun IenSkeleton(
    modifier: Modifier = Modifier,
    height: Dp? = null,
    radius: Dp = IenTheme.radius.sm,
    pattern: IenSkeletonPattern = IenSkeletonPattern.TopList,
    custom: List<IenSkeletonElement>? = null,
    repeatLastItemCount: IenSkeletonRepeat = IenSkeletonRepeat.Count(3),
    play: IenSkeletonPlay = IenSkeletonPlay.Show,
    background: IenSkeletonBackground = IenSkeletonBackground.Grey,
) {
    if (play == IenSkeletonPlay.Hide) {
        return
    }

    val transition = rememberInfiniteTransition(label = "ienSkeleton")
    val waveOffset by transition.animateFloat(
        initialValue = -960f,
        targetValue = 960f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = IenTheme.motion.standardEasing),
        ),
        label = "ienSkeletonWave",
    )
    val waveSpread by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = IenTheme.motion.standardEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "ienSkeletonFluidSpread",
    )
    val colors = skeletonColors(background)
    val brush = Brush.linearGradient(
        0.00f to colors.base,
        0.34f to colors.base,
        0.46f to colors.softWave,
        0.54f to colors.wave,
        0.66f to colors.softWave,
        1.00f to colors.base,
        start = Offset(waveOffset, 0f),
        end = Offset(waveOffset + 760f * waveSpread, 260f * waveSpread),
    )

    if (height != null) {
        IenSkeletonBlock(
            modifier = modifier,
            height = height,
            radius = radius,
            brush = brush,
        )
        return
    }

    val elements = (custom ?: pattern.elements()).withRepeatedLast(repeatLastItemCount)

    Column(
        modifier = modifier.semantics {
            contentDescription = "로딩 중"
            liveRegion = LiveRegionMode.Polite
        },
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
    ) {
        elements.forEachIndexed { index, element ->
            IenSkeletonElementView(
                element = element,
                index = index,
                radius = radius,
                brush = brush,
            )
        }
    }
}

enum class IenSkeletonPattern {
    TopList,
    TopListWithIcon,
    AmountTopList,
    AmountTopListWithIcon,
    SubtitleList,
    SubtitleListWithIcon,
    ListOnly,
    ListWithIconOnly,
    CardOnly,
}

enum class IenSkeletonPlay {
    Show,
    Hide,
}

enum class IenSkeletonBackground {
    White,
    Grey,
    GreyOpacity100,
}

sealed interface IenSkeletonRepeat {
    data class Count(val value: Int) : IenSkeletonRepeat
    data object Infinite : IenSkeletonRepeat
}

sealed interface IenSkeletonElement {
    data object Title : IenSkeletonElement
    data object Subtitle : IenSkeletonElement
    data object List : IenSkeletonElement
    data object ListWithIcon : IenSkeletonElement
    data object Card : IenSkeletonElement
    data class Spacer(val height: Dp) : IenSkeletonElement
}

private data class IenSkeletonColors(
    val base: Color,
    val softWave: Color,
    val wave: Color,
)

private fun IenSkeletonPattern.elements(): List<IenSkeletonElement> = when (this) {
    IenSkeletonPattern.TopList -> listOf(
        IenSkeletonElement.Title,
        IenSkeletonElement.Spacer(4.dp),
        IenSkeletonElement.List,
    )

    IenSkeletonPattern.TopListWithIcon -> listOf(
        IenSkeletonElement.Title,
        IenSkeletonElement.Spacer(4.dp),
        IenSkeletonElement.ListWithIcon,
    )

    IenSkeletonPattern.AmountTopList -> listOf(
        IenSkeletonElement.Title,
        IenSkeletonElement.Subtitle,
        IenSkeletonElement.Spacer(8.dp),
        IenSkeletonElement.List,
    )

    IenSkeletonPattern.AmountTopListWithIcon -> listOf(
        IenSkeletonElement.Title,
        IenSkeletonElement.Subtitle,
        IenSkeletonElement.Spacer(8.dp),
        IenSkeletonElement.ListWithIcon,
    )

    IenSkeletonPattern.SubtitleList -> listOf(
        IenSkeletonElement.Subtitle,
        IenSkeletonElement.Spacer(4.dp),
        IenSkeletonElement.List,
    )

    IenSkeletonPattern.SubtitleListWithIcon -> listOf(
        IenSkeletonElement.Subtitle,
        IenSkeletonElement.Spacer(4.dp),
        IenSkeletonElement.ListWithIcon,
    )

    IenSkeletonPattern.ListOnly -> listOf(IenSkeletonElement.List)
    IenSkeletonPattern.ListWithIconOnly -> listOf(IenSkeletonElement.ListWithIcon)
    IenSkeletonPattern.CardOnly -> listOf(IenSkeletonElement.Card)
}

private fun List<IenSkeletonElement>.withRepeatedLast(repeat: IenSkeletonRepeat): List<IenSkeletonElement> {
    if (isEmpty()) {
        return this
    }
    val repeatCount = when (repeat) {
        is IenSkeletonRepeat.Count -> repeat.value.coerceAtLeast(1)
        IenSkeletonRepeat.Infinite -> 30
    }
    return dropLast(1) + List(repeatCount) { last() }
}

@Composable
private fun skeletonColors(background: IenSkeletonBackground): IenSkeletonColors = when (background) {
    IenSkeletonBackground.White -> IenSkeletonColors(
        base = IenTheme.colors.surface.copy(alpha = 0.70f),
        softWave = IenTheme.colors.surface.copy(alpha = 0.84f),
        wave = IenTheme.colors.surface.copy(alpha = 0.96f),
    )

    IenSkeletonBackground.Grey -> IenSkeletonColors(
        base = IenTheme.colors.surfaceVariant,
        softWave = IenTheme.colors.surfaceWeak.copy(alpha = 0.72f),
        wave = IenTheme.colors.surface.copy(alpha = 0.88f),
    )

    IenSkeletonBackground.GreyOpacity100 -> IenSkeletonColors(
        base = IenTheme.colors.border.copy(alpha = 0.60f),
        softWave = IenTheme.colors.border.copy(alpha = 0.40f),
        wave = IenTheme.colors.border.copy(alpha = 0.24f),
    )
}

@Composable
private fun ColumnScope.IenSkeletonElementView(
    element: IenSkeletonElement,
    index: Int,
    radius: Dp,
    brush: Brush,
) {
    when (element) {
        IenSkeletonElement.Title -> IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(0.48f),
            height = 24.dp,
            radius = radius,
            brush = brush,
        )

        IenSkeletonElement.Subtitle -> IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(0.34f),
            height = 16.dp,
            radius = radius,
            brush = brush,
        )

        IenSkeletonElement.List -> IenSkeletonListRow(
            index = index,
            radius = radius,
            brush = brush,
        )

        IenSkeletonElement.ListWithIcon -> IenSkeletonListRowWithIcon(
            index = index,
            radius = radius,
            brush = brush,
        )

        IenSkeletonElement.Card -> IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(),
            height = 132.dp,
            radius = IenTheme.radius.default,
            brush = brush,
        )

        is IenSkeletonElement.Spacer -> Spacer(modifier = Modifier.height(element.height))
    }
}

@Composable
private fun IenSkeletonListRow(
    index: Int,
    radius: Dp,
    brush: Brush,
) {
    val widthFraction = when (index % 3) {
        0 -> 0.92f
        1 -> 0.78f
        else -> 0.86f
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(widthFraction),
            height = 18.dp,
            radius = radius,
            brush = brush,
        )
    }
}

@Composable
private fun IenSkeletonListRowWithIcon(
    index: Int,
    radius: Dp,
    brush: Brush,
) {
    val primaryWidthFraction = when (index % 3) {
        0 -> 0.70f
        1 -> 0.56f
        else -> 0.64f
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(brush),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        ) {
            IenSkeletonBlock(
                modifier = Modifier.fillMaxWidth(primaryWidthFraction),
                height = 18.dp,
                radius = radius,
                brush = brush,
            )
            IenSkeletonBlock(
                modifier = Modifier.fillMaxWidth(0.38f),
                height = 14.dp,
                radius = radius,
                brush = brush,
            )
        }
    }
}

@Composable
private fun IenSkeletonBlock(
    modifier: Modifier,
    height: Dp,
    radius: Dp,
    brush: Brush,
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(radius))
            .background(brush),
    )
}

@Composable
fun IenProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    size: IenProgressBarSize = IenProgressBarSize.Normal,
    color: Color = IenTheme.colors.brand,
    animate: Boolean = false,
    contentDescription: String? = null,
    showLabel: Boolean = false,
) {
    val safeProgress = progress.coerceIn(0f, 1f)
    val displayedProgress by animateFloatAsState(
        targetValue = safeProgress,
        animationSpec = tween(
            durationMillis = if (animate) IenTheme.motion.normalMillis else 0,
            easing = IenTheme.motion.standardEasing,
        ),
        label = "ienProgressBar",
    )
    val barHeight = when (size) {
        IenProgressBarSize.Light -> 2.dp
        IenProgressBarSize.Normal -> 4.dp
        IenProgressBarSize.Bold -> 8.dp
    }

    Column(
        modifier = modifier.semantics {
            if (contentDescription != null) this.contentDescription = contentDescription
            progressBarRangeInfo = androidx.compose.ui.semantics.ProgressBarRangeInfo(safeProgress, 0f..1f)
        },
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .clip(RoundedCornerShape(IenTheme.radius.full))
                .background(color.copy(alpha = 0.16f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(displayedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(IenTheme.radius.full))
                    .background(color),
            )
        }
        if (showLabel) {
            IenText(
                text = "${(safeProgress * 100).toInt()}%",
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textSecondary,
            )
        }
    }
}

enum class IenProgressBarSize {
    Light,
    Normal,
    Bold,
}

enum class IenLoaderSize { Small, Medium, Large }

@Composable
fun IenLoader(
    modifier: Modifier = Modifier,
    size: IenLoaderSize = IenLoaderSize.Medium,
    label: String? = null,
) {
    Column(
        modifier = modifier.semantics {
            contentDescription = label ?: "로딩 중"
            liveRegion = LiveRegionMode.Polite
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
    ) {
        IenLoaderPrimitive(
            modifier = Modifier.size(
                when (size) {
                    IenLoaderSize.Small -> IenTheme.icon.sm
                    IenLoaderSize.Medium -> IenTheme.icon.lg
                    IenLoaderSize.Large -> IenTheme.icon.xl
                },
            ),
            color = IenTheme.colors.brand,
        )
        if (label != null) {
            IenText(label, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
        }
    }
}

enum class IenStepStatus { Pending, Current, Done, Error }

enum class IenProgressStepperVariant {
    Compact,
    Icon,
}

enum class IenProgressStepperPaddingTop {
    Default,
    Wide,
}

data class IenProgressStep(
    val title: String? = null,
    val status: IenStepStatus? = null,
    val icon: (@Composable () -> Unit)? = null,
) {
    constructor(label: String, status: IenStepStatus) : this(
        title = label,
        status = status,
        icon = null,
    )
}

@Composable
fun IenProgressStepper(
    steps: List<IenProgressStep>,
    modifier: Modifier = Modifier,
    variant: IenProgressStepperVariant = IenProgressStepperVariant.Compact,
    paddingTop: IenProgressStepperPaddingTop = IenProgressStepperPaddingTop.Default,
    activeStepIndex: Int = 0,
    checkForFinish: Boolean = false,
) {
    val safeActiveStepIndex = activeStepIndex.coerceIn(0, (steps.size - 1).coerceAtLeast(0))
    val topPadding = when (paddingTop) {
        IenProgressStepperPaddingTop.Default -> IenTheme.spacing.md
        IenProgressStepperPaddingTop.Wide -> IenTheme.spacing.xl
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPadding)
            .semantics {
                contentDescription = "진행 단계 ${safeActiveStepIndex + 1} / ${steps.size}"
            },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.none),
        verticalAlignment = Alignment.Top,
    ) {
        steps.forEachIndexed { index, step ->
            val status = step.status ?: when {
                index < safeActiveStepIndex -> IenStepStatus.Done
                index == safeActiveStepIndex -> IenStepStatus.Current
                else -> IenStepStatus.Pending
            }
            val color = when (status) {
                IenStepStatus.Pending -> IenTheme.colors.borderStrong
                IenStepStatus.Current -> IenTheme.colors.brand
                IenStepStatus.Done -> IenTheme.colors.brand
                IenStepStatus.Error -> IenTheme.colors.danger
            }
            val titleColor = when (status) {
                IenStepStatus.Pending -> IenTheme.colors.textTertiary
                IenStepStatus.Current -> IenTheme.colors.textPrimary
                IenStepStatus.Done -> IenTheme.colors.textSecondary
                IenStepStatus.Error -> IenTheme.colors.danger
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(progressStepMarkerSize(variant)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (index > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .fillMaxWidth(0.5f)
                                .height(IenTheme.stroke.thin)
                                .background(if (index <= safeActiveStepIndex) IenTheme.colors.brand else IenTheme.colors.border),
                        )
                    }
                    if (index < steps.lastIndex) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxWidth(0.5f)
                                .height(IenTheme.stroke.thin)
                                .background(if (index < safeActiveStepIndex) IenTheme.colors.brand else IenTheme.colors.border),
                        )
                    }
                    ProgressStepMarker(
                        variant = variant,
                        status = status,
                        color = color,
                        icon = step.icon,
                        showFinishedCheck = variant == IenProgressStepperVariant.Icon &&
                            checkForFinish &&
                            index < safeActiveStepIndex,
                    )
                }
                if (step.title != null) {
                    Spacer(Modifier.height(IenTheme.spacing.xs))
                    IenText(
                        text = step.title,
                        style = IenTheme.typography.caption,
                        color = titleColor,
                        maxLines = 2,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
        }
    }
}

private fun progressStepMarkerSize(variant: IenProgressStepperVariant): Dp =
    when (variant) {
        IenProgressStepperVariant.Compact -> 10.dp
        IenProgressStepperVariant.Icon -> 28.dp
    }

@Composable
private fun ProgressStepMarker(
    variant: IenProgressStepperVariant,
    status: IenStepStatus,
    color: Color,
    icon: (@Composable () -> Unit)?,
    showFinishedCheck: Boolean,
) {
    val markerSize = progressStepMarkerSize(variant)
    val backgroundColor = when {
        status == IenStepStatus.Pending -> IenTheme.colors.surfaceVariant
        variant == IenProgressStepperVariant.Icon -> color
        else -> color
    }

    Box(
        modifier = Modifier
            .size(markerSize)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        when {
            showFinishedCheck -> IenIcon(
                imageVector = M3SystemIcons.Filled.Check,
                contentDescription = null,
                tint = IenTheme.colors.onBrand,
                size = IenTheme.icon.sm,
            )
            variant == IenProgressStepperVariant.Icon && icon != null -> icon()
            variant == IenProgressStepperVariant.Icon && status == IenStepStatus.Current -> Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(IenTheme.colors.onBrand),
            )
        }
    }
}

enum class IenResultTone { Success, Failure, Empty, Info }

@Composable
fun IenResult(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    tone: IenResultTone = IenResultTone.Info,
    icon: (@Composable BoxScope.() -> Unit)? = null,
    primaryAction: (@Composable () -> Unit)? = null,
    secondaryAction: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(IenTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
    ) {
        val iconColor = when (tone) {
            IenResultTone.Success -> IenTheme.colors.successWeak
            IenResultTone.Failure -> IenTheme.colors.dangerWeak
            IenResultTone.Empty -> IenTheme.colors.surfaceWeak
            IenResultTone.Info -> IenTheme.colors.infoWeak
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(iconColor),
            contentAlignment = Alignment.Center,
        ) {
            icon?.invoke(this)
        }
        IenText(title, style = IenTheme.typography.title2)
        if (description != null) {
            IenText(description, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
        }
        primaryAction?.invoke()
        secondaryAction?.invoke()
    }
}
