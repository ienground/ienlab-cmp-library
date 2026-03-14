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
    adaptation: AdaptationScope<PlatformPlaceholderAdaptation, PlatformPlaceholderAdaptation>.() -> Unit
): Modifier {
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
    adaptation: AdaptationScope<PlatformPlaceholderAdaptation, PlatformPlaceholderAdaptation>.() -> Unit
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