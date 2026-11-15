package zone.ien.utils.example.ui.screens.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInputSelection
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import androidx.compose.ui.uikit.LocalUIViewController
import platform.UIKit.UIViewController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.coroutines.test.TestResult
import platform.posix.CLOCK_PROCESS_CPUTIME_ID
import platform.posix.clock_gettime
import platform.posix.timespec
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenFullSecureKeyboard
import zone.ien.utils.ui.interactive.IenKeyboardAction
import zone.ien.utils.ui.interactive.IenSecureKeyboardState
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.initializeIenIos

private const val KeypadCpuSamples = 7

class KeypadCompositionPerformanceTest {
    @OptIn(ExperimentalTestApi::class, ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    @Test
    fun textFieldsReadClipboardOnlyForEditingActions(): TestResult = runComposeUiTest {
        val previous = ComposeFoundationFlags.isNewContextMenuEnabled
        val clipboard = CountingClipboard()
        val value = mutableStateOf("ABCD")
        try {
            runOnUiThread { initializeIenIos() }
            setContent {
                IenTheme {
                    CompositionLocalProvider(LocalClipboard provides clipboard) {
                        IenTextField(value = value.value, onValueChange = { value.value = it })
                    }
                }
            }
            waitForIdle()
            assertEquals(0, clipboard.reads, "포커스나 편집 명령 없이 클립보드를 미리 조회하면 안 됩니다")
            val field = onNode(hasSetTextAction())
            field.performSemanticsAction(SemanticsActions.RequestFocus) { assertTrue(it()) }
            field.performTextInputSelection(TextRange(0, 4))
            field.performSemanticsAction(SemanticsActions.CopyText) { assertTrue(it()) }
            waitForIdle()
            assertEquals("ABCD", clipboard.entry?.getPlainText())
            field.performTextInputSelection(TextRange(0, 4))
            field.performSemanticsAction(SemanticsActions.CutText) { assertTrue(it()) }
            waitForIdle()
            assertEquals("", value.value)
            field.performSemanticsAction(SemanticsActions.PasteText) { assertTrue(it()) }
            waitForIdle()
            assertEquals("ABCD", value.value)
            assertTrue(clipboard.reads > 0, "붙여넣기 명령은 실제 클립보드 경로를 사용해야 합니다")
        } finally {
            ComposeFoundationFlags.isNewContextMenuEnabled = previous
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun boundedEagerStressStillComposesEveryKeypad(): TestResult = runComposeUiTest {
        setContent {
            IenTheme {
                Box(Modifier.fillMaxWidth().heightIn(min = 240.dp, max = 280.dp)) {
                    KeypadSection(scrollable = false)
                }
            }
        }
        waitForIdle()
        onNodeWithText("Alphabet Keypad: ").assertExists()
        onNodeWithText("Full Secure Keypad: ").assertExists()
        onNode(hasText("한국어") or hasText("한글")).assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun playgroundDefersTheOffscreenConfigurationExamples(): TestResult = runComposeUiTest {
        setContent {
            IenTheme {
                Box(Modifier.fillMaxWidth().heightIn(max = 600.dp)) {
                    val controller = remember { UIViewController() }
                    CompositionLocalProvider(LocalUIViewController provides controller) {
                        DesignSystemScreen(componentId = "keypad")
                    }
                }
            }
        }
        waitForIdle()
        onNodeWithText("Full Secure Keypad: ").assertDoesNotExist()
        onAllNodesWithText("A")[0].performClick()
        onNodeWithText("Alphabet Keypad: A").assertExists()
        val sections = onAllNodes(hasScrollToIndexAction())[0]
        sections.performScrollToIndex(2)
        onNodeWithText("구성 예시").assertExists()
        onNodeWithText("Full Secure Keypad: ").assertDoesNotExist()
        onAllNodes(hasScrollToIndexAction()).onLast().performScrollToIndex(6)
        onNodeWithText("Full Secure Keypad: ").assertExists()
        sections.performScrollToIndex(0)
        onAllNodes(hasScrollToIndexAction())[1].performScrollToIndex(0)
        onNodeWithText("Alphabet Keypad: A").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun boundedPreviewComposesVisibleKeypadsAndRetainsInputAcrossScrolling(): TestResult = runComposeUiTest {
        setContent {
            IenTheme {
                Box(Modifier.fillMaxWidth().heightIn(min = 240.dp, max = 280.dp)) {
                    CompositionLocalProvider(LocalComponentSectionChrome provides false) {
                        KeypadSection()
                    }
                }
            }
        }
        waitForIdle()
        onNodeWithText("Full Secure Keypad: ").assertDoesNotExist()
        onAllNodesWithText("A")[0].performClick()
        onNodeWithText("Alphabet Keypad: A").assertExists()
        val keypads = onNode(hasScrollToIndexAction())
        listOf(
            1 to "커스텀 배열: ",
            2 to "보안 알파벳 키보드: A",
            3 to "Number Keypad: ",
            4 to "커스텀 숫자 배열: ",
            5 to "보안 숫자 입력:  / 더미: ",
        ).forEach { (index, title) ->
            keypads.performScrollToIndex(index)
            onNodeWithText(title).assertExists()
        }
        keypads.performScrollToIndex(6)
        onNodeWithText("Full Secure Keypad: ").assertExists()
        keypads.performScrollToIndex(7)
        onNode(hasText("한국어") or hasText("한글")).assertExists()
        keypads.performScrollToIndex(0)
        onNodeWithText("Alphabet Keypad: A").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun enabledTransitionComparesOneSecureKeyboardWithTheFullShowcase(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val enabled = mutableStateOf(true)
        val showFullShowcase = mutableStateOf(false)

        setContent {
            val secureKeyboardState = remember { IenSecureKeyboardState(value = "") }
            IenTheme {
                if (showFullShowcase.value) {
                    Box(
                        modifier = Modifier.fillMaxWidth().heightIn(min = 240.dp, max = 280.dp)
                            .verticalScroll(rememberScrollState()),
                        contentAlignment = Alignment.Center,
                    ) {
                        KeypadSection(
                            controls = mapOf("disabled" to (!enabled.value).toString()),
                        )
                    }
                } else {
                    IenFullSecureKeyboard(
                        state = secureKeyboardState,
                        onAction = { _: IenKeyboardAction -> },
                        onLanguageChange = {},
                        enabled = enabled.value,
                    )
                }
            }
        }

        mainClock.advanceTimeByFrame()
        waitForIdle()

        fun measureEnabledTransitionCpuNanos(): Long {
            val start = processCpuTimeNanos()
            runOnIdle { enabled.value = !enabled.value }
            mainClock.advanceTimeByFrame()
            waitForIdle()
            return processCpuTimeNanos() - start
        }

        val singleKeyboardSamples = List(KeypadCpuSamples) { measureEnabledTransitionCpuNanos() }
        runOnIdle { showFullShowcase.value = true }
        mainClock.advanceTimeByFrame()
        waitForIdle()
        val showcaseSamples = List(KeypadCpuSamples) { measureEnabledTransitionCpuNanos() }

        val singleKeyboardMedian = singleKeyboardSamples.median()
        val showcaseMedian = showcaseSamples.median()
        println(
            "iOS 키패드 활성 전환 프로세스 CPU 중앙값: " +
                "singleSecureKeyboard=${singleKeyboardMedian / 1_000_000.0}ms, " +
                "fullKeypadShowcase=${showcaseMedian / 1_000_000.0}ms",
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private class CountingClipboard : Clipboard {
    var reads = 0
    var entry: ClipEntry? = ClipEntry.withPlainText("붙여넣기 검증")

    override suspend fun getClipEntry(): ClipEntry? {
        reads++
        return entry
    }

    override suspend fun setClipEntry(clipEntry: ClipEntry?) {
        entry = clipEntry
    }
}

private fun List<Long>.median(): Long = sorted()[size / 2]

@OptIn(ExperimentalForeignApi::class)
private fun processCpuTimeNanos(): Long = memScoped {
    val time = alloc<timespec>()
    check(clock_gettime(CLOCK_PROCESS_CPUTIME_ID.toUInt(), time.ptr) == 0) {
        "iOS 프로세스 CPU 시간을 읽을 수 없습니다"
    }
    time.tv_sec * 1_000_000_000L + time.tv_nsec
}
