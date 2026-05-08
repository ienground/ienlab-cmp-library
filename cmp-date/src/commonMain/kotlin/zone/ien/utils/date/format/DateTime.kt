package zone.ien.utils.date.format

import androidx.compose.runtime.Composable
import com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormat
import zone.ien.utils.cmp_date.generated.resources.Res
import zone.ien.utils.cmp_date.generated.resources.date_time_format
import zone.ien.utils.cmp_date.generated.resources.date_time_format_no_year
import zone.ien.utils.cmp_date.generated.resources.date_time_format_no_year_short
import zone.ien.utils.cmp_date.generated.resources.date_time_format_short
import zone.ien.utils.cmp_date.generated.resources.date_time_format_short_sec
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

/** 전체 날짜 및 시간 형식을 반환합니다 */
val KDateTimeFormat.Companion.DateTimeFormat: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.date_time_format)).apply {
        weekDayNames = weekdayNames
        setComposableAmPm()
    }

/** 날짜 및 시간 형식을 비동기적으로 가져옵니다 */
suspend fun KDateTimeFormat.Companion.getDateTimeFormat(): KDateTimeFormat {
    return KDateTimeFormat(getString(Res.string.date_time_format)).apply {
        weekDayNames = getWeekdayNames()
        setAmPm()
    }
}

/** 짧은 형식의 날짜 및 시간 형식을 반환합니다 */
val KDateTimeFormat.Companion.DateTimeFormatShort: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.date_time_format_short)).apply {
        weekDayNames = weekdayNames
        setComposableAmPm()
    }

/** 초 단위까지 포함된 짧은 형식의 날짜 및 시간 형식을 반환합니다 */
val KDateTimeFormat.Companion.DateTimeFormatShortSec: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.date_time_format_short_sec)).apply {
        weekDayNames = weekdayNames
        setComposableAmPm()
    }

/** 연도를 포함하지 않는 날짜 및 시간 형식을 반환합니다 */
val KDateTimeFormat.Companion.DateTimeFormatNoYear: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.date_time_format_no_year)).apply {
        weekDayNames = weekdayNames
        setComposableAmPm()
    }

/** 연도를 포함하지 않는 짧은 형식의 날짜 및 시간 형식을 반환합니다 */
val KDateTimeFormat.Companion.DateTimeFormatNoYearShort: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.date_time_format_no_year_short)).apply {
        weekDayNames = weekdayNames
        setComposableAmPm()
    }