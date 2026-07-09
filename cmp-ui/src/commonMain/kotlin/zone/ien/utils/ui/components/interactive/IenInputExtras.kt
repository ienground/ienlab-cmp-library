package zone.ien.utils.ui.components.interactive

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenText

@Immutable
data class IenNumericSpinnerRange(
    val min: Int = Int.MIN_VALUE,
    val max: Int = Int.MAX_VALUE,
    val step: Int = 1,
)

@Composable
fun IenNumericSpinner(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IenNumericSpinnerRange = IenNumericSpinnerRange(),
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
        SpinnerButton(
            text = "-",
            enabled = enabled && value > range.min,
            onClick = { onValueChange((value - range.step).coerceAtLeast(range.min)) },
            description = "값 감소",
        )
        IenText(
            text = value.toString(),
            modifier = Modifier
                .defaultMinSize(minWidth = 36.dp)
                .semantics { contentDescription = "현재 값 $value" },
            style = IenTheme.typography.label1,
        )
        SpinnerButton(
            text = "+",
            enabled = enabled && value < range.max,
            onClick = { onValueChange((value + range.step).coerceAtMost(range.max)) },
            description = "값 증가",
        )
    }
}

@Composable
private fun SpinnerButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    description: String,
) {
    IenText(
        text = text,
        modifier = Modifier
            .defaultMinSize(minWidth = IenTheme.state.minimumTouchTarget, minHeight = IenTheme.state.minimumTouchTarget)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = IenTheme.spacing.sm)
            .semantics { contentDescription = description },
        style = IenTheme.typography.title3,
        color = if (enabled) IenTheme.colors.brand else IenTheme.colors.textDisabled,
    )
}

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
