package zone.ien.utils.adaptive.shimmer

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Shape
import com.revenuecat.placeholder.PlaceholderDefaults
import com.revenuecat.placeholder.PlaceholderHighlight
import com.revenuecat.placeholder.placeholder
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.adaptiveComponent
import zone.ien.hig.adaptive.currentTheme
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.shimmer.LocalIenShimmerShape
import kotlin.math.PI
import kotlin.math.sin

/**
 * 플랫폼에 따라 다른 Placeholder 구현을 제공하는 확장 함수입니다.
 * 기본 색상과 모서리는 IenSkeleton의 Grey 배경 및 테마의 작은 모서리 반경과 일치합니다.
 *
 * @param enabled Placeholder가 활성화되어 있는지 여부
 * @param color Placeholder 색상
 * @param highlight 하이라이트 효과. 기본값은 IenSkeleton과 같은 밝기 펄스입니다.
 * @param placeholderFadeTransitionSpec Placeholder 페이드 전환 애니메이션 스펙
 * @param contentFadeTransitionSpec 콘텐츠 페이드 전환 애니메이션 스펙
 * @param adaptation 플랫폼별 어댑테이션 설정
 * @return Modifier
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun Modifier.adaptivePlaceholder(
    enabled: Boolean = true,
    color: Color = IenTheme.colors.surfaceVariant,
    highlight: PlaceholderHighlight? = PlaceholderDefaults.fade,
    placeholderFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() },
    contentFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() },
    adaptation: AdaptationScope<PlatformPlaceholderAdaptation, PlatformPlaceholderAdaptation>.() -> Unit = {{}}
): Modifier {
    val usesDefaultHighlight = highlight === PlaceholderDefaults.fade
    val placeholderColor = if (usesDefaultHighlight) {
        color.copy(alpha = color.alpha * IenSkeletonMinimumAlpha)
    } else {
        color
    }
    val resolvedHighlight = remember(color, highlight) {
        if (usesDefaultHighlight) IenSkeletonPlaceholderHighlight(color) else highlight
    }

    return adaptiveComponent(
        adaptation = remember { PlaceholderAdaptation() },
        adaptationScope = adaptation,
        material = {
            this.placeholder(
                enabled = enabled,
                color = placeholderColor,
                shape = it.shape,
                highlight = resolvedHighlight,
                placeholderFadeTransitionSpec = placeholderFadeTransitionSpec,
                contentFadeTransitionSpec = contentFadeTransitionSpec
            )
        },
        cupertino = {
            this.placeholder(
                enabled = enabled,
                color = placeholderColor,
                shape = it.shape,
                highlight = resolvedHighlight,
                placeholderFadeTransitionSpec = placeholderFadeTransitionSpec,
                contentFadeTransitionSpec = contentFadeTransitionSpec
            )
        }
    )
}

private const val IenSkeletonMotionDurationMillis = 1200
private const val IenSkeletonMinimumAlpha = 0.2f

private class IenSkeletonPlaceholderHighlight(
    private val color: Color
) : PlaceholderHighlight {
    override val animationSpec = infiniteRepeatable(
        animation = tween<Float>(
            durationMillis = IenSkeletonMotionDurationMillis,
            easing = LinearEasing
        )
    )

    override fun brush(progress: Float, size: Size): Brush = SolidColor(color.copy(alpha = 1f))

    override fun alpha(progress: Float): Float {
        val phase = progress * (PI * 2.0).toFloat()
        val skeletonAlpha = IenSkeletonMinimumAlpha +
            (1f - IenSkeletonMinimumAlpha) * ((sin(phase) + 1f) / 2f)
        val baseAlpha = color.alpha * IenSkeletonMinimumAlpha
        val targetAlpha = color.alpha * skeletonAlpha

        return ((targetAlpha - baseAlpha) / (1f - baseAlpha)).coerceIn(0f, 1f)
    }
}

/**
 * 플랫폼별 Placeholder 어댑테이션 구현 클래스
 *
 * @param shape Placeholder에 적용할 모양
 */
class PlatformPlaceholderAdaptation internal constructor(
    shape: Shape
) {
    var shape by mutableStateOf(shape)
}

@OptIn(ExperimentalAdaptiveApi::class)
private class PlaceholderAdaptation: Adaptation<PlatformPlaceholderAdaptation, PlatformPlaceholderAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): PlatformPlaceholderAdaptation {
        val shape = LocalHigShimmerShape.current

        return remember(shape) {
            PlatformPlaceholderAdaptation(
                shape = shape
            )
        }
    }

    /**
     * Material 플랫폼용 Placeholder 어댑테이션 생성
     * 
     * @return PlatformPlaceholderAdaptation 인스턴스
     */
    @Composable
    override fun rememberMaterialAdaptation(): PlatformPlaceholderAdaptation {
        val shape = LocalIenShimmerShape.current

        return remember(shape) {
            PlatformPlaceholderAdaptation(
                shape = shape
            )
        }
    }
}
