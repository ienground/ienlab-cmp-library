package zone.ien.utils.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.IntSize
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIView
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import kotlin.math.roundToInt
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

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun rememberIenOverlayWindowSize(): State<IntSize> {
    val parent = LocalUIViewController.current.view
    val window = parent.window
        ?: return rememberUpdatedState(LocalWindowInfo.current.containerSize)
    val density = LocalDensity.current.density
    fun currentSize(): IntSize = window.bounds.useContents {
        IntSize((size.width * density).roundToInt(), (size.height * density).roundToInt())
    }
    val size = remember(window, density) { mutableStateOf(currentSize()) }
    DisposableEffect(parent, window, density) {
        val observer = object : UIView(parent.bounds) {
            override fun layoutSubviews() {
                super.layoutSubviews()
                size.value = currentSize()
            }
        }.apply {
            userInteractionEnabled = false
            autoresizingMask = UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
        }
        parent.addSubview(observer)
        onDispose { observer.removeFromSuperview() }
    }
    return size
}
