package zone.ien.utils.ui.shimmer

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
 * @param placeholderFadeTransitionSpec PlaceHolder의 페이스 전환 애니메이션 스펙
 * @param contentFadeTransitionSpec 컴포저블 내용의 페이스 전환 애니메이션 스펙
 * @return PlaceHolder가 적용된 Modifier
 */
@Composable
fun Modifier.m3Placeholder(
    enabled: Boolean = true,
    color: Color = Color.Gray.copy(alpha = 0.35f),
    shape: Shape = LocalM3ShimmerShape.current,
    highlight: PlaceholderHighlight? = PlaceholderDefaults.fade,
    placeholderFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() },
    contentFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() }
) = this.placeholder(
    enabled = enabled,
    color = color,
    shape = shape,
    highlight = highlight,
    placeholderFadeTransitionSpec = placeholderFadeTransitionSpec,
    contentFadeTransitionSpec = contentFadeTransitionSpec
)

/**
 * Material3 스타일의 쉐이머 텍스트 컴포저블
 * 
 * 이 컴포저블은 텍스트에 쉐이머 효과를 적용하여 로딩 상태를 표시합니다.
 * 실제 텍스트는 공백으로 표시되며, PlaceHolder가 적용되어 사용자에게 로딩 상태를 보여줍니다.
 * 
 * @param modifier 텍스트에 적용할 Modifier
 * @param enabled 쉐이머 효과를 활성화할지 여부
 * @param fontSize 텍스트의 글꼴 크기
 * @param fontStyle 텍스트의 스타일 (Italic 등)
 * @param fontWeight 텍스트의 두께
 * @param fontFamily 텍스트의 글꼴
 * @param letterSpacing 글자 간격
 * @param textDecoration 텍스트 장식 (밑줄 등)
 * @param textAlign 텍스트 정렬
 * @param lineHeight 줄 높이
 * @param overflow 텍스트가 넘칠 경우 처리 방식
 * @param softWrap 줄 바꿈 여부
 * @param style 텍스트 스타일
 * @param color 텍스트 색상
 * @param shape 쉐이머 효과의 모양
 * @param highlight 쉐이머 효과의 하이라이트 효과
 * @param placeholderFadeTransitionSpec PlaceHolder의 페이스 전환 애니메이션 스펙
 * @param contentFadeTransitionSpec 컴포저블 내용의 페이스 전환 애니메이션 스펙
 */
@Composable
fun M3TextShimmer(
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
    shape: Shape = LocalM3ShimmerShape.current,
    highlight: PlaceholderHighlight? = PlaceholderDefaults.fade,
    placeholderFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() },
    contentFadeTransitionSpec: () -> FiniteAnimationSpec<Float> = { spring() }
) {
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
                .placeholder(
                    enabled = enabled,
                    color = color,
                    shape = shape,
                    highlight = highlight,
                    placeholderFadeTransitionSpec = placeholderFadeTransitionSpec,
                    contentFadeTransitionSpec = contentFadeTransitionSpec
                )
        )
    }
}