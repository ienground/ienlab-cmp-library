package zone.ien.utils.date

import androidx.compose.runtime.Composable
import com.sunnychung.lib.multiplatform.kdatetime.KDate
import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit
import com.sunnychung.lib.multiplatform.kdatetime.KGregorianCalendar
import com.sunnychung.lib.multiplatform.kdatetime.KGregorianCalendar.addDays
import com.sunnychung.lib.multiplatform.kdatetime.KInstant
import com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset
import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime
import zone.ien.utils.cmp_date.generated.resources.Res
import zone.ien.utils.cmp_date.generated.resources.day_format
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/**
 * 현재 날짜를 반환하는 함수
 *
 * 이 함수는 시스템의 현재 시간을 기준으로 날짜 정보를 반환합니다.
 * 시간 및 시간대 정보는 로컬 시간대로 변환되어 반환됩니다.
 *
 * @return 현재 날짜 객체
 */
fun KDate.Companion.now() = KInstant.now().atLocalZoneOffset().toKZonedDateTime().datePart()

/**
 * 기본 날짜를 반환하는 함수
 *
 * 이 함수는 1990년 1월 1일을 기본 날짜로 반환합니다.
 * 일반적으로 초기값 또는 기본 상태를 나타내는 데 사용됩니다.
 *
 * @return 기본 날짜 객체 (1990년 1월 1일)
 */
fun KDate.Companion.Default() = KDate.now().copy(1990, 1, 1)

/**
 * 밀리초 값을 기반으로 날짜를 생성하는 함수
 *
 * 이 함수는 주어진 밀리초 시간을 기준으로 날짜 객체를 생성합니다.
 * 시간대 정보는 로컬 시간대로 변환되어 반환됩니다.
 *
 * @param value 밀리초 단위의 시간
 * @return 생성된 날짜 객체
 */
fun KDate.Companion.fromMillis(value: Long) = KZonedDateTime.fromMillis(value).datePart()

/**
 * 날짜를 밀리초 단위로 반환하는 함수
 *
 * 이 함수는 날짜 객체를 밀리초 단위의 시간으로 변환합니다.
 * 시간은 00:00:00으로 설정됩니다.
 *
 * @return 날짜의 밀리초 시간 값
 */
fun KDate.timeInMillis() = atTime(KDuration.from(0, 0)).timeInMillis()

/**
 * 날짜에 일 수를 더하는 함수
 *
 * 이 함수는 주어진 일 수를 날짜에 더합니다.
 * 음수 값을 주면 날짜를 줄입니다.
 *
 * @param day 더할 일 수 (음수도 가능)
 * @return 계산된 날짜 객체
 */
fun KDate.plusDay(day: Int) = addDays(day)

/**
 * 날짜에서 일 수를 빼는 함수
 *
 * 이 함수는 주어진 일 수를 날짜에서 뺍니다.
 * 음수 값을 주면 날짜를 늘립니다.
 *
 * @param day 뺄 일 수 (음수도 가능)
 * @return 계산된 날짜 객체
 */
fun KDate.minusDay(day: Int) = addDays(-day)

/**
 * 날짜에 개월 수를 더하는 함수
 *
 * 이 함수는 주어진 개월 수를 날짜에 더합니다.
 * 월별 계산은 윤년과 월의 일 수를 고려합니다.
 *
 * @param month 더할 개월 수 (음수도 가능)
 * @return 계산된 날짜 객체
 */
fun KDate.plusMonth(month: Int): KDate {
    val totalMonths = (this.year * 12 + this.month - 1) + month
    val newYear = if (totalMonths >= 0) totalMonths / 12 else (totalMonths - 11) / 12
    val newMonth = (totalMonths % 12 + 12) % 12 + 1

    val daysInMonth = when (newMonth) {
        2 -> if (KGregorianCalendar.isLeapYear(newYear)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

    return copy(year = newYear, month = newMonth, day = minOf(this.day, daysInMonth))
}

/**
 * 날짜에서 개월 수를 빼는 함수
 *
 * 이 함수는 주어진 개월 수를 날짜에서 뺍니다.
 * 음수 값을 주면 날짜를 늘립니다.
 *
 * @param month 뺄 개월 수 (음수도 가능)
 * @return 계산된 날짜 객체
 */
fun KDate.minusMonth(month: Int): KDate = plusMonth(-month)

/**
 * 날짜에 년 수를 더하는 함수
 *
 * 이 함수는 주어진 년 수를 날짜에 더합니다.
 *
 * @param year 더할 년 수 (음수도 가능)
 * @return 계산된 날짜 객체
 */
fun KDate.plusYear(year: Int): KDate {
    val newYear = this.year + year
    return copy(year = newYear)
}

/**
 * 날짜에서 년 수를 빼는 함수
 *
 * 이 함수는 주어진 년 수를 날짜에서 뺍니다.
 * 음수 값을 주면 날짜를 늘립니다.
 *
 * @param year 뺄 년 수 (음수도 가능)
 * @return 계산된 날짜 객체
 */
fun KDate.minusYear(year: Int): KDate = plusYear(-year)

/**
 * 날짜에 시간을 연결하는 함수
 *
 * 이 함수는 날짜에 시간 정보를 추가하여 KZonedDateTime으로 변환합니다.
 * 시간대 정보는 기본적으로 로컬 시간대로 설정됩니다.
 *
 * @param time 시간 정보 (기본값은 00:00:00)
 * @param timeZone 시간대 정보 (기본값은 로컬 시간대)
 * @return 날짜와 시간을 포함한 KZonedDateTime 객체
 */
fun KDate.atTime(time: KDuration = KDuration.from(0, 0), timeZone: KZoneOffset = KZoneOffset.local()) = KZonedDateTime(year, month, day, time.hourPart(), time.minutePart(), time.secondPart(), time.millisecondPart(), timeZone)

/**
 * 두 날짜가 동일한지 비교하는 함수
 *
 * 이 함수는 두 날짜 객체의 연도, 월, 일 정보를 비교하여 같음을 판단합니다.
 *
 * @param other 비교할 날짜 객체
 * @return 두 날짜가 같으면 true, 그렇지 않으면 false
 */
fun KDate.isEqual(other: KDate) = year == other.year && month == other.month && day == other.day

/**
 * 날짜가 다른 날짜 이후인지 비교하는 함수
 *
 * 이 함수는 현재 날짜가 다른 날짜 이후인지 비교합니다.
 * 포함 여부를 설정하여 같을 때의 결과를 조정할 수 있습니다.
 *
 * @param other 비교할 날짜 객체
 * @param inclusive 같을 때 결과에 포함할지 여부 (기본값은 false)
 * @return 현재 날짜가 다른 날짜 이후이면 true, 그렇지 않으면 false
 */
fun KDate.isAfter(other: KDate, inclusive: Boolean = false) = (year > other.year || (year == other.year && month > other.month) || (year == other.year && month == other.month && day > other.day)) || (isEqual(other) && inclusive)

/**
 * 날짜가 다른 날짜 이전인지 비교하는 함수
 *
 * 이 함수는 현재 날짜가 다른 날짜 이전인지 비교합니다.
 * 포함 여부를 설정하여 같을 때의 결과를 조정할 수 있습니다.
 *
 * @param other 비교할 날짜 객체
 * @param inclusive 같을 때 결과에 포함할지 여부 (기본값은 false)
 * @return 현재 날짜가 다른 날짜 이전이면 true, 그렇지 않으면 false
 */
fun KDate.isBefore(other: KDate, inclusive: Boolean = false) = (year < other.year || (year == other.year && month < other.month) || (year == other.year && month == other.month && day < other.day)) || (isEqual(other) && inclusive)

/**
 * 날짜가 특정 범위 내에 있는지 비교하는 함수
 *
 * 이 함수는 현재 날짜가 시작 날짜와 종료 날짜 사이에 있는지 검사합니다.
 * 포함 여부를 설정하여 경계값 처리를 할 수 있습니다.
 *
 * @param start 시작 날짜
 * @param end 종료 날짜
 * @param inclusive 경계값 포함 여부 (기본값은 (true, true))
 * @return 범위 내에 있으면 true, 그렇지 않으면 false
 */
fun KDate.isBetween(start: KDate, end: KDate, inclusive: Pair<Boolean, Boolean> = Pair(true, true)) = isAfter(start, inclusive = inclusive.first) && isBefore(end, inclusive = inclusive.second)

/**
 * 다른 날짜와의 기간을 문자열로 반환하는 함수
 *
 * 이 함수는 현재 날짜와 타겟 날짜 사이의 일 수를 문자열로 반환합니다.
 * 시간 정보는 무시됩니다.
 *
 * @param target 비교할 날짜 (기본값은 현재 날짜)
 * @return 일수를 포함한 문자열
 */
@Composable
fun KDate.timeDiffToString(target: KDate = KDate.now()): String {
    val diff = target.atTime().minus(this.atTime())
    val day = diff.toDays().toInt()

    return pluralStringResource(Res.plurals.day_format, day, day)
}