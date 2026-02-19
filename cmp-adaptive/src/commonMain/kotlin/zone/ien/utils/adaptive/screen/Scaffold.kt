package zone.ien.utils.adaptive.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveScaffold
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveTopAppBarScaffold(
    modifier: Modifier = Modifier,
    topBarModifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    showTopBar: Boolean = true,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    // todo fab
    topBarAdaptation: AdaptationScope<HigTopAppBarAdaptation, M3TopAppBarAdaptation>.() -> Unit = LocalTopBarAdaptation.current,

    content: @Composable (PaddingValues) -> Unit
) {
    AdaptiveScaffold(
        modifier = modifier,
        topBar = {
            Box {
                AnimatedVisibility(
                    visible = showTopBar,
                    enter = expandVertically(spring(1.2f)) + fadeIn(spring(1.2f)),
                    exit = shrinkVertically(spring(1.2f)) + fadeOut(spring(1.2f))
                ) {
                    AdaptiveTopAppBar(
                        title = title,
                        navigationIcon = navigationIcon,
                        actions = actions,
                        adaptation = topBarAdaptation,
                        modifier = topBarModifier
                    )
                }
                AnimatedVisibility(
                    visible = !showTopBar,
                    enter = expandVertically(spring(1.2f)) + fadeIn(spring(1.2f)),
                    exit = shrinkVertically(spring(1.2f)) + fadeOut(spring(1.2f))
                ) {
                    Box(
                        modifier = Modifier.height(IntrinsicSize.Min)
                    ) {
                        Box(modifier = Modifier.statusBarsPadding())
                        Box(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        },
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        content = content
    )
//    AdaptiveWidget(
//        adaptation = remember {  }
//    )
}

class M3ScaffoldAdaptation internal constructor(
//    windowInsets: WindowInsets,
//    scrollBehavior

) {

}



