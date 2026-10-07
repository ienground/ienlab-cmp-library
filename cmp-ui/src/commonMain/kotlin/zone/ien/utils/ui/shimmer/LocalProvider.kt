package zone.ien.utils.ui.shimmer

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.ui.graphics.Shape
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.LocalIenTokens

/**
 * Material3 스타일의 쉐이머 모양에 대한 CompositionLocal
 * 
 * 이 Local은 쉐이머 효과의 기본 모양을 제공합니다.
 * 기본값은 IenSkeleton과 같은 테마의 작은 모서리 반경을 적용한 연속 곡선 모양입니다.
 * 별도로 제공한 모양은 기본값보다 우선합니다.
 */
val LocalIenShimmerShape: ProvidableCompositionLocal<Shape> = compositionLocalWithComputedDefaultOf {
    ContinuousRoundedRectangle(LocalIenTokens.currentValue.radius.sm)
}
