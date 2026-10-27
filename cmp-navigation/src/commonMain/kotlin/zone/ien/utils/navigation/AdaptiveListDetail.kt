package zone.ien.utils.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenTheme

/**
 * 중간 너비부터 두 pane을 표시하고, 단일 pane에서는 기존 화면 전환을 사용하는 전략을 생성합니다.
 *
 * [BaseNavDisplay]의 sceneStrategies에 전달하고 각 화면에
 * [ListDetailSceneStrategy.listPane] 또는 [ListDetailSceneStrategy.detailPane] 메타데이터를 지정합니다.
 * 화면별 전환에는 [ienPaneAnimation]을 함께 지정합니다.
 *
 * @param directive pane 배치 정책. 기본 pane 간격은 0dp이며, 화면별 비율은
 * [ListDetailSceneStrategy.preferredPaneSize]로 지정합니다.
 */
@ExperimentalMaterial3AdaptiveApi
@Composable
fun <T : Any> rememberIenListDetailSceneStrategy(
    directive: PaneScaffoldDirective = calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(
        currentWindowAdaptiveInfoV2(),
    ).copy(horizontalPartitionSpacerSize = 0.dp),
): ListDetailSceneStrategy<T> = rememberListDetailSceneStrategy(
    shouldHandleSinglePaneLayout = false,
    directive = directive,
)

/**
 * 목록·상세 화면의 메타데이터에 더할 IEN 테마 기반 pane 전환 설정을 생성합니다.
 *
 * @param enterTransition pane이 나타날 때의 전환. 기본값은 테마의 빠른 페이드입니다.
 * @param exitTransition pane이 사라질 때의 전환. 기본값은 테마의 빠른 페이드입니다.
 * @param boundsAnimationSpec pane 위치와 크기 변경에 사용할 애니메이션입니다.
 */
@ExperimentalMaterial3AdaptiveApi
@Composable
fun ienPaneAnimation(
    enterTransition: EnterTransition = fadeIn(
        tween(IenTheme.motion.fastMillis, easing = IenTheme.motion.standardEasing),
    ),
    exitTransition: ExitTransition = fadeOut(
        tween(IenTheme.motion.fastMillis, easing = IenTheme.motion.standardEasing),
    ),
    boundsAnimationSpec: FiniteAnimationSpec<IntRect> = tween(
        IenTheme.motion.normalMillis,
        easing = IenTheme.motion.standardEasing,
    ),
): Map<String, Any> = ListDetailSceneStrategy.paneAnimation(
    enterTransition = enterTransition,
    exitTransition = exitTransition,
    boundsAnimationSpec = boundsAnimationSpec,
)
