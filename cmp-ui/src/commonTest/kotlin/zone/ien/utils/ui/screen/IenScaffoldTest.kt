package zone.ien.utils.ui.screen

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class IenScaffoldTest {
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
