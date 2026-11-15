package zone.ien.utils.example.ui.screens.playground

import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import platform.UIKit.UIViewController

class AdaptivePlaygroundStressTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun adaptiveThemeControlsAndCupertinoScreenCanBeToggledScrolledAndDisposedRepeatedly(): TestResult =
        runComposeUiTest {
            mainClock.autoAdvance = false
            val mounted = mutableStateOf(false)
            var mountCount = 0
            var disposeCount = 0
            var compositionFrameMounted = false

            setContent {
                if (mounted.value) {
                    DisposableEffect(Unit) {
                        mountCount += 1
                        onDispose { disposeCount += 1 }
                    }
                    SideEffect { compositionFrameMounted = true }
                    val viewController = remember { UIViewController() }
                    CompositionLocalProvider(LocalUIViewController provides viewController) {
                        AdaptivePlaygroundScreen()
                    }
                } else {
                    SideEffect { compositionFrameMounted = false }
                    Text("playground-unmounted")
                }
            }

            mainClock.advanceTimeByFrame()
            waitForIdle()
            repeat(2) { cycle ->
                val mountFrameStart = mainClock.currentTime
                runOnIdle { mounted.value = true }
                mainClock.advanceTimeByFrame()
                assertTrue(mainClock.currentTime > mountFrameStart, "Adaptive Playground mount 프레임 전진")
                waitForIdle()
                runOnIdle {
                    assertEquals(cycle + 1, mountCount)
                    assertEquals(cycle, disposeCount)
                    assertTrue(compositionFrameMounted)
                }

                onNodeWithText("Adaptive Playground").assertIsDisplayed()
                onNodeWithText("Material theme").assertExists()
                onNodeWithText("Dark theme").assertExists()
                onNodeWithText("Components enabled").assertExists()
                onNodeWithText("Material3 + Ien").assertIsDisplayed()
                assertTrue(onAllNodes(isToggleable()).fetchSemanticsNodes().size >= 3)

                val themeFrameStart = mainClock.currentTime
                onAllNodes(isToggleable())[0].assertIsOn().performClick()
                mainClock.advanceTimeByFrame()
                assertTrue(mainClock.currentTime > themeFrameStart, "테마 전환 프레임 전진")
                waitForIdle()
                onNodeWithText("Cupertino + HIG").assertIsDisplayed()

                val darkThemeFrameStart = mainClock.currentTime
                onAllNodes(isToggleable())[1].assertIsOff().performClick()
                mainClock.advanceTimeByFrame()
                assertTrue(mainClock.currentTime > darkThemeFrameStart, "다크 테마 전환 프레임 전진")
                waitForIdle()
                onAllNodes(isToggleable())[1].assertIsOn()

                val enabledFrameStart = mainClock.currentTime
                onAllNodes(isToggleable())[2].assertIsOn().performClick()
                mainClock.advanceTimeByFrame()
                assertTrue(mainClock.currentTime > enabledFrameStart, "컴포넌트 활성화 전환 프레임 전진")
                waitForIdle()
                onNodeWithText("Primary action").assertIsNotEnabled()
                onAllNodes(isToggleable())[2].performClick()
                mainClock.advanceTimeByFrame()
                waitForIdle()
                onNodeWithText("Primary action").assertExists()

                repeat(4) {
                    onRoot().performTouchInput { swipeUp() }
                    mainClock.advanceTimeByFrame()
                    waitForIdle()
                }
                repeat(4) {
                    onRoot().performTouchInput { swipeDown() }
                    mainClock.advanceTimeByFrame()
                    waitForIdle()
                }

                val disposeFrameStart = mainClock.currentTime
                runOnIdle { mounted.value = false }
                mainClock.advanceTimeByFrame()
                assertTrue(mainClock.currentTime > disposeFrameStart, "Adaptive Playground dispose 프레임 전진")
                waitForIdle()
                runOnIdle {
                    assertEquals(cycle + 1, mountCount)
                    assertEquals(cycle + 1, disposeCount)
                    assertTrue(!compositionFrameMounted)
                }
                onNodeWithText("playground-unmounted").assertIsDisplayed()
            }
        }
}
