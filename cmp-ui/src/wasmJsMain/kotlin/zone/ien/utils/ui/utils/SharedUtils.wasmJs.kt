package zone.ien.utils.ui.utils

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize

actual class IenDecimalFormat {
    actual fun format(number: Double): String = format(number.toLong())

    actual fun format(number: Int): String = format(number.toLong())

    private fun format(number: Long): String {
        val sign = if (number < 0) "-" else ""
        val digits = number.toString().removePrefix("-")
        return sign + digits
            .reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()
    }
}

actual fun Modifier.animateContentSizeWithoutClipping(
    animationSpec: FiniteAnimationSpec<IntSize>,
    finishedListener: ((initialValue: IntSize, targetValue: IntSize) -> Unit)?,
): Modifier = animateContentSize(
    animationSpec = animationSpec,
    finishedListener = finishedListener,
)
