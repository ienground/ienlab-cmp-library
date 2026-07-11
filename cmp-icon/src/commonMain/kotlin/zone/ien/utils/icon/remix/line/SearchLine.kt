package zone.ien.utils.icon.remix.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Line.Search: ImageVector
    get() {
        if (_SearchLine != null) {
            return _SearchLine!!
        }
        _SearchLine = ImageVector.Builder(
            name = "SearchLine",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(18.031f, 16.617f)
                lineTo(22.314f, 20.899f)
                lineTo(20.899f, 22.314f)
                lineTo(16.617f, 18.031f)
                curveTo(15.077f, 19.263f, 13.124f, 20f, 11f, 20f)
                curveTo(6.032f, 20f, 2f, 15.968f, 2f, 11f)
                curveTo(2f, 6.032f, 6.032f, 2f, 11f, 2f)
                curveTo(15.968f, 2f, 20f, 6.032f, 20f, 11f)
                curveTo(20f, 13.124f, 19.263f, 15.077f, 18.031f, 16.617f)
                close()
                moveTo(16.025f, 15.875f)
                curveTo(17.247f, 14.615f, 18f, 12.896f, 18f, 11f)
                curveTo(18f, 7.133f, 14.868f, 4f, 11f, 4f)
                curveTo(7.133f, 4f, 4f, 7.133f, 4f, 11f)
                curveTo(4f, 14.868f, 7.133f, 18f, 11f, 18f)
                curveTo(12.896f, 18f, 14.615f, 17.247f, 15.875f, 16.025f)
                lineTo(16.025f, 15.875f)
                close()
            }
        }.build()

        return _SearchLine!!
    }

@Suppress("ObjectPropertyName")
private var _SearchLine: ImageVector? = null
