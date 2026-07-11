package zone.ien.utils.icon.remix.fill

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Fill.StarHalf: ImageVector
    get() {
        if (_StarHalfFill != null) {
            return _StarHalfFill!!
        }
        _StarHalfFill = ImageVector.Builder(
            name = "StarHalfFill",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12.001f, 15.968f)
                lineTo(16.247f, 18.345f)
                lineTo(15.299f, 13.572f)
                lineTo(18.872f, 10.267f)
                lineTo(14.039f, 9.694f)
                lineTo(12.001f, 5.275f)
                verticalLineTo(15.968f)
                close()
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
            }
        }.build()

        return _StarHalfFill!!
    }

@Suppress("ObjectPropertyName")
private var _StarHalfFill: ImageVector? = null
