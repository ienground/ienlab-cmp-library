package zone.ien.utils.icon.material.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.MaterialIcons

val MaterialIcons.Filled.ArrowBack: ImageVector
    get() {
        if (_ArrowBack != null) {
            return _ArrowBack!!
        }
        _ArrowBack = ImageVector.Builder(
            name = "Filled.ArrowBack",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFF1F1F1F))) {
                moveToRelative(313f, 520f)
                lineToRelative(224f, 224f)
                lineToRelative(-57f, 56f)
                lineToRelative(-320f, -320f)
                lineToRelative(320f, -320f)
                lineToRelative(57f, 56f)
                lineToRelative(-224f, 224f)
                horizontalLineToRelative(487f)
                verticalLineToRelative(80f)
                lineTo(313f, 520f)
                close()
            }
        }.build()

        return _ArrowBack!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowBack: ImageVector? = null
