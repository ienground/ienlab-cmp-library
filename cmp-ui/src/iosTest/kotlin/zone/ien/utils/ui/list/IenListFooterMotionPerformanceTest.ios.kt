package zone.ien.utils.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.foundation.IenTheme

class IenListFooterMotionPerformanceTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun footerShadowAnimationDoesNotRecomposeItsSlotsForEveryFrame(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false

        var contentCompositions = 0
        var shadowCompositions = 0
        setContent {
            IenTheme {
                IenListFooter(
                    onClick = {},
                    modifier = Modifier.testTag("footer"),
                    border = IenListFooterBorder.None,
                    shadow = {
                        SideEffect { shadowCompositions++ }
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                                .testTag("footer-shadow"),
                        )
                    },
                    shape = RectangleShape,
                ) {
                    SideEffect { contentCompositions++ }
                    Box(Modifier.height(48.dp))
                }
            }
        }
        waitForIdle()

        val initialContentCompositions = runOnIdle { contentCompositions }
        val initialShadowCompositions = runOnIdle { shadowCompositions }
        assertEquals(0, initialShadowCompositions, "그림자 슬롯은 투명 상태에서 합성되면 안 됩니다.")
        onNodeWithTag("footer-shadow", useUnmergedTree = true).assertDoesNotExist()

        onNodeWithTag("footer").performTouchInput { down(center) }
        waitForIdle()
        repeat(3) {
            mainClock.advanceTimeByFrame()
            waitForIdle()
        }

        onNodeWithTag("footer-shadow", useUnmergedTree = true).assertIsDisplayed()
        val contentCompositionsAtFirstVisibleFrame = runOnIdle { contentCompositions }
        val shadowCompositionsAtFirstVisibleFrame = runOnIdle { shadowCompositions }
        assertEquals(1, shadowCompositionsAtFirstVisibleFrame)
        val firstImage = onNodeWithTag("footer").captureToImage()

        repeat(3) {
            mainClock.advanceTimeByFrame()
            waitForIdle()
        }

        val contentCompositionsAfterFrames = runOnIdle { contentCompositions }
        val shadowCompositionsAfterFrames = runOnIdle { shadowCompositions }
        assertEquals(
            contentCompositionsAtFirstVisibleFrame,
            contentCompositionsAfterFrames,
            "그림자 알파 프레임은 푸터 본문 슬롯을 다시 합성하면 안 됩니다.",
        )
        assertEquals(
            shadowCompositionsAtFirstVisibleFrame,
            shadowCompositionsAfterFrames,
            "그림자 알파 프레임은 그림자 슬롯($shadowCompositionsAtFirstVisibleFrame->$shadowCompositionsAfterFrames)을 다시 합성하면 안 됩니다.",
        )
        assertTrue(
            contentCompositionsAfterFrames >= initialContentCompositions,
            "푸터 본문은 초기 상태에서 합성되어야 합니다.",
        )

        val laterImage = onNodeWithTag("footer").captureToImage()
        val firstPixel = firstImage.toPixelMap()[4, 4]
        val laterPixel = laterImage.toPixelMap()[4, 4]
        assertNotEquals(firstPixel, laterPixel, "그림자 알파 프레임이 실제 렌더링에 반영되어야 합니다.")

        onNodeWithTag("footer").performTouchInput { up() }
        waitForIdle()
        mainClock.advanceTimeBy(300)
        waitForIdle()
        onNodeWithTag("footer-shadow", useUnmergedTree = true).assertDoesNotExist()
    }
}
