package zone.ien.utils.icon.tabler.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.tabler.TablerIcons

val TablerIcons.Line.Check: ImageVector
    get() {
        if (_Check != null) {
            return _Check!!
        }
        _Check = ImageVector.Builder(
            name = "Line.Check",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 12f)
                lineToRelative(5f, 5f)
                lineToRelative(10f, -10f)
            }
        }.build()

        return _Check!!
    }

@Suppress("ObjectPropertyName")
private var _Check: ImageVector? = null
