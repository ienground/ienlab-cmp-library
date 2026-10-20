package zone.ien.utils.ui.interactive

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.test.Test
import kotlin.test.assertEquals

class IenActionBarTest {
    @Test
    fun `위와 아래의 세 가지 정렬은 부모 영역을 기준으로 배치된다`() {
        val size = IntSize(80, 40)
        val space = IntSize(320, 480)
        listOf(
            Alignment.Start to 0,
            Alignment.CenterHorizontally to 120,
            Alignment.End to 240,
        ).forEach { (align, x) ->
            assertEquals(
                IntOffset(x, 0),
                IenActionBarSide.Top.alignment(align).align(size, space, LayoutDirection.Ltr),
            )
            assertEquals(
                IntOffset(x, 440),
                IenActionBarSide.Bottom.alignment(align).align(size, space, LayoutDirection.Ltr),
            )
        }
    }

    @Test
    fun `RTL에서는 시작과 끝의 위치만 반전된다`() {
        val size = IntSize(80, 40)
        val space = IntSize(320, 480)
        listOf(
            Alignment.Start to 240,
            Alignment.CenterHorizontally to 120,
            Alignment.End to 0,
        ).forEach { (align, x) ->
            assertEquals(
                IntOffset(x, 440),
                IenActionBarSide.Bottom.alignment(align).align(size, space, LayoutDirection.Rtl),
            )
        }
    }
}
