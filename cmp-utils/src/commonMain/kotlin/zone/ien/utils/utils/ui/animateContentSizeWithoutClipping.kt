package zone.ien.utils.utils.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.constrain
import kotlinx.coroutines.launch

/**
 * 크기 변경 애니메이션을 적용하면서 클리핑을 방지하는 Modifier.
 * 
 * 이 Modifier는 레이아웃 크기가 변경될 때 애니메이션을 적용하면서도
 * 자식 컴포넌트의 클리핑을 방지합니다. 일반적인 크기 변경 애니메이션은
 * 컴포넌트가 특정 크기로 변경될 때 자식이 크기 변경에 따라 클리핑되는 문제를 해결합니다.
 * 
 * @param animationSpec 애니메이션 설정 (기본값: Spring 애니메이션)
 * @param finishedListener 애니메이션이 완료될 때 호출되는 콜백 (기본값: null)
 * @return 크기 변경 애니메이션을 적용한 Modifier
 */
fun Modifier.animateContentSizeWithoutClipping(
    animationSpec: FiniteAnimationSpec<IntSize> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntSize.VisibilityThreshold
    ),
    finishedListener: ((initialValue: IntSize, targetValue: IntSize) -> Unit)? = null
): Modifier = this then SizeAnimationModifierElement(animationSpec, Alignment.TopStart, finishedListener)

/**
 * 크기 변경 애니메이션을 관리하는 Modifier 노드 엘리먼트.
 * 
 * 이 클래스는 애니메이션을 처리하고 수정자 노드의 업데이트를 관리합니다.
 * 
 * @param animationSpec 애니메이션 설정
 * @param alignment 정렬 방식
 * @param finishedListener 애니메이션이 완료될 때 호출되는 콜백
 */
private data class SizeAnimationModifierElement(
    val animationSpec: FiniteAnimationSpec<IntSize>,
    val alignment: Alignment,
    val finishedListener: ((initialValue: IntSize, targetValue: IntSize) -> Unit)?
) : ModifierNodeElement<SizeAnimationModifierNode>() {
    override fun create(): SizeAnimationModifierNode =
        SizeAnimationModifierNode(animationSpec, alignment, finishedListener)

    override fun update(node: SizeAnimationModifierNode) {
        node.animationSpec = animationSpec
        node.listener = finishedListener
        node.alignment = alignment
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "animateContentSize"
        properties["animationSpec"] = animationSpec
        properties["alignment"] = alignment
        properties["finishedListener"] = finishedListener
    }
}

/**
 * 유효하지 않은 크기 값을 나타내는 상수.
 * 
 * 이 상수는 유효하지 않은 크기 데이터를 식별하는 데 사용됩니다.
 */
internal val InvalidSize = IntSize(Int.MIN_VALUE, Int.MIN_VALUE)

/**
 * IntSize가 유효한지 확인하는 확장 프로퍼티.
 * 
 * @return 크기가 유효한 경우 true, 그렇지 않은 경우 false
 */
internal val IntSize.isValid: Boolean
    get() = this != InvalidSize

/**
 * 크기 변경 애니메이션을 처리하는 레이아웃 수정자 노드.
 * 
 * 이 클래스는 레이아웃 크기 변경을 추적하고 애니메이션을 적용합니다.
 * lookahead를 통해 부하를 줄이고 더 효율적인 레이아웃을 제공합니다.
 * 
 * @param animationSpec 애니메이션 설정
 * @param alignment 정렬 방식
 * @param listener 애니메이션이 완료될 때 호출되는 콜백
 */
private class SizeAnimationModifierNode(
    var animationSpec: AnimationSpec<IntSize>,
    var alignment: Alignment = Alignment.TopStart,
    var listener: ((startSize: IntSize, endSize: IntSize) -> Unit)? = null
) : LayoutModifierNodeWithPassThroughIntrinsics() {
    private var lookaheadSize: IntSize = InvalidSize
    private var lookaheadConstraints: Constraints = Constraints()
        set(value) {
            field = value
            lookaheadConstraintsAvailable = true
        }
    private var lookaheadConstraintsAvailable: Boolean = false

    private fun targetConstraints(default: Constraints) =
        if (lookaheadConstraintsAvailable) {
            lookaheadConstraints
        } else {
            default
        }

    /**
     * 애니메이션 데이터를 저장하는 데이터 클래스.
     * 
     * @param anim 애니메이션 동작
     * @param startSize 애니메이션 시작 크기
     */
    data class AnimData(
        val anim: Animatable<IntSize, AnimationVector2D>,
        var startSize: IntSize
    )

    var animData: AnimData? by mutableStateOf(null)

    override fun onReset() {
        super.onReset()
        // Reset은 노드가 재사용될 가능성을 나타냅니다. 이 경우 animData가 오래되었습니다.
        animData = null
    }

    override fun onAttach() {
        super.onAttach()
        // 재연결 시 lookahead scope가 없는 트리에 연결될 수 있습니다.
        lookaheadSize = InvalidSize
        lookaheadConstraintsAvailable = false
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = if (isLookingAhead) {
            lookaheadConstraints = constraints
            measurable.measure(constraints)
        } else {
            // lookahead 제약 조건이 사용 가능한 경우 측정하여 불필요한 재배치를 방지합니다.
            measurable.measure(targetConstraints(constraints))
        }
        val measuredSize = IntSize(placeable.width, placeable.height)
        val (width, height) = if (isLookingAhead) {
            lookaheadSize = measuredSize
            measuredSize
        } else {
            animateTo(if (lookaheadSize.isValid) lookaheadSize else measuredSize).let {
                // 측정 결과를 입력 제약 조건으로 제한하여 부모가 이 레이아웃을 중앙으로 강제하지 않도록 합니다.
                constraints.constrain(it)
            }
        }
        return layout(width, height) {
            val offset = alignment.align(
                size = measuredSize,
                space = IntSize(width, height),
                layoutDirection = this@measure.layoutDirection
            )
            placeable.placeRelative(offset)
        }
    }

    /**
     * 지정된 크기로 애니메이션을 실행합니다.
     * 
     * @param targetSize 애니메이션의 대상 크기
     * @return 애니메이션의 최종 크기
     */
    fun animateTo(targetSize: IntSize): IntSize {
        val data = animData?.apply {
            // TODO(b/322878517): 다시 연결 후 애니메이션을 매끄럽게 계속하는 방법을 찾으세요.
            // 재시작이 올바른 동작인 경우도 있습니다.
            val wasInterrupted = (targetSize != anim.value && !anim.isRunning)

            if (targetSize != anim.targetValue || wasInterrupted) {
                startSize = anim.value
                coroutineScope.launch {
                    val result = anim.animateTo(targetSize, animationSpec)
                    if (result.endReason == AnimationEndReason.Finished) {
                        listener?.invoke(startSize, result.endState.value)
                    }
                }
            }
        } ?: AnimData(
            Animatable(
                targetSize, IntSize.VectorConverter, IntSize(1, 1)
            ),
            targetSize
        )

        animData = data
        return data.anim.value
    }
}

/**
 * 패스스루 인트린식을 포함하는 레이아웃 수정자 노드의 추상 클래스.
 * 
 * 이 클래스는 인트린식 측정 메소드를 구현하여 크기 계산을 전달합니다.
 */
internal abstract class LayoutModifierNodeWithPassThroughIntrinsics :
    LayoutModifierNode, Modifier.Node() {
    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ) = measurable.minIntrinsicWidth(height)

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ) = measurable.minIntrinsicHeight(width)

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ) = measurable.maxIntrinsicWidth(height)

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ) = measurable.maxIntrinsicHeight(width)
}