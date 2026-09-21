package zone.ien.utils.ui.list

import androidx.compose.runtime.Composable

private object NoOpIenSwipeBoxHapticFeedback : IenSwipeBoxHapticFeedback {
    override fun performImpactLight() = Unit
}

@Composable
internal actual fun rememberIenSwipeBoxHapticFeedback(): IenSwipeBoxHapticFeedback =
    NoOpIenSwipeBoxHapticFeedback
