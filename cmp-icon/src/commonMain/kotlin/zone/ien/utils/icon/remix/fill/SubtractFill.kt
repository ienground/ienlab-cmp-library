package zone.ien.utils.icon.remix.fill

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Fill.Subtract: ImageVector
    get() {
        if (_SubtractFill != null) {
            return _SubtractFill!!
        }
        _SubtractFill = ImageVector.Builder(
            name = "SubtractFill",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(19f, 11f)
                horizontalLineTo(5f)
                verticalLineTo(13f)
                horizontalLineTo(19f)
                verticalLineTo(11f)
                close()
            }
        }.build()

        return _SubtractFill!!
    }

@Suppress("ObjectPropertyName")
private var _SubtractFill: ImageVector? = null
