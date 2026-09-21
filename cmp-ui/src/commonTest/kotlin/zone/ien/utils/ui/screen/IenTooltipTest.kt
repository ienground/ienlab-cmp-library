package zone.ien.utils.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.defaultDarkIenColorScheme
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.view.IenTooltipColors
import zone.ien.utils.ui.view.resolveIenTooltipColors

class IenTooltipTest {
    @Test
    fun `다크 테마 Tooltip은 다크 표면과 기본 텍스트 색상을 사용한다`() {
        val colors = defaultDarkIenColorScheme()

        assertEquals(
            IenTooltipColors(
                container = colors.surfaceRaised,
                content = colors.textPrimary,
            ),
            resolveIenTooltipColors(IenSemanticTone.Neutral, colors),
        )
    }

    @Test
    fun `텍스트 너비에 맞추는 Tooltip 옵션을 사용할 수 있다`() {
        assertNotNull(fitContentTooltip)
    }

    @Test
    fun `Composable Tooltip 콘텐츠 오버로드를 사용할 수 있다`() {
        assertNotNull(composableTooltip)
    }

    @Test
    fun `좌우 Tooltip은 앵커 옆 중앙에 배치된다`() {
        val anchorBounds = IntRect(left = 200, top = 100, right = 220, bottom = 120)
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

        assertEquals(IntOffset(x = 112, y = 50), leftPosition)
        assertEquals(IntOffset(x = 228, y = 50), rightPosition)
        assertEquals(0.5f, leftArrowRatio)
        assertEquals(0.5f, rightArrowRatio)
    }

    @Test
    fun `상하 Tooltip은 앵커와 간격을 두고 배치된다`() {
        val anchorBounds = IntRect(left = 200, top = 300, right = 220, bottom = 320)
        val windowSize = IntSize(width = 1000, height = 1000)
        val popupContentSize = IntSize(width = 80, height = 120)
        val density = Density(1f)
        var topArrowRatio = 0f
        var bottomArrowRatio = 0f

        val topPosition = IenTooltipPositionProvider(
            placement = IenTooltipPlacement.Top,
            offset = 8.dp,
            density = density,
            onArrowRatioCalculated = { topArrowRatio = it },
        ).calculatePosition(anchorBounds, windowSize, LayoutDirection.Ltr, popupContentSize)
        val bottomPosition = IenTooltipPositionProvider(
            placement = IenTooltipPlacement.Bottom,
            offset = 8.dp,
            density = density,
            onArrowRatioCalculated = { bottomArrowRatio = it },
        ).calculatePosition(anchorBounds, windowSize, LayoutDirection.Ltr, popupContentSize)

        assertEquals(IntOffset(x = 170, y = 172), topPosition)
        assertEquals(IntOffset(x = 170, y = 328), bottomPosition)
        assertEquals(0.5f, topArrowRatio)
        assertEquals(0.5f, bottomArrowRatio)
    }

    private val fitContentTooltip: @Composable () -> Unit = {
        IenTooltip(
            text = "도움말",
            fitContentWidth = true,
        )
    }

    private val composableTooltip: @Composable () -> Unit = {
        IenTooltip(
            text = { IenText("도움말") },
        )
    }
}
