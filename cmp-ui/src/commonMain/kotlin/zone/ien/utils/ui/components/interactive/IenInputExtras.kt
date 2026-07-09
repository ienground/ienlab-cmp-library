package zone.ien.utils.ui.components.interactive

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.snap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText
import zone.ien.utils.ui.utils.instantPress
import kotlin.math.roundToInt

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

@Composable
fun IenRating(
    value: Float,
    onValueChange: ((Float) -> Unit)? = null,
    modifier: Modifier = Modifier,
    max: Int = 5,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier.semantics {
            contentDescription = "평점 ${value.coerceIn(0f, max.toFloat())} / $max"
        },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
    ) {
        repeat(max.coerceAtLeast(1)) { index ->
            val selected = value >= index + 1
            Canvas(
                modifier = Modifier
                    .defaultMinSize(minWidth = IenTheme.state.minimumTouchTarget, minHeight = IenTheme.state.minimumTouchTarget)
                    .clickable(enabled = enabled && onValueChange != null) { onValueChange?.invoke((index + 1).toFloat()) },
            ) {
                drawStar(selected = selected)
            }
        }
    }
}

private fun DrawScope.drawStar(selected: Boolean) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val outer = size.minDimension * 0.34f
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
    drawPath(
        path = path,
        color = if (selected) androidx.compose.ui.graphics.Color(0xFFFFB020) else androidx.compose.ui.graphics.Color(0xFFD1D6DB),
    )
}
