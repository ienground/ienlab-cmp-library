package zone.ien.utils.adaptive.shimmer

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
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
import zone.ien.utils.ui.shimmer.LocalM3ShimmerShape

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun Modifier.adaptivePlaceholder(
    enabled: Boolean = true,
    color: Color = Color.Gray.copy(alpha = 0.35f),
    highlight: PlaceholderHighlight? = PlaceholderDefaults.fade,
    placeholderFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() },
    contentFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() },
    adaptation: AdaptationScope<PlatformPlaceholderAdaptation, PlatformPlaceholderAdaptation>.() -> Unit = {{}}
): Modifier {
    /**
     * 플랫폼에 따라 다른 Placeholder 구현을 제공하는 확장 함수
     * 
     * @param enabled Placeholder가 활성화되어 있는지 여부
     * @param color Placeholder 색상
     * @param highlight 하이라이트 효과
     * @param placeholderFadeTransitionSpec Placeholder 페이드 전환 애니메이션 스펙
     * @param contentFadeTransitionSpec 콘텐츠 페이드 전환 애니메이션 스펙
     * @param adaptation 플랫폼별 어댑테이션 설정
     * @return Modifier
     */
    return adaptiveComponent(
        adaptation = remember { PlaceholderAdaptation() },
        adaptationScope = adaptation,
        material = {
            this.placeholder(
                enabled = enabled,
                color = color,
                shape = it.shape,
                highlight = highlight,
                placeholderFadeTransitionSpec = placeholderFadeTransitionSpec,
                contentFadeTransitionSpec = contentFadeTransitionSpec
            )
        },
        cupertino = {
            this.placeholder(
                enabled = enabled,
                color = color,
                shape = it.shape,
                highlight = highlight,
                placeholderFadeTransitionSpec = placeholderFadeTransitionSpec,
                contentFadeTransitionSpec = contentFadeTransitionSpec
            )
        }
    )
}

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun TextShimmer(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    style: TextStyle? = null,
    color: Color = Color.Gray.copy(alpha = 0.35f),
    highlight: PlaceholderHighlight? = PlaceholderDefaults.fade,
    placeholderFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() },
    contentFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() },
    adaptation: AdaptationScope<PlatformPlaceholderAdaptation, PlatformPlaceholderAdaptation>.() -> Unit = {}
) {
    /**
     * 플랫폼별로 shimmer 효과를 적용한 텍스트 컴포저블
     * 
     * @param modifier 적용할 Modifier
     * @param enabled shimmer가 활성화되어 있는지 여부
     * @param fontSize 텍스트 폰트 크기
     * @param fontStyle 텍스트 스타일
     * @param fontWeight 텍스트 두께
     * @param fontFamily 텍스트 폰트 패밀리
     * @param letterSpacing 글자 간격
     * @param textDecoration 텍스트 장식
     * @param textAlign 텍스트 정렬
     * @param lineHeight 줄 높이
     * @param overflow 텍스트 오버플로우 처리 방식
     * @param softWrap 소프트 워프 설정
     * @param style 텍스트 스타일
     * @param color 텍스트 색상
     * @param highlight 하이라이트 효과
     * @param placeholderFadeTransitionSpec Placeholder 페이드 전환 애니메이션 스펙
     * @param contentFadeTransitionSpec 콘텐츠 페이드 전환 애니메이션 스펙
     * @param adaptation 플랫폼별 어댑테이션 설정
     */
    Box(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = " ",
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            style = style ?: LocalTextStyle.current
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 3.dp)
                .adaptivePlaceholder(
                    enabled = enabled,
                    color = color,
                    highlight = highlight,
                    placeholderFadeTransitionSpec = placeholderFadeTransitionSpec,
                    contentFadeTransitionSpec = contentFadeTransitionSpec,
                    adaptation = adaptation
                )
        )
    }
}

class PlatformPlaceholderAdaptation internal constructor(
    shape: Shape
) {
    /**
     * 플랫폼별 Placeholder 어댑테이션 구현 클래스
     * 
     * @param shape Placeholder에 적용할 모양
     */
    var shape by mutableStateOf(shape)
}

@OptIn(ExperimentalAdaptiveApi::class)
private class PlaceholderAdaptation: Adaptation<PlatformPlaceholderAdaptation, PlatformPlaceholderAdaptation>() {
    /**
     * Cupertino 플랫폼용 Placeholder 어댑테이션 생성
     * 
     * @return PlatformPlaceholderAdaptation 인스턴스
     */
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
        val shape = LocalM3ShimmerShape.current

        return remember(shape) {
            PlatformPlaceholderAdaptation(
                shape = shape
            )
        }
    }
}