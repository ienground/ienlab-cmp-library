package zone.ien.utils.ui.list

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ScrollingBubbleTest {
    @Test
    fun `스크롤 중에는 첫 번째 표시 항목 인덱스를 버블에 전달한다`() {
        assertEquals(
            expected = 3,
            actual = scrollingBubbleIndex(
                isScrollInProgress = true,
                firstVisibleItemIndex = 3,
                itemCount = 10,
            ),
        )
    }

    @Test
    fun `스크롤 중이 아니면 버블을 표시하지 않는다`() {
        assertNull(
            scrollingBubbleIndex(
                isScrollInProgress = false,
                firstVisibleItemIndex = 3,
                itemCount = 10,
            ),
        )
    }

    @Test
    fun `빈 목록이나 범위를 벗어난 인덱스에서는 버블을 표시하지 않는다`() {
        assertNull(
            scrollingBubbleIndex(
                isScrollInProgress = true,
                firstVisibleItemIndex = 0,
                itemCount = 0,
            ),
        )
        assertNull(
            scrollingBubbleIndex(
                isScrollInProgress = true,
                firstVisibleItemIndex = 10,
                itemCount = 10,
            ),
        )
        assertNull(
            scrollingBubbleIndex(
                isScrollInProgress = true,
                firstVisibleItemIndex = -1,
                itemCount = 10,
            ),
        )
    }

    @Test
    fun `스크롤 진행률에 따라 버블과 썸의 위치를 계산한다`() {
        val position = scrollingBubblePosition(
            trackHeight = 1_000f,
            bubbleHeight = 120f,
            firstVisibleItemIndex = 20,
            firstVisibleItemScrollOffset = 0,
            firstVisibleItemSize = 50,
            visibleItemCount = 10,
            totalItemCount = 50,
            minimumThumbHeight = 48f,
        )

        assertNotNull(position)
        assertEquals(200f, position.thumbHeight)
        assertEquals(400f, position.thumbTop)
        assertEquals(440f, position.bubbleTop)
    }

    @Test
    fun `스크롤할 수 없는 목록에서는 버블 위치를 계산하지 않는다`() {
        val position = scrollingBubblePosition(
            trackHeight = 1_000f,
            bubbleHeight = 120f,
            firstVisibleItemIndex = 0,
            firstVisibleItemScrollOffset = 0,
            firstVisibleItemSize = 50,
            visibleItemCount = 10,
            totalItemCount = 10,
            minimumThumbHeight = 48f,
        )

        assertNull(position)
    }
}
