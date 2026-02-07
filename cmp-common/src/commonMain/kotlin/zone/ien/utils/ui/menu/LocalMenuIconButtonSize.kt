package zone.ien.utils.ui.menu

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalMenuIconButtonSize: ProvidableCompositionLocal<Pair<Dp, Dp>> = staticCompositionLocalOf { Pair(40.dp, 42.dp) }