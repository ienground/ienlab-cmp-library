package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.test.TestResult
import platform.UIKit.UIDatePicker
import platform.UIKit.UINavigationBar
import platform.UIKit.UIViewController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import zone.ien.utils.adaptive.dialog.test.IENDatePickerAllocationTestCount
import zone.ien.utils.adaptive.dialog.test.IENDatePickerAllocationTestReset
import zone.ien.utils.adaptive.dialog.test.IENDatePickerAllocationTestStart
import zone.ien.utils.adaptive.dialog.test.IENDatePickerAllocationTestStop
import zone.ien.utils.adaptive.dialog.DateTimeDialogTest.RecordingPresenter
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme

@OptIn(ExperimentalForeignApi::class, ExperimentalTestApi::class)
class DateTimeDialogAllocationTest {
    @Test
    fun allocationCounterTracksOnlyExplicitUidatepickerInstances(): TestResult = runComposeUiTest {
        assertTrue(IENDatePickerAllocationTestStart())
        try {
            UIDatePicker()
            assertEquals(1, IENDatePickerAllocationTestCount())
        } finally {
            IENDatePickerAllocationTestStop()
        }
    }

    @Test
    fun hiddenDatePickerIsCreatedOnlyWhenShown(): TestResult =
        verifyHiddenAndShown(isTimePicker = false)

    @Test
    fun hiddenTimePickerIsCreatedOnlyWhenShown(): TestResult =
        verifyHiddenAndShown(isTimePicker = true)

    @Test
    fun datePickerCallbacksCanChangeWithoutAllocatingAnotherPicker(): TestResult =
        verifyCallbacksChange(isTimePicker = false)

    @Test
    fun timePickerCallbacksCanChangeWithoutAllocatingAnotherPicker(): TestResult =
        verifyCallbacksChange(isTimePicker = true)

    @Test
    fun datePickerTitleUpdatesItsExistingNavigationBar(): TestResult =
        verifyTitleChange(isTimePicker = false)

    @Test
    fun timePickerTitleUpdatesItsExistingNavigationBar(): TestResult =
        verifyTitleChange(isTimePicker = true)

    private fun verifyHiddenAndShown(isTimePicker: Boolean): TestResult = runComposeUiTest {
        assertTrue(IENDatePickerAllocationTestStart())
        try {
            UIDatePicker()
            assertEquals(1, IENDatePickerAllocationTestCount())
            IENDatePickerAllocationTestReset()

            val visible = mutableStateOf(false)
            val presenter = RecordingPresenter()
            setContent {
                CompositionLocalProvider(LocalUIViewController provides presenter) {
                    IenTheme {
                        Picker(isTimePicker, visible.value, "초기 제목")
                    }
                }
            }
            waitForIdle()
            runOnIdle { assertEquals(0, IENDatePickerAllocationTestCount()) }

            runOnIdle { visible.value = true }
            waitForIdle()
            runOnIdle {
                assertEquals(1, IENDatePickerAllocationTestCount())
                assertEquals(1, presenter.presentCount)
            }
        } finally {
            IENDatePickerAllocationTestStop()
        }
    }

    private fun verifyCallbacksChange(isTimePicker: Boolean): TestResult = runComposeUiTest {
        assertTrue(IENDatePickerAllocationTestStart())
        try {
            UIDatePicker()
            IENDatePickerAllocationTestReset()

            val onDismiss = mutableStateOf<() -> Unit>({})
            val onDateConfirm = mutableStateOf<(Long) -> Unit>({})
            val onTimeConfirm = mutableStateOf<(Int, Int) -> Unit>({ _, _ -> })
            val callbackRevision = mutableIntStateOf(0)
            val presenter = RecordingPresenter()
            setContent {
                CompositionLocalProvider(LocalUIViewController provides presenter) {
                    IenTheme {
                        Picker(
                            isTimePicker = isTimePicker,
                            visible = true,
                            title = "선택",
                            onDismiss = onDismiss.value,
                            onDateConfirm = onDateConfirm.value,
                            onTimeConfirm = onTimeConfirm.value,
                        )
                    }
                }
            }
            waitForIdle()

            val originalSheet = runOnIdle { assertNotNull(presenter.presentedViewController) }
            runOnIdle {
                onDismiss.value = { callbackRevision.intValue++ }
                onDateConfirm.value = { callbackRevision.intValue++ }
                onTimeConfirm.value = { _, _ -> callbackRevision.intValue++ }
            }
            waitForIdle()

            runOnIdle {
                assertEquals(1, IENDatePickerAllocationTestCount())
                assertEquals(1, presenter.presentCount)
                assertEquals(originalSheet, presenter.presentedViewController)
            }
        } finally {
            IENDatePickerAllocationTestStop()
        }
    }

    private fun verifyTitleChange(isTimePicker: Boolean): TestResult = runComposeUiTest {
        assertTrue(IENDatePickerAllocationTestStart())
        try {
            UIDatePicker()
            IENDatePickerAllocationTestReset()

            val title = mutableStateOf("초기 제목")
            val tone = mutableStateOf(IenSemanticTone.Brand)
            val presenter = RecordingPresenter()
            setContent {
                CompositionLocalProvider(LocalUIViewController provides presenter) {
                    IenTheme {
                        Picker(isTimePicker, visible = true, title = title.value, tone = tone.value)
                    }
                }
            }
            waitForIdle()

            val originalSheet = runOnIdle { assertNotNull(presenter.presentedViewController) }
            val originalViewTint = originalSheet.view.tintColor
            val navBar = navigationBar(originalSheet)
            val originalNavigationTint = navBar.tintColor
            assertEquals("초기 제목", navigationTitle(originalSheet))
            runOnIdle {
                title.value = "갱신 제목"
                tone.value = IenSemanticTone.Danger
            }
            waitForIdle()

            runOnIdle {
                assertEquals("갱신 제목", navigationTitle(originalSheet))
                assertNotEquals(originalViewTint, originalSheet.view.tintColor)
                assertNotEquals(originalNavigationTint, navBar.tintColor)
                assertEquals(1, presenter.presentCount)
                assertEquals(originalSheet, presenter.presentedViewController)
            }
        } finally {
            IENDatePickerAllocationTestStop()
        }
    }

    @androidx.compose.runtime.Composable
    private fun Picker(
        isTimePicker: Boolean,
        visible: Boolean,
        title: String,
        tone: IenSemanticTone = IenSemanticTone.Brand,
        onDismiss: () -> Unit = {},
        onDateConfirm: (Long) -> Unit = {},
        onTimeConfirm: (Int, Int) -> Unit = { _, _ -> },
    ) {
        if (isTimePicker) {
            TimePickerDialog(
                visible = visible,
                initialHour = 10,
                initialMinute = 15,
                title = title,
                tone = tone,
                onDismiss = onDismiss,
                onConfirm = onTimeConfirm,
            )
        } else {
            DatePickerDialog(
                visible = visible,
                initialSelectedDateMillis = 1_799_100_000_000L,
                title = title,
                tone = tone,
                onDismiss = onDismiss,
                onConfirm = onDateConfirm,
            )
        }
    }

    private fun navigationTitle(viewController: UIViewController): String? =
        navigationBar(viewController).topItem?.title

    private fun navigationBar(viewController: UIViewController): UINavigationBar =
        viewController.view.subviews.filterIsInstance<UINavigationBar>().single()
}
