package zone.ien.utils.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.layout.fillMaxSize
import com.kyant.backdrop.drawPlainBackdrop
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.runtimeShaderEffect

/**
 * [IenScaffold]가 콘텐츠와 공유하는 기본 스크롤 상태입니다.
 *
 * [IenScaffoldContentEdge]에 별도 스크롤 상태가 없을 때 Scaffold가 생성한 상태를 제공하고,
 * 섹션 컨테이너는 이 값을 우선 사용해 블러 진행도와 실제 스크롤 상태를 맞춥니다.
 */
val LocalIenScaffoldScrollState = staticCompositionLocalOf<ScrollState?> { null }
/**
 * 스크래프트(Scaffold) 콘텐츠 영역의 모서리 스타일 및 블러(Blur) 효과 설정을 정의하는 데이터 클래스입니다.
 *
 * @property enabled 모서리 블러 효과 활성화 여부
 * @property topEnabled 상단 모서리 블러 효과 활성화 여부
 * @property bottomEnabled 하단 모서리 블러 효과 활성화 여부
 * @property topProgress 상단 블러 진행도 (0f ~ 1f)
 * @property bottomProgress 하단 블러 진행도 (0f ~ 1f)
 * @property scrollableState 콘텐츠의 상/하단 블러 진행도를 자동 계산할 상태.
 * [ScrollState], [LazyListState], [LazyGridState]는 스크롤 거리로 진행도를 계산하고,
 * 그 외 상태는 각 방향의 스크롤 가능 여부에 따라 블러를 표시합니다.
 * @property scrollFadeDistance 스크롤 시작/끝 근처에서 블러가 나타나고 사라지는 거리
 * @property topHeight 상단 블러 영역의 높이
 * @property bottomHeight 하단 블러 영역의 높이
 * @property radius 블러 강도 반경
 * @property color 배경 색상 (null인 경우 컨테이너 색상 사용)
 */
@Immutable
data class IenScaffoldContentEdge(
    val enabled: Boolean = true,
    val topEnabled: Boolean = true,
    val bottomEnabled: Boolean = true,
    val topProgress: Float = 1f,
    val bottomProgress: Float = 1f,
    val scrollableState: ScrollableState? = null,
    val scrollFadeDistance: Dp = 48.dp,
    val topHeight: Dp = 168.dp,
    val bottomHeight: Dp = 64.dp,
    val radius: Dp = 18.dp,
    val color: Color? = null,
)

internal fun resolveBottomBlurHeight(
    bottomHeight: Dp,
    bottomBarHeight: Dp,
): Dp = bottomHeight + bottomBarHeight

/**
 * IEN 라이브러리의 기본 스크래프트(Scaffold) 컴포저블입니다.
 * 콘텐츠 영역의 상/하단에 유려한 모서리 블러 효과([IenScaffoldContentEdge])를 적용할 수 있습니다.
 * 오른쪽 시스템 예약 영역이 있는 Duo에서는 버튼과 탭을 세로로 배치합니다.
 * [LocalIenSideBarEnabled]가 `false`인 영역에서는 일반 상단·하단 배치를 사용합니다.
 * 오른쪽 바 배치에서는 콘텐츠 스크롤 방향에 따라 상단 제목과 블러를 숨기거나 다시 표시합니다.
 *
 * @param modifier 적용할 Modifier
 * @param topBar 상단 영역에 표시할 컴포저블
 * @param bottomBar 하단 영역에 표시할 컴포저블
 * @param snackbarHost 스낵바 호스트 컴포저블
 * @param floating 플로팅 액션 버튼(FAB) 컴포저블
 * @param floatingActionButtonPosition FAB의 위치 설정
 * @param containerColor 스크래프트 배경 색상
 * @param contentColor 스크래프트 콘텐츠 기본 색상
 * @param contentWindowInsets 콘텐츠 영역에 적용할 윈도우 인셋
 * @param contentEdge 콘텐츠 영역 모서리 블러 효과 설정
 * @param content 스크래프트 내부에 표시될 메인 콘텐츠
 */
@Composable
fun IenScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: (@Composable () -> Unit)? = null,
    snackbarHost: @Composable () -> Unit = {},
    floating: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.Center,
    containerColor: Color = IenTheme.colors.background,
    contentColor: Color = IenTheme.colors.textPrimary,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    contentEdge: IenScaffoldContentEdge = IenScaffoldContentEdge(enabled = false),
    content: @Composable (PaddingValues) -> Unit,
) {
    val devicePlacement by rememberIenBarPlacement()
    val placement = if (LocalIenSideBarEnabled.current) {
        LocalIenBarPlacement.current ?: devicePlacement
    } else {
        null
    }
    val isSideBar = placement != null && placement.sideWidth > 0.dp
    var topBarHidden by remember(isSideBar) { mutableStateOf(false) }
    val scrollConnection = remember(isSideBar) {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                if (consumed.y != 0f) topBarHidden = consumed.y < 0f
                return Offset.Zero
            }
        }
    }
    val topBarVisibility by animateFloatAsState(
        targetValue = if (isSideBar && topBarHidden) 0f else 1f,
        animationSpec = tween(IenTheme.motion.normalMillis, easing = IenTheme.motion.standardEasing),
        label = "ienDuoTopBarVisibility",
    )
    val defaultScrollState = rememberScrollState()
    val effectiveContentEdge = if (contentEdge.scrollableState == null) {
        contentEdge.copy(scrollableState = defaultScrollState)
    } else {
        contentEdge
    }
    val contentEdgeColor = contentEdge.color ?: containerColor
    val backdrop = rememberLayerBackdrop {
        drawRect(contentEdgeColor)
        drawContent()
    }
    val scrollFadeDistancePx = with(LocalDensity.current) {
        effectiveContentEdge.scrollFadeDistance.toPx().coerceAtLeast(1f)
    }
    val scrollTopProgress = effectiveContentEdge.scrollableState?.topEdgeProgress(scrollFadeDistancePx)
        ?: 0f
    val scrollBottomProgress = effectiveContentEdge.scrollableState?.bottomEdgeProgress(scrollFadeDistancePx)
        ?: 0f
    val effectiveTopProgress = effectiveContentEdge.topProgress.coerceIn(0f, 1f) * scrollTopProgress
    val effectiveBottomProgress = effectiveContentEdge.bottomProgress.coerceIn(0f, 1f) * scrollBottomProgress
    var bottomBarHeightPx by remember { mutableStateOf(0) }
    val targetBottomBarHeight = if (bottomBar == null) {
        0.dp
    } else {
        with(LocalDensity.current) { bottomBarHeightPx.toDp() }
    }
    val bottomBarHeight by animateDpAsState(
        targetValue = targetBottomBarHeight,
        animationSpec = tween(
            durationMillis = IenTheme.motion.normalMillis,
            easing = IenTheme.motion.standardEasing,
        ),
        label = "bottom_bar_blur_height",
    )

    val scaffoldContent: @Composable (PaddingValues) -> Unit = { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().then(
            if (isSideBar) Modifier.nestedScroll(scrollConnection) else Modifier,
        )) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .layerBackdrop(backdrop),
            ) {
                CompositionLocalProvider(LocalIenScaffoldScrollState provides (effectiveContentEdge.scrollableState as? ScrollState)) {
                    content(contentPadding)
                }
            }
            if (effectiveContentEdge.enabled) {
                IenScaffoldEdgeBlur(
                    modifier = Modifier.matchParentSize(),
                    backdrop = backdrop,
                    showTop = effectiveContentEdge.topEnabled,
                    showBottom = effectiveContentEdge.bottomEnabled,
                    topProgress = effectiveTopProgress,
                    topBarVisibility = topBarVisibility,
                    bottomProgress = effectiveBottomProgress,
                    topHeight = effectiveContentEdge.topHeight,
                    bottomHeight = resolveBottomBlurHeight(
                        bottomHeight = effectiveContentEdge.bottomHeight,
                        bottomBarHeight = if (isSideBar) 0.dp else bottomBarHeight,
                    ),
                    radius = effectiveContentEdge.radius,
                    color = contentEdgeColor,
                )
            }
        }
    }
    CompositionLocalProvider(LocalIenBarPlacement provides placement) {
        if (isSideBar) {
            IenSideBarScaffold(
                modifier = modifier,
                placement = placement,
                topBarVisibility = topBarVisibility,
                topBar = topBar,
                bottomBar = bottomBar,
                snackbarHost = snackbarHost,
                floating = floating,
                floatingPosition = floatingActionButtonPosition,
                containerColor = containerColor,
                contentColor = contentColor,
                contentWindowInsets = contentWindowInsets,
                content = scaffoldContent,
            )
        } else {
            Scaffold(
                modifier = modifier,
                topBar = {
                    Box(Modifier.graphicsLayer {
                        translationY = -size.height * (1f - topBarVisibility)
                        alpha = topBarVisibility
                    }) { topBar() }
                },
                bottomBar = {
                    if (bottomBar != null) {
                        Box(Modifier.onSizeChanged { bottomBarHeightPx = it.height }) { bottomBar() }
                    }
                },
                snackbarHost = snackbarHost,
                floatingActionButton = floating,
                floatingActionButtonPosition = floatingActionButtonPosition,
                containerColor = containerColor,
                contentColor = contentColor,
                contentWindowInsets = contentWindowInsets,
                content = scaffoldContent,
            )
        }
    }
}

private fun ScrollableState.topEdgeProgress(fadeDistancePx: Float): Float = when (this) {
    is ScrollState -> topEdgeProgress(fadeDistancePx)
    is LazyListState -> if (layoutInfo.reverseLayout) {
        bottomEdgeProgress(fadeDistancePx)
    } else {
        topEdgeProgress(fadeDistancePx)
    }
    is LazyGridState -> if (layoutInfo.reverseLayout) {
        bottomEdgeProgress(fadeDistancePx)
    } else {
        topEdgeProgress(fadeDistancePx)
    }
    else -> if (canScrollBackward) 1f else 0f
}

private fun ScrollableState.bottomEdgeProgress(fadeDistancePx: Float): Float = when (this) {
    is ScrollState -> bottomEdgeProgress(fadeDistancePx)
    is LazyListState -> if (layoutInfo.reverseLayout) {
        topEdgeProgress(fadeDistancePx)
    } else {
        bottomEdgeProgress(fadeDistancePx)
    }
    is LazyGridState -> if (layoutInfo.reverseLayout) {
        topEdgeProgress(fadeDistancePx)
    } else {
        bottomEdgeProgress(fadeDistancePx)
    }
    else -> if (canScrollForward) 1f else 0f
}

private fun ScrollState.topEdgeProgress(fadeDistancePx: Float): Float {
    if (maxValue <= 0) return 0f
    return (value / fadeDistancePx).coerceIn(0f, 1f)
}

private fun ScrollState.bottomEdgeProgress(fadeDistancePx: Float): Float {
    if (maxValue <= 0) return 0f
    return ((maxValue - value) / fadeDistancePx).coerceIn(0f, 1f)
}

private fun LazyListState.topEdgeProgress(fadeDistancePx: Float): Float {
    val layoutInfo = layoutInfo
    if (layoutInfo.totalItemsCount <= 0) return 0f
    val firstItem = layoutInfo.visibleItemsInfo.firstOrNull { it.index == 0 }
    val firstItemOffset = firstItem?.takeUnless {
        firstVisibleItemIndex > 0 && it.size > 0 && it.offset >= 0
    }?.offset
    return resolveLazyTopEdgeProgress(
        canScrollBackward = canScrollBackward,
        firstItemOffset = firstItemOffset,
        fadeDistancePx = fadeDistancePx,
    )
}

private fun LazyListState.bottomEdgeProgress(fadeDistancePx: Float): Float {
    val layoutInfo = layoutInfo
    if (layoutInfo.totalItemsCount <= 0) return 0f
    if (!canScrollBackward && !canScrollForward) return 0f
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return 0f
    if (lastVisibleItem.index < layoutInfo.totalItemsCount - 1) return 1f

    val remainingPx = (lastVisibleItem.offset + lastVisibleItem.size - layoutInfo.viewportEndOffset).toFloat()
    return (remainingPx / fadeDistancePx).coerceIn(0f, 1f)
}

private fun LazyGridState.topEdgeProgress(fadeDistancePx: Float): Float {
    val layoutInfo = layoutInfo
    if (layoutInfo.totalItemsCount <= 0) return 0f
    val firstItem = layoutInfo.visibleItemsInfo.firstOrNull { it.index == 0 }
    return resolveLazyTopEdgeProgress(
        canScrollBackward = canScrollBackward,
        firstItemOffset = firstItem?.let {
            if (layoutInfo.orientation == Orientation.Vertical) it.offset.y else it.offset.x
        },
        fadeDistancePx = fadeDistancePx,
    )
}

internal fun resolveLazyTopEdgeProgress(
    canScrollBackward: Boolean,
    firstItemOffset: Int?,
    fadeDistancePx: Float,
): Float {
    if (!canScrollBackward) return 0f
    if (firstItemOffset == null) return 1f
    if (firstItemOffset >= 0) return 0f
    return (-firstItemOffset.toFloat() / fadeDistancePx.coerceAtLeast(1f)).coerceIn(0f, 1f)
}

private fun LazyGridState.bottomEdgeProgress(fadeDistancePx: Float): Float {
    val layoutInfo = layoutInfo
    if (layoutInfo.totalItemsCount <= 0) return 0f
    if (!canScrollForward) return 0f
    val visibleItems = layoutInfo.visibleItemsInfo
    if (visibleItems.isEmpty()) return 0f
    if (visibleItems.none { it.index == layoutInfo.totalItemsCount - 1 }) return 1f

    val contentEnd = visibleItems.maxOf {
        if (layoutInfo.orientation == Orientation.Vertical) {
            it.offset.y + it.size.height
        } else {
            it.offset.x + it.size.width
        }
    }
    val remainingPx = (contentEnd - layoutInfo.viewportEndOffset).toFloat()
    return (remainingPx / fadeDistancePx).coerceIn(0f, 1f)
}

@Composable
private fun IenScaffoldEdgeBlur(
    modifier: Modifier = Modifier,
    backdrop: LayerBackdrop,
    showTop: Boolean,
    showBottom: Boolean,
    topProgress: Float,
    topBarVisibility: Float,
    bottomProgress: Float,
    topHeight: Dp,
    bottomHeight: Dp,
    radius: Dp,
    color: Color,
) {
    Box(modifier = modifier) {
        if (showTop && topProgress > 0f) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(topHeight)
                    .graphicsLayer {
                        translationY = -size.height * (1f - topBarVisibility)
                        alpha = topBarVisibility
                    }
                    .drawPlainBackdrop(
                        backdrop = backdrop,
                        shape = { RectangleShape },
                        effects = {
                            blur(radius.toPx())
                            runtimeShaderEffect(
                                "IenTopEdgeBackdropMask",
                                """
                                uniform shader content;

                                uniform float2 size;
                                layout(color) uniform half4 tint;
                                uniform float tintIntensity;
                                uniform float edgeIntensity;

                                half4 main(float2 coord) {
                                    float progress = clamp(coord.y / size.y, 0.0, 1.0);
                                    float edgeAlpha = (1.0 - smoothstep(0.42, 1.0, progress)) * edgeIntensity;
                                    half4 blurred = content.eval(coord) * edgeAlpha;
                                    half4 tintLayer = tint * edgeAlpha;
                                    return mix(blurred, tintLayer, tintIntensity);
                                }
                                """.trimIndent(),
                                "content",
                            ) {
                                setFloatUniform("size", size.width, size.height)
                                setColorUniform("tint", color.copy(alpha = 0.42f))
                                setFloatUniform("tintIntensity", 0.24f)
                                setFloatUniform("edgeIntensity", topProgress)
                            }
                        },
                    ),
            )
        }
        if (showBottom && bottomProgress > 0f) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(bottomHeight)
                    .drawPlainBackdrop(
                        backdrop = backdrop,
                        shape = { RectangleShape },
                        effects = {
                            blur(radius.toPx())
                            runtimeShaderEffect(
                                "IenBottomEdgeBackdropMask",
                                """
                                uniform shader content;

                                uniform float2 size;
                                layout(color) uniform half4 tint;
                                uniform float tintIntensity;
                                uniform float edgeIntensity;

                                half4 main(float2 coord) {
                                    float progress = clamp(coord.y / size.y, 0.0, 1.0);
                                    float edgeAlpha = smoothstep(0.0, 0.58, progress) * edgeIntensity;
                                    half4 blurred = content.eval(coord) * edgeAlpha;
                                    half4 tintLayer = tint * edgeAlpha;
                                    return mix(blurred, tintLayer, tintIntensity);
                                }
                                """.trimIndent(),
                                "content",
                            ) {
                                setFloatUniform("size", size.width, size.height)
                                setColorUniform("tint", color.copy(alpha = 0.42f))
                                setFloatUniform("tintIntensity", 0.24f)
                                setFloatUniform("edgeIntensity", bottomProgress)
                            }
                        },
                    ),
            )
        }
    }
}
