package zone.ien.utils.icon.remix.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Line.ArrowDropRight: ImageVector
    get() {
        if (_ArrowDropRightLine != null) {
            return _ArrowDropRightLine!!
        }
        _ArrowDropRightLine = ImageVector.Builder(
            name = "ArrowDropRightLine",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12.172f, 12f)
                lineTo(9.343f, 9.172f)
                lineTo(10.757f, 7.758f)
                lineTo(15f, 12f)
                lineTo(10.757f, 16.243f)
                lineTo(9.343f, 14.829f)
                lineTo(12.172f, 12f)
                close()
            }
        }.build()

        return _ArrowDropRightLine!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowDropRightLine: ImageVector? = null
