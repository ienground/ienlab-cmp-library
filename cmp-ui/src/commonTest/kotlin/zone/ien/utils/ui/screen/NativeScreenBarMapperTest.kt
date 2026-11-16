package zone.ien.utils.ui.screen

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import zone.ien.utils.ui.menu.ActionMenuItem

class NativeScreenBarMapperTest {
    @Test
    fun `표시 상태와 활성화 상태 및 클릭 동작을 native button에 전달한다`() {
        var clickCount = 0
        val actions = listOf(
            ActionMenuItem.IconMenuItem.AlwaysShown(
                title = "저장",
                onClick = { clickCount++ },
                icon = null,
                enabled = false,
            ),
            ActionMenuItem.NeverShown(
                title = "숨김",
                onClick = {},
                visible = false,
            ),
            ActionMenuItem.NeverShown(
                title = "추가",
                onClick = { clickCount++ },
            ),
        )
        val primaryAction = ActionMenuItem.IconMenuItem.AlwaysShown(
            title = "완료",
            onClick = { clickCount++ },
            icon = null,
        )
        val buttons = actions.toNativeBarButtons(primaryAction)

        assertEquals(listOf("저장", "추가", "완료"), buttons.map { it.title })
        assertFalse(buttons.first().enabled)
        assertTrue(buttons.last().enabled)
        buttons[1].onClick()
        buttons.last().onClick()
        assertEquals(2, clickCount)
    }
}
