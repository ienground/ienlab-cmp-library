package zone.ien.utils.ui.feedback

import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composer
import androidx.compose.runtime.CompositionTracer
import androidx.compose.runtime.InternalComposeTracingApi
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.foundation.IenMotion
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.defaultIenTokens

class IenPullToRefreshMotionPerformanceTest {
    @OptIn(ExperimentalTestApi::class, InternalComposeTracingApi::class)
    @Test
    fun readyColorAnimationDoesNotRecomposeTheIndicatorEveryFrame(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false

        val state = MutablePullToRefreshState(initialFraction = 0.5f)
        val isRefreshing = mutableStateOf(false)
        var indicatorCompositions = 0
        val tracer = object : CompositionTracer {
            override fun isTraceInProgress(): Boolean = true

            override fun traceEventEnd() = Unit

            override fun traceEventStart(key: Int, dirty1: Int, dirty2: Int, info: String) {
                if (info.startsWith("zone.ien.utils.ui.feedback.IenPullToRefreshIndicator (")) {
                    indicatorCompositions++
                }
            }
        }

        Composer.setTracer(tracer)
        try {
            setContent {
                IenTheme(
                    tokens = defaultIenTokens().copy(
                        motion = IenMotion(normalMillis = 500),
                    ),
                ) {
                    IenPullToRefreshIndicator(
                        state = state,
                        isRefreshing = isRefreshing.value,
                        modifier = Modifier.testTag("pull-to-refresh-indicator"),
                    )
                }
            }
            waitForIdle()
            mainClock.advanceTimeByFrame()
            waitForIdle()

            val compositionsAtStart = runOnIdle { indicatorCompositions }
            assertTrue(compositionsAtStart > 0, "인디케이터 합성이 추적되어야 합니다.")

            runOnIdle { isRefreshing.value = true }
            waitForIdle()
            mainClock.advanceTimeByFrame()
            waitForIdle()
            val compositionsAfterStateChange = runOnIdle { indicatorCompositions }

            mainClock.advanceTimeBy(100)
            waitForIdle()

            runOnIdle {
                assertEquals(
                    compositionsAfterStateChange,
                    indicatorCompositions,
                    "준비 색상 프레임은 상태 전환 후 인디케이터를 다시 합성하면 안 됩니다.",
                )
            }
        } finally {
            Composer.setTracer(null)
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun readyColorAnimationContinuesUpdatingTheRenderedFill(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val state = MutablePullToRefreshState(initialFraction = 0.5f)
        val isRefreshing = mutableStateOf(false)
        setContent {
            IenTheme(
                tokens = defaultIenTokens().copy(
                    motion = IenMotion(normalMillis = 500),
                ),
            ) {
                IenPullToRefreshIndicator(
                    state = state,
                    isRefreshing = isRefreshing.value,
                    modifier = Modifier.testTag("pull-to-refresh-indicator"),
                )
            }
        }
        waitForIdle()
        mainClock.advanceTimeByFrame()
        waitForIdle()

        val initialImage = onNodeWithTag("pull-to-refresh-indicator").captureToImage()
        val initialFillPixel = initialImage.toPixelMap()[8, initialImage.height / 2]
        val initialBorderPixel = initialImage.toPixelMap()[initialImage.width / 2, 0]

        runOnIdle { isRefreshing.value = true }
        waitForIdle()
        mainClock.advanceTimeByFrame()
        waitForIdle()

        mainClock.advanceTimeBy(100)
        waitForIdle()

        val updatedImage = onNodeWithTag("pull-to-refresh-indicator").captureToImage()
        val updatedFillPixel = updatedImage.toPixelMap()[8, updatedImage.height / 2]
        val updatedBorderPixel = updatedImage.toPixelMap()[updatedImage.width / 2, 0]
        assertNotEquals(
            initialFillPixel,
            updatedFillPixel,
            "준비 색상 애니메이션은 draw phase에서 실제 배경 색을 갱신해야 합니다.",
        )
        assertNotEquals(
            initialBorderPixel,
            updatedBorderPixel,
            "준비 색상 애니메이션은 draw phase에서 실제 테두리 색을 갱신해야 합니다.",
        )
    }
}

private class MutablePullToRefreshState(initialFraction: Float) : PullToRefreshState {
    private val fraction = mutableFloatStateOf(initialFraction)

    override val distanceFraction: Float
        get() = fraction.floatValue

    override val isAnimating: Boolean
        get() = false

    override suspend fun animateToThreshold() {
        fraction.floatValue = 1f
    }

    override suspend fun animateToHidden() {
        fraction.floatValue = 0f
    }

    override suspend fun snapTo(targetValue: Float) {
        fraction.floatValue = targetValue
    }
}
