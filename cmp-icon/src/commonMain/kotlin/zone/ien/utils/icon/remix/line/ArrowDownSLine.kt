package zone.ien.utils.icon.remix.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Line.ArrowDownS: ImageVector
    get() {
        if (_ArrowDownSLine != null) {
            return _ArrowDownSLine!!
        }
        _ArrowDownSLine = ImageVector.Builder(
            name = "ArrowDownSLine",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12f, 13.171f)
                lineTo(16.95f, 8.222f)
                lineTo(18.364f, 9.636f)
                lineTo(12f, 16f)
                lineTo(5.636f, 9.636f)
                lineTo(7.05f, 8.222f)
                lineTo(12f, 13.171f)
                close()
            }
        }.build()

        return _ArrowDownSLine!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowDownSLine: ImageVector? = null
