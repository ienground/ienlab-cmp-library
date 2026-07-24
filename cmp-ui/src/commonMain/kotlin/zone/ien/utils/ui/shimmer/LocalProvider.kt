package zone.ien.utils.ui.shimmer

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle

/**
 * Material3 스타일의 쉐이머 모양에 대한 CompositionLocal
 * 
 * 이 Local은 쉐이머 효과의 기본 모양을 제공합니다.
 * 기본값으로는 4.dp의 라운드 코너 모양이 적용됩니다.
 */
val LocalIenShimmerShape: ProvidableCompositionLocal<Shape> = staticCompositionLocalOf { ContinuousRoundedRectangle(4.dp) }