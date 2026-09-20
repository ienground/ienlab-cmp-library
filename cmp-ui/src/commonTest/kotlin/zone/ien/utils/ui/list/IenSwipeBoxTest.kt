package zone.ien.utils.ui.list

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.interactive.IenButtonDefault
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant

class IenSwipeBoxTest {
    @Test
    fun `기본 크기는 compose hig SwipeBox 토큰을 따른다`() {
        assertEquals(52f, IenSwipeBoxDefaults.actionItemSize.value)
        assertEquals(60f, IenSwipeBoxDefaults.actionItemWidth.value)
        assertEquals(72f, IenSwipeBoxDefaults.actionItemHeight.value)
    }

    @Test
    fun `액션 빌더는 시작과 끝 액션 순서를 보존한다`() {
        val builder = IenSwipeBoxActionsBuilder()

        builder.start(key = "first") { }
        builder.start(key = "second") { }
        builder.end(key = "last") { }

        assertEquals(listOf("first", "second"), builder.startActions.map { it.key })
        assertEquals(listOf("last"), builder.endActions.map { it.key })
    }

    @Test
    fun `높이를 지정하지 않으면 콘텐츠 높이를 사용하고 액션 최소 높이를 보장한다`() {
        assertEquals(
            96.dp,
            resolveSwipeBoxHeight(
                height = Dp.Unspecified,
                contentHeight = 96.dp,
                hasActions = true,
            ),
        )
        assertEquals(
            IenSwipeBoxDefaults.actionItemHeight,
            resolveSwipeBoxHeight(
                height = Dp.Unspecified,
                contentHeight = 40.dp,
                hasActions = true,
            ),
        )
        assertEquals(
            40.dp,
            resolveSwipeBoxHeight(
                height = Dp.Unspecified,
                contentHeight = 40.dp,
                hasActions = false,
            ),
        )
        assertEquals(
            88.dp,
            resolveSwipeBoxHeight(
                height = 88.dp,
                contentHeight = 40.dp,
                hasActions = true,
            ),
        )
    }

    @Composable
    private fun `스와이프 액션은 IenButton 스타일 API를 사용한다`() {
        Row {
            IenSwipeBoxItem(
                onClick = {},
                variant = IenButtonVariant.Line,
                tone = IenSemanticTone.Danger,
                state = IenButtonState(),
                colors = IenButtonDefault.colors(
                    variant = IenButtonVariant.Line,
                    tone = IenSemanticTone.Danger,
                ),
            )
        }
    }
}
