package zone.ien.utils.adaptive.shimmer

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle

/**
 * HIG (Human Interface Guidelines) Shimmer 모양을 제공하는 CompositionLocal
 * 
 * 이 Local은 Shimmer 효과에 사용될 모양을 정의합니다.
 * 기본값으로는 4dp의 둥근 모서리를 가진 RoundedRectangle이 적용됩니다.
 */
val LocalHigShimmerShape: ProvidableCompositionLocal<Shape> = staticCompositionLocalOf { ContinuousRoundedRectangle(4.dp) }