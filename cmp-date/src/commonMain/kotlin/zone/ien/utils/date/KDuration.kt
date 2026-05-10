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

/**
 * 시간, 분, 초를 기준으로 KDuration 인스턴스를 생성합니다
 *
 * @param hourOfDay 시간 (0-23)
 * @param minute 분 (0-59)
 * @param second 초 (0-59)
 * @return 생성된 KDuration 객체
 */
fun KDuration.Companion.from(hourOfDay: Int = 0, minute: Int = 0, second: Int = 0) = of(hourOfDay * 3600 + minute * 60 + second, KFixedTimeUnit.Second)
/** 현재 시간을 기준으로 KDuration 인스턴스를 생성합니다 */
fun KDuration.Companion.now() = KZonedDateTime.now().let { KDuration.from(it.hour, it.minute, it.second) }
/** 시간과 분을 분 단위로 변환하여 정수로 반환합니다 */
fun KDuration.int() = hourPart() * 60 + minutePart()
/** 분 단위의 정수를 기준으로 KDuration 인스턴스를 생성합니다 */
fun KDuration.Companion.fromInt(value: Int) = KDuration.from(value / 60, value % 60)

/** 지정된 시간만큼 Duration을 추가합니다 */
fun KDuration.plusHour(hour: Int) = plus(KDuration.of(hour, KFixedTimeUnit.Hour))
/** 지정된 시간만큼 Duration을 감소시킵니다 */
fun KDuration.minusHour(hour: Int) = minus(KDuration.of(hour, KFixedTimeUnit.Hour))
/** 지정된 분만큼 Duration을 추가합니다 */
fun KDuration.plusMinute(minute: Int) = plus(KDuration.of(minute, KFixedTimeUnit.Minute))
/** 지정된 분만큼 Duration을 감소시킵니다 */
fun KDuration.minusMinute(minute: Int) = minus(KDuration.of(minute, KFixedTimeUnit.Minute))

/** 두 KDuration 인스턴스가 같은 시간을 갖는지를 비교합니다 */
fun KDuration.isEqual(other: KDuration) = toMilliseconds() == other.toMilliseconds()
/** 현재 Duration이 다른 Duration보다 이후인지를 비교합니다 */
fun KDuration.isAfter(other: KDuration, inclusive: Boolean = false) = toMilliseconds() > other.toMilliseconds() || (isEqual(other) && inclusive)
/** 현재 Duration이 다른 Duration보다 이전인지를 비교합니다 */
fun KDuration.isBefore(other: KDuration, inclusive: Boolean = false) = toMilliseconds() < other.toMilliseconds() || (isEqual(other) && inclusive)
/** 현재 Duration이 지정된 Duration 범위 내에 있는지를 확인합니다 */
fun KDuration.isBetween(start: KDuration, end: KDuration, inclusive: Pair<Boolean, Boolean> = Pair(true, true)) = isAfter(start, inclusive = inclusive.first) && isBefore(end, inclusive = inclusive.second)

/** KDuration 인스턴스를 시간 형식 문자열로 포맷화합니다 */
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
/** Duration 차이를 사람이 읽기 쉬운 문자열로 변환합니다 */
fun KDuration.timeDiffToString(target: KDuration): String {
    val diff = target.minus(this)
    val hour = diff.hourPart()
    val minute = diff.minutePart()
    val result = ArrayList<String>()

    if (hour != 0) result.add(pluralStringResource(Res.plurals.hour_format, hour, hour))
    result.add(pluralStringResource(Res.plurals.minute_format, minute, minute))

    return result.joinToString(" ")
}