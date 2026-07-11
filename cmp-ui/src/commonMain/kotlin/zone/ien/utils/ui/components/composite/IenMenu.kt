package zone.ien.utils.ui.components.composite

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
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenIcon
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.fill.Check
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText
import zone.ien.utils.ui.utils.instantPress

object IenMenu {
    private val ShadowPadding = 48.dp

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
