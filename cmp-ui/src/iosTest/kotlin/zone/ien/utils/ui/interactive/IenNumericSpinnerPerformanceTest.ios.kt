package zone.ien.utils.ui.interactive

import androidx.compose.runtime.Composer
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.CompositionTracer
import androidx.compose.runtime.InternalComposeTracingApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.foundation.IenTheme

@OptIn(ExperimentalTestApi::class, InternalComposeTracingApi::class)
class IenNumericSpinnerPerformanceTest {
    @Test
    fun `radius animation does not reformat an unchanged number and captured state stays current`(): TestResult =
        runComposeUiTest {
            mainClock.autoAdvance = false

            val locale = mutableStateOf("en")
            var formatterCalls = 0
            var numberChanges = 0
            var spinnerCompositions = 0
            val tracer = object : CompositionTracer {
                override fun isTraceInProgress(): Boolean = true

                override fun traceEventEnd() = Unit

                override fun traceEventStart(key: Int, dirty1: Int, dirty2: Int, info: String) {
                    if (info.startsWith("zone.ien.utils.ui.interactive.IenNumericSpinner (")) {
                        spinnerCompositions++
                    }
                }
            }

            Composer.setTracer(tracer)
            try {
                setContent {
                    val currentViewConfiguration = LocalViewConfiguration.current
                    val noTouchSlopViewConfiguration = remember(currentViewConfiguration) {
                        object : ViewConfiguration by currentViewConfiguration {
                            override val touchSlop: Float = 0f
                        }
                    }
                    CompositionLocalProvider(
                        LocalViewConfiguration provides noTouchSlopViewConfiguration,
                    ) {
                        IenTheme {
                            IenNumericSpinner<Int>(
                                modifier = Modifier.testTag("numeric-spinner"),
                                number = 5,
                                minNumber = 0,
                                maxNumber = 10,
                                numberFormatter = { value ->
                                    formatterCalls++
                                    "${locale.value}:$value"
                                },
                                onNumberChange = { numberChanges++ },
                            )
                        }
                    }
                }
                waitForIdle()
                onNodeWithText("en:5").assertIsDisplayed()

                onNodeWithTag("numeric-spinner").performTouchInput {
                    down(center)
                    moveBy(Offset(1f, 0f))
                }
                waitForIdle()
                val callsAfterDragStart = runOnIdle { formatterCalls }
                val compositionsAfterDragStart = runOnIdle { spinnerCompositions }

                repeat(8) {
                    mainClock.advanceTimeByFrame()
                    waitForIdle()
                }

                val callsDuringRadiusAnimation = runOnIdle { formatterCalls }
                val compositionsDuringRadiusAnimation = runOnIdle { spinnerCompositions }
                assertTrue(
                    compositionsDuringRadiusAnimation > compositionsAfterDragStart,
                    "반지름 animation 중 IenNumericSpinner 합성이 진행되어야 합니다",
                )
                assertEquals(
                    callsAfterDragStart,
                    callsDuringRadiusAnimation,
                    "값이 유지되는 반지름 animation 프레임에서 numberFormatter를 다시 호출하면 안 됩니다",
                )

                runOnIdle { locale.value = "fr" }
                mainClock.advanceTimeByFrame()
                waitForIdle()
                onNodeWithText("fr:5").assertIsDisplayed()
                runOnIdle {
                    assertTrue(
                        formatterCalls > callsDuringRadiusAnimation,
                        "캡처된 locale 상태가 바뀌면 numberFormatter 결과를 갱신해야 합니다",
                    )
                }

                onNodeWithTag("numeric-spinner").performTouchInput { up() }
                waitForIdle()
                runOnIdle { assertEquals(0, numberChanges, "임계값 미만 드래그는 숫자를 바꾸면 안 됩니다") }
            } finally {
                Composer.setTracer(null)
            }
        }
}
