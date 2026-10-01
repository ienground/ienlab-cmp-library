package zone.ien.utils.ui.primitives

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import kotlin.test.Test
import kotlin.test.assertNotNull

class IenPrimitivesTest {
    @Test
    fun `Painter 아이콘 오버로드를 호출할 수 있다`() {
        val content: @Composable (Painter) -> Unit = { painter ->
            IenIcon(
                painter = painter,
                contentDescription = null,
            )
        }

        assertNotNull(content)
    }
}
