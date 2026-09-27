package zone.ien.utils.adaptive.screen

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class ScaffoldTest {
    @Test
    fun `Cupertino 액션이 있으면 공통 오른쪽 여백을 사용한다`() {
        assertEquals(8.dp, cupertinoActionsEndSpacing(hasActions = true))
    }

    @Test
    fun `Cupertino 액션이 없으면 오른쪽 여백을 추가하지 않는다`() {
        assertEquals(0.dp, cupertinoActionsEndSpacing(hasActions = false))
    }
}
