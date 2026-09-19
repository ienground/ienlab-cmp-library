package zone.ien.utils.ui.list

import androidx.compose.runtime.Composable
import zone.ien.utils.ui.primitives.IenText

class IenRowsTest {
    @Composable
    private fun composableTextSlotsAreAccepted() {
        IenListRowTexts(
            type = IenListRowTextsType.ThreeRowTypeC,
            top = { IenText("top") },
            middle = { IenText("middle") },
            bottom = { IenText("bottom") },
        )
    }
}
