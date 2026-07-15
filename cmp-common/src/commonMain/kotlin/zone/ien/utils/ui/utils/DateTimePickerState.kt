package zone.ien.utils.ui.utils

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable

/**
 * 주어진 시간을 UTC 시간대에 맞춰 조정하고 밀리초를 반환하는 함수
 *
 * 이 함수는 주어진 타임스탬프를 로컬 시간대 오프셋으로 해석한 후 UTC 시간대로 변환하여
 * 반환합니다.
 *
 * @param time 조정할 타임스탬프 (밀리초 단위)
 * @return UTC 시간대의 조정된 타임스탬프 (밀리초 단위)
 */
internal expect fun adjustDatePickerMillis(time: Long): Long

/**
 * UTC 시간대를 처리하는 Date Picker 상태를 생성하는 함수
 *
 * 이 함수는 DatePickerState를 생성하고, 타임스탬프를 UTC 시간대에 맞춰 조정합니다.
 * 원본 타임스탬프는 UTC 시간대로 변환된 후 Date Picker 구현으로 전달됩니다.
 *
 * @param initialSelectedDateMillis 선택된 날짜의 초기 타임스탬프 (밀리초 단위), 없을 경우 null
 * @param initialDisplayedMonthMillis 표시되는 월의 초기 타임스탬프 (밀리초 단위), 없을 경우 initialSelectedDateMillis 사용
 * @param yearRange 선택 가능한 년도 범위
 * @param initialDisplayMode 날짜 선택기의 초기 표시 모드
 * @param selectableDates 선택 가능한 날짜 제약 조건
 * @return UTC 시간대 처리가 적용된 DatePickerState
 */
@Composable
fun rememberMyDatePickerState(
    @Suppress("AutoBoxing") initialSelectedDateMillis: Long? = null,
    @Suppress("AutoBoxing") initialDisplayedMonthMillis: Long? = initialSelectedDateMillis,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates
): DatePickerState {
    return rememberDatePickerState(
        initialDisplayedMonthMillis = initialDisplayedMonthMillis?.let { adjustDatePickerMillis(it) },
        yearRange = yearRange,
        initialDisplayMode = initialDisplayMode,
        selectableDates = selectableDates,
        initialSelectedDateMillis = initialSelectedDateMillis?.let { adjustDatePickerMillis(it) }
    )
}

/**
 * UTC 시간대를 처리하는 Date Range Picker 상태를 생성하는 함수
 *
 * 이 함수는 DateRangePickerState를 생성하고, 타임스탬프를 UTC 시간대에 맞춰 조정합니다.
 * 원본 타임스탬프는 UTC 시간대로 변환된 후 Date Range Picker 구현으로 전달됩니다.
 *
 * @param initialSelectedStartDateMillis 선택된 시작 날짜의 초기 타임스탬프 (밀리초 단위), 없을 경우 null
 * @param initialSelectedEndDateMillis 선택된 종료 날짜의 초기 타임스탬프 (밀리초 단위), 없을 경우 null
 * @param initialDisplayedMonthMillis 표시되는 월의 초기 타임스탬프 (밀리초 단위), 없을 경우 initialSelectedStartDateMillis 사용
 * @param yearRange 선택 가능한 년도 범위
 * @param initialDisplayMode 날짜 선택기의 초기 표시 모드
 * @param selectableDates 선택 가능한 날짜 제약 조건
 * @return UTC 시간대 처리가 적용된 DateRangePickerState
 */
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
        initialDisplayedMonthMillis = initialDisplayedMonthMillis?.let { adjustDatePickerMillis(it) },
        yearRange = yearRange,
        initialDisplayMode = initialDisplayMode,
        selectableDates = selectableDates,
        initialSelectedStartDateMillis = initialSelectedStartDateMillis?.let { adjustDatePickerMillis(it) },
        initialSelectedEndDateMillis = initialSelectedEndDateMillis?.let { adjustDatePickerMillis(it) },
    )
}
