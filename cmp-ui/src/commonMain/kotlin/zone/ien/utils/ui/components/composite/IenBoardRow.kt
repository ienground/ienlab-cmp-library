package zone.ien.utils.ui.components.composite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import zone.ien.utils.ui.components.foundation.IenTheme
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
            Row(
                modifier = Modifier
                    .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        role = Role.Button,
                    ) { state.setOpened(!state.opened) }
                    .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
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
