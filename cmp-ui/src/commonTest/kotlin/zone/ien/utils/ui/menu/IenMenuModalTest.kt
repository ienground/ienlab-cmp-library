package zone.ien.utils.ui.menu

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class IenMenuModalTest {
    @Test
    fun `모달 오버레이 진행률은 색상 알파에 반영된다`() {
        val color = Color.Black.copy(alpha = 0.42f)

        assertEquals(
            expected = 0.21f,
            actual = color.withIenModalOverlayProgress(0.5f).alpha,
            absoluteTolerance = 0.01f,
        )
    }

    @Test
    fun `모달 오버레이 진행률은 범위를 벗어나지 않는다`() {
        val color = Color.Black.copy(alpha = 0.42f)

        assertEquals(0f, color.withIenModalOverlayProgress(-1f).alpha, absoluteTolerance = 0.001f)
        assertEquals(0.42f, color.withIenModalOverlayProgress(2f).alpha, absoluteTolerance = 0.001f)
    }
}
