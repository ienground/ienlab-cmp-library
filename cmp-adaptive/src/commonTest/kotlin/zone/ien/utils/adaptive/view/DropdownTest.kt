package zone.ien.utils.adaptive.view

import androidx.compose.ui.Alignment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class DropdownTest {
    @Test
    fun `펼쳐진 dropdown은 trigger를 숨기는 배율을 사용한다`() {
        assertEquals(0.001f, dropdownTriggerVisibilityScale(expanded = true))
    }

    @Test
    fun `닫힌 dropdown은 trigger를 원래 배율로 표시한다`() {
        assertEquals(1f, dropdownTriggerVisibilityScale(expanded = false))
    }

    @Test
    fun `비기본 contentAlignment는 dropdown 콘텐츠 정렬로 유지된다`() {
        val alignment = Alignment.BottomEnd

        assertSame(alignment, dropdownContentAlignment(alignment))
    }
}
