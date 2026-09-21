package zone.ien.utils.ui.window

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.PopupProperties

@OptIn(ExperimentalComposeUiApi::class)
internal actual fun ienTooltipPopupProperties(): PopupProperties = PopupProperties(
    focusable = false,
    dismissOnBackPress = true,
    dismissOnClickOutside = true,
    clippingEnabled = false,
    usePlatformDefaultWidth = false,
    usePlatformInsets = false,
)
