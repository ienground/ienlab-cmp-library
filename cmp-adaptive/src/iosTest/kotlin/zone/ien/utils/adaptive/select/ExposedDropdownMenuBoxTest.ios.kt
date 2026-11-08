package zone.ien.utils.adaptive.select

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.coroutines.test.TestResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import zone.ien.utils.adaptive.dialog.DateTimeDialogTest.RecordingPresenter
import zone.ien.utils.ui.foundation.IenTheme

@OptIn(ExperimentalTestApi::class)
class ExposedDropdownMenuBoxTest {
    @Test
    fun singleSelectionDropdownKeepsItsSheetDuringInputChangesAndDismissesWhenRemoved(): TestResult =
        runComposeUiTest {
            val visible = mutableStateOf(true)
            val items = mutableStateOf(mapOf("first" to "첫 번째", "second" to "두 번째"))
            val selectedItem = mutableStateOf("first")
            val presenter = RecordingPresenter()
            var openDropdown: (() -> Unit)? = null

            setContent {
                CompositionLocalProvider(LocalUIViewController provides presenter) {
                    IenTheme {
                        if (visible.value) {
                            ExposedDropdownMenuBox(
                                title = "단일 선택",
                                itemsWithLabels = items.value,
                                currentItem = selectedItem.value,
                                onItemSelected = { selectedItem.value = it },
                                trailingIconButton = { onClick, _ -> openDropdown = onClick },
                                dropdownMenuItem = { _, _ -> },
                                textField = { _, trailingIcon -> trailingIcon() },
                            )
                        }
                    }
                }
            }

            runOnIdle { assertNotNull(openDropdown).invoke() }
            waitForIdle()
            val originalSheet = runOnIdle { presenter.presentedViewController }
            assertNotNull(originalSheet)

            runOnIdle {
                selectedItem.value = "second"
                items.value = mapOf("first" to "첫 항목", "second" to "두 번째 항목", "third" to "세 번째")
            }
            waitForIdle()
            runOnIdle {
                assertEquals(1, presenter.presentCount)
                assertNotNull(presenter.presentedViewController)
                visible.value = false
            }
            waitForIdle()
            runOnIdle {
                assertEquals(1, presenter.dismissCount)
                assertNull(presenter.presentedViewController)
            }
        }

    @Test
    fun multiSelectionDropdownKeepsItsSheetDuringInputChangesAndDismissesWhenRemoved(): TestResult =
        runComposeUiTest {
            val visible = mutableStateOf(true)
            val items = mutableStateOf(mapOf("first" to "첫 번째", "second" to "두 번째"))
            val selectedItems = mutableStateOf(listOf("first"))
            val presenter = RecordingPresenter()
            var openDropdown: (() -> Unit)? = null

            setContent {
                CompositionLocalProvider(LocalUIViewController provides presenter) {
                    IenTheme {
                        if (visible.value) {
                            ExposedDropdownMenuBox(
                                title = "다중 선택",
                                itemsWithLabels = items.value,
                                currentItems = selectedItems.value,
                                onItemsSelected = { selectedItems.value = it },
                                trailingIconButton = { onClick, _ -> openDropdown = onClick },
                                dropdownMenuItem = { _, _, _ -> },
                                textField = { _, trailingIcon -> trailingIcon() },
                            )
                        }
                    }
                }
            }

            runOnIdle { assertNotNull(openDropdown).invoke() }
            waitForIdle()
            val originalSheet = runOnIdle { presenter.presentedViewController }
            assertNotNull(originalSheet)

            runOnIdle {
                selectedItems.value = listOf("second")
                items.value = mapOf("first" to "첫 항목", "second" to "두 번째 항목", "third" to "세 번째")
            }
            waitForIdle()
            runOnIdle {
                assertEquals(1, presenter.presentCount)
                assertNotNull(presenter.presentedViewController)
                visible.value = false
            }
            waitForIdle()
            runOnIdle {
                assertEquals(1, presenter.dismissCount)
                assertNull(presenter.presentedViewController)
            }
        }
}
