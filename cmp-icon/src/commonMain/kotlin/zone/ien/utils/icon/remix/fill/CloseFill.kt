package zone.ien.utils.icon.remix.fill

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Fill.Close: ImageVector
    get() {
        if (_CloseFill != null) {
            return _CloseFill!!
        }
        _CloseFill = ImageVector.Builder(
            name = "CloseFill",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12f, 10.587f)
                lineTo(16.949f, 5.637f)
                lineTo(18.364f, 7.051f)
                lineTo(13.414f, 12.001f)
                lineTo(18.364f, 16.95f)
                lineTo(16.949f, 18.365f)
                lineTo(12f, 13.415f)
                lineTo(7.05f, 18.365f)
                lineTo(5.636f, 16.95f)
                lineTo(10.585f, 12.001f)
                lineTo(5.636f, 7.051f)
                lineTo(7.05f, 5.637f)
                lineTo(12f, 10.587f)
                close()
            }
        }.build()

        return _CloseFill!!
    }

@Suppress("ObjectPropertyName")
private var _CloseFill: ImageVector? = null
