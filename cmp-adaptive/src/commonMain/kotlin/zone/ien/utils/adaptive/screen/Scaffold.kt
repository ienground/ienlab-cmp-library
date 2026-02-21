package zone.ien.utils.adaptive.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import zone.ien.hig.CupertinoButtonSize
import zone.ien.hig.CupertinoLiquidButton
import zone.ien.hig.CupertinoLiquidButtonColors
import zone.ien.hig.CupertinoLiquidButtonDefaults
import zone.ien.hig.CupertinoLiquidButtonDefaults.glassButtonColors
import zone.ien.hig.CupertinoLiquidButtonDefaults.glassProminentButtonColors
import zone.ien.hig.CupertinoLiquidIconButton
import zone.ien.hig.CupertinoScaffold
import zone.ien.hig.CupertinoScaffoldDefaults
import zone.ien.hig.CupertinoTopAppBar
import zone.ien.hig.CupertinoTopAppBarColors
import zone.ien.hig.CupertinoTopAppBarDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.FabPosition
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.utils.adaptive.menu.HigActionMenu
import zone.ien.utils.adaptive.menu.HigActionsMenu
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.menu.M3ActionsMenu
import zone.ien.utils.ui.screen.LocalIsScrollTint
import zone.ien.utils.ui.screen.M3TopAppBarScaffold

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class,
    ExperimentalCupertinoApi::class
)
@Composable
fun AdaptiveTopAppBarScaffold(
    modifier: Modifier = Modifier,
    topBarModifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    showTopBar: Boolean = true,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    fabPosition: FabPosition = FabPosition.Center,
    higFabPosition: FabPosition = fabPosition,
    adaptation: AdaptationScope<HigTopAppBarScaffoldAdaptation, M3TopAppBarScaffoldAdaptation>.() -> Unit = LocalTopBarScaffoldAdaptation.current,
    content: @Composable (PaddingValues) -> Unit
) {
    fun FabPosition.transform(): androidx.compose.material3.FabPosition {
        return when (this) {
            FabPosition.Center -> androidx.compose.material3.FabPosition.Center
            else -> androidx.compose.material3.FabPosition.End
        }
    }

    AdaptiveWidget(
        adaptation = remember { TopAppBarScaffoldAdaptation() },
        adaptationScope = adaptation,
        material = {
            M3TopAppBarScaffold(
                modifier = modifier,
                topBarModifier = topBarModifier,
                title = title,
                showTopBar = showTopBar,
                navigationIcon = navigationIcon,
                actions = actions,
                topBarWindowInsets = it.topBarWindowInsets,
                bottomBar = bottomBar,
                snackbarHost = snackbarHost,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = fabPosition.transform(),
                topAppBarColors = it.colors,
                isCenterAligned = it.isCenterAligned,
                scaffoldContainerColor = it.scaffoldContainerColor,
                scaffoldContentColor = it.scaffoldContentColor,
                contentWindowInsets = it.contentWindowInsets,
                scrollBehavior = it.scrollBehavior,
                isScrollTint = it.isScrollTint,
                content = content
            )
        },
        cupertino = {
            CupertinoScaffold(
                modifier = modifier,
                topBar = {
                    Box {
                        AnimatedVisibility(
                            visible = showTopBar,
                            enter = expandVertically(spring(1.2f)) + fadeIn(spring(1.2f)),
                            exit = shrinkVertically(spring(1.2f)) + fadeOut(spring(1.2f))
                        ) {
                            CupertinoTopAppBar(
                                title = title,
                                modifier = modifier,
                                navigationIcon = navigationIcon,
                                actions = actions,
                                windowInsets = it.topBarWindowInsets,
                                isCenterAligned = it.isCenterAligned,
                                isTransparent = it.isTransparent,
                                isTranslucent = it.isTranslucent,
                                colors = it.colors
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
                floatingActionButtonPosition = higFabPosition,
                containerColor = it.scaffoldContainerColor,
                contentColor = it.scaffoldContentColor,
                contentWindowInsets = it.contentWindowInsets,
                content = content
            )
        }
    )
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveTopAppBarScaffold(
    modifier: Modifier = Modifier,
    topBarModifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    showTopBar: Boolean = true,
    navigationIcon: @Composable () -> Unit = {},
    actions: List<ActionMenuItem> = listOf(),
    primaryAction: ActionMenuItem.IconMenuItem? = null,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    fabPosition: FabPosition = FabPosition.Center,
    higFabPosition: FabPosition = fabPosition,
    adaptation: AdaptationScope<HigTopAppBarScaffoldAdaptation, M3TopAppBarScaffoldAdaptation>.() -> Unit = LocalTopBarScaffoldAdaptation.current,
    content: @Composable (PaddingValues) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val scaffold: @Composable (@Composable (RowScope.() -> Unit)) -> Unit = { actions ->
        AdaptiveTopAppBarScaffold(
            modifier = modifier,
            topBarModifier = topBarModifier,
            title = title,
            showTopBar = showTopBar,
            navigationIcon = navigationIcon,
            actions = actions,
            bottomBar = bottomBar,
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            fabPosition = fabPosition,
            higFabPosition = higFabPosition,
            adaptation = adaptation,
            content = content
        )
    }
    AdaptiveWidget(
        adaptation = remember { TopAppBarScaffoldAdaptation() },
        adaptationScope = adaptation,
        material = {
            scaffold {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    M3ActionsMenu(
                        items = primaryAction?.let { actions + it } ?: actions,
                        isOpen = menuExpanded,
                        closeDropdown = { menuExpanded = false },
                        onToggleOverflow = { menuExpanded = !menuExpanded },
                        maxVisibleItems = 5,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        },
        cupertino = {
            scaffold {
                if (actions.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))

                    @Composable
                    fun liquidButton(
                        onClick: () -> Unit,
                        isIconButton: Boolean,
                        enabled: Boolean = true,
                        colors: CupertinoLiquidButtonColors = glassButtonColors(),
                        backdrop: Backdrop,
                        isBackgroundAdaptive: Boolean = true,
                        content: @Composable () -> Unit
                    ) {
                        if (isIconButton) {
                            CupertinoLiquidIconButton(
                                onClick = onClick,
                                enabled = enabled,
                                colors = colors,
                                backdrop = backdrop,
                                isBackgroundAdaptive = isBackgroundAdaptive,
                            ) {
                                content()
                            }
                        } else {
                            CupertinoLiquidButton(
                                onClick = onClick,
                                enabled = enabled,
                                colors = colors,
                                backdrop = backdrop,
                                isBackgroundAdaptive = isBackgroundAdaptive
                            ) {
                                content()
                            }
                        }
                    }

                    liquidButton(
                        onClick = {},
                        isIconButton = actions.size == 1 && actions.first().let { it is ActionMenuItem.IconMenuItem && it.icon != null },
                        backdrop = it.backdrop,
                        isBackgroundAdaptive = it.isBackgroundAdaptive
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            HigActionsMenu(
                                items = actions,
                                isOpen = menuExpanded,
                                closeDropdown = { menuExpanded = false },
                                onToggleOverflow = { menuExpanded = !menuExpanded },
                                maxVisibleItems = 3,
                            )
                        }
                    }

                    primaryAction?.let { action ->
                        liquidButton(
                            onClick = action.onClick,
                            isIconButton = action.icon != null,
                            enabled = action.enabled,
                            colors = glassProminentButtonColors(),
                            backdrop = it.backdrop,
                            isBackgroundAdaptive = it.isBackgroundAdaptive
                        ) {
                            HigActionMenu(action)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
class M3TopAppBarScaffoldAdaptation internal constructor(
    topBarWindowInsets: WindowInsets,
    contentWindowInsets: WindowInsets,
    scrollBehavior: TopAppBarScrollBehavior,
    isScrollTint: Boolean,
    colors: TopAppBarColors,
    isCenterAligned: Boolean = false,
    scaffoldContainerColor: Color,
    scaffoldContentColor: Color
) {
    var topBarWindowInsets: WindowInsets by mutableStateOf(topBarWindowInsets)
    var contentWindowInsets: WindowInsets by mutableStateOf(contentWindowInsets)
    var scrollBehavior: TopAppBarScrollBehavior by mutableStateOf(scrollBehavior)
    var isScrollTint: Boolean by mutableStateOf(isScrollTint)
    var colors: TopAppBarColors by mutableStateOf(colors)
    var isCenterAligned: Boolean by mutableStateOf(isCenterAligned)
    var scaffoldContainerColor: Color by mutableStateOf(scaffoldContainerColor)
    var scaffoldContentColor: Color by mutableStateOf(scaffoldContentColor)
}

class HigTopAppBarScaffoldAdaptation internal constructor(
    topBarWindowInsets: WindowInsets,
    contentWindowInsets: WindowInsets,
    isBackgroundAdaptive: Boolean = true,
    backdrop: LayerBackdrop,
    isCenterAligned: Boolean = true,
    isTransparent: Boolean = false,
    isTranslucent: Boolean = true,
    colors: CupertinoTopAppBarColors,
    scaffoldContainerColor: Color,
    scaffoldContentColor: Color
) {
    var topBarWindowInsets: WindowInsets by mutableStateOf(topBarWindowInsets)
    var contentWindowInsets: WindowInsets by mutableStateOf(contentWindowInsets)
    var isBackgroundAdaptive: Boolean by mutableStateOf(isBackgroundAdaptive)
    var backdrop: LayerBackdrop by mutableStateOf(backdrop)
    var isCenterAligned: Boolean by mutableStateOf(isCenterAligned)
    var isTransparent: Boolean by mutableStateOf(isTransparent)
    var isTranslucent: Boolean by mutableStateOf(isTranslucent)
    var colors: CupertinoTopAppBarColors by mutableStateOf(colors)
    var scaffoldContainerColor: Color by mutableStateOf(scaffoldContainerColor)
    var scaffoldContentColor: Color by mutableStateOf(scaffoldContentColor)
}

@OptIn(ExperimentalAdaptiveApi::class)
internal class TopAppBarScaffoldAdaptation: Adaptation<HigTopAppBarScaffoldAdaptation, M3TopAppBarScaffoldAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigTopAppBarScaffoldAdaptation {
        val topBarWindowInsets = CupertinoTopAppBarDefaults.windowInsets
        val contentWindowInsets = CupertinoScaffoldDefaults.contentWindowInsets
        val isBackgroundAdaptive = true
        val backdrop = rememberLayerBackdrop()
        val isCenterAligned = true
        val isTransparent = false
        val isTranslucent = true
        val colors = CupertinoTopAppBarDefaults.topAppBarColors()
        val scaffoldContainerColor = CupertinoScaffoldDefaults.containerColor
        val scaffoldContentColor = CupertinoScaffoldDefaults.contentColor

        return remember(topBarWindowInsets, contentWindowInsets, isBackgroundAdaptive, backdrop, isCenterAligned, isTransparent, isTranslucent, colors, scaffoldContainerColor, scaffoldContentColor) {
            HigTopAppBarScaffoldAdaptation(
                topBarWindowInsets = topBarWindowInsets,
                contentWindowInsets = contentWindowInsets,
                isBackgroundAdaptive = isBackgroundAdaptive,
                backdrop = backdrop,
                isCenterAligned = isCenterAligned,
                isTransparent = isTransparent,
                isTranslucent = isTranslucent,
                colors = colors,
                scaffoldContainerColor = scaffoldContainerColor,
                scaffoldContentColor = scaffoldContentColor
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun rememberMaterialAdaptation(): M3TopAppBarScaffoldAdaptation {
        val topBarWindowInsets = TopAppBarDefaults.windowInsets
        val contentWindowInsets = ScaffoldDefaults.contentWindowInsets
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val isScrollTint = LocalIsScrollTint.current
        val colors = TopAppBarDefaults.topAppBarColors()
        val isCenterAligned = false
        val scaffoldContainerColor = MaterialTheme.colorScheme.background
        val scaffoldContentColor = contentColorFor(scaffoldContainerColor)

        return remember(topBarWindowInsets, contentWindowInsets, scrollBehavior, isScrollTint, colors, isCenterAligned) {
            M3TopAppBarScaffoldAdaptation(
                topBarWindowInsets = topBarWindowInsets,
                contentWindowInsets = contentWindowInsets,
                scrollBehavior = scrollBehavior,
                isScrollTint = isScrollTint,
                colors = colors,
                isCenterAligned = isCenterAligned,
                scaffoldContainerColor = scaffoldContainerColor,
                scaffoldContentColor = scaffoldContentColor
            )
        }
    }
}