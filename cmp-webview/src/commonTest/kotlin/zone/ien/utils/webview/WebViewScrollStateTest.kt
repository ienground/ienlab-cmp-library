package zone.ien.utils.webview

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WebViewScrollStateTest {
    @Test
    fun bodyMetricsDoNotUndoConsumedHeaderScroll() {
        val state = WebViewScrollState()
        state.updateHeaderHeight(100f)
        state.updateBody(0f, 1000f)

        assertEquals(35f, state.consumeHeaderDelta(35f))
        state.updateBody(0f, 1000f)

        assertEquals(35f, state.headerOffset)
        assertEquals(35f, state.offset)
        assertEquals(1100f, state.maxOffset)
    }

    @Test
    fun headerConsumesOnlyItsRemainingDistanceBeforeBodyScroll() {
        val state = WebViewScrollState()
        state.updateHeaderHeight(100f)
        state.updateBody(0f, 1000f)
        state.consumeHeaderDelta(70f)

        val consumed = state.consumeHeaderDelta(80f)
        state.updateBody(80f - consumed, 1000f)

        assertEquals(30f, consumed)
        assertEquals(100f, state.headerOffset)
        assertEquals(150f, state.offset)
    }

    @Test
    fun reachingBodyTopPreservesHeaderUntilRemainingScrollExpandsIt() {
        val state = WebViewScrollState()
        state.updateHeaderHeight(100f)
        state.updateBody(0f, 1000f)
        state.consumeHeaderDelta(100f)
        state.updateBody(50f, 1000f)

        state.updateBody(0f, 1000f)
        assertEquals(100f, state.offset)
        assertEquals(-25f, state.consumeHeaderDelta(-25f))
        state.updateBody(0f, 1000f)

        assertEquals(75f, state.offset)
        assertTrue(state.canScrollBackward)
    }

    @Test
    fun bodyScrollCannotCollapseAnUnfinishedHeader() {
        val state = WebViewScrollState()
        state.updateHeaderHeight(100f)
        state.updateBody(0f, 1000f)
        state.consumeHeaderDelta(35f)

        state.updateBody(10f, 1000f)

        assertEquals(35f, state.headerOffset)
        assertEquals(45f, state.offset)
    }

    @Test
    fun nativeInsetScrollIncludesHeaderAndClampsElasticOverscroll() {
        val state = WebViewScrollState()
        state.updateHeaderHeight(100f)

        state.update(150f, 1100f)
        assertEquals(100f, state.headerOffset)
        assertEquals(150f, state.offset)
        state.update(30f, 1100f)
        assertEquals(30f, state.headerOffset)
        assertEquals(30f, state.offset)
        state.update(-20f, 1100f)
        assertEquals(0f, state.offset)
        assertFalse(state.canScrollBackward)
        state.update(1200f, 1100f)
        assertEquals(1100f, state.offset)
        assertFalse(state.canScrollForward)
    }
}
