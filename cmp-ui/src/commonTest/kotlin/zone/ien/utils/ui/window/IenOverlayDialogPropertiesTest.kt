package zone.ien.utils.ui.window

import kotlin.test.Test
import kotlin.test.assertFalse

class IenOverlayDialogPropertiesTest {
    @Test
    fun `오버레이 다이얼로그는 플랫폼 기본 너비를 사용하지 않는다`() {
        val properties = ienOverlayDialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )

        assertFalse(properties.usePlatformDefaultWidth)
        assertFalse(properties.dismissOnBackPress)
        assertFalse(properties.dismissOnClickOutside)
    }
}
