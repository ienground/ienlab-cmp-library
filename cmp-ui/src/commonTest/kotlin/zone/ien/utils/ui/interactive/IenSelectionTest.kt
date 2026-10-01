package zone.ien.utils.ui.interactive

import androidx.compose.runtime.Composable
import kotlin.test.Test
import kotlin.test.assertEquals

class IenSelectionTest {
    @Test
    fun `세그먼트 아이템은 Boolean과 Int 값을 타입 안전하게 보존한다`() {
        val booleanItem = IenSegmentedControlItem(value = true, label = "켜짐")
        val intItem = IenSegmentedControlItem(value = 2, label = "두 명")

        assertEquals(true, booleanItem.value)
        assertEquals(2, intItem.value)
    }

    @Composable
    private fun genericSegmentedControlUsage(selected: Boolean, onChange: (Boolean) -> Unit) {
        IenSegmentedControl(
            items = listOf(
                IenSegmentedControlItem(value = false, label = "꺼짐"),
                IenSegmentedControlItem(value = true, label = "켜짐"),
            ),
            value = selected,
            onChange = onChange,
        )
    }
}
