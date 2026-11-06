package zone.ien.utils.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.foundation.IenTheme

class ScrollingBubblePerformanceTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun pixelScrollingWithinOneItemDoesNotRecomposeBubbleContent(): TestResult = runComposeUiTest {
        val startScrolling = mutableStateOf(false)
        var listState: LazyListState? = null
        var bubbleCompositionCount = 0
        val bubbleIndices = mutableListOf<Int>()
        val bubblePositions = mutableListOf<Float>()

        setContent {
            val state = rememberLazyListState(initialFirstVisibleItemIndex = 150)
            listState = state
            LaunchedEffect(state, startScrolling.value) {
                if (startScrolling.value) {
                    state.scroll {
                        repeat(48) {
                            scrollBy(24f)
                            withFrameNanos { }
                        }
                    }
                }
            }
            IenTheme {
                ScrollingBubble(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                    bubbleContent = { index ->
                        SideEffect {
                            bubbleCompositionCount++
                            bubbleIndices += index
                        }
                        Box(
                            Modifier.fillMaxSize().onGloballyPositioned {
                                bubblePositions += it.positionInRoot().y
                            },
                        )
                    },
                ) {
                    LazyColumn(
                        state = state,
                        modifier = Modifier.fillMaxSize().testTag("scrolling-bubble-list"),
                    ) {
                        items(count = 300) { index ->
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(1200.dp)
                                    .testTag("row-$index"),
                            )
                        }
                    }
                }
            }
        }

        runOnIdle { startScrolling.value = true }
        waitForIdle()

        runOnIdle {
            assertEquals(1152, listState?.firstVisibleItemScrollOffset)
            assertTrue(bubbleIndices.isNotEmpty(), "스크롤 중 버블 내용이 표시되어야 합니다.")
            assertTrue(bubbleIndices.all { it == 150 }, "픽셀 스크롤 동안 버블 인덱스는 유지되어야 합니다.")
            assertTrue(
                bubblePositions.distinct().size > 1,
                "스크롤 위치가 변하면 버블 위치도 갱신되어야 합니다.",
            )
            assertTrue(
                bubbleCompositionCount <= 3,
                "같은 항목 안에서 48회 픽셀 스크롤했지만 버블 내용이 $bubbleCompositionCount 회 재구성되었습니다.",
            )
        }
    }
}
