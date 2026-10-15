package zone.ien.utils.ui.window

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.PopupProperties

internal actual fun ienTooltipPopupProperties(): PopupProperties = PopupProperties(
    focusable = false,
    dismissOnBackPress = true,
    dismissOnClickOutside = true,
    clippingEnabled = false,
    usePlatformDefaultWidth = false,
)

@Composable
internal actual fun rememberIenTooltipPopupKey(): Int = 0

internal actual fun ienTooltipShadowPopupProperties(): PopupProperties? = PopupProperties(
    flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    dismissOnBackPress = false,
    dismissOnClickOutside = false,
    excludeFromSystemGesture = false,
    usePlatformDefaultWidth = false,
)
