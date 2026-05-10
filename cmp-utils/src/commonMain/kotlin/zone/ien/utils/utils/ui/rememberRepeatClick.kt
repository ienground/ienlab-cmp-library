package zone.ien.utils.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlin.time.Clock

/**
 * 반복 클릭 상호작용을 처리하는 Composable 함수를 생성합니다.
 * 
 * 이 함수는 지정된 시간 윈도우 내에서 사용자가 여러 번 클릭했는지를 감지하는 클릭 핸들러를 기억합니다.
 * 일반 클릭과 연속 클릭 감지를 모두 처리하여 복잡한 사용자 상호작용을 지원합니다.
 * 
 * @param n n번째 클릭을 트리거할 때 사용할 클릭 횟수 (기본값: 2)
 * @param intervalMs 연속 클릭을 감지할 시간 윈도우 (밀리초) (기본값: 2000)
 * @param onClick 모든 일반 클릭 시 호출될 함수 (기본값: 빈 함수)
 * @param onNthClick n개의 연속 클릭이 발생했을 때 호출될 함수
 * @return Compose UI 컴포넌트에 사용할 수 있는 클릭 핸들러
 */
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