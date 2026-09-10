package zone.ien.utils.ui.feedback

import androidx.compose.ui.unit.IntSize
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import zone.ien.utils.ui.window.shouldUseIenOverlayWindowBounds

class IenOverlayVisibilityTest {
    @Test
    fun `닫힘 전환 중인 오버레이는 종료 애니메이션을 위해 유지된다`() {
        assertTrue(shouldKeepIenOverlayMounted(visible = false, mounted = true))
    }

    @Test
    fun `닫힘 전환이 끝난 오버레이는 더 이상 유지되지 않는다`() {
        assertFalse(shouldKeepIenOverlayMounted(visible = false, mounted = false))
    }

    @Test
    fun `유효한 윈도우 크기만 오버레이 레이어 크기로 사용한다`() {
        assertTrue(shouldUseIenOverlayWindowBounds(IntSize(100, 100)))
        assertFalse(shouldUseIenOverlayWindowBounds(IntSize.Zero))
    }
}
