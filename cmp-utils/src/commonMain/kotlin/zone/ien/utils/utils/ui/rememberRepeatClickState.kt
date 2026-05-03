package zone.ien.utils.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.time.Clock

@Composable
fun rememberRepeatClickState(
    n: Int = 2,
    intervalMs: Long = 2000L,
    onClick: () -> Unit = {},
    onNthClick: () -> Unit,
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    var clickCount by remember { mutableIntStateOf(0) }

    return remember(n, intervalMs, onClick, onNthClick) {
        {
            val now = Clock.System.now().toEpochMilliseconds()
            if (now - lastClickTime > intervalMs) {
                // 간격 초과 → 첫 번째 클릭으로 리셋
                clickCount = 1
                onClick()
            } else {
                clickCount++
                if (clickCount >= n) {
                    onNthClick()
                    clickCount = 0
                }
            }
            lastClickTime = now
        }
    }
}