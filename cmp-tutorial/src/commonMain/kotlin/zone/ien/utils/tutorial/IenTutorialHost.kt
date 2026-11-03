package zone.ien.utils.tutorial

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import zone.ien.taptargetcmp.LocalTapTargetScope
import zone.ien.taptargetcmp.TapTargetCoordinator
import zone.ien.taptargetcmp.TapTargetStyle
import zone.ien.taptargetcmp.TextDefinition
import zone.ien.utils.ui.foundation.IenTheme

private data class TutorialTargetState(
    val step: Any? = null,
    val onReady: () -> Unit = {},
    val onNext: () -> Unit = {},
)

private val LocalTutorialTarget = staticCompositionLocalOf { TutorialTargetState() }

/**
 * 화면 상태를 보존하면서 [steps] 순서로 안내합니다. ViewModel이나 완료 상태 저장소에 의존하지 않습니다.
 *
 * @param steps 서로 다른 식별자로 구성한 안내 순서입니다. 식별자는 재구성 사이에 동등성을 유지해야 합니다.
 * @param enabled 완료 여부 조회 등 앱의 조건이 충족되었을 때 안내를 활성화합니다.
 * @param restartKey 값이 바뀌면 첫 단계부터 다시 안내합니다.
 * @param onComplete 마지막 단계 또는 건너뛰기로 안내를 마쳤을 때 호출합니다. 영속화는 호출자가 담당합니다.
 * @param skipContent 건너뛰기 콜백을 받아 버튼 등을 표시합니다.
 * @param content 현재 단계 또는 비활성·완료 시 null을 받아 화면을 표시합니다.
 */
@Composable
fun <T : Any> IenTutorialHost(
    modifier: Modifier = Modifier,
    steps: List<T>,
    enabled: Boolean = true,
    restartKey: Any? = Unit,
    onComplete: () -> Unit,
    skipContent: @Composable BoxScope.(onSkip: () -> Unit) -> Unit,
    content: @Composable (currentStep: T?) -> Unit,
) {
    var stepIndex by rememberSaveable(steps, restartKey) { mutableIntStateOf(0) }
    var finished by rememberSaveable(steps, restartKey) { mutableStateOf(false) }
    val active = enabled && !finished
    val step = steps.getOrNull(stepIndex).takeIf { active }
    val currentContent by rememberUpdatedState(content)
    val currentStep by rememberUpdatedState(step)
    // 단계별 coordinator가 바뀌어도 입력값·스크롤·화면 부수 효과의 수명을 유지합니다.
    val screenContent = remember { movableContentOf { currentContent(currentStep) } }
    val finish = {
        if (!finished) {
            finished = true
            onComplete()
        }
    }
    val displayedStepIndex = stepIndex
    val advance = {
        if (step != null && !finished && stepIndex == displayedStepIndex) {
            if (displayedStepIndex == steps.lastIndex) finish() else stepIndex++
        }
    }

    key(steps, restartKey, stepIndex) {
        var ready by remember { mutableStateOf(false) }
        TapTargetCoordinator(
            modifier = modifier.fillMaxSize(),
            showTapTargets = step != null && ready,
            onComplete = advance,
            skipButton = { skipContent(finish) },
        ) {
            CompositionLocalProvider(
                LocalTutorialTarget provides TutorialTargetState(
                    step = step,
                    onReady = { ready = true },
                    onNext = advance,
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(if (step != null) Modifier.clearAndSetSemantics { } else Modifier)
                        .then(
                            if (step != null && !ready) Modifier.pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        awaitPointerEvent(PointerEventPass.Initial).changes.forEach { it.consume() }
                                    }
                                }
                            } else Modifier,
                        ),
                ) {
                    screenContent()
                }
            }
        }
    }
}

/**
 * 현재 단계의 위젯을 표시 영역으로 스크롤하고 강조 항목을 누르면 다음 안내로 이동합니다.
 *
 * @param titleTextStyle 제목 서식입니다. 기본값은 [IenTheme.typography]의 title2입니다.
 * @param descriptionTextStyle 설명 서식입니다. 기본값은 [IenTheme.typography]의 body1입니다.
 * @param titleColor 제목 색상입니다. 기본값은 [IenTheme.colors]의 onBrandWeak입니다.
 * @param descriptionColor 설명 색상입니다. 기본값은 [IenTheme.colors]의 onBrandWeak입니다.
 * @param tapTargetStyle 강조 서식입니다. 기본값은 현재 테마의 brandWeak 배경과 onBrandWeak 강조색입니다.
 */
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.ienTutorialTarget(
    step: Any,
    title: String,
    description: String,
    enabled: Boolean = true,
    titleTextStyle: TextStyle = IenTheme.typography.title2,
    descriptionTextStyle: TextStyle = IenTheme.typography.body1,
    titleColor: Color = IenTheme.colors.onBrandWeak,
    descriptionColor: Color = IenTheme.colors.onBrandWeak,
    tapTargetStyle: TapTargetStyle = TapTargetStyle(
        backgroundColor = IenTheme.colors.brandWeak,
        tapTargetHighlightColor = IenTheme.colors.onBrandWeak,
    ),
): Modifier = composed {
    val target = LocalTutorialTarget.current
    val scope = LocalTapTargetScope.current
    if (!enabled || target.step != step || scope == null) return@composed this

    val requester = remember(scope) { BringIntoViewRequester() }
    var positioned by remember(scope) { mutableStateOf(false) }
    val onReady by rememberUpdatedState(target.onReady)
    LaunchedEffect(scope, positioned) {
        if (positioned) {
            withFrameNanos { }
            requester.bringIntoView()
            withFrameNanos { }
            onReady()
        }
    }
    with(scope) {
        this@composed
            .onGloballyPositioned { positioned = true }
            .tapTarget(
                title = TextDefinition(
                    text = title,
                    textStyle = titleTextStyle,
                    color = titleColor,
                ),
                description = TextDefinition(
                    text = description,
                    textStyle = descriptionTextStyle,
                    color = descriptionColor,
                ),
                precedence = 0,
                onTargetClick = target.onNext,
                onTargetCancel = target.onNext,
                tapTargetStyle = tapTargetStyle,
                bringIntoViewEnabled = false,
                bringIntoViewRequester = requester,
            )
    }
}
