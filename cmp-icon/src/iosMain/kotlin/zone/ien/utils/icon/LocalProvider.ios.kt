package zone.ien.utils.icon

import androidx.compose.runtime.Composable
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.utils.icon.hig.ChevronLeft
import zone.ien.utils.icon.hig.HigSystemIcons
import zone.ien.utils.icon.hig.Xmark

actual object LocalButtonProviderDefault {
    actual val BackIcon: IconData @Composable get() = IconData.Paint(
        AdaptiveIcons.painter(
            material = { HigSystemIcons.ChevronLeft },
            cupertino = { "chevron.left" }
        )
    )
    actual val CloseIcon: IconData @Composable get() = IconData.Paint(
        AdaptiveIcons.painter(
            material = { HigSystemIcons.Xmark },
            cupertino = { "xmark" }
        )
    )
}