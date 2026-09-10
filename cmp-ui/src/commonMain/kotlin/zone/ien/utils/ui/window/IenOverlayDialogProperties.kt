package zone.ien.utils.ui.window

import androidx.compose.ui.window.DialogProperties

internal expect fun ienOverlayDialogProperties(
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
): DialogProperties
