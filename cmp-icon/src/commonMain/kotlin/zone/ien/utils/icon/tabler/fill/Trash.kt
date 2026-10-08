package zone.ien.utils.icon.tabler.fill

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.tabler.TablerIcons

val TablerIcons.Fill.Trash: ImageVector
    get() {
        if (_Trash != null) {
            return _Trash!!
        }
        _Trash = ImageVector.Builder(
            name = "Fill.Trash",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(20f, 6f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.117f, 1.993f)
                lineToRelative(-0.117f, 0.007f)
                horizontalLineToRelative(-0.081f)
                lineToRelative(-0.919f, 11f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.824f, 2.995f)
                lineToRelative(-0.176f, 0.005f)
                horizontalLineToRelative(-8f)
                curveToRelative(-1.598f, 0f, -2.904f, -1.249f, -2.992f, -2.75f)
                lineToRelative(-0.005f, -0.167f)
                lineToRelative(-0.923f, -11.083f)
                horizontalLineToRelative(-0.08f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.117f, -1.993f)
                lineToRelative(0.117f, -0.007f)
                close()
                moveTo(10f, 10f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, 1f)
                verticalLineToRelative(6f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 0f)
                verticalLineToRelative(-6f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, -1f)
                moveToRelative(4f, 0f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, 1f)
                verticalLineToRelative(6f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 0f)
                verticalLineToRelative(-6f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, -1f)
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(14f, 2f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 2f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.993f, 0.117f)
                lineToRelative(-0.007f, -0.117f)
                horizontalLineToRelative(-4f)
                lineToRelative(-0.007f, 0.117f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.993f, -0.117f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.85f, -1.995f)
                lineToRelative(0.15f, -0.005f)
                close()
            }
        }.build()

        return _Trash!!
    }

@Suppress("ObjectPropertyName")
private var _Trash: ImageVector? = null
