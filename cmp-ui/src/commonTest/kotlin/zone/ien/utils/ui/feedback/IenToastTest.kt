package zone.ien.utils.ui.feedback

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertNotSame
import zone.ien.utils.ui.foundation.IenSemanticTone

class IenToastTest {
    @Test
    fun `새 토스트를 표시하면 이전 토스트를 최신 값으로 교체한다`() {
        val state = IenToastState()

        state.showIenToast("첫 번째")
        val firstToast = state.currentToast

        state.showIenToast(
            message = "두 번째",
            tone = IenSemanticTone.Success,
            duration = IenToastDuration.Long,
        )

        val currentToast = state.currentToast
        assertNotNull(firstToast)
        assertNotNull(currentToast)
        assertNotSame(firstToast, currentToast)
        assertEquals("두 번째", currentToast.message)
        assertEquals(IenSemanticTone.Success, currentToast.tone)
        assertEquals(IenToastDuration.Long, currentToast.duration)
    }

    @Test
    fun `토스트를 닫으면 현재 토스트를 제거한다`() {
        val state = IenToastState()
        state.showIenToast("닫힐 메시지")

        state.dismiss()

        assertNull(state.currentToast)
    }

    @Test
    fun `토스트 지속시간은 호스트용 밀리초 계약을 제공한다`() {
        assertEquals(4_000L, IenToastDuration.Short.durationMillis)
        assertEquals(10_000L, IenToastDuration.Long.durationMillis)
        assertNull(IenToastDuration.Indefinite.durationMillis)
    }
}
