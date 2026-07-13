package zone.ien.utils.adaptive.view

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
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

/**
 * 적응형 네비게이션 바 컴포저블
 * 
 * Material 및 Cupertino 플랫폼에 따라 다르게 동작하는 네비게이션 바를 제공합니다.
 * 
 * @param modifier 네비게이션 바에 적용할 수정자
 * @param selectedTabIndex 현재 선택된 탭 인덱스를 반환하는 함수
 * @param onTabSelected 탭이 선택되었을 때 호출되는 콜백
 * @param adaptation 플랫폼별 적응형 설정을 위한 블록
 * @param isNative 네이티브 방식 사용 여부 (기본값: true)
 * @param items 네비게이션 바에 표시할 아이템 목록
 */
@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveNavigationBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: () -> Int,
    onTabSelected: (index: Int) -> Unit,
    adaptation: AdaptationScope<CupertinoNavigationBarAdaptation, IenNavigationBarAdaptation>.() -> Unit = {},
    isNative: Boolean = true,
    items: List<NavigationBarItem>
) {
    if (isNative) {
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
    } else {
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
    adaptation: AdaptationScope<CupertinoNavigationBarAdaptation, IenNavigationBarAdaptation>.() -> Unit = {},
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
    adaptation: AdaptationScope<CupertinoNavigationBarAdaptation, IenNavigationBarAdaptation>.() -> Unit = {},
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
                                        painter = if (selected) item.selectedIcon ?: item.icon else item.icon,
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
    adaptation: AdaptationScope<CupertinoNavigationBarItemAdaptation, IenNavigationBarItemAdaptation>.() -> Unit = {},
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

class IenNavigationBarAdaptation internal constructor(
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
class IenNavigationBarItemAdaptation internal constructor(
    alwaysShowLabel: Boolean
) {
    var alwaysShowLabel by mutableStateOf(alwaysShowLabel)
}

@Stable
@OptIn(ExperimentalCupertinoApi::class)
class CupertinoNavigationBarItemAdaptation internal constructor()

@OptIn(ExperimentalAdaptiveApi::class)
@Stable
private class NavigationBarAdaptation: Adaptation<CupertinoNavigationBarAdaptation, IenNavigationBarAdaptation>() {
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
    override fun rememberMaterialAdaptation(): IenNavigationBarAdaptation {
        val colors = CustomNavigationBarDefaults.colors()
        val alwaysShowLabel = true
        val windowInsets = NavigationBarDefaults.windowInsets

        return remember(colors, alwaysShowLabel, windowInsets) {
            IenNavigationBarAdaptation(
                colors = colors,
                alwaysShowLabel = alwaysShowLabel,
                windowInsets = windowInsets
            )
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class)
@Stable
private class NavigationBarItemAdaptation: Adaptation<CupertinoNavigationBarItemAdaptation, IenNavigationBarItemAdaptation>() {

    @OptIn(ExperimentalCupertinoApi::class)
    @Composable
    override fun rememberCupertinoAdaptation(): CupertinoNavigationBarItemAdaptation {
        return remember { CupertinoNavigationBarItemAdaptation() }
    }

    @Composable
    override fun rememberMaterialAdaptation(): IenNavigationBarItemAdaptation {
        val alwaysShowLabel = true

        return remember(alwaysShowLabel) {
            IenNavigationBarItemAdaptation(
                alwaysShowLabel = alwaysShowLabel
            )
        }
    }
}
