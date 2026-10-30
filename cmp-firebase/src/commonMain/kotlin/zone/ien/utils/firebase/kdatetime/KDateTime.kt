package zone.ien.utils.firebase.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.KDate
import com.sunnychung.lib.multiplatform.kdatetime.KInstant
import com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset
import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KInstantAsLong
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime
import zone.ien.firebase.firestore.Timestamp

/**
 * Timestamp를 KDate로 변환하는 확장 함수
 * @return KDate 객체
 */
fun Timestamp.toKDate() = toKZonedDateTime().datePart()

/**
 * KDate를 Timestamp로 변환하는 확장 함수
 * @return Timestamp 객체
 */
fun KDate.toTimestamp() = KZonedDateTime(year, month, day, 0, 0, 0, 0, KZoneOffset.local()).toKInstant().toTimestamp()

/**
 * Timestamp를 KZonedDateTime으로 변환하는 확장 함수
 * @return KZonedDateTime 객체
 */
fun Timestamp.toKZonedDateTime() = KInstantAsLong(seconds * 1000 + nanoseconds / 1_000_000).atLocalZoneOffset().toKZonedDateTime()

/**
 * KZonedDateTime을 Timestamp로 변환하는 확장 함수
 * @return Timestamp 객체
 */
fun KZonedDateTime.toTimestamp() = toKInstant().toTimestamp()

/**
 * Timestamp를 KInstant로 변환하는 확장 함수
 * @return KInstant 객체
 */
fun Timestamp.toKInstant() = KInstantAsLong(seconds * 1000 + nanoseconds / 1_000_000)

/**
 * KInstant을 Timestamp로 변환하는 확장 함수
 * @return Timestamp 객체
 */
fun KInstant.toTimestamp(): Timestamp {
    val epochMilliseconds = toEpochMilliseconds()
    val seconds = epochMilliseconds / 1000
    val milliseconds = epochMilliseconds % 1000
    val normalizedSeconds = if (milliseconds < 0) seconds - 1 else seconds
    val normalizedMilliseconds = if (milliseconds < 0) milliseconds + 1000 else milliseconds

    return Timestamp(normalizedSeconds, (normalizedMilliseconds * 1_000_000).toInt())
}
