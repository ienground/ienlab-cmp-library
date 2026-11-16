package zone.ien.utils.example.ui.screens.scrollingbubble

import kotlin.test.Test
import kotlin.test.assertEquals
import zone.ien.utils.example.ui.navigation.RootRoute

class ScrollingBubbleScreenTest {
    @Test
    fun `스크롤 버블 샘플 라우트와 항목을 제공한다`() {
        val route: RootRoute = RootRoute.ScrollingBubble

        assertEquals(RootRoute.ScrollingBubble, route)
        assertEquals("Ackee", scrollingBubbleSampleItems.first())
        assertEquals(72, scrollingBubbleSampleItems.size)
    }
}
