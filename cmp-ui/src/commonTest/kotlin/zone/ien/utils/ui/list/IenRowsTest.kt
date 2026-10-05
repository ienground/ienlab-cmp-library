package zone.ien.utils.ui.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class IenRowsTest {
    @Composable
    private fun composableTextSlotsAreAccepted() {
        IenListRowTexts(
            type = IenListRowTextsType.ThreeRowTypeC,
            top = { Text("top") },
            middle = { Text("middle") },
            bottom = { Text("bottom") },
        )
    }
}
