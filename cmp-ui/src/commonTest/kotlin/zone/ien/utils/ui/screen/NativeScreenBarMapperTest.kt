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

        assertEquals(listOf("저장", "더보기", "완료"), buttons.map { it.title })
        assertFalse(buttons.first().enabled)
        assertTrue(buttons.last().enabled)
        assertTrue(buttons.last().isPrimary)
        assertTrue(buttons.dropLast(1).none { it.isPrimary })
        assertEquals(listOf("추가"), buttons[1].menu.map { it.title })
        buttons[1].menu.single().onClick()
        buttons.last().onClick()
        assertEquals(2, clickCount)
    }
    @Test
    fun `표시 공간을 넘는 조건부 액션은 더보기 메뉴로 옮긴다`() {
        val actions = (1..4).map {
            ActionMenuItem.IconMenuItem.ShownIfRoom("액션 $it", icon = null, onClick = {})
        }
        val buttons = actions.toNativeBarButtons()
        assertEquals(listOf("액션 1", "액션 2", "더보기"), buttons.map { it.title })
        assertEquals(listOf("액션 3", "액션 4"), buttons.last().menu.map { it.title })
    }

    @Test
    fun `숨긴 액션은 표시 공간과 더보기 메뉴를 차지하지 않는다`() {
        val actions = listOf(
            ActionMenuItem.NeverShown("숨김", visible = false, onClick = {}),
            ActionMenuItem.IconMenuItem.ShownIfRoom("표시", icon = null, onClick = {}),
        )
        val buttons = actions.toNativeBarButtons()
        assertEquals(listOf("표시"), buttons.map { it.title })
        assertTrue(buttons.single().menu.isEmpty())
    }

    @Test
    fun `공간이 없더라도 고정 액션과 주요 액션은 유지한다`() {
        val actions = listOf(
            ActionMenuItem.IconMenuItem.AlwaysShown("고정", icon = null, onClick = {}),
            ActionMenuItem.IconMenuItem.ShownIfRoom("조건부", icon = null, onClick = {}),
            ActionMenuItem.NeverShown("메뉴 전용", onClick = {}),
        )
        val primary = ActionMenuItem.IconMenuItem.ShownIfRoom("완료", icon = null, onClick = {})
        val buttons = actions.toNativeBarButtons(primary, maxVisibleItems = 0)
        assertEquals(listOf("고정", "더보기", "완료"), buttons.map { it.title })
        assertEquals(listOf("조건부", "메뉴 전용"), buttons[1].menu.map { it.title })
    }

    @Test
    fun `공간에 정확히 들어가는 액션은 더보기 메뉴를 만들지 않는다`() {
        val actions = (1..3).map {
            ActionMenuItem.IconMenuItem.ShownIfRoom("액션 $it", icon = null, onClick = {})
        }
        val buttons = actions.toNativeBarButtons(maxVisibleItems = 3)
        assertEquals(listOf("액션 1", "액션 2", "액션 3"), buttons.map { it.title })
        assertTrue(buttons.all { it.menu.isEmpty() })
    }

    @Test
    fun `버튼과 메뉴에 아이콘과 숫자 및 표시점 배지를 전달한다`() {
        val painter = androidx.compose.ui.graphics.painter.ColorPainter(androidx.compose.ui.graphics.Color.Red)
        val fixed = ActionMenuItem.IconMenuItem.AlwaysShown("고정", icon = null, badge = 12, onClick = {})
        val optional = ActionMenuItem.IconMenuItem.ShownIfRoom("조건부", icon = null, badge = -1, enabled = false, onClick = {})
        val buttons = listOf(fixed, optional).toNativeBarButtons(
            images = mapOf(fixed to painter, optional to painter),
            maxVisibleItems = 0,
        )
        assertEquals(12, buttons.first().badge)
        assertEquals(painter, buttons.first().image)
        val action = buttons.last().menu.single()
        assertEquals(-1, action.badge)
        assertEquals(painter, action.image)
        assertFalse(action.enabled)
    }

}
