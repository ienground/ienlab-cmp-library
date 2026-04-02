package zone.ien.utils.icon.material.sharp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcon

val M3SystemIcon.Sharp.ArrowDropUp: ImageVector
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
                moveToRelative(280f, 560f)
                lineToRelative(200f, -200f)
                lineToRelative(200f, 200f)
                lineTo(280f, 560f)
                close()
            }
        }.build()

        return _ArrowDropUp!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowDropUp: ImageVector? = null
