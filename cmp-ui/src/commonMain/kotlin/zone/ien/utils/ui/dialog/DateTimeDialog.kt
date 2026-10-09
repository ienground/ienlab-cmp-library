package zone.ien.utils.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.ok
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenDateWheelPicker
import zone.ien.utils.ui.interactive.IenTimeWheelPicker
import zone.ien.utils.ui.interactive.datePickerUtcMillisToDate
import zone.ien.utils.ui.interactive.toDatePickerUtcMillis
import zone.ien.utils.ui.utils.rememberMyDatePickerState

/**
 * IenDatePickerDialog은 날짜 선택 다이얼로그를 제공하는 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param initialSelectedDateMillis 초기 선택된 날짜 (밀리초 단위)
 * @param initialDisplayedMonthMillis 초기에 표시되는 달 (밀리초 단위)
 * @param yearRange 선택 가능한 연도 범위
 * @param initialDisplayMode 기존 호출과의 호환을 위해 유지되며 휠 표시에는 영향을 주지 않습니다.
 * @param selectableDates 선택 가능한 날짜 범위
 * @param title 다이얼로그의 제목
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 * @param onConfirm 날짜 선택을 완료했을 때 호출되는 콜백 함수
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenDatePickerDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialSelectedDateMillis: Long? = null,
    initialDisplayedMonthMillis: Long? = initialSelectedDateMillis,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    @Suppress("UNUSED_PARAMETER") initialDisplayMode: DisplayMode = DisplayMode.Picker,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
) {
    if (visible) {
        val datePickerState = rememberMyDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis,
            initialDisplayedMonthMillis = initialDisplayedMonthMillis,
            yearRange = yearRange,
            selectableDates = selectableDates
        )
        val initialDate = datePickerUtcMillisToDate(datePickerState.displayedMonthMillis)
        val initialSelectedDate = datePickerState.selectedDateMillis?.let(::datePickerUtcMillisToDate)
        var selectedDate by remember(datePickerState) {
            mutableStateOf(initialSelectedDate ?: initialDate)
        }
        var hasSelection by remember(datePickerState) {
            mutableStateOf(initialSelectedDate != null)
        }
        val selectedDateMillis = selectedDate.toDatePickerUtcMillis()
        val canConfirm = hasSelection &&
            selectableDates.isSelectableYear(selectedDate.year) &&
            selectableDates.isSelectableDate(selectedDateMillis)

        IenDialogFrame(
            visible = visible,
            onDismiss = onDismiss,
            modifier = modifier,
            maxWidth = 360.dp,
            fixedWidth = 360.dp,
            contentPadding = PaddingValues(0.dp),
            usePlatformDefaultWidth = false,
            horizontalMargin = 16.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 568.dp),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            ) {
                IenAlertDialogTitle(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp),
                )
                IenDateWheelPicker(
                    value = selectedDate,
                    onValueChange = {
                        selectedDate = it
                        hasSelection = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    yearRange = yearRange,
                    selectableDates = selectableDates,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 8.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) {
                    IenConfirmDialogCancelButton(
                        text = stringResource(Res.string.cancel),
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                    )
                    IenButton(
                        onClick = { onConfirm(selectedDateMillis) },
                        modifier = Modifier.weight(1f),
                        size = IenButtonSize.Large,
                        variant = IenButtonVariant.Fill,
                        tone = IenSemanticTone.Brand,
                        state = IenButtonState(enabled = canConfirm),
                        display = IenButtonDisplay.Block,
                    ) {
                        Text(stringResource(Res.string.ok))
                    }
                }
            }
        }
    }
}

/**
 * IenTimePickerDialog은 시간 선택 다이얼로그를 제공하는 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param initialHour 초기 선택된 시간 (24시간 제)
 * @param initialMinute 초기 선택된 분
 * @param is24Hour 24시간 제 여부
 * @param title 다이얼로그의 제목
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 * @param onConfirm 시간 선택을 완료했을 때 호출되는 콜백 함수
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenTimePickerDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialHour: Int,
    initialMinute: Int,
    is24Hour: Boolean = false,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    if (visible) {
        var selectedTime by remember(visible, initialHour, initialMinute, is24Hour) {
            mutableStateOf(
                KDuration.of(
                    initialHour * 3600L + initialMinute * 60L,
                    KFixedTimeUnit.Second,
                ),
            )
        }

        IenDialogFrame(
            visible = visible,
            onDismiss = onDismiss,
            modifier = modifier,
            maxWidth = 360.dp,
            fixedWidth = 360.dp,
            usePlatformDefaultWidth = false,
            horizontalMargin = 16.dp,
        ) {
            IenAlertDialogTitle(text = title)
            IenTimeWheelPicker(
                value = selectedTime,
                onValueChange = { selectedTime = it },
                use24HourFormat = is24Hour,
                showSeconds = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IenConfirmDialogCancelButton(
                    text = stringResource(Res.string.cancel),
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                )
                IenButton(
                    onClick = { onConfirm(selectedTime.hourPart(), selectedTime.minutePart()) },
                    modifier = Modifier.weight(1f),
                    size = IenButtonSize.Large,
                    variant = IenButtonVariant.Fill,
                    tone = IenSemanticTone.Brand,
                    display = IenButtonDisplay.Block,
                ) {
                    Text(stringResource(Res.string.ok))
                }
            }
        }
    }
}

/**
 * IenDatePicker는 IEN 색상 체계를 적용한 날짜 선택 컴포넌트입니다.
 *
 * @param state 날짜 선택 상태
 * @param modifier 적용할 Modifier
 * @param title 상단 제목
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenDatePicker(
    state: DatePickerState,
    modifier: Modifier = Modifier,
    title: String,
) {
    DatePicker(
        state = state,
        colors = DatePickerDefaults.colors(
            containerColor = IenTheme.colors.surfaceRaised,
            titleContentColor = IenTheme.colors.textPrimary,
            headlineContentColor = IenTheme.colors.textPrimary,
            weekdayContentColor = IenTheme.colors.textSecondary,
            subheadContentColor = IenTheme.colors.textSecondary,
            navigationContentColor = IenTheme.colors.textPrimary,
            currentYearContentColor = IenTheme.colors.brand,
            selectedYearContentColor = IenTheme.colors.onBrand,
            selectedYearContainerColor = IenTheme.colors.brand,
            selectedDayContentColor = IenTheme.colors.onBrand,
            selectedDayContainerColor = IenTheme.colors.brand,
            todayContentColor = IenTheme.colors.brand,
            todayDateBorderColor = IenTheme.colors.brand,
            dividerColor = IenTheme.colors.border,
        ),
        title = {
            IenAlertDialogTitle(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp)
            )
        },
        modifier = modifier,
    )
}

/**
 * IenTimePicker는 IEN 색상 체계를 적용한 시간 선택 컴포넌트입니다.
 *
 * @param state 시간 선택 상태
 * @param modifier 적용할 Modifier
 * @param isDial 다이얼 방식 사용 여부
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenTimePicker(
    state: TimePickerState,
    modifier: Modifier = Modifier,
    isDial: Boolean = true,
) {
    val timePickerColors = TimePickerDefaults.colors(
        selectorColor = IenTheme.colors.brand,
        clockDialSelectedContentColor = IenTheme.colors.onBrand,
        clockDialUnselectedContentColor = IenTheme.colors.textPrimary,
        timeSelectorSelectedContainerColor = IenTheme.colors.brandWeak,
        timeSelectorSelectedContentColor = IenTheme.colors.brand,
        timeSelectorUnselectedContainerColor = IenTheme.colors.surfaceWeak,
        timeSelectorUnselectedContentColor = IenTheme.colors.textPrimary,
        periodSelectorSelectedContainerColor = IenTheme.colors.brandWeak,
        periodSelectorSelectedContentColor = IenTheme.colors.brand,
        periodSelectorUnselectedContainerColor = IenTheme.colors.surfaceRaised,
        periodSelectorUnselectedContentColor = IenTheme.colors.textPrimary,
    )

    if (isDial) {
        TimePicker(
            state = state,
            modifier = modifier,
            colors = timePickerColors,
        )
    } else {
        TimeInput(
            state = state,
            modifier = modifier,
            colors = timePickerColors,
        )
    }
}
