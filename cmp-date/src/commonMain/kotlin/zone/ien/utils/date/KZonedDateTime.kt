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

/** 현재 시간을 기준으로 KZonedDateTime 인스턴스를 생성합니다 */
fun KZonedDateTime.Companion.now() = KInstant.now().atLocalZoneOffset().toKZonedDateTime()
/** 시간을 밀리초 단위의 epoch 시간으로 변환합니다 */
fun KZonedDateTime.timeInMillis() = toKInstant().toEpochMilliseconds()
/** 시간대 오프셋을 UTC로 조정합니다 */
fun KZonedDateTime.adjustUTC() = copy(zoneOffset = KZoneOffset.UTC)
/** 밀리초 단위의 epoch 시간을 기준으로 KZonedDateTime 인스턴스를 생성합니다 */
fun KZonedDateTime.Companion.fromMillis(value: Long): KZonedDateTime = KInstantAsLong(value).atLocalZoneOffset().toKZonedDateTime()
/** 기본 KZonedDateTime 인스턴스를 반환합니다 (epoch 0 시간) */
val KZonedDateTime.Companion.Default: KZonedDateTime get() = KZonedDateTime.fromMillis(0)

/** 지정된 일 수 만큼 시간을 추가합니다 */
fun KZonedDateTime.plusDay(day: Int) = plus(KDuration.of(day, KFixedTimeUnit.Day))
/** 지정된 일 수 만큼 시간을 감소시킵니다 */
fun KZonedDateTime.minusDay(day: Int) = minus(KDuration.of(day, KFixedTimeUnit.Day))
/** 지정된 시간 만큼 시간을 추가합니다 */
fun KZonedDateTime.plusHour(hour: Int) = plus(KDuration.of(hour, KFixedTimeUnit.Hour))
/** 지정된 시간 만큼 시간을 감소시킵니다 */
fun KZonedDateTime.minusHour(hour: Int) = minus(KDuration.of(hour, KFixedTimeUnit.Hour))
/** 지정된 분 만큼 시간을 추가합니다 */
fun KZonedDateTime.plusMinute(minute: Int) = plus(KDuration.of(minute, KFixedTimeUnit.Minute))
/** 지정된 분 만큼 시간을 감소시킵니다 */
fun KZonedDateTime.minusMinute(minute: Int) = minus(KDuration.of(minute, KFixedTimeUnit.Minute))

/** 두 KZonedDateTime 인스턴스가 같은 시간을 갖는지를 비교합니다 */
fun KZonedDateTime.isEqual(other: KZonedDateTime) = timeInMillis() == other.timeInMillis()
/** 현재 시간이 다른 시간보다 이후인지를 비교합니다 */
fun KZonedDateTime.isAfter(other: KZonedDateTime, inclusive: Boolean = false) = timeInMillis() > other.timeInMillis() || (isEqual(other) && inclusive)
/** 현재 시간이 다른 시간보다 이전인지를 비교합니다 */
fun KZonedDateTime.isBefore(other: KZonedDateTime, inclusive: Boolean = false) = timeInMillis() < other.timeInMillis() || (isEqual(other) && inclusive)
/** 현재 시간이 지정된 시간 범위 내에 있는지를 확인합니다 */
fun KZonedDateTime.isBetween(start: KZonedDateTime, end: KZonedDateTime, inclusive: Pair<Boolean, Boolean> = Pair(true, true)) = isAfter(start, inclusive = inclusive.first) && isBefore(end, inclusive = inclusive.second)
/** 주어진 날짜 범위 내에 있는지를 확인합니다 */
fun KZonedDateTime.isBetween(date: KDate) = isBetween(date.atTime(KDuration.from(0, 0)), date.plusDay(1).atTime(
    KDuration.from(0, 0)), inclusive = Pair(true, false)
)
/** 두 시간 범위가 겹치는지를 확인합니다 */
fun Pair<KZonedDateTime?, KZonedDateTime?>.isOverlap(target: Pair<KZonedDateTime?, KZonedDateTime?>): Boolean {
    if (first == null || second == null || target.first == null || target.second == null) return false
    return first!!.isBefore(target.second!!, inclusive = false) && target.first!!.isBefore(second!!, inclusive = false)
}

@Composable
/** KZonedDateTime 인스턴스를 포맷화하여 문자열로 반환합니다 */
fun KZonedDateTime.format(): String {
    val today = KZonedDateTime.now()
    return if (today.datePart().isEqual(datePart())) {
        KDateTimeFormat.TimeFormat12.format(this)
    } else {
        KDateTimeFormat.DateFormatNoYearShort.format(this)
    }
}

@Composable
/** 시간 차이를 사람이 읽기 쉬운 문자열로 변환합니다 */
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