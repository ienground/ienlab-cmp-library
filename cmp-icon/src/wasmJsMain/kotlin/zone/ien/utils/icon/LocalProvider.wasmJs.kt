package zone.ien.utils.icon

import androidx.compose.runtime.Composable
import zone.ien.utils.icon.material.M3SystemIcons

actual object LocalButtonProviderDefault {
    actual val BackIcon: IconData @Composable get() = IconData.Vector(M3SystemIcons.ArrowBack)
    actual val CloseIcon: IconData @Composable get() = IconData.Vector(M3SystemIcons.Close)
}
