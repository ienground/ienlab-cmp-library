package zone.ien.utils.example.ui.screens.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.dialog.IenAlertDialog
import zone.ien.utils.ui.dialog.IenDatePickerDialog
import zone.ien.utils.ui.dialog.IenProgressDialog
import zone.ien.utils.ui.dialog.IenTextFieldDialog
import zone.ien.utils.ui.dialog.IenTimePickerDialog
import zone.ien.utils.ui.feedback.IenProgressBar
import zone.ien.utils.ui.interactive.IenSegmentedControl
import zone.ien.utils.ui.interactive.IenSegmentedControlAlignment
import zone.ien.utils.ui.interactive.IenSegmentedControlItem
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.screen.IenBackButton
import zone.ien.utils.ui.screen.IenBottomBar
import zone.ien.utils.ui.screen.IenBottomBarDefaults
import zone.ien.utils.ui.screen.IenChatBottomBar
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import zone.ien.utils.ui.screen.IenTopAppBarScaffold
import zone.ien.utils.ui.section.IenSection
import zone.ien.utils.ui.section.button
import zone.ien.utils.ui.section.checkbox
import zone.ien.utils.ui.section.dangerAction
import zone.ien.utils.ui.section.dropdown
import zone.ien.utils.ui.section.item
import zone.ien.utils.ui.section.link
import zone.ien.utils.ui.section.radio
import zone.ien.utils.ui.section.rangeSlider
import zone.ien.utils.ui.section.secureTextField
import zone.ien.utils.ui.section.slider
import zone.ien.utils.ui.section.switch
import zone.ien.utils.ui.section.textField
import zone.ien.utils.ui.select.IenExposedDropdownMenuBox
import zone.ien.utils.ui.utils.TextFieldDialogData
import zone.ien.utils.ui.view.IenNavigationBar
import zone.ien.utils.ui.view.IenNavigationBarItem
import zone.ien.utils.ui.view.IenEmpty
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
    var sectionSelectedFruit by remember { mutableStateOf<String?>(null) }
    var sectionErrorValue by remember { mutableStateOf("invalid-email") }
    var selectedSectionOption by remember { mutableStateOf("standard") }
    var sectionRangeValue by remember { mutableStateOf(20f..80f) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedTimeHour by remember { mutableStateOf(12) }
    var selectedTimeMinute by remember { mutableStateOf(30) }
    var segmentedPeriod by remember { mutableStateOf("week") }
    val secureTextFieldState = rememberTextFieldState("Password123")
    var sliderValue by remember { mutableStateOf(0.5f) }

    val sectionEmailHasError = sectionErrorValue.isNotBlank() && '@' !in sectionErrorValue
    val selectedDateLabel = selectedDateMillis?.let { millis ->
        Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.UTC)
            .date
            .let { date ->
                "${date.year}-${date.monthNumber.toString().padStart(2, '0')}-${date.day.toString().padStart(2, '0')}"
            }
    } ?: "날짜를 선택하세요"
    val selectedTimeLabel = "${selectedTimeHour.toString().padStart(2, '0')}:${selectedTimeMinute.toString().padStart(2, '0')}"

    // Exposed Dropdown States
    val options = mapOf("apple" to "Apple", "banana" to "Banana", "orange" to "Orange")
    var selectedOption by remember { mutableStateOf("apple") }
    var selectedOptions by remember { mutableStateOf(listOf("apple", "banana")) }

    // Bottom Bar States
    var selectedNavIndex by remember { mutableStateOf(0) }
    var navigationBarVisible by remember { mutableStateOf(true) }
    var bottomBarVisible by remember { mutableStateOf(true) }
    var chatBottomBarVisible by remember { mutableStateOf(true) }
    var chatMessage by remember { mutableStateOf("") }
    var darkTheme by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    IenTheme(darkTheme = darkTheme) {
        val bottomBarWindowInsets = if (!navigationBarVisible && bottomBarVisible) {
            IenBottomBarDefaults.windowInsets
        } else {
            WindowInsets(0.dp)
        }
        val chatBottomBarWindowInsets = if (!navigationBarVisible && !bottomBarVisible && chatBottomBarVisible) {
            IenBottomBarDefaults.windowInsets
        } else {
            WindowInsets(0.dp)
        }

        IenTopAppBarScaffold(
            modifier = modifier,
            contentEdge = IenScaffoldContentEdge(
                scrollableState = scrollState,
            ),
            navigationIcon = {
                IenBackButton {
                    navigateBack()
                }
            },
            title = { Text("UI Module Playground") },
            subtitle = { Text("Testing all migrated Material3 wrapper components") },
            actions = listOf<ActionMenuItem>(),
            bottomBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    IenChatBottomBar(
                        value = chatMessage,
                        onValueChange = { chatMessage = it },
                        onSend = { chatMessage = "" },
                        visible = chatBottomBarVisible,
                        windowInsets = chatBottomBarWindowInsets,
                    )
                    IenBottomBar(
                        actions = { Text("IenBottomBar") },
                        visible = bottomBarVisible,
                        windowInsets = bottomBarWindowInsets,
                    )
                    IenNavigationBar(
                        selectedIndex = selectedNavIndex,
                        itemCount = 3,
                        visible = navigationBarVisible,
                    ) {
                        IenNavigationBarItem(
                            index = 0,
                            onClick = { selectedNavIndex = 0 },
                            icon = {
                                IenIcon(
                                    imageVector = SystemIcons.Save,
                                    contentDescription = null
                                )
                            },
                            label = { Text("저장") }
                        )
                        IenNavigationBarItem(
                            index = 1,
                            onClick = { selectedNavIndex = 1 },
                            icon = {
                                IenIcon(
                                    imageVector = SystemIcons.Edit,
                                    contentDescription = null
                                )
                            },
                            label = { Text("수정") }
                        )
                        IenNavigationBarItem(
                            index = 2,
                            onClick = { selectedNavIndex = 2 },
                            icon = {
                                IenIcon(
                                    imageVector = SystemIcons.Schedule,
                                    contentDescription = null
                                )
                            },
                            label = { Text("스케쥴") }
                        )
                    }
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
                    title = { Text("Dialogs") }
                ) {
                    switch(
                        checked = darkTheme,
                        onCheckedChange = { darkTheme = it },
                        title = { Text("IenTheme dark mode") }
                    )
                    switch(
                        checked = darkTheme,
                        onCheckedChange = { darkTheme = it },
                        enabled = false,
                        title = { Text("IenTheme dark mode") }
                    )
                    button(
                        onClick = { showAlertDialog = true },
                        label = { Text("Open IenAlertDialog") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    button(
                        onClick = { showDatePicker = true },
                        label = { Text("Open IenDatePickerDialog") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    button(
                        onClick = { showTimePicker = true },
                        label = { Text("Open IenTimePickerDialog") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    button(
                        onClick = { showProgressDialog = true },
                        label = { Text("Open IenProgressDialog") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    button(
                        onClick = { showTextFieldDialog = true },
                        label = { Text("Open IenTextFieldDialog") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // ─── Exposed Dropdown Section ───
                IenSection(
                    title = { Text("Exposed Dropdown Menus") }
                ) {
                    item(
                        title = {
                            IenExposedDropdownMenuBox(
                                itemsWithLabels = options,
                                currentItem = selectedOption,
                                onItemSelected = { selectedOption = it },
                                textField = { value, dropdownTrailingIcon ->
                                    textField(
                                        value = value,
                                        onValueChange = {},
                                        readOnly = true,
                                        placeholder = { Text("Select Fruit") },
                                        trailingContent = { dropdownTrailingIcon() }
                                    )
                                }
                            )
                        }
                    )
                    item(
                        title = {
                            IenExposedDropdownMenuBox(
                                itemsWithLabels = options,
                                currentItems = selectedOptions,
                                onItemsSelected = { selectedOptions = it },
                                textField = { value, dropdownTrailingIcon ->
                                    textField(
                                        value = value,
                                        onValueChange = {},
                                        readOnly = true,
                                        placeholder = { Text("Select Fruits (Multi)") },
                                        trailingContent = { dropdownTrailingIcon() }
                                    )
                                }
                            )
                        }
                    )
                }

                // ─── Inputs Section ───
                IenSection(
                    title = { Text("Section Items & Inputs") }
                ) {
                    item(
                        supportingContent = { Text("Supporting content") },
                        title = { Text("Basic Item") }
                    )
                    link(
                        onClick = {},
                        caption = { Text("Clickable section row") },
                        title = { Text("Link Item") }
                    )
                    switch(
                        checked = bottomBarVisible,
                        onCheckedChange = { bottomBarVisible = it },
                        title = { Text("Bottom Bar Visible") }
                    )
                    switch(
                        checked = chatBottomBarVisible,
                        onCheckedChange = { chatBottomBarVisible = it },
                        title = { Text("Chat Bottom Bar Visible") }
                    )
                    switch(
                        checked = navigationBarVisible,
                        onCheckedChange = { navigationBarVisible = it },
                        title = { Text("Bottom Navigation Visible") }
                    )
                    switch(
                        checked = switchChecked,
                        onCheckedChange = { switchChecked = it },
                        title = { Text("Switch Item") }
                    )
                    checkbox(
                        checked = checkboxChecked,
                        onCheckedChange = { checkboxChecked = it },
                        title = { Text("Checkbox Item") }
                    )
                    checkbox(
                        checked = checkboxChecked,
                        onCheckedChange = { checkboxChecked = it },
                        enabled = false,
                        title = { Text("Checkbox Item") }
                    )
                    radio(
                        selected = selectedSectionOption == "standard",
                        onClick = { selectedSectionOption = "standard" },
                        supportingContent = { Text("기본 기능을 사용합니다") },
                        title = { Text("기본 요금제") }
                    )
                    radio(
                        selected = selectedSectionOption == "premium",
                        onClick = { selectedSectionOption = "premium" },
                        supportingContent = { Text("추가 기능을 사용할 수 있습니다") },
                        title = { Text("프리미엄 요금제") }
                    )
                    dropdown(
                        itemsWithLabels = options,
                        currentItem = sectionSelectedFruit,
                        onItemSelected = { sectionSelectedFruit = it },
                        defaultText = "Select a fruit",
                        title = { Text("Dropdown Item") }
                    )
                    textField(
                        value = sectionTextFieldValue,
                        onValueChange = { sectionTextFieldValue = it },
                        placeholder = { Text("Placeholder Text") }
                    )
                    textField(
                        value = "비활성 입력 값",
                        onValueChange = {},
                        enabled = false,
                        placeholder = { Text("Disabled Input") }
                    )
                    textField(
                        value = sectionErrorValue,
                        onValueChange = { sectionErrorValue = it },
                        isError = sectionEmailHasError,
                        singleLine = true,
                        placeholder = { Text("이메일 주소") }
                    )
                    if (sectionEmailHasError) {
                        item(
                            supportingContent = {
                                Text("이메일 주소에 @를 포함해 주세요", color = IenTheme.colors.danger)
                            },
                            title = { Text("오류 상태 안내") }
                        )
                    }
                    secureTextField(
                        state = secureTextFieldState,
                        placeholder = { Text("Password Input") }
                    )
                    item(
                        supportingContent = {
                            IenSegmentedControl(
                                items = listOf(
                                    IenSegmentedControlItem("week", "주간"),
                                    IenSegmentedControlItem("month", "월간"),
                                    IenSegmentedControlItem("year", "연간"),
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                value = segmentedPeriod,
                                onChange = { segmentedPeriod = it },
                                alignment = IenSegmentedControlAlignment.Fixed,
                            )
                        },
                        title = { Text("조회 기간") }
                    )
                    link(
                        onClick = { showDatePicker = true },
                        caption = { Text(selectedDateLabel) },
                        title = { Text("날짜 선택") }
                    )
                    link(
                        onClick = { showTimePicker = true },
                        caption = { Text(selectedTimeLabel) },
                        title = { Text("시간 선택") }
                    )
                    item(
                        supportingContent = {
                            IenProgressBar(
                                progress = 0.72f,
                                modifier = Modifier.fillMaxWidth(),
                                contentDescription = "파일 업로드 진행률",
                            )
                        },
                        title = { Text("파일 업로드 · 72%") }
                    )
                    button(
                        onClick = {},
                        label = { Text("Section Button") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        title = "Slider Item"
                    )
                    rangeSlider(
                        value = sectionRangeValue,
                        onValueChange = { sectionRangeValue = it },
                        valueRange = 0f..100f,
                        steps = 9,
                        title = "가격 범위 · ${sectionRangeValue.start.toInt()}–${sectionRangeValue.endInclusive.toInt()}만원"
                    )
                    dangerAction(
                        onClick = { sectionTextFieldValue = "" },
                        caption = { Text("입력한 텍스트를 비웁니다") },
                        title = { Text("입력 내용 초기화") }
                    )
                }

                // ─── Views & Formatting ───
                IenSection(
                    title = { Text("Views & Formatting") }
                ) {
                    item(
                        title = {
                            IenTooltipBox(
                                label = "This is a custom tooltip text container styling test!"
                            ) {
                                Text("Hover/Click here for IenTooltipBox")
                            }
                        }
                    )
                    item(
                        title = {
                            IenAsteriskTextWrapper {
                                Text("Required Input Field Wrapper")
                            }
                        }
                    )
                }

                // ─── Empty State Preview ───
                IenSection(
                    title = { Text("Empty State Preview") }
                ) {
                    item(
                        title = {
                            IenEmpty(
                                icon = {
                                    content { modifier ->
                                        IenIcon(
                                            imageVector = SystemIcons.Edit,
                                            contentDescription = null,
                                            modifier = modifier,
                                        )
                                    }
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
        IenAlertDialog(
            visible = showAlertDialog,
            title = "Alert Dialog Test",
            message = "This is a legacy Material3 dialog styled under the IEN Theme design tokens.",
            onDismiss = { showAlertDialog = false },
            onConfirm = { showAlertDialog = false }
        )

        IenDatePickerDialog(
            visible = showDatePicker,
            initialSelectedDateMillis = selectedDateMillis,
            title = "Select Date",
            onDismiss = { showDatePicker = false },
            onConfirm = {
                selectedDateMillis = it
                showDatePicker = false
            }
        )

        IenTimePickerDialog(
            visible = showTimePicker,
            initialHour = selectedTimeHour,
            initialMinute = selectedTimeMinute,
            is24Hour = false,
            title = "Select Time",
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                selectedTimeHour = hour
                selectedTimeMinute = minute
                showTimePicker = false
            }
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
