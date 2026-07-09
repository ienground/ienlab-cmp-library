package zone.ien.utils.ui.components.composite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.toneColor
import zone.ien.utils.ui.components.primitives.IenDivider
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

@Immutable
data class IenMenuItem(
    val title: String,
    val onClick: () -> Unit,
    val description: String? = null,
    val enabled: Boolean = true,
    val tone: IenSemanticTone = IenSemanticTone.Neutral,
    val leading: (@Composable () -> Unit)? = null,
    val trailing: (@Composable () -> Unit)? = null,
)

@Composable
fun IenMenu(
    items: List<IenMenuItem>,
    modifier: Modifier = Modifier,
    header: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
) {
    IenSurface(
        modifier = modifier.widthIn(min = IenTheme.state.minimumTouchTarget * 4),
        color = IenTheme.colors.surfaceRaised,
        shape = RoundedCornerShape(IenTheme.radius.lg),
        tonalElevation = IenTheme.elevation.floating,
    ) {
        Column(Modifier.padding(vertical = IenTheme.spacing.xs)) {
            if (header != null) {
                Column(Modifier.padding(IenTheme.spacing.md), content = header)
                IenDivider()
            }
            items.forEach { item ->
                IenMenuItemRow(item)
            }
            if (footer != null) {
                IenDivider()
                Column(Modifier.padding(IenTheme.spacing.md), content = footer)
            }
        }
    }
}

@Composable
private fun IenMenuItemRow(item: IenMenuItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.enabled, role = Role.Button, onClick = item.onClick)
            .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item.leading?.invoke()
        Column(Modifier.weight(1f)) {
            IenText(
                text = item.title,
                style = IenTheme.typography.body2,
                color = if (item.enabled) toneColor(item.tone) else IenTheme.colors.textDisabled,
            )
            if (item.description != null) {
                IenText(
                    text = item.description,
                    style = IenTheme.typography.caption,
                    color = IenTheme.colors.textTertiary,
                )
            }
        }
        item.trailing?.invoke()
    }
}

@Composable
fun IenModal(
    open: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onExited: (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    content: @Composable BoxScope.() -> Unit,
) {
    if (!open) {
        onExited?.invoke()
        return
    }
    Dialog(
        onDismissRequest = { onOpenChange(false) },
        properties = properties,
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            content = content,
        )
    }
}

object IenModal {
    @Composable
    fun Overlay(
        modifier: Modifier = Modifier,
        color: Color = Color.Black.copy(alpha = 0.42f),
        onClick: (() -> Unit)? = null,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable(
                    enabled = onClick != null,
                    role = Role.Button,
                    onClick = { onClick?.invoke() },
                ),
        ) {
            IenSurface(
                modifier = Modifier.fillMaxSize(),
                color = color,
                contentColor = color,
            ) {}
        }
    }

    @Composable
    fun Content(
        modifier: Modifier = Modifier,
        shape: RoundedCornerShape = RoundedCornerShape(IenTheme.radius.xl),
        content: @Composable ColumnScope.() -> Unit,
    ) {
        IenSurface(
            modifier = modifier.fillMaxWidth(),
            color = IenTheme.colors.surfaceRaised,
            shape = shape,
            tonalElevation = IenTheme.elevation.overlay,
        ) {
            Column(
                modifier = Modifier.padding(IenTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                content = content,
            )
        }
    }
}
