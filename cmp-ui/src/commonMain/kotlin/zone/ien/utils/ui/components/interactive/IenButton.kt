package zone.ien.utils.ui.components.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.components.primitives.IenProvideTextStyle
import zone.ien.utils.ui.components.primitives.IenText

enum class IenButtonSize { Small, Medium, Large }
enum class IenIconPlacement { Start, End }
enum class IenButtonDisplay { Inline, Block, Full }

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
    val ienColors = ienButtonColors(variant, tone, state.enabled)
    val height = size.buttonHeight()
    
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.975f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val buttonModifier = modifier
        .then(if (display == IenButtonDisplay.Block || display == IenButtonDisplay.Full) Modifier.fillMaxWidth() else Modifier)
        .heightIn(min = height)
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .semantics { role = Role.Button }

    val resolvedShape = if (display == IenButtonDisplay.Full) {
        RoundedCornerShape(0.dp)
    } else {
        shape
    }

    val resolvedColors = colors ?: ButtonDefaults.buttonColors(
        containerColor = ienColors.container,
        contentColor = ienColors.content,
        disabledContainerColor = ienColors.container.copy(alpha = IenTheme.state.disabledAlpha),
        disabledContentColor = ienColors.content.copy(alpha = IenTheme.state.disabledAlpha),
    )

    val content: @Composable () -> Unit = {
        IenButtonContent(
            text = text,
            loading = state.loading,
            size = size,
            icon = icon,
            iconPlacement = iconPlacement,
        )
    }

    val handleOnClick: () -> Unit = {
        if (state.enabled && !state.loading) {
            onClick()
        }
    }

    when (variant) {
        IenButtonVariant.Fill, IenButtonVariant.Weak -> Button(
            onClick = handleOnClick,
            modifier = buttonModifier,
            enabled = state.enabled,
            shape = resolvedShape,
            colors = resolvedColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { content() },
        )

        IenButtonVariant.Line -> OutlinedButton(
            onClick = handleOnClick,
            modifier = buttonModifier,
            enabled = state.enabled,
            shape = resolvedShape,
            border = BorderStroke(IenTheme.stroke.thin, ienColors.border),
            colors = resolvedColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { content() },
        )

        IenButtonVariant.Ghost -> TextButton(
            onClick = handleOnClick,
            modifier = buttonModifier,
            enabled = state.enabled,
            shape = resolvedShape,
            colors = resolvedColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { content() },
        )
    }
}

@Composable
fun IenIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Medium,
    variant: IenButtonVariant = IenButtonVariant.Ghost,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    state: IenButtonState = IenButtonState(),
    content: @Composable () -> Unit,
) {
    IenButton(
        text = "",
        onClick = onClick,
        modifier = modifier.defaultMinSize(minWidth = size.buttonHeight()),
        size = size,
        variant = variant,
        tone = tone,
        state = state,
        icon = content,
    )
}

@Composable
fun IenTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        size = IenButtonSize.Medium,
        variant = IenButtonVariant.Ghost,
        tone = tone,
        state = state,
    )
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
    val resolved = when (variant) {
        IenButtonVariant.Fill -> IenButtonResolvedColors(toneColor, Color.White, toneColor)
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
