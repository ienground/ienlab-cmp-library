package zone.ien.utils.date.format

import androidx.compose.runtime.Composable
import com.sunnychung.lib.multiplatform.kdatetime.KDate
import com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormat
import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import ienlab_cmp_library.cmp_date.generated.resources.Res
import ienlab_cmp_library.cmp_date.generated.resources.fri
import ienlab_cmp_library.cmp_date.generated.resources.mon
import ienlab_cmp_library.cmp_date.generated.resources.sat
import ienlab_cmp_library.cmp_date.generated.resources.sun
import ienlab_cmp_library.cmp_date.generated.resources.thu
import ienlab_cmp_library.cmp_date.generated.resources.time_am
import ienlab_cmp_library.cmp_date.generated.resources.time_pm
import ienlab_cmp_library.cmp_date.generated.resources.tue
import ienlab_cmp_library.cmp_date.generated.resources.wed
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.date.atTime
import zone.ien.utils.date.from

fun KDateTimeFormat.format(time: KZonedDateTime) = format(time.toKZonedInstant())
fun KDateTimeFormat.format(date: KDate) = format(date.atTime(KDuration.from(0, 0)).toKZonedInstant())

internal val weekdayNames
    @Composable get() = listOf(
        stringResource(Res.string.sun),
        stringResource(Res.string.mon),
        stringResource(Res.string.tue),
        stringResource(Res.string.wed),
        stringResource(Res.string.thu),
        stringResource(Res.string.fri),
        stringResource(Res.string.sat)
    )

internal suspend fun getWeekdayNames() = listOf(
    getString(Res.string.sun),
    getString(Res.string.mon),
    getString(Res.string.tue),
    getString(Res.string.wed),
    getString(Res.string.thu),
    getString(Res.string.fri),
    getString(Res.string.sat)
)

@Composable
internal fun KDateTimeFormat.setComposableAmPm() {
    setAmPmNames(stringResource(Res.string.time_am), stringResource(Res.string.time_pm))
}

internal suspend fun KDateTimeFormat.setAmPm() {
    setAmPmNames(getString(Res.string.time_am), getString(Res.string.time_pm))
}