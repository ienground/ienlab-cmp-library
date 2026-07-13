package zone.ien.utils.ui.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.utils.conditional

internal val DefaultMenuProperties = PopupProperties(
    focusable = true,
    clippingEnabled = false,
)

/**
 * IenDropdownMenu는 드롭다운 메뉴를 표시하기 위한 컴포저블입니다.
 *
 * @param expanded 드롭다운이 열려 있는지 여부
 * @param onDismissRequest 드롭다운을 닫기 위한 콜백 함수
 * @param modifier 적용할 Modifier
 * @param offset 오프셋
 * @param scrollState 스크롤 상태
 * @param properties 다이얼로그 속성
 * @param shape 모양
 * @param containerColor 컨테이너 색상
 * @param tonalElevation 톤탈 침강
 * @param shadowElevation 그림자 침강
 * @param border 테두리
 * @param shadowPadding 그림자가 잘리지 않도록 팝업 측정 영역에 더할 여유 공간
 * @param innerPadding 내부 패딩
 * @param content 메뉴 내용
 */
@Composable
fun IenDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    scrollState: ScrollState = rememberScrollState(),
    properties: PopupProperties = DefaultMenuProperties,
    shape: Shape = RoundedCornerShape(IenTheme.radius.lg),
    containerColor: Color = IenTheme.colors.surfaceRaised,
    tonalElevation: Dp = IenTheme.elevation.none,
    shadowElevation: Dp = IenTheme.elevation.floating,
    border: BorderStroke? = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
    shadowPadding: Dp = 24.dp,
    innerPadding: PaddingValues = PaddingValues(IenTheme.spacing.xxs),
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current
    val visibilityState = remember { MutableTransitionState(false) }
    LaunchedEffect(expanded) {
        visibilityState.targetState = expanded
    }

    if (visibilityState.currentState || visibilityState.targetState) {
        Popup(
            popupPositionProvider = remember(offset, density, shadowPadding) {
                object : PopupPositionProvider {
                    override fun calculatePosition(
                        anchorBounds: IntRect,
                        windowSize: IntSize,
                        layoutDirection: LayoutDirection,
                        popupContentSize: IntSize,
                    ): IntOffset {
                        val offsetX = with(density) { offset.x.roundToPx() }
                        val offsetY = with(density) { offset.y.roundToPx() }
                        val shadowPaddingPx = with(density) { shadowPadding.roundToPx() }
                        val cardSize = IntSize(
                            width = (popupContentSize.width - shadowPaddingPx * 2).coerceAtLeast(0),
                            height = (popupContentSize.height - shadowPaddingPx * 2).coerceAtLeast(0),
                        )
                        val cardX = anchorBounds.right - cardSize.width + offsetX
                        val cardY = anchorBounds.bottom + offsetY
                        return IntOffset(
                            x = cardX.coerceIn(8, maxOf(8, windowSize.width - cardSize.width - 8)),
                            y = cardY.coerceIn(8, maxOf(8, windowSize.height - cardSize.height - 8)),
                        )
                    }
                }
            },
            onDismissRequest = onDismissRequest,
            properties = properties,
        ) {
            AnimatedVisibility(
                visibleState = visibilityState,
                enter = fadeIn(animationSpec = tween(120)) + scaleIn(
                    initialScale = 0.96f,
                    transformOrigin = TransformOrigin(1f, 0f),
                    animationSpec = tween(160),
                ),
                exit = fadeOut(animationSpec = tween(90)) + scaleOut(
                    targetScale = 0.98f,
                    transformOrigin = TransformOrigin(1f, 0f),
                    animationSpec = tween(90),
                ),
            ) {
                Surface(
                    modifier = modifier
                        .padding(shadowPadding)
                        .offset(x = -shadowPadding, y = -shadowPadding)
                        .shadow(elevation = shadowElevation, shape = shape, clip = false),
                    shape = shape,
                    color = containerColor,
                    tonalElevation = tonalElevation,
                    shadowElevation = 0.dp,
                    border = border,
                ) {
                    Column(
                        modifier = Modifier
                            .widthIn(min = 112.dp, max = 280.dp)
                            .width(IntrinsicSize.Max)
                            .padding(innerPadding),
                        content = content,
                    )
                }
            }
        }
    }
}

/**
 * IenDropdownMenuItem은 드롭다운 메뉴 항목을 표시하기 위한 컴포저블입니다.
 *
 * @param text 항목 텍스트
 * @param onClick 항목 클릭 시 호출되는 콜백 함수
 * @param modifier 적용할 Modifier
 * @param leadingIcon leading 아이콘
 * @param trailingIcon trailing 아이콘
 * @param enabled 활성화 여부
 * @param colors 항목 색상
 * @param contentPadding 내용 패딩
 * @param shape 모양
 * @param interactionSource 상호작용 소스
 */
@Composable
fun IenDropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: MenuItemColors = MenuDefaults.itemColors(
        textColor = IenTheme.colors.textPrimary,
        leadingIconColor = IenTheme.colors.textSecondary,
        trailingIconColor = IenTheme.colors.textSecondary,
        disabledTextColor = IenTheme.colors.textDisabled,
        disabledLeadingIconColor = IenTheme.colors.textDisabled,
        disabledTrailingIconColor = IenTheme.colors.textDisabled,
    ),
    contentPadding: PaddingValues = PaddingValues(horizontal = IenTheme.spacing.sm, vertical = IenTheme.spacing.xs),
    shape: Shape? = RoundedCornerShape(IenTheme.radius.sm),
    interactionSource: MutableInteractionSource? = null,
) {
    DropdownMenuItem(
        text = text,
        onClick = onClick,
        modifier = modifier.conditional(shape != null) { shape?.let { clip(it) } ?: this },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    )
}
