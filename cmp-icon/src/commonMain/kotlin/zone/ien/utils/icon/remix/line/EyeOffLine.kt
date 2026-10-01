package zone.ien.utils.icon.remix.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Line.EyeOff: ImageVector
    get() {
        if (_EyeOffLine != null) {
            return _EyeOffLine!!
        }
        _EyeOffLine = ImageVector.Builder(
            name = "EyeOffLine",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(4.412f, 3.004f)
                lineTo(6.03f, 4.622f)
                curveTo(7.76f, 3.89f, 9.77f, 3.5f, 12f, 3.5f)
                curveTo(17.523f, 3.5f, 22f, 7.17f, 23.5f, 12f)
                curveTo(22.83f, 14.15f, 21.59f, 16.03f, 19.96f, 17.47f)
                lineTo(21.996f, 19.506f)
                lineTo(20.582f, 20.92f)
                lineTo(3f, 3.996f)
                lineTo(4.412f, 3.004f)
                close()
                moveTo(18.52f, 16.03f)
                curveTo(19.6f, 14.93f, 20.42f, 13.56f, 20.86f, 12f)
                curveTo(19.28f, 8.4f, 15.9f, 5.5f, 12f, 5.5f)
                curveTo(10.35f, 5.5f, 8.83f, 5.81f, 7.49f, 6.36f)
                lineTo(9.1f, 7.91f)
                curveTo(9.91f, 7.39f, 10.88f, 7.08f, 12f, 7.08f)
                curveTo(14.72f, 7.08f, 16.92f, 9.28f, 16.92f, 12f)
                curveTo(16.92f, 13.12f, 16.61f, 14.09f, 16.09f, 14.9f)
                lineTo(18.52f, 16.03f)
                close()
                moveTo(12f, 18.5f)
                curveTo(13.65f, 18.5f, 15.17f, 18.19f, 16.51f, 17.64f)
                lineTo(14.91f, 16.09f)
                curveTo(14.09f, 16.61f, 13.12f, 16.92f, 12f, 16.92f)
                curveTo(9.28f, 16.92f, 7.08f, 14.72f, 7.08f, 12f)
                curveTo(7.08f, 10.88f, 7.39f, 9.91f, 7.91f, 9.1f)
                lineTo(6.37f, 7.5f)
                curveTo(5.07f, 8.65f, 4.08f, 10.17f, 3.5f, 12f)
                curveTo(4.98f, 16.83f, 8.47f, 18.5f, 12f, 18.5f)
                close()
                moveTo(9.79f, 11.01f)
                lineTo(12.99f, 14.21f)
                curveTo(12.67f, 14.4f, 12.34f, 14.5f, 12f, 14.5f)
                curveTo(10.62f, 14.5f, 9.5f, 13.38f, 9.5f, 12f)
                curveTo(9.5f, 11.66f, 9.6f, 11.33f, 9.79f, 11.01f)
                close()
            }
        }.build()

        return _EyeOffLine!!
    }

@Suppress("ObjectPropertyName")
private var _EyeOffLine: ImageVector? = null
