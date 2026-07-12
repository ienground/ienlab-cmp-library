package zone.ien.utils.ui.components.composite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.loading
import zone.ien.utils.cmp_ui.generated.resources.progress_stepper_step
import zone.ien.utils.cmp_ui.generated.resources.selected
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.fill.Check
import zone.ien.utils.icon.remix.fill.Close
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenIcon
import zone.ien.utils.ui.components.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

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
    var mounted by remember { mutableStateOf(state.visible) }
    val density = LocalDensity.current
    val thresholdPx = with(density) { 150.dp.toPx() }

    val motion = IenTheme.motion
    val normalMillis = motion.normalMillis
    val standardEasing = motion.standardEasing

    LaunchedEffect(state.visible) {
        if (state.visible) {
            mounted = true
            dragOffsetY.snapTo(0f)
        } else if (mounted) {
            delay(normalMillis.toLong())
            mounted = false
        }
    }

    if (!mounted) return

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

    Dialog(
        onDismissRequest = { state.hide() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
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
                            .then(state.detent.sheetHeightModifier())
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
}

private fun IenSheetDetent.sheetHeightModifier(): Modifier = when (this) {
    IenSheetDetent.Content -> Modifier
    IenSheetDetent.Medium -> Modifier.fillMaxHeight(0.5f)
    IenSheetDetent.Full -> Modifier.fillMaxHeight(0.92f)
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
                            imageVector = RemixIcons.Fill.Check,
                            contentDescription = stringResource(Res.string.selected),
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
    if (dismiss == null) {
        IenAlertDialog(
            visible = visible,
            title = title,
            message = message,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            confirmText = confirm.text,
            onConfirmClick = confirm.onClick,
            tone = confirm.tone,
        )
    } else {
        IenConfirmDialog(
            visible = visible,
            onClose = onDismissRequest,
            modifier = modifier,
            title = {
                IenConfirmDialogTitle(text = title)
            },
            description = {
                IenConfirmDialogDescription(text = message)
            },
            cancelButton = {
                IenConfirmDialogCancelButton(
                    text = dismiss.text,
                    onClick = dismiss.onClick,
                    tone = dismiss.tone,
                )
            },
            confirmButton = {
                IenConfirmDialogConfirmButton(
                    text = confirm.text,
                    onClick = confirm.onClick,
                    tone = confirm.tone,
                )
            },
        )
    }
}



enum class IenToastPosition {
    Top,
    Bottom,
}

enum class IenToastAriaLive {
    Polite,
    Assertive,
}

object IenToastDefaults {
    const val DurationMillis: Long = 3000L
    const val ActionDurationMillis: Long = 5000L
    val MaxWidth: Dp = 420.dp
}

class IenToastPositionProvider(
    private val position: IenToastPosition,
    private val density: Density,
    private val higherThanCTA: Boolean = false
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val x = (windowSize.width - popupContentSize.width) / 2
        val y = when (position) {
            IenToastPosition.Top -> {
                with(density) { 16.dp.toPx().toInt() }
            }
            IenToastPosition.Bottom -> {
                val baseOffset = with(density) { 32.dp.toPx().toInt() }
                val ctaOffset = if (higherThanCTA) with(density) { 88.dp.toPx().toInt() } else 0
                windowSize.height - popupContentSize.height - baseOffset - ctaOffset
            }
        }
        return IntOffset(x - anchorBounds.left, y - anchorBounds.top)
    }
}

@Immutable
data class IenToastAction(
    val text: String,
    val onClick: () -> Unit,
)

@Composable
fun IenToast(
    message: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    leftAddon: (@Composable () -> Unit)? = null,
    button: IenToastAction? = null,
) {
    IenToastContent(
        text = message,
        modifier = modifier,
        tone = tone,
        leftAddon = leftAddon,
        button = button,
        ariaLive = IenToastAriaLive.Polite,
    )
}

@Composable
fun IenToast(
    open: Boolean,
    position: IenToastPosition,
    text: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    leftAddon: (@Composable () -> Unit)? = null,
    button: IenToastAction? = null,
    durationMillis: Long = if (button == null) IenToastDefaults.DurationMillis else IenToastDefaults.ActionDurationMillis,
    onExited: (() -> Unit)? = null,
    higherThanCTA: Boolean = false,
    ariaLive: IenToastAriaLive = IenToastAriaLive.Polite,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val initialOffset = with(density) { if (position == IenToastPosition.Top) -80.dp.toPx() else 80.dp.toPx() }
    val targetOffset = with(density) { if (position == IenToastPosition.Top) -120.dp.toPx() else 120.dp.toPx() }
    val offsetY = remember { Animatable(initialOffset) }
    val dismissThreshold = with(density) { 48.dp.toPx() }
    val normalMillis = IenTheme.motion.normalMillis
    var keepInComposition by remember { mutableStateOf(open) }

    LaunchedEffect(open, durationMillis) {
        if (open && durationMillis > 0L) {
            delay(durationMillis)
            onClose()
        }
    }

    LaunchedEffect(open) {
        if (open) {
            keepInComposition = true
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        } else if (keepInComposition) {
            offsetY.animateTo(
                targetValue = targetOffset,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            keepInComposition = false
            onExited?.invoke()
        }
    }

    if (keepInComposition) {
        Popup(
            popupPositionProvider = remember(position, higherThanCTA) {
                IenToastPositionProvider(position, density, higherThanCTA)
            },
            properties = PopupProperties(focusable = false)
        ) {
            AnimatedVisibility(
                visible = open,
                enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)),
                exit = fadeOut(spring(stiffness = Spring.StiffnessMediumLow)),
                modifier = modifier
                    .padding(horizontal = IenTheme.spacing.lg)
                    .offset { IntOffset(0, offsetY.value.roundToInt()) }
                    .pointerInput(open, position) {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                val shouldClose = when (position) {
                                    IenToastPosition.Top -> offsetY.value < -dismissThreshold
                                    IenToastPosition.Bottom -> offsetY.value > dismissThreshold
                                }
                                if (shouldClose) {
                                    onClose()
                                } else {
                                    coroutineScope.launch {
                                        offsetY.animateTo(
                                            targetValue = 0f,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioNoBouncy,
                                                stiffness = Spring.StiffnessMedium
                                            ),
                                        )
                                    }
                                }
                            },
                            onDragCancel = {
                                coroutineScope.launch { offsetY.animateTo(0f) }
                            },
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                coroutineScope.launch {
                                    val next = offsetY.value + dragAmount
                                    offsetY.snapTo(
                                        when (position) {
                                            IenToastPosition.Top -> next.coerceAtMost(0f)
                                            IenToastPosition.Bottom -> next.coerceAtLeast(0f)
                                        }
                                    )
                                }
                            },
                        )
                    },
            ) {
                IenToastContent(
                    text = text,
                    tone = tone,
                    leftAddon = leftAddon,
                    button = if (position == IenToastPosition.Bottom) button else null,
                    ariaLive = ariaLive,
                )
            }
        }
    }
}

@Composable
private fun IenToastContent(
    text: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    leftAddon: (@Composable () -> Unit)? = null,
    button: IenToastAction? = null,
    ariaLive: IenToastAriaLive = IenToastAriaLive.Polite,
) {
    val container = Color(0xFF191F28) // TDS grey900 오리지널 다크 그레이 고정!
    IenSurface(
        modifier = modifier
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(22.dp), clip = false)
            .widthIn(max = IenToastDefaults.MaxWidth)
            .semantics {
                liveRegion = when (ariaLive) {
                    IenToastAriaLive.Polite -> LiveRegionMode.Polite
                    IenToastAriaLive.Assertive -> LiveRegionMode.Assertive
                }
                contentDescription = text
            },
        color = container,
        contentColor = Color.White,
        shape = RoundedCornerShape(22.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = IenTheme.spacing.lg, vertical = IenTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leftAddon?.invoke()
            IenText(
                text = text,
                modifier = Modifier.weight(1f, fill = false),
                color = Color.White,
                style = IenTheme.typography.body2,
            )
            if (button != null) {
                IenToastButton(text = button.text, onClick = button.onClick)
            }
        }
    }
}

@Composable
fun IenToastButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IenText(
        text = text,
        modifier = modifier
            .clip(RoundedCornerShape(IenTheme.radius.sm))
            .clickable(onClick = onClick)
            .padding(horizontal = IenTheme.spacing.sm, vertical = IenTheme.spacing.xs),
        color = Color(0xFF3182F6),
        style = IenTheme.typography.label1.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
    )
}

@Composable
fun IenToastIcon(
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Success,
    size: Dp = 20.dp,
) {
    val color = when (tone) {
        IenSemanticTone.Neutral -> IenTheme.colors.surface
        IenSemanticTone.Brand -> IenTheme.colors.brand
        IenSemanticTone.Success -> IenTheme.colors.success
        IenSemanticTone.Warning -> IenTheme.colors.warning
        IenSemanticTone.Danger -> IenTheme.colors.danger
        IenSemanticTone.Info -> IenTheme.colors.info
    }
    val iconVector = when (tone) {
        IenSemanticTone.Success -> RemixIcons.Fill.Check
        IenSemanticTone.Danger -> RemixIcons.Fill.Close
        else -> RemixIcons.Fill.Check
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center,
    ) {
        IenIcon(
            imageVector = iconVector,
            contentDescription = null,
            modifier = Modifier.size(size * 0.7f),
            tint = color,
        )
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

    val phase = rememberIenSkeletonPhase()
    val colors = skeletonColors(background)

    if (height != null) {
        IenSkeletonBlock(
            modifier = modifier.fillMaxWidth(),
            height = height,
            radius = radius,
            color = colors.base,
            phase = phase,
            animationIndex = 0,
        )
        return
    }

    val elements = (custom ?: pattern.elements()).withRepeatedLast(repeatLastItemCount)
    val contentDescription = stringResource(Res.string.loading)
    Column(
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
            liveRegion = LiveRegionMode.Polite
        },
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
    ) {
        elements.forEachIndexed { index, element ->
            IenSkeletonElementView(
                element = element,
                index = index,
                radius = radius,
                color = colors.base,
                phase = phase,
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

@Composable
fun IenSkeletonMotionGroup(
    modifier: Modifier = Modifier,
    animationIndex: Int = 0,
    content: @Composable BoxScope.() -> Unit,
) {
    val phase = rememberIenSkeletonPhase()
    Box(
        modifier = modifier.ienSkeletonMotion(phase = phase, animationIndex = animationIndex),
    ) {
        CompositionLocalProvider(LocalIenSkeletonBlockMotionEnabled provides false) {
            content()
        }
    }
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
)

private const val IenSkeletonMotionDurationMillis = 1200L

private val LocalIenSkeletonBlockMotionEnabled = staticCompositionLocalOf { true }

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
        base = Color.White,
    )

    IenSkeletonBackground.Grey -> IenSkeletonColors(
        base = Color(0xFFF2F4F6),
    )

    IenSkeletonBackground.GreyOpacity100 -> IenSkeletonColors(
        base = Color(0x0D022047),
    )
}

@Composable
private fun ColumnScope.IenSkeletonElementView(
    element: IenSkeletonElement,
    index: Int,
    radius: Dp,
    color: Color,
    phase: Float,
) {
    when (element) {
        IenSkeletonElement.Title -> IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(0.42f),
            height = 24.dp,
            radius = radius,
            color = color,
            phase = phase,
            animationIndex = index,
        )

        IenSkeletonElement.Subtitle -> IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(0.34f),
            height = 16.dp,
            radius = radius,
            color = color,
            phase = phase,
            animationIndex = index,
        )

        IenSkeletonElement.List -> IenSkeletonListRow(
            index = index,
            radius = radius,
            color = color,
            phase = phase,
        )

        IenSkeletonElement.ListWithIcon -> IenSkeletonListRowWithIcon(
            index = index,
            radius = radius,
            color = color,
            phase = phase,
        )

        IenSkeletonElement.Card -> IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(),
            height = 132.dp,
            radius = IenTheme.radius.default,
            color = color,
            phase = phase,
            animationIndex = index,
        )

        is IenSkeletonElement.Spacer -> Spacer(modifier = Modifier.height(element.height))
    }
}

@Composable
private fun IenSkeletonListRow(
    index: Int,
    radius: Dp,
    color: Color,
    phase: Float,
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
            color = color,
            phase = phase,
            animationIndex = index,
        )
    }
}

@Composable
private fun IenSkeletonListRowWithIcon(
    index: Int,
    radius: Dp,
    color: Color,
    phase: Float,
) {
    val primaryWidthFraction = when (index % 3) {
        0 -> 0.70f
        1 -> 0.56f
        else -> 0.64f
    }
    val secondaryWidthFraction = when (index % 3) {
        0 -> 0.38f
        1 -> 0.44f
        else -> 0.32f
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .ienSkeletonMotion(phase = phase, animationIndex = index)
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        ) {
            IenSkeletonBlock(
                modifier = Modifier.fillMaxWidth(primaryWidthFraction),
                height = 18.dp,
                radius = radius,
                color = color,
                phase = phase,
                animationIndex = index,
                animate = false,
            )
            IenSkeletonBlock(
                modifier = Modifier.fillMaxWidth(secondaryWidthFraction),
                height = 14.dp,
                radius = radius,
                color = color,
                phase = phase,
                animationIndex = index,
                animate = false,
            )
        }
    }
}

@Composable
private fun rememberIenSkeletonPhase(): Float {
    var phase by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { frameMillis ->
                val normalized = (frameMillis % IenSkeletonMotionDurationMillis).toFloat() / IenSkeletonMotionDurationMillis
                phase = normalized * (PI * 2.0).toFloat()
            }
        }
    }
    return phase
}

@Composable
private fun IenSkeletonBlock(
    modifier: Modifier,
    height: Dp,
    radius: Dp,
    color: Color,
    phase: Float,
    animationIndex: Int,
    animate: Boolean = true,
) {
    val motionEnabled = LocalIenSkeletonBlockMotionEnabled.current
    val motionModifier = if (animate && motionEnabled) {
        Modifier.ienSkeletonMotion(phase = phase, animationIndex = animationIndex)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(motionModifier)
            .height(height)
            .clip(RoundedCornerShape(radius))
            .background(color),
    )
}

private fun Modifier.ienSkeletonMotion(
    phase: Float,
    animationIndex: Int,
): Modifier {
    val delayedPhase = phase - animationIndex * 0.78f
    val wave = ((sin(delayedPhase) + 1f) / 2f).coerceIn(0f, 1f)
    val alpha = 0.2f + 0.8f * wave
    val scale = 0.96f + 0.04f * wave

    return graphicsLayer {
        this.alpha = alpha
        scaleX = scale
        scaleY = scale
    }
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
    val defaultLabel = stringResource(Res.string.loading)
    Column(
        modifier = modifier.semantics {
            contentDescription = label ?: defaultLabel
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
    val contentDescription = stringResource(Res.string.progress_stepper_step, safeActiveStepIndex + 1, steps.size)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPadding)
            .semantics {
                this.contentDescription = contentDescription
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
                imageVector = RemixIcons.Fill.Check,
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
