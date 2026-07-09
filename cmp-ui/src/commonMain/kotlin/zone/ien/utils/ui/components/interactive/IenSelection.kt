package zone.ien.utils.ui.components.interactive

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.graphics.graphicsLayer
import zone.ien.utils.ui.utils.instantPress
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

@Composable
fun IenCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
            .clickable(enabled = enabled, role = Role.Checkbox) { onCheckedChange(!checked) },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = IenTheme.colors.brand,
                uncheckedColor = IenTheme.colors.borderStrong,
                checkmarkColor = IenTheme.colors.surface,
            ),
        )
        if (label != null) {
            IenText(
                text = label,
                style = IenTheme.typography.body2,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}

@Composable
fun IenSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = IenTheme.colors.surface,
            checkedTrackColor = IenTheme.colors.brand,
            uncheckedThumbColor = IenTheme.colors.surface,
            uncheckedTrackColor = IenTheme.colors.borderStrong,
        ),
    )
}

enum class IenSegmentedControlSize {
    Small,
    Large,
}

enum class IenSegmentedControlAlignment {
    Fixed,
    Fluid,
}

data class IenSegmentedControlItem(
    val value: String,
    val label: String,
    val enabled: Boolean = true,
    val size: IenSegmentedControlSize? = null,
)

@Composable
fun IenSegmentedControl(
    items: List<IenSegmentedControlItem>,
    modifier: Modifier = Modifier,
    value: String? = null,
    defaultValue: String? = null,
    onChange: (String) -> Unit = {},
    size: IenSegmentedControlSize = IenSegmentedControlSize.Small,
    alignment: IenSegmentedControlAlignment = IenSegmentedControlAlignment.Fixed,
    enabled: Boolean = true,
) {
    var localValue by remember(items, defaultValue) {
        mutableStateOf(defaultValue ?: items.firstOrNull { it.enabled }?.value ?: items.firstOrNull()?.value.orEmpty())
    }
    val selectedValue = value ?: localValue

    val height = when (size) {
        IenSegmentedControlSize.Small -> 40.dp
        IenSegmentedControlSize.Large -> 48.dp
    }
    val selectedIndex = items.indexOfFirst { it.value == selectedValue }.coerceAtLeast(0)

    IenSurface(
        modifier = modifier,
        color = IenTheme.colors.surfaceVariant,
        shape = RoundedCornerShape(IenTheme.radius.full),
    ) {
        Row(
            modifier = Modifier
                .then(if (alignment == IenSegmentedControlAlignment.Fixed) Modifier.fillMaxWidth() else Modifier.horizontalScroll(rememberScrollState()))
                .height(height)
                .padding(IenTheme.spacing.xxs)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                val itemSize = item.size ?: size
                val currentItemMinWidth = when (itemSize) {
                    IenSegmentedControlSize.Small -> 64.dp
                    IenSegmentedControlSize.Large -> 80.dp
                }
                val currentItemHorizontalPadding = when (itemSize) {
                    IenSegmentedControlSize.Small -> IenTheme.spacing.sm
                    IenSegmentedControlSize.Large -> IenTheme.spacing.md
                }
                val currentTextStyle = when (itemSize) {
                    IenSegmentedControlSize.Small -> IenTheme.typography.label2
                    IenSegmentedControlSize.Large -> IenTheme.typography.label1
                }
                val itemEnabled = enabled && item.enabled
                val selected = item.value == selectedValue
                val distanceFromSelected = abs(index - selectedIndex)
                val rippleDelay = (distanceFromSelected * 34).coerceAtMost(136)
                val targetScale = when {
                    selected -> 1.0f
                    distanceFromSelected == 1 -> 0.975f
                    else -> 0.955f
                }
                val targetAlpha = when {
                    !itemEnabled -> IenTheme.state.disabledAlpha
                    selected -> 1.0f
                    distanceFromSelected == 1 -> 0.88f
                    else -> 0.78f
                }
                val targetBlur = when {
                    selected -> 0f
                    distanceFromSelected == 1 -> 0.18f
                    else -> 0.32f
                }
                val targetTranslationY = when {
                    selected -> -1.5f
                    distanceFromSelected == 1 -> 0.5f
                    else -> 0f
                }
                val morphScale by animateFloatAsState(
                    targetValue = targetScale,
                    animationSpec = tween(
                        durationMillis = 260,
                        delayMillis = rippleDelay,
                        easing = IenTheme.motion.standardEasing,
                    ),
                    label = "ienSegmentedControlScale",
                )
                val morphAlpha by animateFloatAsState(
                    targetValue = targetAlpha,
                    animationSpec = tween(
                        durationMillis = 220,
                        delayMillis = rippleDelay,
                        easing = IenTheme.motion.standardEasing,
                    ),
                    label = "ienSegmentedControlAlpha",
                )
                val morphBlur by animateFloatAsState(
                    targetValue = targetBlur,
                    animationSpec = tween(
                        durationMillis = 220,
                        delayMillis = rippleDelay,
                        easing = IenTheme.motion.standardEasing,
                    ),
                    label = "ienSegmentedControlBlur",
                )
                val morphTranslationY by animateFloatAsState(
                    targetValue = targetTranslationY,
                    animationSpec = tween(
                        durationMillis = 260,
                        delayMillis = rippleDelay,
                        easing = IenTheme.motion.standardEasing,
                    ),
                    label = "ienSegmentedControlTranslationY",
                )
                val backgroundColor by animateColorAsState(
                    targetValue = if (selected) IenTheme.colors.surface else Color.Transparent,
                    animationSpec = tween(
                        durationMillis = 220,
                        delayMillis = rippleDelay,
                        easing = IenTheme.motion.standardEasing,
                    ),
                )
                val textColor by animateColorAsState(
                    targetValue = when {
                        !itemEnabled -> IenTheme.colors.textDisabled
                        selected -> IenTheme.colors.textPrimary
                        else -> IenTheme.colors.textSecondary
                    },
                    animationSpec = tween(
                        durationMillis = 180,
                        delayMillis = rippleDelay,
                        easing = IenTheme.motion.standardEasing,
                    ),
                )
                val itemModifier = Modifier
                    .then(
                        if (alignment == IenSegmentedControlAlignment.Fixed) {
                            Modifier.weight(1f)
                        } else {
                            Modifier.widthIn(min = currentItemMinWidth)
                        }
                    )
                    .height(height - IenTheme.spacing.xxs * 2)
                    .graphicsLayer {
                        alpha = morphAlpha
                        scaleX = morphScale
                        scaleY = morphScale
                        translationY = morphTranslationY
                        transformOrigin = TransformOrigin.Center
                    }
                    .blur(morphBlur.dp)
                    .selectable(
                        selected = selected,
                        enabled = itemEnabled,
                        role = Role.RadioButton,
                    ) {
                        if (value == null) {
                            localValue = item.value
                        }
                        onChange(item.value)
                    }

                IenSurface(
                    modifier = itemModifier,
                    color = backgroundColor,
                    contentColor = textColor,
                    shape = RoundedCornerShape(IenTheme.radius.full),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height - IenTheme.spacing.xxs * 2)
                            .padding(horizontal = currentItemHorizontalPadding),
                        contentAlignment = Alignment.Center,
                    ) {
                        IenText(
                            text = item.label,
                            style = currentTextStyle,
                            color = textColor,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IenSegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IenSegmentedControl(
        items = items.mapIndexed { index, label ->
            IenSegmentedControlItem(
                value = index.toString(),
                label = label,
            )
        },
        modifier = modifier,
        value = selectedIndex.toString(),
        onChange = { value -> onSelectedIndexChange(value.toInt()) },
        enabled = enabled,
    )
}

@Composable
private fun IenCheckMark(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 2.5.dp.toPx()
        val path = Path().apply {
            moveTo(size.width * 0.27f, size.height * 0.50f)
            lineTo(size.width * 0.44f, size.height * 0.67f)
            lineTo(size.width * 0.73f, size.height * 0.36f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
fun IenCircleCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean? = null,
    defaultChecked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 24.dp,
    label: String? = null,
) {
    val alpha = if (enabled) 1f else IenTheme.state.disabledAlpha
    val brandColor = IenTheme.colors.brand
    val borderColor = IenTheme.colors.borderStrong

    var localCheckedState by remember { mutableStateOf(defaultChecked ?: false) }
    val isChecked = checked ?: localCheckedState

    val toggleChecked = {
        val target = !isChecked
        if (checked == null) {
            localCheckedState = target
        }
        onCheckedChange?.invoke(target)
    }

    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val density = LocalDensity.current
    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val handleClick = {
        if (enabled) {
            toggleChecked()
        } else {
            coroutineScope.launch {
                val shakeDistance = with(density) { 6.dp.toPx() }
                shakeOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 200
                        -shakeDistance at 30
                        shakeDistance at 70
                        -shakeDistance * 0.6f at 110
                        shakeDistance * 0.6f at 150
                        0f at 200
                    }
                )
            }
        }
    }

    val targetBgColor = if (isChecked) brandColor else Color.Transparent
    val targetBorderColor = if (isChecked) brandColor else borderColor
    val targetCheckColor = if (isChecked) Color.White else borderColor

    val backgroundColor by animateColorAsState(
        targetValue = targetBgColor,
        animationSpec = tween(durationMillis = 200)
    )
    val borderAnimColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(durationMillis = 200)
    )
    val checkAnimColor by animateColorAsState(
        targetValue = targetCheckColor,
        animationSpec = tween(durationMillis = 200)
    )

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
            .instantPress(enabled) { isPressed = it }
            .clickable(
                enabled = true,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Checkbox
            ) { handleClick() },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(x = shakeOffset.value.roundToInt(), y = 0) }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .alpha(alpha)
                .size(size)
                .background(
                    color = backgroundColor,
                    shape = androidx.compose.foundation.shape.CircleShape
                )
                .border(
                    BorderStroke(1.5.dp, borderAnimColor),
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            IenCheckMark(
                color = checkAnimColor,
                modifier = Modifier.size(size * 0.65f)
            )
        }
        if (label != null) {
            IenText(
                text = label,
                style = IenTheme.typography.body2,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}

@Composable
fun IenLineCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean? = null,
    defaultChecked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 24.dp,
    label: String? = null,
) {
    val alpha = if (enabled) 1f else IenTheme.state.disabledAlpha
    val activeColor = IenTheme.colors.brand
    val inactiveColor = IenTheme.colors.borderStrong

    var localCheckedState by remember { mutableStateOf(defaultChecked ?: false) }
    val isChecked = checked ?: localCheckedState

    val toggleChecked = {
        val target = !isChecked
        if (checked == null) {
            localCheckedState = target
        }
        onCheckedChange?.invoke(target)
    }

    val targetCheckColor = if (isChecked) activeColor else inactiveColor
    val checkAnimColor by animateColorAsState(
        targetValue = targetCheckColor,
        animationSpec = tween(durationMillis = 200)
    )

    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val density = LocalDensity.current
    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val handleClick = {
        if (enabled) {
            toggleChecked()
        } else {
            coroutineScope.launch {
                val shakeDistance = with(density) { 6.dp.toPx() }
                shakeOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 200
                        -shakeDistance at 30
                        shakeDistance at 70
                        -shakeDistance * 0.6f at 110
                        shakeDistance * 0.6f at 150
                        0f at 200
                    }
                )
            }
        }
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
            .instantPress(enabled) { isPressed = it }
            .clickable(
                enabled = true,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Checkbox
            ) { handleClick() },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IenCheckMark(
            color = checkAnimColor,
            modifier = Modifier
                .offset { IntOffset(x = shakeOffset.value.roundToInt(), y = 0) }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .alpha(alpha)
                .size(size)
        )
        if (label != null) {
            IenText(
                text = label,
                style = IenTheme.typography.body2,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}
