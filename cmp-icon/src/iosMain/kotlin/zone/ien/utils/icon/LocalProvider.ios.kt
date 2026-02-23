package zone.ien.utils.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import zone.ien.utils.icon.hig.ChevronLeft
import zone.ien.utils.icon.hig.HigIcons
import zone.ien.utils.icon.hig.Xmark
import zone.ien.utils.icon.material.MaterialIcons

actual object LocalButtonProviderDefault {
    actual val BackIcon @Composable get() = HigIcons.ChevronLeft
    actual val CloseIcon @Composable get() = HigIcons.Xmark
}