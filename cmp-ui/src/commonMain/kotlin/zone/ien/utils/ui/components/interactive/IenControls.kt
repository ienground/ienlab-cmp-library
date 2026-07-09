package zone.ien.utils.ui.components.interactive

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToLong
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

@Composable
fun IenSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
    label: String? = null,
    valueLabel: String? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (label != null) {
            IenText(label, modifier = Modifier.weight(0.8f), style = IenTheme.typography.body2)
        }
        Slider(
            value = value.coerceIn(valueRange.start, valueRange.endInclusive),
            onValueChange = onValueChange,
            modifier = Modifier.weight(1.4f),
            valueRange = valueRange,
            steps = steps,
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = IenTheme.colors.brand,
                activeTrackColor = IenTheme.colors.brand,
                inactiveTrackColor = IenTheme.colors.brandWeak,
            ),
        )
        if (valueLabel != null) {
            IenText(valueLabel, style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
        }
    }
}

@Immutable
data class IenStepperRange(
    val min: Int = Int.MIN_VALUE,
    val max: Int = Int.MAX_VALUE,
    val step: Int = 1,
)

@Composable
fun IenStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IenStepperRange = IenStepperRange(),
    enabled: Boolean = true,
    label: String? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (label != null) {
            IenText(label, modifier = Modifier.weight(1f), style = IenTheme.typography.body2)
        }
        StepperAction(
            text = "-",
            enabled = enabled && value > range.min,
            onClick = { onValueChange((value - range.step).coerceAtLeast(range.min)) },
        )
        IenText(value.toString(), style = IenTheme.typography.label1)
        StepperAction(
            text = "+",
            enabled = enabled && value < range.max,
            onClick = { onValueChange((value + range.step).coerceAtMost(range.max)) },
        )
    }
}

@Composable
fun IenStepper(
    modifier: Modifier = Modifier,
    play: Boolean = true,
    delay: Float = 0f,
    staggerDelay: Float = 0.1f,
    content: @Composable IenStepperScope.() -> Unit,
) {
    val scope = remember { IenStepperScope() }
    scope.play = play
    scope.delayMillis = (delay * 1000).roundToLong()
    scope.staggerDelayMillis = (staggerDelay * 1000).roundToLong()
    scope.rowIndex = 0

    Column(
        modifier = modifier.semantics {
            contentDescription = "단계 목록"
        },
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        scope.content()
    }
}

@Stable
class IenStepperScope internal constructor() {
    internal var rowIndex: Int = 0
    internal var play: Boolean = true
    internal var delayMillis: Long = 0L
    internal var staggerDelayMillis: Long = 100L

    @Composable
    fun Row(
        left: @Composable () -> Unit,
        center: @Composable ColumnScope.() -> Unit,
        modifier: Modifier = Modifier,
        right: (@Composable () -> Unit)? = null,
        hideLine: Boolean = false,
    ) {
        val index = rowIndex++
        IenStepperRow(
            left = left,
            center = center,
            modifier = modifier,
            right = right,
            hideLine = hideLine,
            play = play,
            delayMillis = delayMillis + (index * staggerDelayMillis),
        )
    }
}

@Composable
fun IenStepperRow(
    left: @Composable () -> Unit,
    center: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    right: (@Composable () -> Unit)? = null,
    hideLine: Boolean = false,
    play: Boolean = false,
    delayMillis: Long = 0L,
) {
    var visible by remember { mutableStateOf(!play) }

    LaunchedEffect(play) {
        if (play && !visible) {
            delay(delayMillis.coerceAtLeast(0L))
            visible = true
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = IenTheme.motion.normalMillis, easing = IenTheme.motion.standardEasing),
        ) + slideInVertically(
            initialOffsetY = { it / 5 },
            animationSpec = tween(durationMillis = IenTheme.motion.normalMillis, easing = IenTheme.motion.standardEasing),
        ),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 72.dp)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.width(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    left()
                }
                if (!hideLine) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(IenTheme.stroke.thick)
                            .height(28.dp)
                            .clip(RoundedCornerShape(IenTheme.radius.full))
                            .graphicsLayer { alpha = 0.85f }
                            .background(IenTheme.colors.border),
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 2.dp, bottom = IenTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
            ) {
                center()
            }

            if (right != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .height(40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    right()
                }
            }
        }
    }
}

enum class IenStepperTextsType {
    A,
    B,
    C,
}

@Composable
fun IenStepperTexts(
    type: IenStepperTextsType,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    val titleStyle = when (type) {
        IenStepperTextsType.A -> IenTheme.typography.label1
        IenStepperTextsType.B -> IenTheme.typography.title3
        IenStepperTextsType.C -> IenTheme.typography.label1
    }
    val descriptionStyle = when (type) {
        IenStepperTextsType.A,
        IenStepperTextsType.B -> IenTheme.typography.body2
        IenStepperTextsType.C -> IenTheme.typography.caption
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        IenText(
            text = title,
            style = titleStyle,
            color = IenTheme.colors.textPrimary,
        )
        if (description != null) {
            IenText(
                text = description,
                style = descriptionStyle,
                color = IenTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
fun IenStepperNumberIcon(
    number: Int,
    modifier: Modifier = Modifier,
) {
    val safeNumber = number.coerceIn(1, 9)
    IenSurface(
        modifier = modifier.size(28.dp),
        color = IenTheme.colors.brandWeak,
        contentColor = IenTheme.colors.brand,
        shape = RoundedCornerShape(IenTheme.radius.full),
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.brand.copy(alpha = 0.18f)),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            IenText(
                text = safeNumber.toString(),
                style = IenTheme.typography.label2,
                color = IenTheme.colors.brand,
            )
        }
    }
}

enum class IenStepperAssetFrameShape {
    CircleMedium,
    RoundedMedium,
    CleanW24,
    CleanW32,
}

@Composable
fun IenStepperAssetFrame(
    shape: IenStepperAssetFrameShape,
    modifier: Modifier = Modifier,
    backgroundColor: Color = IenTheme.colors.surfaceWeak,
    content: @Composable () -> Unit,
) {
    val size = when (shape) {
        IenStepperAssetFrameShape.CircleMedium,
        IenStepperAssetFrameShape.RoundedMedium,
        IenStepperAssetFrameShape.CleanW32 -> 32.dp
        IenStepperAssetFrameShape.CleanW24 -> 24.dp
    }
    val resolvedShape: Shape = when (shape) {
        IenStepperAssetFrameShape.CircleMedium -> RoundedCornerShape(IenTheme.radius.full)
        IenStepperAssetFrameShape.RoundedMedium -> RoundedCornerShape(IenTheme.radius.default)
        IenStepperAssetFrameShape.CleanW24,
        IenStepperAssetFrameShape.CleanW32 -> RoundedCornerShape(0.dp)
    }

    IenSurface(
        modifier = modifier.size(size),
        color = backgroundColor,
        shape = resolvedShape,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

@Composable
fun IenStepperRightArrow(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.textTertiary,
    frameSize: Dp = 24.dp,
) {
    Box(
        modifier = modifier
            .size(frameSize)
            .semantics { contentDescription = "다음" },
        contentAlignment = Alignment.Center,
    ) {
        IenText(
            text = ">",
            style = IenTheme.typography.title3,
            color = color,
        )
    }
}

@Composable
fun IenStepperRightButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Small,
    variant: IenButtonVariant = IenButtonVariant.Weak,
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        size = size,
        variant = variant,
    )
}

@Composable
private fun StepperAction(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    IenSurface(
        color = if (enabled) IenTheme.colors.brandWeak else IenTheme.colors.surfaceWeak,
        contentColor = if (enabled) IenTheme.colors.brand else IenTheme.colors.textDisabled,
        shape = RoundedCornerShape(IenTheme.radius.full),
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = IenTheme.state.minimumTouchTarget, minHeight = IenTheme.state.minimumTouchTarget)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            IenText(text, style = IenTheme.typography.title3, color = if (enabled) IenTheme.colors.brand else IenTheme.colors.textDisabled)
        }
    }
}

@Immutable
data class IenTabItem(
    val text: String,
    val enabled: Boolean = true,
)

@Composable
fun IenTab(
    items: List<IenTabItem>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
                    .clickable(enabled = item.enabled, role = Role.Tab) { onSelectedIndexChange(index) }
                    .padding(vertical = IenTheme.spacing.xs),
                contentAlignment = Alignment.Center,
            ) {
                IenText(
                    text = item.text,
                    style = IenTheme.typography.label1,
                    color = when {
                        !item.enabled -> IenTheme.colors.textDisabled
                        selected -> IenTheme.colors.brand
                        else -> IenTheme.colors.textSecondary
                    },
                )
            }
        }
    }
}
