package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.M3AlertDialog

@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textDismiss: String,
    styleDismiss: UIAlertActionStyle,
    onDismiss: () -> Unit
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        textDismiss = textDismiss,
        onDismiss = onDismiss
    )
}

@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textDismiss: String,
    styleDismiss: UIAlertActionStyle,
    onDismiss: () -> Unit,
    textConfirm: String,
    styleConfirm: UIAlertActionStyle,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        textDismiss = textDismiss,
        onDismiss = onDismiss,
        textConfirm = textConfirm,
        onConfirm = onConfirm,
        enabledConfirm = enabledConfirm
    )
}

@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textNeutral: String,
    styleNeutral: UIAlertActionStyle,
    onNeutral: () -> Unit,
    enabledNeutral: Boolean,
    textNegative: String,
    styleNegative: UIAlertActionStyle,
    onNegative: () -> Unit,
    textPositive: String,
    stylePositive: UIAlertActionStyle,
    onPositive: () -> Unit,
    enabledPositive: Boolean
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        textNeutral = textNeutral,
        onNeutral = onNeutral,
        enabledNeutral = enabledNeutral,
        textNegative = textNegative,
        onNegative = onNegative,
        textPositive = textPositive,
        onPositive = onPositive,
        enabledPositive = enabledPositive
    )
}