package zone.ien.utils.icon.hig

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val zone.ien.utils.icon.hig.HigIcons.Xmark: ImageVector
    get() {
        if (_Xmark != null) {
            return _Xmark!!
        }
        _Xmark = ImageVector.Builder(
            name = "Xmark",
            defaultWidth = 15.847.dp,
            defaultHeight = 15.496.dp,
            viewportWidth = 15.847f,
            viewportHeight = 15.496f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0f,
                strokeAlpha = 0f
            ) {
                moveTo(0f, 0f)
                horizontalLineToRelative(15.847f)
                verticalLineToRelative(15.496f)
                horizontalLineToRelative(-15.847f)
                close()
            }
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.85f
            ) {
                moveTo(13.993f, 0.272f)
                lineTo(0.253f, 14.012f)
                curveTo(-0.079f, 14.344f, -0.089f, 14.911f, 0.253f, 15.243f)
                curveTo(0.594f, 15.575f, 1.161f, 15.575f, 1.493f, 15.243f)
                lineTo(15.233f, 1.503f)
                curveTo(15.565f, 1.171f, 15.575f, 0.604f, 15.233f, 0.272f)
                curveTo(14.891f, -0.07f, 14.335f, -0.079f, 13.993f, 0.272f)
                close()
                moveTo(15.233f, 14.012f)
                lineTo(1.493f, 0.272f)
                curveTo(1.161f, -0.07f, 0.585f, -0.079f, 0.253f, 0.272f)
                curveTo(-0.079f, 0.614f, -0.079f, 1.171f, 0.253f, 1.503f)
                lineTo(13.993f, 15.243f)
                curveTo(14.325f, 15.575f, 14.901f, 15.585f, 15.233f, 15.243f)
                curveTo(15.565f, 14.901f, 15.565f, 14.344f, 15.233f, 14.012f)
                close()
            }
        }.build()

        return _Xmark!!
    }

@Suppress("ObjectPropertyName")
private var _Xmark: ImageVector? = null
