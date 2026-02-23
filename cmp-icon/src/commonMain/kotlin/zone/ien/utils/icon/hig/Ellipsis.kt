package zone.ien.utils.icon.hig

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val HigIcons.Ellipsis: ImageVector
    get() {
        if (_Ellipsis != null) {
            return _Ellipsis!!
        }
        _Ellipsis = ImageVector.Builder(
            name = "Ellipsis",
            defaultWidth = 18.584.dp,
            defaultHeight = 3.721.dp,
            viewportWidth = 18.584f,
            viewportHeight = 3.721f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0f,
                strokeAlpha = 0f
            ) {
                moveTo(0f, 0f)
                horizontalLineToRelative(18.584f)
                verticalLineToRelative(3.721f)
                horizontalLineToRelative(-18.584f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                fillAlpha = 0.85f
            ) {
                moveTo(16.367f, 3.701f)
                curveTo(17.393f, 3.701f, 18.223f, 2.881f, 18.223f, 1.855f)
                curveTo(18.223f, 0.83f, 17.393f, 0f, 16.367f, 0f)
                curveTo(15.342f, 0f, 14.512f, 0.83f, 14.512f, 1.855f)
                curveTo(14.512f, 2.881f, 15.342f, 3.701f, 16.367f, 3.701f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                fillAlpha = 0.85f
            ) {
                moveTo(9.111f, 3.701f)
                curveTo(10.137f, 3.701f, 10.957f, 2.881f, 10.957f, 1.855f)
                curveTo(10.957f, 0.83f, 10.137f, 0f, 9.111f, 0f)
                curveTo(8.086f, 0f, 7.256f, 0.83f, 7.256f, 1.855f)
                curveTo(7.256f, 2.881f, 8.086f, 3.701f, 9.111f, 3.701f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                fillAlpha = 0.85f
            ) {
                moveTo(1.855f, 3.701f)
                curveTo(2.881f, 3.701f, 3.701f, 2.881f, 3.701f, 1.855f)
                curveTo(3.701f, 0.83f, 2.881f, 0f, 1.855f, 0f)
                curveTo(0.83f, 0f, 0f, 0.83f, 0f, 1.855f)
                curveTo(0f, 2.881f, 0.83f, 3.701f, 1.855f, 3.701f)
                close()
            }
        }.build()

        return _Ellipsis!!
    }

@Suppress("ObjectPropertyName")
private var _Ellipsis: ImageVector? = null
