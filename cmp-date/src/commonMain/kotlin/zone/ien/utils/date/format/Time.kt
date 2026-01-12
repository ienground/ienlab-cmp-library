package zone.ien.utils.date.format

import androidx.compose.runtime.Composable
import com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormat
import ienlab_cmp_library.cmp_date.generated.resources.Res
import ienlab_cmp_library.cmp_date.generated.resources.time_format_12
import ienlab_cmp_library.cmp_date.generated.resources.time_format_12_no_apm
import ienlab_cmp_library.cmp_date.generated.resources.time_format_24
import ienlab_cmp_library.cmp_date.generated.resources.time_format_24_short
import org.jetbrains.compose.resources.stringResource

val KDateTimeFormat.Companion.TimeFormat24: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.time_format_24))

val KDateTimeFormat.Companion.TimeFormat24Short: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.time_format_24_short))

val KDateTimeFormat.Companion.TimeFormat12: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.time_format_12)).apply { setComposableAmPm() }

val KDateTimeFormat.Companion.TimeFormat12NoApm: KDateTimeFormat
    @Composable get() = KDateTimeFormat(stringResource(Res.string.time_format_12_no_apm))

val KDateTimeFormat.Companion.ApmFormat: KDateTimeFormat
    @Composable get() = KDateTimeFormat("A").apply { setComposableAmPm() }