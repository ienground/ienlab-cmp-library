package zone.ien.utils.ui.components.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenTheme

@Composable
fun IenSurface(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.surface,
    contentColor: Color = IenTheme.colors.textPrimary,
    shape: Shape = RoundedCornerShape(IenTheme.radius.md),
    border: BorderStroke? = null,
    tonalElevation: Dp = IenTheme.elevation.none,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = color,
        contentColor = contentColor,
        shape = shape,
        border = border,
        tonalElevation = tonalElevation,
        content = content,
    )
}

@Composable
fun IenText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = IenTheme.typography.body1,
    color: Color = IenTheme.colors.textPrimary,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
    )
}

@Composable
fun IenProvideTextStyle(
    style: TextStyle,
    color: Color = LocalContentColor.current,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides color) {
        ProvideTextStyle(style, content)
    }
}

@Composable
fun IenIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    size: Dp = IenTheme.icon.md,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.size(size),
    )
}

@Composable
fun IenDivider(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.border,
    thickness: Dp = IenTheme.stroke.thin,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color),
    )
}

@Composable
fun IenBorderBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(IenTheme.radius.md),
    color: Color = IenTheme.colors.border,
    width: Dp = IenTheme.stroke.thin,
    padding: PaddingValues = PaddingValues(IenTheme.spacing.md),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .border(width, color, shape)
            .padding(padding),
    ) {
        content()
    }
}

@Composable
fun IenClickable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    role: Role? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = IenTheme.state.minimumTouchTarget, minHeight = IenTheme.state.minimumTouchTarget)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = role,
                onClick = onClick,
            ),
    ) {
        content()
    }
}

@Composable
fun IenLoaderPrimitive(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    strokeWidth: Dp = 2.dp,
) {
    CircularProgressIndicator(
        modifier = modifier.size(IenTheme.icon.md),
        color = color,
        strokeWidth = strokeWidth,
    )
}
