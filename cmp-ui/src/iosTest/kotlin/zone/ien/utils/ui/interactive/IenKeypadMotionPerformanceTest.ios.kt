package zone.ien.utils.ui.interactive

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composer
import androidx.compose.runtime.CompositionTracer
import androidx.compose.runtime.InternalComposeTracingApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.foundation.IenTheme
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.utils.shakeOnDisabledClick

@OptIn(ExperimentalTestApi::class, InternalComposeTracingApi::class)
class IenKeypadMotionPerformanceTest {
    @Test
    fun enablementChangesReadThePaletteAtMostOncePerKey(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val enabled = mutableStateOf(true)
        var paletteReads = 0
        var keyCompositions = 0
        val tracer = object : CompositionTracer {
            override fun isTraceInProgress(): Boolean = true
            override fun traceEventEnd() = Unit
            override fun traceEventStart(key: Int, dirty1: Int, dirty2: Int, info: String) {
                if (info.contains("IenTheme.<get-colors>")) paletteReads++
                if (info.startsWith("zone.ien.utils.ui.interactive.IenKeypadKey (")) keyCompositions++
            }
        }
        Composer.setTracer(tracer)
        try {
            setContent {
                IenTheme {
                    IenAlphabetKeypad(
                        alphabets = listOf("A", "B", "C"),
                        enabled = enabled.value,
                        onKeyClick = {},
                        onBackspaceClick = {},
                    )
                }
            }
            waitForIdle()
            runOnIdle {
                paletteReads = 0
                keyCompositions = 0
                enabled.value = false
            }
            mainClock.advanceTimeByFrame()
            waitForIdle()
            assertEquals(4, keyCompositions, "문자 키 3개와 백스페이스 키의 활성 전환을 추적해야 합니다")
            assertTrue(paletteReads > 0, "테마 팔레트 읽기가 실제로 추적되어야 합니다")
            assertTrue(
                paletteReads <= keyCompositions,
                "키마다 같은 팔레트 읽기를 반복하면 안 됩니다: 팔레트 ${paletteReads}회, 키 ${keyCompositions}개",
            )
        } finally {
            Composer.setTracer(null)
        }
    }

    @Test
    fun tapsNearTheKeyEdgesKeepTheFullTouchTarget(): TestResult = runComposeUiTest {
        var input = ""
        var backspaces = 0
        setContent {
            IenTheme {
                IenAlphabetKeypad(
                    alphabets = listOf("A", "B", "C"),
                    modifier = Modifier.width(300.dp),
                    onKeyClick = { input += it },
                    onBackspaceClick = { backspaces++ },
                )
            }
        }
        waitForIdle()
        val key = onNodeWithText("A")
        val bounds = key.fetchSemanticsNode().boundsInRoot
        assertTrue(bounds.width > bounds.height, "터치 영역은 글자 너비가 아니라 키 전체 너비여야 합니다")
        key.performTouchInput { click(Offset(4f, center.y)) }
        assertEquals("A", input)
        onNodeWithText("⌫").performTouchInput { click(Offset(width - 4f, center.y)) }
        assertEquals(1, backspaces)
    }

    @Test
    fun disabledClickStateChangesDoNotRemeasureContent(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val enabled = mutableStateOf(true)
        val measurements = intArrayOf(0)
        setContent {
            Box(
                Modifier.size(48.dp)
                    .layout { measurable, constraints ->
                        measurements[0]++
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) { placeable.place(0, 0) }
                    }
                    .shakeOnDisabledClick(enabled.value),
            )
        }
        waitForIdle()
        val initialMeasurements = runOnIdle { measurements[0] }
        repeat(4) {
            runOnIdle { enabled.value = !enabled.value }
            mainClock.advanceTimeByFrame()
            waitForIdle()
        }
        assertEquals(initialMeasurements, runOnIdle { measurements[0] })
    }

    @Test
    fun enablementChangesDoNotRemeasureUnchangedKeys(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val enabled = mutableStateOf(true)
        val measurements = intArrayOf(0)
        setContent {
            IenTheme {
                IenAlphabetKeypad(
                    alphabets = listOf("A", "B", "C"),
                    enabled = enabled.value,
                    onKeyClick = {},
                    onBackspaceClick = {},
                    modifier = Modifier.layout { measurable, constraints ->
                        measurements[0]++
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) { placeable.place(0, 0) }
                    },
                )
            }
        }
        waitForIdle()
        val initialMeasurements = runOnIdle { measurements[0] }
        repeat(4) {
            runOnIdle { enabled.value = !enabled.value }
            mainClock.advanceTimeByFrame()
            waitForIdle()
        }
        assertEquals(
            initialMeasurements,
            runOnIdle { measurements[0] },
            "활성 전환은 색상과 입력 처리만 바꾸므로 키패드를 다시 측정하면 안 됩니다",
        )
    }

    @Test
    fun disabledTapShakesWithoutInputAndReenablingResetsTheOffset(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val enabled = mutableStateOf(false)
        var inputs = 0
        setContent {
            IenTheme {
                IenAlphabetKeypad(
                    alphabets = listOf("A"),
                    enabled = enabled.value,
                    onKeyClick = { inputs++ },
                    onBackspaceClick = {},
                )
            }
        }
        waitForIdle()
        val key = onNodeWithText("A")
        key.assertIsNotEnabled()
        val initialCenter = key.fetchSemanticsNode().boundsInRoot.center.x
        key.performTouchInput { click() }
        repeat(4) {
            mainClock.advanceTimeByFrame()
            waitForIdle()
        }
        assertNotEquals(initialCenter, key.fetchSemanticsNode().boundsInRoot.center.x)
        assertEquals(0, inputs)

        runOnIdle { enabled.value = true }
        mainClock.advanceTimeByFrame()
        waitForIdle()
        assertEquals(initialCenter, key.fetchSemanticsNode().boundsInRoot.center.x)
        key.performTouchInput { click() }
        assertEquals(1, inputs)

        runOnIdle { enabled.value = false }
        mainClock.advanceTimeByFrame()
        waitForIdle()
        assertEquals(initialCenter, key.fetchSemanticsNode().boundsInRoot.center.x)
    }
}
