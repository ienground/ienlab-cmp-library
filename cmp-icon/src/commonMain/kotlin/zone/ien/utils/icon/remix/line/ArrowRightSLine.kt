package zone.ien.utils.icon.remix.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Line.ArrowRightS: ImageVector
    get() {
        if (_ArrowRightSLine != null) {
            return _ArrowRightSLine!!
        }
        _ArrowRightSLine = ImageVector.Builder(
            name = "ArrowRightSLine",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(13.172f, 12.001f)
                lineTo(8.222f, 7.051f)
                lineTo(9.636f, 5.637f)
                lineTo(16f, 12.001f)
                lineTo(9.636f, 18.365f)
                lineTo(8.222f, 16.95f)
                lineTo(13.172f, 12.001f)
                close()
            }
        }.build()

        return _ArrowRightSLine!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowRightSLine: ImageVector? = null
