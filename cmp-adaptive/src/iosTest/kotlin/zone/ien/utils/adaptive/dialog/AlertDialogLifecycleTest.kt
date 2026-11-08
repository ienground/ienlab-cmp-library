package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composer
import androidx.compose.runtime.CompositionTracer
import androidx.compose.runtime.InternalComposeTracingApi
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.coroutines.test.TestResult
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIAlertController
import platform.UIKit.UIProgressView
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldTextDidChangeNotification
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.utils.TextFieldDialogData

@OptIn(ExperimentalTestApi::class)
class AlertDialogLifecycleTest {
    @Test
    fun removingAlertDismissesItsNativeController(): TestResult = verifyDismisses {
        HigBaseAlertDialog(visible = it, title = "알림", message = null, buttons = {})
    }

    @Test
    fun hidingAlertDismissesItsNativeController(): TestResult = verifyDismisses(removeFromComposition = false) {
        HigBaseAlertDialog(visible = it, title = "알림", message = null, buttons = {})
    }

    @Test
    fun removingLoadingDialogDismissesItsNativeController(): TestResult = verifyDismisses {
        ProgressDialog(visible = it)
    }

    @Test
    fun removingProgressDialogDismissesItsNativeController(): TestResult = verifyDismisses {
        ProgressDialog(visible = it, progress = { 0.5f })
    }

    @Test
    fun alertTitleAndMessageUpdateWithoutPresentingAgain(): TestResult = runComposeUiTest {
        val title = mutableStateOf("시작")
        val presenter = DateTimeDialogTest.RecordingPresenter()
        setContent {
            CompositionLocalProvider(LocalUIViewController provides presenter) {
                IenTheme {
                    HigBaseAlertDialog(visible = true, title = title.value, message = title.value, buttons = {})
                }
            }
        }
        val alert = runOnIdle { assertIs<UIAlertController>(presenter.presentedViewController) }
        runOnIdle { title.value = "완료" }
        waitForIdle()
        runOnIdle {
            assertEquals("완료", alert.title)
            assertEquals("완료", alert.message)
            assertEquals(1, presenter.presentCount)
        }
    }

    @OptIn(InternalComposeTracingApi::class)
    @Test
    fun progressUpdatesNativeViewWithoutRecomposingTheDialog(): TestResult = runComposeUiTest {
        val progress = mutableFloatStateOf(0.25f)
        val presenter = DateTimeDialogTest.RecordingPresenter()
        var compositions = 0
        val tracer = object : CompositionTracer {
            override fun isTraceInProgress(): Boolean = true
            override fun traceEventEnd() = Unit
            override fun traceEventStart(key: Int, dirty1: Int, dirty2: Int, info: String) {
                if (info.startsWith("zone.ien.utils.adaptive.dialog.ProgressDialog (")) compositions++
            }
        }
        Composer.setTracer(tracer)
        try {
            setContent {
                CompositionLocalProvider(LocalUIViewController provides presenter) {
                    IenTheme { ProgressDialog(visible = true, progress = { progress.floatValue }) }
                }
            }
            val view = runOnIdle {
                assertIs<UIAlertController>(presenter.presentedViewController)
                    .view.subviews.filterIsInstance<UIProgressView>().single()
            }
            val initialCompositions = runOnIdle {
                assertEquals(0.25f, view.progress)
                compositions
            }
            runOnIdle { progress.floatValue = 0.75f }
            waitForIdle()
            runOnIdle {
                assertEquals(0.75f, view.progress)
                assertEquals(initialCompositions, compositions, "진행률 갱신은 UIKit 뷰에 직접 적용되어야 합니다")
                assertEquals(1, presenter.presentCount)
            }
        } finally {
            Composer.setTracer(null)
        }
    }

    @Test
    fun removedTextFieldDialogStopsObservingItsNativeFields(): TestResult = runComposeUiTest {
        val show = mutableStateOf(true)
        val presenter = DateTimeDialogTest.RecordingPresenter()
        var textChanges = 0
        val field = TextFieldDialogData(onValueChange = { textChanges++; it })
        setContent {
            CompositionLocalProvider(LocalUIViewController provides presenter) {
                IenTheme {
                    if (show.value) {
                        TextFieldDialog(
                            visible = true,
                            title = "입력",
                            textFields = mapOf("value" to field),
                            onDismiss = {},
                            onConfirm = {},
                        )
                    }
                }
            }
        }
        val nativeField = runOnIdle {
            assertIs<UITextField>(assertIs<UIAlertController>(presenter.presentedViewController).textFields?.firstOrNull())
        }
        runOnIdle {
            NSNotificationCenter.defaultCenter.postNotificationName(UITextFieldTextDidChangeNotification, nativeField)
            assertEquals(1, textChanges)
            show.value = false
        }
        waitForIdle()
        runOnIdle {
            NSNotificationCenter.defaultCenter.postNotificationName(UITextFieldTextDidChangeNotification, nativeField)
            assertEquals(1, textChanges, "제거된 다이얼로그의 관찰자는 입력 콜백을 실행하면 안 됩니다")
        }
    }

    @Test
    fun initiallyHiddenTextFieldDialogStopsObservingAfterRemoval(): TestResult = runComposeUiTest {
        val mounted = mutableStateOf(true)
        val visible = mutableStateOf(false)
        val presenter = DateTimeDialogTest.RecordingPresenter()
        var textChanges = 0
        val field = TextFieldDialogData(onValueChange = { textChanges++; it })
        setContent {
            CompositionLocalProvider(LocalUIViewController provides presenter) {
                IenTheme {
                    if (mounted.value) {
                        TextFieldDialog(
                            visible = visible.value,
                            title = "입력",
                            textFields = mapOf("value" to field),
                            onDismiss = {},
                            onConfirm = {},
                        )
                    }
                }
            }
        }
        runOnIdle { visible.value = true }
        waitForIdle()
        val nativeField = runOnIdle {
            assertIs<UITextField>(assertIs<UIAlertController>(presenter.presentedViewController).textFields?.firstOrNull())
        }
        runOnIdle {
            NSNotificationCenter.defaultCenter.postNotificationName(UITextFieldTextDidChangeNotification, nativeField)
            assertEquals(1, textChanges)
            mounted.value = false
        }
        waitForIdle()
        runOnIdle {
            NSNotificationCenter.defaultCenter.postNotificationName(UITextFieldTextDidChangeNotification, nativeField)
            assertEquals(1, textChanges, "처음 숨겨져 있던 다이얼로그도 제거 시 관찰자를 해제해야 합니다")
        }
    }

    @Test
    fun nativeTextFieldUsesTheUpdatedValueChangeCallback(): TestResult = runComposeUiTest {
        val version = mutableIntStateOf(1)
        val presenter = DateTimeDialogTest.RecordingPresenter()
        var receivedVersion = 0
        setContent {
            val currentVersion = version.intValue
            CompositionLocalProvider(LocalUIViewController provides presenter) {
                IenTheme {
                    TextFieldDialog(
                        visible = true,
                        title = "입력",
                        textFields = mapOf("value" to TextFieldDialogData(onValueChange = {
                            receivedVersion = currentVersion
                            it
                        })),
                        onDismiss = {},
                        onConfirm = {},
                    )
                }
            }
        }
        val nativeField = runOnIdle {
            assertIs<UITextField>(assertIs<UIAlertController>(presenter.presentedViewController).textFields?.firstOrNull())
        }
        runOnIdle { version.intValue = 2 }
        waitForIdle()
        runOnIdle {
            NSNotificationCenter.defaultCenter.postNotificationName(UITextFieldTextDidChangeNotification, nativeField)
            assertEquals(2, receivedVersion, "표시 중인 입력창은 최신 입력 콜백을 사용해야 합니다")
            assertEquals(1, presenter.presentCount)
        }
    }

    @Test
    fun inputTransformationUpdatesTheDisplayedNativeText(): TestResult = runComposeUiTest {
        val presenter = DateTimeDialogTest.RecordingPresenter()
        setContent {
            CompositionLocalProvider(LocalUIViewController provides presenter) {
                IenTheme {
                    TextFieldDialog(
                        visible = true,
                        title = "입력",
                        textFields = mapOf("value" to TextFieldDialogData(onValueChange = { it.uppercase() })),
                        onDismiss = {},
                        onConfirm = {},
                    )
                }
            }
        }
        runOnIdle {
            val field = assertIs<UITextField>(assertIs<UIAlertController>(presenter.presentedViewController).textFields?.firstOrNull())
            field.text = "ab"
            NSNotificationCenter.defaultCenter.postNotificationName(UITextFieldTextDidChangeNotification, field)
            assertEquals("AB", field.text)
        }
    }

    @Test
    fun changingFieldKeysReplacesTheNativeFormAndStopsItsOldObservers(): TestResult = runComposeUiTest {
        val key = mutableStateOf("first")
        val presenter = DateTimeDialogTest.RecordingPresenter()
        var changes = 0
        setContent {
            CompositionLocalProvider(LocalUIViewController provides presenter) {
                IenTheme {
                    TextFieldDialog(
                        visible = true,
                        title = "입력",
                        textFields = mapOf(key.value to TextFieldDialogData(
                            initialValue = key.value,
                            onValueChange = { changes++; it },
                        )),
                        onDismiss = {},
                        onConfirm = {},
                    )
                }
            }
        }
        val oldField = runOnIdle {
            assertIs<UITextField>(assertIs<UIAlertController>(presenter.presentedViewController).textFields?.firstOrNull())
        }
        runOnIdle { key.value = "second" }
        waitForIdle()
        runOnIdle {
            val field = assertIs<UITextField>(assertIs<UIAlertController>(presenter.presentedViewController).textFields?.firstOrNull())
            assertEquals("second", field.text)
            assertEquals(2, presenter.presentCount)
            assertEquals(1, presenter.dismissCount)
            NSNotificationCenter.defaultCenter.postNotificationName(UITextFieldTextDidChangeNotification, oldField)
            assertEquals(0, changes)
        }
    }

    @Test
    fun changingPresenterStopsThePreviousTextFieldObservers(): TestResult = runComposeUiTest {
        val firstPresenter = DateTimeDialogTest.RecordingPresenter()
        val secondPresenter = DateTimeDialogTest.RecordingPresenter()
        val presenter = mutableStateOf(firstPresenter)
        var changes = 0
        setContent {
            CompositionLocalProvider(LocalUIViewController provides presenter.value) {
                IenTheme {
                    TextFieldDialog(
                        visible = true,
                        title = "입력",
                        textFields = mapOf("value" to TextFieldDialogData(onValueChange = { changes++; it })),
                        onDismiss = {},
                        onConfirm = {},
                    )
                }
            }
        }
        val oldField = runOnIdle {
            assertIs<UITextField>(assertIs<UIAlertController>(firstPresenter.presentedViewController).textFields?.firstOrNull())
        }
        runOnIdle { presenter.value = secondPresenter }
        waitForIdle()
        runOnIdle {
            assertEquals(1, firstPresenter.dismissCount)
            assertNotNull(secondPresenter.presentedViewController)
            NSNotificationCenter.defaultCenter.postNotificationName(UITextFieldTextDidChangeNotification, oldField)
            assertEquals(0, changes, "소유자가 바뀌면 이전 입력창의 관찰자도 해제해야 합니다")
        }
    }

    private fun verifyDismisses(
        removeFromComposition: Boolean = true,
        content: @Composable (Boolean) -> Unit,
    ): TestResult = runComposeUiTest {
        val show = mutableStateOf(true)
        val presenter = DateTimeDialogTest.RecordingPresenter()
        setContent {
            CompositionLocalProvider(LocalUIViewController provides presenter) {
                IenTheme {
                    if (show.value || !removeFromComposition) content(show.value)
                }
            }
        }
        runOnIdle {
            assertNotNull(presenter.presentedViewController)
            show.value = false
        }
        waitForIdle()
        runOnIdle {
            assertEquals(1, presenter.dismissCount)
            assertNull(presenter.presentedViewController)
        }
    }
}
