package zone.ien.utils.ui.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.pull_to_refresh_action
import zone.ien.utils.cmp_ui.generated.resources.pull_to_refresh_pulling
import zone.ien.utils.cmp_ui.generated.resources.pull_to_refresh_ready
import zone.ien.utils.cmp_ui.generated.resources.pull_to_refresh_refreshing
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.drawIenBorder

/** Ien 당김 갱신의 기본 크기입니다. */
object IenPullToRefreshDefaults {
    /** 손을 놓았을 때 갱신을 요청하는 표시 거리입니다. */
    val PositionalThreshold: Dp = 56.dp

    /** 기본 기준 거리에서 콘텐츠가 내려갈 수 있는 최대 표시 거리입니다. */
    val MaxPullDistance: Dp = 96.dp
}

/**
 * 스크롤 콘텐츠에 Material3 당김 갱신 동작과 Ien 세 점 인디케이터를 적용합니다.
 *
 * 갱신 중에도 기존 콘텐츠를 유지하며, 당긴 거리에 맞춰 콘텐츠를 아래로 이동합니다.
 * 당김 없이 [isRefreshing]이 변경되어도 갱신 상태를 표시합니다.
 * 첫 로딩은 호출자가 [content] 안에서 [IenSkeleton] 등으로 별도 표현합니다.
 * [contentPadding]으로 콘텐츠 여백을 지정하며, 기본 인디케이터는 상단 여백을 지나 내려옵니다.
 *
 * @param isRefreshing 실제 데이터 갱신 진행 여부입니다.
 * @param onRefresh 기준 거리를 넘겨 손을 놓거나 접근성 갱신 동작을 실행할 때 호출합니다.
 * @param modifier 컨테이너에 적용할 Modifier입니다.
 * @param state Material3와 공유하는 당김 거리 및 복귀 애니메이션 상태입니다.
 * @param contentAlignment 콘텐츠의 기본 정렬입니다.
 * @param indicator 콘텐츠 위에 표시할 인디케이터 슬롯입니다.
 * @param enabled 당김 제스처와 접근성 갱신 요청을 허용할지 여부입니다.
 * @param threshold 갱신을 요청하는 표시 거리입니다. 0보다 커야 합니다.
 * @param contentPadding 콘텐츠에 적용할 여백입니다. 기본 인디케이터의 이동 거리에도 상단 여백을 반영합니다.
 * @param content LazyColumn 등 스크롤 가능한 콘텐츠입니다.
 */
@Composable
fun IenPullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    enabled: Boolean = true,
    threshold: Dp = IenPullToRefreshDefaults.PositionalThreshold,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    indicator: @Composable BoxScope.() -> Unit = {
        IenPullToRefreshIndicator(
            state = state,
            isRefreshing = isRefreshing,
            modifier = Modifier.align(Alignment.TopCenter),
            maxDistance = threshold,
            topPadding = contentPadding.calculateTopPadding(),
        )
    },
    content: @Composable BoxScope.() -> Unit,
) {
    require(threshold > 0.dp) { "threshold는 0보다 커야 합니다." }
    val refreshAction = stringResource(Res.string.pull_to_refresh_action)
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.clipToBounds().semantics {
            if (enabled && !isRefreshing) {
                onClick(label = refreshAction) {
                    onRefresh()
                    true
                }
            }
        },
        state = state,
        contentAlignment = contentAlignment,
        indicator = indicator,
        enabled = enabled,
        threshold = threshold,
    ) {
        Box(
            modifier = Modifier.padding(contentPadding).graphicsLayer {
                translationY = (state.distanceFraction.coerceAtLeast(0f) * threshold.toPx())
                    .coerceAtMost(maxOf(IenPullToRefreshDefaults.MaxPullDistance, threshold).toPx())
            },
            contentAlignment = contentAlignment,
            content = content,
        )
    }
}

/**
 * 당길 때 점 세 개가 펼쳐지고 갱신 중에는 순서대로 위로 흐르는 인디케이터입니다.
 *
 * [IenPullToRefreshBox]와 동일한 [state]를 전달합니다. 대기 상태에서는 숨겨지며,
 * 애니메이션 배율이 0인 경우와 미리보기 환경에서는 반복 모션이 정지합니다.
 *
 * @param state 컨테이너와 공유하는 Material3 당김 상태입니다.
 * @param isRefreshing 실제 갱신 진행 여부입니다.
 * @param modifier 인디케이터의 배치 및 크기에 적용할 Modifier입니다.
 * @param containerColor 대기 및 당김 상태의 배경색입니다.
 * @param color 점의 색상입니다.
 * @param maxDistance 컨테이너의 threshold와 동일한 표시 거리입니다.
 * @param topPadding 인디케이터가 추가로 내려올 콘텐츠의 상단 여백입니다.
 */
@Composable
fun IenPullToRefreshIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    containerColor: Color = IenTheme.colors.surface,
    color: Color = IenTheme.colors.brand,
    maxDistance: Dp = IenPullToRefreshDefaults.PositionalThreshold,
    topPadding: Dp = 0.dp,
) {
    require(maxDistance > 0.dp) { "maxDistance는 0보다 커야 합니다." }
    val fraction = state.distanceFraction.coerceAtLeast(0f)
    if (!isRefreshing && fraction == 0f) return

    val progress = if (isRefreshing) 1f else fraction.coerceAtMost(1f)
    val ready = isRefreshing || fraction >= 1f
    val colors = IenTheme.colors
    val shape = ContinuousRoundedRectangle(IenTheme.radius.full)
    val readyProgress = animateFloatAsState(
        targetValue = if (ready) 1f else 0f,
        animationSpec = tween(
            durationMillis = IenTheme.motion.normalMillis,
            easing = IenTheme.motion.standardEasing,
        ),
        label = "IenPullToRefreshReadyColor",
    )
    val description = stringResource(
        when {
            isRefreshing -> Res.string.pull_to_refresh_refreshing
            ready -> Res.string.pull_to_refresh_ready
            else -> Res.string.pull_to_refresh_pulling
        },
    )
    val phase: State<Float> = if (isRefreshing && !LocalInspectionMode.current) {
        rememberInfiniteTransition(label = "IenPullToRefresh").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing)),
            label = "IenPullToRefreshDots",
        )
    } else {
        rememberUpdatedState(0f)
    }

    Box(
        modifier = modifier
            .size(width = (36 + 36 * progress).dp, height = 36.dp)
            .graphicsLayer {
                val distance = fraction * (maxDistance + topPadding).toPx()
                val restingOffset = (topPadding + 8.dp).toPx()
                translationY = if (isRefreshing) restingOffset else
                    (distance - 36.dp.toPx() + 8.dp.toPx()).coerceAtMost(restingOffset)
            }
            .dropShadow(
                shape = shape,
                shadow = Shadow(
                    radius = 16.dp,
                    color = colors.textPrimary.copy(alpha = .08f),
                    offset = DpOffset(0.dp, 4.dp),
                ),
            )
            .drawIenBorder(
                BorderStroke(
                    IenTheme.stroke.thin,
                    lerp(colors.border, colors.brandWeak, readyProgress.value),
                ),
                shape,
            )
            .clip(shape)
            .background(lerp(containerColor, colors.brandWeak, readyProgress.value), shape)
            .semantics {
                liveRegion = LiveRegionMode.Polite
                stateDescription = description
                progressBarRangeInfo = if (isRefreshing) ProgressBarRangeInfo.Indeterminate else
                    ProgressBarRangeInfo(progress, 0f..1f)
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(width = 72.dp, height = 36.dp)) {
            repeat(3) { index ->
                val cycle = (phase.value - index / 6f + 1f) % 1f
                val wave = if (!isRefreshing) 0f else when {
                    cycle < .35f -> FastOutSlowInEasing.transform(cycle / .35f)
                    cycle < .65f -> 1f - FastOutSlowInEasing.transform((cycle - .35f) / .3f)
                    else -> 0f
                }
                val scale = if (isRefreshing) .72f + .46f * wave else 1f
                drawCircle(
                    color = color,
                    radius = (if (index == 1) 4.dp else 3.5.dp).toPx() * scale,
                    center = Offset(
                        x = center.x + (index - 1) * 13.dp.toPx() * progress,
                        y = center.y - 6.dp.toPx() * wave,
                    ),
                    alpha = if (isRefreshing) .55f + .45f * wave else
                        if (index == 1) 1f else progress,
                )
            }
        }
    }
}
