package zone.ien.utils.ui.screen

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

val LocalIsScrollTint: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { true }
val LocalM3TopBarSize: ProvidableCompositionLocal<TopBarSize> = staticCompositionLocalOf { TopBarSize.Small }
val LocalHigShowNavTitle: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { false }
val LocalIsM3TopBarCenterAligned: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { false }
val LocalIsHigTopBarCenterAligned: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { true }