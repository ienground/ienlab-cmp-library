package zone.ien.utils.ui.primitives

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import kotlin.test.Test
import kotlin.test.assertNotNull
import zone.ien.utils.icon.IconData

class IenPrimitivesTest {
    @Test
    fun `Painter 아이콘 데이터로 호출할 수 있다`() {
        val content: @Composable (Painter) -> Unit = { painter ->
            IenIcon(
                icon = IconData.Paint(painter),
                contentDescription = null,
            )
        }

        assertNotNull(content)
    }
}
