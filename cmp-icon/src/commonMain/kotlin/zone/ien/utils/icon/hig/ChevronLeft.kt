package zone.ien.utils.icon.hig

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val HigSystemIcons.ChevronLeft: ImageVector
    get() {
        if (_ChevronLeft != null) {
            return _ChevronLeft!!
        }
        _ChevronLeft = ImageVector.Builder(
            name = "ChevronLeft",
            defaultWidth = 12.393.dp,
            defaultHeight = 16.963.dp,
            viewportWidth = 12.393f,
            viewportHeight = 16.963f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0f,
                strokeAlpha = 0f
            ) {
                moveTo(0f, 0f)
                horizontalLineToRelative(12.393f)
                verticalLineToRelative(16.963f)
                horizontalLineToRelative(-12.393f)
                close()
            }
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.85f
            ) {
                moveTo(0f, 8.477f)
                curveTo(0f, 8.721f, 0.088f, 8.936f, 0.273f, 9.121f)
                lineTo(8.018f, 16.69f)
                curveTo(8.184f, 16.865f, 8.398f, 16.953f, 8.652f, 16.953f)
                curveTo(9.16f, 16.953f, 9.551f, 16.572f, 9.551f, 16.065f)
                curveTo(9.551f, 15.811f, 9.443f, 15.596f, 9.287f, 15.43f)
                lineTo(2.178f, 8.477f)
                lineTo(9.287f, 1.523f)
                curveTo(9.443f, 1.357f, 9.551f, 1.133f, 9.551f, 0.889f)
                curveTo(9.551f, 0.381f, 9.16f, 0f, 8.652f, 0f)
                curveTo(8.398f, 0f, 8.184f, 0.088f, 8.018f, 0.254f)
                lineTo(0.273f, 7.832f)
                curveTo(0.088f, 8.008f, 0f, 8.232f, 0f, 8.477f)
                close()
            }
        }.build()

        return _ChevronLeft!!
    }

@Suppress("ObjectPropertyName")
private var _ChevronLeft: ImageVector? = null
