package zone.ien.utils.adaptive.select

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.test.TestResult
import platform.Foundation.NSIndexPath
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UINavigationBar
import platform.UIKit.UINavigationController
import platform.UIKit.UIPickerView
import platform.UIKit.UITableView
import platform.UIKit.UITableViewCellAccessoryType
import platform.UIKit.indexPathForRow
import platform.UIKit.navigationItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import zone.ien.utils.adaptive.dialog.test.IENInvokeZeroArgumentBarButtonAction
import zone.ien.utils.adaptive.dialog.DateTimeDialogTest.RecordingPresenter
import zone.ien.utils.ui.foundation.IenTheme

@OptIn(ExperimentalTestApi::class)
class ExposedDropdownMenuBoxTest {
    @Test
    fun singleSelectionDropdownConfirmsExternalSelectionWithoutRecreatingItsSheet(): TestResult =
        runComposeUiTest {
            val visible = mutableStateOf(true)
            val items = mutableStateOf(mapOf("first" to "첫 번째", "second" to "두 번째"))
            val selectedItem = mutableStateOf("first")
            val presenter = RecordingPresenter()
            var openDropdown: (() -> Unit)? = null
            val confirmedItems = mutableListOf<String>()

            setContent {
                CompositionLocalProvider(LocalUIViewController provides presenter) {
                    IenTheme {
                        if (visible.value) {
                            ExposedDropdownMenuBox(
                                title = "단일 선택",
                                itemsWithLabels = items.value,
                                currentItem = selectedItem.value,
                                onItemSelected = {
                                    confirmedItems += it
                                    selectedItem.value = it
                                },
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
            val sheet = assertNotNull(originalSheet)
            val pickerView = sheet.view.subviews.filterIsInstance<UIPickerView>().single()
            runOnIdle {
                assertEquals(1, presenter.presentCount)
                assertEquals(sheet, presenter.presentedViewController)
                assertEquals(1L, pickerView.selectedRowInComponent(0))
                val navBar = sheet.view.subviews.filterIsInstance<UINavigationBar>().single()
                val navItem = assertNotNull(navBar.topItem)
                confirm(assertNotNull(navItem.rightBarButtonItem))
            }
            waitForIdle()
            runOnIdle {
                assertEquals(listOf("second"), confirmedItems)
                assertEquals(1, presenter.dismissCount)
                assertNull(presenter.presentedViewController)
                assertNotNull(openDropdown).invoke()
            }
            waitForIdle()
            runOnIdle {
                assertEquals(2, presenter.presentCount)
                visible.value = false
            }
            waitForIdle()
            runOnIdle {
                assertEquals(2, presenter.dismissCount)
                assertNull(presenter.presentedViewController)
            }
        }

    @Test
    fun multiSelectionDropdownConfirmsExternalSelectionWithoutRecreatingItsSheet(): TestResult =
        runComposeUiTest {
            val visible = mutableStateOf(true)
            val items = mutableStateOf(mapOf("first" to "첫 번째", "second" to "두 번째"))
            val selectedItems = mutableStateOf(listOf("first"))
            val presenter = RecordingPresenter()
            var openDropdown: (() -> Unit)? = null
            val confirmedItems = mutableListOf<List<String>>()

            setContent {
                CompositionLocalProvider(LocalUIViewController provides presenter) {
                    IenTheme {
                        if (visible.value) {
                            ExposedDropdownMenuBox(
                                title = "다중 선택",
                                itemsWithLabels = items.value,
                                currentItems = selectedItems.value,
                                onItemsSelected = {
                                    confirmedItems += it
                                    selectedItems.value = it
                                },
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
            val sheet = assertIs<UINavigationController>(originalSheet)
            val picker = sheet.viewControllers.first() as ListPickerViewController<String>
            runOnIdle {
                assertEquals(1, presenter.presentCount)
                assertEquals(sheet, presenter.presentedViewController)
                assertEquals(setOf("두 번째 항목"), checkedLabels(picker))
                confirm(assertNotNull(picker.navigationItem.rightBarButtonItem))
            }
            waitForIdle()
            runOnIdle {
                assertEquals(listOf(listOf("second")), confirmedItems)
                assertEquals(1, presenter.dismissCount)
                assertNull(presenter.presentedViewController)
                assertNotNull(openDropdown).invoke()
            }
            waitForIdle()
            runOnIdle {
                assertEquals(2, presenter.presentCount)
                visible.value = false
            }
            waitForIdle()
            runOnIdle {
                assertEquals(2, presenter.dismissCount)
                assertNull(presenter.presentedViewController)
            }
        }

    @Test
    fun singleSelectionDropdownKeepsUserDraftAcrossItemUpdates(): TestResult = runComposeUiTest {
        val visible = mutableStateOf(true)
        val items = mutableStateOf(mapOf("first" to "첫 번째", "second" to "두 번째", "third" to "세 번째"))
        val selectedItem = mutableStateOf("first")
        val presenter = RecordingPresenter()
        var openDropdown: (() -> Unit)? = null
        val confirmedItems = mutableListOf<String>()

        setContent {
            CompositionLocalProvider(LocalUIViewController provides presenter) {
                IenTheme {
                    if (visible.value) {
                        ExposedDropdownMenuBox(
                            title = "단일 선택",
                            itemsWithLabels = items.value,
                            currentItem = selectedItem.value,
                            onItemSelected = { confirmedItems += it },
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
        val sheet = assertNotNull(presenter.presentedViewController)
        val pickerView = sheet.view.subviews.filterIsInstance<UIPickerView>().single()
        runOnIdle {
            pickerView.selectRow(1, animated = false, inComponent = 0)
            pickerView.delegate?.pickerView(pickerView, didSelectRow = 1, inComponent = 0)
            items.value = mapOf("first" to "첫 항목", "second" to "두 번째 항목", "third" to "세 번째", "fourth" to "네 번째")
        }
        waitForIdle()
        runOnIdle {
            assertEquals(sheet, presenter.presentedViewController)
            assertEquals(1L, pickerView.selectedRowInComponent(0))
            val navBar = sheet.view.subviews.filterIsInstance<UINavigationBar>().single()
            val navItem = assertNotNull(navBar.topItem)
            confirm(assertNotNull(navItem.rightBarButtonItem))
        }
        waitForIdle()
        runOnIdle { assertEquals(listOf("second"), confirmedItems) }
    }

    @Test
    fun multiSelectionDropdownKeepsDraftAndExcludesRemovedItems(): TestResult = runComposeUiTest {
        val visible = mutableStateOf(true)
        val items = mutableStateOf(mapOf("first" to "첫 번째", "second" to "두 번째", "third" to "세 번째"))
        val selectedItems = mutableStateOf(listOf("first"))
        val presenter = RecordingPresenter()
        var openDropdown: (() -> Unit)? = null
        val confirmedItems = mutableListOf<List<String>>()

        setContent {
            CompositionLocalProvider(LocalUIViewController provides presenter) {
                IenTheme {
                    if (visible.value) {
                        ExposedDropdownMenuBox(
                            title = "다중 선택",
                            itemsWithLabels = items.value,
                            currentItems = selectedItems.value,
                            onItemsSelected = { confirmedItems += it },
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
        val sheet = assertIs<UINavigationController>(assertNotNull(presenter.presentedViewController))
        val picker = sheet.viewControllers.first() as ListPickerViewController<String>
        val tableView = picker.view.subviews.filterIsInstance<UITableView>().single()
        runOnIdle {
            picker.tableView(
                tableView,
                didSelectRowAtIndexPath = NSIndexPath.indexPathForRow(1, inSection = 0),
            )
            assertEquals(setOf("첫 번째", "두 번째"), checkedLabels(picker))
            items.value = mapOf(
                "first" to "첫 항목",
                "second" to "두 번째 항목",
                "third" to "세 번째",
                "fourth" to "네 번째",
            )
        }
        waitForIdle()
        runOnIdle {
            assertEquals(sheet, presenter.presentedViewController)
            assertEquals(setOf("첫 항목", "두 번째 항목"), checkedLabels(picker))
        }
        runOnIdle { items.value = mapOf("first" to "첫 항목", "third" to "세 번째", "fourth" to "네 번째") }
        waitForIdle()
        runOnIdle {
            assertEquals(sheet, presenter.presentedViewController)
            assertEquals(setOf("첫 항목"), checkedLabels(picker))
            confirm(assertNotNull(picker.navigationItem.rightBarButtonItem))
        }
        waitForIdle()
        runOnIdle { assertEquals(listOf(listOf("first")), confirmedItems) }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun confirm(button: UIBarButtonItem) {
    assertTrue(IENInvokeZeroArgumentBarButtonAction(button))
}

private fun checkedLabels(picker: ListPickerViewController<String>): Set<String> {
    val tableView = picker.view.subviews.filterIsInstance<UITableView>().single()
    val rowCount = picker.tableView(tableView, numberOfRowsInSection = 0).toInt()
    return (0 until rowCount).mapNotNull { row ->
        val cell = picker.tableView(
            tableView,
            cellForRowAtIndexPath = NSIndexPath.indexPathForRow(row.toLong(), inSection = 0),
        )
        cell.textLabel?.text?.takeIf {
            cell.accessoryType == UITableViewCellAccessoryType.UITableViewCellAccessoryCheckmark
        }
    }.toSet()
}
