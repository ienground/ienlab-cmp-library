package zone.ien.utils.ui.screen

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IenPermissionScreenTest {
    @Test
    fun permissionItemKeepsIconAndCopy() {
        val icon = ImageVector.Builder(
            name = "TestIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).build()
        val item = IenPermissionItem(
            icon = icon,
            title = "알림",
            description = "중요한 소식을 알려드립니다.",
        )

        assertEquals(icon, item.icon)
        assertEquals("알림", item.title)
        assertEquals("중요한 소식을 알려드립니다.", item.description)
        assertTrue(item.isRequired)

        val optionalItem = item.copy(isRequired = false)
        assertFalse(optionalItem.isRequired)
    }
}
