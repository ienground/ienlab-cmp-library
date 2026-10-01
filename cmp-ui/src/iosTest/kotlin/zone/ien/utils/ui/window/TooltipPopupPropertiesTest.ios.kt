package zone.ien.utils.ui.window

import androidx.compose.ui.ExperimentalComposeUiApi
import kotlin.test.Test
import kotlin.test.assertFalse

@OptIn(ExperimentalComposeUiApi::class)
class TooltipPopupPropertiesTest {
    @Test
    fun `iOS Tooltip Popup은 플랫폼 inset을 위치 계산에 포함하지 않는다`() {
        assertFalse(ienTooltipPopupProperties().usePlatformInsets)
    }
}
