package zone.ien.utils.ui.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composer
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.CompositionTracer
import androidx.compose.runtime.InternalComposeTracingApi
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestResult
import platform.UIKit.UIViewController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import zone.ien.utils.ui.foundation.IenTheme

@OptIn(ExperimentalTestApi::class, InternalComposeTracingApi::class)
class IenScaffoldPerformanceTest {
    @Test
    fun scrollingDoesNotRecomposeScaffoldWhenBlurIsDisabled(): TestResult = verifyScrolling(false)

    @Test
    fun scrollingOnlyUpdatesBlurDrawingWhenBlurIsEnabled(): TestResult = verifyScrolling(true)

    @Test
    fun scrollingFromBottomUpdatesBottomBlurWithoutRecomposingScaffold(): TestResult =
        verifyScrolling(blurEnabled = true, fromBottom = true)

    private fun verifyScrolling(blurEnabled: Boolean, fromBottom: Boolean = false): TestResult = runComposeUiTest {
        val state = ScrollState(0)
        lateinit var scope: CoroutineScope
        var scaffoldCompositions = 0
        val tracer = object : CompositionTracer {
            override fun isTraceInProgress(): Boolean = true
            override fun traceEventEnd() = Unit
            override fun traceEventStart(key: Int, dirty1: Int, dirty2: Int, info: String) {
                if (info.startsWith("zone.ien.utils.ui.screen.IenScaffold (")) {
                    scaffoldCompositions++
                }
            }
        }
        Composer.setTracer(tracer)
        try {
            val controller = UIViewController()
            setContent {
                scope = rememberCoroutineScope()
                CompositionLocalProvider(LocalUIViewController provides controller) {
                    IenTheme {
                        IenScaffold(
                            containerColor = Color.White,
                            contentEdge = IenScaffoldContentEdge(
                                enabled = blurEnabled,
                                scrollableState = state,
                            ),
                        ) {
                            Box(Modifier.verticalScroll(state)) {
                                Box(Modifier.fillMaxWidth().height(4096.dp).background(Color.Red))
                            }
                        }
                    }
                }
            }
            if (fromBottom) {
                runOnIdle { scope.launch { state.scrollTo(state.maxValue) } }
                waitForIdle()
            }
            val initialOffset = runOnIdle { state.value }
            val initialCompositions = runOnIdle { scaffoldCompositions }
            assertTrue(initialCompositions > 0, "Scaffold의 실제 합성이 추적되어야 합니다")
            val initialImage = onRoot().captureToImage()
            val sampleY = if (fromBottom) initialImage.height - 10 else 10
            val initialEdgeColor = initialImage.toPixelMap()[initialImage.width / 2, sampleY]
            runOnIdle {
                scope.launch {
                    state.scroll {
                        repeat(48) {
                            scrollBy(if (fromBottom) -1f else 1f)
                            withFrameNanos { }
                        }
                    }
                }
            }
            waitForIdle()
            runOnIdle {
                assertEquals(initialOffset + if (fromBottom) -48 else 48, state.value)
                assertEquals(
                    initialCompositions,
                    scaffoldCompositions,
                    "블러 활성화=$blurEnabled, 픽셀 스크롤만으로 Scaffold 전체가 다시 합성되면 안 됩니다",
                )
            }
            val scrolledImage = onRoot().captureToImage()
            val scrolledEdgeColor = scrolledImage.toPixelMap()[scrolledImage.width / 2, sampleY]
            if (blurEnabled) {
                assertNotEquals(initialEdgeColor, scrolledEdgeColor, "블러 페이드는 실제 픽셀을 갱신해야 합니다")
            } else {
                assertEquals(initialEdgeColor, scrolledEdgeColor, "블러를 끄면 단색 본문이 그대로 보여야 합니다")
            }
        } finally {
            Composer.setTracer(null)
        }
    }
}
