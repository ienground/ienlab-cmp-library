package zone.ien.utils.ui.interactive

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.kyant.capsule.ContinuousRoundedRectangle
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface

/** 액션 바가 나타나고 사라지는 세로 방향입니다. */
enum class IenActionBarSide {
    Top,
    Bottom,
}

internal fun IenActionBarSide.alignment(horizontal: Alignment.Horizontal): Alignment = object : Alignment {
    override fun align(size: IntSize, space: IntSize, layoutDirection: LayoutDirection): IntOffset = IntOffset(
        x = horizontal.align(size.width, space.width, layoutDirection),
        y = if (this@alignment == IenActionBarSide.Top) 0 else space.height - size.height,
    )
}

/** 액션 바의 크기 기본값입니다. */
object IenActionBarDefaults {
    val MaxWidth: Dp = 640.dp
}

/**
 * 선택한 항목의 동작을 위나 아래 가장자리에서 슬라이드하여 표시하는 플로팅 액션 바입니다.
 *
 * 화면의 Box 안에서 본문 다음에 배치합니다. 루트는 부모 영역을 채우며 액션 바 바깥의 터치를
 * 차단하지 않습니다. 본문을 밀어내지 않으며, 표시 상태는 호출자가 관리합니다.
 * 닫을 때도 컴포저블을 유지해야 퇴장 애니메이션이 실행됩니다. 자동으로 닫히지 않습니다.
 * 가로 공간이 부족하면 선택 정보와 액션 영역을 스크롤하며 닫기 버튼은 고정합니다.
 * 포커스를 자동으로 가져오지 않으며, 바 내부에 포커스가 있을 때 Escape로 닫을 수 있습니다.
 *
 * @param open 액션 바의 표시 여부입니다.
 * @param onOpenChange 닫기 버튼 또는 Escape 입력으로 요청하는 표시 상태입니다.
 * @param modifier 부모 영역을 덮는 루트에 적용할 Modifier입니다.
 * @param side 표시할 가장자리와 슬라이드 방향입니다.
 * @param align 가로 정렬입니다. Start와 End는 RTL 방향을 따릅니다.
 * @param sideOffset 안전 영역의 위 또는 아래 가장자리에서 떨어질 거리입니다.
 * @param alignOffset 가로 정렬 위치에서 이동할 거리입니다. 양수는 RTL을 고려한 끝 방향입니다.
 * @param windowInsets 피할 시스템 영역입니다. 부모가 이미 처리하면 WindowInsets(0)을 전달합니다.
 * @param maxWidth 액션 바의 최대 너비입니다.
 * @param contentPadding 액션 바 내부의 여백입니다.
 * @param containerColor 액션 바의 배경색입니다.
 * @param contentColor 콘텐츠에 제공하는 기본 전경색입니다.
 * @param selection 선택 개수 등의 정보입니다. 지정하면 액션 앞에 구분선을 표시합니다.
 * @param close 닫기 영역입니다. null이면 생략하며, 기본 버튼은 onOpenChange(false)를 호출합니다.
 * @param content 기존 IenButton 등으로 구성하는 가로 액션 영역입니다. 선택 후 닫기는 호출자가 결정합니다.
 */
@Composable
fun IenActionBar(
    open: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    side: IenActionBarSide = IenActionBarSide.Bottom,
    align: Alignment.Horizontal = Alignment.CenterHorizontally,
    sideOffset: Dp = IenTheme.spacing.md,
    alignOffset: Dp = 0.dp,
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
    maxWidth: Dp = IenActionBarDefaults.MaxWidth,
    contentPadding: PaddingValues = PaddingValues(IenTheme.spacing.xs),
    containerColor: Color = IenTheme.colors.surfaceRaised,
    contentColor: Color = IenTheme.colors.textPrimary,
    selection: (@Composable () -> Unit)? = null,
    close: (@Composable () -> Unit)? = {
        IenIconButton(
            onClick = { onOpenChange(false) },
            size = IenButtonSize.Small,
            variant = IenButtonVariant.Ghost,
            tone = IenSemanticTone.Neutral,
        ) {
            IenIcon(IconData.Vector(SystemIcons.Close), contentDescription = stringResource(Res.string.close))
        }
    },
    content: @Composable RowScope.() -> Unit,
) {
    val motion = IenTheme.motion
    val travel = with(LocalDensity.current) { sideOffset.roundToPx() }
    val direction = if (side == IenActionBarSide.Top) -1 else 1
    val shape = ContinuousRoundedRectangle(IenTheme.radius.xl)
    val shadowPadding = IenTheme.elevation.floating * 2
    Box(
        modifier = modifier.fillMaxSize().windowInsetsPadding(windowInsets)
            .padding(horizontal = IenTheme.spacing.md, vertical = sideOffset),
        contentAlignment = side.alignment(align),
    ) {
        AnimatedVisibility(
            visible = open,
            modifier = Modifier.offset(x = alignOffset).layout { measurable, constraints ->
                // 페이드 레이어에 그림자 영역을 포함하되 외부 배치 크기는 유지합니다.
                val padding = shadowPadding.roundToPx()
                val placeable = measurable.measure(
                    constraints.offset(horizontal = padding * 2, vertical = padding * 2),
                )
                layout(placeable.width - padding * 2, placeable.height - padding * 2) {
                    placeable.place(-padding, -padding)
                }
            },
            enter = slideInVertically(
                animationSpec = tween(motion.normalMillis, easing = motion.standardEasing),
                initialOffsetY = { direction * (it + travel) },
            ) + fadeIn(tween(motion.fastMillis)),
            exit = slideOutVertically(
                animationSpec = tween(motion.normalMillis, easing = motion.standardEasing),
                targetOffsetY = { direction * (it + travel) },
            ) + fadeOut(tween(motion.fastMillis)),
        ) {
            Box(Modifier.padding(shadowPadding)) {
                IenSurface(
                    modifier = Modifier.widthIn(max = maxWidth)
                        .shadow(IenTheme.elevation.floating, shape, clip = false)
                        .onPreviewKeyEvent {
                            if (open && it.key == Key.Escape && it.type == KeyEventType.KeyUp) {
                                onOpenChange(false)
                                true
                            } else {
                                false
                            }
                        }
                        .semantics { isTraversalGroup = true },
                    color = containerColor,
                    contentColor = contentColor,
                    shape = shape,
                    border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
                ) {
                    IenProvideTextStyle(IenTheme.typography.label1, contentColor) {
                        Row(
                            modifier = Modifier.padding(contentPadding),
                            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                modifier = Modifier.weight(1f, fill = false).horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (selection != null) {
                                    Box(Modifier.semantics { liveRegion = LiveRegionMode.Polite }) { selection() }
                                    IenDivider.Vertical(Modifier.height(IenTheme.spacing.xl))
                                }
                                content()
                            }
                            close?.invoke()
                        }
                    }
                }
            }
        }
    }
}
