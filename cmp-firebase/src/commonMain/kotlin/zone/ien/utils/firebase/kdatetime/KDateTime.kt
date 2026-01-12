package zone.ien.utils.firebase.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.KDate
import com.sunnychung.lib.multiplatform.kdatetime.KInstant
import com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset
import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KInstantAsLong
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime
import dev.gitlive.firebase.firestore.Timestamp

fun Timestamp.toKDate() = toKZonedDateTime().datePart()
fun KDate.toTimestamp() = KZonedDateTime(year, month, day, 0, 0, 0, 0, KZoneOffset.local()).toKInstant().toEpochMilliseconds().let { Timestamp(it / 1000, ((it % 1000) * 1000).toInt()) }

fun Timestamp.toKZonedDateTime() = KInstantAsLong(seconds * 1000 + nanoseconds / 1_000_000).atLocalZoneOffset().toKZonedDateTime()
fun KZonedDateTime.toTimestamp() = toKInstant().toEpochMilliseconds().let { Timestamp(it / 1000, ((it % 1000) * 1000).toInt()) }

fun Timestamp.toKInstant() = KInstantAsLong(seconds * 1000)
fun KInstant.toTimestamp() = Timestamp(this.toMilliseconds() / 1000, 0)