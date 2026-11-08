package zone.ien.utils.ui.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestResult
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.menu.IenModal

private const val OverlayStressCycles = 20

class IenOverlayStressTest {
    @OptIn(ExperimentalTestApi::class, ExperimentalForeignApi::class)
    @Test
    fun bottomSheetOpensClosesAndDisposesItsContentRepeatedly(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val state = IenBottomSheetState(visible = false, detent = IenSheetDetent.Medium)
        val cycle = mutableIntStateOf(-1)
        var contentMountCount = 0
        var contentDisposeCount = 0

        setContent {
            IosOverlayTestHost {
                Box(Modifier.fillMaxSize()) {
                    Text("bottom-sheet-host")
                    IenBottomSheet(state = state) {
                        DisposableEffect(cycle.intValue) {
                            contentMountCount += 1
                            onDispose { contentDisposeCount += 1 }
                        }
                        Text("bottom-sheet-content-${cycle.intValue}")
                    }
                }
            }
        }
        repeat(OverlayStressCycles) { index ->
            println("iOS overlay stress BottomSheet ${index + 1}/$OverlayStressCycles")
            val message = "bottom-sheet-content-$index"
            runOnIdle {
                cycle.intValue = index
                state.show(IenSheetDetent.Medium)
            }
            advanceOverlayTransition()
            onNodeWithText(message).assertIsDisplayed()
            runOnIdle {
                assertEquals(index + 1, contentMountCount, "$message mount")
                assertEquals(index, contentDisposeCount, "$message 열린 상태의 dispose 수")
                state.hide()
            }
            advanceOverlayTransition()
            onNodeWithText(message).assertDoesNotExist()
            runOnIdle {
                assertEquals(index + 1, contentDisposeCount, "$message dispose")
            }
        }
    }

    @OptIn(ExperimentalTestApi::class, ExperimentalForeignApi::class)
    @Test
    fun modalOpensClosesAndDisposesItsContentRepeatedly(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val open = mutableStateOf(false)
        val cycle = mutableIntStateOf(-1)
        var contentMountCount = 0
        var contentDisposeCount = 0
        var exitedCount = 0

        setContent {
            IosOverlayTestHost {
                Box(Modifier.fillMaxSize()) {
                    Text("modal-host")
                    IenModal(
                        open = open.value,
                        onOpenChange = { open.value = it },
                        onExited = { exitedCount += 1 },
                    ) {
                        IenModal.Overlay()
                        DisposableEffect(cycle.intValue) {
                            contentMountCount += 1
                            onDispose { contentDisposeCount += 1 }
                        }
                        Box(Modifier.align(Alignment.Center)) {
                            Text("modal-content-${cycle.intValue}")
                            Button(onClick = { open.value = false }) {
                                Text("modal-close-${cycle.intValue}")
                            }
                        }
                    }
                }
            }
        }
        advanceOverlayTransition()
        val initialExitCount = runOnIdle { exitedCount }

        repeat(OverlayStressCycles) { index ->
            println("iOS overlay stress Modal ${index + 1}/$OverlayStressCycles")
            val message = "modal-content-$index"
            val closeLabel = "modal-close-$index"
            runOnIdle {
                cycle.intValue = index
                open.value = true
            }
            advanceOverlayTransition()
            onNodeWithText(message).assertIsDisplayed()
            onNodeWithText(closeLabel).performClick()
            advanceOverlayTransition()
            onNodeWithText(message).assertDoesNotExist()
            runOnIdle {
                assertEquals(index + 1, contentMountCount, "$message mount")
                assertEquals(index + 1, contentDisposeCount, "$message dispose")
                assertEquals(initialExitCount + index + 1, exitedCount, "$message exit callback")
            }
        }
    }

    @OptIn(ExperimentalTestApi::class, ExperimentalForeignApi::class)
    @Test
    fun dialogOpensClosesAndRemovesItsTextRepeatedly(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val visible = mutableStateOf(false)
        val cycle = mutableIntStateOf(-1)
        var confirmCount = 0

        setContent {
            IosOverlayTestHost {
                Box(Modifier.fillMaxSize()) {
                    Text("dialog-host")
                    IenDialog(
                        visible = visible.value,
                        onDismissRequest = { visible.value = false },
                        title = "dialog-title-${cycle.intValue}",
                        message = "dialog-message-${cycle.intValue}",
                        confirm = IenDialogAction(
                            text = "dialog-confirm-${cycle.intValue}",
                            onClick = {
                                confirmCount += 1
                                visible.value = false
                            },
                        ),
                    )
                }
            }
        }

        repeat(OverlayStressCycles) { index ->
            println("iOS overlay stress Dialog ${index + 1}/$OverlayStressCycles")
            val title = "dialog-title-$index"
            val message = "dialog-message-$index"
            val confirmLabel = "dialog-confirm-$index"
            runOnIdle {
                cycle.intValue = index
                visible.value = true
            }
            advanceOverlayTransition()
            onNodeWithText(title).assertIsDisplayed()
            onNodeWithText(message).assertIsDisplayed()
            onNodeWithText(confirmLabel).performClick()
            advanceOverlayTransition()
            onNodeWithText(title).assertDoesNotExist()
            onNodeWithText(message).assertDoesNotExist()
            runOnIdle { assertEquals(index + 1, confirmCount, "$title 확인 동작") }
        }
    }

    @OptIn(ExperimentalTestApi::class, ExperimentalForeignApi::class)
    @Test
    fun toastHostShowsAndDismissesMessagesRepeatedly(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        var toastState: IenToastState? = null

        setContent {
            IosOverlayTestHost {
                val state = rememberIenToastState()
                SideEffect { toastState = state }
                IenToastHost(state = state)
            }
        }

        repeat(OverlayStressCycles) { index ->
            println("iOS overlay stress Toast ${index + 1}/$OverlayStressCycles")
            val message = "toast-message-$index"
            runOnIdle {
                requireNotNull(toastState).show(message, duration = IenToastDuration.Indefinite)
            }
            advanceOverlayTransition()
            onNodeWithText(message).assertIsDisplayed()
            runOnIdle { requireNotNull(toastState).dismiss() }
            advanceOverlayTransition()
            onNodeWithText(message).assertDoesNotExist()
            runOnIdle { assertNull(toastState?.currentToast, "$message 닫힌 상태") }
        }
    }

    @OptIn(ExperimentalTestApi::class, ExperimentalForeignApi::class)
    @Test
    fun snackbarHostShowsAndDismissesMessagesRepeatedly(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val hostState = SnackbarHostState()
        var snackbarScope: CoroutineScope? = null
        var showJob: Job? = null
        var result: SnackbarResult? = null

        setContent {
            IosOverlayTestHost {
                val scope = rememberCoroutineScope()
                SideEffect { snackbarScope = scope }
                IenSnackbarHost(hostState = hostState)
            }
        }

        repeat(OverlayStressCycles) { index ->
            println("iOS overlay stress Snackbar ${index + 1}/$OverlayStressCycles")
            val message = "snackbar-message-$index"
            val actionLabel = "snackbar-close-$index"
            runOnIdle {
                result = null
                showJob = requireNotNull(snackbarScope).launch {
                    result = hostState.showIenSnackbar(
                        message = message,
                        actionLabel = actionLabel,
                        duration = SnackbarDuration.Indefinite,
                    )
                }
            }
            advanceOverlayTransition()
            onNodeWithText(message).assertIsDisplayed()
            onNodeWithText(actionLabel).performClick()
            advanceOverlayTransition()
            onNodeWithText(message).assertDoesNotExist()
            runOnIdle {
                assertEquals(SnackbarResult.ActionPerformed, result, "$message action result")
                assertNull(hostState.currentSnackbarData, "$message closed state")
                assertEquals(true, showJob?.isCompleted, "$message show job completion")
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
private fun IosOverlayTestHost(content: @Composable () -> Unit) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current.density
    val window = remember {
        UIWindow(
            CGRectMake(
                0.0,
                0.0,
                (windowInfo.containerSize.width / density).toDouble(),
                (windowInfo.containerSize.height / density).toDouble(),
            ),
        )
    }
    val viewController = remember(window) {
        UIViewController().also { window.addSubview(it.view) }
    }
    IenTheme {
        CompositionLocalProvider(LocalUIViewController provides viewController, content = content)
    }
}

@OptIn(ExperimentalTestApi::class)
private fun ComposeUiTest.advanceOverlayTransition() {
    mainClock.advanceTimeBy(500)
    waitForIdle()
}
