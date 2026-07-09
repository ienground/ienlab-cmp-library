package zone.ien.utils.ui.components.composite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

@Stable
interface IenBoardRowState {
    val opened: Boolean
    fun setOpened(opened: Boolean)
}

private class DefaultIenBoardRowState(
    initialOpened: Boolean,
) : IenBoardRowState {
    private var currentOpened by mutableStateOf(initialOpened)

    override val opened: Boolean
        get() = currentOpened

    override fun setOpened(opened: Boolean) {
        currentOpened = opened
    }
}

@Composable
fun rememberIenBoardRowState(
    initialOpened: Boolean = false,
): IenBoardRowState = remember { DefaultIenBoardRowState(initialOpened) }

@Composable
fun IenBoardRow(
    title: String,
    modifier: Modifier = Modifier,
    state: IenBoardRowState = rememberIenBoardRowState(),
    prefix: (@Composable () -> Unit)? = null,
    icon: (@Composable (opened: Boolean) -> Unit)? = { IenBoardRowIcon(opened = it) },
    children: @Composable ColumnScope.() -> Unit,
) {
    IenBoardRow(
        title = { IenText(title, style = IenTheme.typography.label1) },
        modifier = modifier,
        state = state,
        prefix = prefix,
        trailing = icon,
        content = children,
    )
}

@Composable
fun IenBoardRow(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    state: IenBoardRowState = rememberIenBoardRowState(),
    prefix: (@Composable () -> Unit)? = null,
    trailing: (@Composable (opened: Boolean) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    IenSurface(modifier = modifier) {
        Column(
            modifier = Modifier.semantics {
                stateDescription = if (state.opened) "펼쳐짐" else "접힘"
            },
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val pressed by interactionSource.collectIsPressedAsState()
            val pressedBackground by animateColorAsState(
                targetValue = if (pressed) IenTheme.colors.textPrimary.copy(alpha = 0.06f) else Color.Transparent,
                animationSpec = tween(IenTheme.motion.instantMillis),
                label = "IenBoardRowPressedBackground",
            )
            val pressedScale by animateFloatAsState(
                targetValue = if (pressed) 0.985f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow,
                ),
                label = "IenBoardRowPressedScale",
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(pressedScale)
                    .background(pressedBackground, RoundedCornerShape(IenTheme.radius.default))
                    .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        role = Role.Button,
                    ) { state.setOpened(!state.opened) }
                    .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                prefix?.invoke()
                Row(modifier = Modifier.weight(1f)) { title() }
                trailing?.invoke(state.opened)
            }
            AnimatedVisibility(
                visible = state.opened,
                enter = fadeIn(tween(IenTheme.motion.fastMillis)) + expandVertically(tween(IenTheme.motion.fastMillis)),
                exit = fadeOut(tween(IenTheme.motion.fastMillis)) + shrinkVertically(tween(IenTheme.motion.fastMillis)),
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = IenTheme.spacing.md,
                        end = IenTheme.spacing.md,
                        bottom = IenTheme.spacing.md,
                    ),
                    content = content,
                )
            }
        }
    }
}

@Composable
fun IenBoardRow(
    opened: Boolean,
    onOpenedChange: (Boolean) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    prefix: (@Composable () -> Unit)? = null,
    icon: (@Composable (opened: Boolean) -> Unit)? = { IenBoardRowIcon(opened = it) },
    children: @Composable ColumnScope.() -> Unit,
) {
    val state = remember(opened, onOpenedChange) {
        object : IenBoardRowState {
            override val opened: Boolean
                get() = opened

            override fun setOpened(opened: Boolean) {
                onOpenedChange(opened)
            }
        }
    }
    IenBoardRow(
        title = title,
        modifier = modifier,
        state = state,
        prefix = prefix,
        icon = icon,
        children = children,
    )
}

@Composable
fun IenBoardRow(
    opened: Boolean,
    onOpenedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    prefix: (@Composable () -> Unit)? = null,
    trailing: (@Composable (opened: Boolean) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = remember(opened, onOpenedChange) {
        object : IenBoardRowState {
            override val opened: Boolean
                get() = opened

            override fun setOpened(opened: Boolean) {
                onOpenedChange(opened)
            }
        }
    }
    IenBoardRow(
        title = title,
        modifier = modifier,
        state = state,
        prefix = prefix,
        trailing = trailing,
        content = content,
    )
}

@Composable
private fun IenBoardRowIcon(
    opened: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = IenTheme.colors.textTertiary
    Box(modifier = modifier.size(IenTheme.icon.md)) {
        Canvas(Modifier.size(IenTheme.icon.md)) {
            val strokeWidth = 2f
            val left = size.width * 0.28f
            val center = size.width * 0.5f
            val right = size.width * 0.72f
            val top = size.height * 0.4f
            val bottom = size.height * 0.62f
            if (opened) {
                drawLine(color, start = Offset(left, bottom), end = Offset(center, top), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                drawLine(color, start = Offset(center, top), end = Offset(right, bottom), strokeWidth = strokeWidth, cap = StrokeCap.Round)
            } else {
                drawLine(color, start = Offset(left, top), end = Offset(center, bottom), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                drawLine(color, start = Offset(center, bottom), end = Offset(right, top), strokeWidth = strokeWidth, cap = StrokeCap.Round)
            }
        }
    }
}
