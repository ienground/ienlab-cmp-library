package zone.ien.utils.ui.components.composite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.toneColor
import zone.ien.utils.ui.components.interactive.toneWeakColor
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

enum class IenBorderSide {
    All,
    Top,
    Bottom,
    Start,
    End,
}

@Immutable
data class IenBorderSpec(
    val side: IenBorderSide = IenBorderSide.All,
    val color: Color? = null,
    val width: Dp = 1.dp,
)

@Composable
fun IenBorder(
    modifier: Modifier = Modifier,
    spec: IenBorderSpec = IenBorderSpec(),
    shape: RoundedCornerShape = RoundedCornerShape(IenTheme.radius.default),
    padding: PaddingValues = PaddingValues(IenTheme.spacing.md),
    content: @Composable () -> Unit,
) {
    val color = spec.color ?: IenTheme.colors.border
    if (spec.side == IenBorderSide.All) {
        IenSurface(
            modifier = modifier,
            border = BorderStroke(spec.width, color),
            shape = shape,
        ) {
            Box(Modifier.padding(padding)) {
                content()
            }
        }
        return
    }

    val strokeWidth = with(LocalDensity.current) { spec.width.toPx() }
    Box(
        modifier = modifier,
    ) {
        Canvas(Modifier.matchParentSize()) {
            when (spec.side) {
                IenBorderSide.Top -> drawLine(color, Offset(0f, 0f), Offset(size.width, 0f), strokeWidth)
                IenBorderSide.Bottom -> drawLine(color, Offset(0f, size.height), Offset(size.width, size.height), strokeWidth)
                IenBorderSide.Start -> drawLine(color, Offset(0f, 0f), Offset(0f, size.height), strokeWidth)
                IenBorderSide.End -> drawLine(color, Offset(size.width, 0f), Offset(size.width, size.height), strokeWidth)
                IenBorderSide.All -> Unit
            }
        }
        Box(Modifier.padding(padding)) {
            content()
        }
    }
}

@Composable
fun IenBottomInfo(
    text: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    icon: (@Composable () -> Unit)? = null,
    action: (@Composable RowScope.() -> Unit)? = null,
) {
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        color = toneWeakColor(tone),
        contentColor = toneColor(tone),
        shape = RoundedCornerShape(IenTheme.radius.default),
    ) {
        Row(
            modifier = Modifier.padding(IenTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.invoke()
            IenText(
                text = text,
                modifier = Modifier.weight(1f),
                style = IenTheme.typography.body2,
                color = toneColor(tone),
            )
            action?.invoke(this)
        }
    }
}

@Composable
fun <T> IenGridList(
    items: List<T>,
    columns: Int,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = IenTheme.spacing.sm,
    verticalSpacing: Dp = IenTheme.spacing.sm,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemContent: @Composable (item: T, index: Int) -> Unit,
) {
    val safeColumns = columns.coerceAtLeast(1)
    Column(
        modifier = modifier.padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
    ) {
        items.chunked(safeColumns).forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
            ) {
                rowItems.forEachIndexed { columnIndex, item ->
                    Box(Modifier.weight(1f)) {
                        itemContent(item, rowIndex * safeColumns + columnIndex)
                    }
                }
                repeat(safeColumns - rowItems.size) {
                    Box(Modifier.weight(1f))
                }
            }
        }
    }
}
