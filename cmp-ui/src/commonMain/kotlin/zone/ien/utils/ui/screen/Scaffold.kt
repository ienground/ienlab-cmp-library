package zone.ien.utils.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.menu.M3ActionsMenu
import zone.ien.utils.ui.utils.conditional

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3TopAppBarScaffold(
    modifier: Modifier = Modifier,
    topBarModifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    showTopBar: Boolean = true,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    topBarWindowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.Center,
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    isCenterAligned: Boolean = true,
    scaffoldContainerColor: Color = MaterialTheme.colorScheme.background,
    scaffoldContentColor: Color = contentColorFor(scaffoldContainerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    isScrollTint: Boolean = LocalIsScrollTint.current,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .conditional(isScrollTint) {
                nestedScroll(scrollBehavior.nestedScrollConnection)
            },
        topBar = {
            Box {
                AnimatedVisibility(
                    visible = showTopBar,
                    enter = expandVertically(spring(1.2f)) + fadeIn(spring(1.2f)),
                    exit = shrinkVertically(spring(1.2f)) + fadeOut(spring(1.2f))
                ) {
                    M3TopAppBar(
                        title = title,
                        navigationIcon = navigationIcon,
                        actions = actions,
                        windowInsets = topBarWindowInsets,
                        colors = topAppBarColors,
                        isCenterAligned = isCenterAligned,
                        scrollBehavior = scrollBehavior,
                        isScrollTint = isScrollTint,
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
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        contentWindowInsets = contentWindowInsets,
        containerColor = scaffoldContainerColor,
        contentColor = scaffoldContentColor,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3TopAppBarScaffold(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    topBarModifier: Modifier = Modifier,
    showTopBar: Boolean = true,
    navigationIcon: @Composable () -> Unit = {},
    actions: List<ActionMenuItem> = listOf(),
    topBarWindowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.Center,
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    isCenterAligned: Boolean = true,
    scaffoldContainerColor: Color = MaterialTheme.colorScheme.background,
    scaffoldContentColor: Color = contentColorFor(scaffoldContainerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    isScrollTint: Boolean = LocalIsScrollTint.current,
    content: @Composable (PaddingValues) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    M3TopAppBarScaffold(
        modifier = modifier,
        title = title,
        topBarModifier = topBarModifier,
        showTopBar = showTopBar,
        navigationIcon = navigationIcon,
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                M3ActionsMenu(
                    items = actions,
                    isOpen = menuExpanded,
                    closeDropdown = { menuExpanded = false },
                    onToggleOverflow = { menuExpanded = !menuExpanded },
                    maxVisibleItems = 5
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        },
        topBarWindowInsets = topBarWindowInsets,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        topAppBarColors = topAppBarColors,
        isCenterAligned = isCenterAligned,
        scaffoldContainerColor = scaffoldContainerColor,
        scaffoldContentColor = scaffoldContentColor,
        contentWindowInsets = contentWindowInsets,
        scrollBehavior = scrollBehavior,
        isScrollTint = isScrollTint,
        content = content
    )
}