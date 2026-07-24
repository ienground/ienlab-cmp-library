package zone.ien.utils.ui.utils

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import zone.ien.utils.utils.DecimalFormat
import zone.ien.utils.utils.ui.animateContentSizeWithoutClipping as utilsAnimateContentSizeWithoutClipping

actual typealias IenDecimalFormat = DecimalFormat

actual fun Modifier.animateContentSizeWithoutClipping(
    animationSpec: FiniteAnimationSpec<IntSize>,
    finishedListener: ((initialValue: IntSize, targetValue: IntSize) -> Unit)?,
): Modifier = utilsAnimateContentSizeWithoutClipping(
    animationSpec = animationSpec,
    finishedListener = finishedListener,
)
