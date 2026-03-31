package zone.ien.utils.ui.utils

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 150
    return rememberUpdatedState(isImeVisible)
}

fun Modifier.advancedImePadding(condition: Boolean) = composed {
//    conditional(condition) {
//        var consumePadding by remember { mutableStateOf(0) }
//        onGloballyPositioned { coordinates ->
//            consumePadding = (coordinates.findRootCoordinates().size.height -
//                    (coordinates.positionInWindow().y + coordinates.size.height)).toInt().coerceAtLeast(0)
//        }
//            .consumeWindowInsets(
//                PaddingValues(bottom = with(LocalDensity.current) { consumePadding.toDp() })
//            )
//    }
//        .
        imePadding()
}

@Composable
fun Modifier.dragToKeyboardClose(isKeyboardVisible: Boolean): Modifier {
    val keyboardManager = LocalSoftwareKeyboardController.current

    return pointerInput(isKeyboardVisible) {
        if (!isKeyboardVisible) return@pointerInput
        detectVerticalDragGestures { change, _ ->
            if (change.positionChange().y > 0) {
                keyboardManager?.hide()
            }
        }
    }
}