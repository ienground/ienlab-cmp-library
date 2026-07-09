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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenIcon
import zone.ien.utils.ui.components.primitives.IenSurface

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
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    initialOpened: Boolean = false,
    state: IenBoardRowState = rememberIenBoardRowState(initialOpened),
    prefix: (@Composable () -> Unit)? = null,
    icon: (@Composable (opened: Boolean) -> Unit)? = { IenBoardRowIcon(opened = it) },
    children: @Composable ColumnScope.() -> Unit,
) {
    IenSurface(
        shape = RectangleShape,
        modifier = modifier
    ) {
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
                targetValue = if (pressed) 0.95f else 1f,
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
                    .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm)
                ,
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                prefix?.invoke()
                Row(modifier = Modifier.weight(1f)) { title() }
                icon?.invoke(state.opened)
            }
            AnimatedVisibility(
                visible = state.opened,
                enter = fadeIn(tween(IenTheme.motion.fastMillis)) + expandVertically(tween(IenTheme.motion.fastMillis)),
                exit = fadeOut(tween(IenTheme.motion.fastMillis)) + shrinkVertically(tween(IenTheme.motion.fastMillis)),
            ) {
                IenSurface(
                    modifier = Modifier
                        .background(IenTheme.colors.brandWeak)
                        .fillMaxWidth(),
                    color = Color.Transparent,
                ) {
                    Column(
                        modifier = Modifier.padding(IenTheme.spacing.md),
                        content = children,
                    )
                }
            }
        }
    }
}

@Composable
fun IenBoardRow(
    opened: Boolean,
    onOpenedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
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
private fun IenBoardRowIcon(
    opened: Boolean,
    modifier: Modifier = Modifier,
) {
    val rotation by animateFloatAsState(
        targetValue = if (opened) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "IenBoardRowIconRotation",
    )
    IenIcon(
        imageVector = M3SystemIcons.ArrowDropDown,
        contentDescription = null,
        modifier = modifier.rotate(rotation),
        tint = IenTheme.colors.textTertiary,
    )
}
