package zone.ien.utils.icon.remix.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Line.Eye: ImageVector
    get() {
        if (_EyeLine != null) {
            return _EyeLine!!
        }
        _EyeLine = ImageVector.Builder(
            name = "EyeLine",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(12f, 4.5f)
                curveTo(7.03f, 4.5f, 2.73f, 7.61f, 1f, 12f)
                curveTo(2.73f, 16.39f, 7.03f, 19.5f, 12f, 19.5f)
                curveTo(16.97f, 19.5f, 21.27f, 16.39f, 23f, 12f)
                curveTo(21.27f, 7.61f, 16.97f, 4.5f, 12f, 4.5f)
                close()
                moveTo(12f, 17.5f)
                curveTo(8.96f, 17.5f, 6.5f, 15.04f, 6.5f, 12f)
                curveTo(6.5f, 8.96f, 8.96f, 6.5f, 12f, 6.5f)
                curveTo(15.04f, 6.5f, 17.5f, 8.96f, 17.5f, 12f)
                curveTo(17.5f, 15.04f, 15.04f, 17.5f, 12f, 17.5f)
                close()
                moveTo(12f, 15.5f)
                curveTo(13.933f, 15.5f, 15.5f, 13.933f, 15.5f, 12f)
                curveTo(15.5f, 10.067f, 13.933f, 8.5f, 12f, 8.5f)
                curveTo(10.067f, 8.5f, 8.5f, 10.067f, 8.5f, 12f)
                curveTo(8.5f, 13.933f, 10.067f, 15.5f, 12f, 15.5f)
                close()
            }
        }.build()

        return _EyeLine!!
    }

@Suppress("ObjectPropertyName")
private var _EyeLine: ImageVector? = null
