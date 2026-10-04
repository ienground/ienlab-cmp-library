package zone.ien.utils.ui.interactive

import com.sunnychung.lib.multiplatform.kdatetime.KDate
import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals

class IenWheelPickerTest {
    @Test
    fun monthChangesClampDayAndRespectLeapYears() {
        assertEquals(KDate(2024, 2, 29), wheelPickerDate(2024, 2, 31))
        assertEquals(KDate(2025, 2, 28), wheelPickerDate(2025, 2, 29))
        assertEquals(KDate(2000, 2, 29), wheelPickerDate(2000, 2, 31))
        assertEquals(KDate(1900, 2, 28), wheelPickerDate(1900, 2, 31))
        assertEquals(KDate(2026, 4, 30), wheelPickerDate(2026, 4, 31))
    }

    @Test
    fun twelveHourConversionHandlesMidnightAndNoon() {
        assertEquals(0, wheelPickerHour(12, false))
        assertEquals(12, wheelPickerHour(12, true))
        assertEquals(13, wheelPickerHour(1, true))
        assertEquals(23, wheelPickerHour(11, true))
    }

    @Test
    fun durationKeepsHoursBeyondOneDay() {
        assertEquals(KDuration.of(25 * 3600L + 5 * 60L + 5, KFixedTimeUnit.Second), wheelPickerDuration(25, 5, 5))
        assertEquals(KDuration.of(0L, KFixedTimeUnit.Second), wheelPickerDuration(0, 0, 0))
    }
}

