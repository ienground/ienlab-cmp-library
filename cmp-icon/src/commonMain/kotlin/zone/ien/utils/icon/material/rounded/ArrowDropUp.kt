package zone.ien.utils.icon.material.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons

val M3SystemIcons.Rounded.ArrowDropUp: ImageVector
    get() {
        if (_ArrowDropUp != null) {
            return _ArrowDropUp!!
        }
        _ArrowDropUp = ImageVector.Builder(
            name = "ArrowDropUp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(328f, 560f)
                quadToRelative(-9f, 0f, -14.5f, -6f)
                reflectiveQuadToRelative(-5.5f, -14f)
                quadToRelative(0f, -2f, 6f, -14f)
                lineToRelative(145f, -145f)
                quadToRelative(5f, -5f, 10f, -7f)
                reflectiveQuadToRelative(11f, -2f)
                quadToRelative(6f, 0f, 11f, 2f)
                reflectiveQuadToRelative(10f, 7f)
                lineToRelative(145f, 145f)
                quadToRelative(3f, 3f, 4.5f, 6.5f)
                reflectiveQuadToRelative(1.5f, 7.5f)
                quadToRelative(0f, 8f, -5.5f, 14f)
                reflectiveQuadToRelative(-14.5f, 6f)
                lineTo(328f, 560f)
                close()
            }
        }.build()

        return _ArrowDropUp!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowDropUp: ImageVector? = null
