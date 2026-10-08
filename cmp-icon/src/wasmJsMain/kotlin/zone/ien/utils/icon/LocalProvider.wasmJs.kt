package zone.ien.utils.icon

import androidx.compose.runtime.Composable
import zone.ien.utils.icon.SystemIcons

actual object LocalButtonProviderDefault {
    actual val BackIcon: IconData @Composable get() = IconData.Vector(SystemIcons.ArrowBack)
    actual val CloseIcon: IconData @Composable get() = IconData.Vector(SystemIcons.Close)
}
