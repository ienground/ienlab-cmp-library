package zone.ien.utils.example

import androidx.compose.runtime.Composable
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.example.ui.screens.playground.HapticFeedbackTestScreen

actual val currentTheme: Theme = Theme.Material3
actual val isIos: Boolean = false

@Composable
actual fun HapticTestScreen() {
    HapticFeedbackTestScreen(navigateBack = {})
}
