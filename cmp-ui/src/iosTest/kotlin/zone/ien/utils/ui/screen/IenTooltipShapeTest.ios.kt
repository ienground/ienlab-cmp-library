package zone.ien.utils.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PathHitTester
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IenTooltipShapeTest {
    @Test
    fun arrowPositionReadsTheLatestRatioWhenCreatingTheOutline() {
        val density = Density(1f)
        val ratio = mutableStateOf(0.15f)
        val shape = IenTooltipShape(
            placement = IenTooltipPlacement.Bottom,
            arrowRatio = ratio,
            arrowSize = 12.dp,
            cornerRadius = 12.dp,
            density = density,
            clipToEnd = IenTooltipClipToEnd.None,
        )
        fun outline() = PathHitTester(
            (shape.createOutline(Size(200f, 80f), LayoutDirection.Ltr, density) as Outline.Generic).path,
        )
        assertTrue(outline().contains(Offset(30f, 2f)))
        assertFalse(outline().contains(Offset(150f, 2f)))
        ratio.value = 0.75f
        assertFalse(outline().contains(Offset(30f, 2f)))
        assertTrue(outline().contains(Offset(150f, 2f)))
    }

    @Test
    fun `상하 Tooltip은 clipToEnd에 따라 화살표의 한쪽 절반만 남긴다`() {
        for (placement in listOf(IenTooltipPlacement.Top, IenTooltipPlacement.Bottom)) {
            val arrowY = if (placement == IenTooltipPlacement.Top) 75f else 5f
            val leftHalfPoint = Offset(26f, arrowY)
            val rightHalfPoint = Offset(34f, arrowY)
            val fullArrow = tooltipPathHitTester(placement, IenTooltipClipToEnd.None)
            val leftArrow = tooltipPathHitTester(placement, IenTooltipClipToEnd.Left)
            val rightArrow = tooltipPathHitTester(placement, IenTooltipClipToEnd.Right)

            assertTrue(fullArrow.contains(leftHalfPoint), "placement=$placement, left half")
            assertTrue(fullArrow.contains(rightHalfPoint), "placement=$placement, right half")
            assertTrue(leftArrow.contains(leftHalfPoint), "placement=$placement, left half")
            assertFalse(leftArrow.contains(rightHalfPoint), "placement=$placement, clipped right half")
            assertFalse(rightArrow.contains(leftHalfPoint), "placement=$placement, clipped left half")
            assertTrue(rightArrow.contains(rightHalfPoint), "placement=$placement, right half")
        }
    }

    @Test
    fun `좌우 Tooltip은 clipToEnd와 관계없이 기존 화살표를 유지한다`() {
        val noneLeft = tooltipPathHitTester(IenTooltipPlacement.Left, IenTooltipClipToEnd.None)
        val clippedLeft = tooltipPathHitTester(IenTooltipPlacement.Left, IenTooltipClipToEnd.Left)
        val clippedRight = tooltipPathHitTester(IenTooltipPlacement.Left, IenTooltipClipToEnd.Right)
        val noneRight = tooltipPathHitTester(IenTooltipPlacement.Right, IenTooltipClipToEnd.None)
        val clippedRightLeft = tooltipPathHitTester(IenTooltipPlacement.Right, IenTooltipClipToEnd.Left)
        val clippedRightRight = tooltipPathHitTester(IenTooltipPlacement.Right, IenTooltipClipToEnd.Right)

        val leftArrowPoint = Offset(199f, 12f)
        val rightArrowPoint = Offset(1f, 12f)

        assertTrue(noneLeft.contains(leftArrowPoint))
        assertTrue(clippedLeft.contains(leftArrowPoint))
        assertTrue(clippedRight.contains(leftArrowPoint))
        assertTrue(noneRight.contains(rightArrowPoint))
        assertTrue(clippedRightLeft.contains(rightArrowPoint))
        assertTrue(clippedRightRight.contains(rightArrowPoint))
    }

    private fun tooltipPathHitTester(
        placement: IenTooltipPlacement,
        clipToEnd: IenTooltipClipToEnd,
    ): PathHitTester {
        val density = Density(1f)
        val outline = IenTooltipShape(
            placement = placement,
            arrowRatio = 0.15f,
            arrowSize = 12.dp,
            cornerRadius = 12.dp,
            density = density,
            clipToEnd = clipToEnd,
        ).createOutline(
            size = Size(200f, 80f),
            layoutDirection = LayoutDirection.Ltr,
            density = density,
        ) as Outline.Generic
        return PathHitTester(outline.path)
    }
}
