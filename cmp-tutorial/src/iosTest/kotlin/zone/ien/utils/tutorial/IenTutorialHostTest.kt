package zone.ien.utils.tutorial

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import zone.ien.utils.ui.foundation.IenTheme

@OptIn(ExperimentalTestApi::class)
class IenTutorialHostTest {
    @Test
    fun `대상 준비 전에는 본문 동작을 차단한다`() = runComposeUiTest {
        mainClock.autoAdvance = false
        var center = Offset.Zero
        var clicks = 0
        setContent {
            IenTheme {
                IenTutorialHost(
                    steps = listOf("첫 단계"),
                    onComplete = {},
                    skipContent = { skip -> Button(onClick = skip) { Text(text = "건너뛰기") } },
                ) { step ->
                    Box(modifier = Modifier.fillMaxSize().padding(top = 120.dp)) {
                        Button(
                            onClick = { clicks++ },
                            modifier = Modifier.size(200.dp, 48.dp)
                                .onGloballyPositioned { center = it.boundsInRoot().center }
                                .ienTutorialTarget("첫 단계", title = step.orEmpty(), description = "설명"),
                        ) { Text(text = "본문") }
                    }
                }
            }
        }
        onNodeWithText("건너뛰기").assertDoesNotExist()
        onRoot().performTouchInput { click(center) }
        runOnIdle { assertEquals(0, clicks) }
    }

    @Test
    fun `단계 전환과 재시작에도 본문 상태와 effect 수명을 보존한다`() = runComposeUiTest {
        val restartKey = mutableStateOf(0)
        var currentStep: String? = null
        var center = Offset.Zero
        var completions = 0
        var starts = 0
        var disposals = 0
        var actualClicks = 0
        val rememberedValues = mutableSetOf<Any>()
        setContent {
            IenTheme {
                IenTutorialHost(
                    steps = listOf("첫 단계", "둘째 단계"),
                    restartKey = restartKey.value,
                    onComplete = { completions++ },
                    skipContent = { skip -> Button(onClick = skip) { Text(text = "건너뛰기") } },
                ) { step ->
                    val value = remember { Any() }
                    SideEffect {
                        currentStep = step
                        rememberedValues += value
                    }
                    DisposableEffect(Unit) {
                        starts++
                        onDispose { disposals++ }
                    }
                    Box(modifier = Modifier.fillMaxSize().padding(top = 120.dp)) {
                        Button(
                            onClick = { actualClicks++ },
                            modifier = Modifier.size(200.dp, 48.dp)
                                .onGloballyPositioned { center = it.boundsInRoot().center }
                                .ienTutorialTarget(step ?: "첫 단계", title = step.orEmpty(), description = "설명"),
                        ) { Text(text = "본문") }
                    }
                }
            }
        }
        onNodeWithText("건너뛰기").assertIsDisplayed()
        onRoot().performTouchInput { click(center) }
        waitForIdle()
        runOnIdle {
            assertEquals("둘째 단계", currentStep)
            assertEquals(0, actualClicks)
            assertEquals(0, completions)
        }
        onRoot().performTouchInput { click(center) }
        waitForIdle()
        runOnIdle {
            assertNull(currentStep)
            assertEquals(1, completions)
            restartKey.value++
        }
        onNodeWithText("건너뛰기").assertIsDisplayed()
        runOnIdle { assertEquals("첫 단계", currentStep) }
        onNodeWithText("건너뛰기").performClick()
        waitForIdle()
        runOnIdle {
            assertNull(currentStep)
            assertEquals(2, completions)
            assertEquals(1, rememberedValues.size)
            assertEquals(1, starts)
            assertEquals(0, disposals)
            assertEquals(0, actualClicks)
        }
        onNodeWithText("본문").performClick()
        runOnIdle { assertEquals(1, actualClicks) }
    }
}
