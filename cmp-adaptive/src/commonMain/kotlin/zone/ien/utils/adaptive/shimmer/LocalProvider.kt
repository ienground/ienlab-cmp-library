package zone.ien.utils.adaptive.shimmer

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.ui.graphics.Shape
import zone.ien.utils.ui.shimmer.LocalIenShimmerShape

/**
 * HIG (Human Interface Guidelines) Shimmer 모양을 제공하는 CompositionLocal
 * 
 * 이 Local은 Shimmer 효과에 사용될 모양을 정의합니다.
 * 기본값은 IenSkeleton과 같은 모서리를 사용하는 [LocalIenShimmerShape]를 따릅니다.
 * 별도로 제공한 HIG 모양은 기본값보다 우선합니다.
 */
val LocalHigShimmerShape: ProvidableCompositionLocal<Shape> = compositionLocalWithComputedDefaultOf {
    LocalIenShimmerShape.currentValue
}
