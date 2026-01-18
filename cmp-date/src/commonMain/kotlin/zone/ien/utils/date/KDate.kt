package zone.ien.utils.date

import androidx.compose.runtime.Composable
import com.sunnychung.lib.multiplatform.kdatetime.KDate
import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit
import com.sunnychung.lib.multiplatform.kdatetime.KGregorianCalendar.addDays
import com.sunnychung.lib.multiplatform.kdatetime.KInstant
import com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset
import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime
import zone.ien.utils.cmp_date.generated.resources.Res
import zone.ien.utils.cmp_date.generated.resources.day_format
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

fun KDate.Companion.now() = KInstant.now().atLocalZoneOffset().toKZonedDateTime().datePart()
fun KDate.Companion.Default() = KDate.now().copy(1990, 1, 1)
fun KDate.Companion.fromMillis(value: Long) = KZonedDateTime.fromMillis(value).datePart()
fun KDate.timeInMillis() = atTime(KDuration.from(0, 0)).timeInMillis()

fun KDate.plusDay(day: Int) = addDays(day)
fun KDate.minusDay(day: Int) = addDays(-day)
fun KDate.plusMonth(month: Int): KDate {
    // 현재 연도와 월 계산
    val totalMonths = (this.year * 12 + this.month - 1) + month
    val newYear = totalMonths / 12
    val newMonth = totalMonths % 12 + 1 // 1~12 사이로 조정

    return copy(year = newYear, month = newMonth)
}
fun KDate.minusMonth(month: Int): KDate = plusMonth(-month)
fun KDate.plusYear(year: Int): KDate {
    val newYear = this.year + year
    return copy(year = newYear)
}
fun KDate.minusYear(year: Int): KDate = plusYear(-year)


fun KDate.atTime(time: KDuration = KDuration.from(0, 0), timeZone: KZoneOffset = KZoneOffset.local()) = KZonedDateTime(year, month, day, time.hourPart(), time.minutePart(), time.secondPart(), time.millisecondPart(), timeZone)

fun KDate.isEqual(other: KDate) = year == other.year && month == other.month && day == other.day
fun KDate.isAfter(other: KDate, inclusive: Boolean = false) = (year > other.year || (year == other.year && month > other.month) || (year == other.year && month == other.month && day > other.day)) || (isEqual(other) && inclusive)
fun KDate.isBefore(other: KDate, inclusive: Boolean = false) = (year < other.year || (year == other.year && month < other.month) || (year == other.year && month == other.month && day < other.day)) || (isEqual(other) && inclusive)
fun KDate.isBetween(start: KDate, end: KDate, inclusive: Pair<Boolean, Boolean> = Pair(true, true)) = isAfter(start, inclusive = inclusive.first) && isBefore(end, inclusive = inclusive.second)

@Composable
fun KDate.timeDiffToString(target: KDate = KDate.now()): String {
    val diff = target.atTime().minus(this.atTime())
    val day = diff.toDays().toInt()

    return pluralStringResource(Res.plurals.day_format, day, day)
}