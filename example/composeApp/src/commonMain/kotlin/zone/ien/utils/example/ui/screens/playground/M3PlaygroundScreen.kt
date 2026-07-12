package zone.ien.utils.example.ui.screens.playground

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.dialog.M3AlertDialog
import zone.ien.utils.ui.dialog.M3DatePickerDialog
import zone.ien.utils.ui.dialog.M3ProgressDialog
import zone.ien.utils.ui.dialog.M3TextFieldDialog
import zone.ien.utils.ui.dialog.M3TimePickerDialog
import zone.ien.utils.ui.screen.M3BackButton
import zone.ien.utils.ui.screen.M3TopAppBarScaffold
import zone.ien.utils.ui.section.M3Section
import zone.ien.utils.ui.section.M3SectionButton
import zone.ien.utils.ui.section.M3SectionCheckboxItem
import zone.ien.utils.ui.section.M3SectionItem
import zone.ien.utils.ui.section.M3SectionSecureTextField
import zone.ien.utils.ui.section.M3SectionSlider
import zone.ien.utils.ui.section.M3SectionSwitchItem
import zone.ien.utils.ui.section.M3SectionTextField
import zone.ien.utils.ui.select.M3ExposedDropdownMenuBox
import zone.ien.utils.ui.utils.TextFieldDialogData
import zone.ien.utils.ui.view.CustomNavigationBar
import zone.ien.utils.ui.view.CustomNavigationBarItem
import zone.ien.utils.ui.view.Empty
import zone.ien.utils.ui.view.M3AsteriskTextWrapper
import zone.ien.utils.ui.view.M3TooltipBox
import zone.ien.utils.ui.menu.ActionMenuItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun M3PlaygroundScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
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

    M3TopAppBarScaffold(
        modifier = modifier,
        navigationIcon = {
            M3BackButton {
                navigateBack()
            }
        },
        title = { Text("UI Module Playground") },
        subtitle = { Text("Testing all migrated Material3 wrapper components") },
        actions = listOf<ActionMenuItem>(),
        bottomBar = {
            CustomNavigationBar(
                selectedIndex = selectedNavIndex,
                itemCount = 3
            ) {
                CustomNavigationBarItem(
                    index = 0,
                    onClick = { selectedNavIndex = 0 },
                    icon = { Icon(imageVector = M3SystemIcons.Save, contentDescription = null) },
                    label = { Text("Save") }
                )
                CustomNavigationBarItem(
                    index = 1,
                    onClick = { selectedNavIndex = 1 },
                    icon = { Icon(imageVector = M3SystemIcons.Edit, contentDescription = null) },
                    label = { Text("Edit") }
                )
                CustomNavigationBarItem(
                    index = 2,
                    onClick = { selectedNavIndex = 2 },
                    icon = { Icon(imageVector = M3SystemIcons.Schedule, contentDescription = null) },
                    label = { Text("Schedule") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier

                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ─── Dialogs Section ───
            M3Section(
                title = { Text("Dialogs") }
            ) {
                M3SectionButton(
                    onClick = { showAlertDialog = true },
                    label = { Text("Open M3AlertDialog") },
                    modifier = Modifier.fillMaxWidth()
                )
                M3SectionButton(
                    onClick = { showDatePicker = true },
                    label = { Text("Open M3DatePickerDialog") },
                    modifier = Modifier.fillMaxWidth()
                )
                M3SectionButton(
                    onClick = { showTimePicker = true },
                    label = { Text("Open M3TimePickerDialog") },
                    modifier = Modifier.fillMaxWidth()
                )
                M3SectionButton(
                    onClick = { showProgressDialog = true },
                    label = { Text("Open M3ProgressDialog") },
                    modifier = Modifier.fillMaxWidth()
                )
                M3SectionButton(
                    onClick = { showTextFieldDialog = true },
                    label = { Text("Open M3TextFieldDialog") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ─── Exposed Dropdown Section ───
            M3Section(
                title = { Text("Exposed Dropdown Menus") }
            ) {
                M3SectionItem(
                    title = {
                        M3ExposedDropdownMenuBox(
                            itemsWithLabels = options,
                            currentItem = selectedOption,
                            onItemSelected = { selectedOption = it },
                            textField = { value, trailingIcon ->
                                M3SectionTextField(
                                    value = value,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text("Select Fruit") },
                                    trailingIcon = { trailingIcon() }
                                )
                            }
                        )
                    }
                )
                M3SectionItem(
                    title = {
                        M3ExposedDropdownMenuBox(
                            itemsWithLabels = options,
                            currentItems = selectedOptions,
                            onItemsSelected = { selectedOptions = it },
                            textField = { value, trailingIcon ->
                                M3SectionTextField(
                                    value = value,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text("Select Fruits (Multi)") },
                                    trailingIcon = { trailingIcon() }
                                )
                            }
                        )
                    }
                )
            }

            // ─── Inputs Section ───
            M3Section(
                title = { Text("Section Items & Inputs") }
            ) {
                M3SectionSwitchItem(
                    checked = switchChecked,
                    onCheckedChange = { switchChecked = it },
                    title = { Text("Switch Item") }
                )
                M3SectionCheckboxItem(
                    checked = checkboxChecked,
                    onCheckedChange = { checkboxChecked = it },
                    title = { Text("Checkbox Item") }
                )
                M3SectionTextField(
                    value = sectionTextFieldValue,
                    onValueChange = { sectionTextFieldValue = it },
                    placeholder = { Text("Placeholder Text") }
                )
                M3SectionSecureTextField(
                    state = secureTextFieldState,
                    placeholder = { Text("Password Input") }
                )
                M3SectionSlider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    title = "Slider Item"
                )
            }

            // ─── Views & Formatting ───
            M3Section(
                title = { Text("Views & Formatting") }
            ) {
                M3SectionItem(
                    title = {
                        M3TooltipBox(
                            label = "This is a custom tooltip text container styling test!"
                        ) {
                            Text("Hover/Click here for M3TooltipBox")
                        }
                    }
                )
                M3SectionItem(
                    title = {
                        M3AsteriskTextWrapper {
                            Text("Required Input Field Wrapper")
                        }
                    }
                )
            }

            // ─── Empty State Preview ───
            M3Section(
                title = { Text("Empty State Preview") }
            ) {
                M3SectionItem(
                    title = {
                        Empty(
                            icon = { modifier ->
                                Icon(
                                    imageVector = M3SystemIcons.Edit,
                                    contentDescription = null,
                                    modifier = modifier
                                )
                            },
                            title = { Text("No Data Available") },
                            content = { Text("Try configuring settings or refreshing the screen to load samples.") }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    // ─── Dialog Instances ───
    M3AlertDialog(
        visible = showAlertDialog,
        title = "Alert Dialog Test",
        message = "This is a legacy Material3 dialog styled under the IEN Theme design tokens.",
        onDismiss = { showAlertDialog = false },
        onConfirm = { showAlertDialog = false }
    )

    M3DatePickerDialog(
        visible = showDatePicker,
        title = "Select Date",
        onDismiss = { showDatePicker = false },
        onConfirm = { showDatePicker = false }
    )

    M3TimePickerDialog(
        visible = showTimePicker,
        initialHour = 12,
        initialMinute = 30,
        is24Hour = false,
        title = "Select Time",
        onDismiss = { showTimePicker = false },
        onConfirm = { hour, minute -> showTimePicker = false }
    )

    M3ProgressDialog(
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

    M3TextFieldDialog(
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
