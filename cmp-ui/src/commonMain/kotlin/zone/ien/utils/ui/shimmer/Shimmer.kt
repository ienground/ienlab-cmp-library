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