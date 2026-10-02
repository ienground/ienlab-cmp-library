package zone.ien.utils.ui.interactive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.date_picker_day
import zone.ien.utils.cmp_ui.generated.resources.date_picker_month
import zone.ien.utils.cmp_ui.generated.resources.date_picker_year
import zone.ien.utils.cmp_ui.generated.resources.duration_picker_hour
import zone.ien.utils.cmp_ui.generated.resources.duration_picker_minute
import zone.ien.utils.cmp_ui.generated.resources.duration_picker_second
import zone.ien.utils.cmp_ui.generated.resources.time_picker_am
import zone.ien.utils.cmp_ui.generated.resources.time_picker_hour
import zone.ien.utils.cmp_ui.generated.resources.time_picker_minute
import zone.ien.utils.cmp_ui.generated.resources.time_picker_pm
import zone.ien.utils.cmp_ui.generated.resources.time_picker_second
import zone.ien.utils.ui.foundation.IenTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * 연·월·일 휠 선택기. 월 또는 연도를 변경하면 일자를 해당 월의 마지막 날 이내로 보정합니다.
 * @param value 현재 날짜.
 * @param onValueChange 사용자가 선택한 유효한 날짜를 전달합니다.
 * @param modifier 루트 레이아웃에 적용할 Modifier.
 * @param yearRange 선택 가능한 연도 범위 (1..9999).
 * @param enabled 선택 가능 여부.
 */
@Composable
fun IenDateWheelPicker(
    value: LocalDate,
    onValueChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    yearRange: IntRange = 1900..2100,
    enabled: Boolean = true,
) {
    require(!yearRange.isEmpty() && yearRange.first >= 1 && yearRange.last <= 9999) { "연도 범위는 1..9999 안이어야 합니다." }
    require(value.year in yearRange) { "날짜가 선택 가능한 연도 범위를 벗어났습니다." }
    val years = remember(yearRange) { yearRange.toList() }
    val months = remember { (1..12).toList() }
    val days = remember(value.year, value.month) { (1..YearMonth(value.year, value.month).numberOfDays).toList() }
    val yearLabel = stringResource(Res.string.date_picker_year, WHEEL_VALUE_PLACEHOLDER)
    val monthLabel = stringResource(Res.string.date_picker_month, WHEEL_VALUE_PLACEHOLDER)
    val dayLabel = stringResource(Res.string.date_picker_day, WHEEL_VALUE_PLACEHOLDER)
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs)) {
        IenWheelPicker(
            items = years,
            selectedIndex = value.year - yearRange.first,
            onSelectedIndexChange = { onValueChange(wheelPickerDate(years[it], value.month.number, value.day)) },
            modifier = Modifier.weight(1.4f),
            enabled = enabled,
            itemLabel = { yearLabel.replace(WHEEL_VALUE_PLACEHOLDER, it.toString()) },
        )
        IenWheelPicker(
            items = months,
            selectedIndex = value.month.number - 1,
            onSelectedIndexChange = { onValueChange(wheelPickerDate(value.year, months[it], value.day)) },
            modifier = Modifier.weight(1f),
            enabled = enabled,
            itemLabel = { monthLabel.replace(WHEEL_VALUE_PLACEHOLDER, it.toString()) },
        )
        IenWheelPicker(
            items = days,
            selectedIndex = value.day - 1,
            onSelectedIndexChange = { onValueChange(LocalDate(value.year, value.month, days[it])) },
            modifier = Modifier.weight(1f),
            enabled = enabled,
            itemLabel = { dayLabel.replace(WHEEL_VALUE_PLACEHOLDER, it.toString()) },
        )
    }
}

/**
 * 하루 안의 정확한 시·분·초를 선택하는 휠. 결과는 항상 24시간제 [LocalTime]입니다.
 * @param value 현재 시각. 나노초는 0이어야 합니다.
 * @param onValueChange 선택한 시각을 전달합니다.
 * @param modifier 루트 레이아웃에 적용할 Modifier.
 * @param use24HourFormat true이면 00..23시, false이면 오전·오후와 1..12시를 표시합니다.
 * @param enabled 선택 가능 여부.
 */
@Composable
fun IenTimeWheelPicker(
    value: LocalTime,
    onValueChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    use24HourFormat: Boolean = true,
    enabled: Boolean = true,
) {
    require(value.nanosecond == 0) { "시각은 정수 초여야 합니다." }
    val hours = remember(use24HourFormat) { if (use24HourFormat) (0..23).toList() else (1..12).toList() }
    val sexagesimal = remember { (0..59).toList() }
    val displayedHour = if (use24HourFormat) value.hour else (value.hour % 12).let { if (it == 0) 12 else it }
    val hourLabel = stringResource(Res.string.time_picker_hour, WHEEL_VALUE_PLACEHOLDER)
    val minuteLabel = stringResource(Res.string.time_picker_minute, WHEEL_VALUE_PLACEHOLDER)
    val secondLabel = stringResource(Res.string.time_picker_second, WHEEL_VALUE_PLACEHOLDER)
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs)) {
        if (!use24HourFormat) {
            IenWheelPicker(
                items = listOf(stringResource(Res.string.time_picker_am), stringResource(Res.string.time_picker_pm)),
                selectedIndex = if (value.hour < 12) 0 else 1,
                onSelectedIndexChange = { onValueChange(LocalTime(value.hour % 12 + it * 12, value.minute, value.second)) },
                modifier = Modifier.weight(1f),
                enabled = enabled,
            )
        }
        IenWheelPicker(
            items = hours,
            selectedIndex = hours.indexOf(displayedHour),
            onSelectedIndexChange = {
                val hour = if (use24HourFormat) hours[it] else wheelPickerHour(hours[it], value.hour >= 12)
                onValueChange(LocalTime(hour, value.minute, value.second))
            },
            modifier = Modifier.weight(1f),
            enabled = enabled,
            itemLabel = { hourLabel.replace(WHEEL_VALUE_PLACEHOLDER, it.toString().padStart(2, '0')) },
        )
        IenWheelPicker(
            items = sexagesimal,
            selectedIndex = value.minute,
            onSelectedIndexChange = { onValueChange(LocalTime(value.hour, it, value.second)) },
            modifier = Modifier.weight(1f),
            enabled = enabled,
            itemLabel = { minuteLabel.replace(WHEEL_VALUE_PLACEHOLDER, it.toString().padStart(2, '0')) },
        )
        IenWheelPicker(
            items = sexagesimal,
            selectedIndex = value.second,
            onSelectedIndexChange = { onValueChange(LocalTime(value.hour, value.minute, it)) },
            modifier = Modifier.weight(1f),
            enabled = enabled,
            itemLabel = { secondLabel.replace(WHEEL_VALUE_PLACEHOLDER, it.toString().padStart(2, '0')) },
        )
    }
}

/**
 * 시각과 구분되는 기간 선택기. 24시간 이상의 기간도 지원합니다.
 * @param value 현재 기간. 음수가 아닌 유한한 정수 초여야 합니다.
 * @param onValueChange 선택한 기간을 전달합니다.
 * @param modifier 루트 레이아웃에 적용할 Modifier.
 * @param maxHours 선택 가능한 최대 시간 (0..9999). 분·초는 각각 0..59입니다.
 * @param enabled 선택 가능 여부.
 */
@Composable
fun IenDurationWheelPicker(
    value: Duration,
    onValueChange: (Duration) -> Unit,
    modifier: Modifier = Modifier,
    maxHours: Int = 23,
    enabled: Boolean = true,
) {
    require(maxHours in 0..9999) { "최대 시간은 0..9999 안이어야 합니다." }
    require(value.isFinite() && value >= Duration.ZERO && value == value.inWholeSeconds.seconds && value.inWholeHours <= maxHours) {
        "기간은 선택 범위 안의 음수가 아닌 정수 초여야 합니다."
    }
    val hour = value.inWholeHours.toInt()
    val minute = (value.inWholeMinutes % 60).toInt()
    val second = (value.inWholeSeconds % 60).toInt()
    val hours = remember(maxHours) { (0..maxHours).toList() }
    val sexagesimal = remember { (0..59).toList() }
    val hourLabel = stringResource(Res.string.duration_picker_hour, WHEEL_VALUE_PLACEHOLDER)
    val minuteLabel = stringResource(Res.string.duration_picker_minute, WHEEL_VALUE_PLACEHOLDER)
    val secondLabel = stringResource(Res.string.duration_picker_second, WHEEL_VALUE_PLACEHOLDER)
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs)) {
        IenWheelPicker(
            items = hours,
            selectedIndex = hour,
            onSelectedIndexChange = { onValueChange(wheelPickerDuration(it, minute, second)) },
            modifier = Modifier.weight(1.4f),
            enabled = enabled,
            itemLabel = { hourLabel.replace(WHEEL_VALUE_PLACEHOLDER, it.toString()) },
        )
        IenWheelPicker(
            items = sexagesimal,
            selectedIndex = minute,
            onSelectedIndexChange = { onValueChange(wheelPickerDuration(hour, it, second)) },
            modifier = Modifier.weight(1f),
            enabled = enabled,
            itemLabel = { minuteLabel.replace(WHEEL_VALUE_PLACEHOLDER, it.toString()) },
        )
        IenWheelPicker(
            items = sexagesimal,
            selectedIndex = second,
            onSelectedIndexChange = { onValueChange(wheelPickerDuration(hour, minute, it)) },
            modifier = Modifier.weight(1f),
            enabled = enabled,
            itemLabel = { secondLabel.replace(WHEEL_VALUE_PLACEHOLDER, it.toString()) },
        )
    }
}

internal fun wheelPickerDate(year: Int, month: Int, day: Int): LocalDate =
    LocalDate(year, month, day.coerceAtMost(YearMonth(year, month).numberOfDays))

internal fun wheelPickerHour(hour: Int, afternoon: Boolean): Int = hour % 12 + if (afternoon) 12 else 0

internal fun wheelPickerDuration(hour: Int, minute: Int, second: Int): Duration = hour.hours + minute.minutes + second.seconds

private const val WHEEL_VALUE_PLACEHOLDER = "__WHEEL_VALUE__"
