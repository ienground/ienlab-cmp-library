package zone.ien.utils.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneInfo
import androidx.navigationevent.NavigationEventTransitionState
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import zone.ien.utils.ui.screen.LocalIenCupertinoTopBarTransition
import zone.ien.utils.ui.screen.IenTopBarTransition

/** 화면 본문의 이동과 분리하여 버튼의 경계와 내용을 전환합니다. */
internal class NavigationTopBarTransition(
    private val scope: SharedTransitionScope,
) : IenTopBarTransition {
    @Composable
    override fun modifier(slot: IenTopBarTransition.Slot): Modifier = with(scope) {
        val visibilityScope = LocalNavAnimatedContentScope.current
        val transition = visibilityScope.transition
        val isRunning = { transition.currentState != transition.targetState }
        if (useCupertinoTopBarTransition && slot == IenTopBarTransition.Slot.Actions) {
            val overlayZIndex = if (transition.currentState == EnterExitState.Visible) 2f else 1f
            return@with Modifier
                .renderInSharedTransitionScopeOverlay(renderInOverlay = isRunning, zIndexInOverlay = overlayZIndex)
                .skipToLookaheadPosition(enabled = isRunning)
                .then(navigationTopBarFade(visibilityScope))
        }
        val state = rememberSharedContentState(this@NavigationTopBarTransition to slot)
        Modifier
            .sharedBounds(
                sharedContentState = state,
                animatedVisibilityScope = visibilityScope,
                enter = EnterTransition.None,
                exit = ExitTransition.None,
                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                zIndexInOverlay = 1f,
            )
            // 상대 화면에 같은 영역이 없어도 화면 본문과 함께 밀리지 않게 합니다.
            .renderInSharedTransitionScopeOverlay(
                renderInOverlay = { !state.isMatchFound && isRunning() },
                zIndexInOverlay = 1f,
            )
            .skipToLookaheadPosition(enabled = { !state.isMatchFound && isRunning() })
            .then(navigationTopBarFade(visibilityScope))
    }
}

/** Cupertino 테마의 버튼은 공유 경계 보간 없이 제자리에서 전환합니다. */
internal val useCupertinoTopBarTransition: Boolean
    @Composable get() = LocalIenCupertinoTopBarTransition.current

/** 플랫폼의 그림자 합성 및 전환 방식에 맞춰 페이드를 적용합니다. */
@Composable
internal expect fun navigationTopBarFade(scope: AnimatedVisibilityScope): Modifier

private const val TopBarAnimationLeadMillis = 200
private const val TopBarFadeDurationMillis = 150
private const val TopBarAppearDurationMillis = 150
internal const val TopBarHiddenScale = 1.15f

/** 제스처 중에는 유지하고, 확정된 화면 전환 마지막에만 짧게 페이드합니다. */
@Composable
internal fun navigationTopBarOpacity(scope: AnimatedVisibilityScope): State<Float> {
    val transition = scope.transition
    val contentTransition = transition.parentTransition ?: transition
    val dispatcher = LocalNavigationEventDispatcherOwner.current?.navigationEventDispatcher
    val history = dispatcher?.history?.collectAsState()
    val gestureFlow = remember(dispatcher) {
        dispatcher?.transitionState?.map { it is NavigationEventTransitionState.InProgress }?.distinctUntilChanged()
    }
    val gestureInProgress = gestureFlow?.collectAsState(
        initial = dispatcher?.transitionState?.value is NavigationEventTransitionState.InProgress,
    )
    val fadeStarted = remember(contentTransition.currentState, contentTransition.targetState) { mutableStateOf(false) }
    val finishReady = remember(contentTransition, history, gestureInProgress) {
        derivedStateOf {
            val navigationHistory = history?.value
            val actualScene = navigationHistory?.let {
                (it.mergedHistory.getOrNull(it.currentIndex) as? SceneInfo<*>)?.scene
            }
            val targetScene = contentTransition.targetState as? Scene<*>
            val committed = actualScene == null || targetScene == null ||
                (actualScene::class == targetScene::class && actualScene.key == targetScene.key)
            val remaining = contentTransition.totalDurationNanos - contentTransition.playTimeNanos
            contentTransition.currentState != contentTransition.targetState &&
                gestureInProgress?.value != true && committed &&
                remaining <= TopBarAnimationLeadMillis * 1_000_000L
        }
    }
    LaunchedEffect(finishReady.value) {
        if (finishReady.value) {
            // 초기 측정에서 본문 전환 길이가 등록된 뒤 종료 여부를 확인합니다.
            if (contentTransition.totalDurationNanos <= TopBarFadeDurationMillis * 1_000_000L) {
                withFrameNanos { }
            }
            if (finishReady.value) fadeStarted.value = true
        }
    }
    // 본문에 별도 애니메이션이 없어도 마지막 페이드를 위한 콘텐츠 수명을 유지합니다.
    transition.animateFloat(
        transitionSpec = { tween(durationMillis = TopBarFadeDurationMillis) },
        label = "TopBarLifetime",
    ) { if (it == EnterExitState.Visible) 1f else 0f }
    val waiting = contentTransition.currentState != contentTransition.targetState && !fadeStarted.value
    val opacity = transition.animateFloat(
        transitionSpec = { tween(durationMillis = TopBarFadeDurationMillis) },
        label = "TopBarOpacity",
    ) {
        val state = if (waiting) transition.currentState else it
        if (state == EnterExitState.Visible) 1f else 0f
    }
    // 확정된 전환 마지막에 새 버튼도 함께 나타나 별도로 기다리지 않습니다.
    val appearing = remember(transition) { Animatable(0f) }
    val showIncoming = transition.currentState == EnterExitState.Visible ||
        (transition.targetState == EnterExitState.Visible &&
            (fadeStarted.value || contentTransition.currentState == contentTransition.targetState))
    LaunchedEffect(appearing, showIncoming) {
        appearing.animateTo(
            targetValue = if (showIncoming) 1f else 0f,
            animationSpec = tween(durationMillis = TopBarAppearDurationMillis),
        )
    }
    return remember(transition, contentTransition, opacity, appearing) {
        derivedStateOf {
            if (transition.targetState == EnterExitState.Visible) {
                appearing.value
            } else if (contentTransition.currentState == contentTransition.targetState) {
                0f
            } else {
                opacity.value * appearing.value
            }
        }
    }
}
