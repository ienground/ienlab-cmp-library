package zone.ien.utils.adaptive.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.LayerBackdrop
import zone.ien.hig.CupertinoLiquidButton
import zone.ien.hig.CupertinoLiquidButtonColors
import zone.ien.hig.CupertinoLiquidButtonDefaults.glassButtonColors
import zone.ien.hig.CupertinoLiquidButtonDefaults.glassProminentButtonColors
import zone.ien.hig.CupertinoNavigationTitle
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
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.menu.HigActionMenu
import zone.ien.utils.adaptive.menu.HigActionsMenu
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.menu.IenActionsMenu
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import zone.ien.utils.ui.screen.LocalIsHigTopBarCenterAligned
import zone.ien.utils.ui.screen.LocalIsM3TopBarCenterAligned
import zone.ien.utils.ui.screen.LocalIsScrollTint
import zone.ien.utils.ui.screen.LocalTopBarMode
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenTopAppBar
import zone.ien.utils.ui.screen.TopBarMode
import zone.ien.utils.utils.ui.animateContentSizeWithoutClipping

/**
 * 적응형 상단바 스캐폴드 컴포저블
 *
 * @param modifier 레이아웃 수정자
 * @param topBarModifier 상단바 수정자
 * @param title 제목
 * @param subtitle 부제목
 * @param showTopBar 상단바 표시 여부. 기본값은 true
 * @param navigationIcon 뒤로가기 아이콘
 * @param actions 액션 버튼들
 * @param bottomBar 하단 바
 * @param snackbarHost 스낵바 호스트
 * @param floatingActionButton 플로팅 액션 버튼
 * @param fabPosition 플로팅 액션 버튼 위치
 * @param higFabPosition HIG 플로팅 액션 버튼 위치
 * @param adaptation 어댑테이션 설정
 * @param content 콘텐츠
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class,
    ExperimentalCupertinoApi::class
)
@Composable
fun AdaptiveTopAppBarScaffold(
    modifier: Modifier = Modifier,
    topBarModifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    subtitle: @Composable (() -> Unit)? = null,
    showTopBar: Boolean = true,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable (RowScope.() -> Unit))? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    fabPosition: FabPosition = FabPosition.Center,
    higFabPosition: FabPosition = fabPosition,
    contentEdge: IenScaffoldContentEdge = IenScaffoldContentEdge(),
    adaptation: AdaptationScope<HigTopAppBarScaffoldAdaptation, IenTopAppBarScaffoldAdaptation>.() -> Unit = LocalTopBarScaffoldAdaptation.current,
    content: @Composable (PaddingValues, @Composable () -> Unit) -> Unit
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
            val materialAdaptation = it
            val scaffoldCoordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }
            val topBarHeight = remember { mutableStateOf(0f) }
            var navigationTitleVisible by remember { mutableStateOf(true) }

            IenScaffold(
                modifier = modifier.onGloballyPositioned {
                    scaffoldCoordinates.value = it
                },
                topBar = {
                    Box {
                        AnimatedVisibility(
                            visible = showTopBar,
                            enter = expandVertically(spring(1.2f)) + fadeIn(spring(1.2f)),
                            exit = shrinkVertically(spring(1.2f)) + fadeOut(spring(1.2f))
                        ) {
                            IenTopAppBar(
                                title = {
                                    AnimatedVisibility(
                                        visible = materialAdaptation.mode == TopBarMode.Static ||
                                            !navigationTitleVisible,
                                        enter = fadeIn(tween(700)) + slideInVertically(tween(700)) { it / 2 },
                                        exit = fadeOut(tween(700)) + slideOutVertically(tween(700)) { it / 2 },
                                    ) {
                                        title()
                                    }
                                },
                                subtitle = subtitle?.let {
                                    {
                                        AnimatedVisibility(
                                            visible = materialAdaptation.mode == TopBarMode.Static ||
                                                !navigationTitleVisible,
                                            enter = fadeIn(tween(700)) + slideInVertically(tween(700)) { it / 2 },
                                            exit = fadeOut(tween(700)) + slideOutVertically(tween(700)) { it / 2 },
                                        ) {
                                            it()
                                        }
                                    }
                                },
                                modifier = topBarModifier.onGloballyPositioned {
                                    topBarHeight.value = it.size.height.toFloat()
                                },
                                navigationIcon = navigationIcon,
                                actions = actions,
                                windowInsets = materialAdaptation.topBarWindowInsets,
                                isCenterAligned = materialAdaptation.isCenterAligned,
                                isScrollTint = materialAdaptation.isScrollTint,
                                mode = TopBarMode.Static,
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
                floating = floatingActionButton,
                floatingActionButtonPosition = fabPosition.transform(),
                containerColor = materialAdaptation.scaffoldContainerColor,
                contentColor = materialAdaptation.scaffoldContentColor,
                contentWindowInsets = materialAdaptation.contentWindowInsets,
                contentEdge = contentEdge,
                content = { contentPadding ->
                    CompositionLocalProvider(LocalTopBarScaffoldScrollState provides contentEdge.scrollState) {
                        content(
                            contentPadding,
                            {
                                if (materialAdaptation.mode == TopBarMode.Expanded) {
                                    IenNavigationTitle(
                                        title = title,
                                        subtitle = subtitle,
                                        topBarHeight = topBarHeight.value,
                                        scaffoldCoordinates = scaffoldCoordinates.value,
                                        onVisibilityChange = { navigationTitleVisible = it },
                                    )
                                }
                            }
                        )
                    }
                }
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
                                subtitle = subtitle,
                                modifier = topBarModifier,
                                navigationIcon = { navigationIcon?.invoke() },
                                actions = {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        content = { actions?.invoke(this) },
                                        modifier = Modifier
                                            .animateContentSizeWithoutClipping()
                                            .heightIn(min = 48.dp)
                                    )
                                },
                                windowInsets = it.topBarWindowInsets,
                                isCenterAligned = it.isCenterAligned,
                                isBackgroundAdaptive = it.isBackgroundAdaptive,
                                isBackgroundGradient = it.isBackgroundGradient,
                                backdrop = it.backdrop,
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
                bottomBar = { bottomBar?.invoke() },
                snackbarHost = snackbarHost,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = higFabPosition,
                containerColor = it.scaffoldContainerColor,
                contentColor = it.scaffoldContentColor,
                contentWindowInsets = it.contentWindowInsets,
                hasNavigationTitle = it.mode == TopBarMode.Expanded,
                content = { contentPadding ->
                    CompositionLocalProvider(LocalTopBarScaffoldScrollState provides contentEdge.scrollState) {
                        content(
                            contentPadding,
                            {
                                if (it.mode == TopBarMode.Expanded) {
                                    CupertinoNavigationTitle(
                                        title = title,
                                        subtitle = subtitle
                                    )
                                }
                            }
                        )
                    }
                }
            )
        }
    )
}

/**
 * 적응형 상단바 스크라프트 컴포저블 (액션 버전)
 *
 * @param modifier 레이아웃 수정자
 * @param topBarModifier 상단바 수정자
 * @param title 제목
 * @param subtitle 부제목
 * @param showTopBar 상단바 표시 여부. 기본값은 true
 * @param navigationIcon 뒤로가기 아이콘
 * @param actions 액션 버튼들 (ActionMenuItem 리스트)
 * @param primaryAction 주요 액션 버튼
 * @param bottomBar 하단 바
 * @param snackbarHost 스낵바 호스트
 * @param floatingActionButton 플로팅 액션 버튼
 * @param fabPosition 플로팅 액션 버튼 위치
 * @param higFabPosition HIG 플로팅 액션 버튼 위치
 * @param adaptation 어댑테이션 설정
 * @param content 콘텐츠
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveTopAppBarScaffold(
    modifier: Modifier = Modifier,
    topBarModifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    subtitle: @Composable (() -> Unit)? = null,
    showTopBar: Boolean = true,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: List<ActionMenuItem> = listOf(),
    primaryAction: ActionMenuItem.IconMenuItem? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    fabPosition: FabPosition = FabPosition.Center,
    higFabPosition: FabPosition = fabPosition,
    contentEdge: IenScaffoldContentEdge = IenScaffoldContentEdge(),
    adaptation: AdaptationScope<HigTopAppBarScaffoldAdaptation, IenTopAppBarScaffoldAdaptation>.() -> Unit = LocalTopBarScaffoldAdaptation.current,
    content: @Composable (PaddingValues, @Composable () -> Unit) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val scaffold: @Composable ((@Composable (RowScope.() -> Unit))?) -> Unit = { actions ->
        AdaptiveTopAppBarScaffold(
            modifier = modifier,
            topBarModifier = topBarModifier,
            title = title,
            subtitle = subtitle,
            showTopBar = showTopBar,
            navigationIcon = navigationIcon,
            actions = actions,
            bottomBar = bottomBar,
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            fabPosition = fabPosition,
            higFabPosition = higFabPosition,
            contentEdge = contentEdge,
            adaptation = adaptation,
            content = content
        )
    }
    AdaptiveWidget(
        adaptation = remember { TopAppBarScaffoldAdaptation() },
        adaptationScope = adaptation,
        material = {
            val menuItems = primaryAction?.let { actions + it } ?: actions
            scaffold(menuItems.takeIf { it.isNotEmpty() }?.let {
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IenActionsMenu(
                            items = it,
                            isOpen = menuExpanded,
                            closeDropdown = { menuExpanded = false },
                            onToggleOverflow = { menuExpanded = !menuExpanded },
                            maxVisibleItems = 3,
                        )
                    }
                }
            })
        },
        cupertino = {
            scaffold {
                Spacer(modifier = Modifier.width(8.dp))

                @Composable
                fun liquidButton(
                    modifier: Modifier = Modifier,
                    onClick: () -> Unit,
                    isIconButton: Boolean,
                    enabled: Boolean = true,
                    colors: CupertinoLiquidButtonColors = glassButtonColors(),
                    backdrop: Backdrop,
                    isBackgroundAdaptive: Boolean = true,
                    content: @Composable () -> Unit
                ) {
                    val horizontalPadding by animateDpAsState(
                        targetValue = if (isIconButton) 8.dp else 16.dp
                    )
                    val verticalPadding by animateDpAsState(
                        targetValue = if (isIconButton) 8.dp else 10.dp
                    )
                    val maxWidth by animateDpAsState(
                        targetValue = if (isIconButton) 48.dp else 360.dp
                    )

                    CupertinoLiquidButton(
                        onClick = onClick,
                        enabled = enabled,
                        colors = colors,
                        backdrop = backdrop,
                        isBackgroundAdaptive = isBackgroundAdaptive,
                        contentPadding = PaddingValues(horizontalPadding, verticalPadding),
                        modifier = modifier.widthIn(min = 48.dp, max = maxWidth)
                    ) {
                        content()
                    }
                }

                val alpha by animateFloatAsState(
                    targetValue = if (actions.any { it.visible }) 1f else 0f,
                    animationSpec = spring(1.2f)
                )
                val isVisible by remember { derivedStateOf { alpha > 0f } }

                if (isVisible) {
                    HigActionsMenu(
                        items = actions,
                        isOpen = menuExpanded,
                        closeDropdown = { menuExpanded = false },
                        onToggleOverflow = { menuExpanded = !menuExpanded },
                        isNative = it.isDropdownNative,
                        maxVisibleItems = 3,
                    ) { content ->
                        liquidButton(
                            onClick = {},
                            isIconButton = actions.count { it.visible } == 1 && actions.first { it.visible }.let { it is ActionMenuItem.IconMenuItem && it.icon != null },
                            backdrop = it.backdrop,
                            isBackgroundAdaptive = it.isBackgroundAdaptive,
                            modifier = Modifier.graphicsLayer {
                                this.alpha = alpha
                            }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(24.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .animateContentSizeWithoutClipping()
                                    .fillMaxHeight()
                            ) {
                                content()
                            }
                        }
                    }
                }

                primaryAction?.let { action ->
                    val alpha by animateFloatAsState(
                        targetValue = if (action.visible) 1f else 0f,
                        animationSpec = spring(1.2f)
                    )

                    AnimatedVisibility(
                        visible = action.visible,
                        enter = slideInHorizontally(spring(1.2f)) { it / 2 },
                        exit = slideOutHorizontally(spring(1.2f)) { it / 2 }
                    ) {
                        liquidButton(
                            onClick = action.onClick,
                            isIconButton = action.icon != null,
                            enabled = action.enabled,
                            colors = glassProminentButtonColors(),
                            backdrop = it.backdrop,
                            isBackgroundAdaptive = it.isBackgroundAdaptive,
                            modifier = Modifier.graphicsLayer {
                                this.alpha = alpha
                                this.compositingStrategy = CompositingStrategy.ModulateAlpha
                            }
                        ) {
                            HigActionMenu(action)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    )
}

/**
 * M3 상단바 스크라프트 어댑테이션 클래스
 *
 * @param topBarWindowInsets 상단바 윈도우 인셋
 * @param contentWindowInsets 콘텐츠 윈도우 인셋
 * @param isScrollTint 스크롤 틴트 여부
 * @param isCenterAligned 중앙 정렬 여부
 * @param scaffoldContainerColor 스크라프트 컨테이너 색상
 * @param scaffoldContentColor 스크라프트 콘텐츠 색상
 * @param mode 상단바 표시 방식
 */
@OptIn(ExperimentalMaterial3Api::class)
class IenTopAppBarScaffoldAdaptation internal constructor(
    topBarWindowInsets: WindowInsets,
    contentWindowInsets: WindowInsets,
    isScrollTint: Boolean,
    isCenterAligned: Boolean,
    scaffoldContainerColor: Color,
    scaffoldContentColor: Color,
    mode: TopBarMode,
) {
    var topBarWindowInsets by mutableStateOf(topBarWindowInsets)
    var contentWindowInsets by mutableStateOf(contentWindowInsets)
    var isScrollTint by mutableStateOf(isScrollTint)
    var isCenterAligned by mutableStateOf(isCenterAligned)
    var scaffoldContainerColor by mutableStateOf(scaffoldContainerColor)
    var scaffoldContentColor by mutableStateOf(scaffoldContentColor)
    var mode by mutableStateOf(mode)
}

/**
 * HIG 상단바 스크라프트 어댑테이션 클래스
 *
 * @param topBarWindowInsets 상단바 윈도우 인셋
 * @param contentWindowInsets 콘텐츠 윈도우 인셋
 * @param isCenterAligned 중앙 정렬 여부
 * @param isBackgroundAdaptive 배경 어댑티브 여부. 기본값은 true
 * @param isBackgroundGradient 배경 그라디언트 여부. 기본값은 false
 * @param backdrop 뒷배경
 * @param isDropdownNative 드롭다운 네이티브 여부
 * @param colors 상단바 색상
 * @param scaffoldContainerColor 스크라프트 컨테이너 색상
 * @param scaffoldContentColor 스크라프트 콘텐츠 색상
 * @param mode 상단바 표시 방식
 */
class HigTopAppBarScaffoldAdaptation internal constructor(
    topBarWindowInsets: WindowInsets,
    contentWindowInsets: WindowInsets,
    isCenterAligned: Boolean,
    isBackgroundAdaptive: Boolean = true,
    isBackgroundGradient: Boolean = false,
    backdrop: LayerBackdrop,
    isDropdownNative: Boolean,
    colors: CupertinoTopAppBarColors,
    scaffoldContainerColor: Color,
    scaffoldContentColor: Color,
    mode: TopBarMode,
) {
    var topBarWindowInsets by mutableStateOf(topBarWindowInsets)
    var contentWindowInsets by mutableStateOf(contentWindowInsets)
    var isCenterAligned by mutableStateOf(isCenterAligned)
    var isBackgroundAdaptive by mutableStateOf(isBackgroundAdaptive)
    var isBackgroundGradient by mutableStateOf(isBackgroundGradient)
    var backdrop by mutableStateOf(backdrop)
    var isDropdownNative by mutableStateOf(isDropdownNative)
    var colors by mutableStateOf(colors)
    var scaffoldContainerColor by mutableStateOf(scaffoldContainerColor)
    var scaffoldContentColor by mutableStateOf(scaffoldContentColor)
    var mode by mutableStateOf(mode)
}

@OptIn(ExperimentalAdaptiveApi::class)
internal class TopAppBarScaffoldAdaptation: Adaptation<HigTopAppBarScaffoldAdaptation, IenTopAppBarScaffoldAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigTopAppBarScaffoldAdaptation {
        val topBarWindowInsets = CupertinoTopAppBarDefaults.windowInsets
        val contentWindowInsets = CupertinoScaffoldDefaults.contentWindowInsets
        val isCenterAligned = LocalIsHigTopBarCenterAligned.current
        val isBackgroundAdaptive = LocalIsBackgroundAdaptive.current
        val isBackgroundGradient = LocalIsBackgroundGradient.current
        val backdrop = rememberDefaultBackdrop()
        val isDropdownNative = true
        val colors = CupertinoTopAppBarDefaults.topAppBarColors()
        val scaffoldContainerColor = MaterialTheme.colorScheme.background// CupertinoScaffoldDefaults.containerColor
        val scaffoldContentColor = contentColorFor(scaffoldContainerColor) // CupertinoScaffoldDefaults.contentColor
        val mode = LocalTopBarMode.current

        return remember(topBarWindowInsets, contentWindowInsets, backdrop,isDropdownNative, isCenterAligned, isBackgroundAdaptive, isBackgroundGradient, colors, scaffoldContainerColor, scaffoldContentColor, mode) {
            HigTopAppBarScaffoldAdaptation(
                topBarWindowInsets = topBarWindowInsets,
                contentWindowInsets = contentWindowInsets,
                isCenterAligned = isCenterAligned,
                isBackgroundAdaptive = isBackgroundAdaptive,
                isBackgroundGradient = isBackgroundGradient,
                backdrop = backdrop,
                isDropdownNative = isDropdownNative,
                colors = colors,
                scaffoldContainerColor = scaffoldContainerColor,
                scaffoldContentColor = scaffoldContentColor,
                mode = mode,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun rememberMaterialAdaptation(): IenTopAppBarScaffoldAdaptation {
        val topBarWindowInsets = TopAppBarDefaults.windowInsets
        val contentWindowInsets = ScaffoldDefaults.contentWindowInsets
        val isScrollTint = LocalIsScrollTint.current
        val isCenterAligned = LocalIsM3TopBarCenterAligned.current
        val scaffoldContainerColor = MaterialTheme.colorScheme.background
        val scaffoldContentColor = contentColorFor(scaffoldContainerColor)
        val mode = LocalTopBarMode.current

        return remember(topBarWindowInsets, contentWindowInsets, isScrollTint, isCenterAligned, scaffoldContainerColor, scaffoldContentColor, mode) {
            IenTopAppBarScaffoldAdaptation(
                topBarWindowInsets = topBarWindowInsets,
                contentWindowInsets = contentWindowInsets,
                isScrollTint = isScrollTint,
                isCenterAligned = isCenterAligned,
                scaffoldContainerColor = scaffoldContainerColor,
                scaffoldContentColor = scaffoldContentColor,
                mode = mode,
            )
        }
    }
}

@Composable
private fun IenNavigationTitle(
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)?,
    topBarHeight: Float,
    scaffoldCoordinates: LayoutCoordinates?,
    onVisibilityChange: (Boolean) -> Unit,
) {
    var offsetDifference by remember { mutableStateOf(0f) }
    var actualTitleHeight by remember { mutableStateOf(0f) }
    val topAppBarExists = topBarHeight > 0f
    val titleAlpha by remember {
        derivedStateOf {
            if (!topAppBarExists) {
                1f
            } else {
                val d = offsetDifference - actualTitleHeight + 50f
                if (d <= 0f) {
                    1f
                } else {
                    (1f - (d / 100f)).coerceIn(0f, 1f)
                }
            }
        }
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = titleAlpha,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "IenNavigationTitleAlpha",
    )

    Column(
        modifier = Modifier
            .animateContentSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .alpha(animatedAlpha)
            .onGloballyPositioned {
                actualTitleHeight = it.size.height.toFloat()
                val scaffoldTop = scaffoldCoordinates?.boundsInWindow()?.top ?: 0f
                offsetDifference = (topBarHeight - it.boundsInWindow().top) + scaffoldTop
                onVisibilityChange(!topAppBarExists || offsetDifference < it.size.height)
            },
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        IenProvideTextStyle(IenTheme.typography.display, IenTheme.colors.textPrimary) {
            title()
        }
        if (subtitle != null) {
            IenProvideTextStyle(IenTheme.typography.body1, IenTheme.colors.textSecondary) {
                subtitle()
            }
        }
    }
}
