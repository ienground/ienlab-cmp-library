package zone.ien.utils.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.key
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.foundation.IenTheme

class IenSwipeBoxGestureTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun changingKeyReleasesOpenStateAndInitializesNewAnchors(): TestResult = runComposeUiTest {
        val rowKey = mutableIntStateOf(0)
        val openState = mutableStateOf<IenSwipeBoxState?>(null)
        var currentState: IenSwipeBoxState? = null
        setContent {
            IenTheme {
                val state = rememberIenSwipeBoxState(
                    key = rowKey.intValue,
                    velocityThreshold = 100f,
                    openSwipeBoxState = openState,
                )
                currentState = state
                IenSwipeBox(
                    modifier = Modifier.width(300.dp).testTag("재생성"),
                    state = state,
                    startToEndFullSwipeEnabled = false,
                    expansionHapticEnabled = false,
                    actionItemBuilder = { start { Text("시작") } },
                ) {
                    Box(Modifier.fillMaxWidth().height(72.dp)) { Text("항목") }
                }
            }
        }
        onNodeWithTag("재생성").performTouchInput {
            swipe(Offset(width / 15f, centerY), Offset(width / 5f, centerY), durationMillis = 120)
        }
        runOnIdle {
            assertSame(currentState, openState.value)
            rowKey.intValue++
        }
        runOnIdle {
            assertNull(openState.value)
            assertEquals(IenSwipeBoxStates.Resting, currentState?.settledValue)
        }
        onNodeWithTag("재생성").performTouchInput {
            swipe(Offset(width / 15f, centerY), Offset(width / 5f, centerY), durationMillis = 120)
        }
        runOnIdle {
            assertSame(currentState, openState.value)
            assertEquals(IenSwipeBoxStates.StartVisible, currentState?.settledValue)
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun fullSwipeRunsActionOnceAndRestoresState(): TestResult = runComposeUiTest {
        var actionCount = 0
        var settledValue = { IenSwipeBoxStates.Resting }
        setContent {
            IenTheme {
                val state = rememberIenSwipeBoxState()
                settledValue = { state.settledValue }
                IenSwipeBox(
                    modifier = Modifier.width(300.dp).testTag("전체 스와이프"),
                    state = state,
                    expansionHapticEnabled = false,
                    actionItemBuilder = { start(onClick = { actionCount++ }) { Text("실행") } },
                ) {
                    Box(Modifier.fillMaxWidth().height(72.dp)) { Text("항목") }
                }
            }
        }
        onNodeWithTag("전체 스와이프").performTouchInput {
            swipe(Offset(width / 20f, centerY), Offset(width * 0.9f, centerY), durationMillis = 300)
        }
        runOnIdle {
            assertEquals(1, actionCount)
            assertEquals(IenSwipeBoxStates.Resting, settledValue())
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun rtlDragKeepsStartActionDirection(): TestResult = runComposeUiTest {
        var settledValue = { IenSwipeBoxStates.Resting }
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                IenTheme {
                    val state = rememberIenSwipeBoxState(velocityThreshold = 100f)
                    settledValue = { state.settledValue }
                    IenSwipeBox(
                        modifier = Modifier.width(300.dp).testTag("RTL"),
                        state = state,
                        startToEndFullSwipeEnabled = false,
                        expansionHapticEnabled = false,
                        actionItemBuilder = { start { Text("시작") } },
                    ) {
                        Box(Modifier.fillMaxWidth().height(72.dp)) { Text("항목") }
                    }
                }
            }
        }
        onNodeWithTag("RTL").performTouchInput {
            swipe(Offset(width * 2f / 3f, centerY), Offset(width * 8f / 15f, centerY), durationMillis = 120)
        }
        runOnIdle { assertEquals(IenSwipeBoxStates.StartVisible, settledValue()) }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun swipeThresholdsRespectCallerConfiguration(): TestResult = runComposeUiTest {
        val settledValues = mutableMapOf<String, () -> IenSwipeBoxStates>()

        setContent {
            IenTheme {
                Column {
                    for (name in listOf("기본", "속도", "거리", "끝 방향")) {
                        key(name) {
                            val state = rememberIenSwipeBoxState(
                                positionalThreshold = { distance ->
                                    distance * if (name == "거리") 0f else 1f
                                },
                                velocityThreshold = if (name == "속도" || name == "끝 방향") {
                                    100f
                                } else {
                                    Float.POSITIVE_INFINITY
                                },
                            )
                            settledValues[name] = { state.settledValue }
                            IenSwipeBox(
                                modifier = Modifier.width(300.dp).testTag(name),
                                state = state,
                                startToEndFullSwipeEnabled = false,
                                endToStartFullSwipeEnabled = false,
                                expansionHapticEnabled = false,
                                actionItemBuilder = {
                                    start { Text("시작 액션") }
                                    end { Text("끝 액션") }
                                },
                            ) {
                                Box(Modifier.fillMaxWidth().height(72.dp)) { Text(name) }
                            }
                        }
                    }
                }
            }
        }

        for (name in listOf("기본", "속도", "거리")) {
            onNodeWithTag(name).performTouchInput {
                swipe(Offset(width / 15f, centerY), Offset(width / 5f, centerY), durationMillis = 120)
            }
        }
        onNodeWithTag("끝 방향").performTouchInput {
            swipe(Offset(width * 2f / 3f, centerY), Offset(width * 8f / 15f, centerY), durationMillis = 120)
        }

        runOnIdle {
            assertEquals(IenSwipeBoxStates.Resting, settledValues.getValue("기본")(), "기본")
            assertEquals(IenSwipeBoxStates.StartVisible, settledValues.getValue("속도")(), "속도")
            assertEquals(IenSwipeBoxStates.StartVisible, settledValues.getValue("거리")(), "거리")
            assertEquals(IenSwipeBoxStates.EndVisible, settledValues.getValue("끝 방향")(), "끝 방향")
        }
    }
}
