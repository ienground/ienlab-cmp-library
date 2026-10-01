package zone.ien.utils.ui.window

import androidx.compose.ui.window.PopupProperties

internal actual fun ienTooltipPopupProperties(): PopupProperties = PopupProperties(
    focusable = false,
    dismissOnBackPress = true,
    dismissOnClickOutside = true,
    clippingEnabled = false,
    usePlatformDefaultWidth = false,
)
