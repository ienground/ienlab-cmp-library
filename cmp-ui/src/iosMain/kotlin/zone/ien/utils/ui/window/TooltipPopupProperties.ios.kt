package zone.ien.utils.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.PopupProperties
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification

@OptIn(ExperimentalComposeUiApi::class)
internal actual fun ienTooltipPopupProperties(): PopupProperties = PopupProperties(
    focusable = false,
    dismissOnBackPress = true,
    dismissOnClickOutside = true,
    clippingEnabled = false,
    usePlatformDefaultWidth = false,
    usePlatformInsets = false,
)

@Composable
internal actual fun rememberIenTooltipPopupKey(): Int {
    var generation by remember { mutableIntStateOf(0) }
    DisposableEffect(Unit) {
        val notificationCenter = NSNotificationCenter.defaultCenter
        val observer = notificationCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
        ) {
            generation++
        }
        onDispose {
            notificationCenter.removeObserver(observer)
        }
    }
    return generation
}
