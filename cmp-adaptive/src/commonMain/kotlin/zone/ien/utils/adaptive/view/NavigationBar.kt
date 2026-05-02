package zone.ien.utils.adaptive.view

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import zone.ien.hig.CupertinoNavigationBar
import zone.ien.hig.CupertinoNavigationBarColors
import zone.ien.hig.CupertinoNavigationBarDefaults
import zone.ien.hig.CupertinoNavigationBarItem
import zone.ien.hig.CupertinoNavigationBarItemData
import zone.ien.hig.CupertinoNavigationBarNative
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.currentTheme
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.view.CustomNavigationBar
import zone.ien.utils.ui.view.CustomNavigationBarColors
import zone.ien.utils.ui.view.CustomNavigationBarDefaults
import zone.ien.utils.ui.view.CustomNavigationBarItem

data class NavigationBarItem(
    val onClick: () -> Unit,
    val icon: IconData,
    val selectedIcon: IconData? = null,
    val label: String
)

internal data class NavigationBarState(
    val selectedTabIndex: () -> Int,
    val onTabSelected: (Int) -> Unit,
)
internal val LocalNavigationBarState = compositionLocalOf<NavigationBarState?> { null }

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveNavigationBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: () -> Int,
    onTabSelected: (index: Int) -> Unit,
    adaptation: AdaptationScope<CupertinoNavigationBarAdaptation, MaterialNavigationBarAdaptation>.() -> Unit = {},
    isNative: Boolean = true,
    items: List<NavigationBarItem>
) {
    if (isNative) {
        AdaptiveNavigationBar(
            modifier = modifier,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = onTabSelected,
            tabsCount = items.size,
            adaptation = adaptation,
        ) {
            items.forEachIndexed { index, item ->
                val selected = selectedTabIndex() == index

                AdaptiveNavigationBarItem(
                    index = index,
                    onClick = item.onClick,
                    icon = {
                        ComplexIcon(
                            icon = if (selected && currentTheme == Theme.Material3 && item.selectedIcon != null) item.selectedIcon else item.icon
                        )
                    },
                    label = { Text(text = item.label) }
                )
            }
        }
    } else {
        AdaptiveNavigationBarNative(
            modifier = modifier,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = onTabSelected,
            adaptation = adaptation,
            items = items.map {
                CupertinoNavigationBarItemData(
                    onClick = it.onClick,
                    icon = when (it.icon) {
                        is IconData.Vector -> rememberVectorPainter(it.icon.imageVector)
                        is IconData.Paint -> it.icon.painter
                    },
                    selectedIcon = it.selectedIcon?.let {
                        when (it) {
                            is IconData.Vector -> rememberVectorPainter(it.imageVector)
                            is IconData.Paint -> it.painter
                        }
                    },
                    label = it.label
                )
            }
        )
    }
}

@OptIn(ExperimentalCupertinoApi::class)
@ExperimentalAdaptiveApi
@Composable
private fun AdaptiveNavigationBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: () -> Int,
    onTabSelected: (index: Int) -> Unit,
    tabsCount: Int,
    adaptation: AdaptationScope<CupertinoNavigationBarAdaptation, MaterialNavigationBarAdaptation>.() -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    CompositionLocalProvider(
        LocalNavigationBarState provides NavigationBarState(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = onTabSelected,
        )
    ) {
        AdaptiveWidget(
            adaptation = remember {
                NavigationBarAdaptation()
            },
            adaptationScope = adaptation,
            cupertino = {
                CupertinoNavigationBar(
                    modifier = modifier,
                    colors = it.colors,
                    windowInsets = it.windowInsets,
                    backdrop = it.backdrop,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = onTabSelected,
                    tabsCount = tabsCount,
                    content = content
                )
            },
            material = {
                CustomNavigationBar(
                    modifier = modifier,
                    colors = it.colors,
                    selectedIndex = selectedTabIndex(),
                    itemCount = tabsCount,
                    windowInsets = it.windowInsets,
                    content = content
                )
            }
        )
    }
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
private fun AdaptiveNavigationBarNative(
    modifier: Modifier = Modifier,
    selectedTabIndex: () -> Int,
    onTabSelected: (index: Int) -> Unit,
    adaptation: AdaptationScope<CupertinoNavigationBarAdaptation, MaterialNavigationBarAdaptation>.() -> Unit = {},
    items: List<CupertinoNavigationBarItemData>
) {
    CompositionLocalProvider(
        LocalNavigationBarState provides NavigationBarState(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = onTabSelected,
        )
    ) {
        AdaptiveWidget(
            adaptation = remember {
                NavigationBarAdaptation()
            },
            adaptationScope = adaptation,
            cupertino = {
                CupertinoNavigationBarNative(
                    modifier = modifier,
                    colors = it.colors,
                    windowInsets = it.windowInsets,
                    backdrop = it.backdrop,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = onTabSelected,
                    items = items
                )
            },
            material = {
                CustomNavigationBar(
                    selectedIndex = selectedTabIndex(),
                    modifier = modifier,
                    colors = it.colors,
                    windowInsets = it.windowInsets,
                    itemCount = items.size,
                    content = {
                        items.forEachIndexed { index, item ->
                            val selected = index == selectedTabIndex()
                            CustomNavigationBarItem(
                                index = index,
                                onClick = item.onClick,
                                icon = {
                                    Icon(
                                        painter =
                                            if (selected && item.selectedIcon != null) item.selectedIcon!!
                                            else item.icon
                                        ,
                                        contentDescription = item.label,
                                    )
                                },
                                label = { Text(text = item.label) },
                                alwaysShowLabel = it.alwaysShowLabel
                            )
                        }
                    }
                )
            }
        )
    }
}

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun RowScope.AdaptiveNavigationBarItem(
    index: Int,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    adaptation: AdaptationScope<CupertinoNavigationBarItemAdaptation, MaterialNavigationBarItemAdaptation>.() -> Unit = {},
) {
    val navState = LocalNavigationBarState.current
    val resolvedOnClick: () -> Unit = {
        navState?.onTabSelected(index)
        onClick()
    }

    AdaptiveWidget(
        adaptation = remember {
            NavigationBarItemAdaptation()
        },
        adaptationScope = adaptation,
        cupertino = {
            CupertinoNavigationBarItem(
                onClick = resolvedOnClick,
                icon = icon,
                modifier = modifier,
                enabled = enabled,
                label = label,
                interactionSource = interactionSource,
            )
        },
        material = {
            CustomNavigationBarItem(
                index = index,
                onClick = resolvedOnClick,
                icon = icon,
                label = label ?: {},
                alwaysShowLabel = it.alwaysShowLabel,
                enabled = enabled,
                modifier = modifier
            )
        }
    )
}

class MaterialNavigationBarAdaptation internal constructor(
    colors: CustomNavigationBarColors,
    alwaysShowLabel: Boolean,
    windowInsets: WindowInsets,
) {
    var colors: CustomNavigationBarColors by mutableStateOf(colors)
    var alwaysShowLabel: Boolean by mutableStateOf(alwaysShowLabel)
    var windowInsets: WindowInsets by mutableStateOf(windowInsets)
}

@OptIn(ExperimentalCupertinoApi::class)
class CupertinoNavigationBarAdaptation internal constructor(
    colors: CupertinoNavigationBarColors,
    windowInsets: WindowInsets,
    backdrop: LayerBackdrop
) {
    var colors: CupertinoNavigationBarColors by mutableStateOf(colors)
    var windowInsets: WindowInsets by mutableStateOf(windowInsets)
    var backdrop: LayerBackdrop by mutableStateOf(backdrop)
}

@Stable
class MaterialNavigationBarItemAdaptation internal constructor(
    alwaysShowLabel: Boolean
) {
    var alwaysShowLabel by mutableStateOf(alwaysShowLabel)
}

@Stable
@OptIn(ExperimentalCupertinoApi::class)
class CupertinoNavigationBarItemAdaptation internal constructor()

@OptIn(ExperimentalAdaptiveApi::class)
@Stable
private class NavigationBarAdaptation: Adaptation<CupertinoNavigationBarAdaptation, MaterialNavigationBarAdaptation>() {
    @OptIn(ExperimentalCupertinoApi::class)
    @Composable
    override fun rememberCupertinoAdaptation(): CupertinoNavigationBarAdaptation {
        val colors = CupertinoNavigationBarDefaults.colors()
        val windowInsets = CupertinoNavigationBarDefaults.windowInsets
        val backdrop = rememberLayerBackdrop()

        return remember(colors, windowInsets, backdrop) {
            CupertinoNavigationBarAdaptation(
                colors = colors,
                windowInsets = windowInsets,
                backdrop = backdrop
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): MaterialNavigationBarAdaptation {
        val colors = CustomNavigationBarDefaults.colors()
        val alwaysShowLabel = true
        val windowInsets = NavigationBarDefaults.windowInsets

        return remember(colors, alwaysShowLabel, windowInsets) {
            MaterialNavigationBarAdaptation(
                colors = colors,
                alwaysShowLabel = alwaysShowLabel,
                windowInsets = windowInsets
            )
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class)
@Stable
private class NavigationBarItemAdaptation: Adaptation<CupertinoNavigationBarItemAdaptation, MaterialNavigationBarItemAdaptation>() {

    @OptIn(ExperimentalCupertinoApi::class)
    @Composable
    override fun rememberCupertinoAdaptation(): CupertinoNavigationBarItemAdaptation {
        return remember { CupertinoNavigationBarItemAdaptation() }
    }

    @Composable
    override fun rememberMaterialAdaptation(): MaterialNavigationBarItemAdaptation {
        val alwaysShowLabel = true

        return remember(alwaysShowLabel) {
            MaterialNavigationBarItemAdaptation(
                alwaysShowLabel = alwaysShowLabel
            )
        }
    }
}