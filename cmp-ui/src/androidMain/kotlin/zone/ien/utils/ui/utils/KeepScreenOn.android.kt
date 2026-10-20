package zone.ien.utils.ui.utils

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView

private class KeepScreenOnState(val originalValue: Boolean, var count: Int = 0)

private val keepScreenOnStates = mutableMapOf<View, KeepScreenOnState>()

@Composable
actual fun KeepScreenOn(enabled: Boolean) {
    val view = LocalView.current
    if (enabled) {
        DisposableEffect(view) {
            val state = keepScreenOnStates.getOrPut(view) {
                KeepScreenOnState(view.keepScreenOn)
            }
            state.count++
            view.keepScreenOn = true

            onDispose {
                state.count--
                if (state.count == 0) {
                    view.keepScreenOn = state.originalValue
                    keepScreenOnStates.remove(view)
                }
            }
        }
    }
}
