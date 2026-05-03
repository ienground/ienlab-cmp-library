package zone.ien.utils.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlin.time.Clock

@Composable
fun rememberRepeatClick(
    n: Int = 2,
    intervalMs: Long = 2000L,
    onClick: () -> Unit = {},
    onNthClick: () -> Unit,
): () -> Unit {
    val currentOnClick by rememberUpdatedState(onClick)
    val currentOnNthClick by rememberUpdatedState(onNthClick)

    var lastClickTime by remember { mutableLongStateOf(0L) }
    var clickCount by remember { mutableIntStateOf(0) }

    return remember(n, intervalMs) {
        {
            val now = Clock.System.now().toEpochMilliseconds()
            if (now - lastClickTime > intervalMs) {
                clickCount = 1
                currentOnClick()
            } else {
                clickCount++
                if (clickCount >= n) {
                    currentOnNthClick()
                    clickCount = 0
                } else {
                    currentOnClick()
                }
            }
            lastClickTime = now
        }
    }
}