package zone.ien.utils.ui.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 조건에 따라 Modifier를 적용하는 확장 함수
 * 
 * 이 함수는 특정 조건이 참일 경우에만 Modifier를 적용합니다.
 * 조건이 거짓일 경우 원래 Modifier를 그대로 반환합니다.
 * 
 * 사용 예시:
 * ```kotlin
 * modifier
 *     .conditional(shouldApplyPadding) {
 *         padding(16.dp)
 *     }
 *     .conditional(shouldApplyBackground) {
 *         background(Color.Blue)
 *     }
 * ```
 * 
 * @param condition 적용할 조건
 * @param modifier 조건이 참일 때 적용할 Modifier 블록
 * @return 조건에 따라 수정된 Modifier
 */
@Composable
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

/**
 * 터치가 시작되는 즉시(0ms 지연) 눌림 상태를 감지하여 콜백을 호출하는 Modifier 확장 함수
 * 
 * @param enabled 제스처 감지 활성화 여부
 * @param onPressedChange 눌림 상태 변경 시 호출될 콜백 (Pressed -> true, Released/Cancelled -> false)
 * @return 눌림 상태 감지 기능이 적용된 Modifier
 */
fun Modifier.instantPress(
    enabled: Boolean = true,
    onPressedChange: (Boolean) -> Unit
): Modifier = if (enabled) {
    this.pointerInput(enabled) {
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false)
            onPressedChange(true)
            waitForUpOrCancellation()
            onPressedChange(false)
        }
    }
} else {
    this
}

@Composable
internal fun Modifier.shakeOnDisabledClick(enabled: Boolean): Modifier {
    if (enabled) return this

    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val shakeDistance = with(LocalDensity.current) { 6.dp.toPx() }

    return this
        .pointerInput(shakeDistance) {
            awaitEachGesture {
                awaitFirstDown(
                    requireUnconsumed = false,
                    pass = PointerEventPass.Initial,
                )
                coroutineScope.launch {
                    shakeOffset.snapTo(0f)
                    shakeOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = keyframes {
                            durationMillis = 200
                            -shakeDistance at 30
                            shakeDistance at 70
                            -shakeDistance * 0.6f at 110
                            shakeDistance * 0.6f at 150
                            0f at 200
                        },
                    )
                }
                waitForUpOrCancellation(pass = PointerEventPass.Initial)
            }
        }
        .offset { IntOffset(x = shakeOffset.value.roundToInt(), y = 0) }
}
