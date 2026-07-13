package zone.ien.utils.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.input.pointer.pointerInput
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalRippleConfiguration
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenBadge
import zone.ien.utils.ui.interactive.IenBadgeSize
import zone.ien.utils.ui.interactive.IenBadgeVariant
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.LocalIenButtonPressedReporter
import zone.ien.utils.ui.interactive.LocalIenButtonScalePressedOverride
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.interactive.IenTextButtonSize
import zone.ien.utils.ui.interactive.IenTextButtonVariant
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import com.kyant.backdrop.drawPlainBackdrop
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.runtimeShaderEffect
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.agreement_optional
import zone.ien.utils.cmp_ui.generated.resources.agreement_required
import zone.ien.utils.cmp_ui.generated.resources.agreement_terms
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.line.ArrowDownWide
import zone.ien.utils.ui.interactive.IenCircleCheckbox
import zone.ien.utils.ui.interactive.IenDotCheckbox
import zone.ien.utils.utils.ui.animateContentSizeWithoutClipping

internal val LocalIenTopBarFloatingSlotHiddenRequester = staticCompositionLocalOf<((Boolean) -> Unit)?> { null }

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

@Immutable
data class IenScaffoldContentEdge(
    val enabled: Boolean = true,
    val topEnabled: Boolean = true,
    val bottomEnabled: Boolean = true,
    val topProgress: Float = 1f,
    val bottomProgress: Float = 1f,
    val topHeight: Dp = 220.dp,
    val bottomHeight: Dp = 132.dp,
    val radius: Dp = 22.dp,
    val color: Color? = null,
)

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
    contentEdge: IenScaffoldContentEdge = IenScaffoldContentEdge(),
    content: @Composable (PaddingValues) -> Unit,
) {
    val contentEdgeColor = contentEdge.color ?: containerColor
    val backdrop = rememberLayerBackdrop {
        drawRect(contentEdgeColor)
        drawContent()
    }

    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = { bottomBar?.invoke() },
        snackbarHost = snackbarHost,
        floatingActionButton = floating,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .layerBackdrop(backdrop),
            ) {
                content(contentPadding)
            }
            if (contentEdge.enabled) {
                IenScaffoldEdgeBlur(
                    modifier = Modifier.matchParentSize(),
                    backdrop = backdrop,
                    showTop = contentEdge.topEnabled,
                    showBottom = contentEdge.bottomEnabled,
                    topProgress = contentEdge.topProgress.coerceIn(0f, 1f),
                    bottomProgress = contentEdge.bottomProgress.coerceIn(0f, 1f),
                    topHeight = contentEdge.topHeight,
                    bottomHeight = contentEdge.bottomHeight,
                    radius = contentEdge.radius,
                    color = contentEdgeColor,
                )
            }
        }
    }
}

@Composable
private fun IenScaffoldEdgeBlur(
    modifier: Modifier = Modifier,
    backdrop: LayerBackdrop,
    showTop: Boolean,
    showBottom: Boolean,
    topProgress: Float,
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

@Composable
fun IenTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    titleAlignment: IenTopBarTitleAlignment = IenTopBarTitleAlignment.Start,
    showDivider: Boolean = false,
    windowInsets: WindowInsets = WindowInsets.statusBars,
    contentPadding: PaddingValues = PaddingValues(horizontal = IenTheme.spacing.md, vertical = 6.dp),
    contentHeight: Dp = 64.dp,
    containerColor: Color = Color.Transparent,
    floatingSlots: Boolean = true,
) {
    IenTopBar(
        title = {
            IenText(
                text = title,
                style = IenTheme.typography.title3,
                textAlign = if (titleAlignment == IenTopBarTitleAlignment.Center) TextAlign.Center else null,
            )
        },
        modifier = modifier,
        subtitle = subtitle?.let {
            {
                IenText(
                    text = it,
                    style = IenTheme.typography.caption,
                    color = IenTheme.colors.textSecondary,
                    textAlign = if (titleAlignment == IenTopBarTitleAlignment.Center) TextAlign.Center else null,
                )
            }
        },
        navigationIcon = navigationIcon,
        actions = actions,
        titleAlignment = titleAlignment,
        showDivider = showDivider,
        windowInsets = windowInsets,
        contentPadding = contentPadding,
        contentHeight = contentHeight,
        containerColor = containerColor,
        floatingSlots = floatingSlots,
    )
}

@Composable
fun IenTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    titleAlignment: IenTopBarTitleAlignment = IenTopBarTitleAlignment.Start,
    showDivider: Boolean = false,
    windowInsets: WindowInsets = WindowInsets.statusBars,
    contentPadding: PaddingValues = PaddingValues(horizontal = IenTheme.spacing.md, vertical = 6.dp),
    contentHeight: Dp = 64.dp,
    containerColor: Color = Color.Transparent,
    floatingSlots: Boolean = true,
) {
    val insetPadding = windowInsets.asPaddingValues()
    val topPadding = insetPadding.calculateTopPadding()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(topPadding + contentHeight)
            .background(containerColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(topPadding + contentHeight)
                .padding(top = topPadding)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (navigationIcon != null) {
                IenTopBarFloatingSlot(enabled = floatingSlots) {
                    navigationIcon()
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = if (titleAlignment == IenTopBarTitleAlignment.Center) Alignment.CenterHorizontally else Alignment.Start,
            ) {
                IenProvideTextStyle(IenTheme.typography.title3, IenTheme.colors.textPrimary) {
                    title()
                }
                if (subtitle != null) {
                    IenProvideTextStyle(IenTheme.typography.caption, IenTheme.colors.textSecondary) {
                        subtitle()
                    }
                }
            }
            if (actions != null) {
                IenTopBarFloatingSlot(enabled = floatingSlots) {
                    Row(
                        modifier = Modifier
                            .clipToBounds()
                            .animateContentSize(animationSpec = tween(durationMillis = 110, easing = IenTheme.motion.standardEasing)),
                        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CompositionLocalProvider(LocalContentColor provides IenTheme.colors.textPrimary) {
                            actions.invoke(this)
                        }
                    }
                }
            }
        }
        if (showDivider) {
            IenDivider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IenTopBarFloatingSlot(
    enabled: Boolean,
    content: @Composable () -> Unit,
) {
    if (!enabled) {
        content()
        return
    }

    var pressedTokens by remember { mutableStateOf(setOf<Any>()) }
    var hiddenRequested by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressedTokens.isNotEmpty()) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
    )
    val pressedOverlayAlpha by animateFloatAsState(
        targetValue = if (pressedTokens.isNotEmpty()) 0.08f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
    )
    val visibleScale by animateFloatAsState(
        targetValue = if (hiddenRequested) 0.001f else 1f,
        animationSpec = spring(1.2f),
    )
    val pressedReporter: (Any, Boolean) -> Unit = { token, pressed ->
        pressedTokens = if (pressed) {
            pressedTokens + token
        } else {
            pressedTokens - token
        }
    }

    Box(
        modifier = Modifier.graphicsLayer {
            clip = false
        },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = scale * visibleScale
                    scaleY = scale * visibleScale
                    clip = false
                }
                .shadow(elevation = IenTheme.elevation.floating, shape = CircleShape, clip = false)
                .background(IenTheme.colors.surface.copy(alpha = 0.92f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (pressedOverlayAlpha > 0f) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(IenTheme.colors.textPrimary.copy(alpha = pressedOverlayAlpha)),
                )
            }
        }
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .padding(horizontal = IenTheme.spacing.xxs, vertical = IenTheme.spacing.xxs),
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(
                LocalIenButtonPressedReporter provides pressedReporter,
                LocalIenButtonScalePressedOverride provides 1f,
                LocalIenTopBarFloatingSlotHiddenRequester provides { hiddenRequested = it },
                LocalIenNavigationButtonVariant provides IenButtonVariant.Ghost,
                LocalRippleConfiguration provides null,
            ) {
                content()
            }
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

class IenTooltipPositionProvider(
    private val placement: IenTooltipPlacement,
    private val offset: Dp,
    private val density: Density,
    private val onArrowRatioCalculated: (Float) -> Unit
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val preferredX = anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2
        val offsetPx = with(density) { offset.toPx().toInt() }
        val paddingPx = with(density) { 32.dp.toPx() }
        val safeMarginPx = with(density) { 16.dp.toPx() }

        val minX = (-paddingPx + safeMarginPx).toInt()
        val maxX = (windowSize.width - popupContentSize.width + paddingPx - safeMarginPx).toInt()
        val x = preferredX.coerceIn(minX, maxX)

        val y = when (placement) {
            IenTooltipPlacement.Top -> {
                anchorBounds.top - popupContentSize.height + paddingPx.toInt() - offsetPx
            }
            IenTooltipPlacement.Bottom -> {
                anchorBounds.bottom - paddingPx.toInt() + offsetPx
            }
        }

        val anchorCenterX = anchorBounds.left + anchorBounds.width / 2f
        val relativeAnchorCenterX = anchorCenterX - x
        val bodyWidth = popupContentSize.width - 2 * paddingPx
        val arrowOffsetInBody = relativeAnchorCenterX - paddingPx

        val arrowRatio = if (bodyWidth > 0) {
            (arrowOffsetInBody / bodyWidth).coerceIn(0.12f, 0.88f)
        } else {
            0.5f
        }

        onArrowRatioCalculated(arrowRatio)

        return IntOffset(x, y)
    }
}

class IenTooltipShape(
    private val placement: IenTooltipPlacement,
    private val arrowRatio: Float,
    private val arrowSize: Dp,
    private val cornerRadius: Dp,
    private val density: Density
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val arrowSizePx = with(this.density) { arrowSize.toPx() }
        val radiusPx = with(this.density) { cornerRadius.toPx() }
        val w = size.width
        val h = size.height

        val path = Path().apply {
            val bodyTop = if (placement == IenTooltipPlacement.Bottom) arrowSizePx / 2f else 0f
            val bodyBottom = if (placement == IenTooltipPlacement.Top) h - arrowSizePx / 2f else h

            val rect = Rect(0f, bodyTop, w, bodyBottom)
            addRoundRect(RoundRect(rect, CornerRadius(radiusPx)))

            val arrowWidth = arrowSizePx
            val arrowHeight = arrowSizePx / 2f
            val arrowX = w * arrowRatio.coerceIn(0.1f, 0.9f) - (arrowWidth / 2f)

            when (placement) {
                IenTooltipPlacement.Bottom -> {
                    moveTo(arrowX, bodyTop)
                    lineTo(arrowX + arrowWidth / 2f, bodyTop - arrowHeight)
                    lineTo(arrowX + arrowWidth, bodyTop)
                }
                IenTooltipPlacement.Top -> {
                    moveTo(arrowX, bodyBottom)
                    lineTo(arrowX + arrowWidth / 2f, bodyBottom + arrowHeight)
                    lineTo(arrowX + arrowWidth, bodyBottom)
                }
            }
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun IenTooltip(
    text: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
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
    anchor: (@Composable BoxScope.(toggle: () -> Unit) -> Unit)? = null,
) {
    var internalOpen by remember { mutableStateOf(defaultOpen) }
    val isOpen = open ?: internalOpen
    val resolvedPlacement = if (autoFlip && placement == IenTooltipPlacement.Top) {
        IenTooltipPlacement.Bottom
    } else {
        placement
    }
    val resolvedOffset = offset ?: 8.dp
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

    val toggle = { updateOpen(!isOpen) }

    var keepInComposition by remember { mutableStateOf(isOpen) }
    LaunchedEffect(isOpen) {
        if (isOpen) {
            keepInComposition = true
        } else if (keepInComposition) {
            delay(150L)
            keepInComposition = false
        }
    }

    var dynamicArrowRatio by remember { mutableStateOf(anchorPositionByRatio) }
    LaunchedEffect(anchorPositionByRatio) {
        dynamicArrowRatio = anchorPositionByRatio
    }

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .pointerInput(isOpen) {
                detectTapGestures(
                    onTap = {
                        toggle()
                    },
                    onLongPress = {
                        updateOpen(true)
                    }
                )
            }
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
        anchor?.invoke(this, toggle)
        if (anchor == null) {
            IenText("?", style = IenTheme.typography.label1, color = IenTheme.colors.brand)
        }

        if (keepInComposition) {
            Popup(
                popupPositionProvider = remember(resolvedPlacement, resolvedOffset) {
                    IenTooltipPositionProvider(resolvedPlacement, resolvedOffset, density) { ratio ->
                        dynamicArrowRatio = ratio
                    }
                },
                onDismissRequest = { updateOpen(false) },
                properties = PopupProperties(
                    focusable = false,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true,
                    clippingEnabled = false
                )
            ) {
                IenTooltipPopup(
                    visible = isOpen,
                    text = text,
                    tone = tone,
                    messageAlign = messageAlign,
                    anchorPositionByRatio = dynamicArrowRatio,
                    clipToEnd = clipToEnd,
                    placement = resolvedPlacement,
                    motionVariant = motionVariant,
                    scale = motionScale,
                    width = width,
                )
            }
        }
    }
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
    messageAlign: IenTooltipMessageAlign,
    anchorPositionByRatio: Float,
    clipToEnd: IenTooltipClipToEnd,
    placement: IenTooltipPlacement,
    motionVariant: IenTooltipMotionVariant,
    scale: Float,
    width: Dp?,
) {
    val isNeutral = tone == IenSemanticTone.Neutral
    val backgroundColor = if (isNeutral) {
        Color.White
    } else {
        zone.ien.utils.ui.interactive.toneColor(tone)
    }
    val contentColor = if (isNeutral) {
        IenTheme.colors.textPrimary
    } else {
        Color.White
    }
    val arrowRatio = anchorPositionByRatio.coerceIn(0.05f, 0.95f)
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

    val density = LocalDensity.current
    val tooltipShape = remember(placement, arrowRatio, density) {
        IenTooltipShape(
            placement = placement,
            arrowRatio = arrowRatio,
            arrowSize = 12.dp,
            cornerRadius = 12.dp,
            density = density
        )
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(IenTheme.motion.fastMillis)),
        exit = fadeOut(tween(IenTheme.motion.fastMillis)),
    ) {
        Box(
            modifier = Modifier
                .padding(32.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                    translationY = when (placement) {
                        IenTooltipPlacement.Top -> if (visible) 0f else 4f
                        IenTooltipPlacement.Bottom -> if (visible) 0f else -4f
                    }
                }
                .then(if (width != null) Modifier.width(width) else Modifier.widthIn(max = 280.dp)),
            contentAlignment = Alignment.Center
        ) {
            IenSurface(
                modifier = Modifier.shadow(
                    elevation = 16.dp,
                    shape = tooltipShape,
                    clip = false,
                    ambientColor = Color(0x80001D3A),
                    spotColor = Color(0x80001D3A)
                ),
                color = backgroundColor,
                contentColor = contentColor,
                shape = tooltipShape,
            ) {
                val topPadding = if (placement == IenTooltipPlacement.Bottom) 16.dp else 10.dp
                val bottomPadding = if (placement == IenTooltipPlacement.Top) 16.dp else 10.dp

                IenText(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = topPadding,
                            bottom = bottomPadding
                        ),
                    style = IenTheme.typography.label2.copy(fontWeight = FontWeight.Bold),
                    color = contentColor,
                    textAlign = when (messageAlign) {
                        IenTooltipMessageAlign.Left -> TextAlign.Start
                        IenTooltipMessageAlign.Center -> TextAlign.Center
                        IenTooltipMessageAlign.Right -> TextAlign.End
                    },
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



private fun IenTooltipMotionVariant.hiddenScale(): Float = when (this) {
    IenTooltipMotionVariant.Weak -> 0.98f
    IenTooltipMotionVariant.Strong -> 0.92f
}

private fun IenTooltipMotionVariant.stiffness(): Float = when (this) {
    IenTooltipMotionVariant.Weak -> Spring.StiffnessMediumLow
    IenTooltipMotionVariant.Strong -> Spring.StiffnessMedium
}

enum class IenAgreementVariant {
    XLarge,
    Large,
    Medium,
    MediumTitle,
    Small,
    SmallLast
}

enum class IenAgreementCheckboxVariant {
    Checkbox,
    Dot,
    Hidden
}

enum class IenAgreementCheckboxMotionVariant {
    Strong,
    Weak
}

enum class IenAgreementNecessityVariant {
    Optional,
    Mandatory
}

enum class IenAgreementDescriptionVariant {
    Box,
    Normal
}

enum class IenAgreementHeaderVariant {
    XLarge,
    Large,
    Medium,
    MediumTitle,
    Small,
    SmallLast,
}

val LocalIenAgreementIndent = staticCompositionLocalOf { 0 }
val LocalIenAgreementVariant = staticCompositionLocalOf { IenAgreementVariant.Large }
val LocalIenAgreementCollapsibleState = staticCompositionLocalOf { false }
val LocalIenAgreementCollapsibleToggle = staticCompositionLocalOf<() -> Unit> { {} }
val LocalIenAgreementPushableToggle = staticCompositionLocalOf<() -> Unit> { {} }

@Composable
fun IenAgreement(
    modifier: Modifier = Modifier,
    variant: IenAgreementVariant = IenAgreementVariant.Large,
    indent: Int = 0,
    onClick: (() -> Unit)? = null,
    onPressEnd: (() -> Unit)? = onClick,
    left: (@Composable RowScope.() -> Unit)? = null,
    middle: (@Composable RowScope.() -> Unit)? = null,
    right: (@Composable RowScope.() -> Unit)? = null,
) {
    val currentIndent = LocalIenAgreementIndent.current + indent
    val minHeight = variant.agreementMinHeight()
    val verticalPadding = variant.agreementVerticalPadding()
    val startPadding = IenTheme.spacing.md + currentIndent.agreementIndentPadding()
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pressAlpha by animateFloatAsState(
        targetValue = if (pressed && onPressEnd != null) 0.72f else 1f,
        animationSpec = tween(durationMillis = 120, easing = IenTheme.motion.standardEasing),
        label = "ienAgreementPressAlpha",
    )

    CompositionLocalProvider(LocalIenAgreementVariant provides variant) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight)
                .then(
                    if (onPressEnd != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.Button,
                            onClick = onPressEnd,
                        )
                    } else {
                        Modifier
                    }
                )
                .graphicsLayer { alpha = pressAlpha }
                .padding(start = startPadding, end = IenTheme.spacing.md)
                .padding(vertical = verticalPadding),
            horizontalArrangement = Arrangement.spacedBy(variant.agreementSlotGap()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (left != null) {
                Row(
                    modifier = Modifier.width(24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    left.invoke(this)
                }
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                middle?.invoke(this)
            }
            if (right != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    right.invoke(this)
                }
            }
        }
    }
}

@Composable
fun IenAgreementCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    variant: IenAgreementCheckboxVariant = IenAgreementCheckboxVariant.Checkbox,
    motionVariant: IenAgreementCheckboxMotionVariant = IenAgreementCheckboxMotionVariant.Weak,
    transitionDelay: Float = 0f,
    enabled: Boolean = true,
) {
    val delayMillis = ((transitionDelay + 0.1f) * 1000).toInt().coerceAtLeast(0)
    val scale by animateFloatAsState(
        targetValue = if (checked) 1f else 0.86f,
        animationSpec = when (motionVariant) {
            IenAgreementCheckboxMotionVariant.Strong -> spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium,
            )
            IenAgreementCheckboxMotionVariant.Weak -> tween(
                durationMillis = 180,
                delayMillis = delayMillis,
                easing = IenTheme.motion.standardEasing,
            )
        },
        label = "ienAgreementCheckboxScale",
    )

    when (variant) {
        IenAgreementCheckboxVariant.Checkbox -> {
            IenCircleCheckbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                modifier = modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            )
        }
        IenAgreementCheckboxVariant.Dot -> {
            IenDotCheckbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                modifier = modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            )
        }
        IenAgreementCheckboxVariant.Hidden -> {
            Spacer(modifier = modifier.size(24.dp))
        }
    }
}

@Composable
fun IenAgreementText(
    text: String,
    modifier: Modifier = Modifier,
    necessity: (@Composable () -> Unit)? = null,
    onPressEnd: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val alpha by animateFloatAsState(
        targetValue = if (pressed && onPressEnd != null) 0.64f else 1f,
        animationSpec = tween(durationMillis = 120, easing = IenTheme.motion.standardEasing),
        label = "ienAgreementTextPressAlpha",
    )
    Row(
        modifier = modifier
            .then(
                if (onPressEnd != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onPressEnd,
                    )
                } else Modifier
            )
            .graphicsLayer { this.alpha = alpha },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        necessity?.invoke()
        IenText(
            text = text,
            modifier = Modifier.weight(1f, fill = false),
            style = LocalIenAgreementVariant.current.agreementTextStyle(),
            color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            fontWeight = if (LocalIenAgreementVariant.current == IenAgreementVariant.MediumTitle) FontWeight.Bold else null,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun IenAgreementNecessity(
    variant: IenAgreementNecessityVariant,
    modifier: Modifier = Modifier,
    text: String = if (variant == IenAgreementNecessityVariant.Mandatory) stringResource(Res.string.agreement_required) else stringResource(Res.string.agreement_optional)
) {
    val isMandatory = variant == IenAgreementNecessityVariant.Mandatory
    IenText(
        text = text,
        modifier = modifier,
        style = IenTheme.typography.caption,
        color = if (isMandatory) IenTheme.colors.brand else IenTheme.colors.textSecondary,
        fontWeight = FontWeight.Bold,
    )
}

enum class IenAgreementBadgeVariant {
    Fill,
    Clear
}

@Composable
fun IenAgreementBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: IenAgreementBadgeVariant = IenAgreementBadgeVariant.Clear,
    bgColor: Color? = null,
    textColor: Color? = null,
) {
    val resolvedBg = if (variant == IenAgreementBadgeVariant.Fill) (bgColor ?: IenTheme.colors.brandWeak) else Color.Transparent
    val resolvedText = textColor ?: IenTheme.colors.brand
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(IenTheme.radius.xs))
            .background(resolvedBg)
            .padding(horizontal = 6.dp, vertical = 3.dp),
    ) {
        IenText(text = text, style = IenTheme.typography.caption, color = resolvedText)
    }
}

@Composable
fun IenAgreementRightArrow(
    modifier: Modifier = Modifier,
    collapsed: Boolean? = null,
    onArrowClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = onArrowClick,
) {
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    val resolvedCollapsed = collapsed ?: LocalIenAgreementCollapsibleState.current
    val rotation by animateFloatAsState(
        targetValue = if (resolvedCollapsed) 180f else 0f,
        animationSpec = tween(durationMillis = IenTheme.motion.fastMillis, easing = IenTheme.motion.standardEasing),
        label = "ienAgreementRightArrowRotation",
    )
    Icon(
        imageVector = RemixIcons.Line.ArrowDownWide,
        contentDescription = null,
        tint = IenTheme.colors.textDisabled,
        modifier = modifier
            .rotate(rotation)
            .then(clickableModifier)
    )
}

@Composable
fun IenAgreementDescription(
    text: String,
    modifier: Modifier = Modifier,
    variant: IenAgreementDescriptionVariant = IenAgreementDescriptionVariant.Normal,
    indent: Int = 0,
) {
    val startPadding = (LocalIenAgreementIndent.current + indent).agreementIndentPadding() + IenTheme.spacing.md + 32.dp
    if (variant == IenAgreementDescriptionVariant.Box) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = startPadding, end = IenTheme.spacing.md, top = IenTheme.spacing.xs, bottom = IenTheme.spacing.xs)
                .clip(RoundedCornerShape(IenTheme.radius.default))
                .background(IenTheme.colors.surfaceWeak)
                .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
        ) {
            IenText(
                text = text,
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textSecondary
            )
        }
    } else {
        IenText(
            text = text,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = startPadding, end = IenTheme.spacing.md, top = IenTheme.spacing.xxs),
            style = IenTheme.typography.caption,
            color = IenTheme.colors.textTertiary,
        )
    }
}

@Composable
fun IenAgreementHeader(
    text: String,
    modifier: Modifier = Modifier,
    variant: IenAgreementHeaderVariant = IenAgreementHeaderVariant.Medium,
    indent: Int = 0,
) {
    val currentIndent = LocalIenAgreementIndent.current + indent
    val agreementVariant = variant.toAgreementVariant()
    IenText(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = currentIndent.agreementIndentPadding() + IenTheme.spacing.md,
                top = agreementVariant.agreementVerticalPadding(),
                end = IenTheme.spacing.md,
                bottom = IenTheme.spacing.xs,
            ),
        style = agreementVariant.agreementTextStyle(),
        color = IenTheme.colors.textPrimary,
        fontWeight = if (agreementVariant == IenAgreementVariant.XLarge || agreementVariant == IenAgreementVariant.MediumTitle) FontWeight.Bold else null,
    )
}

private fun IenAgreementHeaderVariant.toAgreementVariant(): IenAgreementVariant = when (this) {
    IenAgreementHeaderVariant.XLarge -> IenAgreementVariant.XLarge
    IenAgreementHeaderVariant.Large -> IenAgreementVariant.Large
    IenAgreementHeaderVariant.Medium -> IenAgreementVariant.Medium
    IenAgreementHeaderVariant.MediumTitle -> IenAgreementVariant.MediumTitle
    IenAgreementHeaderVariant.Small -> IenAgreementVariant.Small
    IenAgreementHeaderVariant.SmallLast -> IenAgreementVariant.SmallLast
}

@Composable
fun IenAgreementPressable(
    onPressEnd: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val alpha by animateFloatAsState(
        targetValue = if (pressed) 0.72f else 1f,
        animationSpec = tween(durationMillis = 120, easing = IenTheme.motion.standardEasing),
        label = "ienAgreementPressableAlpha",
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onPressEnd,
            )
            .graphicsLayer { this.alpha = alpha },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun IenAgreementGroup(
    modifier: Modifier = Modifier,
    showGradient: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val lineStart = IenTheme.spacing.md + 12.dp
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (showGradient) {
                    Modifier.drawBehind {
                        val x = lineStart.toPx()
                        drawLine(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFFE5E8EB),
                                    Color(0xFFE5E8EB),
                                    Color.Transparent,
                                ),
                            ),
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round,
                        )
                    }
                } else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        content()
    }
}

@Composable
fun IenAgreementCollapsible(
    collapsed: Boolean? = null,
    modifier: Modifier = Modifier,
    defaultCollapsed: Boolean = false,
    onCollapsedChange: ((Boolean) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    var localCollapsed by remember { mutableStateOf(defaultCollapsed) }
    val resolvedCollapsed = collapsed ?: localCollapsed
    val changeCollapsed: (Boolean) -> Unit = { next ->
        if (collapsed == null) {
            localCollapsed = next
        }
        onCollapsedChange?.invoke(next)
    }
    CompositionLocalProvider(
        LocalIenAgreementCollapsibleState provides resolvedCollapsed,
        LocalIenAgreementCollapsibleToggle provides { changeCollapsed(!resolvedCollapsed) }
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            content()
        }
    }
}

@Composable
fun IenAgreementCollapsibleTrigger(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val toggle = LocalIenAgreementCollapsibleToggle.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = toggle)
    ) {
        content()
    }
}

@Composable
fun IenAgreementCollapsibleContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val collapsed = LocalIenAgreementCollapsibleState.current
    AnimatedVisibility(
        visible = !collapsed,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            content()
        }
    }
}

@Composable
fun IenAgreementIndentPushable(
    pushed: Boolean? = null,
    modifier: Modifier = Modifier,
    defaultPushed: Boolean = false,
    onPushedChange: ((Boolean) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    var localPushed by remember { mutableStateOf(defaultPushed) }
    val resolvedPushed = pushed ?: localPushed
    val currentIndent = LocalIenAgreementIndent.current
    val nextIndent = if (resolvedPushed) currentIndent + 1 else currentIndent
    val changePushed: (Boolean) -> Unit = { next ->
        if (pushed == null) {
            localPushed = next
        }
        onPushedChange?.invoke(next)
    }
    CompositionLocalProvider(
        LocalIenAgreementIndent provides nextIndent,
        LocalIenAgreementPushableToggle provides { changePushed(!resolvedPushed) },
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            content()
        }
    }
}

@Composable
fun IenAgreementIndentPushableTrigger(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val toggle = LocalIenAgreementPushableToggle.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = toggle,
            )
    ) {
        content()
    }
}

@Composable
fun IenAgreementIndentPushableContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        content()
    }
}

@Composable
private fun IenAgreementVariant.agreementTextStyle(): TextStyle = when (this) {
    IenAgreementVariant.XLarge -> IenTheme.typography.title3
    IenAgreementVariant.Large -> IenTheme.typography.body1
    IenAgreementVariant.Medium -> IenTheme.typography.body2
    IenAgreementVariant.MediumTitle -> IenTheme.typography.body1
    IenAgreementVariant.Small,
    IenAgreementVariant.SmallLast -> IenTheme.typography.body2
}

private fun IenAgreementVariant.agreementMinHeight(): Dp = when (this) {
    IenAgreementVariant.XLarge -> 64.dp
    IenAgreementVariant.Large -> 56.dp
    IenAgreementVariant.Medium,
    IenAgreementVariant.MediumTitle -> 48.dp
    IenAgreementVariant.Small -> 40.dp
    IenAgreementVariant.SmallLast -> 36.dp
}

private fun IenAgreementVariant.agreementVerticalPadding(): Dp = when (this) {
    IenAgreementVariant.XLarge -> 14.dp
    IenAgreementVariant.Large -> 10.dp
    IenAgreementVariant.Medium,
    IenAgreementVariant.MediumTitle -> 8.dp
    IenAgreementVariant.Small -> 6.dp
    IenAgreementVariant.SmallLast -> 4.dp
}

private fun IenAgreementVariant.agreementSlotGap(): Dp = when (this) {
    IenAgreementVariant.XLarge,
    IenAgreementVariant.Large -> 10.dp
    IenAgreementVariant.Medium,
    IenAgreementVariant.MediumTitle -> 8.dp
    IenAgreementVariant.Small,
    IenAgreementVariant.SmallLast -> 8.dp
}

private fun Int.agreementIndentPadding(): Dp = (coerceAtLeast(0) * 24).dp

@Immutable
data class IenAgreementItem(
    val id: String,
    val title: String,
    val checked: Boolean,
    val required: Boolean = false,
    val description: String? = null,
    val enabled: Boolean = true,
    val indent: Boolean = false,
)

@Composable
fun IenAgreement(
    items: List<IenAgreementItem>,
    onItemCheckedChange: (id: String, checked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.agreement_terms),
    onAllCheckedChange: ((Boolean) -> Unit)? = null,
    variant: IenAgreementVariant = IenAgreementVariant.Large,
    itemVariant: IenAgreementVariant = IenAgreementVariant.Small,
) {
    val allChecked = items.isNotEmpty() && items.all { it.checked }
    val shape = RoundedCornerShape(IenTheme.radius.lg)
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
        shape = shape,
    ) {
        Column(
            modifier = Modifier.padding(IenTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)
        ) {
            IenAgreement(
                variant = IenAgreementVariant.XLarge,
                onClick = {
                    val nextChecked = !allChecked
                    if (onAllCheckedChange != null) {
                        onAllCheckedChange(nextChecked)
                    } else {
                        items.forEach { onItemCheckedChange(it.id, nextChecked) }
                    }
                },
                left = {
                    IenAgreementCheckbox(
                        checked = allChecked,
                        onCheckedChange = { checked ->
                            if (onAllCheckedChange != null) {
                                onAllCheckedChange(checked)
                            } else {
                                items.forEach { onItemCheckedChange(it.id, checked) }
                            }
                        }
                    )
                },
                middle = {
                    IenText(
                        text = title,
                        style = IenTheme.typography.body1,
                        color = IenTheme.colors.textPrimary,
                    )
                }
            )
            IenDivider()
            items.forEach { item ->
                IenAgreement(
                    variant = if (item.indent) IenAgreementVariant.Small else itemVariant,
                    indent = if (item.indent) 1 else 0,
                    onClick = {
                        if (item.enabled) onItemCheckedChange(item.id, !item.checked)
                    },
                    left = {
                        IenAgreementCheckbox(
                            checked = item.checked,
                            onCheckedChange = { if (item.enabled) onItemCheckedChange(item.id, it) },
                            enabled = item.enabled,
                            variant = if (item.indent) IenAgreementCheckboxVariant.Dot else IenAgreementCheckboxVariant.Checkbox
                        )
                    },
                    middle = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
                        ) {
                            IenAgreementText(
                                text = item.title,
                                enabled = item.enabled,
                                necessity = if (item.required) {
                                    { IenAgreementNecessity(IenAgreementNecessityVariant.Mandatory) }
                                } else null
                            )
                            if (item.description != null) {
                                IenText(
                                    text = item.description,
                                    style = IenTheme.typography.caption,
                                    color = if (item.enabled) IenTheme.colors.textTertiary else IenTheme.colors.textDisabled,
                                )
                            }
                        }
                    }
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
    val shouldCompose = visible || takeSpace

    if (shouldCompose) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .graphicsLayer {
                    this.alpha = alpha
                    scaleX = scale
                    scaleY = scale
                    translationY = translation
                },
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
