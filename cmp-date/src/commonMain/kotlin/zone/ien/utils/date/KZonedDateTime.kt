package zone.ien.utils.date

import androidx.compose.runtime.Composable
import com.sunnychung.lib.multiplatform.kdatetime.KDate
import com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormat
import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit
import com.sunnychung.lib.multiplatform.kdatetime.KInstant
import com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset
import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KInstantAsLong
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime
import zone.ien.utils.cmp_date.generated.resources.Res
import zone.ien.utils.cmp_date.generated.resources.day_format
import zone.ien.utils.cmp_date.generated.resources.hour_format
import zone.ien.utils.cmp_date.generated.resources.minute_format
import zone.ien.utils.cmp_date.generated.resources.month_format
import zone.ien.utils.cmp_date.generated.resources.time_just_now
import zone.ien.utils.cmp_date.generated.resources.week_format
import zone.ien.utils.cmp_date.generated.resources.year_format
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.date.format.DateFormatNoYearShort
import zone.ien.utils.date.format.TimeFormat12
import zone.ien.utils.date.format.format

fun KZonedDateTime.Companion.now() = KInstant.now().atLocalZoneOffset().toKZonedDateTime()
fun KZonedDateTime.timeInMillis() = toKInstant().toEpochMilliseconds()
fun KZonedDateTime.adjustUTC() = copy(zoneOffset = KZoneOffset.UTC)
fun KZonedDateTime.Companion.fromMillis(value: Long): KZonedDateTime = KInstantAsLong(value).atLocalZoneOffset().toKZonedDateTime()
val KZonedDateTime.Companion.Default: KZonedDateTime get() = KZonedDateTime.fromMillis(0)

fun KZonedDateTime.plusDay(day: Int) = plus(KDuration.of(day, KFixedTimeUnit.Day))
fun KZonedDateTime.minusDay(day: Int) = minus(KDuration.of(day, KFixedTimeUnit.Day))
fun KZonedDateTime.plusHour(hour: Int) = plus(KDuration.of(hour, KFixedTimeUnit.Hour))
fun KZonedDateTime.minusHour(hour: Int) = minus(KDuration.of(hour, KFixedTimeUnit.Hour))
fun KZonedDateTime.plusMinute(minute: Int) = plus(KDuration.of(minute, KFixedTimeUnit.Minute))
fun KZonedDateTime.minusMinute(minute: Int) = minus(KDuration.of(minute, KFixedTimeUnit.Minute))

fun KZonedDateTime.isEqual(other: KZonedDateTime) = timeInMillis() == other.timeInMillis()
fun KZonedDateTime.isAfter(other: KZonedDateTime, inclusive: Boolean = false) = timeInMillis() > other.timeInMillis() || (isEqual(other) && inclusive)
fun KZonedDateTime.isBefore(other: KZonedDateTime, inclusive: Boolean = false) = timeInMillis() < other.timeInMillis() || (isEqual(other) && inclusive)
fun KZonedDateTime.isBetween(start: KZonedDateTime, end: KZonedDateTime, inclusive: Pair<Boolean, Boolean> = Pair(true, true)) = isAfter(start, inclusive = inclusive.first) && isBefore(end, inclusive = inclusive.second)
fun KZonedDateTime.isBetween(date: KDate) = isBetween(date.atTime(KDuration.from(0, 0)), date.plusDay(1).atTime(
    KDuration.from(0, 0)), inclusive = Pair(true, false)
)
fun Pair<KZonedDateTime?, KZonedDateTime?>.isOverlap(target: Pair<KZonedDateTime?, KZonedDateTime?>): Boolean {
    if (first == null || second == null || target.first == null || target.second == null) return false
    return first!!.isBefore(target.second!!, inclusive = false) && target.first!!.isBefore(second!!, inclusive = false)
}

@Composable
fun KZonedDateTime.format(): String {
    val today = KZonedDateTime.now()
    return if (today.datePart().isEqual(datePart())) {
        KDateTimeFormat.TimeFormat12.format(this)
    } else {
        KDateTimeFormat.DateFormatNoYearShort.format(this)
    }
}

@Composable
fun KZonedDateTime.timeDiffToString(target: KZonedDateTime = KZonedDateTime.now()): String {
    val diff = target.minus(this)
    val week = diff.toTimeUnitValue(KFixedTimeUnit.Week).toInt()
    val day = diff.toDays().toInt()
    val hour = diff.toHours().toInt() % 24
    val minute = diff.toMinutes().toInt() % 60

    return when {
        day >= 365 -> pluralStringResource(Res.plurals.year_format, day / 365, day / 365)
        day >= 30 -> pluralStringResource(Res.plurals.month_format, day / 30, day / 30)
        week >= 1 -> pluralStringResource(Res.plurals.week_format, week, week)
        day >= 1 -> pluralStringResource(Res.plurals.day_format, day, day)
        hour >= 1 -> pluralStringResource(Res.plurals.hour_format, hour, hour)
        else -> {
            when (minute) {
                0 -> stringResource(Res.string.time_just_now)
                else -> pluralStringResource(Res.plurals.minute_format, minute, minute)
            }
        }
    }
}