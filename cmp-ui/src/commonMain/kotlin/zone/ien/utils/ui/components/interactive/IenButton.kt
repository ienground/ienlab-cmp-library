package zone.ien.utils.ui.components.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import zone.ien.utils.ui.utils.instantPress
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.line.ArrowRightS
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.components.primitives.IenProvideTextStyle
import zone.ien.utils.ui.components.primitives.IenText

enum class IenButtonSize { Small, Medium, Large }
enum class IenIconPlacement { Start, End }
enum class IenButtonDisplay { Inline, Block, Full }
enum class IenTextButtonSize { XSmall, Small, Medium, Large, XLarge, XXLarge }
enum class IenTextButtonVariant { Clear, Arrow, Underline }

sealed interface IenButtonVariant {
    data object Fill : IenButtonVariant
    data object Weak : IenButtonVariant
    data object Line : IenButtonVariant
    data object Ghost : IenButtonVariant
}

@Immutable
data class IenButtonState(
    val enabled: Boolean = true,
    val loading: Boolean = false,
)

@Composable
fun IenButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    icon: (@Composable () -> Unit)? = null,
    iconPlacement: IenIconPlacement = IenIconPlacement.Start,
    shape: Shape = RoundedCornerShape(IenTheme.radius.default),
    contentPadding: PaddingValues = size.buttonPadding(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    display: IenButtonDisplay = IenButtonDisplay.Inline,
    colors: ButtonColors? = null,
) {
    val height = size.buttonHeight()
    val buttonModifier = modifier
        .then(if (display == IenButtonDisplay.Block || display == IenButtonDisplay.Full) Modifier.fillMaxWidth() else Modifier)
        .heightIn(min = height)

    val resolvedShape = if (display == IenButtonDisplay.Full) {
        RoundedCornerShape(0.dp)
    } else {
        shape
    }

    IenButtonContainer(
        onClick = onClick,
        modifier = buttonModifier,
        variant = variant,
        tone = tone,
        state = state,
        shape = resolvedShape,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        scalePressed = 0.975f,
        colors = colors,
    ) {
        IenButtonContent(
            text = text,
            loading = state.loading,
            size = size,
            icon = icon,
            iconPlacement = iconPlacement,
        )
    }
}

@Composable
fun IenIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = RoundedCornerShape(IenTheme.radius.default),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: ButtonColors? = null,
    content: @Composable () -> Unit,
) {
    val buttonSize = when (size) {
        IenButtonSize.Small -> 36.dp
        IenButtonSize.Medium -> 44.dp
        IenButtonSize.Large -> 52.dp
    }

    val buttonModifier = modifier.size(buttonSize)

    val innerContent: @Composable () -> Unit = {
        val iconSize = when (size) {
            IenButtonSize.Small -> 18.dp
            IenButtonSize.Medium -> 24.dp
            IenButtonSize.Large -> 28.dp
        }
        IenProvideTextStyle(IenTheme.typography.body1, LocalContentColor.current) {
            Box(
                modifier = Modifier.size(iconSize),
                contentAlignment = Alignment.Center
            ) {
                if (state.loading) {
                    IenLoaderPrimitive(color = LocalContentColor.current)
                } else {
                    content()
                }
            }
        }
    }

    IenButtonContainer(
        onClick = onClick,
        modifier = buttonModifier,
        variant = variant,
        tone = tone,
        state = state,
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        scalePressed = 0.95f,
        colors = colors,
        content = innerContent,
    )
}

@Composable
fun IenTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenTextButtonSize = IenTextButtonSize.Medium,
    variant: IenTextButtonVariant = IenTextButtonVariant.Clear,
    disabled: Boolean = false,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(enabled = !disabled),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val enabled = state.enabled && !disabled
    val contentColor = if (enabled) {
        toneColor(tone)
    } else {
        toneColor(tone).copy(alpha = IenTheme.state.disabledAlpha)
    }
    val textStyle = size.textStyle().let {
        if (variant == IenTextButtonVariant.Underline) {
            it.copy(textDecoration = TextDecoration.Underline)
        } else {
            it
        }
    }

    IenButtonContainer(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = size.minHeight()),
        variant = IenButtonVariant.Ghost,
        tone = tone,
        state = state.copy(enabled = enabled),
        shape = RoundedCornerShape(IenTheme.radius.sm),
        contentPadding = size.contentPadding(),
        interactionSource = interactionSource,
        scalePressed = 0.97f,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            disabledContentColor = contentColor,
        ),
    ) {
        IenProvideTextStyle(textStyle, contentColor) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(size.iconGap()),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IenText(
                    text = text,
                    color = LocalContentColor.current,
                    style = textStyle,
                )
                if (variant == IenTextButtonVariant.Arrow) {
                    Icon(
                        imageVector = RemixIcons.Line.ArrowRightS,
                        contentDescription = null,
                        tint = LocalContentColor.current,
                        modifier = Modifier.size(size.chevronSize())
                    )
                }
            }
        }
    }
}

@Composable
private fun IenButtonContent(
    text: String,
    loading: Boolean,
    size: IenButtonSize,
    icon: (@Composable () -> Unit)?,
    iconPlacement: IenIconPlacement,
) {
    val textStyle = when (size) {
        IenButtonSize.Small -> IenTheme.typography.label2
        IenButtonSize.Medium -> IenTheme.typography.label1
        IenButtonSize.Large -> IenTheme.typography.body1
    }
    IenProvideTextStyle(textStyle, LocalContentColor.current) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (loading) {
                IenLoaderPrimitive(color = LocalContentColor.current)
            } else {
                if (iconPlacement == IenIconPlacement.Start) icon?.invoke()
                if (text.isNotEmpty()) IenText(text = text, color = LocalContentColor.current, style = textStyle)
                if (iconPlacement == IenIconPlacement.End) icon?.invoke()
            }
        }
    }
}

@Immutable
private data class IenButtonResolvedColors(
    val container: Color,
    val content: Color,
    val border: Color,
)

@Composable
private fun ienButtonColors(
    variant: IenButtonVariant,
    tone: IenSemanticTone,
    enabled: Boolean,
): IenButtonResolvedColors {
    val toneColor = toneColor(tone)
    val weakColor = toneWeakColor(tone)
    val onToneColor = toneOnColor(tone)
    val resolved = when (variant) {
        IenButtonVariant.Fill -> IenButtonResolvedColors(toneColor, onToneColor, toneColor)
        IenButtonVariant.Weak -> IenButtonResolvedColors(weakColor, toneColor, weakColor)
        IenButtonVariant.Line -> IenButtonResolvedColors(Color.Transparent, toneColor, IenTheme.colors.borderStrong)
        IenButtonVariant.Ghost -> IenButtonResolvedColors(Color.Transparent, toneColor, Color.Transparent)
    }
    return if (enabled) resolved else resolved.copy(
        container = resolved.container.copy(alpha = IenTheme.state.disabledAlpha),
        content = resolved.content.copy(alpha = IenTheme.state.disabledAlpha),
    )
}

@Composable
internal fun toneColor(tone: IenSemanticTone): Color = when (tone) {
    IenSemanticTone.Neutral -> IenTheme.colors.textPrimary
    IenSemanticTone.Brand -> IenTheme.colors.brand
    IenSemanticTone.Success -> IenTheme.colors.success
    IenSemanticTone.Warning -> IenTheme.colors.warning
    IenSemanticTone.Danger -> IenTheme.colors.danger
    IenSemanticTone.Info -> IenTheme.colors.info
}

@Composable
internal fun toneWeakColor(tone: IenSemanticTone): Color = when (tone) {
    IenSemanticTone.Neutral -> IenTheme.colors.surfaceWeak
    IenSemanticTone.Brand -> IenTheme.colors.brandWeak
    IenSemanticTone.Success -> IenTheme.colors.successWeak
    IenSemanticTone.Warning -> IenTheme.colors.warningWeak
    IenSemanticTone.Danger -> IenTheme.colors.dangerWeak
    IenSemanticTone.Info -> IenTheme.colors.infoWeak
}

@Composable
internal fun toneOnColor(tone: IenSemanticTone): Color = when (tone) {
    IenSemanticTone.Neutral -> IenTheme.colors.surfaceRaised
    IenSemanticTone.Brand -> IenTheme.colors.onBrand
    IenSemanticTone.Success -> IenTheme.colors.onSuccess
    IenSemanticTone.Warning -> IenTheme.colors.onWarning
    IenSemanticTone.Danger -> IenTheme.colors.onDanger
    IenSemanticTone.Info -> IenTheme.colors.onInfo
}

private fun IenButtonSize.buttonHeight(): Dp = when (this) {
    IenButtonSize.Small -> 36.dp
    IenButtonSize.Medium -> 44.dp
    IenButtonSize.Large -> 52.dp
}

private fun IenButtonSize.buttonPadding(): PaddingValues = when (this) {
    IenButtonSize.Small -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    IenButtonSize.Medium -> PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    IenButtonSize.Large -> PaddingValues(horizontal = 20.dp, vertical = 14.dp)
}

@Composable
private fun IenTextButtonSize.textStyle(): TextStyle = when (this) {
    IenTextButtonSize.XSmall -> IenTheme.typography.caption
    IenTextButtonSize.Small -> IenTheme.typography.label2
    IenTextButtonSize.Medium -> IenTheme.typography.label1
    IenTextButtonSize.Large -> IenTheme.typography.body2
    IenTextButtonSize.XLarge -> IenTheme.typography.body1
    IenTextButtonSize.XXLarge -> IenTheme.typography.title3
}

private fun IenTextButtonSize.minHeight(): Dp = when (this) {
    IenTextButtonSize.XSmall -> 28.dp
    IenTextButtonSize.Small -> 32.dp
    IenTextButtonSize.Medium -> 36.dp
    IenTextButtonSize.Large -> 40.dp
    IenTextButtonSize.XLarge -> 44.dp
    IenTextButtonSize.XXLarge -> 48.dp
}

private fun IenTextButtonSize.contentPadding(): PaddingValues = when (this) {
    IenTextButtonSize.XSmall -> PaddingValues(horizontal = 4.dp, vertical = 4.dp)
    IenTextButtonSize.Small -> PaddingValues(horizontal = 5.dp, vertical = 5.dp)
    IenTextButtonSize.Medium -> PaddingValues(horizontal = 6.dp, vertical = 6.dp)
    IenTextButtonSize.Large -> PaddingValues(horizontal = 7.dp, vertical = 7.dp)
    IenTextButtonSize.XLarge -> PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    IenTextButtonSize.XXLarge -> PaddingValues(horizontal = 8.dp, vertical = 8.dp)
}

private fun IenTextButtonSize.iconGap(): Dp = when (this) {
    IenTextButtonSize.XSmall,
    IenTextButtonSize.Small,
    IenTextButtonSize.Medium -> 2.dp
    IenTextButtonSize.Large,
    IenTextButtonSize.XLarge,
    IenTextButtonSize.XXLarge -> 3.dp
}

private fun IenTextButtonSize.chevronSize(): Dp = when (this) {
    IenTextButtonSize.XSmall -> 10.dp
    IenTextButtonSize.Small -> 12.dp
    IenTextButtonSize.Medium -> 14.dp
    IenTextButtonSize.Large -> 16.dp
    IenTextButtonSize.XLarge -> 18.dp
    IenTextButtonSize.XXLarge -> 20.dp
}

@Composable
internal fun IenButtonContainer(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = RoundedCornerShape(IenTheme.radius.default),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    scalePressed: Float = 0.975f,
    colors: ButtonColors? = null,
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    val ienColors = ienButtonColors(variant, tone, state.enabled)
    val interactiveEnabled = state.enabled && !state.loading

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && interactiveEnabled) scalePressed else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val buttonModifier = modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .semantics { role = Role.Button }
        .instantPress(interactiveEnabled) { isPressed = it }

    val resolvedColors = colors ?: ButtonDefaults.buttonColors(
        containerColor = ienColors.container,
        contentColor = ienColors.content,
        disabledContainerColor = if (state.loading && state.enabled) {
            ienColors.container
        } else {
            ienColors.container.copy(alpha = IenTheme.state.disabledAlpha)
        },
        disabledContentColor = if (state.loading && state.enabled) {
            ienColors.content
        } else {
            ienColors.content.copy(alpha = IenTheme.state.disabledAlpha)
        },
    )

    val handleOnClick: () -> Unit = {
        if (state.enabled && !state.loading) {
            onClick()
        }
    }

    when (variant) {
        IenButtonVariant.Fill, IenButtonVariant.Weak -> Button(
            onClick = handleOnClick,
            modifier = buttonModifier,
            enabled = interactiveEnabled,
            shape = shape,
            colors = resolvedColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { content() },
        )

        IenButtonVariant.Line -> OutlinedButton(
            onClick = handleOnClick,
            modifier = buttonModifier,
            enabled = interactiveEnabled,
            shape = shape,
            border = border ?: BorderStroke(IenTheme.stroke.thin, ienColors.border),
            colors = resolvedColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { content() },
        )

        IenButtonVariant.Ghost -> TextButton(
            onClick = handleOnClick,
            modifier = buttonModifier,
            enabled = interactiveEnabled,
            shape = shape,
            colors = resolvedColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { content() },
        )
    }
}
