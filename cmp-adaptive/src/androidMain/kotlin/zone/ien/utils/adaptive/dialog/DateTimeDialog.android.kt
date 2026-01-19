package zone.ien.utils.adaptive.dialog

import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.M3DatePickerDialog
import zone.ien.utils.ui.dialog.M3TimePickerDialog

@Composable
actual fun DatePickerDialog(
    modifier: Modifier,
    visible: Boolean,
    initialSelectedDateMillis: Long?,
    initialDisplayedMonthMillis: Long?,
    yearRange: IntRange,
    initialDisplayMode: DisplayMode,
    selectableDates: SelectableDates,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    M3DatePickerDialog(
        modifier = modifier,
        visible = visible,
        initialSelectedDateMillis = initialSelectedDateMillis,
        initialDisplayedMonthMillis = initialDisplayedMonthMillis,
        yearRange = yearRange,
        initialDisplayMode = initialDisplayMode,
        selectableDates = selectableDates,
        title = title,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}

@Composable
actual fun TimePickerDialog(
    modifier: Modifier,
    visible: Boolean,
    initialHour: Int,
    initialMinute: Int,
    is24Hour: Boolean,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    M3TimePickerDialog(
        modifier = modifier,
        visible = visible,
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = is24Hour,
        title = title,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}