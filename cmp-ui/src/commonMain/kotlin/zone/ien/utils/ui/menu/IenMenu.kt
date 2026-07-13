package zone.ien.utils.ui.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.fill.Check
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.utils.instantPress

/**
 * 앵커 컴포저블을 감싸서 팝업 메뉴를 표시하는 유틸리티 오브젝트입니다.
 */
object IenMenu {
    private val ShadowPadding = 48.dp

    /**
     * 메뉴 팝업이 표시될 위치를 정의하는 열거형 클래스입니다.
     */
    enum class Placement {
        Top,
        TopStart,
        TopEnd,
        Right,
        RightStart,
        RightEnd,
        Bottom,
        BottomStart,
        BottomEnd,
        Left,
        LeftStart,
        LeftEnd,
    }

    /**
     * IenMenu를 호출하여 팝업 메뉴와 그 트리거가 되는 자식 컴포저블을 렌더링합니다.
     *
     * @param open 메뉴가 열려 있는지 여부
     * @param onClose 메뉴를 닫을 때 호출되는 콜백 함수
     * @param modifier 전체 컨테이너에 적용할 Modifier
     * @param offset 메뉴 팝업의 추가 오프셋
     * @param placement 메뉴가 나타날 방향 및 정렬 방식 (기본값: [Placement.BottomStart])
     * @param properties 팝업의 창 속성 (기본값: focusable, dismissOnClickOutside, dismissOnBackPress가 true로 설정됨)
     * @param dropdown 팝업 내부에 표시할 드롭다운 내용
     * @param children 메뉴를 열기 위한 트리거 역할을 할 앵커 컴포저블
     */
    @Composable
    operator fun invoke(
        open: Boolean,
        onClose: () -> Unit,
        modifier: Modifier = Modifier,
        offset: DpOffset = DpOffset(0.dp, 8.dp),
        placement: Placement = Placement.BottomStart,
        properties: PopupProperties = PopupProperties(
            focusable = true,
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
        dropdown: @Composable () -> Unit,
        children: @Composable () -> Unit,
    ) {
        Trigger(
            open = open,
            onClose = onClose,
            modifier = modifier,
            placement = placement,
            offset = offset,
            properties = properties,
            dropdown = dropdown,
            children = children,
        )
    }

    /**
     * 메뉴의 열림 상태를 내부 혹은 외부 상태로 제어할 수 있는 메뉴 트리거 컴포저블입니다.
     *
     * @param modifier 트리거 컨테이너에 적용할 Modifier
     * @param open 메뉴가 열려 있는지 여부 (외부 상태 제어용, null인 경우 내부 상태를 사용)
     * @param defaultOpen [open]이 제공되지 않았을 때의 초기 열림 상태
     * @param onOpen 메뉴가 열릴 때 호출되는 콜백 함수
     * @param onClose 메뉴가 닫힐 때 호출되는 콜백 함수
     * @param placement 메뉴가 나타날 방향 및 정렬 방식
     * @param offset 메뉴 팝업의 추가 오프셋
     * @param properties 팝업의 창 속성
     * @param dropdown 팝업 내부에 표시할 드롭다운 내용
     * @param children 메뉴를 열기 위한 트리거 역할을 할 컴포저블
     */
    @Composable
    fun Trigger(
        modifier: Modifier = Modifier,
        open: Boolean? = null,
        defaultOpen: Boolean = false,
        onOpen: (() -> Unit)? = null,
        onClose: (() -> Unit)? = null,
        placement: Placement = Placement.BottomStart,
        offset: DpOffset = DpOffset.Zero,
        properties: PopupProperties = PopupProperties(
            focusable = true,
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
        dropdown: @Composable () -> Unit,
        children: @Composable () -> Unit,
    ) {
        val density = LocalDensity.current
        var internalOpen by remember { mutableStateOf(defaultOpen) }
        val expanded = open ?: internalOpen
        val requestOpen: () -> Unit = {
            if (open == null) {
                internalOpen = true
            }
            onOpen?.invoke()
        }
        val requestClose: () -> Unit = {
            if (open == null) {
                internalOpen = false
            }
            onClose?.invoke()
        }

        val visibilityState = remember { MutableTransitionState(false) }
        LaunchedEffect(expanded) {
            visibilityState.targetState = expanded
        }

        Box(
            modifier = modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
                onClick = requestOpen,
            ),
        ) {
            children()
            if (visibilityState.currentState || visibilityState.targetState) {
                Popup(
                    popupPositionProvider = remember(offset, placement, density) {
                        object : PopupPositionProvider {
                            override fun calculatePosition(
                                anchorBounds: IntRect,
                                windowSize: IntSize,
                                layoutDirection: LayoutDirection,
                                popupContentSize: IntSize,
                            ): IntOffset {
                                val offsetX = with(density) { offset.x.roundToPx() }
                                val offsetY = with(density) { offset.y.roundToPx() }
                                val shadowPadding = with(density) { ShadowPadding.roundToPx() }
                                val cardSize = IntSize(
                                    width = (popupContentSize.width - shadowPadding * 2).coerceAtLeast(0),
                                    height = (popupContentSize.height - shadowPadding * 2).coerceAtLeast(0),
                                )
                                val cardX = placement.menuX(anchorBounds, cardSize, offsetX)
                                val cardY = placement.menuY(anchorBounds, cardSize, offsetY)
                                val clampedCardX = cardX.coerceIn(
                                    8,
                                    maxOf(8, windowSize.width - cardSize.width - 8),
                                )
                                val clampedCardY = cardY.coerceIn(
                                    8,
                                    maxOf(8, windowSize.height - cardSize.height - 8),
                                )
                                return IntOffset(
                                    x = clampedCardX,
                                    y = clampedCardY,
                                )
                            }
                        }
                    },
                    onDismissRequest = requestClose,
                    properties = properties,
                ) {
                    AnimatedVisibility(
                        visibleState = visibilityState,
                        enter = fadeIn(animationSpec = tween(120)) + scaleIn(
                            initialScale = 0.96f,
                            transformOrigin = placement.transformOrigin(),
                            animationSpec = tween(160),
                        ),
                        exit = fadeOut(animationSpec = tween(90)) + scaleOut(
                            targetScale = 0.98f,
                            transformOrigin = placement.transformOrigin(),
                            animationSpec = tween(90),
                        ),
                    ) {
                        dropdown()
                    }
                }
            }
        }
    }

    /**
     * IenMenu의 드롭다운 컨테이너를 구성하는 컴포저블입니다.
     *
     * @param modifier 드롭다운 컨테이너에 적용할 Modifier
     * @param onDismissRequest 드롭다운 영역 밖을 터치하거나 닫기를 원할 때 호출되는 콜백 함수
     * @param header 드롭다운 최상단에 표시할 헤더 컴포저블
     * @param shape 드롭다운 모서리 둥글기 모양 (기본값: 28.dp RoundedCornerShape)
     * @param minWidth 최소 너비 (기본값: 180.dp)
     * @param maxWidth 최대 너비 (기본값: 280.dp)
     * @param content 드롭다운 항목들을 포함할 내부 컴포저블
     */
    @Composable
    fun Dropdown(
        modifier: Modifier = Modifier,
        onDismissRequest: (() -> Unit)? = null,
        header: (@Composable () -> Unit)? = null,
        shape: Shape = RoundedCornerShape(28.dp),
        minWidth: Dp = 180.dp,
        maxWidth: Dp = 280.dp,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        val cardInteractionSource = remember { MutableInteractionSource() }
        Box(
            modifier = modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = onDismissRequest != null,
                    onClick = { onDismissRequest?.invoke() },
                )
                .padding(ShadowPadding),
        ) {
            IenSurface(
                modifier = Modifier
                    .offset(x = -ShadowPadding, y = -ShadowPadding)
                    .widthIn(min = minWidth, max = maxWidth)
                    .shadow(elevation = ShadowPadding - 24.dp, shape = shape, clip = false)
                    .clickable(
                        interactionSource = cardInteractionSource,
                        indication = null,
                        onClick = {},
                    ),
                color = IenTheme.colors.surfaceRaised,
                shape = shape,
                border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border.copy(alpha = 0.35f)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .padding(vertical = IenTheme.spacing.sm),
                ) {
                    header?.invoke()
                    content()
                }
            }
        }
    }

    /**
     * 드롭다운 메뉴 최상단에 텍스트 형태의 헤더를 렌더링하는 컴포저블입니다.
     *
     * @param text 헤더에 표시할 텍스트
     * @param modifier 헤더 영역에 적용할 Modifier
     */
    @Composable
    fun Header(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        Header(modifier = modifier) {
            IenText(
                text = text,
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textTertiary,
            )
        }
    }

    /**
     * 드롭다운 메뉴 최상단에 커스텀 헤더 내용을 렌더링하는 컴포저블입니다.
     *
     * @param modifier 헤더 영역에 적용할 Modifier
     * @param content 헤더 내부에 표시할 컴포저블
     */
    @Composable
    fun Header(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = IenTheme.spacing.xs),
        ) {
            content()
        }
    }

    /**
     * 텍스트 형태의 일반 메뉴 항목을 렌더링하는 컴포저블입니다.
     *
     * @param text 항목에 표시할 텍스트
     * @param onClick 항목 클릭 시 호출되는 콜백 함수
     * @param modifier 항목에 적용할 Modifier
     * @param left 항목 왼쪽에 표시할 컴포저블 (예: 아이콘)
     * @param right 항목 오른쪽에 표시할 컴포저블
     * @param enabled 항목 활성화 여부
     * @param selected 항목 선택 여부
     */
    @Composable
    fun DropdownItem(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        left: (@Composable () -> Unit)? = null,
        right: (@Composable () -> Unit)? = null,
        enabled: Boolean = true,
        selected: Boolean = false,
    ) {
        DropdownItem(
            onClick = onClick,
            modifier = modifier,
            left = left,
            right = right,
            enabled = enabled,
            selected = selected,
        ) {
            IenText(
                text = text,
                style = IenTheme.typography.body2,
                color = when {
                    !enabled -> IenTheme.colors.textDisabled
                    selected -> IenTheme.colors.brand
                    else -> IenTheme.colors.textPrimary
                },
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
    }

    /**
     * 커스텀 내용을 구성할 수 있는 일반 메뉴 항목 컴포저블입니다.
     *
     * @param onClick 항목 클릭 시 호출되는 콜백 함수
     * @param modifier 항목에 적용할 Modifier
     * @param left 항목 왼쪽에 표시할 컴포저블 (예: 아이콘)
     * @param right 항목 오른쪽에 표시할 컴포저블
     * @param enabled 항목 활성화 여부
     * @param selected 항목 선택 여부
     * @param content 항목의 본문 내용을 구성하는 컴포저블
     */
    @Composable
    fun DropdownItem(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        left: (@Composable () -> Unit)? = null,
        right: (@Composable () -> Unit)? = null,
        enabled: Boolean = true,
        selected: Boolean = false,
        content: @Composable () -> Unit,
    ) {
        MenuItemLayout(
            modifier = modifier,
            enabled = enabled,
            selected = selected,
            role = Role.Button,
            onClick = onClick,
            left = left,
            right = right,
            content = content,
        )
    }

    /**
     * 체크 상태(체크박스 형태)를 가지는 텍스트 메뉴 항목 컴포저블입니다.
     *
     * @param checked 체크 여부
     * @param onCheckedChange 체크 상태 변경 시 호출되는 콜백 함수
     * @param text 항목에 표시할 텍스트
     * @param modifier 항목에 적용할 Modifier
     * @param enabled 항목 활성화 여부
     * @param right 항목 오른쪽에 표시할 컴포저블
     */
    @Composable
    fun DropdownCheckItem(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        text: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        right: (@Composable () -> Unit)? = null,
    ) {
        DropdownCheckItem(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
            right = right,
        ) {
            IenText(
                text = text,
                style = IenTheme.typography.body2,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
                fontWeight = if (checked) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
    }

    /**
     * 체크 상태(체크박스 형태)를 가지며 커스텀 본문 내용을 구성할 수 있는 메뉴 항목 컴포저블입니다.
     *
     * @param checked 체크 여부
     * @param onCheckedChange 체크 상태 변경 시 호출되는 콜백 함수
     * @param modifier 항목에 적용할 Modifier
     * @param enabled 항목 활성화 여부
     * @param right 항목 오른쪽에 표시할 컴포저블
     * @param content 항목의 본문 내용을 구성하는 컴포저블
     */
    @Composable
    fun DropdownCheckItem(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        right: (@Composable () -> Unit)? = null,
        content: @Composable () -> Unit,
    ) {
        MenuItemLayout(
            modifier = modifier,
            enabled = enabled,
            selected = checked,
            role = Role.Checkbox,
            onClick = { onCheckedChange(!checked) },
            left = { CheckIndicator(checked = checked, enabled = enabled) },
            right = right,
            content = content,
        )
    }

    /**
     * 드롭다운 메뉴 항목에서 사용하기 위해 최적화된 아이콘 컴포저블입니다.
     *
     * @param imageVector 표시할 [ImageVector]
     * @param contentDescription 아이콘의 설명 텍스트
     * @param modifier 아이콘에 적용할 Modifier
     * @param tint 아이콘 색상 (기본값: textTertiary)
     */
    @Composable
    fun DropdownIcon(
        imageVector: ImageVector,
        contentDescription: String? = null,
        modifier: Modifier = Modifier,
        tint: Color = IenTheme.colors.textTertiary,
    ) {
        IenIcon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
            size = IenTheme.icon.sm,
        )
    }

    @Composable
    private fun MenuItemLayout(
        modifier: Modifier,
        enabled: Boolean,
        selected: Boolean,
        role: Role,
        onClick: () -> Unit,
        left: (@Composable () -> Unit)?,
        right: (@Composable () -> Unit)?,
        content: @Composable () -> Unit,
    ) {
        var isPressed by remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }
        val pressAlpha by animateFloatAsState(
            targetValue = if (isPressed && enabled) 1f else 0f,
            animationSpec = tween(durationMillis = 150),
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .instantPress(enabled = enabled) { isPressed = it }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    role = role,
                    onClick = onClick,
                ),
        ) {
            if (pressAlpha > 0f || selected) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .graphicsLayer(alpha = if (pressAlpha > 0f) pressAlpha else 0.4f)
                        .background(if (selected) IenTheme.colors.brandWeak else IenTheme.colors.surfaceVariant),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                left?.invoke()
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
                right?.invoke()
            }
        }
    }

    @Composable
    private fun CheckIndicator(
        checked: Boolean,
        enabled: Boolean,
    ) {
        Box(
            modifier = Modifier.size(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                IenIcon(
                    imageVector = RemixIcons.Fill.Check,
                    contentDescription = "선택됨",
                    modifier = Modifier.size(16.dp),
                    tint = if (enabled) IenTheme.colors.brand else IenTheme.colors.textDisabled,
                )
            }
        }
    }
}

private fun IenMenu.Placement.menuX(
    anchorBounds: IntRect,
    cardSize: IntSize,
    offsetX: Int,
): Int = when (this) {
    IenMenu.Placement.Left,
    IenMenu.Placement.LeftStart,
    IenMenu.Placement.LeftEnd -> anchorBounds.left - cardSize.width - offsetX
    IenMenu.Placement.Right,
    IenMenu.Placement.RightStart,
    IenMenu.Placement.RightEnd -> anchorBounds.right + offsetX
    IenMenu.Placement.TopStart,
    IenMenu.Placement.BottomStart -> anchorBounds.left + offsetX
    IenMenu.Placement.TopEnd,
    IenMenu.Placement.BottomEnd -> anchorBounds.right - cardSize.width + offsetX
    IenMenu.Placement.Top,
    IenMenu.Placement.Bottom -> anchorBounds.left + (anchorBounds.width - cardSize.width) / 2 + offsetX
}

private fun IenMenu.Placement.menuY(
    anchorBounds: IntRect,
    cardSize: IntSize,
    offsetY: Int,
): Int = when (this) {
    IenMenu.Placement.Top,
    IenMenu.Placement.TopStart,
    IenMenu.Placement.TopEnd -> anchorBounds.top - cardSize.height - offsetY
    IenMenu.Placement.Bottom,
    IenMenu.Placement.BottomStart,
    IenMenu.Placement.BottomEnd -> anchorBounds.bottom + offsetY
    IenMenu.Placement.LeftStart,
    IenMenu.Placement.RightStart -> anchorBounds.top + offsetY
    IenMenu.Placement.LeftEnd,
    IenMenu.Placement.RightEnd -> anchorBounds.bottom - cardSize.height + offsetY
    IenMenu.Placement.Left,
    IenMenu.Placement.Right -> anchorBounds.top + (anchorBounds.height - cardSize.height) / 2 + offsetY
}

private fun IenMenu.Placement.transformOrigin(): TransformOrigin = when (this) {
    IenMenu.Placement.TopStart -> TransformOrigin(0f, 1f)
    IenMenu.Placement.Top -> TransformOrigin(0.5f, 1f)
    IenMenu.Placement.TopEnd -> TransformOrigin(1f, 1f)
    IenMenu.Placement.BottomStart -> TransformOrigin(0f, 0f)
    IenMenu.Placement.Bottom -> TransformOrigin(0.5f, 0f)
    IenMenu.Placement.BottomEnd -> TransformOrigin(1f, 0f)
    IenMenu.Placement.LeftStart -> TransformOrigin(1f, 0f)
    IenMenu.Placement.Left -> TransformOrigin(1f, 0.5f)
    IenMenu.Placement.LeftEnd -> TransformOrigin(1f, 1f)
    IenMenu.Placement.RightStart -> TransformOrigin(0f, 0f)
    IenMenu.Placement.Right -> TransformOrigin(0f, 0.5f)
    IenMenu.Placement.RightEnd -> TransformOrigin(0f, 1f)
}
