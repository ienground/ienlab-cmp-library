package zone.ien.utils.icon

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

enum class IconStyle {
    Filled, Rounded, Sharp
}
val LocalIconStyle: ProvidableCompositionLocal<IconStyle> = staticCompositionLocalOf { IconStyle.Filled }