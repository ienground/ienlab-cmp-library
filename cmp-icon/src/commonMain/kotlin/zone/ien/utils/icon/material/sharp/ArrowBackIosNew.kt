package zone.ien.utils.icon.material.sharp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.MaterialIcons

val MaterialIcons.Sharp.ArrowBackIosNew: ImageVector
    get() {
        if (_ArrowBackIosNew != null) {
            return _ArrowBackIosNew!!
        }
        _ArrowBackIosNew = ImageVector.Builder(
            name = "ArrowBackIosNew",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(640f, 880f)
                lineTo(240f, 480f)
                lineToRelative(400f, -400f)
                lineToRelative(71f, 71f)
                lineToRelative(-329f, 329f)
                lineToRelative(329f, 329f)
                lineToRelative(-71f, 71f)
                close()
            }
        }.build()

        return _ArrowBackIosNew!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowBackIosNew: ImageVector? = null
