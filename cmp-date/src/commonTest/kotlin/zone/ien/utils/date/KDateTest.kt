package zone.ien.utils.date

import com.sunnychung.lib.multiplatform.kdatetime.KDate
import kotlin.test.Test
import kotlin.test.assertEquals

class KDateTest {
    @Test
    fun `윤일에서_연도_이동_시_대상_월의_말일로_보정한다`() {
        val leapDay = KDate(2024, 2, 29)

        assertEquals(KDate(2025, 2, 28), leapDay.plusYear(1))
        assertEquals(KDate(2023, 2, 28), leapDay.minusYear(1))
    }
}
