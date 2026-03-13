package zone.ien.utils.icon

import androidx.compose.runtime.Composable
import zone.ien.utils.icon.material.MaterialIcons

actual object LocalButtonProviderDefault {
    actual val BackIcon: IconData @Composable get() = IconData.Vector(MaterialIcons.ArrowBack)
    actual val CloseIcon: IconData @Composable get() = IconData.Vector(MaterialIcons.Close)
}