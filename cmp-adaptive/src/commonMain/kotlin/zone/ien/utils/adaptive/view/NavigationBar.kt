package zone.ien.utils.adaptive.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
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
import zone.ien.utils.adaptive.screen.LocalBackdrop
import zone.ien.utils.adaptive.theme.ienCupertinoNavigationBarColors
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenBadge
import zone.ien.utils.ui.interactive.IenBadgeSize
import zone.ien.utils.ui.interactive.IenBadgeVariant
import zone.ien.utils.ui.view.IenNavigationBar
import zone.ien.utils.ui.view.IenNavigationBarColors
import zone.ien.utils.ui.view.IenNavigationBarDefaults
import zone.ien.utils.ui.view.IenNavigationBarItem
import zone.ien.utils.ui.view.IenNavigationBarItemDirection
import zone.ien.utils.ui.view.IenNavigationBar2
import zone.ien.utils.ui.view.IenNavigationBarType
import zone.ien.utils.ui.primitives.IenIcon

data class NavigationBarItem(
    val onClick: () -> Unit,
    val icon: IconData,
    val selectedIcon: IconData? = null,
    val label: String,
    val direction: IenNavigationBarItemDirection = IenNavigationBarItemDirection.Horizontal,
    /** 0이면 숨기고, 음수이면 점으로 표시하며, 100 이상은 `99+`로 표시하는 배지 값입니다. */
    val badge: Int = 0,
)

internal data class NavigationBarState(
    val selectedTabIndex: () -> Int,
    val onTabSelected: (Int) -> Unit,
)
internal val LocalNavigationBarState = compositionLocalOf<NavigationBarState?> { null }
private val LocalNavigationBarAlwaysShowLabel = compositionLocalOf { true }

/**
 * 적응형 네비게이션 바 컴포저블
 * 
 * Material 및 Cupertino 플랫폼에 따라 다르게 동작하는 네비게이션 바를 제공합니다.
 * 
 * @param modifier 네비게이션 바에 적용할 수정자
 * @param selectedTabIndex 현재 선택된 탭 인덱스를 반환하는 함수
 * @param onTabSelected 탭이 선택되었을 때 호출되는 콜백
 * @param adaptation 플랫폼별 적응형 설정을 위한 블록. Material에서는 `type`으로 `IenNavigationBarType.Type1` 또는 `Type2`를 지정할 수 있습니다.
 * @param isNative 네이티브 방식 사용 여부 (기본값: true). 배지가 있는 항목은 배지를 표시하는 컴포저블 방식을 사용합니다.
 * @param items 네비게이션 바에 표시할 아이템 목록
 * @param visible 네비게이션 바 표시 여부
 */
@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveNavigationBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: () -> Int,
    onTabSelected: (index: Int) -> Unit,
    adaptation: AdaptationScope<CupertinoNavigationBarAdaptation, IenNavigationBarAdaptation>.() -> Unit = {},
    isNative: Boolean = true,
    items: List<NavigationBarItem>,
    visible: Boolean = true,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = IenTheme.motion.fastMillis,
                easing = IenTheme.motion.standardEasing,
            )
        ) + slideInVertically(
            animationSpec = tween(
                durationMillis = IenTheme.motion.normalMillis,
                easing = IenTheme.motion.standardEasing,
            ),
            initialOffsetY = { it },
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = IenTheme.motion.fastMillis,
                easing = IenTheme.motion.standardEasing,
            )
        ) + slideOutVertically(
            animationSpec = tween(
                durationMillis = IenTheme.motion.normalMillis,
                easing = IenTheme.motion.standardEasing,
            ),
            targetOffsetY = { it },
        ),
        modifier = modifier,
    ) {
        if (isNative && items.none { it.badge != 0 }) {
            AdaptiveNavigationBarNative(
                modifier = Modifier,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = onTabSelected,
                adaptation = adaptation,
                directions = items.map { it.direction },
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
                modifier = Modifier,
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
                            IenIcon(
                                icon = if (selected && currentTheme == Theme.Material3 && item.selectedIcon != null) item.selectedIcon else item.icon
                            )
                        },
                        label = { Text(text = item.label) },
                        direction = item.direction,
                        badge = item.badge,
                    )
                }
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
                val navigationBarAdaptation = it
                val navigationBarContent: @Composable RowScope.() -> Unit = {
                    CompositionLocalProvider(
                        LocalNavigationBarAlwaysShowLabel provides navigationBarAdaptation.alwaysShowLabel
                    ) {
                        content()
                    }
                }
                val colors = navigationBarAdaptation.colorsFor(navigationBarAdaptation.type)
                when (navigationBarAdaptation.type) {
                    IenNavigationBarType.Type1 -> IenNavigationBar(
                        modifier = modifier,
                        colors = colors,
                        selectedIndex = selectedTabIndex(),
                        itemCount = tabsCount,
                        windowInsets = navigationBarAdaptation.windowInsets,
                        content = navigationBarContent,
                    )
                    IenNavigationBarType.Type2 -> IenNavigationBar2(
                        modifier = modifier,
                        colors = colors,
                        selectedIndex = selectedTabIndex(),
                        itemCount = tabsCount,
                        windowInsets = navigationBarAdaptation.windowInsets,
                        content = navigationBarContent,
                    )
                }
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
    directions: List<IenNavigationBarItemDirection>,
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
                val navigationBarAdaptation = it
                val navigationBarContent: @Composable RowScope.() -> Unit = {
                    items.forEachIndexed { index, item ->
                        val selected = index == selectedTabIndex()
                        IenNavigationBarItem(
                            index = index,
                            onClick = item.onClick,
                            icon = {
                                IenIcon(
                                    painter = if (selected) item.selectedIcon ?: item.icon else item.icon,
                                    contentDescription = item.label,
                                )
                            },
                            label = { Text(text = item.label) },
                            direction = directions[index],
                            alwaysShowLabel = navigationBarAdaptation.alwaysShowLabel,
                        )
                    }
                }
                val colors = navigationBarAdaptation.colorsFor(navigationBarAdaptation.type)
                when (navigationBarAdaptation.type) {
                    IenNavigationBarType.Type1 -> IenNavigationBar(
                        selectedIndex = selectedTabIndex(),
                        modifier = modifier,
                        colors = colors,
                        windowInsets = navigationBarAdaptation.windowInsets,
                        itemCount = items.size,
                        content = navigationBarContent,
                    )
                    IenNavigationBarType.Type2 -> IenNavigationBar2(
                        selectedIndex = selectedTabIndex(),
                        modifier = modifier,
                        colors = colors,
                        windowInsets = navigationBarAdaptation.windowInsets,
                        itemCount = items.size,
                        content = navigationBarContent,
                    )
                }
            }
        )
    }
}

/**
 * 적응형 네비게이션 바 항목입니다.
 *
 * @param badge 0이면 숨기고, 음수이면 점으로 표시하며, 100 이상은 `99+`로 표시하는 배지 값입니다.
 */
@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun RowScope.AdaptiveNavigationBarItem(
    index: Int,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    direction: IenNavigationBarItemDirection = IenNavigationBarItemDirection.Horizontal,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    badge: Int = 0,
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
                icon = {
                    NavigationBarItemIcon(
                        badge = badge,
                        icon = icon,
                    )
                },
                modifier = modifier,
                enabled = enabled,
                label = label,
                interactionSource = interactionSource,
            )
        },
        material = {
            IenNavigationBarItem(
                index = index,
                onClick = resolvedOnClick,
                icon = icon,
                label = label ?: {},
                direction = direction,
                alwaysShowLabel = it.alwaysShowLabel,
                enabled = enabled,
                modifier = modifier,
                badge = badge,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationBarItemIcon(
    badge: Int,
    icon: @Composable () -> Unit,
) {
    if (badge == 0) {
        icon()
    } else {
        BadgedBox(
            badge = {
                if (badge < 0) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(IenTheme.colors.danger),
                    )
                } else {
                    IenBadge(
                        text = if (badge > 99) "99+" else badge.toString(),
                        size = IenBadgeSize.Small,
                        variant = IenBadgeVariant.Fill,
                        tone = IenSemanticTone.Danger,
                    )
                }
            },
            content = { icon() },
        )
    }
}

/** Material 네비게이션 바의 표시 방식과 색상, 인셋 설정입니다. */
class IenNavigationBarAdaptation internal constructor(
    colors: IenNavigationBarColors,
    alwaysShowLabel: Boolean,
    windowInsets: WindowInsets,
) {
    /** Material 네비게이션 바 유형입니다. */
    var type: IenNavigationBarType by mutableStateOf(IenNavigationBarType.Type1)
    private val defaultColors = colors
    private var customColors by mutableStateOf<IenNavigationBarColors?>(null)
    var colors: IenNavigationBarColors
        get() = customColors ?: defaultColors
        set(value) {
            customColors = value
        }

    @Composable
    internal fun colorsFor(type: IenNavigationBarType): IenNavigationBarColors = customColors ?: when (type) {
        IenNavigationBarType.Type1 -> defaultColors
        IenNavigationBarType.Type2 -> IenNavigationBarDefaults.type2Colors()
    }

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
        val colors = ienCupertinoNavigationBarColors()
        val windowInsets = CupertinoNavigationBarDefaults.windowInsets
        val backdrop = LocalBackdrop.current ?: rememberLayerBackdrop()

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
        val colors = IenNavigationBarDefaults.colors()
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
        val alwaysShowLabel = LocalNavigationBarAlwaysShowLabel.current

        return remember(alwaysShowLabel) {
            IenNavigationBarItemAdaptation(
                alwaysShowLabel = alwaysShowLabel
            )
        }
    }
}
