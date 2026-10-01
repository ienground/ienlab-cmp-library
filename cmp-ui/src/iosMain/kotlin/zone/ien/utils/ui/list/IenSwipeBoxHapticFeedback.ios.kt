package zone.ien.utils.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

private class IosIenSwipeBoxHapticFeedback(
    private val generator: UIImpactFeedbackGenerator,
) : IenSwipeBoxHapticFeedback {
    override fun performImpactLight() {
        generator.prepare()
        generator.impactOccurred()
    }
}

@Composable
internal actual fun rememberIenSwipeBoxHapticFeedback(): IenSwipeBoxHapticFeedback = remember {
    IosIenSwipeBoxHapticFeedback(
        generator = UIImpactFeedbackGenerator(
            UIImpactFeedbackStyle.UIImpactFeedbackStyleLight,
        ),
    )
}
