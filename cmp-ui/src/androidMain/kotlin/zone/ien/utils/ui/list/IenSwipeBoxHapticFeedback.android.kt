package zone.ien.utils.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback

private class AndroidIenSwipeBoxHapticFeedback(
    private val hapticFeedback: HapticFeedback,
) : IenSwipeBoxHapticFeedback {
    override fun performImpactLight() {
        hapticFeedback.performHapticFeedback(
            IenSwipeBoxHapticFeedbackType.ImpactLight.androidType,
        )
    }
}

@Composable
internal actual fun rememberIenSwipeBoxHapticFeedback(): IenSwipeBoxHapticFeedback {
    val hapticFeedback = LocalHapticFeedback.current
    return remember(hapticFeedback) {
        AndroidIenSwipeBoxHapticFeedback(hapticFeedback)
    }
}
