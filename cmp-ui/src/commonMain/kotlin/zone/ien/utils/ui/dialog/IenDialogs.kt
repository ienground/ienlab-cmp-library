package zone.ien.utils.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.ok
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.interactive.IenTextButtonSize
import zone.ien.utils.ui.interactive.IenTextButtonVariant
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText

enum class IenDialogButtonLayout {
    Horizontal,
    Vertical,
}

@Composable
fun IenAlertDialog(
    visible: Boolean,
    title: String,
    message: String? = null,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(Res.string.close),
    onConfirmClick: () -> Unit = onDismissRequest,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    closeOnDimmerClick: Boolean = true,
    closeOnBackEvent: Boolean = true,
    onEntered: (() -> Unit)? = null,
    onExited: (() -> Unit)? = null,
) {
    IenAlertDialog(
        visible = visible,
        onClose = onDismissRequest,
        modifier = modifier,
        closeOnDimmerClick = closeOnDimmerClick,
        closeOnBackEvent = closeOnBackEvent,
        onEntered = onEntered,
        onExited = onExited,
        title = {
            IenAlertDialogTitle(text = title)
        },
        description = message?.let {
            {
                IenAlertDialogDescription(text = it)
            }
        },
        alertButton = {
            IenAlertDialogAlertButton(
                text = confirmText,
                onClick = onConfirmClick,
                tone = tone,
            )
        },
    )
}

@Composable
fun IenAlertDialog(
    visible: Boolean,
    onClose: () -> Unit,
    title: @Composable () -> Unit,
    alertButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: (@Composable () -> Unit)? = null,
    closeOnDimmerClick: Boolean = true,
    closeOnBackEvent: Boolean = true,
    onEntered: (() -> Unit)? = null,
    onExited: (() -> Unit)? = null,
) {
    IenDialogFrame(
        visible = visible,
        onClose = onClose,
        modifier = modifier,
        closeOnDimmerClick = closeOnDimmerClick,
        closeOnBackEvent = closeOnBackEvent,
        onEntered = onEntered,
        onExited = onExited,
    ) {
        title()
        description?.invoke()
        alertButton()
    }
}

@Composable
fun IenConfirmDialog(
    visible: Boolean,
    title: String,
    message: String? = null,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(Res.string.ok),
    dismissText: String = stringResource(Res.string.cancel),
    destructive: Boolean = false,
    closeOnDimmerClick: Boolean = true,
    closeOnBackEvent: Boolean = true,
    onEntered: (() -> Unit)? = null,
    onExited: (() -> Unit)? = null,
    buttonLayout: IenDialogButtonLayout = IenDialogButtonLayout.Horizontal,
) {
    IenConfirmDialog(
        visible = visible,
        onClose = onDismissRequest,
        modifier = modifier,
        closeOnDimmerClick = closeOnDimmerClick,
        closeOnBackEvent = closeOnBackEvent,
        onEntered = onEntered,
        onExited = onExited,
        title = {
            IenConfirmDialogTitle(text = title)
        },
        description = message?.let {
            {
                IenConfirmDialogDescription(text = it)
            }
        },
        cancelButton = {
            IenConfirmDialogCancelButton(
                text = dismissText,
                onClick = onDismissRequest,
            )
        },
        confirmButton = {
            IenConfirmDialogConfirmButton(
                text = confirmText,
                onClick = onConfirmClick,
                tone = if (destructive) IenSemanticTone.Danger else IenSemanticTone.Brand,
            )
        },
        buttonLayout = buttonLayout,
    )
}

@Composable
fun IenConfirmDialog(
    visible: Boolean,
    onClose: () -> Unit,
    title: @Composable () -> Unit,
    cancelButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: (@Composable () -> Unit)? = null,
    closeOnDimmerClick: Boolean = true,
    closeOnBackEvent: Boolean = true,
    onEntered: (() -> Unit)? = null,
    onExited: (() -> Unit)? = null,
    buttonLayout: IenDialogButtonLayout = IenDialogButtonLayout.Horizontal,
) {
    IenDialogFrame(
        visible = visible,
        onClose = onClose,
        modifier = modifier,
        closeOnDimmerClick = closeOnDimmerClick,
        closeOnBackEvent = closeOnBackEvent,
        onEntered = onEntered,
        onExited = onExited,
    ) {
        title()
        description?.invoke()
        when (buttonLayout) {
            IenDialogButtonLayout.Horizontal -> Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    cancelButton()
                }
                Box(modifier = Modifier.weight(1f)) {
                    confirmButton()
                }
            }
            IenDialogButtonLayout.Vertical -> Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            ) {
                confirmButton()
                cancelButton()
            }
        }
    }
}

@Composable
fun IenAlertDialogTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.textPrimary,
    style: TextStyle = IenTheme.typography.title2,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    IenText(
        text = text,
        modifier = modifier,
        style = style.copy(fontWeight = fontWeight),
        color = color,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun IenConfirmDialogTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.textPrimary,
    style: TextStyle = IenTheme.typography.title2,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    IenAlertDialogTitle(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        fontWeight = fontWeight,
    )
}

@Composable
fun IenAlertDialogDescription(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.textSecondary,
    style: TextStyle = IenTheme.typography.body2,
    fontWeight: FontWeight = FontWeight.Medium,
) {
    IenText(
        text = text,
        modifier = modifier,
        style = style.copy(fontWeight = fontWeight),
        color = color,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun IenConfirmDialogDescription(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.textSecondary,
    style: TextStyle = IenTheme.typography.body2,
    fontWeight: FontWeight = FontWeight.Medium,
) {
    IenAlertDialogDescription(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        fontWeight = fontWeight,
    )
}

@Composable
fun IenAlertDialogAlertButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    size: IenTextButtonSize = IenTextButtonSize.Medium,
    variant: IenTextButtonVariant = IenTextButtonVariant.Clear,
) {
    IenTextButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        size = size,
        tone = tone,
        variant = variant,
    )
}

@Composable
fun IenConfirmDialogCancelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    variant: IenButtonVariant = IenButtonVariant.Weak,
    size: IenButtonSize = IenButtonSize.Large,
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        size = size,
        variant = variant,
        tone = tone,
        display = IenButtonDisplay.Block,
    )
}

@Composable
fun IenConfirmDialogConfirmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    size: IenButtonSize = IenButtonSize.Large,
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        size = size,
        variant = variant,
        tone = tone,
        display = IenButtonDisplay.Block,
    )
}

@Composable
private fun IenDialogFrame(
    visible: Boolean,
    onClose: () -> Unit,
    modifier: Modifier,
    closeOnDimmerClick: Boolean,
    closeOnBackEvent: Boolean,
    onEntered: (() -> Unit)?,
    onExited: (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(visible) {
        if (visible) {
            onEntered?.invoke()
        } else {
            onExited?.invoke()
        }
    }
    if (!visible) return
    Dialog(
        onDismissRequest = {
            if (closeOnDimmerClick || closeOnBackEvent) {
                onClose()
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = closeOnBackEvent,
            dismissOnClickOutside = closeOnDimmerClick,
        ),
    ) {
        IenSurface(
            modifier = modifier
                .fillMaxWidth()
                .widthIn(max = 320.dp),
            color = IenTheme.colors.surfaceRaised,
            shape = RoundedCornerShape(IenTheme.radius.xl),
            tonalElevation = IenTheme.elevation.overlay,
        ) {
            Column(
                modifier = Modifier
                    .padding(IenTheme.spacing.lg)
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                content = content,
            )
        }
    }
}
