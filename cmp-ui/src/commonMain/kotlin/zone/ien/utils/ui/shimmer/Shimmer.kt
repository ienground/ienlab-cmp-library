package zone.ien.utils.ui.shimmer

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * Material3 스타일의 PlaceHolder를 적용하는 Modifier
 * 
 * 이 Modifier는 컴포저블 요소에 쉐이머(-placeholder) 효과를 적용합니다.
 * 일반적으로 로딩 상태나 데이터가 아직 준비되지 않은 경우 표시됩니다.
 * 
 * @param enabled PlaceHolder를 활성화할지 여부
 * @param color PlaceHolder의 색상
 * @param shape PlaceHolder의 모양
 * @param highlight PlaceHolder의 하이라이트 효과
 * @param placeholderFadeTransitionSpec PlaceHolder의 페이드 전환 애니메이션 스펙
 * @param contentFadeTransitionSpec 컴포저블 내용의 페이드 전환 애니메이션 스펙
 * @return PlaceHolder가 적용된 Modifier
 */
@Composable
fun Modifier.m3Placeholder(
    enabled: Boolean = true,
    color: Color = Color.Gray.copy(alpha = 0.35f),
    shape: Shape = LocalIenShimmerShape.current,
    highlight: IenPlaceholderHighlight? = IenPlaceholderDefaults.fade,
    placeholderFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() },
    contentFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() }
) = this.ienPlaceholder(
    enabled = enabled,
    color = color,
    shape = shape,
    highlight = highlight,
    placeholderFadeTransitionSpec = placeholderFadeTransitionSpec,
    contentFadeTransitionSpec = contentFadeTransitionSpec
)
