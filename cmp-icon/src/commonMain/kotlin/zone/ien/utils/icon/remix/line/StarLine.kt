package zone.ien.utils.icon.remix.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Line.Star: ImageVector
    get() {
        if (_StarLine != null) {
            return _StarLine!!
        }
        _StarLine = ImageVector.Builder(
            name = "StarLine",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12.001f, 18.26f)
                lineTo(4.947f, 22.208f)
                lineTo(6.522f, 14.28f)
                lineTo(0.588f, 8.792f)
                lineTo(8.615f, 7.84f)
                lineTo(12.001f, 0.5f)
                lineTo(15.386f, 7.84f)
                lineTo(23.413f, 8.792f)
                lineTo(17.479f, 14.28f)
                lineTo(19.054f, 22.208f)
                lineTo(12.001f, 18.26f)
                close()
                moveTo(12.001f, 15.968f)
                lineTo(16.247f, 18.345f)
                lineTo(15.299f, 13.572f)
                lineTo(18.872f, 10.267f)
                lineTo(14.039f, 9.694f)
                lineTo(12.001f, 5.275f)
                lineTo(9.962f, 9.694f)
                lineTo(5.129f, 10.267f)
                lineTo(8.702f, 13.572f)
                lineTo(7.754f, 18.345f)
                lineTo(12.001f, 15.968f)
                close()
            }
        }.build()

        return _StarLine!!
    }

@Suppress("ObjectPropertyName")
private var _StarLine: ImageVector? = null
