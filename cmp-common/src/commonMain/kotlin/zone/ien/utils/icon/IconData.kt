package zone.ien.utils.icon

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import zone.ien.utils.icon.IconData.Paint
import zone.ien.utils.icon.IconData.Vector

sealed interface IconData {
    data class Vector(val imageVector: ImageVector): IconData
    data class Paint(val painter: Painter): IconData

    companion object
}

@Composable
fun ComplexIcon(
    icon: IconData,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    when (icon) {
        is Vector -> {
            androidx.compose.material3.Icon(
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint
            )
        }
        is Paint -> {
            androidx.compose.material3.Icon(
                painter = icon.painter,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint
            )
        }
    }
}

