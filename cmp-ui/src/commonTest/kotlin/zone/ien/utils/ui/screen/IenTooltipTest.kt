package zone.ien.utils.ui.screen

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class IenTooltipTest {
    @Test
    fun `좌우 Tooltip은 앵커 옆 중앙에 배치된다`() {
        val anchorBounds = IntRect(left = 100, top = 100, right = 120, bottom = 120)
        val windowSize = IntSize(width = 1000, height = 1000)
        val popupContentSize = IntSize(width = 80, height = 120)
        val density = Density(1f)
        var leftArrowRatio = 0f
        var rightArrowRatio = 0f

        val leftPosition = IenTooltipPositionProvider(
            placement = IenTooltipPlacement.Left,
            offset = 8.dp,
            density = density,
            onArrowRatioCalculated = { leftArrowRatio = it },
        ).calculatePosition(anchorBounds, windowSize, LayoutDirection.Ltr, popupContentSize)
        val rightPosition = IenTooltipPositionProvider(
            placement = IenTooltipPlacement.Right,
            offset = 8.dp,
            density = density,
            onArrowRatioCalculated = { rightArrowRatio = it },
        ).calculatePosition(anchorBounds, windowSize, LayoutDirection.Ltr, popupContentSize)

        assertEquals(IntOffset(x = 44, y = 50), leftPosition)
        assertEquals(IntOffset(x = 96, y = 50), rightPosition)
        assertEquals(0.5f, leftArrowRatio)
        assertEquals(0.5f, rightArrowRatio)
    }
}
