package zone.ien.utils.ui.components.interactive

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText
import zone.ien.utils.ui.utils.instantPress
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

enum class IenNumericSpinnerSize {
    Tiny,
    Small,
    Medium,
    Large,
}

@Composable
fun IenNumericSpinner(
    modifier: Modifier = Modifier,
    size: IenNumericSpinnerSize = IenNumericSpinnerSize.Medium,
    number: Int? = null,
    defaultNumber: Int = 0,
    minNumber: Int = 0,
    maxNumber: Int = 999,
    disable: Boolean = false,
    decreaseAriaLabel: String = "빼기",
    increaseAriaLabel: String = "더하기",
    onNumberChange: (Int) -> Unit = {},
) {
    val min = minOf(minNumber, maxNumber)
    val max = maxOf(minNumber, maxNumber)
    val step = 1
    var internalNumber by remember(min, max) { mutableIntStateOf(defaultNumber.coerceIn(min, max)) }
    val currentNumber = (number ?: internalNumber).coerceIn(min, max)
    val spec = numericSpinnerSpec(size)
    val canDecrease = !disable && currentNumber > min
    val canIncrease = !disable && currentNumber < max
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current
    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    var swipingNumber by remember { mutableStateOf(false) }
    var dragOffsetPx by remember { mutableFloatStateOf(0f) }
    val numberOffsetPx by animateFloatAsState(
        targetValue = if (swipingNumber) dragOffsetPx else 0f,
        animationSpec = if (swipingNumber) {
            snap()
        } else {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow,
            )
        },
    )
    val spinnerFullRadius = spec.height * 0.5f
    val numberFullRadius = spec.numberBoxHeight * 0.5f

    val spinnerRadius by animateDpAsState(
        targetValue = if (swipingNumber) spinnerFullRadius else spec.radius,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
    )
    val numberRadius by animateDpAsState(
        targetValue = if (swipingNumber) numberFullRadius else spec.numberRadius,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
    )

    var isMinusPressed by remember { mutableStateOf(false) }
    var isPlusPressed by remember { mutableStateOf(false) }
    val isAnyPressed = (isMinusPressed && canDecrease) || (isPlusPressed && canIncrease)

    val totalScale by animateFloatAsState(
        targetValue = if (isAnyPressed) 0.97f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    fun shake() {
        coroutineScope.launch {
            val distance = with(density) { 6.dp.toPx() }
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 200
                    -distance at 30
                    distance at 70
                    -distance * 0.6f at 110
                    distance * 0.6f at 150
                    0f at 200
                },
            )
        }
    }

    fun requestNumber(nextNumber: Int, available: Boolean) {
        if (!available) {
            shake()
            return
        }
        val coerced = nextNumber.coerceIn(min, max)
        if (coerced == currentNumber) return
        if (number == null) internalNumber = coerced
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        onNumberChange(coerced)
    }

    fun settleNumberBox() {
        dragOffsetPx = 0f
        swipingNumber = false
    }

    IenSurface(
        modifier = modifier
            .graphicsLayer {
                scaleX = totalScale
                scaleY = totalScale
            }
            .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
            .height(spec.height)
            .widthIn(min = spec.minWidth)
            .semantics(mergeDescendants = true) {
                contentDescription = "숫자 스피너"
                stateDescription = currentNumber.toString()
                liveRegion = LiveRegionMode.Polite
                if (disable) disabled()
            },
        color = IenTheme.colors.surfaceVariant,
        contentColor = IenTheme.colors.textPrimary,
        shape = RoundedCornerShape(spinnerRadius),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = spec.outerHorizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.none),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SpinnerButton(
                text = "-",
                available = canDecrease,
                width = spec.actionWidth,
                height = spec.height,
                textStyle = spec.buttonTextStyle,
                description = decreaseAriaLabel,
                onPressedChange = { isMinusPressed = it },
                onClick = { requestNumber(currentNumber - step, canDecrease) },
            )
            Box(
                modifier = Modifier
                    .offset { IntOffset(numberOffsetPx.roundToInt(), 0) }
                    .zIndex(1f)
                    .width(spec.numberBoxWidth)
                    .height(spec.numberBoxHeight)
                    .shadow(
                        elevation = spec.numberElevation,
                        shape = RoundedCornerShape(numberRadius),
                        clip = false,
                    )
                    .clip(RoundedCornerShape(numberRadius))
                    .background(IenTheme.colors.surface)
                    .pointerInput(currentNumber, canDecrease, canIncrease, disable) {
                        var dragAmount = 0f
                        detectHorizontalDragGestures(
                            onDragStart = {
                                dragAmount = 0f
                                dragOffsetPx = 0f
                                swipingNumber = true
                            },
                            onHorizontalDrag = { change, amount ->
                                dragAmount += amount
                                val maxOffset = spec.actionWidth.toPx()
                                dragOffsetPx = dragAmount.coerceIn(-maxOffset, maxOffset)
                                change.consume()
                            },
                            onDragEnd = {
                                val threshold = 24.dp.toPx()
                                when {
                                    dragAmount <= -threshold -> requestNumber(currentNumber - step, canDecrease)
                                    dragAmount >= threshold -> requestNumber(currentNumber + step, canIncrease)
                                }
                                dragAmount = 0f
                                settleNumberBox()
                            },
                            onDragCancel = {
                                dragAmount = 0f
                                settleNumberBox()
                            },
                        )
                    }
                    .semantics { contentDescription = "현재 값 $currentNumber" },
                contentAlignment = Alignment.Center,
            ) {
                IenText(
                    text = currentNumber.toString(),
                    style = spec.numberTextStyle,
                    color = if (disable) IenTheme.colors.textDisabled else IenTheme.colors.textPrimary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
            SpinnerButton(
                text = "+",
                available = canIncrease,
                width = spec.actionWidth,
                height = spec.height,
                textStyle = spec.buttonTextStyle,
                description = increaseAriaLabel,
                onPressedChange = { isPlusPressed = it },
                onClick = { requestNumber(currentNumber + step, canIncrease) },
            )
        }
    }
}

@Composable
private fun SpinnerButton(
    text: String,
    available: Boolean,
    width: Dp,
    height: Dp,
    textStyle: TextStyle,
    scalePressed: Float = 0.95f,
    onPressedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    description: String,
) {
    val contentColor = if (available) IenTheme.colors.textSecondary else IenTheme.colors.textDisabled
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && available) scalePressed else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .instantPress(available) {
                isPressed = it
                onPressedChange(it)
            }
            .clickable(
                role = Role.Button,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .semantics {
                contentDescription = description
                if (!available) disabled()
            },
        contentAlignment = Alignment.Center,
    ) {
        IenText(
            text = text,
            style = textStyle,
            color = contentColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Composable
private fun numericSpinnerSpec(size: IenNumericSpinnerSize): NumericSpinnerSpec =
    when (size) {
        IenNumericSpinnerSize.Tiny -> NumericSpinnerSpec(
            height = 32.dp,
            minWidth = 92.dp,
            actionWidth = 30.dp,
            numberBoxWidth = 40.dp,
            numberBoxHeight = 28.dp,
            outerHorizontalPadding = IenTheme.spacing.xxs,
            radius = IenTheme.radius.default,
            numberRadius = IenTheme.radius.default,
            numberElevation = 3.dp,
            numberTextStyle = IenTheme.typography.label2,
            buttonTextStyle = IenTheme.typography.label1,
        )
        IenNumericSpinnerSize.Small -> NumericSpinnerSpec(
            height = 36.dp,
            minWidth = 104.dp,
            actionWidth = 34.dp,
            numberBoxWidth = 46.dp,
            numberBoxHeight = 32.dp,
            outerHorizontalPadding = IenTheme.spacing.xxs,
            radius = IenTheme.radius.default,
            numberRadius = IenTheme.radius.default,
            numberElevation = 4.dp,
            numberTextStyle = IenTheme.typography.label1,
            buttonTextStyle = IenTheme.typography.title3,
        )
        IenNumericSpinnerSize.Medium -> NumericSpinnerSpec(
            height = 44.dp,
            minWidth = 120.dp,
            actionWidth = 40.dp,
            numberBoxWidth = 56.dp,
            numberBoxHeight = 38.dp,
            outerHorizontalPadding = IenTheme.spacing.xxs,
            radius = IenTheme.radius.default,
            numberRadius = IenTheme.radius.default,
            numberElevation = 5.dp,
            numberTextStyle = IenTheme.typography.title3,
            buttonTextStyle = IenTheme.typography.title3,
        )
        IenNumericSpinnerSize.Large -> NumericSpinnerSpec(
            height = 52.dp,
            minWidth = 140.dp,
            actionWidth = 48.dp,
            numberBoxWidth = 68.dp,
            numberBoxHeight = 46.dp,
            outerHorizontalPadding = IenTheme.spacing.xxs,
            radius = IenTheme.radius.default,
            numberRadius = IenTheme.radius.default,
            numberElevation = 6.dp,
            numberTextStyle = IenTheme.typography.title2,
            buttonTextStyle = IenTheme.typography.title2,
        )
    }

@Immutable
private data class NumericSpinnerSpec(
    val height: Dp,
    val minWidth: Dp,
    val actionWidth: Dp,
    val numberBoxWidth: Dp,
    val numberBoxHeight: Dp,
    val outerHorizontalPadding: Dp,
    val radius: Dp,
    val numberRadius: Dp,
    val numberElevation: Dp,
    val numberTextStyle: TextStyle,
    val buttonTextStyle: TextStyle,
)

enum class IenRatingSize {
    Tiny,
    Small,
    Medium,
    Large,
    Big,
}

enum class IenRatingVariant {
    Full,
    Compact,
    IconOnly,
}

@Composable
fun IenRating(
    value: Float,
    onValueChange: ((Float) -> Unit)? = null,
    modifier: Modifier = Modifier,
    max: Int = 5,
    readOnly: Boolean = onValueChange == null,
    size: IenRatingSize = IenRatingSize.Medium,
    variant: IenRatingVariant = IenRatingVariant.Full,
    disabled: Boolean = false,
    enabled: Boolean = true,
    ariaLabel: String = if (readOnly) "현재 별점 현황" else "별점 평가",
    valueText: String? = null,
) {
    val itemCount = max.coerceAtLeast(1)
    val resolvedValue = value.coerceIn(0f, itemCount.toFloat())
    val latestResolvedValue = rememberUpdatedState(resolvedValue)
    val isDisabled = disabled || !enabled
    val valueChange = onValueChange
    val interactive = !readOnly && !isDisabled
    val displayVariant = if (readOnly) variant else IenRatingVariant.Full
    val hapticFeedback = LocalHapticFeedback.current
    var animationTrigger by remember { mutableIntStateOf(0) }
    var clickedIndex by remember { mutableIntStateOf(resolvedValue.roundToInt().coerceIn(1, itemCount) - 1) }
    var ratingPressed by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(IntSize.Zero) }
    val selectedIndex = resolvedValue.roundToInt().coerceIn(1, itemCount) - 1
    val resolvedValueText = valueText ?: "${itemCount}점 만점 중 ${resolvedValue.toRatingText()}점"
    val itemSpacing = size.ratingItemSpacing()

    Row(
        modifier = modifier
            .onSizeChanged { rowSize = it }
            .ratingGesture(
                enabled = interactive && valueChange != null,
                itemCount = itemCount,
                rowSize = rowSize,
                currentValue = latestResolvedValue,
                hapticFeedback = hapticFeedback,
                onValueChange = valueChange,
                onPressedChange = { ratingPressed = it },
                onClickedIndexChange = { clickedIndex = it },
                onRelease = { animationTrigger += 1 },
            )
            .selectableGroup()
            .semantics {
                contentDescription = ariaLabel
                stateDescription = resolvedValueText
                if (isDisabled) disabled()
            },
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (displayVariant) {
            IenRatingVariant.Full -> {
                repeat(itemCount) { index ->
                    IenRatingStar(
                        index = index,
                        value = resolvedValue,
                        size = size,
                        isDisabled = isDisabled,
                        pressed = ratingPressed,
                        animationTrigger = animationTrigger,
                        clickedIndex = clickedIndex,
                    )
                }
            }
            IenRatingVariant.Compact -> {
                IenRatingStar(
                    index = selectedIndex.coerceAtLeast(0),
                    value = 1f,
                    size = size,
                    isDisabled = isDisabled,
                    pressed = false,
                    animationTrigger = animationTrigger,
                    clickedIndex = 0,
                )
                IenText(
                    text = "${resolvedValue.toRatingText()}",
                    style = size.ratingLabelStyle(),
                    color = if (isDisabled) IenTheme.colors.textDisabled else IenTheme.colors.textSecondary,
                )
            }
            IenRatingVariant.IconOnly -> {
                IenRatingStar(
                    index = selectedIndex.coerceAtLeast(0),
                    value = 1f,
                    size = size,
                    isDisabled = isDisabled,
                    pressed = false,
                    animationTrigger = animationTrigger,
                    clickedIndex = 0,
                )
            }
        }
    }
}

private fun Modifier.ratingGesture(
    enabled: Boolean,
    itemCount: Int,
    rowSize: IntSize,
    currentValue: State<Float>,
    hapticFeedback: HapticFeedback,
    onValueChange: ((Float) -> Unit)?,
    onPressedChange: (Boolean) -> Unit,
    onClickedIndexChange: (Int) -> Unit,
    onRelease: () -> Unit,
): Modifier {
    if (!enabled || onValueChange == null) return this

    fun ratingFromX(x: Float): Int? {
        val width = rowSize.width.takeIf { it > 0 } ?: return null
        val progress = x.coerceIn(0f, width.toFloat()) / width.toFloat()
        return ((progress * itemCount).toInt() + 1).coerceIn(1, itemCount)
    }

    fun updateRatingFromX(x: Float, lastRating: Int, forceEffect: Boolean): Int {
        val nextRating = ratingFromX(x) ?: return lastRating
        onClickedIndexChange(nextRating - 1)
        if (nextRating != lastRating) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onValueChange(nextRating.toFloat())
            return nextRating
        }
        if (forceEffect) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
        return lastRating
    }

    return pointerInput(itemCount, rowSize) {
        awaitEachGesture {
            try {
                val down = awaitFirstDown(requireUnconsumed = false)
                var lastRating = currentValue.value.roundToInt().coerceIn(1, itemCount)
                onPressedChange(true)
                lastRating = updateRatingFromX(down.position.x, lastRating, forceEffect = true)

                while (true) {
                    val event = awaitPointerEvent()
                    val change = event.changes.firstOrNull { it.id == down.id } ?: event.changes.firstOrNull()
                    if (change == null || !change.pressed) break
                    lastRating = updateRatingFromX(change.position.x, lastRating, forceEffect = false)
                    change.consume()
                }
            } finally {
                onPressedChange(false)
                onRelease()
            }
        }
    }
}

private const val RatingGlowLayerScale = 5.0f
private const val RatingPressedScale = 0.86f
private const val RatingSelectedBaseScale = 1f
private const val RatingEmptyBaseScale = 0.92f

@Composable
private fun IenRatingStar(
    index: Int,
    value: Float,
    size: IenRatingSize,
    isDisabled: Boolean,
    pressed: Boolean,
    animationTrigger: Int,
    clickedIndex: Int,
) {
    val iconSize = size.ratingIconSize()
    val motionEasing = IenTheme.motion.standardEasing
    val fillFraction = (value - index).coerceIn(0f, 1f)
    val isFilled = fillFraction > 0f
    val baseScale = if (isFilled) RatingSelectedBaseScale else RatingEmptyBaseScale
    val starScale = remember { Animatable(baseScale) }
    val glowAlpha = remember { Animatable(0f) }
    val pressScale by animateFloatAsState(
        targetValue = if (pressed && isFilled) RatingPressedScale else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh, dampingRatio = Spring.DampingRatioMediumBouncy),
    )
    val pressGlow by animateFloatAsState(
        targetValue = if (pressed && isFilled) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioNoBouncy),
    )
    val glowValue = (glowAlpha.value + pressGlow).coerceIn(0f, 1.2f)
    val starAlpha by animateFloatAsState(
        targetValue = when {
            isDisabled -> 0.42f
            isFilled -> 1f
            else -> 0.72f
        },
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioNoBouncy),
    )

    LaunchedEffect(baseScale) {
        if (animationTrigger == 0) {
            starScale.snapTo(baseScale)
        }
    }

    LaunchedEffect(animationTrigger, isFilled) {
        if (animationTrigger == 0) {
            glowAlpha.snapTo(0f)
            starScale.snapTo(baseScale)
            return@LaunchedEffect
        }

        if (isFilled && !isDisabled) {
            val delayMillis = (abs(index - clickedIndex) * 12L).coerceAtMost(48L)
            val popScale = if (index == clickedIndex) 1.26f else 1.2f
            delay(delayMillis.milliseconds)
            starScale.snapTo(0.9f)
            glowAlpha.snapTo(1.15f)
            launch {
                glowAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 260, easing = motionEasing),
                )
            }
            starScale.animateTo(
                targetValue = popScale,
                animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioMediumBouncy),
            )
            starScale.animateTo(
                targetValue = baseScale,
                animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioMediumBouncy),
            )
        } else {
            glowAlpha.snapTo(0f)
            starScale.animateTo(
                targetValue = 0.92f,
                animationSpec = tween(durationMillis = 90, easing = motionEasing),
            )
        }
    }

    val itemModifier = Modifier
        .size(iconSize)
        .semantics {
            contentDescription = "${index + 1}점"
            selected = fillFraction > 0f
            if (isDisabled) disabled()
        }

    Box(
        modifier = itemModifier,
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .size(iconSize * RatingGlowLayerScale)
                .graphicsLayer {
                    val glowScale = 0.78f + glowValue * 0.42f
                    scaleX = glowScale
                    scaleY = glowScale
                    alpha = glowValue.coerceAtMost(1f)
                },
        ) {
            drawRatingGlow(
                alpha = glowValue,
                fillFraction = fillFraction,
                isDisabled = isDisabled,
            )
        }
        Canvas(
            modifier = Modifier
                .size(iconSize)
                .graphicsLayer {
                    val scale = starScale.value * pressScale
                    scaleX = scale
                    scaleY = scale
                    alpha = starAlpha
                },
        ) {
            drawStar(fillFraction = fillFraction, isDisabled = isDisabled)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    IenTheme {
        Scaffold {
            Box(
                modifier = Modifier.padding(it)
            ) {
                IenRating(
                    value = 3f,
                    onValueChange = {},
                    size = IenRatingSize.Medium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun IenRatingSize.ratingIconSize(): Dp {
    return when (this) {
        IenRatingSize.Tiny -> 18.dp
        IenRatingSize.Small -> 24.dp
        IenRatingSize.Medium -> 34.dp
        IenRatingSize.Large -> 44.dp
        IenRatingSize.Big -> 56.dp
    }
}

@Composable
private fun IenRatingSize.ratingItemSpacing(): Dp {
    return when (this) {
        IenRatingSize.Tiny,
        IenRatingSize.Small -> 2.dp
        IenRatingSize.Medium -> IenTheme.spacing.xxs
        IenRatingSize.Large,
        IenRatingSize.Big -> IenTheme.spacing.xs
    }
}

@Composable
private fun IenRatingSize.ratingLabelStyle(): TextStyle {
    return when (this) {
        IenRatingSize.Tiny,
        IenRatingSize.Small -> IenTheme.typography.caption
        IenRatingSize.Medium -> IenTheme.typography.label2
        IenRatingSize.Large,
        IenRatingSize.Big -> IenTheme.typography.label1
    }
}

private fun Float.toRatingText(): String {
    return if (this % 1f == 0f) {
        roundToInt().toString()
    } else {
        val rounded = (this * 10f).roundToInt() / 10f
        rounded.toString()
    }
}

private fun DrawScope.drawStar(fillFraction: Float, isDisabled: Boolean) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val outer = size.minDimension * 0.46f
    val inner = outer * 0.46f
    val path = Path()
    repeat(10) { index ->
        val angle = ((index * 36.0) - 90.0) * kotlin.math.PI / 180.0
        val radius = if (index % 2 == 0) outer else inner
        val point = Offset(
            x = center.x + kotlin.math.cos(angle).toFloat() * radius,
            y = center.y + kotlin.math.sin(angle).toFloat() * radius,
        )
        if (index == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
    }
    path.close()
    val emptyColor = if (isDisabled) Color(0xFFD1D6DB) else Color(0xFFE5E8EB)
    val selectedColor = if (isDisabled) Color(0xFFB0B8C1) else Color(0xFFFFC84D)
    drawPath(
        path = path,
        color = emptyColor,
    )
    if (fillFraction > 0f) {
        clipRect(right = size.width * fillFraction.coerceIn(0f, 1f)) {
            drawPath(path = path, color = selectedColor)
        }
    }
}

private fun DrawScope.drawRatingGlow(
    alpha: Float,
    fillFraction: Float,
    isDisabled: Boolean,
) {
    if (alpha <= 0f || fillFraction <= 0f || isDisabled) return
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension * 0.3f
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFA800).copy(alpha = alpha * 0.30f),
                Color(0xFFFFC400).copy(alpha = alpha * 0.20f),
                Color(0xFFFFE08A).copy(alpha = alpha * 0.10f),
                Color.Transparent,
            ),
            center = center,
            radius = radius * 1.55f,
        ),
        radius = radius * 1.55f,
        center = center,
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFF9800).copy(alpha = alpha * 0.78f),
                Color(0xFFFFBD2E).copy(alpha = alpha * 0.56f),
                Color(0xFFFFDC73).copy(alpha = alpha * 0.34f),
                Color.Transparent,
            ),
            center = center,
            radius = radius,
        ),
        radius = radius,
        center = center,
    )
}
