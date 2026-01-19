package zone.ien.utils.adaptive.dialog

import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.M3DatePickerDialog

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