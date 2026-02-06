package zone.ien.utils.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import zone.ien.utils.icon.material.MaterialIcons

actual object LocalButtonProviderDefault {
    actual val BackIcon @Composable get() = MaterialIcons.ArrowBack
    actual val CloseIcon @Composable get() = MaterialIcons.Close
}