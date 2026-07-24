package zone.ien.utils.icon.remix.fill

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Fill.Check: ImageVector
    get() {
        if (_CheckFill != null) {
            return _CheckFill!!
        }
        _CheckFill = ImageVector.Builder(
            name = "CheckFill",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(10f, 15.171f)
                lineTo(19.192f, 5.979f)
                lineTo(20.606f, 7.393f)
                lineTo(10f, 17.999f)
                lineTo(3.636f, 11.635f)
                lineTo(5.05f, 10.221f)
                lineTo(10f, 15.171f)
                close()
            }
        }.build()

        return _CheckFill!!
    }

@Suppress("ObjectPropertyName")
private var _CheckFill: ImageVector? = null
