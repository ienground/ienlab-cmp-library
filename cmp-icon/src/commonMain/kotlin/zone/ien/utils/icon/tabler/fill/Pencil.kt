package zone.ien.utils.icon.tabler.fill

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.tabler.TablerIcons

val TablerIcons.Fill.Pencil: ImageVector
    get() {
        if (_Pencil != null) {
            return _Pencil!!
        }
        _Pencil = ImageVector.Builder(
            name = "Fill.Pencil",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12.085f, 6.5f)
                lineToRelative(5.415f, 5.415f)
                lineToRelative(-8.793f, 8.792f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.707f, 0.293f)
                horizontalLineToRelative(-4f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, -1f)
                verticalLineToRelative(-4f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.293f, -0.707f)
                close()
                moveTo(17.491f, 3.802f)
                arcToRelative(3.828f, 3.828f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.716f, 6.405f)
                lineToRelative(-0.292f, 0.293f)
                lineToRelative(-5.415f, -5.415f)
                lineToRelative(0.293f, -0.292f)
                arcToRelative(3.83f, 3.83f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.698f, -0.991f)
            }
        }.build()

        return _Pencil!!
    }

@Suppress("ObjectPropertyName")
private var _Pencil: ImageVector? = null
