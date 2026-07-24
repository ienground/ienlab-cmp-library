package zone.ien.utils.ui.utils

import com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset
import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KInstantAsLong
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime

internal actual fun adjustDatePickerMillis(time: Long): Long =
    KInstantAsLong(time)
        .atLocalZoneOffset()
        .toKZonedDateTime()
        .copy(zoneOffset = KZoneOffset.UTC)
        .toKInstant()
        .toEpochMilliseconds()
