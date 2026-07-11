package zone.ien.utils.ui.components.composite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.IenButton
import zone.ien.utils.ui.components.interactive.IenButtonSize
import zone.ien.utils.ui.components.interactive.IenButtonVariant
import zone.ien.utils.ui.components.interactive.IenButtonDisplay
import zone.ien.utils.ui.components.interactive.IenCheckbox
import zone.ien.utils.ui.components.interactive.IenBadge
import zone.ien.utils.ui.components.interactive.IenBadgeSize
import zone.ien.utils.ui.components.interactive.IenBadgeVariant
import zone.ien.utils.ui.components.interactive.IenButtonState
import zone.ien.utils.ui.components.interactive.IenTextButton
import zone.ien.utils.ui.components.interactive.IenTextButtonSize
import zone.ien.utils.ui.components.interactive.IenTextButtonVariant
import zone.ien.utils.ui.components.primitives.IenDivider
import zone.ien.utils.ui.components.primitives.IenProvideTextStyle
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText
import kotlinx.coroutines.delay

enum class IenTopBarTitleAlignment {
    Start,
    Center,
}

enum class IenTopRightVerticalAlign {
    Center,
    End,
}

enum class IenTopTitleSize {
    Default,
    Large,
}

enum class IenTopSubtitleSize {
    Small,
    Medium,
    Large,
}

enum class IenTopSelectorType {
    Arrow,
    Clear,
}

@Immutable
data class IenTopSubtitleBadge(
    val text: String,
    val tone: IenSemanticTone = IenSemanticTone.Brand,
    val variant: IenBadgeVariant = IenBadgeVariant.Weak,
)

@Composable
fun IenScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floating: @Composable () -> Unit = {},
    containerColor: Color = IenTheme.colors.background,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floating,
        containerColor = containerColor,
        contentColor = IenTheme.colors.textPrimary,
        contentWindowInsets = contentWindowInsets,
        content = content,
    )
}

@Composable
fun IenTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    titleAlignment: IenTopBarTitleAlignment = IenTopBarTitleAlignment.Start,
    showDivider: Boolean = true,
    windowInsets: WindowInsets = WindowInsets.statusBars,
    contentPadding: PaddingValues = PaddingValues(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
) {
    val insetPadding = windowInsets.asPaddingValues()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(IenTheme.colors.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = insetPadding.calculateTopPadding())
                .defaultMinSize(minHeight = 56.dp)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            navigationIcon?.invoke()
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = if (titleAlignment == IenTopBarTitleAlignment.Center) Alignment.CenterHorizontally else Alignment.Start,
            ) {
                IenText(
                    text = title,
                    style = IenTheme.typography.title3,
                    textAlign = if (titleAlignment == IenTopBarTitleAlignment.Center) TextAlign.Center else null,
                )
                if (subtitle != null) {
                    IenText(
                        text = subtitle,
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textSecondary,
                        textAlign = if (titleAlignment == IenTopBarTitleAlignment.Center) TextAlign.Center else null,
                    )
                }
            }
            actions?.invoke(this)
        }
        if (showDivider) {
            IenDivider()
        }
    }
}

@Composable
fun IenTop(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    upperGap: Dp = 24.dp,
    lowerGap: Dp = 24.dp,
    upper: (@Composable () -> Unit)? = null,
    lower: (@Composable () -> Unit)? = null,
    subtitleTop: (@Composable () -> Unit)? = null,
    subtitleBottom: (@Composable () -> Unit)? = null,
    right: (@Composable () -> Unit)? = null,
    rightVerticalAlign: IenTopRightVerticalAlign = IenTopRightVerticalAlign.Center,
    contentPadding: PaddingValues = PaddingValues(horizontal = IenTheme.spacing.xl),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .padding(top = upperGap, bottom = lowerGap),
    ) {
        if (upper != null) {
            upper()
            Spacer(Modifier.height(IenTheme.spacing.md))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            verticalAlignment = when (rightVerticalAlign) {
                IenTopRightVerticalAlign.Center -> Alignment.CenterVertically
                IenTopRightVerticalAlign.End -> Alignment.Bottom
            },
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
            ) {
                subtitleTop?.invoke()
                title()
                subtitleBottom?.invoke()
            }
            if (right != null) {
                right()
            }
        }
        if (lower != null) {
            Spacer(Modifier.height(IenTheme.spacing.md))
            lower()
        }
    }
}

@Composable
fun IenTop(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigation: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    IenTop(
        modifier = modifier,
        upperGap = IenTheme.spacing.sm,
        lowerGap = IenTheme.spacing.sm,
        title = { IenTopTitleParagraph(title, size = IenTopTitleSize.Large) },
        subtitleBottom = subtitle?.let { { IenTopSubtitleParagraph(it, size = IenTopSubtitleSize.Small) } },
        upper = navigation,
        right = actions?.let { action -> { Row(content = action) } },
        contentPadding = PaddingValues(horizontal = IenTheme.spacing.md),
    )
}

@Composable
fun IenTopTitleParagraph(
    text: String,
    modifier: Modifier = Modifier,
    size: IenTopTitleSize = IenTopTitleSize.Default,
    color: Color = IenTheme.colors.textPrimary,
    style: TextStyle = size.titleStyle(),
    fontWeight: FontWeight = FontWeight.Bold,
    maxLines: Int = Int.MAX_VALUE,
) {
    IenText(
        text = text,
        modifier = modifier.semantics { heading() },
        style = style.copy(fontWeight = fontWeight),
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun IenTopTitleTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorTone: IenSemanticTone = IenSemanticTone.Neutral,
    variant: IenTextButtonVariant = IenTextButtonVariant.Clear,
    size: IenTextButtonSize = IenTextButtonSize.XLarge,
    state: IenButtonState = IenButtonState(),
) {
    IenTextButton(
        text = text,
        onClick = onClick,
        modifier = modifier.semantics { heading() },
        size = size,
        variant = variant,
        tone = colorTone,
        state = state,
    )
}

@Composable
fun IenTopTitleSelector(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: IenTopSelectorType = IenTopSelectorType.Arrow,
    color: Color = IenTheme.colors.textPrimary,
    style: TextStyle = IenTheme.typography.title2,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    IenTopSelector(
        text = text,
        onClick = onClick,
        modifier = modifier.semantics {
            heading()
            role = Role.Button
        },
        type = type,
        color = color,
        style = style.copy(fontWeight = fontWeight),
        iconSize = 22.dp,
    )
}

@Composable
fun IenTopSubtitleParagraph(
    text: String,
    modifier: Modifier = Modifier,
    size: IenTopSubtitleSize = IenTopSubtitleSize.Large,
    color: Color = IenTheme.colors.textSecondary,
    style: TextStyle = size.subtitleStyle(),
    fontWeight: FontWeight = size.subtitleWeight(),
    maxLines: Int = Int.MAX_VALUE,
) {
    IenText(
        text = text,
        modifier = modifier,
        style = style.copy(fontWeight = fontWeight),
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun IenTopSubtitleTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorTone: IenSemanticTone = IenSemanticTone.Neutral,
    variant: IenTextButtonVariant = IenTextButtonVariant.Arrow,
    size: IenTextButtonSize = IenTextButtonSize.Medium,
    state: IenButtonState = IenButtonState(),
) {
    IenTextButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        size = size,
        variant = variant,
        tone = colorTone,
        state = state,
    )
}

@Composable
fun IenTopSubtitleSelector(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: IenTopSelectorType = IenTopSelectorType.Arrow,
    size: IenTopSubtitleSize = IenTopSubtitleSize.Large,
    color: Color = IenTheme.colors.textSecondary,
    style: TextStyle = size.subtitleStyle(),
    fontWeight: FontWeight = size.subtitleWeight(),
) {
    IenTopSelector(
        text = text,
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        type = type,
        color = color,
        style = style.copy(fontWeight = fontWeight),
        iconSize = when (size) {
            IenTopSubtitleSize.Small -> 14.dp
            IenTopSubtitleSize.Medium -> 16.dp
            IenTopSubtitleSize.Large -> 18.dp
        },
    )
}

@Composable
fun IenTopSubtitleBadges(
    badges: List<IenTopSubtitleBadge>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        badges.forEach { badge ->
            IenBadge(
                text = badge.text,
                size = IenBadgeSize.Small,
                variant = badge.variant,
                tone = badge.tone,
            )
        }
    }
}

@Composable
fun IenTopLowerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Small,
    variant: IenButtonVariant = IenButtonVariant.Weak,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        size = size,
        variant = variant,
        tone = tone,
        state = state,
    )
}

@Composable
fun IenTopLowerCTA(
    leftButton: @Composable RowScope.() -> Unit,
    rightButton: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leftButton()
        rightButton()
    }
}

@Composable
fun RowScope.IenTopLowerCTAButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier.weight(1f),
        size = size,
        variant = variant,
        tone = tone,
        state = state,
        display = IenButtonDisplay.Block,
    )
}

@Composable
fun IenTopRightButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Medium,
    variant: IenButtonVariant = IenButtonVariant.Weak,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        size = size,
        variant = variant,
        tone = tone,
        state = state,
    )
}

@Composable
fun IenTopRightAssetContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.defaultMinSize(minWidth = 60.dp, minHeight = 60.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun IenTopUpperAssetContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.defaultMinSize(minWidth = 72.dp, minHeight = 72.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        content()
    }
}

@Composable
private fun IenTopSelector(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    type: IenTopSelectorType,
    color: Color,
    style: TextStyle,
    iconSize: Dp,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = IenTheme.spacing.xxs),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IenProvideTextStyle(style, color) {
            IenText(
                text = text,
                style = style,
                color = LocalContentColor.current,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (type == IenTopSelectorType.Arrow) {
            IenTopArrow(size = iconSize, color = color)
        }
    }
}

@Composable
private fun IenTopArrow(
    size: Dp,
    color: Color,
) {
    Canvas(modifier = Modifier.size(size)) {
        val strokeWidth = size.toPx() * 0.12f
        val startX = size.toPx() * 0.34f
        val endX = size.toPx() * 0.66f
        val topY = size.toPx() * 0.28f
        val centerY = size.toPx() * 0.50f
        val bottomY = size.toPx() * 0.72f

        drawLine(
            color = color,
            start = Offset(startX, topY),
            end = Offset(endX, centerY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(endX, centerY),
            end = Offset(startX, bottomY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun IenTopTitleSize.titleStyle(): TextStyle = when (this) {
    IenTopTitleSize.Default -> IenTheme.typography.title2
    IenTopTitleSize.Large -> IenTheme.typography.title1
}

@Composable
private fun IenTopSubtitleSize.subtitleStyle(): TextStyle = when (this) {
    IenTopSubtitleSize.Small -> IenTheme.typography.caption
    IenTopSubtitleSize.Medium -> IenTheme.typography.label1
    IenTopSubtitleSize.Large -> IenTheme.typography.body1
}

private fun IenTopSubtitleSize.subtitleWeight(): FontWeight = when (this) {
    IenTopSubtitleSize.Small,
    IenTopSubtitleSize.Medium -> FontWeight.Normal
    IenTopSubtitleSize.Large -> FontWeight.Medium
}

@Composable
fun IenTooltip(
    text: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    size: IenTooltipSize = IenTooltipSize.Medium,
    defaultOpen: Boolean = false,
    open: Boolean? = null,
    onOpenChange: ((Boolean) -> Unit)? = null,
    messageAlign: IenTooltipMessageAlign = IenTooltipMessageAlign.Left,
    placement: IenTooltipPlacement = IenTooltipPlacement.Bottom,
    motionVariant: IenTooltipMotionVariant = IenTooltipMotionVariant.Weak,
    offset: Dp? = null,
    anchorPositionByRatio: Float = 0.5f,
    openOnHover: Boolean = false,
    openOnFocus: Boolean = false,
    dismissible: Boolean = false,
    autoFlip: Boolean = false,
    strategy: IenTooltipStrategy = IenTooltipStrategy.Absolute,
    clipToEnd: IenTooltipClipToEnd = IenTooltipClipToEnd.None,
    width: Dp? = null,
    anchor: (@Composable BoxScope.() -> Unit)? = null,
) {
    var internalOpen by remember { mutableStateOf(defaultOpen) }
    val isOpen = open ?: internalOpen
    val resolvedPlacement = if (autoFlip && placement == IenTooltipPlacement.Top) {
        IenTooltipPlacement.Bottom
    } else {
        placement
    }
    val resolvedOffset = offset ?: size.offset()
    val motionScale by animateFloatAsState(
        targetValue = if (isOpen) 1f else motionVariant.hiddenScale(),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = motionVariant.stiffness(),
        ),
    )

    fun updateOpen(next: Boolean) {
        if (open == null) {
            internalOpen = next
        }
        onOpenChange?.invoke(next)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(resolvedOffset),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (resolvedPlacement == IenTooltipPlacement.Top) {
            IenTooltipPopup(
                visible = isOpen,
                text = text,
                tone = tone,
                size = size,
                messageAlign = messageAlign,
                anchorPositionByRatio = anchorPositionByRatio,
                clipToEnd = clipToEnd,
                placement = resolvedPlacement,
                motionVariant = motionVariant,
                scale = motionScale,
                width = width,
            )
        }

        Box(
            modifier = Modifier
                .then(
                    if (dismissible) {
                        Modifier.clickable(
                            indication = null,
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        ) { updateOpen(!isOpen) }
                    } else {
                        Modifier
                    },
                )
                .onFocusChanged { focusState ->
                    if (openOnFocus) {
                        updateOpen(focusState.isFocused)
                    }
                }
                .then(if (openOnFocus) Modifier.focusable() else Modifier)
                .semantics {
                    contentDescription = text
                },
            contentAlignment = Alignment.Center,
        ) {
            anchor?.invoke(this)
            if (anchor == null) {
                IenText("?", style = IenTheme.typography.label1, color = IenTheme.colors.brand)
            }
        }

        if (resolvedPlacement == IenTooltipPlacement.Bottom) {
            IenTooltipPopup(
                visible = isOpen || openOnHover,
                text = text,
                tone = tone,
                size = size,
                messageAlign = messageAlign,
                anchorPositionByRatio = anchorPositionByRatio,
                clipToEnd = clipToEnd,
                placement = resolvedPlacement,
                motionVariant = motionVariant,
                scale = motionScale,
                width = width,
            )
        }
    }
}

enum class IenTooltipSize {
    Small,
    Medium,
    Large,
}

enum class IenTooltipMessageAlign {
    Left,
    Center,
    Right,
}

enum class IenTooltipPlacement {
    Top,
    Bottom,
}

enum class IenTooltipMotionVariant {
    Weak,
    Strong,
}

enum class IenTooltipStrategy {
    Absolute,
    Fixed,
}

enum class IenTooltipClipToEnd {
    None,
    Left,
    Right,
}

@Composable
private fun IenTooltipPopup(
    visible: Boolean,
    text: String,
    tone: IenSemanticTone,
    size: IenTooltipSize,
    messageAlign: IenTooltipMessageAlign,
    anchorPositionByRatio: Float,
    clipToEnd: IenTooltipClipToEnd,
    placement: IenTooltipPlacement,
    motionVariant: IenTooltipMotionVariant,
    scale: Float,
    width: Dp?,
) {
    val color = if (tone == IenSemanticTone.Neutral) {
        IenTheme.colors.textPrimary
    } else {
        zone.ien.utils.ui.components.interactive.toneColor(tone)
    }
    val arrowRatio = anchorPositionByRatio.coerceIn(0f, 1f)
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = when (motionVariant) {
                IenTooltipMotionVariant.Weak -> IenTheme.motion.fastMillis
                IenTooltipMotionVariant.Strong -> IenTheme.motion.normalMillis
            },
            easing = IenTheme.motion.standardEasing,
        ),
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(IenTheme.motion.fastMillis)),
        exit = fadeOut(tween(IenTheme.motion.fastMillis)),
    ) {
        Column(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                    translationY = when (placement) {
                        IenTooltipPlacement.Top -> if (visible) 0f else 4f
                        IenTooltipPlacement.Bottom -> if (visible) 0f else -4f
                    }
                }
                .then(if (width != null) Modifier.width(width) else Modifier.widthIn(max = size.maxWidth())),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (placement == IenTooltipPlacement.Bottom) {
                IenTooltipArrowRow(
                    color = color,
                    size = size.arrowSize(),
                    ratio = arrowRatio,
                    placement = placement,
                    clipToEnd = clipToEnd,
                )
            }
            IenSurface(
                color = color,
                contentColor = IenTheme.colors.surface,
                shape = RoundedCornerShape(size.radius()),
            ) {
                IenText(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(size.contentPadding()),
                    style = size.textStyle(),
                    color = IenTheme.colors.surface,
                    textAlign = when (messageAlign) {
                        IenTooltipMessageAlign.Left -> TextAlign.Start
                        IenTooltipMessageAlign.Center -> TextAlign.Center
                        IenTooltipMessageAlign.Right -> TextAlign.End
                    },
                )
            }
            if (placement == IenTooltipPlacement.Top) {
                IenTooltipArrowRow(
                    color = color,
                    size = size.arrowSize(),
                    ratio = arrowRatio,
                    placement = placement,
                    clipToEnd = clipToEnd,
                )
            }
        }
    }
}

@Composable
private fun IenTooltipArrowRow(
    color: Color,
    size: Dp,
    ratio: Float,
    placement: IenTooltipPlacement,
    clipToEnd: IenTooltipClipToEnd,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(ratio.coerceIn(0.01f, 0.99f)))
        IenTooltipArrow(color = color, size = size, placement = placement, clipToEnd = clipToEnd)
        Spacer(modifier = Modifier.weight((1f - ratio).coerceIn(0.01f, 0.99f)))
    }
}

@Composable
private fun IenTooltipArrow(
    color: Color,
    size: Dp,
    placement: IenTooltipPlacement,
    clipToEnd: IenTooltipClipToEnd,
) {
    Canvas(
        modifier = Modifier
            .width(size)
            .height(size / 2),
    ) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            when (placement) {
                IenTooltipPlacement.Bottom -> {
                    moveTo(if (clipToEnd == IenTooltipClipToEnd.Right) w / 2f else 0f, h)
                    lineTo(w / 2f, 0f)
                    lineTo(if (clipToEnd == IenTooltipClipToEnd.Left) w / 2f else w, h)
                }
                IenTooltipPlacement.Top -> {
                    moveTo(if (clipToEnd == IenTooltipClipToEnd.Right) w / 2f else 0f, 0f)
                    lineTo(w / 2f, h)
                    lineTo(if (clipToEnd == IenTooltipClipToEnd.Left) w / 2f else w, 0f)
                }
            }
            close()
        }
        drawPath(path = path, color = color)
    }
}

private fun IenTooltipSize.offset(): Dp = when (this) {
    IenTooltipSize.Small -> 6.dp
    IenTooltipSize.Medium -> 8.dp
    IenTooltipSize.Large -> 10.dp
}

private fun IenTooltipSize.arrowSize(): Dp = when (this) {
    IenTooltipSize.Small -> 12.dp
    IenTooltipSize.Medium -> 14.dp
    IenTooltipSize.Large -> 16.dp
}

private fun IenTooltipSize.radius(): Dp = when (this) {
    IenTooltipSize.Small -> 6.dp
    IenTooltipSize.Medium -> 8.dp
    IenTooltipSize.Large -> 10.dp
}

private fun IenTooltipSize.maxWidth(): Dp = when (this) {
    IenTooltipSize.Small -> 180.dp
    IenTooltipSize.Medium -> 220.dp
    IenTooltipSize.Large -> 260.dp
}

private fun IenTooltipSize.contentPadding(): PaddingValues = when (this) {
    IenTooltipSize.Small -> PaddingValues(horizontal = 8.dp, vertical = 6.dp)
    IenTooltipSize.Medium -> PaddingValues(horizontal = 10.dp, vertical = 8.dp)
    IenTooltipSize.Large -> PaddingValues(horizontal = 12.dp, vertical = 10.dp)
}

@Composable
private fun IenTooltipSize.textStyle() = when (this) {
    IenTooltipSize.Small -> IenTheme.typography.caption
    IenTooltipSize.Medium -> IenTheme.typography.label2
    IenTooltipSize.Large -> IenTheme.typography.body2
}

private fun IenTooltipMotionVariant.hiddenScale(): Float = when (this) {
    IenTooltipMotionVariant.Weak -> 0.98f
    IenTooltipMotionVariant.Strong -> 0.92f
}

private fun IenTooltipMotionVariant.stiffness(): Float = when (this) {
    IenTooltipMotionVariant.Weak -> Spring.StiffnessMediumLow
    IenTooltipMotionVariant.Strong -> Spring.StiffnessMedium
}

enum class IenAgreementV4Variant {
    Plain,
    Box,
}

enum class IenAgreementV4RightVerticalAlign {
    Center,
    Top,
}

@Composable
fun IenAgreementV4(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: IenAgreementV4Variant = IenAgreementV4Variant.Plain,
    indent: Boolean = false,
    rightVerticalAlign: IenAgreementV4RightVerticalAlign = IenAgreementV4RightVerticalAlign.Center,
    onPressEnd: (() -> Unit)? = null,
    left: @Composable RowScope.() -> Unit = {
        IenAgreementV4Checkbox(
            checked = checked,
            onCheckedChange = {
                onCheckedChange(it)
                onPressEnd?.invoke()
            },
            enabled = enabled,
        )
    },
    middle: @Composable ColumnScope.() -> Unit,
    right: (@Composable RowScope.() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(IenTheme.radius.default)
    val baseRowModifier = Modifier
        .fillMaxWidth()
        .clip(shape)
        .clickable(enabled = enabled, role = Role.Checkbox) {
            onCheckedChange(!checked)
            onPressEnd?.invoke()
        }
        .padding(
            start = if (indent) 48.dp else IenTheme.spacing.md,
            top = IenTheme.spacing.sm,
            end = IenTheme.spacing.md,
            bottom = IenTheme.spacing.sm,
        )
    val rowModifier = if (variant == IenAgreementV4Variant.Box) {
        baseRowModifier
    } else {
        modifier.then(baseRowModifier)
    }

    val content: @Composable () -> Unit = {
        Row(
            modifier = rowModifier.defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            verticalAlignment = when (rightVerticalAlign) {
                IenAgreementV4RightVerticalAlign.Center -> Alignment.CenterVertically
                IenAgreementV4RightVerticalAlign.Top -> Alignment.Top
            },
        ) {
            left()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = IenTheme.spacing.xxs),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
            ) {
                middle()
            }
            right?.invoke(this)
        }
    }

    if (variant == IenAgreementV4Variant.Box) {
        IenSurface(
            modifier = modifier.fillMaxWidth(),
            color = IenTheme.colors.surface,
            border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
            shape = shape,
        ) {
            content()
        }
    } else {
        content()
    }
}

@Composable
fun IenAgreementV4Checkbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IenCheckbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
fun IenAgreementV4Text(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    required: Boolean? = null,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (required != null) {
            IenBadge(
                text = if (required) "필수" else "선택",
                size = IenBadgeSize.Small,
                variant = IenBadgeVariant.Weak,
                tone = if (required) IenSemanticTone.Brand else IenSemanticTone.Neutral,
            )
        }
        IenText(
            text = title,
            modifier = Modifier.weight(1f),
            style = IenTheme.typography.body2,
            color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
    if (description != null) {
        IenText(
            text = description,
            style = IenTheme.typography.caption,
            color = if (enabled) IenTheme.colors.textTertiary else IenTheme.colors.textDisabled,
        )
    }
}

@Composable
fun IenAgreementV4RightButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IenTextButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        size = IenTextButtonSize.Small,
        variant = IenTextButtonVariant.Arrow,
        disabled = !enabled,
        tone = IenSemanticTone.Neutral,
    )
}

@Immutable
data class IenAgreementItemV4(
    val id: String,
    val title: String,
    val checked: Boolean,
    val required: Boolean = false,
    val description: String? = null,
    val enabled: Boolean = true,
    val indent: Boolean = false,
)

@Composable
fun IenAgreementV4(
    items: List<IenAgreementItemV4>,
    onItemCheckedChange: (id: String, checked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "약관 동의",
    onAllCheckedChange: ((Boolean) -> Unit)? = null,
    variant: IenAgreementV4Variant = IenAgreementV4Variant.Box,
    itemVariant: IenAgreementV4Variant = IenAgreementV4Variant.Plain,
    itemRight: (@Composable RowScope.(IenAgreementItemV4) -> Unit)? = null,
) {
    val allChecked = items.isNotEmpty() && items.all { it.checked }
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        border = if (variant == IenAgreementV4Variant.Box) BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border) else null,
        shape = RoundedCornerShape(IenTheme.radius.lg),
    ) {
        Column(Modifier.padding(IenTheme.spacing.md), verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm)) {
            IenAgreementV4(
                checked = allChecked,
                onCheckedChange = { checked ->
                    if (onAllCheckedChange != null) {
                        onAllCheckedChange(checked)
                    } else {
                        items.forEach { onItemCheckedChange(it.id, checked) }
                    }
                },
                variant = IenAgreementV4Variant.Plain,
                middle = {
                    IenText(
                        text = title,
                        style = IenTheme.typography.body1,
                        color = IenTheme.colors.textPrimary,
                    )
                },
            )
            IenDivider()
            items.forEach { item ->
                IenAgreementV4(
                    checked = item.checked,
                    onCheckedChange = { onItemCheckedChange(item.id, it) },
                    enabled = item.enabled,
                    variant = itemVariant,
                    indent = item.indent,
                    rightVerticalAlign = if (item.description != null) IenAgreementV4RightVerticalAlign.Top else IenAgreementV4RightVerticalAlign.Center,
                    middle = {
                        IenAgreementV4Text(
                            title = item.title,
                            description = item.description,
                            required = item.required,
                            enabled = item.enabled,
                        )
                    },
                    right = itemRight?.let { rightSlot ->
                        { rightSlot(item) }
                    },
                )
            }
        }
    }
}

enum class IenBottomCTABackground {
    Default,
    None,
}

enum class IenBottomCTAAnimation {
    Slide,
    Fade,
    Scale,
}

@Immutable
data class IenBottomCTAShowAfterDelay(
    val animation: IenBottomCTAAnimation = IenBottomCTAAnimation.Slide,
    val delayMillis: Int = 1_000,
)

@Composable
fun IenBottomCTA(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    background: IenBottomCTABackground = IenBottomCTABackground.Default,
    hasSafeAreaPadding: Boolean = true,
    hasPaddingBottom: Boolean = true,
    fixed: Boolean = false,
    fixedAboveKeyboard: Boolean = false,
    takeSpace: Boolean = fixed,
    show: Boolean = true,
    showAfterDelay: IenBottomCTAShowAfterDelay? = null,
    hideOnScroll: Boolean = false,
    hideOnScrollDistanceThreshold: Float = 1f,
    scrollDelta: Float = 0f,
    topAccessory: (@Composable () -> Unit)? = null,
    bottomAccessory: (@Composable () -> Unit)? = null,
) {
    IenBottomCTAContainer(
        modifier = modifier,
        background = background,
        hasSafeAreaPadding = hasSafeAreaPadding,
        hasPaddingBottom = hasPaddingBottom,
        fixed = fixed,
        fixedAboveKeyboard = fixedAboveKeyboard,
        takeSpace = takeSpace,
        show = show,
        showAfterDelay = showAfterDelay,
        hideOnScroll = hideOnScroll,
        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
        scrollDelta = scrollDelta,
        topAccessory = topAccessory,
        bottomAccessory = bottomAccessory,
    ) {
        IenButton(
            text = text,
            onClick = onClick,
            display = IenButtonDisplay.Block,
            state = IenButtonState(enabled = enabled),
            variant = variant,
        )
    }
}

@Composable
fun IenDoubleBottomCTA(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    secondaryText: String,
    onSecondaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryEnabled: Boolean = true,
    secondaryEnabled: Boolean = true,
    background: IenBottomCTABackground = IenBottomCTABackground.Default,
    hasSafeAreaPadding: Boolean = true,
    hasPaddingBottom: Boolean = true,
    fixed: Boolean = false,
    takeSpace: Boolean = fixed,
    show: Boolean = true,
    showAfterDelay: IenBottomCTAShowAfterDelay? = null,
    hideOnScroll: Boolean = false,
    hideOnScrollDistanceThreshold: Float = 1f,
    scrollDelta: Float = 0f,
    topAccessory: (@Composable () -> Unit)? = null,
    bottomAccessory: (@Composable () -> Unit)? = null,
) {
    IenDoubleBottomCTA(
        modifier = modifier,
        background = background,
        hasSafeAreaPadding = hasSafeAreaPadding,
        hasPaddingBottom = hasPaddingBottom,
        fixed = fixed,
        takeSpace = takeSpace,
        show = show,
        showAfterDelay = showAfterDelay,
        hideOnScroll = hideOnScroll,
        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
        scrollDelta = scrollDelta,
        topAccessory = topAccessory,
        bottomAccessory = bottomAccessory,
        leftButton = {
            IenBottomCTAButton(
                text = secondaryText,
                onClick = onSecondaryClick,
                variant = IenButtonVariant.Weak,
                tone = IenSemanticTone.Neutral,
                enabled = secondaryEnabled,
            )
        },
        rightButton = {
            IenBottomCTAButton(
                text = primaryText,
                onClick = onPrimaryClick,
                enabled = primaryEnabled,
            )
        },
    )
}

@Composable
fun IenDoubleBottomCTA(
    leftButton: @Composable RowScope.() -> Unit,
    rightButton: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    background: IenBottomCTABackground = IenBottomCTABackground.Default,
    hasSafeAreaPadding: Boolean = true,
    hasPaddingBottom: Boolean = true,
    fixed: Boolean = false,
    takeSpace: Boolean = fixed,
    show: Boolean = true,
    showAfterDelay: IenBottomCTAShowAfterDelay? = null,
    hideOnScroll: Boolean = false,
    hideOnScrollDistanceThreshold: Float = 1f,
    scrollDelta: Float = 0f,
    topAccessory: (@Composable () -> Unit)? = null,
    bottomAccessory: (@Composable () -> Unit)? = null,
) {
    IenBottomCTAContainer(
        modifier = modifier,
        background = background,
        hasSafeAreaPadding = hasSafeAreaPadding,
        hasPaddingBottom = hasPaddingBottom,
        fixed = fixed,
        fixedAboveKeyboard = false,
        takeSpace = takeSpace,
        show = show,
        showAfterDelay = showAfterDelay,
        hideOnScroll = hideOnScroll,
        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
        scrollDelta = scrollDelta,
        topAccessory = topAccessory,
        bottomAccessory = bottomAccessory,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leftButton()
            rightButton()
        }
    }
}

@Composable
fun RowScope.IenBottomCTAButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier.weight(1f),
        size = IenButtonSize.Large,
        display = IenButtonDisplay.Block,
        variant = variant,
        tone = tone,
        state = IenButtonState(enabled = enabled),
    )
}

@Composable
fun BoxScope.IenFixedBottomCTA(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues? = null,
    enabled: Boolean = true,
    background: IenBottomCTABackground = IenBottomCTABackground.Default,
    hasSafeAreaPadding: Boolean = true,
    hasPaddingBottom: Boolean = true,
    fixedAboveKeyboard: Boolean = false,
    show: Boolean = true,
    showAfterDelay: IenBottomCTAShowAfterDelay? = null,
    hideOnScroll: Boolean = false,
    hideOnScrollDistanceThreshold: Float = 1f,
    scrollDelta: Float = 0f,
    topAccessory: (@Composable () -> Unit)? = null,
    bottomAccessory: (@Composable () -> Unit)? = null,
) {
    IenBottomCTA(
        text = text,
        onClick = onClick,
        modifier = modifier.align(Alignment.BottomCenter),
        enabled = enabled,
        background = background,
        hasSafeAreaPadding = hasSafeAreaPadding,
        hasPaddingBottom = hasPaddingBottom,
        fixed = true,
        fixedAboveKeyboard = fixedAboveKeyboard,
        takeSpace = false,
        show = show,
        showAfterDelay = showAfterDelay,
        hideOnScroll = hideOnScroll,
        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
        scrollDelta = scrollDelta,
        topAccessory = topAccessory,
        bottomAccessory = bottomAccessory,
    )
}

@Composable
fun BoxScope.IenFixedDoubleBottomCTA(
    leftButton: @Composable RowScope.() -> Unit,
    rightButton: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    background: IenBottomCTABackground = IenBottomCTABackground.Default,
    hasSafeAreaPadding: Boolean = true,
    hasPaddingBottom: Boolean = true,
    show: Boolean = true,
    showAfterDelay: IenBottomCTAShowAfterDelay? = null,
    hideOnScroll: Boolean = false,
    hideOnScrollDistanceThreshold: Float = 1f,
    scrollDelta: Float = 0f,
    topAccessory: (@Composable () -> Unit)? = null,
    bottomAccessory: (@Composable () -> Unit)? = null,
) {
    IenDoubleBottomCTA(
        leftButton = leftButton,
        rightButton = rightButton,
        modifier = modifier.align(Alignment.BottomCenter),
        background = background,
        hasSafeAreaPadding = hasSafeAreaPadding,
        hasPaddingBottom = hasPaddingBottom,
        fixed = true,
        takeSpace = false,
        show = show,
        showAfterDelay = showAfterDelay,
        hideOnScroll = hideOnScroll,
        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
        scrollDelta = scrollDelta,
        topAccessory = topAccessory,
        bottomAccessory = bottomAccessory,
    )
}

@Composable
private fun IenBottomCTAContainer(
    modifier: Modifier,
    background: IenBottomCTABackground,
    hasSafeAreaPadding: Boolean,
    hasPaddingBottom: Boolean,
    fixed: Boolean,
    fixedAboveKeyboard: Boolean,
    takeSpace: Boolean,
    show: Boolean,
    showAfterDelay: IenBottomCTAShowAfterDelay?,
    hideOnScroll: Boolean,
    hideOnScrollDistanceThreshold: Float,
    scrollDelta: Float,
    topAccessory: (@Composable () -> Unit)?,
    bottomAccessory: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
) {
    var delayedVisible by remember(showAfterDelay) { mutableStateOf(showAfterDelay == null) }
    LaunchedEffect(show, showAfterDelay) {
        if (show && showAfterDelay != null) {
            delayedVisible = false
            delay(showAfterDelay.delayMillis.toLong())
            delayedVisible = true
        } else {
            delayedVisible = show
        }
    }
    val visible = show && delayedVisible && !(hideOnScroll && scrollDelta > hideOnScrollDistanceThreshold)
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(IenTheme.motion.normalMillis, easing = IenTheme.motion.standardEasing),
    )
    val scale by animateFloatAsState(
        targetValue = if (visible || showAfterDelay?.animation != IenBottomCTAAnimation.Scale) 1f else 0.96f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
    )
    val translation by animateFloatAsState(
        targetValue = if (visible || showAfterDelay?.animation != IenBottomCTAAnimation.Slide) 0f else 18f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
    )
    val navigationBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val keyboardBottom = if (fixedAboveKeyboard) WindowInsets.ime.asPaddingValues().calculateBottomPadding() else 0.dp
    val defaultBottom = 20.dp
    val safeBottom = when {
        !hasPaddingBottom -> 0.dp
        hasSafeAreaPadding -> if (navigationBottom > defaultBottom) navigationBottom else defaultBottom
        else -> defaultBottom
    }
    val bottomPadding = if (keyboardBottom > safeBottom) keyboardBottom else safeBottom
    val containerColor = when (background) {
        IenBottomCTABackground.Default -> IenTheme.colors.surface
        IenBottomCTABackground.None -> Color.Transparent
    }
    val shouldCompose = visible || takeSpace

    if (shouldCompose) {
        IenSurface(
            modifier = modifier
                .fillMaxWidth()
                .graphicsLayer {
                    this.alpha = alpha
                    scaleX = scale
                    scaleY = scale
                    translationY = translation
                },
            color = containerColor,
            tonalElevation = if (fixed) IenTheme.elevation.floating else 0.dp,
        ) {
            Column(
                modifier = Modifier.padding(
                    start = IenTheme.spacing.md,
                    top = IenTheme.spacing.md,
                    end = IenTheme.spacing.md,
                    bottom = bottomPadding,
                ),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            ) {
                topAccessory?.invoke()
                content()
                bottomAccessory?.invoke()
            }
        }
    }
}
