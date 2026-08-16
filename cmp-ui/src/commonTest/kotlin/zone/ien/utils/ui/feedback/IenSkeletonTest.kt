package zone.ien.utils.ui.feedback

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import zone.ien.utils.ui.foundation.defaultLightIenColorScheme

class IenSkeletonTest {
    private val colors = defaultLightIenColorScheme().copy(
        surfaceRaised = Color.Red,
        surfaceVariant = Color.Green,
        textPrimary = Color.Blue,
    )

    @Test
    fun `Grey 배경은 테마의 대체 표면 색상을 사용한다`() {
        val color = resolveIenSkeletonColor(IenSkeletonBackground.Grey, colors)

        assertEquals(Color.Green, color)
    }

    @Test
    fun `White 배경은 테마의 강조 표면 색상을 사용한다`() {
        val color = resolveIenSkeletonColor(IenSkeletonBackground.White, colors)

        assertEquals(Color.Red, color)
    }

    @Test
    fun `GreyOpacity100 배경은 테마의 기본 텍스트 색상에 투명도를 적용한다`() {
        val color = resolveIenSkeletonColor(IenSkeletonBackground.GreyOpacity100, colors)

        assertEquals(Color.Blue.copy(alpha = 0.05f), color)
    }
}
