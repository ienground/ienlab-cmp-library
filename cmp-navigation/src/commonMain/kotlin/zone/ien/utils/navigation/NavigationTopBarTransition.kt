package zone.ien.utils.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import zone.ien.utils.ui.screen.IenTopBarTransition

/** 화면 본문의 이동과 분리하여 버튼의 경계와 내용을 전환합니다. */
internal class NavigationTopBarTransition(
    private val scope: SharedTransitionScope,
) : IenTopBarTransition {
    @Composable
    override fun modifier(slot: IenTopBarTransition.Slot): Modifier = with(scope) {
        val visibilityScope = LocalNavAnimatedContentScope.current
        val state = rememberSharedContentState(this@NavigationTopBarTransition to slot)
        val transition = visibilityScope.transition
        val isRunning = { transition.currentState != transition.targetState }


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

/** 플랫폼의 그림자 합성 방식에 맞춰 전환 진행도의 페이드를 적용합니다. */
@Composable
internal expect fun navigationTopBarFade(scope: AnimatedVisibilityScope): Modifier
