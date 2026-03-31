package zone.ien.utils.icon

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector

enum class IconStyle {
    Filled, Rounded, Sharp
}
val LocalIconStyle: ProvidableCompositionLocal<IconStyle> = staticCompositionLocalOf { IconStyle.Filled }
val LocalBackButtonIcon: ProvidableCompositionLocal<IconData?> = staticCompositionLocalOf { null }
val LocalCloseButtonIcon: ProvidableCompositionLocal<IconData?> = staticCompositionLocalOf { null }