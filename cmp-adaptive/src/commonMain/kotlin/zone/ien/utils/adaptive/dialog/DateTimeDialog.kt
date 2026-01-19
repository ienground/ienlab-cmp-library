package zone.ien.utils.adaptive.dialog

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * @param selectableDates is not working at iOS. try using [zone.ien.utils.ui.dialog.M3DatePickerDialog]
 */
@Composable
expect fun DatePickerDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialSelectedDateMillis: Long? = null,
    initialDisplayedMonthMillis: Long? = initialSelectedDateMillis,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
)