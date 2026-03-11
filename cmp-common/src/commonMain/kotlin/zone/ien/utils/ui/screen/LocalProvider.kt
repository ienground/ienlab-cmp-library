package zone.ien.utils.ui.screen

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

val LocalIsScrollTint: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { true }
val LocalTopAppBarSize: ProvidableCompositionLocal<TopAppBarSize> = staticCompositionLocalOf { TopAppBarSize.Small }
val LocalIsM3TopBarCenterAligned: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { false }
val LocalIsHigTopBarCenterAligned: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { true }