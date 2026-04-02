package zone.ien.utils.icon.material.sharp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcon

val M3SystemIcon.Sharp.Save: ImageVector
    get() {
        if (_Save != null) {
            return _Save!!
        }
        _Save = ImageVector.Builder(
            name = "Sharp.Save",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFF1F1F1F))) {
                moveTo(840f, 280f)
                verticalLineToRelative(560f)
                lineTo(120f, 840f)
                verticalLineToRelative(-720f)
                horizontalLineToRelative(560f)
                lineToRelative(160f, 160f)
                close()
                moveTo(480f, 720f)
                quadToRelative(50f, 0f, 85f, -35f)
                reflectiveQuadToRelative(35f, -85f)
                quadToRelative(0f, -50f, -35f, -85f)
                reflectiveQuadToRelative(-85f, -35f)
                quadToRelative(-50f, 0f, -85f, 35f)
                reflectiveQuadToRelative(-35f, 85f)
                quadToRelative(0f, 50f, 35f, 85f)
                reflectiveQuadToRelative(85f, 35f)
                close()
                moveTo(240f, 400f)
                horizontalLineToRelative(360f)
                verticalLineToRelative(-160f)
                lineTo(240f, 240f)
                verticalLineToRelative(160f)
                close()
            }
        }.build()

        return _Save!!
    }

@Suppress("ObjectPropertyName")
private var _Save: ImageVector? = null
