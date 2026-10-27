package zone.ien.utils.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.LocalContentColor
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenTheme

internal data class IenBarPlacement(
    val sideWidth: Dp = 0.dp,
    val controlsTop: Dp = 0.dp,
    val controlsBottom: Dp = 0.dp,
    val centerFromRight: Dp = sideWidth / 2,
    val titleTopInset: Dp = 0.dp,
)

internal val LocalIenBarPlacement = staticCompositionLocalOf<IenBarPlacement?> { null }
internal object IenSideTopBar

/**
 * 하위 [IenScaffold]가 Duo의 오른쪽 시스템 예약 영역에 맞춰 바를 배치할지 지정합니다.
 * 화면 오른쪽 끝에 닿지 않는 분할 pane에서는 `false`로 제공해 일반 상단·하단 배치를 사용합니다.
 */
val LocalIenSideBarEnabled = staticCompositionLocalOf { true }

/** 현재 [IenScaffold]가 시스템 예약 영역에 맞춰 오른쪽에 버튼과 탭을 배치하는지 나타냅니다. */
val isIenSideBarLayout: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalIenBarPlacement.current?.sideWidth?.let { it > 0.dp } == true

/** 현재 영역에서 기기의 오른쪽 시스템 예약 영역에 맞춘 세로 바 배치를 사용할 수 있는지 나타냅니다. */
val isIenSideBarAvailable: Boolean
    @Composable
    get() {
        val placement by rememberIenBarPlacement()
        return LocalIenSideBarEnabled.current && placement?.sideWidth?.let { it > 0.dp } == true
    }

@Composable
internal expect fun rememberIenBarPlacement(): State<IenBarPlacement?>

internal fun ienSideBarContentPadding(
    left: Dp,
    top: Dp,
    right: Dp,
    bottom: Dp,
    topBarHeight: Dp,
    sideBarWidth: Dp,
): PaddingValues = PaddingValues.Absolute(
    left = left,
    top = maxOf(top, topBarHeight),
    right = maxOf(right, sideBarWidth),
    bottom = bottom,
)

internal fun ienSideBarSnackbarOffset(
    bottomInset: Int,
    fabHeight: Int,
    floatingGap: Int,
): Int = bottomInset + if (fabHeight > 0) fabHeight + floatingGap else 0

@Composable
internal fun IenSideBarScaffold(
    modifier: Modifier,
    placement: IenBarPlacement,
    topBarVisibility: Float,
    topBar: @Composable () -> Unit,
    bottomBar: (@Composable () -> Unit)?,
    snackbarHost: @Composable () -> Unit,
    floating: @Composable () -> Unit,
    floatingPosition: FabPosition,
    containerColor: Color,
    contentColor: Color,
    contentWindowInsets: WindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    val bottomScrollState = rememberScrollState()
    val shadowPadding = maxOf(IenTheme.spacing.xs, IenTheme.elevation.floating * 2)
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        SubcomposeLayout(modifier.background(containerColor)) { constraints ->
            val width = constraints.maxWidth
            val height = constraints.maxHeight
            val sideWidth = placement.sideWidth.roundToPx().coerceAtMost(width)
            val controlsTop = placement.controlsTop.roundToPx().coerceAtMost(height)
            val controlsBottom = placement.controlsBottom.roundToPx().coerceAtMost(height - controlsTop)
            val topShadowPadding = shadowPadding.roundToPx().coerceAtMost(controlsTop)
            val gap = 8.dp.roundToPx()
            val controlsHeight = height - controlsTop - controlsBottom
            val loose = Constraints(maxWidth = width, maxHeight = height)
            val top = subcompose("top", topBar).map { measurable ->
                val isSide = measurable.layoutId == IenSideTopBar
                val barConstraints = if (isSide) Constraints(
                    maxWidth = sideWidth,
                    maxHeight = (if (bottomBar == null) controlsHeight else controlsHeight / 2) + topShadowPadding,
                ) else loose
                isSide to measurable.measure(barConstraints)
            }
            val sideTopHeight = ((top.filter { it.first }.maxOfOrNull { it.second.height } ?: 0) - topShadowPadding)
                .coerceAtLeast(0)
            val bottom = subcompose("bottom") {
                if (bottomBar != null) {
                    Box(Modifier.verticalScroll(bottomScrollState, reverseScrolling = true).padding(vertical = shadowPadding)) {
                        bottomBar()
                    }
                }
            }.map {
                it.measure(Constraints(
                    maxWidth = sideWidth,
                    maxHeight = (controlsHeight - sideTopHeight - if (sideTopHeight > 0) gap else 0).coerceAtLeast(0),
                ))
            }
            val bottomHeight = bottom.maxOfOrNull { it.height } ?: 0
            val topHeight = top.filterNot { it.first }.maxOfOrNull { it.second.height } ?: 0
            val hasBars = topHeight > 0 || top.any { it.first } || bottomHeight > 0
            val right = maxOf(
                contentWindowInsets.getRight(this, LayoutDirection.Ltr),
                if (hasBars) sideWidth else 0,
            ).coerceAtMost(width)
            val padding = ienSideBarContentPadding(
                left = contentWindowInsets.getLeft(this, LayoutDirection.Ltr).toDp(),
                top = contentWindowInsets.getTop(this).toDp(),
                right = right.toDp(),
                bottom = contentWindowInsets.getBottom(this).toDp(),
                topBarHeight = topHeight.toDp(),
                sideBarWidth = if (hasBars) sideWidth.toDp() else 0.dp,
            )
            val body = subcompose("content") { content(padding) }.map { it.measure(loose) }
            val overlayConstraints = Constraints(maxWidth = width - right, maxHeight = height)
            val snackbar = subcompose("snackbar", snackbarHost).map { it.measure(overlayConstraints) }
            val fab = subcompose("floating", floating).map { it.measure(overlayConstraints) }
            val bottomInset = contentWindowInsets.getBottom(this)
            val floatingGap = 16.dp.roundToPx()
            val fabHeight = fab.filter { it.width > 0 && it.height > 0 }
                .maxOfOrNull { it.height } ?: 0
            val snackbarOffset = ienSideBarSnackbarOffset(bottomInset, fabHeight, floatingGap)
            layout(width, height) {
                body.forEach { it.place(0, 0) }
                top.forEach { (isSide, bar) ->
                    if (isSide) {
                        bar.place(
                            (width - placement.centerFromRight.roundToPx() - bar.width / 2).coerceIn(0, width - bar.width),
                            controlsTop - topShadowPadding,
                        )
                    } else {
                        bar.placeWithLayer(0, 0) {
                            translationY = -bar.height * (1f - topBarVisibility)
                            alpha = topBarVisibility
                        }
                    }
                }
                bottom.forEach {
                    it.place(
                        (width - placement.centerFromRight.roundToPx() - it.width / 2).coerceIn(0, width - it.width),
                        height - controlsBottom - it.height,
                    )
                }
                fab.forEach {
                    val x = when (floatingPosition) {
                        FabPosition.Center -> (width - right - it.width) / 2
                        FabPosition.Start -> if (layoutDirection == LayoutDirection.Ltr) floatingGap
                            else width - right - it.width - floatingGap
                        else -> if (layoutDirection == LayoutDirection.Ltr) width - right - it.width - floatingGap
                            else floatingGap
                    }
                    it.place(x.coerceAtLeast(0), (height - bottomInset - floatingGap - it.height).coerceAtLeast(0))
                }
                snackbar.forEach {
                    it.place((width - right - it.width) / 2, (height - snackbarOffset - it.height).coerceAtLeast(0))
                }
            }
        }
    }
}
