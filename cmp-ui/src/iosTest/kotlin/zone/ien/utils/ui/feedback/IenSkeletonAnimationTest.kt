package zone.ien.utils.ui.feedback

import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.foundation.IenTheme

class IenSkeletonAnimationTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun skeletonAnimationDoesNotRecomposeItsBlocksEveryFrame(): TestResult = runComposeUiTest {
        var blockCompositions = 0
        val compositionCounter = Modifier.composed {
            SideEffect { blockCompositions++ }
            this
        }

        mainClock.autoAdvance = false
        setContent {
            IenTheme {
                IenSkeleton(
                    custom = List(32) {
                        IenSkeletonElement.Block(
                            height = 24.dp,
                            modifier = compositionCounter,
                        )
                    },
                    repeatLastItemCount = IenSkeletonRepeat.Count(1),
                )
            }
        }
        waitForIdle()

        val initialCompositions = runOnIdle { blockCompositions }
        assertTrue(initialCompositions > 0, "블록 합성 카운터가 초기 렌더링을 기록해야 합니다.")

        mainClock.advanceTimeBy(1000)
        waitForIdle()

        val compositionsAfterAnimationFrames = runOnIdle { blockCompositions }
        assertEquals(
            initialCompositions,
            compositionsAfterAnimationFrames,
            "애니메이션 프레임은 스켈레톤 블록을 다시 합성하지 않아야 합니다.",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun skeletonAnimationContinuesChangingItsRenderedPixels(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        setContent {
            IenTheme {
                IenSkeleton(
                    modifier = Modifier.testTag("skeleton"),
                    custom = listOf(IenSkeletonElement.Block(height = 40.dp)),
                    repeatLastItemCount = IenSkeletonRepeat.Count(1),
                    width = 80.dp,
                )
            }
        }
        waitForIdle()

        val firstImage = onNodeWithTag("skeleton").captureToImage()
        val firstCenterPixel = firstImage.toPixelMap()[firstImage.width / 2, firstImage.height / 2]

        mainClock.advanceTimeBy(400)
        waitForIdle()

        val secondImage = onNodeWithTag("skeleton").captureToImage()
        val secondCenterPixel = secondImage.toPixelMap()[secondImage.width / 2, secondImage.height / 2]
        assertNotEquals(
            firstCenterPixel,
            secondCenterPixel,
            "스켈레톤 모션은 진행 중에도 실제 렌더링을 갱신해야 합니다.",
        )
    }
}
