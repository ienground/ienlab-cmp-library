package zone.ien.utils.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenBadge
import zone.ien.utils.ui.interactive.IenBadgeSize
import zone.ien.utils.ui.interactive.IenBadgeVariant
import zone.ien.utils.ui.interactive.toneGradientBrush
import zone.ien.utils.ui.utils.shakeOnDisabledClick
import zone.ien.utils.ui.primitives.IenIcon

// ─── CompositionLocals ───────────────────────────────────────────────────────

/**
 * 현재 선택된 네비게이션 바 항목의 인덱스를 제공하는 CompositionLocal입니다.
 */
internal val LocalNavigationBarSelectedIndex = compositionLocalOf { -1 }

/** Material 네비게이션 바의 시각적 유형입니다. */
enum class IenNavigationBarType {
    /** 기본 NavigationBar 디자인입니다. */
    Type1,

    /** 플로팅 캡슐 디자인입니다. */
    Type2,
}

internal val LocalIenNavigationBarType = compositionLocalOf { IenNavigationBarType.Type1 }

/**
 * 네비게이션 바 및 하위 항목에서 사용할 색상 구성을 제공하는 CompositionLocal입니다.
 */
internal val LocalNavigationBarColors = compositionLocalOf {
    IenNavigationBarColors(
        containerColor = Color.Unspecified,
        selectedItemBackgroundColor = Color.Unspecified,
        selectedIconColor = Color.Unspecified,
        selectedTextColor = Color.Unspecified,
        unselectedIconColor = Color.Unspecified,
        unselectedTextColor = Color.Unspecified,
    )
}

/**
 * 네비게이션 바에서 각 항목의 가로 위치 정보(왼쪽 시작 지점과 너비)를 나타내는 데이터 클래스입니다.
 *
 * @property left 항목의 왼쪽 시작 지점 (Dp)
 * @property width 항목의 너비 (Dp)
 */
internal data class NavigationBarItemBounds(
    val left: Dp,
    val width: Dp,
)

/**
 * 네비게이션 바 항목의 경계 정보(위치 및 너비)를 업데이트하는 콜백 함수를 제공하는 CompositionLocal입니다.
 */
internal val LocalNavigationBarItemBoundsUpdater = compositionLocalOf<(Int, NavigationBarItemBounds) -> Unit> {
    { _, _ -> }
}

/**
 * 네비게이션 바 항목의 아이콘과 라벨 배치 방향입니다.
 */
enum class IenNavigationBarItemDirection {
    Horizontal,
    Vertical,
}

// ─── Colors ──────────────────────────────────────────────────────────────────

/**
 * 사용자 정의 네비게이션 바([IenNavigationBar])의 색상 구성 정보를 담는 데이터 클래스입니다.
 *
 * @property containerColor 네비게이션 바의 배경색
 * @property selectedItemBackgroundColor 선택된 항목의 배경색 (인디케이터 색상)
 * @property selectedIconColor 선택된 항목의 아이콘 색상
 * @property selectedTextColor 선택된 항목의 텍스트 색상
 * @property unselectedIconColor 선택되지 않은 항목의 아이콘 색상
 * @property unselectedTextColor 선택되지 않은 항목의 텍스트 색상
 */
@Immutable
data class IenNavigationBarColors(
    val containerColor: Color,
    val selectedItemBackgroundColor: Color,
    val selectedIconColor: Color,
    val selectedTextColor: Color,
    val unselectedIconColor: Color,
    val unselectedTextColor: Color,
)

/**
 * 사용자 정의 네비게이션 바의 기본값 및 색상 생성을 위한 유틸리티 객체입니다.
 */
object IenNavigationBarDefaults {
    /**
     * [IenNavigationBar]에 적용할 색상 구성을 생성합니다.
     *
     * @param containerColor 네비게이션 바의 배경색
     * @param selectedItemBackgroundColor 선택된 항목의 배경색
     * @param selectedIconColor 선택된 항목의 아이콘 색상
     * @param selectedTextColor 선택된 항목의 텍스트 색상
     * @param unselectedIconColor 선택되지 않은 항목의 아이콘 색상
     * @param unselectedTextColor 선택되지 않은 항목의 텍스트 색상
     */
    @Composable
    fun colors(
        containerColor: Color = IenTheme.colors.brand,
        selectedItemBackgroundColor: Color = IenTheme.colors.surface,
        selectedIconColor: Color = IenTheme.colors.brand,
        selectedTextColor: Color = IenTheme.colors.brand,
        unselectedIconColor: Color = Color.White.copy(alpha = 0.72f),
        unselectedTextColor: Color = Color.White.copy(alpha = 0.72f),
    ) = IenNavigationBarColors(
        containerColor = containerColor,
        selectedItemBackgroundColor = selectedItemBackgroundColor,
        selectedIconColor = selectedIconColor,
        selectedTextColor = selectedTextColor,
        unselectedIconColor = unselectedIconColor,
        unselectedTextColor = unselectedTextColor,
    )

    /** [IenNavigationBar2]에 적용할 기본 색상 구성을 생성합니다. */
    @Composable
    fun type2Colors() = IenNavigationBarColors(
        containerColor = IenTheme.colors.surface,
        selectedItemBackgroundColor = IenTheme.colors.surface,
        selectedIconColor = IenTheme.colors.textPrimary,
        selectedTextColor = IenTheme.colors.textPrimary,
        unselectedIconColor = IenTheme.colors.textSecondary,
        unselectedTextColor = IenTheme.colors.textSecondary,
    )
}

// ─── IenNavigationBar ─────────────────────────────────────────────────────

/**
 * IenNavigationBar는 사용자 정의 네비게이션 바를 표시하기 위한 컴포저블입니다.
 *
 * @param selectedIndex 선택된 항목 인덱스
 * @param itemCount 항목 개수
 * @param modifier 적용할 Modifier
 * @param colors 색상
 * @param windowInsets 윈도우 인셋
 * @param visible 네비게이션 바 표시 여부
 * @param content 항목 내용
 */
@Composable
fun IenNavigationBar(
    selectedIndex: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    colors: IenNavigationBarColors = IenNavigationBarDefaults.colors(),
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    visible: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    IenNavigationBarImpl(
        selectedIndex = selectedIndex,
        itemCount = itemCount,
        modifier = modifier,
        colors = colors,
        windowInsets = windowInsets,
        visible = visible,
        content = content,
    )
}

/**
 * 플로팅 캡슐 디자인의 두 번째 네비게이션 바 유형입니다.
 *
 * @param selectedIndex 선택된 항목 인덱스
 * @param itemCount 항목 개수
 * @param modifier 적용할 Modifier
 * @param colors 색상
 * @param windowInsets 윈도우 인셋
 * @param visible 네비게이션 바 표시 여부
 * @param content 항목 내용
 */
@Composable
fun IenNavigationBar2(
    selectedIndex: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    colors: IenNavigationBarColors = IenNavigationBarDefaults.type2Colors(),
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    visible: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val navBarPadding = windowInsets.asPaddingValues()
    val layoutDirection = LocalLayoutDirection.current

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
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = navBarPadding.calculateStartPadding(layoutDirection),
                    end = navBarPadding.calculateEndPadding(layoutDirection),
                    bottom = navBarPadding.calculateBottomPadding(),
                ),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 8.dp, vertical = 18.dp),
            ) {
                Surface(
                    color = colors.containerColor,
                    shape = ContinuousCapsule(),
                    tonalElevation = 0.dp,
                    shadowElevation = 18.dp,
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .height(78.dp),
                ) {
                    CompositionLocalProvider(
                        LocalNavigationBarSelectedIndex provides selectedIndex,
                        LocalNavigationBarColors provides colors,
                        LocalIenNavigationBarType provides IenNavigationBarType.Type2,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .selectableGroup()
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            content = content,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IenNavigationBarImpl(
    selectedIndex: Int,
    itemCount: Int,
    modifier: Modifier,
    colors: IenNavigationBarColors,
    windowInsets: WindowInsets,
    visible: Boolean,
    content: @Composable RowScope.() -> Unit,
) {
    val navBarPadding = windowInsets.asPaddingValues()
    val layoutDirection = LocalLayoutDirection.current
    val itemBounds = remember { mutableStateMapOf<Int, NavigationBarItemBounds>() }
    val selectedBounds = itemBounds[selectedIndex]
    val indicatorOffset by animateDpAsState(
        targetValue = selectedBounds?.left ?: 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "navigationBarIndicatorOffset",
    )
    val indicatorWidth by animateDpAsState(
        targetValue = selectedBounds?.width ?: 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "navigationBarIndicatorWidth",
    )

    CompositionLocalProvider(
        LocalNavigationBarSelectedIndex provides selectedIndex,
        LocalNavigationBarColors provides colors,
        LocalNavigationBarItemBoundsUpdater provides { index, bounds -> itemBounds[index] = bounds },
        LocalIenNavigationBarType provides IenNavigationBarType.Type1,
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
            modifier = modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = navBarPadding.calculateStartPadding(layoutDirection),
                        end = navBarPadding.calculateEndPadding(layoutDirection),
                        bottom = navBarPadding.calculateBottomPadding(),
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                ) {
                    val containerShape = ContinuousCapsule()
                    Surface(
                        color = Color.Transparent,
                        shape = containerShape,
                        tonalElevation = 0.dp,
                        shadowElevation = 18.dp,
                        modifier = Modifier
                            .height(78.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(
                                    brush = if (colors.containerColor == IenTheme.colors.brand) {
                                        toneGradientBrush(IenSemanticTone.Brand)
                                    } else {
                                        androidx.compose.ui.graphics.SolidColor(colors.containerColor)
                                    },
                                    shape = containerShape,
                                )
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                        ) {
                            if (selectedBounds != null) {
                                Box(
                                    modifier = Modifier
                                        .offset(x = indicatorOffset)
                                        .width(indicatorWidth)
                                        .fillMaxHeight()
                                        .background(
                                            color = colors.selectedItemBackgroundColor,
                                            shape = ContinuousCapsule(),
                                        )
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxHeight(),
                            ) {
                                content()
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── IenNavigationBarItem ─────────────────────────────────────────────────

/**
 * IenNavigationBarItem는 네비게이션 바 항목을 표시하기 위한 컴포저블입니다.
 *
 * @param index 항목 인덱스
 * @param onClick 클릭 시 호출되는 콜백 함수
 * @param icon 아이콘
 * @param label 라벨
 * @param direction 아이콘과 라벨 배치 방향
 * @param alwaysShowLabel 항상 라벨 표시 여부
 * @param enabled 활성화 여부
 * @param modifier 적용할 Modifier
 * @param badge 표시할 배지 숫자. 0이면 숨기고, 음수이면 점으로 표시하며, 100 이상은 `99+`로 표시합니다.
 * @param selectedIcon 선택된 상태에서 표시할 아이콘. 기본값은 [icon]입니다.
 */
@Composable
fun RowScope.IenNavigationBarItem(
    index: Int,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    direction: IenNavigationBarItemDirection = IenNavigationBarItemDirection.Horizontal,
    alwaysShowLabel: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    badge: Int = 0,
    selectedIcon: (@Composable () -> Unit)? = null,
) {
    if (LocalIenNavigationBarType.current == IenNavigationBarType.Type2) {
        IenNavigationBar2Item(
            index = index,
            onClick = onClick,
            icon = icon,
            selectedIcon = selectedIcon,
            label = label,
            direction = direction,
            alwaysShowLabel = alwaysShowLabel,
            enabled = enabled,
            modifier = modifier,
            badge = badge,
        )
        return
    }

    val selectedIndex = LocalNavigationBarSelectedIndex.current
    val colors = LocalNavigationBarColors.current
    val updateItemBounds = LocalNavigationBarItemBoundsUpdater.current
    val density = LocalDensity.current
    val selected = index == selectedIndex
    val itemIcon = if (selected) selectedIcon ?: icon else icon
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val iconColor by animateColorAsState(
        targetValue = when {
            !enabled && selected -> IenTheme.colors.onBrandWeak.copy(alpha = IenTheme.state.disabledAlpha)
            !enabled -> colors.unselectedIconColor.copy(alpha = 0.38f)
            selected -> colors.selectedIconColor
            else -> colors.unselectedIconColor
        },
        label = "iconColor"
    )
    val textColor by animateColorAsState(
        targetValue = when {
            !enabled && selected -> colors.selectedTextColor.copy(alpha = IenTheme.state.disabledAlpha)
            !enabled -> colors.unselectedTextColor.copy(alpha = 0.38f)
            selected -> colors.selectedTextColor
            else -> colors.unselectedTextColor
        },
        label = "textColor"
    )

    val pressScale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.975f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh,
        ),
        label = "itemPressScale",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .then(
                when {
                    direction == IenNavigationBarItemDirection.Vertical && (selected || alwaysShowLabel) ->
                        Modifier.widthIn(min = 64.dp)
                    selected || alwaysShowLabel -> Modifier
                    else -> Modifier.width(48.dp)
                }
            )
            .fillMaxHeight()
            .onGloballyPositioned { coordinates ->
                updateItemBounds(
                    index,
                    NavigationBarItemBounds(
                        left = with(density) { coordinates.positionInParent().x.toDp() },
                        width = with(density) { coordinates.size.width.toDp() },
                    )
                )
            }
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .shakeOnDisabledClick(enabled)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick
            )
    ) {
        val showLabel = alwaysShowLabel || selected

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .then(
                    when {
                        direction == IenNavigationBarItemDirection.Vertical ->
                            Modifier.padding(horizontal = 4.dp)
                        showLabel -> Modifier.padding(horizontal = 24.dp)
                        else -> Modifier.width(48.dp)
                    }
                )
                .fillMaxHeight()
        ) {
            when (direction) {
                IenNavigationBarItemDirection.Horizontal -> {
                    CompositionLocalProvider(LocalContentColor provides iconColor) {
                        IenNavigationBarItemIcon(badge = badge, icon = itemIcon)
                    }

                    if (showLabel) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(IntrinsicSize.Max),
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))
                            ProvideTextStyle(
                                IenTheme.typography.label2.copy(color = textColor)
                            ) {
                                label()
                            }
                        }
                    }
                }

                IenNavigationBarItemDirection.Vertical -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        CompositionLocalProvider(LocalContentColor provides iconColor) {
                            IenNavigationBarItemIcon(badge = badge, icon = itemIcon)
                        }

                        if (showLabel) {
                            Spacer(modifier = Modifier.height(3.dp))
                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                ProvideTextStyle(
                                    IenTheme.typography.label2.copy(color = textColor)
                                ) {
                                    label()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.IenNavigationBar2Item(
    index: Int,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    selectedIcon: (@Composable () -> Unit)?,
    label: @Composable () -> Unit,
    direction: IenNavigationBarItemDirection,
    alwaysShowLabel: Boolean,
    enabled: Boolean,
    modifier: Modifier,
    badge: Int,
) {
    val selected = index == LocalNavigationBarSelectedIndex.current
    val colors = LocalNavigationBarColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val selectedBounce = remember { Animatable(1f) }
    val itemScale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh,
        ),
        label = "ienNavigationBar2ItemPressScale",
    )
    val iconColor by animateColorAsState(
        targetValue = if (enabled) {
            if (selected) colors.selectedIconColor else colors.unselectedIconColor
        } else {
            IenTheme.colors.textDisabled
        },
        animationSpec = tween(
            durationMillis = IenTheme.motion.fastMillis,
            easing = IenTheme.motion.standardEasing,
        ),
        label = "ienNavigationBar2ItemIconColor",
    )
    val textColor by animateColorAsState(
        targetValue = if (enabled) {
            if (selected) colors.selectedTextColor else colors.unselectedTextColor
        } else {
            IenTheme.colors.textDisabled
        },
        animationSpec = tween(
            durationMillis = IenTheme.motion.fastMillis,
            easing = IenTheme.motion.standardEasing,
        ),
        label = "ienNavigationBar2ItemTextColor",
    )

    LaunchedEffect(selected) {
        if (!selected) {
            selectedBounce.snapTo(1f)
            return@LaunchedEffect
        }

        selectedBounce.snapTo(1f)
        selectedBounce.animateTo(
            targetValue = 1f,
            animationSpec = keyframes {
                durationMillis = 360
                1f at 0
                1.16f at 110
                0.96f at 230
                1f at 360
            },
        )
    }

    val showLabel = alwaysShowLabel || selected
    val itemSizeModifier = if (alwaysShowLabel) {
        Modifier.weight(1f).widthIn(min = 54.dp)
    } else if (selected) {
        Modifier.weight(1.75f).widthIn(min = 112.dp)
    } else {
        Modifier.weight(1f).widthIn(min = 54.dp)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .then(itemSizeModifier)
            .height(66.dp)
            .graphicsLayer {
                scaleX = itemScale * selectedBounce.value
                scaleY = itemScale * selectedBounce.value
            }
            .shakeOnDisabledClick(enabled)
            .clickable(
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .semantics { this.selected = selected },
    ) {
        val navigationBarIcon: @Composable () -> Unit = {
            Box(
                modifier = Modifier.size(30.dp),
                contentAlignment = Alignment.Center,
            ) {
                CompositionLocalProvider(LocalContentColor provides iconColor) {
                    (if (selected) selectedIcon ?: icon else icon).invoke()
                }
                if (badge != 0) {
                    IenNavigationBarBadge(
                        badge = badge,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-1).dp)
                            .wrapContentSize(Alignment.TopEnd, unbounded = true),
                    )
                }
            }
        }
        val navigationBarLabel: @Composable () -> Unit = {
            ProvideTextStyle(IenTheme.typography.label2.copy(color = textColor)) {
                label()
            }
        }

        when (direction) {
            IenNavigationBarItemDirection.Horizontal -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    navigationBarIcon()
                    if (showLabel) {
                        Spacer(modifier = Modifier.width(8.dp))
                        navigationBarLabel()
                    }
                }
            }

            IenNavigationBarItemDirection.Vertical -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    navigationBarIcon()
                    if (showLabel) {
                        Spacer(modifier = Modifier.height(5.dp))
                        navigationBarLabel()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IenNavigationBarItemIcon(
    badge: Int,
    icon: @Composable () -> Unit,
) {
    if (badge == 0) {
        icon()
    } else {
        BadgedBox(
            badge = { IenNavigationBarBadge(badge) },
            content = { icon() },
        )
    }
}

@Composable
private fun IenNavigationBarBadge(
    badge: Int,
    modifier: Modifier = Modifier,
) {
    if (badge < 0) {
        Box(
            modifier = modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(IenTheme.colors.danger),
        )
    } else {
        IenBadge(
            text = if (badge > 99) "99+" else badge.toString(),
            modifier = modifier,
            size = IenBadgeSize.Small,
            variant = IenBadgeVariant.Fill,
            tone = IenSemanticTone.Danger,
        )
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFEEF0F8)
@Composable
private fun IenNavigationBarPreview() {
    var selectedIndex by remember { mutableStateOf(0) }

    IenTheme {
        IenNavigationBar(
            selectedIndex = selectedIndex,
            itemCount = 4,
            windowInsets = WindowInsets(0.dp)
        ) {
            IenNavigationBarItem(
                index = 0,
                onClick = { selectedIndex = 0 },
                icon = { IenIcon(SystemIcons.Save, contentDescription = null) },
                label = { Text("홈") }
            )
            IenNavigationBarItem(
                index = 1,
                onClick = { selectedIndex = 1 },
                icon = { IenIcon(SystemIcons.Edit, contentDescription = null) },
                label = { Text("기록") }
            )
            IenNavigationBarItem(
                index = 2,
                onClick = { selectedIndex = 2 },
                icon = { IenIcon(SystemIcons.Schedule, contentDescription = null) },
                label = { Text("통계") },
                badge = 3,
            )
            IenNavigationBarItem(
                index = 3,
                onClick = { selectedIndex = 3 },
                icon = { IenIcon(SystemIcons.Delete, contentDescription = null) },
                label = { Text("설정") }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF0F8, name = "Index 1 Selected")
@Composable
private fun IenNavigationBarPreview1() {
    IenTheme {
        IenNavigationBar(
            selectedIndex = 1,
            itemCount = 4,
            windowInsets = WindowInsets(0.dp)
        ) {
            IenNavigationBarItem(
                index = 0,
                onClick = {},
                icon = { IenIcon(SystemIcons.Save, contentDescription = null) },
                label = { Text("홈") }
            )
            IenNavigationBarItem(
                index = 1,
                onClick = {},
                icon = { IenIcon(SystemIcons.Edit, contentDescription = null) },
                label = { Text("기록") }
            )
            IenNavigationBarItem(
                index = 2,
                onClick = {},
                icon = { IenIcon(SystemIcons.Schedule, contentDescription = null) },
                label = { Text("통계") }
            )
            IenNavigationBarItem(
                index = 3,
                onClick = {},
                icon = { IenIcon(SystemIcons.Delete, contentDescription = null) },
                label = { Text("설정") }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF0F8, name = "Disabled Item")
@Composable
private fun IenNavigationBarPreviewDisabled() {
    IenTheme {
        IenNavigationBar(
            selectedIndex = 0,
            itemCount = 4,
            windowInsets = WindowInsets(0.dp)
        ) {
            IenNavigationBarItem(
                index = 0,
                onClick = {},
                icon = { IenIcon(SystemIcons.Save, contentDescription = null) },
                label = { Text("홈") }
            )
            IenNavigationBarItem(
                index = 1,
                onClick = {},
                enabled = false,
                icon = { IenIcon(SystemIcons.Edit, contentDescription = null) },
                label = { Text("기록") }
            )
            IenNavigationBarItem(
                index = 2,
                onClick = {},
                enabled = false,
                icon = { IenIcon(SystemIcons.Schedule, contentDescription = null) },
                label = { Text("통계") }
            )
            IenNavigationBarItem(
                index = 3,
                onClick = {},
                icon = { IenIcon(SystemIcons.Delete, contentDescription = null) },
                label = { Text("설정") }
            )
        }
    }
}
