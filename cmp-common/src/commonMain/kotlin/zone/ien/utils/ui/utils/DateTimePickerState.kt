package zone.ien.utils.ui.utils

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset
import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KInstantAsLong
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime

internal fun KZonedDateTime.Companion.adjustAndGetMillis(time: Long) = KInstantAsLong(time).atLocalZoneOffset().toKZonedDateTime().copy(zoneOffset = KZoneOffset.UTC).toKInstant().toEpochMilliseconds()

@Composable
fun rememberMyDatePickerState(
    @Suppress("AutoBoxing") initialSelectedDateMillis: Long? = null,
    @Suppress("AutoBoxing") initialDisplayedMonthMillis: Long? = initialSelectedDateMillis,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates
): DatePickerState {
    return rememberDatePickerState(
        initialDisplayedMonthMillis = initialDisplayedMonthMillis?.let { KZonedDateTime.adjustAndGetMillis(it) },
        yearRange = yearRange,
        initialDisplayMode = initialDisplayMode,
        selectableDates = selectableDates,
        initialSelectedDateMillis = initialSelectedDateMillis?.let { KZonedDateTime.adjustAndGetMillis(it) }
    )
}

@Composable
fun rememberMyDateRangePickerState(
    @Suppress("AutoBoxing") initialSelectedStartDateMillis: Long? = null,
    @Suppress("AutoBoxing") initialSelectedEndDateMillis: Long? = null,
    @Suppress("AutoBoxing") initialDisplayedMonthMillis: Long? = initialSelectedStartDateMillis,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates
): DateRangePickerState {
    return rememberDateRangePickerState(
        initialDisplayedMonthMillis = initialDisplayedMonthMillis?.let { KZonedDateTime.adjustAndGetMillis(it) },
        yearRange = yearRange,
        initialDisplayMode = initialDisplayMode,
        selectableDates = selectableDates,
        initialSelectedStartDateMillis = initialSelectedStartDateMillis?.let { KZonedDateTime.adjustAndGetMillis(it) },
        initialSelectedEndDateMillis = initialSelectedEndDateMillis?.let { KZonedDateTime.adjustAndGetMillis(it) },
    )
}