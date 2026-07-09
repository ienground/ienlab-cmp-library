package zone.ien.utils.ui.components.composite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.window.Dialog
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.IenButton
import zone.ien.utils.ui.components.interactive.IenButtonSize
import zone.ien.utils.ui.components.interactive.IenButtonVariant
import zone.ien.utils.ui.components.interactive.IenTextButton
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
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    primaryActionText: String? = null,
    onPrimaryActionClick: (() -> Unit)? = null,
    secondaryActionText: String? = null,
    onSecondaryActionClick: (() -> Unit)? = null,
    closeActionText: String = "닫기",
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    if (!visible) return
    Dialog(onDismissRequest = onDismissRequest) {
        IenSurface(
            modifier = modifier.fillMaxWidth(),
            color = IenTheme.colors.surfaceRaised,
            shape = RoundedCornerShape(IenTheme.radius.xl),
            tonalElevation = IenTheme.elevation.overlay,
        ) {
            Column(
                modifier = Modifier.padding(IenTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            ) {
                IenText(title, style = IenTheme.typography.title2)
                if (description != null) {
                    IenText(description, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
                }
                content?.invoke(this)
                Spacer(Modifier.height(IenTheme.spacing.xs))
                Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                    if (secondaryActionText != null && onSecondaryActionClick != null) {
                        IenButton(
                            text = secondaryActionText,
                            onClick = onSecondaryActionClick,
                            modifier = Modifier.weight(1f),
                            size = IenButtonSize.Medium,
                            variant = IenButtonVariant.Weak,
                        )
                    }
                    if (primaryActionText != null && onPrimaryActionClick != null) {
                        IenButton(
                            text = primaryActionText,
                            onClick = onPrimaryActionClick,
                            modifier = Modifier.weight(1f),
                            size = IenButtonSize.Medium,
                        )
                    }
                }
                Box(Modifier.align(Alignment.End)) {
                    IenTextButton(text = closeActionText, onClick = onDismissRequest)
                }
            }
        }
    }
}
