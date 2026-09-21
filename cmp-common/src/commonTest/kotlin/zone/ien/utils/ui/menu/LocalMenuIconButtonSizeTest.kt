package zone.ien.utils.ui.menu

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalMenuIconButtonSizeTest {
    @Test
    fun defaultMenuIconButtonSizeIsSquare() {
        assertEquals(40.dp, DefaultMenuIconButtonSize.first)
        assertEquals(40.dp, DefaultMenuIconButtonSize.second)
    }
}
