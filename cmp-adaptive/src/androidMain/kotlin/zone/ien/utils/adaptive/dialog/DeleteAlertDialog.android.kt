package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.M3AlertDialog
import zone.ien.utils.ui.dialog.M3DeleteAlertDialog

@Composable
actual fun DeleteAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean
) {
    M3DeleteAlertDialog(
        modifier = modifier,
        visible = visible,
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        enabledConfirm = enabledConfirm
    )
}