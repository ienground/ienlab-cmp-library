package zone.ien.utils.ui.components.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
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
