package zone.ien.utils.ui.components.composite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.toneColor
import zone.ien.utils.ui.components.interactive.toneWeakColor
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

sealed interface IenBorderVariant {
    data object Full : IenBorderVariant
    data object Padding24 : IenBorderVariant
    data class Height(val height: Dp = 16.dp) : IenBorderVariant
}

@Composable
fun IenBorder(
    modifier: Modifier = Modifier,
    variant: IenBorderVariant = IenBorderVariant.Full,
    color: Color = IenTheme.colors.border,
    thickness: Dp = IenTheme.stroke.thin,
) {
    when (variant) {
        IenBorderVariant.Full -> Box(
            modifier = modifier
                .fillMaxWidth()
                .height(thickness)
                .background(color),
        )

        IenBorderVariant.Padding24 -> Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(thickness)
                .background(color),
        )

        is IenBorderVariant.Height -> Box(
            modifier = modifier
                .fillMaxWidth()
                .height(variant.height)
                .background(IenTheme.colors.surfaceWeak),
        )
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
