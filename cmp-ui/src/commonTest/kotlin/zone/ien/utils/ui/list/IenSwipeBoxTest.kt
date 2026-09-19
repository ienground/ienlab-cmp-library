package zone.ien.utils.ui.list

import kotlin.test.Test
import kotlin.test.assertEquals

class IenSwipeBoxTest {
    @Test
    fun `기본 크기는 compose hig SwipeBox 토큰을 따른다`() {
        assertEquals(52f, IenSwipeBoxDefaults.actionItemSize.value)
        assertEquals(60f, IenSwipeBoxDefaults.actionItemWidth.value)
        assertEquals(72f, IenSwipeBoxDefaults.actionItemHeight.value)
    }

    @Test
    fun `액션 빌더는 시작과 끝 액션 순서를 보존한다`() {
        val builder = IenSwipeBoxActionsBuilder()

        builder.start(key = "first") { }
        builder.start(key = "second") { }
        builder.end(key = "last") { }

        assertEquals(listOf("first", "second"), builder.startActions.map { it.key })
        assertEquals(listOf("last"), builder.endActions.map { it.key })
    }
}
