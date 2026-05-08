package zone.ien.utils.date.format

import androidx.compose.runtime.Composable
import com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormat
import zone.ien.utils.cmp_date.generated.resources.Res
import zone.ien.utils.cmp_date.generated.resources.time_format_12
import zone.ien.utils.cmp_date.generated.resources.time_format_12_no_apm
import zone.ien.utils.cmp_date.generated.resources.time_format_24
import zone.ien.utils.cmp_date.generated.resources.time_format_24_short
import org.jetbrains.compose.resources.stringResource

/** 24시간 형식의 시간 형식을 반환합니다 */
val KDateTimeFormat.Companion.TimeFormat24: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.time_format_24))

/** 24시간 형식의 짧은 시간 형식을 반환합니다 */
val KDateTimeFormat.Companion.TimeFormat24Short: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.time_format_24_short))

/** 12시간 형식의 시간 형식을 반환합니다 */
val KDateTimeFormat.Companion.TimeFormat12: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.time_format_12)).apply { setComposableAmPm() }

/** 12시간 형식의 APM을 포함하지 않는 시간 형식을 반환합니다 */
val KDateTimeFormat.Companion.TimeFormat12NoApm: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.time_format_12_no_apm))

/** APM 형식의 시간 형식을 반환합니다 */
val KDateTimeFormat.Companion.ApmFormat: KDateTimeFormat
    @Composable get() = KDateTimeFormat("A").apply { setComposableAmPm() }