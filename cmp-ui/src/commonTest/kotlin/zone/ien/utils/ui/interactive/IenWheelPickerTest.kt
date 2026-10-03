package zone.ien.utils.ui.interactive

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class IenWheelPickerTest {
    @Test
    fun monthChangesClampDayAndRespectLeapYears() {
        assertEquals(LocalDate(2024, 2, 29), wheelPickerDate(2024, 2, 31))
        assertEquals(LocalDate(2025, 2, 28), wheelPickerDate(2025, 2, 29))
        assertEquals(LocalDate(2000, 2, 29), wheelPickerDate(2000, 2, 31))
        assertEquals(LocalDate(1900, 2, 28), wheelPickerDate(1900, 2, 31))
        assertEquals(LocalDate(2026, 4, 30), wheelPickerDate(2026, 4, 31))
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
        assertEquals(25.hours + 5.minutes + 5.seconds, wheelPickerDuration(25, 5, 5))
        assertEquals(0.seconds, wheelPickerDuration(0, 0, 0))
    }
}
