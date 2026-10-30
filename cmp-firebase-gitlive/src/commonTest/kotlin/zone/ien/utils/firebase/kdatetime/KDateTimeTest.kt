package zone.ien.utils.firebase.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.serializer.KInstantAsLong
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime
import dev.gitlive.firebase.firestore.Timestamp
import kotlin.test.Test
import kotlin.test.assertEquals

class KDateTimeTest {
    @Test
    fun `밀리초와_음수_epoch를_timestamp로_왕복한다`() {
        val cases = listOf(
            Triple(0L, 0L, 0),
            Triple(1L, 0L, 1_000_000),
            Triple(999L, 0L, 999_000_000),
            Triple(1_234L, 1L, 234_000_000),
            Triple(-1L, -1L, 999_000_000),
            Triple(-1_001L, -2L, 999_000_000),
        )

        cases.forEach { (milliseconds, expectedSeconds, expectedNanoseconds) ->
            val timestamp = Timestamp(expectedSeconds, expectedNanoseconds)

            val fromTimestamp = timestamp.toKInstant().toTimestamp()
            assertEquals(expectedSeconds, fromTimestamp.seconds, "Timestamp -> KInstant -> Timestamp: $milliseconds ms")
            assertEquals(expectedNanoseconds, fromTimestamp.nanoseconds, "Timestamp -> KInstant -> Timestamp: $milliseconds ms")

            val fromInstant = KInstantAsLong(milliseconds).toTimestamp()
            assertEquals(expectedSeconds, fromInstant.seconds, "KInstant -> Timestamp: $milliseconds ms")
            assertEquals(expectedNanoseconds, fromInstant.nanoseconds, "KInstant -> Timestamp: $milliseconds ms")

            val fromZonedDateTime = KInstantAsLong(milliseconds)
                .atLocalZoneOffset()
                .toKZonedDateTime()
                .toTimestamp()
            assertEquals(expectedSeconds, fromZonedDateTime.seconds, "KZonedDateTime -> Timestamp: $milliseconds ms")
            assertEquals(expectedNanoseconds, fromZonedDateTime.nanoseconds, "KZonedDateTime -> Timestamp: $milliseconds ms")
        }
    }
}
