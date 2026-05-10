package zone.ien.utils.ui.menu

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 메뉴 아이콘 버튼의 크기를 정의하는 CompositionLocal
 *
 * 이 CompositionLocal은 메뉴 아이콘 버튼의 너비와 높이를 나타내는 Dp 값의 쌍을 제공합니다.
 */
val LocalMenuIconButtonSize: ProvidableCompositionLocal<Pair<Dp, Dp>> = staticCompositionLocalOf { Pair(40.dp, 42.dp) }