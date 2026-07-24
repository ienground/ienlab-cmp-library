package zone.ien.utils.icon.remix.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons

val RemixIcons.Line.ArrowDownWide: ImageVector
    get() {
        if (_ArrowDownWideLine != null) {
            return _ArrowDownWideLine!!
        }
        _ArrowDownWideLine = ImageVector.Builder(
            name = "ArrowDownWideLine",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12f, 15.632f)
                lineTo(20.968f, 10.884f)
                lineTo(20.032f, 9.116f)
                lineTo(12f, 13.368f)
                lineTo(3.968f, 9.116f)
                lineTo(3.032f, 10.884f)
                lineTo(12f, 15.632f)
                close()
            }
        }.build()

        return _ArrowDownWideLine!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowDownWideLine: ImageVector? = null
