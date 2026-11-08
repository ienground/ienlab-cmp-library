package zone.ien.utils.ui.feedback

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import zone.ien.utils.ui.window.shouldUseIenOverlayWindowBounds
import zone.ien.utils.ui.window.isIenOverlayReady

class IenOverlayVisibilityTest {
    @Test
    fun `회전 전 윈도우 크기나 첫 배치가 남아 있으면 오버레이 진입을 시작하지 않는다`() {
        val portrait = IntSize(466, 678)
        val landscape = IntSize(678, 466)
        assertFalse(isIenOverlayReady(landscape, portrait, landscape))
        assertFalse(isIenOverlayReady(landscape, landscape, IntSize.Zero))
        assertFalse(isIenOverlayReady(landscape, landscape, portrait))
        assertTrue(isIenOverlayReady(landscape, landscape, landscape))
    }

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

    @Test
    fun `세로 힌지 양쪽 너비가 같으면 원본처럼 왼쪽에 시트를 배치한다`() {
        assertEquals(
            IntRect(0, 20, 480, 780),
            ienSheetAvailableBounds(IntRect(0, 20, 1000, 780), IntRect(480, 0, 520, 800)),
        )
    }

    @Test
    fun `가로 힌지에서는 아래쪽 영역에 시트를 배치한다`() {
        assertEquals(
            IntRect(20, 520, 780, 1000),
            ienSheetAvailableBounds(IntRect(20, 0, 780, 1000), IntRect(0, 480, 800, 520)),
        )
    }

    @Test
    fun `힌지 반대편이 더 넓으면 시트가 잘리지 않는 영역을 선택한다`() {
        assertEquals(
            IntRect(0, 0, 700, 800),
            ienSheetAvailableBounds(IntRect(0, 0, 1000, 800), IntRect(700, 0, 740, 800)),
        )
    }

    @Test
    fun `힌지가 없거나 현재 윈도우 밖에 있으면 안전 영역을 유지한다`() {
        val bounds = IntRect(10, 20, 390, 780)
        assertEquals(bounds, ienSheetAvailableBounds(bounds, null))
        assertEquals(bounds, ienSheetAvailableBounds(bounds, IntRect(480, 0, 520, 800)))
    }

    @Test
    fun `Duo Medium은 디자인 원본의 하단 여백과 너비를 따른다`() {
        val density = Density(1f)
        assertEquals(IntRect(8, 314, 458, 670), ienDuoSheetBounds(IntSize(466, 678), false, 0f, density))
        assertEquals(IntRect(8, 37, 670, 458), ienDuoSheetBounds(IntSize(678, 466), false, 0f, density))
        assertEquals(IntRect(8, 587, 661, 943), ienDuoSheetBounds(IntSize(669, 951), true, 0f, density))
        assertEquals(IntRect(149, 305, 802, 661), ienDuoSheetBounds(IntSize(951, 669), true, 0f, density))
    }

    @Test
    fun `Duo Full은 외부 화면의 상하 여백과 내부 화면의 상단 여백을 유지한다`() {
        val density = Density(1f)
        assertEquals(IntRect(8, 54, 458, 670), ienDuoSheetBounds(IntSize(466, 678), false, 1f, density))
        assertEquals(IntRect(8, 37, 670, 458), ienDuoSheetBounds(IntSize(678, 466), false, 1f, density))
        assertEquals(IntRect(8, 82, 661, 951), ienDuoSheetBounds(IntSize(669, 951), true, 1f, density))
        assertEquals(IntRect(149, 82, 802, 669), ienDuoSheetBounds(IntSize(951, 669), true, 1f, density))
    }

    @Test
    fun `Duo 배치는 픽셀 밀도에 비례하고 접힌 영역을 넘지 않는다`() {
        assertEquals(
            IntRect(298, 610, 1604, 1322),
            ienDuoSheetBounds(IntSize(1902, 1338), true, 0f, Density(2f)),
        )
        assertEquals(
            IntRect(8, 305, 452, 661),
            ienDuoSheetBounds(IntSize(951, 669), true, 0f, Density(1f), IntRect(460, 0, 491, 669)),
        )
        val bounds = ienDuoSheetBounds(IntSize(12, 10), true, 1f, Density(1f))
        assertTrue(bounds.width >= 0 && bounds.height >= 0)
        assertTrue(bounds.left >= 0 && bounds.top >= 0 && bounds.right <= 12 && bounds.bottom <= 10)
    }
}
