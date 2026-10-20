package zone.ien.utils.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import platform.UIKit.UIApplication

private var keepScreenOnCount = 0
private var originalIdleTimerDisabled = false

@Composable
actual fun KeepScreenOn(enabled: Boolean) {
    if (enabled) {
        DisposableEffect(Unit) {
            val application = UIApplication.sharedApplication
            if (keepScreenOnCount == 0) {
                originalIdleTimerDisabled = application.idleTimerDisabled
            }
            keepScreenOnCount++
            application.idleTimerDisabled = true

            onDispose {
                keepScreenOnCount--
                if (keepScreenOnCount == 0) {
                    application.idleTimerDisabled = originalIdleTimerDisabled
                }
            }
        }
    }
}
