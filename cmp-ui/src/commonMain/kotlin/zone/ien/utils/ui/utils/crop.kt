package zone.ien.utils.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.crop(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
): Modifier = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val horizontalPx = horizontal.roundToPx()
    val verticalPx = vertical.roundToPx()

    layout(
        (placeable.width - horizontalPx * 2).coerceAtLeast(0),
        (placeable.height - verticalPx * 2).coerceAtLeast(0)
    ) {
        placeable.placeRelative(-horizontalPx, -verticalPx)
    }
}