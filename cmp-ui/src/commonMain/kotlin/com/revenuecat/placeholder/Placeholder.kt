package com.revenuecat.placeholder

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

class PlaceholderHighlight internal constructor()

object PlaceholderDefaults {
    val fade: PlaceholderHighlight = PlaceholderHighlight()
}

@Composable
fun Modifier.placeholder(
    enabled: Boolean = true,
    color: Color = Color.Gray.copy(alpha = 0.35f),
    shape: Shape,
    highlight: PlaceholderHighlight? = PlaceholderDefaults.fade,
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
