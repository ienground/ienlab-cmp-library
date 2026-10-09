package zone.ien.utils.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface

/**
 * IEN 테마의 둥근 표면에 액션과 선택적인 플로팅 액션 버튼을 배치하는 하단 바입니다.
 * [IenScaffold]의 bottomBar 슬롯에 사용할 수 있으며, 액션 영역이 남은 너비를 차지합니다.
 *
 * @param actions 시작 쪽 액션 영역. RowScope의 weight로 액션 사이 간격을 조절할 수 있습니다.
 * @param modifier 안전 영역과 외부 여백을 포함하는 하단 바 전체에 적용할 Modifier
 * @param floatingActionButton 끝 쪽에 표시할 선택적인 플로팅 액션 버튼
 * @param containerColor 하단 바 표면 색상
 * @param contentColor 내부 콘텐츠 기본 색상
 * @param shape 하단 바 표면 모양
 * @param elevation 표면 그림자의 높이
 * @param contentPadding 표면 내부 콘텐츠 여백
 * @param windowInsets 표면 바깥에 적용하고 소비할 안전 영역 인셋
 * @param visible 하단 바 표시 여부
 */
@Composable
fun IenBottomBar(
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: (@Composable () -> Unit)? = null,
    containerColor: Color = IenTheme.colors.surface,
    contentColor: Color = IenTheme.colors.textPrimary,
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.full),
    elevation: Dp = IenTheme.elevation.floating,
    contentPadding: PaddingValues = PaddingValues(IenTheme.spacing.xs),
    windowInsets: WindowInsets = IenBottomBarDefaults.windowInsets,
    visible: Boolean = true,
) {
    IenBottomBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = shape,
        elevation = elevation,
        contentPadding = contentPadding,
        windowInsets = windowInsets,
        visible = visible,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
        if (floatingActionButton != null) {
            Box(
                modifier = Modifier.padding(start = IenTheme.spacing.sm),
                contentAlignment = Alignment.Center,
            ) {
                floatingActionButton()
            }
        }
    }
}

/**
 * 호출자가 RowScope 안에서 콘텐츠를 자유롭게 배치할 수 있는 하단 바입니다.
 * 버튼의 클릭 동작과 접근성 설명은 콘텐츠를 제공하는 호출자가 지정합니다.
 *
 * @param modifier 안전 영역과 외부 여백을 포함하는 하단 바 전체에 적용할 Modifier
 * @param containerColor 하단 바 표면 색상
 * @param contentColor 내부 콘텐츠 기본 색상
 * @param shape 하단 바 표면 모양
 * @param elevation 표면 그림자의 높이
 * @param contentPadding 표면 내부 콘텐츠 여백
 * @param windowInsets 표면 바깥에 적용하고 소비할 안전 영역 인셋
 * @param content 하단 바 콘텐츠. weight와 Spacer로 각 항목의 배치를 조절할 수 있습니다.
 */
@Composable
fun IenBottomBar(
    modifier: Modifier = Modifier,
    containerColor: Color = IenTheme.colors.surface,
    contentColor: Color = IenTheme.colors.textPrimary,
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.full),
    elevation: Dp = IenTheme.elevation.floating,
    contentPadding: PaddingValues = PaddingValues(IenTheme.spacing.xs),
    windowInsets: WindowInsets = IenBottomBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit,
) {
    IenBottomBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = shape,
        elevation = elevation,
        contentPadding = contentPadding,
        windowInsets = windowInsets,
        visible = true,
        content = content,
    )
}

/**
 * 호출자가 표시 여부와 RowScope 콘텐츠를 지정하는 하단 바입니다.
 *
 * @param modifier 안전 영역과 외부 여백을 포함하는 하단 바 전체에 적용할 Modifier
 * @param containerColor 하단 바 표면 색상
 * @param contentColor 내부 콘텐츠 기본 색상
 * @param shape 하단 바 표면 모양
 * @param elevation 표면 그림자의 높이
 * @param contentPadding 표면 내부 콘텐츠 여백
 * @param windowInsets 표면 바깥에 적용하고 소비할 안전 영역 인셋
 * @param visible 하단 바 표시 여부
 * @param content 하단 바 콘텐츠. weight와 Spacer로 각 항목의 배치를 조절할 수 있습니다.
 */
@Composable
fun IenBottomBar(
    modifier: Modifier = Modifier,
    containerColor: Color = IenTheme.colors.surface,
    contentColor: Color = IenTheme.colors.textPrimary,
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.full),
    elevation: Dp = IenTheme.elevation.floating,
    contentPadding: PaddingValues = PaddingValues(IenTheme.spacing.xs),
    windowInsets: WindowInsets = IenBottomBarDefaults.windowInsets,
    visible: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = IenTheme.motion.fastMillis,
                easing = IenTheme.motion.standardEasing,
            ),
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
            ),
        ) + slideOutVertically(
            animationSpec = tween(
                durationMillis = IenTheme.motion.normalMillis,
                easing = IenTheme.motion.standardEasing,
            ),
            targetOffsetY = { it },
        ),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.xs),
        ) {
            IenSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = elevation, shape = shape, clip = false),
                color = containerColor,
                contentColor = contentColor,
                shape = shape,
            ) {
                IenProvideTextStyle(IenTheme.typography.body2, contentColor) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = IenBottomBarDefaults.ContentHeight)
                            .padding(contentPadding),
                        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
                        verticalAlignment = Alignment.CenterVertically,
                        content = content,
                    )
                }
            }
        }
    }
}

/** 하단 바의 기본 크기와 안전 영역 설정입니다. */
object IenBottomBarDefaults {
    /** 내부 여백을 포함한 표면의 최소 높이입니다. 큰 콘텐츠는 높이를 확장합니다. */
    val ContentHeight: Dp = 64.dp

    /** 하단 시스템 영역과 가로 방향 디스플레이 안전 영역을 피하는 기본 인셋입니다. */
    val windowInsets: WindowInsets
        @Composable get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
}
