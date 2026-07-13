package zone.ien.utils.example.ui.screens.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.dialog.IenAlertDialog
import zone.ien.utils.ui.dialog.IenDatePickerDialog
import zone.ien.utils.ui.dialog.IenProgressDialog
import zone.ien.utils.ui.dialog.IenTextFieldDialog
import zone.ien.utils.ui.dialog.IenTimePickerDialog
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.screen.IenBackButton
import zone.ien.utils.ui.screen.IenTopAppBarScaffold
import zone.ien.utils.ui.section.IenSection
import zone.ien.utils.ui.section.IenSectionButton
import zone.ien.utils.ui.section.IenSectionCheckboxItem
import zone.ien.utils.ui.section.IenSectionItem
import zone.ien.utils.ui.section.IenSectionSecureTextField
import zone.ien.utils.ui.section.IenSectionSlider
import zone.ien.utils.ui.section.IenSectionSwitchItem
import zone.ien.utils.ui.section.IenSectionTextField
import zone.ien.utils.ui.select.IenExposedDropdownMenuBox
import zone.ien.utils.ui.utils.TextFieldDialogData
import zone.ien.utils.ui.view.CustomNavigationBar
import zone.ien.utils.ui.view.CustomNavigationBarItem
import zone.ien.utils.ui.view.Empty
import zone.ien.utils.ui.view.IenAsteriskTextWrapper
import zone.ien.utils.ui.view.IenTooltipBox
import zone.ien.utils.ui.menu.ActionMenuItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun IenPlaygroundScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {}
) {
    // Dialog visible states
    var showAlertDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showProgressDialog by remember { mutableStateOf(false) }
    var showTextFieldDialog by remember { mutableStateOf(false) }

    // Section Inputs States
    var switchChecked by remember { mutableStateOf(true) }
    var checkboxChecked by remember { mutableStateOf(false) }
    var sectionTextFieldValue by remember { mutableStateOf("Hello Section Text Field") }
    val secureTextFieldState = rememberTextFieldState("Password123")
    var sliderValue by remember { mutableStateOf(0.5f) }

    // Exposed Dropdown States
    val options = mapOf("apple" to "Apple", "banana" to "Banana", "orange" to "Orange")
    var selectedOption by remember { mutableStateOf("apple") }
    var selectedOptions by remember { mutableStateOf(listOf("apple", "banana")) }

    // Navigation Bar State
    var selectedNavIndex by remember { mutableStateOf(0) }
    var darkTheme by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    IenTheme(darkTheme = darkTheme) {
        IenTopAppBarScaffold(
            modifier = modifier,
            scrollableState = scrollState,
            navigationIcon = {
//                IenTextButton(text = "닫기", onClick = navigateBack)
                IenBackButton {
                    navigateBack()
                }
            },
            title = { IenText("UI Module Playground") },
            subtitle = { IenText("Testing all migrated Material3 wrapper components") },
            actions = listOf<ActionMenuItem>(),
            bottomBar = {
                CustomNavigationBar(
                    selectedIndex = selectedNavIndex,
                    itemCount = 3
                ) {
                    CustomNavigationBarItem(
                        index = 0,
                        onClick = { selectedNavIndex = 0 },
                        icon = {
                            IenIcon(
                                imageVector = M3SystemIcons.Save,
                                contentDescription = null
                            )
                        },
                        label = { IenText("Save") }
                    )
                    CustomNavigationBarItem(
                        index = 1,
                        onClick = { selectedNavIndex = 1 },
                        icon = {
                            IenIcon(
                                imageVector = M3SystemIcons.Edit,
                                contentDescription = null
                            )
                        },
                        label = { IenText("Edit") }
                    )
                    CustomNavigationBarItem(
                        index = 2,
                        onClick = { selectedNavIndex = 2 },
                        icon = {
                            IenIcon(
                                imageVector = M3SystemIcons.Schedule,
                                contentDescription = null
                            )
                        },
                        label = { IenText("Schedule") }
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IenTheme.colors.background)
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // ─── Dialogs Section ───
                IenSection(
                    title = { IenText("Dialogs") }
                ) {
                    IenSectionSwitchItem(
                        checked = darkTheme,
                        onCheckedChange = { darkTheme = it },
                        title = { IenText("IenTheme dark mode") }
                    )
                    IenSectionSwitchItem(
                        checked = darkTheme,
                        onCheckedChange = { darkTheme = it },
                        enabled = false,
                        title = { IenText("IenTheme dark mode") }
                    )
                    IenSectionButton(
                        onClick = { showAlertDialog = true },
                        label = { IenText("Open IenAlertDialog") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    IenSectionButton(
                        onClick = { showDatePicker = true },
                        label = { IenText("Open IenDatePickerDialog") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    IenSectionButton(
                        onClick = { showTimePicker = true },
                        label = { IenText("Open IenTimePickerDialog") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    IenSectionButton(
                        onClick = { showProgressDialog = true },
                        label = { IenText("Open IenProgressDialog") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    IenSectionButton(
                        onClick = { showTextFieldDialog = true },
                        label = { IenText("Open IenTextFieldDialog") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // ─── Exposed Dropdown Section ───
                IenSection(
                    title = { IenText("Exposed Dropdown Menus") }
                ) {
                    IenSectionItem(
                        title = {
                            IenExposedDropdownMenuBox(
                                itemsWithLabels = options,
                                currentItem = selectedOption,
                                onItemSelected = { selectedOption = it },
                                textField = { value, trailingIcon ->
                                    IenSectionTextField(
                                        value = value,
                                        onValueChange = {},
                                        readOnly = true,
                                        placeholder = { IenText("Select Fruit") },
                                        trailingIcon = { trailingIcon() }
                                    )
                                }
                            )
                        }
                    )
                    IenSectionItem(
                        title = {
                            IenExposedDropdownMenuBox(
                                itemsWithLabels = options,
                                currentItems = selectedOptions,
                                onItemsSelected = { selectedOptions = it },
                                textField = { value, trailingIcon ->
                                    IenSectionTextField(
                                        value = value,
                                        onValueChange = {},
                                        readOnly = true,
                                        placeholder = { IenText("Select Fruits (Multi)") },
                                        trailingIcon = { trailingIcon() }
                                    )
                                }
                            )
                        }
                    )
                }

                // ─── Inputs Section ───
                IenSection(
                    title = { IenText("Section Items & Inputs") }
                ) {
                    IenSectionSwitchItem(
                        checked = switchChecked,
                        onCheckedChange = { switchChecked = it },
                        title = { IenText("Switch Item") }
                    )
                    IenSectionCheckboxItem(
                        checked = checkboxChecked,
                        onCheckedChange = { checkboxChecked = it },
                        title = { IenText("Checkbox Item") }
                    )
                    IenSectionCheckboxItem(
                        checked = checkboxChecked,
                        onCheckedChange = { checkboxChecked = it },
                        enabled = false,
                        title = { IenText("Checkbox Item") }
                    )
                    IenSectionTextField(
                        value = sectionTextFieldValue,
                        onValueChange = { sectionTextFieldValue = it },
                        placeholder = { IenText("Placeholder Text") }
                    )
                    IenSectionSecureTextField(
                        state = secureTextFieldState,
                        placeholder = { IenText("Password Input") }
                    )
                    IenSectionSlider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        title = "Slider Item"
                    )
                }

                // ─── Views & Formatting ───
                IenSection(
                    title = { IenText("Views & Formatting") }
                ) {
                    IenSectionItem(
                        title = {
                            IenTooltipBox(
                                label = "This is a custom tooltip text container styling test!"
                            ) {
                                IenText("Hover/Click here for IenTooltipBox")
                            }
                        }
                    )
                    IenSectionItem(
                        title = {
                            IenAsteriskTextWrapper {
                                IenText("Required Input Field Wrapper")
                            }
                        }
                    )
                }

                // ─── Empty State Preview ───
                IenSection(
                    title = { IenText("Empty State Preview") }
                ) {
                    IenSectionItem(
                        title = {
                            Empty(
                                icon = { modifier ->
                                    IenIcon(
                                        imageVector = M3SystemIcons.Edit,
                                        contentDescription = null,
                                        modifier = modifier
                                    )
                                },
                                title = { IenText("No Data Available") },
                                content = { IenText("Try configuring settings or refreshing the screen to load samples.") }
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // ─── Dialog Instances ───
        IenAlertDialog(
            visible = showAlertDialog,
            title = "Alert Dialog Test",
            message = "This is a legacy Material3 dialog styled under the IEN Theme design tokens.",
            onDismiss = { showAlertDialog = false },
            onConfirm = { showAlertDialog = false }
        )

        IenDatePickerDialog(
            visible = showDatePicker,
            title = "Select Date",
            onDismiss = { showDatePicker = false },
            onConfirm = { showDatePicker = false }
        )

        IenTimePickerDialog(
            visible = showTimePicker,
            initialHour = 12,
            initialMinute = 30,
            is24Hour = false,
            title = "Select Time",
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute -> showTimePicker = false }
        )

        IenProgressDialog(
            visible = showProgressDialog,
            isLoadingIndicator = false,
            isWavyIndicator = false
        )

        // Automatically hide progress dialog after 3 seconds for showcase
        if (showProgressDialog) {
            androidx.compose.runtime.LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(3000)
                showProgressDialog = false
            }
        }

        IenTextFieldDialog(
            visible = showTextFieldDialog,
            title = "Profile Edit",
            message = "Enter your display name and status message.",
            textFields = mapOf(
                "name" to TextFieldDialogData(
                    initialValue = "",
                    placeholder = "Display Name"
                ),
                "status" to TextFieldDialogData(
                    initialValue = "",
                    placeholder = "Status Message"
                )
            ),
            onDismiss = { showTextFieldDialog = false },
            onConfirm = { data ->
                showTextFieldDialog = false
            }
        )
    }
}
