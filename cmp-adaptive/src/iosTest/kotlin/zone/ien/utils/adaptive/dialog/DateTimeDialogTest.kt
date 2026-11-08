package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.coroutines.test.TestResult
import platform.UIKit.UIViewController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import zone.ien.utils.ui.foundation.IenTheme

@OptIn(ExperimentalTestApi::class)
class DateTimeDialogTest {
    @Test
    fun removingDatePickerFromCompositionDismissesNativeSheet(): TestResult =
        verifySheetDismisses(isTimePicker = false, removeFromComposition = true)

    @Test
    fun hidingDatePickerDismissesNativeSheet(): TestResult =
        verifySheetDismisses(isTimePicker = false, removeFromComposition = false)

    @Test
    fun removingTimePickerFromCompositionDismissesNativeSheet(): TestResult =
        verifySheetDismisses(isTimePicker = true, removeFromComposition = true)

    private fun verifySheetDismisses(
        isTimePicker: Boolean,
        removeFromComposition: Boolean,
    ): TestResult = runComposeUiTest {
        val showPicker = mutableStateOf(true)
        val presenter = RecordingPresenter()
        setContent {
            CompositionLocalProvider(LocalUIViewController provides presenter) {
                IenTheme {
                    if (showPicker.value || !removeFromComposition) {
                        if (isTimePicker) {
                            TimePickerDialog(
                                visible = showPicker.value,
                                initialHour = 10,
                                initialMinute = 0,
                                title = "시간 선택",
                                onDismiss = { showPicker.value = false },
                                onConfirm = { _, _ -> showPicker.value = false },
                            )
                        } else {
                            DatePickerDialog(
                                visible = showPicker.value,
                                initialSelectedDateMillis = 1_799_100_000_000L,
                                title = "날짜 선택",
                                onDismiss = { showPicker.value = false },
                                onConfirm = { showPicker.value = false },
                            )
                        }
                    }
                }
            }
        }
        runOnIdle { assertNotNull(presenter.presentedViewController) }
        runOnIdle { showPicker.value = false }
        waitForIdle()
        runOnIdle {
            assertEquals(1, presenter.dismissCount)
            assertNull(presenter.presentedViewController)
        }
    }

    internal class RecordingPresenter : UIViewController(nibName = null, bundle = null) {
        private var sheet: UIViewController? = null
        var presentCount = 0
            private set
        var dismissCount = 0
            private set

        override fun presentedViewController(): UIViewController? = sheet

        override fun presentViewController(
            viewControllerToPresent: UIViewController,
            animated: Boolean,
            completion: (() -> Unit)?,
        ) {
            presentCount++
            sheet = viewControllerToPresent
            completion?.invoke()
        }

        override fun dismissViewControllerAnimated(flag: Boolean, completion: (() -> Unit)?) {
            dismissCount++
            sheet = null
            completion?.invoke()
        }
    }
}
