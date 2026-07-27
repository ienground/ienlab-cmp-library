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
fun KDate.toTimestamp() = KZonedDateTime(year, month, day, 0, 0, 0, 0, KZoneOffset.local()).toKInstant().toEpochMilliseconds().let { Timestamp(it / 1000, ((it % 1000) * 1000).toInt()) }

/**
 * Timestamp를 KZonedDateTime으로 변환하는 확장 함수
 * @return KZonedDateTime 객체
 */
fun Timestamp.toKZonedDateTime() = KInstantAsLong(seconds * 1000 + nanoseconds / 1_000_000).atLocalZoneOffset().toKZonedDateTime()

/**
 * KZonedDateTime을 Timestamp로 변환하는 확장 함수
 * @return Timestamp 객체
 */
fun KZonedDateTime.toTimestamp() = toKInstant().toEpochMilliseconds().let { Timestamp(it / 1000, ((it % 1000) * 1000).toInt()) }

/**
 * Timestamp를 KInstant로 변환하는 확장 함수
 * @return KInstant 객체
 */
fun Timestamp.toKInstant() = KInstantAsLong(seconds * 1000)

/**
 * KInstant을 Timestamp로 변환하는 확장 함수
 * @return Timestamp 객체
 */
fun KInstant.toTimestamp() = Timestamp(this.toMilliseconds() / 1000, 0)