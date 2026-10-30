package zone.ien.utils.ui.screen

import androidx.compose.ui.unit.IntRect
import kotlin.test.Test
import kotlin.test.assertEquals

class IenBottomCTATest {
    @Test
    fun sideCTAStaysBetweenSystemRegions() {
        assertEquals(
            IntRect(928, 120, 1000, 760),
            ienBottomCTASideBounds(1000, 800, 72, 120, 40, 36, 0),
        )
    }

    @Test
    fun closedDuoUsesCameraCenter() {
        assertEquals(
            IntRect(904, 120, 976, 760),
            ienBottomCTASideBounds(1000, 800, 72, 120, 40, 60, 0),
        )
    }

    @Test
    fun keyboardReplacesBottomReservationWithoutAddingItTwice() {
        assertEquals(
            IntRect(928, 120, 1000, 480),
            ienBottomCTASideBounds(1000, 800, 72, 120, 40, 36, 320),
        )
    }

    @Test
    fun smallParentKeepsBoundsInsideAvailableSpace() {
        assertEquals(
            IntRect(0, 30, 40, 30),
            ienBottomCTASideBounds(40, 30, 72, 120, 40, 60, 320),
        )
    }
}
