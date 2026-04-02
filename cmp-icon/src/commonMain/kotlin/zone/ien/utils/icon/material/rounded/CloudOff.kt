package zone.ien.utils.icon.material.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons

val M3SystemIcons.Rounded.CloudOff: ImageVector
    get() {
        if (_CloudOff != null) {
            return _CloudOff!!
        }
        _CloudOff = ImageVector.Builder(
            name = "CloudOff",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(260f, 800f)
                quadToRelative(-92f, 0f, -156f, -64f)
                reflectiveQuadTo(40f, 580f)
                quadToRelative(0f, -77f, 47.5f, -137f)
                reflectiveQuadTo(210f, 366f)
                quadToRelative(3f, -8f, 6f, -15.5f)
                reflectiveQuadToRelative(6f, -16.5f)
                lineTo(84f, 196f)
                quadToRelative(-11f, -11f, -11f, -28f)
                reflectiveQuadToRelative(11f, -28f)
                quadToRelative(11f, -11f, 28f, -11f)
                reflectiveQuadToRelative(28f, 11f)
                lineToRelative(680f, 680f)
                quadToRelative(11f, 11f, 11.5f, 27.5f)
                reflectiveQuadTo(820f, 876f)
                quadToRelative(-11f, 11f, -27.5f, 11.5f)
                reflectiveQuadTo(764f, 877f)
                lineToRelative(-78f, -77f)
                lineTo(260f, 800f)
                close()
                moveTo(864f, 750f)
                lineTo(322f, 209f)
                quadToRelative(35f, -24f, 74.5f, -36.5f)
                reflectiveQuadTo(480f, 160f)
                quadToRelative(117f, 0f, 198.5f, 81.5f)
                reflectiveQuadTo(760f, 440f)
                quadToRelative(69f, 8f, 114.5f, 59.5f)
                reflectiveQuadTo(920f, 620f)
                quadToRelative(0f, 39f, -15f, 72.5f)
                reflectiveQuadTo(864f, 750f)
                close()
            }
        }.build()

        return _CloudOff!!
    }

@Suppress("ObjectPropertyName")
private var _CloudOff: ImageVector? = null
