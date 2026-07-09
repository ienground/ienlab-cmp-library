package zone.ien.utils.ui.components.composite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenTheme

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

sealed interface IenBottomGradient {
    data object None : IenBottomGradient
    data object Default : IenBottomGradient
    data class Custom(val fromColor: Color, val toColor: Color = Color.Transparent) : IenBottomGradient
}

@Composable
fun IenBottomInfo(
    modifier: Modifier = Modifier,
    bottomGradient: IenBottomGradient = IenBottomGradient.Default,
    backgroundColor: Color = IenTheme.colors.surfaceWeak,
    contentPadding: PaddingValues = PaddingValues(
        start = 24.dp,
        end = 24.dp,
        top = 24.dp,
        bottom = 16.dp
    ),
    content: @Composable () -> Unit,
) {
    val resolvedGradient = when (bottomGradient) {
        IenBottomGradient.None -> null
        IenBottomGradient.Default -> IenBottomGradient.Custom(
            fromColor = backgroundColor,
            toColor = Color.Transparent
        )
        is IenBottomGradient.Custom -> bottomGradient
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(contentPadding)
        ) {
            content()
        }

        if (resolvedGradient != null) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(resolvedGradient.fromColor, resolvedGradient.toColor)
                        )
                    )
            )
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
