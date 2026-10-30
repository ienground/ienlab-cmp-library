package zone.ien.utils.ui.layout

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

/**
 * Expands a full-span lazy grid item's content into the grid's added horizontal item gutters.
 *
 * Apply this modifier to the root of a full-span item when the grid's `contentPadding` includes
 * additional horizontal gutters. Pass only those added gutters so other insets remain in place.
 *
 * @param start Added content padding at the logical start edge.
 * @param end Added content padding at the logical end edge. Defaults to [start].
 * @return A modifier that expands and offsets the item content across the added gutters.
 */
fun Modifier.lazyGridFullSpanItemBleed(
    start: Dp,
    end: Dp = start,
): Modifier = layout { measurable, constraints ->
    val startPx = start.roundToPx()
    val endPx = end.roundToPx()
    val hasBoundedWidth = constraints.hasBoundedWidth
    val expandedWidth = if (hasBoundedWidth) {
        constraints.maxWidth + startPx + endPx
    } else {
        constraints.maxWidth
    }
    val placeable = measurable.measure(
        if (hasBoundedWidth) {
            constraints.copy(minWidth = expandedWidth, maxWidth = expandedWidth)
        } else {
            constraints
        }
    )
    val layoutWidth = if (hasBoundedWidth) constraints.maxWidth else placeable.width

    layout(layoutWidth, placeable.height) {
        placeable.placeRelative(if (hasBoundedWidth) -startPx else 0, 0)
    }
}
