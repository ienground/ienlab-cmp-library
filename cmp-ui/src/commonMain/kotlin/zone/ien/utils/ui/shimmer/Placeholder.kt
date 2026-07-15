package zone.ien.utils.ui.shimmer

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

class IenPlaceholderHighlight internal constructor()

object IenPlaceholderDefaults {
    val fade: IenPlaceholderHighlight = IenPlaceholderHighlight()
}

@Composable
fun Modifier.ienPlaceholder(
    enabled: Boolean = true,
    color: Color = Color.Gray.copy(alpha = 0.35f),
    shape: Shape,
    highlight: IenPlaceholderHighlight? = IenPlaceholderDefaults.fade,
    placeholderFadeTransitionSpec: () -> FiniteAnimationSpec<Float>,
    contentFadeTransitionSpec: () -> FiniteAnimationSpec<Float>,
): Modifier {
    @Suppress("UNUSED_EXPRESSION")
    highlight
    @Suppress("UNUSED_EXPRESSION")
    placeholderFadeTransitionSpec
    @Suppress("UNUSED_EXPRESSION")
    contentFadeTransitionSpec

    return if (enabled) {
        clip(shape).background(color)
    } else {
        this
    }
}
