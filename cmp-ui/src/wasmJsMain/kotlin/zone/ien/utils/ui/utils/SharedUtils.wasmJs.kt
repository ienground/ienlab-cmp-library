package zone.ien.utils.ui.utils

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize

actual fun Modifier.animateContentSizeWithoutClipping(
    animationSpec: FiniteAnimationSpec<IntSize>,
    finishedListener: ((initialValue: IntSize, targetValue: IntSize) -> Unit)?,
): Modifier = animateContentSize(
    animationSpec = animationSpec,
    finishedListener = finishedListener,
)
