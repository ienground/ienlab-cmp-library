package zone.ien.utils.icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector

enum class IconStyle {
    Filled, Rounded, Sharp
}
val LocalIconStyle: ProvidableCompositionLocal<IconStyle> = staticCompositionLocalOf { IconStyle.Filled }
val LocalBackButtonIcon: ProvidableCompositionLocal<ImageVector?> = staticCompositionLocalOf { null }
val LocalCloseButtonIcon: ProvidableCompositionLocal<ImageVector?> = staticCompositionLocalOf { null }