package zone.ien.utils.ui.screen

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class IenScaffoldTest {
    @Test
    fun topBlurRemainsHiddenAtScrollStart() {
        assertEquals(0f, resolveLazyTopEdgeProgress(false, null, 48f))
    }

    @Test
    fun topBlurUsesFirstItemOffsetInsideContentPadding() {
        assertEquals(0.125f, resolveLazyTopEdgeProgress(true, -6, 48f))
    }

    @Test
    fun topBlurRemainsHiddenForZeroHeightFirstItemAtOrigin() {
        assertEquals(0f, resolveLazyTopEdgeProgress(true, 0, 48f))
    }

    @Test
    fun topBlurIsFullAfterFirstItemLeavesLayout() {
        assertEquals(1f, resolveLazyTopEdgeProgress(true, null, 48f))
    }

    @Test
    fun topBlurClampsOffsetsToFadeRange() {
        assertEquals(0f, resolveLazyTopEdgeProgress(true, 6, 48f))
        assertEquals(1f, resolveLazyTopEdgeProgress(true, -60, 48f))
    }

    @Test
    fun defaultBottomBlurHeightIsCompact() {
        assertEquals(64.dp, IenScaffoldContentEdge().bottomHeight)
    }

    @Test
    fun bottomBlurHeightIncludesBottomBarHeight() {
        assertEquals(
            160.dp,
            resolveBottomBlurHeight(
                bottomHeight = 96.dp,
                bottomBarHeight = 64.dp,
            ),
        )
    }

    @Test
    fun bottomBlurHeightRemainsDefaultWithoutBottomBar() {
        assertEquals(
            96.dp,
            resolveBottomBlurHeight(
                bottomHeight = 96.dp,
                bottomBarHeight = 0.dp,
            ),
        )
    }
}
