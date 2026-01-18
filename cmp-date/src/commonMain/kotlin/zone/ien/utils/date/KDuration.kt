package zone.ien.utils.date

import androidx.compose.runtime.Composable
import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit
import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import zone.ien.utils.cmp_date.generated.resources.Res
import zone.ien.utils.cmp_date.generated.resources.hour_format
import zone.ien.utils.cmp_date.generated.resources.minute_format
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

fun KDuration.Companion.from(hourOfDay: Int = 0, minute: Int = 0, second: Int = 0) = of(hourOfDay * 3600 + minute * 60 + second, KFixedTimeUnit.Second)
fun KDuration.Companion.now() = KZonedDateTime.now().let { KDuration.from(it.hour, it.minute, it.second) }
fun KDuration.int() = hourPart() * 60 + minutePart()
fun KDuration.Companion.fromInt(value: Int) = KDuration.from(value / 60, value % 60)

fun KDuration.plusHour(hour: Int) = plus(KDuration.of(hour, KFixedTimeUnit.Hour))
fun KDuration.minusHour(hour: Int) = minus(KDuration.of(hour, KFixedTimeUnit.Hour))
fun KDuration.plusMinute(minute: Int) = plus(KDuration.of(minute, KFixedTimeUnit.Minute))
fun KDuration.minusMinute(minute: Int) = minus(KDuration.of(minute, KFixedTimeUnit.Minute))

fun KDuration.isEqual(other: KDuration) = toMilliseconds() == other.toMilliseconds()
fun KDuration.isAfter(other: KDuration, inclusive: Boolean = false) = toMilliseconds() > other.toMilliseconds() || (isEqual(other) && inclusive)
fun KDuration.isBefore(other: KDuration, inclusive: Boolean = false) = toMilliseconds() < other.toMilliseconds() || (isEqual(other) && inclusive)
fun KDuration.isBetween(start: KDuration, end: KDuration, inclusive: Pair<Boolean, Boolean> = Pair(true, true)) = isAfter(start, inclusive = inclusive.first) && isBefore(end, inclusive = inclusive.second)

fun KDuration.format(): String {
    val hour = hourPart()
    val minute = minutePart()
    val second = secondPart()
    val result: ArrayList<String> = arrayListOf()

    if (hour != 0) result.add(hour.toString().padStart(2, '0'))
    result.add(minute.toString().padStart(2, '0'))
    result.add(second.toString().padStart(2, '0'))

    return result.joinToString(":")
}

@Composable
fun KDuration.timeDiffToString(target: KDuration): String {
    val diff = target.minus(this)
    val hour = diff.hourPart()
    val minute = diff.minutePart()
    val result = ArrayList<String>()

    if (hour != 0) result.add(pluralStringResource(Res.plurals.hour_format, hour, hour))
    result.add(pluralStringResource(Res.plurals.minute_format, minute, minute))

    return result.joinToString(" ")
}