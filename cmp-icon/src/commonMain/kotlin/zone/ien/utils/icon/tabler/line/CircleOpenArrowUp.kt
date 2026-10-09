package zone.ien.utils.icon.tabler.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.tabler.TablerIcons

val TablerIcons.Line.CircleOpenArrowUp: ImageVector
    get() {
        if (_CircleOpenArrowUp != null) {
            return _CircleOpenArrowUp!!
        }
        _CircleOpenArrowUp = ImageVector.Builder(
            name = "Line.CircleOpenArrowUp",
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
                moveTo(15.998f, 20.066f)
                arcToRelative(9f, 9f, 0f, isMoreThanHalf = true, isPositiveArc = false, -3.998f, 0.934f)
                verticalLineToRelative(-13f)
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(16f, 12f)
                lineToRelative(-4f, -4f)
                lineToRelative(-4f, 4f)
            }
        }.build()

        return _CircleOpenArrowUp!!
    }

@Suppress("ObjectPropertyName")
private var _CircleOpenArrowUp: ImageVector? = null
