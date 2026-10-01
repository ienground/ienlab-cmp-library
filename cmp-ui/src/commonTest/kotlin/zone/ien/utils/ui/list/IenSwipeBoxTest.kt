package zone.ien.utils.ui.list

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.interactive.IenButtonDefault
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.primitives.IenText

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

    @Test
    fun `액션 행 너비를 초과하면 확장 상태로 판단한다`() {
        assertFalse(
            isSwipeBoxActionExpanding(
                revealedWidth = 128.dp,
                itemWidth = 60.dp,
                actionCount = 2,
                actionRowOuterPadding = 8.dp,
            ),
        )
        assertTrue(
            isSwipeBoxActionExpanding(
                revealedWidth = 128.1.dp,
                itemWidth = 60.dp,
                actionCount = 2,
                actionRowOuterPadding = 8.dp,
            ),
        )
    }

    @Test
    fun `확장 햅틱 임계점은 스와이프 방향별로 판정한다`() {
        assertTrue(
            isSwipeBoxExpansionThresholdReached(
                offset = 61f,
                targetValue = IenSwipeBoxStates.Resting,
                isStartActionItemSupplied = true,
                isEndActionItemSupplied = true,
                startExpansionThresholdPx = 60f,
                endExpansionThresholdPx = 60f,
                fullExpansionStart = true,
                fullExpansionEnd = true,
            ),
        )
        assertTrue(
            isSwipeBoxExpansionThresholdReached(
                offset = -61f,
                targetValue = IenSwipeBoxStates.Resting,
                isStartActionItemSupplied = true,
                isEndActionItemSupplied = true,
                startExpansionThresholdPx = 60f,
                endExpansionThresholdPx = 60f,
                fullExpansionStart = true,
                fullExpansionEnd = true,
            ),
        )
        assertFalse(
            isSwipeBoxExpansionThresholdReached(
                offset = 0f,
                targetValue = IenSwipeBoxStates.Resting,
                isStartActionItemSupplied = true,
                isEndActionItemSupplied = true,
                startExpansionThresholdPx = 60f,
                endExpansionThresholdPx = 60f,
                fullExpansionStart = true,
                fullExpansionEnd = true,
            ),
        )
        assertTrue(
            isSwipeBoxExpansionThresholdReached(
                offset = 0f,
                targetValue = IenSwipeBoxStates.StartFullyExpanded,
                isStartActionItemSupplied = true,
                isEndActionItemSupplied = true,
                startExpansionThresholdPx = 60f,
                endExpansionThresholdPx = 60f,
                fullExpansionStart = true,
                fullExpansionEnd = true,
            ),
        )
    }

    @Test
    fun `확장 햅틱은 Android에서 가벼운 충격 피드백으로 매핑한다`() {
        assertEquals(
            HapticFeedbackType.GestureThresholdActivate,
            IenSwipeBoxHapticFeedbackType.ImpactLight.androidType,
        )
    }

    @Test
    fun `라벨은 확장 상태에서 옵션이 켜진 경우에만 표시한다`() {
        assertFalse(
            shouldRenderSwipeBoxLabel(
                isExpanding = false,
                showLabelOnExpansion = true,
            ),
        )
        assertFalse(
            shouldRenderSwipeBoxLabel(
                isExpanding = false,
                showLabelOnExpansion = false,
            ),
        )
        assertFalse(
            shouldRenderSwipeBoxLabel(
                isExpanding = true,
                showLabelOnExpansion = false,
            ),
        )
        assertTrue(
            shouldRenderSwipeBoxLabel(
                isExpanding = true,
                showLabelOnExpansion = true,
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
                showLabelOnExpansion = false,
                colors = IenButtonDefault.colors(
                    variant = IenButtonVariant.Line,
                    tone = IenSemanticTone.Danger,
                ),
            )
        }
    }

    @Composable
    private fun `스와이프 액션은 Composable 라벨을 받을 수 있다`() {
        Row {
            IenSwipeBoxItem(
                onClick = {},
                label = { IenText("삭제") },
            )
        }
    }
}
