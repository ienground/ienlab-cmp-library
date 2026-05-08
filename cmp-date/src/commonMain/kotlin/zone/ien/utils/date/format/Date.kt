package zone.ien.utils.date.format

import androidx.compose.runtime.Composable
import com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormat
import zone.ien.utils.cmp_date.generated.resources.Res
import zone.ien.utils.cmp_date.generated.resources.date_format
import zone.ien.utils.cmp_date.generated.resources.date_format_no_year
import zone.ien.utils.cmp_date.generated.resources.date_format_no_year_short
import zone.ien.utils.cmp_date.generated.resources.date_format_short
import org.jetbrains.compose.resources.stringResource

/** 전체 날짜 형식을 반환합니다 */
val KDateTimeFormat.Companion.DateFormat: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.date_format)).apply { weekDayNames = weekdayNames }

/** 연도를 포함하지 않는 날짜 형식을 반환합니다 */
val KDateTimeFormat.Companion.DateFormatNoYear: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.date_format_no_year)).apply { weekDayNames = weekdayNames }

/** 짧은 형식의 날짜 형식을 반환합니다 */
val KDateTimeFormat.Companion.DateFormatShort: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.date_format_short)).apply { weekDayNames = weekdayNames }

/** 연도를 포함하지 않는 짧은 형식의 날짜 형식을 반환합니다 */
val KDateTimeFormat.Companion.DateFormatNoYearShort: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.date_format_no_year_short)).apply { weekDayNames = weekdayNames }