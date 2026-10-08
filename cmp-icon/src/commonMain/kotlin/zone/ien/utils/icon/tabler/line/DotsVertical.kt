package zone.ien.utils.icon.tabler.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.tabler.TablerIcons

val TablerIcons.Line.DotsVertical: ImageVector
    get() {
        if (_DotsVertical != null) {
            return _DotsVertical!!
        }
        _DotsVertical = ImageVector.Builder(
            name = "Line.DotsVertical",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(14f, 12f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4f, 0f)
                quadToRelative(0f, -0.053f, 0.005f, -0.102f)
                arcToRelative(1.996f, 1.996f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.995f, -1.898f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 2f)
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(14f, 19f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4f, 0f)
                quadToRelative(0f, -0.052f, 0.005f, -0.102f)
                arcToRelative(1.996f, 1.996f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.995f, -1.898f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 2f)
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(14f, 5f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4f, 0f)
                quadToRelative(0f, -0.053f, 0.005f, -0.102f)
                arcToRelative(1.996f, 1.996f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.995f, -1.898f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 2f)
            }
        }.build()

        return _DotsVertical!!
    }

@Suppress("ObjectPropertyName")
private var _DotsVertical: ImageVector? = null
