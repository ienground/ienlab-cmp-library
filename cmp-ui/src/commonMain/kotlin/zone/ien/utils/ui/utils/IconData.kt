package zone.ien.utils.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface IconData {
    data class Vector(val imageVector: ImageVector): IconData
    data class Paint(val painter: Painter): IconData
}