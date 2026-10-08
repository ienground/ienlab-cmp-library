package zone.ien.utils.icon.material.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons

val M3SystemIcons.Filled.ChevronRight: ImageVector
    get() {
        if (_ChevronRight != null) {
            return _ChevronRight!!
        }
        _ChevronRight = ImageVector.Builder(
            name = "Filled.ChevronRight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFF1F1F1F))) {
                moveTo(504f, 480f)
                lineTo(320f, 296f)
                lineToRelative(56f, -56f)
                lineToRelative(240f, 240f)
                lineToRelative(-240f, 240f)
                lineToRelative(-56f, -56f)
                lineToRelative(184f, -184f)
                close()
            }
        }.build()

        return _ChevronRight!!
    }

@Suppress("ObjectPropertyName")
private var _ChevronRight: ImageVector? = null
