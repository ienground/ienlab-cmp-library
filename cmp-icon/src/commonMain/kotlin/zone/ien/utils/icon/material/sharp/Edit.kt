package zone.ien.utils.icon.material.sharp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcon

val M3SystemIcon.Sharp.Edit: ImageVector
    get() {
        if (_Edit != null) {
            return _Edit!!
        }
        _Edit = ImageVector.Builder(
            name = "Edit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(120f, 840f)
                verticalLineToRelative(-170f)
                lineToRelative(585f, -583f)
                lineToRelative(167f, 171f)
                lineToRelative(-582f, 582f)
                lineTo(120f, 840f)
                close()
                moveTo(704f, 312f)
                lineTo(760f, 256f)
                lineTo(704f, 200f)
                lineTo(648f, 256f)
                lineTo(704f, 312f)
                close()
            }
        }.build()

        return _Edit!!
    }

@Suppress("ObjectPropertyName")
private var _Edit: ImageVector? = null
