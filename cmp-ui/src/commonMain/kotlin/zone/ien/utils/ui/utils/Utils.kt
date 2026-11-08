package zone.ien.utils.ui.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
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
 * import zone.ien.utils.ui.foundation.IenTheme
 *
 * modifier
 *     .conditional(shouldApplyPadding) {
 *         padding(IenTheme.spacing.md)
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

internal fun Modifier.shakeOnDisabledClick(enabled: Boolean): Modifier =
    this then DisabledClickShakeElement(enabled)

private data class DisabledClickShakeElement(val enabled: Boolean) : ModifierNodeElement<DisabledClickShakeNode>() {
    override fun create(): DisabledClickShakeNode = DisabledClickShakeNode(enabled)

    override fun update(node: DisabledClickShakeNode) {
        node.updateEnabled(enabled)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "shakeOnDisabledClick"
        properties["enabled"] = enabled
    }
}

private class DisabledClickShakeNode(private var enabled: Boolean) : DelegatingNode(), LayoutModifierNode {
    override val shouldAutoInvalidate: Boolean = false
    private var shakeOffset by mutableFloatStateOf(0f)
    private var shakeJob: Job? = null

    init {
        delegate(SuspendingPointerInputModifierNode {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                if (!enabled) {
                    val shakeDistance = 6.dp.toPx()
                    shakeJob?.cancel()
                    shakeJob = coroutineScope.launch {
                        try {
                            Animatable(0f).animateTo(
                                targetValue = 0f,
                                animationSpec = keyframes {
                                    durationMillis = 200
                                    -shakeDistance at 30
                                    shakeDistance at 70
                                    -shakeDistance * 0.6f at 110
                                    shakeDistance * 0.6f at 150
                                    0f at 200
                                },
                            ) { shakeOffset = value }
                        } finally {
                            shakeOffset = 0f
                        }
                    }
                }
                waitForUpOrCancellation(pass = PointerEventPass.Initial)
            }
        })
    }

    fun updateEnabled(enabled: Boolean) {
        this.enabled = enabled
        if (enabled) {
            shakeJob?.cancel()
            shakeOffset = 0f
        }
    }

    override fun onDetach() {
        shakeJob?.cancel()
        shakeOffset = 0f
    }

    override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeRelativeWithLayer(shakeOffset.roundToInt(), 0)
        }
    }
}
