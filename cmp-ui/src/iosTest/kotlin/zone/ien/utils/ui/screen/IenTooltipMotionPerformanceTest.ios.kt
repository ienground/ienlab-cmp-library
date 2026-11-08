package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composer
import androidx.compose.runtime.CompositionTracer
import androidx.compose.runtime.InternalComposeTracingApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.isPopup
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.foundation.IenMotion
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.defaultIenTokens

@OptIn(ExperimentalTestApi::class, InternalComposeTracingApi::class)
class IenTooltipMotionPerformanceTest {
    @Test
    fun movingTheAnchorUpdatesTheVisibleArrow(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val anchorOffset = mutableIntStateOf(0)
        setContent {
            IenTheme {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                    Box(Modifier.offset { IntOffset(anchorOffset.intValue, 0) }) {
                        IenTooltip(
                            text = "화살표 이동 검증",
                            defaultOpen = true,
                            width = 240.dp,
                            placement = IenTooltipPlacement.Bottom,
                            anchor = { Box(Modifier.size(48.dp)) },
                        )
                    }
                }
            }
        }
        repeat(5) {
            mainClock.advanceTimeByFrame()
            waitForIdle()
        }
        val before = onNode(isPopup()).captureToImage().toPixelMap()
        runOnIdle { anchorOffset.intValue = 80 }
        repeat(3) {
            mainClock.advanceTimeByFrame()
            waitForIdle()
        }
        val after = onNode(isPopup()).captureToImage().toPixelMap()
        assertEquals(before.width, after.width)
        assertEquals(before.height, after.height)
        val tipY = (0 until before.height).first { y ->
            (0 until before.width).any { x -> before[x, y].alpha > 0.95f }
        } + 1
        val beforeTipPixels = (0 until before.width).filter { before[it, tipY].alpha > 0.5f }
        val afterTipPixels = (0 until after.width).filter { after[it, tipY].alpha > 0.5f }
        assertTrue(beforeTipPixels.isNotEmpty(), "이동 전 화살표 끝이 표시되어야 합니다")
        assertTrue(afterTipPixels.isNotEmpty(), "이동 후 화살표가 기존 clip에 잘리면 안 됩니다")
        assertTrue(afterTipPixels.average() > beforeTipPixels.average() + 20, "화살표가 앵커를 따라 이동해야 합니다")
    }

    @Test
    fun initialPopupPositioningDoesNotRecomposeTheTooltip(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        var popupCompositions = 0
        val tracer = object : CompositionTracer {
            override fun isTraceInProgress(): Boolean = true
            override fun traceEventEnd() = Unit
            override fun traceEventStart(key: Int, dirty1: Int, dirty2: Int, info: String) {
                if (info.startsWith("zone.ien.utils.ui.screen.IenTooltipPopup (")) popupCompositions++
            }
        }
        Composer.setTracer(tracer)
        try {
            setContent {
                IenTheme {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                        IenTooltip(
                            text = "위치 계산 검증",
                            defaultOpen = true,
                            width = 240.dp,
                            placement = IenTooltipPlacement.Bottom,
                            anchor = { Box(Modifier.size(48.dp)) },
                        )
                    }
                }
            }
            repeat(5) {
                mainClock.advanceTimeByFrame()
                waitForIdle()
            }
            onNodeWithText("위치 계산 검증").assertIsDisplayed()
            assertEquals(1, popupCompositions, "최초 위치와 화살표 계산은 팝업을 다시 합성하면 안 됩니다")
        } finally {
            Composer.setTracer(null)
        }
    }

    @Test
    fun `툴팁 열기 spring 프레임은 구현과 팝업을 재합성하지 않는다`(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false

        var tooltipImplCompositions = 0
        var tooltipPopupCompositions = 0
        val tracer = object : CompositionTracer {
            override fun isTraceInProgress(): Boolean = true

            override fun traceEventEnd() = Unit

            override fun traceEventStart(key: Int, dirty1: Int, dirty2: Int, info: String) {
                when {
                    info.startsWith("zone.ien.utils.ui.screen.IenTooltipImpl (") -> {
                        tooltipImplCompositions++
                    }
                    info.startsWith("zone.ien.utils.ui.screen.IenTooltipPopup (") -> {
                        tooltipPopupCompositions++
                    }
                }
            }
        }

        Composer.setTracer(tracer)
        try {
            val open = mutableStateOf(false)
            setContent {
                IenTheme(
                    tokens = defaultIenTokens().copy(
                        motion = IenMotion(fastMillis = 1, normalMillis = 1),
                    ),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        IenTooltip(
                            text = { Text("툴팁 본문") },
                            open = open.value,
                            onOpenChange = { open.value = it },
                            placement = IenTooltipPlacement.Bottom,
                            motionVariant = IenTooltipMotionVariant.Strong,
                            anchorPositionByRatio = 0.5f,
                            anchor = { toggle ->
                                Button(onClick = toggle) {
                                    Text("툴팁 열기")
                                }
                            },
                        )
                    }
                }
            }
            waitForIdle()

            onNodeWithText("툴팁 열기").performClick()
            waitForIdle()
            runOnIdle {
                assertTrue(open.value, "툴팁 열기 동작이 상태를 갱신해야 합니다")
            }

            repeat(2) {
                mainClock.advanceTimeByFrame()
                waitForIdle()
            }
            onNodeWithText("툴팁 본문").assertIsDisplayed()

            repeat(2) {
                mainClock.advanceTimeByFrame()
                waitForIdle()
            }
            val bodyPixelsBeforeSpringFrames = onNodeWithText("툴팁 본문").captureToImage().toPixelMap()

            val animationStartTime = mainClock.currentTime
            runOnIdle {
                assertTrue(open.value, "툴팁이 열린 상태여야 합니다")
            }

            var implCompositionsAtOpen = 0
            var popupCompositionsAtOpen = 0
            runOnIdle {
                implCompositionsAtOpen = tooltipImplCompositions
                popupCompositionsAtOpen = tooltipPopupCompositions
            }
            assertTrue(implCompositionsAtOpen > 0, "IenTooltipImpl 합성이 추적되어야 합니다")
            assertTrue(popupCompositionsAtOpen > 0, "IenTooltipPopup 합성이 추적되어야 합니다")

            repeat(6) {
                mainClock.advanceTimeByFrame()
                waitForIdle()
            }
            assertTrue(mainClock.currentTime > animationStartTime, "spring 진행을 위해 테스트 시계가 전진해야 합니다")
            val bodyPixelsAfterSpringFrames = onNodeWithText("툴팁 본문").captureToImage().toPixelMap()
            var springPixelsChanged =
                bodyPixelsBeforeSpringFrames.width != bodyPixelsAfterSpringFrames.width ||
                    bodyPixelsBeforeSpringFrames.height != bodyPixelsAfterSpringFrames.height
            if (!springPixelsChanged) {
                outer@ for (y in 0 until bodyPixelsBeforeSpringFrames.height) {
                    for (x in 0 until bodyPixelsBeforeSpringFrames.width) {
                        if (bodyPixelsBeforeSpringFrames[x, y] != bodyPixelsAfterSpringFrames[x, y]) {
                            springPixelsChanged = true
                            break@outer
                        }
                    }
                }
            }
            assertTrue(springPixelsChanged, "열기 spring은 팝업 콘텐츠의 실제 표시 픽셀을 갱신해야 합니다")

            runOnIdle {
                assertEquals(
                    implCompositionsAtOpen,
                    tooltipImplCompositions,
                    "spring 프레임에서 IenTooltipImpl이 재합성되면 안 됩니다",
                )
                assertEquals(
                    popupCompositionsAtOpen,
                    tooltipPopupCompositions,
                    "spring 프레임에서 IenTooltipPopup이 재합성되면 안 됩니다",
                )
            }
        } finally {
            Composer.setTracer(null)
        }
    }
}
