package zone.ien.utils.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

@Composable
internal actual fun disablePlatformDialogDim() = Unit

@OptIn(ExperimentalComposeUiApi::class)
internal actual fun ienOverlayDialogProperties(
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
): DialogProperties = DialogProperties(
    dismissOnBackPress = dismissOnBackPress,
    dismissOnClickOutside = dismissOnClickOutside,
    usePlatformDefaultWidth = false,
    usePlatformInsets = false,
    useSoftwareKeyboardInset = true,
    scrimColor = Color.Transparent,
    animateTransition = false,
)

@Composable
internal actual fun rememberIenOverlayWindowSize(): State<IntSize> =
    rememberUpdatedState(LocalWindowInfo.current.containerSize)
