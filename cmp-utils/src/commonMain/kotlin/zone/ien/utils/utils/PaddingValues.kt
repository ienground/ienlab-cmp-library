package zone.ien.utils.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

@Composable
@ReadOnlyComposable
fun PaddingValues.copy(
    top: Dp = calculateTopPadding(),
    start: Dp = calculateStartPadding(LocalLayoutDirection.current),
    bottom: Dp = calculateBottomPadding(),
    end: Dp = calculateEndPadding(LocalLayoutDirection.current),
): PaddingValues =
    PaddingValues(
        top = top,
        start = start,
        end = end,
        bottom = bottom,
    )

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current

    return PaddingValues(
        top = calculateTopPadding() + other.calculateTopPadding(),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
        start = calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection),
        end = calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection),
    )
}