package zone.ien.utils.ui.components.composite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.IenButton
import zone.ien.utils.ui.components.interactive.IenButtonSize
import zone.ien.utils.ui.components.interactive.IenButtonDisplay
import zone.ien.utils.ui.components.interactive.IenButtonVariant
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

@Composable
fun IenAlertDialog(
    visible: Boolean,
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "확인",
    onConfirmClick: () -> Unit = onDismissRequest,
    tone: IenSemanticTone = IenSemanticTone.Brand,
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
                IenText(message, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
                IenButton(
                    text = confirmText,
                    onClick = onConfirmClick,
                    display = IenButtonDisplay.Block,
                    size = IenButtonSize.Medium,
                    tone = tone,
                )
            }
        }
    }
}

@Composable
fun IenConfirmDialog(
    visible: Boolean,
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "확인",
    dismissText: String = "취소",
    destructive: Boolean = false,
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
                IenText(message, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) {
                    IenButton(
                        text = dismissText,
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f),
                        size = IenButtonSize.Medium,
                        variant = IenButtonVariant.Weak,
                        tone = IenSemanticTone.Neutral,
                    )
                    IenButton(
                        text = confirmText,
                        onClick = onConfirmClick,
                        modifier = Modifier.weight(1f),
                        size = IenButtonSize.Medium,
                        tone = if (destructive) IenSemanticTone.Danger else IenSemanticTone.Brand,
                    )
                }
            }
        }
    }
}
