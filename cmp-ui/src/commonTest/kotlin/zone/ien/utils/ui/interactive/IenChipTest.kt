package zone.ien.utils.ui.interactive

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class IenChipTest {
    private val normalBrush = Brush.linearGradient(listOf(Color.Red, Color.Blue))
    private val selectedBrush = Brush.linearGradient(listOf(Color.Green, Color.Yellow))

    private val colors = IenChipColors(
        container = Color.White,
        content = Color.Black,
        border = Color.Gray,
        containerBrush = normalBrush,
        selectedContainer = Color.Blue,
        selectedContent = Color.White,
        selectedBorder = Color.Cyan,
        selectedContainerBrush = selectedBrush,
        disabledContainer = Color.LightGray,
        disabledContent = Color.DarkGray,
        disabledBorder = Color.Transparent,
        disabledSelectedContainer = Color.Gray,
        disabledSelectedContent = Color.Black,
        disabledSelectedBorder = Color.DarkGray,
    )

    @Test
    fun `선택된 Chip은 선택 상태 색상과 브러시를 사용한다`() {
        val resolved = colors.resolve(selected = true, enabled = true)

        assertEquals(Color.Blue, resolved.container)
        assertEquals(Color.White, resolved.content)
        assertEquals(Color.Cyan, resolved.border)
        assertSame(selectedBrush, resolved.containerBrush)
    }

    @Test
    fun `비활성 선택 Chip은 비활성 선택 색상을 사용하고 브러시를 제거한다`() {
        val resolved = colors.resolve(selected = true, enabled = false)

        assertEquals(Color.Gray, resolved.container)
        assertEquals(Color.Black, resolved.content)
        assertEquals(Color.DarkGray, resolved.border)
        assertNull(resolved.containerBrush)
    }

    @Test
    fun `로딩 중인 Chip은 클릭할 수 없다`() {
        assertFalse(IenChipState(loading = true).isInteractive)
        assertFalse(IenChipState(enabled = false).isInteractive)
        assertTrue(IenChipState().isInteractive)
    }
}
